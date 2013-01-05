//sample codes for lukeuddi(uddi.luke@gmail.com)



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

import android.content.Context;
import android.util.Log;

import com.tc.TCUtil;
import com.tc.net.NetUtil;

public class CGNewsData {
	public static class Handler extends DefaultHandler {

		public void characters(char ac[], int i, int j) throws SAXException {
			super.characters(ac, i, j);
			String s = new String(ac, i, j);
			if (s.trim().length() > 0)
				characters = s;
			if (url != null)
				url.append(s);
		}

		public void endDocument() throws SAXException {
			super.endDocument();
		}

		public void endElement(String s, String s1, String s2) throws SAXException {
			super.endElement(s, s1, s2);
			tag = null;
			if ("status".equals(s1))
				CGNewsData.status = characters;
			if ("error".equals(s1) && cgNewsData != null)
				cgNewsData.error = characters;
			if (top)
				if ("img".equals(s1))
					CGNewsData.topInfo.img = characters;
				else if ("summary".equals(s1))
					CGNewsData.topInfo.summary = characters;
				else if ("title".equals(s1))
					CGNewsData.topInfo.title = characters;
				else if ("url".equals(s1))
					CGNewsData.topInfo.url = characters;
				else if ("source".equals(s1))
					CGNewsData.topInfo.source = characters;
			if (level == 2)
				if ("title".equals(s1))
					cgNewsData.title = characters;
				else if ("catid".equals(s1))
					cgNewsData.catid = Integer.parseInt(characters);
				else if ("datetime".equals(s1))
					cgNewsData.dateTime = characters;
				else if ("img".equals(s1))
					cgNewsData.img = characters;
				else if ("thumbnail".equals(s1))
					cgNewsData.thumbnail = characters;
				else if ("url".equals(s1))
					cgNewsData.url = url.toString();
				else if ("summary".equals(s1))
					cgNewsData.summary = characters;
				else if ("source".equals(s1))
					cgNewsData.source = characters;
				else if ("flag".equals(s1))
					cgNewsData.flag = Integer.parseInt(characters);
				else if ("info".equals(s1))
					cgNewsList.add(cgNewsData);
		}

		public ArrayList getGetNewsData() {
			return cgNewsList;
		}

		public ArrayList getInforTypes() {
			return CGNewsData.inforList;
		}

		public TopInfo getTopInfo() {
			return CGNewsData.topInfo;
		}

		public void startDocument() throws SAXException {
			super.startDocument();
			tag = null;
			level = -1;
			cgNewsList = new ArrayList();
			CGNewsData.inforList = new ArrayList();
		}

		public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
			super.startElement(s, s1, s2, attributes);
			tag = s1;
			if ("xml".equals(s1)) {
				level = 1;
			} else if ("info".equals(s1)) {
				cgNewsData = new CGNewsData();
				cgNewsData.id = attributes.getValue("id");
			} else if (!"status".equals(s1) && !"error".equals(s1))
				if ("infors".equals(s1)) {
					if (attributes.getValue("timestamp") != null)
						CGNewsData.timestamp = Integer.parseInt(attributes.getValue("timestamp"));
				} else if ("infortype".equals(s1)) {
					level = 2;
					top = false;
				} else if ("type".equals(s1)) {
					inforType = new InforType();
					inforType.name = attributes.getValue("name");
					inforType.id = Integer.parseInt(attributes.getValue("id"));
					CGNewsData.inforList.add(inforType);
				} else if ("topinfo".equals(s1)) {
					CGNewsData.topInfo = new TopInfo();
					CGNewsData.topInfo.id = attributes.getValue("id");
					top = true;
				} else if ("url".equals(s1))
					url = new StringBuffer();

		}

		private static final int IDLE_LEVEL = -1;
		private static final int UPDATE_LEVEL = 2;
		private static final int XML_LEVEL = 1;
		private static boolean top = false;
		private CGNewsData cgNewsData;
		private ArrayList cgNewsList;
		private String characters;
		private InforType inforType;
		private int level;
		private String tag;
		private StringBuffer url;

		public Handler() {
		}
	}

	public static class InforType {

		public int id;
		public String name;

		public InforType() {
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
				arraylist = handler.getGetNewsData();
			}
			return arraylist;

		}

		private static final String TAG = Paraser.class.getSimpleName();

		public Paraser() {
		}
	}

	public static class TopInfo {

		public String id;
		public String img;
		public String source;
		public String summary;
		public String title;
		public String url;

		public TopInfo() {
		}
	}

	public CGNewsData() {
	}

	public static String getHtmlPath(String s, int i) {
		String s1 = (new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina/CG/").append(i).append("/").append("news/")
				.append(s.split("/")[-1 + s.split("/").length]).toString();
		String s2 = (new StringBuilder()).append(s1.substring(0, -4 + s1.length())).append("/").toString();
		String s3 = s2.split("/")[-1 + s2.split("/").length];
		return (new StringBuilder()).append(s2).append(s3).append(".html").toString();
	}

	public static InputStream getImageBitmap(String s) {
		return NetUtil.getInputStream(NetUtil.getClient(), NetUtil.getHttpGet(s, null));
	}

	public static ArrayList getInforTypes() {
		return inforList;
	}

	public static ArrayList getNewsData(Context context, int i, int j, int k) {
		HashMap hashmap = new HashMap();
		hashmap.put("cgid", Integer.valueOf(i));
		hashmap.put("timestamp", Integer.valueOf(j));
		hashmap.put("min", Integer.valueOf(k));
		InputStream inputstream = NetUtil.getInputStream(NetUtil.getClient(),
				NetUtil.getHttpGet("https://service.itouchchina.com/restsvcs/infors", hashmap));
		String s = (new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina/CG/").append(i).append("/").append("news/news.xml").toString();
		if (inputstream != null)
			TCUtil.saveNewFile(inputstream, s);
		return Paraser.parse(TCUtil.getFileInputStream(context, s));
	}

	public static ArrayList getNewsDataFromNative(String s) {
		ArrayList arraylist = null;
		try {
			arraylist = Paraser.parse(new FileInputStream(new File(s)));
		} catch (FileNotFoundException e) {
			Log.e(TAG, "", e);
		}
		return arraylist;

	}

	public static String getNewsZip(String s, int i) {
		InputStream inputstream = NetUtil.getInputStream(NetUtil.getClient(), NetUtil.getHttpGet(s, null));
		String s1 = (new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina/CG/").append(i).append("/").append("news/")
				.append(s.split("/")[-1 + s.split("/").length]).toString();
		String s2 = (new StringBuilder()).append(s1.substring(0, -4 + s1.length())).append("/").toString();
		if (inputstream != null) {
			TCUtil.saveNewFile(inputstream, s1);
			TCUtil.newsUnzip(s1, s2);
		}
		String s3 = s2.split("/")[-1 + s2.split("/").length];
		return (new StringBuilder()).append(s2).append(s3).append(".html").toString();
	}

	public static void getSmallImage(String s, int i) {
		InputStream inputstream = NetUtil.getInputStream(NetUtil.getClient(), NetUtil.getHttpGet(s, null));
		String s1 = (new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina/CG/").append(i).append("/").append("news/")
				.append(s.split("/")[-1 + s.split("/").length]).toString();
		if (inputstream != null)
			TCUtil.saveNewFile(inputstream, s1);
	}

	public static TopInfo getTopInfo() {
		return topInfo;
	}

	private static ArrayList inforList;
	public static String status;
	public static int timestamp;
	private static TopInfo topInfo;
	public int catid;
	public String dateTime;
	public String error;
	public int flag;
	public String id;
	public String img;
	public String source;
	public String summary;
	public String thumbnail;
	public String title;
	public String url;
	private static final String TAG = CGNewsData.class.getSimpleName();

	/*
	 * static ArrayList access$002(ArrayList arraylist) { inforList = arraylist;
	 * return arraylist; }
	 */

	/*
	 * static TopInfo access$102(TopInfo topinfo) { topInfo = topinfo; return
	 * topinfo; }
	 */
}
