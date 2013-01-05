//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.xmlpull.v1.XmlPullParser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.tc.TCData;
import com.tc.TCUtil;
import com.tc.logic.CGNewsData;
import com.touchchina.cityguide.sz.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData, CGGalleryActivity, CGNewsItemActivity

public class CGNewsActivity extends CGBaseActivity implements android.view.View.OnClickListener, android.widget.AdapterView.OnItemClickListener {
	private class CGNewsAdapter extends BaseAdapter {

		public int getCount() {
			return cgNewsList.size();
		}

		public Object getItem(int i) {
			return cgNewsList.get(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			if (view == null)
				view = LayoutInflater.from(CGNewsActivity.this).inflate(0x7f03001c, null);
			ImageView imageview = (ImageView) view.findViewById(0x7f0a009d);
			TextView textview = (TextView) view.findViewById(0x7f0a009f);
			textview.setTextColor(0xff000000);
			TextView textview1 = (TextView) view.findViewById(0x7f0a008a);
			ProgressBar progressbar = (ProgressBar) view.findViewById(0x7f0a009e);
			textview.setText(((CGNewsData) cgNewsList.get(i)).title);
			textview1.setHint(((CGNewsData) cgNewsList.get(i)).summary);
			if (bitmapList != null && bitmapList.size() > 0 && i < bitmapList.size()) {
				if (bitmapList.get(i) != null) {
					imageview.setImageBitmap((Bitmap) bitmapList.get(i));
					imageview.setVisibility(0);
					progressbar.setVisibility(8);
				} else {
					imageview.setVisibility(8);
					progressbar.setVisibility(0);
				}
			} else {
				imageview.setVisibility(8);
				progressbar.setVisibility(0);
			}
			view.setBackgroundColor(colors[i % colors.length]);
			return view;
		}

		private ArrayList bitmapList;
		private ArrayList cgNewsList;
		private int colors[] = { -1, 0xfff4f4f4 };

		public CGNewsAdapter(ArrayList arraylist, ArrayList arraylist1) {

			cgNewsList = arraylist;
			bitmapList = arraylist1;
		}
	}

	private class CGNewsAsy extends AsyncTask {

		protected Object doInBackground(Object aobj[]) {
			return doInBackground((String[]) aobj);
		}

		protected String doInBackground(String as[]) {
			if (!(new File((new StringBuilder()).append(cgPath).append("news/bigImage").toString())).exists())
				(new File((new StringBuilder()).append(cgPath).append("news/bigImage").toString())).mkdirs();
			if (as[0].equals("all")) {
				// goto _L2; else goto _L1
				if ((new File(newsPath)).exists()) {
					cgNewsList = CGNewsData.getNewsDataFromNative(newsPath);
					topInfo = CGNewsData.getTopInfo();
					if (topInfo == null && cgNewsList != null && cgNewsList.size() > 0) {
						isHasTop = true;
						topInfo = new com.tc.logic.CGNewsData.TopInfo();
						topInfo.img = ((CGNewsData) cgNewsList.get(0)).img;
						topInfo.title = ((CGNewsData) cgNewsList.get(0)).title;
						topInfo.source = ((CGNewsData) cgNewsList.get(0)).source;
						topInfo.url = ((CGNewsData) cgNewsList.get(0)).url;
						cgNewsList.remove(0);
					}
					publishProgress(new String[] { "top" });
					cgNewsListClone = cgNewsList;
					CGNewsActivity.inforList = CGNewsData.getInforTypes();
					if (topInfo != null) {
						String s3 = (new StringBuilder()).append(cgPath).append("news/bigImage/")
								.append(topInfo.img.split("/")[-1 + topInfo.img.split("/").length]).toString();
						topBitmap = BitmapFactory.decodeFile(s3);
						if (topBitmap == null) {
							topImageInputStream = CGNewsData.getImageBitmap(topInfo.img);
							String s4 = (new StringBuilder()).append(cgPath).append("news/bigImage/")
									.append(topInfo.img.split("/")[-1 + topInfo.img.split("/").length]).toString();
							if (topImageInputStream != null)
								TCUtil.saveNewFile(topImageInputStream, s4);
							topBitmap = BitmapFactory.decodeFile(s3);
						}
					}
					if (cgNewsList != null && cgNewsList.size() > 0) {
						for (int j = 0; j < cgNewsList.size(); j++) {
							String s2 = (new StringBuilder())
									.append(cgPath)
									.append("news/")
									.append(((CGNewsData) cgNewsList.get(j)).thumbnail.split("/")[-1
											+ ((CGNewsData) cgNewsList.get(j)).thumbnail.split("/").length]).toString();
							bit = BitmapFactory.decodeFile(s2);
							if (bit != null)
								continue;
							InputStream inputstream = CGNewsData.getImageBitmap(((CGNewsData) cgNewsList.get(j)).thumbnail);
							if (inputstream != null)
								TCUtil.saveNewFile(inputstream, s2);
						}

					}
					publishProgress(new String[] { "all" });
					updateTime = getTime((new StringBuilder()).append(cgPath).append("news/news.xml").toString());
					timeStampCompare();
				} else {
					CGNewsActivity.flag = true;
					getNewsXml(CGNewsActivity.this, CG_DATA.CG_ID, updateTime, 20);
					if (topInfo == null && cgNewsList != null && cgNewsList.size() > 0) {
						isHasTop = true;
						topInfo = new com.tc.logic.CGNewsData.TopInfo();
						topInfo.img = ((CGNewsData) cgNewsList.get(0)).img;
						topInfo.title = ((CGNewsData) cgNewsList.get(0)).title;
						topInfo.source = ((CGNewsData) cgNewsList.get(0)).source;
						topInfo.url = ((CGNewsData) cgNewsList.get(0)).url;
						cgNewsList.remove(0);
					}
					publishProgress(new String[] { "top" });
					cgNewsListClone = cgNewsList;
					publishProgress(new String[] { "news" });
					if (topInfo != null) {
						topImageInputStream = CGNewsData.getImageBitmap(topInfo.img);
						String s1 = (new StringBuilder()).append(cgPath).append("news/bigImage/")
								.append(topInfo.img.split("/")[-1 + topInfo.img.split("/").length]).toString();
						TCUtil.saveNewFile(topImageInputStream, s1);
						topBitmap = BitmapFactory.decodeFile(s1);
					}
					if (cgNewsList != null && cgNewsList.size() > 0) {
						int i = 0;
						while (i < cgNewsList.size()) {
							String s = (new StringBuilder())
									.append(cgPath)
									.append("news/")
									.append(((CGNewsData) cgNewsList.get(i)).thumbnail.split("/")[-1
											+ ((CGNewsData) cgNewsList.get(i)).thumbnail.split("/").length]).toString();
							if ((new File(s)).exists()) {
								bit = BitmapFactory.decodeFile(s);
							} else {
								CGNewsData.getSmallImage(((CGNewsData) cgNewsList.get(i)).thumbnail, CG_DATA.CG_ID);
								bit = BitmapFactory.decodeFile(s);
							}
							bitmapList.add(TCUtil.getRoundedCornerBitmap(bit));
							publishProgress(new String[] { "done" });
							i++;
						}
					}
				}

			}

			{
				if (as[0].equals("update"))
					timeStampCompare();
				return as[0];
			}

		}

		protected void onPostExecute(Object obj) {
			onPostExecute((String) obj);
		}

		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			setTypeId();
			typeRelativeLayout.setVisibility(0);
			if (s.equals("all")) {
				if (cgNewsList != null && cgNewsList.size() > 0) {
					cgNewsAdapter = new CGNewsAdapter(cgNewsList, bitmapList);
					cgNewsListView.setAdapter(cgNewsAdapter);
				}
				if (topBitmap != null)
					setBitmap(topBitmap);
				newsUpdateButton.setVisibility(0);
				newsUpdateProgressBar.setVisibility(8);
				perform();
			} else if (cgNewsList != null && cgNewsList.size() > 0) {
				newsUpdateProgressBar.setVisibility(8);
				newsUpdateButton.setVisibility(0);
				if (topBitmap != null)
					setBitmap(topBitmap);
				cgNewsAdapter = new CGNewsAdapter(cgNewsList, bitmapList);
				cgNewsListView.setAdapter(cgNewsAdapter);
				perform();
			} else {
				newsUpdateProgressBar.setVisibility(8);
				newsUpdateButton.setVisibility(0);
			}
			if (!"OK".equals(CGNewsData.status))
				typeRelativeLayout.setVisibility(8);
		}

		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected void onProgressUpdate(Object aobj[]) {
			onProgressUpdate((String[]) aobj);
		}

		protected void onProgressUpdate(String as[]) {
			super.onProgressUpdate(as);
			setTypeId();
			blankView.setVisibility(0);
			typeRelativeLayout.setVisibility(0);
			if (as[0].equals("all")) {
				if (topBitmap != null) {
					setBitmap(topBitmap);
				} else {
					Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), 0x7f02017a);
					setBitmap(bitmap1);
				}
				if (cgNewsList != null && cgNewsList.size() > 0) {
					for (int j = 0; j < cgNewsList.size(); j++) {
						String s = (new StringBuilder())
								.append(cgPath)
								.append("news/")
								.append(((CGNewsData) cgNewsList.get(j)).thumbnail.split("/")[-1
										+ ((CGNewsData) cgNewsList.get(j)).thumbnail.split("/").length]).toString();
						bit = BitmapFactory.decodeFile(s);
						bitmapList.add(TCUtil.getRoundedCornerBitmap(bit));
					}

				}
				setTypes();
			}
			if (as[0].equals("news")) {
				if (topInfo != null) {
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), 0x7f02017a);
					setBitmap(bitmap);
				}
				setTypes();
			}
			if (as[0].equals("done")) {
				int i = 0;
				if (cgNewsList != null && cgNewsList.size() > 0) {
					i = cgNewsListView.getFirstVisiblePosition();
					cgNewsAdapter = new CGNewsAdapter(cgNewsList, bitmapList);
					cgNewsListView.setAdapter(cgNewsAdapter);
				}
				perform();
				cgNewsListView.setSelectionFromTop(i, 0);
			}
			if (as[0].equals("top") && topInfo != null) {
				cgNewsListView.addHeaderView(top_layout);
				typeFlag = "hasTopImage";
			}
		}

		private String newsPath;

		private CGNewsAsy() {

			newsPath = (new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina").append("/").append("CG").append("/")
					.append(CG_DATA.CG_ID).append("/").append("news/news.xml").toString();
		}

	}

	public CGNewsActivity() {
		tag = "all";
	}

	private ArrayList getNewsXml(Context context, int i, int j, int k) {
		cgNewsList = CGNewsData.getNewsData(context, i, j, k);
		inforList = CGNewsData.getInforTypes();
		topInfo = CGNewsData.getTopInfo();
		return cgNewsList;
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
					if ("infors".equals(xmlpullparser.getName())) {
						s1 = xmlpullparser.getAttributeValue(null, "timestamp");
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

	private boolean timeStampCompare() {
		flag = false;
		newsData = CGNewsData.getNewsData(this, CG_DATA.CG_ID, updateTime, 20);
		if (updateTime < CGNewsData.timestamp) { // goto _L2; else goto _L1
			ArrayList arraylist;
			flag = true;
			inforList = CGNewsData.getInforTypes();
			topInfo = CGNewsData.getTopInfo();
			if (topInfo == null && newsData != null && newsData.size() > 0) {
				isHasTop = true;
				topInfo = new com.tc.logic.CGNewsData.TopInfo();
				topInfo.img = ((CGNewsData) newsData.get(0)).img;
				topInfo.title = ((CGNewsData) newsData.get(0)).title;
				topInfo.source = ((CGNewsData) newsData.get(0)).source;
				topInfo.url = ((CGNewsData) newsData.get(0)).url;
				newsData.remove(0);
			}
			if (!(new File((new StringBuilder()).append(cgPath).append("news/bigImage").toString())).exists())
				(new File((new StringBuilder()).append(cgPath).append("news/bigImage").toString())).mkdirs();
			if (topInfo != null) {
				topImageInputStream = CGNewsData.getImageBitmap(topInfo.img);
				String s1 = (new StringBuilder()).append(cgPath).append("news/bigImage/")
						.append(topInfo.img.split("/")[-1 + topInfo.img.split("/").length]).toString();
				TCUtil.saveNewFile(topImageInputStream, s1);
				topBitmap = BitmapFactory.decodeFile(s1);
			}
			arraylist = new ArrayList();
			if (newsData != null && newsData.size() > 0) // goto _L4; else goto
															// _L3
			{
				int i = 0;
				while (i < newsData.size()) {
					String s = (new StringBuilder())
							.append(cgPath)
							.append("news/")
							.append(((CGNewsData) newsData.get(i)).thumbnail.split("/")[-1
									+ ((CGNewsData) newsData.get(i)).thumbnail.split("/").length]).toString();
					int j = 0;
					while (j < cgNewsList.size()) {
						if (((CGNewsData) newsData.get(i)).id.equals(((CGNewsData) cgNewsList.get(j)).id))
							break;
						j++;
					}
					if (j < cgNewsList.size()) {
						if (((CGNewsData) newsData.get(i)).dateTime.equals(((CGNewsData) cgNewsList.get(j)).dateTime)) {
							bit = BitmapFactory.decodeFile(s);
							arraylist.add(TCUtil.getRoundedCornerBitmap(bit));
						} else {
							CGNewsData.getSmallImage(((CGNewsData) newsData.get(i)).thumbnail, CG_DATA.CG_ID);
							bit = BitmapFactory.decodeFile(s);
							arraylist.add(TCUtil.getRoundedCornerBitmap(bit));
						}

					} else {
						CGNewsData.getSmallImage(((CGNewsData) newsData.get(i)).thumbnail, CG_DATA.CG_ID);
						bit = BitmapFactory.decodeFile(s);
						arraylist.add(TCUtil.getRoundedCornerBitmap(bit));
					}
					i++;
				}
				if (cgNewsList != null)
					cgNewsList.clear();
				cgNewsList = newsData;
				cgNewsListClone = cgNewsList;
				if (bitmapList != null)
					bitmapList.clear();
				bitmapList = arraylist;
				return true;
			}
		}
		return false;

	}

	public void onBackPressed() {
		if (cgNewsList == null || bitmapList == null)
			TCUtil.deleteEveryFile(new File((new StringBuilder()).append(cgPath).append("news/news.xml").toString()));
		if (topBitmap != null)
			topBitmap = null;
		super.onBackPressed();
	}

	public void onClick(View view) {
		if (view.getId() == newsId) {
			tag = "discount";
			cgNewsListView.setVisibility(8);
			cgNewsDiscountListView.setVisibility(0);
			TCUtil.flurryLog("newsTab", getString(0x7f0800b2));
			view.setBackgroundDrawable(getResources().getDrawable(0x7f0200ef));
			((TextView) view).setTextColor(-1);
			newsAndHuaBao.setTextColor(0xff000000);
			huabao.setTextColor(0xff000000);
			newsAndHuaBao.setBackgroundColor(0);
			huabao.setBackgroundColor(0);
			if (cgNewsList != null && cgNewsList.size() > 0) {
				ArrayList arraylist = new ArrayList();
				ArrayList arraylist1 = new ArrayList();
			    for(int i=0;i<cgNewsList.size();i++){
			    	CGNewsData cgnewsdata = (CGNewsData) cgNewsList.get(i);
			    	if (cgnewsdata.catid == view.getId()) {
						arraylist.add(cgnewsdata);
						//int i = cgNewsList.indexOf(cgnewsdata);
						if (i < bitmapList.size())
							arraylist1.add(bitmapList.get(i));
					}
			    }
				cgNewsListClone = arraylist;
				cgNewsAdapter = new CGNewsAdapter(arraylist, arraylist1);
				cgNewsDiscountListView.setAdapter(cgNewsAdapter);
			}
		}
		switch (view.getId()) {
		case R.id.newsBackButton:
			super.onBackPressed();
			break;
		case R.id.newsUpdateProgress:
			break;
		case R.id.newsUpdateButton:
			FlurryAgent.logEvent("newsRefresh");
			newsUpdateProgressBar.setVisibility(0);
			newsUpdateButton.setVisibility(8);
			cgNewsAsy = new CGNewsAsy();
			cgNewsAsy.execute(new String[] { "update" });
			break;
		case R.id.buttonLinearLayout:
			break;
		case R.id.cg_news_all:
			tag = "all";
			cgNewsListView.setVisibility(0);
			cgNewsDiscountListView.setVisibility(8);
			TCUtil.flurryLog("newsTab", getString(0x7f0800af));
			view.setBackgroundDrawable(getResources().getDrawable(0x7f0200ef));
			((TextView) view).setTextColor(-1);
			dazhe.setTextColor(0xff000000);
			huabao.setTextColor(0xff000000);
			dazhe.setBackgroundColor(0);
			huabao.setBackgroundColor(0);
			setTypes();
			break;
		case R.id.cg_news_huabao:
			tag = "huabao";
			TCUtil.flurryLog("newsTab", getString(0x7f0800b3));
			view.setBackgroundDrawable(getResources().getDrawable(0x7f0200ef));
			((TextView) view).setTextColor(-1);
			newsAndHuaBao.setTextColor(0xff000000);
			dazhe.setTextColor(0xff000000);
			newsAndHuaBao.setBackgroundColor(0);
			dazhe.setBackgroundColor(0);
			Bundle bundle1 = new Bundle();
			bundle1.putBoolean("is_from_news", true);
			startActivity(this, CGGalleryActivity.class, bundle1);
			break;
		case R.id.cg_news_discount:
		case R.id.cg_news_listview:
		case R.id.cg_news_listview_discount:
			break;
		case R.id.topinfoImage:
			if (topInfo != null) {
				String s = (new StringBuilder()).append(cgPath).append("news/bigImage/")
						.append(topInfo.img.split("/")[-1 + topInfo.img.split("/").length]).toString();
				Bundle bundle = new Bundle();
				bundle.putString("small_image_path", s);
				bundle.putString("news_summary", topInfo.summary);
				bundle.putString("news_title", topInfo.title);
				bundle.putString("news_source", topInfo.source);
				bundle.putString("news_url", topInfo.url);
				bundle.putBoolean("is_last_update", flag);
				startActivity(this, CGNewsItemActivity.class, bundle);
			}
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030019);
		cgNewsListView = (ListView) findViewById(0x7f0a0095);
		cgNewsListView.setOnItemClickListener(this);
		cgNewsDiscountListView = (ListView) findViewById(0x7f0a0096);
		cgNewsDiscountListView.setOnItemClickListener(this);
		newsAndHuaBao = (TextView) findViewById(0x7f0a0092);
		newsAndHuaBao.setOnClickListener(this);
		huabao = (TextView) findViewById(0x7f0a0093);
		huabao.setOnClickListener(this);
		dazhe = (TextView) findViewById(0x7f0a0094);
		dazhe.setOnClickListener(this);
		newsBackButton = (ImageView) findViewById(0x7f0a008e);
		newsBackButton.setOnClickListener(this);
		newsUpdateButton = (ImageView) findViewById(0x7f0a0090);
		newsUpdateButton.setOnClickListener(this);
		top_layout = (LinearLayout) LayoutInflater.from(this).inflate(0x7f03001a, null);
		topinfoImageView = (ImageView) top_layout.findViewById(0x7f0a0097);
		topinfoImageView.setOnClickListener(this);
		topTextView = (TextView) top_layout.findViewById(0x7f0a0098);
		titleText = (TextView) findViewById(0x7f0a0001);
		titleText.setText(getString(0x7f0800b0));
		blankView = findViewById(0x7f0a007d);
		typeRelativeLayout = (LinearLayout) findViewById(0x7f0a0091);
		newsUpdateProgressBar = (ProgressBar) findViewById(0x7f0a008f);
		bitmapList = new ArrayList();
		cgPath = (new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina").append("/").append("CG").append("/").append(CG_DATA.CG_ID)
				.append("/").toString();
		cgNewsAsy = new CGNewsAsy();
		cgNewsAsy.execute(new String[] { "all" });
	}

	public void onItemClick(AdapterView adapterview, View view, int i, long l) {
		if ("all".equals(tag) && "hasTopImage".equals(typeFlag))
			i--;
		if (cgNewsListClone != null && cgNewsListClone.size() > 0) {
			String s = (new StringBuilder())
					.append(cgPath)
					.append("news/")
					.append(((CGNewsData) cgNewsListClone.get(i)).thumbnail.split("/")[-1
							+ ((CGNewsData) cgNewsListClone.get(i)).thumbnail.split("/").length]).toString();
			Bundle bundle = new Bundle();
			bundle.putString("small_image_path", s);
			bundle.putString("news_url", ((CGNewsData) cgNewsListClone.get(i)).url);
			bundle.putString("news_summary", ((CGNewsData) cgNewsListClone.get(i)).summary);
			bundle.putString("news_title", ((CGNewsData) cgNewsListClone.get(i)).title);
			bundle.putString("news_source", ((CGNewsData) cgNewsListClone.get(i)).source);
			bundle.putBoolean("is_last_update", flag);
			startActivity(this, CGNewsItemActivity.class, bundle);
		}
	}

	protected void onResume() {
		super.onResume();
		if (typeRelativeLayout.getVisibility() == 0 && "huabao".equals(tag))
			newsAndHuaBao.performClick();
		flag = false;
		if (!(new File((new StringBuilder()).append(cgPath).append("news/news.xml").toString())).exists() && !TCUtil.isNetAvailable(this)) {
			android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
			builder.setTitle(getString(0x7f08007f));
			builder.setMessage(getString(0x7f080080));
			builder.setNegativeButton(getString(0x7f080082), new android.content.DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int i) {
					dialoginterface.dismiss();
					finish();
				}

			});
			builder.setPositiveButton(getString(0x7f080081), new android.content.DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int i) {
					dialoginterface.dismiss();
					TCUtil.startWirelessSetting(CGNewsActivity.this);
				}

			});
			AlertDialog alertdialog = builder.create();
			alertdialog.show();
			alertdialog.setCancelable(false);
		}
	}

	public void perform() {
		if (tag != null)
			if (tag.equals("discount"))
				dazhe.performClick();
			else
				newsAndHuaBao.performClick();
	}

	public void setBitmap(Bitmap bitmap) {
		if (topInfo != null) {
			topinfoImageView.setImageBitmap(bitmap);
			topinfoImageView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(TCData.SCREEN_WIDTH, -70
					+ (int) (((float) bitmap.getHeight() * (float) TCData.SCREEN_WIDTH) / (float) bitmap.getWidth())));
			topinfoImageView.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
			topTextView.setText(topInfo.title);
		}
	}

	public void setListViewHeightBasedOnChildren(ListView listview) {
	}

	public void setTypeId() {
		if (inforList != null && inforList.size() > 0) {
			for (int i = 0; i < inforList.size(); i++)
				if (((com.tc.logic.CGNewsData.InforType) inforList.get(i)).name.equals(getString(0x7f0800b2))) {
					newsId = ((com.tc.logic.CGNewsData.InforType) inforList.get(i)).id;
					dazhe.setId(newsId);
				}

		}
	}

	public void setTypes() {
		ArrayList arraylist = new ArrayList();
		ArrayList arraylist1 = new ArrayList();
		if (cgNewsList != null && cgNewsList.size() > 0) {
			Iterator iterator = cgNewsList.iterator();
			do {
				if (!iterator.hasNext())
					break;
				CGNewsData cgnewsdata = (CGNewsData) iterator.next();
				int i = dazhe.getId();
				if (cgnewsdata.catid != i) {
					arraylist.add(cgnewsdata);
					int j = cgNewsList.indexOf(cgnewsdata);
					if (j < bitmapList.size())
						arraylist1.add(bitmapList.get(j));
				}
			} while (true);
		}
		cgNewsListClone = arraylist;
		cgNewsAdapter = new CGNewsAdapter(arraylist, arraylist1);
		cgNewsListView.setAdapter(cgNewsAdapter);
	}

	private static boolean flag = false;
	private static ArrayList inforList;
	private Bitmap bit;
	private ArrayList bitmapList;
	private View blankView;
	private CGNewsAdapter cgNewsAdapter;
	private CGNewsAsy cgNewsAsy;
	private ListView cgNewsDiscountListView;
	private ArrayList cgNewsList;
	private ArrayList cgNewsListClone;
	private ListView cgNewsListView;
	private String cgPath;
	private TextView dazhe;
	private TextView huabao;
	private boolean isHasTop;
	private TextView newsAndHuaBao;
	private ImageView newsBackButton;
	private ArrayList newsData;
	private int newsId;
	private ImageView newsUpdateButton;
	private ProgressBar newsUpdateProgressBar;
	private String tag;
	private TextView titleText;
	private Bitmap topBitmap;
	private InputStream topImageInputStream;
	private com.tc.logic.CGNewsData.TopInfo topInfo;
	private TextView topTextView;
	private LinearLayout top_layout;
	private ImageView topinfoImageView;
	private String typeFlag;
	private LinearLayout typeRelativeLayout;
	private int updateTime;
	private static final String TAG = CGNewsActivity.class.getName();

	/*
	 * static ArrayList access$102(CGNewsActivity cgnewsactivity, ArrayList
	 * arraylist) { cgnewsactivity.cgNewsList = arraylist; return arraylist; }
	 */

	/*
	 * static boolean access$1202(boolean flag1) { flag = flag1; return flag1; }
	 */

	/*
	 * static CGNewsAdapter access$1602(CGNewsActivity cgnewsactivity,
	 * CGNewsAdapter cgnewsadapter) { cgnewsactivity.cgNewsAdapter =
	 * cgnewsadapter; return cgnewsadapter; }
	 */

	/*
	 * static com.tc.logic.CGNewsData.TopInfo access$202(CGNewsActivity
	 * cgnewsactivity, com.tc.logic.CGNewsData.TopInfo topinfo) {
	 * cgnewsactivity.topInfo = topinfo; return topinfo; }
	 */

	/*
	 * static String access$2202(CGNewsActivity cgnewsactivity, String s) {
	 * cgnewsactivity.typeFlag = s; return s; }
	 */

	/*
	 * static boolean access$302(CGNewsActivity cgnewsactivity, boolean flag1) {
	 * cgnewsactivity.isHasTop = flag1; return flag1; }
	 */

	/*
	 * static ArrayList access$402(CGNewsActivity cgnewsactivity, ArrayList
	 * arraylist) { cgnewsactivity.cgNewsListClone = arraylist; return
	 * arraylist; }
	 */

	/*
	 * static ArrayList access$502(ArrayList arraylist) { inforList = arraylist;
	 * return arraylist; }
	 */

	/*
	 * static Bitmap access$602(CGNewsActivity cgnewsactivity, Bitmap bitmap) {
	 * cgnewsactivity.topBitmap = bitmap; return bitmap; }
	 */

	/*
	 * static InputStream access$702(CGNewsActivity cgnewsactivity, InputStream
	 * inputstream) { cgnewsactivity.topImageInputStream = inputstream; return
	 * inputstream; }
	 */

	/*
	 * static Bitmap access$802(CGNewsActivity cgnewsactivity, Bitmap bitmap) {
	 * cgnewsactivity.bit = bitmap; return bitmap; }
	 */

	/*
	 * static int access$902(CGNewsActivity cgnewsactivity, int i) {
	 * cgnewsactivity.updateTime = i; return i; }
	 */
}
