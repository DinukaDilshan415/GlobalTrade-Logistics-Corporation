package me.dinuka.gtlc.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import me.dinuka.gtlc.dto.ErrorResponse;

import java.util.Map;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    @Override
    public Response toResponse(ValidationException e) {
        System.out.println(e.getMessage());

        ErrorResponse body = ErrorResponse.of(
                "bad request",
                e.getMessage(),
                Response.Status.BAD_REQUEST.getStatusCode()
                );

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(body)
                .build();
    }
}
