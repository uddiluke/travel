//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.flurry.android.FlurryAgent;
import com.tc.TCUtil;
import com.touchchina.cityguide.sz.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData, CGIntroduceActivity, CGGalleryActivity

public class CGStartActivity extends CGBaseActivity {
	private class CGAsyncTask extends AsyncTask {

		protected Integer doInBackground(String as[]) {
			File file = new File(CG_DATA.CG_PLAN_PATH);
			if (file != null && !file.exists())
				TCUtil.doAssertUnzip(CGStartActivity.this, "plan.zip", CG_DATA.CG_PATH);
			SQLiteDatabase sqlitedatabase = CGData.CGSGData.openDatabase(CGStartActivity.this, CG_DATA);
			CG_DATA.CG_REGION = getRegion(sqlitedatabase);
			CG_DATA.CG_BRAND = getBrand(sqlitedatabase);
			CG_DATA.CG_SG_DATAS = new ArrayList();
			CG_DATA.CG_INFO_DATAS = new ArrayList();
			CG_DATA.CG_FAVORITE_DATA = new CGData.CGFavoriteData();
			CG_DATA.CG_MORE_DATA = new CGData.CGMoreData();
			CG_DATA.CG_RECOMMEND_DATA = new CGData.CGRecommendData();
			CG_DATA.CG_SITE_DATAS = new ArrayList();
			int i = 0;
			while (i < as.length) {
				String s = as[i];
				String as1[] = s.split(":");
				String s1 = as1[0];
				String s2 = as1[1];
				String s3 = as1[2];
				if (s1.equals("sg")) {
					CGData.CGSGData cgsgdata = CGData.CGSGData.query(CGStartActivity.this, CG_DATA, s2);
					cgsgdata.chName = s3;
					CG_DATA.CG_SG_DATAS.add(cgsgdata);
				} else if (s1.equals("cg")) {
					CGData.InfoData infodata = CGData.InfoData.InfoParaser.parse(CGStartActivity.this, CG_DATA, s2);
					infodata.chName = s3;
					CG_DATA.CG_INFO_DATAS.add(infodata);
				} else if (s1.equals("favorite")) {
					CG_DATA.CG_FAVORITE_DATA.siteTypes = new ArrayList();
					CG_DATA.CG_FAVORITE_DATA.sortTypes = new ArrayList();
					String as2[] = getResources().getStringArray(0x7f050001);
					for (int j = 0; j < as2.length; j++)
						CG_DATA.CG_FAVORITE_DATA.sortTypes.add(as2[j]);

					for (int k = 0; k < CG_DATA.CG_INFO_DATA.cgInfoItems.size(); k++) {
						CGData.CGInfoData.CGInfoItem cginfoitem = (CGData.CGInfoData.CGInfoItem) CG_DATA.CG_INFO_DATA.cgInfoItems.get(k);
						if (cginfoitem.type.equals("sg"))
							CG_DATA.CG_FAVORITE_DATA.siteTypes.add((new StringBuilder()).append(cginfoitem.chName).append(":")
									.append(cginfoitem.name).toString());
					}

					CG_DATA.CG_FAVORITE_DATA.chName = s3;
					CG_DATA.CG_FAVORITE_DATA.tableName = CG_DATA.CG_APPLICATION;
					CGData.CGFavoriteData.query(CGStartActivity.this, CG_DATA);
				} else if (s1.equals("more")) {
					CG_DATA.CG_MORE_DATA.chName = s3;
					CGData.CGMoreData.MoreDataParaser.parse(CGStartActivity.this, CG_DATA);
				} else if (s1.equals("recommend")) {
					CG_DATA.CG_RECOMMEND_DATA.chName = s3;
					CGData.CGRecommendData.RecommendDataParaser.parse(CGStartActivity.this, CG_DATA);
				}
				publishProgress(new String[] { s });
				i++;
			}
			CGData.CGPlanData.parase(CGStartActivity.this, CG_DATA, sqlitedatabase);
			CGData.CGSGData.closeDatabase();
			return Integer.valueOf(0);
		}

		protected Object doInBackground(Object aobj[]) {
			return doInBackground((String[]) aobj);
		}

		protected void onCancelled() {
			super.onCancelled();
		}

		protected void onPostExecute(Integer integer) {
			//super.onPostExecute(integer);
			progressbar.setVisibility(4);
			if(firstTime)
				startActivity(CGStartActivity.this, CGIntroduceActivity.class);
			else
				startActivity(CGStartActivity.this, CGGalleryActivity.class);
			finish();
		}

		protected void onPostExecute(Object obj) {
			onPostExecute((Integer) obj);
		}

		protected void onPreExecute() {
			super.onPreExecute();
			progressbar.setVisibility(0);
		}

		protected void onProgressUpdate(Object aobj[]) {
			onProgressUpdate((String[]) aobj);
		}

		protected void onProgressUpdate(String as[]) {
			super.onProgressUpdate(as);
			Log.i(CGStartActivity.TAG, as[0]);
		}

	}

	public CGStartActivity() {
	}

	private CGData.CGBrand getBrand(SQLiteDatabase sqlitedatabase) {
		Cursor cursor = sqlitedatabase.rawQuery("select Brand.[id] , Brand.[name] , Brand.[type] , Brand.[logo] , Brand.[aliasname] from Brand ",
				null);
		CGData.CGBrand cgbrand = null;
		if (cursor != null && cursor.moveToFirst()) {
			cgbrand = new CGData.CGBrand();
			cgbrand.sgBrands = new ArrayList();
			do {
				CGData.CGBrand.SGBrand sgbrand = new CGData.CGBrand.SGBrand();
				int i = 0 + 1;
				sgbrand.id = cursor.getInt(0);
				int j = i + 1;
				sgbrand.name = cursor.getString(i);
				int k = j + 1;
				sgbrand.type = cursor.getString(j);
				StringBuilder stringbuilder = (new StringBuilder()).append(CG_DATA.CG_PATH).append("brands/");
				int l = k + 1;
				sgbrand.logo = stringbuilder.append(cursor.getString(k)).toString();
				int _tmp = l + 1;
				sgbrand.aliasName = cursor.getString(l);
				cgbrand.sgBrands.add(sgbrand);
			} while (cursor.moveToNext());
			cursor.close();
		}
		return cgbrand;
	}

	private CGData.CGRegion getRegion(SQLiteDatabase sqlitedatabase) {
		Cursor cursor = sqlitedatabase.rawQuery("select Region.[id] , Region.[name] from Region ", null);
		CGData.CGRegion cgregion = null;
		if (cursor != null && cursor.moveToFirst()) {
			cgregion = new CGData.CGRegion();
			cgregion.sgRegions = new HashMap();
			do {
				int i = 0 + 1;
				int j = cursor.getInt(0);
				int _tmp = i + 1;
				String s = cursor.getString(i);
				cgregion.sgRegions.put(Integer.valueOf(j), s);
			} while (cursor.moveToNext());
			cursor.close();
		}
		return cgregion;
	}

	private void setBackground() {
		findViewById(R.id.background).setBackgroundDrawable(TCUtil.getDrawable(this, CG_DATA.CG_COVER_PATH));
	}

	public void onBackPressed() {
	}

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.cg_start);
		progressbar = (ProgressBar) findViewById(R.id.progressBar);
		setBackground();
		SharedPreferences sharedpreferences = getSharedPreferences("config", 0);
		if (sharedpreferences.getBoolean("first", true)) {
			firstTime = true;
			android.content.SharedPreferences.Editor editor = sharedpreferences.edit();
			editor.putBoolean("first", false);
			editor.commit();
		}
		ArrayList arraylist = new ArrayList();
		CGData.CGInfoData.CGInfoItem cginfoitem;
		for (Iterator iterator = CG_DATA.CG_INFO_DATA.cgInfoItems.iterator(); iterator.hasNext(); arraylist.add((new StringBuilder())
				.append(cginfoitem.type).append(":").append(cginfoitem.name).append(":").append(cginfoitem.chName).toString()))
			cginfoitem = (CGData.CGInfoData.CGInfoItem) iterator.next();

		String as[] = new String[arraylist.size()];
		for (int i = 0; i < as.length; i++)
			as[i] = (String) arraylist.get(i);

		asyncTask = new CGAsyncTask();
		asyncTask.execute(as);
	}

	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(getApplicationContext(), CGData.CG_FLURRY);
		FlurryAgent.logEvent("onStartSession");
		if (getIntent().getBooleanExtra("is_first_install", false))
			FlurryAgent.logEvent("FirstInstall");
	}

	private static final String TAG = CGStartActivity.class.getSimpleName();
	private CGAsyncTask asyncTask;
	private boolean firstTime;
	private ProgressBar progressbar;

}
