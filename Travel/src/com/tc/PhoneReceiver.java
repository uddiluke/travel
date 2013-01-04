// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc;

import android.content.*;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.tc.sg.SGDataManager;

public class PhoneReceiver extends BroadcastReceiver
{

    public PhoneReceiver()
    {
        listener = new PhoneStateListener() {

            public void onCallStateChanged(int i, String s)
            {
                switch(i){
                case 0:
                	  Log.i(PhoneReceiver.TAG, "CALL_STATE_IDLE");
                      if(PhoneReceiver.OUT_GOING && PhoneReceiver.CALL_STATE == 0)
                      {
                          PhoneReceiver.OUT_GOING = false;
                          PhoneReceiver.CALL_STATE = 1;
                          SGDataManager.pause();
                      } else
                      if(PhoneReceiver.HAS_OFFHOOK && PhoneReceiver.CALL_STATE == 1)
                      {
                          SGDataManager.play();
                          PhoneReceiver.HAS_OFFHOOK = false;
                          PhoneReceiver.CALL_STATE = 0;
                      } else
                      if(PhoneReceiver.CALL_STATE == 2)
                      {
                          SGDataManager.play();
                          PhoneReceiver.CALL_STATE = 0;
                      }
                      break;
                case 1:
                	  Log.i(PhoneReceiver.TAG, "CALL_STATE_RINGING");
                      if(PhoneReceiver.CALL_STATE == 0)
                      {
                          PhoneReceiver.CALL_STATE = 2;
                          SGDataManager.pause();
                      }
                      break;
                case 2:
                	 Log.i(PhoneReceiver.TAG, "CALL_STATE_OFFHOOK");
                     if(PhoneReceiver.CALL_STATE == 1 && !PhoneReceiver.HAS_OFFHOOK)
                         PhoneReceiver.HAS_OFFHOOK = true;
                     break;
                }
           
            }

        }
;
    }

    public void onReceive(Context context, Intent intent)
    {
        ((TelephonyManager)context.getSystemService("phone")).listen(listener, 32);
        if("android.intent.action.NEW_OUTGOING_CALL".equals(intent.getAction()))
        {
            OUT_GOING = true;
            CALL_STATE = 0;
        }
    }

    public static int CALL_STATE;
    public static boolean HAS_OFFHOOK;
    public static boolean OUT_GOING;
    private static final String TAG = PhoneReceiver.class.getSimpleName();
    private PhoneStateListener listener;


}
