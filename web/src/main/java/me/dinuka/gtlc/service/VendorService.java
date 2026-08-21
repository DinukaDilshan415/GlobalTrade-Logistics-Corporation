package me.dinuka.gtlc.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.dto.vendorDTO;
import me.dinuka.gtlc.ejb.VendorSessionBean;
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
                    return Response.status(Response.Status.BAD_REQUEST).entity(
                            Map.of("message", "Vendor ID is required")
                    ).build();
                } else if (companyName.isEmpty()){
                    return Response.status(Response.Status.BAD_REQUEST).entity(
                            Map.of("message", "Company Name is required")
                    ).build();
                } else if (contactPerson.isEmpty()) {
                    return Response.status(Response.Status.BAD_REQUEST).entity(
                            Map.of("message", "Contact Person is required")
                    ).build();
                } else if (email.isEmpty()) {
                    return Response.status(Response.Status.BAD_REQUEST).entity(
                            Map.of("message", "Email is required")
                    ).build();
                } else if (phone.isEmpty()) {
                    return Response.status(Response.Status.BAD_REQUEST).entity(
                            Map.of("message", "Phone number is required")
                    ).build();
                } else if (!RegexValidator.isValidSlPhone(phone)) {
                    return Response.status(Response.Status.BAD_REQUEST).entity(
                            Map.of("message", "Enter a valid phone number")
                    ).build();
                }else if (address.isEmpty()) {
                    return Response.status(Response.Status.BAD_REQUEST).entity(
                            Map.of("message", "Address is required")
                    ).build();
                } else if (registrationNumber.isEmpty()) {
                    return Response.status(Response.Status.BAD_REQUEST).entity(
                            Map.of("message", "Registration Number is required")
                    ).build();
                } else if (Integer.parseInt(countryId) <= 0) {
                    return Response.status(Response.Status.BAD_REQUEST).entity(
                            Map.of("message", "Country is required")
                    ).build();
                } else if (complianceInfo.isEmpty()) {
                    return Response.status(Response.Status.BAD_REQUEST).entity(
                            Map.of("message", "Compliance Information is required")
                    ).build();
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
