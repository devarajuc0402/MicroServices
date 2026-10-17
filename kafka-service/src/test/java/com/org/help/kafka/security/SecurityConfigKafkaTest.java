package com.org.help.kafka.security;

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
public class SecurityConfigKafkaTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void getSuccessMessage() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/kafka/message")
					.with(SecurityMockMvcRequestPostProcessors
							.jwt()
							.authorities(new SimpleGrantedAuthority("ROLE_KAFKA"))
					)
			)
			
			.andDo(MockMvcResultHandlers.print())
			
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void getConfigSuccessMessage() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/kafka/config-kafka-test")
					.with(SecurityMockMvcRequestPostProcessors
							.jwt()
							.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
					)
			)
			
			.andDo(MockMvcResultHandlers.print())
			
			.andExpect(MockMvcResultMatchers
					.status().isOk()
			);
	}
	
	@Test
	public void get401Error() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/kafka/config-kafka-test")
			)
			
			.andDo(MockMvcResultHandlers.print())
			
			.andExpect(MockMvcResultMatchers
					.status().isUnauthorized()
			);
	}
	
	@Test
	public void get404NotFoundError() throws Exception {
		
		mockMvc
			.perform(MockMvcRequestBuilders
					.get("/api/kafka/NOT_FOUND")
					.with(SecurityMockMvcRequestPostProcessors
							.jwt()
							.authorities(new SimpleGrantedAuthority("ROLE_KAFKA"))
					)
			)
			
			.andDo(MockMvcResultHandlers.print())
			
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
}
