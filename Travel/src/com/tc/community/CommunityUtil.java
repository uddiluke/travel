// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.community;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tc.TCData;
import com.tc.cg.CGService;
import com.tc.net.NetUtil;
import com.tc.weibo.WeiboSettings;

// Referenced classes of package com.tc.community:
//            CommunityJSON, LoginRegisterActivity, RegisterActivity

public class CommunityUtil {

	public CommunityUtil() {
	}

	public static void TClogout(Context context) {
		setTCToken(context, null);
		setTCNickName(context, null);
		setLoginType(context, null);
		setSinaBind(context, null, null);
		setTencentBind(context, false);
		context.sendBroadcast(new Intent("com.touchchina.action.TC_LOGOUT"));
	}

	public static void addTimeAndMD5(List list) {
		ArrayList arraylist = new ArrayList();
		NameValuePair namevaluepair;
		for (Iterator iterator = list.iterator(); iterator.hasNext(); arraylist.add((new StringBuilder()).append(namevaluepair.getName()).append("=")
				.append(namevaluepair.getValue()).toString()))
			namevaluepair = (NameValuePair) iterator.next();

		Collections.sort(arraylist);
		StringBuffer stringbuffer = new StringBuffer();
		int i = arraylist.size();
		for (int j = 0; j < i; j++) {
			stringbuffer.append((String) arraylist.get(j));
			if (j != i - 1)
				stringbuffer.append("&");
		}

		String s = getServerTime();
		list.add(new BasicNameValuePair("t", s));
		list.add(new BasicNameValuePair("m", NetUtil.toMd5((new StringBuilder()).append(s).append("itouch").append(stringbuffer.toString())
				.append("china").toString())));
	}

	public static String communityPointType2SgType(String s) {
		String s1 = null;
		if ("1".equalsIgnoreCase(s)) { // goto _L2; else goto _L1
			s1 = "Site";
		} else if ("2".equalsIgnoreCase(s))
			s1 = "Restaurant";
		else if ("3".equalsIgnoreCase(s))
			s1 = "Shopping";
		else if ("4".equalsIgnoreCase(s))
			s1 = "Hotel";
		else if ("5".equalsIgnoreCase(s))
			s1 = "Fun";
		else if ("6".equalsIgnoreCase(s))
			s1 = "Mallshop";

		return s1;

	}

	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		int i = bitmap.getWidth();
		int j = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(1.0F, -1F);
		Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, j / 2, i, j / 2, matrix, false);
		Bitmap bitmap2 = Bitmap.createBitmap(i, j + j / 2, android.graphics.Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap2);
		canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
		Paint paint = new Paint();
		canvas.drawRect(0.0F, j, i, j + 4, paint);
		canvas.drawBitmap(bitmap1, 0.0F, j + 4, null);
		Paint paint1 = new Paint();
		paint1.setShader(new LinearGradient(0.0F, bitmap.getHeight(), 0.0F, 4 + bitmap2.getHeight(), 0x70ffffff, 0xffffff,
				android.graphics.Shader.TileMode.CLAMP));
		paint1.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
		canvas.drawRect(0.0F, j, i, 4 + bitmap2.getHeight(), paint1);
		return bitmap2;
	}

	public static Bitmap getBigBitmap(Context context, String s) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap bitmap = BitmapFactory.decodeStream(getCommunityPicInputStream((new StringBuilder()).append(s).append(".big").toString()));
		Bitmap bitmap1;
		if (bitmap != null) {
			if (bitmap.getWidth() > TCData.SCREEN_WIDTH || bitmap.getHeight() > TCData.SCREEN_HEIGHT) {
				float f = (1.0F * (float) bitmap.getWidth()) / (float) TCData.SCREEN_WIDTH;
				float f1 = (1.0F * (float) bitmap.getHeight()) / (float) TCData.SCREEN_HEIGHT;
				int i;
				int j;

				if (f > f1) {
					i = (int) ((float) bitmap.getWidth() / f);
					j = (int) ((float) bitmap.getHeight() / f);
				} else {
					i = (int) ((float) bitmap.getWidth() / f1);
					j = (int) ((float) bitmap.getHeight() / f1);
				}
				bitmap = Bitmap.createScaledBitmap(bitmap, i, j, true);
			}
			Bitmap bitmap2;
			Canvas canvas;
			bitmap2 = BitmapFactory.decodeResource(context.getResources(), 0x7f02003e);
			bitmap1 = Bitmap.createBitmap(bitmap.getWidth() + bitmap2.getWidth(), bitmap.getHeight() + bitmap2.getHeight(),
					android.graphics.Bitmap.Config.ARGB_8888);
			canvas = new Canvas(bitmap1);
			canvas.drawARGB(0, 0, 0, 0);
			canvas.drawBitmap(bitmap, bitmap2.getWidth() / 2, bitmap2.getHeight() / 2, paint);
			bitmap.recycle();
			canvas.drawBitmap(bitmap2, bitmap.getWidth(), bitmap.getHeight(), paint);
			bitmap2.recycle();
		} else {
			bitmap1 = null;
		}
		return bitmap1;
	}

	public static InputStream getCommunityPicInputStream(String s) {
		InputStream inputstream = null;
		try {
			inputstream = (new URL((new StringBuilder()).append("http://service.itouchchina.com/").append(s).toString())).openConnection()
					.getInputStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return inputstream;

	}

	public static String getExtra(Context context, String s, int i) {
		return (new StringBuilder()).append(context.getString(0x7f08015a)).append(s).append(context.getString(0x7f08015b)).append(i * 2)
				.append(context.getString(0x7f08015c)).toString();
	}

	public static String getLoginType(Context context) {
		return context.getSharedPreferences("community_authorise_info", 0).getString("login_type", null);
	}

	private static String getPicOrientation(String s) {
		try {
			return (new ExifInterface(s)).getAttribute("Orientation");
		} catch (IOException e) {
			return "1";
		}

	}

	public static Bitmap getPlusBitmap(Context context, String s) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap bitmap = BitmapFactory.decodeStream(getCommunityPicInputStream(s));
		Bitmap bitmap1;
		if (bitmap != null) {
			Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), 0x7f02003f);
			bitmap1 = Bitmap.createBitmap(bitmap.getWidth() + bitmap2.getWidth() / 2, bitmap.getHeight() + bitmap2.getHeight() / 2,
					android.graphics.Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap1);
			canvas.drawARGB(0, 0, 0, 0);
			canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
			bitmap.recycle();
			canvas.drawBitmap(bitmap2, bitmap.getWidth() - bitmap2.getWidth() / 2, bitmap.getHeight() - bitmap2.getHeight() / 2, paint);
			bitmap2.recycle();
		} else {
			bitmap1 = null;
		}
		return bitmap1;
	}

	public static Matrix getRotateMatrix(String s) {
		Matrix matrix = new Matrix();
		String s1 = getPicOrientation(s);
		if (s1.equals("3"))
			matrix.setRotate(180F);
		else if (s1.equals("6"))
			matrix.setRotate(90F);
		else if (s1.equals("8"))
			matrix.setRotate(270F);
		else
			matrix.setRotate(0.0F);
		return matrix;
	}

	public static String getServerTime() {
		return (new StringBuilder()).append("").append((long) CGService.TD + System.currentTimeMillis() / 1000L).toString();
	}

	public static int getTCAnonymousOptionCount(Context context) {
		return context.getSharedPreferences("community_authorise_info", 0).getInt("tc_anonymous_option_count", 0);
	}

	public static String getTCAnonymousToken(Context context) {
		return context.getSharedPreferences("community_authorise_info", 0).getString("tc_anonymous_token", null);
	}

	public static String getTCNickName(Context context) {
		return context.getSharedPreferences("community_authorise_info", 0).getString("tc_nick_name", null);
	}

	public static String getTCToken(Context context) {
		return context.getSharedPreferences("community_authorise_info", 0).getString("tc_oauth_token", null);
	}

	public static String getTimeAndMD5(String s) {
		String as[] = s.substring("https://service.itouchchina.com/commentsvcs/".length()).split("/");
		ArrayList arraylist = new ArrayList();
		int l;
		for (int i = 1; i < as.length; i = l + 1) {
			StringBuilder stringbuilder = (new StringBuilder()).append(as[i]).append("=");
			l = i + 1;
			arraylist.add(stringbuilder.append(as[l]).toString());
		}

		Collections.sort(arraylist);
		StringBuffer stringbuffer = new StringBuffer();
		int j = arraylist.size();
		for (int k = 0; k < j; k++) {
			stringbuffer.append((String) arraylist.get(k));
			if (k != j - 1)
				stringbuffer.append("&");
		}

		String s1 = getServerTime();
		return (new StringBuilder()).append(s).append("/t/").append(s1).append("/m/")
				.append(NetUtil.toMd5((new StringBuilder()).append(s1).append("itouch").append(stringbuffer.toString()).append("china").toString()))
				.toString();
	}

	public static Bitmap int2Icon(Context context, int i) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), 0x7f020047);
		Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap1);
		Paint paint = new Paint();
		paint.setDither(true);
		paint.setFilterBitmap(true);
		canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
		bitmap.recycle();
		Paint paint1 = new Paint(257);
		paint1.setColor(-1);
		paint1.setTypeface(Typeface.DEFAULT_BOLD);
		paint1.setTextAlign(android.graphics.Paint.Align.CENTER);
		canvas.drawText(String.valueOf(i), bitmap.getWidth() / 2, 3 + bitmap.getHeight() / 2, paint1);
		return bitmap1;
	}

	public static boolean isSinaBind(Context context) {
		boolean flag;
		if (WeiboSettings.getToken(context) != null)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public static boolean isTencentBind(Context context) {
		return context.getSharedPreferences("community_authorise_info", 0).getBoolean("is_tencent_binded", false);
	}

	public static void setLoginType(Context context, String s) {
		android.content.SharedPreferences.Editor editor = context.getSharedPreferences("community_authorise_info", 0).edit();
		editor.putString("login_type", s);
		editor.commit();
	}

	public static void setSinaBind(Context context, String s, String s1) {
		WeiboSettings.putToken(context, s);
		WeiboSettings.putTokenSecret(context, s1);
		WeiboSettings.putUserName(context, null);
		WeiboSettings.putPassword(context, null);
	}

	public static void setTCAnonymousOptionCount(Context context, int i) {
		android.content.SharedPreferences.Editor editor = context.getSharedPreferences("community_authorise_info", 0).edit();
		editor.putInt("tc_anonymous_option_count", i);
		editor.commit();
	}

	public static void setTCAnonymousToken(Context context, String s) {
		android.content.SharedPreferences.Editor editor = context.getSharedPreferences("community_authorise_info", 0).edit();
		editor.putString("tc_anonymous_token", s);
		editor.commit();
	}

	public static void setTCNickName(Context context, String s) {
		android.content.SharedPreferences.Editor editor = context.getSharedPreferences("community_authorise_info", 0).edit();
		editor.putString("tc_nick_name", s);
		editor.commit();
	}

	public static void setTCToken(Context context, String s) {
		android.content.SharedPreferences.Editor editor = context.getSharedPreferences("community_authorise_info", 0).edit();
		editor.putString("tc_oauth_token", s);
		editor.commit();
	}

	public static void setTD(Context context) {
		String s = CommunityJSON.getCurTime(context);
		if (s != null)
			CGService.TD = (int) ((long) Integer.parseInt(s) - System.currentTimeMillis() / 1000L);
		else
			CGService.TD = 0;
	}

	public static void setTencentBind(Context context, boolean flag) {
		android.content.SharedPreferences.Editor editor = context.getSharedPreferences("community_authorise_info", 0).edit();
		editor.putBoolean("is_tencent_binded", flag);
		editor.commit();
	}

	public static String sgType2CommunityPointType(String s) {
		String s1 = null;
		if ("site".equalsIgnoreCase(s)) {
			s1 = "1";
		} else if ("restaurant".equalsIgnoreCase(s))
			s1 = "2";
		else if ("shopping".equalsIgnoreCase(s))
			s1 = "3";
		else if ("hotel".equalsIgnoreCase(s))
			s1 = "4";
		else if ("fun".equalsIgnoreCase(s))
			s1 = "5";
		else if ("mallshop".equalsIgnoreCase(s))
			s1 = "6";

		return s1;

	}

	public static void showAnonymous2TimesDialog(final Context context, final int cg_id, final Handler handler) {
		android.app.AlertDialog.Builder builder = (new android.app.AlertDialog.Builder(context)).setTitle(0x7f08013e);
		String as[] = new String[3];
		as[0] = context.getString(0x7f08013f);
		as[1] = context.getString(0x7f080140);
		as[2] = context.getString(0x7f080142);
		builder.setItems(as, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialoginterface, int j) {
				switch (j) {
				case 0:
					Intent intent1 = new Intent(context, LoginRegisterActivity.class);
					intent1.putExtra("cg_id", cg_id);
					context.startActivity(intent1);
					break;
				case 1:
					Intent intent = new Intent(context, RegisterActivity.class);
					intent.putExtra("cg_id", cg_id);
					context.startActivity(intent);
					break;
				case 2:
					handler.sendEmptyMessage(0);
					break;
				}

			}

		}).show();
	}

	public static void showAnonymous5TimesDialog(final Context context, final int cg_id) {
		android.app.AlertDialog.Builder builder = (new android.app.AlertDialog.Builder(context)).setTitle(0x7f08013d);
		String as[] = new String[3];
		as[0] = context.getString(0x7f08013f);
		as[1] = context.getString(0x7f080140);
		as[2] = context.getString(0x7f08001d);
		builder.setItems(as, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialoginterface, int j) {
				switch (j) {
				case 0:
					Intent intent1 = new Intent(context, LoginRegisterActivity.class);
					intent1.putExtra("cg_id", cg_id);
					context.startActivity(intent1);
					break;
				case 1:
					Intent intent = new Intent(context, RegisterActivity.class);
					intent.putExtra("cg_id", cg_id);
					context.startActivity(intent);
					break;
				}

			}

		}).show();
	}

	public static void showAnonymousFirstTimesDialog(final Context context, final int cg_id, final Handler handler) {
		android.app.AlertDialog.Builder builder = (new android.app.AlertDialog.Builder(context)).setTitle(0x7f08013d);
		String as[] = new String[4];
		as[0] = context.getString(0x7f08013f);
		as[1] = context.getString(0x7f080140);
		as[2] = context.getString(0x7f080141);
		as[3] = context.getString(0x7f08001d);
		builder.setItems(as, new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialoginterface, int j) {
				switch (j) {
				case 0:
					Intent intent1 = new Intent(context, LoginRegisterActivity.class);
					intent1.putExtra("cg_id", cg_id);
					context.startActivity(intent1);
					break;
				case 1:
					Intent intent = new Intent(context, RegisterActivity.class);
					intent.putExtra("cg_id", cg_id);
					context.startActivity(intent);
					break;
				case 2:
					handler.sendEmptyMessage(0);
					break;
				}

			}

		}).show();
	}

	public static void showCommunityToast(Context context, String s) {
		Toast toast;
		LinearLayout linearlayout;
		ImageView imageview;
		TextView textview;
		toast = Toast.makeText(context, null, 0);
		linearlayout = (LinearLayout) toast.getView();
		linearlayout.setGravity(17);
		imageview = new ImageView(context);
		imageview.setLayoutParams(new android.view.ViewGroup.LayoutParams(-2, -2));
		imageview.setPadding(50, 40, 50, 30);
		textview = new TextView(context);
		textview.setGravity(17);
		textview.setLayoutParams(new android.view.ViewGroup.LayoutParams(-2, -2));
		textview.setTextSize(17F);
		textview.setTextColor(-1);
		if (s == null) { // goto _L2; else goto _L1

			textview.setText(0x7f08017b);
			imageview.setImageResource(0x7f020036);
		} else {
			textview.setText(context.getResources().getIdentifier((new StringBuilder()).append("error").append(s).append("Content").toString(),
					"string", context.getPackageName()));
			if (s.equals("7") || s.equals("23") || s.equals("8") || s.equals("9") || s.equals("10") || s.equals("13") || s.equals("14")
					|| s.equals("15") || s.equals("16") || s.equals("17") || s.equals("24")) {
				imageview.setImageResource(0x7f020042);
			} else {
				imageview.setImageResource(0x7f020036);
				if (s.equals("099"))
					setTD(context);
			}
		}
		linearlayout.addView(imageview, 0);
		linearlayout.addView(textview, 1);
		toast.setGravity(17, 0, 0);
		toast.show();

	}

	public static final String ACCOUNT_EMAIL_FORMAT_WRONG = "1";
	public static final String ACCOUNT_NICK_OR_PSW_EMPTY = "6";
	public static final String ACCOUNT_UNCOMPLATE_E = "5";
	public static final String ACCOUNT_UNCOMPLATE_E_P = "4";
	public static final String ACCOUNT_UNCOMPLATE_N_E_P = "0";
	public static final String BIND_SUCCESS = "16";
	public static final String CANNOT_BIND = "3";
	public static final String CANNOT_REPLY_EMPTY = "22";
	public static final String CHANGE_PASSWORD_SUCCESS = "10";
	public static final String CHANGE_USERNAME_SUCCESS = "9";
	public static final String COMMUNITE_SUCCESS = "13";
	private static final String COMMUNITY_AUTHORISE_INFO = "community_authorise_info";
	public static final String GET_NEXT_COMMUNITY_FAILED = "11";
	private static final String IS_TENCENT_BINDED = "is_tencent_binded";
	private static final String LOGIN_TYPE = "login_type";
	public static final String NICK_NAME_FORMAT_WRONG = "26";
	public static final String NICK_NAME_TOO_LONG = "19";
	public static final String NICK_NAME_TOO_SHORT = "18";
	public static final String PSW_DIFFERENT = "21";
	public static final String PSW_LENGTH_WRONG = "20";
	public static final String REGIST_SUCCESS = "23";
	public static final String REPLY_SUCCESS = "14";
	public static final String RESET_SUCCESS = "8";
	public static final String SERVER_NO_RESPONSE = "2";
	private static final String TC_ANONYMOUS_OPTION_COUNT = "tc_anonymous_option_count";
	private static final String TC_ANONYMOUS_TOKEN = "tc_anonymous_token";
	public static final String TC_LOGOUT_BROADCAST = "com.touchchina.action.TC_LOGOUT";
	private static final String TC_NICK_NAME = "tc_nick_name";
	private static final String TC_OAUTH_TOKEN = "tc_oauth_token";
	public static final String TEXT_TOO_LONG = "12";
	public static final String UNBIND_SUCCESS = "17";
	public static final String ZANED = "7";
	public static final String ZAN_SUCCESS = "15";
}
