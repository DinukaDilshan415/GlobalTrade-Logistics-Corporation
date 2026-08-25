package me.dinuka.gtlc.dto;

import jakarta.ws.rs.core.EntityPart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomDocDTO {
    private String caseId;
    private String caseNumber;
    private EntityPart commercialInvoice;
    private EntityPart certOfOrigin;
    private EntityPart permit;
    private EntityPart insuranceCert;
    private EntityPart customsDeclaration;
    private EntityPart otherDocs;
}
