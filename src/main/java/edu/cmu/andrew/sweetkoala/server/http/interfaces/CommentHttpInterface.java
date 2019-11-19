package edu.cmu.andrew.sweetkoala.server.http.interfaces;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.sweetkoala.server.http.responses.AppResponse;
import edu.cmu.andrew.sweetkoala.server.managers.CustomerManager;
import edu.cmu.andrew.sweetkoala.server.models.Comment;
import edu.cmu.andrew.sweetkoala.server.managers.CommentManager;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/comments")

public class CommentHttpInterface extends HttpInterface{

    private ObjectWriter ow;
    private MongoCollection<Document> commentCollection = null;

    public CommentHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Path("/reset")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse resetComments(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            CommentManager.getInstance().reSetComment();

            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST comments", e);
        }

    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postComments(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Comment newcomment = new Comment(
                    null,
                    json.getString("event_id"),
                    json.getString("customer_id"),
                    json.getString("description")
            );
            CommentManager.getInstance().createComment(newcomment);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST comments", e);
        }

    }

    //Sorting: http://localhost:8080/api/comments?sortby=riderBalance
    //Pagination: http://localhost:8080/api/comments?offset=1&count=2
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getComments(@Context HttpHeaders headers){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Comment> comments = null;

            comments = CommentManager.getInstance().getCommentList();

            if(comments != null)
                return new AppResponse(comments);
            else
                throw new HttpBadRequestException(0, "Problem with getting comments");
        }catch (Exception e){
            throw handleException("GET /comments", e);
        }
    }

    @GET
    @Path("/{commentId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleComment(@Context HttpHeaders headers, @PathParam("commentId") String commentId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Comment> comments = CommentManager.getInstance().getCommentById(commentId);

            if(comments != null)
                return new AppResponse(comments);
            else
                throw new HttpBadRequestException(0, "Problem with getting comments");
        }catch (Exception e){
            throw handleException("GET /comments/{commentId}", e);
        }


    }



}
