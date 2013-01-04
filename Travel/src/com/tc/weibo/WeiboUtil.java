// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.weibo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import java.io.*;
import java.util.ArrayList;

// Referenced classes of package com.tc.weibo:
//            WeiboActivity

public class WeiboUtil
{

    public WeiboUtil()
    {
    }

    public static ArrayList getFriendList(Context context)
    {
        ArrayList arraylist = null;
        try
        {
            ObjectInputStream objectinputstream = new ObjectInputStream(context.openFileInput("friend_list"));
            arraylist = (ArrayList)objectinputstream.readObject();
            objectinputstream.close();
        }
        catch(Exception exception)
        {
            Log.e(TAG, "getFridenList", exception);
        }
        return arraylist;
    }

    public static void saveFriendList(Context context, ArrayList arraylist)
    {
    	try{
        ObjectOutputStream objectoutputstream = new ObjectOutputStream(context.openFileOutput("friend_list", 0));
        objectoutputstream.writeObject(arraylist);
        objectoutputstream.close();
    	}catch(Exception e){
            Log.e(TAG, "getFridenList", e);
    	}


    }

    public static void startCameraForResult(WeiboActivity weiboactivity, int i)
    {
        Uri uri = Uri.fromFile(new File(WeiboActivity.CAMERA_PATH));
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", uri);
        weiboactivity.startActivityForResult(intent, i);
    }

    public static final String FRIEND_LIST = "friend_list";
    private static final String TAG = WeiboUtil.class.getSimpleName();

}
