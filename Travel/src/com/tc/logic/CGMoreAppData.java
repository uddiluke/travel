// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.tc.TCUtil;
import com.tc.net.NetUtil;

public class CGMoreAppData {
	public static class Handler extends DefaultHandler {

		public void characters(char ac[], int i, int j) throws SAXException {
			super.characters(ac, i, j);
			String s = new String(ac, i, j);
			if (s.trim().length() > 0)
				characters = s;
			if (url != null)
				url.append(s);
			if (desc != null)
				desc.append(s);
		}

		public void endDocument() throws SAXException {
			super.endDocument();
		}

		public void endElement(String s, String s1, String s2) throws SAXException {
			super.endElement(s, s1, s2);
			tag = null;
			if (level == 3)
				if ("id".equals(s1))
					CGMoreAppData.topApp.id = characters;
				else if ("name".equals(s1))
					CGMoreAppData.topApp.name = characters;
				else if ("type".equals(s1))
					CGMoreAppData.topApp.type = characters;
				else if ("icon".equals(s1))
					CGMoreAppData.topApp.icon = characters;
				else if ("timestamp".equals(s1))
					CGMoreAppData.topApp.timestamp = characters;
				else if ("summary".equals(s1))
					CGMoreAppData.topApp.summary = characters;
				else if ("topimg".equals(s1))
					CGMoreAppData.topApp.topimage = characters;
			if (level == 2)
				if ("id".equals(s1))
					cgMoreAppData.id = characters;
				else if ("name".equals(s1))
					cgMoreAppData.name = characters;
				else if ("type".equals(s1))
					cgMoreAppData.type = characters;
				else if ("icon".equals(s1))
					cgMoreAppData.icon = characters;
				else if ("url".equals(s1))
					cgMoreAppData.url = url.toString();
				else if ("os".equals(s1))
					cgMoreAppData.os = characters;
				else if ("timestamp".equals(s1))
					cgMoreAppData.timestamp = characters;
				else if ("summary".equals(s1))
					cgMoreAppData.summary = characters;
				else if ("desc".equals(s1))
					cgMoreAppData.desc = desc.toString();
				else if ("item".equals(s1))
					CGMoreAppData.screenShotsList.add(characters);
				else if ("app".equals(s1))
					cgMoreAppList.add(cgMoreAppData);
			if (level == 4)
				if ("status".equals(s1))
					CGMoreAppData.status = characters;
				else if ("timestamp".equals(s1))
					CGMoreAppData.timestampApps = characters;
		}

		public ArrayList getMoreAppData() {
			return cgMoreAppList;
		}

		public ArrayList getScreenShots() {
			return CGMoreAppData.screenShotsList;
		}

		public TopApp getTopApp() {
			return CGMoreAppData.topApp;
		}

		public void startDocument() throws SAXException {
			super.startDocument();
			tag = null;
			level = -1;
			cgMoreAppList = new ArrayList();
			CGMoreAppData.topApp = new TopApp();
		}

		public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
			super.startElement(s, s1, s2, attributes);
			tag = s1;
			if ("xml".equals(s1)) {
				level = 1;
			} else if ("apps".equals(s1))
				level = 4;
			else if (!"error".equals(s1))
				if ("topapp".equals(s1))
					level = 3;
				else if ("app".equals(s1)) {
					cgMoreAppData = new CGMoreAppData();
					level = 2;
				} else if ("screenShots".equals(s1))
					CGMoreAppData.screenShotsList = new ArrayList();
				else if ("url".equals(s1))
					url = new StringBuffer();
				else if ("desc".equals(s1))
					desc = new StringBuffer();

		}

		private static final int APPS_LEVEL = 4;
		private static final int IDLE_LEVEL = -1;
		private static final int TOP_LEVEL = 3;
		private static final int UPDATE_LEVEL = 2;
		private static final int XML_LEVEL = 1;
		private CGMoreAppData cgMoreAppData;
		private ArrayList cgMoreAppList;
		private String characters;
		private StringBuffer desc;
		private int level;
		private String tag;
		private StringBuffer url;

		public Handler() {
		}
	}

	public static class Paraser {

		public static ArrayList parse(InputStream inputstream) {
			ArrayList arraylist;
			arraylist = null;
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
				arraylist = handler.getMoreAppData();
			}
			return arraylist;

		}

		private static final String TAG = Paraser.class.getSimpleName();

		public Paraser() {
		}
	}

	public static class TopApp {

		public String icon;
		public String id;
		public String name;
		public String summary;
		public String timestamp;
		public String topimage;
		public String type;

		public TopApp() {
		}
	}

	public CGMoreAppData() {
	}

	public static InputStream getImageBitmap(String s) {
		return NetUtil.getInputStream(NetUtil.getClient(), NetUtil.getHttpGet(s, null));
	}

	public static ArrayList getMoreAppData(String s, String s1) {
		ArrayList arraylist;
		InputStream inputstream = NetUtil.getInputStream(NetUtil.getClient(), NetUtil.getHttpGet(s, new HashMap()));
		if (inputstream != null)
			TCUtil.saveNewFile(inputstream, s1);
		arraylist = null;
		try {
			arraylist = Paraser.parse(new FileInputStream(new File(s1)));
		} catch (FileNotFoundException e) {
			Log.e("CGMOREAPPDATA", "", e);
		}

		return arraylist;

	}

	public static ArrayList getMoreAppItemData(String s) {
		HashMap hashmap = new HashMap();
		hashmap.put("id", s);
		return Paraser
				.parse(NetUtil.getInputStream(NetUtil.getClient(), NetUtil.getHttpGet("https://service.itouchchina.com/restsvcs/appi", hashmap)));
	}

	public static ArrayList getMoreDataFromNative(String s) {
		ArrayList arraylist = null;
		try {
			arraylist = Paraser.parse(new FileInputStream(new File(s)));
		} catch (FileNotFoundException e) {
			Log.e("CGMoreAPPData", "", e);
		}
		return arraylist;

	}

	public static ArrayList getScreenShots() {
		return screenShotsList;
	}

	public static InputStream getSmallImage(String s) {
		return NetUtil.getInputStream(NetUtil.getClient(), NetUtil.getHttpGet(s, null));
	}

	public static TopApp getTopApp() {
		return topApp;
	}

	private static ArrayList screenShotsList;
	public static String status;
	public static String timestampApps;
	private static TopApp topApp;
	public String desc;
	public String icon;
	public String id;
	public String name;
	public String os;
	public String summary;
	public String timestamp;
	public String type;
	public String url;

	/*
	 * static TopApp access$002(TopApp topapp) { topApp = topapp; return topapp;
	 * }
	 */

	/*
	 * static ArrayList access$102(ArrayList arraylist) { screenShotsList =
	 * arraylist; return arraylist; }
	 */
}
