package com.mimorphism.mangotracko.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mimorphism.mangotracko.model.MangoInfo;

import reactor.core.publisher.Mono;

@Service
public class MangoInfoService {
	
	private static final Logger log = LoggerFactory.getLogger(MangoInfoService.class);

    private WebClient webClient;
    
	private String API_BASE_URL;
	
    private  String query;
            
    public  MangoInfoService() {
    	API_BASE_URL = "https://graphql.anilist.co";
    	
    	query = "{Media (search: $title, type: MANGA){"
        		+ "    coverImage{"
        		+ " 	large"
        		+ "    }"
        		+ "     staff{\r\n"
        		+ "        nodes {\r\n"
        		+ "          name {\r\n"
        		+ "            full\r\n"
        		+ "          }\r\n"
        		+ "        }\r\n"
        		+ "    \r\n"
        		+ "    }\r\n"
        		+ "   \r\n"
        		+ "  }}"; 
    	

//    	variables = "{title: {mangoTitle}}";
    	
    	webClient = WebClient.builder()
                    .baseUrl(API_BASE_URL)
                    .build();
        }

    
    private String queryBuilder(String mangoTitle) 
    {
    	String json = "";
    	
    	try {
    	    ObjectMapper mapper = new ObjectMapper();
    	    ObjectNode mango = mapper.createObjectNode();
    	    mango.put("query", query.replace("$title", "\"" + mangoTitle + "\""));
    	    json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mango);
    	    return json;
    	} catch (Exception ex) {
    	    ex.printStackTrace();
    	}
		return json;
    	
    }
    
  
    
    public MangoInfo getMangoInfo(String mangoTitle) 
    {
    	MangoInfo mangoInfo = null;
    	String apiResult = callAnilistService(mangoTitle);
    	        
    	if(apiResult != null && !apiResult.isEmpty()) 
    	{
    		ObjectMapper objectMapper = new ObjectMapper();
        	JsonNode resultNode;
        	mangoInfo = new MangoInfo();
        	try {
        		resultNode = objectMapper.readTree(apiResult);
    			 mangoInfo.setCoverImage(resultNode.at("/data/Media/coverImage/large").asText());
    			 mangoInfo.setStaff(resultNode.at("/data/Media/staff/nodes").get(0).at("/name/full").asText());
    		} catch (JsonMappingException e) {
    			e.printStackTrace();
    		} catch (JsonProcessingException e) {
    			e.printStackTrace();
    		}

    	}
    	return mangoInfo;

    	
    }


	private String callAnilistService(String mangoTitle) {
		String apiResult = webClient.post()
    	        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    	        .bodyValue(queryBuilder(mangoTitle))
    	        .retrieve()
    	        .bodyToMono(String.class)
    	        .onErrorResume((throwable) -> {
    	            log.error("POST request failed.Most probably mango title: " + mangoTitle);
    	            return null;
    	        })
    	        .block();
		
		return apiResult;
	}
 
}