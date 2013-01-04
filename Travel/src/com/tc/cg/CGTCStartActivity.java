// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.cg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tc.TCData;
import com.tc.TCUtil;
import com.tc.logic.CGLastUpdateData;
import com.touchchina.cityguide.sz.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData, CGStartActivity, CGUtil, 
//            CGDataManager, CGUpdateActivity

public class CGTCStartActivity extends CGBaseActivity {
	private class TCAsyncTask extends AsyncTask {

		protected Integer doInBackground(String as[]) {
			isUnZip = TCUtil.isUnZipInterrapted("cg", CG_ID, false);
			if ((new File((new StringBuilder()).append(CG_DATA.CG_PATH).append("content.xml").toString())).exists()) {
				updateTime = getTime((new StringBuilder()).append(CG_DATA.CG_PATH).append("content.xml").toString());
				flag = 1;
			}
			cgLastUpdate = CGLastUpdateData.getLastUpdate(CGTCStartActivity.this, CG_DATA.CG_ID, CG_DATA.CG_VERSION, updateTime);

			boolean noCG = false;
			try {
				if (getAssets().open("cg.zip") == null)
					noCG = true;
			} catch (Exception e) {
				noCG = true;
			}
			if (flag != 0 || noCG) {

				if (flag == 0 && cgLastUpdate == null) {
					flag = 2;
					return Integer.valueOf(flag);
				} else if (flag == 1 && cgLastUpdate == null) {
					flag = 3;
					return Integer.valueOf(flag);
				} else {
					if (cgLastUpdate != null && cgLastUpdate.lastUpdate != null && cgLastUpdate.lastUpdate.length() > 0) {
						lastUpdate = Integer.parseInt(cgLastUpdate.lastUpdate);
						fileSize = TCUtil.getDownloadFileSize(cgLastUpdate.fileSize);
					}
					if (flag == 0) {
						String as2[] = new String[1];
						as2[0] = (new StringBuilder()).append(lastUpdate).append("").toString();
						publishProgress(as2);
						return Integer.valueOf(0);
					} else if (flag == 1 && lastUpdate != updateTime) {
						String as1[] = new String[1];
						as1[0] = (new StringBuilder()).append(lastUpdate).append("").toString();
						publishProgress(as1);
						flag = 1;
						return Integer.valueOf(0);
					} else {
						return Integer.valueOf(flag);
					}
				}

			}
			{
				return Integer.valueOf(4);
			}
		}

		protected Object doInBackground(Object aobj[]) {
			return doInBackground((String[]) aobj);
		}

		protected void onPostExecute(Integer integer) {
			super.onPostExecute(integer);
			Log.e(TAG, "This is cy speaking");
			progressbar.setVisibility(4);
			if (isUnZip) {
				Bundle bundle = new Bundle();
				bundle.putInt("last_Update", lastUpdate);
				bundle.putInt("update_time", updateTime);
				boolean flag1;
				if (flag == 0)
					flag1 = true;
				else
					flag1 = false;
				bundle.putBoolean("is_first_install", flag1);
				bundle.putBoolean("is_unzip_immediately", isUnZip);
				startActivity(CGTCStartActivity.this, CGUpdateActivity.class, bundle);
				CGUtil.startCGService(CGTCStartActivity.this);
				finish();
			}
			if (integer.intValue() != 1) {
				if (integer.intValue() == 2) {
					android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CGTCStartActivity.this);
					builder.setTitle(getString(0x7f08007f));
					builder.setMessage(getString(0x7f0800ad));
					builder.setNegativeButton(getString(0x7f08008e), new android.content.DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialoginterface, int i) {
							dialoginterface.dismiss();
							finish();
						}

					});
					builder.setPositiveButton(getString(0x7f0800ae), new android.content.DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialoginterface, int i) {
							String as[] = new String[2];
							as[0] = CG_DATA.CG_PATH;
							as[1] = CG_DATA.CG_DATA_BASE_PATH;
							asyncTask = new TCAsyncTask();
							asyncTask.execute(as);
							dialoginterface.dismiss();
						}
					});
					AlertDialog alertdialog = builder.create();
					alertdialog.show();
					alertdialog.setCancelable(false);
				} else if (integer.intValue() == 3) {
					startActivity(CGTCStartActivity.this, CGStartActivity.class);
					CGUtil.startCGService(CGTCStartActivity.this);
					finish();
				} else if (integer.intValue() == 4) {
					Bundle bundle1 = new Bundle();
					bundle1.putBoolean("is_assert_zip", true);
					startActivity(CGTCStartActivity.this, CGUpdateActivity.class, bundle1);
					CGUtil.startCGService(CGTCStartActivity.this);
					finish();
				}

			} else {
				startActivity(CGTCStartActivity.this, CGStartActivity.class);
				CGUtil.startCGService(CGTCStartActivity.this);
				finish();
			}

		}

		protected void onPostExecute(Object obj) {
			onPostExecute((Integer) obj);
		}

		protected void onPreExecute() {
			super.onPreExecute();
			progressbar.setVisibility(0);
		}

		protected void onProgressUpdate(Object[] obj) {
			onProgressUpdate((String[]) obj);
		}

		protected void onProgressUpdate(String as[]) {
			super.onProgressUpdate(as);
			cg_update_time.setText((new StringBuilder()).append(TCUtil.timeStamp2YYMMDD(lastUpdate)).append(getString(0x7f080095)).toString());
			tv2.setText((new StringBuilder()).append(getString(0x7f080092)).append("\"").append(CG_DATA.CG_NAME).append(getString(0x7f080093))
					.append("\"").append("(").append(fileSize).append(")").toString());
			ll.setVisibility(0);
		}

	}

	public CGTCStartActivity() {
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

	private void startNextActivity() {
		TCUtil.isUnZipInterrapted("cg", CG_ID, true);
		if ((new File((new StringBuilder()).append(CG_DATA.CG_PATH).append("content.xml").toString())).exists()) {
			startActivity(this, CGStartActivity.class);
			CGUtil.startCGService(this);
		} else {
			finish();
		}
	}

	public void onBackPressed() {
	}

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.tc_start);
		DisplayMetrics displaymetrics;
		try {
			// check download resources
			isAsset = getAssets().open("cg.zip");
		} catch (IOException ioexception) {
		}
		displaymetrics = TCUtil.getDisplayMetrics(this);
		TCData.SCREEN_WIDTH = displaymetrics.widthPixels;
		TCData.SCREEN_HEIGHT = displaymetrics.heightPixels;
		if (Math.min(displaymetrics.widthPixels, displaymetrics.heightPixels) > 320)
			TCData.USE_2X = true;
		else
			TCData.USE_2X = false;

		TCData.GOOGLE_MAP_USEABLE = TCUtil.googMapEnable();

		CG_DATA = new CGData();

		// no use currently
		// CG_DATA.setRateData(this);
		// parse cg_info_data, main menu displayed in first page.
		CGData.CGInfoData.CGInfoParaser.parse(this, CG_DATA);
		// save Data in manager
		CGDataManager.addCGData(CG_DATA);

		CG_ID = CG_DATA.CG_ID;
		CG_DATA.CG_PATH = (new StringBuilder()).append(TCData.CG_ROOT).append(CG_DATA.CG_ID).append("/").toString();
		CG_DATA.CG_PLAN_PATH = (new StringBuilder()).append(CG_DATA.CG_PATH).append("plans/").toString();
		CG_DATA.CG_PLAN_MY_PATH = (new StringBuilder()).append(CG_DATA.CG_PLAN_PATH).append("MyPlan/").toString();
		CG_DATA.CG_PLAN_MODEL_PATH = CG_DATA.CG_PLAN_PATH;

		if (TCData.USE_2X)
			CG_DATA.CG_COVER_PATH = (new StringBuilder()).append(CG_DATA.CG_PATH).append("cover@2x.jpg").toString();
		else
			CG_DATA.CG_COVER_PATH = (new StringBuilder()).append(CG_DATA.CG_PATH).append("cover.jpg").toString();

		CG_DATA.CG_DATA_BASE_PATH = (new StringBuilder()).append("TouchChina/CG/").append(CG_DATA.CG_ID).append("/").toString();

		progressbar = (ProgressBar) findViewById(R.id.progressBar);
		ll = (LinearLayout) findViewById(R.id.ll);
		cg_update_time = (TextView) findViewById(R.id.cg_update_time);
		tv2 = (TextView) findViewById(R.id.tv2);
		ok = (Button) findViewById(R.id.update_ok);
		cancel = (Button) findViewById(R.id.update_cancel);
		ok.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {

				if (TCUtil.getSdCardFree() > 50L) {
					Bundle bundle1 = new Bundle();
					bundle1.putInt("last_Update", lastUpdate);
					bundle1.putInt("update_time", updateTime);

					bundle1.putBoolean("is_first_install", flag > 0 ? false : true);
					bundle1.putBoolean("is_unzip_immediately", isUnZip);
					startActivity(CGTCStartActivity.this, CGUpdateActivity.class, bundle1);
					// start CG Service
					CGUtil.startCGService(CGTCStartActivity.this);
					// close activity
					finish();
				} else {
					Toast.makeText(CGTCStartActivity.this, getString(R.string.sdcard_not_free), 1).show();
				}
			}

		});
		cancel.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				String s2;
				if (flag == 1)
					s2 = getString(R.string.shifouquexiaoxiazai);
				else
					s2 = getString(R.string.quedingtuichuma);
				TCUtil.createPositiveDialog(CGTCStartActivity.this, s2, new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialoginterface, int i) {
						if (flag == 1) {
							startActivity(CGTCStartActivity.this, CGStartActivity.class);
							CGUtil.startCGService(CGTCStartActivity.this);
						}
						finish();
					}

				}).show();
			}

		});

		if (!CG_DATA.CG_PATH.startsWith("file:///android_asset/")) {
			String s = (new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina").append("/").toString();
			String s1 = (new StringBuilder()).append(s).append(".nomedia").toString();
			if (!TCUtil.fileExists(s1)) {
				File file = new File(s);
				if (!file.exists())
					file.mkdirs();

				try {
					(new File(s1)).createNewFile();
				} catch (IOException ioexception1) {
				}
			}
		}
		IntentFilter intentfilter = new IntentFilter("android.intent.action.MEDIA_MOUNTED");
		intentfilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
		intentfilter.addAction("android.intent.action.MEDIA_REMOVED");
		intentfilter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
		intentfilter.addAction("android.intent.action.MEDIA_SHARED");
		intentfilter.addAction("android.intent.action.MEDIA_SCANNER_STARTED");
		intentfilter.addAction("android.intent.action.MEDIA_SCANNER_FINISHED");
		intentfilter.addDataScheme("file");
		registerReceiver(broadcastReceiver, intentfilter);
	}

	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	protected void onResume() {
		super.onResume();
		if (alertDialog != null && alertDialog.isShowing())
			alertDialog.dismiss();
		if (!TCUtil.isSDCardAvailable()) {
			android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
			builder.setTitle(getString(0x7f08005f));
			builder.setNegativeButton(getString(0x7f080082), new android.content.DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int i) {
					dialoginterface.dismiss();
					finish();
				}

			});
			builder.setPositiveButton(getString(0x7f080060), new android.content.DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int i) {
					dialoginterface.dismiss();
					TCUtil.startSDCardSetting(CGTCStartActivity.this);
				}

			});
			alertDialog = builder.create();
			alertDialog.setCancelable(false);
			alertDialog.show();
		}
		if (!CG_DATA.CG_PATH.startsWith("file:///android_asset/")) {
			if (asyncTask == null)
				if (!TCUtil.isNetAvailable(this)) {
					if (isAsset == null) {
						android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(this);
						builder1.setTitle(getString(0x7f08007f));
						builder1.setMessage(getString(0x7f080080));
						builder1.setNegativeButton(getString(0x7f080082), new android.content.DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialoginterface, int i) {
								dialoginterface.dismiss();
								startNextActivity();
								finish();
							}

						});
						builder1.setPositiveButton(getString(0x7f080081), new android.content.DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialoginterface, int i) {
								dialoginterface.dismiss();
								TCUtil.startWirelessSetting(CGTCStartActivity.this);
							}

						});
						AlertDialog alertdialog = builder1.create();
						alertdialog.show();
						alertdialog.setCancelable(false);
					} else if ((new File((new StringBuilder()).append(CG_DATA.CG_PATH).append("content.xml").toString())).exists())
						(new Handler()).postDelayed(new Runnable() {

							public void run() {
								startNextActivity();
								finish();
							}

						}, 2000L);
					else
						(new Handler()).postDelayed(new Runnable() {

							public void run() {
								Bundle bundle = new Bundle();
								bundle.putBoolean("is_assert_zip", true);
								startActivity(CGTCStartActivity.this, CGUpdateActivity.class, bundle);
								CGUtil.startCGService(CGTCStartActivity.this);
								finish();
							}

						}, 2000L);
				} else {
					String as[] = new String[2];
					as[0] = CG_DATA.CG_PATH;
					as[1] = CG_DATA.CG_DATA_BASE_PATH;
					asyncTask = new TCAsyncTask();
					asyncTask.execute(as);
				}
		} else {
			startActivity(this, CGStartActivity.class);
			CGUtil.startCGService(this);
			finish();
		}
	}

	private static final String TAG = CGTCStartActivity.class.getSimpleName();
	public static AlertDialog alertDialog;
	private TCAsyncTask asyncTask;
	private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			if (CGTCStartActivity.alertDialog != null && CGTCStartActivity.alertDialog.isShowing())
				CGTCStartActivity.alertDialog.cancel();
			if (intent.getAction().equals("android.intent.action.MEDIA_MOUNTED") || intent.getAction().equals("android.intent.action.MEDIA_REMOVED")
					|| intent.getAction().equals("android.intent.action.ACTION_MEDIA_UNMOUNTED")
					|| intent.getAction().equals("android.intent.action.ACTION_MEDIA_BAD_REMOVAL")
					|| intent.getAction().equals("android.intent.action.MEDIA_REMOVED")
					|| !intent.getAction().equals("android.intent.action.MEDIA_REMOVED"))
				;
			return;
		}

	};
	private Button cancel;
	private CGLastUpdateData cgLastUpdate;
	private TextView cg_update_time;
	private String fileSize;
	private int flag;
	private InputStream isAsset;
	private boolean isUnZip;
	private int lastUpdate;
	private LinearLayout ll;
	private Button ok;
	private ProgressBar progressbar;
	private TextView tv2;
	private int updateTime;

	/*
	 * static boolean access$002(CGTCStartActivity cgtcstartactivity, boolean
	 * flag1) { cgtcstartactivity.isUnZip = flag1; return flag1; }
	 */

	/*
	 * static int access$102(CGTCStartActivity cgtcstartactivity, int i) {
	 * cgtcstartactivity.updateTime = i; return i; }
	 */

	/*
	 * static int access$302(CGTCStartActivity cgtcstartactivity, int i) {
	 * cgtcstartactivity.flag = i; return i; }
	 */

	/*
	 * static CGLastUpdateData access$402(CGTCStartActivity cgtcstartactivity,
	 * CGLastUpdateData cglastupdatedata) { cgtcstartactivity.cgLastUpdate =
	 * cglastupdatedata; return cglastupdatedata; }
	 */

	/*
	 * static int access$502(CGTCStartActivity cgtcstartactivity, int i) {
	 * cgtcstartactivity.lastUpdate = i; return i; }
	 */

	/*
	 * static String access$602(CGTCStartActivity cgtcstartactivity, String s) {
	 * cgtcstartactivity.fileSize = s; return s; }
	 */

	/*
	 * static TCAsyncTask access$802(CGTCStartActivity cgtcstartactivity,
	 * TCAsyncTask tcasynctask) { cgtcstartactivity.asyncTask = tcasynctask;
	 * return tcasynctask; }
	 */
}
