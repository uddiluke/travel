package com.tc.cg;

import android.app.Activity;
import android.app.Dialog;
import android.content.*;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.*;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.flurry.android.FlurryAgent;
import com.google.android.maps.*;
import com.tc.TCData;
import com.tc.TCUtil;
import com.tc.google.GoogleMapView;
import com.tc.google.MyItemizedOverlay;
import com.touchchina.cityguide.sz.R;

import java.util.*;

// Referenced classes of package com.tc.cg:
//            CGUtil, CGDataManager, CGData, CGSiteItemActivity

public class CGMapActivity extends MapActivity implements android.view.View.OnClickListener {
	private class FilterAdapter extends BaseAdapter {

		public int getCount() {
			return siteTypes.size();
		}

		public Object getItem(int i) {
			return siteTypes.get(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			if (view == null)
				view = TCUtil.getLayoutInflater(context).inflate(0x7f030008, null);
			SiteType sitetype = (SiteType) getItem(i);
			ImageView imageview = (ImageView) view.findViewById(0x7f0a0044);
			TextView textview = (TextView) view.findViewById(0x7f0a0045);
			String s = sitetype.cnName;
			int j = 0x7f020025;
			if (sitetype.checked)
				j = 0x7f020024;
			imageview.setImageResource(j);
			textview.setText(s);
			return view;
		}

		private Context context;

		public FilterAdapter(Context context1) {
			context = context1;
		}
	}

	private class SearchAdapter extends BaseAdapter {

		public int getCount() {
			return searchSGItems.size();
		}

		public Object getItem(int i) {
			return searchSGItems.get(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			if (view == null)
				view = TCUtil.getLayoutInflater(context).inflate(0x7f03002c, null);
			CGData.CGSGData.SGItem sgitem = (CGData.CGSGData.SGItem) getItem(i);
			ImageView imageview = (ImageView) view.findViewById(0x7f0a0066);
			android.graphics.Bitmap bitmap = TCUtil.getBitmap(context, sgitem.icon);
			android.graphics.Bitmap bitmap1 = TCUtil.getRoundedCornerBitmap(bitmap);
			TCUtil.recycleBitmap(bitmap);
			imageview.setImageBitmap(bitmap1);
			((TextView) view.findViewById(0x7f0a0067)).setText(sgitem.name);
			view.findViewById(0x7f0a0068).setVisibility(8);
			return view;
		}

		private Context context;

		public SearchAdapter(Context context1) {

			context = context1;
		}
	}

	private class SiteType {

		boolean checked;
		String cnName;
		List sgItems;
		String siteType;

		public SiteType(String s, boolean flag) {

			checked = flag;
			String as[] = s.split(":");
			if (as.length > 1) {
				cnName = as[0];
				siteType = as[1];
			} else {
				cnName = s;
			}
		}
	}

	public CGMapActivity() {
		zoomListener = new com.tc.google.GoogleMapView.MapViewZoomListener() {

			public void zoom() {
				addOverlayItem();
				refreshMap();
			}

		};
		siteTypes = new ArrayList<SiteType>();
		itemListener = new android.widget.AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView adapterview, View view, int i, long l) {
				siteTypesChanged = true;
				SiteType sitetype = (SiteType) siteTypes.get(i);
				TCUtil.flurryLog("filterInMap", sitetype.siteType);
				if (i == 0) {
					for (SiteType type : siteTypes)
						type.checked = false;

					((SiteType) siteTypes.get(0)).checked = true;

				} else {

					((SiteType) siteTypes.get(0)).checked = true;
					sitetype.checked = !sitetype.checked;
					boolean flag1 = true;
					for (SiteType type : siteTypes) {
						flag1 = type.checked;
						if (!flag1)
							break;
					}
					if (!flag1)
						((SiteType) siteTypes.get(0)).checked = false;

				}

				filterAdapter.notifyDataSetChanged();
				addOverlayItem();
				myItemizedOverlay.showPopView(null);
				refreshMap();

			}

		};
		sgItemComparator = new Comparator() {

			public int compare(CGData.CGSGData.SGItem sgitem, CGData.CGSGData.SGItem sgitem1) {
				return -1 * CGUtil.compare(Float.valueOf(sgitem.commentGrade), Float.valueOf(sgitem1.commentGrade));
			}

			public int compare(Object obj, Object obj1) {
				return compare((CGData.CGSGData.SGItem) obj, (CGData.CGSGData.SGItem) obj1);
			}

		};
		locationListener = new LocationListener() {

			public void onLocationChanged(Location location) {
				updateLocation(location);
			}

			public void onProviderDisabled(String s) {
			}

			public void onProviderEnabled(String s) {
			}

			public void onStatusChanged(String s, int i, Bundle bundle) {
			}

		};
	}

	private void addOverlayItem() {
		clearOverlayItem();
		boolean flag = false;
		if (((SiteType) siteTypes.get(0)).checked)
			flag = true;
		for (int i = 1; i < siteTypes.size(); i++)
			if (flag || ((SiteType) siteTypes.get(i)).checked)
				addSGData(((SiteType) siteTypes.get(i)).sgItems);

	}

	private void addSGData(List list) {
		int i;
		int j;
		i = getZoomLevel();
		if (i < 1)
			i = 1;
		Log.i(TAG, (new StringBuilder()).append("level").append(i).toString());
		j = 0;
		while (j < list.size()) {
			CGData.CGSGData.SGItem sgitem = (CGData.CGSGData.SGItem) list.get(j);
			if (sgitem.isMain) {
				addSGItem(sgitem);
			} else if (inScreen(sgitem)) {

				addSGItem(sgitem);
				if (i >= 0)
					i--;
				else
					break;
			}
			j++;
		}

	}

	private OverlayItem addSGItem(CGData.CGSGData.SGItem sgitem) {
		OverlayItem overlayitem = new OverlayItem(toGeoPoint(sgitem.lat, sgitem.lon), (new StringBuilder()).append(sgitem.type).append(":")
				.append(sgitem.id).append(":").append(sgitem.name).toString(), "false:true:true");
		Drawable drawable = TCUtil.getDrawable(this, sgitem.logo);
		Log.i(TAG, (new StringBuilder()).append("width").append(drawable.getIntrinsicWidth()).toString());
		overlayitem.setMarker(TCUtil.boundCenterBottom(drawable, 0, 0));
		myItemizedOverlay.addOverlayItem(overlayitem);
		return overlayitem;
	}

	private void clearOverlayItem() {
		myItemizedOverlay.clearOverlayItem();
		if (locationOverlayItem != null)
			myItemizedOverlay.addOverlayItem(locationOverlayItem);
	}

	private boolean inScreen(CGData.CGSGData.SGItem sgitem) {
		boolean flag = false;
		GeoPoint geopoint = mapView.getProjection().fromPixels(0, 0);
		DisplayMetrics displaymetrics = TCUtil.getDisplayMetrics(this);
		GeoPoint geopoint1 = mapView.getProjection().fromPixels(displaymetrics.widthPixels, displaymetrics.heightPixels);
		if (1000000D * sgitem.lat < (double) geopoint.getLatitudeE6() && 1000000D * sgitem.lon > (double) geopoint.getLongitudeE6()
				&& 1000000D * sgitem.lat > (double) geopoint1.getLatitudeE6() && 1000000D * sgitem.lon < (double) geopoint1.getLongitudeE6())
			flag = true;
		return flag;
	}

	private void removeLoactionLinistener() {
		if (locationManager != null)
			locationManager.removeUpdates(locationListener);
	}

	private void requestLoactionLinistener() {
		if (locationManager != null) {
			String s = TCUtil.getBestProvider(locationManager);
			locationManager.requestLocationUpdates(s, 1000L, 0.0F, locationListener);
		}
	}

	private void showSearchDialog(Activity activity) {
		final SearchAdapter searchAdapter = new SearchAdapter(getApplication());
		final Dialog dialog = new Dialog(activity, 0x7f090003);
		dialog.setContentView(0x7f03002b);
		android.view.WindowManager.LayoutParams layoutparams = dialog.getWindow().getAttributes();
		Rect rect = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		int i = rect.top;
		layoutparams.width = TCData.SCREEN_WIDTH;
		layoutparams.height = TCData.SCREEN_HEIGHT - i;
		dialog.getWindow().setAttributes(layoutparams);
		dialog.onWindowAttributesChanged(layoutparams);
		final EditText searchEdit = (EditText) dialog.findViewById(0x7f0a00df);
		String s = searchEdit.getText().toString();
		searchEdit.setText(s);
		searchEdit.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable editable) {
				searchSGItems = new ArrayList();
				Iterator iterator1 = srcSearchSGItems.iterator();
				do {
					if (!iterator1.hasNext())
						break;
					CGData.CGSGData.SGItem sgitem1 = (CGData.CGSGData.SGItem) iterator1.next();
					if (sgitem1.name.toLowerCase().contains(editable.toString().toLowerCase()))
						searchSGItems.add(sgitem1);
				} while (true);
				searchAdapter.notifyDataSetChanged();
			}

			public void beforeTextChanged(CharSequence charsequence, int j, int k, int l) {
			}

			public void onTextChanged(CharSequence charsequence, int j, int k, int l) {
			}

		});
		dialog.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

			public void onCancel(DialogInterface dialoginterface) {
				searchEdit.setText(searchEdit.getText());
				searchButton.setImageResource(0x7f020092);
			}

		});
		dialog.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {

			public void onDismiss(DialogInterface dialoginterface) {
				searchEdit.setText(searchEdit.getText());
			}

		});
		ListView listview = (ListView) dialog.findViewById(0x7f0a00f9);
		listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView adapterview, View view, int j, long l) {
				FlurryAgent.logEvent("searchInMap");
				TCUtil.hideSoftKeyBroad(CGMapActivity.this, searchEdit);
				CGData.CGSGData.SGItem sgitem1 = (CGData.CGSGData.SGItem) searchSGItems.get(j);
				setCenterGeoPoint(sgitem1.lat, sgitem1.lon);
				clearOverlayItem();
				OverlayItem overlayitem = addSGItem(sgitem1);
				myItemizedOverlay.showPopView(overlayitem);
				refreshMap();
				searchEdit.setText(searchEdit.getText());
				searchButton.setImageResource(0x7f020092);
				dialog.dismiss();
			}

		});
		searchSGItems = new ArrayList();
		Iterator iterator = srcSearchSGItems.iterator();
		do {
			if (!iterator.hasNext())
				break;
			CGData.CGSGData.SGItem sgitem = (CGData.CGSGData.SGItem) iterator.next();
			if (sgitem.name.toLowerCase().contains(s.toLowerCase()))
				searchSGItems.add(sgitem);
		} while (true);
		listview.setAdapter(searchAdapter);
		dialog.show();
	}

	private void updateLocation(Location location) {
		if (location != null) {
			if (setCenterPoint) {
				setCenterPoint = false;
				setCenterGeoPoint(location.getLatitude(), location.getLongitude());
			}
			if (locationOverlayItem != null) {
				for (int i = 0; i < myItemizedOverlay.size(); i++)
					if (myItemizedOverlay.getItem(i).getTitle().equals("@me"))
						myItemizedOverlay.removeAt(i);

			}
			locationOverlayItem = new OverlayItem(toGeoPoint(location.getLatitude(), location.getLongitude()), "@me", "false:false:false");
			Drawable drawable = TCUtil.boundCenter(TCUtil.getDrawable(this, TCData.getPoiIcon("myself")), 0, 0);
			locationOverlayItem.setMarker(drawable);
			myItemizedOverlay.addOverlayItem(locationOverlayItem);
			addOverlayItem();
			refreshMap();
		}
	}

	protected int getMaxZoomLevel() {
		return mapView.getMaxZoomLevel();
	}

	public int getZoomLevel() {
		return mapView.getZoomLevel();
	}

	protected boolean isRouteDisplayed() {
		return false;
	}

	public void toBackPressed() {
		CGUtil.showExitDialog(this);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backButton:
			this.toBackPressed();// onBackPressed();
			break;
		case R.id.locationButton:
			FlurryAgent.logEvent("getLocationInMap");
			if (!locationState) {
				locationButton.setImageResource(0x7f0200ea);
				if (!TCUtil.isGPSAvailable(this)) {
					TCUtil.showTip(this, 0x7f08004b);
				} else {
					locationState = true;
					setCenterPoint = true;
					requestLoactionLinistener();
				}
			} else {
				locationState = false;
				removeLoactionLinistener();
				locationButton.setImageResource(0x7f0200bf);
			}
			break;
		case R.id.searchButton:
			FlurryAgent.logEvent("showSearchInMap");
			view.getTag();
			searchButton.setImageResource(0x7f020093);
			showSearchDialog(this);
			break;
		case R.id.filterButton:
			FlurryAgent.logEvent("showFilterInMap");
			if (((String) view.getTag()).length() > 0) {
				view.setTag("");
				filterButton.setImageResource(0x7f020090);
				filterGallery.setVisibility(8);
			} else {
				view.setTag("on");
				filterButton.setImageResource(0x7f020091);
				filterGallery.setVisibility(0);
			}
			break;
		case R.id.searchLayout:
		case R.id.searchEdit:
			showSearchDialog(this);
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Bundle bundle1 = getIntent().getExtras();
		CG_ID = bundle1.getInt("cg_id");
		FROM_OTHER_APPLICATION = bundle1.getBoolean("from_other_app", true);
		CG_DATA = CGDataManager.getCGData(CG_ID);
		setContentView(0x7f030012);
		locationManager = (LocationManager) getSystemService("location");
		titleText = (TextView) findViewById(0x7f0a0001);
		titleText.setText(bundle1.getString("map_title"));
		locationButton = (ImageView) findViewById(0x7f0a0070);
		locationButton.setOnClickListener(this);
		filterButton = (ImageView) findViewById(0x7f0a0072);
		filterButton.setImageResource(0x7f020090);
		filterButton.setTag("");
		filterButton.setOnClickListener(this);
		searchButton = (ImageView) findViewById(0x7f0a0071);
		searchButton.setImageResource(0x7f020092);
		searchButton.setTag("on");
		searchButton.setOnClickListener(this);
		for (int i = 0; i < CG_DATA.CG_SG_DATAS.size(); i++)
			Collections.sort(((CGData.CGSGData) CG_DATA.CG_SG_DATAS.get(i)).sgItems, sgItemComparator);

		SiteType sitetype = new SiteType(getString(0x7f08001a), true);
		siteTypes.add(sitetype);
		for (int j = 0; j < CG_DATA.CG_INFO_DATA.cgInfoItems.size(); j++) {
			CGData.CGInfoData.CGInfoItem cginfoitem = (CGData.CGInfoData.CGInfoItem) CG_DATA.CG_INFO_DATA.cgInfoItems.get(j);
			if (cginfoitem.type.equals("sg")) {
				SiteType sitetype1 = new SiteType((new StringBuilder()).append(cginfoitem.chName).append(":").append(cginfoitem.name).toString(),
						false);
				sitetype1.sgItems = CGData.getCGSGData(CG_DATA, cginfoitem.name).sgItems;
				siteTypes.add(sitetype1);
			}
		}

		FilterAdapter filteradapter = new FilterAdapter(this);
		filterAdapter = filteradapter;
		filterGallery = (Gallery) findViewById(0x7f0a0073);
		filterGallery.setAdapter(filterAdapter);
		filterGallery.setOnItemClickListener(itemListener);
		filterGallery.setVisibility(8);
		mapView = (GoogleMapView) findViewById(0x7f0a0074);
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
		mapView.getOverlays().clear();
		zoomButtonsController = mapView.getZoomButtonsController();
		Drawable drawable = TCUtil.getDrawable(this, TCData.getPoiIcon("info"));
		GoogleMapView googlemapview = mapView;
		android.view.View.OnClickListener onclicklistener = new android.view.View.OnClickListener() {

			public void onClick(View view) {
				if (view.getId() == R.id.detailButton) {
					String as[] = ((String) view.getTag()).split(":");
					TCUtil.flurryLog("EnterDetailInMap", as[2]);
					Bundle bundle2 = new Bundle();
					bundle2.putString("sg_type", as[0]);
					bundle2.putInt("sg_id", Integer.parseInt(as[1]));
					bundle2.putInt("cg_id", CG_ID);
					bundle2.putBoolean("from_other_app", FROM_OTHER_APPLICATION);
					TCUtil.startActivity(CGMapActivity.this, CGSiteItemActivity.class, bundle2);
				}

			}

		};
		MyItemizedOverlay myitemizedoverlay = new MyItemizedOverlay(googlemapview, drawable, onclicklistener);
		myItemizedOverlay = myitemizedoverlay;
		mapView.getOverlays().add(myItemizedOverlay);
		MyLocationOverlay mylocationoverlay = new MyLocationOverlay(this, mapView);
		myLocationOverlay = mylocationoverlay;
		mapView.getOverlays().add(myLocationOverlay);
		mapController = mapView.getController();
		mapView.setMapViewZoomListener(zoomListener);
		setCenterGeoPoint(CG_DATA.CG_LAT, CG_DATA.CG_LON);
		setZoomLevel(CG_DATA.CG_MAP_LEVEL);
		srcSearchSGItems = new ArrayList();
		for (Iterator iterator = CG_DATA.CG_SG_DATAS.iterator(); iterator.hasNext();) {
			CGData.CGSGData cgsgdata = (CGData.CGSGData) iterator.next();
			Collections.sort(cgsgdata.sgItems, sgItemComparator);
			Iterator iterator1 = cgsgdata.sgItems.iterator();
			while (iterator1.hasNext()) {
				CGData.CGSGData.SGItem sgitem = (CGData.CGSGData.SGItem) iterator1.next();
				srcSearchSGItems.add(sgitem);
			}
		}

		addOverlayItem();
		refreshMap();
	}

	protected void onPause() {
		if (locationState)
			removeLoactionLinistener();
		myLocationOverlay.disableCompass();
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
		if (locationState)
			requestLoactionLinistener();
		myLocationOverlay.enableCompass();
	}

	public void refreshMap() {
		mapView.invalidate();
	}

	public void setCenterGeoPoint(double d, double d1) {
		GeoPoint geopoint = toGeoPoint(d, d1);
		mapController.setCenter(geopoint);
		mapController.animateTo(geopoint);
	}

	public void setZoomLevel(int i) {
		mapController.setZoom(i);
	}

	protected GeoPoint toGeoPoint(double d, double d1) {
		return new GeoPoint((int) (d * 1000000D), (int) (d1 * 1000000D));
	}

	public void zoomIn() {
		zoomButtonsController.setZoomOutEnabled(true);
		if (getZoomLevel() >= getMaxZoomLevel()) {
			zoomButtonsController.setZoomInEnabled(false);
		} else {
			mapController.zoomIn();
			addOverlayItem();
		}
	}

	public void zoomOut() {
		zoomButtonsController.setZoomInEnabled(true);
		if (getZoomLevel() == 2) {
			zoomButtonsController.setZoomOutEnabled(false);
		} else {
			mapController.zoomOut();
			addOverlayItem();
		}
	}

	public static final String KEY_CG_ID = "cg_id";
	public static final String KEY_FROM_OTHER_APPLICATION = "from_other_app";
	public static final String KEY_TITLE = "map_title";
	private static final String TAG = CGMapActivity.class.getSimpleName();
	public CGData CG_DATA;
	public int CG_ID;
	public boolean FROM_OTHER_APPLICATION;
	private FilterAdapter filterAdapter;
	private ImageView filterButton;
	private Gallery filterGallery;
	private android.widget.AdapterView.OnItemClickListener itemListener;
	private ImageView locationButton;
	private LocationListener locationListener;
	private LocationManager locationManager;
	private OverlayItem locationOverlayItem;
	private boolean locationState;
	private MapController mapController;
	private GoogleMapView mapView;
	private MyItemizedOverlay myItemizedOverlay;
	private MyLocationOverlay myLocationOverlay;
	private ImageView searchButton;
	List searchSGItems;
	private boolean setCenterPoint;
	private Comparator sgItemComparator;
	private List<SiteType> siteTypes;
	private boolean siteTypesChanged;
	List srcSearchSGItems;
	private TextView titleText;
	private ZoomButtonsController zoomButtonsController;
	private com.tc.google.GoogleMapView.MapViewZoomListener zoomListener;

	/*
	 * static boolean access$102(CGMapActivity cgmapactivity, boolean flag) {
	 * cgmapactivity.siteTypesChanged = flag; return flag; }
	 */

}
