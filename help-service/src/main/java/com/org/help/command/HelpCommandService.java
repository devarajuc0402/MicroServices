package com.org.help.command;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import com.org.help.entity.HelpEntity;
import com.org.help.query.HelpQueryHandler;

@Service
public class HelpCommandService {

	private final Logger log = 
			LoggerFactory.getLogger(HelpCommandService.class);
	
	@Autowired
	private HelpCommandHandler helpCommandHandler;
	
	@Autowired
	private HelpQueryHandler helpQueryHandler;
	
	@CachePut(
			cacheNames = "helpService",
			key = "#result.helpId"
	)	
	public HelpEntity insertCommandServiceHelpData(HelpEntity helpEntity) {
		log.info("insert help data");		
		return helpCommandHandler.insertCommandHandlerHelpData(helpEntity);
	}
	
	@CachePut(
			cacheNames = "helpService",
			key = "#helpId"
	)
	public Optional<?> updatePutCommandServiceHelpData(int helpId, HelpEntity helpEntity) {
		Optional<HelpEntity> res = helpQueryHandler.getQueryHandlerHelpById(helpId);
		
		if(res.isEmpty()) {
			return Optional.empty();
		}
		helpEntity = mapHelpEntity(res, helpEntity);
		HelpEntity help = helpCommandHandler.insertCommandHandlerHelpData(helpEntity);
		log.info("update existing help data");

		return Optional.of(help);
	}
	
	@CachePut(
			cacheNames = "helpService",
			key = "#result.helpId"
	)
	public HelpEntity updateOrInsertCommandServiceHelpData(HelpEntity helpEntity) {
		HelpEntity help = new HelpEntity();
		Long longHelpId = helpEntity.getHelpId();
		if(longHelpId != null) {
			int helpId = longHelpId.intValue();
			Optional<HelpEntity> res = helpQueryHandler.getQueryHandlerHelpById(helpId);
			if(!res.isEmpty()) {
				helpEntity = mapHelpEntity(res, helpEntity);
				help = helpCommandHandler.insertCommandHandlerHelpData(helpEntity);
			}
			log.info("update existing help data");
			
			return help;
		} else {
			if(helpEntity.getTopic() == null) {
				helpEntity.setTopic("empty");	
			}
			help = helpCommandHandler.insertCommandHandlerHelpData(helpEntity);
			log.info("inserting help data");
			
			return help;	
		}
	}
	
	@CacheEvict(
			cacheNames = "helpService",
			key = "#helpId"
	)
	public Optional<?> deleteCommandServiceByHelpId(long helpId) {
		
		boolean isPresent = helpQueryHandler.existsQueryHandlerByHelpId(helpId);
		
		if(isPresent) {
			helpCommandHandler.deleteCommandHandlerByHelpId(helpId);
			HelpEntity helpEntity = new HelpEntity();
			helpEntity.setHelpId(helpId);
			Optional<?> result = Optional.of(helpEntity);
			log.info("delete existing help data by id");
			
			return result;
		}
		return Optional.empty();
	}
	
	@CacheEvict(
			cacheNames = "helpService",
			key = "#p0"
	)
	public Optional<?> deleteCommandServiceByHelpData(HelpEntity helpEntity) {
		
		boolean isPresent = helpQueryHandler.existsQueryHandlerByHelpId(helpEntity.getHelpId());
		
		if(isPresent) {
			helpCommandHandler.deleteCommandHandlerByHelpEntity(helpEntity);
			Optional<?> result = Optional.of(helpEntity);
			log.info("delete existing help data");
			
			return result;
		}
		return Optional.empty();
	}

	private HelpEntity mapHelpEntity(Optional<HelpEntity> help, HelpEntity helpEntity) {
		HelpEntity helpData = (HelpEntity) help.get();
		helpData.setHelpId(helpData.getHelpId());
		if(helpEntity.getInfo() != null) {
			helpData.setInfo(helpEntity.getInfo());
		}
		if(helpEntity.getSeq() != 0) {
			helpData.setSeq(helpEntity.getSeq());
		}
		if(helpEntity.getTopic() != null) {
			helpData.setTopic(helpEntity.getTopic());
		}
		log.info("mapped help data");
		return helpData;
	}
	
}
