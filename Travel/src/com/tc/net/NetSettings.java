//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.net;

import android.content.Context;
import android.content.SharedPreferences;

public class NetSettings
{

    public NetSettings()
    {
    }

    public static String getTimestamp(Context context)
    {
        return context.getSharedPreferences("net_settings", 0).getString("time_stamp", "");
    }

    public static String getToken(Context context)
    {
        return context.getSharedPreferences("net_settings", 0).getString("token", "");
    }

    public static String getUserName(Context context)
    {
        return context.getSharedPreferences("net_settings", 0).getString("user_name", "");
    }

    public static String getUserPassword(Context context)
    {
        return context.getSharedPreferences("net_settings", 0).getString("user_password", "");
    }

    public static void putTimestamp(Context context, String s)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences("net_settings", 0).edit();
        editor.putString("time_stamp", s);
        editor.commit();
    }

    public static void putToken(Context context, String s)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences("net_settings", 0).edit();
        editor.putString("token", s);
        editor.commit();
    }

    public static void putUserName(Context context, String s)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences("net_settings", 0).edit();
        editor.putString("user_name", s);
        editor.commit();
    }

    public static void putUserPassword(Context context, String s)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences("net_settings", 0).edit();
        editor.putString("user_password", s);
        editor.commit();
    }

    public static final String KEY_TIME_STAMP = "time_stamp";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_PASSWORD = "user_password";
    public static final String SETTINGS_NAME = "net_settings";
}
