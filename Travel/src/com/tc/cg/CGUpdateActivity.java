// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.cg;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.xmlpull.v1.XmlPullParser;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tc.TCData;
import com.tc.TCUtil;
import com.tc.logic.CGDownData;
import com.tc.logic.CGLastUpdateData;
import com.tc.net.DownAsyncTask;
import com.tc.net.GetTimeData;
import com.touchchina.cityguide.sz.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGStartActivity, CGData, CGUtil

public class CGUpdateActivity extends CGBaseActivity {
	private class DownloadingInfo {

		long TOTAL;
		DownAsyncTask downAsyncTask;
		long hasDownloaded;
		boolean isDownloading;
		boolean isUnZip;
		int timeoutCount;
	}

	private class UnzipAsyncTask extends AsyncTask {

		protected Object doInBackground(Object aobj[]) {
			return doInBackground((String[]) aobj);
		}

		protected String doInBackground(String as[]) {
			ZipInputStream zipinputstream = null;
			int l = 0;
			try {
				zipinputstream = new ZipInputStream(getAssets().open(as[0]));
				while (true) {
					ZipEntry zipentry = zipinputstream.getNextEntry();
					if (zipentry == null)
						break;
					l++;
					publishProgress(new String[] { zipentry.getName(), String.valueOf(l) });
					if (zipentry.isDirectory()) {
						String s1 = zipentry.getName().substring(0, -1 + zipentry.getName().length());
						(new File((new StringBuilder()).append(as[1]).append(s1).toString())).mkdirs();
					} else {
						File file = new File((new StringBuilder()).append(as[1]).append(zipentry.getName()).toString());
						File file1 = file.getParentFile();
						if (!file1.exists())
							file1.mkdirs();
						file.createNewFile();
						FileOutputStream fileoutputstream = new FileOutputStream(file);
						byte abyte0[] = new byte[512];
						do {
							int i = zipinputstream.read(abyte0);
							if (i == -1)
								break;
							fileoutputstream.write(abyte0, 0, i);
							fileoutputstream.flush();
						} while (true);
						fileoutputstream.close();
					}
				}
				zipinputstream.close();

			} catch (Exception e) {
				Log.e("CGUpdateActivity", "unzipForlder", e);
			}
			return "ok";

		}

		protected void onPostExecute(Object obj) {
			onPostExecute((String) obj);
		}

		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			startActivity(CGUpdateActivity.this, CGStartActivity.class);
			finish();
		}

		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected void onProgressUpdate(Object aobj[]) {
			onProgressUpdate((String[]) aobj);
		}

		protected void onProgressUpdate(String as[]) {
			super.onProgressUpdate(as);
			progressBar.setMax(count);
			progressBar.setProgress(Integer.parseInt(as[1]));
		}

	}

	public CGUpdateActivity() {
		list = new ArrayList();
		list1 = new ArrayList();
		requestCode = 1001;
		count = 1688;
		handler = new Handler() {

			public void handleMessage(Message message) {
				switch (message.what) {
				case 0:
					long al[] = (long[]) (long[]) message.obj;
					downloadingInfo.hasDownloaded = al[0];
					downloadingInfo.TOTAL = al[1];
					downloadingInfo.isDownloading = true;
					downloadingInfo.isUnZip = false;
					downloadingInfo.timeoutCount = 0;
					refreshUI();
					break;
				case 1:
					downloadingInfo.hasDownloaded = ((Long) message.obj).longValue();
					downloadingInfo.TOTAL = ((Long) message.obj).longValue();
					downloadingInfo.isDownloading = false;
					downloadingInfo.isUnZip = true;
					downloadingInfo.timeoutCount = 0;
					refreshUI();
					break;
				case 2:
					if (!downloadingInfo.isDownloading) {
						downloadingInfo.isDownloading = false;
						downloadingInfo.isUnZip = false;
						downloadingInfo.timeoutCount = 0;
						refreshUI();
					}
					break;
				case 3:
					if ((new File((new StringBuilder()).append(CG_DATA.CG_PATH).append("cg.apk").toString())).exists()
							&& (new File((new StringBuilder()).append(CG_DATA.CG_PATH).append("content.xml").toString())).exists()) {
						int i = TCUtil.getVersionCode(CGUpdateActivity.this);
						int j = getVersion((new StringBuilder()).append(CG_DATA.CG_PATH).append("content.xml").toString());
						if (i != 0 && j != 0 && i > j) {
							Intent intent = new Intent("android.intent.action.VIEW");
							intent.setDataAndType(Uri.fromFile(new File((new StringBuilder()).append(CG_DATA.CG_PATH).append("cg.apk").toString())),
									"application/vnd.android.package-archive");
							startActivityForResult(intent, requestCode);
							break;
						}
					}
					Bundle bundle = new Bundle();
					bundle.putBoolean("is_first_install", true);
					startActivity(CGUpdateActivity.this, CGStartActivity.class, bundle);
					finish();
					break;
				case 4:
					downloadingInfo.timeoutCount = 0;
					lastUpdate = Integer.parseInt(CGLastUpdateData
							.getLastUpdate(CGUpdateActivity.this, CG_DATA.CG_ID, CG_DATA.CG_VERSION, updateTime).lastUpdate);
					parameters = CGDownData.getCGDown_newParams((new StringBuilder()).append("").append(CG_ID).toString(), (new StringBuilder())
							.append("").append(updateTime).toString(), (new StringBuilder()).append("").append(lastUpdate).toString(),
							GetTimeData.getGotTime(CGUpdateActivity.this));
					downloadingInfo.downAsyncTask = new DownAsyncTask(handler);
					DownAsyncTask downasynctask1 = downloadingInfo.downAsyncTask;
					Object aobj1[] = new Object[4];
					aobj1[0] = "get";
					aobj1[1] = TCData.CG_ROOT;
					aobj1[2] = parameters;
					aobj1[3] = CGDownData.getUrl();
					downasynctask1.execute(aobj1);
					break;
				case 5:
					if (TCUtil.isNetAvailable(CGUpdateActivity.this)) {
						if (downloadingInfo.timeoutCount > 10) {
							downloadingInfo.timeoutCount = 0;
							downloadingInfo.isDownloading = false;
							downloadingInfo.isUnZip = false;
							refreshUI();
						} else {
							DownloadingInfo downloadinginfo = downloadingInfo;
							downloadinginfo.timeoutCount = 1 + downloadinginfo.timeoutCount;
							parameters = CGDownData.getCGDown_newParams((new StringBuilder()).append("").append(CG_ID).toString(),
									(new StringBuilder()).append("").append(updateTime).toString(),
									(new StringBuilder()).append("").append(lastUpdate).toString(), GetTimeData.getGotTime(CGUpdateActivity.this));
							downloadingInfo.downAsyncTask = new DownAsyncTask(handler);
							DownAsyncTask downasynctask = downloadingInfo.downAsyncTask;
							Object aobj[] = new Object[4];
							aobj[0] = "get";
							aobj[1] = TCData.CG_ROOT;
							aobj[2] = parameters;
							aobj[3] = CGDownData.getUrl();
							downasynctask.execute(aobj);
						}
					} else {
						downloadingInfo.isDownloading = false;
						downloadingInfo.isUnZip = false;
						refreshUI();
					}
					break;
				case 6:
					CGUpdateActivity.textViewStatus.setText(0x7f08009a);
					CGUpdateActivity.textViewProgress.setVisibility(8);
					progressBar.setProgress(message.arg1);
					progressBar.setMax(message.arg2);
					break;
				default:
					Toast.makeText(CGUpdateActivity.this, getString(0x7f080063), 1).show();
				}

			}

		};
	}

	private int getVersion(String s) {
		int i = 0;
		XmlPullParser xmlpullparser;
		int j;
		java.io.InputStream inputstream = TCUtil.getFileInputStream(this, s);
		xmlpullparser = Xml.newPullParser();
		try {
			xmlpullparser.setInput(inputstream, "UTF-8");
			j = xmlpullparser.getEventType();
			while (j != XmlPullParser.END_DOCUMENT) {
				if (j == XmlPullParser.START_TAG) {
					if ("ver".equals(xmlpullparser.getName())) {
						i = Integer.parseInt(xmlpullparser.getAttributeValue(null, "ver"));
						break;
					}
				}
				j = xmlpullparser.next();
			}
			inputstream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;

	}

	private void refreshUI() {
		if (downloadingInfo.isDownloading) {
			textViewStatus.setText(0x7f080087);
			ok.setText(0x7f080098);
		} else if (downloadingInfo.isUnZip) {
			textViewStatus.setText(0x7f08009a);
			ok.setClickable(false);
			ok.setEnabled(false);
			cancel.setClickable(false);
			cancel.setEnabled(false);
		} else if (!downloadingInfo.isDownloading || !downloadingInfo.isUnZip) {
			textViewStatus.setText(0x7f080099);
			ok.setText(0x7f08009c);
		}
		if (downloadingInfo.TOTAL != 0L) {
			textViewProgress.setText((new StringBuilder())
					.append(TCUtil.formatNum((100D * (double) downloadingInfo.hasDownloaded) / (double) downloadingInfo.TOTAL)).append("%")
					.append(" (").append(TCUtil.getDownloadFileSize(downloadingInfo.hasDownloaded)).append("/")
					.append(TCUtil.getDownloadFileSize(downloadingInfo.TOTAL)).append(")").toString());
			progressBar.setProgress((int) ((100D * (double) downloadingInfo.hasDownloaded) / (double) downloadingInfo.TOTAL));
		} else {
			textViewProgress.setText("0.0% (0.0MB/0.0MB)");
			progressBar.setProgress(0);
		}
		if (progressBar.getProgress() < 100 && !bs[progressBar.getProgress() / 25]) {
			flipper.setCurrentItem(progressBar.getProgress() / 25);
			bs[progressBar.getProgress() / 25] = true;
		}
	}

	protected void onActivityResult(int i, int j, Intent intent) {
		if (i == requestCode && j == 0) {
			startActivity(this, CGStartActivity.class);
			finish();
		}
		super.onActivityResult(i, j, intent);
	}

	public void onBackPressed() {
	}

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030034);
		ok = (Button) findViewById(0x7f0a012d);
		cancel = (Button) findViewById(0x7f0a012e);
		iv1 = new ImageView(this);
		iv1.setImageResource(R.drawable.a1);
		iv1.setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
		iv2 = new ImageView(this);
		iv2.setImageResource(R.drawable.a2);
		iv2.setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
		iv3 = new ImageView(this);
		iv3.setImageResource(R.drawable.a3);
		iv3.setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
		iv4 = new ImageView(this);
		iv4.setImageResource(R.drawable.a4);
		iv4.setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
		list.add(iv1);
		list.add(iv2);
		list.add(iv3);
		list.add(iv4);
		ImageView imageview = (ImageView) findViewById(R.id.d);
		ImageView imageview1 = (ImageView) findViewById(R.id.dd);
		ImageView imageview2 = (ImageView) findViewById(R.id.ddd);
		ImageView imageview3 = (ImageView) findViewById(R.id.dddd);
		list1.add(imageview);
		list1.add(imageview1);
		list1.add(imageview2);
		list1.add(imageview3);
		flipper = (ViewPager) findViewById(0x7f0a0126);
		PagerAdapter pageradapter = new PagerAdapter() {

			public void destroyItem(View view, int i, Object obj) {
				((ViewPager) view).removeView((View) list.get(i));
			}

			public void finishUpdate(View view) {
			}

			public int getCount() {
				return list.size();
			}

			public Object instantiateItem(View view, int i) {
				((ViewPager) view).addView((View) list.get(i), 0);
				return list.get(i);
			}

			public boolean isViewFromObject(View view, Object obj) {
				boolean flag;
				if (view == obj)
					flag = true;
				else
					flag = false;
				return flag;
			}

			public void restoreState(Parcelable parcelable, ClassLoader classloader) {
			}

			public Parcelable saveState() {
				return null;
			}

			public void startUpdate(View view) {
			}

		};
		flipper.setAdapter(pageradapter);
		// when page changes, the indication image icon should change(selected
		// or unselected).
		flipper.setOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener() {

			public void onPageScrollStateChanged(int i) {
			}

			public void onPageScrolled(int i, float f, int j) {
			}

			public void onPageSelected(int i) {
				((ImageView) list1.get(i)).setImageResource(R.drawable.brand8);// selected
				for (int j = 0; j < list1.size(); j++)
					if (j != i)
						((ImageView) list1.get(j)).setImageResource(R.drawable.brand7);// unselected

			}

		});
		textViewStatus = (TextView) findViewById(R.id.CGDownloadStatus);
		textViewProgress = (TextView) findViewById(R.id.CGDownloadProgress);
		progressBar = (ProgressBar) findViewById(R.id.pb);
		if (getIntent().getBooleanExtra("is_unzip_immediately", false)) {
			textViewStatus.setText(0x7f08009a);
			ok.setClickable(false);
			ok.setEnabled(false);
			cancel.setClickable(false);
			cancel.setEnabled(false);
			textViewProgress.setText("");
			progressBar.setProgress(100);
			TCUtil.isUnZipInterrapted("cg", CG_ID, true);
			startActivity(this, CGStartActivity.class);
			finish();
		} else if (getIntent().getBooleanExtra("is_assert_zip", false)) {
			textViewStatus.setText(0x7f08009a);
			ok.setClickable(false);
			ok.setEnabled(false);
			cancel.setClickable(false);
			cancel.setEnabled(false);
			textViewProgress.setText("");
			progressBar.setProgress(100);
			if (TCUtil.getSdCardFree() < 50L)
				Toast.makeText(this, getString(0x7f080063), 1).show();
			UnzipAsyncTask unzipasynctask = new UnzipAsyncTask();
			String as[] = new String[2];
			as[0] = "cg.zip";
			as[1] = (new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina").append("/").append("CG").append("/").append(CG_DATA.CG_ID)
					.append("/").toString();
			unzipasynctask.execute(as);
		} else {
			lastUpdate = getIntent().getIntExtra("last_Update", 0x7fffffff);
			updateTime = getIntent().getIntExtra("update_time", 0);
			parameters = CGDownData.getCGDown_newParams((new StringBuilder()).append("").append(CG_ID).toString(), (new StringBuilder()).append("")
					.append(updateTime).toString(), (new StringBuilder()).append("").append(lastUpdate).toString(), GetTimeData.getGotTime(this));
			downloadingInfo = new DownloadingInfo();
			downloadingInfo.isDownloading = true;
			downloadingInfo.downAsyncTask = new DownAsyncTask(handler);
			DownAsyncTask downasynctask = downloadingInfo.downAsyncTask;
			Object aobj[] = new Object[4];
			aobj[0] = "get";
			aobj[1] = TCData.CG_ROOT;
			aobj[2] = parameters;
			aobj[3] = CGDownData.getUrl();
			downasynctask.execute(aobj);
		}
		ok.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				if (downloadingInfo.isDownloading) {
					downloadingInfo.isDownloading = false;
					downloadingInfo.downAsyncTask.cancel();
				} else {
					downloadingInfo.isDownloading = true;
					parameters = CGDownData.getCGDown_newParams((new StringBuilder()).append("").append(CG_ID).toString(), (new StringBuilder())
							.append("").append(updateTime).toString(), (new StringBuilder()).append("").append(lastUpdate).toString(),
							GetTimeData.getGotTime(CGUpdateActivity.this));
					downloadingInfo.downAsyncTask = new DownAsyncTask(handler);
					DownAsyncTask downasynctask1 = downloadingInfo.downAsyncTask;
					Object aobj1[] = new Object[4];
					aobj1[0] = "get";
					aobj1[1] = TCData.CG_ROOT;
					aobj1[2] = parameters;
					aobj1[3] = CGDownData.getUrl();
					downasynctask1.execute(aobj1);
				}
				refreshUI();
			}

		});
		cancel.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				String s;
				if (getIntent().getBooleanExtra("is_first_install", true))
					s = getString(0x7f0800aa);
				else
					s = getString(0x7f0800ac);
				TCUtil.createPositiveDialog(CGUpdateActivity.this, s, new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialoginterface, int i) {
						downloadingInfo.downAsyncTask.cancel();
						if (getIntent().getBooleanExtra("is_first_install", true)) {
							finish();
						} else {
							startActivity(CGUpdateActivity.this, CGStartActivity.class);
							CGUtil.startCGService(CGUpdateActivity.this);
							finish();
						}
					}

				}).show();
			}

		});
	}

	public static final String KEY_IS_ASSERT_ZIP = "is_assert_zip";
	public static final String KEY_IS_FIRST_INSTALL = "is_first_install";
	public static final String KEY_IS_UNZIP_IMMEDIATELY = "is_unzip_immediately";
	public static final String KEY_LAST_UPDATE = "last_Update";
	public static final String KEY_UPDATE_TIME = "update_time";
	private static TextView textViewProgress;
	private static TextView textViewStatus;
	private boolean bs[] = { false, false, false, false };
	private Button cancel;
	private int count;
	private DownloadingInfo downloadingInfo;
	private ViewPager flipper;
	private Handler handler;
	private ImageView iv1;
	private ImageView iv2;
	private ImageView iv3;
	private ImageView iv4;
	private int lastUpdate;
	private ArrayList list;
	private ArrayList list1;
	private Button ok;
	private Map parameters;
	private ProgressBar progressBar;
	int requestCode;
	private int updateTime;

}
