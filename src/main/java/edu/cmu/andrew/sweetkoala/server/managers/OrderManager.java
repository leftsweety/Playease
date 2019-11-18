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

public class OrderManager extends Manager {

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

    public void createOrder(Order order) throws AppException {

        try{
            JSONObject json = new JSONObject(order);

            String order_id = null;
            String customer_id = null;
            String date = null;
            String time = null;
            String number_of_people = null;
            String status = null;

            Document newDoc = new Document()
                    .append("customer_id", order.getCustomer_id())
                    .append("date", order.getDate())
                    .append("time", order.getTime())
                    .append("number_of_people", order.getNumber_of_people())
                    .append("status", order.getStatus());
            if (newDoc != null)
                orderCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new order");

        }catch(Exception e){
            throw handleException("Create Order", e);
        }
    }

}


