// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.tc.net.NetUtil;

public class CGGalleryData {
	public static class CGGalleryXmlData {

		public List items;
		public int modified;

		public CGGalleryXmlData() {
		}

		public static class ImageItem {

			public String content;
			public int id;
			public String image;
			public int modified;
			public long size;
			public String url;

			public ImageItem() {
			}
		}
	}

	public static class Parser {

		public static CGGalleryXmlData parse(Context context, InputStream inputstream) {
			CGGalleryXmlData cggalleryxmldata = null;
			XmlPullParser xmlpullparser;
			int i;
			xmlpullparser = Xml.newPullParser();
			try {
				xmlpullparser.setInput(inputstream, "UTF-8");
				i = xmlpullparser.getEventType();
				while (i != XmlPullParser.END_DOCUMENT) {
					if (i == XmlPullParser.START_TAG) {
						if ("gallery".equals(xmlpullparser.getName())) {
							cggalleryxmldata = new CGGalleryXmlData();
							cggalleryxmldata.items = new ArrayList();
							if (xmlpullparser.getAttributeCount() >= 1)
								cggalleryxmldata.modified = Integer.parseInt(xmlpullparser.getAttributeValue(null, "modified"));
						} else if ("item".equals(xmlpullparser.getName()) && xmlpullparser.getAttributeCount() >= 1) {
							CGGalleryXmlData.ImageItem imageitem = new CGGalleryXmlData.ImageItem();
							imageitem.id = Integer.parseInt(xmlpullparser.getAttributeValue(null, "id"));
							imageitem.content = xmlpullparser.getAttributeValue(null, "content");
							imageitem.image = xmlpullparser.getAttributeValue(null, "image");
							imageitem.url = xmlpullparser.getAttributeValue(null, "url");
							imageitem.modified = Integer.parseInt(xmlpullparser.getAttributeValue(null, "image_modified"));
							cggalleryxmldata.items.add(imageitem);

						}
						i = xmlpullparser.next();
					}
				}
			} catch (IOException e) {
				Log.e(TAG, "", e);
			} catch (XmlPullParserException e) {
				Log.e(TAG, "", e);
			}
			return cggalleryxmldata;
		}

		public Parser() {
		}
	}

	public CGGalleryData() {
	}

	public static InputStream cgGalleryImage(Context context, String s, int i, CGGalleryXmlData.ImageItem imageitem) {
		HashMap hashmap = new HashMap();
		hashmap.put("cgid", Integer.valueOf(i));
		hashmap.put("imageid", Integer.valueOf(imageitem.id));
		return getInputStream(NetUtil.getClient(), NetUtil.getHttpGet("https://service.itouchchina.com/restsvcs/galleryimage", hashmap), imageitem);
	}

	public static InputStream cgGalleryXml(Context context, String s, int i, String s1) {
		HashMap hashmap = new HashMap();
		hashmap.put("cgid", Integer.valueOf(i));
		hashmap.put("t", s1);
		return NetUtil.getInputStream(NetUtil.getClient(), NetUtil.getHttpGet("https://service.itouchchina.com/restsvcs/galleryxml", hashmap));
	}

	private static InputStream getInputStream(HttpClient httpclient, HttpGet httpget, CGGalleryXmlData.ImageItem imageitem) {
		InputStream inputstream = null;
		HttpResponse httpresponse;
		try {
			httpresponse = httpclient.execute(httpget);
			if (httpresponse != null)
				imageitem.size = httpresponse.getEntity().getContentLength();
			inputstream = NetUtil.handleResponse(httpresponse);
		} catch (ClientProtocolException e) {
			Log.e(TAG, "", e);
		} catch (IOException e) {
			Log.e(TAG, "", e);
		}

		return inputstream;

	}

	private static final String TAG = CGGalleryData.class.getSimpleName();

}
