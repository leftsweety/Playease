package edu.cmu.andrew.sweetkoala.server.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppException;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.sweetkoala.server.models.Comment;
import edu.cmu.andrew.sweetkoala.server.models.Type;
import edu.cmu.andrew.sweetkoala.server.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;


import java.lang.String;
import java.util.ArrayList;

public class TypeManager extends Manager {
    public static TypeManager _self;
    private MongoCollection<Document> typeCollection;


    public TypeManager() {
        this.typeCollection = MongoPool.getInstance().getCollection("types");
    }

    public static TypeManager getInstance(){
        if (_self == null)
            _self = new TypeManager();
        return _self;
    }


    public void createType(Type type) throws AppException {

        try{
            JSONObject json = new JSONObject(type);

            Document newDoc = new Document()
                    .append("event_type", type.getEvent_type());
            if (newDoc != null)
                typeCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new type");

        }catch(Exception e){
            throw handleException("Create Type", e);
        }

    }

    public ArrayList<Type> getTypeList() throws AppException {
        try{
            ArrayList<Type> typeList = new ArrayList<>();
            FindIterable<Document> typeDocs = typeCollection.find();
            for(Document typeDoc: typeDocs) {
                Type type = new Type(
                        typeDoc.getObjectId("_id").toString(),
                        typeDoc.getString("event_type")
                );
                typeList.add(type);
            }
            return new ArrayList<>(typeList);
        } catch(Exception e){
            throw handleException("Get Type List", e);
        }
    }


    public ArrayList<Type> getTypeById(String id) throws AppException {
        try{
            ArrayList<Type> typeList = new ArrayList<>();
            FindIterable<Document> typeDocs = typeCollection.find();
            for(Document typeDoc: typeDocs) {
                if(typeDoc.getObjectId("_id").toString().equals(id)) {
                    Type type = new Type(
                            typeDoc.getObjectId("_id").toString(),
                            typeDoc.getString("event_type")
                    );
                    typeList.add(type);
                }
            }
            return new ArrayList<>(typeList);
        } catch(Exception e){
            throw handleException("Get Type List", e);
        }
    }

    public void reSetType(){

        MongoClient mongoCli = new MongoClient();
        MongoDatabase db = mongoCli.getDatabase("playease");
        MongoCollection<Document> collection = db.getCollection("types");
        collection.drop();
        db.createCollection("types");
        typeInsert(db,"types","","Movie");
        typeInsert(db,"types","","KTV");
        typeInsert(db,"types","","Hiking");

    }
    private static void typeInsert(MongoDatabase db, String collectionName,String type_id, String event_type){
        MongoCollection<Document> collection = db.getCollection(collectionName);
        Document document = new Document().append("event_type", event_type);
        collection.insertOne(document);
    }
}
