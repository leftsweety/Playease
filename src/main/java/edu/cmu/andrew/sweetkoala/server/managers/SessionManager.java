package edu.cmu.andrew.sweetkoala.server.managers;

import edu.cmu.andrew.sweetkoala.server.exceptions.*;
import edu.cmu.andrew.sweetkoala.server.models.Session;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.sweetkoala.server.models.Customer;
import edu.cmu.andrew.sweetkoala.server.models.Publisher;
import edu.cmu.andrew.sweetkoala.server.utils.APPCrypt;
import edu.cmu.andrew.sweetkoala.server.utils.MongoPool;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.core.HttpHeaders;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SessionManager {

    private static SessionManager self;
    private ObjectWriter ow;
    private MongoCollection<Document> customerCollection;
    private MongoCollection<Document> publisherCollection;
    public static HashMap<String,Session> SessionMapCustomer = new HashMap<String, Session>();
    public static HashMap<String,Session> SessionMapPublisher = new HashMap<String, Session>();

    private SessionManager() {
        this.customerCollection = MongoPool.getInstance().getCollection("customers");
        this.publisherCollection = MongoPool.getInstance().getCollection("publishers");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    public static SessionManager getInstance(){
        if (self == null)
            self = new SessionManager();
        return self;
    }

    public Session create(Object request, int userType) throws AppException {

        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));

            if (!json.has("email"))
                throw new AppBadRequestException(55, "missing email");
            if (!json.has("password"))
                throw new AppBadRequestException(55, "missing password");
            BasicDBObject query = new BasicDBObject();

            //System.out.println(json.getString("email"));
            //System.out.println(json.getString("password"));

            query.put("email", json.getString("email"));
            //query.put("password", APPCrypt.encrypt(json.getString("password")));
            query.put("password", json.getString("password"));

            if (userType == 0) //customer
            {
                Document item = customerCollection.find(query).first();
                if (item == null) {
                    Manager.getInstance().setRole(-1);
                    throw new AppNotFoundException(0, "No customer found matching credentials");
                }

                Customer customer = convertDocumentToCustomer(item);

                customer.setCustomer_Id(item.getObjectId("_id").toString());
                Session sessionVal = new Session(customer);
                Manager.getInstance().setCustomer(customer);
                Manager.getInstance().setRole(0);
                SessionMapCustomer.put(sessionVal.token,sessionVal);
                return sessionVal;
            }
            else if (userType == 1) //publisher
            {
                Document item = publisherCollection.find(query).first();
                if (item == null) {
                    Manager.getInstance().setRole(-1);
                    throw new AppNotFoundException(0, "No publisher found matching credentials");
                }

                Publisher publisher = convertDocumentToPublisher(item);

                publisher.setPublisher_id(item.getObjectId("_id").toString());
                Session sessionVal = new Session(publisher);
                Manager.getInstance().setPublisher(publisher);
                Manager.getInstance().setRole(1);
                SessionMapPublisher.put(sessionVal.token,sessionVal);
                return sessionVal;
            }
            else
                throw new AppNotFoundException(0, "No user type found matching credentials");
        }
        catch (JsonProcessingException e) {
            throw new AppBadRequestException(33, e.getMessage());
        }
        catch (AppBadRequestException e) {
            throw e;
        }
        catch (AppNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new AppInternalServerException(0, e.getMessage());
        }

    }

    private Customer convertDocumentToCustomer(Document customerDoc) {
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
        return customer;
    }

    private Publisher convertDocumentToPublisher(Document publisherDoc) {
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
        return publisher;
    }

    public Session getSessionForTokenCustomer(HttpHeaders headers) throws Exception{
        List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
        if (authHeaders == null)
            throw new AppUnauthorizedException(70,"No Authorization Headers");
        String token = authHeaders.get(0);

        if(SessionManager.getInstance().SessionMapCustomer.containsKey(token))
            return SessionManager.getInstance().SessionMapCustomer.get(token);
        else
            throw new AppUnauthorizedException(70,"Invalid Token");

    }

    public Session getSessionForTokenPublisher(HttpHeaders headers) throws Exception{
        List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
        if (authHeaders == null)
            throw new AppUnauthorizedException(70,"No Authorization Headers");
        String token = authHeaders.get(0);

        if(SessionManager.getInstance().SessionMapPublisher.containsKey(token))
            return SessionManager.getInstance().SessionMapPublisher.get(token);
        else
            throw new AppUnauthorizedException(70,"Invalid Token");

    }

    public ArrayList getHomePageInfo() throws Exception {
        if (Manager.getInstance().getRole() == -1)
            throw new AppUnauthorizedException(70,"Invalid User");
        else if (Manager.getInstance().getRole() == -0) {
            ArrayList<Customer> customerList = new ArrayList<Customer>();
            customerList.add(Manager.getInstance().getCustomer());
            return (customerList);
        }
        else {
            ArrayList<Publisher> publisherList = new ArrayList<Publisher>();
            publisherList.add(Manager.getInstance().getPublisher());
            return (publisherList);
        }
    }
}
