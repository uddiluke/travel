//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.google;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.maps.*;
import com.tc.TCUtil;
import java.util.ArrayList;
import java.util.List;

public class MyItemizedOverlay extends ItemizedOverlay
    implements com.google.android.maps.ItemizedOverlay.OnFocusChangeListener
{

    public MyItemizedOverlay(MapView mapview, Drawable drawable, android.view.View.OnClickListener onclicklistener)
    {
        super(boundCenterBottom(drawable));
        overlayItems = new ArrayList();
        mapView = mapview;
        defaultMarker = drawable;
        popView = TCUtil.getLayoutInflater(mapview.getContext()).inflate(0x7f030057, null);
        com.google.android.maps.MapView.LayoutParams layoutparams = new com.google.android.maps.MapView.LayoutParams(-2, -2, null, 81);
        mapview.addView(popView, layoutparams);
        detailButton = (ImageView)popView.findViewById(0x7f0a01f7);
        detailButton.setOnClickListener(onclicklistener);
        popView.setVisibility(8);
        setOnFocusChangeListener(this);
    }

    public void addOverlayItem(OverlayItem overlayitem)
    {
        overlayItems.add(overlayitem);
        setLastFocusedIndex(-1);
        populate();
    }

    public void clearOverlayItem()
    {
        overlayItems.clear();
        setLastFocusedIndex(-1);
        populate();
    }

    protected OverlayItem createItem(int i)
    {
        return (OverlayItem)overlayItems.get(i);
    }

    public void draw(Canvas canvas, MapView mapview, boolean flag)
    {
        super.draw(canvas, mapview, false);
    }

    public void onFocusChanged(ItemizedOverlay itemizedoverlay, OverlayItem overlayitem)
    {
        showPopView(overlayitem);
    }

    protected boolean onTap(int i)
    {
        setFocus(createItem(i));
        return true;
    }

    public boolean onTap(GeoPoint geopoint, MapView mapview)
    {
        return super.onTap(geopoint, mapview);
    }

    public void removeAt(int i)
    {
        overlayItems.remove(i);
    }

    public void showPopView(OverlayItem overlayitem)
    {
        popView.setVisibility(8);
        newFocus = overlayitem;
        if(overlayitem != null && !"@me".equals(overlayitem.getTitle()))
        {
            com.google.android.maps.MapView.LayoutParams layoutparams = (com.google.android.maps.MapView.LayoutParams)popView.getLayoutParams();
            layoutparams.point = overlayitem.getPoint();
            layoutparams.x = -9;
            layoutparams.y = -defaultMarker.getIntrinsicHeight();
            String as[] = overlayitem.getSnippet().split(":");
            if(as != null)
            {
                if(as[0].equals("true"))
                    popView.findViewById(0x7f0a000a).setVisibility(0);
                if(as[1].equals("true"))
                {
                    TextView textview = (TextView)popView.findViewById(0x7f0a00f3);
                    String s = overlayitem.getTitle();
                    String as1[] = s.split(":");
                    if(as1.length > 0)
                        s = as1[-1 + as1.length];
                    if(s.length() > 8)
                        s = (new StringBuilder()).append(s.substring(0, 8)).append("...").toString();
                    textview.setText(s);
                }
                if(as[2].equals("true"))
                    detailButton.setVisibility(0);
            } else
            {
                popView.findViewById(0x7f0a000a).setVisibility(4);
            }
            detailButton.setTag(overlayitem.getTitle());
            mapView.updateViewLayout(popView, layoutparams);
            popView.setVisibility(0);
        }
    }

    public int size()
    {
        return overlayItems.size();
    }

    public static final String GPS_ITEM_DESCRIPTION = "@me";
    private Drawable defaultMarker;
    private ImageView detailButton;
    private MapView mapView;
    private OverlayItem newFocus;
    private List overlayItems;
    private View popView;
}
