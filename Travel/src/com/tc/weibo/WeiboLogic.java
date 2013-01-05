//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.weibo;

import android.location.Location;
import java.util.HashMap;
import java.util.Map;

public class WeiboLogic
{
    public class Key
    {

        public static final String KEY_IMAGE = "image";
        public static final String KEY_LOCATION = "location";
        public static final String KEY_METHOD = "method";
        public static final String KEY_STATUS = "status";
        public static final String KEY_USR_NAME = "usr_name";
        public static final String KEY_USR_PASSWORD = "usr_password";
        public static final String KEY_WEIBO_EXCEPTION = "weibo_exception";


      
    }


    public WeiboLogic()
    {
    }

    public static Object[] getFriendList()
    {
        return (new Object[] {
            "get_friend_list", null
        });
    }

    public static Object[] getLoginParams(String s, String s1)
    {
        HashMap hashmap = new HashMap();
        hashmap.put("usr_name", s);
        hashmap.put("usr_password", s1);
        return (new Object[] {
            "login", hashmap
        });
    }

    public static Object[] status(String s, Location location, byte abyte0[])
    {
        HashMap hashmap = new HashMap();
        hashmap.put("status", s);
        hashmap.put("location", location);
        hashmap.put("image", abyte0);
        return (new Object[] {
            "status", hashmap
        });
    }

    public static final String METHOD_GET_FRIEND_LIST = "get_friend_list";
    public static final String METHOD_LOGIN = "login";
    public static final String METHOD_STATUS = "status";
}
