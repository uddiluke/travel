//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.community;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tc.TCUtil;
import com.tc.cg.CGBaseActivity;
import com.tc.cg.CGData;
import com.tc.cg.CGUtil;

// Referenced classes of package com.tc.community:
//            CommunityUtil, CommunityData, CommunityActivity, LoginRegisterActivity, 
//            RegisterActivity, CommunityItemActivity, CommunityJSON

public class CommentSquareAcivity extends CGBaseActivity implements android.view.View.OnClickListener {
	private class CommunityBaseAdapter extends BaseAdapter {

		public int getCount() {
			return communityDatas.size();
		}

		public Object getItem(int i) {
			return communityDatas.get(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			byte byte0;
			CommunityData communitydata;
			byte0 = 8;
			if (view == null) {
				LayoutInflater layoutinflater = TCUtil.getLayoutInflater(CommentSquareAcivity.this);
				ImageView imageview1;
				int j;
				if (pointId > 0)
					j = 0x7f030039;
				else
					j = 0x7f03003a;
				view = layoutinflater.inflate(j, null);
			}
			communitydata = (CommunityData) getItem(i);
			if (pointId > 0) { // goto _L2; else goto _L1

				((RatingBar) view.findViewById(0x7f0a0110)).setRating(communitydata.star / 2);
				((TextView) view.findViewById(0x7f0a0112)).setText(communitydata.content);
				((TextView) view.findViewById(0x7f0a0113)).setText(communitydata.createTime.substring(0, 10));
				ImageView imageview = (ImageView) view.findViewById(0x7f0a0114);
				if (communitydata.img != null)
					byte0 = 0;
				imageview.setVisibility(byte0);
				if (communitydata.source != null)
					((TextView) view.findViewById(0x7f0a0115)).setText((new StringBuilder()).append(getString(0x7f080173))
							.append(communitydata.source).toString());
				else
					((TextView) view.findViewById(0x7f0a0115)).setText("");
				((TextView) view.findViewById(0x7f0a0111)).setText(communitydata.userName);
				((TextView) view.findViewById(0x7f0a0116)).setText((new StringBuilder()).append("").append(communitydata.like).toString());
				if (communitydata.fresh > 0) {
					((ImageView) view.findViewById(0x7f0a0119)).setImageResource(0x7f02003d);
					((TextView) view.findViewById(0x7f0a0118)).setText((new StringBuilder()).append(getString(0x7f080167)).append("(")
							.append(communitydata.fresh).append(")").toString());
				} else {
					((ImageView) view.findViewById(0x7f0a0119)).setImageResource(0x7f02003c);
					((TextView) view.findViewById(0x7f0a0118)).setText((new StringBuilder()).append("").append(communitydata.reply).toString());
				}
			} else {
				TextView textview = (TextView) view.findViewById(0x7f0a0172);
				String s;
				Iterator iterator;
				ImageView imageview;
				if (tab_status[2])
					s = getString(0x7f080172);
				else
					s = communitydata.userName;
				textview.setText(s);
				iterator = CGData.getCGSGData(CG_DATA, CommunityUtil.communityPointType2SgType(communitydata.pointType)).sgItems.iterator();
				do {
					if (!iterator.hasNext())
						break;
					com.tc.cg.CGData.CGSGData.SGItem sgitem = (com.tc.cg.CGData.CGSGData.SGItem) iterator.next();
					if (sgitem.id != communitydata.pointId)
						continue;
					((TextView) view.findViewById(0x7f0a0176))
							.setText((new StringBuilder()).append("\"").append(sgitem.name).append("\"").toString());
					break;
				} while (true);
				((TextView) view.findViewById(0x7f0a0177)).setText(communitydata.content);
				((TextView) view.findViewById(0x7f0a0178)).setText(communitydata.createTime.substring(0, 10));
				((RatingBar) view.findViewById(0x7f0a0173)).setRating(communitydata.star / 2);
				imageview = (ImageView) view.findViewById(0x7f0a0179);
				if (communitydata.img != null)
					byte0 = 0;
				imageview.setVisibility(byte0);
				if (communitydata.source != null)
					((TextView) view.findViewById(0x7f0a017a)).setText((new StringBuilder()).append(getString(0x7f080173))
							.append(communitydata.source).toString());
				else
					((TextView) view.findViewById(0x7f0a017a)).setText("");
				((TextView) view.findViewById(0x7f0a017b)).setText((new StringBuilder()).append("").append(communitydata.like).toString());
				if (communitydata.fresh > 0) {
					((ImageView) view.findViewById(0x7f0a017e)).setImageResource(0x7f02003d);
					((TextView) view.findViewById(0x7f0a017d)).setText((new StringBuilder()).append(getString(0x7f080167)).append("(")
							.append(communitydata.fresh).append(")").toString());
				} else {
					((ImageView) view.findViewById(0x7f0a017e)).setImageResource(0x7f02003c);
					((TextView) view.findViewById(0x7f0a017d)).setText((new StringBuilder()).append("").append(communitydata.reply).toString());
				}
			}

			return view;

		}

		private List communityDatas;

		public CommunityBaseAdapter(List list) {

			communityDatas = list;
		}
	}

	public CommentSquareAcivity() {
		broadcastReceiver = new BroadcastReceiver() {

			public void onReceive(Context context, Intent intent) {
				if ("com.touchchina.action.TC_LOGOUT".equals(intent.getAction()))
					mine = null;
			}

		};
	}

	private void setTabSelected(int i) {
		int j = 0x7f06000f;
		tab_status[0] = false;
		tab_status[1] = false;
		tab_status[2] = false;
		tab_status[i] = true;
		TextView textview = communitySquare_tab1;
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
		textview1 = communitySquare_tab1;
		resources = getResources();
		if (tab_status[0])
			l = j;
		else
			l = 0x7f060010;
		textview1.setTextColor(resources.getColor(l));
		textview2 = communitySquare_tab2;
		if (tab_status[1])
			i1 = 0x7f020045;
		else
			i1 = 0x7f020046;
		textview2.setBackgroundResource(i1);
		textview3 = communitySquare_tab2;
		resources1 = getResources();
		if (tab_status[1])
			j1 = j;
		else
			j1 = 0x7f060010;
		textview3.setTextColor(resources1.getColor(j1));
		textview4 = communitySquare_tab3;
		if (tab_status[2])
			k1 = 0x7f020048;
		else
			k1 = 0x7f020049;
		textview4.setBackgroundResource(k1);
		textview5 = communitySquare_tab3;
		resources2 = getResources();
		if (!tab_status[2])
			j = 0x7f060010;
		textview5.setTextColor(resources2.getColor(j));
		refreshCommunity.setVisibility(0);
		communitySquare_no_community.setVisibility(8);
		communitySquare_listview.setVisibility(8);
		communitySquare_wait.setVisibility(8);
		login_layut.setVisibility(8);
		if (pointId <= 0)
			if (i == 2 && CommunityUtil.getTCNickName(this) != null)
				communitySquareTitle.setText(CommunityUtil.getTCNickName(this));
			else
				communitySquareTitle.setText(0x7f080149);
	}

	private void showHot() {
		communitySquare_listview.setVisibility(0);
		if (hots.size() == 0) { // goto _L2; else goto _L1
			communitySquare_listview.setVisibility(8);
			communitySquare_no_community.setVisibility(0);
			if (pointId > 0)
				communitySquare_no_community_text.setText(0x7f080135);
			else
				communitySquare_no_community_text.setText(0x7f080136);
		} else {
			communitySquare_no_community.setVisibility(8);
			communityBaseAdapter = new CommunityBaseAdapter(hots);
			communitySquare_listview.setAdapter(communityBaseAdapter);
			if (hot_total == hots.size()) {

				if (communitySquare_listview.getFooterViewsCount() == 1)
					communitySquare_listview.removeFooterView(communitySquare_listview_foot);
			} else if (communitySquare_listview.getFooterViewsCount() == 0)
				communitySquare_listview.addFooterView(communitySquare_listview_foot);

			communitySquare_listview.setAdapter(communityBaseAdapter);
		}

	}

	private void showMine() {
		communitySquare_listview.setVisibility(0);
		if (mine.size() == 0) {// goto _L2; else goto _L1
			communitySquare_listview.setVisibility(8);
			communitySquare_no_community.setVisibility(0);
			communitySquare_no_community_text.setText(0x7f080136);
			if (pointId > 0)
				communitySquare_no_community_text.setText(0x7f080137);
			else
				communitySquare_no_community_text.setText(0x7f080136);
		} else {
			communitySquare_no_community.setVisibility(8);
			communityBaseAdapter = new CommunityBaseAdapter(mine);
			communitySquare_listview.setAdapter(communityBaseAdapter);
			if (mine_total == mine.size()) {

				if (communitySquare_listview.getFooterViewsCount() == 1)
					communitySquare_listview.removeFooterView(communitySquare_listview_foot);
			} else if (communitySquare_listview.getFooterViewsCount() == 0)
				communitySquare_listview.addFooterView(communitySquare_listview_foot);

			communitySquare_listview.setAdapter(communityBaseAdapter);
		}
	}

	private void showNew() {
		communitySquare_listview.setVisibility(0);
		if (news.size() == 0) { // goto _L2; else goto _L1
			communitySquare_listview.setVisibility(8);
			communitySquare_no_community.setVisibility(0);
			if (pointId > 0)
				communitySquare_no_community_text.setText(0x7f080135);
			else
				communitySquare_no_community_text.setText(0x7f080136);
		} else {
			communitySquare_no_community.setVisibility(8);
			communityBaseAdapter = new CommunityBaseAdapter(news);
			communitySquare_listview.setAdapter(communityBaseAdapter);
			if (new_total == news.size()) {

				if (communitySquare_listview.getFooterViewsCount() == 1)
					communitySquare_listview.removeFooterView(communitySquare_listview_foot);
			} else if (communitySquare_listview.getFooterViewsCount() == 0)
				communitySquare_listview.addFooterView(communitySquare_listview_foot);
			communitySquare_listview.setAdapter(communityBaseAdapter);
		}

	}

	private void updateHot() {
		(new AsyncTask() {

			protected Object doInBackground(Object aobj[]) {

				hashMap = CommunityJSON.getCommentList(CommentSquareAcivity.this, CGData.CG_COMMUNITY_CODE, 0, 0, 20, "hot", sgType, pointId);
				updateNoRead();
				return null;
			}

			protected void onPostExecute(Object[] obj) {
				communitySquare_wait.setVisibility(8);
				if (hashMap != null) // goto _L2; else goto _L1
				{
					if (hashMap.get("LIST_STATUS").equals("OK")) {
						hot_total = ((Integer) hashMap.get("TOTAL_COUNT")).intValue();
						hot_timeline = ((Integer) hashMap.get("TIME_LINE")).intValue();
						hots = (List<CommunityData>) hashMap.get("COMMUNITY_LIST");
						if (tab_status[0])
							showHot();
					} else {
						if (hashMap.get("LIST_STATUS").equals("099"))
							CommunityUtil.showCommunityToast(CommentSquareAcivity.this, (String) hashMap.get("LIST_STATUS"));
					}
				} else {
					CommunityUtil.showCommunityToast(CommentSquareAcivity.this, "2");
				}
				isRefreshing[0] = false;

			}

			protected void onPreExecute() {
				isRefreshing[0] = true;
				communitySquare_wait.setVisibility(0);
			}

			HashMap hashMap;

		}).execute(null);
	}

	private void updateMine() {
		(new AsyncTask() {

			protected Object doInBackground(Object aobj[]) {

				hashMap = CommunityJSON.getMyComments(CommentSquareAcivity.this, CGData.CG_COMMUNITY_CODE, 0, 0, 20, sgType, pointId);
				updateNoRead();
				return null;
			}

			protected void onPostExecute(Object obj) {

				communitySquare_wait.setVisibility(8);
				if (hashMap != null) {
					if (hashMap.get("LIST_STATUS").equals("OK")) {
						mine_total = ((Integer) hashMap.get("TOTAL_COUNT")).intValue();
						mine_timeline = ((Integer) hashMap.get("TIME_LINE")).intValue();
						mine = (List) hashMap.get("COMMUNITY_LIST");
						if (tab_status[2] && CommunityUtil.getTCToken(CommentSquareAcivity.this) != null)
							showMine();
					} else {
						CommunityUtil.showCommunityToast(CommentSquareAcivity.this, "11");
					}
				} else {
					CommunityUtil.showCommunityToast(CommentSquareAcivity.this, "2");
				}
				isRefreshing[2] = false;
			}

			protected void onPreExecute() {
				isRefreshing[2] = true;
				communitySquare_wait.setVisibility(0);
			}

			HashMap hashMap;

		}).execute(null);
	}

	private void updateNew() {
		(new AsyncTask() {

			protected Object doInBackground(Object aobj[]) {

				hashMap = CommunityJSON.getCommentList(CommentSquareAcivity.this, CGData.CG_COMMUNITY_CODE, 0, 0, 20, "new", sgType, pointId);
				updateNoRead();
				return null;
			}

			protected void onPostExecute(Object[] obj) {

				communitySquare_wait.setVisibility(8);
				if (hashMap != null) {
					if (hashMap.get("LIST_STATUS").equals("OK")) {
						new_total = ((Integer) hashMap.get("TOTAL_COUNT")).intValue();
						new_timeline = ((Integer) hashMap.get("TIME_LINE")).intValue();
						news = (List) hashMap.get("COMMUNITY_LIST");
						if (tab_status[1])
							showNew();
					} else if (hashMap.get("LIST_STATUS").equals("099"))
						CommunityUtil.showCommunityToast(CommentSquareAcivity.this, (String) hashMap.get("LIST_STATUS"));
				} else
					CommunityUtil.showCommunityToast(CommentSquareAcivity.this, "2");
				isRefreshing[1] = false;
			}

			protected void onPreExecute() {
				isRefreshing[1] = true;
				communitySquare_wait.setVisibility(0);
			}

			HashMap hashMap;

		}).execute(null);
	}

	private void updateNoRead() {
		if (CommunityUtil.getTCToken(this) == null)
			communitySquare_tab3_count.setVisibility(8);
		else
			(new AsyncTask() {

				protected Object doInBackground(Object aobj[]) {

					count = CommunityJSON.getMyNoReads(CommentSquareAcivity.this, CGData.CG_COMMUNITY_CODE, sgType, pointId);
					return null;
				}

				protected void onPostExecute(Object obj) {

					if (count > 0) {
						communitySquare_tab3_count.setVisibility(0);
						communitySquare_tab3_count.setImageBitmap(CommunityUtil.int2Icon(CommentSquareAcivity.this, count));
					} else {
						communitySquare_tab3_count.setVisibility(8);
					}
				}

				int count;

			}).execute(null);
	}

	protected void onActivityResult(int i, int j, Intent intent) {
		if (i == 0) {
			CommunityData communitydata;

			communitydata = (CommunityData) intent.getSerializableExtra("COMMUNITY_DATA");
			if (hots != null) {
				for (CommunityData tmp : hots) {
					if (tmp.id == communitydata.id) {
						tmp.reply = communitydata.reply;
						tmp.like = communitydata.like;
						break;
					}
				}
			}

			if (news != null) {
				for (CommunityData tmp : news) {
					if (tmp.id == communitydata.id) {
						tmp.reply = communitydata.reply;
						tmp.like = communitydata.like;
						break;
					}
				}
			}

			if (tab_status[2]) {
				setTabSelected(2);
				updateMine();
			} else {
				communityBaseAdapter.notifyDataSetChanged();
			}

		} else if (i == 1) {

			for (int k = 0; i < tab_status.length; i++) {
				if (tab_status[k]) {
					setTabSelected(k);
					break;
				}
			}
			updateHot();
			updateNew();
			if (CommunityUtil.getTCToken(this) != null)
				updateMine();

		} else if (i == 2) {
			setTabSelected(2);
			communitySquare_tab3_count.setVisibility(8);
			if (CommunityUtil.getTCToken(this) != null) {
				if (mine != null)
					showMine();
				else
					updateMine();
			} else {
				communitySquare_listview.setVisibility(8);
				refreshCommunity.setVisibility(8);
				login_layut.setVisibility(0);
			}
		}

	}

	public void onBackPressed() {
		if (pointId > 0)
			super.onBackPressed();
		else
			CGUtil.showExitDialog(this);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case 2131362193:
			if (tab_status[0]) {
				if (!isRefreshing[0]) {
					setTabSelected(0);
					updateHot();
				}
			} else if (tab_status[1]) {
				if (!isRefreshing[1]) {
					setTabSelected(1);
					updateNew();
				}
			} else if (tab_status[2] && !isRefreshing[2]) {
				setTabSelected(2);
				updateMine();
			}

			break;
		case 2131362197:

			setTabSelected(0);
			if (hots != null)
				showHot();
			else
				updateHot();

			break;
		case 2131362198:
			setTabSelected(1);
			if (news != null)
				showNew();
			else
				updateNew();

			break;
		case 2131362199:

			setTabSelected(2);
			communitySquare_tab3_count.setVisibility(8);
			if (CommunityUtil.getTCToken(this) != null) {
				if (mine != null) {
					if (pointId > 0 && mine.size() == 0) {
						Bundle bundle1 = new Bundle();
						bundle1.putInt("pointid", pointId);
						bundle1.putString("pointtype", sgType);
						bundle1.putString("point_name", pointName);
						bundle1.putInt("point_commentCount", getIntent().getIntExtra("point_commentCount", 0));
						bundle1.putFloat("RATING_AVERAGE", getIntent().getFloatExtra("RATING_AVERAGE", 0.0F));
						startActivityForResult(this, CommunityActivity.class, bundle1, 1);
						resumeRefresh = false;
					} else {
						showMine();
					}
				} else {
					if (pointId > 0) {
						Bundle bundle = new Bundle();
						bundle.putInt("pointid", pointId);
						bundle.putString("pointtype", sgType);
						bundle.putString("point_name", pointName);
						bundle.putInt("point_commentCount", getIntent().getIntExtra("point_commentCount", 0));
						bundle.putFloat("RATING_AVERAGE", getIntent().getFloatExtra("RATING_AVERAGE", 0.0F));
						startActivityForResult(this, CommunityActivity.class, bundle, 1);
						resumeRefresh = false;
					}
					updateMine();
				}
			} else {
				refreshCommunity.setVisibility(8);
				login_layut.setVisibility(0);
			}

			break;
		case 2131362206:
			startActivityForResult(this, LoginRegisterActivity.class, new Bundle(), 2);
			resumeRefresh = false;

			break;
		case 2131362208:
			startActivityForResult(this, RegisterActivity.class, new Bundle(), 2);
			resumeRefresh = false;
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f03003d);
		tab_status = (new boolean[] { true, false, false });
		isRefreshing = (new boolean[] { true, false, false });
		pointId = getIntent().getIntExtra("pointid", -1);
		sgType = getIntent().getStringExtra("pointtype");
		pointName = getIntent().getStringExtra("point_name");
		backButton = (ImageView) findViewById(0x7f0a0153);
		if (pointId < 0) {
			backButton.setVisibility(8);
			findViewById(0x7f0a0192).setVisibility(8);
		} else {
			findViewById(0x7f0a0193).setOnClickListener(new android.view.View.OnClickListener() {

				public void onClick(View view) {
					Bundle bundle1 = new Bundle();
					bundle1.putInt("pointid", pointId);
					bundle1.putString("pointtype", sgType);
					bundle1.putString("point_name", pointName);
					bundle1.putInt("point_commentCount", getIntent().getIntExtra("point_commentCount", 0));
					bundle1.putFloat("RATING_AVERAGE", getIntent().getFloatExtra("RATING_AVERAGE", 0.0F));
					startActivityForResult(CommentSquareAcivity.this, CommunityActivity.class, bundle1, 1);
					resumeRefresh = false;
				}

			});
		}
		backButton.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				onBackPressed();
			}

		});
		refreshCommunity = (ImageView) findViewById(0x7f0a0191);
		refreshCommunity.setOnClickListener(this);
		communitySquare_tab1 = (TextView) findViewById(0x7f0a0195);
		communitySquare_tab1.setOnClickListener(this);
		communitySquare_tab2 = (TextView) findViewById(0x7f0a0196);
		communitySquare_tab2.setOnClickListener(this);
		communitySquare_tab3 = (TextView) findViewById(0x7f0a0197);
		communitySquare_tab3.setOnClickListener(this);
		communitySquare_tab3_count = (ImageView) findViewById(0x7f0a0198);
		communitySquare_no_community = (LinearLayout) findViewById(0x7f0a019a);
		communitySquare_no_community_text = (TextView) findViewById(0x7f0a019b);
		communitySquare_listview = (ListView) findViewById(0x7f0a0199);
		communitySquare_listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView adapterview, View view, int i, long l) {
				CommunityData communitydata = null;
				Intent intent;
				if (tab_status[0])
					communitydata = (CommunityData) hots.get(i);
				else if (tab_status[1])
					communitydata = (CommunityData) news.get(i);
				else if (tab_status[2])
					communitydata = (CommunityData) mine.get(i);
				communitydata.fresh = 0;
				intent = new Intent(CommentSquareAcivity.this, CommunityItemActivity.class);
				if (pointName != null)
					intent.putExtra("POI_NAME", pointName);
				else
					intent.putExtra("POI_NAME", ((TextView) view.findViewById(0x7f0a0176)).getText());
				intent.putExtra("COMMUNITY_DATA", communitydata);
				intent.putExtra("cg_id", CG_ID);
				intent.putExtra("from_other_app", FROM_OTHER_APPLICATION);
				startActivityForResult(intent, 0);
				resumeRefresh = false;
			}

		});
		hot_total = 0;
		new_total = 0;
		mine_total = 0;
		hot_timeline = 0;
		new_timeline = 0;
		mine_timeline = 0;
		communitySquare_listview_foot = (LinearLayout) getLayoutInflater().inflate(0x7f03003e, null);
		if (communitySquare_listview.getFooterViewsCount() == 0)
			communitySquare_listview.addFooterView(communitySquare_listview_foot);
		communitySquare_listview.setOnScrollListener(new android.widget.AbsListView.OnScrollListener() {

			public void onScroll(AbsListView abslistview, int i, int j, int k) {
				if (i + j == k) {
					if (!tab_status[0] || isRefreshing[0] || hots == null || hots.size() >= hot_total) {
						if (tab_status[1] && !isRefreshing[1] && news != null && news.size() < new_total)
							(new AsyncTask() {

								protected Object doInBackground(Object aobj[]) {

									hashMap = CommunityJSON.getCommentList(CommentSquareAcivity.this, CGData.CG_COMMUNITY_CODE, new_timeline,
											news.size(), 20, "new", sgType, pointId);
									return null;
								}

								protected void onPostExecute(Object obj) {

									if (hashMap != null) {
										if (hashMap.get("LIST_STATUS").equals("OK")) {
											new_total = ((Integer) hashMap.get("TOTAL_COUNT")).intValue();
											datas = (List) hashMap.get("COMMUNITY_LIST");
											CommunityData communitydata;
											for (Iterator iterator = datas.iterator(); iterator.hasNext(); news.add(communitydata))
												communitydata = (CommunityData) iterator.next();

											if (tab_status[1]) {
												if (news.size() == new_total) {
													if (communitySquare_listview.getFooterViewsCount() == 1)
														communitySquare_listview.removeFooterView(communitySquare_listview_foot);
												} else if (communitySquare_listview.getFooterViewsCount() == 0)
													communitySquare_listview.addFooterView(communitySquare_listview_foot);
												communityBaseAdapter.notifyDataSetChanged();
											}
										} else if (hashMap.get("LIST_STATUS").equals("099"))
											CommunityUtil.showCommunityToast(CommentSquareAcivity.this, (String) hashMap.get("LIST_STATUS"));
									} else {
										CommunityUtil.showCommunityToast(CommentSquareAcivity.this, "11");
									}
									isRefreshing[1] = false;
								}

								protected void onPreExecute() {
									super.onPreExecute();
									isRefreshing[1] = true;
								}

								List datas;
								HashMap hashMap;

							}).execute(null);
						else if (tab_status[2] && !isRefreshing[2] && mine != null && mine.size() < mine_total)
							(new AsyncTask() {

								protected Object doInBackground(Object aobj[]) {

									hashMap = CommunityJSON.getMyComments(CommentSquareAcivity.this, CGData.CG_COMMUNITY_CODE, mine_timeline,
											mine.size(), 20, sgType, pointId);
									return null;
								}

								protected void onPostExecute(Object obj) {

									if (hashMap != null) {
										if (hashMap.get("LIST_STATUS").equals("OK")) {
											mine_total = ((Integer) hashMap.get("TOTAL_COUNT")).intValue();
											datas = (List) hashMap.get("COMMUNITY_LIST");
											CommunityData communitydata;
											for (Iterator iterator = datas.iterator(); iterator.hasNext(); mine.add(communitydata))
												communitydata = (CommunityData) iterator.next();

											if (tab_status[2] && CommunityUtil.getTCToken(CommentSquareAcivity.this) != null) {
												if (mine.size() == mine_total) {
													if (communitySquare_listview.getFooterViewsCount() == 1)
														communitySquare_listview.removeFooterView(communitySquare_listview_foot);
												} else if (communitySquare_listview.getFooterViewsCount() == 0)
													communitySquare_listview.addFooterView(communitySquare_listview_foot);
												communityBaseAdapter.notifyDataSetChanged();
											}
										} else if (hashMap.get("LIST_STATUS").equals("099"))
											CommunityUtil.showCommunityToast(CommentSquareAcivity.this, (String) hashMap.get("LIST_STATUS"));
									} else {
										CommunityUtil.showCommunityToast(CommentSquareAcivity.this, "11");
									}
									isRefreshing[2] = false;
								}

								protected void onPreExecute() {
									super.onPreExecute();
									isRefreshing[2] = true;
								}

								List datas;
								HashMap hashMap;

							}).execute(null);
					} else {
						(new AsyncTask() {

							protected Object doInBackground(Object aobj[]) {

								hashMap = CommunityJSON.getCommentList(CommentSquareAcivity.this, CGData.CG_COMMUNITY_CODE, hot_timeline,
										hots.size(), 20, "hot", sgType, pointId);
								return null;
							}

							protected void onPostExecute(Object obj) {

								if (hashMap != null) {
									if (hashMap.get("LIST_STATUS").equals("OK")) {
										hot_total = ((Integer) hashMap.get("TOTAL_COUNT")).intValue();
										datas = (List) hashMap.get("COMMUNITY_LIST");
										CommunityData communitydata;
										for (Iterator iterator = datas.iterator(); iterator.hasNext(); hots.add(communitydata))
											communitydata = (CommunityData) iterator.next();

										if (tab_status[0]) {
											if (hots.size() == hot_total) {
												if (communitySquare_listview.getFooterViewsCount() == 1)
													communitySquare_listview.removeFooterView(communitySquare_listview_foot);
											} else if (communitySquare_listview.getFooterViewsCount() == 0)
												communitySquare_listview.addFooterView(communitySquare_listview_foot);
											communityBaseAdapter.notifyDataSetChanged();
										}
									} else if (hashMap.get("LIST_STATUS").equals("099"))
										CommunityUtil.showCommunityToast(CommentSquareAcivity.this, (String) hashMap.get("LIST_STATUS"));
								} else {
									CommunityUtil.showCommunityToast(CommentSquareAcivity.this, "11");
								}
								isRefreshing[0] = false;
							}

							protected void onPreExecute() {
								super.onPreExecute();
								isRefreshing[0] = true;
							}

							List datas;
							HashMap hashMap;

						}).execute(null);

					}

				}

			}

			public void onScrollStateChanged(AbsListView abslistview, int i) {
			}

		});
		login_layut = (RelativeLayout) findViewById(0x7f0a019c);
		communitySquare_wait = (LinearLayout) findViewById(0x7f0a01a1);
		communitySquareTitle = (TextView) findViewById(0x7f0a0190);
		if (pointId > 0)
			communitySquareTitle.setText((new StringBuilder()).append(pointName).append(getString(0x7f080134)).toString());
		loginButton = (Button) findViewById(0x7f0a019e);
		loginButton.setOnClickListener(this);
		registerButton = (Button) findViewById(0x7f0a01a0);
		registerButton.setOnClickListener(this);
		if (getIntent().getBooleanExtra("POI_MINE_IMMEDIATELY", false)) {
			setTabSelected(2);
			if (CommunityUtil.getTCToken(this) != null) {
				updateMine();
			} else {
				refreshCommunity.setVisibility(8);
				login_layut.setVisibility(0);
			}
		} else {
			updateHot();
		}
		resumeRefresh = false;
		registerReceiver(broadcastReceiver, new IntentFilter("com.touchchina.action.TC_LOGOUT"));
	}

	protected void onDestroy() {
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}

	protected void onResume() {
		super.onResume();
		if (!resumeRefresh) {
			resumeRefresh = true;
			return;
		}

		hots = null;
		news = null;
		mine = null;
		for (int i = 0; i < tab_status.length; i++) {
			if (tab_status[i]) {
				setTabSelected(i);

				break;
			}
		}
		updateHot();
		updateNew();
		if (tab_status[2])
			if (CommunityUtil.getTCToken(this) == null) {
				communitySquare_listview.setVisibility(8);
				refreshCommunity.setVisibility(8);
				login_layut.setVisibility(0);
				communitySquare_no_community.setVisibility(8);
				communitySquare_wait.setVisibility(8);
				if (pointId <= 0)
					communitySquareTitle.setText(0x7f080149);
			} else {
				updateMine();
			}

	}

	public static final String POI_COMMENT_COUNT = "point_commentCount";
	public static final String POI_ID = "pointid";
	public static final String POI_MINE_IMMEDIATELY = "POI_MINE_IMMEDIATELY";
	public static final String POI_NAME = "point_name";
	public static final String POI_TYPE = "pointtype";
	private static final int TO_COMMENT_ITEM = 0;
	private static final int TO_COMMENT_POI = 1;
	private static final int TO_LOGIN_REGIST = 2;
	private ImageView backButton;
	private BroadcastReceiver broadcastReceiver;
	private CommunityBaseAdapter communityBaseAdapter;
	private TextView communitySquareTitle;
	private ListView communitySquare_listview;
	private LinearLayout communitySquare_listview_foot;
	private LinearLayout communitySquare_no_community;
	private TextView communitySquare_no_community_text;
	private TextView communitySquare_tab1;
	private TextView communitySquare_tab2;
	private TextView communitySquare_tab3;
	private ImageView communitySquare_tab3_count;
	private LinearLayout communitySquare_wait;
	private int hot_timeline;
	private int hot_total;
	private List<CommunityData> hots;
	private boolean isRefreshing[];
	private Button loginButton;
	private RelativeLayout login_layut;
	private List mine;
	private int mine_timeline;
	private int mine_total;
	private int new_timeline;
	private int new_total;
	private List<CommunityData> news;
	private int pointId;
	private String pointName;
	private ImageView refreshCommunity;
	private Button registerButton;
	private boolean resumeRefresh;
	private String sgType;
	private boolean tab_status[];

	/*
	 * static List access$002(CommentSquareAcivity commentsquareacivity, List
	 * list) { commentsquareacivity.mine = list; return list; }
	 */

	/*
	 * static int access$1002(CommentSquareAcivity commentsquareacivity, int i)
	 * { commentsquareacivity.hot_timeline = i; return i; }
	 */

	/*
	 * static int access$1402(CommentSquareAcivity commentsquareacivity, int i)
	 * { commentsquareacivity.new_total = i; return i; }
	 */

	/*
	 * static int access$1502(CommentSquareAcivity commentsquareacivity, int i)
	 * { commentsquareacivity.new_timeline = i; return i; }
	 */

	/*
	 * static int access$1602(CommentSquareAcivity commentsquareacivity, int i)
	 * { commentsquareacivity.mine_total = i; return i; }
	 */

	/*
	 * static int access$1702(CommentSquareAcivity commentsquareacivity, int i)
	 * { commentsquareacivity.mine_timeline = i; return i; }
	 */

	/*
	 * static boolean access$402(CommentSquareAcivity commentsquareacivity,
	 * boolean flag) { commentsquareacivity.resumeRefresh = flag; return flag; }
	 */

	/*
	 * static List access$602(CommentSquareAcivity commentsquareacivity, List
	 * list) { commentsquareacivity.hots = list; return list; }
	 */

	/*
	 * static List access$702(CommentSquareAcivity commentsquareacivity, List
	 * list) { commentsquareacivity.news = list; return list; }
	 */

	/*
	 * static int access$902(CommentSquareAcivity commentsquareacivity, int i) {
	 * commentsquareacivity.hot_total = i; return i; }
	 */
}
