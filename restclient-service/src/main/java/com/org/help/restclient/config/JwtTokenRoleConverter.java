package com.org.help.restclient.config;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class JwtTokenRoleConverter {

	@Bean
	public JwtAuthenticationConverter JwtAuthenticationConverter() {
		
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		
		converter.setJwtGrantedAuthoritiesConverter(jwt -> {
			
			Map<String, List<String>> realmAccess = jwt.getClaim("realm_access");
			
			System.out.println("KEYCLOAK realm_access = " + realmAccess);
			
			if(realmAccess == null) {
				return Collections.emptyList();
			}
			
			List<String> roles = realmAccess.get("roles");
			
			System.out.println("KEYCLOAK ROLES = " + roles);
			
			if(roles == null) {
				return Collections.emptyList();
			}
			
			return roles.stream()
					.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
					.collect(Collectors.toList());
		});
		
		
		
		return converter;
	}
}
