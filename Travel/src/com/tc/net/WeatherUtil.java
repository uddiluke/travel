// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.net;

import android.util.Log;
import com.tc.logic.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class WeatherUtil
{
    private static final String Yahoo_Weather_Url="http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%3DWOID%20and%20u%3D'c'&format=xml";
	private static final String Yahoo_huashan="http://xml.weather.yahoo.com/forecastrss/CHXX0388_c.xml";
    private static final HashMap<String,String> cityWoidMap=new HashMap<String,String>();
    public WeatherUtil()
    {
    	cityWoidMap.put("HUASHAN", "12713937");
    }

    public static String getCurrentlyDate()
    {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
    }

    public static int getImageName(String s, boolean flag)
    {
        int i= 0x7f02004c;
        String as[] = s.split("/");
        if(flag) {

        if(Constant.NIGHT_IMAGE_MAP.containsKey(as[-1 + as.length]))
            i = ((Integer)Constant.NIGHT_IMAGE_MAP.get(as[-1 + as.length])).intValue();
        }else{
        	 if(Constant.DAY_IMAGE_MAP.containsKey(as[-1 + as.length]))
                 i = ((Integer)Constant.DAY_IMAGE_MAP.get(as[-1 + as.length])).intValue();	
        }

        return i;

    }

    public static String getNextDate(String s)
    {
        String as[] = s.split("-");
        int i = Integer.valueOf(as[2]).intValue();
        int j = Integer.valueOf(as[1]).intValue();
        int k = Integer.valueOf(as[0]).intValue();
        StringBuffer stringbuffer = new StringBuffer();
        int l;
        if(((Integer)Constant.MONTH_MAP.get(Integer.valueOf(j))).intValue() == i)
        {
            l = 1;
            if(++j == 13)
            {
                j = 1;
                k++;
            }
        } else
        {
            l = i + 1;
        }
        stringbuffer.append(k).append("-");
        if(j < 10)
            stringbuffer.append("0");
        stringbuffer.append(j).append("-");
        if(l < 10)
            stringbuffer.append("0");
        stringbuffer.append(l);
        return stringbuffer.toString();
    }

    public static BaseWeather getWeatherByCity(String s)
        throws ParserConfigurationException, SAXException, IOException
    {
    	String woid="12713937";
    	if(cityWoidMap.containsKey(s)){
    		woid=cityWoidMap.get(s);
    	}
        XMLReader xmlreader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        XmlParse xmlparse = new XmlParse();
        xmlreader.setContentHandler(xmlparse);
        //URL url = new URL(Yahoo_Weather_Url.replace("WOID", woid));
        //five days forecasts
        URL url = new URL(Yahoo_huashan);
        Log.d("test", (new StringBuilder()).append("Url\uFF1A").append(url).toString());
        
        xmlreader.parse(new InputSource(new InputStreamReader(url.openStream(), "UTF-8")));
        return xmlparse.getBaseWeather();
    }

    public static int getWeatherIcon(String s)
    {
        int i = 0x7f0200f1;
        String as[] = s.split("/");
        if((as != null || as.length != 0) && Constant.WEATHER_ICON.containsKey(as[-1 + as.length]))
            i = ((Integer)Constant.WEATHER_ICON.get(as[-1 + as.length])).intValue();
        return i;
    }

    public static boolean isNight()
    {
        int i = Calendar.getInstance().get(11);
        Log.d("test", (new StringBuilder()).append("hour=").append(i).toString());
        boolean flag;
        if(i >= 20 || i <= 7)
            flag = true;
        else
            flag = false;
        return flag;
    }
  
}
