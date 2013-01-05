//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.community;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class AccountEditActivity extends CGBaseActivity {

	public AccountEditActivity() {
	}

	public void onBackPressed() {
		super.onBackPressed();
		TCUtil.hideSoftKeyBroad(this, nickName);
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030000);
		findViewById(0x7f0a0000).setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				onBackPressed();
			}

		});
		((TextView) findViewById(0x7f0a0001)).setText(0x7f08014b);
		sendButton = (TextView) findViewById(0x7f0a0002);
		nickName = (EditText) findViewById(0x7f0a0004);
		psw = (EditText) findViewById(0x7f0a0006);
		isPswHintClean = false;
		nickNameEdited = false;
		if ("touchchina".equals(CommunityUtil.getLoginType(this))) { // goto
																		// _L2;
																		// else
																		// goto
																		// _L1
			nickName.setText(CommunityUtil.getTCNickName(this));
		} else {
			((TextView) findViewById(0x7f0a0003)).setText(0x7f080169);
			nickName.setFocusable(false);
			((TextView) findViewById(0x7f0a0005)).setText(0x7f080150);
			isPswHintClean = true;
			psw.setText(CommunityUtil.getTCNickName(this));
			psw.setInputType(1);
			if ("sina".equals(CommunityUtil.getLoginType(this)))
				nickName.setText((new StringBuilder()).append(getString(0x7f08016a)).append(getString(0x7f08014c)).append(getString(0x7f08014f))
						.toString());
			else if ("tencent".equals(CommunityUtil.getLoginType(this)))
				nickName.setText((new StringBuilder()).append(getString(0x7f08016a)).append(getString(0x7f08014d)).append(getString(0x7f08014f))
						.toString());
		}
		nickName.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable editable) {
			}

			public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
			}

			public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
				sendButton.setVisibility(0);
				nickNameEdited = true;
			}

		});
		psw.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable editable) {
			}

			public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
			}

			public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
				sendButton.setVisibility(0);
			}

		});

		sendButton.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				if ("touchchina".equals(CommunityUtil.getLoginType(AccountEditActivity.this))) {
					if ("".equals(nickName.getText().toString().trim()) || "".equals(psw.getText().toString().trim()))
						CommunityUtil.showCommunityToast(AccountEditActivity.this, "6");
					else if (nickName.getText().toString().trim().getBytes().length < 6)
						CommunityUtil.showCommunityToast(AccountEditActivity.this, "18");
					else if (nickName.getText().toString().trim().length() > 12)
						CommunityUtil.showCommunityToast(AccountEditActivity.this, "19");
					else if (!nickName.getText().toString().trim().matches("^[a-zA-Z0-9_\u4E00-\u9FA5]+$"))
						CommunityUtil.showCommunityToast(AccountEditActivity.this, "26");
					else if (psw.getText().toString().trim().length() > 15 || psw.getText().toString().trim().length() < 6) {
						CommunityUtil.showCommunityToast(AccountEditActivity.this, "20");
					} else {
						final ProgressDialog progressDialog = new ProgressDialog(AccountEditActivity.this);
						progressDialog.setMessage(getString(0x7f08004c));
						progressDialog.show();
						(new AsyncTask() {

							protected Object doInBackground(Object aobj[]) {

								if (!nickName.getText().toString().equals(CommunityUtil.getTCNickName(AccountEditActivity.this))) {
									// goto _L2; else goto _L1
									nickNameChanged = true;

									nickNameStatus = CommunityJSON.changeNickName(AccountEditActivity.this, CGData.CG_COMMUNITY_CODE, nickName
											.getText().toString());

								} else {
									if (nickNameEdited) {
										nickNameChanged = true;
										nickNameStatus = "OK";
									}
								}

								if (!psw.getText().toString().trim().equals(getString(0x7f080166).trim())) {
									pswChanged = true;

									pswStatus = CommunityJSON.changePwd(AccountEditActivity.this, CGData.CG_COMMUNITY_CODE, psw.getText().toString()
											.trim());
								}
								return null;

							}

							protected void onPostExecute(Object obj) {
								progressDialog.dismiss();
								if (nickNameChanged)
									if ("OK".equals(nickNameStatus)) {
										sendButton.setVisibility(8);
										CommunityUtil.showCommunityToast(AccountEditActivity.this, "9");
									} else {
										CommunityUtil.showCommunityToast(AccountEditActivity.this, nickNameStatus);
									}
								if (pswChanged)
									if ("OK".equals(pswStatus)) {
										sendButton.setVisibility(8);
										CommunityUtil.showCommunityToast(AccountEditActivity.this, "10");
									} else {
										CommunityUtil.showCommunityToast(AccountEditActivity.this, pswStatus);
									}
							}

							boolean nickNameChanged;
							String nickNameStatus;
							boolean pswChanged;
							String pswStatus;

						}).execute(null);
					}
				} else if ("".equals(psw.getText().toString().trim()))
					CommunityUtil.showCommunityToast(AccountEditActivity.this, "6");
				else if (psw.getText().toString().trim().getBytes().length < 6)
					CommunityUtil.showCommunityToast(AccountEditActivity.this, "18");
				else if (psw.getText().toString().trim().length() > 12)
					CommunityUtil.showCommunityToast(AccountEditActivity.this, "19");
				else if (!psw.getText().toString().trim().matches("^[a-zA-Z0-9_\u4E00-\u9FA5]+$")) {
					CommunityUtil.showCommunityToast(AccountEditActivity.this, "26");
				} else {
					final ProgressDialog progressDialog = new ProgressDialog(AccountEditActivity.this);
					progressDialog.setMessage(getString(0x7f08004c));
					progressDialog.show();
					(new AsyncTask() {

						protected Object doInBackground(Object aobj[]) {
						
							if (!psw.getText().toString().equals(CommunityUtil.getTCNickName(AccountEditActivity.this))) {

								CGData _tmp = CG_DATA;
								nickNameStatus = CommunityJSON.changeNickName(AccountEditActivity.this, CGData.CG_COMMUNITY_CODE, psw.getText()
										.toString());
							} else {
								nickNameStatus = "OK";
							}
							return null;
						}

						protected void onPostExecute(Object obj) {
						
							progressDialog.dismiss();
							if ("OK".equals(nickNameStatus)) {
								sendButton.setVisibility(8);
								CommunityUtil.showCommunityToast(AccountEditActivity.this, "9");
							} else {
								CommunityUtil.showCommunityToast(AccountEditActivity.this, nickNameStatus);
							}
						}

						String nickNameStatus;

					}).execute(null);
				}
			}

		});

	}

	boolean isPswHintClean;
	EditText nickName;
	boolean nickNameEdited;
	EditText psw;
	TextView sendButton;
}
