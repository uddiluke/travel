// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.community.TencentWeibo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class QStr
{

    public QStr()
    {
    }

    public static String encode(String s)
    {
        String s2;
        if(s == null)
        {
            s2 = "";
        } else
        {
            String s1;
            try
            {
                s1 = URLEncoder.encode(s, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~").replace("#", "%23");
            }
            catch(UnsupportedEncodingException unsupportedencodingexception)
            {
                throw new RuntimeException(unsupportedencodingexception.getMessage(), unsupportedencodingexception);
            }
            s2 = s1;
        }
        return s2;
    }
}
