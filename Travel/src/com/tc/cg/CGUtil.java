//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import javax.xml.transform.sax.SAXSource;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;

import com.flurry.android.FlurryAgent;
import com.tc.TCData;
import com.tc.TCUtil;

// Referenced classes of package com.tc.cg:
//            CGData, CGService

public class CGUtil {

	public CGUtil() {
	}

	public static CGData.CGPlanData.CGPlan addCGPlan(Context context, CGData cgdata, long l, float f, int i, int j, int k, String s, String s1,
			String s2) {
		CGData.CGPlanData.CGPlan cgplan;
		float f1;
		android.database.sqlite.SQLiteDatabase sqlitedatabase;
		File file;
		cgplan = null;
		f1 = f;
		if (f1 > (int) f1) {
			f1 = 1 + (int) f1;
			i = 0;
			j = 0;
		}
		sqlitedatabase = CGData.CGSGData.openDatabase(context, cgdata);
		file = new File(cgdata.CG_PLAN_MODEL_PATH);
		if (file != null && file.exists() && file.isDirectory()) { // goto _L2;
																	// else goto
																	// _L1
			int i1 = 0x7fffffff;
			int j1 = 0;
			File file1=null;
			File afile[] = file.listFiles();
			int k1 = afile.length;
			int l1 = 0;
			while (l1 < k1) {
				File file2 = afile[l1];
				if (file2.isFile()){
	
				String s8 = file2.getAbsolutePath();
				String as[] = s8.substring(s8.lastIndexOf("plan_") + "plan_".length(), s8.length() - "_0.xml".length()).split("_");
				int j5 = Integer.parseInt(as[0]);
				int k5 = Integer.parseInt(as[1]);
				int l5 = Integer.parseInt(as[2]);
				int i6 = Integer.parseInt(as[3]);
				float f2 = 10F * Math.abs((float) k5 - f1);
				int j6;
				float f3;
				int k6;
				float f4;
				int l6;
				int i7;
				if (j == l5)
					j6 = 0;
				else
					j6 = 1;
				f3 = f2 + (float) j6;
				if (i == i6)
					k6 = 0;
				else
					k6 = 1;
				f4 = f3 + (float) k6;
				if (k == j5)
					l6 = 0;
				else
					l6 = 2;
				i7 = (int) (f4 + (float) l6);
				if (i1 > i7) {
					i1 = i7;
					file1 = file2;
					j1 = (int) (f1 - (float) k5);
					Log.i("CGPlan", (new StringBuilder()).append(s8).append(" result ").append(i1).append("needAddDayCount").append(j1).toString());
				}
				}
				l1++;
			}

			if (s2 != null || s2.length() > 0) {
				s2 = s2.trim();
			} else {
				for (CGData.CGPlanData.CGPlan.CGPlayType cgplaytype : cgdata.CG_PLAN_DATA.INTEREST_TYPE_ARRAY) {
					if (cgplaytype.id == k) {
						s2 = (new StringBuilder()).append(cgplaytype.name.substring(0, 2)).append(context.getString(0x7f0800f7)).toString();
						break;
					}
				}
			}
			if (cgdata.CG_PLAN_DATA.CG_PLAN_LIST.size() > 0) {
				ArrayList arraylist;
				arraylist = new ArrayList();
				for (CGData.CGPlanData.CGPlan plan : cgdata.CG_PLAN_DATA.CG_PLAN_LIST) {
					arraylist.add(plan.name);
				}
				if (arraylist.contains(s2)) {
					StringBuffer sb = new StringBuffer(s2);
					int index = s2.lastIndexOf(" ");
					int num = Integer.parseInt(s2.substring(index + 1, index + 2));
					sb.replace(index + 1, index + 2, "" + (num + 1));
					s2 = sb.toString();
				} else {
					s2 = (new StringBuilder()).append(s2).append(" 2").toString();
				}

			}
			FileInputStream fileinputstream;
			cgplan = new CGData.CGPlanData.CGPlan(l, f, i, j, k, s, s1, s2, false);
			fileinputstream = null;
			CGData.CGPlanData.CGPlan.CGPlanDay.CGPlanSection.CGPlanItem cgplanitem=null;
			CGData.CGPlanData.CGPlan.CGPlanDay.CGPlanSection cgplansection=null;
			CGData.CGPlanData.CGPlan.CGPlanDay cgplanday=null;
			
			try{
			FileInputStream fileinputstream1 = new FileInputStream(file1);
			XmlPullParser xmlpullparser;
			int j2;
			
			xmlpullparser = Xml.newPullParser();
			xmlpullparser.setInput(fileinputstream1, "utf-8");
			j2 = xmlpullparser.getEventType();
		
			while (j2 != XmlPullParser.END_DOCUMENT) {
				String name = xmlpullparser.getName();
				if (j2 == XmlPullParser.START_TAG) {
					if ("day".equals(name)) {
						int j4 = Integer.parseInt(xmlpullparser.getAttributeValue(null, "day"));
						cgplanday = new CGData.CGPlanData.CGPlan.CGPlanDay(j4);
						cgplan.cgPlanDayList.add(cgplanday);
					} else if ("section".equals(xmlpullparser.getName())) {
						boolean hasDinner = false;
						String dinner = xmlpullparser.getAttributeValue(null, "dinner");
						if (dinner.equals("true") || dinner.equals("1"))
							hasDinner = true;
						;
						int sectionID = Integer.parseInt(xmlpullparser.getAttributeValue(null, "section"));
						int end = Integer.parseInt(xmlpullparser.getAttributeValue(null, "end"));
						int begin = Integer.parseInt(xmlpullparser.getAttributeValue(null, "begin"));

						cgplansection = new CGData.CGPlanData.CGPlan.CGPlanDay.CGPlanSection(sectionID, hasDinner, end, begin);
						cgplanday.cgPlanSectionList.add(cgplansection);
					} else if ("item".equals(xmlpullparser.getName())) {
						int day = Integer.parseInt(xmlpullparser.getAttributeValue(null, "day"));
						String comment = xmlpullparser.getAttributeValue(null, "comment");
						int type = Integer.parseInt(xmlpullparser.getAttributeValue(null, "type"));
						int id = Integer.parseInt(xmlpullparser.getAttributeValue(null, "id"));
						String mapIDString = xmlpullparser.getAttributeValue(null, "mapid");
						int mapID = 0;
						if (mapIDString != null) {
							mapID = Integer.parseInt(mapIDString);
							cgplanitem = new CGData.CGPlanData.CGPlan.CGPlanDay.CGPlanSection.CGPlanItem(day, comment, type, id, mapID,
									String.valueOf(k));
							cgplanitem.setTimesAndName(sqlitedatabase);
						} else
							cgplanitem = new CGData.CGPlanData.CGPlan.CGPlanDay.CGPlanSection.CGPlanItem(day, comment, type, id, mapID,
									String.valueOf(k));
						cgplanitem.sgItem = CGData.getSGItem(cgdata, type, id);
						if (cgplanitem.name == null)
							cgplanitem.name = cgplanitem.sgItem.name;
						cgplansection.cgPlanItemList.add(cgplanitem);
					}

				} else if (j2 == XmlPullParser.END_TAG) {
					if ("day".equals(name)) {
						for (CGData.CGPlanData.CGPlan.CGPlanDay.CGPlanSection section : cgplanday.cgPlanSectionList) {
							for (CGData.CGPlanData.CGPlan.CGPlanDay.CGPlanSection.CGPlanItem item : section.cgPlanItemList) {
								cgplanday.sgItemList.add(item.sgItem);
							}
						}

					}
				}

				j2 = xmlpullparser.next();
			}
			fileinputstream1.close();
			
			}catch(IOException e){
				Log.e("CGUTIL", "",e);
			}catch(XmlPullParserException e){
				Log.e("CGUTIL", "",e);
			}

			for (int i2 = 0; i2 < j1; i2++) {
				cgplanday = new CGData.CGPlanData.CGPlan.CGPlanDay(1 + cgplan.cgPlanDayList.size());
				cgplanday.setDefaultCGPlanSectionList();
				cgplan.cgPlanDayList.add(cgplanday);
			}

			cgdata.CG_PLAN_DATA.CG_PLAN_LIST.add(cgplan);
			Comparator comparator = new Comparator() {

				public int compare(CGData.CGPlanData.CGPlan cgplan1, CGData.CGPlanData.CGPlan cgplan2) {
					return CGUtil.compare(Long.valueOf(cgplan1.currentTimeMillis), Long.valueOf(cgplan2.currentTimeMillis));
				}

				public int compare(Object obj, Object obj1) {
					return compare((CGData.CGPlanData.CGPlan) obj, (CGData.CGPlanData.CGPlan) obj1);
				}

			};
			Collections.sort(cgdata.CG_PLAN_DATA.CG_PLAN_LIST, comparator);
			createCGPlanXml(context, cgdata, cgplan);

		}
		CGData.CGSGData.closeDatabase();
		return cgplan;

	}

	public static int compare(Boolean boolean1, Boolean boolean2) {
		int i = 1;
		if (boolean1.equals(boolean2))
			return 0;
		else
			return boolean1 ? 1 : -1;

	}

	public static int compare(Double double1, Double double2) {
		double d = double1.doubleValue();
		double d1 = double2.doubleValue();
		byte byte0;
		if (d < d1)
			byte0 = -1;
		else if (d == d1)
			byte0 = 0;
		else
			byte0 = 1;
		return byte0;
	}

	public static int compare(Float float1, Float float2) {
		float f = float1.floatValue();
		float f1 = float2.floatValue();
		byte byte0;
		if (f < f1)
			byte0 = -1;
		else if (f == f1)
			byte0 = 0;
		else
			byte0 = 1;
		return byte0;
	}

	public static int compare(Integer integer, Integer integer1) {
		int i = integer.intValue();
		int j = integer1.intValue();
		byte byte0;
		if (i < j)
			byte0 = -1;
		else if (i == j)
			byte0 = 0;
		else
			byte0 = 1;
		return byte0;
	}

	public static int compare(Long long1, Long long2) {
		long l = long1.longValue();
		long l1 = long2.longValue();
		byte byte0;
		if (l < l1)
			byte0 = -1;
		else if (l == l1)
			byte0 = 0;
		else
			byte0 = 1;
		return byte0;
	}

	public static int compare(Object obj, Object obj1) {
		int i;
		if (obj instanceof String)
			i = compare((String) obj, (String) obj1);
		else if (obj instanceof Integer)
			i = compare((Integer) obj, (Integer) obj1);
		else if (obj instanceof Float)
			i = compare((Float) obj, (Float) obj1);
		else if (obj instanceof Double)
			i = compare((Float) obj, (Float) obj1);
		else
			i = 1;
		return i;
	}

	public static int compare(String s, String s1) {
		byte[] first, second;
		try {
			first = s.getBytes("GB2312");
			second = s1.getBytes("GB2312");
			int length = Math.min(first.length, second.length);
			for (int i = 0; i < length; i++) {
				if (first[i] != second[i]) {
					return first[i] - second[i] > 0 ? 1 : -1;
				}
			}
			if (first.length == second.length)
				return 0;
			return first.length - second.length > 0 ? 1 : -1;
		} catch (UnsupportedEncodingException e) {
			return 0;
		}

	}

	public static void createCGPlanXml(Context context, CGData cgdata, CGData.CGPlanData.CGPlan cgplan) {
		File file;
		file = new File((new StringBuilder()).append(cgdata.CG_PLAN_MY_PATH).append(cgplan.interest).append("_").append(cgplan.currentTimeMillis)
				.append(".xml").toString());
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		if (file.exists())
			file.delete();

		FileOutputStream fileoutputstream;
		XmlSerializer xmlserializer;
		try {
			file.createNewFile();

			fileoutputstream = new FileOutputStream(file);
			xmlserializer = Xml.newSerializer();
			xmlserializer.setOutput(fileoutputstream, "UTF-8");
			xmlserializer.startDocument("UTF-8", Boolean.valueOf(true));
			xmlserializer.startTag(null, "plan");
			xmlserializer.attribute(null, "name", String.valueOf(cgplan.name));

			String s = null;
			if (cgplan.begindate.contains("_")) {
				s = cgplan.begindate.replace("_", "-");
			} else
				s = cgplan.begindate;

			xmlserializer.attribute(null, "begindate", s);
			String s1 = null;
			if (cgplan.enddate.contains("_")) {
				s1 = cgplan.enddate.replace("_", "-");
			} else
				s1 = cgplan.enddate;

			xmlserializer.attribute(null, "enddate", s1);
			xmlserializer.attribute(null, "arrive", String.valueOf(cgplan.arrive));
			xmlserializer.attribute(null, "leave", String.valueOf(cgplan.leave));
			xmlserializer.attribute(null, "days", String.valueOf(cgplan.days));
			xmlserializer.attribute(null, "interest", String.valueOf(cgplan.interest));
			xmlserializer.attribute(null, "time", String.valueOf(cgplan.currentTimeMillis));
			xmlserializer.attribute(null, "opened", String.valueOf(cgplan.opened));

			for (CGData.CGPlanData.CGPlan.CGPlanDay cgplanday : cgplan.cgPlanDayList) {
				xmlserializer.startTag(null, "day");
				xmlserializer.attribute(null, "day", String.valueOf(cgplanday.noDay));
				for (CGData.CGPlanData.CGPlan.CGPlanDay.CGPlanSection cgplansection : cgplanday.cgPlanSectionList) {
					xmlserializer.startTag(null, "section");
					xmlserializer.attribute(null, "section", String.valueOf(cgplansection.no));
					xmlserializer.attribute(null, "begin", String.valueOf(cgplansection.begin));
					xmlserializer.attribute(null, "end", String.valueOf(cgplansection.end));
					xmlserializer.attribute(null, "dinner", String.valueOf(cgplansection.dinner));
					for (CGData.CGPlanData.CGPlan.CGPlanDay.CGPlanSection.CGPlanItem cgplanitem : cgplansection.cgPlanItemList) {

						xmlserializer.startTag(null, "item");
						xmlserializer.attribute(null, "mapid", String.valueOf(cgplanitem.mapid));
						xmlserializer.attribute(null, "id", String.valueOf(cgplanitem.id));
						xmlserializer.attribute(null, "type", String.valueOf(cgplanitem.type));
						xmlserializer.attribute(null, "comment", cgplanitem.comment);
						xmlserializer.attribute(null, "day", String.valueOf(cgplanitem.noDay));
						xmlserializer.attribute(null, "begin", String.valueOf(cgplanitem.begin));
						xmlserializer.attribute(null, "end", String.valueOf(cgplanitem.end));
						xmlserializer.endTag(null, "item");
					}
					xmlserializer.endTag(null, "section");
				}
				xmlserializer.endTag(null, "day");
			}
			xmlserializer.endTag(null, "plan");
			xmlserializer.endDocument();
			xmlserializer.flush();
			fileoutputstream.close();
		} catch (IOException e) {
			Log.e("CGUtil", "", e);
		}
	}

	public static void deleteCGPlan(CGData cgdata, int i) {
		CGData.CGPlanData.CGPlan cgplan = (CGData.CGPlanData.CGPlan) cgdata.CG_PLAN_DATA.CG_PLAN_LIST.get(i);
		long l = cgplan.currentTimeMillis;
		int j = cgplan.interest;
		cgdata.CG_PLAN_DATA.CG_PLAN_LIST.remove(i);
		File file = new File((new StringBuilder()).append(cgdata.CG_PLAN_MY_PATH).append(j).append("_").append(l).append(".xml").toString());
		if (file.exists())
			file.delete();
		File file1 = new File((new StringBuilder()).append(cgdata.CG_PLAN_MY_PATH).append(j).append("_").append(l).toString());
		if (file1.exists())
			file1.delete();
	}

	public static void deleteCGPlan(CGData cgdata, CGData.CGPlanData.CGPlan cgplan) {
		long l = cgplan.currentTimeMillis;
		int i = cgplan.interest;
		File file = new File((new StringBuilder()).append(cgdata.CG_PLAN_MY_PATH).append(i).append("_").append(l).append(".xml").toString());
		if (file.exists())
			file.delete();
		File file1 = new File((new StringBuilder()).append(cgdata.CG_PLAN_MY_PATH).append(i).append("_").append(l).toString());
		if (file1.exists())
			file1.delete();
	}

	public static CGData.CGPlanData.CGPlan modifyCGPlan(Context context, CGData cgdata, int i) {
		CGData.CGPlanData.CGPlan cgplan = (CGData.CGPlanData.CGPlan) cgdata.CG_PLAN_DATA.CG_PLAN_LIST.get(i);
		int j = 1;
		Iterator iterator;
		if (cgplan.begindate != null && cgplan.begindate.length() > 0)
			try {
				SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy_MM_dd");
				Date date = simpledateformat.parse(cgplan.begindate);
				date.setTime((long) ((float) date.getTime() + 1000F * (60F * (60F * (24F * ((0.5F + cgplan.days) - 1.0F))))));
				cgplan.enddate = simpledateformat.format(date);
			} catch (ParseException parseexception) {
			}
		for (iterator = cgplan.cgPlanDayList.iterator(); iterator.hasNext();) {
			CGData.CGPlanData.CGPlan.CGPlanDay cgplanday = (CGData.CGPlanData.CGPlan.CGPlanDay) iterator.next();
			int k = j + 1;
			cgplanday.noDay = j;
			j = k;
		}

		createCGPlanXml(context, cgdata, cgplan);
		return cgplan;
	}

	public static CGData.CGPlanData.CGPlan modifyCGPlan(Context context, CGData cgdata, CGData.CGPlanData.CGPlan cgplan) {
		int i = 1;
		cgplan.days = cgplan.cgPlanDayList.size();
		Iterator iterator;
		if (cgplan.begindate != null && cgplan.begindate.length() > 0)
			try {
				SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy_MM_dd");
				Date date = simpledateformat.parse(cgplan.begindate);
				date.setTime((long) ((float) date.getTime() + 1000F * (60F * (60F * (24F * ((0.5F + cgplan.days) - 1.0F))))));
				cgplan.enddate = simpledateformat.format(date);
			} catch (ParseException parseexception) {
			}
		for (iterator = cgplan.cgPlanDayList.iterator(); iterator.hasNext();) {
			CGData.CGPlanData.CGPlan.CGPlanDay cgplanday = (CGData.CGPlanData.CGPlan.CGPlanDay) iterator.next();
			int j = i + 1;
			cgplanday.noDay = i;
			i = j;
		}

		createCGPlanXml(context, cgdata, cgplan);
		return cgplan;
	}

	public static void openBrowser(Context context, String s) {
		Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(s));
		intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
		context.startActivity(intent);
	}

	public static boolean planNameVerify(Context context, CGData cgdata, int i, String s) {
		int j = 0;
		while (j < cgdata.CG_PLAN_DATA.CG_PLAN_LIST.size()) {
			if (j == i || !((CGData.CGPlanData.CGPlan) cgdata.CG_PLAN_DATA.CG_PLAN_LIST.get(j)).name.equals(s))
				j++;
			else
				return false;
		}
		return true;

	}

	public static CGData.CGPlanData.CGPlan resetCGPlan(Context context, CGData cgdata, int i) {
		CGData.CGPlanData.CGPlan cgplan = (CGData.CGPlanData.CGPlan) cgdata.CG_PLAN_DATA.CG_PLAN_LIST.get(i);
		float f = cgplan.days;
		int j = cgplan.leave;
		int k = cgplan.arrive;
		int l = cgplan.interest;
		String s = cgplan.enddate;
		String s1 = cgplan.begindate;
		String s2 = cgplan.name;
		long l1 = cgplan.currentTimeMillis;
		deleteCGPlan(cgdata, i);
		CGData.CGPlanData.CGPlan cgplan1 = addCGPlan(context, cgdata, l1, f, j, k, l, s, s1, s2);
		cgplan1.opened = false;
		return cgplan1;
	}

	public static void showExitDialog(final Activity activity) {
		final Dialog dialog = new Dialog(activity, 0x7f090003);
		dialog.setContentView(0x7f03006f);
		android.view.WindowManager.LayoutParams layoutparams = dialog.getWindow().getAttributes();
		layoutparams.width = TCData.SCREEN_WIDTH;
		layoutparams.height = TCData.SCREEN_HEIGHT;
		dialog.getWindow().setAttributes(layoutparams);
		dialog.onWindowAttributesChanged(layoutparams);
		android.view.View.OnClickListener onclicklistener = new android.view.View.OnClickListener() {

			public void onClick(View view) {
				if (view.getId() == 0x7f0a00c4) {

					((NotificationManager) activity.getSystemService("notification")).cancel(0x121111);
					TCUtil.finishActivity(activity);
					FlurryAgent.logEvent("onEndSession");
					FlurryAgent.onEndSession(activity.getApplicationContext());
					CGUtil.stopCGService(activity);
					System.exit(0);
				}
				dialog.dismiss();
			}

		};
		((Button) dialog.findViewById(0x7f0a00c4)).setOnClickListener(onclicklistener);
		((Button) dialog.findViewById(0x7f0a025f)).setOnClickListener(onclicklistener);
		dialog.show();
	}

	public static void startCGService(Context context) {
		context.startService(new Intent(context, CGService.class));
	}

	public static void stopCGService(Activity activity) {
		activity.stopService(new Intent(activity, CGService.class));
	}
}
