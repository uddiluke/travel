//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.net;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

import com.tc.TCUtil;

// Referenced classes of package com.tc.net:
//            SiteGuideDownloadData

public class DownloadData {
	private class DownloadDataHandler extends DefaultHandler {

		public ArrayList getSiteGuideDownloadDatas() {
			return siteGuideDownloadDatas;
		}

		public void startDocument() throws SAXException {
			super.startDocument();
			siteGuideDownloadDatas = new ArrayList();
		}

		public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
			super.startElement(s, s1, s2, attributes);
			if ("DownloadData".equals(s1)) { // goto _L2; else goto _L1
				cgLastTimeStamp = attributes.getValue("cgLastTimeStamp");
				if (cgLastTimeStamp == null || cgLastTimeStamp.equals(""))
					cgLastTimeStamp = "0";
				sgLastTimeStamp = attributes.getValue("sgLastTimeStamp");
				if (sgLastTimeStamp == null || sgLastTimeStamp.equals(""))
					sgLastTimeStamp = "0";
			} else if ("SiteGuide".equals(s1)) {
				SiteGuideDownloadData siteguidedownloaddata = new SiteGuideDownloadData();
				siteguidedownloaddata.guidename = attributes.getValue("guidename");
				siteguidedownloaddata.cgid = Integer.parseInt(attributes.getValue("cgid"));
				siteguidedownloaddata.guidetype = attributes.getValue("guidetype");
				siteguidedownloaddata.guideid = Integer.parseInt(attributes.getValue("guideid"));
				siteguidedownloaddata.timestamp = Integer.parseInt(attributes.getValue("timestamp"));
				siteGuideDownloadDatas.add(siteguidedownloaddata);
			}

		}
	}

	public DownloadData() {
		cgLastTimeStamp = "0";
		sgLastTimeStamp = "0";
		siteGuideDownloadDatas = new ArrayList();
	}

	public DownloadData(InputStream inputstream) {
		parse(inputstream);
	}

	public void parse(InputStream inputstream) {
		try {
			SAXParser saxparser = SAXParserFactory.newInstance().newSAXParser();
			DownloadDataHandler downloaddatahandler = new DownloadDataHandler();
			saxparser.parse(inputstream, downloaddatahandler);
			siteGuideDownloadDatas = downloaddatahandler.getSiteGuideDownloadDatas();
			inputstream.close();
		} catch (SAXException e) {
			Log.e(TAG, "", e);
		} catch (ParserConfigurationException e) {
			Log.e(TAG, "", e);
		} catch (IOException e) {
			Log.e(TAG, "", e);
		}
	}

	public void saveXML() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, IOException {
		XmlSerializer xmlserializer = Xml.newSerializer();
		xmlserializer.setOutput(new FileOutputStream((new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina").append("/downloadData")
				.toString()), "utf-8");
		xmlserializer.startDocument("utf-8", Boolean.valueOf(true));
		xmlserializer.startTag("", "DownloadData");
		xmlserializer.attribute("", "cgLastTimeStamp", cgLastTimeStamp);
		xmlserializer.attribute("", "sgLastTimeStamp", sgLastTimeStamp);
		for (Iterator iterator = siteGuideDownloadDatas.iterator(); iterator.hasNext(); xmlserializer.endTag("", "SiteGuide")) {
			SiteGuideDownloadData siteguidedownloaddata = (SiteGuideDownloadData) iterator.next();
			xmlserializer.startTag("", "SiteGuide");
			xmlserializer.attribute("", "guidename", siteguidedownloaddata.guidename);
			xmlserializer.attribute("", "cgid", (new StringBuilder()).append("").append(siteguidedownloaddata.cgid).toString());
			xmlserializer.attribute("", "guidetype", siteguidedownloaddata.guidetype);
			xmlserializer.attribute("", "guideid", (new StringBuilder()).append("").append(siteguidedownloaddata.guideid).toString());
			xmlserializer.attribute("", "timestamp", (new StringBuilder()).append("").append(siteguidedownloaddata.timestamp).toString());
			xmlserializer.attribute("", "timestamp", "0");
		}

		xmlserializer.endTag("", "DownloadData");
		xmlserializer.endDocument();
	}

	public String cgLastTimeStamp;
	public String sgLastTimeStamp;
	public ArrayList siteGuideDownloadDatas;
	private static final String TAG = DownAsyncTask.class.getSimpleName();
}
