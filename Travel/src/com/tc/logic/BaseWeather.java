//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.logic;

import java.util.ArrayList;

public class BaseWeather
{

    public BaseWeather()
    {
    }

    public String getCityName()
    {
        return cityName;
    }

     public ArrayList<Weather> getWeatherList()
    {
        return weatherList;
    }

    public void setCityName(String s)
    {
        cityName = s;
    }

    public void setWeatherList(ArrayList<Weather> arraylist)
    {
        weatherList = arraylist;
    }

    private String cityName;
    private ArrayList<Weather> weatherList;
}
