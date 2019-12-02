package edu.cmu.andrew.sweetkoala.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.sweetkoala.server.http.responses.AppResponse;
import edu.cmu.andrew.sweetkoala.server.http.utils.PATCH;
import edu.cmu.andrew.sweetkoala.server.models.Order;
import edu.cmu.andrew.sweetkoala.server.managers.OrderManager;
import edu.cmu.andrew.sweetkoala.server.utils.*;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/orders")

public class OrderHttpInterface extends HttpInterface {

    private ObjectWriter ow;
    private MongoCollection<Document> orderCollection = null;

    public OrderHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    // http://localhost:8080/api/orders/reset
    @POST
    @Path("/reset")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse resetOrders(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            OrderManager.getInstance().resetOrders();

            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST orders", e);
        }
    }

    // http://localhost:8080/api/orders
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postOrders(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Order neworder = new Order(
                    null,
                    json.getString("event_id"),
                    json.getString("customer_id"),
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

    // http://localhost:8080/api/orders
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getOrders(@Context HttpHeaders headers) {
        try{
            AppLogger.info("Got an API call");
            ArrayList<Order> orders = null;

            orders = OrderManager.getInstance().getOrderList();

            if(orders != null)
                return new AppResponse(orders);
            else
                throw new HttpBadRequestException(0, "Problem with getting orders");
        }catch (Exception e){
            throw handleException("GET /orders", e);
        }
    }

    // http://localhost:8080/api/orders/5dd31c004569682f31782099
    @GET
    @Path("/{orderId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleOrder(@Context HttpHeaders headers, @PathParam("orderId") String orderId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Order> orders = OrderManager.getInstance().getOrderById(orderId);

            if(orders != null)
                return new AppResponse(orders);
            else
                throw new HttpBadRequestException(0, "Problem with getting orders");
        }catch (Exception e){
            throw handleException("GET /orders/{orderId}", e);
        }
    }

    // http://localhost:8080/api/orders/5dd321084569682f9dc20ec7
    @PATCH
    @Path("/{orderId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchOrders(Object request, @PathParam("orderId") String orderId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Order order = new Order(
                    orderId,
                    json.getString("event_id"),
                    json.getString("customer_id"),
                    json.getString("date"),
                    json.getString("time"),
                    json.getInt("number_of_people"),
                    json.getString("status")
            );

            OrderManager.getInstance().updateOrder(order);

        }catch (Exception e){
            throw handleException("PATCH order/{orderId}", e);
        }
        return new AppResponse("Update Successful");
    }

    // http://localhost:8080/api/orders/5dd321084569682f9dc20ec7
    @DELETE
    @Path("/{orderId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteOrders(@PathParam("orderId") String orderId){

        try{
            System.out.println("Delete Successful");
            OrderManager.getInstance().deleteOrder( orderId);
            System.out.println("Delete Successful");
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE orders/{orderId}", e);
        }
    }
}
