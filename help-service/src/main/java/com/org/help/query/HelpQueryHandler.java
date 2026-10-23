package com.org.help.query;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.org.help.entity.HelpEntity;
import com.org.help.repository.HelpRepository;

@Component
public class HelpQueryHandler {

	@Autowired
	private HelpRepository helpRepository;

	@Transactional(readOnly = true)
	public List<HelpEntity> getQueryHandlerHelpList() {
		return helpRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<HelpEntity> getQueryHandlerHelpById(int helpId) {
		return helpRepository.findByHelpId(helpId);
	}

	@Transactional(readOnly = true)
	public List<HelpEntity> getQueryHandlerHelpIdLimit(int startHelpId, int endHelpId) {
		return helpRepository.getByHelpIdLimit(startHelpId, endHelpId);
	}

}
