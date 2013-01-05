//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.community;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tc.TCUtil;
import com.tc.cg.CGBaseActivity;
import com.tc.cg.CGData;
import com.tc.community.TencentWeibo.OAuth;
import com.tc.community.TencentWeibo.TencentTokenActivity;
import com.tc.weibo.WeiboSettings;

// Referenced classes of package com.tc.community:
//            CommunityUtil, SinaBindActivity, CommunityJSON

public class BindActivity extends CGBaseActivity {

	public BindActivity() {
	}

	private void refreshUI() {
		isSinaBind.setChecked(CommunityUtil.isSinaBind(this));
		isTencentBind.setChecked(CommunityUtil.isTencentBind(this));
	}

	protected void onActivityResult(int i, int j, Intent intent) {
		super.onActivityResult(i, j, intent);

		if (intent != null) {
			final OAuth oAuth = (OAuth) intent.getSerializableExtra("TENCENT_TOKEN_INFO");

			(new AsyncTask() {

				protected Object doInBackground(Object aobj[]) {
					statusCode = CommunityJSON.bind(BindActivity.this, CGData.CG_COMMUNITY_CODE, "tencent", oAuth.getName(), oAuth.getOauth_token(),
							oAuth.getOauth_token_secret(), false);
					return null;
				}

				protected void onPostExecute(Object obj) {
					if ("OK".equals(statusCode))
						CommunityUtil.showCommunityToast(BindActivity.this, "16");
					else if ("109".equals(statusCode))
						(new android.app.AlertDialog.Builder(BindActivity.this)).setTitle(0x7f08019e).setMessage(0x7f08019f)
								.setNegativeButton(0x7f08001d, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										refreshUI();
									}

								}).setPositiveButton(0x7f08001e, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										(new AsyncTask() {

											protected Object doInBackground(Object aobj[]) {

												status1 = CommunityJSON.bind(BindActivity.this, CGData.CG_COMMUNITY_CODE, "tencent", oAuth.getName(),
														oAuth.getOauth_token(), oAuth.getOauth_token_secret(), true);
												return null;
											}

											protected void onPostExecute(Object obj) {

												if ("OK".equals(status1))
													CommunityUtil.showCommunityToast(BindActivity.this, "16");
												else
													CommunityUtil.showCommunityToast(BindActivity.this, statusCode);
												refreshUI();
											}

											String status1;

										}).execute(null);
									}

								}).show();
					else if ("124".equals(statusCode))
						(new android.app.AlertDialog.Builder(BindActivity.this)).setTitle(0x7f0801ad).setMessage(0x7f0801ae)
								.setNegativeButton(0x7f08001d, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										refreshUI();
									}

								}).setPositiveButton(0x7f08001e, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										(new AsyncTask() {

											protected Object doInBackground(Object aobj[]) {

												status1 = CommunityJSON.bind(BindActivity.this, CGData.CG_COMMUNITY_CODE, "tencent", oAuth.getName(),
														oAuth.getOauth_token(), oAuth.getOauth_token_secret(), true);
												return null;
											}

											protected void onPostExecute(Object obj) {

												if ("OK".equals(status1))
													CommunityUtil.showCommunityToast(BindActivity.this, "16");
												else
													CommunityUtil.showCommunityToast(BindActivity.this, statusCode);
												refreshUI();
											}

											String status1;

										}).execute(null);
									}

								}).show();
					else
						CommunityUtil.showCommunityToast(BindActivity.this, statusCode);
					refreshUI();
				}

				String statusCode;

			}).execute(null);
		}
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030003);
		((TextView) findViewById(0x7f0a0001)).setText(0x7f080156);
		findViewById(0x7f0a0000).setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				onBackPressed();
			}

		});
		changedFromTouchSina = false;
		changedFromTouchTencent = false;
		isSinaBind = (ToggleButton) findViewById(0x7f0a0012);
		isSinaBind.setOnTouchListener(new android.view.View.OnTouchListener() {

			public boolean onTouch(View view, MotionEvent motionevent) {
				changedFromTouchSina = true;
				return false;
			}

		});
		isSinaBind.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton compoundbutton, boolean flag) {
				if (changedFromTouchSina)
					if (flag) {
						if (TCUtil.isNetAvailable(BindActivity.this))
							startActivity(BindActivity.this, SinaBindActivity.class);
						else
							TCUtil.showNetErrorDialog(BindActivity.this);
					} else {
						(new android.app.AlertDialog.Builder(BindActivity.this)).setCancelable(false).setMessage(0x7f080178)
								.setPositiveButton(0x7f08001e, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										(new AsyncTask() {

											protected Object doInBackground(Object aobj[]) {

												status = CommunityJSON.unBind(BindActivity.this, CGData.CG_COMMUNITY_CODE, "sina");
												return null;
											}

											protected void onPostExecute(Object obj) {

												if ("OK".equals(status)) {
													WeiboSettings.putUserName(BindActivity.this, null);
													WeiboSettings.putPassword(BindActivity.this, null);
													WeiboSettings.putToken(BindActivity.this, null);
													WeiboSettings.putTokenSecret(BindActivity.this, null);
													CommunityUtil.showCommunityToast(BindActivity.this, "17");
												} else {
													CommunityUtil.showCommunityToast(BindActivity.this, status);
												}
												refreshUI();
											}

											String status;

										}).execute(null);
									}

								}).setNegativeButton(0x7f08001d, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										refreshUI();
									}

								}).show();
					}
				changedFromTouchSina = false;
			}

		});
		isTencentBind = (ToggleButton) findViewById(0x7f0a0014);
		isTencentBind.setOnTouchListener(new android.view.View.OnTouchListener() {

			public boolean onTouch(View view, MotionEvent motionevent) {
				changedFromTouchTencent = true;
				return false;
			}

		});
		isTencentBind.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton compoundbutton, boolean flag) {
				if (changedFromTouchTencent)
					if (flag) {
						if (TCUtil.isNetAvailable(BindActivity.this))
							startActivityForResult(new Intent(BindActivity.this, TencentTokenActivity.class), 0);
						else
							TCUtil.showNetErrorDialog(BindActivity.this);
					} else {
						(new android.app.AlertDialog.Builder(BindActivity.this)).setCancelable(false).setMessage(0x7f080178)
								.setPositiveButton(0x7f08001e, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										(new AsyncTask() {

											protected Object doInBackground(Object aobj[]) {

												status = CommunityJSON.unBind(BindActivity.this, CGData.CG_COMMUNITY_CODE, "tencent");
												return null;
											}

											protected void onPostExecute(Object obj) {

												if ("OK".equals(status))
													CommunityUtil.showCommunityToast(BindActivity.this, "17");
												else
													CommunityUtil.showCommunityToast(BindActivity.this, status);
												refreshUI();
											}

											private String status;

										}).execute(null);
									}

								}).setNegativeButton(0x7f08001d, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										refreshUI();
									}

								}).show();
					}
				changedFromTouchTencent = false;
			}

		});
		refreshUI();
	}

	protected void onResume() {
		super.onResume();
		refreshUI();
	}

	boolean changedFromTouchSina;
	boolean changedFromTouchTencent;
	ToggleButton isSinaBind;
	ToggleButton isTencentBind;
	View loginDialogView;

}
