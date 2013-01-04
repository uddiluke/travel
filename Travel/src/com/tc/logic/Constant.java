// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.logic;

import java.util.HashMap;

import com.touchchina.cityguide.R;

public class Constant
{

    public Constant()
    {
    }

    public static final String ATTRIBUTE_NAME = "data";
    public static final String CITY_NAME_PATH = "postal_code";
    public static final int CITY_NOT_EXIST = 2;
    public static final String CONDITION_PATH = "condition";
    public static final String CURRENT_CONDITION = "current_conditions";
    public static final String CURRENT_TEMPERATURE = "temp_c";
    public static final HashMap DAY_IMAGE_MAP;
    public static final String DAY_WEEK_PATH = "day_of_week";
    public static final int DEFAULT_IMG = 0x7f02004c;
    public static final int DEFAULT_WEATHER_IMG = 0x7f0200f1;
    public static final String ERROR_PATH = "problem_cause";
    public static final String FIRST_DATE_PATH = "forecast_date";
    public static final String FORECAST_CONDITION = "forecast_conditions";
    public static final String GOOGLE_WEATHER_URL_CN = "http://www.google.com/ig/api?hl=zh-cn&weather=";
    public static final String HUMIDITY_PATH = "humidity";
    public static final String INFORMATION_PATH = "forecast_information";
    public static final HashMap MONTH_MAP;
    public static final int NET_LINK_ERROR = 1;
    public static final HashMap NIGHT_IMAGE_MAP;
    public static final int SUCCESS_FULL = 3;
    public static final String TEMPERATURE_MAX_PATH = "high";
    public static final String TEMPERATURE_MIN_PATH = "low";
    public static final String T_SIGN = "\260";
    public static final HashMap WEATHER_ICON;
    public static final String WEATHER_IMAGE_PATH = "icon";
    public static final String WIND_PATH = "wind_condition";

    static 
    {
        MONTH_MAP = new HashMap(12);
        MONTH_MAP.put((1), (31));
        MONTH_MAP.put((2), (28));
        MONTH_MAP.put((3), (31));
        MONTH_MAP.put((4), (30));
        MONTH_MAP.put((5), (31));
        MONTH_MAP.put((6), (30));
        MONTH_MAP.put((7), (31));
        MONTH_MAP.put((8), (31));
        MONTH_MAP.put((9), (30));
        MONTH_MAP.put((10), (31));
        MONTH_MAP.put((11), (30));
        MONTH_MAP.put((12), (31));
        DAY_IMAGE_MAP = new HashMap();
        DAY_IMAGE_MAP.put("0", (R.drawable.storm));
        DAY_IMAGE_MAP.put("1", (R.drawable.thunderstorm));
        DAY_IMAGE_MAP.put("2", (R.drawable.storm));
        DAY_IMAGE_MAP.put("3", (R.drawable.thunderstorm));
        DAY_IMAGE_MAP.put("4", (R.drawable.thunderstorm));
        DAY_IMAGE_MAP.put("5", (R.drawable.rain_snow));
        DAY_IMAGE_MAP.put("6", (R.drawable.rain_snow));
        DAY_IMAGE_MAP.put("7", (R.drawable.rain_snow));
        DAY_IMAGE_MAP.put("8", (R.drawable.light_rain));
        DAY_IMAGE_MAP.put("9", (R.drawable.light_rain));
        DAY_IMAGE_MAP.put("10", (R.drawable.light_rain));
        DAY_IMAGE_MAP.put("11", (R.drawable.heavy_rain));
        DAY_IMAGE_MAP.put("12", (R.drawable.heavy_rain));
        DAY_IMAGE_MAP.put("13", (R.drawable.chance_snow));
        DAY_IMAGE_MAP.put("14", (R.drawable.chance_snow));
        DAY_IMAGE_MAP.put("15", (R.drawable.snow));
        DAY_IMAGE_MAP.put("16", (R.drawable.snow));
        DAY_IMAGE_MAP.put("17", (R.drawable.icy));
        DAY_IMAGE_MAP.put("18", (R.drawable.rain_snow));
        DAY_IMAGE_MAP.put("19", (R.drawable.mist));
        DAY_IMAGE_MAP.put("20", (R.drawable.mist));
        DAY_IMAGE_MAP.put("21", (R.drawable.mist));
        DAY_IMAGE_MAP.put("22", (R.drawable.mist));
        DAY_IMAGE_MAP.put("23", (R.drawable.storm));
        DAY_IMAGE_MAP.put("24", (R.drawable.mist));
        DAY_IMAGE_MAP.put("25", (R.drawable.icy));
        DAY_IMAGE_MAP.put("26", (R.drawable.mist));
        DAY_IMAGE_MAP.put("27", (R.drawable.n_sunny_cloudy));
        DAY_IMAGE_MAP.put("28", (R.drawable.d_sunny_cloudy));
        DAY_IMAGE_MAP.put("29", (R.drawable.n_sunny_cloudy));
        DAY_IMAGE_MAP.put("30", (R.drawable.d_sunny_cloudy));
        DAY_IMAGE_MAP.put("31", (R.drawable.n_sunny));
        DAY_IMAGE_MAP.put("32", (R.drawable.sunny));
        DAY_IMAGE_MAP.put("33", (R.drawable.mist));
        DAY_IMAGE_MAP.put("34", (R.drawable.mist));
        DAY_IMAGE_MAP.put("35", (R.drawable.icy));
        DAY_IMAGE_MAP.put("36", (R.drawable.sunny));
        DAY_IMAGE_MAP.put("37", (R.drawable.chance_storm));
        DAY_IMAGE_MAP.put("38", (R.drawable.thunderstorm));
        DAY_IMAGE_MAP.put("39", (R.drawable.thunderstorm));
        DAY_IMAGE_MAP.put("40", (R.drawable.chance_rain));
        DAY_IMAGE_MAP.put("41", (R.drawable.heavy_snow));
        DAY_IMAGE_MAP.put("42", (R.drawable.chance_snow));
        DAY_IMAGE_MAP.put("43", (R.drawable.heavy_snow));
        DAY_IMAGE_MAP.put("44", (R.drawable.d_sunny_cloudy));
        DAY_IMAGE_MAP.put("45", (R.drawable.thunderstorm));
        DAY_IMAGE_MAP.put("46", (R.drawable.chance_snow));
        DAY_IMAGE_MAP.put("47", (R.drawable.chance_storm));
//        
//        DAY_IMAGE_MAP.put("13", (R.drawable.chance_snow));
//        DAY_IMAGE_MAP.put("38", (R.drawable.chance_storm));
//        DAY_IMAGE_MAP.put("39", (R.drawable.chance_storm));
//        DAY_IMAGE_MAP.put("26", (R.drawable.cloudy));
//        DAY_IMAGE_MAP.put("cn_cloudy.gif", (R.drawable.cloudy));
//        DAY_IMAGE_MAP.put("cn_heavyrain.gif", (R.drawable.heavy_rain));
//        DAY_IMAGE_MAP.put("cn_heavysnow.gif", (R.drawable.heavy_snow));
//        DAY_IMAGE_MAP.put("cn_lightrain.gif", (R.drawable.light_rain));
//        DAY_IMAGE_MAP.put("cn_overcast.gif", (R.drawable.cloudy));
//        DAY_IMAGE_MAP.put("cn_showers.gif", (R.drawable.chance_rain));
//        DAY_IMAGE_MAP.put("icy.gif", (R.drawable.icy));
//        DAY_IMAGE_MAP.put("cn_fog.gif", (R.drawable.mist));
//        DAY_IMAGE_MAP.put("fog.gif", (R.drawable.mist));
//        DAY_IMAGE_MAP.put("mist.gif", (R.drawable.mist));
//        DAY_IMAGE_MAP.put("haze.gif", (R.drawable.mist));
//        DAY_IMAGE_MAP.put("mostly_cloudy.gif", (R.drawable.d_sunny_cloudy));
//        DAY_IMAGE_MAP.put("mostly_sunny.gif", (R.drawable.d_sunny_cloudy));
//        DAY_IMAGE_MAP.put("partly_cloudy.gif", (R.drawable.d_sunny_cloudy));
//        DAY_IMAGE_MAP.put("sleet.gif", (R.drawable.rain_snow));
//        DAY_IMAGE_MAP.put("snow.gif", (R.drawable.snow));
//        DAY_IMAGE_MAP.put("storm.gif", (R.drawable.storm));
//        DAY_IMAGE_MAP.put("dust.gif", (R.drawable.storm));
//        DAY_IMAGE_MAP.put("sunny.gif", (R.drawable.sunny));
//        DAY_IMAGE_MAP.put("sunny_cloudy.gif", (R.drawable.d_sunny_cloudy));
//        DAY_IMAGE_MAP.put("thunderstorm.gif", (R.drawable.thunderstorm));
//        DAY_IMAGE_MAP.put("chance_of_rain.gif", (0x7f02002c));
//        DAY_IMAGE_MAP.put("chance_of_snow.gif", (R.drawable.chance_snow));
//        DAY_IMAGE_MAP.put("flurries.gif", (R.drawable.chance_snow));
//        DAY_IMAGE_MAP.put("chance_of_storm.gif", (0x7f02002e));
//        DAY_IMAGE_MAP.put("cloudy.gif", (R.drawable.cloudy));
//        DAY_IMAGE_MAP.put("cn_cloudy.gif", (R.drawable.cloudy));
//        DAY_IMAGE_MAP.put("cn_heavyrain.gif", (R.drawable.heavy_rain));
//        DAY_IMAGE_MAP.put("cn_heavysnow.gif", (R.drawable.heavy_snow));
//        DAY_IMAGE_MAP.put("cn_lightrain.gif", (R.drawable.light_rain));
//        DAY_IMAGE_MAP.put("cn_overcast.gif", (R.drawable.cloudy));
//        DAY_IMAGE_MAP.put("cn_showers.gif", (0x7f02002c));
//        DAY_IMAGE_MAP.put("icy.gif", (R.drawable.icy));
//        DAY_IMAGE_MAP.put("cn_fog.gif", (0x7f0200e1));
//        DAY_IMAGE_MAP.put("fog.gif", (0x7f0200e1));
//        DAY_IMAGE_MAP.put("mist.gif", (0x7f0200e1));
//        DAY_IMAGE_MAP.put("haze.gif", (0x7f0200e1));
//        DAY_IMAGE_MAP.put("mostly_cloudy.gif", (R.drawable.d_sunny_cloudy));
//        DAY_IMAGE_MAP.put("mostly_sunny.gif", (R.drawable.d_sunny_cloudy));
//        DAY_IMAGE_MAP.put("partly_cloudy.gif", (R.drawable.d_sunny_cloudy));
//        DAY_IMAGE_MAP.put("sleet.gif", (R.drawable.rain_snow));
//        DAY_IMAGE_MAP.put("snow.gif", (R.drawable.snow));
//        DAY_IMAGE_MAP.put("storm.gif", (R.drawable.storm));
//        DAY_IMAGE_MAP.put("dust.gif", (R.drawable.storm));
//        DAY_IMAGE_MAP.put("sunny.gif", (R.drawable.sunny));
//        DAY_IMAGE_MAP.put("sunny_cloudy.gif", (R.drawable.d_sunny_cloudy));
//        DAY_IMAGE_MAP.put("thunderstorm.gif", (R.drawable.thunderstorm));
        NIGHT_IMAGE_MAP = new HashMap();
        NIGHT_IMAGE_MAP.put("0", (R.drawable.storm));
        NIGHT_IMAGE_MAP.put("1", (R.drawable.n_thunderstorm));
        NIGHT_IMAGE_MAP.put("2", (R.drawable.storm));
        NIGHT_IMAGE_MAP.put("3", (R.drawable.n_thunderstorm));
        NIGHT_IMAGE_MAP.put("4", (R.drawable.n_thunderstorm));
        NIGHT_IMAGE_MAP.put("5", (R.drawable.rain_snow));
        NIGHT_IMAGE_MAP.put("6", (R.drawable.rain_snow));
        NIGHT_IMAGE_MAP.put("7", (R.drawable.rain_snow));
        NIGHT_IMAGE_MAP.put("8", (R.drawable.light_rain));
        NIGHT_IMAGE_MAP.put("9", (R.drawable.light_rain));
        NIGHT_IMAGE_MAP.put("10", (R.drawable.light_rain));
        NIGHT_IMAGE_MAP.put("11", (R.drawable.heavy_rain));
        NIGHT_IMAGE_MAP.put("12", (R.drawable.heavy_rain));
        NIGHT_IMAGE_MAP.put("13", (R.drawable.chance_snow));
        NIGHT_IMAGE_MAP.put("14", (R.drawable.chance_snow));
        NIGHT_IMAGE_MAP.put("15", (R.drawable.snow));
        NIGHT_IMAGE_MAP.put("16", (R.drawable.snow));
        NIGHT_IMAGE_MAP.put("17", (R.drawable.icy));
        NIGHT_IMAGE_MAP.put("18", (R.drawable.rain_snow));
        NIGHT_IMAGE_MAP.put("19", (R.drawable.n_mist));
        NIGHT_IMAGE_MAP.put("20", (R.drawable.n_mist));
        NIGHT_IMAGE_MAP.put("21", (R.drawable.n_mist));
        NIGHT_IMAGE_MAP.put("22", (R.drawable.n_mist));
        NIGHT_IMAGE_MAP.put("23", (R.drawable.storm));
        NIGHT_IMAGE_MAP.put("24", (R.drawable.n_mist));
        NIGHT_IMAGE_MAP.put("25", (R.drawable.icy));
        NIGHT_IMAGE_MAP.put("26", (R.drawable.n_mist));
        NIGHT_IMAGE_MAP.put("27", (R.drawable.n_sunny_cloudy));
        NIGHT_IMAGE_MAP.put("28", (R.drawable.d_sunny_cloudy));
        NIGHT_IMAGE_MAP.put("29", (R.drawable.n_sunny_cloudy));
        NIGHT_IMAGE_MAP.put("30", (R.drawable.d_sunny_cloudy));
        NIGHT_IMAGE_MAP.put("31", (R.drawable.n_sunny));
        NIGHT_IMAGE_MAP.put("32", (R.drawable.n_sunny));
        NIGHT_IMAGE_MAP.put("33", (R.drawable.n_mist));
        NIGHT_IMAGE_MAP.put("34", (R.drawable.n_mist));
        NIGHT_IMAGE_MAP.put("35", (R.drawable.icy));
        NIGHT_IMAGE_MAP.put("36", (R.drawable.n_sunny));
        NIGHT_IMAGE_MAP.put("37", (R.drawable.n_thunderstorm));
        NIGHT_IMAGE_MAP.put("38", (R.drawable.n_thunderstorm));
        NIGHT_IMAGE_MAP.put("39", (R.drawable.n_thunderstorm));
        NIGHT_IMAGE_MAP.put("40", (R.drawable.n_chance_rain));
        NIGHT_IMAGE_MAP.put("41", (R.drawable.heavy_snow));
        NIGHT_IMAGE_MAP.put("42", (R.drawable.chance_snow));
        NIGHT_IMAGE_MAP.put("43", (R.drawable.heavy_snow));
        NIGHT_IMAGE_MAP.put("44", (R.drawable.n_sunny_cloudy));
        NIGHT_IMAGE_MAP.put("45", (R.drawable.n_thunderstorm));
        NIGHT_IMAGE_MAP.put("46", (R.drawable.chance_snow));
        NIGHT_IMAGE_MAP.put("47", (R.drawable.n_chance_storm));
//        NIGHT_IMAGE_MAP.put("chance_of_rain.gif", (R.drawable.n_chance_rain));
//        NIGHT_IMAGE_MAP.put("chance_of_snow.gif", (R.drawable.chance_snow));
//        NIGHT_IMAGE_MAP.put("flurries.gif", (R.drawable.chance_snow));
//        NIGHT_IMAGE_MAP.put("chance_of_storm.gif", (R.drawable.n_chance_storm));
//        NIGHT_IMAGE_MAP.put("cloudy.gif", (R.drawable.cloudy));
//        NIGHT_IMAGE_MAP.put("cn_cloudy.gif", (R.drawable.cloudy));
//        NIGHT_IMAGE_MAP.put("cn_heavyrain.gif", (R.drawable.heavy_rain));
//        NIGHT_IMAGE_MAP.put("cn_heavysnow.gif", (R.drawable.heavy_snow));
//        NIGHT_IMAGE_MAP.put("cn_lightrain.gif", (R.drawable.light_rain));
//        NIGHT_IMAGE_MAP.put("cn_overcast.gif", (R.drawable.cloudy));
//        NIGHT_IMAGE_MAP.put("cn_showers.gif", (R.drawable.n_chance_rain));
//        NIGHT_IMAGE_MAP.put("icy.gif", (R.drawable.icy));
//        NIGHT_IMAGE_MAP.put("cn_fog.gif", (R.drawable.n_mist));
//        NIGHT_IMAGE_MAP.put("fog.gif", (R.drawable.n_mist));
//        NIGHT_IMAGE_MAP.put("haze.gif", (R.drawable.n_mist));
//        NIGHT_IMAGE_MAP.put("mist.gif", (R.drawable.n_mist));
//        NIGHT_IMAGE_MAP.put("mostly_cloudy.gif", (R.drawable.n_sunny_cloudy));
//        NIGHT_IMAGE_MAP.put("mostly_sunny.gif", (R.drawable.n_sunny_cloudy));
//        NIGHT_IMAGE_MAP.put("partly_cloudy.gif", (R.drawable.n_sunny_cloudy));
//        NIGHT_IMAGE_MAP.put("sleet.gif", (R.drawable.rain_snow));
//        NIGHT_IMAGE_MAP.put("snow.gif", (R.drawable.snow));
//        NIGHT_IMAGE_MAP.put("storm.gif", (R.drawable.storm));
//        NIGHT_IMAGE_MAP.put("dust.gif", (R.drawable.storm));
//        NIGHT_IMAGE_MAP.put("sunny.gif", (R.drawable.n_sunny));
//        NIGHT_IMAGE_MAP.put("sunny_cloudy.gif", (R.drawable.n_sunny_cloudy));
//        NIGHT_IMAGE_MAP.put("thunderstorm.gif", (R.drawable.thunderstorm));
        
        WEATHER_ICON = new HashMap();
//        WEATHER_ICON.put("chance_of_rain.gif", (R.drawable.chance_of_rain));
//        WEATHER_ICON.put("chance_of_snow.gif", (R.drawable.chance_of_snow));
//        WEATHER_ICON.put("flurries.gif", (R.drawable.chance_of_snow));
//        WEATHER_ICON.put("chance_of_storm.gif", (R.drawable.scattered_thunderstorms));
//        WEATHER_ICON.put("cloudy.gif", (R.drawable.cloudy_icon));
//        WEATHER_ICON.put("cn_cloudy.gif", (R.drawable.cloudy_icon));
//        WEATHER_ICON.put("cn_heavyrain.gif", (R.drawable.showers_icon));
//        WEATHER_ICON.put("cn_heavysnow.gif", (R.drawable.snow_showers));
//        WEATHER_ICON.put("cn_lightrain.gif", (R.drawable.light_rain_icon));
//        WEATHER_ICON.put("cn_overcast.gif", (R.drawable.cloudy_icon));
//        WEATHER_ICON.put("cn_showers.gif", (R.drawable.showers_icon));
//        WEATHER_ICON.put("icy.gif", (R.drawable.freezing_drizzle));
//        WEATHER_ICON.put("cn_fog.gif", (R.drawable.mist_icon));
//        WEATHER_ICON.put("fog.gif", (R.drawable.mist_icon));
//        WEATHER_ICON.put("haze.gif", (R.drawable.mist_icon));
//        WEATHER_ICON.put("mostly_cloudy.gif", (R.drawable.partly_sunny));
//        WEATHER_ICON.put("mostly_sunny.gif", (R.drawable.partly_sunny));
//        WEATHER_ICON.put("partly_cloudy.gif", (R.drawable.partly_sunny));
//        WEATHER_ICON.put("sleet.gif", (R.drawable.sleet_icon));
//        WEATHER_ICON.put("snow.gif", (R.drawable.snow_icon));
//        WEATHER_ICON.put("storm.gif", (R.drawable.storm_icon));
//        WEATHER_ICON.put("dust.gif", (R.drawable.storm_icon));
//        WEATHER_ICON.put("sunny.gif", (R.drawable.sunny_icon));
//        WEATHER_ICON.put("sunny_cloudy.gif", (R.drawable.partly_sunny));
//        WEATHER_ICON.put("thunderstorm.gif", (R.drawable.thunderstorm_icon));
        WEATHER_ICON.put("0", (R.drawable.storm_icon));
        WEATHER_ICON.put("1", (R.drawable.thunderstorm_icon));
        WEATHER_ICON.put("2", (R.drawable.storm_icon));
        WEATHER_ICON.put("3", (R.drawable.thunderstorm_icon));
        WEATHER_ICON.put("4", (R.drawable.thunderstorm_icon));
        WEATHER_ICON.put("5", (R.drawable.rain_snow_icon));
        WEATHER_ICON.put("6", (R.drawable.rain_snow_icon));
        WEATHER_ICON.put("7", (R.drawable.rain_snow_icon));
        WEATHER_ICON.put("8", (R.drawable.light_rain_icon));
        WEATHER_ICON.put("9", (R.drawable.light_rain_icon));
        WEATHER_ICON.put("10", (R.drawable.light_rain_icon));
        WEATHER_ICON.put("11", (R.drawable.showers_icon));
        WEATHER_ICON.put("12", (R.drawable.showers_icon));
        WEATHER_ICON.put("13", (R.drawable.chance_of_snow));
        WEATHER_ICON.put("14", (R.drawable.chance_of_snow));
        WEATHER_ICON.put("15", (R.drawable.snow_icon));
        WEATHER_ICON.put("16", (R.drawable.snow_icon));
        WEATHER_ICON.put("17", (R.drawable.freezing_drizzle));
        WEATHER_ICON.put("18", (R.drawable.rain_snow_icon));
        WEATHER_ICON.put("19", (R.drawable.mist_icon));
        WEATHER_ICON.put("20", (R.drawable.mist_icon));
        WEATHER_ICON.put("21", (R.drawable.mist_icon));
        WEATHER_ICON.put("22", (R.drawable.mist_icon));
        WEATHER_ICON.put("23", (R.drawable.storm_icon));
        WEATHER_ICON.put("24", (R.drawable.mist_icon));
        WEATHER_ICON.put("25", (R.drawable.freezing_drizzle));
        WEATHER_ICON.put("26", (R.drawable.mist_icon));
        WEATHER_ICON.put("27", (R.drawable.partly_sunny));
        WEATHER_ICON.put("28", (R.drawable.partly_sunny));
        WEATHER_ICON.put("29", (R.drawable.partly_sunny));
        WEATHER_ICON.put("30", (R.drawable.partly_sunny));
        WEATHER_ICON.put("31", (R.drawable.sunny_icon));
        WEATHER_ICON.put("32", (R.drawable.sunny_icon));
        WEATHER_ICON.put("33", (R.drawable.mist_icon));
        WEATHER_ICON.put("34", (R.drawable.mist_icon));
        WEATHER_ICON.put("35", (R.drawable.freezing_drizzle));
        WEATHER_ICON.put("36", (R.drawable.sunny_icon));
        WEATHER_ICON.put("37", (R.drawable.thunderstorm_icon));
        WEATHER_ICON.put("38", (R.drawable.thunderstorm_icon));
        WEATHER_ICON.put("39", (R.drawable.thunderstorm_icon));
        WEATHER_ICON.put("40", (R.drawable.chance_of_rain));
        WEATHER_ICON.put("41", (R.drawable.snow_showers));
        WEATHER_ICON.put("42", (R.drawable.chance_of_snow));
        WEATHER_ICON.put("43", (R.drawable.snow_showers));
        WEATHER_ICON.put("44", (R.drawable.partly_sunny));
        WEATHER_ICON.put("45", (R.drawable.thunderstorm_icon));
        WEATHER_ICON.put("46", (R.drawable.chance_of_snow));
        WEATHER_ICON.put("47", (R.drawable.chancestorm));
    }
}
