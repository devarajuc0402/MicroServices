package com.org.help.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

//@SpringBootTest(properties = {
//	"spring.cloud.config.enabled=false",
//	"spring.cloud.config.import-check.enabled=false",
//    "app.kafka.topic.enabled=false"
//})
@SpringBootTest
@AutoConfigureMockMvc
public class HelpControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
    private JwtDecoder jwtDecoder;
	
	@Test
	public void getAllHelpControllerList() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/help/list")
					
					.with(SecurityMockMvcRequestPostProcessors
							.jwt()
							.authorities(new SimpleGrantedAuthority("ROLE_HELP"))
					)
			)
				
			.andDo(MockMvcResultHandlers.print())
			
			.andExpect(MockMvcResultMatchers
					.status()
					.is2xxSuccessful()
			);
	}
	
	@Test
	public void get401Error() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/help/list")
			)
			
			.andDo(MockMvcResultHandlers.print())
			
			.andExpect(MockMvcResultMatchers
					.status()
					.isUnauthorized()
			);
	}
	
	@Test
	public void get404NotFound() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/help/list/NOT_FOUND")
					
					.with(SecurityMockMvcRequestPostProcessors
							.jwt()
							.authorities(new SimpleGrantedAuthority("ROLE_HELP"))
					)
			)

			.andDo(MockMvcResultHandlers.print())
			
			.andExpect(MockMvcResultMatchers
					.status()
					.isNotFound()
			);
	}
}
