package com.org.help.restclient.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigRestclientTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getSuccessMessage() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/restclient/message")
					.with(SecurityMockMvcRequestPostProcessors
							.user("testuser")
							.roles("USER", "REST")
					)
			)
			.andExpect(
					MockMvcResultMatchers
						.status()
						.isOk()
			);
	}
	
	@Test
	public void getSuccessConfigTestResponse() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/restclient/config-restclient-test")
					.with(SecurityMockMvcRequestPostProcessors
							.user("testuser")
							.roles("USER", "REST")
							
					)
			)
			.andExpect(
					MockMvcResultMatchers
						.status()
						.isOk()
			);
	}
	
	@Test
	public void getJwt401ErrorMessage() throws Exception  {
		
		mockMvc
			.perform(MockMvcRequestBuilders.get("/api/restclient/message"))
			.andExpect(
					MockMvcResultMatchers
						.status()
						.isUnauthorized()
			);
	}
	
	@Test
	public void getJwt401ErrorConfigTest() throws Exception  {
		
		mockMvc
			.perform(MockMvcRequestBuilders.get("/config-restclient-test"))
			.andExpect(
					MockMvcResultMatchers
						.status()
						.isUnauthorized()
					);
	}
	
	@Test
	public void get404NotFoundError() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/request)_not_found")
					.with(SecurityMockMvcRequestPostProcessors
							.user("testuser")
							.roles("USER", "REST")
					)
			)

			.andExpect(MockMvcResultMatchers
					.status()
					.isNotFound()
			);
			
	}

}
