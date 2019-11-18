package edu.cmu.andrew.sweetkoala.server.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppException;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.sweetkoala.server.models.Favorite_Location;
import edu.cmu.andrew.sweetkoala.server.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;


import java.lang.String;
import java.util.ArrayList;

public class Favorite_LocationManager extends Manager{

    public static Favorite_LocationManager _self;
    private MongoCollection<Document> favorite_LocationCollection;


    public Favorite_LocationManager() {
        this.favorite_LocationCollection = MongoPool.getInstance().getCollection("favorite_locations");
    }

    public static Favorite_LocationManager getInstance(){
        if (_self == null)
            _self = new Favorite_LocationManager();
        return _self;
    }

    public void resetFavorite_Locations() throws AppException {
        try{
            favorite_LocationCollection.drop();

            Favorite_Location newFavorite_Location1 = new Favorite_Location(
                    null,
                    "12345678",
                    "Zihan Cao"
            );
            this.getInstance().createFavorite_Location(newFavorite_Location1);

        } catch(Exception e){
            throw handleException("Reset Favorite Location List", e);
        }
    }

    public void createFavorite_Location(Favorite_Location favorite_Location) throws AppException {

        try{
            JSONObject json = new JSONObject(favorite_Location);

            Document newDoc = new Document()
                    .append("customer_id", favorite_Location.getCustomer_id())
                    .append("event_id", favorite_Location.getEvent_id());
            if (newDoc != null)
                favorite_LocationCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new favorite location");

        }catch(Exception e){
            throw handleException("Create Favorite Location", e);
        }
    }

    public ArrayList<Favorite_Location> getFavorite_LocationList() throws AppException {
        try{
            ArrayList<Favorite_Location> favorite_LocationList = new ArrayList<>();
            FindIterable<Document> favorite_LocationDocs = favorite_LocationCollection.find();
            for(Document favorite_LocationDoc: favorite_LocationDocs) {
                Favorite_Location favorite_Location = new Favorite_Location(
                        favorite_LocationDoc.getObjectId("_id").toString(),
                        favorite_LocationDoc.getString("customer_id"),
                        favorite_LocationDoc.getString("event_id")
                );
                favorite_LocationList.add(favorite_Location);
            }
            return new ArrayList<>(favorite_LocationList);
        } catch(Exception e){
            throw handleException("Get Event List", e);
        }
    }

    public ArrayList<Favorite_Location> getFavorite_LocationById(String id) throws AppException {
        try{
            ArrayList<Favorite_Location> favorite_LocationList = new ArrayList<>();
            FindIterable<Document> favorite_LocationDocs = favorite_LocationCollection.find();
            for(Document favorite_LocationDoc: favorite_LocationDocs) {
                if(favorite_LocationDoc.getObjectId("_id").toString().equals(id)) {
                    Favorite_Location favorite_Location = new Favorite_Location(
                            favorite_LocationDoc.getObjectId("_id").toString(),
                            favorite_LocationDoc.getString("customer_id"),
                            favorite_LocationDoc.getString("event_id")
                    );
                    favorite_LocationList.add(favorite_Location);
                }
            }
            return new ArrayList<>(favorite_LocationList);
        } catch(Exception e){
            throw handleException("Get Favorite Location List", e);
        }
    }

    public void updateFavorite_Location( Favorite_Location favorite_Location) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(favorite_Location.getFavorite_location_id()));
            Bson newValue = new Document()
                    .append("customer_id", favorite_Location.getCustomer_id())
                    .append("event_id", favorite_Location.getEvent_id());
            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                favorite_LocationCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update favorite location details");

        } catch(Exception e) {
            throw handleException("Update Favorite Location", e);
        }
    }

    public void deleteFavorite_Location(String favorite_LocationId) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(favorite_LocationId));
            favorite_LocationCollection.deleteOne(filter);
        }catch (Exception e){
            throw handleException("Delete Favorite Location", e);
        }
    }

}
