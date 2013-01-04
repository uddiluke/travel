// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.community;

import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tc.TCUtil;
import com.tc.cg.CGBaseActivity;
import com.tc.cg.CGData;
import com.tc.weibo.WeiboSettings;

// Referenced classes of package com.tc.community:
//            CommunityUtil, CommunityJSON

public class SinaBindActivity extends CGBaseActivity {

	public SinaBindActivity() {
	}

	public void onBackPressed() {
		super.onBackPressed();
		TCUtil.hideSoftKeyBroad(this, sinaAccount);
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f03006e);
		setTitle(0x7f080162);
		sinaAccount = (EditText) findViewById(0x7f0a025a);
		sinaPsw = (EditText) findViewById(0x7f0a025b);
		cancel = (Button) findViewById(0x7f0a025c);
		cancel.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				onBackPressed();
			}

		});
		ok = (Button) findViewById(0x7f0a025d);
		ok.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				sinaAccount_tostring = sinaAccount.getText().toString().trim();
				sinaPsw_tostring = sinaPsw.getText().toString().trim();
				if ("".equals(sinaAccount_tostring) || "".equals(sinaPsw_tostring))
					CommunityUtil.showCommunityToast(SinaBindActivity.this, "4");
				else
					(new AsyncTask() {

						protected Object doInBackground(Object aobj[]) {

							try {
								accessToken = (new Weibo()).getXAuthAccessToken(sinaAccount_tostring, sinaPsw_tostring);
								if (accessToken != null) {

									status = CommunityJSON.bind(SinaBindActivity.this, CGData.CG_COMMUNITY_CODE, "sina", (new StringBuilder())
											.append("").append(accessToken.getUserId()).toString(), accessToken.getToken(),
											accessToken.getTokenSecret(), false);
								}
							} catch (WeiboException weiboexception) {
								status = "100";
								weiboexception.printStackTrace();
							}
							return null;
						}

						protected void onPostExecute(Object obj) {

							progressDialog.dismiss();
							if ("OK".equals(status)) {
								WeiboSettings.putUserName(SinaBindActivity.this, accessToken.getScreenName());
								WeiboSettings.putPassword(SinaBindActivity.this, sinaPsw_tostring);
								WeiboSettings.putToken(SinaBindActivity.this, accessToken.getToken());
								WeiboSettings.putTokenSecret(SinaBindActivity.this, accessToken.getTokenSecret());
								onBackPressed();
								CommunityUtil.showCommunityToast(SinaBindActivity.this, "16");
							} else if ("109".equals(status))
								(new android.app.AlertDialog.Builder(SinaBindActivity.this)).setTitle(0x7f08019e).setMessage(0x7f08019f)
										.setNegativeButton(0x7f08001d, null)
										.setPositiveButton(0x7f08001e, new android.content.DialogInterface.OnClickListener() {

											public void onClick(DialogInterface dialoginterface, int i) {
												(new AsyncTask() {

													protected Object doInBackground(Object aobj[]) {

														status1 = CommunityJSON.bind(SinaBindActivity.this, CGData.CG_COMMUNITY_CODE, "sina",
																(new StringBuilder()).append("").append(accessToken.getUserId()).toString(),
																accessToken.getToken(), accessToken.getTokenSecret(), true);
														return null;
													}

													protected void onPostExecute(Object obj) {

														progressDialog.dismiss();
														if ("OK".equals(status1)) {
															onBackPressed();
															CommunityUtil.showCommunityToast(SinaBindActivity.this, "16");
														} else {
															CommunityUtil.showCommunityToast(SinaBindActivity.this, status1);
														}
													}

													protected void onPreExecute() {
														progressDialog.show();
													}

													String status1;

												}).execute(null);
											}

										}).show();
							else if ("124".equals(status))
								(new android.app.AlertDialog.Builder(SinaBindActivity.this)).setTitle(0x7f0801ad).setMessage(0x7f0801ae)
										.setNegativeButton(0x7f08001d, null)
										.setPositiveButton(0x7f08001e, new android.content.DialogInterface.OnClickListener() {

											public void onClick(DialogInterface dialoginterface, int i) {
												(new AsyncTask() {

													protected Object doInBackground(Object aobj[]) {

														status1 = CommunityJSON.bind(SinaBindActivity.this, CGData.CG_COMMUNITY_CODE, "sina",
																(new StringBuilder()).append("").append(accessToken.getUserId()).toString(),
																accessToken.getToken(), accessToken.getTokenSecret(), true);
														return null;
													}

													protected void onPostExecute(Object obj) {

														progressDialog.dismiss();
														if ("OK".equals(status1)) {
															onBackPressed();
															CommunityUtil.showCommunityToast(SinaBindActivity.this, "16");
														} else {
															CommunityUtil.showCommunityToast(SinaBindActivity.this, status1);
														}
													}

													protected void onPreExecute() {
														progressDialog.show();
													}

													String status1;

												}).execute(null);
											}

										}).show();
							else
								CommunityUtil.showCommunityToast(SinaBindActivity.this, status);
						}

						protected void onPreExecute() {
							progressDialog.setCancelable(false);
							progressDialog.setMessage(getString(0x7f08008f));
							progressDialog.show();
						}

						ProgressDialog progressDialog;
						String status;

						{

							progressDialog = new ProgressDialog(SinaBindActivity.this);
						}
					}).execute(null);
			}

		});
	}

	AccessToken accessToken;
	Button cancel;
	Button ok;
	EditText sinaAccount;
	String sinaAccount_tostring;
	EditText sinaPsw;
	String sinaPsw_tostring;

	static {
		Weibo.CONSUMER_KEY = "3780182769";
		Weibo.CONSUMER_SECRET = "832c9b655611271fa6f662c0281f3493";
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
	}
}
