package com.org.help.query;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.org.help.entity.HelpEntity;

@Service
public class HelpQueryService {

	@Autowired
	private HelpQueryHandler helpQueryHandler;


	@Cacheable(
			cacheNames = "helpService",
			key = "'all'"
	)
	public List<HelpEntity> getQueryServiceHelpList() {
		System.out.println("getQueryServiceHelpList()");
		return helpQueryHandler.getQueryHandlerHelpList();
	}
	
	@Cacheable(
			cacheNames = "helpService",
			key = "#helpId"
	)
	public Optional<HelpEntity> getQueryServiceHelpById(int helpId) {
		System.out.println("getQueryServiceHelpById()");
		return helpQueryHandler.getQueryHandlerHelpById(helpId);
	}
	
	@Cacheable(
			cacheNames = "helpService",
			key = "#startHelpId +'_'+ #endHelpId"
	)
	public List<HelpEntity> getQueryServiceHelpIdLimit(int startHelpId, int endHelpId) {
		System.out.println("getQueryServiceHelpIdLimit()");
		return helpQueryHandler.getQueryHandlerHelpIdLimit(startHelpId, endHelpId);
	}
}
