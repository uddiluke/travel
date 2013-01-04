// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.logic;


public class CurrentWeather extends Weather
{

    public CurrentWeather()
    {
    }

  

    public String getCurrent_tem()
    {
        return current_tem;
    }

    public String getHumidity()
    {
        return humidity;
    }

    public String getWind_condition()
    {
        return presentWindDirection()+"  Level:"+presentWindSpeed();
    }
  
    public void setCurrent_tem(String s)
    {
        current_tem = s;
    }

    public void setHumidity(String s)
    {
        humidity = s;
    }

    public void setWind_condition(String s)
    {
        wind_condition = s;
    }
    public void setWindSpeed(String s){
    	windSpeed=Float.parseFloat(s);
    }
    public void setWindDirection(String s){
    	windDirection=Float.parseFloat(s);
    }

   
    private String current_tem;
    private String humidity;
    private String wind_condition;
    private float windDirection=0;
    private float windSpeed=0;
    private String visibility;
    private String sunset,sunrise;
	private String updateDate;
    public String presentWindDirection(){
    	if((windDirection>337.5 && windDirection<360)||(windDirection>0 && windDirection<22.5))
    		return "N";
    	if((windDirection>22.5 && windDirection<67.5)){
    		return "NE";
    	}if((windDirection>67.5 && windDirection<112.5)){
    		return "E";
    	}if((windDirection>112.5 && windDirection<157.5)){
    		return "SE";
    	}if((windDirection>157.5 && windDirection<202.5)){
    		return "S";
    	}if((windDirection>202.5 && windDirection<247.5)){
    		return "SW";
    	}if((windDirection>247.5 && windDirection<292.5)){
    		return "W";
    	}if((windDirection>292.5 && windDirection<337.5)){
    		return "NW";
    	}
        return "";
    }
    public String presentWindSpeed(){
    	if(windSpeed<1)
    		return "0";
    	if(windSpeed>1 && windSpeed<5.5)
    		return "1";
    	if(windSpeed>5.5 && windSpeed<11)
    		return "2";
    	if(windSpeed>11 && windSpeed<19)
    		return "3";
    	if(windSpeed>19 && windSpeed<28)
    		return "4";
    	if(windSpeed>28 && windSpeed<38)
    		return "5";
    	if(windSpeed>38 && windSpeed<49)
    		return "6";
    	if(windSpeed>49 && windSpeed<61)
    		return "7";
    	if(windSpeed>61 && windSpeed<74)
    		return "8";
    	if(windSpeed>74 && windSpeed<88)
    		return "9";
    	if(windSpeed>88 && windSpeed<102)
    		return "10";
    	if(windSpeed>102 && windSpeed<117)
    		return "11";
    	if(windSpeed>117)
    		return "12";
    	return "";
    }

	public void setVisibility(String value) {
		this.visibility=value;
		
	}

	public void setSunset(String value) {
		this.sunset=value;
		
	}

	public void setSunrise(String value) {
		this.sunrise=value;
		
	}

    public void setUpdateDate(String updateDate2){
    	this.updateDate=updateDate2;
    	if(updateDate!=null){
    		int index=updateDate.indexOf(",");
    		if(index>-1){
    		    //setCurrently_week(updateDate.substring(0,index));
    		   updateDate=updateDate.substring(index+2);
    		   /*index=updateDate.lastIndexOf(":");
    		   if(index>0){
    			   index=updateDate.lastIndexOf(" ",index-1);
    			   setCurrently_date(updateDate.substring(0,index));
    		   }*/
    		}
    	}
    		
    }

	public String getUpdateDate() {
		return this.updateDate;
	}

	
}
