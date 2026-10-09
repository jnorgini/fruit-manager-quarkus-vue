package org.acme.fruit.resource;

import java.util.List;
import java.util.Set;

import org.acme.fruit.domain.Role;
import org.acme.fruit.domain.User;
import org.acme.fruit.dto.AuthDTO;
import org.acme.fruit.dto.UserRegisterDTO;
import org.acme.fruit.repository.UserRepository;
import org.acme.fruit.service.TokenService;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

	@Inject
	TokenService tokenService;

	@Inject
	UserRepository userRepository;

	@POST
	@Path("/register")
	@Transactional
	public Response register(@Valid UserRegisterDTO registerRequest) {
		if (userRepository.findByUsername(registerRequest.username) != null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Este nome de usuário já está cadastrado.")
					.build();
		}

		User newUser = new User();
		newUser.username = registerRequest.username;
		newUser.password = BcryptUtil.bcryptHash(registerRequest.password);

		if (registerRequest.roles != null && !registerRequest.roles.isEmpty()) {
			newUser.roles = registerRequest.roles;
		} else {
			newUser.roles = Set.of(Role.USER);
		}

		userRepository.persist(newUser);
		return Response.status(Response.Status.CREATED).entity("Usuário criado com sucesso!").build();
	}

	@POST
	@Path("/login")
	public Response login(@Valid AuthDTO loginRequest) {
		User user = userRepository.findByUsername(loginRequest.username);

		if (user != null && BcryptUtil.matches(loginRequest.password, user.password)) {
			String token = tokenService.generateToken(user.username, user.roles);
			return Response.ok(new TokenResponse(token)).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).entity("Usuário ou senha inválidos.").build();
	}

	public static class TokenResponse {
		public String token;

		public TokenResponse(String token) {
			this.token = token;
		}
	}

	@GET
	public Response listUsers() {
		List<User> users = userRepository.listAll();
		return Response.ok(users).build();
	}

	@GET
	@Path("/{id}")
	public Response getById(@PathParam("id") Long id) {
		User user = userRepository.findById(id);
		return Response.ok(user).build();
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public Response deleteUser(@PathParam("id") Long id) {
		User user = userRepository.findById(id);
		userRepository.delete(user);
		return Response.noContent().build();
	}

}
