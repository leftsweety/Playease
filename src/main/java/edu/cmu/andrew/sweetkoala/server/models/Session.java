package edu.cmu.andrew.sweetkoala.server.models;

import edu.cmu.andrew.sweetkoala.server.utils.APPCrypt;

import java.util.UUID;

public class Session {

    public  String token = null;
    public String userId = null;
    public String username = null;

    public Session(User user) throws Exception{
        this.userId = user.id;
        //this.token = APPCrypt.encrypt(user.id);
        this.token = UUID.randomUUID().toString();
        this.username = user.username;
    }

    public Session(Customer customer) throws Exception{
        this.userId = customer.customer_id;
        //this.token = APPCrypt.encrypt(user.id);
        this.token = UUID.randomUUID().toString();
        this.username = customer.username;
    }

    public Session(Publisher publisher) throws Exception{
        this.userId = publisher.publisher_id;
        //this.token = APPCrypt.encrypt(user.id);
        this.token = UUID.randomUUID().toString();
        this.username = publisher.username;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}

