// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.cg;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.tc.TCData;
import com.tc.TCUtil;
import com.tc.community.CommunityUtil;
import com.tc.logic.CGDownData;
import com.tc.logic.CGLastUpdateData;
import com.tc.net.DownAsyncTask;
import com.tc.net.GetTimeData;
import com.touchchina.cityguide.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGUtil, CGData, CGMoreBroadcastReceiver, 
//            CGSettingActivity, CGAboutActivity, CGRecommendActivity, CGMoreAppActivity, 
//            CGFeedBackActivity

public class CGMoreActivity extends CGBaseActivity implements android.view.View.OnClickListener {
	private class AppForUpdateAsy extends AsyncTask {

		protected Object doInBackground(Object aobj[]) {
			return doInBackground((String[]) aobj);
		}

		protected String doInBackground(String as[]) {
			updateTime = getTime((new StringBuilder()).append(CG_DATA.CG_PATH).append("content.xml").toString());
			cgLastUpdate = CGLastUpdateData.getLastUpdate(CGMoreActivity.this, CG_DATA.CG_ID, CG_DATA.CG_VERSION, updateTime);
			t = GetTimeData.getGotTime(CGMoreActivity.this);
			if (cgLastUpdate != null) {
				fileSize = TCUtil.getDownloadFileSize(cgLastUpdate.fileSize);
				lastUpdate = cgLastUpdate.lastUpdate;
			}
			return fileSize;
		}

		protected void onPostExecute(Object obj) {
			onPostExecute((String) obj);
		}

		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			parameters = CGDownData.getCGDown_newParams(String.valueOf(CG_DATA.CG_ID), String.valueOf(updateTime), lastUpdate, t);
			alertDialog.dismiss();
			if (s != null)
				if (lastUpdate != null) {
					if (updateTime < Integer.parseInt(lastUpdate)) {
						android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CGMoreActivity.this);
						builder.setTitle((new StringBuilder()).append(getString(0x7f0800c6)).append(s).append(getString(0x7f0800c7)).toString());
						builder.setIcon(0x7f0200a1);
						builder.setNegativeButton(getString(0x7f080142), new android.content.DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialoginterface, int i) {
								dialoginterface.dismiss();
							}

						});
						builder.setPositiveButton(getString(0x7f0800c8), new android.content.DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialoginterface, int i) {
								dialoginterface.dismiss();
								DownAsyncTask downasynctask = new DownAsyncTask(new MoreAppHandler());
								Object aobj[] = new Object[4];
								aobj[0] = "get";
								aobj[1] = TCData.CG_ROOT;
								aobj[2] = parameters;
								aobj[3] = (new StringBuilder()).append(CGDownData.getUrl()).append("cgdown_new").toString();
								downasynctask.execute(aobj);
								hasDownload = true;
								manager.notify(0x121111, notification);
							}

						});
						AlertDialog alertdialog = builder.create();
						alertdialog.show();
						alertdialog.setCancelable(true);
					} else {
						Toast.makeText(CGMoreActivity.this, getString(0x7f0800ca), 1).show();
					}
				} else {
					Toast.makeText(CGMoreActivity.this, getString(0x7f0800cd), 1).show();
				}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			alertDialog = new ProgressDialog(CGMoreActivity.this);
			alertDialog.setProgressStyle(0);
			alertDialog.setMessage(getString(0x7f0800c5));
			alertDialog.show();
			alertDialog.setCancelable(false);
		}

		private String fileSize;
		private String lastUpdate;
		private Map parameters;
		private String t;

	}

	private class Content {

		public String androidurl;
		public String contentText;

	}

	private class MoreAdapter extends BaseAdapter {

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
			return moreData.items.size();
		}

		public Object getItem(int i) {
			return moreData.items.get(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			if (view == null)
				view = TCUtil.getLayoutInflater(context).inflate(0x7f030017, null);
			CGData.CGMoreData.CGMoreItem cgmoreitem = (CGData.CGMoreData.CGMoreItem) getItem(i);
			((ImageView) view.findViewById(0x7f0a0066)).setImageBitmap(getBitmap(context, cgmoreitem.icon));
			((TextView) view.findViewById(0x7f0a0067)).setText(cgmoreitem.name);
			view.findViewById(0x7f0a0068).setVisibility(8);
			return view;
		}

		Map bitmaps;
		private Context context;

		public MoreAdapter(Context context1) {

			bitmaps = new HashMap();
			context = context1;
		}
	}

	private class MoreAppHandler extends Handler {

		public void handleMessage(Message message) {
			super.handleMessage(message);
			if (message.what == 0) {

				if (currentTimeMillis == 0L || System.currentTimeMillis() - currentTimeMillis > 800L) {
					long al[] = (long[]) (long[]) message.obj;
					hasDownloaded = al[0];
					total = al[1];
					currentTimeMillis = System.currentTimeMillis();
					notification.contentView.setTextViewText(
							0x7f0a008c,
							(new StringBuilder()).append(getString(0x7f0800cb)).append(" ")
									.append(TCUtil.formatNum((100D * (double) hasDownloaded) / (double) total)).append("%").toString());
					notification.contentView.setProgressBar(0x7f0a008d, (int) total, (int) hasDownloaded, false);
					manager.notify(0x121111, notification);
				}
			} else if (message.what == 1) {
				notification.contentView.setTextViewText(0x7f0a008c, getString(0x7f08009a));
				notification.contentView.setProgressBar(0x7f0a008d, (int) total, (int) hasDownloaded, true);
				manager.notify(0x121111, notification);
			} else if (message.what == 3) {
				notification.contentView.setTextViewText(0x7f0a008c, (new StringBuilder()).append(getString(0x7f080000))
						.append(getString(0x7f08009b)).toString());
				notification.contentView.setProgressBar(0x7f0a008d, 100, 100, false);
				manager.notify(0x121111, notification);
				hasDownload = false;
			}
		}

		private long currentTimeMillis;
		private long hasDownloaded;

		private long total;

	}

	public CGMoreActivity() {
		manager = null;
		notification = null;
		hasDownload = false;
		itemListener = new android.widget.AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView adapterview, View view, int i, long l) {
				CGData.CGMoreData.CGMoreItem cgmoreitem;
				Bundle bundle;
				cgmoreitem = (CGData.CGMoreData.CGMoreItem) moreAdapter.getItem(i);
				bundle = new Bundle();
				if ("setting".equals(cgmoreitem.type)) { // goto _L2; else goto
															// _L1
					FlurryAgent.logEvent("Setting");
					startActivity(CGMoreActivity.this, CGSettingActivity.class);
				} else if ("about".equals(cgmoreitem.type)) {
					FlurryAgent.logEvent("aboutUs");
					startActivity(CGMoreActivity.this, CGAboutActivity.class);
				} else if ("recommend".equals(cgmoreitem.type)) {
					FlurryAgent.logEvent("MoreApps");
					startActivity(CGMoreActivity.this, CGRecommendActivity.class);
				} else if ("jingcaituijian".equals(cgmoreitem.type))
					startActivity(CGMoreActivity.this, CGMoreAppActivity.class);
				else if ("tuijian".equals(cgmoreitem.type)) {
					Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
					Content content = getContent((new StringBuilder()).append(CG_DATA.CG_PATH).append("content.xml").toString());
					String s = (new StringBuilder()).append(content.contentText).append("\n").append(content.androidurl).toString();
					ArrayList arraylist = new ArrayList();
					arraylist.add(Uri.fromFile(new File((new StringBuilder()).append(CG_DATA.CG_PATH).append("cover.jpg").toString())));
					arraylist.add(Uri.fromFile(new File((new StringBuilder()).append(CG_DATA.CG_PATH).append("screenshot.jpg").toString())));
					intent.putExtra("android.intent.extra.TEXT", (new StringBuilder()).append(s).append("\n").append("\n").toString());
					String s1 = CommunityUtil.getTCNickName(getApplicationContext());
					String s2;
					if (s1 == null)
						s2 = (new StringBuilder()).append(getString(0x7f0801bf)).append("  ").append(getString(0x7f080000)).toString();
					else
						s2 = (new StringBuilder()).append(s1).append(" ").append(getString(0x7f0801c0)).append("  ").append(getString(0x7f080000))
								.toString();
					intent.putExtra("android.intent.extra.SUBJECT", s2);
					intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arraylist);
					intent.setType("image/*");
					startActivity(Intent.createChooser(intent, getString(0x7f0800d8)));
				} else if ("fankui".equals(cgmoreitem.type)) {
					bundle.putString(CGFeedBackActivity.FEED_BACK_NAME, cgmoreitem.name);
					startActivity(CGMoreActivity.this, CGFeedBackActivity.class, bundle);
				} else if ("update".equals(cgmoreitem.type) && showNetSettingDialog()) {
					if (!hasDownload)
						(new AppForUpdateAsy()).execute(new String[] { "" });
					else
						Toast.makeText(CGMoreActivity.this, getString(0x7f0800cc), 1).show();
				}
			}

		};
	}

	private Content getContent(String s) {
		Content content = null;
		InputStream inputstream = TCUtil.getFileInputStream(this, s);
		XmlPullParser xmlpullparser = Xml.newPullParser();
		try {
			xmlpullparser.setInput(inputstream, "UTF-8");
			int j = xmlpullparser.getEventType();
			while (j != XmlPullParser.END_DOCUMENT) {
				if (j == XmlPullParser.START_TAG) {
					if ("text".equals(xmlpullparser.getName()))
						content.contentText = xmlpullparser.nextText();
					else if ("androidurl".equals(xmlpullparser.getName()))
						content.androidurl = xmlpullparser.nextText();
				} else if (j == XmlPullParser.START_DOCUMENT) {
					content = new Content();
				}
					j = xmlpullparser.next();
				
			}
		} catch (Exception e) {
			Log.e(TAG, "private int getTime(String filepath)", e);
		}

		return content;

	}

	private int getTime(String s) {
		String s1 = null;
		InputStream inputstream = TCUtil.getFileInputStream(this, s);
		XmlPullParser xmlpullparser = Xml.newPullParser();
		try {
			xmlpullparser.setInput(inputstream, "UTF-8");
			int j = xmlpullparser.getEventType();
			while (j != XmlPullParser.END_DOCUMENT) {
				if (j == XmlPullParser.START_TAG) {
					if ("city".equals(xmlpullparser.getName())) {
						s1 = xmlpullparser.getAttributeValue(null, "time");

						break;
					}
				} 
					j = xmlpullparser.next();
				
			}
		} catch (Exception e) {
			Log.e(TAG, "private int getTime(String filepath)", e);
		}

		if (s1 != null && s1.length() > 0) {
			try {
				return Integer.parseInt(s1);
			} catch (Exception e) {
				Log.e(TAG, "private int getTime(String filepath)", e);
			}

		}
		return 0;
	}

	public void onBackPressed() {
		CGUtil.showExitDialog(this);
	}

	public void onClick(View view) {
		if (view.getId() == R.id.backButton)
			onBackPressed();

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		moreData = CG_DATA.CG_MORE_DATA;
		setContentView(0x7f030013);
		notification = new Notification(0x7f0200a1, (new StringBuilder()).append(getString(0x7f080000)).append(getString(0x7f0800cb)).toString(),
				System.currentTimeMillis());
		notification.flags = 32;
		notification.icon = 0x7f0200a1;
		notification.contentView = new RemoteViews(getApplication().getPackageName(), 0x7f030018);
		notification.contentIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, CGMoreBroadcastReceiver.class), 0);
		manager = (NotificationManager) getSystemService("notification");
		findViewById(0x7f0a0000).setVisibility(4);
		((TextView) findViewById(0x7f0a0001)).setText(moreData.chName);
		findViewById(0x7f0a0000).setOnClickListener(this);
		moreList = (ListView) findViewById(0x7f0a0075);
		moreList.setOnItemClickListener(itemListener);
		moreAdapter = new MoreAdapter(this);
		moreList.setAdapter(moreAdapter);
	}

	protected void onDestroy() {
		super.onDestroy();
		moreAdapter.clearBitmaps();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

	public boolean showNetSettingDialog() {
		boolean flag = false;
		if (!TCUtil.isNetAvailable(this)) {
			android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
			builder.setTitle(getString(0x7f08007f));
			builder.setMessage(getString(0x7f080080));
			builder.setNegativeButton(getString(0x7f080082), new android.content.DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int i) {
					dialoginterface.dismiss();
				}

			});
			builder.setPositiveButton(getString(0x7f080081), new android.content.DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int i) {
					dialoginterface.dismiss();
					TCUtil.startWirelessSetting(CGMoreActivity.this);
				}

			});
			AlertDialog alertdialog = builder.create();
			alertdialog.show();
			alertdialog.setCancelable(false);
		} else {
			flag = true;
		}
		return flag;
	}

	public static final int NOTIFICATION_ID = 0x121111;
	private static final String TAG = CGMoreActivity.class.getSimpleName();
	private ProgressDialog alertDialog;
	private CGLastUpdateData cgLastUpdate;
	private boolean hasDownload;
	private android.widget.AdapterView.OnItemClickListener itemListener;
	private NotificationManager manager;
	private MoreAdapter moreAdapter;
	private CGData.CGMoreData moreData;
	private ListView moreList;
	private Notification notification;
	private int updateTime;

	/*
	 * static boolean access$302(CGMoreActivity cgmoreactivity, boolean flag) {
	 * cgmoreactivity.hasDownload = flag; return flag; }
	 */

	/*
	 * static int access$502(CGMoreActivity cgmoreactivity, int i) {
	 * cgmoreactivity.updateTime = i; return i; }
	 */

	/*
	 * static CGLastUpdateData access$702(CGMoreActivity cgmoreactivity,
	 * CGLastUpdateData cglastupdatedata) { cgmoreactivity.cgLastUpdate =
	 * cglastupdatedata; return cglastupdatedata; }
	 */

	/*
	 * static ProgressDialog access$802(CGMoreActivity cgmoreactivity,
	 * ProgressDialog progressdialog) { cgmoreactivity.alertDialog =
	 * progressdialog; return progressdialog; }
	 */
}
