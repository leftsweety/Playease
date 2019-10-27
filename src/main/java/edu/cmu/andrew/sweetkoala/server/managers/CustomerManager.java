package edu.cmu.andrew.sweetkoala.server.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppException;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.sweetkoala.server.models.Customer;
import edu.cmu.andrew.sweetkoala.server.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;


import java.lang.String;
import java.util.ArrayList;

public class CustomerManager extends Manager {

    public static CustomerManager _self;
    private MongoCollection<Document> customerCollection;


    public CustomerManager() {
        this.customerCollection = MongoPool.getInstance().getCollection("customers");
    }

    public static CustomerManager getInstance(){
        if (_self == null)
            _self = new CustomerManager();
        return _self;
    }

    public void createCustomer(Customer customer) throws AppException {

        try{
            JSONObject json = new JSONObject(customer);

            Document newDoc = new Document()
                    .append("first_name", customer.getFirst_name())
                    .append("last_name", customer.getLast_name())
                    .append("username", customer.getUsername())
                    .append("password", customer.getPassword())
                    .append("email",customer.getEmail())
                    .append("phone",customer.getPhone())
                    .append("preference_type_id",customer.getPreference_type_id())
                    .append("coin",customer.getCoin());
            if (newDoc != null)
                customerCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new customer");

        }catch(Exception e){
            throw handleException("Create Customer", e);
        }

    }

}
