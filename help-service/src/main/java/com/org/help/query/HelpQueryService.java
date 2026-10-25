package com.org.help.query;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.org.help.entity.HelpEntity;

@Service
public class HelpQueryService {
	
	private final Logger log = 
			LoggerFactory.getLogger(HelpQueryService.class);

	@Autowired
	private HelpQueryHandler helpQueryHandler;


	@Cacheable(
			cacheNames = "helpService",
			key = "'all'"
	)
	public List<HelpEntity> getQueryServiceHelpList() {
		log.info("get help list");
		return helpQueryHandler.getQueryHandlerHelpList();
	}
	
	@Cacheable(
			cacheNames = "helpService",
			key = "#helpId"
	)
	public Optional<HelpEntity> getQueryServiceHelpById(int helpId) {
		log.info("get help list by helpId: {}", helpId);
		return helpQueryHandler.getQueryHandlerHelpById(helpId);
	}
	
	@Cacheable(
			cacheNames = "helpService",
			key = "#startHelpId +'_'+ #endHelpId"
	)
	public List<HelpEntity> getQueryServiceHelpIdLimit(int startHelpId, int endHelpId) {
		log.info("get help list by startId: {} & endHelpId: {}", startHelpId, endHelpId);
		return helpQueryHandler.getQueryHandlerHelpIdLimit(startHelpId, endHelpId);
	}
	
	@Cacheable(
			cacheNames = "helpService",
			key = "#startHelpId +'_'+ #endHelpId"
	)
	public boolean existsQueryServiceByHelpId(int helpId) {
		log.info("check help exist by helpId: {}", helpId);
		return helpQueryHandler.existsQueryHandlerByHelpId(helpId);
	}
}
