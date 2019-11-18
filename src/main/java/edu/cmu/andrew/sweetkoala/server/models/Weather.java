package edu.cmu.andrew.sweetkoala.server.models;

public class Weather {


    String weather_id = null;
    String type_id = null;
    String status = null;

    public Weather(String id, String type_id, String status) {
        this.weather_id = id;
        this.type_id = type_id;
        this.status = status;
    }

    public void setWeather_id(String id){ this.weather_id = id; }

    public String getWeather_id() {
        return weather_id;
    }

    public String getType_id() {
        return type_id;
    }

    public String getStatus() { return status; }

}
