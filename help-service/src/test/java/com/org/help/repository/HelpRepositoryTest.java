package com.org.help.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.org.help.entity.HelpEntity;

@DataJpaTest(properties = {
		"spring.cloud.config.enabled=false",
		"spring.cloud.config.import-check.enabled=false"
})
public class HelpRepositoryTest {

	@Autowired
	private HelpRepository helpRepository;
	
	@Test
	public void getHelpDataAll() {
		
		List<HelpEntity> helpList = helpRepository.findAll();
		System.out.println(helpList);

		assertThat(helpList)
			.isNotNull();
		
		assertThat(helpList)
			.hasSizeGreaterThanOrEqualTo(0);
	}
	
	@Test
	public void getHelpDataLimit() {
	
		List<HelpEntity> helpList = mockHelpEntityList();
		List<HelpEntity> insertList = helpRepository.saveAll(helpList);
		System.out.println(insertList);
		
		long helpId1 = insertList.get(0).getHelpId();
		long helpId2 = insertList.get(1).getHelpId();
		
		assertThat(helpId1)
			.isNotNull();
		assertThat(helpId2)
			.isNotNull();
		
		assertThat(helpId1)
			.isEqualTo(2);
		
		List<HelpEntity> helpListLimit = helpRepository.getByHelpIdLimit(1, 2);
		System.out.println(helpListLimit);
		
		assertThat(helpListLimit)
			.isNotNull();
		
		assertThat(helpListLimit.get(0).getHelpId())
			.isEqualTo(helpId1);
		
	}
	
	@Test
	public void getHelpDataById() {
		
		HelpEntity helpEntity = mockHelpEntity();
		
		HelpEntity insertData = helpRepository.save(helpEntity);
		System.out.println(insertData);
		
		assertThat(insertData)
			.isNotNull();
		
		assertThat(insertData.getHelpId())
			.isNotNull();
		
		int helpId = insertData.getHelpId().intValue();
		Optional<HelpEntity> help = helpRepository.findByHelpId(helpId);
		System.out.println(help);
		
		assertThat(help)
			.isNotNull();
		
		assertThat(help.get().getHelpId())
			.isEqualTo(insertData.getHelpId());
	}
	
	@Test
	public void insertTest() {

		HelpEntity helpEntity = mockHelpEntity();
		
		HelpEntity insertData = helpRepository.save(helpEntity);
		System.out.println(insertData);
		
		assertThat(insertData)
			.isNotNull();
		
		assertThat(insertData.getHelpId())
			.isNotNull();
	}

	private HelpEntity mockHelpEntity() {

		HelpEntity helpEntity = new HelpEntity();
		helpEntity.setInfo("Database Test");
		helpEntity.setSeq(1);
		helpEntity.setTopic("Junit");
		
		return helpEntity;
	}
	
	public List<HelpEntity> mockHelpEntityList() {

		List<HelpEntity> helpList = new ArrayList<HelpEntity>();
		HelpEntity helpEntity = new HelpEntity();
		
		helpEntity.setInfo("Database Test 1");
		helpEntity.setSeq(1);
		helpEntity.setTopic("Junit 1");
		helpList.add(helpEntity);
		
		helpEntity = new HelpEntity();
		helpEntity.setInfo("Database Test 2");
		helpEntity.setSeq(2);
		helpEntity.setTopic("Junit 2");
		helpList.add(helpEntity);
		
		return helpList;
	}
	
}
