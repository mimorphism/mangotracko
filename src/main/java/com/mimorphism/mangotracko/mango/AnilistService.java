package com.mimorphism.mangotracko.mango;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mimorphism.mangotracko.mango.mangoinfo.model.anilist.MangoInfo;
import com.mimorphism.mangotracko.util.GraphqlSchemaReaderUtil;


@Service
public class AnilistService implements MangoInfoService {
	
	private static final Logger log = LoggerFactory.getLogger(AnilistService.class);

    private WebClient webClient;
    
	private String API_BASE_URL;
	            
    public  AnilistService(@Value( "${anilist.api.url}") String url )
    {

    	API_BASE_URL = url;
    	webClient = WebClient.builder()
                    .baseUrl(API_BASE_URL)
                    .build();
        }

    
    private AnilistGraphqlRequestBody buildQuery(String mangoTitle) 
    {
    	AnilistGraphqlRequestBody body = new AnilistGraphqlRequestBody();
    	String query,variables;
    	try {

			 query = GraphqlSchemaReaderUtil.getSchemaFromFileName("getMangoInfo");
			 variables = GraphqlSchemaReaderUtil.getSchemaFromFileName("variables").replace("mangoTitle",   mangoTitle);
			 body.setQuery(query);
			 body.setVariables(variables);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return body;
    }
    
  
    
    public MangoInfo getMangoInfo(String mangoTitle) 
    {
    	return callAnilistService(mangoTitle);
    }


	private MangoInfo callAnilistService(String mangoTitle) {
		MangoInfo apiResult = webClient.post()
    	        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    	        .bodyValue(buildQuery(mangoTitle))
    	        .retrieve()
    	        .bodyToMono(MangoInfo.class)
    	        .onErrorResume((throwable) -> {
    	            log.error("POST request failed.Most probably mango title: " + mangoTitle);
    	            return null;
    	        })
    	        .block();
		
		return apiResult;
	}
}