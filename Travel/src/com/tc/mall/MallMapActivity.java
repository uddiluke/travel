// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.mall;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tc.TCData;
import com.tc.TCUtil;
import com.tc.sg.ImageMapView;
import com.touchchina.cityguide.sz.R;

// Referenced classes of package com.tc.mall:
//            MallData

public class MallMapActivity extends Activity implements android.view.View.OnClickListener {

	public MallMapActivity() {
		searchAdapter = new BaseAdapter() {

			public int getCount() {
				return pois.size();
			}

			public Object getItem(int i) {
				return pois.get(i);
			}

			public long getItemId(int i) {
				return (long) i;
			}

			public View getView(int i, View view, ViewGroup viewgroup) {
				if (view == null)
					view = TCUtil.getLayoutInflater(MallMapActivity.this).inflate(0x7f03002c, null);
				MallData.MallPOIData.POI poi1 = (MallData.MallPOIData.POI) getItem(i);
				view.findViewById(0x7f0a0066).setVisibility(8);
				((TextView) view.findViewById(0x7f0a0067)).setText((new StringBuilder()).append(poi1.name).append(" ( ").append(poi1.number)
						.append(" )").toString());
				view.findViewById(0x7f0a0068).setVisibility(8);
				return view;
			}

		};
		poiTapListener = new com.tc.sg.ImageMapView.PoiListener() {

			public void poiHide() {
				hidePopView();
			}

			public void poiMove(com.tc.sg.SGData.PoiData.PoiPoint poipoint) {
				showPopView(poipoint);
			}

			public void poiShow(com.tc.sg.SGData.PoiData.PoiPoint poipoint) {
				com.tc.sg.SGData.MapData.MapPoint mappoint = new com.tc.sg.SGData.MapData.MapPoint(poipoint.mapPoint);
				imageMapView.update(mappoint);
				showPopView(poipoint);
			}

		};
	}

	private void hidePopView() {
		if (popViewLayout.isShown())
			popViewLayout.setVisibility(8);
	}

	private void search(String s) {
		pois = new ArrayList();
		for (Iterator iterator = MallData.MALL_POI_DATAS.iterator(); iterator.hasNext();) {
			Iterator iterator1 = ((MallData.MallPOIData) iterator.next()).POIS.iterator();
			while (iterator1.hasNext()) {
				MallData.MallPOIData.POI poi1 = (MallData.MallPOIData.POI) iterator1.next();
				if (poi.floorIndex == poi1.floorIndex && poi1.name.toLowerCase().contains(s.toLowerCase())) {
					Log.i(TAG, (new StringBuilder()).append("poi.floorIndex ").append(poi1.floorIndex).toString());
					pois.add(poi1);
				}
			}
		}

		searchAdapter.notifyDataSetChanged();
	}

	private void showPopView(com.tc.sg.SGData.PoiData.PoiPoint poipoint) {
		com.tc.sg.SGData.MapData.ScreenPoint screenpoint = imageMapView.toScreenPoint(poipoint.mapPoint);
		screenpoint.x = screenpoint.x + (imageMapView.POI_TOUCH_WEIGH / 2 - imageMapView.POI_POINT_X);
		screenpoint.y = screenpoint.y + -imageMapView.POI_POINT_Y;
		nameText.setText(poipoint.title);
		popView.setTag(poipoint);
		popView.measure(0, 0);
		int i = popView.getMeasuredWidth();
		int j = popView.getMeasuredHeight();
		android.widget.AbsoluteLayout.LayoutParams layoutparams = new android.widget.AbsoluteLayout.LayoutParams(i, j, screenpoint.x - i / 2,
				12 + (screenpoint.y - j));
		if (!popViewLayout.isShown()) {
			popViewLayout.removeAllViews();
			popViewLayout.addView(popView, 0, layoutparams);
			popViewLayout.setVisibility(0);
		} else {
			popViewLayout.updateViewLayout(popView, layoutparams);
		}
	}

	private void showSearchDialog(Activity activity) {
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
		EditText edittext = (EditText) dialog.findViewById(0x7f0a00df);
		edittext.setHint(getString(0x7f08007e));
		edittext.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable editable) {
				search(editable.toString());
			}

			public void beforeTextChanged(CharSequence charsequence, int j, int k, int l) {
			}

			public void onTextChanged(CharSequence charsequence, int j, int k, int l) {
			}

		});
		dialog.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

			public void onCancel(DialogInterface dialoginterface) {
			}

		});
		dialog.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {

			public void onDismiss(DialogInterface dialoginterface) {
			}

		});
		ListView listview = (ListView) dialog.findViewById(0x7f0a00f9);
		listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView adapterview, View view, int j, long l) {
				MallData.MallPOIData.POI poi2 = (MallData.MallPOIData.POI) searchAdapter.getItem(j);
				com.tc.sg.SGData.MapData.MapPoint mappoint = new com.tc.sg.SGData.MapData.MapPoint();
				mappoint.x = poi2.x;
				mappoint.y = poi2.y;
				imageMapView.locationUpdate(mappoint);
				dialog.dismiss();
			}

		});
		pois = new ArrayList();
		for (Iterator iterator = MallData.MALL_POI_DATAS.iterator(); iterator.hasNext();) {
			Iterator iterator1 = ((MallData.MallPOIData) iterator.next()).POIS.iterator();
			while (iterator1.hasNext()) {
				MallData.MallPOIData.POI poi1 = (MallData.MallPOIData.POI) iterator1.next();
				if (poi.floorIndex == poi1.floorIndex)
					pois.add(poi1);
			}
		}

		listview.setAdapter(searchAdapter);
		dialog.show();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case 2131361792:
			onBackPressed();
			break;
		case 2131361904:
			showSearchDialog(this);
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.mall_map);
		Bundle bundle1 = getIntent().getExtras();
		poi = MallData.getPOI(bundle1.getInt("poi_type"), bundle1.getInt("poi_id"));
		mapData = new com.tc.sg.SGData.MapData(poi);
		((TextView) findViewById(0x7f0a0001)).setText((new StringBuilder()).append(mapData.name).append(getString(0x7f080032))
				.append(getString(0x7f080033)).toString());
		imageMapView = (ImageMapView) findViewById(0x7f0a01d0);
		if (TCData.USE_2X)
			imageMapView.setCenterMapPoint(poi.x, poi.y);
		else
			imageMapView.setCenterMapPoint(poi.x / 2, poi.y / 2);
		imageMapView.setMapData(mapData);
		imageMapView.setPoiTapListener(poiTapListener);
		popViewLayout = (AbsoluteLayout) findViewById(0x7f0a01d1);
		popViewLayout.setVisibility(8);
		popView = getLayoutInflater().inflate(0x7f030066, null);
		popView.setDrawingCacheEnabled(true);
		nameText = (TextView) popView.findViewById(0x7f0a00f3);
		imageMapView.setPopView(popView);
		locationButton = (ImageView) findViewById(0x7f0a0070);
		locationButton.setOnClickListener(this);
		findViewById(0x7f0a0000).setOnClickListener(this);
	}

	protected void onDestroy() {
		super.onDestroy();
		imageMapView.clearBuffer();
	}

	public static final String KEY_POI_ID = "poi_id";
	public static final String KEY_POI_TYPE = "poi_type";
	private static final String TAG = MallMapActivity.class.getSimpleName();
	private ImageMapView imageMapView;
	ImageView locationButton;
	private com.tc.sg.SGData.MapData mapData;
	private TextView nameText;
	private MallData.MallPOIData.POI poi;
	private com.tc.sg.ImageMapView.PoiListener poiTapListener;
	private List pois;
	private View popView;
	private AbsoluteLayout popViewLayout;
	private BaseAdapter searchAdapter;

}
