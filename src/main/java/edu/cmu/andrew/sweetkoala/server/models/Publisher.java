package edu.cmu.andrew.sweetkoala.server.models;

public class Publisher {

    String publisher_id = null;
    String first_name = null;
    String last_name = null;
    String username = null;
    String password= null;
    String email= null;
    String phone  = null;
    String location  = null;
    String type_id  = null;
    Integer coin = null;

    public Publisher(String publisher_id, String first_name, String last_name, String username, String password, String email, String phone, String location, String type_id, Integer coin) {
        this.publisher_id = publisher_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.type_id = type_id;
        this.coin = coin;
    }


    public void setPublisher_id(String publisher_id) {
        this.publisher_id = publisher_id;
    }

    public String getPublisher_id() {
        return publisher_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public String getType_id() {
        return type_id;
    }

    public Integer getCoin() {
        return coin;
    }

}
