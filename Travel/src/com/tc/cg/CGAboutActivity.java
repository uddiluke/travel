// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.cg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.tc.TCHtmlActivity;
import com.tc.TCUtil;
import com.touchchina.cityguide.sz.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity

public class CGAboutActivity extends CGBaseActivity implements android.view.View.OnClickListener {

	public CGAboutActivity() {
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backButton:
			onBackPressed();
			break;
		case R.id.copyrightTableRow:
			Intent intent1 = new Intent(this, TCHtmlActivity.class);
			intent1.putExtra("title", getString(0x7f0800b6));
			intent1.putExtra("url", PRIVATE_POLICY);
			startActivity(intent1);
			break;
		case R.id.aboutscreetTableRow:
			Intent intent = new Intent(this, TCHtmlActivity.class);
			intent.putExtra("title", getString(0x7f0800c1));
			intent.putExtra("url", VERSION_INFO);
			startActivity(intent);
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030004);
		titleText = (TextView) findViewById(0x7f0a0001);
		titleText.setText(getString(0x7f0800bb));
		aboutInfo = (TextView) findViewById(0x7f0a0017);
		aboutInfo.setText((new StringBuilder()).append("@").append(getString(0x7f0800c3)).toString());
		aboutAppName = (TextView) findViewById(0x7f0a0015);
		aboutAppName.setText(getString(0x7f080000));
		aboutAppVersion = (TextView) findViewById(0x7f0a0016);
		aboutAppVersion.setText((new StringBuilder()).append(getString(0x7f0800c2)).append(TCUtil.getVersionName(this)).toString());
		copyRight = (RelativeLayout) findViewById(0x7f0a0018);
		copyRight.setOnClickListener(this);
		aboutScreet = (RelativeLayout) findViewById(0x7f0a0019);
		aboutScreet.setOnClickListener(this);
		backButton = (ImageView) findViewById(0x7f0a0000);
		backButton.setOnClickListener(this);
	}

	public static final String PRIVATE_POLICY = "http://www.itouchchina.com/privatepolicy.html";
	public static final String VERSION_INFO = "http://www.itouchchina.com/copyright.html";
	private TextView aboutAppName;
	private TextView aboutAppVersion;
	private TextView aboutInfo;
	private RelativeLayout aboutScreet;
	private ImageView backButton;
	private RelativeLayout copyRight;
	private TextView titleText;
}
