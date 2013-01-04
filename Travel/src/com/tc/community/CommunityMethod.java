// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.community;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

import com.tc.net.NetUtil;

// Referenced classes of package com.tc.community:
//            CommunityUtil

public class CommunityMethod {

	public CommunityMethod() {
	}

	public static JSONObject getAnonymousToken() {
		JSONObject jsonobject;
		HttpClient httpclient;
		HttpGet httpget;
		jsonobject = null;
		httpclient = NetUtil.getClient();
		httpget = new HttpGet(CommunityUtil.getTimeAndMD5("https://service.itouchchina.com/commentsvcs/anonymoustoken"));
		JSONObject jsonobject1;
		try {
			HttpResponse httpresponse = httpclient.execute(httpget);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {

				String s = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s);
				jsonobject1 = new JSONObject(s);
				jsonobject = jsonobject1;

				httpget.abort();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonobject;

	}

	public static JSONObject getCommentList(String s, String s1, String s2, String s3, String s4, String s5, String s6) {
		JSONObject jsonobject = null;
		HttpClient httpclient = NetUtil.getClient();
		String s7;
		HttpGet httpget;
		HttpResponse httpresponse;
		String s8;
		JSONObject jsonobject1;
		if (s5 == null || "".equals(s5))
			s7 = (new StringBuilder()).append("https://service.itouchchina.com/commentsvcs/commentlist/appname/").append(s).append("/timeline/")
					.append(s1).append("/offset/").append(s2).append("/max/").append(s3).append("/sort/").append(s4).toString();
		else
			s7 = (new StringBuilder()).append("https://service.itouchchina.com/commentsvcs/commentlist/appname/").append(s).append("/timeline/")
					.append(s1).append("/offset/").append(s2).append("/max/").append(s3).append("/sort/").append(s4).append("/pointtype/").append(s5)
					.append("/pointid/").append(s6).toString();
		httpget = new HttpGet(CommunityUtil.getTimeAndMD5(s7));
		try {
			httpresponse = httpclient.execute(httpget);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {

				s8 = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s8);
				jsonobject1 = new JSONObject(s8);
				jsonobject = jsonobject1;

				httpget.abort();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonobject;

	}

	public static JSONObject getCurTime() {
		JSONObject jsonobject;
		HttpClient httpclient;
		HttpGet httpget;
		jsonobject = null;
		httpclient = NetUtil.getClient();
		httpget = new HttpGet("https://service.itouchchina.com/commentsvcs/curtime");
		try {
			HttpResponse httpresponse = httpclient.execute(httpget);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {
				String s = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s);
				jsonobject = new JSONObject(s);

			}
			httpget.abort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonobject;
	}

	public static JSONObject getLastCommentTime(String s, String s1, String s2, String s3) {
		JSONObject jsonobject;
		HttpClient httpclient;
		HttpGet httpget;
		jsonobject = null;
		httpclient = NetUtil.getClient();
		httpget = new HttpGet(CommunityUtil.getTimeAndMD5((new StringBuilder())
				.append("https://service.itouchchina.com/commentsvcs/lastcommenttime/oauth_token/").append(s).append("/appname/").append(s1)
				.append("/pointtype/").append(s2).append("/pointid/").append(s3).toString()));
		try {
			HttpResponse httpresponse = httpclient.execute(httpget);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {
				String s4 = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s4);
				jsonobject = new JSONObject(s4);

			}
			httpget.abort();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonobject;

	}

	public static JSONObject getMyComments(String s, String s1, String s2, String s3, String s4, String s5, String s6) {
		JSONObject jsonobject = null;
		HttpClient httpclient = NetUtil.getClient();
		String s7;
		HttpGet httpget;
		HttpResponse httpresponse;

		if (s5 == null || "".equals(s5))
			s7 = (new StringBuilder()).append("https://service.itouchchina.com/commentsvcs/mycomments/oauth_token/").append(s).append("/appname/")
					.append(s1).append("/timeline/").append(s2).append("/offset/").append(s3).append("/max/").append(s4).toString();
		else
			s7 = (new StringBuilder()).append("https://service.itouchchina.com/commentsvcs/mycomments/oauth_token/").append(s).append("/appname/")
					.append(s1).append("/timeline/").append(s2).append("/offset/").append(s3).append("/max/").append(s4).append("/pointtype/")
					.append(s5).append("/pointid/").append(s6).toString();
		httpget = new HttpGet(CommunityUtil.getTimeAndMD5(s7));
		try {
			httpresponse = httpclient.execute(httpget);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {

				s7 = EntityUtils.toString(httpresponse.getEntity());

				jsonobject = new JSONObject(s7);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		httpget.abort();
		return jsonobject;

	}

	public static JSONObject getMyNoReads(String s, String s1, String s2, String s3) {
		JSONObject jsonobject = null;
		HttpClient httpclient = NetUtil.getClient();
		String s4;
		HttpGet httpget;
		HttpResponse httpresponse;

		if (s2 == null || "".equals(s2))
			s4 = (new StringBuilder()).append("https://service.itouchchina.com/commentsvcs/mynoreads/oauth_token/").append(s).append("/appname/")
					.append(s1).toString();
		else
			s4 = (new StringBuilder()).append("https://service.itouchchina.com/commentsvcs/mynoreads/oauth_token/").append(s).append("/appname/")
					.append(s1).append("/pointtype/").append(s2).append("/pointid/").append(s3).toString();
		httpget = new HttpGet(CommunityUtil.getTimeAndMD5(s4));
		try {
			httpresponse = httpclient.execute(httpget);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {

				s4 = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s4);
				jsonobject = new JSONObject(s4);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		httpget.abort();
		return jsonobject;

	}

	public static JSONObject getReplies(String s, String s1, String s2, String s3, String s4, String s5) {
		JSONObject jsonobject = null;
		HttpClient httpclient = NetUtil.getClient();
		String s6;
		HttpGet httpget;
		HttpResponse httpresponse;

		if (s4 == null || "".equals(s4))
			s6 = (new StringBuilder()).append("https://service.itouchchina.com/commentsvcs/replies/commentid/").append(s).append("/timeline/")
					.append(s1).append("/offset/").append(s2).append("/max/").append(s3).toString();
		else
			s6 = (new StringBuilder()).append("https://service.itouchchina.com/commentsvcs/replies/commentid/").append(s).append("/timeline/")
					.append(s1).append("/offset/").append(s2).append("/max/").append(s3).append("/oauth_token/").append(s4).append("/appname/")
					.append(s5).toString();
		httpget = new HttpGet(CommunityUtil.getTimeAndMD5(s6));
		try {
			httpresponse = httpclient.execute(httpget);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {

				s6 = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s6);
				jsonobject = new JSONObject(s6);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		httpget.abort();
		return jsonobject;

	}

	public static JSONObject postBind(String s, String s1, String s2, String s3, String s4, String s5, boolean flag) {
		JSONObject jsonobject = null;
		HttpClient httpclient = NetUtil.getClient();
		HttpPost httppost = new HttpPost("https://service.itouchchina.com/commentsvcs/bind");
		ArrayList arraylist = new ArrayList();
		arraylist.add(new BasicNameValuePair("oauth_token", s));
		arraylist.add(new BasicNameValuePair("appname", s1));
		arraylist.add(new BasicNameValuePair("type", s2));
		arraylist.add(new BasicNameValuePair("new_uid", s3));
		arraylist.add(new BasicNameValuePair("new_oauth_token", s4));
		arraylist.add(new BasicNameValuePair("new_oauth_token_secret", s5));
		String s6;
		HttpResponse httpresponse;

		if (flag)
			s6 = "true";
		else
			s6 = "false";
		arraylist.add(new BasicNameValuePair("forcebind", s6));
		CommunityUtil.addTimeAndMD5(arraylist);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(arraylist, "UTF-8"));
			httpresponse = httpclient.execute(httppost);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {

				s6 = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s6);
				jsonobject = new JSONObject(s6);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		httppost.abort();
		return jsonobject;

	}

	public static JSONObject postChangePwd(String s, String s1, String s2) {
		JSONObject jsonobject;
		HttpClient httpclient;
		HttpPost httppost;
		ArrayList arraylist;
		jsonobject = null;
		httpclient = NetUtil.getClient();
		httppost = new HttpPost("https://service.itouchchina.com/commentsvcs/changepwd");
		arraylist = new ArrayList();
		arraylist.add(new BasicNameValuePair("oauth_token", s));
		arraylist.add(new BasicNameValuePair("appname", s1));
		arraylist.add(new BasicNameValuePair("newpwd", s2));
		CommunityUtil.addTimeAndMD5(arraylist);

		try {
			httppost.setEntity(new UrlEncodedFormEntity(arraylist, "UTF-8"));
			HttpResponse httpresponse = httpclient.execute(httppost);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {

				String s3 = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s3);
				jsonobject = new JSONObject(s3);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		httppost.abort();
		return jsonobject;

	}

	public static JSONObject postComment(String s, String s1, String s2, String s3) {
		JSONObject jsonobject;
		HttpClient httpclient;
		HttpPost httppost;
		ArrayList arraylist;
		jsonobject = null;
		httpclient = NetUtil.getClient();
		httppost = new HttpPost("https://service.itouchchina.com/commentsvcs/comment");
		arraylist = new ArrayList();
		arraylist.add(new BasicNameValuePair("oauth_token", s));
		arraylist.add(new BasicNameValuePair("appname", s1));
		arraylist.add(new BasicNameValuePair("content", s2));
		arraylist.add(new BasicNameValuePair("pid", s3));
		CommunityUtil.addTimeAndMD5(arraylist);

		try {
			httppost.setEntity(new UrlEncodedFormEntity(arraylist, "UTF-8"));
			HttpResponse httpresponse = httpclient.execute(httppost);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {

				String s4 = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s4);
				jsonobject = new JSONObject(s4);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		httppost.abort();
		return jsonobject;

	}

	public static JSONObject postComment(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8, String s9,
			String s10) {
		JSONObject jsonobject;
		HttpClient httpclient;
		HttpPost httppost;
		MultipartEntity multipartentity;
		HashMap hashmap;
		jsonobject = null;
		httpclient = NetUtil.getClient();
		httppost = new HttpPost("https://service.itouchchina.com/commentsvcs/comment");
		multipartentity = new MultipartEntity();
		hashmap = new HashMap();
		try {
			multipartentity.addPart("oauth_token", new StringBody(s));
			multipartentity.addPart("appname", new StringBody(s1, Charset.forName("UTF-8")));
			multipartentity.addPart("pointtype", new StringBody(s2));
			multipartentity.addPart("pointid", new StringBody(s3));
			multipartentity.addPart("mark", new StringBody(s4));
			multipartentity.addPart("content", new StringBody(s5, Charset.forName("UTF-8")));
			hashmap.put("oauth_token", s);
			hashmap.put("appname", s1);
			hashmap.put("pointtype", s2);
			hashmap.put("pointid", s3);
			hashmap.put("mark", s4);
			hashmap.put("content", s5);
			if (s6 != null && !"".equals(s6)) { // goto _L2; else goto _L1
				multipartentity.addPart("pic", new FileBody(new File(s6)));
			}

			if (s7 != null && !"".equals(s7)) {
				multipartentity.addPart("transmit", new StringBody(s7));
				multipartentity.addPart("extra", new StringBody(s10, Charset.forName("UTF-8")));
				hashmap.put("transmit", s7);
				hashmap.put("extra", s10);
			} else {
				if (!"".equals(s8)) {
					multipartentity.addPart("la", new StringBody(s8));
					multipartentity.addPart("lo", new StringBody(s9));
					hashmap.put("la", s8);
					hashmap.put("lo", s9);
				}
				String s12 = CommunityUtil.getServerTime();
				multipartentity.addPart("t", new StringBody(s12));
				multipartentity.addPart("m", new StringBody(NetUtil.getMd5(s12, hashmap)));
			}
			JSONObject jsonobject1;
			httppost.setEntity(multipartentity);
			HttpResponse httpresponse = httpclient.execute(httppost);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {

				String s11 = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s11);
				jsonobject1 = new JSONObject(s11);
				jsonobject = jsonobject1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		httppost.abort();
		return jsonobject;

	}

	public static JSONObject postLikeComment(String s, String s1, String s2) {
		JSONObject jsonobject;
		HttpClient httpclient;
		HttpPost httppost;
		ArrayList arraylist;
		jsonobject = null;
		httpclient = NetUtil.getClient();
		httppost = new HttpPost("https://service.itouchchina.com/commentsvcs/likecomment");
		arraylist = new ArrayList();
		arraylist.add(new BasicNameValuePair("oauth_token", s));
		arraylist.add(new BasicNameValuePair("appname", s1));
		arraylist.add(new BasicNameValuePair("commentid", s2));
		CommunityUtil.addTimeAndMD5(arraylist);
		JSONObject jsonobject1;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(arraylist, "UTF-8"));
			HttpResponse httpresponse = httpclient.execute(httppost);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {

				String s3 = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s3);
				jsonobject1 = new JSONObject(s3);
				jsonobject = jsonobject1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		httppost.abort();
		return jsonobject;

	}

	public static JSONObject postOtherUserLogin(String s, String s1, String s2, String s3, String s4) {
		JSONObject jsonobject;
		HttpClient httpclient;
		HttpPost httppost;
		ArrayList arraylist;
		jsonobject = null;
		httpclient = NetUtil.getClient();
		httppost = new HttpPost("https://service.itouchchina.com/commentsvcs/otherlogin");
		arraylist = new ArrayList();
		arraylist.add(new BasicNameValuePair("type", s));
		arraylist.add(new BasicNameValuePair("uid", s1));
		arraylist.add(new BasicNameValuePair("oauth_token", s2));
		arraylist.add(new BasicNameValuePair("oauth_token_secret", s3));
		arraylist.add(new BasicNameValuePair("appname", s4));
		CommunityUtil.addTimeAndMD5(arraylist);
		JSONObject jsonobject1;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(arraylist, "UTF-8"));
			HttpResponse httpresponse = httpclient.execute(httppost);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {

				String s5 = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s5);
				jsonobject1 = new JSONObject(s5);
				jsonobject = jsonobject1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		httppost.abort();
		return jsonobject;

	}

	public static JSONObject postRegistry(String s, String s1, String s2, String s3) {
		JSONObject jsonobject;
		HttpClient httpclient;
		HttpPost httppost;
		ArrayList arraylist;
		jsonobject = null;
		httpclient = NetUtil.getClient();
		httppost = new HttpPost("https://service.itouchchina.com/commentsvcs/registry");
		arraylist = new ArrayList();
		arraylist.add(new BasicNameValuePair("nickname", s));
		arraylist.add(new BasicNameValuePair("useremail", s1));
		arraylist.add(new BasicNameValuePair("userpwd", s2));
		arraylist.add(new BasicNameValuePair("appname", s3));
		CommunityUtil.addTimeAndMD5(arraylist);
		JSONObject jsonobject1;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(arraylist, "UTF-8"));
			HttpResponse httpresponse = httpclient.execute(httppost);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {
				String s4 = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s4);
				jsonobject1 = new JSONObject(s4);
				jsonobject = jsonobject1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		httppost.abort();
		return jsonobject;

	}

	public static JSONObject postResetPwd(String s) {
		JSONObject jsonobject;
		HttpClient httpclient;
		HttpPost httppost;
		ArrayList arraylist;
		jsonobject = null;
		httpclient = NetUtil.getClient();
		httppost = new HttpPost("https://service.itouchchina.com/commentsvcs/resetpwd");
		arraylist = new ArrayList();
		arraylist.add(new BasicNameValuePair("useremail", s));
		CommunityUtil.addTimeAndMD5(arraylist);
		JSONObject jsonobject1;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(arraylist, "UTF-8"));
			HttpResponse httpresponse = httpclient.execute(httppost);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {

				String s1 = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s1);
				jsonobject1 = new JSONObject(s1);
				jsonobject = jsonobject1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		httppost.abort();
		return jsonobject;

	}

	public static JSONObject postUnbind(String s, String s1, String s2) {
		JSONObject jsonobject;
		HttpClient httpclient;
		HttpPost httppost;
		ArrayList arraylist;
		jsonobject = null;
		httpclient = NetUtil.getClient();
		httppost = new HttpPost("https://service.itouchchina.com/commentsvcs/unbind");
		arraylist = new ArrayList();
		arraylist.add(new BasicNameValuePair("oauth_token", s));
		arraylist.add(new BasicNameValuePair("appname", s1));
		arraylist.add(new BasicNameValuePair("type", s2));
		CommunityUtil.addTimeAndMD5(arraylist);
		JSONObject jsonobject1;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(arraylist, "UTF-8"));
			HttpResponse httpresponse = httpclient.execute(httppost);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {

				String s3 = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s3);
				jsonobject1 = new JSONObject(s3);
				jsonobject = jsonobject1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		httppost.abort();
		return jsonobject;

	}

	public static JSONObject postUpdateUser(String s, String s1, String s2) {
		JSONObject jsonobject;
		HttpClient httpclient;
		HttpPost httppost;
		ArrayList arraylist;
		jsonobject = null;
		httpclient = NetUtil.getClient();
		httppost = new HttpPost("https://service.itouchchina.com/commentsvcs/updateuser");
		arraylist = new ArrayList();
		arraylist.add(new BasicNameValuePair("oauth_token", s));
		arraylist.add(new BasicNameValuePair("appname", s1));
		arraylist.add(new BasicNameValuePair("nickname", s2));
		CommunityUtil.addTimeAndMD5(arraylist);
		JSONObject jsonobject1;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(arraylist, "UTF-8"));
			HttpResponse httpresponse = httpclient.execute(httppost);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {

				String s3 = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s3);
				jsonobject1 = new JSONObject(s3);
				jsonobject = jsonobject1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		httppost.abort();
		return jsonobject;

	}

	public static JSONObject postUserLogin(String s, String s1, String s2) {
		JSONObject jsonobject;
		HttpClient httpclient;
		HttpPost httppost;
		ArrayList arraylist;
		jsonobject = null;
		httpclient = NetUtil.getClient();
		httppost = new HttpPost("https://service.itouchchina.com/commentsvcs/userlogin");
		arraylist = new ArrayList();
		arraylist.add(new BasicNameValuePair("useremail", s));
		arraylist.add(new BasicNameValuePair("userpwd", s1));
		arraylist.add(new BasicNameValuePair("appname", s2));
		CommunityUtil.addTimeAndMD5(arraylist);
		JSONObject jsonobject1;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(arraylist, "UTF-8"));
			HttpResponse httpresponse = httpclient.execute(httppost);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {
				String s3 = EntityUtils.toString(httpresponse.getEntity());
				Log.v("", s3);
				jsonobject1 = new JSONObject(s3);
				jsonobject = jsonobject1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		httppost.abort();
		return jsonobject;

	}

	public static final String COMMUNITY_SERVER_URL = "https://service.itouchchina.com/commentsvcs/";
	public static final String DOUBAN = "douban";
	public static final String HOT = "hot";
	public static final String NEW = "new";
	public static final String RENREN = "renren";
	public static final String SINA = "sina";
	public static final String TC = "touchchina";
	public static final String TENCENT = "tencent";
}
