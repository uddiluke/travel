// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.cg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tc.TCData;
import com.tc.TCUtil;
import com.tc.logic.CGMoreAppData;
import com.tc.weibo.WeiboActivity;
import com.touchchina.cityguide.sz.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData

public class CGMoreAppItemActivity extends CGBaseActivity implements android.view.View.OnClickListener, android.view.View.OnTouchListener {
	private class ImageAdapter extends BaseAdapter {

		public int getCount() {
			int i;
			if (screenShotsList != null)
				i = screenShotsList.size();
			else
				i = 2;
			return i;
		}

		public Object getItem(int i) {
			return Integer.valueOf(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			ImageView imageview;
			if (view == null)
				imageview = new ImageView(context);
			else
				imageview = (ImageView) view;
			imageview.setLayoutParams(new android.widget.Gallery.LayoutParams(200, 320));
			if (screenShotsList != null && screenShotsList.size() > 0 && i < galleryBitmapList.size() && galleryBitmapList.get(i) != null)
				imageview.setImageBitmap((Bitmap) galleryBitmapList.get(i));
			return imageview;
		}

		private Context context;
		private boolean flag;
		private ArrayList galleryBitmapList;

		public ImageAdapter(Context context1, ArrayList arraylist) {

			flag = false;
			context = context1;
			galleryBitmapList = arraylist;
		}
	}

	private class MoreAppItemAsy extends AsyncTask {

		protected Object doInBackground(Object aobj[]) {
			return doInBackground((String[]) aobj);
		}

		protected String doInBackground(String as[]) {
			cgMoreAppData = CGMoreAppData.getMoreAppItemData(getIntent().getStringExtra("appID"));
			java.io.InputStream inputstream = CGMoreAppData.getSmallImage(getIntent().getStringExtra("appIcon"));
			String as1[] = getIntent().getStringExtra("appIcon").split("/");
			iconFileName = (new StringBuilder()).append(cgPath).append("more/screenShots/")
					.append(getIntent().getStringExtra("appIcon").split("/")[-1 + as1.length]).toString();
			if (inputstream != null) {
				TCUtil.saveNewFile(inputstream, iconFileName);
				iconFile = true;
			}
			iconBitmap = BitmapFactory.decodeFile(iconFileName);
			screenShotsList = CGMoreAppData.getScreenShots();
			publishProgress(new String[] { "undone" });
			if (isGalleryTrue()) {
				int i = 0;
				while (i < screenShotsList.size()) {
					if (i == 1) {
						firstGalleryPath = (new StringBuilder()).append(cgPath).append("more/screenShots/")
								.append(((String) screenShotsList.get(i)).split("/")[-1 + ((String) screenShotsList.get(i)).split("/").length])
								.toString();
						java.io.InputStream inputstream1 = CGMoreAppData.getSmallImage((String) screenShotsList.get(i));
						if (inputstream1 != null) {
							TCUtil.saveNewFile(inputstream1, firstGalleryPath);
							galleryFile = true;
						}
						galleryBitmap = BitmapFactory.decodeFile(firstGalleryPath);
						galleryBitmapList.add(galleryBitmap);
						publishProgress(new String[] { "gallery" });
					} else {
						galleryBitmap = BitmapFactory.decodeStream(CGMoreAppData.getSmallImage((String) screenShotsList.get(i)));
						galleryBitmapList.add(galleryBitmap);
					}
					publishProgress(new String[] { "done" });
					i++;
				}
			}
			return null;
		}

		protected void onPostExecute(Object obj) {
			onPostExecute((String) obj);
		}

		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			alertDialog.dismiss();
		}

		protected void onPreExecute() {
			super.onPreExecute();
			alertDialog = new ProgressDialog(CGMoreAppItemActivity.this);
			alertDialog.setProgressStyle(0);
			alertDialog.setMessage(getString(0x7f0800c4));
			alertDialog.show();
			alertDialog.setCancelable(true);
			ImageAdapter imageadapter = new ImageAdapter(CGMoreAppItemActivity.this, galleryBitmapList);
			albumGallery.setAdapter(imageadapter);
		}

		protected void onProgressUpdate(Object aobj[]) {
			onProgressUpdate((String[]) aobj);
		}

		protected void onProgressUpdate(String as[]) {
			super.onProgressUpdate(as);
			if ("undone".equals(as[0]) && isTrue()) {
				iconButton.setImageBitmap(iconBitmap);
				contentText.setText(((CGMoreAppData) cgMoreAppData.get(0)).desc);
				if (screenShotsList != null && screenShotsList.size() > 0) {
					for (int j = 0; j < screenShotsList.size(); j++) {
						ImageView imageview = new ImageView(CGMoreAppItemActivity.this);
						imageview.setPadding(5, 5, 5, 5);
						list.add(imageview);
						indicatorsLinearLayout.addView(imageview);
					}

				}
				albumGallery.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView adapterview, View view, int i, long l) {
						if (screenShotsList != null && screenShotsList.size() > 0) {
							int j = 0;
							while (j < screenShotsList.size()) {
								if (j == i)
									((ImageView) list.get(j)).setImageResource(0x7f020012);
								else
									((ImageView) list.get(j)).setImageResource(0x7f020011);
								j++;
							}
						}
					}

					public void onNothingSelected(AdapterView adapterview) {
					}

				});
			}
			if ("done".equals(as[0])) {
				int i = albumGallery.getSelectedItemPosition();
				ImageAdapter imageadapter1 = new ImageAdapter(CGMoreAppItemActivity.this, galleryBitmapList);
				albumGallery.setAdapter(imageadapter1);
				albumGallery.setSelection(i);
			}
			if ("gallery".equals(as[0])) {
				alertDialog.dismiss();
				ImageAdapter imageadapter = new ImageAdapter(CGMoreAppItemActivity.this, galleryBitmapList);
				albumGallery.setAdapter(imageadapter);
			}
		}

	}

	public CGMoreAppItemActivity() {
		list = new ArrayList();
	}

	public boolean isGalleryTrue() {
		boolean flag;
		if (screenShotsList != null && screenShotsList.size() > 0)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public boolean isTrue() {
		boolean flag;
		if (cgMoreAppData != null && cgMoreAppData.size() > 0)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backButton:
			onBackPressed();
			break;

		case R.id.moredownloadButton:
			if (isTrue() && ((CGMoreAppData) cgMoreAppData.get(0)).url.startsWith("http"))
				startActivity(new Intent("android.intent.action.VIEW", Uri.parse(((CGMoreAppData) cgMoreAppData.get(0)).url)));
			break;
		case R.id.item_share:
			final Dialog dialogShare = new Dialog(this, 0x7f090003);
			android.view.WindowManager.LayoutParams layoutparams = dialogShare.getWindow().getAttributes();
			layoutparams.x = 0;
			layoutparams.y = 800;
			layoutparams.width = TCData.SCREEN_WIDTH;
			layoutparams.height = TCData.SCREEN_HEIGHT;
			dialogShare.getWindow().setAttributes(layoutparams);
			dialogShare.onWindowAttributesChanged(layoutparams);
			View view1 = TCUtil.getLayoutInflater(this).inflate(0x7f030052, null);
			TextView textview = (TextView) view1.findViewById(0x7f0a01d9);
			TextView textview1 = (TextView) view1.findViewById(0x7f0a01da);
			TextView textview2 = (TextView) view1.findViewById(0x7f0a01db);
			dialogShare.setContentView(view1, new android.widget.LinearLayout.LayoutParams(TCData.SCREEN_WIDTH, -2));
			dialogShare.show();
			textview.setOnClickListener(new android.view.View.OnClickListener() {

				public void onClick(View view2) {
					String s = null;
					if (isTrue())
						s = ((CGMoreAppData) cgMoreAppData.get(0)).url;
					Bundle bundle = new Bundle();
					bundle.putParcelable("icon_bitmap", iconBitmap);
					bundle.putBoolean("has_camera", false);
					bundle.putString(
							"status",
							(new StringBuilder()).append(getString(0x7f0800b9)).append("\"").append(nameFlag).append("\"")
									.append(getString(0x7f0800ba)).append("\n").append(s).append(" ").append(getString(0x7f080038)).toString());
					startActivity(CGMoreAppItemActivity.this, WeiboActivity.class, bundle);
					dialogShare.dismiss();
				}

			});
			textview1.setOnClickListener(new android.view.View.OnClickListener() {

				public void onClick(View view2) {
					String s = null;
					String s1 = null;
					if (isTrue()) {
						s = ((CGMoreAppData) cgMoreAppData.get(0)).url;
						s1 = ((CGMoreAppData) cgMoreAppData.get(0)).desc;
					}
					Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
					String s2 = (new StringBuilder()).append("<html><body><h1><font face='verdana' size='5'>").append(getString(0x7f0800b9))
							.append("\"").append(nameFlag).append("\"").append(getString(0x7f0800ba)).append("</font></h1></body></html>").toString();
					ArrayList arraylist = new ArrayList();
					if (iconFile)
						arraylist.add(Uri.fromFile(new File(iconFileName)));
					if (galleryFile)
						arraylist.add(Uri.fromFile(new File(firstGalleryPath)));
					intent.putExtra("android.intent.extra.TEXT", (new StringBuilder()).append(Html.fromHtml(s2)).append(s).append("\n").append("\n")
							.append(s1).toString());
					intent.putExtra("android.intent.extra.SUBJECT", nameFlag);
					intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arraylist);
					intent.setType("image/*");
					startActivity(Intent.createChooser(intent, getString(0x7f0800d8)));
					dialogShare.dismiss();
				}

			});
			textview2.setOnClickListener(new android.view.View.OnClickListener() {

				public void onClick(View view2) {
					dialogShare.dismiss();
				}

			});
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.cg_more_app_item);
		cgPath = (new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina").append("/").append("CG").append("/").append(CG_DATA.CG_ID)
				.append("/").toString();
		if (!(new File((new StringBuilder()).append(cgPath).append("more/screenShots").toString())).exists())
			(new File((new StringBuilder()).append(cgPath).append("more/screenShots").toString())).mkdirs();
		galleryBitmapList = new ArrayList();
		indicatorsLinearLayout = (LinearLayout) findViewById(0x7f0a0084);
		nameFlag = getIntent().getStringExtra("appName");
		titleText = (TextView) findViewById(0x7f0a0001);
		titleText.setText(nameFlag);
		findViewById(0x7f0a0000).setOnClickListener(this);
		nameText = (TextView) findViewById(0x7f0a0080);
		nameText.setText(nameFlag);
		iconButton = (ImageView) findViewById(0x7f0a007f);
		downloadButton = (ImageView) findViewById(0x7f0a0081);
		itemShare = (ImageView) findViewById(0x7f0a007e);
		itemShare.setOnClickListener(this);
		downloadButton.setOnClickListener(this);
		downloadButton.setOnTouchListener(this);
		contentText = (TextView) findViewById(0x7f0a0082);
		albumGallery = (Gallery) findViewById(0x7f0a0083);
		(new MoreAppItemAsy()).execute(new String[] { "" });
	}

	protected void onDestroy() {
		super.onDestroy();
		if (isGalleryTrue()) {
			TCUtil.recycleBitmap(iconBitmap);
			for (int j = 0; j < galleryBitmapList.size(); j++)
				TCUtil.recycleBitmap((Bitmap) galleryBitmapList.get(j));

		}
		File afile[] = (new File((new StringBuilder()).append(cgPath).append("more/screenShots/").toString())).listFiles();
		for (int i = 0; i < afile.length; i++)
			TCUtil.deleteEveryFile((new StringBuilder()).append(cgPath).append("more/screenShots/").append(afile[i].getName()).toString());

	}

	protected void onResume() {
		super.onResume();
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
					TCUtil.startWirelessSetting(CGMoreAppItemActivity.this);
				}

			});
			AlertDialog alertdialog = builder.create();
			alertdialog.show();
			alertdialog.setCancelable(false);
		}
	}

	public boolean onTouch(View view, MotionEvent motionevent) {
		if (view.getId() == 0x7f0a00f4) {
			if (motionevent.getAction() == 0) {
				downloadButton.setAlpha(70);
			} else if (motionevent.getAction() == 1)
				downloadButton.setAlpha(255);
		}
		return false;
	}

	public static final String APP_ICON = "appIcon";
	public static final String APP_ID = "appID";
	public static final String APP_NAME = "appName";
	private Gallery albumGallery;
	private ProgressDialog alertDialog;
	private ArrayList cgMoreAppData;
	private String cgPath;
	private TextView contentText;
	private ImageView downloadButton;
	private String firstGalleryPath;
	private Bitmap galleryBitmap;
	private ArrayList galleryBitmapList;
	private boolean galleryFile;
	private Bitmap iconBitmap;
	private ImageView iconButton;
	private boolean iconFile;
	private String iconFileName;
	private LinearLayout indicatorsLinearLayout;
	private ImageView itemShare;
	private List list;
	private String nameFlag;
	private TextView nameText;
	private ArrayList screenShotsList;
	private TextView titleText;

	/*
	 * static ArrayList access$102(CGMoreAppItemActivity cgmoreappitemactivity,
	 * ArrayList arraylist) { cgmoreappitemactivity.cgMoreAppData = arraylist;
	 * return arraylist; }
	 */

	/*
	 * static ProgressDialog access$1102(CGMoreAppItemActivity
	 * cgmoreappitemactivity, ProgressDialog progressdialog) {
	 * cgmoreappitemactivity.alertDialog = progressdialog; return
	 * progressdialog; }
	 */

	/*
	 * static String access$202(CGMoreAppItemActivity cgmoreappitemactivity,
	 * String s) { cgmoreappitemactivity.iconFileName = s; return s; }
	 */

	/*
	 * static boolean access$402(CGMoreAppItemActivity cgmoreappitemactivity,
	 * boolean flag) { cgmoreappitemactivity.iconFile = flag; return flag; }
	 */

	/*
	 * static Bitmap access$502(CGMoreAppItemActivity cgmoreappitemactivity,
	 * Bitmap bitmap) { cgmoreappitemactivity.iconBitmap = bitmap; return
	 * bitmap; }
	 */

	/*
	 * static ArrayList access$602(CGMoreAppItemActivity cgmoreappitemactivity,
	 * ArrayList arraylist) { cgmoreappitemactivity.screenShotsList = arraylist;
	 * return arraylist; }
	 */

	/*
	 * static String access$702(CGMoreAppItemActivity cgmoreappitemactivity,
	 * String s) { cgmoreappitemactivity.firstGalleryPath = s; return s; }
	 */

	/*
	 * static boolean access$802(CGMoreAppItemActivity cgmoreappitemactivity,
	 * boolean flag) { cgmoreappitemactivity.galleryFile = flag; return flag; }
	 */

	/*
	 * static Bitmap access$902(CGMoreAppItemActivity cgmoreappitemactivity,
	 * Bitmap bitmap) { cgmoreappitemactivity.galleryBitmap = bitmap; return
	 * bitmap; }
	 */
}
