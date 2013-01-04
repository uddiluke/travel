// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.community.TencentWeibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

// Referenced classes of package com.tc.community.TencentWeibo:
//            OAuth, OAuthClient

public class TencentTokenActivity extends Activity {

	public TencentTokenActivity() {
	}

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030076);
		webView = (WebView) findViewById(0x7f0a026f);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView webview, String s1) {
				boolean flag = false;
				if (s1.startsWith("http://")) {
					webview.loadUrl(s1);
					flag = true;
				} else if (s1.startsWith("qweibo4android://OAuthActivity") || s1.startsWith("QWeibo4android://OAuthActivity")) {
					String as[] = s1.substring("qweibo4android://OAuthActivity".length() + "?".length()).split("&");
					if (as != null) {
						if (as.length >= 2) {
							String s2 = as[1].split("=")[1];
							oAuth.setOauth_verifier(s2);
							try {
								oAuth = oAuthClient.accessToken(oAuth);
							} catch (Exception e) {
								finish();
								return flag;
							}
							Intent intent = new Intent();
							intent.putExtra("TENCENT_TOKEN_INFO", oAuth);
							setResult(0, intent);
							finish();
						} else {
							finish();
						}
						flag = true;
					}
				}
				return flag;

			}

		});
		oAuth = new OAuth("QWeibo4android://OAuthActivity");
		oAuthClient = new OAuthClient();
		try {
			oAuth = oAuthClient.requestToken(oAuth);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String s = (new StringBuilder()).append("http://open.t.qq.com/cgi-bin/authorize?oauth_token=").append(oAuth.getOauth_token()).toString();
		webView.loadUrl(s);

		return;

	}

	public static final String TENCENT_TOKEN_INFO = "TENCENT_TOKEN_INFO";
	public OAuth oAuth;
	public OAuthClient oAuthClient;
	private WebView webView;
}
