package edu.cmu.andrew.sweetkoala.server.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppException;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.sweetkoala.server.models.Comment;
import edu.cmu.andrew.sweetkoala.server.utils.MongoPool;
import org.bson.Document;
import org.json.JSONObject;


import java.lang.String;
import java.util.ArrayList;

public class CommentManager extends Manager {
    public static CommentManager _self;
    private MongoCollection<Document> commentCollection;


    public CommentManager() {
        this.commentCollection = MongoPool.getInstance().getCollection("comments");
    }

    public static CommentManager getInstance(){
        if (_self == null)
            _self = new CommentManager();
        return _self;
    }


    public void createComment(Comment comment) throws AppException {

        try{
            JSONObject json = new JSONObject(comment);

            Document newDoc = new Document()
                    .append("event_id", comment.getEvent_id())
                    .append("customer_id", comment.getCustomer_id())
                    .append("description",comment.getDescription());
            if (newDoc != null)
                commentCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new comment");

        }catch(Exception e){
            throw handleException("Create Comment", e);
        }

    }

    public ArrayList<Comment> getCommentList() throws AppException {
        try{
            ArrayList<Comment> commentList = new ArrayList<>();
            FindIterable<Document> commentDocs = commentCollection.find();
            for(Document commentDoc: commentDocs) {
                Comment comment = new Comment(
                        commentDoc.getObjectId("_id").toString(),
                        commentDoc.getString("event_id"),
                        commentDoc.getString("customer_id"),
                        commentDoc.getString("description")
                );
                commentList.add(comment);
            }
            return new ArrayList<>(commentList);
        } catch(Exception e){
            throw handleException("Get Comment List", e);
        }
    }


    public ArrayList<Comment> getCommentById(String id) throws AppException {
        try{
            ArrayList<Comment> commentList = new ArrayList<>();
            FindIterable<Document> commentDocs = commentCollection.find();
            for(Document commentDoc: commentDocs) {
                if(commentDoc.getObjectId("_id").toString().equals(id)) {
                    Comment comment = new Comment(
                            commentDoc.getObjectId("_id").toString(),
                            commentDoc.getString("event_id"),
                            commentDoc.getString("customer_id"),
                            commentDoc.getString("description")
                    );
                    commentList.add(comment);
                }
            }
            return new ArrayList<>(commentList);
        } catch(Exception e){
            throw handleException("Get Comment List", e);
        }
    }

    public void reSetComment(){

        MongoClient mongoCli = new MongoClient();
        MongoDatabase db = mongoCli.getDatabase("playease");
        MongoCollection<Document> collection = db.getCollection("comments");
        collection.drop();
        db.createCollection("comments");
        commentInsert(db,"comments","","111","456","Cozy Room");
        commentInsert(db,"comments","","363","123","Amazing hiking");
        commentInsert(db,"comments","","324","234","I like this movie so much!");

    }
    static void commentInsert(MongoDatabase db, String collectionName, String comment_id, String customer_id, String event_id, String description){
        MongoCollection<Document> collection = db.getCollection(collectionName);
        Document document = new Document().append("customer_id", customer_id).append("event_id", event_id)
                .append("description", description);
        collection.insertOne(document);
    }

}
