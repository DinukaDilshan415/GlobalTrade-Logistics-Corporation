package me.dinuka.gtlc.exception;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import me.dinuka.gtlc.dto.ErrorResponse;

@Provider
public class InvalidAccessRoleExceptionMapper implements ExceptionMapper<ForbiddenException> {
    @Override
    public Response toResponse(ForbiddenException e) {
        System.out.println(e.getMessage());

        ErrorResponse body = ErrorResponse.of(
                "forbidden",
                e.getMessage(),
                Response.Status.FORBIDDEN.getStatusCode()
        );

        return Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON)
                .entity(body).build();
    }
}
