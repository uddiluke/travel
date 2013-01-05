//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;

import com.tc.net.NetUtil;

// Referenced classes of package com.tc.logic:
//            LogicSettings, TimeStampData

public class CGMarkData {
	public static class CGMarkDataParser {

		public static CGMarkData parse(InputStream inputstream) {
			CGMarkData cgmarkdata;
			cgmarkdata = null;
			if (inputstream != null) {

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
				cgmarkdata = handler.getMarkData();
			}

			return cgmarkdata;

		}

		public CGMarkDataParser() {
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
			super.endElement(s, s1, s2);
			tag = null;
			if (level == 2) { // goto _L2; else goto _L1
				if ("status".equals(s1)) {
					markData.status = characters;
				} else if ("error".equals(s1))
					markData.error = characters;
				else if ("pointtype".equals(s1))
					markData.type = characters;
				else if ("pointid".equals(s1))
					markData.id = characters;
				else if ("avgmark".equals(s1))
					markData.avgerageMark = Float.parseFloat(characters);
				else if ("markcount".equals(s1))
					markData.markCount = Integer.parseInt(characters);
				else if ("mark".equals(s1))
					markData.mark = Float.parseFloat(characters);
			}

		}

		public CGMarkData getMarkData() {
			return markData;
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
			else if ("cgmark".equals(s1)) {
				level = 2;
				markData = new CGMarkData();
			} else if ("status".equals(s1) || "error".equals(s1) || "pointtype".equals(s1) || "pointid".equals(s1) || "avgmark".equals(s1)
					|| !"markcount".equals(s1))
				;
		}

		private static final int IDLE_LEVEL = -1;
		private static final int MARK_LEVEL = 2;
		private static final int XML_LEVEL = 1;
		private String characters;
		private int level;
		private CGMarkData markData;
		private String tag;

		public Handler() {
			level = -1;
		}
	}

	public CGMarkData() {
	}

	public static CGMarkData cgMark(Context context, String s, String s1, int i) {
		if (LogicSettings.getToken(context, s).length() > 0) {
			String s2 = TimeStampData.getTimeStamp(context);
			if (s2.length() > 0) {
				CGMarkData cgmarkdata;
				HashMap hashmap = new HashMap();
				hashmap.put("pointid", Integer.valueOf(i));
				hashmap.put("pointtype", s1);
				hashmap.put("m", NetUtil.getMd5(s2, hashmap));
				hashmap.put("t", s2);
				cgmarkdata = CGMarkDataParser.parse(NetUtil.getInputStream(NetUtil.getClient(),
						NetUtil.getHttpGet("https://service.itouchchina.com/restsvcs/cgmark", hashmap)));
				if (cgmarkdata != null && cgmarkdata.status.equals("OK"))
					return cgmarkdata;
			}
		}
		return null;

	}

	private static final String TAG = CGMarkData.class.getSimpleName();
	public float avgerageMark;
	public String error;
	public String id;
	public float mark;
	public int markCount;
	public String status;
	public String type;

}
