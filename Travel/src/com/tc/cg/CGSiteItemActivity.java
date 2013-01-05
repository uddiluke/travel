//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tc.TCApplication;
import com.tc.TCData;
import com.tc.TCPhotosActivity;
import com.tc.TCUtil;
import com.tc.community.CommentSquareAcivity;
import com.tc.community.CommunityActivity;
import com.tc.community.CommunityData;
import com.tc.community.CommunityJSON;
import com.tc.community.CommunityUtil;
import com.tc.google.GoogleMapActivity;
import com.tc.logic.CGMarkData;
import com.tc.mall.MallStartActivity;
import com.tc.sg.SGStartActivity;
import com.touchchina.cityguide.sz.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData, CGSGActivity

public class CGSiteItemActivity extends CGBaseActivity implements android.view.View.OnClickListener {
	private class CGMarkDataAsyncTask extends AsyncTask {

		protected CGMarkData doInBackground(CGData.CGSGData.SGItem asgitem[]) {
			return CGMarkData.cgMark(CGSiteItemActivity.this, CG_DATA.CG_APPLICATION, asgitem[0].type.toLowerCase(), asgitem[0].id);
		}

		protected Object doInBackground(Object aobj[]) {
			return doInBackground((CGData.CGSGData.SGItem[]) aobj);
		}

		protected void onPostExecute(CGMarkData cgmarkdata) {
			super.onPostExecute(cgmarkdata);
			if (cgmarkdata != null) {
				sgItem.commentCount = cgmarkdata.markCount;
				sgItem.commentGrade = cgmarkdata.avgerageMark;
				commentCountText.setText((new StringBuilder()).append(sgItem.commentCount).append(getString(0x7f080021)).toString());
				markRatingBar.setRating(sgItem.commentGrade / 2.0F);
			}
		}

		protected void onPostExecute(Object obj) {
			onPostExecute((CGMarkData) obj);
		}

	}

	public CGSiteItemActivity() {
		handler = new Handler() {

			public void handleMessage(Message message) {
				if (message.what == 0) {
					for (com.tc.TCApplication.DownloadingInfo downloadinginfo : ((TCApplication) getApplicationContext()).downloadingInfos) {
						if (downloadinginfo.guideid == sgItem.sgId) {
							TextView textview = webViewLayoutDownloadLayoutP;
							StringBuilder stringbuilder = (new StringBuilder()).append(getString(0x7f0801b7)).append(downloadinginfo.name)
									.append(getString(0x7f0801b8));
							String s;
							ProgressBar progressbar;
							int i;
							if (downloadinginfo.TOTAL == 0L)
								s = "0.0%";
							else
								s = (new StringBuilder())
										.append(TCUtil.formatNum((100D * (double) downloadinginfo.hasDownloaded) / (double) downloadinginfo.TOTAL))
										.append("%").toString();
							textview.setText(stringbuilder.append(s).toString());
							progressbar = webViewLayoutDownloadLayoutPB;
							if (downloadinginfo.TOTAL == 0L)
								i = 0;
							else
								i = (int) ((100D * (double) downloadinginfo.hasDownloaded) / (double) downloadinginfo.TOTAL);
							progressbar.setProgress(i);
							break;
						}
					}
					if (sgItem.timestamp > 0 && !TCUtil.isUnZipInterrapted("sg", sgItem.sgId, false)) {
						onResume();
						removeMessages(0);
					} else {
						sendEmptyMessage(0);
					}

				}

			}

		};

	}

	private boolean isDownloading() {
		if (((TCApplication) getApplicationContext()).downloadingInfos != null) { // goto
																					// _L2;
																					// else
																					// goto
																					// _L1
			for (com.tc.TCApplication.DownloadingInfo info : ((TCApplication) getApplicationContext()).downloadingInfos) {
				if (info.guideid == sgItem.sgId)
					return true;
			}
		}
		return false;

	}

	private void setTabSelected(int i) {
		int j = 0x7f06000f;
		tab_status[0] = false;
		tab_status[1] = false;
		tab_status[2] = false;
		tab_status[i] = true;
		TextView textview = middle2_tab1;
		int k;
		TextView textview1;
		Resources resources;
		int l;
		TextView textview2;
		int i1;
		TextView textview3;
		Resources resources1;
		int j1;
		TextView textview4;
		int k1;
		TextView textview5;
		Resources resources2;
		if (tab_status[0])
			k = 0x7f020043;
		else
			k = 0x7f020044;
		textview.setBackgroundResource(k);
		textview1 = middle2_tab1;
		resources = getResources();
		if (tab_status[0])
			l = j;
		else
			l = 0x7f060010;
		textview1.setTextColor(resources.getColor(l));
		textview2 = middle2_tab2;
		if (tab_status[1])
			i1 = 0x7f020045;
		else
			i1 = 0x7f020046;
		textview2.setBackgroundResource(i1);
		textview3 = middle2_tab2;
		resources1 = getResources();
		if (tab_status[1])
			j1 = j;
		else
			j1 = 0x7f060010;
		textview3.setTextColor(resources1.getColor(j1));
		textview4 = middle2_tab3;
		if (tab_status[2])
			k1 = 0x7f020048;
		else
			k1 = 0x7f020049;
		textview4.setBackgroundResource(k1);
		textview5 = middle2_tab3;
		resources2 = getResources();
		if (!tab_status[2])
			j = 0x7f060010;
		textview5.setTextColor(resources2.getColor(j));
	}

	public void onBackPressed() {
		super.onBackPressed();
	}

	public void onClick(View view) {
		Bundle bundle = new Bundle();
		switch (view.getId()) {
		case R.id.backButton:
			onBackPressed();
			break;
		case R.id.iconButton:
			bundle.putSerializable("album_data", sgItem.albumData);
			startActivity(this, TCPhotosActivity.class, bundle);
			break;
		case R.id.favoriteButton:
			TCUtil.flurryLog((new StringBuilder()).append(sgItem.type).append("ToFavor").toString(), sgItem.name);
			String s;
			int i;
			int j;
			if (sgItem.isFavorite()) {
				sgItem.current = 0;
				s = getString(0x7f08002d);
				i = 0x7f020143;
			} else {
				sgItem.current = (int) (System.currentTimeMillis() / 1000L);
				s = getString(0x7f08002e);
				i = 0x7f020144;
			}
			j = 0;
			for (CGData.CGFavoriteData.SGFavoriteItem sgfavoriteitem : CGData.CGFavoriteData.favoriteItems) {
				if (sgfavoriteitem.id == sgItem.id && sgfavoriteitem.type.equals(sgType)) {
					CGData.CGFavoriteData.favoriteItems.remove(j);
					CGData.CGFavoriteData.delete(this, CG_DATA, sgType, sgItem.id);
				}
				j++;
			}

			if (sgItem.current > 0) {
				CGData.CGFavoriteData.favoriteItems.add(new CGData.CGFavoriteData.SGFavoriteItem(sgItem.id, sgType, sgItem.current));
				CGData.CGFavoriteData.insert(this, CG_DATA, sgType, sgItem.id, sgItem.current);
			}
			TCUtil.showToastBottom(this, s);
			favoriteButton.setImageResource(i);
			break;
		case R.id.mapButton:
			if (TCData.GOOGLE_MAP_USEABLE) {
				CGData.CGMapData cgmapdata = new CGData.CGMapData();
				cgmapdata.name = sgItem.name;
				cgmapdata.icon = sgItem.logo;
				cgmapdata.lon = sgItem.lon;
				cgmapdata.lat = sgItem.lat;
				bundle.putSerializable("map_data", cgmapdata);
				startActivity(this, GoogleMapActivity.class, bundle);
			} else {
				TCUtil.showTip(this, 0x7f08000a);
			}
			break;
		case R.id.communityButton:
		case R.id.commentButton:
			Log.v("", (new StringBuilder()).append("--------------->").append(sgItem.commentGrade).toString());
			if (communited) {
				bundle.putInt("pointid", sgItem.id);
				bundle.putString("pointtype", sgType);
				bundle.putString("point_name", sgItem.name);
				bundle.putBoolean("POI_MINE_IMMEDIATELY", true);
				bundle.putInt("point_commentCount", sgItem.commentCount);
				bundle.putFloat("RATING_AVERAGE", sgItem.commentGrade);
				startActivity(this, CommentSquareAcivity.class, bundle);
			} else {
				bundle.putInt("cg_id", CG_ID);
				bundle.putBoolean("from_other_app", FROM_OTHER_APPLICATION);
				bundle.putInt("pointid", sgItem.id);
				bundle.putString("pointtype", sgType);
				bundle.putString("point_name", sgItem.name);
				bundle.putInt("point_commentCount", sgItem.commentCount);
				bundle.putFloat("RATING_AVERAGE", sgItem.commentGrade);
				Intent intent1 = new Intent(this, CommunityActivity.class);
				intent1.putExtras(bundle);
				startActivity(intent1);
			}
			break;
		case R.id.middle2_tab1:
			setTabSelected(0);
			communityLayout.setVisibility(8);
			webViewLayout.setVisibility(0);
			if (sgItem.isMain)
				webViewLayoutDownloadLayout.setVisibility(0);
			TCUtil.loadUrl(this, webView, sgItem.guideUrl);
			break;
		case R.id.middle2_tab2:
			setTabSelected(1);
			communityLayout.setVisibility(8);
			webViewLayout.setVisibility(0);
			webViewLayoutDownloadLayout.setVisibility(8);
			TCUtil.loadUrl(this, webView, sgItem.addressUrl);
			break;

		case R.id.middle2_tab3:
			setTabSelected(2);
			communityLayout.setVisibility(0);
			webViewLayout.setVisibility(8);
			(new AsyncTask() {

				protected Object doInBackground(Object aobj[]) {
					return doInBackground((Void[]) aobj);
				}

				protected Void doInBackground(Void avoid[]) {
					CGSiteItemActivity cgsiteitemactivity = CGSiteItemActivity.this;
					CGData _tmp = CG_DATA;
					hashMap = CommunityJSON.getCommentList(cgsiteitemactivity, CGData.CG_COMMUNITY_CODE, 0, 0, 1, "hot", sgType, sgItem.id);
					return null;
				}

				protected void onPostExecute(Object obj) {
					onPostExecute((Void) obj);
				}

				protected void onPostExecute(Void void1) {
					super.onPostExecute(void1);

					if (hashMap != null) { // goto _L2; else goto _L1
						if (hashMap.get("LIST_STATUS").equals("OK")) {
							communityDatas = (List) hashMap.get("COMMUNITY_LIST");
							if (communityDatas != null && communityDatas.size() > 0) {
								WannaBeFirstOne = (TextView) findViewById(0x7f0a011a);
								WannaBeFirstOne.setVisibility(8);
								findViewById(0x7f0a010f).setVisibility(0);
								communityLayoutButton.setText(0x7f080139);
								community_list_item1_community = (TextView) findViewById(0x7f0a0112);
								community_list_item1_date = (TextView) findViewById(0x7f0a0113);
								community_list_item1_pic = (ImageView) findViewById(0x7f0a0114);
								community_list_item1_username = (TextView) findViewById(0x7f0a0111);
								community_list_item1_zan_count = (TextView) findViewById(0x7f0a0116);
								community_list_item1_community_count = (TextView) findViewById(0x7f0a0118);
								CommunityData communitydata = (CommunityData) communityDatas.get(0);
								((RatingBar) findViewById(0x7f0a0110)).setRating(communitydata.star);
								community_list_item1_community.setText(communitydata.content);
								community_list_item1_date.setText(communitydata.createTime.substring(0, 10));
								ImageView imageview = community_list_item1_pic;
								byte byte0;
								if (communitydata.img == null)
									byte0 = 8;
								else
									byte0 = 0;
								imageview.setVisibility(byte0);
								community_list_item1_username.setText(communitydata.userName);
								community_list_item1_zan_count.setText((new StringBuilder()).append("").append(communitydata.like).toString());
								community_list_item1_community_count.setText((new StringBuilder()).append("").append(communitydata.reply).toString());
							}
						} else {
							if (hashMap.get("LIST_STATUS").equals("099"))
								CommunityUtil.setTD(CGSiteItemActivity.this);
						}
					}
				}

				protected void onPreExecute() {
					hashMap = null;
					communityDatas = null;
				}

				HashMap hashMap;

			}).execute(null);
			break;

		case R.id.communityLayoutButton:
			if (communityLayoutButton.getText().toString().trim().equals(getString(0x7f080138))) {
				bundle.putInt("cg_id", CG_ID);
				bundle.putBoolean("from_other_app", FROM_OTHER_APPLICATION);
				bundle.putInt("pointid", sgItem.id);
				bundle.putString("pointtype", sgType);
				bundle.putString("point_name", sgItem.name);
				bundle.putInt("point_commentCount", sgItem.commentCount);
				bundle.putFloat("RATING_AVERAGE", sgItem.commentGrade);
				Intent intent = new Intent(this, CommunityActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				bundle.putInt("pointid", sgItem.id);
				bundle.putString("pointtype", sgType);
				bundle.putString("point_name", sgItem.name);
				bundle.putInt("point_commentCount", sgItem.commentCount);
				bundle.putFloat("RATING_AVERAGE", sgItem.commentGrade);
				startActivity(this, CommentSquareAcivity.class, bundle);
			}
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030031);
		communited = false;
		Bundle bundle1 = getIntent().getExtras();
		sgType = bundle1.getString("sg_type");
		sgItem = CGData.getSGItem(CG_DATA, sgType, bundle1.getInt("sg_id"));
		isSG = sgType.equals("Site");
		if (sgItem.albumData == null) {
			sgItem.albumData = CGData.CGSGData.SGItem.AlbumParaser.parse(this, CG_DATA, sgType, sgItem.id);
			sgItem.albumData.name = (new StringBuilder()).append(sgItem.name).append(getString(0x7f080020)).toString();
		}
		((TextView) findViewById(0x7f0a0001)).setText(sgItem.name);
		findViewById(0x7f0a0000).setOnClickListener(this);
		iconButton = (ImageView) findViewById(0x7f0a00f2);
		iconButton.setOnClickListener(this);
		nameText = (TextView) findViewById(0x7f0a00f3);
		siteTypeText = (TextView) findViewById(0x7f0a0106);
		commentCountText = (TextView) findViewById(0x7f0a0107);
		markRatingBar = (RatingBar) findViewById(0x7f0a0061);
		priceRatingBar = (RatingBar) findViewById(0x7f0a005f);
		webViewLayout = (RelativeLayout) findViewById(0x7f0a011b);
		webViewLayoutDownloadLayout = (FrameLayout) findViewById(0x7f0a011c);
		webViewLayoutDownloadLayout0 = (LinearLayout) findViewById(0x7f0a011d);
		webViewLayoutDownloadLayout1 = (LinearLayout) findViewById(0x7f0a0120);
		webViewLayoutDownloadLayoutIV = (ImageView) findViewById(0x7f0a011e);
		webViewLayoutDownloadLayoutBT = (TextView) findViewById(0x7f0a011f);
		webViewLayoutDownloadLayoutP = (TextView) findViewById(0x7f0a0121);
		webViewLayoutDownloadLayoutPB = (ProgressBar) findViewById(0x7f0a0122);
		Bitmap bitmap;
		Bitmap bitmap1;
		if (sgItem.isMain)
			webViewLayoutDownloadLayout.setVisibility(0);
		else
			webViewLayoutDownloadLayout.setVisibility(8);
		webView = (WebView) findViewById(0x7f0a0123);
		webView.setScrollBarStyle(0);
		communityLayout = (RelativeLayout) findViewById(0x7f0a010d);
		favoriteButton = (ImageView) findViewById(0x7f0a00ff);
		if (sgItem.isFavorite())
			favoriteButton.setImageResource(0x7f020144);
		else
			favoriteButton.setImageResource(0x7f020143);
		favoriteButton.setOnClickListener(this);
		communityButton = (ImageView) findViewById(0x7f0a0101);
		communityButton.setOnClickListener(this);
		mapButton = (ImageView) findViewById(0x7f0a0100);
		mapButton.setOnClickListener(this);
		tab_status = (new boolean[] { true, false, false });
		middle2_tab1 = (TextView) findViewById(0x7f0a010a);
		middle2_tab1.setOnClickListener(this);
		middle2_tab2 = (TextView) findViewById(0x7f0a010b);
		middle2_tab2.setOnClickListener(this);
		middle2_tab3 = (TextView) findViewById(0x7f0a010c);
		middle2_tab3.setOnClickListener(this);
		commentButton = (ImageButton) findViewById(0x7f0a0108);
		commentButton.setOnClickListener(this);
		bitmap = TCUtil.getBitmap(this, sgItem.icon);
		bitmap1 = TCUtil.getRoundedCornerBitmap(bitmap);
		TCUtil.recycleBitmap(bitmap);
		iconButton.setImageBitmap(bitmap1);
		nameText.setText(sgItem.name);
		if (isSG) {
			siteTypeText.setText(sgItem.siteType);
			middle2_tab1.setText(getString(0x7f080022));
			middle2_tab2.setText(getString(0x7f080023));
			priceRatingBar.setVisibility(8);
		} else {
			siteTypeText.setVisibility(8);
			middle2_tab1.setText(getString(0x7f080024));
			middle2_tab2.setText(getString(0x7f080025));
			priceRatingBar.setRating(sgItem.price / 2.0F);
		}
		communityLayoutButton = (Button) findViewById(0x7f0a010e);
		communityLayoutButton.setText((new StringBuilder()).append("        ").append(getString(0x7f080138)).append("        ").toString());
		communityLayoutButton.setOnClickListener(this);
		TCUtil.loadUrl(this, webView, sgItem.guideUrl);
	}

	protected void onResume() {
		super.onResume();
		commentCountText.setText((new StringBuilder()).append(sgItem.commentCount).append(getString(0x7f080021)).toString());
		markRatingBar.setRating(sgItem.commentGrade / 2.0F);
		CGMarkDataAsyncTask cgmarkdataasynctask;
		CGData.CGSGData.SGItem asgitem[];
		if (sgItem.timestamp > 0 && !TCUtil.isUnZipInterrapted("sg", sgItem.sgId, false)) {
			webViewLayoutDownloadLayout0.setVisibility(0);
			webViewLayoutDownloadLayout1.setVisibility(8);
			Bitmap bitmap = TCUtil.getRoundedCornerBitmap(TCUtil.getBitmap(this,
					(new StringBuilder()).append(sgItem.icon.substring(0, sgItem.icon.lastIndexOf("icon.jpg"))).append("sgicon.jpg").toString()));
			Bitmap bitmap1 = TCUtil.scaleBitmap(bitmap, (2 * bitmap.getWidth()) / 3, (2 * bitmap.getHeight()) / 3);
			if (!TCData.USE_2X)
				bitmap1 = TCUtil.scaleBitmap(bitmap1, bitmap1.getWidth() / 2, bitmap1.getHeight() / 2);
			webViewLayoutDownloadLayout.setBackgroundDrawable(null);
			webViewLayoutDownloadLayoutIV.setImageBitmap(bitmap1);
			webViewLayoutDownloadLayoutBT.setText(0x7f0801b5);
			webViewLayoutDownloadLayoutBT.setTextColor(0xff888888);
			webViewLayoutDownloadLayoutBT.setTextSize(13F);
			webViewLayoutDownloadLayout.setOnClickListener(null);
			webViewLayoutDownloadLayoutIV.setOnClickListener(new android.view.View.OnClickListener() {

				public void onClick(View view) {
					if (sgItem.type.equals("Shopping")) {
						Bundle bundle = new Bundle();
						bundle.putInt(MallStartActivity.KEY_MALL_ID, sgItem.sgId);
						bundle.putString(MallStartActivity.KEY_MALL_APPLICATION, (new StringBuilder()).append(sgItem.type).append(sgItem.sgId)
								.toString());
						startActivity(CGSiteItemActivity.this, MallStartActivity.class, bundle);

					} else if (sgItem.type.equals("Site")) {
						TCUtil.flurryLog("SiteGuideStart", sgItem.name);
						Bundle bundle1 = new Bundle();
						bundle1.putInt("sg_id", sgItem.sgId);
						bundle1.putBoolean("from_other_app", false);
						startActivity(CGSiteItemActivity.this, SGStartActivity.class, bundle1);
					}
				}

			});
			handler.removeMessages(0);
		} else {
			if (isDownloading()) {
				webViewLayoutDownloadLayout0.setVisibility(8);
				webViewLayoutDownloadLayout1.setVisibility(0);
				webViewLayoutDownloadLayout.setBackgroundDrawable(null);
				handler.sendEmptyMessage(0);
			} else {
				webViewLayoutDownloadLayout0.setVisibility(0);
				webViewLayoutDownloadLayout1.setVisibility(8);
				webViewLayoutDownloadLayout.setBackgroundResource(0x7f02014c);
				webViewLayoutDownloadLayoutIV.setImageResource(0x7f02014b);
				webViewLayoutDownloadLayoutBT.setText((new StringBuilder()).append(getString(0x7f0801b6)).append("    ").toString());
				webViewLayoutDownloadLayoutBT.setTextColor(-1);
				webViewLayoutDownloadLayoutBT.setTextSize(17F);
				handler.removeMessages(0);
			}
			webViewLayoutDownloadLayout.setOnClickListener(new android.view.View.OnClickListener() {

				public void onClick(View view) {
					startActivity(CGSiteItemActivity.this, CGSGActivity.class);
				}

			});
		}
		cgmarkdataasynctask = new CGMarkDataAsyncTask();
		asgitem = new CGData.CGSGData.SGItem[1];
		asgitem[0] = sgItem;
		cgmarkdataasynctask.execute(asgitem);
		if (tab_status[2])
			(new AsyncTask() {

				protected Object doInBackground(Object aobj[]) {
					return doInBackground((Void[]) aobj);
				}

				protected Void doInBackground(Void avoid[]) {
					CGSiteItemActivity cgsiteitemactivity = CGSiteItemActivity.this;
					CGData _tmp = CG_DATA;
					hashMap = CommunityJSON.getCommentList(cgsiteitemactivity, CGData.CG_COMMUNITY_CODE, 0, 0, 1, "hot", sgType, sgItem.id);
					return null;
				}

				protected void onPostExecute(Object obj) {
					onPostExecute((Void) obj);
				}

				protected void onPostExecute(Void void1) {
					super.onPostExecute(void1);
					if (hashMap != null) {
						if (hashMap.get("LIST_STATUS").equals("OK")) {
							communityDatas = (List) hashMap.get("COMMUNITY_LIST");
							if (communityDatas != null && communityDatas.size() > 0) {
								WannaBeFirstOne = (TextView) findViewById(0x7f0a011a);
								WannaBeFirstOne.setVisibility(8);
								findViewById(0x7f0a010f).setVisibility(0);
								communityLayoutButton.setText(0x7f080139);
								community_list_item1_ratingbar = (RatingBar) findViewById(0x7f0a0110);
								community_list_item1_community = (TextView) findViewById(0x7f0a0112);
								community_list_item1_date = (TextView) findViewById(0x7f0a0113);
								community_list_item1_pic = (ImageView) findViewById(0x7f0a0114);
								community_list_item1_username = (TextView) findViewById(0x7f0a0111);
								community_list_item1_zan_count = (TextView) findViewById(0x7f0a0116);
								community_list_item1_community_count = (TextView) findViewById(0x7f0a0118);
								CommunityData communitydata = (CommunityData) communityDatas.get(0);
								community_list_item1_ratingbar.setRating(communitydata.star);
								community_list_item1_community.setText(communitydata.content);
								community_list_item1_date.setText(communitydata.createTime.substring(0, 10));
								ImageView imageview = community_list_item1_pic;
								byte byte0;
								if (communitydata.img == null)
									byte0 = 8;
								else
									byte0 = 0;
								imageview.setVisibility(byte0);
								community_list_item1_username.setText(communitydata.userName);
								community_list_item1_zan_count.setText((new StringBuilder()).append("").append(communitydata.like).toString());
								community_list_item1_community_count.setText((new StringBuilder()).append("").append(communitydata.reply).toString());
							}

						} else {

							if (hashMap.get("LIST_STATUS").equals("099"))
								CommunityUtil.setTD(CGSiteItemActivity.this);
						}
					}

				}

				protected void onPreExecute() {
					hashMap = null;
					communityDatas = null;
				}

				HashMap hashMap;

			}).execute(null);
		(new AsyncTask() {

			protected Object doInBackground(Object aobj[]) {
				return doInBackground((Void[]) aobj);
			}

			protected Void doInBackground(Void avoid[]) {
				CGSiteItemActivity cgsiteitemactivity = CGSiteItemActivity.this;
				CGData _tmp = CG_DATA;
				hashMap = CommunityJSON.getMyComments(cgsiteitemactivity, CGData.CG_COMMUNITY_CODE, 0, 0, 0, sgType, sgItem.id);
				return null;
			}

			protected void onPostExecute(Object obj) {
				onPostExecute((Void) obj);
			}

			protected void onPostExecute(Void void1) {
				super.onPostExecute(void1);
				if (hashMap != null && hashMap.get("LIST_STATUS").equals("OK") && ((Integer) hashMap.get("TOTAL_COUNT")).intValue() > 0)
					communited = true;
				else
					communited = false;
			}

			List datas;
			HashMap hashMap;

			{

				hashMap = null;
				datas = null;
			}
		}).execute(null);
	}

	public static final String KEY_SG_ID = "sg_id";
	public static final String KEY_SG_TYPE = "sg_type";
	private TextView WannaBeFirstOne;
	private ImageButton commentButton;
	private TextView commentCountText;
	private boolean communited;
	private ImageView communityButton;
	private List communityDatas;
	private RelativeLayout communityLayout;
	private Button communityLayoutButton;
	private TextView community_list_item1_community;
	private TextView community_list_item1_community_count;
	private TextView community_list_item1_date;
	private ImageView community_list_item1_pic;
	private RatingBar community_list_item1_ratingbar;
	private TextView community_list_item1_username;
	private TextView community_list_item1_zan_count;
	private ImageView favoriteButton;
	private Handler handler;
	private ImageView iconButton;
	private boolean isSG;
	private ImageView mapButton;
	private RatingBar markRatingBar;
	private TextView middle2_tab1;
	private TextView middle2_tab2;
	private TextView middle2_tab3;
	private TextView nameText;
	private RatingBar priceRatingBar;
	private CGData.CGSGData.SGItem sgItem;
	private String sgType;
	private TextView siteTypeText;
	private boolean tab_status[];
	private WebView webView;
	private RelativeLayout webViewLayout;
	private FrameLayout webViewLayoutDownloadLayout;
	private LinearLayout webViewLayoutDownloadLayout0;
	private LinearLayout webViewLayoutDownloadLayout1;
	private TextView webViewLayoutDownloadLayoutBT;
	private ImageView webViewLayoutDownloadLayoutIV;
	private TextView webViewLayoutDownloadLayoutP;
	private ProgressBar webViewLayoutDownloadLayoutPB;

	/*
	 * static TextView access$1002(CGSiteItemActivity cgsiteitemactivity,
	 * TextView textview) { cgsiteitemactivity.community_list_item1_date =
	 * textview; return textview; }
	 */

	/*
	 * static ImageView access$1102(CGSiteItemActivity cgsiteitemactivity,
	 * ImageView imageview) { cgsiteitemactivity.community_list_item1_pic =
	 * imageview; return imageview; }
	 */

	/*
	 * static TextView access$1202(CGSiteItemActivity cgsiteitemactivity,
	 * TextView textview) { cgsiteitemactivity.community_list_item1_username =
	 * textview; return textview; }
	 */

	/*
	 * static TextView access$1302(CGSiteItemActivity cgsiteitemactivity,
	 * TextView textview) { cgsiteitemactivity.community_list_item1_zan_count =
	 * textview; return textview; }
	 */

	/*
	 * static TextView access$1402(CGSiteItemActivity cgsiteitemactivity,
	 * TextView textview) {
	 * cgsiteitemactivity.community_list_item1_community_count = textview;
	 * return textview; }
	 */

	/*
	 * static boolean access$1502(CGSiteItemActivity cgsiteitemactivity, boolean
	 * flag) { cgsiteitemactivity.communited = flag; return flag; }
	 */

	/*
	 * static List access$402(CGSiteItemActivity cgsiteitemactivity, List list)
	 * { cgsiteitemactivity.communityDatas = list; return list; }
	 */

	/*
	 * static TextView access$602(CGSiteItemActivity cgsiteitemactivity,
	 * TextView textview) { cgsiteitemactivity.WannaBeFirstOne = textview;
	 * return textview; }
	 */

	/*
	 * static RatingBar access$802(CGSiteItemActivity cgsiteitemactivity,
	 * RatingBar ratingbar) { cgsiteitemactivity.community_list_item1_ratingbar
	 * = ratingbar; return ratingbar; }
	 */

	/*
	 * static TextView access$902(CGSiteItemActivity cgsiteitemactivity,
	 * TextView textview) { cgsiteitemactivity.community_list_item1_community =
	 * textview; return textview; }
	 */
}
