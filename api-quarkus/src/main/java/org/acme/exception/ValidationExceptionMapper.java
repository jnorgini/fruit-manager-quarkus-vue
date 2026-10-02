package org.acme.exception;

import java.util.Map;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(ConstraintViolationException exception) {
		String errorMessage = exception.getConstraintViolations().iterator().next().getMessage();
		return Response
				.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(Map.of("status",
						Response.Status.BAD_REQUEST.getStatusCode(), "error", "Bad Request", "message", errorMessage))
				.build();
	}
	
}
