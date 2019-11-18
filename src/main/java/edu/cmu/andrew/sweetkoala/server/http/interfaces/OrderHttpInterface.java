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



    // http://localhost:8080/api/events
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postOrders(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Order neworder = new Order(
                    null,
                    json.getString("customer_id"),
                    json.getString("date"),
                    json.getString("time"),
                    json.getString("num ber_of_people"),
                    json.getString("status")
            );

            OrderManager.getInstance().createOrder(neworder);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST orders", e);
        }
    }

}
