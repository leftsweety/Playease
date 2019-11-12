package edu.cmu.andrew.sweetkoala.server.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppException;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.sweetkoala.server.models.Order;
import edu.cmu.andrew.sweetkoala.server.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;

public class OrderManager {

    public static OrderManager _self;
    private MongoCollection<Document> orderCollection;


    public OrderManager() {
        this.orderCollection = MongoPool.getInstance().getCollection("orders");
    }

    public static OrderManager getInstance(){
        if (_self == null)
            _self = new OrderManager();
        return _self;
    }

}


