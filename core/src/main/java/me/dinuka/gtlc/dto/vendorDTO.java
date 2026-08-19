package me.dinuka.gtlc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class vendorDTO {
    private String vendorId;
    private String companyName;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private String country;
    private String registrationNumber;
    private String status;
    private String complianceInfo;

}
