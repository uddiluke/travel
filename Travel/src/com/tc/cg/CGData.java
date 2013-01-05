//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Xml;

import com.tc.TCData;
import com.tc.TCUtil;

// Referenced classes of package com.tc.cg:
//            CGUtil, GetSGContentTimeStampHandler, CGService

public class CGData {
	public static class CGAlbumData extends com.tc.TCData.TCAlbumData {

		public CGAlbumData() {
		}
	}

	public static class CGBrand {

		public String name;
		public List sgBrands;

		public CGBrand() {
		}

		public static class SGBrand implements Serializable {

			public String aliasName;
			public int id;
			public String logo;
			public String name;
			public String type;

			public SGBrand() {
			}

			public SGBrand(SGBrand sgbrand) {
				id = sgbrand.id;
				name = sgbrand.name;
				type = sgbrand.type;
				logo = sgbrand.logo;
				aliasName = sgbrand.aliasName;
			}
		}
	}

	public static class CGFavoriteData implements Serializable {

		public static void delete(Context context, CGData cgdata, String s, int i) {
			DBOperator.getInstance(context, cgdata).deleteItem(s, i);
		}

		public static void insert(Context context, CGData cgdata, String s, int i, int j) {
			DBOperator.getInstance(context, cgdata).insertItem(s, i, j);
		}

		public static void query(Context context, CGData cgdata) {
			DBOperator.getInstance(context, cgdata).query();
			Iterator iterator = favoriteItems.iterator();
			do {
				if (!iterator.hasNext())
					break;
				SGFavoriteItem sgfavoriteitem = (SGFavoriteItem) iterator.next();
				CGSGData.SGItem sgitem = CGData.getSGItem(cgdata, sgfavoriteitem.type, sgfavoriteitem.id);
				if (sgitem != null)
					sgitem.current = sgfavoriteitem.current;
			} while (true);
		}

		public static final String CURRENT = "current";
		public static final int DB_VERSION = 1;
		public static final String ID = "id";
		public static final String TABLE_NAME = "favroite";
		public static final String TYPE = "type";
		public static List<CGData.CGFavoriteData.SGFavoriteItem> favoriteItems = new ArrayList<CGData.CGFavoriteData.SGFavoriteItem>();
		public String chName;
		public List siteTypes;
		public List sortTypes;
		public String tableName;

		public CGFavoriteData() {
		}

		private static class DBHelper extends SQLiteOpenHelper {

			public void execSQL(String s, Object aobj[]) {
				SQLiteDatabase sqlitedatabase = getWritableDatabase();
				sqlitedatabase.execSQL(s, aobj);
				sqlitedatabase.close();
			}

			public void onCreate(SQLiteDatabase sqlitedatabase) {
				sqlitedatabase.execSQL("CREATE TABLE favroite ( id INTEGER NOT NULL , type VARCHAR NOT NULL , current INTEGER NOT NULL );");
			}

			public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j) {
				sqlitedatabase.execSQL("DROP TABLE IF EXISTS favroite");
				onCreate(sqlitedatabase);
			}

			public Cursor query(String s, String as[]) {
				SQLiteDatabase sqlitedatabase = getReadableDatabase();
				Cursor cursor = sqlitedatabase.rawQuery(s, as);
				if (cursor != null && !cursor.moveToFirst())
					cursor = null;
				sqlitedatabase.close();
				return cursor;
			}

			private CGData cgData;

			public DBHelper(Context context, CGData cgdata) {
				super(context, (new StringBuilder()).append(cgdata.CG_APPLICATION).append(".sqlite").toString(), null, 1);
				cgData = cgdata;
			}
		}

		private static class DBOperator {

			private CGFavoriteData.SGFavoriteItem cursorToData(Cursor cursor) {
				return new CGFavoriteData.SGFavoriteItem(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
			}

			public static CGFavoriteData.DBOperator getInstance(Context context, CGData cgdata) {
				if (instance == null)
					instance = new CGFavoriteData.DBOperator(context, cgdata);
				return instance;
			}

			public void deleteItem(String s, int i) {
				Object aobj[] = new Object[2];
				aobj[0] = s;
				aobj[1] = Integer.valueOf(i);
				dbHelper.execSQL("delete from favroite where type = ? and id = ? ", aobj);
			}

			public void insertItem(String s, int i, int j) {
				Object aobj[] = new Object[3];
				aobj[0] = s;
				aobj[1] = Integer.valueOf(i);
				aobj[2] = Integer.valueOf(j);
				dbHelper.execSQL("insert into favroite ( type , id , current ) values ( ? , ? , ? ) ", aobj);
			}

			public void query() {
				Cursor cursor = dbHelper.query("select * from favroite", null);
				if (cursor != null) {
					do {
						CGFavoriteData.SGFavoriteItem sgfavoriteitem = cursorToData(cursor);
						CGFavoriteData.favoriteItems.add(sgfavoriteitem);
					} while (cursor.moveToNext());
					cursor.close();
				}
			}

			private static DBOperator instance;
			private CGFavoriteData.DBHelper dbHelper;

			private DBOperator(Context context, CGData cgdata) {
				dbHelper = new CGFavoriteData.DBHelper(context, cgdata);
			}
		}

		public static class SGFavoriteItem implements Serializable {

			public int current;
			public int id;
			public String type;

			public SGFavoriteItem(int i, String s, int j) {
				type = s;
				current = j;
				id = i;
			}
		}

	}

	public static class CGInfoData {

		public List cgInfoItems;

		public CGInfoData() {
			cgInfoItems = new ArrayList();
		}

		public static class CGInfoHandler extends DefaultHandler {

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

				if (!"city".equals(s1) && "item".equals(s1))
					level = 1;

			}

			public void startDocument() throws SAXException {
				super.startDocument();
				tag = null;
				level = -1;
			}

			public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
				super.startElement(s, s1, s2, attributes);
				tag = s1;
				if ("city".equals(s1)) {
					level = 1;
					cgData.CG_INFO_DATA = new CGInfoData();
					cgData.CG_ID = Integer.parseInt(attributes.getValue("id"));

					CGData.CG_COMMUNITY_CODE = attributes.getValue("community_code");
					cgData.CG_WGS84_USED = Boolean.parseBoolean(attributes.getValue("wgs84_used"));
					cgData.CG_NAME = attributes.getValue("name");
					cgData.CG_CNAME = attributes.getValue("cname");
					cgData.CG_LAT = Double.parseDouble(attributes.getValue("lat"));
					cgData.CG_LON = Double.parseDouble(attributes.getValue("lon"));

					CGData.CG_FLURRY = attributes.getValue("flurry");
					cgData.CG_VERSION = attributes.getValue("version");
					cgData.CG_WEATHER = attributes.getValue("weather");
					cgData.CG_PLAN_ARRAY = attributes.getValue("plan").split("split");
					cgData.CG_MAP_LEVEL = Integer.parseInt(attributes.getValue("level"));
				} else if ("item".equals(s1)) {
					level = 2;
					CGInfoData.CGInfoItem cginfoitem = new CGInfoData.CGInfoItem();
					cginfoitem.name = attributes.getValue("name");
					cginfoitem.chName = attributes.getValue("ch_name");
					cginfoitem.type = attributes.getValue("type");
					cginfoitem.url = attributes.getValue("url");
					cginfoitem.id = Integer.parseInt(attributes.getValue("id"));
					cgData.CG_INFO_DATA.cgInfoItems.add(cginfoitem);
				}

			}

			private static final int CITY_LEVEL = 1;
			private static final int IDLE_LEVEL = -1;
			private static final int ITEM_LEVEL = 2;
			private CGData cgData;
			private String characters;
			private int level;
			private String tag;

			public CGInfoHandler(CGData cgdata) {
				cgData = cgdata;
			}
		}

		public static class CGInfoItem {

			public String chName;
			public int id;
			public String name;
			public String type;
			public String url;

			public CGInfoItem() {
			}
		}

		public static class CGInfoParaser {

			public static void parse(Context context, CGData cgdata) {
				parse(context, cgdata, TCUtil.getFileInputStream(context, "file:///android_asset/cg_info_data.xml"));
			}

			private static void parse(Context context, CGData cgdata, InputStream inputstream) {
				try {
					SAXParserFactory.newInstance().newSAXParser().parse(inputstream, new CGInfoData.CGInfoHandler(cgdata));
				} catch (ParserConfigurationException e) {
					Log.e(CGData.TAG, " CGInfoData parse(Context context, String type, InputStream inputStream) ", e);
				} catch (SAXException e) {
					Log.e(CGData.TAG, " CGInfoData parse(Context context, String type, InputStream inputStream) ", e);
				} catch (IOException e) {
					Log.e(CGData.TAG, " CGInfoData parse(Context context, String type, InputStream inputStream) ", e);
				}

			}

			public CGInfoParaser() {
			}
		}

	}

	public static class CGMapData extends com.tc.TCData.TCMapData {

		public CGMapData() {
		}
	}

	public static class CGMoreData implements Serializable {

		public String chName;
		public List items;

		public CGMoreData() {
		}

		public static class CGMoreItem {

			public String icon;
			public String name;
			public String type;
			public String url;

			public CGMoreItem() {
			}
		}

		public static class MoreDataHandler extends DefaultHandler {

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
				if ("city".equals(s1))
					level = -1;
				else if ("item".equals(s1))
					level = 1;

			}

			public void startDocument() throws SAXException {
				super.startDocument();
				tag = null;
				level = -1;
			}

			public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
				super.startElement(s, s1, s2, attributes);
				tag = s1;
				if ("city".equals(s1)) {
					level = 1;
					cgData.CG_MORE_DATA.items = new ArrayList();
				} else if ("item".equals(s1)) {
					level = 2;
					CGMoreData.CGMoreItem cgmoreitem = new CGMoreData.CGMoreItem();
					cgData.CG_MORE_DATA.items.add(cgmoreitem);
					cgmoreitem.icon = TCData.getListIcon(attributes.getValue("icon"));
					cgmoreitem.name = attributes.getValue("name");
					cgmoreitem.type = attributes.getValue("type");
					cgmoreitem.url = (new StringBuilder()).append("file:///android_asset/").append(attributes.getValue("url")).toString();
				}

			}

			private static final int CITY_LEVEL = 1;
			private static final int IDLE_LEVEL = -1;
			private static final int ITEM_LEVEL = 2;
			private CGData cgData;
			private String characters;
			private int level;
			private String tag;

			public MoreDataHandler(CGData cgdata) {
				cgData = cgdata;
			}
		}

		public static class MoreDataParaser {

			public static void parse(Context context, CGData cgdata) {
				parse(context, cgdata, TCUtil.getFileInputStream(context, "file:///android_asset/cg_more_data.xml"));
			}

			private static void parse(Context context, CGData cgdata, InputStream inputstream) {
				try {
					SAXParserFactory.newInstance().newSAXParser().parse(inputstream, new CGMoreData.MoreDataHandler(cgdata));
				} catch (ParserConfigurationException e) {
					Log.e(CGData.TAG, " CGMoreData parse(Context context, String type, InputStream inputStream) ", e);
				} catch (SAXException e) {
					Log.e(CGData.TAG, " CGMoreData parse(Context context, String type, InputStream inputStream) ", e);
				} catch (IOException e) {
					Log.e(CGData.TAG, " CGMoreData parse(Context context, String type, InputStream inputStream) ", e);
				}
			}

			public MoreDataParaser() {
			}
		}

	}

	public static class CGPlanData {

		public static CGPlanData parase(Context context, CGData cgdata, SQLiteDatabase sqlitedatabase) {
			File file = new File(cgdata.CG_PLAN_PATH);
			if (file != null && file.exists() && file.isDirectory()) {
				File file1;
				cgdata.CG_PLAN_DATA = new CGPlanData();
				HashMap hashmap = new HashMap();
				Cursor cursor = sqlitedatabase.rawQuery("select * from Interest ;", null);
				if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
					do
						hashmap.put(cursor.getString(cursor.getColumnIndex("name")), Integer.valueOf(cursor.getInt(cursor.getColumnIndex("id"))));
					while (cursor.moveToNext());
					cursor.close();
				}
				cgdata.CG_PLAN_DATA.INTEREST_TYPE_ARRAY = new ArrayList();
				String as[] = context.getResources().getStringArray(0x7f050005);
				String as1[] = cgdata.CG_PLAN_ARRAY;
				int i = as1.length;
				for (int j = 0; j < i; j++) {
					String s7 = as1[j];
					int i4 = 0;
					do {
						// int j4 = as.length;
						if (i4 >= as.length)
							break;
						String as2[] = as[i4].split(":");
						if (as2[0].equals(s7)) {
							String as3[] = as2[1].split(",");
							List list = cgdata.CG_PLAN_DATA.INTEREST_TYPE_ARRAY;
							CGPlan.CGPlayType cgplaytype = new CGPlan.CGPlayType(as3[0], as3[1], ((Integer) hashmap.get(s7)).intValue());
							list.add(cgplaytype);
						}
						i4++;
					} while (true);
				}

				file1 = new File(cgdata.CG_PLAN_MY_PATH);
				if (file1 != null && file1.exists() && file1.isDirectory()) {
					File afile[] = file1.listFiles();
					if (afile == null || afile.length <= 0) {
						// goto _L4; else goto _L5
						// L4
					} else {

						int k = afile.length;
						int l = 0;
						// k = afile.length;
						// l = 0;
						// _L16:
						// if(l >= k) goto _L4; else goto _L6
						while (k > l) {
							File file2 = afile[l];
							if (file2.isFile() && file2.getAbsolutePath().endsWith(".xml")) { // goto
																								// _L8;
																								// else
																								// goto
																								// _L7
								int i1;
								CGPlan cgplan;
								FileInputStream fileinputstream;
								i1 = 0;
								cgplan = null;
								fileinputstream = null;
								Log.i(CGData.TAG, file2.getAbsolutePath());
								try {
									FileInputStream fileinputstream1 = new FileInputStream(file2);
									XmlPullParser xmlpullparser;
									int j1;
									CGPlan.CGPlanDay.CGPlanSection.CGPlanItem cgplanitem;
									CGPlan.CGPlanDay.CGPlanSection cgplansection;
									CGPlan.CGPlanDay cgplanday;
									CGPlan cgplan1;
									xmlpullparser = Xml.newPullParser();
									xmlpullparser.setInput(fileinputstream1, "utf-8");
									j1 = xmlpullparser.getEventType();
									cgplanitem = null;
									cgplansection = null;
									cgplanday = null;
							
									while (j1 != XmlPullParser.END_DOCUMENT) {
										switch (j1) {
										case XmlPullParser.START_TAG: {
											if ("plan".equals(xmlpullparser.getName())) {

												float f = Float.parseFloat(xmlpullparser.getAttributeValue(null, "days"));
												int k3 = Integer.parseInt(xmlpullparser.getAttributeValue(null, "leave"));
												int l3 = Integer.parseInt(xmlpullparser.getAttributeValue(null, "arrive"));
												i1 = Integer.parseInt(xmlpullparser.getAttributeValue(null, "interest"));
												String s3 = xmlpullparser.getAttributeValue(null, "enddate");
												if (s3.contains("-"))
													s3 = s3.replace("-", "_");
												String s4 = xmlpullparser.getAttributeValue(null, "begindate");
												if (s4.contains("-"))
													s4 = s4.replace("-", "_");
												String s5 = xmlpullparser.getAttributeValue(null, "name");
												boolean flag1 = Boolean.parseBoolean(xmlpullparser.getAttributeValue(null, "opened"));
												String s6 = file2.getName();
												cgplan = new CGPlan(Long.parseLong(s6.substring(1 + s6.indexOf("_"), s6.indexOf(".xml"))), f, k3, l3,
														i1, s3, s4, s5, flag1);

											} else if ("day".equals(xmlpullparser.getName())) {

												int j3 = Integer.parseInt(xmlpullparser.getAttributeValue(null, "day"));
												cgplanday = new CGPlan.CGPlanDay(j3);
												cgplan.cgPlanDayList.add(cgplanday);

											} else if ("section".equals(xmlpullparser.getName())) {

												boolean flag = Boolean.parseBoolean(xmlpullparser.getAttributeValue(null, "dinner"));
												int k2 = Integer.parseInt(xmlpullparser.getAttributeValue(null, "end"));
												int l2 = Integer.parseInt(xmlpullparser.getAttributeValue(null, "begin"));
												int i3 = Integer.parseInt(xmlpullparser.getAttributeValue(null, "section"));
												cgplansection = new CGPlan.CGPlanDay.CGPlanSection(i3, flag, k2, l2);
												cgplanday.cgPlanSectionList.add(cgplansection);

											} else if ("item".equals(xmlpullparser.getName())) {

												int l1;
												String s;
												int i2;
												int j2;
												l1 = Integer.parseInt(xmlpullparser.getAttributeValue(null, "day"));
												s = xmlpullparser.getAttributeValue(null, "comment");
												i2 = Integer.parseInt(xmlpullparser.getAttributeValue(null, "type"));
												j2 = Integer.parseInt(xmlpullparser.getAttributeValue(null, "id"));
												String s1 = xmlpullparser.getAttributeValue(null, "mapid");
												if (s1 != null) {
													cgplanitem = new CGPlan.CGPlanDay.CGPlanSection.CGPlanItem(l1, s, i2, j2, Integer.parseInt(s1),
															String.valueOf(i1));
												} else {
													cgplanitem = new CGPlan.CGPlanDay.CGPlanSection.CGPlanItem(l1, s, i2, j2, 0, String.valueOf(i1));
												}
												cgplanitem.setTimesAndName(sqlitedatabase);
												CGSGData.SGItem sgitem = CGData.getSGItem(cgdata, i2, j2);
												cgplanitem.sgItem = sgitem;
												if (cgplanitem.name == null) {
													cgplanitem.name = cgplanitem.sgItem.name;
												}
												cgplansection.cgPlanItemList.add(cgplanitem);

											}

										}
											break;
										case XmlPullParser.END_TAG:
											if ("day".equals(xmlpullparser.getName())) {
												for (Iterator iterator = cgplanday.cgPlanSectionList.iterator(); iterator.hasNext();) {
													Iterator iterator1 = ((CGPlan.CGPlanDay.CGPlanSection) iterator.next()).cgPlanItemList.iterator();
													while (iterator1.hasNext()) {
														CGPlan.CGPlanDay.CGPlanSection.CGPlanItem cgplanitem2 = (CGPlan.CGPlanDay.CGPlanSection.CGPlanItem) iterator1
																.next();
														cgplanday.sgItemList.add(cgplanitem2.sgItem);
													}
												}
											}
											break;

										}

										j1 = xmlpullparser.next();

									}

									if (j1 == XmlPullParser.END_DOCUMENT) {
										cgdata.CG_PLAN_DATA.CG_PLAN_LIST.add(cgplan);
										if (fileinputstream != null)
											fileinputstream.close();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

							} else {
								Log.e(CGData.TAG,
										(new StringBuilder()).append(file2.getAbsolutePath()).append(" \u4E0D\u662F\u6587\u4EF6\u8DEF\u5F84")
												.toString());
								
							}
							l++;
						}

					}
				}

				{
					// L4
					Comparator comparator = new Comparator() {

						public int compare(CGPlanData.CGPlan cgplan, CGPlanData.CGPlan cgplan1) {
							return CGUtil.compare(Long.valueOf(cgplan.currentTimeMillis), Long.valueOf(cgplan1.currentTimeMillis));
						}

						public int compare(Object obj, Object obj1) {
							return compare((CGPlanData.CGPlan) obj, (CGPlanData.CGPlan) obj1);
						}

					};
					Collections.sort(cgdata.CG_PLAN_DATA.CG_PLAN_LIST, comparator);
					// return
					// L2
					return cgdata.CG_PLAN_DATA;
				}

			} else {
				// L2
				return cgdata.CG_PLAN_DATA;
			}

		}

		public String toString() {
			StringBuffer stringbuffer = new StringBuffer("CGPlanData ");
			stringbuffer.append("\r\n");
			for (Iterator iterator = CG_PLAN_LIST.iterator(); iterator.hasNext(); stringbuffer.append(((CGPlan) iterator.next()).toString()).append(
					"\r\n"))
				;
			stringbuffer.append("\r\n");
			return stringbuffer.toString();
		}

		public List<CGPlan> CG_PLAN_LIST;
		public List<CGPlan.CGPlayType> INTEREST_TYPE_ARRAY;

		public CGPlanData() {
			INTEREST_TYPE_ARRAY = new ArrayList<CGPlan.CGPlayType>();
			CG_PLAN_LIST = new ArrayList<CGPlan>();
		}

		public static class CGPlan {

			public void checkCGPlanDayList(CGData cgdata, Context context) {
				File file;
				for (Iterator iterator = cgPlanDayList.iterator(); iterator.hasNext();) {
					Iterator iterator3 = ((CGPlanDay) iterator.next()).cgPlanSectionList.iterator();
					while (iterator3.hasNext()) {
						Iterator iterator4 = ((CGPlanDay.CGPlanSection) iterator3.next()).cgPlanItemList.iterator();
						while (iterator4.hasNext())
							((CGPlanDay.CGPlanSection.CGPlanItem) iterator4.next()).errorDescription = null;
					}
				}

				file = new File((new StringBuilder()).append(cgdata.CG_PLAN_MY_PATH).append(interest).append("_").append(currentTimeMillis)
						.toString());
				if (file.exists()) {
					FileInputStream fileinputstream;
					CGPlanDay cgplanday;
					CGPlanDay.CGPlanSection cgplansection;
					fileinputstream = null;
					cgplanday = null;
					cgplansection = null;
					try {
						FileInputStream fileinputstream1 = new FileInputStream(file);
						XmlPullParser xmlpullparser;
						int i;
						xmlpullparser = Xml.newPullParser();
						xmlpullparser.setInput(fileinputstream1, "utf-8");
						i = xmlpullparser.getEventType();
						while (i != XmlPullParser.END_DOCUMENT) {
							switch (i) {
							case XmlPullParser.START_TAG:
								if (!"plan".equals(xmlpullparser.getName()))
									if ("day".equals(xmlpullparser.getName())) {
										int i1 = Integer.parseInt(xmlpullparser.getAttributeValue(null, "day"));
										cgplanday = (CGPlanDay) cgPlanDayList.get(i1 - 1);
									} else if ("section".equals(xmlpullparser.getName())) {
										int l = Integer.parseInt(xmlpullparser.getAttributeValue(null, "section"));
										cgplansection = (CGPlanDay.CGPlanSection) cgplanday.cgPlanSectionList.get(l);
									} else if ("item".equals(xmlpullparser.getName())) {
										int j = Integer.parseInt(xmlpullparser.getAttributeValue(null, "type"));
										int k = Integer.parseInt(xmlpullparser.getAttributeValue(null, "id"));
										String s = xmlpullparser.getAttributeValue(null, "err");
										Iterator iterator2 = cgplansection.cgPlanItemList.iterator();
										while (iterator2.hasNext()) {
											CGPlanDay.CGPlanSection.CGPlanItem cgplanitem = (CGPlanDay.CGPlanSection.CGPlanItem) iterator2.next();
											if (cgplanitem.type == j && cgplanitem.id == k)
												cgplanitem.errorDescription = (new StringBuilder()).append(s).append(":2").toString();
										}
									}
								break;
							}
							i = xmlpullparser.next();
						}
						fileinputstream1.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					for (Iterator iterator1 = cgPlanDayList.iterator(); iterator1.hasNext(); ((CGPlanDay) iterator1.next()).checkCGPlanSectionList(
							cgdata, context))
						;
					return;
				}

			}

			public String toString() {
				StringBuffer stringbuffer = new StringBuffer("CGPlan ");
				stringbuffer.append("currentTimeMillis ").append(currentTimeMillis).append(",");
				stringbuffer.append("name ").append(name).append(",");
				stringbuffer.append("interest ").append(interest).append(",");
				stringbuffer.append("days ").append(days).append(",");
				stringbuffer.append("leave ").append(leave).append(",");
				stringbuffer.append("arrive ").append(arrive).append(",");
				stringbuffer.append("enddate ").append(enddate).append(",");
				stringbuffer.append("begindate ").append(begindate).append(",");
				stringbuffer.append("opened ").append(opened).append(",");
				stringbuffer.append("\r\n");
				for (Iterator iterator = cgPlanDayList.iterator(); iterator.hasNext(); stringbuffer.append(((CGPlanDay) iterator.next()).toString())
						.append("\r\n"))
					;
				stringbuffer.append("\r\n");
				return stringbuffer.toString();
			}

			public int arrive;
			public String begindate;
			public List<CGPlanDay> cgPlanDayList;
			public long currentTimeMillis;
			public float days;
			public String enddate;
			public int interest;
			public int leave;
			public String name;
			public boolean opened;

			public CGPlan(long l, float f, int i, int j, int k, String s, String s1, String s2, boolean flag) {
				if (s == null)
					s = "";
				if (s1 == null)
					s1 = "";
				if (l == 0L)
					l = System.currentTimeMillis();
				currentTimeMillis = l;
				days = f;
				leave = i;
				arrive = j;
				interest = k;
				enddate = s;
				begindate = s1;
				name = s2;
				opened = flag;
				cgPlanDayList = new ArrayList<CGPlanDay>();
			}

			public static class CGPlanDay {

				private int getDistanceTime(CGSGData.SGItem sgitem, CGSGData.SGItem sgitem1) {
					double d = TCUtil.getDistance(sgitem.lat, sgitem.lon, sgitem1.lat, sgitem1.lon);
					int i;
					if (d < 3000D)
						i = 0;
					else if (d < 5000D)
						i = 3600;
					else if (d < 10000D)
						i = 7200;
					else
						i = 1800 * ((int) (7200D + (3600D * (d - 10000D)) / 30000D) / 1800);
					return i;
				}

				public void checkCGPlanSectionList(CGData cgdata, Context context) {
					for (Iterator iterator = cgPlanSectionList.iterator(); iterator.hasNext();) {
						CGPlanSection cgplansection = (CGPlanSection) iterator.next();
						int i = 0;
						while (i < cgplansection.cgPlanItemList.size()) {
							if (i == 0) {
								((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).begin = cgplansection.begin;
								((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).end = (int) ((long) ((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList
										.get(i)).begin + ((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).spendTime);
								if (cgplansection.dinner && ((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).type != 2) {
									if (((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).errorDescription == null)
										((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).errorDescription = context
												.getString(0x7f080128);
								} else if (!cgplansection.dinner && cgplansection.no != 0
										&& ((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).type == 2
										&& ((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).errorDescription == null)
									((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).errorDescription = context.getString(0x7f080127);
							} else {
								CGSGData.SGItem sgitem = ((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i - 1)).sgItem;
								CGSGData.SGItem sgitem1 = ((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).sgItem;
								((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).begin = ((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList
										.get(i - 1)).end + getDistanceTime(sgitem, sgitem1);
								((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).end = (int) ((long) ((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList
										.get(i)).begin + ((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).spendTime);
								if (((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).begin > cgplansection.end)
									((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).errorDescription = context.getString(0x7f080126);
								else if (!cgplansection.dinner && ((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).type == 2) {
									if (((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).errorDescription == null)
										((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).errorDescription = context
												.getString(0x7f080127);
								} else if (cgplansection.dinner && ((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).type != 2
										&& ((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).errorDescription == null)
									((CGPlanSection.CGPlanItem) cgplansection.cgPlanItemList.get(i)).errorDescription = context.getString(0x7f080128);
							}
							i++;
						}
					}

				}

				public void setDefaultCGPlanSectionList() {
					int i = 0;
					while (i < 5) {
						switch (i) {
						case 0:
							this.cgPlanSectionList.add(new CGPlanSection(i, false, 43200, 21600));
							break;
						case 1:
							this.cgPlanSectionList.add(new CGPlanSection(i, true, 46800, 39600));
							break;
						case 2:
							this.cgPlanSectionList.add(new CGPlanSection(i, false, 64800, 43200));
							break;
						case 3:
							this.cgPlanSectionList.add(new CGPlanSection(i, true, 68400, 61200));
							break;
						case 4:
							this.cgPlanSectionList.add(new CGPlanSection(i, false, 86400, 64800));
							break;
						}
						i++;
					}

				}

				public String toString() {
					StringBuffer stringbuffer = new StringBuffer("CGPlanDay ");
					stringbuffer.append("noDay ").append(noDay).append("\r\n");
					for (Iterator iterator = cgPlanSectionList.iterator(); iterator.hasNext(); stringbuffer.append(
							((CGPlanSection) iterator.next()).toString()).append("\r\n"))
						;
					stringbuffer.append("\r\n");
					return stringbuffer.toString();
				}

				public List<CGPlanSection> cgPlanSectionList;
				public int noDay;
				public List<CGSGData.SGItem> sgItemList;

				public CGPlanDay() {
					this(1);
				}

				public CGPlanDay(int i) {
					noDay = i;
					cgPlanSectionList = new ArrayList();
					sgItemList = new ArrayList<CGSGData.SGItem>();
				}

				public static class CGPlanSection {

					public String toString() {
						StringBuffer stringbuffer = new StringBuffer("CGPlanSection ");
						stringbuffer.append("no ").append(no).append(",");
						stringbuffer.append("dinner ").append(dinner).append(",");
						stringbuffer.append("end ").append(end).append(",");
						stringbuffer.append("begin ").append(begin).append(",");
						stringbuffer.append("\r\n");
						for (Iterator iterator = cgPlanItemList.iterator(); iterator.hasNext(); stringbuffer.append(
								((CGPlanItem) iterator.next()).toString()).append("\r\n"))
							;
						stringbuffer.append("\r\n");
						return stringbuffer.toString();
					}

					public int begin;
					public List<CGPlanItem> cgPlanItemList;
					public boolean dinner;
					public int end;
					public int no;

					public CGPlanSection(int i, boolean flag, int j, int k) {
						no = i;
						dinner = flag;
						end = j;
						begin = k;
						cgPlanItemList = new ArrayList<CGPlanItem>();
					}

					public static class CGPlanItem {

						public void setTimesAndName(SQLiteDatabase sqlitedatabase) {
							String as[] = new String[3];
							as[0] = String.valueOf(interest);
							as[1] = String.valueOf(type);
							as[2] = String.valueOf(id);
							Cursor cursor = sqlitedatabase.rawQuery("select * from InterestMap where interestid = ? and type = ? and poiid = ?", as);
							if (cursor != null && cursor.getCount() > 0) {
								if (cursor.moveToFirst()) {
									mapid = cursor.getInt(cursor.getColumnIndex("mapid"));
									spendTime = cursor.getInt(cursor.getColumnIndex("spendtime"));
									endTime = cursor.getString(cursor.getColumnIndex("endtime"));
									beginTime = cursor.getString(cursor.getColumnIndex("begintime"));
									name = cursor.getString(cursor.getColumnIndex("name"));
								}
								cursor.close();
							}
						}

						public String toString() {
							StringBuffer stringbuffer = new StringBuffer("CGPlanItem ");
							stringbuffer.append("noDay ").append(noDay).append(",");
							stringbuffer.append("begin ").append(begin).append(",");
							stringbuffer.append("end ").append(end).append(",");
							stringbuffer.append("type ").append(type).append(",");
							stringbuffer.append("id ").append(id).append(",");
							stringbuffer.append("mapid ").append(mapid).append(",");
							stringbuffer.append("comment ").append(comment).append(",");
							return stringbuffer.toString();
						}

						public int begin;
						public String beginTime;
						public String comment;
						public int end;
						public String endTime;
						public String errorDescription;
						public int id;
						public String interest;
						public int mapid;
						public String name;
						public int noDay;
						public CGSGData.SGItem sgItem;
						public long spendTime;
						public int type;

						public CGPlanItem(int i, String s, int j, int k, int l, String s1) {
							noDay = i;
							if (s == null)
								s = "";
							comment = s;
							type = j;
							id = k;
							mapid = l;
							interest = s1;
							spendTime = 5400L;
						}
					}

				}

			}

			public static class CGPlayType {

				public int id;
				public String name;
				public String resourceName;

				public CGPlayType(String s, String s1, int i) {
					name = s;
					resourceName = s1;
					id = i;
				}
			}

		}

	}

	public static class CGRecommendData implements Serializable {

		public String chName;
		public List items;

		public CGRecommendData() {
		}

		public static class CGRecommendItem implements Serializable {

			public String content;
			public String icon;
			public List images;
			public String name;
			public String type;
			public String url;

			public CGRecommendItem() {
			}

			public static class Parser {

				public static CGRecommendItem parse(Context context, String s) {
					String s1;
					CGRecommendData.CGRecommendItem cgrecommenditem;
					s1 = null;
					cgrecommenditem = null;
					XmlPullParser xmlpullparser;
					int i;
					// CGRecommendData.CGRecommendItem cgrecommenditem1;
					try {
						InputStream inputstream = TCUtil
								.getFileInputStream(context, (new StringBuilder()).append(s).append("app/app.xml").toString());
						xmlpullparser = Xml.newPullParser();
						xmlpullparser.setInput(inputstream, "UTF-8");
						i = xmlpullparser.getEventType();
						// cgrecommenditem1 = null;
						while (i != XmlPullParser.END_DOCUMENT) {
							switch (i) {
							case XmlPullParser.START_TAG:
								s1 = xmlpullparser.getName();
								if ("app".equals(s1)) {
									cgrecommenditem = new CGRecommendData.CGRecommendItem();
									cgrecommenditem.images = new ArrayList();
								}
								break;
							case XmlPullParser.TEXT:
								if ("desc".equals(s1)) {

									cgrecommenditem.content = xmlpullparser.getText();

								} else if ("item".equals(s1)) {

									cgrecommenditem.images.add((new StringBuilder()).append(s).append("app/").append(xmlpullparser.getText())
											.toString());

								}
								break;

							}

							i = xmlpullparser.next();
						}

						inputstream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return cgrecommenditem;
				}

				public Parser() {
				}
			}

		}

		public static class RecommendDataHandler extends DefaultHandler {

			public void characters(char ac[], int i, int j) throws SAXException {
				super.characters(ac, i, j);
				String s = new String(ac, i, j);
				if (s.trim().length() > 0)
					characters = s;
				if (content != null)
					content.append(s);
			}

			public void endDocument() throws SAXException {
				super.endDocument();
			}

			public void endElement(String s, String s1, String s2) throws SAXException {
				super.endElement(s, s1, s2);
				tag = null;
				if ("recommend".equals(s1)) {// goto _L2; else goto _L1
					level = -1;
				} else if ("item".equals(s1))
					level = 1;
				else if ("array".equals(s1))
					level = 2;
				else if ("image".equals(s1)) {
					if (level == 5) {
						recommendItem.images.add((new StringBuilder()).append(recommendPath).append(characters).toString());
						level = 3;
					}
				} else if ("content".equals(s1))
					recommendItem.content = content.toString();

			}

			public void startDocument() throws SAXException {
				super.startDocument();
				tag = null;
				level = -1;
			}

			public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
				super.startElement(s, s1, s2, attributes);
				tag = s1;
				if ("recommend".equals(s1)) {

					level = 1;
					cgData.CG_RECOMMEND_DATA.items = new ArrayList();
				} else if ("item".equals(s1)) {
					level = 2;
					recommendItem = new CGRecommendData.CGRecommendItem();
					cgData.CG_RECOMMEND_DATA.items.add(recommendItem);
					recommendItem.images = new ArrayList();
					recommendItem.icon = (new StringBuilder()).append(recommendPath).append(attributes.getValue("icon")).toString();
					recommendItem.name = attributes.getValue("name");
					recommendItem.url = attributes.getValue("url");
				} else if ("content".equals(s1)) {
					level = 4;
					content = new StringBuffer();
				} else if ("array".equals(s1))
					level = 3;
				else if ("image".equals(s1) && level == 3)
					level = 5;

			}

			private static final int ARRAY_LEVEL = 3;
			private static final int CONTENT_LEVEL = 4;
			private static final int IDLE_LEVEL = -1;
			private static final int IMAGE_LEVEL = 5;
			private static final int ITEM_LEVEL = 2;
			private static final int RECOMMEND_LEVEL = 1;
			private CGData cgData;
			private String characters;
			private StringBuffer content;
			private int level;
			private CGRecommendData.CGRecommendItem recommendItem;
			private String recommendPath;
			private String tag;

			public RecommendDataHandler(CGData cgdata) {
				recommendPath = "file:///android_asset/recommend/";
				cgData = cgdata;
			}
		}

		public static class RecommendDataParaser {

			public static void parse(Context context, CGData cgdata) {
				parse(context, cgdata, TCUtil.getFileInputStream(context, "file:///android_asset/cg_recommend_data.xml"));
			}

			private static void parse(Context context, CGData cgdata, InputStream inputstream) {
				try {
					SAXParserFactory.newInstance().newSAXParser().parse(inputstream, new CGRecommendData.RecommendDataHandler(cgdata));
				} catch (ParserConfigurationException e) {
					Log.e(CGData.TAG, "parse(Context context, String type, InputStream inputStream) ", e);
				} catch (SAXException e) {
					Log.e(CGData.TAG, "parse(Context context, String type, InputStream inputStream) ", e);
				} catch (IOException e) {
					Log.e(CGData.TAG, "parse(Context context, String type, InputStream inputStream) ", e);
				}

			}

			public RecommendDataParaser() {
			}
		}

	}

	public static class CGRegion {

		public String name;
		public Map sgRegions;

		public CGRegion() {
		}
	}

	public static class CGSGData implements Serializable {

		public static void closeDatabase() {
			if (database != null) {
				database.close();
				database = null;
			}
		}

		public static String getSQL(String s) {
			String s1;
			if ("Site".equals(s))
				s1 = NO_PRICE_TYPE_SQL;
			else
				s1 = HAS_PRICE_TYPE_SQL;
			return s1.replace("[TYPE]", s);
		}

		public static SQLiteDatabase openDatabase(Context context, CGData cgdata) {
			if (database == null) {
				String s = (new StringBuilder()).append(TCUtil.getSDPath()).append(cgdata.CG_DATA_BASE_PATH).append("cityguide.sqlite").toString();
				if (!TCUtil.fileExists(s)) {
					if (cgdata.CG_PATH.startsWith("file:///android_asset/")) {
						TCUtil.makeDirs((new StringBuilder()).append(TCUtil.getSDPath()).append(cgdata.CG_DATA_BASE_PATH).toString());
						TCUtil.writeFile(
								TCUtil.getAssetInputStream(context, (new StringBuilder()).append(cgdata.CG_DATA_BASE_PATH).append("cityguide.sqlite")
										.toString()), s);
						database = SQLiteDatabase.openDatabase(s, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
					}
				} else {
					database = SQLiteDatabase.openDatabase(s, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
				}
			}
			return database;

		}

		public static CGSGData query(Context context, CGData cgdata, String s) {
			SGItem sgitem = null;
			String s1 = getSQL(s);
			Cursor cursor = database.rawQuery(s1, null);
			cursor.moveToFirst();
			CGSGData cgsgdata = null;
			if (cursor != null) {
				cgsgdata = new CGSGData();
				boolean flag = "Site".equals(s);
				cgsgdata.isSG = flag;
				cgsgdata.type = s;
				ArrayList arraylist = new ArrayList();
				cgsgdata.sgItems = arraylist;
				ArrayList arraylist1 = new ArrayList();
				cgsgdata.siteTypes = arraylist1;
				ArrayList arraylist2 = new ArrayList();
				cgsgdata.sortTypes = arraylist2;
				String as[] = context.getResources().getStringArray(0x7f050000);
				int i = as.length;
				if (cgsgdata.isSG)
					i -= 2;
				for (int j = 0; j < i; j++)
					cgsgdata.sortTypes.add(as[j]);

				if (cursor.getCount() > 0)
					do {
						int k = 0 + 1;
						int l = cursor.getInt(0);
						int i1 = k + 1;
						String s2 = cursor.getString(k);
						int j1 = i1 + 1;
						float f = cursor.getFloat(i1);
						int k1 = j1 + 1;
						float f1 = cursor.getFloat(j1);
						int l1 = k1 + 1;
						float f2 = cursor.getFloat(k1);
						int i2 = l1 + 1;
						String s3 = cursor.getString(l1);
						int j2 = i2 + 1;
						String s4 = cursor.getString(i2);
						int k2 = j2 + 1;
						int l2 = cursor.getInt(j2);
						int i3 = k2 + 1;
						boolean flag1;
						int j3;
						int k3;
						int l3;
						String s5;
						int i4;
						String s6;
						float f3;
						String s7;
						CGBrand.SGBrand sgbrand;
						if (cursor.getShort(k2) == 1)
							flag1 = true;
						else
							flag1 = false;
						j3 = i3 + 1;
						k3 = cursor.getInt(i3);
						l3 = j3 + 1;
						s5 = cursor.getString(j3);
						i4 = l3 + 1;
						s6 = cursor.getString(l3);
						f3 = -1F;
						s7 = null;
						if (!cgsgdata.isSG) {
							int i5 = i4 + 1;
							f3 = cursor.getFloat(i4);
							int _tmp = i5 + 1;
							s7 = cursor.getString(i5);
						}
						sgbrand = null;
						if (s6 != null && s6.length() > 0) {
							List list = cgdata.CG_BRAND.sgBrands;
							int k4 = 0;
							do {
								int l4 = list.size();
								if (k4 >= l4)
									break;
								CGBrand.SGBrand sgbrand1 = (CGBrand.SGBrand) list.get(k4);
								if (sgbrand1.logo.contains(s6))
									sgbrand = sgbrand1;
								k4++;
							} while (true);
						}
						if (!cgsgdata.siteTypes.contains(s4))
							cgsgdata.siteTypes.add(s4);
						if (sgitem != null && sgitem.id == l) {
							sgitem.siteType = (new StringBuilder()).append(sgitem.siteType).append(",").append(s4).toString();
						} else {
							String s8 = (new StringBuilder()).append(cgdata.CG_PATH).append(s.toLowerCase()).append("/").append(l).toString();
							sgitem = new SGItem(l, f1, f, s4, s2, s3, s5, f2, k3, l2, flag1, f3, s7, sgbrand, (new StringBuilder()).append(s8)
									.append("/icon.jpg").toString(), s, 0);
							if (cgsgdata.isSG) {
								sgitem.guideUrl = (new StringBuilder()).append(s8).append("/intro/guide.html").toString();
								sgitem.addressUrl = (new StringBuilder()).append(s8).append("/intro/introduce.html").toString();
							} else {
								sgitem.guideUrl = (new StringBuilder()).append(s8).append("/intro/introduce.html").toString();
								sgitem.addressUrl = (new StringBuilder()).append(s8).append("/intro/address.html").toString();
							}
							if (sgbrand != null) {
								String s9 = sgbrand.logo;
								if (TCData.USE_2X) {
									int j4 = s9.lastIndexOf(".");
									s9 = (new StringBuilder()).append(s9.substring(0, j4)).append("@2x").append(s9.substring(j4)).toString();
								}
								sgitem.logo = s9;
							} else {
								sgitem.logo = TCData.getPoiIcon(s);
							}
							if (sgitem.isMain) {
								if ((new File((new StringBuilder()).append(TCData.SG_ROOT).append(sgitem.sgId).append("/content.xml").toString()))
										.exists())
									try {
										FileInputStream fileinputstream = new FileInputStream((new StringBuilder()).append(TCData.SG_ROOT)
												.append(sgitem.sgId).append("/content.xml").toString());
										SAXParser saxparser = SAXParserFactory.newInstance().newSAXParser();
										GetSGContentTimeStampHandler getsgcontenttimestamphandler = new GetSGContentTimeStampHandler();
										saxparser.parse(fileinputstream, getsgcontenttimestamphandler);
										sgitem.timestamp = getsgcontenttimestamphandler.timestamp;
										fileinputstream.close();
										if (CGService.initTimeStamps != null
												&& CGService.initTimeStamps.get(Integer.valueOf(sgitem.sgId)) != null
												&& ((Integer) CGService.initTimeStamps.get(Integer.valueOf(sgitem.sgId))).intValue() > sgitem.timestamp)
											sgitem.timestamp = ((Integer) CGService.initTimeStamps.get(Integer.valueOf(sgitem.sgId))).intValue();
									} catch (FileNotFoundException filenotfoundexception) {
										filenotfoundexception.printStackTrace();
									} catch (ParserConfigurationException parserconfigurationexception) {
										parserconfigurationexception.printStackTrace();
									} catch (SAXException saxexception) {
										saxexception.printStackTrace();
									} catch (IOException ioexception) {
										ioexception.printStackTrace();
									}
								else
									sgitem.timestamp = 0;
								cgdata.CG_SITE_DATAS.add(sgitem);
							}
							cgsgdata.sgItems.add(sgitem);
						}
					} while (cursor.moveToNext());
				cursor.close();
			}
			return cgsgdata;
		}

		private static String HAS_PRICE_TYPE_SQL = "select [TYPE].[id] , [TYPE].[name] , [TYPE].[lo], [TYPE].[la] , [TYPE].[grade] , [TYPE].[flag] , [TYPE]Type.[name] , [TYPE].[guideid] , [TYPE].[mainguide] , [TYPE].[commentcount] , Region.[name] , [TYPE].[brandicon] , [TYPE].[price] , [TYPE].[phone] from [TYPE] , [TYPE]Type , [TYPE]TypeMap , Region where [TYPE]TypeMap.[[TYPE]] = [TYPE].[id] and [TYPE]TypeMap.[type] = [TYPE]Type.[id] and Region.[id] = [TYPE].[region] order by [TYPE].[grade] desc , [TYPE].[id] ";
		private static String NO_PRICE_TYPE_SQL = "select [TYPE].[id] , [TYPE].[name] , [TYPE].[lo], [TYPE].[la] , [TYPE].[grade] , [TYPE].[flag] , [TYPE]Type.[name] , [TYPE].[guideid] , [TYPE].[mainguide] , [TYPE].[commentcount] , Region.[name] , [TYPE].[brandicon] from [TYPE] , [TYPE]Type , [TYPE]TypeMap , Region where [TYPE]TypeMap.[[TYPE]] = [TYPE].[id] and [TYPE]TypeMap.[type] = [TYPE]Type.[id] and Region.[id] = [TYPE].[region] order by [TYPE].[grade] desc , [TYPE].[id] ";
		public static SQLiteDatabase database;
		public String chName;
		public boolean isSG;
		public List<SGItem> sgItems;
		public List siteTypes;
		public List sortTypes;
		public String type;

		public CGSGData() {
		}

		public CGSGData(CGSGData cgsgdata) {
			chName = cgsgdata.chName;
			isSG = cgsgdata.isSG;
			type = cgsgdata.type;
			sgItems = new ArrayList<SGItem>();
			SGItem sgitem;
			for (Iterator iterator = cgsgdata.sgItems.iterator(); iterator.hasNext(); sgItems.add(sgitem))
				sgitem = (SGItem) iterator.next();

			siteTypes = cgsgdata.siteTypes;
			sortTypes = cgsgdata.sortTypes;
		}

		public static class SGItem implements Serializable {

			public boolean isFavorite() {
				boolean flag1;
				if (current > 0)
					flag1 = true;
				else
					flag1 = false;
				return flag1;
			}

			public String addressUrl;
			public CGAlbumData albumData;
			public CGBrand.SGBrand brand;
			public int commentCount;
			public float commentGrade;
			public int current;
			public double distance;
			public String flag;
			public String guideUrl;
			public String icon;
			public int id;
			public boolean isMain;
			public double lat;
			public String logo;
			public double lon;
			public String name;
			public String phone;
			public float price;
			public String region;
			public int sgId;
			public String siteType;
			public int timestamp;
			public String type;

			public SGItem(int i, double d, double d1, String s, String s1, String s2, String s3, float f, int j, int k, boolean flag1, float f1,
					String s4, CGBrand.SGBrand sgbrand, String s5, String s6, int l) {
				distance = -1D;
				sgId = -1;
				id = i;
				lat = d;
				lon = d1;
				siteType = s;
				name = s1;
				flag = s2;
				region = s3;
				price = f1;
				commentGrade = f;
				commentCount = j;
				sgId = k;
				boolean flag2;
				if (flag1 && s2.startsWith("1"))
					flag2 = true;
				else
					flag2 = false;
				isMain = flag2;
				phone = s4;
				brand = sgbrand;
				icon = s5;
				type = s6;
				timestamp = l;
			}

			public static class AlbumHandler extends DefaultHandler {

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
					if ("conf".equals(s1)) {
						level = -1;
					} else if ("album".equals(s1))
						level = 1;
					else if ("img".equals(s1)) {
						level = 1;
						album.album.add((new StringBuilder()).append(cgData.CG_PATH).append(type.toLowerCase()).append("/").append(id)
								.append("/intro/icon/").append(characters).toString());
					}

				}

				public CGAlbumData getCGAlbum() {
					return album;
				}

				public void startDocument() throws SAXException {
					super.startDocument();
					tag = null;
					level = -1;
				}

				public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
					super.startElement(s, s1, s2, attributes);
					tag = s1;
					if ("conf".equals(s1)) {
						level = 1;
						album = new CGAlbumData();
					} else if ("album".equals(s1)) {
						level = 1;
						album.album = new ArrayList();
					} else if ("img".equals(s1))
						level = 2;

				}

				private static final int ALBUM_LEVEL = 1;
				private static final int CONF_LEVEL = 1;
				private static final int IDLE_LEVEL = -1;
				private static final int IMG_LEVEL = 2;
				private CGAlbumData album;
				private CGData cgData;
				private String characters;
				private int id;
				private int level;
				private String tag;
				private String type;

				public AlbumHandler(CGData cgdata, String s, int i) {
					type = s;
					id = i;
					cgData = cgdata;
				}
			}

			public static class AlbumParaser {

				public static CGAlbumData parse(Context context, CGData cgdata, String s, int i) {
					return parse(
							context,
							cgdata,
							s,
							i,
							TCUtil.getFileInputStream(context, (new StringBuilder()).append(cgdata.CG_PATH).append(s.toLowerCase()).append("/")
									.append(i).append("/conf.xml").toString()));
				}

				private static CGAlbumData parse(Context context, CGData cgdata, String s, int i, InputStream inputstream) {

					CGAlbumData cgalbumdata = null;
					CGAlbumData cgalbumdata1;
					try {
						SAXParser saxparser = SAXParserFactory.newInstance().newSAXParser();
						CGSGData.SGItem.AlbumHandler albumhandler = new CGSGData.SGItem.AlbumHandler(cgdata, s, i);
						saxparser.parse(inputstream, albumhandler);
						cgalbumdata1 = albumhandler.getCGAlbum();
						cgalbumdata = cgalbumdata1;
					} catch (ParserConfigurationException e) {
						Log.e(CGData.TAG, " CGAlbum parse(Context context, String type, InputStream inputStream) ", e);
					} catch (SAXException e) {
						Log.e(CGData.TAG, " CGAlbum parse(Context context, String type, InputStream inputStream) ", e);
					} catch (IOException e) {
						Log.e(CGData.TAG, " CGAlbum parse(Context context, String type, InputStream inputStream) ", e);
					}

					return cgalbumdata;

				}

				public AlbumParaser() {
				}
			}

		}

	}

	public static class InfoData implements Serializable {

		public String chName;
		public List<InfoItem> infoItems;
		public String modified;
		public String name;
		public String type;

		public InfoData(String s) {
			infoItems = new ArrayList<InfoItem>();
			type = s;
		}

		public static class InfoHandler extends DefaultHandler {

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
				if (!"city".equals(s1)) {
					if ("item".equals(s1))
						level = 1;
					else if ("img".equals(s1) && level == 3)
						cgData.ALBUM_DATA.album.add((new StringBuilder()).append(cgData.CG_PATH).append(characters).toString());
				}

			}

			public InfoData getInfoData() {
				return infoData;
			}

			public void startDocument() throws SAXException {
				super.startDocument();
				tag = null;
				level = -1;
			}

			public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
				super.startElement(s, s1, s2, attributes);
				tag = s1;
				if ("city".equals(s1)) {
					level = 1;
					cgData.CG_APPLICATION = (new StringBuilder()).append("android_CG_").append(cgData.CG_ID).toString();
					infoData = new InfoData(type);
					infoData.name = attributes.getValue("name");
				} else if ("gallery".equals(s1)) {
					level = 1;
					infoData = new InfoData(type);
					infoData.modified = attributes.getValue("modified");
				} else if ("item".equals(s1)) {
					level = 2;
					InfoData.InfoItem infoitem = new InfoData.InfoItem();
					infoitem.name = attributes.getValue("name");
					infoitem.type = attributes.getValue("type");
					String s3 = attributes.getValue("url");
					String s4;
					String s5;
					String s6;
					String s7;
					if (s3 != null && s3.length() > 0)
						if (s3.endsWith(".html"))
							infoitem.url = (new StringBuilder()).append(cgData.CG_PATH).append(s3).toString();
						else
							infoitem.url = s3;
					if ("album".equals(infoitem.type)) {
						cgData.ALBUM_DATA = new CGAlbumData();
						cgData.ALBUM_DATA.name = infoitem.name;
						cgData.ALBUM_DATA.album = new ArrayList();
					}
					s4 = attributes.getValue("icon");
					if (s4 != null && s4.length() > 0)
						infoitem.icon = (new StringBuilder()).append(cgData.CG_PATH).append(type.toLowerCase()).append("/").append(s4).toString();
					s5 = attributes.getValue("image");
					if (s5 != null && s5.length() > 0)
						if (cgData.CG_PATH.startsWith("file:///android_asset/"))
							infoitem.imageUrl = s5;
						else
							infoitem.imageUrl = (new StringBuilder()).append(cgData.CG_PATH).append(s5).toString();
					s6 = attributes.getValue("id");
					if (s6 != null && s6.length() > 0)
						infoitem.id = Integer.parseInt(s6);
					s7 = attributes.getValue("image_modified");
					if (s7 != null && s7.length() > 0)
						infoitem.modified = Integer.parseInt(s7);
					infoitem.content = attributes.getValue("content");
					infoData.infoItems.add(infoitem);
				} else if ("img".equals(s1) && level == 2)
					level = 3;

			}

			private static final int CITY_LEVEL = 1;
			private static final int GALLERY_LEVEL = 1;
			private static final int IDLE_LEVEL = -1;
			private static final int IMAGE_LEVEL = 3;
			private static final int ITEM_LEVEL = 2;
			private CGData cgData;
			private String characters;
			private InfoData infoData;
			private int level;
			private String tag;
			private String type;

			public InfoHandler(CGData cgdata, String s) {
				type = s;
				cgData = cgdata;
			}
		}

		public static class InfoItem implements Serializable {

			public String content;
			public String icon;
			public int id;
			public String imageUrl;
			public int index;
			public int modified;
			public String name;
			public String type;
			public String url;

			public InfoItem() {
			}
		}

		public static class InfoParaser {

			public static InfoData parse(Context context, CGData cgdata, String s) {
				return parse(
						context,
						cgdata,
						s,
						TCUtil.getFileInputStream(context, (new StringBuilder()).append(cgdata.CG_PATH).append(s.toLowerCase()).append("/").append(s)
								.append(".xml").toString()));
			}

			private static InfoData parse(Context context, CGData cgdata, String s, InputStream inputstream) {

				try {
					SAXParser saxparser = SAXParserFactory.newInstance().newSAXParser();
					InfoData.InfoHandler infohandler = new InfoData.InfoHandler(cgdata, s);
					saxparser.parse(inputstream, infohandler);
					return infohandler.getInfoData();

				} catch (ParserConfigurationException e) {
					Log.e(CGData.TAG, " InfoData parse(Context context, String type, InputStream inputStream) ", e);

				} catch (SAXException e) {
					Log.e(CGData.TAG, " InfoData parse(Context context, String type, InputStream inputStream) ", e);
				} catch (IOException e) {
					Log.e(CGData.TAG, " InfoData parse(Context context, String type, InputStream inputStream) ", e);
				}
				return null;

			}

			public InfoParaser() {
			}
		}

	}

	public CGData() {
		CG_WGS84_USED = false;
	}

	public static CGSGData getCGFavroiteSGData(CGData cgdata, String s) {
		CGSGData cgsgdata = new CGSGData();
		cgsgdata.type = s;
		cgsgdata.chName = cgdata.CG_FAVORITE_DATA.chName;
		cgsgdata.sgItems = new ArrayList();
		cgsgdata.sortTypes = cgdata.CG_FAVORITE_DATA.sortTypes;
		cgsgdata.siteTypes = cgdata.CG_FAVORITE_DATA.siteTypes;
		for (Iterator iterator = cgdata.CG_SG_DATAS.iterator(); iterator.hasNext();) {
			Iterator iterator1 = ((CGSGData) iterator.next()).sgItems.iterator();
			while (iterator1.hasNext()) {
				CGSGData.SGItem sgitem = (CGSGData.SGItem) iterator1.next();
				if (sgitem.current > 0)
					cgsgdata.sgItems.add(sgitem);
			}
		}

		return cgsgdata;
	}

	public static InfoData getCGInfoData(CGData cgdata, String s) {
		for (InfoData infodata : cgdata.CG_INFO_DATAS) {
			if (infodata.type.equals(s))
				return infodata;
		}
		return null;
	}

	public static CGSGData getCGSGData(CGData cgdata, String s) {
		if ("Favorite".equals(s)) {
			return getCGFavroiteSGData(cgdata, s);
		} else {
			for (CGSGData cgsgdata : cgdata.CG_SG_DATAS) {
				if (cgsgdata.type.equalsIgnoreCase(s))
					return cgsgdata;
			}
		}
		return null;

	}

	public static CGSGData.SGItem getSGItem(CGData cgdata, int i, int j) {

		String s = "";
		switch (i) {
		case 1:
			return getSGItem(cgdata, "site", j);
		case 2:
			return getSGItem(cgdata, "restaurant", j);
		case 3:
			return getSGItem(cgdata, "hotel", j);
		case 4:
			return getSGItem(cgdata, "shopping", j);
		case 5:
			return getSGItem(cgdata, "fun", j);
		default:
			Log.e(TAG, (new StringBuilder()).append("error :").append(i).toString());
			return getSGItem(cgdata, "", j);
		}

	}

	public static CGSGData.SGItem getSGItem(CGData cgdata, String s, int i) {

		for (CGSGData cgsgdata : cgdata.CG_SG_DATAS) {
			if (cgsgdata.type.equalsIgnoreCase(s)) {
				for (CGSGData.SGItem sgitem : cgsgdata.sgItems) {
					if (sgitem.id == i)
						return sgitem;
				}
			}
		}
		return null;
	}

	public void setRateData(Context context) {
		CG_RATE_CODE = null;
		InputStream inputstream = TCUtil.getAssetInputStream(context, "rate_data.xml");
		if (inputstream != null) {
			XmlPullParser xmlpullparser = Xml.newPullParser();
			try {
				xmlpullparser.setInput(inputstream, "UTF-8");
				for (; xmlpullparser.getEventType() != 1; xmlpullparser.next())
					if (xmlpullparser.getEventType() == 2) {
						if ("rate_code".equals(xmlpullparser.getName()))
							CG_RATE_CODE = xmlpullparser.nextText();
						if ("from".equals(xmlpullparser.getName()))
							CG_RATE_FROM = xmlpullparser.nextText();
						if ("to".equals(xmlpullparser.getName()))
							CG_RATE_TO = xmlpullparser.nextText();
						if ("rate".equals(xmlpullparser.getName()))
							CG_RATE = Float.parseFloat(xmlpullparser.nextText());
						if ("rate_time".equals(xmlpullparser.getName()))
							CG_RATE_TIME = xmlpullparser.nextText();
					}

			} catch (XmlPullParserException xmlpullparserexception) {
				xmlpullparserexception.printStackTrace();
			} catch (IOException ioexception) {
				ioexception.printStackTrace();
			}
		}
	}

	public static String CG_COMMUNITY_CODE;
	public static String CG_FLURRY;
	private static final String TAG = CGData.class.getSimpleName();
	public CGAlbumData ALBUM_DATA;
	public String CG_APPLICATION;
	public CGBrand CG_BRAND;
	public String CG_CNAME;
	public String CG_COVER_PATH;
	public String CG_DATA_BASE_PATH;
	public CGFavoriteData CG_FAVORITE_DATA;
	public int CG_ID;
	public CGInfoData CG_INFO_DATA;
	public List<InfoData> CG_INFO_DATAS;
	public double CG_LAT;
	public double CG_LON;
	public int CG_MAP_LEVEL;
	public CGMoreData CG_MORE_DATA;
	public String CG_NAME;
	public String CG_PATH;
	public String CG_PLAN_ARRAY[];
	public CGPlanData CG_PLAN_DATA;
	public String CG_PLAN_MODEL_PATH;
	public String CG_PLAN_MY_PATH;
	public String CG_PLAN_PATH;
	public float CG_RATE;
	public String CG_RATE_CODE;
	public String CG_RATE_FROM;
	public String CG_RATE_TIME;
	public String CG_RATE_TO;
	public CGRecommendData CG_RECOMMEND_DATA;
	public CGRegion CG_REGION;
	public List<CGSGData> CG_SG_DATAS;
	public List<CGData.CGSGData.SGItem> CG_SITE_DATAS;
	public String CG_VERSION;
	public String CG_WEATHER;
	public boolean CG_WGS84_USED;
	private HashMap<Integer, Integer> initHashMap;

}
