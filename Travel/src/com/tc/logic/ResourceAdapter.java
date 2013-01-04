// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.logic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.ArrayList;

// Referenced classes of package com.tc.logic:
//            BaseWeather, CurrentWeather, Weather

public class ResourceAdapter extends BaseAdapter
{

    public ResourceAdapter(Context context1)
    {
        context = context1;
    }

    public static BaseWeather getBaseWeather()
    {
        return baseWeather;
    }

    public static int getMessageCode()
    {
        return messageCode;
    }

    public static void setBaseWeather(BaseWeather baseweather)
    {
        baseWeather = baseweather;
    }

    public static void setMessageCode(int i)
    {
        messageCode = i;
    }

    public int getCount()
    {
        return imgs.length;
    }

  
    public Object getItem(int i)
    {
        return null;
    }

    public long getItemId(int i)
    {
        return 0L;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        ImageView imageview = new ImageView(context);
        imageview.setImageResource(imgs[i]);
        imageview.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
        imageview.setLayoutParams(new android.widget.Gallery.LayoutParams(87, 60));
        return imageview;
    }

    public Weather getWeatherByIndex(int i)
    {
        Weather weather;
        if(baseWeather == null)
            weather = null;
        else
            weather = (Weather)baseWeather.getWeatherList().get(i);
        return weather;
    }

    private static BaseWeather baseWeather;
    public static int current_icon;
    public static int current_img;
    public static int imgs[];
    private static int messageCode;
    public static int weather_icon[];
    private Context context;
}
