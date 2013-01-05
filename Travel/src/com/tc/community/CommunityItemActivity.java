//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.community;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.tc.TCData;
import com.tc.TCUtil;
import com.tc.cg.CGBaseActivity;
import com.tc.cg.CGData;

// Referenced classes of package com.tc.community:
//            CommunityData, CommunityUtil, ReplyActivity, CommunityJSON

public class CommunityItemActivity extends CGBaseActivity implements android.view.View.OnClickListener {
	private class CommunityItemSimpeAdapter extends SimpleAdapter {

		private void bindView(int i, View view) {
			Map map = (Map) mData.get(i);
			if (map != null) {
				View aview[] = (View[]) (View[]) view.getTag();
				String as[] = mFrom;
				int j = mTo.length;
				int k = 0;
				while (k < j) {
					View view1 = aview[k];
					if (view1 != null) {
						Object obj = map.get(as[k]);
						if (view1 instanceof ImageView) {
							if (obj instanceof Boolean)
								if (!((Boolean) obj).booleanValue())
									view1.setVisibility(8);
								else
									view1.setVisibility(0);
						} else if (view1 instanceof TextView && (obj instanceof String)) {
							view1.setVisibility(0);
							((TextView) view1).setText((String) obj);
						}
					}
					k++;
				}
			}
		}

		private View getView(int i, View view, ViewGroup viewgroup, int j) {
			View view1;
			if (view == null) {
				view1 = mInflater.inflate(j, viewgroup, false);
				int ai[] = mTo;
				int k = ai.length;
				View aview[] = new View[k];
				for (int l = 0; l < k; l++)
					aview[l] = view1.findViewById(ai[l]);

				view1.setTag(aview);
			} else {
				view1 = view;
			}
			bindView(i, view1);
			return view1;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			return getView(i, view, viewgroup, mResource);
		}

		private List mData;
		private String mFrom[];
		private LayoutInflater mInflater;
		private int mResource;
		private int mTo[];

		public CommunityItemSimpeAdapter(Context context, List list, int i, String as[], int ai[]) {
			super(context, list, i, as, ai);
			mData = list;
			mResource = i;
			mFrom = as;
			mTo = ai;
			mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
		}
	}

	public CommunityItemActivity() {
	}

	private void refreshList() {
		isRefresh = true;
		hashMaps.clear();
		communityItemSimpeAdapter.notifyDataSetChanged();
		if (community_item_listview.getFooterViewsCount() == 0) {
			community_item_listview.addFooterView(community_item_listview_foot);
			communityItemSimpeAdapter.notifyDataSetChanged();
		}
		(new AsyncTask() {

			protected Object doInBackground(Object aobj[]) {

				hashMap = CommunityJSON.getReplies(CommunityItemActivity.this, CGData.CG_COMMUNITY_CODE, 0, communityData.id, 0, 20);
				return null;
			}

			protected void onPostExecute(Object obj) {

				if (hashMap != null) {
					if (hashMap.get("LIST_STATUS").equals("OK")) {
						communityData.reply = ((Integer) hashMap.get("TOTAL_COUNT")).intValue();
						timeline = ((Integer) hashMap.get("TIME_LINE")).intValue();
						communityDatas = (List) hashMap.get("COMMUNITY_LIST");
						Iterator iterator = communityDatas.iterator();
						while (iterator.hasNext()) {
							CommunityData communitydata = (CommunityData) iterator.next();
							HashMap hashmap = new HashMap();
							boolean flag;
							if (hashMaps.size() == 0)
								flag = true;
							else
								flag = false;
							hashmap.put("ishead", Boolean.valueOf(flag));
							hashmap.put("username", communitydata.userName);
							hashmap.put("date", communitydata.createTime.substring(0, 10));
							hashmap.put("community", communitydata.content);
							hashMaps.add(hashmap);
						}
						if (communityData.reply == hashMaps.size()) {
							if (community_item_listview.getFooterViewsCount() == 1)
								community_item_listview.removeFooterView(community_item_listview_foot);
						} else if (community_item_listview.getFooterViewsCount() == 0)
							community_item_listview.addFooterView(community_item_listview_foot);
						communityItemSimpeAdapter.notifyDataSetChanged();
						community_item_listview.setVisibility(0);
						community_item_community_count.setText((new StringBuilder()).append("").append(communityData.reply).toString());
					} else if (hashMap.get("LIST_STATUS").equals("099"))
						CommunityUtil.showCommunityToast(CommunityItemActivity.this, (String) hashMap.get("LIST_STATUS"));
				} else {
					CommunityUtil.showCommunityToast(CommunityItemActivity.this, "2");
				}
				isRefresh = false;
			}

			List communityDatas;
			HashMap hashMap;

		}).execute(null);
	}

	public void finish() {
		Intent intent = new Intent();
		intent.putExtra("COMMUNITY_DATA", communityData);
		setResult(0, intent);
		super.finish();
	}

	protected void onActivityResult(int i, int j, Intent intent) {
		super.onActivityResult(i, j, intent);
		if (intent != null) {
			CommunityData communitydata = communityData;
			communitydata.reply = j + communitydata.reply;
			community_item_community_count.setText((new StringBuilder()).append("").append(communityData.reply).toString());
			if (hashMaps.size() > 0) {
				((HashMap) hashMaps.get(0)).remove("ishead");
				((HashMap) hashMaps.get(0)).put("ishead", Boolean.valueOf(false));
			}
			HashMap hashmap = new HashMap();
			hashmap.put("ishead", Boolean.valueOf(true));
			String s;
			if (CommunityUtil.getTCNickName(this) == null)
				s = getString(0x7f080133);
			else
				s = CommunityUtil.getTCNickName(this);
			hashmap.put("username", s);
			hashmap.put("date", (new SimpleDateFormat(getString(0x7f080132))).format(new Date(System.currentTimeMillis())));
			hashmap.put("community", intent.getStringExtra("REPLY"));
			hashMaps.add(0, hashmap);
			communityItemSimpeAdapter.notifyDataSetChanged();
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case 2131362131:
			onBackPressed();
			break;
		case 2131362132:
			if (!isRefresh)
				refreshList();
			break;
		case 2131362134:
		case 2131362187:
		case 2131362188:
			if (!pressedZan) {
				pressedZan = true;
				FlurryAgent.logEvent("DoLikeForComment");
				if (CommunityUtil.getTCToken(this) != null) {
					if (zaned) {
						CommunityUtil.showCommunityToast(this, "7");
						pressedZan = false;
					} else {
						(new AsyncTask() {

							protected Object doInBackground(Object aobj[]) {

								statusCode = CommunityJSON.like(CommunityItemActivity.this, CGData.CG_COMMUNITY_CODE, communityData.id);
								return null;
							}

							protected void onPostExecute(Object obj) {
								pressedZan = false;
								if (statusCode != null && statusCode.charAt(-1 + statusCode.length()) == '_') {
									communityData.like = Integer.parseInt(statusCode.substring(0, -1 + statusCode.length()));
									community_item_zan_count.setText((new StringBuilder()).append("").append(communityData.like).toString());
									community_item_zan.setImageResource(0x7f020039);
									zaned = true;
									CommunityUtil.showCommunityToast(CommunityItemActivity.this, "15");
								} else {
									CommunityUtil.showCommunityToast(CommunityItemActivity.this, statusCode);
								}
							}

							protected void onPreExecute() {

								pressedZan = true;
							}

							String statusCode;

						}).execute(null);
					}
				} else if (zaned) {
					CommunityUtil.showCommunityToast(this, "7");
					pressedZan = false;
				} else if (CommunityUtil.getTCAnonymousToken(this) != null) {
					if (CommunityUtil.getTCAnonymousOptionCount(this) < 2) {
						(new AsyncTask() {

							protected Object doInBackground(Object aobj[]) {

								statusCode = CommunityJSON.like(CommunityItemActivity.this, CGData.CG_COMMUNITY_CODE, communityData.id);
								return null;
							}

							protected void onPostExecute(Object obj) {

								pressedZan = false;
								if (statusCode != null && statusCode.charAt(-1 + statusCode.length()) == '_') {
									communityData.like = Integer.parseInt(statusCode.substring(0, -1 + statusCode.length()));
									community_item_zan_count.setText((new StringBuilder()).append("").append(communityData.like).toString());
									community_item_zan.setImageResource(0x7f020039);
									zaned = true;
									CommunityUtil.setTCAnonymousOptionCount(CommunityItemActivity.this,
											1 + CommunityUtil.getTCAnonymousOptionCount(CommunityItemActivity.this));
									CommunityUtil.showCommunityToast(CommunityItemActivity.this, "15");
								} else {
									CommunityUtil.showCommunityToast(CommunityItemActivity.this, statusCode);
								}
							}

							protected void onPreExecute() {

								pressedZan = true;
							}

							String statusCode;

						}).execute(null);
					} else if (CommunityUtil.getTCAnonymousOptionCount(this) < 5) {
						CommunityUtil.showAnonymous2TimesDialog(this, CG_ID, new Handler() {

							public void handleMessage(Message message) {
								(new AsyncTask() {

									protected Object doInBackground(Object aobj[]) {

										statusCode = CommunityJSON.like(CommunityItemActivity.this, CGData.CG_COMMUNITY_CODE, communityData.id);
										return null;
									}

									protected void onPostExecute(Object obj) {

										pressedZan = false;
										if (statusCode != null && statusCode.charAt(-1 + statusCode.length()) == '_') {
											communityData.like = Integer.parseInt(statusCode.substring(0, -1 + statusCode.length()));
											community_item_zan_count.setText((new StringBuilder()).append("").append(communityData.like).toString());
											community_item_zan.setImageResource(0x7f020039);
											zaned = true;
											CommunityUtil.setTCAnonymousOptionCount(CommunityItemActivity.this,
													1 + CommunityUtil.getTCAnonymousOptionCount(CommunityItemActivity.this));
											CommunityUtil.showCommunityToast(CommunityItemActivity.this, "15");
										} else {
											CommunityUtil.showCommunityToast(CommunityItemActivity.this, statusCode);
										}
									}

									protected void onPreExecute() {
										super.onPreExecute();
										pressedZan = true;
									}

									String statusCode;

								}).execute(null);
							}

						});
						pressedZan = false;
					} else {
						CommunityUtil.showAnonymous5TimesDialog(this, CG_ID);
						pressedZan = false;
					}
				} else {
					CommunityUtil.showAnonymousFirstTimesDialog(this, CG_ID, new Handler() {

						public void handleMessage(Message message) {
							(new AsyncTask() {

								protected Object doInBackground(Object aobj[]) {

									statusCode = CommunityJSON.getAnonymousToken(CommunityItemActivity.this);
									return null;
								}

								protected void onPostExecute(Object obj) {

									if ("OK".equals(statusCode))
										(new AsyncTask() {

											protected Object doInBackground(Object aobj[]) {

												statusCode = CommunityJSON.like(CommunityItemActivity.this, CGData.CG_COMMUNITY_CODE,
														communityData.id);
												return null;
											}

											protected void onPostExecute(Object obj) {

												if (statusCode != null && statusCode.charAt(-1 + statusCode.length()) == '_') {
													communityData.like = Integer.parseInt(statusCode.substring(0, -1 + statusCode.length()));
													community_item_zan_count.setText((new StringBuilder()).append("").append(communityData.like)
															.toString());
													community_item_zan.setImageResource(0x7f020039);
													zaned = true;
													CommunityUtil.setTCAnonymousOptionCount(CommunityItemActivity.this,
															1 + CommunityUtil.getTCAnonymousOptionCount(CommunityItemActivity.this));
													CommunityUtil.showCommunityToast(CommunityItemActivity.this, "15");
												} else {
													CommunityUtil.showCommunityToast(CommunityItemActivity.this, statusCode);
												}
											}

											String statusCode;

										}).execute(null);
									else
										CommunityUtil.showCommunityToast(CommunityItemActivity.this, statusCode);
								}

								protected void onPreExecute() {
									super.onPreExecute();
									pressedZan = true;
								}

								String statusCode;

							}).execute(null);
						}

					});
					pressedZan = false;
				}
			}
			break;
		case 2131362135:
		case 2131362189:
		case 2131362190:
			FlurryAgent.logEvent("DoReplyForComment");
			Intent intent = new Intent(this, ReplyActivity.class);
			intent.putExtra("COMMUNITY_DATA", communityData);
			intent.putExtra("cg_id", CG_ID);
			intent.putExtra("ID", communityData.id);
			startActivityForResult(intent, 0);
			break;
		case 2131362184:
			if (!isLoadingBig) {
				isLoadingBig = true;
				final Dialog builder = new Dialog(this, 0x7f09000c);
				final View dialogView = TCUtil.getLayoutInflater(this).inflate(0x7f030037, null);
				final ProgressBar progressbar = (ProgressBar) dialogView.findViewById(0x7f0a015a);
				final TextView dialogTextView = (TextView) dialogView.findViewById(0x7f0a015b);
				final ImageView dialogImage = (ImageView) dialogView.findViewById(0x7f0a015c);
				android.view.WindowManager.LayoutParams layoutparams = builder.getWindow().getAttributes();
				layoutparams.width = TCData.SCREEN_WIDTH;
				layoutparams.height = TCData.SCREEN_HEIGHT;
				builder.getWindow().setAttributes(layoutparams);
				builder.onWindowAttributesChanged(layoutparams);
				builder.setCanceledOnTouchOutside(true);
				builder.setContentView(dialogView, new android.widget.RelativeLayout.LayoutParams(-20 + TCData.SCREEN_WIDTH, 400));
				(new AsyncTask() {

					protected Object doInBackground(Object aobj[]) {

						bitmap = CommunityUtil.getBigBitmap(CommunityItemActivity.this, communityData.img);
						return null;
					}

					protected void onPostExecute(Object obj) {

						if (bitmap != null) {
							progressbar.setVisibility(8);
							dialogImage.setImageBitmap(bitmap);
							dialogImage.setVisibility(0);
							builder.setContentView(dialogView, new android.widget.RelativeLayout.LayoutParams(-2, -2));
						} else {
							progressbar.setVisibility(8);
							dialogTextView.setText(getString(0x7f0800b5));
							dialogTextView.setVisibility(0);
							builder.setContentView(dialogView, new android.widget.RelativeLayout.LayoutParams(-20 + TCData.SCREEN_WIDTH, 400));
						}
						dialogView.setOnClickListener(new android.view.View.OnClickListener() {

							public void onClick(View view) {
								builder.cancel();
							}

						});
						isLoadingBig = false;
					}

					Bitmap bitmap;

				}).execute(null);
				builder.show();
			}
			break;

		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030036);
		communityData = (CommunityData) getIntent().getSerializableExtra("COMMUNITY_DATA");
		pressedZan = false;
		findViewById(0x7f0a0153).setOnClickListener(this);
		findViewById(0x7f0a0154).setOnClickListener(this);
		community_item_listview_head = (LinearLayout) getLayoutInflater().inflate(0x7f03003c, null);
		((TextView) community_item_listview_head.findViewById(0x7f0a0184)).setText(communityData.userName);
		((TextView) community_item_listview_head.findViewById(0x7f0a0185)).setText(getIntent().getStringExtra("POI_NAME"));
		((RatingBar) community_item_listview_head.findViewById(0x7f0a0186)).setRating(communityData.star / 2);
		((TextView) community_item_listview_head.findViewById(0x7f0a0187)).setText(communityData.content);
		if (communityData.img != null)
			(new AsyncTask() {

				protected Object doInBackground(Object aobj[]) {

					bitmap = CommunityUtil.getPlusBitmap(CommunityItemActivity.this, communityData.img);
					return null;
				}

				protected void onPostExecute(Object obj) {

					if (bitmap != null) {
						community_item_pic = (ImageView) community_item_listview_head.findViewById(0x7f0a0188);
						community_item_pic.setImageBitmap(bitmap);
						community_item_pic.setOnClickListener(CommunityItemActivity.this);
					}
				}

				Bitmap bitmap;

			}).execute(null);
		((TextView) community_item_listview_head.findViewById(0x7f0a0189)).setText(communityData.createTime.substring(0, 10));
		if (communityData.source != null)
			((TextView) community_item_listview_head.findViewById(0x7f0a018a)).setText((new StringBuilder()).append(getString(0x7f080173))
					.append(communityData.source).toString());
		else
			((TextView) community_item_listview_head.findViewById(0x7f0a018a)).setVisibility(8);
		community_item_zan_count = (TextView) community_item_listview_head.findViewById(0x7f0a018b);
		community_item_zan_count.setText((new StringBuilder()).append("").append(communityData.like).toString());
		community_item_zan_count.setOnClickListener(this);
		community_item_listview_head.findViewById(0x7f0a018c).setOnClickListener(this);
		community_item_community_count = (TextView) community_item_listview_head.findViewById(0x7f0a018d);
		community_item_community_count.setText((new StringBuilder()).append("").append(communityData.reply).toString());
		community_item_community_count.setOnClickListener(this);
		community_item_listview_head.findViewById(0x7f0a018e).setOnClickListener(this);
		community_item_zan = (ImageView) findViewById(0x7f0a0156);
		community_item_zan.setOnClickListener(this);
		community_item_reply = (ImageView) findViewById(0x7f0a0157);
		community_item_reply.setOnClickListener(this);
		community_item_listview = (ListView) findViewById(0x7f0a0158);
		community_item_listview_foot = (LinearLayout) getLayoutInflater().inflate(0x7f03003e, null);
		community_item_listview.addFooterView(community_item_listview_foot);
		hashMaps = new ArrayList();
		communityItemSimpeAdapter = new CommunityItemSimpeAdapter(this, hashMaps, 0x7f03003b, new String[] { "ishead", "username", "date",
				"community" }, new int[] { 0x7f0a017f, 0x7f0a0180, 0x7f0a0181, 0x7f0a0182 });
		community_item_listview.addHeaderView(community_item_listview_head);
		community_item_listview.setAdapter(communityItemSimpeAdapter);
		community_item_listview.setOnScrollListener(new android.widget.AbsListView.OnScrollListener() {

			public void onScroll(AbsListView abslistview, int i, int j, int k) {
				if (i + j == k && !isRefresh && hashMaps.size() < communityData.reply) {
					isRefresh = true;
					(new AsyncTask() {

						protected Object doInBackground(Object aobj[]) {

							hashMap = CommunityJSON.getReplies(CommunityItemActivity.this, CGData.CG_COMMUNITY_CODE, timeline, communityData.id,
									hashMaps.size(), 20);
							return null;
						}

						protected void onPostExecute(Object obj) {

							if (hashMap != null) { // goto _L2; else goto _L1
								if (hashMap.get("LIST_STATUS").equals("OK")) {
									communityData.reply = ((Integer) hashMap.get("TOTAL_COUNT")).intValue();
									communityDatas = (List) hashMap.get("COMMUNITY_LIST");
									Iterator iterator = communityDatas.iterator();
									while (iterator.hasNext()) {
										CommunityData communitydata = (CommunityData) iterator.next();
										HashMap hashmap = new HashMap();
										boolean flag;
										if (hashMaps.size() == 0)
											flag = true;
										else
											flag = false;
										hashmap.put("ishead", Boolean.valueOf(flag));
										hashmap.put("username", communitydata.userName);
										hashmap.put("date", communitydata.createTime.substring(0, 10));
										hashmap.put("community", communitydata.content);
										hashMaps.add(hashmap);
									}
									if (communityData.reply == hashMaps.size() && community_item_listview.getFooterViewsCount() == 1)
										community_item_listview.removeFooterView(community_item_listview_foot);
									communityItemSimpeAdapter.notifyDataSetChanged();
									community_item_community_count.setText((new StringBuilder()).append("").append(communityData.reply).toString());
								} else {
									if (hashMap.get("LIST_STATUS").equals("099"))
										CommunityUtil.showCommunityToast(CommunityItemActivity.this, (String) hashMap.get("LIST_STATUS"));
								}

							} else
								CommunityUtil.showCommunityToast(CommunityItemActivity.this, "2");

							isRefresh = false;

						}

						List communityDatas;
						HashMap hashMap;

					}).execute(null);
				}
			}

			public void onScrollStateChanged(AbsListView abslistview, int i) {
			}

		});
		refreshList();
	}

	public static final String COMMUNITY_DATA = "COMMUNITY_DATA";
	public static final String POI_NAME = "POI_NAME";
	private CommunityData communityData;
	private CommunityItemSimpeAdapter communityItemSimpeAdapter;
	private TextView community_item_community_count;
	private ListView community_item_listview;
	private LinearLayout community_item_listview_foot;
	private LinearLayout community_item_listview_head;
	private ImageView community_item_pic;
	private ImageView community_item_reply;
	private ImageView community_item_zan;
	private TextView community_item_zan_count;
	private ArrayList hashMaps;
	private boolean isLoadingBig;
	private boolean isRefresh;
	private boolean pressedZan;
	private int timeline;
	private boolean zaned;

	/*
	 * static boolean access$1002(CommunityItemActivity communityitemactivity,
	 * boolean flag) { communityitemactivity.isLoadingBig = flag; return flag; }
	 */

	/*
	 * static ImageView access$102(CommunityItemActivity communityitemactivity,
	 * ImageView imageview) { communityitemactivity.community_item_pic =
	 * imageview; return imageview; }
	 */

	/*
	 * static boolean access$1102(CommunityItemActivity communityitemactivity,
	 * boolean flag) { communityitemactivity.pressedZan = flag; return flag; }
	 */

	/*
	 * static boolean access$1402(CommunityItemActivity communityitemactivity,
	 * boolean flag) { communityitemactivity.zaned = flag; return flag; }
	 */

	/*
	 * static boolean access$302(CommunityItemActivity communityitemactivity,
	 * boolean flag) { communityitemactivity.isRefresh = flag; return flag; }
	 */

	/*
	 * static int access$502(CommunityItemActivity communityitemactivity, int i)
	 * { communityitemactivity.timeline = i; return i; }
	 */

}
