//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;

import com.tc.TCUtil;
import com.tc.logic.LogicSettings;

// Referenced classes of package com.tc.net:
//            NetUtil

public class GetTimeData {
	public static class GetTimeDataParser {

		public static GetTimeData parse(InputStream inputstream) {
			GetTimeData gettimedata = null;
			try {
				SAXParser saxparser = SAXParserFactory.newInstance().newSAXParser();
				Handler handler = new Handler();
				saxparser.parse(inputstream, handler);
				gettimedata = handler.getGetTimeData();
			} catch (SAXException e) {
				Log.e(TAG, "", e);
			} catch (IOException e) {
				Log.e(TAG, "", e);
			} catch (ParserConfigurationException e) {
				Log.e(TAG, "", e);
			}
			return gettimedata;

		}

		public static GetTimeData parse(byte abyte0[]) {
			ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte0);
			GetTimeData gettimedata = parse(((InputStream) (bytearrayinputstream)));
			TCUtil.inputstreamClose(bytearrayinputstream);
			return gettimedata;
		}

		private static final String TAG = GetTimeDataParser.class.getSimpleName();

		public GetTimeDataParser() {
		}
	}

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

		public GetTimeData getGetTimeData() {
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
				getTimeData = new GetTimeData();
			} else if ("status".equals(s1) || "error".equals(s1) || !"time".equals(s1))
				;
		}

		private static final int GET_TIME_LEVEL = 2;
		private static final int IDLE_LEVEL = -1;
		private static final int XML_LEVEL = 1;
		private String characters;
		private GetTimeData getTimeData;
		private int level;
		private String tag;

		public Handler() {
			level = -1;
		}
	}

	public GetTimeData() {
	}

	public static String getGotTime(Context context) {
		String s = LogicSettings.getTimeStamp(context);
		org.apache.http.client.HttpClient httpclient = NetUtil.getClient();
		org.apache.http.HttpResponse httpresponse = NetUtil.getHttpResponse(httpclient, NetUtil.getHttpGet(httpclient, getUrl(), getParams()));
		if (httpresponse != null) {
			GetTimeData gettimedata = GetTimeDataParser.parse(NetUtil.handleResponseB(httpresponse));
			if (gettimedata.status.equals("OK")) {
				s = gettimedata.time;
				LogicSettings.putTimeStamp(context, s);
			}
		}
		return s;
	}

	private static String getMethod() {
		return "get";
	}

	private static Map getParams() {
		return null;
	}

	private static String getUrl() {
		return "https://service.itouchchina.com/restsvcs/gettime";
	}

	public String error;
	public String status;
	public String time;
}
