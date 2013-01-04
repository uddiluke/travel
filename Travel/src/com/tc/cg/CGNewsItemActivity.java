// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.cg;

import java.io.File;
import java.io.InputStream;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.tc.TCData;
import com.tc.TCUtil;
import com.tc.logic.CGNewsData;
import com.tc.weibo.WeiboActivity;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData

public class CGNewsItemActivity extends CGBaseActivity implements android.view.View.OnClickListener {
	private class CGNewsItemAsy extends AsyncTask {

		protected Object doInBackground(Object aobj[]) {
			return doInBackground((String[]) aobj);
		}

		protected String doInBackground(String as[]) {
			String s = CGNewsData.getHtmlPath(as[0], CG_DATA.CG_ID);
			String s1;
			if ((new File(s)).exists() && !flag)
				s1 = s;
			else
				s1 = CGNewsData.getNewsZip(as[0], CG_DATA.CG_ID);
			return s1;
		}

		protected void onPostExecute(Object obj) {
			onPostExecute((String) obj);
		}

		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			itemRelativeLayout.setVisibility(8);
			if (TCUtil.isNetAvailable(CGNewsItemActivity.this) && (new File(s)).exists())
				newsWebView.loadUrl((new StringBuilder()).append("file:////").append(s).toString());
			if (!TCUtil.isNetAvailable(CGNewsItemActivity.this) && (new File(s)).exists())
				newsWebView.loadUrl((new StringBuilder()).append("file:////").append(s).toString());
			if (!TCUtil.isNetAvailable(CGNewsItemActivity.this) && !(new File(s)).exists()) {
				android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CGNewsItemActivity.this);
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
						TCUtil.startWirelessSetting(CGNewsItemActivity.this);
					}

				});
				AlertDialog alertdialog = builder.create();
				alertdialog.show();
				alertdialog.setCancelable(false);
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			newsItemProgressBar.setVisibility(0);
		}

	}

	public CGNewsItemActivity() {
		dialogId = 10000;
	}

	public void onClick(View view) {
		if (view.getId() == 0x7f0a0000) // goto _L2; else goto _L1
			super.onBackPressed();
		else if (view.getId() == 0x7f0a0099)
			showDialog(dialogId);

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f03001b);
		newsItemProgressBar = (ProgressBar) findViewById(0x7f0a009b);
		newsWebView = (WebView) findViewById(0x7f0a009c);
		newsWebView.setScrollBarStyle(0x2000000);
		newsShareImageView = (ImageView) findViewById(0x7f0a0099);
		newsShareImageView.setOnClickListener(this);
		newsWebView.getSettings().setJavaScriptEnabled(true);
		newsWebView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView webview, final String url) {
				if (url.endsWith("gif") || url.endsWith("jpg") || url.endsWith("png")) {
					final Dialog builder = new Dialog(CGNewsItemActivity.this, 0x7f09000c);
					final View dialogView = TCUtil.getLayoutInflater(CGNewsItemActivity.this).inflate(0x7f030053, null);
					final ProgressBar progressbar = (ProgressBar) dialogView.findViewById(0x7f0a015a);
					final TextView textview = (TextView) dialogView.findViewById(0x7f0a015b);
					final ImageView dialogImage = (ImageView) dialogView.findViewById(0x7f0a015c);
					final GifView gifView = (GifView) dialogView.findViewById(0x7f0a015d);
					android.view.WindowManager.LayoutParams layoutparams = builder.getWindow().getAttributes();
					layoutparams.width = TCData.SCREEN_WIDTH;
					layoutparams.height = TCData.SCREEN_HEIGHT;
					builder.getWindow().setAttributes(layoutparams);
					builder.onWindowAttributesChanged(layoutparams);
					builder.setCanceledOnTouchOutside(true);
					builder.setContentView(dialogView, new android.widget.RelativeLayout.LayoutParams(-20 + TCData.SCREEN_WIDTH, 400));
					new AsyncTask() {

						protected Object doInBackground(Object aobj[]) {
							return doInBackground((String[]) aobj);
						}

						protected String doInBackground(String as[]) {
							String s = (new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina/CG/").append(CG_ID).append("/")
									.append("news/bigImage/").append(url.split("/")[-1 + url.split("/").length]).toString();
							if ((new File(s)).exists()) {
								bit = BitmapFactory.decodeFile(s);
							} else {
								inputStream = CGNewsData.getImageBitmap(url.substring(6));
								TCUtil.saveNewFile(inputStream, s);
								bit = BitmapFactory.decodeFile(s);
							}
							return null;
						}

						protected void onPostExecute(Object obj) {
							onPostExecute((String) obj);
						}

						protected void onPostExecute(String s) {
							if (bit != null) {
								if (url.endsWith("gif")) {
									progressbar.setVisibility(8);
									gifView.setVisibility(0);
									gifView.setGifImage(inputStream);
									builder.setContentView(dialogView, new android.widget.RelativeLayout.LayoutParams(320, 175));
								} else {
									progressbar.setVisibility(8);
									dialogImage.setImageBitmap(bit);
									dialogImage.setVisibility(0);
									int i = (int) (((float) bit.getHeight() / (float) bit.getWidth()) * ((float) TCData.SCREEN_WIDTH - 20F));
									builder.setContentView(dialogView, new android.widget.RelativeLayout.LayoutParams(-20 + TCData.SCREEN_WIDTH, i));
								}
							} else {
								progressbar.setVisibility(8);
								textview.setText(getString(0x7f0800b5));
								textview.setVisibility(0);
								builder.setContentView(dialogView, new android.widget.RelativeLayout.LayoutParams(-20 + TCData.SCREEN_WIDTH, 400));
							}
							dialogView.setOnClickListener(new android.view.View.OnClickListener() {

								public void onClick(View view) {
									builder.cancel();
									bit = null;
								}

							});
						}

					}.execute(new String[] { "" });
					builder.show();
				}
				return true;
			}

			Bitmap bit;
			InputStream inputStream;

		});
		Bundle bundle1 = getIntent().getExtras();
		flag = bundle1.getBoolean("is_last_update");
		titleText = (TextView) findViewById(0x7f0a0001);
		titleText.setText(getString(0x7f0800b0));
		backImageButton = (ImageView) findViewById(0x7f0a0000);
		backImageButton.setOnClickListener(this);
		itemRelativeLayout = (RelativeLayout) findViewById(0x7f0a009a);
		cgNewsItemAsy = new CGNewsItemAsy();
		CGNewsItemAsy cgnewsitemasy = cgNewsItemAsy;
		String as[] = new String[1];
		as[0] = bundle1.getString("news_url");
		cgnewsitemasy.execute(as);
	}

	protected Dialog onCreateDialog(int i) {
		final Dialog dialogShare = new Dialog(this, 0x7f090003);
		android.view.WindowManager.LayoutParams layoutparams = dialogShare.getWindow().getAttributes();
		layoutparams.x = 0;
		layoutparams.y = 800;
		layoutparams.width = TCData.SCREEN_WIDTH;
		layoutparams.height = TCData.SCREEN_HEIGHT;
		dialogShare.getWindow().setAttributes(layoutparams);
		dialogShare.onWindowAttributesChanged(layoutparams);
		View view = TCUtil.getLayoutInflater(this).inflate(0x7f030052, null);
		TextView textview = (TextView) view.findViewById(0x7f0a01d9);
		TextView textview1 = (TextView) view.findViewById(0x7f0a01da);
		TextView textview2 = (TextView) view.findViewById(0x7f0a01db);
		dialogShare.setContentView(view, new android.widget.LinearLayout.LayoutParams(TCData.SCREEN_WIDTH, -2));
		dialogShare.show();
		textview.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view1) {
				Bundle bundle = new Bundle();
				bundle.putBoolean("has_camera", false);
				bundle.putString(
						"status",
						(new StringBuilder()).append(getString(0x7f080037)).append("\u3010").append(getIntent().getExtras().getString("news_title"))
								.append("\u3011").append(getIntent().getExtras().getString("news_summary")).append("\n")
								.append(getIntent().getExtras().getString("news_source")).toString());
				bundle.putString("path_camera", getIntent().getExtras().getString("small_image_path"));
				startActivity(CGNewsItemActivity.this, WeiboActivity.class, bundle);
				dialogShare.dismiss();
			}

		});
		textview1.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view1) {
				Intent intent = new Intent("android.intent.action.SEND");
				intent.putExtra(
						"android.intent.extra.TEXT",
						(new StringBuilder()).append(getIntent().getExtras().getString("news_title")).append(" \uFF1A ")
								.append(getIntent().getExtras().getString("news_summary")).append("\n")
								.append(getIntent().getExtras().getString("news_source")).toString());
				intent.putExtra("android.intent.extra.SUBJECT",
						(new StringBuilder()).append(getString(0x7f080037)).append("\u3010").append(getIntent().getExtras().getString("news_title"))
								.append("\u3011").toString());
				intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(getIntent().getExtras().getString("small_image_path"))));
				intent.setType("image/*");
				startActivity(Intent.createChooser(intent, getString(0x7f0800d8)));
				dialogShare.dismiss();
			}

		});
		textview2.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view1) {
				dialogShare.dismiss();
			}

		});
		return dialogShare;
	}

	public boolean onKeyDown(int i, KeyEvent keyevent) {
		backImageButton.setVisibility(0);
		boolean flag1;
		if (i == 4 && newsWebView.canGoBack()) {
			newsWebView.goBack();
			flag1 = true;
		} else {
			flag1 = super.onKeyDown(i, keyevent);
		}
		return flag1;
	}

	public boolean onTouchEvent(MotionEvent motionevent) {
		Log.i("onTouchEvent", "onTouchEvent");
		return super.onTouchEvent(motionevent);
	}

	public static final String CG_NEWS_SMALL_IMAGE_PATH = "small_image_path";
	public static final String CG_NEWS_SOURCE = "news_source";
	public static final String CG_NEWS_SUMMARY = "news_summary";
	public static final String CG_NEWS_TITLE = "news_title";
	public static final String CG_NEWS_URL = "news_url";
	public static final String CG_NEWS_isLastUpdate = "is_last_update";
	private ImageView backImageButton;
	private CGNewsItemAsy cgNewsItemAsy;
	private int dialogId;
	private boolean flag;
	private RelativeLayout itemRelativeLayout;
	private ProgressBar newsItemProgressBar;
	private ImageView newsShareImageView;
	private WebView newsWebView;
	private TextView titleText;

}
