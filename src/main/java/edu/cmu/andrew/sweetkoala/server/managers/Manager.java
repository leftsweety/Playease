package edu.cmu.andrew.sweetkoala.server.managers;

import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppException;
import edu.cmu.andrew.sweetkoala.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.sweetkoala.server.utils.MongoPool;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.bson.Document;

public class Manager {
    protected MongoCollection<Document> userCollection;
    protected MongoCollection<Document> sessionCollection;

    protected MongoCollection<Document> publisherCollection;
<<<<<<< HEAD
=======
    protected MongoCollection<Document> commentCollection;
    protected MongoCollection<Document> typeCollection;
    protected MongoCollection<Document> weatherCollection;
    protected MongoCollection<Document> ratingCollection;
>>>>>>> Playease 1.1.3 for Zuotian Li

    public Manager() {
        this.userCollection = MongoPool.getInstance().getCollection("users");
        this.publisherCollection = MongoPool.getInstance().getCollection("publishers");
<<<<<<< HEAD
=======
        this.commentCollection = MongoPool.getInstance().getCollection("comments");
        this.typeCollection = MongoPool.getInstance().getCollection("types");
        this.weatherCollection = MongoPool.getInstance().getCollection("weathers");
        this.ratingCollection = MongoPool.getInstance().getCollection("ratings");
>>>>>>> Playease 1.1.3 for Zuotian Li
    }

    protected AppException handleException(String message, Exception e){
        if (e instanceof AppException && !(e instanceof AppInternalServerException))
            return (AppException)e;
        AppLogger.error(message, e);
        return new AppInternalServerException(-1);
    }
}
