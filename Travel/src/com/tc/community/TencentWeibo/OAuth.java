// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.community.TencentWeibo;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package com.tc.community.TencentWeibo:
//            QParameter

public class OAuth
    implements Serializable
{

    public OAuth()
    {
        oauth_consumer_key = "801064081";
        oauth_consumer_secret = "9a179614c7a7ff436fcd181f011f41a7";
        oauth_signature_method = "HMAC-SHA1";
        oauth_timestamp = "";
        oauth_nonce = "";
        oauth_callback = "null";
        oauth_version = "1.0";
        oauth_token = "";
        oauth_token_secret = "";
        oauth_verifier = "";
        status = 0;
        msg = "";
        random = new Random();
        name = "";
    }

    public OAuth(String s)
    {
        oauth_consumer_key = "801064081";
        oauth_consumer_secret = "9a179614c7a7ff436fcd181f011f41a7";
        oauth_signature_method = "HMAC-SHA1";
        oauth_timestamp = "";
        oauth_nonce = "";
        oauth_callback = "null";
        oauth_version = "1.0";
        oauth_token = "";
        oauth_token_secret = "";
        oauth_verifier = "";
        status = 0;
        msg = "";
        random = new Random();
        name = "";
        oauth_callback = s;
    }

    public OAuth(String s, String s1, String s2)
    {
        oauth_consumer_key = "801064081";
        oauth_consumer_secret = "9a179614c7a7ff436fcd181f011f41a7";
        oauth_signature_method = "HMAC-SHA1";
        oauth_timestamp = "";
        oauth_nonce = "";
        oauth_callback = "null";
        oauth_version = "1.0";
        oauth_token = "";
        oauth_token_secret = "";
        oauth_verifier = "";
        status = 0;
        msg = "";
        random = new Random();
        name = "";
        oauth_consumer_key = s;
        oauth_consumer_secret = s1;
        oauth_callback = s2;
    }

    private String generateNonce()
    {
        return String.valueOf(0x1e208 + random.nextInt(0x96b477));
    }

    private String generateTimeStamp()
    {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }

    public List getAccessParams()
    {
        List list = getTokenParams();
        if(oauth_verifier != null && !"".equals(oauth_verifier))
            list.add(new QParameter("oauth_verifier", oauth_verifier));
        return list;
    }

    public String getMsg()
    {
        return msg;
    }

    public String getName()
    {
        return name;
    }

    public String getOauth_callback()
    {
        return oauth_callback;
    }

    public String getOauth_consumer_key()
    {
        return oauth_consumer_key;
    }

    public String getOauth_consumer_secret()
    {
        return oauth_consumer_secret;
    }

    public String getOauth_nonce()
    {
        return oauth_nonce;
    }

    public String getOauth_signature_method()
    {
        return oauth_signature_method;
    }

    public String getOauth_timestamp()
    {
        return oauth_timestamp;
    }

    public String getOauth_token()
    {
        return oauth_token;
    }

    public String getOauth_token_secret()
    {
        return oauth_token_secret;
    }

    public String getOauth_verifier()
    {
        return oauth_verifier;
    }

    public String getOauth_version()
    {
        return oauth_version;
    }

    public List getParams()
    {
        ArrayList arraylist = new ArrayList();
        oauth_timestamp = generateTimeStamp();
        oauth_nonce = generateNonce();
        if(oauth_consumer_key != null && !"".equals(oauth_consumer_key.trim()))
            arraylist.add(new QParameter("oauth_consumer_key", oauth_consumer_key));
        if(oauth_signature_method != null && !"".equals(oauth_signature_method.trim()))
            arraylist.add(new QParameter("oauth_signature_method", oauth_signature_method));
        if(oauth_timestamp != null && !"".equals(oauth_timestamp.trim()))
            arraylist.add(new QParameter("oauth_timestamp", oauth_timestamp));
        if(oauth_nonce != null && !"".equals(oauth_nonce.trim()))
            arraylist.add(new QParameter("oauth_nonce", oauth_nonce));
        if(oauth_callback != null && !"".equals(oauth_callback.trim()))
            arraylist.add(new QParameter("oauth_callback", oauth_callback));
        if(oauth_version != null && !"".equals(oauth_version.trim()))
            arraylist.add(new QParameter("oauth_version", oauth_version));
        return arraylist;
    }

    public int getStatus()
    {
        return status;
    }

    public List getTokenParams()
    {
        ArrayList arraylist = new ArrayList();
        oauth_timestamp = generateTimeStamp();
        oauth_nonce = generateNonce();
        arraylist.add(new QParameter("oauth_consumer_key", oauth_consumer_key));
        arraylist.add(new QParameter("oauth_signature_method", oauth_signature_method));
        arraylist.add(new QParameter("oauth_timestamp", oauth_timestamp));
        arraylist.add(new QParameter("oauth_nonce", oauth_nonce));
        arraylist.add(new QParameter("oauth_token", oauth_token));
        arraylist.add(new QParameter("oauth_version", oauth_version));
        return arraylist;
    }

    public void setMsg(String s)
    {
        msg = s;
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setOauth_callback(String s)
    {
        oauth_callback = s;
    }

    public void setOauth_consumer_key(String s)
    {
        oauth_consumer_key = s;
    }

    public void setOauth_consumer_secret(String s)
    {
        oauth_consumer_secret = s;
    }

    public void setOauth_nonce(String s)
    {
        oauth_nonce = s;
    }

    public void setOauth_signature_method(String s)
    {
        oauth_signature_method = s;
    }

    public void setOauth_timestamp(String s)
    {
        oauth_timestamp = s;
    }

    public void setOauth_token(String s)
    {
        oauth_token = s;
    }

    public void setOauth_token_secret(String s)
    {
        oauth_token_secret = s;
    }

    public void setOauth_verifier(String s)
    {
        oauth_verifier = s;
    }

    public void setOauth_version(String s)
    {
        oauth_version = s;
    }

    public void setStatus(int i)
    {
        status = i;
    }

    private String msg;
    private String name;
    private String oauth_callback;
    private String oauth_consumer_key;
    private String oauth_consumer_secret;
    private String oauth_nonce;
    private String oauth_signature_method;
    private String oauth_timestamp;
    private String oauth_token;
    private String oauth_token_secret;
    private String oauth_verifier;
    private String oauth_version;
    private Random random;
    private int status;
}
