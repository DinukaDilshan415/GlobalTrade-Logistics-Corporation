package me.dinuka.gtlc.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.security.SecurityConstants;
import me.dinuka.gtlc.service.CustomService;

import java.io.File;
import java.io.FileNotFoundException;

@Path( "/custom-document")
public class CustomDocumentController {

    @Inject
    private CustomService customService;

    @GET
    @RolesAllowed({SecurityConstants.ROLE_ADMIN, SecurityConstants.CUSTOMS_AGENT})
    @Path("/{caseId}/{fileName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewDocument(
            @PathParam("caseId") String caseId,
            @PathParam("fileName") String fileName
    ) {
        try {

            File file = customService.getDoc(caseId, fileName);

            return Response.ok(file)
                    .header("Content-Disposition", "inline; filename=\"" + file.getName() + "\"")
                    .header("X-Content-Type-Options", "nosniff")
                    .build();
        } catch (FileNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Document not found\"}")
                    .build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity("{\"error\":\"Could not retrieve document\"}")
                    .build();
        }
    }
}
