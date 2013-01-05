//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GetSGContentTimeStampHandler extends DefaultHandler
{

    public GetSGContentTimeStampHandler()
    {
    }

    public void startElement(String s, String s1, String s2, Attributes attributes)
        throws SAXException
    {
        super.startElement(s, s1, s2, attributes);
        if("site".equals(s1))
            if(attributes.getValue("timestamp") != null)
                timestamp = Integer.parseInt(attributes.getValue("timestamp"));
            else
                timestamp = 0;
    }

    int timestamp;
}
