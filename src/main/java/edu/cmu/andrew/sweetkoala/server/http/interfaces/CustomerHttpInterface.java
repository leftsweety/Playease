package edu.cmu.andrew.sweetkoala.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.sweetkoala.server.http.responses.AppResponse;
import edu.cmu.andrew.sweetkoala.server.http.utils.PATCH;
import edu.cmu.andrew.sweetkoala.server.models.Customer;
import edu.cmu.andrew.sweetkoala.server.managers.CustomerManager;
import edu.cmu.andrew.sweetkoala.server.utils.*;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/customers")

public class CustomerHttpInterface extends HttpInterface {

    private ObjectWriter ow;
    private MongoCollection<Document> customerCollection = null;

    public CustomerHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Path("/reset")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse resetCustomers(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            CustomerManager.getInstance().resetCustomers();

            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST customers", e);
        }

    }


    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postCustomers(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            String id = null;
            String first_name = null;
            String last_name = null;
            String username = null;
            String password = null;
            String email = null;
            String phone = null;
            String preference_type_id = null;
            Integer coin;

            Customer newcustomer = new Customer(
                    null,
                    json.getString("first_name"),
                    json.getString("last_name"),
                    json.getString("username"),
                    json.getString("password"),
                    json.getString("email"),
                    json.getString("phone"),
                    json.getString("preference_type_id"),
                    json.getInt("coin")
            );

            CustomerManager.getInstance().createCustomer(newcustomer);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST customers", e);
        }

    }

    //Get List: http://localhost:8080/api/customers
    //Sorting: http://localhost:8080/api/customers?sortby=coin
    //Pagination: http://localhost:8080/api/customers?offset=1&count=1
    //Filter: http://localhost:8080/api/customers?preference_type_id=1
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getUsers(@Context HttpHeaders headers, @QueryParam("sortby") String sortby, @QueryParam("offset") Integer offset,
                                @QueryParam("count") Integer count, @QueryParam("preference_type_id") String preference_type_id){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Customer> customers = null;

            if (sortby != null)
                customers = CustomerManager.getInstance().getCustomerListSorted(sortby);
            else if (offset != null && count != null)
                customers = CustomerManager.getInstance().getCustomerListPaginated(offset, count);
            else if (preference_type_id != null)
                customers = CustomerManager.getInstance().getCustomerListFliterByTypeId(preference_type_id);
            else
                customers = CustomerManager.getInstance().getCustomerList();

            if(customers != null)
                return new AppResponse(customers);
            else
                throw new HttpBadRequestException(0, "Problem with getting customers");
        }catch (Exception e){
            throw handleException("GET /customers", e);
        }
    }

    @PATCH
    @Path("/{customerId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchCustomers(Object request, @PathParam("customerId") String customerId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Customer customer = new Customer(
                    customerId,
                    json.getString("first_name"),
                    json.getString("last_name"),
                    json.getString("username"),
                    json.getString("password"),
                    json.getString("email"),
                    json.getString("phone"),
                    json.getString("preference_type_id"),
                    json.getInt("coin")
            );

            CustomerManager.getInstance().updateCustomer(customer);

        }catch (Exception e){
            throw handleException("PATCH customer/{customerId}", e);
        }

        return new AppResponse("Update Successful");
    }

    @DELETE
    @Path("/{customerId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteCustomers(@PathParam("customerId") String customerId){

        try{
            System.out.println("Delete Successful");
            CustomerManager.getInstance().deleteCustomer( customerId);
            System.out.println("Delete Successful");
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE customers/{customerId}", e);
        }

    }

    @GET
    @Path("/{customerId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleCustomer(@Context HttpHeaders headers, @PathParam("customerId") String customerId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Customer> customers = CustomerManager.getInstance().getCustomerById(customerId);

            if(customers != null)
                return new AppResponse(customers);
            else
                throw new HttpBadRequestException(0, "Problem with getting customers");
        }catch (Exception e){
            throw handleException("GET /customers/{customerId}", e);
        }

    }



}
