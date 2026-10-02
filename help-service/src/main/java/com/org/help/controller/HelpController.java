package com.org.help.controller;

import java.time.Duration;
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
import com.org.help.restclient.RestClientService;
import com.org.help.service.HelpService;

@RestController
@RequestMapping("/api/help")
public class HelpController {
	
	private static final Logger logger =
            LogManager.getLogger(HelpController.class);
	
	@Autowired
	HelpService helpService;
	
	@Autowired
	KafkaProducerService kafkaProducerService;

	/* GET - get All help list */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/list", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllHelpController() {
		List<HelpEntity> helpList = new ArrayList<HelpEntity>();
		try {
			CompletableFuture<List<HelpEntity>> future = helpService.getAllHelpService();
			
			future.supplyAsync(() -> {
				try {
					Thread.sleep(Duration.ofSeconds(2));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "done";
			});
			
			helpList = future.get();
			
			List<HelpEntity> resultList = helpList;
			if(helpList.isEmpty()) {
				return ResponseEntity.ok(Optional.of(resultList
						.stream()
						.findFirst()
						.orElseThrow(() -> 
						new ResourceNotFoundException(resultList.toString()))));
			}
		} catch (Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Something went wrong to get Help All list "+e);
		}
		kafkaProducerService.produce(helpList);
		return ResponseEntity.ok(helpList);
	}
	
	
	/* GET - get a specific help ID through request param */
	@RequestMapping(value = "/id", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getHelpIdRequestParamController(@RequestParam int helpId) throws ResourceNotFoundException {

		Optional<HelpEntity> helpEntity = helpService.getHelpIdService(helpId);

		if(!helpEntity.isPresent()) {
			ResponseEntity<HelpEntity> validationMsg = ResponseEntity.of(Optional.of(helpEntity
					.orElseThrow(() -> new ResourceNotFoundException(helpId+""))));
			logger.info(helpEntity);
			return validationMsg;
			
		} else {
			logger.info(helpEntity);
			kafkaProducerService.produce(helpEntity.get());
			return ResponseEntity.ok(helpEntity);
		}
	}
	
	
	/* GET - get a specific help ID through path variable */
	@RequestMapping(value = "/id/{helpId}", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getHelpIdPathVariableController(@PathVariable int helpId) throws ResourceNotFoundException {
		Optional<HelpEntity> helpEntity = helpService.getHelpIdService(helpId);
		
		if(!helpEntity.isPresent()) {
			return ResponseEntity.of(Optional.of(helpEntity
					.orElseThrow(() -> new ResourceNotFoundException(helpId+""))));
		} else {
			kafkaProducerService.produce(helpEntity.get());
			return ResponseEntity.ok(helpEntity);
		}
	}

	
	/* GET - get help details from start id to end id */
	@RequestMapping(value = "/id/range", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getHelpIdRequestParamController(
			@RequestParam int startHelpId,
			@RequestParam int endHelpId) throws Exception {

		CompletableFuture<List<HelpEntity>> future = helpService.getHelpIdLimitService(startHelpId, endHelpId);
		List<HelpEntity> helpEntityList = future.get();

		if(helpEntityList.isEmpty()) {
			return ResponseEntity.of(Optional.of(helpEntityList.stream()
					.findFirst().orElseThrow(() -> new Exception(helpEntityList.toString()))));

		} else {
			kafkaProducerService.produce(helpEntityList);
			return ResponseEntity.ok(helpEntityList);
		}
	}
	
	
	/* POST - insert new help data into db */
	@RequestMapping(value = "/insert", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertHelpDataController(@RequestBody HelpEntity helpEntity) {
		
		HelpEntity help = helpService.insertHelpDataService(helpEntity);
		try {
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Something went wrong to insert Help ID details");
		}
		kafkaProducerService.produce(help);
		return ResponseEntity.ok(help);
	}
	
	
	/* PUT - partial update data into db */
	@RequestMapping(value = "/updatePut/{helpId}", method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updatePutHelpDataController(
			@PathVariable int helpId,
			@RequestBody HelpEntity helpEntity) {
		
		Optional<?> help = helpService.updatePutHelpDataService(helpId, helpEntity);
		try {
			if(help.isEmpty()) {
				return ResponseEntity
						.status(HttpStatus.NOT_FOUND)
						.body("Something went wrong to update data");
			} else {
				kafkaProducerService.produce((HelpEntity)help.get());
				return ResponseEntity.ok(help);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Something went wrong to insert Help ID details");
		}
	}


	/* PUT - update completly new help data into db */ 
	@RequestMapping(value = "/updateOrInsert", method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateOrInsertHelpDataController(@RequestBody HelpEntity helpEntity) {
		
		HelpEntity help = helpService.updateOrInsertHelpDataService(helpEntity);
		try {
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Something went wrong to insert Help ID details");
		}
		kafkaProducerService.produce(help);
		return ResponseEntity.ok(help);
	}

	/* PATCH - update completly new help data into db */ 
	@RequestMapping(value = "/patchupdate/{id}", method = RequestMethod.PATCH,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> patchUpdateHelpDataController(
			@PathVariable int id,
			@RequestBody HelpEntity helpEntity) {
		
		Optional<?> help = helpService.updatePutHelpDataService(id, helpEntity);
		try {
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Something went wrong to insert Help ID details");
		}
		kafkaProducerService.produce((HelpEntity)help.get());
		return ResponseEntity.ok(help);
	}
	
	/* DELETE - delete a help data by id from db */ 
	@RequestMapping(value = "/delete/id/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteByIdHelpDataController(@PathVariable int id) {
		
		Optional<?> help = helpService.deleteByIdHelpDataService(id);
		try {
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Something went wrong to insert Help ID details");
		}
		kafkaProducerService.produce((HelpEntity)help.get());
		return ResponseEntity.ok(help);
	}
	
	/* DELETE - delete a help data by entity from db */ 
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteByIdHelpDataController(@RequestBody HelpEntity helpEntity) {
		
		Optional<?> help = helpService.deleteHelpDataService(helpEntity);
		try {
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Something went wrong to insert Help ID details");
		}
		kafkaProducerService.produce((HelpEntity)help.get());
		return ResponseEntity.ok(help);
	}
	
	@Autowired
	RestClientService restClientService;
	
	/* GET - get all the help data through Rest Client */ 
	@RequestMapping(value = "/restclient", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRestClientHelpDataController() {

		List<HelpEntity> list = restClientService.getHelpData();
		System.out.println(list);
		
		try {
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Something went wrong to insert Help ID details");
		}
		kafkaProducerService.produce(list);
		return ResponseEntity.ok(list);
	}
}
