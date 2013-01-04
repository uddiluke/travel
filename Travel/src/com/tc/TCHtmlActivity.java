// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

// Referenced classes of package com.tc:
//            TCUtil

public class TCHtmlActivity extends Activity {

	public TCHtmlActivity() {
	}

	public void init() {
		contentWebView = (WebView) findViewById(0x7f0a0260);
		WebSettings websettings = contentWebView.getSettings();
		websettings.setAllowFileAccess(true);
		websettings.setJavaScriptEnabled(true);
		websettings.setSupportZoom(false);
		websettings.setBuiltInZoomControls(true);
		contentWebView.setScrollBarStyle(0);
		contentWebView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView webview, String s) {
				loadurl(webview, s);
				return true;
			}

		});
		contentWebView.setWebChromeClient(new WebChromeClient() {

			public boolean onJsAlert(WebView webview, String s, String s1, JsResult jsresult) {
				return true;
			}

			public boolean onJsBeforeUnload(WebView webview, String s, String s1, JsResult jsresult) {
				return super.onJsBeforeUnload(webview, s, s1, jsresult);
			}

			public boolean onJsConfirm(WebView webview, String s, String s1, JsResult jsresult) {
				return true;
			}

			public boolean onJsPrompt(WebView webview, String s, String s1, String s2, JsPromptResult jspromptresult) {
				return true;
			}

			public void onProgressChanged(WebView webview, int i) {
				setProgress(i * 100);
				super.onProgressChanged(webview, i);
			}

			public void onReceivedTitle(WebView webview, String s) {
				setTitle(s);
				super.onReceivedTitle(webview, s);
			}

		});
	}

	public void loadurl(final WebView view, String s) {
		loadurl = s;
		(new Thread() {

			public void run() {
				TCUtil.loadUrl(TCHtmlActivity.this, view, loadurl);
			}

		}).start();
	}

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030070);
		getWindow().setFeatureInt(2, -1);
		backButton = (ImageView) findViewById(0x7f0a0000);
		backButton.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				onBackPressed();
			}

		});
		Bundle bundle1 = getIntent().getExtras();
		String s = bundle1.getString("url");
		textView = (TextView) findViewById(0x7f0a0001);
		textView.setText(bundle1.getString("title"));
		init();
		loadurl(contentWebView, s);
	}

	public static final String KEY_TITLE = "title";
	public static final String KEY_URL = "url";
	private static final String TAG = TCHtmlActivity.class.getSimpleName();
	ImageView backButton;
	private WebView contentWebView;
	private String loadurl;
	TextView textView;

}
