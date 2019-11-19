package edu.cmu.andrew.sweetkoala.server.models;


public class Payment {

    String payment_id = null;
    String order_id = null;
    Integer cost = null;
    String date = null;
    String time = null;



    public Payment(String id, String order_id, int cost, String date, String time) {
        this.payment_id = id;
        this.order_id = order_id;
        this.cost = cost;
        this.date = date;
        this.time = time;
    }

    public void setPayment_id(String id){ this.payment_id = id; }

    public String getOrder_id() {
        return order_id;
    }

    public Integer getCost() {
        return cost;
    }

    public String getDate() {
        return date;
    }

    public String getTime() { return time; }
}
