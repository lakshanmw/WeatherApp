package com.example.lakshan.weatherapp.services;

import com.example.lakshan.weatherapp.model.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Lakshan on 10/11/2018.
 */

public interface EndPointService {

    @GET("weather")
    Call<WeatherData> getWeatherData(@Query("id") Integer id, @Query("units") String units, @Query("APPID") String appid);
}
