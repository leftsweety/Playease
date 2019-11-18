package edu.cmu.andrew.sweetkoala.server.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppException;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.sweetkoala.server.models.Comment;
import edu.cmu.andrew.sweetkoala.server.models.Weather;
import edu.cmu.andrew.sweetkoala.server.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;


import java.lang.String;
import java.util.ArrayList;

public class WeatherManager extends Manager {
    public static WeatherManager _self;
    private MongoCollection<Document> weatherCollection;


    public WeatherManager() {
        this.weatherCollection = MongoPool.getInstance().getCollection("weathers");
    }

    public static WeatherManager getInstance(){
        if (_self == null)
            _self = new WeatherManager();
        return _self;
    }


    public void createWeather(Weather weather) throws AppException {

        try{
            JSONObject json = new JSONObject(weather);

            Document newDoc = new Document()
<<<<<<< HEAD
                    .append("event_weather", weather.getType_id()).append("status",weather.getStatus());
=======
                    .append("type_id", weather.getType_id()).append("status",weather.getStatus());
>>>>>>> 1.1.3 Version for Zuotian Li
            if (newDoc != null)
                weatherCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new weather");

        }catch(Exception e){
            throw handleException("Create Weather", e);
        }

    }

    public ArrayList<Weather> getWeatherList() throws AppException {
        try{
            ArrayList<Weather> weatherList = new ArrayList<>();
            FindIterable<Document> weatherDocs = weatherCollection.find();
            for(Document weatherDoc: weatherDocs) {
                Weather weather = new Weather(
                        weatherDoc.getObjectId("_id").toString(),
                        weatherDoc.getString("type_id"),
                        weatherDoc.getString("status")
                );
                weatherList.add(weather);
            }
            return new ArrayList<>(weatherList);
        } catch(Exception e){
            throw handleException("Get Weather List", e);
        }
    }


    public ArrayList<Weather> getWeatherById(String id) throws AppException {
        try{
            ArrayList<Weather> weatherList = new ArrayList<>();
            FindIterable<Document> weatherDocs = weatherCollection.find();
            for(Document weatherDoc: weatherDocs) {
                if(weatherDoc.getObjectId("_id").toString().equals(id)) {
                    Weather weather = new Weather(
                            weatherDoc.getObjectId("_id").toString(),
                            weatherDoc.getString("type_id"),
                            weatherDoc.getString("status")
                    );
                    weatherList.add(weather);
                }
            }
            return new ArrayList<>(weatherList);
        } catch(Exception e){
            throw handleException("Get Weather List", e);
        }
    }

    public void reSetWeather(){

        MongoClient mongoCli = new MongoClient();
        MongoDatabase db = mongoCli.getDatabase("playease");
        MongoCollection<Document> collection = db.getCollection("weathers");
        collection.drop();
        db.createCollection("weathers");
        weatherInsert(db,"weathers","","111","Snow");
        weatherInsert(db,"weathers","","112","Rainy");
        weatherInsert(db,"weathers","","113","Sunny");

    }
    private static void weatherInsert(MongoDatabase db, String collectionName,String weather_id, String type_id, String status){
        MongoCollection<Document> collection = db.getCollection(collectionName);
        Document document = new Document().append("type_id", type_id).append("status",status);
        collection.insertOne(document);
    }
}
