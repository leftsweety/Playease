package edu.cmu.andrew.sweetkoala.server.http.interfaces;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppException;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpInternalServerException;
import edu.cmu.andrew.sweetkoala.server.http.responses.AppResponse;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import edu.cmu.andrew.sweetkoala.server.managers.Manager;
import edu.cmu.andrew.sweetkoala.server.managers.WeatherManager;
import edu.cmu.andrew.sweetkoala.server.http.interfaces.HttpInterface;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONException;

import javax.annotation.security.PermitAll;

import javax.ws.rs.core.Response;
import java.util.TreeMap;

@Path("")

public class MainHttpInterface extends ResourceConfig {

    private ObjectWriter ow;


    public MainHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @OPTIONS
    @PermitAll
    public Response options() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * GET /
     *
     * @return {  Version, Release Date, Environment, Mail Prefix, Up Since, Warranty Conditions }
     */

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public Object VersionGet() {
        TreeMap<String,String> version = new TreeMap<>();
        version.put("Release Version", "1.0");
        return new AppResponse(version);
    }

    @POST
    @Path("/reset")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse resetOverall(Object request){

        try{

            Manager.getInstance().resetOverall();

            return new AppResponse("Insert Successful");

        }catch (Exception e){

            throw handleException("reset error",e);
        }

    }

    protected WebApplicationException handleException(String message, Exception e){
        if(e instanceof JSONException)
            return new HttpBadRequestException(-1, "Bad request data provided: " + e.getMessage());
        if (e instanceof AppException)
            return ((AppException) e).getHttpException();

        AppLogger.error(message, e);
        return new HttpInternalServerException(-1);
    }


}
