package org.acme.exception;

import java.util.Map;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import io.quarkus.security.ForbiddenException;

@Provider
public class SecurityExceptionMapper implements ExceptionMapper<ForbiddenException> {

	@Override
	public Response toResponse(ForbiddenException exception) {
		return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON)
				.entity(Map.of("status", Response.Status.FORBIDDEN.getStatusCode(), "error", "Forbidden", "message",
						"Você não tem nível de permissão suficiente para realizar esta ação."))
				.build();
	}

}
