package edu.cmu.andrew.sweetkoala.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.sweetkoala.server.http.responses.AppResponse;
import edu.cmu.andrew.sweetkoala.server.http.utils.PATCH;
import edu.cmu.andrew.sweetkoala.server.models.Favorite_Location;
import edu.cmu.andrew.sweetkoala.server.managers.Favorite_LocationManager;
import edu.cmu.andrew.sweetkoala.server.utils.*;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/favoriteLocations")

public class Favorite_LocationHttpInterface extends HttpInterface {

    private ObjectWriter ow;
    private MongoCollection<Document> Favorite_LocationCollection = null;

    public Favorite_LocationHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    // http://localhost:8080/api/favoriteLocations/reset
    @POST
    @Path("/reset")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse resetFavorite_Locations(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Favorite_LocationManager.getInstance().resetFavorite_Locations();

            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST favorite locations", e);
        }
    }

    // http://localhost:8080/api/favoriteLocations
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postFavorite_Locations(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Favorite_Location newFavorite_Location = new Favorite_Location(
                    null,
                    json.getString("customer_id"),
                    json.getString("event_id")
            );

            Favorite_LocationManager.getInstance().createFavorite_Location(newFavorite_Location);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST favorite locations", e);
        }
    }

    // http://localhost:8080/api/favoriteLocations
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getFavorite_Locations(@Context HttpHeaders headers) {
        try{
            AppLogger.info("Got an API call");
            ArrayList<Favorite_Location> favorite_Locations = null;

            favorite_Locations = Favorite_LocationManager.getInstance().getFavorite_LocationList();

            if(favorite_Locations != null)
                return new AppResponse(favorite_Locations);
            else
                throw new HttpBadRequestException(0, "Problem with getting favorite locations");
        }catch (Exception e){
            throw handleException("GET /favorite locations", e);
        }
    }

    // http://localhost:8080/api/favoriteLocations/5dcc8bf9ae092861bf45cbb7
    @GET
    @Path("/{favorite_LocationId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleEvent(@Context HttpHeaders headers, @PathParam("favorite_LocationId") String favorite_LocationId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Favorite_Location> favorite_locations = Favorite_LocationManager.getInstance().getFavorite_LocationById(favorite_LocationId);

            if(favorite_locations != null)
                return new AppResponse(favorite_locations);
            else
                throw new HttpBadRequestException(0, "Problem with getting favorite locations");
        }catch (Exception e){
            throw handleException("GET /favorite locations/{favorite_LocationId}", e);
        }
    }

    // http://localhost:8080/api/favoriteLocations/5dcc8bf9ae092861bf45cbb7
    @PATCH
    @Path("/{favorite_LocationId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchFavorite_Locations(Object request, @PathParam("favorite_LocationId") String favorite_LocationId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Favorite_Location favorite_Location = new Favorite_Location(
                    favorite_LocationId,
                    json.getString("customer_id"),
                    json.getString("event_id")
            );

            Favorite_LocationManager.getInstance().updateFavorite_Location(favorite_Location);

        }catch (Exception e){
            throw handleException("PATCH favorite location/{favorite_LocationId}", e);
        }
        return new AppResponse("Update Successful");
    }

    // http://localhost:8080/api/events/5dcc8bf9ae092861bf45cbb7
    @DELETE
    @Path("/{favorite_LocationId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteFavorite_Locations(@PathParam("favorite_LocationId") String favorite_LocationId){

        try{
            System.out.println("Delete Successful");
            Favorite_LocationManager.getInstance().deleteFavorite_Location( favorite_LocationId);
            System.out.println("Delete Successful");
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE favoritel ocation/{favorite_LocationId}", e);
        }
    }

}
