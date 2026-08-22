package me.dinuka.gtlc.service;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import me.dinuka.gtlc.ejb.GetSessionBean;
import me.dinuka.gtlc.entity.Country;

import java.util.List;

@RequestScoped
public class GetService {

    @Inject
    private GetSessionBean getSessionBean;

    public Response getCountries(){
        List<Country> countries = getSessionBean.getCountries();

        if(countries.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.status(Response.Status.OK)
                    .entity(countries).build();
        }
    }

    public Response getCountriesWithWarehouses(){
        String countriesWithWarehouses = getSessionBean.getCountriesWithWarehouses();

        if(countriesWithWarehouses == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.status(Response.Status.OK)
                    .entity(countriesWithWarehouses).build();
        }
    }

    public Response warehouseProducts(String warehouseId){
        String warehouseProducts = getSessionBean.getWarehouseProducts(warehouseId);

        if(warehouseProducts == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.status(Response.Status.OK)
                    .entity(warehouseProducts)
                    .build();
        }
    }
}
