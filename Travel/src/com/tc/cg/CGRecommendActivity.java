// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.cg;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.tc.TCUtil;
import com.touchchina.cityguide.sz.R;

import java.util.*;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData, CGRecommendItemActivity

public class CGRecommendActivity extends CGBaseActivity
    implements android.view.View.OnClickListener
{
    private class RecommendAdapter extends BaseAdapter
    {

        private Bitmap getBitmap(Context context1, String s)
        {
            Bitmap bitmap = null;
            if(bitmaps.containsKey(s))
                bitmap = (Bitmap)bitmaps.get(s);
            if(bitmap == null)
            {
                bitmap = TCUtil.getBitmap(context1, s);
                bitmaps.put(s, bitmap);
            }
            return bitmap;
        }

        public void clearBitmaps()
        {
            for(Iterator iterator = bitmaps.entrySet().iterator(); iterator.hasNext(); TCUtil.recycleBitmap((Bitmap)((java.util.Map.Entry)iterator.next()).getValue()));
            bitmaps.clear();
        }

        public int getCount()
        {
            return recommendData.items.size();
        }

        public Object getItem(int i)
        {
            return recommendData.items.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }

        public View getView(int i, View view, ViewGroup viewgroup)
        {
            if(view == null)
                view = TCUtil.getLayoutInflater(context).inflate(0x7f03002a, null);
            CGData.CGRecommendData.CGRecommendItem cgrecommenditem = (CGData.CGRecommendData.CGRecommendItem)getItem(i);
            ((ImageView)view.findViewById(0x7f0a0066)).setImageBitmap(getBitmap(context, cgrecommenditem.icon));
            ((TextView)view.findViewById(0x7f0a0067)).setText(cgrecommenditem.name);
            view.findViewById(0x7f0a0068).setVisibility(8);
            return view;
        }

        Map bitmaps;
        private Context context;
      

        public RecommendAdapter(Context context1)
        {
          
            bitmaps = new HashMap();
            context = context1;
        }
    }


    public CGRecommendActivity()
    {
        itemListener = new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                TCUtil.flurryLog("View", ((CGData.CGRecommendData.CGRecommendItem)recommendData.items.get(i)).name);
                Bundle bundle = new Bundle();
                bundle.putInt(CGRecommendItemActivity.KEY_POSITION, i);
                startActivity(CGRecommendActivity.this, CGRecommendItemActivity.class, bundle);
            }

        
        }
;
    }

    public void onClick(View view)
    {
        if(view.getId()==R.id.backButton)
        	 onBackPressed(); 
      
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        recommendData = CG_DATA.CG_RECOMMEND_DATA;
        setContentView(0x7f030029);
        ((TextView)findViewById(0x7f0a0001)).setText(recommendData.chName);
        findViewById(0x7f0a0000).setOnClickListener(this);
        recommendList = (ListView)findViewById(0x7f0a00f7);
        recommendList.setOnItemClickListener(itemListener);
        recommendAdapter = new RecommendAdapter(this);
        recommendList.setAdapter(recommendAdapter);
    }

    protected void onDestroy()
    {
        recommendAdapter.clearBitmaps();
        super.onDestroy();
    }

    protected void onPause()
    {
        super.onPause();
    }

    protected void onResume()
    {
        super.onResume();
    }

    private static final String TAG = CGRecommendActivity.class.getSimpleName();
    private android.widget.AdapterView.OnItemClickListener itemListener;
    private RecommendAdapter recommendAdapter;
    private CGData.CGRecommendData recommendData;
    private ListView recommendList;


}
