package edu.cmu.andrew.sweetkoala.server.http.interfaces;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.sweetkoala.server.http.responses.AppResponse;
import edu.cmu.andrew.sweetkoala.server.managers.RatingManager;
import edu.cmu.andrew.sweetkoala.server.models.Weather;
import edu.cmu.andrew.sweetkoala.server.managers.WeatherManager;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/weathers")

public class WeatherHttpInterface extends HttpInterface{

    private ObjectWriter ow;
    private MongoCollection<Document> weatherCollection = null;

    public WeatherHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postWeathers(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Weather newweather = new Weather(
                    null,
                    json.getString("type_id"),
                    json.getString("status")
            );
            WeatherManager.getInstance().createWeather(newweather);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST weathers", e);
        }

    }

    //Sorting: http://localhost:8080/api/weathers?sortby=riderBalance
    //Pagination: http://localhost:8080/api/weathers?offset=1&count=2
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getWeathers(@Context HttpHeaders headers){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Weather> weathers = null;

            weathers = WeatherManager.getInstance().getWeatherList();

            if(weathers != null)
                return new AppResponse(weathers);
            else
                throw new HttpBadRequestException(0, "Problem with getting weathers");
        }catch (Exception e){
            throw handleException("GET /weathers", e);
        }
    }

    @GET
    @Path("/{weatherId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleWeather(@Context HttpHeaders headers, @PathParam("weatherId") String weatherId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Weather> weathers = WeatherManager.getInstance().getWeatherById(weatherId);

            if(weathers != null)
                return new AppResponse(weathers);
            else
                throw new HttpBadRequestException(0, "Problem with getting weathers");
        }catch (Exception e){
            throw handleException("GET /weathers/{weatherId}", e);
        }

    }

    @POST
    @Path("/reset")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse resetWeathers(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            WeatherManager.getInstance().reSetWeather();

            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST ratings", e);
        }

    }



}
