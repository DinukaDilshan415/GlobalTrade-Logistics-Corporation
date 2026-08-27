package me.dinuka.gtlc.ejb.alerts;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.entity.CustomsCase;
import me.dinuka.gtlc.enums.AlertSeverity;
import me.dinuka.gtlc.enums.AlertType;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class CustomsService {
    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    @EJB
    private AlertService alertService;

    public void checkCustomsDeadlines(){
        List<CustomsCase> customsCases = em.createNamedQuery("CustomsCase.findAll", CustomsCase.class).getResultList();

        for(CustomsCase customsCase : customsCases){
            if (customsCase.getCustomStatus().getStatus().equals("SUBMITTED") || customsCase.getCustomStatus().getStatus().equals("UNDER_REVIEW")){
                if (customsCase.getDeadline().isBefore(LocalDateTime.now())){
                    checkDeadline(customsCase);
                }
            }
        }
    }

    private void checkDeadline(CustomsCase customsCase) {
        alertService.createAlert(
                AlertType.CUSTOMS_DEADLINE,
                AlertSeverity.NOTICE,
                "Customs case "+customsCase.getCaseNumber()+" requires attention. Deadline is "+customsCase.getDeadline()
                        .format(java.time.format.DateTimeFormatter
                        .ofPattern("MMM dd, yyyy HH:mm:ss", java.util.Locale.ENGLISH)),
                "CUSTOMS",
                customsCase.getCaseNumber()
        );
    }
}
