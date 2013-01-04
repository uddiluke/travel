// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.google;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
import com.tc.TCData;
import com.tc.TCUtil;

// Referenced classes of package com.tc.google:
//            MyItemizedOverlay, GoogleMapView

public class GoogleMapActivity extends MapActivity implements android.view.View.OnClickListener {

	public GoogleMapActivity() {
		zoomListener = new GoogleMapView.MapViewZoomListener() {

			public void zoom() {
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

	private void updateLocation(Location location) {
		if (location != null) {
			setCenterGeoPoint(location.getLatitude(), location.getLongitude());
			locationState = false;
			removeLoactionLinistener();
			if (locationOverlayItem != null) {
				for (int i = 0; i < myItemizedOverlay.size(); i++)
					if (myItemizedOverlay.getItem(i).getTitle().equals("@me"))
						myItemizedOverlay.removeAt(i);

			}
			locationOverlayItem = new OverlayItem(toGeoPoint(location.getLatitude(), location.getLongitude()), "@me", "false:false:false");
			android.graphics.drawable.Drawable drawable = TCUtil.boundCenter(TCUtil.getDrawable(this, TCData.getPoiIcon("myself")), 0, 0);
			locationOverlayItem.setMarker(drawable);
			myItemizedOverlay.addOverlayItem(locationOverlayItem);
			refreshMap();
		}
	}

	protected boolean isRouteDisplayed() {
		return false;
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case 2131361792:
			onBackPressed();
			break;
		case 2131361904:
			if (!locationState) {
				locationState = true;
				requestLoactionLinistener();
			}
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030071);
		locationManager = (LocationManager) getSystemService("location");
		backButton = (ImageView) findViewById(0x7f0a0000);
		backButton.setOnClickListener(this);
		locationButton = (ImageView) findViewById(0x7f0a0070);
		locationButton.setOnClickListener(this);
		Bundle bundle1 = getIntent().getExtras();
		textView = (TextView) findViewById(0x7f0a0001);
		mapData = (com.tc.TCData.TCMapData) bundle1.getSerializable("map_data");
		textView.setText(mapData.name);
		mapView = (GoogleMapView) findViewById(0x7f0a0074);
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
		mapView.getOverlays().clear();
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		android.graphics.drawable.Drawable drawable = TCUtil.getDrawable(this, mapData.icon);
		myItemizedOverlay = new MyItemizedOverlay(mapView, drawable, null);
		OverlayItem overlayitem = new OverlayItem(toGeoPoint(mapData.lat, mapData.lon), mapData.name, "false:true:fasle");
		myItemizedOverlay.addOverlayItem(overlayitem);
		mapView.getOverlays().add(myItemizedOverlay);
		mapController = mapView.getController();
		mapView.setMapViewZoomListener(zoomListener);
		setZoomLevel(-4 + mapView.getMaxZoomLevel());
		setCenterGeoPoint(mapData.lat, mapData.lon);
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

	public static final String KEY_MAP_DATA = "map_data";
	private static final String TAG = GoogleMapActivity.class.getSimpleName();
	private ImageView backButton;
	private ImageView locationButton;
	private LocationListener locationListener;
	private LocationManager locationManager;
	private OverlayItem locationOverlayItem;
	private boolean locationState;
	private MapController mapController;
	private com.tc.TCData.TCMapData mapData;
	private GoogleMapView mapView;
	private MyItemizedOverlay myItemizedOverlay;
	private MyLocationOverlay myLocationOverlay;
	private TextView textView;
	private GoogleMapView.MapViewZoomListener zoomListener;

}
