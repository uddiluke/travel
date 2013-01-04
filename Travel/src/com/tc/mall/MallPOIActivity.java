// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.mall;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tc.TCPhotosActivity;
import com.tc.TCUtil;
import com.tc.logic.CGMarkData;
import com.tc.weibo.WeiboActivity;

// Referenced classes of package com.tc.mall:
//            MallCommentActivity, MallData, MallMapActivity

public class MallPOIActivity extends Activity implements android.view.View.OnClickListener {
	private class CGMarkDataMarkDataAsyncTask extends AsyncTask {

		protected CGMarkData doInBackground(MallData.MallPOIData.POI apoi[]) {
			return CGMarkData.cgMark(MallPOIActivity.this, MallData.MALL_APPLICATION, "mallshop".toLowerCase(), apoi[0].id);
		}

		protected Object doInBackground(Object aobj[]) {
			return doInBackground((MallData.MallPOIData.POI[]) aobj);
		}

		protected void onPostExecute(CGMarkData cgmarkdata) {

			if (cgmarkdata != null) {
				poi.commentCount = cgmarkdata.markCount;
				poi.commentGrade = cgmarkdata.avgerageMark;
				commentCountText.setText((new StringBuilder()).append(poi.commentCount).append(getString(0x7f080021)).toString());
				markRatingBar.setRating(poi.commentGrade / 2.0F);
			}
		}

		protected void onPostExecute(Object obj) {
			onPostExecute((CGMarkData) obj);
		}

	}

	public MallPOIActivity() {
	}

	public void onBackPressed() {
		super.onBackPressed();
	}

	public void onClick(View view) {
		Bundle bundle = new Bundle();
		switch (view.getId()) {
		case 2131361792:
			onBackPressed();
			break;
		case 2131362034:
			bundle.putSerializable("album_data", poi.albumData);
			TCUtil.startActivity(this, TCPhotosActivity.class, bundle);
			break;
		case 2131362047:
			String s;
			int i;
			int j;
			if (poi.isFavorite()) {
				poi.current = 0;
				s = getString(0x7f08002d);
				i = 0x7f020143;
			} else {
				poi.current = (int) (System.currentTimeMillis() / 1000L);
				s = getString(0x7f08002e);
				i = 0x7f020144;
			}
			j = 0;
			for (MallData.MallPOIData.POI poi1 : MallData.MallFavoriteData.POIS) {
				if (poi1.id == poi.id && poi1.typeId == poi.typeId) {
					MallData.MallFavoriteData.POIS.remove(j);
					MallData.MallFavoriteData.delete(this, poi.typeId, poi.id);
				}
			}

			if (poi.current > 0) {
				MallData.MallFavoriteData.POIS.add(poi);
				MallData.MallFavoriteData.insert(this, poi.typeId, poi.id, poi.current);
			}
			TCUtil.showToastBottom(this, s);
			favoriteButton.setImageResource(i);
			break;
		case 2131362048:
			bundle.putInt("poi_id", poi.id);
			bundle.putInt("poi_type", poi.typeId);
			TCUtil.startActivity(this, MallMapActivity.class, bundle);
			break;
		case 2131362056:
			bundle.putInt("poi_type", poi.typeId);
			bundle.putInt("poi_id", poi.id);
			TCUtil.startActivity(this, MallCommentActivity.class, bundle);
			break;
		case 2131362258:
			bundle.putBoolean("has_camera", false);
			bundle.putString("status", (new StringBuilder()).append(getString(0x7f080036)).append(MallData.MALL_NAME).append(",").append(poi.name)
					.append(". ").append(getString(0x7f080052)).toString());
			TCUtil.startActivity(this, WeiboActivity.class, bundle);
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f03004f);
		Bundle bundle1 = getIntent().getExtras();
		poi = MallData.getPOI(bundle1.getInt("poi_type"), bundle1.getInt("poi_id"));
		((TextView) findViewById(0x7f0a0001)).setText(poi.name);
		findViewById(0x7f0a0000).setOnClickListener(this);
		iconButton = (ImageView) findViewById(0x7f0a00f2);
		iconButton.setOnClickListener(this);
		nameText = (TextView) findViewById(0x7f0a00f3);
		floorText = (TextView) findViewById(0x7f0a01d3);
		commentCountText = (TextView) findViewById(0x7f0a0107);
		markRatingBar = (RatingBar) findViewById(0x7f0a0061);
		webView = (WebView) findViewById(0x7f0a0123);
		webView.setScrollBarStyle(0);
		favoriteButton = (ImageView) findViewById(0x7f0a00ff);
		android.graphics.Bitmap bitmap;
		android.graphics.Bitmap bitmap1;
		String s;
		CGMarkDataMarkDataAsyncTask cgmarkdatamarkdataasynctask;
		MallData.MallPOIData.POI apoi[];
		if (poi.isFavorite())
			favoriteButton.setImageResource(0x7f020144);
		else
			favoriteButton.setImageResource(0x7f020143);
		favoriteButton.setOnClickListener(this);
		weiboButton = (ImageView) findViewById(0x7f0a01d2);
		weiboButton.setOnClickListener(this);
		mapButton = (ImageView) findViewById(0x7f0a0100);
		mapButton.setOnClickListener(this);
		guideIcon = (ImageView) findViewById(0x7f0a0063);
		guideText = (TextView) findViewById(0x7f0a01d5);
		commentButton = (ImageButton) findViewById(0x7f0a0108);
		commentButton.setOnClickListener(this);
		bitmap = TCUtil.getBitmap(this, poi.listIcon);
		bitmap1 = TCUtil.getRoundedCornerBitmap(bitmap);
		TCUtil.recycleBitmap(bitmap);
		iconButton.setImageBitmap(bitmap1);
		nameText.setText(poi.name);
		commentCountText.setText((new StringBuilder()).append(poi.commentCount).append(getString(0x7f080021)).toString());
		markRatingBar.setRating(poi.commentGrade / 2.0F);
		s = (new StringBuilder()).append(MallData.getFloorName(poi.floorIndex)).append(getString(0x7f080032)).append(poi.number).toString();
		floorText.setText(s);
		guideIcon.setImageResource(0x7f020029);
		guideText.setText(getString(0x7f080024));
		TCUtil.loadUrl(this, webView, poi.url);
		cgmarkdatamarkdataasynctask = new CGMarkDataMarkDataAsyncTask();
		apoi = new MallData.MallPOIData.POI[1];
		apoi[0] = poi;
		cgmarkdatamarkdataasynctask.execute(apoi);
	}

	protected void onResume() {
		super.onResume();
		commentCountText.setText((new StringBuilder()).append(poi.commentCount).append(getString(0x7f080021)).toString());
		markRatingBar.setRating(poi.commentGrade / 2.0F);
	}

	public static final String KEY_POI_ID = "poi_id";
	public static final String KEY_POI_TYPE = "poi_type";
	private ImageButton commentButton;
	private TextView commentCountText;
	private ImageView favoriteButton;
	private TextView floorText;
	private ImageView guideIcon;
	private TextView guideText;
	private ImageView iconButton;
	private ImageView mapButton;
	private RatingBar markRatingBar;
	private TextView nameText;
	private MallData.MallPOIData.POI poi;
	private WebView webView;
	private ImageView weiboButton;

}
