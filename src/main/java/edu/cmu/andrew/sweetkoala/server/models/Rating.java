package edu.cmu.andrew.sweetkoala.server.models;

public class Rating {

    String rate_id = null;
    String customer_id = null;
    String publisher_id = null;
    Integer star;


    public Rating(String rate_id, String customer_id, String publisher_id, Integer star) {
        this.rate_id = rate_id;
        this.customer_id = customer_id;
        this.publisher_id = publisher_id;
        this.star = star;
    }

    public void setRate_id(String id){ this.rate_id = id; }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getRate_id(){
        return rate_id;
    }

    public String getPublisher_id() {
        return publisher_id;
    }

    public Integer getStar() {
        return star;
    }

}
