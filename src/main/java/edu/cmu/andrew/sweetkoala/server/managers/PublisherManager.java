package edu.cmu.andrew.sweetkoala.server.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppException;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.sweetkoala.server.models.Publisher;
import edu.cmu.andrew.sweetkoala.server.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;


import java.lang.String;
import java.util.ArrayList;

public class PublisherManager extends Manager {
    public static PublisherManager _self;
    private MongoCollection<Document> publisherCollection;

    public PublisherManager() {
        this.publisherCollection = MongoPool.getInstance().getCollection("publishers");
    }

    public static PublisherManager getInstance(){
        if (_self == null)
            _self = new PublisherManager();
        return _self;
    }


    public void createPublisher(Publisher publisher) throws AppException {

        try{
            JSONObject json = new JSONObject(publisher);

            Document newDoc = new Document()
                    .append("first_name", publisher.getFirst_name())
                    .append("last_name", publisher.getLast_name())
                    .append("username",publisher.getUsername())
                    .append("password",publisher.getPassword())
                    .append("email",publisher.getEmail())
                    .append("phone",publisher.getPhone())
                    .append("location", publisher.getLocation())
                    .append("type_id",publisher.getType_id())
                    .append("coin", publisher.getCoin());
            if (newDoc != null)
                publisherCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new publisher");

        }catch(Exception e){
            throw handleException("Create Publisher", e);
        }

    }

    public void updatePublisher( Publisher publisher) throws AppException {
        try {


            Bson filter = new Document("_id", new ObjectId(publisher.getPublisher_id()));
            Bson newValue = new Document()
                    .append("first_name", publisher.getFirst_name())
                    .append("last_name", publisher.getLast_name())
                    .append("username",publisher.getUsername())
                    .append("password",publisher.getPassword())
                    .append("email",publisher.getEmail())
                    .append("phone",publisher.getPhone())
                    .append("location", publisher.getLocation())
                    .append("type_id",publisher.getType_id())
                    .append("coin", publisher.getCoin());
            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                publisherCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update publisher details");

        } catch(Exception e) {
            throw handleException("Update Publisher", e);
        }
    }

    public void deletePublisher(String publisherId) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(publisherId));
            publisherCollection.deleteOne(filter);
        }catch (Exception e){
            throw handleException("Delete Publisher", e);
        }
    }

    public ArrayList<Publisher> getPublisherList() throws AppException {
        try{
            ArrayList<Publisher> publisherList = new ArrayList<>();
            FindIterable<Document> publisherDocs = publisherCollection.find();
            for(Document publisherDoc: publisherDocs) {
                Publisher publisher = new Publisher(
                        publisherDoc.getObjectId("_id").toString(),
                        publisherDoc.getString("first_name"),
                        publisherDoc.getString("last_name"),
                        publisherDoc.getString("username"),
                        publisherDoc.getString("password"),
                        publisherDoc.getString("email"),
                        publisherDoc.getString("phone"),
                        publisherDoc.getString("location"),
                        publisherDoc.getString("type_id"),
                        publisherDoc.getInteger("coin")
                );
                publisherList.add(publisher);
            }
            return new ArrayList<>(publisherList);
        } catch(Exception e){
            throw handleException("Get Publisher List", e);
        }
    }

    public ArrayList<Publisher> getPublisherListSorted(String sortby) throws AppException {
        try{
            ArrayList<Publisher> publisherList = new ArrayList<>();
            BasicDBObject sortParams = new BasicDBObject();
            sortParams.put(sortby, 1);
            FindIterable<Document> publisherDocs = publisherCollection.find().sort(sortParams);
            for(Document publisherDoc: publisherDocs) {
                Publisher publisher = new Publisher(
                        publisherDoc.getObjectId("_id").toString(),
                        publisherDoc.getString("first_name"),
                        publisherDoc.getString("last_name"),
                        publisherDoc.getString("username"),
                        publisherDoc.getString("password"),
                        publisherDoc.getString("email"),
                        publisherDoc.getString("phone"),
                        publisherDoc.getString("location"),
                        publisherDoc.getString("type_id"),
                        publisherDoc.getInteger("coin")
                );
                publisherList.add(publisher);
            }
            return new ArrayList<>(publisherList);
        } catch(Exception e){
            throw handleException("Get Publisher List", e);
        }
    }

    public ArrayList<Publisher> getPublisherListFiltered(String filteredBy) throws AppException {
        try {ArrayList<Publisher> publisherList = new ArrayList<>();
        FindIterable<Document> publisherDocs = publisherCollection.find();
        for(Document publisherDoc: publisherDocs) {
            if(publisherDoc.getString("type_id").equals(filteredBy)) {
                Publisher publisher = new Publisher(
                        publisherDoc.getObjectId("_id").toString(),
                        publisherDoc.getString("first_name"),
                        publisherDoc.getString("last_name"),
                        publisherDoc.getString("username"),
                        publisherDoc.getString("password"),
                        publisherDoc.getString("email"),
                        publisherDoc.getString("phone"),
                        publisherDoc.getString("location"),
                        publisherDoc.getString("type_id"),
                        publisherDoc.getInteger("coin")
                );
                publisherList.add(publisher);
            }
        }
        return new ArrayList<>(publisherList);
        } catch(Exception e){
        throw handleException("Get Publisher List", e);
    }
    }

    public ArrayList<Publisher> getPublisherListPaginated(Integer offset, Integer count) throws AppException {
        try{
            ArrayList<Publisher> publisherList = new ArrayList<>();
            BasicDBObject sortParams = new BasicDBObject();
            sortParams.put("riderBalance", 1);
            FindIterable<Document> publisherDocs = publisherCollection.find().sort(sortParams).skip(offset).limit(count);
            for(Document publisherDoc: publisherDocs) {
                Publisher publisher = new Publisher(
                        publisherDoc.getObjectId("_id").toString(),
                        publisherDoc.getString("first_name"),
                        publisherDoc.getString("last_name"),
                        publisherDoc.getString("username"),
                        publisherDoc.getString("password"),
                        publisherDoc.getString("email"),
                        publisherDoc.getString("phone"),
                        publisherDoc.getString("location"),
                        publisherDoc.getString("type_id"),
                        publisherDoc.getInteger("coin")
                );
                publisherList.add(publisher);
            }
            return new ArrayList<>(publisherList);
        } catch(Exception e){
            throw handleException("Get Publisher List", e);
        }
    }

    public ArrayList<Publisher> getPublisherById(String id) throws AppException {
        try{
            ArrayList<Publisher> publisherList = new ArrayList<>();
            FindIterable<Document> publisherDocs = publisherCollection.find();
            for(Document publisherDoc: publisherDocs) {
                if(publisherDoc.getObjectId("_id").toString().equals(id)) {
                    Publisher publisher = new Publisher(
                            publisherDoc.getObjectId("_id").toString(),
                            publisherDoc.getString("first_name"),
                            publisherDoc.getString("last_name"),
                            publisherDoc.getString("username"),
                            publisherDoc.getString("password"),
                            publisherDoc.getString("email"),
                            publisherDoc.getString("phone"),
                            publisherDoc.getString("location"),
                            publisherDoc.getString("type_id"),
                            publisherDoc.getInteger("coin")
                    );
                    publisherList.add(publisher);
                }
            }
            return new ArrayList<>(publisherList);
        } catch(Exception e){
            throw handleException("Get Publisher List", e);
        }
    }

    public void reSetPublisher(){

        MongoClient mongoCli = new MongoClient();
        MongoDatabase db = mongoCli.getDatabase("playease");
        MongoCollection<Document> collection = db.getCollection("publishers");
        collection.drop();
        db.createCollection("publishers");
        publisherInsert(db,"publishers","","Rubio","Cao","Koala","123","yo@gmail.com","88888888","hangzhou","001",736);
        publisherInsert(db,"publishers","","Tony","ritu","Rao","89qa","5790dfgh@hlk.com","856788","hangzhou","003",5796);
        publisherInsert(db,"publishers","","Xinyong","Lyu","aray","703","678@fghj.com","4567","Fuyang","002",5666);


    }
    private static void publisherInsert(MongoDatabase db, String collectionName,String publisher_id, String first_name, String last_name, String username, String password, String email, String phone, String location, String type_id, Integer coin){
        MongoCollection<Document> collection = db.getCollection(collectionName);
        Document document = new Document().append("first_name", first_name).append("last_name", last_name)
                .append("username", username).append("password",password).append("email",email)
                .append("phone",phone).append("location",location).append("type_id",type_id).append("coin",coin);
        collection.insertOne(document);
    }


}
