// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.cg;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tc.community.AccountEditActivity;
import com.tc.community.BindActivity;
import com.tc.community.CommunityUtil;
import com.tc.community.LoginRegisterActivity;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity

public class CGSettingActivity extends CGBaseActivity {

	public CGSettingActivity() {
	}

	private void refreshUI() {
		if (CommunityUtil.getTCToken(this) == null) {
			settingLayout1.setVisibility(8);
			settingLayout2.setVisibility(8);
			accountText.setText(0x7f080158);
		} else {
			settingLayout1.setVisibility(0);
			settingLayout2.setVisibility(0);
			accountText.setText(0x7f080159);
			isTClogin.setChecked(true);
		}
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f03002d);
		((TextView) findViewById(0x7f0a0001)).setText(getString(0x7f080026));
		backButton = (ImageView) findViewById(0x7f0a0000);
		backButton.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				onBackPressed();
			}

		});
		settingLayout0 = (RelativeLayout) findViewById(0x7f0a00fa);
		settingLayout0.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				if (CommunityUtil.getTCToken(CGSettingActivity.this) == null)
					startActivity(CGSettingActivity.this, LoginRegisterActivity.class);
				else
					startActivity(CGSettingActivity.this, AccountEditActivity.class);
			}

		});
		settingLayout1 = (RelativeLayout) findViewById(0x7f0a0011);
		settingLayout1.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				startActivity(CGSettingActivity.this, BindActivity.class);
			}

		});
		settingLayout2 = (RelativeLayout) findViewById(0x7f0a0013);
		accountText = (TextView) findViewById(0x7f0a00fb);
		changeFromTouch = false;
		isTClogin = (ToggleButton) findViewById(0x7f0a00fc);
		isTClogin.setOnTouchListener(new android.view.View.OnTouchListener() {

			public boolean onTouch(View view, MotionEvent motionevent) {
				changeFromTouch = true;
				return false;
			}

		});
		isTClogin.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton compoundbutton, boolean flag) {
				if (changeFromTouch)
					if (flag)
						startActivity(CGSettingActivity.this, LoginRegisterActivity.class);
					else
						(new android.app.AlertDialog.Builder(CGSettingActivity.this)).setCancelable(false).setTitle(0x7f080176)
								.setMessage(0x7f080177).setPositiveButton(0x7f08001e, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										CommunityUtil.TClogout(CGSettingActivity.this);
										refreshUI();
									}

								}).setNegativeButton(0x7f08001d, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										isTClogin.setChecked(true);
									}

								}).show();
				changeFromTouch = false;
			}

		});
	}

	protected void onResume() {
		super.onResume();
		refreshUI();
	}

	private TextView accountText;
	private ImageView backButton;
	private boolean changeFromTouch;
	private ToggleButton isTClogin;
	private RelativeLayout settingLayout0;
	private RelativeLayout settingLayout1;
	private RelativeLayout settingLayout2;

	/*
	 * static boolean access$002(CGSettingActivity cgsettingactivity, boolean
	 * flag) { cgsettingactivity.changeFromTouch = flag; return flag; }
	 */

}
