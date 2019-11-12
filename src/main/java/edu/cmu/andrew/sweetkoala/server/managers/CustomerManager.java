package edu.cmu.andrew.sweetkoala.server.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

    public void updateCustomer( Customer customer) throws AppException {
        try {


            Bson filter = new Document("_id", new ObjectId(customer.getCustomer_Id()));
            Bson newValue = new Document()
                    .append("first_name", customer.getFirst_name())
                    .append("last_name", customer.getLast_name())
                    .append("username", customer.getUsername())
                    .append("password", customer.getPassword())
                    .append("email",customer.getEmail())
                    .append("phone",customer.getPhone())
                    .append("preference_type_id",customer.getPreference_type_id())
                    .append("coin",customer.getCoin());
            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                customerCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update customer details");

        } catch(Exception e) {
            throw handleException("Update Customer", e);
        }
    }

    public void deleteCustomer(String customerId) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(customerId));
            customerCollection.deleteOne(filter);
        }catch (Exception e){
            throw handleException("Delete Customer", e);
        }
    }

    public ArrayList<Customer> getCustomerList() throws AppException {
        try{
            ArrayList<Customer> customerList = new ArrayList<>();
            FindIterable<Document> customerDocs = customerCollection.find();
            for(Document customerDoc: customerDocs) {
                Customer customer = new Customer(
                        customerDoc.getObjectId("_id").toString(),
                        customerDoc.getString("first_name"),
                        customerDoc.getString("last_name"),
                        customerDoc.getString("username"),
                        customerDoc.getString("password"),
                        customerDoc.getString("email"),
                        customerDoc.getString("phone"),
                        customerDoc.getString("preference_type_id"),
                        customerDoc.getInteger("coin")
                );
                customerList.add(customer);
            }
            return new ArrayList<>(customerList);
        } catch(Exception e){
            throw handleException("Get Customer List", e);
        }
    }

    public ArrayList<Customer> getCustomerListSorted(String sortby) throws AppException {
        try{
            ArrayList<Customer> customerList = new ArrayList<>();
            BasicDBObject sortParams = new BasicDBObject();
            sortParams.put(sortby, 1);
            FindIterable<Document> customerDocs = customerCollection.find().sort(sortParams);
            for(Document customerDoc: customerDocs) {
                Customer customer = new Customer(
                        customerDoc.getObjectId("_id").toString(),
                        customerDoc.getString("first_name"),
                        customerDoc.getString("last_name"),
                        customerDoc.getString("username"),
                        customerDoc.getString("password"),
                        customerDoc.getString("email"),
                        customerDoc.getString("phone"),
                        customerDoc.getString("preference_type_id"),
                        customerDoc.getInteger("coin")
                );
                customerList.add(customer);
            }
            return new ArrayList<>(customerList);
        } catch(Exception e){
            throw handleException("Get Customer List", e);
        }
    }

    public ArrayList<Customer> getCustomerListPaginated(Integer offset, Integer count) throws AppException {
        try{
            ArrayList<Customer> customerList = new ArrayList<>();
            BasicDBObject sortParams = new BasicDBObject();
            sortParams.put("coin", 1);
            FindIterable<Document> customerDocs = customerCollection.find().sort(sortParams).skip(offset).limit(count);
            for(Document customerDoc: customerDocs) {
                Customer customer = new Customer(
                        customerDoc.getObjectId("_id").toString(),
                        customerDoc.getString("first_name"),
                        customerDoc.getString("last_name"),
                        customerDoc.getString("username"),
                        customerDoc.getString("password"),
                        customerDoc.getString("email"),
                        customerDoc.getString("phone"),
                        customerDoc.getString("preference_type_id"),
                        customerDoc.getInteger("coin")
                );
                customerList.add(customer);
            }
            return new ArrayList<>(customerList);
        } catch(Exception e){
            throw handleException("Get Customer List", e);
        }
    }

    public ArrayList<Customer> getCustomerListFliterByTypeId(String preference_type_id) throws AppException {
        try{
            ArrayList<Customer> customerList = new ArrayList<>();
            FindIterable<Document> customerDocs = customerCollection.find();
            for(Document customerDoc: customerDocs) {
                if (customerDoc.getString("preference_type_id").equals(preference_type_id)) {
                    Customer customer = new Customer(
                            customerDoc.getObjectId("_id").toString(),
                            customerDoc.getString("first_name"),
                            customerDoc.getString("last_name"),
                            customerDoc.getString("username"),
                            customerDoc.getString("password"),
                            customerDoc.getString("email"),
                            customerDoc.getString("phone"),
                            customerDoc.getString("preference_type_id"),
                            customerDoc.getInteger("coin")
                    );
                    customerList.add(customer);
                }
            }
            return new ArrayList<>(customerList);
        } catch(Exception e){
            throw handleException("Get Customer List", e);
        }
    }

    public ArrayList<Customer> getCustomerById(String id) throws AppException {
        try{
            ArrayList<Customer> customerList = new ArrayList<>();
            FindIterable<Document> customerDocs = customerCollection.find();
            for(Document customerDoc: customerDocs) {
                if(customerDoc.getObjectId("_id").toString().equals(id)) {
                    Customer customer = new Customer(
                            customerDoc.getObjectId("_id").toString(),
                            customerDoc.getString("first_name"),
                            customerDoc.getString("last_name"),
                            customerDoc.getString("username"),
                            customerDoc.getString("password"),
                            customerDoc.getString("email"),
                            customerDoc.getString("phone"),
                            customerDoc.getString("preference_type_id"),
                            customerDoc.getInteger("coin")
                    );
                    customerList.add(customer);
                }
            }
            return new ArrayList<>(customerList);
        } catch(Exception e){
            throw handleException("Get Customer List", e);
        }
    }
    public void resetCustomers() throws AppException {
        try{
            customerCollection.drop();

            Customer newcustomer1 = new Customer(
                    null,
                    "Zihan",
                    "Cao",
                    "Koalarubio",
                    "123456",
                    "zihanrubio@gmail.com",
                    "6692649059",
                    "1",
                    2
            );
            this.getInstance().createCustomer(newcustomer1);

            Customer newcustomer2 = new Customer(
                    null,
                    "Zuotian",
                    "Li",
                    "Lizuotian",
                    "1234567890",
                    "lizuotian@gmail.com",
                    "1234567890",
                    "2",
                    10
            );
            this.getInstance().createCustomer(newcustomer2);

            Customer newcustomer3 = new Customer(
                    null,
                    "Xinyong",
                    "Lyu",
                    "Lyuxinyong",
                    "12345678",
                    "lyuxinyong@gmail.com",
                    "2345678901",
                    "2",
                    20
            );
            this.getInstance().createCustomer(newcustomer3);

        } catch(Exception e){
            throw handleException("Reset Customer List", e);
        }
    }

}
