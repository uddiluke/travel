// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.cg;

import java.util.Iterator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.touchchina.cityguide.sz.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGUtil, CGSiteActivity, CGInfoActivity, 
//            CGWeatherActivity, CGNewsActivity, Rate, CGSGActivity, 
//            CGData

public class CGGuideActivity extends CGBaseActivity implements android.view.View.OnClickListener {

	public CGGuideActivity() {
	}

	public void onBackPressed() {
		CGUtil.showExitDialog(this);
	}

	public void onClick(View view) {
		String s;
		Bundle bundle;
		s = (String) view.getTag();
		bundle = new Bundle();
		FlurryAgent.logEvent((new StringBuilder()).append("Enter").append(s).append("FromDashboard").toString());
		switch (view.getId()) {
		case R.id.newsImageButton:
			startActivity(this, CGNewsActivity.class, bundle);
			break;
		case R.id.downloadTab:
			break;
		case R.id.downloadImageButton:
			startActivity(this, CGSGActivity.class);
			break;
		case R.id.siteImageButton:
		case R.id.hotelImageButton:
		case R.id.restaurantImageButton:
		case R.id.shoppingImageButton:
		case R.id.funImageButton:
		case R.id.favoriteImageButton:
			bundle.putString(CGSiteActivity.KEY_CG_TYPE, s);
			startActivity(this, CGSiteActivity.class, bundle);
			break;
		case R.id.trafficImageButton:
		case R.id.introduceImageButton:
			bundle.putString(CGInfoActivity.KEY_INFO_TYPE, s);
			startActivity(this, CGInfoActivity.class, bundle);
			break;
		case R.id.rateTab:
			break;
		case R.id.rateImageButton:
			startActivity(this, Rate.class, bundle);
			break;
		case R.id.weatherImageButton:

			startActivity(this, CGWeatherActivity.class, bundle);
			break;

		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.cg_guide);
		((TextView) findViewById(0x7f0a0001)).
		     setText((new StringBuilder()).append(CG_DATA.CG_NAME).append(getString(0x7f080003)).toString());
		findViewById(0x7f0a0000).setVisibility(4);
		View view = findViewById(0x7f0a0053);
		view.setOnClickListener(this);
		view.setTag("Site");
		View view1 = findViewById(0x7f0a0054);
		view1.setOnClickListener(this);
		view1.setTag("Hotel");
		View view2 = findViewById(0x7f0a0055);
		view2.setOnClickListener(this);
		view2.setTag("Restaurant");
		View view3 = findViewById(0x7f0a0056);
		view3.setOnClickListener(this);
		view3.setTag("Shopping");
		View view4 = findViewById(0x7f0a0057);
		view4.setOnClickListener(this);
		view4.setTag("Fun");
		View view5 = findViewById(0x7f0a0058);
		view5.setOnClickListener(this);
		view5.setTag("traffics");
		View view6 = findViewById(0x7f0a0059);
		view6.setOnClickListener(this);
		view6.setTag("intro");
		View view7 = findViewById(0x7f0a005c);
		view7.setOnClickListener(this);
		view7.setTag("weather");
		View view8 = findViewById(0x7f0a005d);
		view8.setOnClickListener(this);
		view8.setTag("Favorite");
		View view9 = findViewById(0x7f0a0050);
		view9.setOnClickListener(this);
		view9.setTag("news");
		View view10 = findViewById(0x7f0a0052);
		view10.setOnClickListener(this);
		view10.setTag("download");
		Iterator iterator = CG_DATA.CG_INFO_DATA.cgInfoItems.iterator();
		do {
			if (!iterator.hasNext())
				break;
			if (!((CGData.CGInfoData.CGInfoItem) iterator.next()).type.equals("news"))
				continue;
			findViewById(0x7f0a004f).setVisibility(0);
			break;
		} while (true);
		View view11 = findViewById(0x7f0a005b);
		view11.setOnClickListener(this);
		view11.setTag("Currency");
		if (CG_DATA.CG_RATE_CODE != null)
			findViewById(0x7f0a005a).setVisibility(0);
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

	private static final String TAG = CGGuideActivity.class.getSimpleName();

}
