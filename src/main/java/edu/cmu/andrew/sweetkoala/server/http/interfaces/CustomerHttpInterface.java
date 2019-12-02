package edu.cmu.andrew.sweetkoala.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.sweetkoala.server.http.responses.AppResponse;
import edu.cmu.andrew.sweetkoala.server.http.utils.PATCH;
import edu.cmu.andrew.sweetkoala.server.managers.*;
import edu.cmu.andrew.sweetkoala.server.models.*;
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


    // http://localhost:8080/api/customers/reset
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

    // http://localhost:8080/api/customers
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postCustomers(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

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
    public AppResponse getCustomers(@Context HttpHeaders headers, @QueryParam("sortby") String sortby, @QueryParam("offset") Integer offset,
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

    // http://localhost:8080/api/customers/5dcc8bf9ae092861bf45cbb7
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

    // http://localhost:8080/api/customers/5dcc8bf9ae092861bf45cbb7
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

    // http://localhost:8080/api/customers/5dcc8bf9ae092861bf45cbb7
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

    // http://localhost:8080/api/customers/orders/histories/5de49f3bceda381df411edc5
    @GET
    @Path("/orders/histories/{customerId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getCustomerOrderHistory(@Context HttpHeaders headers, @PathParam("customerId") String customerId){

        try{
            Session session = SessionManager.getInstance().getSessionForTokenCustomer(headers);
            if(!session.getUserId().equals(customerId))
                throw new HttpBadRequestException(0, "Invalid user id");
            AppLogger.info("Got an API call");
            ArrayList<Order> orders = OrderManager.getInstance().getOrderByCustomerId(customerId);

            if(orders != null) {
                return new AppResponse(orders);
            }
            else
                throw new HttpBadRequestException(0, "Problem with getting customer's order history");
        }catch (Exception e){
            throw handleException("GET customers/orders/histories/customerId", e);
        }
    }

    // http://localhost:8080/api/customers/createOrder/{customerId}
    @POST
    @Path("/createOrder/{customerId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse createOrder(@Context HttpHeaders headers,Object request, @PathParam("customerId") String customerId){
        JSONObject json = null;
        try{
            Session session = SessionManager.getInstance().getSessionForTokenCustomer(headers);
            if(!session.getUserId().equals(customerId))
                throw new HttpBadRequestException(0, "Invalid user id");
            AppLogger.info("Got an API call");

            json = new JSONObject(ow.writeValueAsString(request));

                json = new JSONObject(ow.writeValueAsString(request));

                Order neworder = new Order(
                        null,
                        json.getString("event_id"),
                        customerId,
                        json.getString("date"),
                        json.getString("time"),
                        json.getInt("number_of_people"),
                        json.getString("status")
                );

                OrderManager.getInstance().createOrder(neworder);
                return new AppResponse("Insert Successful");

            }catch (Exception e){
                throw handleException("POST orders", e);
            }
    }

   // DELETE /customers/{customerId}/cancelOrder?orderId=orderId
   @DELETE
   @Path("/{customerId}/cancelOrder")
   @Produces({MediaType.APPLICATION_JSON})
   public AppResponse DeleteOrder(@Context HttpHeaders headers, @PathParam("customerId") String customerId, @QueryParam("orderId") String orderId){
       try{
           Session session = SessionManager.getInstance().getSessionForTokenCustomer(headers);
           if(!session.getUserId().equals(customerId))
               throw new HttpBadRequestException(0, "Invalid user id");
           AppLogger.info("Got an API call");

           OrderManager.getInstance().deleteOrder(orderId);
           System.out.println("Delete Successful");
           return new AppResponse("Delete Successful");

       }catch (Exception e){
           throw handleException("POST orders", e);
       }
   }

   // GET /customers/addressUpdate/{customerId}?password=newpassword
   @PATCH
   @Path("/addressUpdate/{customerId}")
   @Consumes({ MediaType.APPLICATION_JSON})
   @Produces({ MediaType.APPLICATION_JSON})
   public AppResponse patchCustomerAddress(Object request, @PathParam("customerId") String customerId, @QueryParam("password") String password){

       try{
           AppLogger.info("Got an API call");
           ArrayList<Customer> customers = CustomerManager.getInstance().getCustomerById(customerId);

           if(customers != null){
               Customer mycustomer = new Customer(
                       customerId,
                       customers.get(0).getFirst_name(),
                       customers.get(0).getLast_name(),
                       customers.get(0).getUsername(),
                       password,
                       customers.get(0).getEmail(),
                       customers.get(0).getPhone(),
                       customers.get(0).getPreference_type_id(),
                       customers.get(0).getCoin()
               );
               CustomerManager.getInstance().updateCustomer(mycustomer);
           }

           else
               throw new HttpBadRequestException(0, "Problem with getting customers");
       }catch (Exception e){
           throw handleException("PATCH customer/{customerId}", e);
       }
       return new AppResponse("Update Successful");
   }

   // GET /customers/paymentOverview/listPayment/customersId
   @GET
   @Path("/paymentOverview/listPayment/{customerId}")
   @Produces({MediaType.APPLICATION_JSON})
   public AppResponse getCustomerPayment(@Context HttpHeaders headers, @PathParam("customerId") String customerId){

       try{
           Session session = SessionManager.getInstance().getSessionForTokenCustomer(headers);
           if(!session.getUserId().equals(customerId))
               throw new HttpBadRequestException(0, "Invalid user id");
           AppLogger.info("Got an API call");
           ArrayList<Payment> payments = PaymentManager.getInstance().getPaymentByCustomerId(customerId);

           if(payments != null) {
               return new AppResponse(payments);
           }
           else
               throw new HttpBadRequestException(0, "Problem with getting customer's order history");
       }catch (Exception e){
           throw handleException("GET customers/orders/histories/customerId", e);
       }
   }

   //GET /customers/weather/customerId - get the weather of customers’ locations
   @GET
   @Path("/weather/{customerId}")
   @Produces({MediaType.APPLICATION_JSON})
   public AppResponse getCustomerWeather(@Context HttpHeaders headers, @PathParam("customerId") String customerId) {
       try{
           Session session = SessionManager.getInstance().getSessionForTokenCustomer(headers);
           if(!session.getUserId().equals(customerId))
               throw new HttpBadRequestException(0, "Invalid user id");
           AppLogger.info("Got an API call");
           return new AppResponse("Rainy");
       }catch (Exception e){
           throw handleException("GET /weather/{customerId}", e);
       }
   }

    @GET
    @Path("/localtime/{customerId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getCustomerTime(@Context HttpHeaders headers, @PathParam("customerId") String customerId) {

        try{
            Session session = SessionManager.getInstance().getSessionForTokenCustomer(headers);
            if(!session.getUserId().equals(customerId))
                throw new HttpBadRequestException(0, "Invalid user id");
            AppLogger.info("Got an API call");
            return new AppResponse("2019-11-11");
        }catch (Exception e){
            throw handleException("GET /localtime/{customerId}", e);
        }
    }

    // http://localhost:8080/api/customers/location/5de4a295ceda381e8c85c687/5de4a295ceda381e8c85c681
    @GET
    @Path("/location/{publisherId}/{customerId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPublisherLocation(@Context HttpHeaders headers, @PathParam("publisherId") String publisherId, @PathParam("customerId") String customerId){

        try{
            Session session = SessionManager.getInstance().getSessionForTokenCustomer(headers);
            if(!session.getUserId().equals(customerId))
                throw new HttpBadRequestException(0, "Invalid user id");
            AppLogger.info("Got an API call");
            ArrayList<Publisher> publishers = PublisherManager.getInstance().getPublisherById(publisherId);

            if(publishers != null) {
                String location = null;
                for (Publisher publisher : publishers)
                    location = publisher.getLocation();
                return new AppResponse(location);
            }
            else
                throw new HttpBadRequestException(0, "Problem with getting publishers");
        }catch (Exception e){
            throw handleException("GET /publishers/{publisherId}", e);
        }
    }

    //GET /customers/searchdetail{customerId}?type=typeId&time=MMDDYYYY
    @GET
    @Path("/searchdetail/{customerId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSearchEvent(@Context HttpHeaders headers,  @PathParam("customerId") String customerId, @QueryParam("type") String typeId, @QueryParam("time") String date){

        try{
            Session session = SessionManager.getInstance().getSessionForTokenCustomer(headers);
            if(!session.getUserId().equals(customerId))
                throw new HttpBadRequestException(0, "Invalid user id");
            AppLogger.info("Got an API call");
            ArrayList<Event> events = EventManager.getInstance().getEventByType(typeId,date);

            if(events != null) {
                return new AppResponse(events);
            }
            else
                throw new HttpBadRequestException(0, "Problem with getting customer's order history");
        }catch (Exception e){
            throw handleException("GET customers/searchdetail/{customerId}", e);
        }
    }




}
