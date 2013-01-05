//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.mall;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tc.TCData;
import com.tc.TCUtil;

public class MallData {
	public static class MallAlbumData extends com.tc.TCData.TCAlbumData {

		public MallAlbumData() {
		}
	}

	public static class MallFavoriteData implements Serializable {

		public static void close(Context context) {
			DBOperator.getInstance(context);
			DBOperator.close();
		}

		public static void delete(Context context, int i, int j) {
			DBOperator.getInstance(context).deleteItem(i, j);
		}

		public static void insert(Context context, int i, int j, int k) {
			DBOperator.getInstance(context).insertItem(i, j, k);
		}

		public static void query(Context context) {
			DBOperator.getInstance(context).query();
		}

		public static final String CURRENT = "current";
		public static final int DB_VERSION = 1;
		public static final String ID = "id";
		public static List<MallData.MallPOIData.POI> POIS;
		public static final String TABLE_NAME = "favorite";
		public static final String TYPE_ID = "type_id";
		public String chName;
		public List siteTypes;
		public List sortTypes;
		public String tableName;

		public MallFavoriteData() {
		}

		public MallFavoriteData(MallFavoriteData mallfavoritedata) {
			tableName = mallfavoritedata.tableName;
			chName = mallfavoritedata.chName;
			sortTypes = mallfavoritedata.sortTypes;
			siteTypes = mallfavoritedata.siteTypes;
		}

		private static class DBHelper extends SQLiteOpenHelper {

			public void execSQL(String s, Object aobj[]) {
				SQLiteDatabase sqlitedatabase = getWritableDatabase();
				sqlitedatabase.execSQL(s, aobj);
				sqlitedatabase.close();
			}

			public void onCreate(SQLiteDatabase sqlitedatabase) {
				sqlitedatabase.execSQL("CREATE TABLE favorite ( id INTEGER NOT NULL , type_id INTEGER NOT NULL , current INTEGER NOT NULL );");
			}

			public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j) {
				sqlitedatabase.execSQL("DROP TABLE IF EXISTS favorite");
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

			public DBHelper(Context context) {
				super(context, (new StringBuilder()).append(MallData.MALL_APPLICATION).append(".sqlite").toString(), null, 1);
			}
		}

		private static class DBOperator {

			public static void close() {
				if (instance != null)
					instance = null;
			}

			private MallPOIData.POI cursorToData(Cursor cursor) {
				int i = cursor.getInt(0);
				int j = cursor.getInt(1);
				int k = cursor.getInt(2);
				MallPOIData.POI poi = MallData.getPOI(j, i);
				poi.current = k;
				return poi;
			}

			public static MallFavoriteData.DBOperator getInstance(Context context) {
				if (instance == null)
					instance = new MallFavoriteData.DBOperator(context);
				return instance;
			}

			public void deleteItem(int i, int j) {
				Object aobj[] = new Object[2];
				aobj[0] = Integer.valueOf(i);
				aobj[1] = Integer.valueOf(j);
				dbHelper.execSQL("delete from favorite where type_id = ? and id = ? ", aobj);
			}

			public void insertItem(int i, int j, int k) {
				Object aobj[] = new Object[3];
				aobj[0] = Integer.valueOf(i);
				aobj[1] = Integer.valueOf(j);
				aobj[2] = Integer.valueOf(k);
				dbHelper.execSQL("insert into favorite ( type_id , id , current ) values ( ? , ? , ? ) ", aobj);
			}

			public void query() {
				Cursor cursor = dbHelper.query("select * from favorite", null);
				if (cursor != null) {
					do {
						MallPOIData.POI poi = cursorToData(cursor);
						MallFavoriteData.POIS.add(poi);
					} while (cursor.moveToNext());
					cursor.close();
				}
			}

			private static MallFavoriteData.DBOperator instance;
			private MallFavoriteData.DBHelper dbHelper;

			private DBOperator(Context context) {
				dbHelper = new MallFavoriteData.DBHelper(context);
			}
		}

		public static class MallFavoriteItem implements Serializable {

			public int current;
			public int id;
			public int typeId;

			public MallFavoriteItem(int i, int j, int k) {
				typeId = j;
				current = k;
				id = i;
			}
		}

	}

	public static class MallFloorData {

		public static Floor getFloor(int i) {
			for (Floor floor : MallData.MALL_FLOOR_DATA.FLOORS) {
				if (i == floor.index) {
					return floor;
				}
			}
			return null;

		}

		public static void getMallFloorData(SQLiteDatabase sqlitedatabase) {
			Cursor cursor = sqlitedatabase
					.rawQuery(
							"select MallFloor.[id] , MallFloor.[name] , MallFloor.[orderIndex] , MallFloor.[mapWidth] , MallFloor.[mapHeight] from MallFloor ",
							null);
			if (cursor != null && cursor.moveToFirst()) {
				MallData.MALL_FLOOR_DATA.FLOORS = new ArrayList<Floor>();
				do {
					Floor floor = new Floor();
					int i = 0 + 1;
					floor.id = cursor.getInt(0);
					int j = i + 1;
					floor.name = cursor.getString(i);
					int k = j + 1;
					floor.index = cursor.getInt(j);
					int l = k + 1;
					floor.width = cursor.getInt(k);
					int _tmp = l + 1;
					floor.height = cursor.getInt(l);
					MallData.MALL_FLOOR_DATA.FLOORS.add(floor);
				} while (cursor.moveToNext());
				cursor.close();
			}
		}

		public List<Floor> FLOORS;

		public MallFloorData() {
		}

		public static class Floor {

			public int height;
			public int id;
			public int index;
			public String name;
			public int width;

			public Floor() {
			}
		}

	}

	public static class MallMoreData {

		public List MORE_ITEMS;
		public String chName;

		public MallMoreData() {
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
				if ("site".equals(s1))
					level = -1;
				else if ("collection".equals(s1))
					level = 1;
				else if ("more".equals(s1))
					level = 1;
				else if ("item".equals(s1)) {
					if (level == 2) {
						String s3 = characters;
						moreItem.url = (new StringBuilder()).append(MallData.MALL_PATH).append(s3).toString();
						if (moreItem.icon == null || moreItem.icon.length() == 0) {
							String s4 = s3.substring(1 + s3.lastIndexOf("/"), s3.lastIndexOf(".html"));
							moreItem.icon = TCData.getListIcon(s4);
						} else {
							moreItem.icon = (new StringBuilder()).append(MallData.MALL_PATH).append(moreItem.icon).toString();
						}
					}
				} else if ("address".equals(s1)) {
					if (level == 3)
						MallData.MALL_TRAFFIC_DATA.address = characters;
				} else if ("subway".equals(s1)) {
					if (level == 3)
						MallData.MALL_TRAFFIC_DATA.subway = characters;
				} else if ("bus".equals(s1)) {
					if (level == 3)
						MallData.MALL_TRAFFIC_DATA.bus = characters;
				} else if ("latitude".equals(s1)) {
					if (level == 3)
						MallData.MALL_TRAFFIC_DATA.lat = Double.parseDouble(characters);
				} else if ("longitude".equals(s1)) {
					if (level == 3)
						MallData.MALL_TRAFFIC_DATA.lon = Double.parseDouble(characters);
				} else if ("taxi".equals(s1)) {
					if (level == 3) {
						MallData.MALL_TRAFFIC_DATA.siteName = MallData.MALL_NAME;
						MallData.MALL_TRAFFIC_DATA.taxi.url = (new StringBuilder()).append(MallData.MALL_PATH).append(characters).toString();
					}
				} else if ("taxidest".equals(s1)) {
					if (level == 3)
						MallData.MALL_TRAFFIC_DATA.taxi.destination = characters;
				} else if ("taxitel".equals(s1) && level == 3)
					MallData.MALL_TRAFFIC_DATA.taxi.telephone = characters;

			}

			public void startDocument() throws SAXException {
				super.startDocument();
				tag = null;
				level = -1;
			}

			public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
				super.startElement(s, s1, s2, attributes);
				tag = s1;
				if ("site".equals(s1)) {
					level = 1;
					MallData.MALL_MORE_DATA = new MallMoreData();
					MallData.MALL_MORE_DATA.MORE_ITEMS = new ArrayList();
					MallData.MALL_NAME = attributes.getValue("name");
					MallData.MALL_HAS_AUDIO = "YES".equals(attributes.getValue("audio"));
					MallData.MALL_HAS_LOCATION = "YES".equals(attributes.getValue("location"));
				} else if ("collection".equals(s1))
					level = 2;
				else if ("more".equals(s1))
					level = 4;
				else if ("item".equals(s1)) {
					if (level == 2) {
						moreItem = new MallMoreData.MoreItem();
						MallData.MALL_MORE_DATA.MORE_ITEMS.add(moreItem);
						moreItem.name = attributes.getValue("name");
						moreItem.description = attributes.getValue("description");
						moreItem.icon = attributes.getValue("icon");
					}
				} else if ("transport".equals(s1))
					level = 3;

			}

			private static final int COLLECTION_LEVEL = 2;
			private static final int IDLE_LEVEL = -1;
			private static final int MORE_LEVEL = 4;
			private static final int SITE_LEVEL = 1;
			private static final int TRANSPORT_LEVEL = 3;
			private String characters;
			private int level;
			MallMoreData.MoreItem moreItem;
			private String tag;

			public MoreDataHandler() {
			}
		}

		public static class MoreDataParaser {

			public static void parse(Context context) {
				parse(context, TCUtil.getFileInputStream(context, (new StringBuilder()).append(MallData.MALL_PATH).append("content.xml").toString()));
			}

			private static void parse(Context context, InputStream inputstream) {
				MallMoreData.MoreDataHandler moredatahandler = new MallMoreData.MoreDataHandler();
				try {
					SAXParserFactory.newInstance().newSAXParser().parse(inputstream, moredatahandler);
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return;

			}

			public MoreDataParaser() {
			}
		}

		public static class MoreItem {

			public String description;
			public String icon;
			public String name;
			public String url;

			public MoreItem() {
			}
		}

	}

	public static class MallPOIData {

		public static void getMallPOIData(Context context, SQLiteDatabase sqlitedatabase) {
			Cursor cursor = sqlitedatabase
					.rawQuery(
							"select MallPOI.[id] , MallPOI.[name] , MallPOI.[grade] , MallPOI.[commentcount] , MallPOI.[phonenumber] , MallPOIFloor.[x] , MallPOIFloor.[y] , MallPOIFloor.[floor] , MallPOIFloor.[number] , MallPOITypeMap.[typeid] from MallPOI , MallPOIFloor , MallPOITypeMap where MallPOI.[id] = MallPOIFloor.[pointid] and MallPOI.[id] = MallPOITypeMap.[pointid] order by MallPOITypeMap.[typeid] , MallPOI.[grade] desc ",
							null);
			MallPOIData mallpoidata = null;
			int i = -1;
			if (cursor != null && cursor.moveToFirst()) {
				MallData.MALL_POI_DATAS = new ArrayList();
				do {
					POI poi = new POI();
					int j = 0 + 1;
					poi.id = cursor.getInt(0);
					int k = j + 1;
					poi.name = cursor.getString(j);
					int l = k + 1;
					poi.commentGrade = cursor.getFloat(k);
					int i1 = l + 1;
					poi.commentCount = cursor.getInt(l);
					int j1 = i1 + 1;
					poi.telephone = cursor.getString(i1);
					int k1 = j1 + 1;
					poi.x = cursor.getInt(j1);
					int l1 = k1 + 1;
					poi.y = cursor.getInt(k1);
					int i2 = l1 + 1;
					poi.floorIndex = cursor.getInt(l1);
					int j2 = i2 + 1;
					poi.number = cursor.getString(i2);
					int _tmp = j2 + 1;
					poi.typeId = cursor.getInt(j2);
					if (i != poi.typeId) {
						i = poi.typeId;
						mallpoidata = new MallPOIData();
						mallpoidata.POIS = new ArrayList();
						mallpoidata.typeId = poi.typeId;
						MallData.MALL_POI_DATAS.add(mallpoidata);
					}
					POI.POIDataParaser.parse(context, poi);
					poi.listIcon = (String) poi.albumData.album.get(0);
					poi.mapIcon = MallData.getMapIcon(poi.typeId);
					mallpoidata.POIS.add(poi);
				} while (cursor.moveToNext());
				cursor.close();
			}
		}

		public List<POI> POIS;
		public int typeId;

		public MallPOIData() {
		}

		public MallPOIData(MallPOIData mallpoidata) {
			typeId = mallpoidata.typeId;
			POIS = new ArrayList();
			POI poi;
			for (Iterator iterator = mallpoidata.POIS.iterator(); iterator.hasNext(); POIS.add(poi))
				poi = (POI) iterator.next();

		}

		public static class POI {

			public boolean isFavorite() {
				boolean flag1;
				if (current > 0)
					flag1 = true;
				else
					flag1 = false;
				return flag1;
			}

			public MallAlbumData albumData;
			public int commentCount;
			public float commentGrade;
			public int current;
			public String flag;
			public int floorIndex;
			public int id;
			public String listIcon;
			public String mapIcon;
			public String name;
			public String number;
			public String telephone;
			public int typeId;
			public String url;
			public int x;
			public int y;

			public POI() {
			}

			public static class POIDataHandler extends DefaultHandler {

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
					if ("point".equals(s1)) {
						level = -1;
					} else if (!"id".equals(s1) && !"album".equals(s1))
						if ("img".equals(s1)) {
							if (level == 4) {
								level = 3;
								poi.albumData.album.add((new StringBuilder()).append(MallData.MALL_PATH).append(characters).toString());
							}
						} else if ("url".equals(s1) && level == 5)
							poi.url = (new StringBuilder()).append(MallData.MALL_PATH).append(characters).toString();

				}

				public void startDocument() throws SAXException {
					super.startDocument();
					tag = null;
					level = -1;
				}

				public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
					super.startElement(s, s1, s2, attributes);
					tag = s1;
					if ("point".equals(s1)) {
						level = 1;
					} else if ("id".equals(s1))
						level = 2;
					else if ("album".equals(s1)) {
						level = 3;
						poi.albumData = new MallAlbumData();
						poi.albumData.name = poi.name;
						poi.albumData.album = new ArrayList();
					} else if ("img".equals(s1)) {
						if (level == 3)
							level = 4;
					} else if ("url".equals(s1))
						level = 5;

				}

				private static final int ALBUM_LEVEL = 3;
				private static final int IDLE_LEVEL = -1;
				private static final int ID_LEVEL = 2;
				private static final int IMG_LEVEL = 4;
				private static final int POINT_LEVEL = 1;
				private static final int URL_LEVEL = 5;
				private String characters;
				private int level;
				private MallPOIData.POI poi;
				private String tag;

				public POIDataHandler(MallPOIData.POI poi1) {
					poi = poi1;
				}
			}

			public static class POIDataParaser {

				public static void parse(Context context, MallPOIData.POI poi) {
					parse(context,
							TCUtil.getFileInputStream(context, (new StringBuilder()).append(MallData.MALL_PATH).append("poi/point").append(poi.id)
									.append(".xml").toString()), poi);
				}

				private static void parse(Context context, InputStream inputstream, MallPOIData.POI poi) {
					MallPOIData.POI.POIDataHandler poidatahandler = new MallPOIData.POI.POIDataHandler(poi);
					try {
						SAXParserFactory.newInstance().newSAXParser().parse(inputstream, poidatahandler);
					} catch (SAXException e) {
						Log.e(MallData.TAG, "static void parse(Context context, InputStream inputStream, POI poi)", e);
					} catch (IOException e) {
						Log.e(MallData.TAG, "static void parse(Context context, InputStream inputStream, POI poi)", e);
					} catch (ParserConfigurationException e) {
						Log.e(MallData.TAG, "static void parse(Context context, InputStream inputStream, POI poi)", e);
					}

					return;

				}

				public POIDataParaser() {
				}
			}

		}

	}

	public static class MallTrafficData extends com.tc.TCData.TCSGTrafficData {

		public MallTrafficData() {
		}
	}

	public static class MallTypeData {

		public static void getMallTypeData(SQLiteDatabase sqlitedatabase) {
			Cursor cursor = sqlitedatabase
					.rawQuery(
							"select MallPOIType.[id] , MallPOIType.[name] , MallPOIType.[orderIndex] , MallPOIType.[icon] , MallPOIType.[mapicon] from MallPOIType ",
							null);
			if (cursor != null && cursor.moveToFirst()) {
				MallData.MALL_TYPE_DATA.TYPES = new ArrayList();
				do {
					Type type = new Type();
					int i = 0 + 1;
					type.id = cursor.getInt(0);
					int j = i + 1;
					type.name = cursor.getString(i);
					int k = j + 1;
					type.index = cursor.getInt(j);
					StringBuilder stringbuilder = (new StringBuilder()).append(MallData.MALL_PATH).append("typeicons/");
					int l = k + 1;
					type.listIcon = stringbuilder.append(cursor.getString(k)).toString();
					StringBuilder stringbuilder1 = (new StringBuilder()).append(MallData.MALL_PATH).append("typeicons/");
					int _tmp = l + 1;
					type.poiIcon = stringbuilder1.append(cursor.getString(l)).toString();
					if (TCData.USE_2X) {
						type.listIcon = (new StringBuilder()).append(type.listIcon.substring(0, type.listIcon.lastIndexOf(".png"))).append("@2x.png")
								.toString();
						type.poiIcon = (new StringBuilder()).append(type.poiIcon.substring(0, type.poiIcon.lastIndexOf(".png"))).append("@2x.png")
								.toString();
					}
					MallData.MALL_TYPE_DATA.TYPES.add(type);
				} while (cursor.moveToNext());
				cursor.close();
			}
		}

		public List<Type> TYPES;

		public MallTypeData() {
		}

		public static class Type {

			public int id;
			public int index;
			public String listIcon;
			public String name;
			public String poiIcon;

			public Type() {
			}
		}
	}

	public MallData() {
	}

	public static int getFloorIndex(String s) {
		for (MallFloorData.Floor floor : MALL_FLOOR_DATA.FLOORS) {
			if (floor.name.equals(s))
				return floor.index;
		}
		return 0x7fffffff;

	}

	public static String getFloorName(int i) {
		for (MallFloorData.Floor floor : MALL_FLOOR_DATA.FLOORS) {
			if (floor.index == i)
				return floor.name;
		}
		return null;

	}

	public static MallPOIData getMallPOIData(int i) {
		for (MallPOIData mallpoidata : MALL_POI_DATAS) {
			if (mallpoidata.typeId == i)
				return mallpoidata;
		}
		return null;

	}

	public static String getMapIcon(int i) {
		for (MallTypeData.Type type : MALL_TYPE_DATA.TYPES) {
			if (type.id == i)
				return type.poiIcon;
		}
		return null;

	}

	public static MallPOIData.POI getPOI(int i, int j) {

		for (MallPOIData mallpoidata : MALL_POI_DATAS) {
			if (mallpoidata.typeId == i) {
				for (MallPOIData.POI poi : mallpoidata.POIS) {
					if (poi.id == j)
						return poi;
				}
			}

		}
		return null;
	}

	public static String getTypeIcon(String s) {
		String s1;
		if (TCData.USE_2X)
			s1 = (new StringBuilder()).append(MALL_PATH).append("typeicons/mall_icon_").append(s).append("@2x.png").toString();
		else
			s1 = (new StringBuilder()).append(MALL_PATH).append("typeicons/mall_icon_").append(s).append(".png").toString();
		return s1;
	}

	public static int getTypeId(String s) {

		for (MallTypeData.Type type : MALL_TYPE_DATA.TYPES) {
			if (type.name.equals(s)) {
				return type.id;
			}
		}
		return -1;

	}

	public static String MALL_APPLICATION;
	public static String MALL_COVER_PATH;
	public static String MALL_DATA_BASE_PATH;
	public static MallFavoriteData MALL_FAVORITE_DATA = new MallFavoriteData();
	public static MallFloorData MALL_FLOOR_DATA = new MallFloorData();
	public static String MALL_FLURRY;
	public static boolean MALL_HAS_AUDIO;
	public static boolean MALL_HAS_LOCATION;
	public static int MALL_ID;
	public static MallMoreData MALL_MORE_DATA;
	public static String MALL_NAME;
	public static String MALL_PATH;
	public static List<MallPOIData> MALL_POI_DATAS;
	public static MallTrafficData MALL_TRAFFIC_DATA = new MallTrafficData();
	public static MallTypeData MALL_TYPE_DATA = new MallTypeData();
	public static String MALL_ZIP_PATH;
	private static final String TAG = MallData.class.getSimpleName();

}
