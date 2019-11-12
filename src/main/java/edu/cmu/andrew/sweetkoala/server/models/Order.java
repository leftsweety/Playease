package edu.cmu.andrew.sweetkoala.server.models;

public class Order {

    String order_id = null;
    String customer_id = null;
    String date = null;
    String time = null;
    String number_of_people = null;
    String status = null;

    public Order(String order_id, String customer_id, String date, String time, String number_of_people, String status) {
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.date = date;
        this.time = time;
        this.number_of_people = number_of_people;
        this.status = status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getNumber_of_people() {
        return number_of_people;
    }

    public String getStatus() {
        return status;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setNumber_of_people(String number_of_people) {
        this.number_of_people = number_of_people;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}