//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

// Referenced classes of package com.tc.net:
//            MyHttpClient

public class NetUtil {

	public NetUtil() {
	}

	public static HttpClient getClient() {
		BasicHttpParams basichttpparams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(basichttpparams, 10000);
		HttpConnectionParams.setSoTimeout(basichttpparams, 10000);
		HttpProtocolParams.setUserAgent(basichttpparams, (new StringBuilder()).append("Android").append(ANDROID_VERSION).toString());
		HttpClientParams.setRedirecting(basichttpparams, false);
		return new MyHttpClient(basichttpparams);
	}

	public static HttpGet getHttpGet(String s, Map map) {
		if (map != null) {
			for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
				java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
				String s1 = (new StringBuilder()).append(s).append("/").toString();
				s = (new StringBuilder()).append(s1).append((String) entry.getKey()).append("/").append(entry.getValue()).toString();
			}

		}
		return new HttpGet(s);
	}

	public static HttpGet getHttpGet(HttpClient httpclient, String s, Map map) {
		if (map != null) {
			for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
				java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
				String s1 = (new StringBuilder()).append(s).append("/").toString();
				s = (new StringBuilder()).append(s1).append((String) entry.getKey()).append("/").append(entry.getValue()).toString();
			}

		}
		return new HttpGet(s);
	}

	public static HttpPost getHttpPost(String s, Map map) {
		HttpPost httppost;
		httppost = new HttpPost(s);
		Log.i(TAG, s);
		if (map != null) {
			ArrayList arraylist;
			UrlEncodedFormEntity urlencodedformentity;
			arraylist = new ArrayList();
			java.util.Map.Entry entry;
			for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); arraylist.add(new BasicNameValuePair((String) entry.getKey(),
					String.valueOf(entry.getValue()))))
				entry = (java.util.Map.Entry) iterator.next();

			urlencodedformentity = null;

			try {
				urlencodedformentity = new UrlEncodedFormEntity(arraylist, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, "", e);
			}

			httppost.setEntity(urlencodedformentity);
		}
		return httppost;
		// UnsupportedEncodingException unsupportedencodingexception;
		// unsupportedencodingexception;
		// Log.e(TAG, unsupportedencodingexception.toString());

	}

	public static HttpPost getHttpPost(HttpClient httpclient, String s, Map map) {
		HttpPost httppost;
		httppost = new HttpPost(s);
		Log.i(TAG, s);
		if (map != null) {

			ArrayList arraylist;
			UrlEncodedFormEntity urlencodedformentity;
			arraylist = new ArrayList();
			java.util.Map.Entry entry;
			for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); arraylist.add(new BasicNameValuePair((String) entry.getKey(),
					String.valueOf(entry.getValue()))))
				entry = (java.util.Map.Entry) iterator.next();

			urlencodedformentity = null;
			try {
				urlencodedformentity = new UrlEncodedFormEntity(arraylist, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, "", e);
			}
			httppost.setEntity(urlencodedformentity);
		}
		return httppost;
	}

	public static HttpResponse getHttpResponse(HttpClient httpclient, HttpGet httpget) {
		HttpResponse httpresponse = null;
		try {
			httpresponse = httpclient.execute(httpget);
		} catch (ClientProtocolException e) {
			Log.e(TAG, "", e);
		} catch (IOException e) {
			Log.e(TAG, "", e);
		}

		return httpresponse;

	}

	public static HttpResponse getHttpResponse(HttpClient httpclient, HttpPost httppost) {
		HttpResponse httpresponse = null;

		try {
			httpresponse = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			Log.e(TAG, "", e);
		} catch (IOException e) {
			Log.e(TAG, "", e);
		}
		return httpresponse;

	}

	public static InputStream getInputStream(HttpClient httpclient, HttpGet httpget) {
		InputStream inputstream = null;
		try {
			inputstream = handleResponse(httpclient.execute(httpget));
		} catch (ClientProtocolException e) {
			Log.e(TAG, "", e);
		} catch (IOException e) {
			Log.e(TAG, "", e);
		}

		return inputstream;

	}

	public static InputStream getInputStream(HttpClient httpclient, HttpPost httppost) {
		InputStream inputstream = null;

		try {
			inputstream = handleResponse(httpclient.execute(httppost));
		} catch (ClientProtocolException e) {
			Log.e(TAG, "", e);
		} catch (IOException e) {
			Log.e(TAG, "", e);
		}
		return inputstream;

	}

	public static String getMd5(String s, Map map) {
		ArrayList arraylist = new ArrayList();
		java.util.Map.Entry entry;
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); arraylist.add((new StringBuilder()).append((String) entry.getKey())
				.append("=").append(entry.getValue()).toString()))
			entry = (java.util.Map.Entry) iterator.next();

		Collections.sort(arraylist);
		StringBuffer stringbuffer = new StringBuffer();
		int i = arraylist.size();
		for (int j = 0; j < i; j++) {
			stringbuffer.append((String) arraylist.get(j));
			if (j != i - 1)
				stringbuffer.append("&");
		}

		return toMd5((new StringBuilder()).append(s).append("itouch").append(stringbuffer.toString()).append("china").toString());
	}

	public static InputStream handleResponse(HttpResponse httpresponse) {
		InputStream inputstream = null;
		if (httpresponse != null) { // goto _L2; else goto _L1
			int i = httpresponse.getStatusLine().getStatusCode();
			if (i == 200 || i == 555) {
				try {
					inputstream = httpresponse.getEntity().getContent();
				} catch (IllegalStateException e) {
					Log.e(TAG, "", e);
				} catch (IOException e) {
					Log.e(TAG, "", e);
				}
			} else if (i == 301 || i == 302) {
				Header aheader[] = httpresponse.getHeaders("Location");
				if (aheader != null && aheader.length != 0) {

					String s = aheader[-1 + aheader.length].getValue();
					inputstream = getInputStream(getClient(), getHttpGet(s, null));

				}
			}
		}
		return inputstream;

	}

	public static byte[] handleResponseB(HttpResponse httpresponse) {
		byte abyte0[] = null;
		if (httpresponse != null && httpresponse.getStatusLine().getStatusCode() == 200)
			try {
				abyte0 = EntityUtils.toByteArray(httpresponse.getEntity());
				String s = new String(abyte0);
				Log.i(TAG, s);
			} catch (IOException ioexception) {
				Log.e(TAG, ioexception.toString());
			}
		return abyte0;
	}

	public static String toHexString(byte abyte0[]) {
		StringBuilder stringbuilder = new StringBuilder(2 * abyte0.length);
		for (int i = 0; i < abyte0.length; i++) {
			stringbuilder.append(HEX_DIGITS[(0xf0 & abyte0[i]) >>> 4]);
			stringbuilder.append(HEX_DIGITS[0xf & abyte0[i]]);
		}

		return stringbuilder.toString();
	}

	public static String toMd5(String s) {
		return toMd5(s.getBytes());
	}

	public static String toMd5(byte abyte0[]) {
		String s = "";
		MessageDigest messagedigest;
		try {
			messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.update(abyte0);
			s = toHexString(messagedigest.digest());
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "", e);
		}
		return s;
	}

	private static final String ANDROID_VERSION;
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static final String TAG = NetUtil.class.getSimpleName();

	static {
		ANDROID_VERSION = android.os.Build.VERSION.RELEASE;
	}
}
