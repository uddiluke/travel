// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.logic;

import android.content.Context;
import android.content.SharedPreferences;

public class LogicSettings
{

    public LogicSettings()
    {
    }

    public static boolean getApplicationRegistered(Context context, String s)
    {
        return context.getSharedPreferences("logic_settings", 0).getBoolean(s, false);
    }

    public static String getTimeStamp(Context context)
    {
        return context.getSharedPreferences("logic_settings", 0).getString("get_time", "");
    }

    public static String getToken(Context context, String s)
    {
        return context.getSharedPreferences("logic_settings", 0).getString((new StringBuilder()).append("token_").append(s).toString(), "");
    }

    public static void putApplicationRegistered(Context context, String s)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences("logic_settings", 0).edit();
        editor.putBoolean(s, true);
        editor.commit();
    }

    public static void putTimeStamp(Context context, String s)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences("logic_settings", 0).edit();
        editor.putString("get_time", s);
        editor.commit();
    }

    public static void putToken(Context context, String s, String s1)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences("logic_settings", 0).edit();
        editor.putString((new StringBuilder()).append("token_").append(s).toString(), s1);
        editor.commit();
    }

    private static final String KEY_GET_TIME = "get_time";
    private static final String KEY_TOKEN = "token_";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_PASSWORD = "user_password";
    private static final String SETTINGS_NAME = "logic_settings";
}
