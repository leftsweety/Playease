package edu.cmu.andrew.sweetkoala.server.models;

public class Customer {

    String id = null;
    String first_name = null;
    String last_name = null;
    String username = null;
    String password = null;
    String email = null;
    String phone = null;
    String preference_type_id = null;
    Integer coin;

    public Customer(String id, String first_name, String last_name, String username, String password, String email, String phone, String preference_type_id, Integer coin) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.preference_type_id = preference_type_id;
        this.coin = coin;
    }

    public String getId() {
        return id;
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

    public String getPreference_type_id() {
        return preference_type_id;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPreference_type_id(String preference_type_id) {
        this.preference_type_id = preference_type_id;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }



}
