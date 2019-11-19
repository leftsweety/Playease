package edu.cmu.andrew.sweetkoala.server.http.interfaces;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.sweetkoala.server.http.responses.AppResponse;
import edu.cmu.andrew.sweetkoala.server.managers.CustomerManager;
import edu.cmu.andrew.sweetkoala.server.models.Payment;
import edu.cmu.andrew.sweetkoala.server.managers.PaymentManager;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/payments")

public class PaymentHttpInterface extends HttpInterface{

    private ObjectWriter ow;
    private MongoCollection<Document> paymentCollection = null;

    public PaymentHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postPayments(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Payment newpayment = new Payment(
                    null,
                    json.getString("order_id"),
                    json.getInt("cost"),
                    json.getString("date"),
                    json.getString("time")
            );
            PaymentManager.getInstance().createPayment(newpayment);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST payments", e);
        }

    }

    //Sorting: http://localhost:8080/api/payments?sortby=riderBalance
    //Pagination: http://localhost:8080/api/payments?offset=1&count=2
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPayments(@Context HttpHeaders headers){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Payment> payments = null;

            payments = PaymentManager.getInstance().getPaymentList();

            if(payments != null)
                return new AppResponse(payments);
            else
                throw new HttpBadRequestException(0, "Problem with getting payments");
        }catch (Exception e){
            throw handleException("GET /payments", e);
        }
    }

    @GET
    @Path("/{paymentId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSinglePayment(@Context HttpHeaders headers, @PathParam("paymentId") String paymentId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Payment> payments = PaymentManager.getInstance().getPaymentById(paymentId);

            if(payments != null)
                return new AppResponse(payments);
            else
                throw new HttpBadRequestException(0, "Problem with getting payments");
        }catch (Exception e){
            throw handleException("GET /payments/{paymentId}", e);
        }


    }
    @POST
    @Path("/reset")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse resetPayments(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            PaymentManager.getInstance().reSetPayment();

            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST payments", e);
        }

    }



}
