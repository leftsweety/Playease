package edu.cmu.andrew.sweetkoala.server.models;


public class Type {

    String type_id = null;
    String event_type = null;


    public Type(String id, String event_type) {
        this.type_id = id;
        this.event_type = event_type;
    }

    public void setId(String id){ this.type_id = id; }

    public String getType_id() {
        return type_id;
    }

    public String getEvent_type() { return event_type; }
}
