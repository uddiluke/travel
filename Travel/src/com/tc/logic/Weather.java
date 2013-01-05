//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.logic;


public class Weather
{

    public Weather()
    {
    }

    public String getCondition()
    {
        return condition;
    }

    public String getCurrently_date()
    {
        return currently_date;
    }

    public String getCurrently_img()
    {
        return currently_img;
    }

    public String getCurrently_week()
    {
        return currently_week;
    }

    public String getTemperature_max()
    {
        return temperature_max;
    }

    public String getTemperature_min()
    {
        return temperature_min;
    }

    public void setCondition(String s)
    {
        condition = s;
    }

    public void setCurrently_date(String s)
    {
        currently_date = s;
    }

    public void setCurrently_img(String s)
    {
        currently_img = s;
    }

    public void setCurrently_week(String s)
    {
        currently_week = s;
    }

    public void setTemperature_max(String s)
    {
        temperature_max = s;
    }

    public void setTemperature_min(String s)
    {
        temperature_min = s;
    }
    public String getHumidity(){
    	return "\u6E7F\u5EA6\uFF1A\u65E0\u6570\u636E";
    }
     
    public String getWind_condition()
    {
    	return "\u98CE\u5411\uFF1A\u65E0\u6570\u636E";
    }
    
    private String condition;
    private String currently_date;
    private String currently_img;
    private String currently_week;
    private String temperature_max;
    private String temperature_min;
}
