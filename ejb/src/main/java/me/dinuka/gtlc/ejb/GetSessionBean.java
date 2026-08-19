package me.dinuka.gtlc.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.entity.Country;

import java.util.List;

@Stateless
public class GetSessionBean {

    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    public List<Country> getCountries(){
        return em.createNamedQuery("Country.findAll", Country.class).getResultList();
    }

}
