package me.dinuka.gtlc.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.dto.CustomDocDTO;
import me.dinuka.gtlc.ejb.CustomSessionBean;
import me.dinuka.gtlc.util.JwtUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
public class CustomService {

    @Inject
    private CustomSessionBean customSessionBean;

    @Resource(lookup = "storage/customDocs")
    private String UPLOAD_DIR;

    public Response getAllCases(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();

                String allCases = customSessionBean.getAllCases();

                return Response.status(Response.Status.OK)
                        .entity(allCases)
                        .build();
            } else {
                System.out.println("Invalid or expired token:");
                return Response.status(Response.Status.UNAUTHORIZED).entity(
                        Map.of("message", "Invalid or expired token")
                ).build();
            }
        } else {
            System.out.println("Authorization header is missing:");
            return Response.status(Response.Status.UNAUTHORIZED).entity(
                    Map.of("message", "Authorization header is missing")
            ).build();
        }
    }

    public Response submitDocuments(String authHeader, CustomDocDTO dto){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();

                String submitted = customSessionBean.submitDocuments(dto);

                return Response.status(Response.Status.OK)
                        .entity(submitted)
                        .build();
            } else {
                System.out.println("Invalid or expired token:");
                return Response.status(Response.Status.UNAUTHORIZED).entity(
                        Map.of("message", "Invalid or expired token")
                ).build();
            }
        } else {
            System.out.println("Authorization header is missing:");
            return Response.status(Response.Status.UNAUTHORIZED).entity(
                    Map.of("message", "Authorization header is missing")
            ).build();
        }
    }

    public Response getDocuments(String authHeader, String caseId){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();

                String caseDocuments = customSessionBean.getCaseDocuments(caseId);

                return Response.status(Response.Status.OK)
                        .entity(caseDocuments)
                        .build();
            } else {
                System.out.println("Invalid or expired token:");
                return Response.status(Response.Status.UNAUTHORIZED).entity(
                        Map.of("message", "Invalid or expired token")
                ).build();
            }
        } else {
            System.out.println("Authorization header is missing:");
            return Response.status(Response.Status.UNAUTHORIZED).entity(
                    Map.of("message", "Authorization header is missing")
            ).build();
        }
    }

    public File getDoc(String caseId, String fileName) throws FileNotFoundException{

        if (UPLOAD_DIR == null || UPLOAD_DIR.trim().isEmpty()) {
            throw new IllegalStateException("Payara storage path is unconfigured.");
        }

        Path filePath = Paths.get(UPLOAD_DIR, caseId, fileName);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new FileNotFoundException("Requested document not found for case directory: " + caseId);
        }

        return file;
    }

    public Response getReviewCases(String authHeader){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();

                String reviewCases = customSessionBean.getReviewCases(username);

                return Response.status(Response.Status.OK)
                        .entity(reviewCases)
                        .build();
            } else {
                System.out.println("Invalid or expired token:");
                return Response.status(Response.Status.UNAUTHORIZED).entity(
                        Map.of("message", "Invalid or expired token")
                ).build();
            }
        } else {
            System.out.println("Authorization header is missing:");
            return Response.status(Response.Status.UNAUTHORIZED).entity(
                    Map.of("message", "Authorization header is missing")
            ).build();
        }
    }

    public Response updateCaseStatus(String authHeader, Map<String, String> body){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String username = jwt.getSubject();

                String caseId = body.get("caseId");
                String status = body.get("newStatus");

                String updated = customSessionBean.updateCaseStatus(caseId, status);

                return Response.status(Response.Status.OK)
                        .entity(updated)
                        .build();
            } else {
                System.out.println("Invalid or expired token:");
                return Response.status(Response.Status.UNAUTHORIZED).entity(
                        Map.of("message", "Invalid or expired token")
                ).build();
            }
        } else {
            System.out.println("Authorization header is missing:");
            return Response.status(Response.Status.UNAUTHORIZED).entity(
                    Map.of("message", "Authorization header is missing")
            ).build();
        }
    }
}
