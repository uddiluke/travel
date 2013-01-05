//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.logic;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

// Referenced classes of package com.tc.logic:
//            BaseWeather, Weather, CurrentWeather

public class XmlParse extends DefaultHandler
{
    
    public XmlParse()
    {
        baseWeather = new BaseWeather();
        baseWeather.setWeatherList(new ArrayList<Weather>());
       
    }
    private StringBuffer sb;
    public void endElement(String uri, String localName, String qName)
        throws SAXException
    {
    	if(startPubDate){
    		currentWeather.setUpdateDate(sb.toString());
    		startPubDate=false;
    	}
    }
    
    @Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		
        if(startPubDate){
    	   sb.append(ch,start,length);
    	}
	}

	public BaseWeather getBaseWeather()
    {
        return baseWeather;
    }
    
    public void startElement(String uri, String localName, String allName, Attributes attributes)
        throws SAXException
    {
        if("query".equals(localName)){
        	if(!"1".equals(attributes.getValue("yahoo:count"))){
        		 throw new IllegalArgumentException("cannot get weather data!");
        	}
        }else if("yweather:location".equals(allName)){
        	 baseWeather.setCityName(attributes.getValue("city"));
        	 currentWeather=new CurrentWeather();
        	 baseWeather.getWeatherList().add(currentWeather);
        }else if("pubDate".equals(localName)){
        	startPubDate=true;
        	sb=new StringBuffer();
        }
        else if("yweather:condition".equals(allName)){
        	  currentWeather.setCondition(attributes.getValue("text"));
        	  currentWeather.setCurrent_tem(attributes.getValue("temp"));
        	  currentWeather.setCurrently_img(attributes.getValue("code"));
        	  
        }else if("yweather:wind".equals(allName)){
      	    currentWeather.setWindDirection(attributes.getValue("direction"));
            currentWeather.setWindSpeed(attributes.getValue("speed"));
        }else if("yweather:atmosphere".equals(allName)){
    	    currentWeather.setHumidity(attributes.getValue("humidity"));
    	    currentWeather.setVisibility(attributes.getValue("visibility"));
       }else if("yweather:astronomy".equals(allName)){
    	   currentWeather.setSunset(attributes.getValue("sunset"));
    	   currentWeather.setSunrise(attributes.getValue("sunrise")); 
       }else if("yweather:forecast".equals(allName)){
    	   if(currentWeather.getTemperature_max()==null){
    		   //current day forecasts
    		   currentWeather.setTemperature_min(attributes.getValue("low"));
    		   currentWeather.setTemperature_max(attributes.getValue("high"));
    		   currentWeather.setCurrently_week(attributes.getValue("day"));
    		   currentWeather.setCurrently_date(attributes.getValue("date"));
    		   currentWeather.setCurrently_img(attributes.getValue("code"));
    	   }else{
    	       weather = new Weather();
    	       baseWeather.getWeatherList().add(weather);
    	       weather.setTemperature_min(attributes.getValue("low"));
    	       weather.setTemperature_max(attributes.getValue("high"));
    	       weather.setCurrently_week(attributes.getValue("day"));
    	       weather.setCurrently_date(attributes.getValue("date"));
    	       weather.setCurrently_img(attributes.getValue("code"));
    	   }
       }
       
    }

    private BaseWeather baseWeather;
    private CurrentWeather currentWeather;
    private Weather weather;
    private boolean startPubDate=false;
    private static final String iconPath="http://l.yimg.com/a/i/us/we/52/";
}
