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

import java.util.ArrayList;

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

    public void resetOrders() throws AppException {
        try{
            orderCollection.drop();

            Order newOrder1 = new Order(
                    null,
                    "12345678",
                    "2019-11-22",
                    "11:11:11",
                    5,
                    "Cancelled"
            );
            this.getInstance().createOrder(newOrder1);

        } catch(Exception e){
            throw handleException("Reset Order List", e);
        }
    }

    public void createOrder(Order order) throws AppException {

        try{
            JSONObject json = new JSONObject(order);

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

    public ArrayList<Order> getOrderList() throws AppException {
        try{
            ArrayList<Order> orderList = new ArrayList<>();
            FindIterable<Document> orderDocs = orderCollection.find();
            for(Document orderDoc: orderDocs) {
                Order order = new Order(
                        orderDoc.getObjectId("_id").toString(),
                        orderDoc.getString("customer_id"),
                        orderDoc.getString("date"),
                        orderDoc.getString("time"),
                        orderDoc.getInteger("number_of_people"),
                        orderDoc.getString("status")
                );
                orderList.add(order);
            }
            return new ArrayList<>(orderList);
        } catch(Exception e){
            throw handleException("Get Order List", e);
        }
    }

    public ArrayList<Order> getOrderById(String id) throws AppException {
        try{
            ArrayList<Order> orderList = new ArrayList<>();
            FindIterable<Document> orderDocs = orderCollection.find();
            for(Document orderDoc: orderDocs) {
                if(orderDoc.getObjectId("_id").toString().equals(id)) {
                    Order order = new Order(
                            orderDoc.getObjectId("_id").toString(),
                            orderDoc.getString("customer_id"),
                            orderDoc.getString("date"),
                            orderDoc.getString("time"),
                            orderDoc.getInteger("number_of_people"),
                            orderDoc.getString("status")
                    );
                    orderList.add(order);
                }
            }
            return new ArrayList<>(orderList);
        } catch(Exception e){
            throw handleException("Get Order List", e);
        }
    }

    public void updateOrder( Order order) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(order.getOrder_id()));
            Bson newValue = new Document()
                    .append("customer_id", order.getCustomer_id())
                    .append("date", order.getDate())
                    .append("time", order.getTime())
                    .append("number_of_people", order.getNumber_of_people())
                    .append("status", order.getStatus());
            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                orderCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update order details");

        } catch(Exception e) {
            throw handleException("Update Order", e);
        }
    }

    public void deleteOrder(String orderId) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(orderId));
            orderCollection.deleteOne(filter);
        }catch (Exception e){
            throw handleException("Delete Order", e);
        }
    }

}


