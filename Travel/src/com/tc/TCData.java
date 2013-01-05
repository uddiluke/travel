//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc;

import java.io.Serializable;
import java.util.List;

// Referenced classes of package com.tc:
//            TCUtil

public class TCData {
	public static class TCAlbumData implements Serializable {

		public List album;
		public String name;

		public TCAlbumData() {
		}
	}

	public static class TCDownResult {

		public String error;
		public String status;

		public TCDownResult() {
		}
	}

	public static class TCMapData implements Serializable {

		public String icon;
		public double lat;
		public double lon;
		public String name;

		public TCMapData() {
		}
	}

	public static class TCSGTrafficData implements Serializable {

		public String address;
		public String bus;
		public double lat;
		public double lon;
		public String siteName;
		public String subway;
		public Taxi taxi=new Taxi();

		public static class Taxi implements Serializable {

			public String destination;
			public String telephone;
			public String url;

		}
	}

	public TCData() {
	}

	public static String getListIcon(String s) {
		String s1;
		if (USE_2X)
			s1 = (new StringBuilder()).append("file:///android_asset/icon/list_").append(s.toLowerCase()).append("@2x.png").toString();
		else
			s1 = (new StringBuilder()).append("file:///android_asset/icon/list_").append(s.toLowerCase()).append(".png").toString();
		return s1;
	}

	public static String getPoiIcon(String s) {
		String s1;
		if (USE_2X)
			s1 = (new StringBuilder()).append("file:///android_asset/poi/poi_").append(s.toLowerCase()).append("@2x.png").toString();
		else
			s1 = (new StringBuilder()).append("file:///android_asset/poi/poi_").append(s.toLowerCase()).append(".png").toString();
		return s1;
	}

	public static final String ASSET_PATH = "file:///android_asset/";
	public static String CG_ROOT = (new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina").append("/").append("CG").append("/")
			.toString();
	public static final String CG_TAG = "CG";
	public static boolean GOOGLE_MAP_USEABLE = false;
	public static final String HTML_PROTOCOL = "html://";
	public static final String MALL_TYPE = "mallshop";
	public static final String PHOTOS_PROTOCOL = "photos://";
	public static int SCREEN_HEIGHT = 0;
	public static int SCREEN_WIDTH = 0;
	public static String SG_ROOT = (new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina").append("/").append("SG").append("/")
			.toString();
	public static final String SG_TAG = "SG";
	public static final String SG_TYPE = "spot";
	public static final String TC_TAG = "TouchChina";
	public static final String TRAFFIC_PROTOCOL = "traffic://";
	public static boolean USE_2X = false;
	public static final String WEIBO_KEY = "3780182769";
	public static final String WEIBO_SECRET = "832c9b655611271fa6f662c0281f3493";

}
