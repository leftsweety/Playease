package edu.cmu.andrew.sweetkoala.server.models;

public class Favorite_Location {

    String favorite_location_id = null;
    String customer_id = null;
    String event_id = null;

    public Favorite_Location(String favorite_location_id, String customer_id, String event_id) {
        this.favorite_location_id = favorite_location_id;
        this.customer_id = customer_id;
        this.event_id = event_id;
    }

    public String getFavorite_location_id() {
        return favorite_location_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setFavorite_location_id(String favorite_location_id) {
        this.favorite_location_id = favorite_location_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }
}
