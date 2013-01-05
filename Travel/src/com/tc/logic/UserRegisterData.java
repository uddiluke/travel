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

import com.tc.TCUtil;
import com.tc.net.NetUtil;

// Referenced classes of package com.tc.logic:
//            LogicSettings, TimeStampData

public class UserRegisterData {
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
					registerUserData.status = characters;
				} else if ("error".equals(s1))
					registerUserData.error = characters;
			}

		}

		public UserRegisterData getRegisterUserData() {
			return registerUserData;
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
			else if ("reguser".equals(s1)) {
				level = 2;
				registerUserData = new UserRegisterData();
			} else if ("status".equals(s1) || !"error".equals(s1))
				;
		}

		private static final int IDLE_LEVEL = -1;
		private static final int REGUSER_LEVEL = 2;
		private static final int XML_LEVEL = 1;
		private String characters;
		private int level;
		private UserRegisterData registerUserData;
		private String tag;

		public Handler() {
			level = -1;
		}
	}

	public static class UserRegisterDataParser {

		public static UserRegisterData parse(InputStream inputstream) {
			UserRegisterData userregisterdata;
			userregisterdata = null;
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
				userregisterdata = handler.getRegisterUserData();
			}
			return userregisterdata;

		}

		public UserRegisterDataParser() {
		}
	}

	public UserRegisterData() {
	}

	public static void userRegister(Context context, String s) {
		if (!LogicSettings.getApplicationRegistered(context, s)) {
			String s1 = TimeStampData.getTimeStamp(context);
			if (s1.length() > 0) {
				String s2 = TCUtil.getApplicationUserName(context, s);
				String s3 = TCUtil.getApplicationUserPassword(context, s);
				HashMap hashmap = new HashMap();
				hashmap.put("username", s2);
				hashmap.put("userpwd", s3);
				hashmap.put("m", NetUtil.getMd5(s1, hashmap));
				hashmap.put("t", s1);
				UserRegisterData userregisterdata = UserRegisterDataParser.parse(NetUtil.getInputStream(NetUtil.getClient(),
						NetUtil.getHttpPost("https://service.itouchchina.com/restsvcs/reguser", hashmap)));
				if (userregisterdata != null && (userregisterdata.status.equals("OK") || userregisterdata.status.equals("100"))) {
					LogicSettings.putToken(context, s, "");
					LogicSettings.putApplicationRegistered(context, s);
				}
			}
		}
	}

	private static final String TAG = UserRegisterData.class.getSimpleName();
	public String error;
	public String status;

}
