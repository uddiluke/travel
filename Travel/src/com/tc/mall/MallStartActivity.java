// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.mall;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.tc.TCData;
import com.tc.TCUtil;
import com.tc.logic.UserLoginData;
import com.tc.logic.UserRegisterData;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.tc.mall:
//            MallData, MallGuideActivity

public class MallStartActivity extends Activity
{
    private class MallAsyncTask extends AsyncTask
    {

        protected Integer doInBackground(String as[])
        {
            MallData.MallMoreData.MoreDataParaser.parse(MallStartActivity.this);
            MallData.MALL_MORE_DATA.chName = getString(0x7f080034);
            SQLiteDatabase sqlitedatabase = SQLiteDatabase.openDatabase(as[0], null, 16);
            MallData.MallFloorData.getMallFloorData(sqlitedatabase);
            MallData.MallTypeData.getMallTypeData(sqlitedatabase);
            MallData.MallPOIData.getMallPOIData(MallStartActivity.this, sqlitedatabase);
            sqlitedatabase.close();
            MallData.MALL_FAVORITE_DATA.siteTypes = new ArrayList();
            MallData.MALL_FAVORITE_DATA.sortTypes = new ArrayList();
            MallData.MALL_FAVORITE_DATA.chName = getString(0x7f080031);
            MallData.MALL_FAVORITE_DATA.tableName = MallData.MALL_APPLICATION;
            MallData.MallFavoriteData _tmp = MallData.MALL_FAVORITE_DATA;
            MallData.MallFavoriteData.POIS = new ArrayList();
            String as1[] = getResources().getStringArray(0x7f050002);
            for(int i = 0; i < as1.length; i++)
                MallData.MALL_FAVORITE_DATA.sortTypes.add(as1[i]);

            for(int j = 0; j < MallData.MALL_TYPE_DATA.TYPES.size(); j++)
                MallData.MALL_FAVORITE_DATA.siteTypes.add(((MallData.MallTypeData.Type)MallData.MALL_TYPE_DATA.TYPES.get(j)).name);

            MallData.MallFavoriteData.close(MallStartActivity.this);
            MallData.MallFavoriteData.query(MallStartActivity.this);
            UserRegisterData.userRegister(MallStartActivity.this, MallData.MALL_APPLICATION);
            UserLoginData.userLogin(MallStartActivity.this, MallData.MALL_APPLICATION);
            return Integer.valueOf(0);
        }

        protected  Object doInBackground(Object aobj[])
        {
            return doInBackground((String[])aobj);
        }

        protected void onCancelled()
        {
            super.onCancelled();
        }

        protected void onPostExecute(Integer integer)
        {
            super.onPostExecute(integer);
            progressbar.setVisibility(4);
            TCUtil.startActivity(MallStartActivity.this,MallGuideActivity.class);
            finish();
        }

        protected  void onPostExecute(Object obj)
        {
            onPostExecute((Integer)obj);
        }

        protected void onPreExecute()
        {
            super.onPreExecute();
            progressbar.setVisibility(0);
        }

        protected void onProgressUpdate(Object aobj[])
        {
            onProgressUpdate((String[])aobj);
        }

        protected  void onProgressUpdate(String as[])
        {
            super.onProgressUpdate(as);
        }

      

    }


    public MallStartActivity()
    {
    }

    private void setBackground()
    {
        findViewById(0x7f0a0125).setBackgroundDrawable(TCUtil.getDrawable(this, MallData.MALL_COVER_PATH));
    }

    public void onBackPressed()
    {
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f030051);
        progressbar = (ProgressBar)findViewById(0x7f0a001b);
        Bundle bundle1 = getIntent().getExtras();
        MallData.MALL_ID = bundle1.getInt(KEY_MALL_ID);
        MallData.MALL_PATH = (new StringBuilder()).append(TCData.SG_ROOT).append(MallData.MALL_ID).append("/").toString();
        MallData.MALL_ZIP_PATH = (new StringBuilder()).append(MallData.MALL_PATH).append("mall100002_v3.zip").toString();
        MallData.MALL_FLURRY = "";
        MallData.MALL_APPLICATION = bundle1.getString(KEY_MALL_APPLICATION);
        MallData.MALL_DATA_BASE_PATH = (new StringBuilder()).append(MallData.MALL_PATH).append("mallguide.sqlite").toString();
        String as[];
        if(TCData.USE_2X)
            MallData.MALL_COVER_PATH = (new StringBuilder()).append(MallData.MALL_PATH).append("icons/cover@2x.png").toString();
        else
            MallData.MALL_COVER_PATH = (new StringBuilder()).append(MallData.MALL_PATH).append("icons/cover.png").toString();
        TCUtil.unzip(MallData.MALL_ZIP_PATH, MallData.MALL_PATH);
        as = new String[1];
        as[0] = MallData.MALL_DATA_BASE_PATH;
        asyncTask = new MallAsyncTask();
        asyncTask.execute(as);
        setBackground();
    }

    public static String KEY_MALL_APPLICATION = "mall_application";
    public static String KEY_MALL_ID = "mall_id";
    private static final String TAG = MallStartActivity.class.getSimpleName();
    private MallAsyncTask asyncTask;
    private ProgressBar progressbar;


}
