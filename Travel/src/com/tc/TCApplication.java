//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.tc.net.DownAsyncTask;
import java.util.ArrayList;

public class TCApplication extends Application
{
    public class DownloadingInfo
    {

        public long TOTAL;
        public DownAsyncTask downAsyncTask;
        public int guideid;
        public Handler handler;
        public long hasDownloaded;
        public boolean isDownloading;
        public boolean isUnZip;
        public String name;
        public int timeoutCount;

  
    }

    public static class MyGeneralListener
        implements MKGeneralListener
    {

        public void onGetNetworkState(int i)
        {
            Log.e(TCApplication.TAG, (new StringBuilder()).append("onGetNetworkState :").append(i).toString());
        }

        public void onGetPermissionState(int i)
        {
            if(i == 300)
            {
                TCApplication.tcApplication.isBKeyRight = false;
                Log.e(TCApplication.TAG, "onGetPermissionState : ERROR_PERMISSION_DENIED");
            } else
            {
                Log.e(TCApplication.TAG, (new StringBuilder()).append("onGetPermissionState : ").append(i).toString());
            }
        }

        public MyGeneralListener()
        {
        }
    }


    public TCApplication()
    {
        bMapManager = null;
        isBKeyRight = true;
    }

    public void onCreate()
    {
        if(bMapManager == null)
        {
            tcApplication = this;
            bMapManager = new BMapManager(getApplicationContext());
            bMapManager.init("CA7562C34D2CCA12AEBD64C02FF4F4BD4388B90C", new MyGeneralListener());
        }
        super.onCreate();
    }

    public void onLowMemory()
    {
        super.onLowMemory();
        Toast.makeText(this, getString(0x7f080062), 1).show();
    }

    public void onTerminate()
    {
        if(bMapManager != null)
        {
            bMapManager.destroy();
            bMapManager = null;
        }
        super.onTerminate();
    }

    private static final String B_MAP_KEY = "CA7562C34D2CCA12AEBD64C02FF4F4BD4388B90C";
    private static final String TAG = TCApplication.class.getSimpleName();
    private static TCApplication tcApplication;
    public BMapManager bMapManager;
    public ArrayList<DownloadingInfo> downloadingInfos;
    private boolean isBKeyRight;





/*
    static boolean access$202(TCApplication tcapplication, boolean flag)
    {
        tcapplication.isBKeyRight = flag;
        return flag;
    }

*/
}
