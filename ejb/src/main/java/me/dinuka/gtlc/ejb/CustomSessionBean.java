package me.dinuka.gtlc.ejb;

import com.google.gson.Gson;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.entity.CustomsCase;

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

    public String getAllCases(){
        List<CustomsCase> customsCases = em.createNamedQuery("CustomsCase.findAllInOrder", CustomsCase.class)
                .getResultList();

        if(customsCases.isEmpty()){
            return gson.toJson(Map.of(
                    "status", false,
                    "message", "No Customs Cases Found"
            ));
        }

        ArrayList<Map<String, Object>> customsCaseList = new ArrayList<>();
        for(CustomsCase customsCase : customsCases){
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
            if (customsCase.getCustomStatus().getStatus().equals("CLEARED")){
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
}
