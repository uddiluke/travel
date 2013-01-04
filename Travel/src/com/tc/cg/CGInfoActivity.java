// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.cg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tc.TCHtmlActivity;
import com.tc.TCPhotosActivity;
import com.tc.TCUtil;
import com.touchchina.cityguide.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData

public class CGInfoActivity extends CGBaseActivity implements android.view.View.OnClickListener {
	private class InfoAdapter extends BaseAdapter {

		private Bitmap getBitmap(Context context1, String s) {
			Bitmap bitmap = null;
			if (bitmaps.containsKey(s))
				bitmap = (Bitmap) bitmaps.get(s);
			if (bitmap == null) {
				bitmap = TCUtil.getBitmap(context1, s);
				bitmaps.put(s, bitmap);
			}
			return bitmap;
		}

		public void clearBitmaps() {
			for (Iterator iterator = bitmaps.entrySet().iterator(); iterator.hasNext(); TCUtil.recycleBitmap((Bitmap) ((java.util.Map.Entry) iterator
					.next()).getValue()))
				;
			bitmaps.clear();
		}

		public int getCount() {
			return infoData.infoItems.size();
		}

		public Object getItem(int i) {
			return infoData.infoItems.get(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			if (view == null)
				view = TCUtil.getLayoutInflater(context).inflate(0x7f03000f, null);
			CGData.InfoData.InfoItem infoitem = (CGData.InfoData.InfoItem) getItem(i);
			((ImageView) view.findViewById(0x7f0a0066)).setImageBitmap(getBitmap(context, infoitem.icon));
			((TextView) view.findViewById(0x7f0a0067)).setText(infoitem.name);
			view.findViewById(0x7f0a0068).setVisibility(8);
			return view;
		}

		Map bitmaps;
		private Context context;
		private CGData.InfoData infoData;

		public InfoAdapter(Context context, CGData.InfoData infoData) {
			this.context = context;
			bitmaps = new HashMap();
			this.infoData = infoData;

		}
	}

	public CGInfoActivity() {
		itemListener = new android.widget.AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView adapterview, View view, int i, long l) {
				CGData.InfoData.InfoItem infoitem;
				Bundle bundle;
				infoitem = (CGData.InfoData.InfoItem) infoAdapter.getItem(i);
				TCUtil.flurryLog("cityIntroductItem", infoitem.name);
				bundle = new Bundle();
				if ("html".equals(infoitem.type)) {
					bundle.putString("title", infoitem.name);
					bundle.putString("url", infoitem.url);
					startActivity(CGInfoActivity.this, TCHtmlActivity.class, bundle);
				} else if ("album".equals(infoitem.type)) {
					bundle.putSerializable("album_data", CG_DATA.ALBUM_DATA);
					startActivity(CGInfoActivity.this, TCPhotosActivity.class, bundle);
				}

			}

		};

	}

	public void onClick(View view) {
		if (view.getId() == R.id.backButton) {
			onBackPressed();
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f03000e);
		infoData = CGData.getCGInfoData(CG_DATA, getIntent().getExtras().getString(KEY_INFO_TYPE));
		((TextView) findViewById(0x7f0a0001)).setText(infoData.chName);
		findViewById(0x7f0a0000).setOnClickListener(this);
		infoList = (ListView) findViewById(0x7f0a0065);
		infoList.setOnItemClickListener(itemListener);
		infoAdapter = new InfoAdapter(this, infoData);
		infoList.setAdapter(infoAdapter);
	}

	protected void onDestroy() {
		infoAdapter.clearBitmaps();
		super.onDestroy();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

	public static String KEY_INFO_TYPE = "info_type";
	private static final String TAG = CGInfoActivity.class.getSimpleName();
	private InfoAdapter infoAdapter;
	private CGData.InfoData infoData;
	private ListView infoList;
	private android.widget.AdapterView.OnItemClickListener itemListener;

}
