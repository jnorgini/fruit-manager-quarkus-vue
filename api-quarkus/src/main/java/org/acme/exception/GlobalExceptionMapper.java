package org.acme.exception;

import java.util.Map;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

	@Override
	public Response toResponse(ResourceNotFoundException exception) {
		return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(Map.of("status",
				Response.Status.NOT_FOUND.getStatusCode(), "error", "Not Found", "message", exception.getMessage()))
				.build();
	}

}
