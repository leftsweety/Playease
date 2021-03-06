package edu.cmu.andrew.sweetkoala.server.managers;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppException;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.sweetkoala.server.models.Order;
import edu.cmu.andrew.sweetkoala.server.models.Payment;
import edu.cmu.andrew.sweetkoala.server.utils.MongoPool;
import org.bson.Document;
import org.json.JSONObject;


import java.lang.String;
import java.util.ArrayList;

public class PaymentManager extends Manager {
    public static PaymentManager _self;
    private MongoCollection<Document> paymentCollection;
    private MongoCollection<Document> orderCollection;


    public PaymentManager() {
        this.paymentCollection = MongoPool.getInstance().getCollection("payments");
        this.orderCollection = MongoPool.getInstance().getCollection("orders");
    }

    public static PaymentManager getInstance(){
        if (_self == null)
            _self = new PaymentManager();
        return _self;
    }


    public void createPayment(Payment payment) throws AppException {

        try{
            JSONObject json = new JSONObject(payment);

            Document newDoc = new Document()
                    .append("order_id", payment.getOrder_id()).append("cost",payment.getCost()).append("date",payment.getDate()).append("time",payment.getTime());
            if (newDoc != null)
                paymentCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new payment");

        }catch(Exception e){
            throw handleException("Create Payment", e);
        }

    }

    public ArrayList<Payment> getPaymentList() throws AppException {
        try{
            ArrayList<Payment> paymentList = new ArrayList<>();
            FindIterable<Document> paymentDocs = paymentCollection.find();
            for(Document paymentDoc: paymentDocs) {
                Payment payment = new Payment(
                        paymentDoc.getObjectId("_id").toString(),
                        paymentDoc.getString("order_id"),
                        paymentDoc.getInteger("cost"),
                        paymentDoc.getString("date"),
                        paymentDoc.getString("time")
                );
                paymentList.add(payment);
            }
            return new ArrayList<>(paymentList);
        } catch(Exception e){
            throw handleException("Get Payment List", e);
        }
    }


    public ArrayList<Payment> getPaymentById(String id) throws AppException {
        try{
            ArrayList<Payment> paymentList = new ArrayList<>();
            FindIterable<Document> paymentDocs = paymentCollection.find();
            for(Document paymentDoc: paymentDocs) {
                if(paymentDoc.getObjectId("_id").toString().equals(id)) {
                    Payment payment = new Payment(
                            paymentDoc.getObjectId("_id").toString(),
                            paymentDoc.getString("order_id"),
                            paymentDoc.getInteger("cost"),
                            paymentDoc.getString("date"),
                            paymentDoc.getString("time")
                    );
                    paymentList.add(payment);
                }
            }
            return new ArrayList<>(paymentList);
        } catch(Exception e){
            throw handleException("Get Payment List", e);
        }
    }

    public void reSetPayment(){

        MongoClient mongoCli = new MongoClient();
        MongoDatabase db = mongoCli.getDatabase("playease");
        MongoCollection<Document> collection = db.getCollection("Payments");
        collection.drop();
        db.createCollection("payments");
        paymentInsert(db,"payments","","111",69,"15-5-5","07-08-45");
        paymentInsert(db,"payments","","112",56,"","");
        paymentInsert(db,"payments","","113",67,"","");


    }
    static void paymentInsert(MongoDatabase db, String collectionName, String payment_id, String order_id, Integer cost, String date, String time){
        MongoCollection<Document> collection = db.getCollection(collectionName);
        Document document = new Document().append("order_id", order_id).append("cost",cost).append("date", date).append("time",time);
        collection.insertOne(document);
    }

    public ArrayList<Payment> getPaymentByCustomerId(String id) throws AppException {
        try{

            ArrayList<Order> orderList = new ArrayList<>();
            FindIterable<Document> orderDocs = orderCollection.find();
            for(Document orderDoc: orderDocs) {
                if(orderDoc.getString("customer_id").equals(id)) {
                    Order order = new Order(
                            orderDoc.getObjectId("_id").toString(),
                            orderDoc.getString("event_id"),
                            orderDoc.getString("customer_id"),
                            orderDoc.getString("date"),
                            orderDoc.getString("time"),
                            orderDoc.getInteger("number_of_people"),
                            orderDoc.getString("status")
                    );
                    orderList.add(order);
                }
            }

            ArrayList<Payment> paymentList = new ArrayList<>();
            FindIterable<Document> paymentDocs = paymentCollection.find();
            for(Document paymentDoc: paymentDocs) {
                for(Order myOrder: orderList){
                    if(paymentDoc.getString("order_id").equals(myOrder.getOrder_id())) {
                        Payment payment = new Payment(
                                paymentDoc.getObjectId("_id").toString(),
                                paymentDoc.getString("order_id"),
                                paymentDoc.getInteger("cost"),
                                paymentDoc.getString("date"),
                                paymentDoc.getString("time")
                        );
                        paymentList.add(payment);
                }

                }
            }
            return paymentList;
        } catch(Exception e){
            throw handleException("Get Order List", e);
        }
    }
}
