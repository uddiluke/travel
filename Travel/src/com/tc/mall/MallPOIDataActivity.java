// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.mall;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tc.TCUtil;
import com.tc.weibo.WeiboActivity;

// Referenced classes of package com.tc.mall:
//            MallData, MallPOIActivity

public class MallPOIDataActivity extends Activity implements android.view.View.OnClickListener {
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
			String s1 = sitetype.cnName;
			int j = 0x7f020025;
			if (sitetype.checked)
				j = 0x7f020024;
			imageview.setImageResource(j);
			textview.setText(s1);
			return view;
		}

		private Context context;

		public FilterAdapter(Context context1) {

			context = context1;
		}
	}

	private class POIAdapter extends BaseAdapter {

		private Bitmap getBitmap(Context context1, MallData.MallPOIData.POI poi) {
			return TCUtil.getRoundedCornerBitmap(TCUtil.getBitmap(context1, poi.listIcon));
		}

		public int getCount() {
			return mallPOIData.POIS.size();
		}

		public Object getItem(int i) {
			return mallPOIData.POIS.get(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			if (view == null)
				view = TCUtil.getLayoutInflater(context).inflate(0x7f030050, null);
			MallData.MallPOIData.POI poi = (MallData.MallPOIData.POI) poiAdapter.getItem(i);
			((ImageView) view.findViewById(0x7f0a005e)).setImageBitmap(poiAdapter.getBitmap(context, poi));
			((TextView) view.findViewById(0x7f0a0001)).setText(poi.name);
			((TextView) view.findViewById(0x7f0a01d3)).setText((new StringBuilder()).append(MallData.getFloorName(poi.floorIndex))
					.append(getString(0x7f080032)).append(poi.number).toString());
			((RatingBar) view.findViewById(0x7f0a0061)).setRating(poi.commentGrade / 2.0F);
			ImageView imageview = (ImageView) view.findViewById(0x7f0a0064);
			if (poi.isFavorite())
				imageview.setVisibility(0);
			else
				imageview.setVisibility(4);
			return view;
		}

		private Context context;

		public POIAdapter(Context context1) {

			context = context1;
		}
	}

	private class SiteType {

		boolean checked;
		String cnName;
		String siteType;

		public SiteType(String s1, boolean flag) {

			checked = flag;
			String as[] = s1.split(":");
			if (as.length > 1) {
				cnName = as[0];
				siteType = as[1];
			} else {
				cnName = s1;
			}
		}
	}

	public MallPOIDataActivity() {
		siteTypes = new ArrayList();
		itemListener = new android.widget.AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView adapterview, View view, int i, long l) {
				if (adapterview.getId() == 0x7f0a01ca) { // goto _L2; else goto
															// _L1
					MallData.MallPOIData.POI poi = (MallData.MallPOIData.POI) poiAdapter.getItem(i);
					Bundle bundle = new Bundle();
					bundle.putInt("poi_id", poi.id);
					bundle.putInt("poi_type", poi.typeId);
					TCUtil.startActivity(MallPOIDataActivity.this, MallPOIActivity.class, bundle);
				} else {

					if (adapterview.getId() == 0x7f0a0073) {
						SiteType sitetype = (SiteType) siteTypes.get(i);
						if (i == 0) {
							for (Iterator iterator1 = siteTypes.iterator(); iterator1.hasNext();)
								((SiteType) iterator1.next()).checked = false;
							((SiteType) siteTypes.get(0)).checked = true;
						} else {
							((SiteType) siteTypes.get(0)).checked = false;
							boolean flag;
							boolean flag1;
							if (!sitetype.checked)
								flag = true;
							else
								flag = false;
							sitetype.checked = flag;
							flag1 = false;
							for (Iterator iterator = siteTypes.iterator(); iterator.hasNext();)
								flag1 |= ((SiteType) iterator.next()).checked;

							if (!flag1)
								((SiteType) siteTypes.get(0)).checked = true;
						}
						filterAdapter.notifyDataSetChanged();
						searchFilter();
					}

				}

			}
		};
		s = "";
		textWatcher = new TextWatcher() {

			public void afterTextChanged(Editable editable) {
				s = editable.toString();
				searchFilter();
			}

			public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
			}

			public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
			}

		};
	}

	private Dialog createWeiboDialog() {
		return TCUtil.createPositiveDialog(this, getString(0x7f080035), new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialoginterface, int i) {
				Bundle bundle = new Bundle();
				bundle.putBoolean("has_camera", true);
				bundle.putString("status", "");
				TCUtil.startActivity(MallPOIDataActivity.this, WeiboActivity.class, bundle);
			}

		});
	}

	private void searchFilter() {
		mallPOIData.POIS = new ArrayList();
		Iterator iterator = srcMallPOIData.POIS.iterator();
		do {
			if (!iterator.hasNext())
				break;
			MallData.MallPOIData.POI poi = (MallData.MallPOIData.POI) iterator.next();
			if (poi.name.toLowerCase().contains(s.toLowerCase())) {
				int i = 0;
				while (i < siteTypes.size()) {
					SiteType sitetype = (SiteType) siteTypes.get(i);
					if ((((SiteType) siteTypes.get(0)).checked || sitetype.checked) && MallData.getFloorIndex(sitetype.cnName) == poi.floorIndex)
						mallPOIData.POIS.add(poi);
					i++;
				}
			}
		} while (true);
		poiAdapter.notifyDataSetChanged();
	}

	protected void onActivityResult(int i, int j, Intent intent) {
		if (j == -1 && i == 1000)
			showDialog(1000);
		super.onActivityResult(i, j, intent);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case 2131361792:
			onBackPressed();
			break;
		case 2131361906:
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
		case 2131362251:
			TCUtil.startCameraForResult(this, 1000);
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f03004e);
		Bundle bundle1 = getIntent().getExtras();
		srcMallPOIData = MallData.getMallPOIData(bundle1.getInt(KEY_MALL_POI_TYPE_ID));
		mallPOIData = new MallData.MallPOIData(srcMallPOIData);
		((TextView) findViewById(0x7f0a0001)).setText(bundle1.getString(KEY_MALL_POI_NAME));
		findViewById(0x7f0a0000).setOnClickListener(this);
		siteTypes.add(new SiteType(getString(0x7f08001a), true));
		for (int i = 0; i < MallData.MALL_FLOOR_DATA.FLOORS.size(); i++)
			siteTypes.add(new SiteType(((MallData.MallFloorData.Floor) MallData.MALL_FLOOR_DATA.FLOORS.get(i)).name, false));

		filterAdapter = new FilterAdapter(this);
		filterGallery = (Gallery) findViewById(0x7f0a0073);
		filterGallery.setAdapter(filterAdapter);
		filterGallery.setOnItemClickListener(itemListener);
		filterGallery.setSelection(1);
		filterGallery.setVisibility(8);
		poiList = (ListView) findViewById(0x7f0a01ca);
		poiAdapter = new POIAdapter(this);
		poiList.setAdapter(poiAdapter);
		poiList.setOnItemClickListener(itemListener);
		String s1 = "";
		if (mallPOIData.POIS.size() > 0) {
			String s2 = ((MallData.MallPOIData.POI) mallPOIData.POIS.get(0)).name;
			if (s2.contains("("))
				s2 = s2.substring(0, s2.indexOf("("));
			(new StringBuilder()).append(getString(0x7f080019)).append("\"").append(s2).append("\"").toString();
			s1 = (new StringBuilder()).append(getString(0x7f080019)).append("\"").append(((MallData.MallPOIData.POI) mallPOIData.POIS.get(0)).name)
					.append("\"").toString();
		}
		searchEdit = (EditText) findViewById(0x7f0a00df);
		searchEdit.setHint(s1);
		searchEdit.addTextChangedListener(textWatcher);
		filterButton = (ImageView) findViewById(0x7f0a0072);
		filterButton.setImageResource(0x7f020090);
		filterButton.setTag("");
		filterButton.setOnClickListener(this);
		cameraButton = (ImageView) findViewById(0x7f0a01cb);
		cameraButton.setOnClickListener(this);
	}

	protected Dialog onCreateDialog(int i) {
		Dialog dialog = null;
		if (i == 1000)
			dialog = createWeiboDialog();
		return dialog;

	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onResume() {
		super.onResume();
		searchFilter();
	}

	public static String KEY_MALL_POI_NAME = "name";
	public static String KEY_MALL_POI_TYPE_ID = "type_id";
	private static final String TAG = MallPOIDataActivity.class.getSimpleName();
	private static final int TAKE_CAMERA_REQUEST_CODE = 1000;
	private ImageView cameraButton;
	private FilterAdapter filterAdapter;
	private ImageView filterButton;
	private Gallery filterGallery;
	private android.widget.AdapterView.OnItemClickListener itemListener;
	private MallData.MallPOIData mallPOIData;
	private POIAdapter poiAdapter;
	private ListView poiList;
	String s;
	private EditText searchEdit;
	private List siteTypes;
	private MallData.MallPOIData srcMallPOIData;
	private TextWatcher textWatcher;

}
