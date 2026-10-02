package com.org.help.restclient.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.org.help.restclient.entity.HelpEntity;
import com.org.help.restclient.exception.ResourceNotFoundException;
import com.org.help.restclient.service.RestClientService;

@RestController
@RequestMapping("/api/help/restclient")
public class RestClientController {

	@Autowired
	RestClientService restClientService;
	
	// GET : get all the data of help list
    @RequestMapping(value = "/list", method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllHelpListController() throws ResourceNotFoundException {
    	
    	List<Object> helpList = restClientService.getAllHelpDataService();
    	
    	if(helpList.size() == 0) {
    		return ResponseEntity.ok(Optional.of(helpList.stream()
    				.findFirst()
    				.orElseThrow(() -> new ResourceNotFoundException(helpList.toString()))));
    	}
    	
    	System.out.println(helpList);
    	
    	return ResponseEntity.ok(helpList);
    }
    
	// GET : get help data by id
    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getHelpDataByIdController(@PathVariable int id) throws ResourceNotFoundException {
    	
    	Object helpList = restClientService.getHelpDataByIdService(id);
    	
    	System.out.println(helpList);
    	
    	return ResponseEntity.ok(helpList);
    }
    
	// POST : insert help data into db
    @RequestMapping(value = "/insert", method = RequestMethod.POST,
    		produces = MediaType.APPLICATION_JSON_VALUE,
    		consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insertHelpDataController(@RequestBody HelpEntity data) throws ResourceNotFoundException {
    	
    	HelpEntity helpList = restClientService.insertHelpDataService(data);
    	
    	System.out.println(helpList);
    	
    	return ResponseEntity.ok(helpList);
    }
    
    // PUT : update help data by id into db
    @RequestMapping(value = "/put/update/{id}", method = RequestMethod.PUT,
    		produces = MediaType.APPLICATION_JSON_VALUE,
    		consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putHelpDataController(
    		@PathVariable int id,
    		@RequestBody HelpEntity data) throws ResourceNotFoundException {
    	
    	HelpEntity helpList = restClientService.putUpdateHelpDataService(id, data);
    	
    	System.out.println(helpList);
    	
    	return ResponseEntity.ok(helpList);
    }

    @RequestMapping(value = "/put/insert", method = RequestMethod.PUT,
    		produces = MediaType.APPLICATION_JSON_VALUE,
    		consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putHelpDataController(
    		@RequestBody HelpEntity data) throws ResourceNotFoundException {
    	
    	HelpEntity helpList = restClientService.putInsertHelpDataService(data);
    	
    	System.out.println(helpList);
    	
    	return ResponseEntity.ok(helpList);
    }
    
    @RequestMapping(value = "/patch/{id}", method = RequestMethod.PATCH,
    		produces = MediaType.APPLICATION_JSON_VALUE,
    		consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> patchHelpDataController(
    		@PathVariable int id,
    		@RequestBody HelpEntity data) throws ResourceNotFoundException {
    	
    	HelpEntity helpList = restClientService.patchInsertHelpDataService(id, data);
    	
    	System.out.println(helpList);
    	
    	return ResponseEntity.ok(helpList);
    }
    
    @RequestMapping(value = "/delete/id/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteByIdHelpDataController(
    		@PathVariable int id) throws ResourceNotFoundException {
    	
    	String helpList = restClientService.deleteByIdHelpDataService(id);
    	
    	System.out.println(helpList);
    	
    	return ResponseEntity.ok(helpList);
    }

}


