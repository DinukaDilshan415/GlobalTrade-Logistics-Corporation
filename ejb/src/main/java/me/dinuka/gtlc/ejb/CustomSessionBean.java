package me.dinuka.gtlc.ejb;

import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.EntityPart;
import me.dinuka.gtlc.dto.CustomDocDTO;
import me.dinuka.gtlc.entity.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class CustomSessionBean {
    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    Gson gson = new Gson();

    @Resource(lookup = "storage/customDocs")
    private String UPLOAD_DIR;

    public String getAllCases() {
        List<CustomsCase> customsCases = em.createNamedQuery("CustomsCase.findAllInOrder", CustomsCase.class)
                .getResultList();

        if (customsCases.isEmpty()) {
            return gson.toJson(Map.of(
                    "status", false,
                    "message", "No Customs Cases Found"
            ));
        }

        ArrayList<Map<String, Object>> customsCaseList = new ArrayList<>();
        for (CustomsCase customsCase : customsCases) {
            HashMap<String, Object> customsCaseMap = new HashMap<>();
            customsCaseMap.put("id", customsCase.getId());
            customsCaseMap.put("caseNumber", customsCase.getCaseNumber());
            customsCaseMap.put("shipmentId", customsCase.getShipment().getShipmentIdString());
            customsCaseMap.put("customsValue", customsCase.getCustomsValue());
            customsCaseMap.put("dutyAmount", customsCase.getEstimatedDuty());
            customsCaseMap.put("riskLevel", customsCase.getRiskLevel());
            customsCaseMap.put("submittedDate", customsCase.getSubmittedAt()
                    .format(java.time.format.DateTimeFormatter
                            .ofPattern("MMM dd, yyyy", java.util.Locale.ENGLISH)));
            customsCaseMap.put("deadline", customsCase.getDeadline()
                    .format(DateTimeFormatter
                            .ofPattern("MMM dd, yyyy HH:mm:ss", java.util.Locale.ENGLISH)));
            if (customsCase.getCustomStatus().getStatus().equals("CLEARED")) {
                customsCaseMap.put("clearedDate", customsCase.getSubmittedAt());
            } else {
                customsCaseMap.put("clearedDate", null);
            }
            customsCaseMap.put("assignedOfficer", customsCase.getCustomAgent() != null ? customsCase.getCustomAgent().getName() : "Not Assigned");
            customsCaseMap.put("remarks", customsCase.getRemarks());
            customsCaseMap.put("status", customsCase.getCustomStatus().getStatus());
            customsCaseList.add(customsCaseMap);
        }
        return gson.toJson(Map.of(
                "status", true,
                "data", customsCaseList
        ));
    }

    public String submitDocuments(CustomDocDTO dto) {
        String caseId = dto.getCaseId();
        String caseNumber = dto.getCaseNumber();
        EntityPart commercialInvoice = dto.getCommercialInvoice();
        EntityPart certOfOrigin = dto.getCertOfOrigin();
        EntityPart permit = dto.getPermit();
        EntityPart insuranceCert = dto.getInsuranceCert();
        EntityPart customsDeclaration = dto.getCustomsDeclaration();
        EntityPart otherDocs = dto.getOtherDocs();

        try {
            String saved = saveCaseFiles(caseId, caseNumber, commercialInvoice, certOfOrigin, permit, insuranceCert, customsDeclaration, otherDocs);

            if (saved.equals("success")) {
                return gson.toJson(Map.of(
                        "status", true,
                        "message", "Documents Uploaded Successfully"
                ));
            } else {
                return gson.toJson(Map.of(
                        "status", false,
                        "message", saved
                ));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String saveCaseFiles(String caseId, String caseNumber, EntityPart... parts) throws IOException {

        if (UPLOAD_DIR == null || UPLOAD_DIR.trim().isEmpty()) {
            throw new IllegalStateException("Payara JNDI configuration 'storage/customDocs' is missing or empty.");
        }

        Path caseFolderPath = Paths.get(UPLOAD_DIR, caseId);

        if (!Files.exists(caseFolderPath)) {
            Files.createDirectories(caseFolderPath);
        }

        CustomsCase customsCase = em.createNamedQuery("CustomsCase.findById", CustomsCase.class)
                .setParameter("id", Integer.parseInt(caseId))
                .getSingleResult();

        CustomStatus customStatus = em.createNamedQuery("CustomStatus.findByStatus", CustomStatus.class)
                .setParameter("status", "SUBMITTED")
                .getSingleResult();

        for (EntityPart part : parts) {
            if (part != null && part.getFileName().isPresent()) {
                String originalFileName = part.getFileName().get().toLowerCase();
                String contentType = part.getMediaType() != null ? part.getMediaType().toString() : "";

                if (!originalFileName.endsWith(".pdf") || !contentType.equalsIgnoreCase("application/pdf")) {
                    return part.getFileName().orElse("unknown.pdf") + " is Invalid file type. Only PDF files are allowed.";
                }

                String fileName = caseNumber + "-" + part.getName() + ".pdf";
                Path targetFileLocation = caseFolderPath.resolve(fileName);

                try (InputStream fileStream = part.getContent()) {
                    Files.copy(fileStream, targetFileLocation, StandardCopyOption.REPLACE_EXISTING);
                }

                DocumentType documentType = null;
                if (part.getName().equalsIgnoreCase("commercialInvoice")) {
                    documentType = em.createNamedQuery("DocumentType.findByTypeId", DocumentType.class).setParameter("id", 1).getSingleResult();
                } else if (part.getName().equalsIgnoreCase("certOfOrigin")) {
                    documentType = em.createNamedQuery("DocumentType.findByTypeId", DocumentType.class).setParameter("id", 2).getSingleResult();
                } else if (part.getName().equalsIgnoreCase("permit")) {
                    documentType = em.createNamedQuery("DocumentType.findByTypeId", DocumentType.class).setParameter("id", 3).getSingleResult();
                } else if (part.getName().equalsIgnoreCase("insuranceCert")) {
                    documentType = em.createNamedQuery("DocumentType.findByTypeId", DocumentType.class).setParameter("id", 4).getSingleResult();
                } else if (part.getName().equalsIgnoreCase("customsDeclaration")) {
                    documentType = em.createNamedQuery("DocumentType.findByTypeId", DocumentType.class).setParameter("id", 5).getSingleResult();
                } else if (part.getName().equalsIgnoreCase("otherDocs")) {
                    documentType = em.createNamedQuery("DocumentType.findByTypeId", DocumentType.class).setParameter("id", 6).getSingleResult();
                }

                CustomsDocument customsDocument = new CustomsDocument();
                customsDocument.setCustomsCase(customsCase);
                customsDocument.setDocumentType(documentType);
                customsDocument.setFilePath(fileName);
                customsDocument.setUploadedAt(java.time.LocalDateTime.now());
                em.merge(customsDocument);
            }
        }

        customsCase.setCustomStatus(customStatus);
        em.merge(customsCase);

        return "success";
    }

    public String getCaseDocuments(String caseId) {
        CustomsCase customsCase = em.createNamedQuery("CustomsCase.findById", CustomsCase.class)
                .setParameter("id", Integer.valueOf(caseId))
                .getSingleResult();

        HashMap<String, String> caseDocuments = new HashMap<>();
        caseDocuments.put("commercialInvoice", "/custom-document/" + caseId + "/" + customsCase.getCaseNumber() + "-commercialInvoice.pdf");
        caseDocuments.put("certOfOrigin", "/custom-document/" + caseId + "/" + customsCase.getCaseNumber() + "-certOfOrigin.pdf");
        caseDocuments.put("permit", "/custom-document/" + caseId + "/" + customsCase.getCaseNumber() + "-permit.pdf");
        caseDocuments.put("insuranceCert", "/custom-document/" + caseId + "/" + customsCase.getCaseNumber() + "-insuranceCert.pdf");
        caseDocuments.put("customsDeclaration", "/custom-document/" + caseId + "/" + customsCase.getCaseNumber() + "-customsDeclaration.pdf");
        caseDocuments.put("otherDocs", "/custom-document/" + caseId + "/" + customsCase.getCaseNumber() + "-otherDocs.pdf");

        return gson.toJson(caseDocuments);
    }

    public String getReviewCases(String email) {
        User user = em.createNamedQuery("User.findByEmail", User.class).setParameter("email", email).getSingleResult();

        List<CustomAgent> customAgents = em.createNamedQuery("CustomAgent.findByUser", CustomAgent.class).setParameter("user", user).getResultList();

        if (customAgents.isEmpty()) {
            return gson.toJson(Map.of(
                    "status", false,
                    "message", "You are not assigned to any Customs Agent"
            ));
        } else {
            CustomAgent customAgent = customAgents.get(0);

            List<Shipment> shipmentList = em.createNamedQuery("Shipment.findByOriginCountry", Shipment.class)
                    .setParameter("originCountry", customAgent.getCountry())
                    .getResultList();

            if (!shipmentList.isEmpty()) {
                List<CustomsCase> customsCases = em.createNamedQuery("CustomsCase.findByShipments", CustomsCase.class)
                        .setParameter("shipments", shipmentList)
                        .getResultList();

                ArrayList<Map<String, Object>> customsCaseList = new ArrayList<>();

                for (CustomsCase customsCase : customsCases) {
                    HashMap<String, Object> customsCaseMap = new HashMap<>();
                    customsCaseMap.put("id", customsCase.getId());
                    customsCaseMap.put("caseNumber", customsCase.getCaseNumber());
                    customsCaseMap.put("shipmentId", customsCase.getShipment().getShipmentIdString());
                    customsCaseMap.put("weight", customsCase.getShipment().getWeight() + " kg");
                    customsCaseMap.put("itemDescription", customsCase.getShipment().getCarrier() + "/" + customsCase.getShipment().getDescription());
                    customsCaseMap.put("customsValue", customsCase.getCustomsValue());
                    customsCaseMap.put("dutyAmount", customsCase.getEstimatedDuty());
                    customsCaseMap.put("riskLevel", customsCase.getRiskLevel().equals("-") ? "UNASSIGNED" : customsCase.getRiskLevel());
                    customsCaseMap.put("deadline", customsCase.getDeadline()
                            .format(DateTimeFormatter
                                    .ofPattern("MMM dd, yyyy HH:mm:ss", java.util.Locale.ENGLISH)));
                    customsCaseMap.put("remarks", customsCase.getRemarks());
                    customsCaseMap.put("status", customsCase.getCustomStatus().getStatus());

                    if (!customsCase.getCustomStatus().getStatus().equals("DOCUMENTS_REQUIRED")) {
                        customsCaseList.add(customsCaseMap);
                    }
                }

                if (!customsCaseList.isEmpty()) {
                    return gson.toJson(Map.of(
                            "status", true,
                            "data", customsCaseList
                    ));
                }
            }
        }
        return gson.toJson(Map.of(
                "status", false,
                "message", "No Customs Cases Found"
        ));
    }

    public String updateCaseStatus(String caseId, String status) {
        CustomsCase customsCase = em.createNamedQuery("CustomsCase.findById", CustomsCase.class)
                .setParameter("id", Integer.valueOf(caseId))
                .getSingleResult();

        CustomStatus customStatus = em.createNamedQuery("CustomStatus.findByStatus", CustomStatus.class)
                .setParameter("status", status)
                .getSingleResult();

        customsCase.setCustomStatus(customStatus);
        em.merge(customsCase);

        return gson.toJson(Map.of(
                "message", "Case Status Updated Successfully"
        ));
    }
}
