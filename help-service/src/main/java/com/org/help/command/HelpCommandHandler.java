package com.org.help.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.org.help.entity.HelpEntity;
import com.org.help.repository.HelpRepository;

@Component
public class HelpCommandHandler {

	@Autowired
	private HelpRepository helpRepository;
	
	@Transactional
	public HelpEntity insertCommandHandlerHelpData(HelpEntity helpEntity) {
		return helpRepository.save(helpEntity);
	}
	
	@Transactional
	public void deleteCommandHandlerByHelpId(long helpId) {
		helpRepository.deleteById(helpId);
	}
	
	@Transactional
	public void deleteCommandHandlerByHelpEntity(HelpEntity helpEntity) {
		helpRepository.delete(helpEntity);
	}
}
