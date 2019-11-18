package edu.cmu.andrew.sweetkoala.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.sweetkoala.server.http.responses.AppResponse;
import edu.cmu.andrew.sweetkoala.server.http.utils.PATCH;
import edu.cmu.andrew.sweetkoala.server.models.Event;
import edu.cmu.andrew.sweetkoala.server.managers.EventManager;
import edu.cmu.andrew.sweetkoala.server.utils.*;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/events")

public class EventHttpInterface extends HttpInterface {

    private ObjectWriter ow;
    private MongoCollection<Document> eventCollection = null;

    public EventHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    // http://localhost:8080/api/events/reset
    @POST
    @Path("/reset")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse resetEvents(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            EventManager.getInstance().resetEvents();

            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST events", e);
        }
    }

    // http://localhost:8080/api/events
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postEvents(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Event newevent = new Event(
                    null,
                    json.getString("publisher_id"),
                    json.getString("name"),
                    json.getString("type_id"),
                    json.getString("date"),
                    json.getString("time"),
                    json.getString("location"),
                    json.getInt("price"),
                    json.getInt("capacity"),
                    json.getInt("recommended_people"),
                    json.getString("description")
            );

            EventManager.getInstance().createEvent(newevent);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST events", e);
        }
    }

    // http://localhost:8080/api/events
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getEvents(@Context HttpHeaders headers) {
        try{
            AppLogger.info("Got an API call");
            ArrayList<Event> events = null;

            events = EventManager.getInstance().getEventList();

            if(events != null)
                return new AppResponse(events);
            else
                throw new HttpBadRequestException(0, "Problem with getting events");
        }catch (Exception e){
            throw handleException("GET /events", e);
        }
    }

    // http://localhost:8080/api/events/5dcc8bf9ae092861bf45cbb7
    @GET
    @Path("/{eventId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleEvent(@Context HttpHeaders headers, @PathParam("eventId") String eventId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Event> events = EventManager.getInstance().getEventById(eventId);

            if(events != null)
                return new AppResponse(events);
            else
                throw new HttpBadRequestException(0, "Problem with getting events");
        }catch (Exception e){
            throw handleException("GET /events/{eventId}", e);
        }
    }

    // http://localhost:8080/api/customers/5dcc8bf9ae092861bf45cbb7
    @PATCH
    @Path("/{eventId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchEvents(Object request, @PathParam("eventId") String eventId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Event event = new Event(
                    eventId,
                    json.getString("publisher_id"),
                    json.getString("name"),
                    json.getString("type_id"),
                    json.getString("date"),
                    json.getString("time"),
                    json.getString("location"),
                    json.getInt("price"),
                    json.getInt("capacity"),
                    json.getInt("recommended_people"),
                    json.getString("description")
            );

            EventManager.getInstance().updateEvent(event);

        }catch (Exception e){
            throw handleException("PATCH event/{eventId}", e);
        }
        return new AppResponse("Update Successful");
    }

    // http://localhost:8080/api/events/5dcc8bf9ae092861bf45cbb7
    @DELETE
    @Path("/{eventId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteEvents(@PathParam("eventId") String eventId){

        try{
            System.out.println("Delete Successful");
            EventManager.getInstance().deleteEvent( eventId);
            System.out.println("Delete Successful");
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE events/{eventId}", e);
        }
    }

}
