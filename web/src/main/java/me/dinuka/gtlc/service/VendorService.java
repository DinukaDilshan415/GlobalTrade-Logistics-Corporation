package me.dinuka.gtlc.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.dto.vendorDTO;
import me.dinuka.gtlc.ejb.VendorSessionBean;
import me.dinuka.gtlc.exception.ValidationException;
import me.dinuka.gtlc.util.JwtUtil;
import me.dinuka.gtlc.util.RegexValidator;

import java.util.Map;

@RequestScoped
public class VendorService {

    @Inject
    private VendorSessionBean vendorSessionBean;

    public Response getVendorProfile(String authHeader){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Validate token
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);

                String email = jwt.getSubject();

                String checker = vendorSessionBean.vendorProfileChecker(email);

                return Response.status(Response.Status.OK)
                        .entity(checker)
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
    
    public Response getShipments(String authHeader){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String userEmail = jwt.getSubject();

                String shipments = vendorSessionBean.getShipments(userEmail);

                return Response.status(Response.Status.OK)
                        .entity(shipments)
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

    public Response getAllVendors(String authHeader){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String userEmail = jwt.getSubject();

                String vendors = vendorSessionBean.getAllVendors();

                return Response.status(Response.Status.OK)
                        .entity(vendors)
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

    public Response updateStatus(String authHeader, Map<String, String> body){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String userEmail = jwt.getSubject();

                String vendorId = body.get("vendorId");
                String newStatus = body.get("newStatus");

                String updated = vendorSessionBean.updateVendorStatus(vendorId, newStatus);

                return Response.status(Response.Status.OK)
                        .entity(Map.of("message", updated))
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

    public Response saveVendorAccountOpenRequest(String authHeader, vendorDTO dto){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String userEmail = jwt.getSubject();

                String vendorId = dto.getVendorId();
                String companyName = dto.getCompanyName();
                String contactPerson = dto.getContactPerson();
                String email = dto.getEmail();
                String phone = dto.getPhone();
                String address = dto.getAddress();
                String registrationNumber = dto.getRegistrationNumber();
                String countryId = dto.getCountry();
                String complianceInfo = dto.getComplianceInfo();

                if (vendorId.isEmpty()){
                    throw new ValidationException("Vendor ID is required");
                } else if (companyName.isEmpty()){
                    throw new ValidationException("Company Name is required");
                } else if (contactPerson.isEmpty()) {
                    throw new ValidationException("Contact Person is required");
                } else if (email.isEmpty()) {
                    throw new ValidationException("Email is required");
                } else if (phone.isEmpty()) {
                    throw new ValidationException("Phone is required");
                } else if (!RegexValidator.isValidSlPhone(phone)) {
                    throw new ValidationException("Enter a valid phone number");
                }else if (address.isEmpty()) {
                    throw new ValidationException("Address is required");
                } else if (registrationNumber.isEmpty()) {
                    throw new ValidationException("Registration Number is required");
                } else if (Integer.parseInt(countryId) <= 0) {
                    throw new ValidationException("Country is required");
                } else if (complianceInfo.isEmpty()) {
                    throw new ValidationException("Compliance Information is required");
                } else {
                    String saved = vendorSessionBean.saveVendorAccountOpenRequest(userEmail, dto);

                    if(saved.equals("success")){
                        return Response.status(Response.Status.OK).entity(
                                Map.of("message", "Account Open Request Submitted Successfully")
                        ).build();
                    } else {
                        return Response.status(Response.Status.BAD_REQUEST).entity(
                                Map.of("message", saved)
                        ).build();
                    }

                }
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

    public Response saveShipment(String authHeader, Map<String, Object> body){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (JwtUtil.isValid(token)) {
                DecodedJWT jwt = JwtUtil.parseToken(token);
                String userEmail = jwt.getSubject();

                String savedShipment = vendorSessionBean.saveShipment(userEmail, body);

                return Response.status(Response.Status.OK)
                        .entity(savedShipment)
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
