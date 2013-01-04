// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.google;

import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import com.google.android.maps.MapView;
import com.tc.TCUtil;

public class GoogleMapView extends MapView
{
    public static interface MapViewZoomListener
    {

        public abstract void zoom();
    }


    public GoogleMapView(Context context)
    {
        this(context, null, 0);
    }

    public GoogleMapView(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public GoogleMapView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        popView = TCUtil.getLayoutInflater(context).inflate(0x7f030057, null);
        com.google.android.maps.MapView.LayoutParams layoutparams = new com.google.android.maps.MapView.LayoutParams(-2, -2, null, 81);
        addView(popView, layoutparams);
        popView.setVisibility(8);
    }

    public GoogleMapView(Context context, String s)
    {
        super(context, s);
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        if(zoomListener != null && motionevent.getPointerCount() == 2)
            zoomListener.zoom();
        return super.onTouchEvent(motionevent);
    }

    public void setMapViewZoomListener(MapViewZoomListener mapviewzoomlistener)
    {
        zoomListener = mapviewzoomlistener;
    }

    private static final String TAG = GoogleMapView.class.getSimpleName();
    private View popView;
    private MapViewZoomListener zoomListener;

}
