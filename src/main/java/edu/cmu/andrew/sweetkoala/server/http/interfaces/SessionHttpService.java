package edu.cmu.andrew.sweetkoala.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.sweetkoala.server.http.responses.AppResponse;
import edu.cmu.andrew.sweetkoala.server.managers.CustomerManager;
import edu.cmu.andrew.sweetkoala.server.managers.PublisherManager;
import edu.cmu.andrew.sweetkoala.server.managers.SessionManager;
import edu.cmu.andrew.sweetkoala.server.models.Customer;
import edu.cmu.andrew.sweetkoala.server.models.Publisher;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.json.JSONObject;

import javax.annotation.security.PermitAll;
import javax.mail.Session;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("sessions")

public class SessionHttpService extends HttpInterface{
    private SessionManager service;
    private ObjectWriter ow;


    public SessionHttpService() {
        service = SessionManager.getInstance();
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    @OPTIONS
    @PermitAll
    public Response optionsById() {

        return Response.ok().build();
    }

    //http://localhost:8080/api/sessions/login/customer
    @POST
    @Path("/login/customer")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse createCustomerSession(Object request) throws Exception {

        return new AppResponse(service.create(request, 0));
    }

    //http://localhost:8080/api/sessions/login/publisher
    @POST
    @Path("/login/publisher")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse createPublisherSession(Object request) throws Exception {

        return new AppResponse(service.create(request, 1));
    }

    //http://localhost:8080/api/sessions/signup/customer
    @POST
    @Path("/signup/customer")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postCustomerInfo(Object request){

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

    //http://localhost:8080/api/sessions/signup/publisher
    @POST
    @Path("/signup/publisher")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postPublisherInfo(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Publisher newpublisher = new Publisher(
                    null,
                    json.getString("first_name"),
                    json.getString("last_name"),
                    json.getString("username"),
                    json.getString("password"),
                    json.getString("email"),
                    json.getString("phone"),
                    json.getString("location"),
                    json.getString("type_id"),
                    json.getInt("coin")
            );
            PublisherManager.getInstance().createPublisher(newpublisher);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST publishers", e);
        }
    }

    //http://localhost:8080/api/sessions/homepage
    @GET
    @Path("/homepage")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getHomePageInfo(@Context HttpHeaders headers){
        try{
            AppLogger.info("Got an API call");
                return new AppResponse(SessionManager.getInstance().getHomePageInfo());
        }catch (Exception e){
            throw handleException("GET /ratings", e);
        }
    }

}
