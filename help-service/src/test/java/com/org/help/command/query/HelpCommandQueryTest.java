package com.org.help.command.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.org.help.command.HelpCommandHandler;
import com.org.help.command.HelpCommandService;
import com.org.help.entity.HelpEntity;
import com.org.help.query.HelpQueryHandler;
import com.org.help.query.HelpQueryService;
import com.org.help.repository.HelpRepository;
import com.org.help.repository.HelpRepositoryTest;

@DataJpaTest(properties = {
	"spring.cloud.config.enabled=false",
	"spring.cloud.config.import-check.enabled=false"
})
@Import({
	HelpQueryHandler.class,
	HelpQueryService.class,
	HelpCommandHandler.class,
	HelpCommandService.class
})
public class HelpCommandQueryTest {

	@Autowired
	private HelpRepository helpRepository;
	
	@Autowired
	private HelpQueryService helpQueryService;
	
	@Autowired
	private HelpCommandService helpCommandService;
	
	@Test
	public void deleteHelpById() {
		
		HelpRepositoryTest helpRepositoryTest = new HelpRepositoryTest();
		List<HelpEntity> helpList = helpRepositoryTest.mockHelpEntityList();
		List<HelpEntity> insertHelp = helpRepository.saveAll(helpList);
		
		assertThat(insertHelp)
			.isNotNull();
		
		boolean helpExistHelpId1 = helpQueryService.existsQueryServiceByHelpId(1);
		boolean helpExistHelpId2 = helpQueryService.existsQueryServiceByHelpId(2);
		
		assertThat(helpExistHelpId1)
			.isEqualTo(true);
		assertThat(helpExistHelpId2)
			.isEqualTo(true);
		
		helpCommandService.deleteCommandServiceByHelpId(1);
		helpCommandService.deleteCommandServiceByHelpId(2);
		
		helpExistHelpId1 = helpQueryService.existsQueryServiceByHelpId(1);
		helpExistHelpId2 = helpQueryService.existsQueryServiceByHelpId(2);
		
		assertTrue(helpExistHelpId1 == false && helpExistHelpId2 == false);
	}
}
