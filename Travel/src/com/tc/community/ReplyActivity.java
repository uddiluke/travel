//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.community;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tc.TCUtil;
import com.tc.cg.CGBaseActivity;
import com.tc.cg.CGData;

// Referenced classes of package com.tc.community:
//            CommunityUtil, CommunityJSON

public class ReplyActivity extends CGBaseActivity {

	public ReplyActivity() {
	}

	public void finish() {
		if (replyed) {
			Intent intent = new Intent();
			intent.putExtra("REPLY", reply_content.getText().toString());
			setResult(replyCount, intent);
		}
		TCUtil.hideSoftKeyBroad(this, reply_content);
		super.finish();
	}

	public void onBackPressed() {
		super.onBackPressed();
		TCUtil.hideSoftKeyBroad(this, reply_content);
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f03005b);
		id = getIntent().getIntExtra("ID", -1);
		replyCount = 0;
		replyed = false;
		findViewById(0x7f0a0000).setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				onBackPressed();
			}

		});
		findViewById(0x7f0a00c4).setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				if (reply_content.getText().toString().length() > 140)
					CommunityUtil.showCommunityToast(ReplyActivity.this, "12");
				else if (reply_content.getText().toString().length() == 0)
					CommunityUtil.showCommunityToast(ReplyActivity.this, "22");
				else if (CommunityUtil.getTCToken(ReplyActivity.this) != null)
					(new AsyncTask() {

						protected Object doInBackground(Object aobj[]) {

							statusCode = CommunityJSON.comment(ReplyActivity.this, CGData.CG_COMMUNITY_CODE, reply_content.getText().toString(), id);
							return null;
						}

						protected void onPostExecute(Object obj) {

							progressDialog.dismiss();
							if ("OK".equals(statusCode)) {
								replyCount = 1;
								replyed = true;
								finish();
								CommunityUtil.showCommunityToast(ReplyActivity.this, "14");
							} else {
								CommunityUtil.showCommunityToast(ReplyActivity.this, statusCode);
							}
						}

						protected void onPreExecute() {
							replyCount = 0;
							progressDialog = new ProgressDialog(ReplyActivity.this);
							progressDialog.setTitle(0x7f08004d);
							progressDialog.setMessage(getString(0x7f08004c));
							progressDialog.show();
						}

						ProgressDialog progressDialog;
						String statusCode;

					}).execute(null);
				else if (CommunityUtil.getTCAnonymousToken(ReplyActivity.this) != null) {
					if (CommunityUtil.getTCAnonymousOptionCount(ReplyActivity.this) < 2)
						(new AsyncTask() {

							protected Object doInBackground(Object aobj[]) {

								statusCode = CommunityJSON.comment(ReplyActivity.this, CGData.CG_COMMUNITY_CODE, reply_content.getText().toString(),
										id);
								return null;
							}

							protected void onPostExecute(Object obj) {

								progressDialog.dismiss();
								if ("OK".equals(statusCode)) {
									replyCount = 1;
									CommunityUtil.setTCAnonymousOptionCount(ReplyActivity.this,
											1 + CommunityUtil.getTCAnonymousOptionCount(ReplyActivity.this));
									replyed = true;
									finish();
									CommunityUtil.showCommunityToast(ReplyActivity.this, "14");
								} else {
									CommunityUtil.showCommunityToast(ReplyActivity.this, statusCode);
								}
							}

							protected void onPreExecute() {
								replyCount = 0;
								progressDialog = new ProgressDialog(ReplyActivity.this);
								progressDialog.setTitle(0x7f08004d);
								progressDialog.setMessage(getString(0x7f08004c));
								progressDialog.show();
							}

							ProgressDialog progressDialog;
							String statusCode;

						}).execute(null);
					else if (CommunityUtil.getTCAnonymousOptionCount(ReplyActivity.this) < 5)
						CommunityUtil.showAnonymous2TimesDialog(ReplyActivity.this, CG_ID, new Handler() {

							public void handleMessage(Message message) {
								(new AsyncTask() {

									protected Object doInBackground(Object aobj[]) {

										statusCode = CommunityJSON.comment(ReplyActivity.this, CGData.CG_COMMUNITY_CODE, reply_content.getText()
												.toString(), id);
										return null;
									}

									protected void onPostExecute(Object obj) {

										progressDialog.dismiss();
										if ("OK".equals(statusCode)) {
											replyCount = 1;
											CommunityUtil.setTCAnonymousOptionCount(ReplyActivity.this,
													1 + CommunityUtil.getTCAnonymousOptionCount(ReplyActivity.this));
											replyed = true;
											finish();
											CommunityUtil.showCommunityToast(ReplyActivity.this, "14");
										} else {
											CommunityUtil.showCommunityToast(ReplyActivity.this, statusCode);
										}
									}

									protected void onPreExecute() {
										replyCount = 0;
										progressDialog = new ProgressDialog(ReplyActivity.this);
										progressDialog.setTitle(0x7f08004d);
										progressDialog.setMessage(getString(0x7f08004c));
										progressDialog.show();
									}

									ProgressDialog progressDialog;
									String statusCode;

								}).execute(null);
							}

						});
					else
						CommunityUtil.showAnonymous5TimesDialog(ReplyActivity.this, CG_ID);
				} else {
					CommunityUtil.showAnonymousFirstTimesDialog(ReplyActivity.this, CG_ID, new Handler() {

						public void handleMessage(Message message) {
							(new AsyncTask() {

								protected Object doInBackground(Object aobj[]) {

									statusCode = CommunityJSON.getAnonymousToken(ReplyActivity.this);
									return null;
								}

								protected void onPostExecute(Object obj) {

									if ("OK".equals(statusCode))
										(new AsyncTask() {

											protected Object doInBackground(Object aobj[]) {

												statusCode = CommunityJSON.comment(ReplyActivity.this, CGData.CG_COMMUNITY_CODE, reply_content
														.getText().toString(), id);
												return null;
											}

											protected void onPostExecute(Object obj) {

												progressDialog.dismiss();
												if ("OK".equals(statusCode)) {
													replyCount = 1;
													CommunityUtil.setTCAnonymousOptionCount(ReplyActivity.this,
															1 + CommunityUtil.getTCAnonymousOptionCount(ReplyActivity.this));
													replyed = true;
													finish();
													CommunityUtil.showCommunityToast(ReplyActivity.this, "14");
												} else {
													CommunityUtil.showCommunityToast(ReplyActivity.this, statusCode);
												}
											}

											protected void onPreExecute() {
												replyCount = 0;
												progressDialog = new ProgressDialog(ReplyActivity.this);
												progressDialog.setTitle(0x7f08004d);
												progressDialog.setMessage(getString(0x7f08004c));
												progressDialog.show();
											}

											ProgressDialog progressDialog;
											String statusCode;

										}).execute(null);
									else
										CommunityUtil.showCommunityToast(ReplyActivity.this, statusCode);
								}

								String statusCode;

							}).execute(null);
						}

					});
				}
			}

		});
		reply_text1 = (TextView) findViewById(0x7f0a0219);
		reply_text2 = (TextView) findViewById(0x7f0a0217);
		reply_leftcount = (TextView) findViewById(0x7f0a0218);
		reply_content = (EditText) findViewById(0x7f0a021a);
		reply_content.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable editable) {
				if (editable.length() <= 140) {
					reply_text1.setTextColor(getResources().getColor(0x7f060011));
					reply_text1.setText(0x7f08015e);
					reply_text2.setTextColor(getResources().getColor(0x7f060011));
					reply_leftcount.setTextColor(getResources().getColor(0x7f060011));
					reply_leftcount.setText((new StringBuilder()).append("").append(140 - editable.length()).toString());
				} else {
					reply_text1.setTextColor(getResources().getColor(0x7f060012));
					reply_text1.setText(0x7f08015f);
					reply_text2.setTextColor(getResources().getColor(0x7f060012));
					reply_leftcount.setTextColor(getResources().getColor(0x7f060012));
					reply_leftcount.setText((new StringBuilder()).append("").append(-140 + editable.length()).toString());
				}
			}

			public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
			}

			public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
			}

		});
	}

	public static final String ID = "ID";
	public static final String REPLY = "REPLY";
	private int id;
	private int replyCount;
	private EditText reply_content;
	private TextView reply_leftcount;
	private TextView reply_text1;
	private TextView reply_text2;
	private boolean replyed;

	/*
	 * static int access$102(ReplyActivity replyactivity, int i) {
	 * replyactivity.replyCount = i; return i; }
	 */

	/*
	 * static boolean access$302(ReplyActivity replyactivity, boolean flag) {
	 * replyactivity.replyed = flag; return flag; }
	 */

}
