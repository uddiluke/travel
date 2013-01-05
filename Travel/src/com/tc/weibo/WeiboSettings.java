//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.weibo;

import android.content.Context;
import android.content.SharedPreferences;

public class WeiboSettings
{

    public WeiboSettings()
    {
    }

    public static String getPassword(Context context)
    {
        return context.getSharedPreferences("weibo_settings", 0).getString("password", null);
    }

    public static String getToken(Context context)
    {
        return context.getSharedPreferences("weibo_settings", 0).getString("token", null);
    }

    public static String getTokenSecret(Context context)
    {
        return context.getSharedPreferences("weibo_settings", 0).getString("token_secret", null);
    }

    public static String getUserName(Context context)
    {
        return context.getSharedPreferences("weibo_settings", 0).getString("user_name", null);
    }

    public static void putPassword(Context context, String s)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences("weibo_settings", 0).edit();
        editor.putString("password", s);
        editor.commit();
    }

    public static void putToken(Context context, String s)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences("weibo_settings", 0).edit();
        editor.putString("token", s);
        editor.commit();
    }

    public static void putTokenSecret(Context context, String s)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences("weibo_settings", 0).edit();
        editor.putString("token_secret", s);
        editor.commit();
    }

    public static void putUserName(Context context, String s)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences("weibo_settings", 0).edit();
        editor.putString("user_name", s);
        editor.commit();
    }

    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_TOKEN_SECRET = "token_secret";
    public static final String KEY_USER_NAME = "user_name";
    public static final String SETTINGS_NAME = "weibo_settings";
}
