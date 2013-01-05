//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Window;
import com.baidu.mapapi.*;
import com.tc.TCApplication;

// Referenced classes of package com.tc.cg:
//            CGDataManager, CGData

public class CGBaseBaiduActivity extends MapActivity {

	public CGBaseBaiduActivity() {
	}

	protected boolean isRouteDisplayed() {
		return false;
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		getWindow().setFormat(1);
		Bundle bundle1 = getIntent().getExtras();
		if (bundle1 != null) {
			CG_ID = bundle1.getInt("cg_id");
			FROM_OTHER_APPLICATION = bundle1.getBoolean("from_other_app", true);
			CG_DATA = CGDataManager.getCGData(CG_ID);
		}
		bMapManager = ((TCApplication) getApplication()).bMapManager;
		bMapManager.start();
	}

	public void startActivity(Context context, Class class1) {
		Intent intent = new Intent(context, class1);
		Bundle bundle = new Bundle();
		bundle.putInt("cg_id", CG_ID);
		bundle.putBoolean("from_other_app", FROM_OTHER_APPLICATION);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	public void startActivity(Context context, Class class1, Bundle bundle) {
		Intent intent = new Intent(context, class1);
		bundle.putInt("cg_id", CG_ID);
		bundle.putBoolean("from_other_app", FROM_OTHER_APPLICATION);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	protected GeoPoint toBaiduGeoPont(Location location) {
		return CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(new GeoPoint((int) (1000000D * location.getLatitude()),
				(int) (1000000D * location.getLongitude()))));
	}

	protected GeoPoint toBaiduGeoPont(CGData cgdata, double d, double d1) {
		return toBaiduGeoPont(cgdata, (int) (d * 1000000D), (int) (d1 * 1000000D));
	}

	protected GeoPoint toBaiduGeoPont(CGData cgdata, int i, int j) {
		GeoPoint geopoint;
		if (cgdata.CG_WGS84_USED)
			geopoint = CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(new GeoPoint(i, j)));
		else
			geopoint = CoordinateConvert.bundleDecode(CoordinateConvert.fromGcjToBaidu(new GeoPoint(i, j)));
		return geopoint;
	}

	protected GeoPoint toBaiduGeoPont(CGData cgdata, CGData.CGSGData.SGItem sgitem) {
		return toBaiduGeoPont(cgdata, sgitem.lat, sgitem.lon);
	}

	public static final String KEY_CG_ID = "cg_id";
	public static final String KEY_FROM_OTHER_APPLICATION = "from_other_app";
	public CGData CG_DATA;
	public int CG_ID;
	public boolean FROM_OTHER_APPLICATION;
	public BMapManager bMapManager;
}
