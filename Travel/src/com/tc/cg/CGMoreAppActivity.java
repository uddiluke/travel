//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tc.TCData;
import com.tc.TCUtil;
import com.tc.logic.CGMoreAppData;
import com.touchchina.cityguide.sz.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData, CGMoreAppItemActivity

public class CGMoreAppActivity extends CGBaseActivity implements android.view.View.OnClickListener, android.widget.AdapterView.OnItemClickListener {
	private class MoreAppAdapter extends BaseAdapter {

		public int getCount() {
			return cgMoreAppDataList.size();
		}

		public Object getItem(int i) {
			return Integer.valueOf(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			if (view == null)
				view = LayoutInflater.from(CGMoreAppActivity.this).inflate(0x7f030016, null);
			ImageView imageview = (ImageView) view.findViewById(0x7f0a0086);
			TextView textview = (TextView) view.findViewById(0x7f0a0089);
			textview.setTextColor(0xff000000);
			TextView textview1 = (TextView) view.findViewById(0x7f0a008a);
			ProgressBar progressbar = (ProgressBar) view.findViewById(0x7f0a0087);
			textview.setText(((CGMoreAppData) cgMoreAppDataList.get(i)).name);
			textview1.setHint(((CGMoreAppData) cgMoreAppDataList.get(i)).summary);
			if (bitmapList != null && bitmapList.size() > 0 && i < bitmapList.size()) {
				if (bitmapList.get(i) != null) {
					imageview.setImageBitmap((Bitmap) bitmapList.get(i));
					imageview.setVisibility(0);
					progressbar.setVisibility(8);
				}
			} else {
				imageview.setVisibility(8);
				progressbar.setVisibility(0);
			}
			view.setBackgroundColor(colors[i % colors.length]);
			return view;
		}

		private ArrayList<Bitmap> bitmapList;
		private ArrayList<CGMoreAppData> cgMoreAppDataList;
		private int colors[] = { -1, 0xfff4f4f4 };

		public MoreAppAdapter(ArrayList<CGMoreAppData> arraylist, ArrayList<Bitmap> arraylist1) {

			cgMoreAppDataList = arraylist;
			bitmapList = arraylist1;
		}
	}

	private class MoreAppAsy extends AsyncTask {

		protected Object doInBackground(Object aobj[]) {
			return doInBackground((String[]) aobj);
		}

		protected String doInBackground(String as[]) {
			if (!(new File(morePath)).exists()) { // goto _L2; else goto _L1

				cgMoreAppList = CGMoreAppData.getMoreAppData("https://service.itouchchina.com/restsvcs/apps/os/android", morePath);
				publishProgress(new String[] { "undone" });
				topApp = CGMoreAppData.getTopApp();
				if (topApp != null) {
					String s7 = (new StringBuilder()).append(CG_DATA.CG_PATH).append("more/")
							.append(topApp.topimage.split("/")[-1 + topApp.topimage.split("/").length]).toString();
					if (CGMoreAppData.getImageBitmap(topApp.topimage) != null)
						TCUtil.saveNewFile(CGMoreAppData.getImageBitmap(topApp.topimage), s7);
					topBitmap = BitmapFactory.decodeFile(s7);
				}
				publishProgress(new String[] { "topimage" });
				bitmapList = new ArrayList<Bitmap>();
				if (isTrue()) {
					for (int l = 0; l < cgMoreAppList.size(); l++) {
						String s6 = (new StringBuilder())
								.append(CG_DATA.CG_PATH)
								.append("more/")
								.append(((CGMoreAppData) cgMoreAppList.get(l)).icon.split("/")[-2
										+ ((CGMoreAppData) cgMoreAppList.get(l)).icon.split("/").length])
								.append(((CGMoreAppData) cgMoreAppList.get(l)).icon.split("/")[-1
										+ ((CGMoreAppData) cgMoreAppList.get(l)).icon.split("/").length]).toString();
						java.io.InputStream inputstream1 = CGMoreAppData.getSmallImage(((CGMoreAppData) cgMoreAppList.get(l)).icon);
						if (inputstream1 != null) {
							TCUtil.saveNewFile(inputstream1, s6);
							bitmapList.add(BitmapFactory.decodeFile(s6));
						}
						publishProgress(new String[] { "done" });
					}

				}
			} else {

				cgMoreAppList = CGMoreAppData.getMoreDataFromNative(morePath);
				topApp = CGMoreAppData.getTopApp();
				bitmapList = new ArrayList<Bitmap>();
				if (isTrue()) {
					int k = 0;
					while (k < cgMoreAppList.size()) {
						String s4 = (new StringBuilder())
								.append(CG_DATA.CG_PATH)
								.append("more/")
								.append(((CGMoreAppData) cgMoreAppList.get(k)).icon.split("/")[-2
										+ ((CGMoreAppData) cgMoreAppList.get(k)).icon.split("/").length])
								.append(((CGMoreAppData) cgMoreAppList.get(k)).icon.split("/")[-1
										+ ((CGMoreAppData) cgMoreAppList.get(k)).icon.split("/").length]).toString();
						if (BitmapFactory.decodeFile(s4) != null) {
							bitmapList.add(BitmapFactory.decodeFile(s4));
						} else {
							String s5 = (new StringBuilder())
									.append(CG_DATA.CG_PATH)
									.append("more/")
									.append(((CGMoreAppData) cgMoreAppList.get(k)).icon.split("/")[-2
											+ ((CGMoreAppData) cgMoreAppList.get(k)).icon.split("/").length])
									.append(((CGMoreAppData) cgMoreAppList.get(k)).icon.split("/")[-1
											+ ((CGMoreAppData) cgMoreAppList.get(k)).icon.split("/").length]).toString();
							java.io.InputStream inputstream = CGMoreAppData.getSmallImage(((CGMoreAppData) cgMoreAppList.get(k)).icon);
							if (inputstream != null) {
								TCUtil.saveNewFile(inputstream, s5);
								bitmapList.add(BitmapFactory.decodeFile(s5));
							}
						}
						k++;
					}
				}
			}
			if (topApp != null) {
				String s3 = (new StringBuilder()).append(CG_DATA.CG_PATH).append("more/")
						.append(topApp.topimage.split("/")[-1 + topApp.topimage.split("/").length]).toString();
				topBitmap = BitmapFactory.decodeFile(s3);
				if (topBitmap == null) {
					TCUtil.saveNewFile(CGMoreAppData.getImageBitmap(topApp.topimage), s3);
					topBitmap = BitmapFactory.decodeFile(s3);
				}
			}
			publishProgress(new String[] { "hasxmlundone" });
			timestamp = getTime(morePath);
			cgMoreAppCopyList = CGMoreAppData.getMoreAppData("https://service.itouchchina.com/restsvcs/apps/os/android", morePath);
			topAppCopy = CGMoreAppData.getTopApp();
			if (topAppCopy != null && !topApp.timestamp.equals(topAppCopy.timestamp)) {
				String s2 = (new StringBuilder()).append(CG_DATA.CG_PATH).append("more/")
						.append(topAppCopy.topimage.split("/")[-1 + topAppCopy.topimage.split("/").length]).toString();
				TCUtil.saveNewFile(CGMoreAppData.getImageBitmap(topAppCopy.topimage), s2);
				topBitmap = BitmapFactory.decodeFile(s2);
				topApp = topAppCopy;
			}
			publishProgress(new String[] { "topImageUpdate" });
			lastTimestamp = CGMoreAppData.timestampApps;
			if (!timestamp.equals(lastTimestamp)) {
				bitmapCopyList = new ArrayList<Bitmap>();
				if (cgMoreAppCopyList != null && cgMoreAppCopyList.size() > 0) { // goto
																					// _L6;
																					// else
																					// goto
																					// _L5
					int i = 0;
					while (i < cgMoreAppCopyList.size()) {
						// boolean flag = true;
						int j = 0;
						while (j < cgMoreAppList.size()) {
							if (((CGMoreAppData) cgMoreAppCopyList.get(i)).id.equals(((CGMoreAppData) cgMoreAppList.get(j)).id)) {
								break;
							}
							j++;
						}
						if (j < cgMoreAppList.size()) {
							// String s;
							if (((CGMoreAppData) cgMoreAppCopyList.get(i)).timestamp.equals(((CGMoreAppData) cgMoreAppList.get(j)).timestamp)) {
								bitmapCopyList.add(BitmapFactory.decodeFile((new StringBuilder())
										.append(CG_DATA.CG_PATH)
										.append("more/")
										.append(((CGMoreAppData) cgMoreAppCopyList.get(i)).icon.split("/")[-2
												+ ((CGMoreAppData) cgMoreAppCopyList.get(i)).icon.split("/").length])
										.append(((CGMoreAppData) cgMoreAppCopyList.get(i)).icon.split("/")[-1
												+ ((CGMoreAppData) cgMoreAppCopyList.get(i)).icon.split("/").length]).toString()));
							} else {
								String s1 = (new StringBuilder())
										.append(CG_DATA.CG_PATH)
										.append("more/")
										.append(((CGMoreAppData) cgMoreAppCopyList.get(i)).icon.split("/")[-2
												+ ((CGMoreAppData) cgMoreAppCopyList.get(i)).icon.split("/").length])
										.append(((CGMoreAppData) cgMoreAppCopyList.get(i)).icon.split("/")[-1
												+ ((CGMoreAppData) cgMoreAppCopyList.get(i)).icon.split("/").length]).toString();
								TCUtil.deleteFile((new StringBuilder())
										.append(CG_DATA.CG_PATH)
										.append("more/")
										.append(((CGMoreAppData) cgMoreAppList.get(j)).icon.split("/")[-2
												+ ((CGMoreAppData) cgMoreAppList.get(j)).icon.split("/").length])
										.append(((CGMoreAppData) cgMoreAppList.get(j)).icon.split("/")[-1
												+ ((CGMoreAppData) cgMoreAppList.get(j)).icon.split("/").length]).toString());
								TCUtil.saveNewFile(CGMoreAppData.getImageBitmap(((CGMoreAppData) cgMoreAppCopyList.get(i)).icon), s1);
								bitmapCopyList.add(BitmapFactory.decodeFile(s1));
							}
						} else {
							String s = (new StringBuilder())
									.append(CG_DATA.CG_PATH)
									.append("more/")
									.append(((CGMoreAppData) cgMoreAppCopyList.get(i)).icon.split("/")[-2
											+ ((CGMoreAppData) cgMoreAppCopyList.get(i)).icon.split("/").length])
									.append(((CGMoreAppData) cgMoreAppCopyList.get(i)).icon.split("/")[-1
											+ ((CGMoreAppData) cgMoreAppCopyList.get(i)).icon.split("/").length]).toString();
							TCUtil.saveNewFile(CGMoreAppData.getImageBitmap(((CGMoreAppData) cgMoreAppCopyList.get(i)).icon), s);
							bitmapCopyList.add(BitmapFactory.decodeFile(s));
						}
						i++;
					}
				}
				cgMoreAppList = cgMoreAppCopyList;
				bitmapList = bitmapCopyList;
				publishProgress(new String[] { "updateDone" });
			}

			return null;

		}

		protected void onPostExecute(Object obj) {
			onPostExecute((String) obj);
		}

		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			moreProgressBar.setVisibility(8);
			moreUpdateButton.setVisibility(0);
		}

		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected void onProgressUpdate(Object aobj[]) {
			onProgressUpdate((String[]) aobj);
		}

		protected void onProgressUpdate(String as[]) {
			super.onProgressUpdate(as);
			if ("undone".equals(as[0])) { // goto _L2; else goto _L1

				Bitmap bitmap;
				if (isTrue()) {
					MoreAppAdapter moreappadapter3 = new MoreAppAdapter(cgMoreAppList, bitmapList);
					moreAppList.setAdapter(moreappadapter3);
					setListViewHeightBasedOnChildren(moreAppList);
				} else {
					moreLinearLayout.setBackgroundResource(0x7f0200e3);
				}
				bitmap = BitmapFactory.decodeResource(getResources(), 0x7f02017a);
				topImageView.setImageResource(0x7f02017a);
				topImageView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(TCData.SCREEN_WIDTH,
						(int) (((float) bitmap.getHeight() * (float) TCData.SCREEN_WIDTH) / (float) bitmap.getWidth())));
				topImageView.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
			} else if ("topimage".equals(as[0]))
				setTopImageBitmap();
			else if ("done".equals(as[0])) {
				if (isTrue()) {
					int i = moreAppList.getFirstVisiblePosition();
					MoreAppAdapter moreappadapter2 = new MoreAppAdapter(cgMoreAppList, bitmapList);
					moreAppList.setAdapter(moreappadapter2);
					setListViewHeightBasedOnChildren(moreAppList);
					moreAppList.setSelectionFromTop(i, 0);
				} else {
					moreLinearLayout.setBackgroundResource(0x7f0200e3);
				}
			} else if ("hasxmlundone".equals(as[0])) {
				MoreAppAdapter moreappadapter = new MoreAppAdapter(cgMoreAppList, bitmapList);
				moreAppList.setAdapter(moreappadapter);
				setListViewHeightBasedOnChildren(moreAppList);
				setTopImageBitmap();
			} else if ("updateDone".equals(as[0])) {
				MoreAppAdapter moreappadapter1 = new MoreAppAdapter(cgMoreAppList, bitmapList);
				moreAppList.setAdapter(moreappadapter1);
				setListViewHeightBasedOnChildren(moreAppList);
			} else if ("topImageUpdate".equals(as[0]))
				setTopImageBitmap();

		}

	}

	public CGMoreAppActivity() {
	}

	private String getTime(String s) {

		String s1 = null;
		InputStream inputstream = TCUtil.getFileInputStream(this, s);
		XmlPullParser xmlpullparser = Xml.newPullParser();
		try {
			xmlpullparser.setInput(inputstream, "UTF-8");
			int j = xmlpullparser.getEventType();
			while (j != XmlPullParser.END_DOCUMENT) {
				if (j == XmlPullParser.START_TAG) {
					if ("timestamp".equals(xmlpullparser.getName())) {
						s1 = xmlpullparser.nextText();

						break;
					}
				} 
					j = xmlpullparser.next();
				
			}
		} catch (Exception e) {
			Log.e(TAG, "private int getTime(String filepath)", e);
		}

		return s1;

	}

	private void setTopImageBitmap() {
		if (topBitmap != null) {
			topImageView.setImageBitmap(topBitmap);
			topImageView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(TCData.SCREEN_WIDTH,
					(int) (((float) topBitmap.getHeight() * (float) TCData.SCREEN_WIDTH) / (float) topBitmap.getWidth())));
			topImageView.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
			topImageView.setOnClickListener(new android.view.View.OnClickListener() {

				public void onClick(View view) {
					Bundle bundle = new Bundle();
					bundle.putString("appID", topApp.id);
					bundle.putString("appName", topApp.name);
					bundle.putString("appIcon", topApp.icon);
					startActivity(CGMoreAppActivity.this, CGMoreAppItemActivity.class, bundle);
				}

			});
		}
	}

	public boolean isTrue() {
		boolean flag;
		if (cgMoreAppList != null && cgMoreAppList.size() > 0)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.moreBackButton:
			super.onBackPressed();
			break;
		case R.id.moreUpdateProgress:
			break;
		case R.id.moreUpdateButton:
			moreProgressBar.setVisibility(0);
			moreUpdateButton.setVisibility(8);
			(new MoreAppAsy()).execute(new String[] { "" });
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.cg_more_app);
		if (!(new File((new StringBuilder()).append(CG_DATA.CG_PATH).append("more").toString())).exists())
			(new File((new StringBuilder()).append(CG_DATA.CG_PATH).append("more").toString())).mkdirs();
		moreUpdateButton = (ImageView) findViewById(0x7f0a0078);
		moreUpdateButton.setOnClickListener(this);
		moreBackButton = (ImageView) findViewById(0x7f0a0076);
		moreBackButton.setOnClickListener(this);
		moreProgressBar = (ProgressBar) findViewById(0x7f0a0077);
		moreAppList = (ListView) findViewById(0x7f0a007c);
		moreAppList.setOnItemClickListener(this);
		topImageView = (ImageView) findViewById(0x7f0a007b);
		moreLinearLayout = (LinearLayout) findViewById(0x7f0a007a);
		morePath = (new StringBuilder()).append(CG_DATA.CG_PATH).append("more/more.xml").toString();
		scrollview = (ScrollView) findViewById(0x7f0a0079);
		handler = new Handler();
		handler.postDelayed(new Runnable() {

			public void run() {
				scrollview.scrollTo(0, -400);
			}

		}, 300L);
		(new MoreAppAsy()).execute(new String[] { "" });
	}

	protected void onDestroy() {
		super.onDestroy();
		if (bitmapList != null && bitmapList.size() > 0) {
			for (int i = 0; i < bitmapList.size(); i++)
				TCUtil.recycleBitmap((Bitmap) bitmapList.get(i));

		}
	}

	public void onItemClick(AdapterView adapterview, View view, int i, long l) {
		if (isTrue()) {
			Bundle bundle = new Bundle();
			bundle.putString("appID", ((CGMoreAppData) cgMoreAppList.get(i)).id);
			bundle.putString("appName", ((CGMoreAppData) cgMoreAppList.get(i)).name);
			bundle.putString("appIcon", ((CGMoreAppData) cgMoreAppList.get(i)).icon);
			startActivity(this, CGMoreAppItemActivity.class, bundle);
		}
	}

	protected void onResume() {
		super.onResume();
		if (!TCUtil.isNetAvailable(this) && !(new File(morePath)).exists())
			moreLinearLayout.setBackgroundResource(0x7f0200e3);
	}

	public void setListViewHeightBasedOnChildren(ListView listview) {
		ListAdapter listadapter = listview.getAdapter();
		if (listadapter != null) {
			int i = 0;
			for (int j = 0; j < listadapter.getCount(); j++) {
				View view = listadapter.getView(j, null, listview);
				view.measure(0, 0);
				i += view.getMeasuredHeight();
			}

			android.view.ViewGroup.LayoutParams layoutparams = listview.getLayoutParams();
			layoutparams.height = i + listview.getDividerHeight() * (-1 + listadapter.getCount());
			listview.setLayoutParams(layoutparams);
		}
	}

	private ArrayList<Bitmap> bitmapCopyList;
	private ArrayList<Bitmap> bitmapList;
	private ArrayList<CGMoreAppData> cgMoreAppCopyList;
	private ArrayList<CGMoreAppData> cgMoreAppList;
	private Handler handler;
	private String lastTimestamp;
	private ListView moreAppList;
	private ImageView moreBackButton;
	private LinearLayout moreLinearLayout;
	private String morePath;
	private ProgressBar moreProgressBar;
	private ImageView moreUpdateButton;
	private ScrollView scrollview;
	private String timestamp;
	private com.tc.logic.CGMoreAppData.TopApp topApp;
	private com.tc.logic.CGMoreAppData.TopApp topAppCopy;
	private Bitmap topBitmap;
	private ImageView topImageView;
	public static final String TAG = CGMoreAppActivity.class.getSimpleName();

	/*
	 * static com.tc.logic.CGMoreAppData.TopApp access$1002(CGMoreAppActivity
	 * cgmoreappactivity, com.tc.logic.CGMoreAppData.TopApp topapp) {
	 * cgmoreappactivity.topAppCopy = topapp; return topapp; }
	 */

	/*
	 * static String access$1102(CGMoreAppActivity cgmoreappactivity, String s)
	 * { cgmoreappactivity.lastTimestamp = s; return s; }
	 */

	/*
	 * static ArrayList access$1202(CGMoreAppActivity cgmoreappactivity,
	 * ArrayList arraylist) { cgmoreappactivity.bitmapCopyList = arraylist;
	 * return arraylist; }
	 */

	/*
	 * static ArrayList access$302(CGMoreAppActivity cgmoreappactivity,
	 * ArrayList arraylist) { cgmoreappactivity.cgMoreAppList = arraylist;
	 * return arraylist; }
	 */

	/*
	 * static com.tc.logic.CGMoreAppData.TopApp access$402(CGMoreAppActivity
	 * cgmoreappactivity, com.tc.logic.CGMoreAppData.TopApp topapp) {
	 * cgmoreappactivity.topApp = topapp; return topapp; }
	 */

	/*
	 * static Bitmap access$502(CGMoreAppActivity cgmoreappactivity, Bitmap
	 * bitmap) { cgmoreappactivity.topBitmap = bitmap; return bitmap; }
	 */

	/*
	 * static ArrayList access$602(CGMoreAppActivity cgmoreappactivity,
	 * ArrayList arraylist) { cgmoreappactivity.bitmapList = arraylist; return
	 * arraylist; }
	 */

	/*
	 * static String access$702(CGMoreAppActivity cgmoreappactivity, String s) {
	 * cgmoreappactivity.timestamp = s; return s; }
	 */

	/*
	 * static ArrayList access$902(CGMoreAppActivity cgmoreappactivity,
	 * ArrayList arraylist) { cgmoreappactivity.cgMoreAppCopyList = arraylist;
	 * return arraylist; }
	 */
}
