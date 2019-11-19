package edu.cmu.andrew.sweetkoala.server.models;


public class Comment {

    String comment_id = null;
    String event_id = null;
    String customer_id = null;
    String description = null;


    public Comment(String comment_id, String event_id, String customer_id, String description) {
        this.comment_id = comment_id;
        this.event_id = event_id;
        this.customer_id = customer_id;
        this.description = description;
    }

    public void setComment_id(String id){ this.comment_id = id; }

    public String getComment_id() {
        return comment_id;
    }

    public String getEvent_id(){
        return event_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getDescription() {
        return description;
    }

}
