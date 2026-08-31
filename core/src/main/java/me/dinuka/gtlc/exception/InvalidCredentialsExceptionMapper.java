package me.dinuka.gtlc.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import me.dinuka.gtlc.dto.ErrorResponse;

@Provider
public class InvalidCredentialsExceptionMapper implements ExceptionMapper<InvalidCredentialsException> {
    @Override
    public Response toResponse(InvalidCredentialsException exception) {
        System.out.println(exception.getMessage());

        ErrorResponse body = ErrorResponse.of(
                "unauthorized",
                exception.getMessage(),
                Response.Status.UNAUTHORIZED.getStatusCode()
        );

        return Response.status(Response.Status.UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON)
                .entity(body).build();
    }
}
