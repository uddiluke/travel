//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.tc.community.CommunityUtil;

public class CGService extends Service {
	private class InitTimeStampHandler extends DefaultHandler {

		public void startDocument() throws SAXException {
			super.startDocument();
			initTimeStamps = new HashMap();
		}

		public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
			super.startElement(s, s1, s2, attributes);
			if ("item".equals(s1))
				initTimeStamps.put(Integer.valueOf(Integer.parseInt(attributes.getValue("id"))),
						Integer.valueOf(Integer.parseInt(attributes.getValue("timestamp"))));
		}

		HashMap initTimeStamps;

	}

	/**
	 * dispose time zone change for foreign people.
	 */
	public CGService() {
	}

	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onCreate() {
		super.onCreate();
		IntentFilter intentfilter = new IntentFilter();
		intentfilter.addAction(Intent.ACTION_TIME_CHANGED);
		intentfilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
		broadcastReceiver = new BroadcastReceiver() {

			public void onReceive(Context context, Intent intent) {
				CommunityUtil.setTD(context);
			}

		};
		registerReceiver(broadcastReceiver, intentfilter);
		CommunityUtil.setTD(this);
		try {
			InputStream inputstream = getAssets().open("initTimeStamp.xml");
			SAXParser saxparser = SAXParserFactory.newInstance().newSAXParser();
			InitTimeStampHandler inittimestamphandler = new InitTimeStampHandler();
			saxparser.parse(inputstream, inittimestamphandler);
			initTimeStamps = inittimestamphandler.initTimeStamps;
			inputstream.close();
		} catch (Exception e) {
			Log.e(TAG, "", e);
		}

	}

	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	private static final String TAG = CGService.class.getSimpleName();
	public static int TD;
	public static HashMap initTimeStamps;
	private BroadcastReceiver broadcastReceiver;

}
