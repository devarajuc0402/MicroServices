package com.org.help.restclient.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ConfigRestclientControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void getConfigSuccessMessage() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/restclient/config-restclient-test")
					
					.with(SecurityMockMvcRequestPostProcessors
							.jwt()
							.authorities(new SimpleGrantedAuthority("ROLE_USER"))
					)
			)
			.andExpect(
					MockMvcResultMatchers
						.status()
						.isOk()	
			);
	}
	
	@Test
	public void getConfigNotFound() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/restclient/config-restclient-test/NOT_FOUND")
					
					.with(SecurityMockMvcRequestPostProcessors
							.jwt()
							.authorities(new SimpleGrantedAuthority("ROLE_USER"))
					)
			)
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void getConfig401Error() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/restclient/config-restclient-test")
			)
			.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}
}
