//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.community;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.tc.TCUtil;

// Referenced classes of package com.tc.community:
//            CommunityUtil, CommunityMethod, CommunityData

public class CommunityJSON {

	public CommunityJSON() {
	}

	public static String bind(Context context, String s, String s1, String s2, String s3, String s4, boolean flag) {
		String s5 = null;
		if (TCUtil.isNetAvailable(context)) {
			JSONObject jsonobject = CommunityMethod.postBind(CommunityUtil.getTCToken(context), s, s1, s2, s3, s4, flag);
			if (jsonobject != null)
				try {
					s5 = jsonobject.getJSONObject("bind").getString("status");
					if ("OK".equals(s5))
						if (s1.equals("sina"))
							CommunityUtil.setSinaBind(context, s3, s4);
						else if (s1.equals("tencent"))
							CommunityUtil.setTencentBind(context, true);
				} catch (JSONException jsonexception) {
					jsonexception.printStackTrace();
				}
		}
		return s5;
	}

	public static String changeNickName(Context context, String s, String s1) {
		String s2 = null;
		if (TCUtil.isNetAvailable(context)) {
			JSONObject jsonobject = CommunityMethod.postUpdateUser(CommunityUtil.getTCToken(context), s, s1);
			if (jsonobject != null)
				try {
					s2 = jsonobject.getJSONObject("updateuser").getString("status");
					if ("OK".equals(s2))
						CommunityUtil.setTCNickName(context, s1);
				} catch (JSONException jsonexception) {
					jsonexception.printStackTrace();
				}
		}
		return s2;
	}

	public static String changePwd(Context context, String s, String s1) {
		String s2 = null;
		if (TCUtil.isNetAvailable(context)) {
			JSONObject jsonobject = CommunityMethod.postChangePwd(CommunityUtil.getTCToken(context), s, s1);
			if (jsonobject != null)
				try {
					s2 = jsonobject.getJSONObject("changepwd").getString("status");
					if ("OK".equals(s2))
						CommunityUtil.setTCToken(context, jsonobject.getJSONObject("changepwd").getString("newtoken"));
				} catch (JSONException jsonexception) {
					jsonexception.printStackTrace();
				}
		}
		return s2;
	}

	public static String comment(Context context, String s, String s1, int i) {
		String s2 = null;
		if (TCUtil.isNetAvailable(context)) {

			JSONObject jsonobject;

			if (CommunityUtil.getTCToken(context) == null)
				s2 = CommunityUtil.getTCAnonymousToken(context);
			else
				s2 = CommunityUtil.getTCToken(context);
			jsonobject = CommunityMethod.postComment(s2, s, s1, (new StringBuilder()).append("").append(i).toString());
			if (jsonobject != null)
				try {
					s2 = jsonobject.getJSONObject("comment").getString("status");
				} catch (JSONException e) {
					Log.e(CommunityItemActivity.class.getSimpleName(), "", e);
				}

		}
		return s2;

	}

	public static String comment(Context context, String s, String s1, int i, String s2, int j, String s3, String s4, String s5, String s6, String s7) {
		String s8 = null;
		if (TCUtil.isNetAvailable(context)) {

			JSONObject jsonobject;

			if (CommunityUtil.getTCToken(context) == null)
				s8 = CommunityUtil.getTCAnonymousToken(context);
			else
				s8 = CommunityUtil.getTCToken(context);
			jsonobject = CommunityMethod.postComment(s8, s, CommunityUtil.sgType2CommunityPointType(s1), (new StringBuilder()).append("").append(i)
					.toString(), (new StringBuilder()).append("").append(j * 2).toString(), s3, s4, s5, s6, s7, s2);
			if (jsonobject != null)
				try {
					s8 = jsonobject.getJSONObject("comment").getString("status");
				} catch (JSONException e) {
					Log.e(CommunityItemActivity.class.getSimpleName(), "", e);
				}
		}

		return s8;

	}

	public static String getAnonymousToken(Context context) {
		String s = null;
		if (TCUtil.isNetAvailable(context)) {
			JSONObject jsonobject = CommunityMethod.getAnonymousToken();
			if (jsonobject != null)
				try {
					s = jsonobject.getJSONObject("anonymoustoken").getString("status");
					if ("OK".equals(s)) {
						CommunityUtil.setTCAnonymousToken(context, jsonobject.getJSONObject("anonymoustoken").getString("anonymoustoken"));
						CommunityUtil.setTCAnonymousOptionCount(context, 0);
					}
				} catch (JSONException jsonexception) {
					jsonexception.printStackTrace();
				}
		}
		return s;
	}

	public static HashMap getCommentList(Context context, String s, int i, int j, int k, String s1, String s2, int l) {
		HashMap hashmap;
		JSONObject jsonobject;
		hashmap = null;
		if (TCUtil.isNetAvailable(context)) {

			jsonobject = CommunityMethod.getCommentList(s, (new StringBuilder()).append("").append(i).toString(), (new StringBuilder()).append("")
					.append(j).toString(), (new StringBuilder()).append("").append(k).toString(), s1, CommunityUtil.sgType2CommunityPointType(s2),
					(new StringBuilder()).append("").append(l).toString());
			if (jsonobject != null) {
				try {
					String s3 = jsonobject.getJSONObject("commentlist").getString("status");
					if ("OK".equals(s3)) {

						if ("OK".equals(s3)) {

							JSONArray jsonarray = jsonobject.getJSONObject("commentlist").getJSONArray("comments");
							ArrayList arraylist = new ArrayList();
							for (int ii = 0; ii < jsonarray.length(); ii++) {
								JSONObject jsonobject1 = jsonarray.getJSONObject(ii);
								arraylist.add(new CommunityData(jsonobject1.getInt("id"), jsonobject1.getString("pointtype"), jsonobject1
										.getInt("pointid"), jsonobject1.getString("content"), jsonobject1.getString("username"), jsonobject1
										.getInt("like"), jsonobject1.getInt("reply"), jsonobject1.getInt("star"), jsonobject1.getString("img"),
										jsonobject1.getString("createtime"), jsonobject1.getString("source")));

							}
							hashmap = new HashMap();
							hashmap.put("TOTAL_COUNT", Integer.valueOf(jsonobject.getJSONObject("commentlist").getInt("count")));
							hashmap.put("TIME_LINE", Integer.valueOf(jsonobject.getJSONObject("commentlist").getInt("timeline")));
							hashmap.put("COMMUNITY_LIST", arraylist);
							hashmap.put("LIST_STATUS", s3);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return hashmap;

	}

	public static String getCurTime(Context context) {
		String s = null;
		JSONObject jsonobject;

		if (TCUtil.isNetAvailable(context)) {

			jsonobject = CommunityMethod.getCurTime();
			if (jsonobject != null)
				try {
					s = jsonobject.getJSONObject("curtime").getString("time");
				} catch (JSONException e) {

					e.printStackTrace();
				}

		}
		return s;

	}

	public static HashMap getMyComments(Context context, String s, int i, int j, int k, String s1, int l) {
		HashMap hashmap = null;
		if (TCUtil.isNetAvailable(context)) {// goto _L2; else goto _L1
			JSONObject jsonobject = CommunityMethod.getMyComments(CommunityUtil.getTCToken(context), s, (new StringBuilder()).append("").append(i)
					.toString(), (new StringBuilder()).append("").append(j).toString(), (new StringBuilder()).append("").append(k).toString(),
					CommunityUtil.sgType2CommunityPointType(s1), (new StringBuilder()).append("").append(l).toString());
			if (jsonobject != null) {
				try {
					String s2;

					s2 = jsonobject.getJSONObject("mycomments").getString("status");
					hashmap = new HashMap();
					if ("OK".equals(s2)) {
						JSONArray jsonarray = jsonobject.getJSONObject("mycomments").getJSONArray("comments");

						ArrayList arraylist = new ArrayList();
						for (int ii = 0; ii < jsonarray.length(); ii++) {
							JSONObject jsonobject1 = jsonarray.getJSONObject(ii);
							arraylist.add(new CommunityData(jsonobject1.getInt("id"), jsonobject1.getString("pointtype"), jsonobject1
									.getInt("pointid"), jsonobject1.getString("content"), jsonobject1.getString("username"), jsonobject1
									.getInt("like"), jsonobject1.getInt("reply"), jsonobject1.getInt("star"), jsonobject1.getString("img"),
									jsonobject1.getString("createtime"), jsonobject1.getInt("fresh")));

						}

						Integer integer = Integer.valueOf(jsonobject.getJSONObject("mycomments").getInt("count"));
						hashmap.put("TOTAL_COUNT", integer);
						Integer integer1 = Integer.valueOf(jsonobject.getJSONObject("mycomments").getInt("timeline"));
						hashmap.put("TIME_LINE", integer1);
						hashmap.put("COMMUNITY_LIST", arraylist);

					}
					hashmap.put("LIST_STATUS", s2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return hashmap;

	}

	public static int getMyNoReads(Context context, String s, String s1, int i) {
		int j;
		JSONObject jsonobject;
		j = 0;
		if (TCUtil.isNetAvailable(context)) {

			jsonobject = CommunityMethod.getMyNoReads(CommunityUtil.getTCToken(context), s, CommunityUtil.sgType2CommunityPointType(s1),
					(new StringBuilder()).append("").append(i).toString());
			if (jsonobject != null)
				try {
					j = jsonobject.getJSONObject("mynoreads").getInt("count");
				} catch (JSONException e) {
					e.printStackTrace();
				}
		}
		return j;

	}

	public static HashMap getReplies(Context context, String s, int i, int j, int k, int l) {
		HashMap hashmap;
		JSONObject jsonobject;
		hashmap = null;
		if (TCUtil.isNetAvailable(context)) {

			jsonobject = CommunityMethod.getReplies((new StringBuilder()).append("").append(j).toString(), (new StringBuilder()).append("").append(i)
					.toString(), (new StringBuilder()).append("").append(k).toString(), (new StringBuilder()).append("").append(l).toString(),
					CommunityUtil.getTCToken(context), s);
			if (jsonobject != null) {
				try {
					String s1 = jsonobject.getJSONObject("replies").getString("status");
					hashmap = new HashMap();
					JSONArray jsonarray;
					ArrayList arraylist;
					if ("OK".equals(s1)) {

						jsonarray = jsonobject.getJSONObject("replies").getJSONArray("comments");
						arraylist = new ArrayList();
						for (int ii = 0; ii < jsonarray.length(); ii++) {
							JSONObject jsonobject1 = jsonarray.getJSONObject(ii);
							arraylist.add(new CommunityData(jsonobject1.getInt("id"), null, -1, jsonobject1.getString("content"), jsonobject1
									.getString("username"), -1, -1, -1, null, jsonobject1.getString("createtime"), null));

						}
						hashmap.put("TOTAL_COUNT", Integer.valueOf(jsonobject.getJSONObject("replies").getInt("count")));
						hashmap.put("TIME_LINE", Integer.valueOf(jsonobject.getJSONObject("replies").getInt("timeline")));
						hashmap.put("COMMUNITY_LIST", arraylist);
						hashmap.put("LIST_STATUS", s1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return hashmap;

	}

	public static String like(Context context, String s, int i) {
		String s1 = null;
		if (TCUtil.isNetAvailable(context)) {

			JSONObject jsonobject;
			if (CommunityUtil.getTCToken(context) == null)
				s1 = CommunityUtil.getTCAnonymousToken(context);
			else
				s1 = CommunityUtil.getTCToken(context);
			jsonobject = CommunityMethod.postLikeComment(s1, s, (new StringBuilder()).append("").append(i).toString());
			if (jsonobject != null) {
				try {
					s1 = jsonobject.getJSONObject("likecomment").getString("status");
					if ("OK".equals(s1))
						s1 = (new StringBuilder()).append(jsonobject.getJSONObject("likecomment").getInt("likeCount")).append("_").toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return s1;

	}

	public static String login(Context context, String s, String s1, String s2) {
		String s3 = null;
		if (TCUtil.isNetAvailable(context)) {
			JSONObject jsonobject = CommunityMethod.postUserLogin(s, s1, s2);
			if (jsonobject != null)
				try {
					s3 = jsonobject.getJSONObject("userlogin").getString("status");
					if ("OK".equals(s3)) {
						CommunityUtil.setTCToken(context, jsonobject.getJSONObject("userlogin").getJSONObject("token").getString("access_token"));
						CommunityUtil.setLoginType(context, "touchchina");
						CommunityUtil.setTCNickName(context, jsonobject.getJSONObject("userlogin").getString("nickname"));
						if (jsonobject.getJSONObject("userlogin").has("sina"))
							CommunityUtil.setSinaBind(context, jsonobject.getJSONObject("userlogin").getString("sina_token"), jsonobject
									.getJSONObject("userlogin").getString("sina_token_secret"));
						else
							CommunityUtil.setSinaBind(context, null, null);
						CommunityUtil.setTencentBind(context, jsonobject.getJSONObject("userlogin").has("tencent"));
					}
				} catch (JSONException jsonexception) {
					jsonexception.printStackTrace();
				}
		}
		return s3;
	}

	public static String otherUserLogin(Context context, String s, String s1, String s2, String s3, String s4) {
		String s5 = null;
		if (TCUtil.isNetAvailable(context)) {
			JSONObject jsonobject = CommunityMethod.postOtherUserLogin(s, s1, s2, s3, s4);
			if (jsonobject != null)
				try {
					s5 = jsonobject.getJSONObject("otherlogin").getString("status");
					if ("OK".equals(s5)) {
						CommunityUtil.setTCToken(context, jsonobject.getJSONObject("otherlogin").getJSONObject("token").getString("access_token"));
						CommunityUtil.setLoginType(context, s);
						CommunityUtil.setTCNickName(context, jsonobject.getJSONObject("otherlogin").getString("nickname"));
						if (jsonobject.getJSONObject("otherlogin").has("sina"))
							CommunityUtil.setSinaBind(context, jsonobject.getJSONObject("otherlogin").getString("sina_token"), jsonobject
									.getJSONObject("otherlogin").getString("sina_token_secret"));
						else
							CommunityUtil.setSinaBind(context, null, null);
						CommunityUtil.setTencentBind(context, jsonobject.getJSONObject("otherlogin").has("tencent"));
					}
				} catch (JSONException jsonexception) {
					jsonexception.printStackTrace();
				}
		}
		return s5;
	}

	public static String register(Context context, String s, String s1, String s2, String s3) {
		String s4 = null;
		if (TCUtil.isNetAvailable(context)) {
			JSONObject jsonobject = CommunityMethod.postRegistry(s, s1, s2, s3);
			if (jsonobject != null)
				try {
					s4 = jsonobject.getJSONObject("registry").getString("status");
					if ("OK".equals(s4)) {
						CommunityUtil.setTCToken(context, jsonobject.getJSONObject("registry").getJSONObject("token").getString("access_token"));
						CommunityUtil.setTCNickName(context, s);
					}
				} catch (JSONException jsonexception) {
					jsonexception.printStackTrace();
				}
		}
		return s4;
	}

	public static String resetPSW(Context context, String s) {
		String s1;
		JSONObject jsonobject;
		s1 = null;
		if (TCUtil.isNetAvailable(context)) {

			jsonobject = CommunityMethod.postResetPwd(s);
			if (jsonobject != null)
				try {
					s1 = jsonobject.getJSONObject("resetpwd").getString("status");
				} catch (JSONException e) {
					e.printStackTrace();
				}

		}
		return s1;

	}

	public static String unBind(Context context, String s, String s1) {
		String s2 = null;
		if (TCUtil.isNetAvailable(context)) {
			JSONObject jsonobject = CommunityMethod.postUnbind(CommunityUtil.getTCToken(context), s, s1);
			if (jsonobject != null)
				try {
					s2 = jsonobject.getJSONObject("unbind").getString("status");
					if ("OK".equals(s2))
						if (s1.equals("sina"))
							CommunityUtil.setSinaBind(context, null, null);
						else if (s1.equals("tencent"))
							CommunityUtil.setTencentBind(context, false);
				} catch (JSONException jsonexception) {
					jsonexception.printStackTrace();
				}
		}
		return s2;
	}

	public static final String COMMUNITY_LIST = "COMMUNITY_LIST";
	public static final int ITEM_NUM = 20;
	public static final String LIST_STATUS = "LIST_STATUS";
	public static final String TIME_LINE = "TIME_LINE";
	public static final String TOTAL_COUNT = "TOTAL_COUNT";
}
