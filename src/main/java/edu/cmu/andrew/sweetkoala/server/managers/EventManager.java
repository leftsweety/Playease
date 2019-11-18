package edu.cmu.andrew.sweetkoala.server.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppException;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.sweetkoala.server.models.Event;
import edu.cmu.andrew.sweetkoala.server.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;


import java.lang.String;
import java.util.ArrayList;

public class EventManager extends Manager {

    public static EventManager _self;
    private MongoCollection<Document> eventCollection;

    public EventManager() {
        this.eventCollection = MongoPool.getInstance().getCollection("events");
    }

    public static EventManager getInstance(){
        if (_self == null)
            _self = new EventManager();
        return _self;
    }

    public void resetEvents() throws AppException {
        try{
            eventCollection.drop();

            Event newEvent1 = new Event(
                    null,
                    "12345678",
                    "Zihan Cao",
                    "12345678",
                    "2019-11-05",
                    "11:11:11",
                    "Mtn View",
                    100,
                    5,
                    3,
                    "Hiking"
            );
            this.getInstance().createEvent(newEvent1);

        } catch(Exception e){
            throw handleException("Reset Customer List", e);
        }
    }

    public void createEvent(Event event) throws AppException {

        try{
            JSONObject json = new JSONObject(event);

            Document newDoc = new Document()
                    .append("publisher_id", event.getPublisher_id())
                    .append("name", event.getName())
                    .append("type_id", event.getType_id())
                    .append("date", event.getDate())
                    .append("time", event.getTime())
                    .append("location", event.getLocation())
                    .append("price", event.getPrice())
                    .append("capacity", event.getCapacity())
                    .append("recommended_people", event.getRecommended_people())
                    .append("description", event.getDescription());
            if (newDoc != null)
                eventCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new event");

        }catch(Exception e){
            throw handleException("Create Event", e);
        }
    }

    public ArrayList<Event> getEventList() throws AppException {
        try{
            ArrayList<Event> eventList = new ArrayList<>();
            FindIterable<Document> eventDocs = eventCollection.find();
            for(Document eventDoc: eventDocs) {
                Event event = new Event(
                        eventDoc.getObjectId("_id").toString(),
                        eventDoc.getString("publisher_id"),
                        eventDoc.getString("name"),
                        eventDoc.getString("type_id"),
                        eventDoc.getString("date"),
                        eventDoc.getString("time"),
                        eventDoc.getString("location"),
                        eventDoc.getInteger("price"),
                        eventDoc.getInteger("capacity"),
                        eventDoc.getInteger("recommended_people"),
                        eventDoc.getString("description")
                );
                eventList.add(event);
            }
            return new ArrayList<>(eventList);
        } catch(Exception e){
            throw handleException("Get Event List", e);
        }
    }

    public ArrayList<Event> getEventById(String id) throws AppException {
        try{
            ArrayList<Event> eventList = new ArrayList<>();
            FindIterable<Document> eventDocs = eventCollection.find();
            for(Document eventDoc: eventDocs) {
                if(eventDoc.getObjectId("_id").toString().equals(id)) {
                    Event event = new Event(
                            eventDoc.getObjectId("_id").toString(),
                            eventDoc.getString("publisher_id"),
                            eventDoc.getString("name"),
                            eventDoc.getString("type_id"),
                            eventDoc.getString("date"),
                            eventDoc.getString("time"),
                            eventDoc.getString("location"),
                            eventDoc.getInteger("price"),
                            eventDoc.getInteger("capacity"),
                            eventDoc.getInteger("recommended_people"),
                            eventDoc.getString("description")
                    );
                    eventList.add(event);
                }
            }
            return new ArrayList<>(eventList);
        } catch(Exception e){
            throw handleException("Get Event List", e);
        }
    }

    public void updateEvent( Event event) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(event.getEvent_Id()));
            Bson newValue = new Document()
                    .append("publisher_id", event.getPublisher_id())
                    .append("name", event.getName())
                    .append("type_id", event.getType_id())
                    .append("date", event.getDate())
                    .append("time", event.getTime())
                    .append("location", event.getLocation())
                    .append("price", event.getPrice())
                    .append("capacity", event.getCapacity())
                    .append("recommended_people", event.getRecommended_people())
                    .append("description", event.getDescription());
            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                eventCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update event details");

        } catch(Exception e) {
            throw handleException("Update Event", e);
        }
    }

    public void deleteEvent(String eventId) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(eventId));
            eventCollection.deleteOne(filter);
        }catch (Exception e){
            throw handleException("Delete Event", e);
        }
    }
}
