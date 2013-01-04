// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.logic;

import android.content.Context;
import android.util.Log;
import com.tc.net.NetUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CGLastUpdateData {
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
					cgLastUpdateData.status = characters;
				} else if ("error".equals(s1))
					cgLastUpdateData.error = characters;
				else if ("lastupdate".equals(s1))
					cgLastUpdateData.lastUpdate = characters;
				else if ("filesize".equals(s1))
					cgLastUpdateData.fileSize = Integer.parseInt(characters);
			}

		}

		public CGLastUpdateData getGetTimeData() {
			return cgLastUpdateData;
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
			else if ("cgupdate".equals(s1)) {
				level = 2;
				cgLastUpdateData = new CGLastUpdateData();
			} else if ("status".equals(s1) || "error".equals(s1) || !"lastupdate".equals(s1))
				;
		}

		private static final int IDLE_LEVEL = -1;
		private static final int UPDATE_LEVEL = 2;
		private static final int XML_LEVEL = 1;
		private CGLastUpdateData cgLastUpdateData;
		private String characters;
		private int level;
		private String tag;

		public Handler() {
		}
	}

	public static class Paraser {

		public static CGLastUpdateData parse(InputStream inputstream) {
			CGLastUpdateData cglastupdatedata;
			cglastupdatedata = null;
			if (inputstream != null) {

				// CGLastUpdateData cglastupdatedata1;
				try {
					Handler handler = new Handler();
					SAXParserFactory.newInstance().newSAXParser().parse(inputstream, handler);
					cglastupdatedata = handler.getGetTimeData();
				} catch (Exception e) {
					Log.e(TAG, "public static CGLastUpdateData parse(InputStream inputStream) ", e);
				}
				/*
				 * catch(SAXException e){ Log.e(TAG,
				 * "public static CGLastUpdateData parse(InputStream inputStream) "
				 * , e); }catch(IOException e){ Log.e(TAG,
				 * "public static CGLastUpdateData parse(InputStream inputStream) "
				 * , e); }
				 */
			}

			return cglastupdatedata;
		}

		private static final String TAG = CGLastUpdateData.Paraser.class.getSimpleName();

		public Paraser() {
		}
	}

	public CGLastUpdateData() {
	}

	public static CGLastUpdateData getLastUpdate(Context context, int i, String s, int j) {
		HashMap hashmap = new HashMap();
		hashmap.put("cgid", Integer.valueOf(i));
		hashmap.put("appversion", s);
		hashmap.put("timestamp", Integer.valueOf(j));
		CGLastUpdateData cglastupdatedata = Paraser.parse(NetUtil.getInputStream(NetUtil.getClient(), NetUtil.getHttpGet(Version_URL, null)));
		if (cglastupdatedata == null || !cglastupdatedata.status.equals("OK"))
			cglastupdatedata = null;
		return cglastupdatedata;
	}

	public String error;
	public int fileSize;
	public String lastUpdate;
	public String status;
	// "https://service.itouchchina.com/restsvcs/cgupdate
	public static final String Version_URL = "http://192.168.0.125:4242/version";
}
