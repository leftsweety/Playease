package edu.cmu.andrew.sweetkoala.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.sweetkoala.server.http.responses.AppResponse;
import edu.cmu.andrew.sweetkoala.server.managers.SessionManager;

import javax.annotation.security.PermitAll;
import javax.mail.Session;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("sessions")

public class SessionHttpService {
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
    public AppResponse createCustomer(Object request) throws Exception {

        return new AppResponse(service.create(request, 0));
    }

    //http://localhost:8080/api/sessions/login/publisher
    @POST
    @Path("/login/publisher")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse createPublisher(Object request) throws Exception {

        return new AppResponse(service.create(request, 1));
    }


}
