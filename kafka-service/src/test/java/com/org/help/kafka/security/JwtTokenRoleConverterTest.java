package com.org.help.kafka.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import com.org.help.kafka.config.JwtTokenRoleConverter;

@SpringBootTest
public class JwtTokenRoleConverterTest {

	
	private final JwtTokenRoleConverter jwtTokenRoleConverter = 
			new JwtTokenRoleConverter();
	
	@Test
	public void converterJwtTest() {
		
		Jwt jwt = Jwt.withTokenValue("test-token")
				.header("alg", "RS256")
				.claim("realm_access", 
						Map.of("roles", List.of("ADMIN", "KAFKA"))
				)
				.build();
		
		JwtAuthenticationConverter converter = jwtTokenRoleConverter.jwtAuthenticationConverter();

		AbstractAuthenticationToken authentication = converter.convert(jwt);

		assertThat(authentication.getAuthorities())
			.extracting(GrantedAuthority::getAuthority)
			.containsExactlyInAnyOrder(
					"ROLE_ADMIN",
					"ROLE_KAFKA",
					"FACTOR_BEARER"
			);
	}
	
}
