package com.org.help.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.org.help.entity.HelpEntity;
import com.org.help.repository.HelpRepository;

@Service
public class HelpService {

	@Autowired
	private HelpRepository helpRepository;
	
//	@Cacheable(
//			cacheNames = "helpService",
//			key = "'all'"
//	)
//	@Async
//	public CompletableFuture<List<HelpEntity>> getAllHelpService() {
//		List<HelpEntity> helpList = helpRepository.findAll();
//		
//		CompletableFuture<List<HelpEntity>> future = CompletableFuture.completedFuture(helpList);
////		CompletableFuture<List<HelpEntity>> future = HelpService.process(helpList);
//		
//		return future;
//	}
	
//	@Cacheable(
//			cacheNames = "helpService",
//			key = "#helpId"
//	)
////	@Async
//	public Optional<HelpEntity> getHelpIdService(int helpId) {
//		Optional<HelpEntity> help = helpRepository.findByHelpId(helpId);
//		return help;
//	}
	
//	@Cacheable(
//			cacheNames = "helpService",
//			key = "#startHelpId +'_'+ #endHelpId"
//	)
//	@Async
//	public CompletableFuture<List<HelpEntity>> getHelpIdLimitService(int startHelpId, int endHelpId) {
//		List<HelpEntity> help = helpRepository.getByHelpIdLimit(startHelpId, endHelpId);
//		
//		CompletableFuture<List<HelpEntity>> future = HelpService.process(help);
//		return future;
//	}
	
	@CachePut(
			cacheNames = "helpService",
			key = "#result.helpId"
	)
	public HelpEntity insertHelpDataService(HelpEntity helpEntity) {
		HelpEntity help = helpRepository.save(helpEntity);
		return help;
	}
	
	@CachePut(
			cacheNames = "helpService",
			key = "#helpId"
	)
	public Optional<?> updatePutHelpDataService(int helpId, HelpEntity helpEntity) {
		
		Optional<HelpEntity> res = helpRepository.findByHelpId(helpId);
		
		if(res.isEmpty()) {
			return Optional.empty();
		}
		helpEntity = mapHelpEntity(res, helpEntity);
		HelpEntity help = helpRepository.save(helpEntity);
		return Optional.of(help);
	}

	@CachePut(
			cacheNames = "helpService",
			key = "#result.helpId"
	)
	public HelpEntity updateOrInsertHelpDataService(HelpEntity helpEntity) {
		HelpEntity help = new HelpEntity();
		Long longHelpId = helpEntity.getHelpId();
		if(longHelpId != null) {
			int helpId = longHelpId.intValue();
			Optional<HelpEntity> res = helpRepository.findByHelpId(helpId);
			if(!res.isEmpty()) {
				helpEntity = mapHelpEntity(res, helpEntity);
				help = helpRepository.save(helpEntity);
			}
			return help;
		} else {
			if(helpEntity.getTopic() == null) {
				helpEntity.setTopic("empty");	
			}
			help = helpRepository.save(helpEntity);
			return help;	
		}
	}
	
	@CacheEvict(
			cacheNames = "helpService",
			key = "#helpId"
	)
	public Optional<?> deleteByIdHelpDataService(int helpId) {
		helpRepository.deleteById((long)helpId);
		Optional<?> result = Optional.of(helpId+" Deleted Successfully");
		
		return result;	
	}
	
	@CacheEvict(
			cacheNames = "helpService",
			key = "#result.helpId"
	)
	public Optional<?> deleteHelpDataService(HelpEntity helpEntity) {
		helpRepository.delete(helpEntity);
		Optional<?> result = Optional.of(helpEntity.getHelpId()+" Deleted Successfully");
		
		return result;	
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
		return helpData;
	}
	
	@Async
	public static CompletableFuture<List<HelpEntity>> process(List<HelpEntity> help) {

	    return CompletableFuture.completedFuture(help);
	}
	
}
