// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.logic;

import android.content.Context;
import android.util.Log;
import com.tc.TCUtil;
import com.tc.net.NetUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

// Referenced classes of package com.tc.logic:
//            LogicSettings, TimeStampData

public class UserLoginData
{
    public static class Handler extends DefaultHandler
    {

        public void characters(char ac[], int i, int j)
            throws SAXException
        {
            super.characters(ac, i, j);
            String s = new String(ac, i, j);
            if(s.trim().length() > 0)
                characters = s;
        }

        public void endDocument()
            throws SAXException
        {
            super.endDocument();
        }

        public void endElement(String s, String s1, String s2)
            throws SAXException
        {
            super.endElement(s, s1, s2);
            tag = null;
            if(level == 2) {//goto _L2; else goto _L1
            	 if("status".equals(s1)) {
            		 userLoginData.status = characters; 
            	 }else  if("error".equals(s1))
                     userLoginData.error = characters;
                 else
                 if("token".equals(s1))
                     userLoginData.token = characters;
            }

        }

        public UserLoginData getUserLoginData()
        {
            return userLoginData;
        }

        public void startDocument()
            throws SAXException
        {
            super.startDocument();
            tag = null;
            level = -1;
        }

        public void startElement(String s, String s1, String s2, Attributes attributes)
            throws SAXException
        {
            super.startElement(s, s1, s2, attributes);
            tag = s1;
            if("xml".equals(s1))
                level = 1;
            else
            if("userlogin".equals(s1))
            {
                level = 2;
                userLoginData = new UserLoginData();
            } else
            if("status".equals(s1) || "error".equals(s1) || !"token".equals(s1));
        }

        private static final int IDLE_LEVEL = -1;
        private static final int USER_LOGIN_LEVEL = 2;
        private static final int XML_LEVEL = 1;
        private String characters;
        private int level;
        private String tag;
        private UserLoginData userLoginData;

        public Handler()
        {
            level = -1;
        }
    }

    public static class UserLoginDataParser
    {

        public static UserLoginData parse(InputStream inputstream)
        {
            UserLoginData userlogindata;
            userlogindata = null;
            if(inputstream != null){

            Handler handler = new Handler();
            try {
				SAXParserFactory.newInstance().newSAXParser().parse(inputstream, handler);
			} catch (SAXException e) {
                Log.e(TAG, "", e);
			} catch (IOException e) {
				 Log.e(TAG, "", e);
			} catch (ParserConfigurationException e) {
				 Log.e(TAG, "", e);
			}
            userlogindata = handler.getUserLoginData();

            }
            return userlogindata;
       
        }

        public UserLoginDataParser()
        {
        }
    }


    public UserLoginData()
    {
    }

    public static void userLogin(Context context, String s)
    {
        if(LogicSettings.getApplicationRegistered(context, s))
        {
            String s1 = TimeStampData.getTimeStamp(context);
            if(s1.length() > 0)
            {
                String s2 = TCUtil.getApplicationUserName(context, s);
                String s3 = TCUtil.getApplicationUserPassword(context, s);
                HashMap hashmap = new HashMap();
                hashmap.put("username", s2);
                hashmap.put("userpwd", s3);
                hashmap.put("m", NetUtil.getMd5(s1, hashmap));
                hashmap.put("t", s1);
                UserLoginData userlogindata = UserLoginDataParser.parse(NetUtil.getInputStream(NetUtil.getClient(), NetUtil.getHttpPost("https://service.itouchchina.com/restsvcs/userlogin", hashmap)));
                if(userlogindata != null && userlogindata.status.equals("OK"))
                    LogicSettings.putToken(context, s, userlogindata.token);
            }
        }
    }

    private static final String TAG = UserLoginData.class.getSimpleName();
    public String error;
    public String status;
    public String token;


}
