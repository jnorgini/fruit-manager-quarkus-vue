package org.acme.fruit.service;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.fruit.domain.Role;
import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class TokenService {

	public String generateToken(String username, Set<Role> roles) {

		Set<String> stringRoles = roles.stream()
				.map(Role::name)
				.collect(Collectors.toSet());

		return Jwt.issuer("https://quarkus-api-frutas.com")
				.upn(username)
				.groups(stringRoles)
				.expiresIn(Duration.ofHours(2))
				//.expiresIn(Duration.ofMinutes(1)) test
				.sign();
	}
	
}
