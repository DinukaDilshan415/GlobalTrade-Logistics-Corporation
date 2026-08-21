package me.dinuka.gtlc.ejb;

import com.google.gson.Gson;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.entity.Country;
import me.dinuka.gtlc.entity.Warehouse;

import java.util.List;
import java.util.Map;

@Stateless
public class GetSessionBean {
    Gson gson = new Gson();

    @PersistenceContext(unitName = "gtlcPU")
    private EntityManager em;

    public List<Country> getCountries(){
        return em.createNamedQuery("Country.findAll", Country.class).getResultList();
    }

    public String getCountriesWithWarehouses(){
        List<Country> countries = em.createNamedQuery("Country.findAll", Country.class).getResultList();

        List<Country> countryList = em.createNamedQuery("Warehouse.findDistinctCountries", Country.class).getResultList();

        List<Warehouse> warehouses = em.createNamedQuery("Warehouse.findAll", Warehouse.class).getResultList();

        return gson.toJson(Map.of(
                "countries", countries,
                "warehouseCountries", countryList,
                "warehouses", warehouses));
    }

}
