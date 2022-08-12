package com.mimorphism.mangotracko.mango;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.mimorphism.mangotracko.exception.AnilistMangoNotFoundException;
import com.mimorphism.mangotracko.exception.MangoInfoServiceProviderUnreachableException;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.AnilistGraphqlRequestBody;
import com.mimorphism.mangotracko.mango.mangoinfo.anilist.MangoInfo;
import com.mimorphism.mangotracko.util.GraphqlSchemaReaderUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * @author mimo
 * @version 1.0
 * @since Jun 9, 2022
 * 
 * 	if (mangoInfo.isPresent()) {// TODO fails perhaps because of 1. mango doesnt exists in anilist's db 
	2.infoservice provider is down, or ceases toexist(unlikely). submit as a task to executorservice
 */
@Service
public class AnilistService implements MangoInfoService {

	private static final Logger log = LoggerFactory.getLogger(AnilistService.class);

	private WebClient webClient;

	private String API_BASE_URL;
	
	private static final String MANGO_NOT_FOUND_IN_INFO_PROVIDER = "MangoService.ANILIST_MANGO_NOT_FOUND";
	
	private static final String MANGO_INFO_SERVICE_DOWN = "MangoInfoService.ANILIST_DOWN";

	public AnilistService(@Value("${anilist.api.url}") String url) {

		API_BASE_URL = url;
		webClient = WebClient.builder().baseUrl(API_BASE_URL).build();
	}

	private AnilistGraphqlRequestBody buildQuery(String mangoTitle) {
		AnilistGraphqlRequestBody body = new AnilistGraphqlRequestBody();
		String query, variables;
		try {

			query = GraphqlSchemaReaderUtil.getSchemaFromFileName("getMangoInfoByTitle");
			variables = GraphqlSchemaReaderUtil.getSchemaFromFileName("variablesForMangoTitle").replace("mangoTitle", mangoTitle);
			body.setQuery(query);
			body.setVariables(variables);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return body;
	}
	
	private AnilistGraphqlRequestBody buildQuery(Long anilistId) {
		AnilistGraphqlRequestBody body = new AnilistGraphqlRequestBody();
		String query, variables;
		try {

			query = GraphqlSchemaReaderUtil.getSchemaFromFileName("getMangoInfoByAnilistId");
			variables = GraphqlSchemaReaderUtil.getSchemaFromFileName("variablesForAnilistId").replace("anilistId", String.valueOf(anilistId));
			body.setQuery(query);
			body.setVariables(variables);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return body;
	}


	public MangoInfo getMangoInfoByTitle(String mangoTitle) throws AnilistMangoNotFoundException {
		try {
			return callAnilistService(mangoTitle);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	private MangoInfo callAnilistService(String mangoTitle) throws UnknownHostException {
		MangoInfo apiResultMono = ((ResponseSpec) webClient.post()
    	        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    	        .bodyValue(buildQuery(mangoTitle))
    	        .retrieve()
    	        .onStatus(httpStatus -> httpStatus.value() == NOT_FOUND.value(),
                	error -> Mono.error(new AnilistMangoNotFoundException(MANGO_NOT_FOUND_IN_INFO_PROVIDER))))
    	        .bodyToMono(MangoInfo.class)
    	        .onErrorResume(UnknownHostException.class, error -> Mono.error(new MangoInfoServiceProviderUnreachableException(MANGO_INFO_SERVICE_DOWN)))
    	        .block();
//    	            log.error(String.format("POST request failed.Most probably mango title: %s but anything could go wrong", mangoTitle );
//    	        	log.error(throwable.getMessage());
//    	        )
//    	        .block();
		return apiResultMono;
	}
	
	private MangoInfo callAnilistService(Long anilistId) throws UnknownHostException {
		MangoInfo apiResultMono = ((ResponseSpec) webClient.post()
    	        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    	        .bodyValue(buildQuery(anilistId))
    	        .retrieve()
    	        .onStatus(httpStatus -> httpStatus.value() == NOT_FOUND.value(),
                	error -> Mono.error(new AnilistMangoNotFoundException(MANGO_NOT_FOUND_IN_INFO_PROVIDER))))
    	        .bodyToMono(MangoInfo.class)
    	        .onErrorResume(UnknownHostException.class, error -> Mono.error(new MangoInfoServiceProviderUnreachableException(MANGO_INFO_SERVICE_DOWN)))
    	        .block();
//    	            log.error(String.format("POST request failed.Most probably mango title: %s but anything could go wrong", mangoTitle );
//    	        	log.error(throwable.getMessage());
//    	        )
//    	        .block();
		return apiResultMono;
	}

	@Override
	public MangoInfo getMangoInfoByAnilistId(Long anilistId) throws AnilistMangoNotFoundException {
		try {
			return callAnilistService(anilistId);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}