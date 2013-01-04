// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

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
