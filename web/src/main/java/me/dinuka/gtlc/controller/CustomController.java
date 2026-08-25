package me.dinuka.gtlc.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.EntityPart;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.dto.CustomDocDTO;
import me.dinuka.gtlc.security.SecurityConstants;
import me.dinuka.gtlc.service.CustomService;

@Path("/custom")
public class CustomController {

    @Inject
    private CustomService customService;

    @GET
    @RolesAllowed(SecurityConstants.ROLE_ADMIN)
    @Path("/getAllCases")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCases(@HeaderParam("Authorization") String authHeader) {
        return customService.getAllCases(authHeader);
    }

    @POST
    @RolesAllowed(SecurityConstants.ROLE_ADMIN)
    @Path("/submitDocuments")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response submitDocuments(
            @HeaderParam("Authorization") String authHeader,
            @FormParam("caseId") String caseId,
            @FormParam("caseNumber") String caseNumber,
            @FormParam("commercialInvoice") EntityPart commercialInvoice,
            @FormParam("certOfOrigin") EntityPart certOfOrigin,
            @FormParam("permit") EntityPart permit,
            @FormParam("insuranceCert") EntityPart insuranceCert,
            @FormParam("customsDeclaration") EntityPart customsDeclaration,
            @FormParam("otherDocs") EntityPart otherDocs
    ) {
        CustomDocDTO customDocDTO = new CustomDocDTO();
        customDocDTO.setCaseId(caseId);
        customDocDTO.setCaseNumber(caseNumber);
        customDocDTO.setCommercialInvoice(commercialInvoice);
        customDocDTO.setCertOfOrigin(certOfOrigin);
        customDocDTO.setPermit(permit);
        customDocDTO.setInsuranceCert(insuranceCert);
        customDocDTO.setCustomsDeclaration(customsDeclaration);
        customDocDTO.setOtherDocs(otherDocs);

        return customService.submitDocuments(authHeader, customDocDTO);
    }
}
