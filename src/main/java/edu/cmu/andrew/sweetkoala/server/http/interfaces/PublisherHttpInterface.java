package edu.cmu.andrew.sweetkoala.server.http.interfaces;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.sweetkoala.server.http.responses.AppResponse;
import edu.cmu.andrew.sweetkoala.server.http.utils.PATCH;
import edu.cmu.andrew.sweetkoala.server.models.Publisher;
import edu.cmu.andrew.sweetkoala.server.managers.PublisherManager;
import edu.cmu.andrew.sweetkoala.server.managers.SessionManager;
import edu.cmu.andrew.sweetkoala.server.models.Session;
import edu.cmu.andrew.sweetkoala.server.utils.*;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Locale;

@Path("/publishers")

public class PublisherHttpInterface extends HttpInterface{

    private ObjectWriter ow;
    private MongoCollection<Document> publisherCollection = null;

    public PublisherHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postPublishers(Object request){

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

    //Sorting: http://localhost:8080/api/publishers?sortby=coin
    //Pagination: http://localhost:8080/api/publishers?offset=1&count=2
    //Filtering: http://localhost:8080/api/publisher?type_id=001
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPublishers(@Context HttpHeaders headers, @QueryParam("sortby") String sortby, @QueryParam("offset") Integer offset,
                                @QueryParam("count") Integer count,@QueryParam("type_id") String type_id){
        try{
            AppLogger.info("Got an API call");
            System.out.println("Test2");
            ArrayList<Publisher> publishers = null;

            if(sortby != null)
                publishers = PublisherManager.getInstance().getPublisherListSorted(sortby);
            else if(offset != null && count != null)
                publishers = PublisherManager.getInstance().getPublisherListPaginated(offset, count);
            else if(type_id!=null){
                publishers = PublisherManager.getInstance().getPublisherListFiltered(type_id);
            }
            else
                publishers = PublisherManager.getInstance().getPublisherList();

            System.out.println(publishers.size());
            if(publishers != null)
            {
                System.out.println("Test1");
                return new AppResponse(publishers);
            }

            else
                throw new HttpBadRequestException(0, "Problem with getting publishers");
        }catch (Exception e){
            throw handleException("GET /publishers", e);
        }
    }

/*
  //http://server.com/api/publishers?begin=11&count=10
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPublishersPaginated(@Context HttpHeaders headers){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Publisher> publishers = PublisherManager.getInstance().getPublisherList();

            if(publishers != null)
                return new AppResponse(publishers);
            else
                throw new HttpBadRequestException(0, "Problem with getting publishers");
        }catch (Exception e){
            throw handleException("GET /publishers", e);
        }
    }*/

    @GET
    @Path("/{publisherId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSinglePublisher(@Context HttpHeaders headers, @PathParam("publisherId") String publisherId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Publisher> publishers = PublisherManager.getInstance().getPublisherById(publisherId);

            if(publishers != null)
                return new AppResponse(publishers);
            else
                throw new HttpBadRequestException(0, "Problem with getting publishers");
        }catch (Exception e){
            throw handleException("GET /publishers/{publisherId}", e);
        }


    }


    @PATCH
    @Path("/{publisherId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchPublishers(Object request, @PathParam("publisherId") String publisherId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Publisher publisher = new Publisher(
                    publisherId,
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

            PublisherManager.getInstance().updatePublisher(publisher);

        }catch (Exception e){
            throw handleException("PATCH publishers/{publisherId}", e);
        }

        return new AppResponse("Update Successful");
    }


    @DELETE
    @Path("/{publisherId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deletePublishers(@PathParam("publisherId") String publisherId){

        try{
            PublisherManager.getInstance().deletePublisher(publisherId);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE publishers/{publisherId}", e);
        }

    }

    @POST
    @Path("/reset")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse resetPublishers(){

        try{
            PublisherManager.getInstance().reSetPublisher();
            ArrayList<Publisher> publishers = PublisherManager.getInstance().getPublisherList();
            return new AppResponse(publishers);
        }catch (Exception e){
            throw handleException("Reset publishers", e);
        }

    }

    // http://localhost:8080/api/publishers/location/5de34728c97ba821ce644f1e
    @GET
    @Path("/location/{publisherId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPublisherLocation(@Context HttpHeaders headers, @PathParam("publisherId") String publisherId){

        try{
            Session session = SessionManager.getInstance().getSessionForTokenPublisher(headers);
            if(!session.getUserId().equals(publisherId))
                throw new HttpBadRequestException(0, "Invalid user id");
            AppLogger.info("Got an API call");
            ArrayList<Publisher> publishers = PublisherManager.getInstance().getPublisherById(publisherId);

            if(publishers != null) {
                String location = null;
                for (Publisher publisher : publishers)
                    location = publisher.getLocation();
                return new AppResponse(location);
            }
            else
                throw new HttpBadRequestException(0, "Problem with getting publishers");
        }catch (Exception e){
            throw handleException("GET /publishers/{publisherId}", e);
        }


    }

}
