package edu.cmu.andrew.sweetkoala.server.models;

public class Event {

    String event_id = null;
    String publisher_id = null;
    String name = null;
    String type_id = null;
    String date = null;
    String time = null;
    String location = null;
    Integer price = -1;
    Integer capacity = -1;
    Integer recommended_people = -1;
    String description = null;

    public Event(String event_id, String publisher_id, String name, String type_id, String date, String time, String location, Integer price, Integer capacity, Integer recommended_people, String description) {
        this.event_id = event_id;
        this.publisher_id = publisher_id;
        this.name = name;
        this.type_id = type_id;
        this.date = date;
        this.time = time;
        this.location = location;
        this.price = price;
        this.capacity = capacity;
        this.recommended_people = recommended_people;
        this.description = description;
    }

    public String getEvent_Id() {
        return event_id;
    }

    public String getPublisher_id() {
        return publisher_id;
    }

    public String getName() {
        return name;
    }

    public String getType_id() {
        return type_id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getRecommended_people() {
        return recommended_people;
    }

    public String getDescription() {
        return description;
    }

    public void setEvent_Id(String event_id) {
        this.event_id = event_id;
    }

    public void setPublisher_id(String publisher_id) {
        this.publisher_id = publisher_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setRecommended_people(Integer recommended_people) {
        this.recommended_people = recommended_people;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
