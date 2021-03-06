package edu.cmu.andrew.sweetkoala.server.http.interfaces;

import edu.cmu.andrew.sweetkoala.server.exceptions.AppException;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.sweetkoala.server.http.exceptions.HttpInternalServerException;
import edu.cmu.andrew.sweetkoala.server.models.Customer;
import edu.cmu.andrew.sweetkoala.server.models.Publisher;
import edu.cmu.andrew.sweetkoala.server.utils.AppLogger;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONException;

import javax.ws.rs.WebApplicationException;

public class HttpInterface extends ResourceConfig {

    protected WebApplicationException handleException(String message, Exception e){
        if(e instanceof JSONException)
            return new HttpBadRequestException(-1, "Bad request data provided: " + e.getMessage());
        if (e instanceof AppException)
            return ((AppException) e).getHttpException();

        AppLogger.error(message, e);
        return new HttpInternalServerException(-1);
    }
}

