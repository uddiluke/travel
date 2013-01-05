//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.community.TencentWeibo;

import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

// Referenced classes of package com.tc.community.TencentWeibo:
//            QParameter, QStr, Base64Encoder, QHttpClient, 
//            OAuth

public class OAuthClient
{

    public OAuthClient()
    {
    }

    private static String encodeParams(List list)
    {
        StringBuilder stringbuilder = new StringBuilder();
        QParameter qparameter;
        for(Iterator iterator = list.iterator(); iterator.hasNext(); stringbuilder.append(QStr.encode(qparameter.getValue())))
        {
            qparameter = (QParameter)iterator.next();
            if(stringbuilder.length() != 0)
                stringbuilder.append("&");
            stringbuilder.append(QStr.encode(qparameter.getName()));
            stringbuilder.append("=");
        }

        return stringbuilder.toString();
    }

    private String generateSignature(String s, String s1, String s2)
    {
        try {
        Mac mac = Mac.getInstance("HmacSHA1");
        StringBuilder stringbuilder = (new StringBuilder()).append(QStr.encode(s1)).append("&");
        String s3="";
        if(s2 == null){
          	 s3= QStr.encode(s2);
        }
      
			mac.init(new SecretKeySpec(stringbuilder.append(s3).toString().getBytes(), "HmacSHA1"));
			 return new String(Base64Encoder.encode(mac.doFinal(s.getBytes())));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       return null;

    }

    private String generateSignature(URL url, String s, String s1, String s2, List list)
    {
        return generateSignature(generateSignatureBase(url, s2, list), s, s1);
    }

    private String generateSignatureBase(URL url, String s, List list)
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(s.toUpperCase());
        stringbuilder.append("&");
        stringbuilder.append(QStr.encode(getNormalizedUrl(url)));
        stringbuilder.append("&");
        stringbuilder.append(QStr.encode(encodeParams(list)));
        return stringbuilder.toString();
    }

    private static String getNormalizedUrl(URL url)
    {
        String s1;
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(url.getProtocol());
        stringbuilder.append("://");
        stringbuilder.append(url.getHost());
        if((url.getProtocol().equals("http") || url.getProtocol().equals("https")) && url.getPort() != -1)
        {
            stringbuilder.append(":");
            stringbuilder.append(url.getPort());
        }
        stringbuilder.append(url.getPath());
        s1 = stringbuilder.toString();
        String s = s1;

        return s;
 
    }

    public OAuth accessToken(OAuth oauth)
        throws Exception
    {
        if(!parseToken((new QHttpClient()).httpGet("http://open.t.qq.com/cgi-bin/access_token", getOauthParams("http://open.t.qq.com/cgi-bin/access_token", "GET", oauth.getOauth_consumer_secret(), oauth.getOauth_token_secret(), oauth.getAccessParams())), oauth))
            oauth.setStatus(2);
        return oauth;
    }

    public String getOauthParams(String s, String s1, String s2, String s3, List list)
    {
        String s4;
        String s5;
        URL url;
        Collections.sort(list);
        s4 = s;
        s5 = encodeParams(list);
        if(s5 != null && !s5.equals(""))
            s4 = (new StringBuilder()).append(s4).append("?").append(s5).toString();
        url = null;
        try {
			url = new URL(s4);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     

        String s6 = generateSignature(url, s2, s3, s1, list);
        String s7 = (new StringBuilder()).append(s5).append("&oauth_signature=").toString();
        return (new StringBuilder()).append(s7).append(QStr.encode(s6)).toString();
    
    }

    public boolean parseToken(String s, OAuth oauth)
        throws Exception
    {
        boolean flag = false;
        if(s != null && !s.equals("")) {
            oauth.setMsg(s);
            String as[] = s.split("&");
            if(as.length >= 2)
            {
                String s1 = as[0];
                String s2 = as[1];
                String s3 = as[2];
                String as1[] = s1.split("=");
                if(as1.length >= 2)
                {
                    oauth.setOauth_token(as1[1]);
                    String as2[] = s2.split("=");
                    if(as2.length >= 2)
                    {
                        oauth.setOauth_token_secret(as2[1]);
                        String as3[] = s3.split("=");
                        if(as3.length >= 2)
                        {
                            oauth.setName(as3[1]);
                            flag = true;
                        }
                    }
                }
            }
        }

        return flag;

    }

    public OAuth requestToken(OAuth oauth)
        throws Exception
    {
        QHttpClient qhttpclient = new QHttpClient();
        String s = getOauthParams("http://open.t.qq.com/cgi-bin/request_token", "GET", oauth.getOauth_consumer_secret(), "", oauth.getParams());
        System.out.println((new StringBuilder()).append("queryString:").append(s).toString());
        String s1 = qhttpclient.httpGet("http://open.t.qq.com/cgi-bin/request_token", s);
        System.out.println((new StringBuilder()).append("responseData:").append(s1).toString());
        if(!parseToken(s1, oauth))
            oauth.setStatus(1);
        return oauth;
    }

    private static final String hashAlgorithmName = "HmacSHA1";
}
