package edu.cmu.andrew.sweetkoala.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.sweetkoala.server.http.responses.AppResponse;
import edu.cmu.andrew.sweetkoala.server.managers.RatingManager;
import edu.cmu.andrew.sweetkoala.server.models.Rating;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

    @Path("/ratings")

    public class RatingHttpInterface extends HttpInterface{

        private ObjectWriter ow;
        private MongoCollection<Document> ratingCollection = null;

        public RatingHttpInterface() {
            ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        }

        @POST
        @Consumes({MediaType.APPLICATION_JSON})
        @Produces({MediaType.APPLICATION_JSON})
        public AppResponse postRatings(Object request){

            try{
                JSONObject json = null;
                json = new JSONObject(ow.writeValueAsString(request));

                Rating newrating = new Rating(
                        null,
                        json.getString("customer_id"),
                        json.getString("publisher_id"),
                        json.getInt("star")
                );
                RatingManager.getInstance().createRating(newrating);
                return new AppResponse("Insert Successful");

            }catch (Exception e){
                throw handleException("POST ratings", e);
            }

        }

        //Sorting: http://localhost:8080/api/ratings?sortby=riderBalance
        //Pagination: http://localhost:8080/api/ratings?offset=1&count=2
        @GET
        @Produces({MediaType.APPLICATION_JSON})
        public AppResponse getRatings(@Context HttpHeaders headers){
            try{
                AppLogger.info("Got an API call");
                ArrayList<Rating> ratings = null;

                ratings = RatingManager.getInstance().getRatingList();

                if(ratings != null)
                    return new AppResponse(ratings);
                else
                    throw new HttpBadRequestException(0, "Problem with getting ratings");
            }catch (Exception e){
                throw handleException("GET /ratings", e);
            }
        }

        @GET
        @Path("/{ratingId}")
        @Produces({MediaType.APPLICATION_JSON})
        public AppResponse getSingleRating(@Context HttpHeaders headers, @PathParam("ratingId") String ratingId){

            try{
                AppLogger.info("Got an API call");
                ArrayList<Rating> ratings = RatingManager.getInstance().getRatingById(ratingId);

                if(ratings != null)
                    return new AppResponse(ratings);
                else
                    throw new HttpBadRequestException(0, "Problem with getting ratings");
            }catch (Exception e){
                throw handleException("GET /ratings/{ratingId}", e);
            }


        }
        @POST
        @Path("/reset")
        @Consumes({MediaType.APPLICATION_JSON})
        @Produces({MediaType.APPLICATION_JSON})
        public AppResponse resetRatings(Object request){

            try{
                JSONObject json = null;
                json = new JSONObject(ow.writeValueAsString(request));

                RatingManager.getInstance().reSetRating();


                RatingManager.getInstance().reSetRating();
                ArrayList<Rating> publishers = RatingManager.getInstance().getRatingList();
                return new AppResponse("Insert Successful");

            }catch (Exception e){
                throw handleException("POST ratings", e);
            }

        }



    }

