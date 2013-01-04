// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.community.TencentWeibo;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class QHttpClient
{

    public QHttpClient()
    {
    }

    public String httpGet(String s, String s1)
        throws Exception
    {
        HttpClient httpclient;
        GetMethod getmethod;
        if(s1 != null && !s1.equals(""))
            s = (new StringBuilder()).append(s).append("?").append(s1).toString();
        httpclient = new HttpClient();
        getmethod = new GetMethod(s);
        getmethod.getParams().setParameter("http.socket.timeout", new Integer(20000));
        String s2;
        if(httpclient.executeMethod(getmethod) == 200);
        s2 = getmethod.getResponseBodyAsString();
        getmethod.releaseConnection();
        return s2;
      
    }

    private static final int CONNECTION_TIMEOUT = 20000;
}
