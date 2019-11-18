package edu.cmu.andrew.sweetkoala.server.managers;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppException;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.sweetkoala.server.models.Rating;
import edu.cmu.andrew.sweetkoala.server.utils.MongoPool;
import org.bson.Document;
import org.json.JSONObject;


import java.lang.String;
import java.util.ArrayList;

public class RatingManager extends Manager {
    public static RatingManager _self;
    private MongoCollection<Document> ratingCollection;


    public RatingManager() {
        this.ratingCollection = MongoPool.getInstance().getCollection("ratings");
    }

    public static RatingManager getInstance(){
        if (_self == null)
            _self = new RatingManager();
        return _self;
    }


    public void createRating(Rating rating) throws AppException {

        try{
            JSONObject json = new JSONObject(rating);

            Document newDoc = new Document()
                    .append("customer_id", rating.getCustomer_id()).append("publisher_id",rating.getPublisher_id()).append("star",rating.getStar());
            if (newDoc != null)
                ratingCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new rating");

        }catch(Exception e){
            throw handleException("Create Rating", e);
        }

    }

    public ArrayList<Rating> getRatingList() throws AppException {
        try{
            ArrayList<Rating> ratingList = new ArrayList<>();
            FindIterable<Document> ratingDocs = ratingCollection.find();
            for(Document ratingDoc: ratingDocs) {
                Rating rating = new Rating(
                        ratingDoc.getObjectId("_id").toString(),
                        ratingDoc.getString("customer_id"),
                        ratingDoc.getString("publisher_id"),
                        ratingDoc.getInteger("star")
                );
                ratingList.add(rating);
            }
            return new ArrayList<>(ratingList);
        } catch(Exception e){
            throw handleException("Get Rating List", e);
        }
    }


    public ArrayList<Rating> getRatingById(String id) throws AppException {
        try{
            ArrayList<Rating> ratingList = new ArrayList<>();
            FindIterable<Document> ratingDocs = ratingCollection.find();
            for(Document ratingDoc: ratingDocs) {
                if(ratingDoc.getObjectId("_id").toString().equals(id)) {
                    Rating rating = new Rating(
                            ratingDoc.getObjectId("_id").toString(),
                            ratingDoc.getString("customer_id"),
                            ratingDoc.getString("publisher_id"),
                            ratingDoc.getInteger("star")
                    );
                    ratingList.add(rating);
                }
            }
            return new ArrayList<>(ratingList);
        } catch(Exception e){
            throw handleException("Get Rating List", e);
        }
    }

    public void reSetRating(){

        MongoClient mongoCli = new MongoClient();
        MongoDatabase db = mongoCli.getDatabase("playease");
        MongoCollection<Document> collection = db.getCollection("ratings");
        collection.drop();
        db.createCollection("ratings");
        ratingInsert(db,"ratings","","111","111",4);
        ratingInsert(db,"ratings","","112","123",4);
        ratingInsert(db,"ratings","","113","345",5);

    }
    private static void ratingInsert(MongoDatabase db, String collectionName,String rating_id, String customer_id, String publisher_id, Integer star){
        MongoCollection<Document> collection = db.getCollection(collectionName);
        Document document = new Document().append("customer_id", customer_id).append("publisher_id",publisher_id).append("star", star);
        collection.insertOne(document);
    }
}
