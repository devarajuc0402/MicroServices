package com.org.help.restclient.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.org.help.restclient.entity.HelpEntity;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class RestClientService {

	private final RestClient restClient;
	
	public RestClientService(RestClient restClient) {
		this.restClient = restClient;
	}
	
	@Cacheable(
			cacheNames = "restClientService",
			key = "'all'"
	)
	@CircuitBreaker(
			name = "restclientCircuitBreaker",
			fallbackMethod = "restclientListFallback"
	)
	public List<Object> getAllHelpDataService() {
		
		return restClient.get()
				.uri("/list")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.body(new ParameterizedTypeReference<List<Object>>() {});
	}
	
	@Cacheable(
			cacheNames = "restClientService",
			key = "#id"
	)
	@CircuitBreaker(
			name = "restclientCircuitBreaker",
			fallbackMethod = "restclientFallback"
	)
	public Object getHelpDataByIdService(int id) {
		
		return restClient.get()
				.uri("/id/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.body(Object.class);
	}
	
	@CachePut(
			cacheNames = "restClientService",
			key = "#result.helpId"
	)
	@CircuitBreaker(
			name = "restclientCircuitBreaker",
			fallbackMethod = "restclientFallback"
	)
	public HelpEntity insertHelpDataService(HelpEntity data) {
		
		return restClient.post()
				.uri("/insert")
				.body(data)
//				.headers(h -> {
//					h.set("", "");
//					h.set("", "");
//				})
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.body(HelpEntity.class);
	}
	
	@CachePut(
			cacheNames = "restClientService",
			key = "#result.helpId"
	)
	@CircuitBreaker(
			name = "restclientCircuitBreaker",
			fallbackMethod = "restclientFallback"
	)
	public HelpEntity putUpdateHelpDataService(int id, HelpEntity data) {
		
		return restClient.put()
				.uri("/updatePut/{id}", id)
				.body(data)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.body(HelpEntity.class);
		
	}
	
	@CachePut(
			cacheNames = "restClientService",
			key = "#result.helpId"
	)
	@CircuitBreaker(
			name = "restclientCircuitBreaker",
			fallbackMethod = "restclientFallback"
	)
	public HelpEntity putInsertHelpDataService(HelpEntity data) {
		
		return restClient.put()
				.uri("/updateOrInsert")
				.body(data)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.body(HelpEntity.class);

	}
	
	@CachePut(
			cacheNames = "restClientService",
			key = "#result.helpId"
	)
	@CircuitBreaker(
			name = "restclientCircuitBreaker",
			fallbackMethod = "restclientFallback"
	)
	public HelpEntity patchInsertHelpDataService(int id, HelpEntity data) {
		
		return restClient.patch()
				.uri("/patchupdate/{id}", id)
				.body(data)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.body(HelpEntity.class);

	}
	
	@CacheEvict(
			cacheNames = "restClientService",
			key = "#id"
	)
	@CircuitBreaker(
			name = "restclientCircuitBreaker",
			fallbackMethod = "restclientFallback"
	)
	public String deleteByIdHelpDataService(int id) {
		
		return restClient.delete()
				.uri("/delete/id/{id}", id)
				.retrieve()
				.body(String.class);

	}
	
    // restClientServiceFallback method
    @Cacheable(
    		cacheNames = "restClientService",
    		key = "'fallback'"
    )
	public List<Object> restclientListFallback(Throwable throwable) {
    	
    	List<Object> list = new ArrayList<Object>();
    	
    	System.out.println("========== FALLBACK CALLED ==========");
		System.out.println("Fallback called: " + throwable.getMessage());
		
    	list.add("Help Service is currently unavailable. Please try again later.");
    	return list;
    	
    }
    
    // restclientFallback method
    @Cacheable(
    		cacheNames = "restClientService",
    		key = "'fallback'"
    )
    public Object restclientFallback(Throwable throwable) {
    	
    	System.out.println("========== FALLBACK CALLED ==========");
		System.out.println("Fallback called: " + throwable.getMessage());
		
		
		return ResponseEntity
				.status(HttpStatus.SERVICE_UNAVAILABLE)
				.contentType(MediaType.TEXT_PLAIN)
				.body("Help Service is currently unavailable. Please try again later.");
    	
    }
	
}
