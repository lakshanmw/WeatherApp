package com.example.lakshan.weatherapp.model;

/**
 * Created by Lakshan on 10/11/2018.
 */

public class CityDetails {

    private int cityId;
    private String City;

    public CityDetails(int cityId, String city) {
        this.cityId = cityId;
        City = city;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
