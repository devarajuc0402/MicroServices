package com.org.help.restclient.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import com.org.help.restclient.config.JwtTokenRoleConverter;

public class JwtTokenRoleConverterTest {
	
	private final JwtTokenRoleConverter jwtTokenRoleConverter = 
			new JwtTokenRoleConverter();
	
	@Test
	public void convertFromRolesTest() {
		
		Jwt jwt = Jwt.withTokenValue("test-token")
				.header("alg", "RS256")
				.claim("realm_access", 
						Map.of("roles", List.of("USER", "REST"))
				)
				.build();
		
		JwtAuthenticationConverter converter = jwtTokenRoleConverter.jwtAuthenticationConverter();
		
		AbstractAuthenticationToken authentication = converter.convert(jwt);
		
		assertThat(authentication).isNotNull();
		
		assertThat(authentication.getAuthorities())
			.extracting(GrantedAuthority::getAuthority)
			.containsExactlyInAnyOrder(
					"ROLE_USER",
					"ROLE_REST",
					"FACTOR_BEARER"
			);
	}
}
