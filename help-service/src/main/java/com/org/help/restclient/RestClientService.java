package com.org.help.restclient;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.org.help.entity.HelpEntity;

@Service
public class RestClientService {
	
	private final RestClient restClient;
	
	public RestClientService(RestClient restClient) {
		this.restClient = restClient;
	}

	public List<HelpEntity> getHelpData() {
		
		List<HelpEntity> response = restClient.get()
				.uri("/list")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.body(new ParameterizedTypeReference<List<HelpEntity>>() {});
		
		return response;
	}

}
