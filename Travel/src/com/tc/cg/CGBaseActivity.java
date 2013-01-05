//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

// Referenced classes of package com.tc.cg:
//            CGDataManager, CGData

public class CGBaseActivity extends Activity {

	public CGBaseActivity() {
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		getWindow().setFormat(1);
		Bundle bundle1 = getIntent().getExtras();
		if (bundle1 != null) {
			CG_ID = bundle1.getInt("cg_id");
			FROM_OTHER_APPLICATION = bundle1.getBoolean("from_other_app", true);
			CG_DATA = CGDataManager.getCGData(CG_ID);
		}
	}

	public void startActivity(Context context, Class class1) {
		Intent intent = new Intent(context, class1);
		Bundle bundle = new Bundle();
		bundle.putInt("cg_id", CG_ID);
		bundle.putBoolean("from_other_app", FROM_OTHER_APPLICATION);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	public void startActivity(Context context, Class class1, Bundle bundle) {
		Intent intent = new Intent(context, class1);
		bundle.putInt("cg_id", CG_ID);
		bundle.putBoolean("from_other_app", FROM_OTHER_APPLICATION);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	public void startActivityForResult(Activity activity, Class class1, Bundle bundle, int i) {
		Intent intent = new Intent(activity, class1);
		bundle.putInt("cg_id", CG_ID);
		bundle.putBoolean("from_other_app", FROM_OTHER_APPLICATION);
		intent.putExtras(bundle);
		activity.startActivityForResult(intent, i);
	}

	public static final String KEY_CG_ID = "cg_id";
	public static final String KEY_FROM_OTHER_APPLICATION = "from_other_app";
	public CGData CG_DATA;
	public int CG_ID;
	public boolean FROM_OTHER_APPLICATION;
}
