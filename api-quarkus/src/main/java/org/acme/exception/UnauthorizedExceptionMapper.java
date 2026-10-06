package org.acme.exception;

import java.util.Map;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import io.quarkus.security.UnauthorizedException;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {

	@Override
	public Response toResponse(UnauthorizedException exception) {
		return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON)
				.entity(Map.of("status", Response.Status.UNAUTHORIZED.getStatusCode(), "error", "Unauthorized",
						"message", "Credenciais de acesso ausentes, expiradas ou inválidas. Faça login novamente."))
				.build();
	}

}
