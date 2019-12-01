package edu.cmu.andrew.sweetkoala.server.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppException;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.sweetkoala.server.models.*;
import edu.cmu.andrew.sweetkoala.server.utils.MongoPool;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
public class Manager {

    public static Manager _self;

    protected MongoCollection<Document> userCollection;
    protected MongoCollection<Document> sessionCollection;

    protected MongoCollection<Document> publisherCollection;
    protected MongoCollection<Document> commentCollection;
    protected MongoCollection<Document> typeCollection;
    protected MongoCollection<Document> weatherCollection;
    protected MongoCollection<Document> ratingCollection;
    protected MongoCollection<Document> customerCollection;
    private MongoCollection<Document> eventCollection;
    private MongoCollection<Document> orderCollection;
    private MongoCollection<Document> favorite_LocationCollection;
    private MongoCollection<Document> paymentCollection;



    public Manager() {

        this.userCollection = MongoPool.getInstance().getCollection("users");
        this.publisherCollection = MongoPool.getInstance().getCollection("publishers");
        this.commentCollection = MongoPool.getInstance().getCollection("comments");
        this.typeCollection = MongoPool.getInstance().getCollection("types");
        this.weatherCollection = MongoPool.getInstance().getCollection("weathers");
        this.ratingCollection = MongoPool.getInstance().getCollection("ratings");
        this.customerCollection = MongoPool.getInstance().getCollection("customers");
        this.eventCollection = MongoPool.getInstance().getCollection("events");
        this.favorite_LocationCollection = MongoPool.getInstance().getCollection("favorite_locations");
        this.orderCollection = MongoPool.getInstance().getCollection("orders");
        this.paymentCollection = MongoPool.getInstance().getCollection("payments");
    }

    public static Manager getInstance(){
        if (_self == null)
            _self = new Manager();
        return _self;
    }

    protected AppException handleException(String message, Exception e){
        if (e instanceof AppException && !(e instanceof AppInternalServerException))
            return (AppException)e;
        AppLogger.error(message, e);
        return new AppInternalServerException(-1);
    }

    public String findObjectId(String keyName, String name, MongoCollection<Document> collection){
        FindIterable<Document> iter = collection.find(Filters.eq(keyName, name));
        String temp = iter.first().getObjectId("_id").toString();
        return temp;
    }

    public void resetOverall() throws AppException {
        try{
            MongoClient mongoCli = new MongoClient();
            MongoDatabase db = mongoCli.getDatabase("playease");

            customerCollection.drop();
            publisherCollection.drop();
            typeCollection.drop();
            weatherCollection.drop();
            eventCollection.drop();
            favorite_LocationCollection.drop();
            orderCollection.drop();
            paymentCollection.drop();
            ratingCollection.drop();
            commentCollection.drop();

            new CustomerManager().getInstance().resetCustomers();
            new TypeManager().getInstance().reSetType();

            String type1 = this.findObjectId("event_type","Movie", typeCollection);
            String type2 = this.findObjectId("event_type","KTV", typeCollection);

            PublisherManager publisherManager = new PublisherManager();
            publisherManager.getInstance().publisherInsert(db,"publishers","","Rubio","Cao","Koala","123","yo@gmail.com","88888888","hangzhou",type1,736);
            publisherManager.getInstance().publisherInsert(db,"publishers","","Tony","ritu","Rao","89qa","5790dfgh@hlk.com","856788","hangzhou",type2,5796);

            WeatherManager weatherManager = new WeatherManager();
            weatherManager.getInstance().weatherInsert(db,"weathers","",type1,"Rainy");
            weatherManager.getInstance().weatherInsert(db,"weathers","",type2,"Sunny");

            String publisher1 = this.findObjectId("first_name","Rubio",publisherCollection);
            String publisher2 = this.findObjectId("first_name","Tony", publisherCollection);

            EventManager eventManager = new EventManager();
            Event newEvent1 = new Event(null, publisher1, "A new Movie", type1, "2019-09-05", "11:01:11", "Mtn View", 120, 6, 2, "Frozen2");
            eventManager.getInstance().createEvent(newEvent1);
            Event newEvent2 = new Event(null, publisher2, "A KTV", type2, "2019-11-05", "11:11:11", "Mtn View", 100, 5, 3, "Jay Chou");
            eventManager.getInstance().createEvent(newEvent2);

            String cus1 = this.findObjectId("first_name","Xinyong",customerCollection);
            String cus2 = this.findObjectId("first_name","Zuotian",customerCollection);
            String event1 = this.findObjectId("name","A new Movie", eventCollection);
            String event2 = this.findObjectId("name","A KTV", eventCollection);
            Favorite_LocationManager favorite_locationManager = new Favorite_LocationManager();
            Favorite_Location favorite_location1 = new Favorite_Location("", cus1, event1);
            Favorite_Location favorite_location2 = new Favorite_Location("", cus2, event2);
            favorite_locationManager.getInstance().createFavorite_Location(favorite_location1);
            favorite_locationManager.getInstance().createFavorite_Location(favorite_location2);

            OrderManager orderManager = new OrderManager();
            Order newOrder1 = new Order(null, cus1, "2019-11-22", "11:11:11", 5, "Cancelled");
            orderManager.getInstance().createOrder(newOrder1);
            Order newOrder2 = new Order(null, cus2, "2019-10-22", "11:17:11", 4, "Completed");
            orderManager.getInstance().createOrder(newOrder2);

            String order1 = this.findObjectId("date","2019-11-22", orderCollection);
            String order2 = this.findObjectId("date","2019-10-22", orderCollection);
            PaymentManager paymentManager = new PaymentManager();
            paymentManager.getInstance().paymentInsert(db,"payments","",order1,69,"15-5-5","07-08-45");
            paymentManager.getInstance().paymentInsert(db,"payments","",order2,58,"15-6-5","03-08-45");

            RatingManager ratingManager = new RatingManager();
            ratingManager.ratingInsert(db,"ratings","",cus1,publisher1,5);
            ratingManager.ratingInsert(db,"ratings","",cus2,publisher2,4);

            CommentManager commentManager = new CommentManager();
            commentManager.commentInsert(db,"comments","",cus1,event1,"Cozy Room");
            commentManager.commentInsert(db,"comments","",cus2,event2,"Nice Voice");


        } catch(Exception e){
            throw handleException("Reset Customer List", e);
        }
    }
}
