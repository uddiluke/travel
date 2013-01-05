//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.tc.weibo.WeiboActivity;
import com.touchchina.cityguide.sz.R;

// Referenced classes of package com.tc:
//            TCData
// from 20121218

public class TCUtil {

	public TCUtil() {
	}

	public static Drawable boundCenter(Drawable drawable, int i, int j) {
		int k = drawable.getIntrinsicWidth();
		int l = drawable.getIntrinsicHeight();
		drawable.setBounds(i + -k / 2, j + -l / 2, i + k / 2, j + l / 2);
		return drawable;
	}

	public static Drawable boundCenterBottom(Drawable drawable, int i, int j) {
		int k = drawable.getIntrinsicWidth();
		int l = drawable.getIntrinsicHeight();
		drawable.setBounds(i + -k / 2, j + -l, i + k / 2, j);
		return drawable;
	}

	public static void call(Context context, String s) {
		try {
			context.startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + s)));
		} catch (Exception e) {
			context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("tel:" + s)));
		}
	}

	public static void createNewFile(File file) {
		try {
			if (file.exists())
				file.delete();
			else
				file.mkdirs();
			file.createNewFile();
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
	}

	public static Dialog createPositiveDialog(Context context, String s, android.content.DialogInterface.OnClickListener onclicklistener) {
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.dialog_title));
		builder.setMessage(s);
		builder.setPositiveButton(context.getString(R.string.dialog_comfirm), onclicklistener);
		builder.setNegativeButton(context.getString(R.string.dialog_cancel), new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialoginterface, int i) {
				dialoginterface.dismiss();
			}

		});
		return builder.create();
	}

	public static void deleteEveryFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File afile[] = file.listFiles();
				if (afile != null) {
					for (int i = 0; i < afile.length; i++)
						deleteEveryFile(afile[i]);

					file.delete();
				} else {
					file.delete();
				}
			}
		}

	}

	public static void deleteEveryFile(String s) {
		deleteEveryFile(new File(s));
	}

	public static void deleteFile(String s) {
		(new File(s)).delete();
	}

	public static boolean doAssertUnzip(Context context, String s, String s1) {

		boolean flag = false;
		try {
			ZipInputStream zipinputstream = new ZipInputStream(new BufferedInputStream(context.getAssets().open(s)));
			BufferedOutputStream bufferedoutputstream = null;
			while (true) {
				ZipEntry zipentry = zipinputstream.getNextEntry();
				if (zipentry == null)
					break;
				byte abyte0[] = new byte[4096];
				File file = new File((new StringBuilder()).append(s1).append(zipentry.getName()).toString());
				File file1 = new File(file.getParent());
				if (!file1.exists())
					file1.mkdirs();
				bufferedoutputstream = new BufferedOutputStream(new FileOutputStream(file), 4096);
				do {
					int i = zipinputstream.read(abyte0, 0, 4096);
					if (i == -1)
						break;
					bufferedoutputstream.write(abyte0, 0, i);
				} while (true);
				bufferedoutputstream.close();

			}
			zipinputstream.close();
			flag = true;
		} catch (IOException e) {
			Log.e(TAG, "public static void doUnzip(String zipFile, String targetDir)", e);
		}
		return flag;

	}

	public static boolean doUnzip(String s, String s1) {
		ZipInputStream zipinputstream = null;

		try {
			zipinputstream = new ZipInputStream(new FileInputStream(s));
			while (true) {
				ZipEntry zipentry = zipinputstream.getNextEntry();
				if (zipentry == null)
					break;

				if (zipentry.isDirectory()) {
					String fileName = zipentry.getName().substring(0, -1 + zipentry.getName().length());
					(new File((new StringBuilder()).append(s1).append(fileName).toString())).mkdirs();
				} else {
					File file = new File((new StringBuilder()).append(s1).append(zipentry.getName()).toString());
					File file1 = file.getParentFile();
					if (!file1.exists())
						file1.mkdirs();
					file.createNewFile();
					FileOutputStream fileoutputstream = new FileOutputStream(file);
					byte abyte0[] = new byte[512];
					do {
						int i = zipinputstream.read(abyte0);
						if (i == -1)
							break;
						fileoutputstream.write(abyte0, 0, i);
						fileoutputstream.flush();
					} while (true);
					fileoutputstream.close();
				}
			}
			zipinputstream.close();
			return true;
		} catch (Exception e) {
			Log.e("CGUpdateActivity", "unzipForlder", e);
		}
		return false;
	}

	public static boolean fileExists(String s) {
		File file = new File(s);
		boolean flag;
		if (file.isFile() && file.exists())
			flag = true;
		else
			flag = false;
		return flag;
	}

	public static void finishActivity(Activity activity) {
		if (!activity.isFinishing())
			activity.finish();
	}

	public static void flurryLog(String s, String s1) {
		HashMap hashmap = new HashMap();
		hashmap.put("name", s1);
		FlurryAgent.logEvent(s, hashmap);
	}

	public static String formatNum(double d) {
		return decimalFormat.format(d);
	}

	public static String getApplicationUserName(Context context, String s) {
		return (new StringBuilder()).append(s).append("_")
				.append(android.provider.Settings.Secure.getString(context.getContentResolver(), "android_id")).toString();
	}

	public static String getApplicationUserPassword(Context context, String s) {
		return (new StringBuilder()).append(s).append("_touchchina").toString();
	}

	public static InputStream getAssetInputStream(Context context, String s) {
		try {
			return context.getAssets().open(s);
		} catch (IOException e) {
			Log.e(TAG, (new StringBuilder()).append("getAssetInputStream ").append(s).toString(), e);
			return null;
		}

	}

	public static Location getBestLocation(Context context) {
		Location location = null;
		LocationManager locationmanager = (LocationManager) context.getSystemService("location");
		String s = getBestProvider(locationmanager);
		if (s != null && s.length() > 0)
			location = locationmanager.getLastKnownLocation(s);
		return location;
	}

	public static String getBestProvider(LocationManager locationmanager) {
		String s = null;
		if (locationmanager != null) {
			Criteria criteria = new Criteria();
			criteria.setAccuracy(2);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(0);
			s = locationmanager.getBestProvider(criteria, true);
			if (s == null)
				s = "network";
		}
		return s;
	}

	public static Bitmap getBitmap(Context context, String s) {
		Bitmap bitmap;
		if (s.startsWith("file:///android_asset/")) {
			InputStream inputstream1 = getAssetInputStream(context, s.substring("file:///android_asset/".length()));
			bitmap = BitmapFactory.decodeStream(inputstream1);
			inputstreamClose(inputstream1);
		} else {
			bitmap = BitmapFactory.decodeFile(s);
		}
		if (bitmap == null) {
			InputStream inputstream = getAssetInputStream(context, "icon/null.png");
			bitmap = BitmapFactory.decodeStream(inputstream);
			inputstreamClose(inputstream);
		}
		return bitmap;
	}

	public static byte[] getBitmapByteArray(Bitmap bitmap) {
		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 80, bytearrayoutputstream);
		byte abyte0[] = bytearrayoutputstream.toByteArray();
		try {
			bytearrayoutputstream.close();
		} catch (IOException ioexception) {
		}
		return abyte0;
	}

	public static byte[] getByteArray(InputStream inputstream) {
		try {
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			byte abyte1[] = new byte[1024];
			do {
				int i = inputstream.read(abyte1);
				if (i == -1)
					break;
				bytearrayoutputstream.write(abyte1, 0, i);
			} while (true);
			byte result[] = bytearrayoutputstream.toByteArray();
			bytearrayoutputstream.close();
			inputstream.close();
			return result;
		} catch (IOException e) {
			return null;
		}

	}

	public static Date getDateAfter(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(5, i + calendar.get(5));
		return calendar.getTime();
	}

	/**
	 * calculate the display parameters
	 * 
	 * @param activity
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Activity activity) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		return displaymetrics;
	}

	public static double getDistance(double d, double d1, double d2, double d3) {
		double d4 = rad(d);
		double d5 = rad(d2);
		double d6 = rad(d1);
		double d7 = rad(d3);
		double d8 = d4 - d5;
		double d9 = 0.5D * (d6 - d7) * (Math.cos(d4) + Math.cos(d5));
		return 6371229D * Math.sqrt(Math.pow(d8, 2D) + Math.pow(d9, 2D));
	}

	public static String getDistance(double d) {
		String s = "";
		if (d > -0.5D)
			if (d > 1000D) {
				DecimalFormat decimalformat = new DecimalFormat("0");
				double d1 = d / 1000D;
				s = (new StringBuilder()).append(decimalformat.format(d1)).append("km").toString();
			} else {
				DecimalFormat decimalformat1 = new DecimalFormat("0");
				s = (new StringBuilder()).append(decimalformat1.format(d)).append(" m").toString();
			}
		return s;
	}

	public static String getDownloadFileSize(long l) {
		if (l < 0) {
			return "";
		} else if (l < 1024) {
			return (new StringBuilder()).append(l).append("B").toString();
		} else {
			if (l < 0x100000L)
				return (new StringBuilder()).append(decimalFormat.format((1.0D * (double) l) / 1024D)).append("KB").toString();
			else if (l < 0x40000000L)
				return (new StringBuilder()).append(decimalFormat.format((1.0D * (double) l) / 1024D / 1024D)).append("MB").toString();
		}
		return "" + l;

	}

	public static Drawable getDrawable(Context context, int i) {
		Paint paint = new Paint(257);
		paint.setColor(-1);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextAlign(android.graphics.Paint.Align.CENTER);
		String s;
		byte byte0;
		Bitmap bitmap;
		BitmapDrawable bitmapdrawable;
		if (TCData.USE_2X) {
			s = "poi/poi_normal@2x.png";
			paint.setTextSize(30F);
			byte0 = 8;
		} else {
			s = "poi/poi_normal.png";
			paint.setTextSize(15F);
			byte0 = 4;
		}
		bitmap = BitmapFactory.decodeStream(getAssetInputStream(context, s)).copy(android.graphics.Bitmap.Config.ARGB_8888, true);
		(new Canvas(bitmap)).drawText(String.valueOf(i), bitmap.getWidth() / 2 - byte0, bitmap.getHeight() / 2, paint);
		bitmapdrawable = new BitmapDrawable(bitmap);
		bitmapdrawable.setTargetDensity(context.getResources().getDisplayMetrics());
		return bitmapdrawable;
	}

	public static Drawable getDrawable(Context context, String s) {
		BitmapDrawable bitmapdrawable;
		if (s.startsWith("file:///android_asset/"))
			bitmapdrawable = new BitmapDrawable(getAssetInputStream(context, s.substring("file:///android_asset/".length())));
		else
			bitmapdrawable = new BitmapDrawable(s);
		bitmapdrawable.setTargetDensity(context.getResources().getDisplayMetrics());
		return bitmapdrawable;
	}

	public static Drawable getDrawable1(Context context, String s) {
		BitmapDrawable bitmapdrawable;
		if (s.startsWith("file:///android_asset/"))
			bitmapdrawable = new BitmapDrawable(getAssetInputStream(context, s.substring("file:///android_asset/".length())));
		else
			bitmapdrawable = new BitmapDrawable(s);
		return bitmapdrawable;
	}

	public static InputStream getFileInputStream(Context context, String s) {
		Object obj = null;
		if (s.startsWith("file:///android_asset/")) {
			String s1 = s.substring("file:///android_asset/".length());

			try {
				return context.getAssets().open(s1);
			} catch (IOException ioexception) {
				Log.e(TAG, "static InputStream getFileInputStream(Context context, String filename)", ioexception);
				return null;
			}

		} else {
			try {
				return new FileInputStream(new File(s));
			} catch (IOException e) {
				Log.e(TAG, "static InputStream getFileInputStream(Context context, String filename)", e);
			}
			return null;
		}
	}

	public static LayoutInflater getLayoutInflater(Context context) {
		return (LayoutInflater) context.getSystemService("layout_inflater");
	}

	public static Bitmap getPhotoBitmap(Context context, Intent intent) {
		try {
			return android.provider.MediaStore.Images.Media.getBitmap(context.getContentResolver(), intent.getData());
		} catch (FileNotFoundException e) {
			Log.e(TAG, "getPhotoBitmap", e);
		} catch (IOException e) {
			Log.e(TAG, "getPhotoBitmap", e);
		}
		return null;
	}

	public static Bitmap getRemoveBitmap(Context context, Bitmap bitmap) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap bitmap1;
		try {
			Bitmap bitmap2 = BitmapFactory.decodeStream(context.getAssets().open("remove@2x.png"));
			bitmap1 = Bitmap.createBitmap(bitmap.getWidth() + bitmap2.getWidth() / 2, bitmap.getHeight() + bitmap2.getHeight() / 2,
					android.graphics.Bitmap.Config.ARGB_8888);
			Canvas canvas1 = new Canvas(bitmap1);
			canvas1.drawARGB(0, 0, 0, 0);
			canvas1.drawBitmap(bitmap, bitmap2.getWidth() / 2, bitmap2.getHeight() / 2, paint);
			bitmap.recycle();
			canvas1.drawBitmap(bitmap2, 0.0F, 0.0F, paint);
			bitmap2.recycle();
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
			bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap1);
			canvas.drawARGB(0, 0, 0, 0);
			canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
			bitmap.recycle();
		}
		return bitmap1;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap bitmap1 = null;
		if (bitmap != null) {
			bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap1);
			Paint paint = new Paint();
			Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			RectF rectf = new RectF(rect);
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(0xffff0000);
			canvas.drawRoundRect(rectf, 15F, 15F, paint);
			paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			bitmap.recycle();
		}
		return bitmap1;
	}

	public static String getSDPath() {
		return (new StringBuilder()).append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/").toString();
	}

	public static long getSdCardFree() {
		long l = 0L;
		if (Environment.getExternalStorageState().equals("mounted")) {
			StatFs statfs = new StatFs(Environment.getExternalStorageDirectory().getPath());
			l = ((long) statfs.getBlockSize() * (long) statfs.getAvailableBlocks()) / 1024L / 1024L;
		}
		return l;
	}

	public static int getVersionCode(Context context) {
		int code = 0;

		List<PackageInfo> list = context.getPackageManager().getInstalledPackages(0);
		if (list != null && list.size() > 0) {
			String s = context.getApplicationContext().getPackageName();
			for (PackageInfo info : list) {
				if (info.packageName.equals(s)) {
					return info.versionCode;
				}
			}
		}
		return code;
	}

	public static String getVersionName(Context context) {

		List<PackageInfo> list = context.getPackageManager().getInstalledPackages(0);
		if (list != null && list.size() > 0) {
			String s = context.getApplicationContext().getPackageName();
			for (PackageInfo info : list) {
				if (info.packageName.equals(s)) {
					if (info.versionName != null)
						return info.versionName.split("_")[1];
					else
						return "";
				}
			}
		}
		return "";

	}

	/**
	 * check google map provided by phone
	 * 
	 * @return
	 */
	public static boolean googMapEnable() {
		boolean flag = true;
		try {
			Class.forName("com.google.android.maps.MapActivity");
		} catch (Exception exception) {
			flag = false;
		}
		if (!flag)
			try {
				Class.forName("com.google.android.maps.MapsActivity");
			} catch (Exception exception1) {
				flag = false;
			}
		return flag;
	}

	public static void hideSoftKeyBroad(Context context, EditText edittext) {
		((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(edittext.getWindowToken(), 0);
	}

	public static void hideSoftKeyBroads(Context context) {
		((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(((Activity) context).getWindow().peekDecorView()
				.getWindowToken(), 0);
	}

	public static void inputstreamClose(InputStream inputstream) {
		try {
			inputstream.close();
		} catch (Exception e) {
			Log.e(TAG, "inputstreamClose", e);
		}

	}

	public static boolean isGPSAvailable(Context context) {
		List list = ((LocationManager) context.getSystemService("location")).getProviders(true);
		boolean flag = true;
		if (list != null) {
			if (list.size() == 1 && "passive".equals(list.get(0)))
				flag = false;
		} else {
			flag = false;
		}
		return flag;
	}

	public static boolean isNetAvailable(Context context) {
		NetworkInfo networkinfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
		boolean flag;
		if (networkinfo != null && networkinfo.isAvailable())
			flag = true;
		else
			flag = false;
		return flag;
	}

	public static boolean isSDCardAvailable() {
		boolean flag;
		if (Environment.getExternalStorageState().equals("mounted"))
			flag = true;
		else
			flag = false;
		return flag;
	}

	public static boolean isUnZipInterrapted(String s, int i, boolean flag) {
		String s1 = null;
		boolean flag1 = false;
		File file;
		if (s.equals("cg"))
			s1 = (new StringBuilder()).append(TCData.CG_ROOT).append(i).append("/Cache/").toString();
		else if (s.equals("sg"))
			s1 = (new StringBuilder()).append(TCData.SG_ROOT).append(i).append("/Cache/").toString();
		file = new File(s1);
		if (file.exists())
			if (file.list().length == 1) {
				if ((long) Integer.parseInt(file.list()[0].split("_")[1]) == (new File((new StringBuilder()).append(s1).append(file.list()[0])
						.toString())).length()) {
					flag1 = true;
					if (flag)
						unzip((new StringBuilder()).append(s1).append(file.list()[0]).toString(), s1.substring(0, s1.lastIndexOf("Cache/")));
				} else {
					flag1 = false;
				}
			} else if (file.list().length == 0)
				flag1 = false;
		return flag1;
	}

	public static void loadUrl(Activity activity, WebView webview, String s) {
		if (!s.contains(":")) {
			webview.loadUrl((new StringBuilder()).append("file://").append(s).toString());

		} else if (s.startsWith("tel:")) {
			activity.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(s)));
		} else if (s.startsWith("mailto:"))
			activity.startActivity(Intent.createChooser(new Intent("android.intent.action.SENDTO", Uri.parse(s)), activity.getString(0x7f080084)));
		else if (s.equalsIgnoreCase("http://www.itouchchina.com/privatepolicy.html")
				|| s.equalsIgnoreCase("http://www.itouchchina.com/copyright.html"))
			webview.loadUrl(s);
		else if (s.startsWith("http://"))
			activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(s)));
		else if (s.startsWith("file://"))
			webview.loadUrl(s);

	}

	public static double log2(double d) {
		return Math.log(d) / Math.log(2D);
	}

	public static void makeDirs(String s) {
		File file = new File(s);
		if (!file.exists())
			file.mkdirs();
	}

	public static void newsUnzip(String s, String s1) {
		if (!fileExists(s))
			return;
		try {
			ZipInputStream zipinputstream = new ZipInputStream(new FileInputStream(s));
			BufferedOutputStream bufferedoutputstream = null;
			while (true) {
				ZipEntry zipentry = zipinputstream.getNextEntry();
				if (zipentry == null)
					break;
				byte abyte0[] = new byte[4096];
				File file = new File((new StringBuilder()).append(s1).append(zipentry.getName()).toString());
				File file1 = new File(file.getParent());
				if (!file1.exists())
					file1.mkdirs();
				bufferedoutputstream = new BufferedOutputStream(new FileOutputStream(file), 4096);
				do {
					int i = zipinputstream.read(abyte0, 0, 4096);
					if (i == -1)
						break;
					bufferedoutputstream.write(abyte0, 0, i);
				} while (true);
				bufferedoutputstream.close();

			}
			zipinputstream.close();

		} catch (IOException e) {
			Log.e(TAG, "public static void doUnzip(String zipFile, String targetDir)", e);
		}

	}

	private static double rad(double d) {
		return (3.1415926535897931D * d) / 180D;
	}

	public static void recycleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled())
			bitmap.recycle();
	}

	public static void saveNewFile(InputStream inputstream, String s) {
		if (fileExists(s))
			deleteFile(s);
		try {
			FileOutputStream fileoutputstream = new FileOutputStream(s);
			byte abyte0[] = new byte[1024];
			do {
				int i = inputstream.read(abyte0);
				if (i <= 0)
					break;
				fileoutputstream.write(abyte0, 0, i);
			} while (true);
			fileoutputstream.close();
			inputstream.close();
		} catch (IOException ioexception) {
		}
	}

	public static Bitmap scaleBitmap(Bitmap bitmap, int i, int j) {
		Bitmap bitmap1;
		if (bitmap != null) {
			int k = bitmap.getWidth();
			int l = bitmap.getHeight();
			float f = (float) i / (float) k;
			float f1 = (float) j / (float) l;
			Matrix matrix = new Matrix();
			matrix.postScale(f, f1);
			bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, k, l, matrix, true);
		} else {
			bitmap1 = null;
		}
		return bitmap1;
	}

	public static void selectImage(Activity activity, int i) {
		Intent intent = new Intent("android.intent.action.GET_CONTENT");
		intent.setType("image/*");
		activity.startActivityForResult(Intent.createChooser(intent, activity.getString(0x7f080083)), i);
	}

	public static void setFormat(Activity activity) {
		activity.getWindow().setFormat(1);
	}

	public static void setTargetHeapUtilization() {
	}

	public static void showNetErrorDialog(final Activity activity) {
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
		builder.setTitle(activity.getString(0x7f08007f));
		builder.setMessage(activity.getString(0x7f080080));
		builder.setNegativeButton(activity.getString(0x7f080082), new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialoginterface, int i) {
				dialoginterface.dismiss();
				activity.finish();
			}

		});
		builder.setPositiveButton(activity.getString(0x7f080081), new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialoginterface, int i) {
				dialoginterface.dismiss();
				TCUtil.startWirelessSetting(activity);
			}

		});
		builder.create().show();
	}

	public static void showTip(Context context, int i) {
		Toast.makeText(context, context.getString(i), 1).show();
	}

	public static void showToastBottom(Context context, String s) {
		Toast toast = Toast.makeText(context, s, 0);
		toast.setGravity(81, 0, 80);
		toast.show();
	}

	public static void startActivity(Context context, Class class1) {
		context.startActivity(new Intent(context, class1));
	}

	public static void startActivity(Context context, Class class1, Bundle bundle) {
		Intent intent = new Intent(context, class1);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	public static void startActivityForResult(Activity activity, Class class1, Bundle bundle, int i) {
		Intent intent = new Intent(activity, class1);
		intent.putExtras(bundle);
		activity.startActivityForResult(intent, i);
	}

	public static void startCameraForResult(Activity activity, int i) {
		Uri uri = Uri.fromFile(new File(WeiboActivity.CAMERA_PATH));
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra("output", uri);
		activity.startActivityForResult(intent, i);
	}

	public static void startPackage(Activity activity, String s, String s1, Bundle bundle) {
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(s, s1));
		intent.putExtras(bundle);
		activity.startActivity(intent);
	}

	public static void startSDCardSetting(Activity activity) {
		activity.startActivity(new Intent("android.settings.MEMORY_CARD_SETTINGS"));
	}

	public static void startWirelessSetting(Activity activity) {
		activity.startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
	}

	public static String timeStamp2YYMMDD(int i) {
		return (new SimpleDateFormat("yyyy.MM.dd")).format(new Date(1000L * (long) i));
	}

	public static void unzip(String s, String s1) {
		if (fileExists(s) && doUnzip(s, s1))
			deleteFile(s);
	}

	public static void writeFile(InputStream inputstream, String s) {
		try {
			FileOutputStream fileoutputstream = new FileOutputStream(s);
			byte abyte0[] = new byte[1024];
			do {
				int i = inputstream.read(abyte0);
				if (i <= 0)
					break;
				fileoutputstream.write(abyte0, 0, i);
			} while (true);
			fileoutputstream.close();
			inputstream.close();
		} catch (IOException ioexception) {
		}
	}

	private static final String TAG = TCUtil.class.getSimpleName();
	private static final DecimalFormat decimalFormat = new DecimalFormat("0.0");

}
