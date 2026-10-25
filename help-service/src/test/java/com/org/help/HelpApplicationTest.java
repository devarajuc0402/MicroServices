package com.org.help;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class HelpApplicationTest {

	@MockitoBean
    private JwtDecoder jwtDecoder;
	
	@Test
	public void contextLoad() {
		System.out.println(jwtDecoder);
	}
}
