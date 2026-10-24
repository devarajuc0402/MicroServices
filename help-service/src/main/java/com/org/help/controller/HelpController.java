package com.org.help.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.help.entity.HelpEntity;
import com.org.help.exception.ResourceNotFoundException;
import com.org.help.kafka.service.KafkaProducerService;
import com.org.help.query.HelpQueryService;
import com.org.help.restclient.RestClientService;
import com.org.help.service.HelpService;

@RestController
@RequestMapping("/api/help")
public class HelpController {
	
	private static final Logger logger =
            LogManager.getLogger(HelpController.class);
	
	private final HelpQueryService helpQueryService;
	
	@Autowired
	HelpService helpService;
	
	@Autowired
	KafkaProducerService kafkaProducerService;
	
	public HelpController(
			HelpQueryService helpQueryService) {
		this.helpQueryService = helpQueryService;
	}

	/* GET - get All help list */
	@RequestMapping(value = "/list", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllHelpController() throws Exception {
		
//		List<HelpEntity> helpList = helpQueryService.getQueryServiceHelpList();
		
		try {
			
			CompletableFuture<List<HelpEntity>> future = CompletableFuture.supplyAsync(() -> {

					return helpQueryService.getQueryServiceHelpList();
					
			});
			// checked how thenCompose working
			CompletableFuture<List<HelpEntity>> future1 = 
					CompletableFuture.supplyAsync(() -> {
						return helpQueryService.getQueryServiceHelpList();
						
					}).thenCompose(list -> {
							return CompletableFuture.supplyAsync(() -> list);
					});
			List<HelpEntity> list = future1.get();
			System.out.println(list);

			List<HelpEntity> helpList = future.get();
			
			if(helpList.isEmpty()) {
				
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResourceNotFoundException(helpList.toString()));
			}
			
			kafkaProducerService.produce(helpList);
			return ResponseEntity.ok(helpList);
			
		} catch (Exception e) {
			
			throw new Exception(e.toString());
		}
	}
	
	
	/* GET - get a specific help ID through request param */
	@RequestMapping(value = "/id", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getHelpIdRequestParamController(@RequestParam int helpId) throws Exception {

//		Optional<HelpEntity> helpEntity = helpQueryService.getQueryServiceHelpById(helpId);
		
		try {
			CompletableFuture<Optional<HelpEntity>> future = 
					CompletableFuture.supplyAsync(() -> {
						
						return helpQueryService.getQueryServiceHelpById(helpId);
						
					});
			Optional<HelpEntity> helpEntity = future.get();

			if(!helpEntity.isPresent()) {
				
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResourceNotFoundException(helpEntity.toString()));
				
			} else {
				logger.info(helpEntity);
				kafkaProducerService.produce(helpEntity.get());
				return ResponseEntity.ok(helpEntity);
			}			
		} catch (Exception e) {
			
			throw new Exception(e.toString());
		}

	}
	
	
	/* GET - get a specific help ID through path variable */
	@RequestMapping(value = "/id/{helpId}", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getHelpIdPathVariableController(@PathVariable int helpId) throws Exception {
		try {
		
//			Optional<HelpEntity> helpEntity = helpQueryService.getQueryServiceHelpById(helpId);
			
			CompletableFuture<Optional<HelpEntity>> future = 
					
					CompletableFuture.supplyAsync(() -> {
						return helpQueryService.getQueryServiceHelpById(helpId);
					
					});
			Optional<HelpEntity> helpEntity = future.get();

			if(!helpEntity.isPresent()) {
				
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResourceNotFoundException(helpEntity.toString()));
			
			} else {
				
				kafkaProducerService.produce(helpEntity.get());
				return ResponseEntity.ok(helpEntity);
			}
			
		} catch (Exception e) {
			
			throw new Exception(e.toString());
		
		}
	}

	
	/* GET - get help details from start id to end id */
	@RequestMapping(value = "/id/range", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getHelpIdRequestParamController(
			@RequestParam int startHelpId,
			@RequestParam int endHelpId) throws Exception {

//		List<HelpEntity> helpLimitList = helpQueryService.getQueryServiceHelpIdLimit(startHelpId, endHelpId);
		
		try {
			
			CompletableFuture<List<HelpEntity>> future = 
					CompletableFuture.supplyAsync(() -> {
						
						return helpQueryService.getQueryServiceHelpIdLimit(startHelpId, endHelpId);
						
					})
					.thenApply(updateFuture -> {
						return updateFuture;
					});
			
			List<HelpEntity> helpEntityList = future.get();
		
			if(helpEntityList.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResourceNotFoundException(helpEntityList.toString()));

			} else {
				kafkaProducerService.produce(helpEntityList);
				return ResponseEntity.ok(helpEntityList);
			}
			
		} catch (Exception e) {
			
			throw new Exception(e.toString());
			
		}
	}
	
	
	/* POST - insert new help data into db */
	@RequestMapping(value = "/insert", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertHelpDataController(@RequestBody HelpEntity helpEntity) 
		throws Exception {
		
//		HelpEntity help = helpService.insertHelpDataService(helpEntity);
		
		try {
			
			CompletableFuture<HelpEntity> future = 
					CompletableFuture.supplyAsync(() -> {
						return helpService.insertHelpDataService(helpEntity);
					});
			HelpEntity help = future.get();
		
			kafkaProducerService.produce(help);
			return ResponseEntity.ok(help);
			
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		
	}
	
	
	/* PUT - partial update data into db */
	@RequestMapping(value = "/updatePut/{helpId}", method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updatePutHelpDataController(
			@PathVariable int helpId,
			@RequestBody HelpEntity helpEntity) throws Exception {
		
//		Optional<?> help = helpService.updatePutHelpDataService(helpId, helpEntity);
		
		try {
			
			CompletableFuture<Optional<?>> future = 
					CompletableFuture.supplyAsync(() -> {
						
						return helpService.updatePutHelpDataService(helpId, helpEntity);
						
					});
			Optional<?> help = future.get();

			if(help.isEmpty()) {
				
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResourceNotFoundException(help.toString()));
			
			} else {
				kafkaProducerService.produce((HelpEntity)help.get());
				return ResponseEntity.ok(help);
			}
		} catch (Exception e) {
			
			throw new Exception(e.toString());
			
		}
	}


	/* PUT - update completely new help data into db */ 
	@RequestMapping(value = "/updateOrInsert", method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateOrInsertHelpDataController(@RequestBody HelpEntity helpEntity) 
		throws Exception {
		
//		HelpEntity help = helpService.updateOrInsertHelpDataService(helpEntity);

		try {
			
			CompletableFuture<HelpEntity> future = 
					CompletableFuture.supplyAsync(() -> {
						
						return helpService.updateOrInsertHelpDataService(helpEntity);
						
					});
			HelpEntity help = future.get();
		
			kafkaProducerService.produce(help);
			return ResponseEntity.ok(help);
			
		} catch (Exception e) {
			
			throw new Exception(e.toString());
		}
	}

	/* PATCH - update completely new help data into db */ 
	@RequestMapping(value = "/patchupdate/{id}", method = RequestMethod.PATCH,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> patchUpdateHelpDataController(
			@PathVariable int id,
			@RequestBody HelpEntity helpEntity) throws Exception {
		
//		Optional<?> help = helpService.updatePutHelpDataService(id, helpEntity);
		
		try {
			
			CompletableFuture<Optional<?>> future = 
					CompletableFuture.supplyAsync(() -> {
						
						return helpService.updatePutHelpDataService(id, helpEntity);
						
					});
			Optional<?> help = future.get();
			
			if(help.isEmpty()) {
				
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResourceNotFoundException(help.get().toString()));
			} else {
				
				kafkaProducerService.produce((HelpEntity)help.get());
				return ResponseEntity.ok(help);
				
			}
			
		} catch (Exception e) {
			
			throw new Exception(e.toString());
		}
	}
	
	/* DELETE - delete a help data by id from db */ 
	@RequestMapping(value = "/delete/id/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteByIdHelpDataController(@PathVariable int id) throws Exception {
		
//		Optional<?> help = helpService.deleteByIdHelpDataService(id);
		
		try {
		
			CompletableFuture<Optional<?>> future = 
					CompletableFuture.supplyAsync(() -> {
						
						return helpService.deleteByIdHelpDataService(id);
					});
			
			Optional<?> help = future.get();
			
			kafkaProducerService.produce((HelpEntity)help.get());
			return ResponseEntity.ok(help);
			
		} catch (Exception e) {
			
			throw new Exception(e.toString());
		}
		
	}
	
	/* DELETE - delete a help data by entity from db */ 
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteByIdHelpDataController(@RequestBody HelpEntity helpEntity) throws Exception {
		
//		Optional<?> help = helpService.deleteHelpDataService(helpEntity);
		
		try {
		
			CompletableFuture<Optional<?>> future = 
					CompletableFuture.supplyAsync(() -> {
						
						return helpService.deleteHelpDataService(helpEntity);
					});
			Optional<?> help = future.get();
			
			kafkaProducerService.produce((HelpEntity)help.get());
			return ResponseEntity.ok(help);
			
		} catch (Exception e) {
			
			throw new Exception(e.toString());
		}
		
	}
	
	@Autowired
	RestClientService restClientService;
	
	/* GET - get all the help data through Rest Client */ 
	@RequestMapping(value = "/restclient", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRestClientHelpDataController() throws Exception {

//		List<HelpEntity> list = restClientService.getHelpData();
//		System.out.println(list);
		
		try {
			
			CompletableFuture<List<HelpEntity>> future = 
					CompletableFuture.supplyAsync(() -> {
						
						return restClientService.getHelpData();
					});
			
			List<HelpEntity> list = future.get();
			System.out.println(list);

			kafkaProducerService.produce(list);
			return ResponseEntity.ok(list);
			
		} catch (Exception e) {
			
			throw new Exception(e.toString());
		}
	}
}
