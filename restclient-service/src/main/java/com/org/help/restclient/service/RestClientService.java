package com.org.help.restclient.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.org.help.restclient.entity.HelpEntity;

@Service
public class RestClientService {

	private final RestClient restClient;
	
	public RestClientService(RestClient restClient) {
		this.restClient = restClient;
	}
	
	public List<Object> getAllHelpDataService() {
		
		return restClient.get()
				.uri("/list")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.body(new ParameterizedTypeReference<List<Object>>() {});
	}
	
	public Object getHelpDataByIdService(int id) {
		
		return restClient.get()
				.uri("/id/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.body(Object.class);
	}
	
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
	
	public HelpEntity putUpdateHelpDataService(int id, HelpEntity data) {
		
		return restClient.put()
				.uri("/updatePut/{id}", id)
				.body(data)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.body(HelpEntity.class);
		
	}
	
	public HelpEntity putInsertHelpDataService(HelpEntity data) {
		
		return restClient.put()
				.uri("/updateOrInsert")
				.body(data)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.body(HelpEntity.class);

	}
	
	public HelpEntity patchInsertHelpDataService(int id, HelpEntity data) {
		
		return restClient.patch()
				.uri("/patchupdate/{id}", id)
				.body(data)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.body(HelpEntity.class);

	}
	
	public String deleteByIdHelpDataService(int id) {
		
		return restClient.delete()
				.uri("/delete/id/{id}", id)
				.retrieve()
				.body(String.class);

	}
	
}
