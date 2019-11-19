package edu.cmu.andrew.sweetkoala.server.http.interfaces;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.sweetkoala.server.http.responses.AppResponse;
import edu.cmu.andrew.sweetkoala.server.managers.WeatherManager;
import edu.cmu.andrew.sweetkoala.server.models.Type;
import edu.cmu.andrew.sweetkoala.server.managers.TypeManager;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/types")

public class TypeHttpInterface extends HttpInterface{

    private ObjectWriter ow;
    private MongoCollection<Document> typeCollection = null;

    public TypeHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postTypes(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Type newtype = new Type(
                    null,
                    json.getString("event_type")
            );
            TypeManager.getInstance().createType(newtype);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST types", e);
        }

    }

    //Sorting: http://localhost:8080/api/types?sortby=riderBalance
    //Pagination: http://localhost:8080/api/types?offset=1&count=2
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getTypes(@Context HttpHeaders headers){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Type> types = null;

            types = TypeManager.getInstance().getTypeList();

            if(types != null)
                return new AppResponse(types);
            else
                throw new HttpBadRequestException(0, "Problem with getting types");
        }catch (Exception e){
            throw handleException("GET /types", e);
        }
    }

    @GET
    @Path("/{typeId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleType(@Context HttpHeaders headers, @PathParam("typeId") String typeId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Type> types = TypeManager.getInstance().getTypeById(typeId);

            if(types != null)
                return new AppResponse(types);
            else
                throw new HttpBadRequestException(0, "Problem with getting types");
        }catch (Exception e){
            throw handleException("GET /types/{typeId}", e);
        }


    }
    @POST
    @Path("/reset")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse resetTypes(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

           TypeManager.getInstance().reSetType();

            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST ratings", e);
        }

    }



}
