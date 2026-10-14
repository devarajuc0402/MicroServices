package com.org.help.restclient.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class HelpRestClientControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void getHelpSuccessMessage() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/restclient/message")
					
					.with(SecurityMockMvcRequestPostProcessors
							.jwt()
							.authorities(new SimpleGrantedAuthority("ROLE_USER"))
					)
			)
			
			.andDo(MockMvcResultHandlers.print())
			
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void getHelpUrlNotFound() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/restclient/message/NOT_FOUND")
					
					.with(SecurityMockMvcRequestPostProcessors
							.jwt()
							.authorities(new SimpleGrantedAuthority("ROLE_USER"))
					)
			)
			
			.andDo(MockMvcResultHandlers.print())
			
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void getHelp401Error() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/restclient/message")
			)
			
			.andDo(MockMvcResultHandlers.print())
			
			.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}
}
