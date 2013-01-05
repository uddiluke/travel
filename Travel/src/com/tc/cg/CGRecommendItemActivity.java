//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tc.TCApplication;
import com.tc.TCUtil;
import com.tc.mall.MallStartActivity;
import com.tc.sg.SGStartActivity;
import com.touchchina.cityguide.sz.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData, CGUtil

public class CGRecommendItemActivity extends CGBaseActivity implements android.view.View.OnClickListener {
	private class ImageAdapter extends BaseAdapter {

		public int getCount() {
			return recommendItem.images.size();
		}

		public Object getItem(int i) {
			return recommendItem.images.get(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			ImageView imageview;
			if (view == null)
				imageview = new ImageView(context);
			else
				imageview = (ImageView) view;
			imageview.setAdjustViewBounds(true);
			imageview.setLayoutParams(new android.widget.Gallery.LayoutParams(240, 360));
			imageview.setImageBitmap(TCUtil.getBitmap(context, (String) getItem(i)));
			return imageview;
		}

		Context context;

		public ImageAdapter(Context context1) {

			context = context1;
		}
	}

	public CGRecommendItemActivity() {
		list = new ArrayList();
	}

	private boolean isDownloading() {
		if (((TCApplication) getApplicationContext()).downloadingInfos != null) {
			for (TCApplication.DownloadingInfo info : ((TCApplication) getApplicationContext()).downloadingInfos) {
				if (info.guideid == sgItem.sgId) {
					return true;
				}
			}
		}
		return false;

	}

	public void onClick(View view) {
		if (view.getId() == R.id.backButton) {
			onBackPressed();
		}

	}

	protected void onCreate(Bundle bundle) {
		Bundle bundle1;
		super.onCreate(bundle);
		setContentView(0x7f030028);
		indicatorsLinearLayout = (LinearLayout) findViewById(0x7f0a00f6);
		bundle1 = getIntent().getExtras();
		int i = bundle1.getInt(KEY_POSITION, -1);
		if (i == -1)
			recommendItem = (CGData.CGRecommendData.CGRecommendItem) bundle1.getSerializable(KEY_ITEM);
		else
			recommendItem = (CGData.CGRecommendData.CGRecommendItem) CG_DATA.CG_RECOMMEND_DATA.items.get(i);
		indicatorsLinearLayout = (LinearLayout) findViewById(0x7f0a00f6);
		for (int j = 0; j < recommendItem.images.size(); j++) {
			ImageView imageview = new ImageView(this);
			imageview.setPadding(5, 5, 5, 5);
			list.add(imageview);
			indicatorsLinearLayout.addView(imageview);
		}

		((TextView) findViewById(0x7f0a0001)).setText(recommendItem.name);
		findViewById(0x7f0a0000).setOnClickListener(this);
		nameText = (TextView) findViewById(0x7f0a00f3);
		nameText.setText(recommendItem.name);
		iconButton = (ImageView) findViewById(0x7f0a00f2);
		android.graphics.Bitmap bitmap = TCUtil.getBitmap(this, recommendItem.icon);
		iconButton.setImageBitmap(bitmap);
		downloadLayout = (RelativeLayout) findViewById(0x7f0a00f1);
		downloadButton = (ImageView) findViewById(0x7f0a00f4);
		id = bundle1.getInt(KEY_ID);
		Iterator iterator = CG_DATA.CG_SITE_DATAS.iterator();
		do {
			if (!iterator.hasNext())
				break;
			CGData.CGSGData.SGItem sgitem = (CGData.CGSGData.SGItem) iterator.next();
			if (sgitem.id == id)
				sgItem = sgitem;
		} while (true);
		if (bundle1.getInt(KEY_UPDATE_TIMESTAMP) > sgItem.timestamp) // goto
																		// _L2;
																		// else
																		// goto
																		// _L1
		{
			if (isDownloading()) {
				downloadButton.setImageResource(0x7f02005b);
				downloadLayout.setOnClickListener(new android.view.View.OnClickListener() {

					public void onClick(View view) {
						if (recommendItem.url != null) {
							TCUtil.flurryLog("Download", recommendItem.name);
							CGUtil.openBrowser(CGRecommendItemActivity.this, recommendItem.url);
						} else {
							setResult(77);
							finish();
						}
					}

				});
			} else {
				downloadLayout.setOnClickListener(new android.view.View.OnClickListener() {

					public void onClick(View view) {
						if (recommendItem.url != null) {
							TCUtil.flurryLog("Download", recommendItem.name);
							CGUtil.openBrowser(CGRecommendItemActivity.this, recommendItem.url);
						} else {
							Intent intent = new Intent();
							intent.putExtra(CGRecommendItemActivity.KEY_ID, id);
							setResult(7, intent);
							finish();
						}
					}

				});
			}

		} else {
			if (sgItem.timestamp > 0 && !TCUtil.isUnZipInterrapted("sg", sgItem.sgId, false)) {
				downloadButton.setImageResource(0x7f02005a);
				downloadLayout.setOnClickListener(new android.view.View.OnClickListener() {

					public void onClick(View view) {
						if (recommendItem.url != null) { // goto _L2; else goto
															// _L1
							TCUtil.flurryLog("Download", recommendItem.name);
							CGUtil.openBrowser(CGRecommendItemActivity.this, recommendItem.url);
						} else if (sgItem.type.equals("Shopping")) {
							Bundle bundle2 = new Bundle();
							bundle2.putInt(MallStartActivity.KEY_MALL_ID, sgItem.sgId);
							bundle2.putString(MallStartActivity.KEY_MALL_APPLICATION, (new StringBuilder()).append(sgItem.type).append(sgItem.sgId)
									.toString());
							startActivity(CGRecommendItemActivity.this, MallStartActivity.class, bundle2);
						} else if (sgItem.type.equals("Site")) {
							TCUtil.flurryLog("SiteGuideStart", sgItem.name);
							Bundle bundle3 = new Bundle();
							bundle3.putInt("sg_id", sgItem.sgId);
							bundle3.putBoolean("from_other_app", false);
							startActivity(CGRecommendItemActivity.this, SGStartActivity.class, bundle3);
						}
					}
				});
			}
		}
		contentText = (TextView) findViewById(0x7f0a004e);
		contentText.setText(recommendItem.content);
		albumGallery = (Gallery) findViewById(0x7f0a00f5);
		imageAdapter = new ImageAdapter(this);
		albumGallery.setAdapter(imageAdapter);
		albumGallery.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView adapterview, View view, int k, long l) {
				int i1 = 0;
				while (i1 < recommendItem.images.size()) {
					if (i1 == k)
						((ImageView) list.get(i1)).setImageResource(0x7f020012);
					else
						((ImageView) list.get(i1)).setImageResource(0x7f020011);
					i1++;
				}
			}

			public void onNothingSelected(AdapterView adapterview) {
			}

		});

	}

	public static String KEY_ID = "id";
	public static String KEY_ITEM = "item";
	public static String KEY_POSITION = "position";
	public static String KEY_UPDATE_TIMESTAMP = "KEY_UPDATE_TIMESTAMP";
	private Gallery albumGallery;
	private TextView contentText;
	private ImageView downloadButton;
	private RelativeLayout downloadLayout;
	private ImageView iconButton;
	int id;
	private ImageAdapter imageAdapter;
	private LinearLayout indicatorsLinearLayout;
	private List list;
	private TextView nameText;
	private CGData.CGRecommendData.CGRecommendItem recommendItem;
	private CGData.CGSGData.SGItem sgItem;

}
