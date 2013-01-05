//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.logic;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;

import com.tc.net.NetUtil;

// Referenced classes of package com.tc.logic:
//            LogicSettings

public class TimeStampData {
	public static class Handler extends DefaultHandler {

		public void characters(char ac[], int i, int j) throws SAXException {
			super.characters(ac, i, j);
			String s = new String(ac, i, j);
			if (s.trim().length() > 0)
				characters = s;
		}

		public void endDocument() throws SAXException {
			super.endDocument();
		}

		public void endElement(String s, String s1, String s2) throws SAXException {
			super.endElement(s, s1, s2);
			tag = null;
			if (level == 2) { // goto _L2; else goto _L1
				if ("status".equals(s1)) {
					getTimeData.status = characters;
				} else if ("error".equals(s1))
					getTimeData.error = characters;
				else if ("time".equals(s1))
					getTimeData.time = characters;
			}

		}

		public TimeStampData getGetTimeData() {
			return getTimeData;
		}

		public void startDocument() throws SAXException {
			super.startDocument();
			tag = null;
			level = -1;
		}

		public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
			super.startElement(s, s1, s2, attributes);
			tag = s1;
			if ("xml".equals(s1))
				level = 1;
			else if ("gettime".equals(s1)) {
				level = 2;
				getTimeData = new TimeStampData();
			} else if ("status".equals(s1) || "error".equals(s1) || !"time".equals(s1))
				;
		}

		private static final int GET_TIME_LEVEL = 2;
		private static final int IDLE_LEVEL = -1;
		private static final int XML_LEVEL = 1;
		private String characters;
		private TimeStampData getTimeData;
		private int level;
		private String tag;

		public Handler() {
		}
	}

	public static class Paraser {

		public static TimeStampData parse(InputStream inputstream) {
			TimeStampData timestampdata;
			timestampdata = null;
			if (inputstream != null) {

				TimeStampData timestampdata1;
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
				timestampdata = handler.getGetTimeData();
			}
			return timestampdata;

		}

		private static final String TAG = Paraser.class.getSimpleName();

		public Paraser() {
		}
	}

	public TimeStampData() {
	}

	public static String getTimeStamp(Context context) {
		String s = LogicSettings.getTimeStamp(context);
		TimeStampData timestampdata = Paraser.parse(NetUtil.getInputStream(NetUtil.getClient(),
				NetUtil.getHttpGet("https://service.itouchchina.com/restsvcs/gettime", null)));
		if (timestampdata != null && timestampdata.status.equals("OK")) {
			s = timestampdata.time;
			LogicSettings.putTimeStamp(context, s);
		}
		return s;
	}

	public String error;
	public String status;
	public String time;
}
