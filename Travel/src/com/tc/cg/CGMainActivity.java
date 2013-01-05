//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.flurry.android.FlurryAgent;
import com.tc.TCData;
import com.tc.TCUtil;
import com.tc.community.CommentSquareAcivity;
import com.tc.plan.CityGuidePlanActivity;
import com.tc.weibo.WeiboActivity;

// Referenced classes of package com.tc.cg:
//            CGDataManager, CGMapActivity, CGGuideActivity, CGMoreActivity, 
//            CGData

public class CGMainActivity extends TabActivity {

	public CGMainActivity() {
		GOOGLE_MAP_USEABLE = TCData.GOOGLE_MAP_USEABLE;
		radioGroupListener = new android.widget.RadioGroup.OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup radiogroup, int i) {
				if (checkedId == i)
					return;
				String tag = (String) radioGroup.findViewById(i).getTag();
				String logName = "";

				if (tag.equals("plan"))
					logName = "";
				else if (tag.equals("guide"))
					logName = "Dashboard";
				else if (tag.equals("map"))
					logName = "Map";
				else if (tag.equals("commentSquare"))
					logName = "CommentSquare";
				else if (tag.equals("more"))
					logName = "More";

				FlurryAgent.logEvent((new StringBuilder()).append("TabTo").append(logName).toString());
				if (!GOOGLE_MAP_USEABLE && tag.equals("map")) {
					checkedId = i;
					TCUtil.showTip(CGMainActivity.this, 0x7f08000a);
				} else if (tag.equals("weibo")) {
					radioGroup.check(checkedId);
					Bundle bundle = new Bundle();
					bundle.putBoolean("has_camera", false);
					bundle.putString("status",
							(new StringBuilder()).append(getString(0x7f080036)).append(CG_DATA.CG_NAME).append(getString(0x7f080052)).toString());
					TCUtil.startActivity(CGMainActivity.this, WeiboActivity.class, bundle);
				} else {
					checkedId = i;
					tabHost.setCurrentTabByTag(tag);
				}
				if (tag.equals("commentSquare"))
					FlurryAgent.logEvent("TabToCommentCenter", true);
				else
					FlurryAgent.endTimedEvent("TabToCommentCenter");

			}
		};
	}

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle2 = intent.getExtras();
			CG_ID = bundle2.getInt("cg_id");
			FROM_OTHER_APPLICATION = bundle2.getBoolean("from_other_app", true);
			CG_DATA = CGDataManager.getCGData(CG_ID);
		}
		setContentView(0x7f030011);
		tabHost = getTabHost();
		int i;
		if (GOOGLE_MAP_USEABLE)
			CLSES = (new Class[] { CityGuidePlanActivity.class, CGMapActivity.class, CGGuideActivity.class, CommentSquareAcivity.class,
					CGMoreActivity.class });
		else
			CLSES = (new Class[] { CityGuidePlanActivity.class, CGGuideActivity.class, CGGuideActivity.class, CommentSquareAcivity.class,
					CGMoreActivity.class });
		checkedId = 0x7f0a006d;
		i = 0;
		while (i < TAGS.length) {
			String s = TAGS[i];
			Class class1 = CLSES[i];
			Bundle bundle1 = new Bundle();
			if (GOOGLE_MAP_USEABLE) {
				android.widget.TabHost.TabSpec tabspec1 = tabHost.newTabSpec(s).setIndicator(s);
				Intent intent2 = new Intent(this, class1);
				if ("map".equals(s))
					bundle1.putString("map_title", (new StringBuilder()).append(CG_DATA.CG_NAME).append(getString(0x7f08002f)).toString());
				else if (!"more".equals(s))
					;
				bundle1.putInt("cg_id", CG_ID);
				bundle1.putBoolean("from_other_app", FROM_OTHER_APPLICATION);
				intent2.putExtras(bundle1);
				tabspec1.setContent(intent2);
				tabHost.addTab(tabspec1);
			} else if (!"map".equals(s)) {
				android.widget.TabHost.TabSpec tabspec = tabHost.newTabSpec(s).setIndicator(s);
				Intent intent1 = new Intent(this, class1);
				bundle1.putInt("cg_id", CG_ID);
				bundle1.putBoolean("from_other_app", FROM_OTHER_APPLICATION);
				intent1.putExtras(bundle1);
				tabspec.setContent(intent1);
				tabHost.addTab(tabspec);
			}
			i++;
		}
		radioGroup = (RadioGroup) findViewById(0x7f0a006a);
		radioGroup.setOnCheckedChangeListener(radioGroupListener);
		tabHost.setCurrentTabByTag((String) radioGroup.findViewById(checkedId).getTag());
		getIntent().getExtras();
	}

	public static final String KEY_CG_ID = "cg_id";
	public static final String KEY_FROM_OTHER_APPLICATION = "from_other_app";
	public static String KEY_URL = "url";
	private static final String TAG = CGMainActivity.class.getSimpleName();
	public CGData CG_DATA;
	public int CG_ID;
	private Class CLSES[];
	public boolean FROM_OTHER_APPLICATION;
	private boolean GOOGLE_MAP_USEABLE;
	private String TAGS[] = { "plan", "map", "guide", "commentSquare", "more" };
	private int checkedId;
	private RadioGroup radioGroup;
	private android.widget.RadioGroup.OnCheckedChangeListener radioGroupListener;
	TabHost tabHost;

	/*
	 * static int access$002(CGMainActivity cgmainactivity, int i) {
	 * cgmainactivity.checkedId = i; return i; }
	 */

}
