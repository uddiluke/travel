// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.logic;

import android.util.Log;
import com.tc.TCUtil;
import com.tc.net.NetUtil;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CGDownData {
	public static class CGDownDataParaser {

		public static com.tc.TCData.TCDownResult parse(InputStream inputstream) {
			com.tc.TCData.TCDownResult tcdownresult;
			tcdownresult = null;
			if (inputstream == null)
				return null;
			// com.tc.TCData.TCDownResult tcdownresult1;
			try {
				SAXParser saxparser = SAXParserFactory.newInstance().newSAXParser();
				Handler handler = new Handler();
				saxparser.parse(inputstream, handler);
				tcdownresult = handler.getDownResult();

			} catch (Exception e) {
				Log.e(CGDownData.TAG, "CommentData parse", e);
			}
			return tcdownresult;

		}

		public static com.tc.TCData.TCDownResult parse(byte abyte0[]) {
			ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte0);
			com.tc.TCData.TCDownResult tcdownresult = parse(((InputStream) (bytearrayinputstream)));
			TCUtil.inputstreamClose(bytearrayinputstream);
			return tcdownresult;
		}

		public CGDownDataParaser() {
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
				if ("status".equals(s1))
					downResult.status = characters;
				else if ("error".equals(s1))
					downResult.error = characters;
			}

		}

		public com.tc.TCData.TCDownResult getDownResult() {
			return downResult;
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
			else if ("cgdown".equals(s1)) {
				level = 2;
				downResult = new com.tc.TCData.TCDownResult();
			} else if ("status".equals(s1) || !"error".equals(s1))
				;
		}

		private static final int DOWN_LEVEL = 2;
		private static final int IDLE_LEVEL = -1;
		private static final int XML_LEVEL = 1;
		private String characters;
		private com.tc.TCData.TCDownResult downResult;
		private int level;
		private String tag;

		public Handler() {
			level = -1;
		}
	}

	public CGDownData() {
	}

	public static Map getCGAllSGTimeStamp(String s, String s1) {
		HashMap hashmap = new HashMap();
		hashmap.put("cgid", s);
		hashmap.put("guidetype", s1.toLowerCase());
		return hashmap;
	}

	public static Map getCGDown_newParams(String s, String s1, String s2, String s3) {
		HashMap hashmap = new HashMap();
		hashmap.put("cgid", s);
		hashmap.put("timestamp", s1);
		hashmap.put("lasttimestamp", s2);
		hashmap.put("m", NetUtil.getMd5(s3, hashmap));
		hashmap.put("t", s3);
		return hashmap;
	}

	public static Map getCGGuideLastUpdateParams(String s, String s1) {
		HashMap hashmap = new HashMap();
		hashmap.put("cgid", s);
		hashmap.put("guidetype", s1.toLowerCase());
		return hashmap;
	}

	public static Map getCGGuideUpdateParams(String s, String s1, String s2, String s3, String s4, String s5) {
		HashMap hashmap = new HashMap();
		hashmap.put("cgid", s);
		hashmap.put("cgapp", s1);
		hashmap.put("guidetype", s2.toLowerCase());
		hashmap.put("guideid", s3);
		hashmap.put("guideapp", s4);
		hashmap.put("timestamp", s5);
		return hashmap;
	}

	public static Map getCGUpdateParams(String s, String s1, String s2) {
		HashMap hashmap = new HashMap();
		hashmap.put("cgid", s);
		hashmap.put("appversion", s1);
		hashmap.put("timestamp", s2);
		return hashmap;
	}

	public static String getMethod() {
		return "get";
	}

	public static Map getSGDownParams(String s, String s1, String s2, String s3, String s4, String s5) {
		HashMap hashmap = new HashMap();
		hashmap.put("cgid", s);
		hashmap.put("guidetype", s1.toLowerCase());
		hashmap.put("guideid", s2);
		hashmap.put("timestamp", s3);
		hashmap.put("lasttimestamp", s4);
		hashmap.put("m", NetUtil.getMd5(s5, hashmap));
		hashmap.put("t", s5);
		return hashmap;
	}

	public static String getUrl() {
		// return "https://service.itouchchina.com/restsvcs/cgdown_new";
		return "http://192.168.0.125:4242/download_mirror";
	}

	private static final String TAG = CGDownData.class.getSimpleName();
	public com.tc.TCData.TCDownResult downResult;

}
