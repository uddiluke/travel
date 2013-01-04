// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.net;

import android.os.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.*;
import org.apache.http.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

// Referenced classes of package com.tc.net:
//            NetUtil

public class DownLoadUpdateAsyncTask extends AsyncTask
{
    private class DownLoadUpdateAsyncTaskHandler extends DefaultHandler
    {

        public void startElement(String s, String s1, String s2, Attributes attributes)
            throws SAXException
        {
            super.startElement(s, s1, s2, attributes);
            if("getCGAllSGTimeStamp".equals(method) && "item".equals(s1))
            {
                String s3 = attributes.getValue("timestamp");
                if(s3 == null || "".equals(s3))
                    s3 = "0";
                hashMap.put(Integer.valueOf(Integer.parseInt(attributes.getValue("id"))), new DownLoadUpdateData(Integer.parseInt(s3), attributes.getValue("updateinfo")));
            }
        }

        HashMap hashMap;
        String method;

        public DownLoadUpdateAsyncTaskHandler(String s)
        {
            method = s;
            if("getCGAllSGTimeStamp".equals(s))
                hashMap = new HashMap();
        }
    }

    public class DownLoadUpdateData
    {
        public int timestamp;
        public String updateInfo;

        public DownLoadUpdateData(int i, String s)
        {
           
            timestamp = i;
            updateInfo = s;
        }
    }


    public DownLoadUpdateAsyncTask(Handler handler1)
    {
        handler = handler1;
    }

    protected Object doInBackground(Object aobj[])
    {
        String s = (String)aobj[0];
        Map map = (Map)aobj[1];
        String s1 = (String)aobj[2];
        org.apache.http.client.HttpClient httpclient = NetUtil.getClient();
        HttpResponse httpresponse = NetUtil.getHttpResponse(httpclient, NetUtil.getHttpGet(httpclient, s1, map));
        if(httpresponse != null && httpresponse.getStatusLine().getStatusCode() == 200)
            try
            {
                java.io.InputStream inputstream = httpresponse.getEntity().getContent();
                SAXParser saxparser = SAXParserFactory.newInstance().newSAXParser();
                DownLoadUpdateAsyncTaskHandler downloadupdateasynctaskhandler = new DownLoadUpdateAsyncTaskHandler(s);
                saxparser.parse(inputstream, downloadupdateasynctaskhandler);
                Message message = new Message();
                message.what = 0;
                message.obj = downloadupdateasynctaskhandler.hashMap;
                handler.sendMessage(message);
            }
            catch(IllegalStateException illegalstateexception)
            {
                illegalstateexception.printStackTrace();
            }
            catch(IOException ioexception)
            {
                ioexception.printStackTrace();
            }
            catch(ParserConfigurationException parserconfigurationexception)
            {
                parserconfigurationexception.printStackTrace();
            }
            catch(SAXException saxexception)
            {
                saxexception.printStackTrace();
            }
        return null;
    }

    private Handler handler;
}
