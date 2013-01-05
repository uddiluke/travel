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

public class CGCommentData {
	public static class CGCommentDataParser {

		public static CGCommentData parse(InputStream inputstream) {
			CGCommentData cgcommentdata;
			cgcommentdata = null;
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
				cgcommentdata = handler.getCommentData();

			}
			return cgcommentdata;

		}

		public CGCommentDataParser() {
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
					commentData.status = characters;
				} else if ("error".equals(s1))
					commentData.error = characters;
				else if ("pointtype".equals(s1))
					commentData.type = characters;
				else if ("pointid".equals(s1))
					commentData.id = characters;
				else if ("avgmark".equals(s1))
					commentData.avgerageMark = Float.parseFloat(characters);
				else if ("markcount".equals(s1))
					commentData.markCount = Integer.parseInt(characters);
			}

		}

		public CGCommentData getCommentData() {
			return commentData;
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
			else if ("cgcomment".equals(s1)) {
				level = 2;
				commentData = new CGCommentData();
			} else if ("status".equals(s1) || "error".equals(s1) || "pointtype".equals(s1) || "pointid".equals(s1) || "avgmark".equals(s1)
					|| !"markcount".equals(s1))
				;
		}

		private static final int COMMENT_LEVEL = 2;
		private static final int IDLE_LEVEL = -1;
		private static final int XML_LEVEL = 1;
		private String characters;
		private CGCommentData commentData;
		private int level;
		private String tag;

		public Handler() {
			level = -1;
		}
	}

	public CGCommentData() {
	}

	public static CGCommentData cgComment(Context context, String s, String s1, int i, int j, String s2) {
		String s3 = LogicSettings.getToken(context, s);
		if (s3.length() > 0) { // goto _L2; else goto _L1
			String s4 = TimeStampData.getTimeStamp(context);
			if (s4.length() > 0) {
				CGCommentData cgcommentdata;
				HashMap hashmap = new HashMap();
				hashmap.put("user_token", s3);
				hashmap.put("pointid", Integer.valueOf(i));
				hashmap.put("pointtype", s1);
				hashmap.put("mark", Integer.valueOf(j));
				hashmap.put("comment", s2);
				hashmap.put("m", NetUtil.getMd5(s4, hashmap));
				hashmap.put("t", s4);
				cgcommentdata = CGCommentDataParser.parse(NetUtil.getInputStream(NetUtil.getClient(),
						NetUtil.getHttpPost("https://service.itouchchina.com/restsvcs/cgcomment", hashmap)));
				if (cgcommentdata != null && cgcommentdata.status.equals("OK"))
					return cgcommentdata;
			}
		}
		return null;

	}

	private static final String TAG = CGCommentData.class.getSimpleName();
	public float avgerageMark;
	public String error;
	public String id;
	public int markCount;
	public String status;
	public String type;

}
