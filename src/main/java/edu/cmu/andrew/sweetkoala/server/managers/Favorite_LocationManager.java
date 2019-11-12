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

public class Favorite_LocationManager {

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

}
