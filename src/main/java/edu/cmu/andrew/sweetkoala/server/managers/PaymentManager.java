package edu.cmu.andrew.sweetkoala.server.managers;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppException;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.sweetkoala.server.models.Payment;
import edu.cmu.andrew.sweetkoala.server.utils.MongoPool;
import org.bson.Document;
import org.json.JSONObject;


import java.lang.String;
import java.util.ArrayList;

public class PaymentManager extends Manager {
    public static PaymentManager _self;
    private MongoCollection<Document> paymentCollection;


    public PaymentManager() {
        this.paymentCollection = MongoPool.getInstance().getCollection("payments");
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
<<<<<<< HEAD
        MongoCollection<Document> collection = db.getCollection("Payments");
        collection.drop();
        db.createCollection("payments");
        paymentInsert(db,"payments","","111",69,"15-5-5","07-08-45");
        paymentInsert(db,"payments","","112",56,"","");
        paymentInsert(db,"payments","","113",67,"","");
=======
        MongoCollection<Document> collection = db.getCollection("payments");
        collection.drop();
        db.createCollection("payments");
        paymentInsert(db,"payments","","111",69,"15-5-5","07-08-45");
        paymentInsert(db,"payments","","112",56,"15-5-6","07-08-43");
        paymentInsert(db,"payments","","113",67,"15-5-4","07-08-23");
>>>>>>> 1.1.3 Version for Zuotian Li

    }
    private static void paymentInsert(MongoDatabase db, String collectionName,String payment_id, String order_id, Integer cost, String date, String time){
        MongoCollection<Document> collection = db.getCollection(collectionName);
        Document document = new Document().append("order_id", order_id).append("cost",cost).append("date", date).append("time",time);
        collection.insertOne(document);
    }
}
