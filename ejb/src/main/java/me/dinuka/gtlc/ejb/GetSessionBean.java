package me.dinuka.gtlc.ejb;

import com.google.gson.Gson;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.gtlc.entity.*;

import java.util.ArrayList;
import java.util.HashMap;
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

    public String getWarehouseProducts(String warehouseId){
        Warehouse warehouse = em.createNamedQuery("Warehouse.findById", Warehouse.class)
                .setParameter("id", warehouseId)
                .getSingleResult();

        List<Inventory> inventoryList = em.createNamedQuery("Inventory.findByWarehouseWithValidQty", Inventory.class)
                .setParameter("warehouse", warehouse)
                .getResultList();

        if(inventoryList.isEmpty()){
            return null;
        } else {
            ArrayList<Map<String, Object>> inventoryMap = new ArrayList<>();
            for(Inventory inventory : inventoryList){
                Map<String, Object> inventoryMap1 = Map.of(
                        "id", inventory.getId(),
                        "name", inventory.getProductName(),
                        "hsCode", inventory.getHsCode(),
                        "availableQty", inventory.getQuantity(),
                        "unitValue", inventory.getUnitValue(),
                        "warehouseId", inventory.getWarehouse().getId()
                );
                inventoryMap.add(inventoryMap1);
            }

            return gson.toJson(inventoryMap);
        }
    }

    public String userReferenceData(){
        List<Country> countries = em.createNamedQuery("Country.findAll", Country.class).getResultList();

        List<UserStatus> statusList = em.createNamedQuery("UserStatus.findAll", UserStatus.class).getResultList();

        ArrayList<String> statuses = new ArrayList<>();

        for(UserStatus status : statusList){
            statuses.add(status.getStatus());
        }

        List<Roles> rolesList = em.createNamedQuery("Roles.findAll", Roles.class).getResultList();

        ArrayList<HashMap<String, Object>> roleList = new ArrayList<>();

        for(Roles role : rolesList){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", role.getId());
            map.put("name", role.getRole());
            if(role.getRole().equals("logistics coordinator")){
                map.put("fields", List.of(
                        Map.of(
                                "name", "country",
                                "label", "Operating Region",
                                "type", "country_select"
                        )
                ));
            } else if(role.getRole().equals("customs agent")){
                map.put("fields", List.of(
                        Map.of(
                                "name", "position",
                                "label", "Position / Title",
                                "type", "text"
                        ),
                        Map.of(
                                "name", "reg_number",
                                "label", "Registration Number",
                                "type", "text"
                        ),
                        Map.of(
                                "name", "country",
                                "label", "Assigned Country",
                                "type", "country_select"
                        )
                ));
            } else if(role.getRole().equals("vendor manager")){
                map.put("fields", List.of(
                        Map.of(
                                "name", "country",
                                "label", "Managed Region",
                                "type", "country_select"
                        ))
                );
            } else {
                map.put("fields", List.of());
            }
            roleList.add(map);
        }

        HashMap<String, Object> userReferenceData = new HashMap<>();

        userReferenceData.put("countries", countries);
        userReferenceData.put("statuses", statuses);
        userReferenceData.put("roles", roleList);

        return gson.toJson(userReferenceData);
    }

}
