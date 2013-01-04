// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.mall;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.tc.TCHtmlActivity;
import com.tc.TCUtil;
import com.tc.sg.SGTrafficActivity;
import java.util.List;

// Referenced classes of package com.tc.mall:
//            MallData

public class MallMoreActivity extends Activity
    implements android.view.View.OnClickListener
{
    private class MoreAdapter extends BaseAdapter
    {

        private Bitmap getBitmap(Context context1, String s)
        {
            return TCUtil.getBitmap(context1, s);
        }

        public int getCount()
        {
            return mallMoreData.MORE_ITEMS.size();
        }

        public Object getItem(int i)
        {
            return mallMoreData.MORE_ITEMS.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }

        public View getView(int i, View view, ViewGroup viewgroup)
        {
            if(view == null)
                view = TCUtil.getLayoutInflater(context).inflate(0x7f03004d, null);
            MallData.MallMoreData.MoreItem moreitem = (MallData.MallMoreData.MoreItem)getItem(i);
            ((ImageView)view.findViewById(0x7f0a0066)).setImageBitmap(getBitmap(context, moreitem.icon));
            ((TextView)view.findViewById(0x7f0a0067)).setText(moreitem.name);
            ((TextView)view.findViewById(0x7f0a0068)).setText(moreitem.description);
            return view;
        }

        private Context context;
     

        public MoreAdapter(Context context1)
        {
           
            context = context1;
        }
    }


    public MallMoreActivity()
    {
        mallMoreData = MallData.MALL_MORE_DATA;
        itemListener = new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                MallData.MallMoreData.MoreItem moreitem = (MallData.MallMoreData.MoreItem)moreAdapter.getItem(i);
                Bundle bundle = new Bundle();
                if(moreitem.url.contains("trafficinfo"))
                {
                    bundle.putString("url", moreitem.url);
                    TCUtil.startActivity(MallMoreActivity.this,SGTrafficActivity.class, bundle);
                } else
                {
                    bundle.putString("title", moreitem.name);
                    bundle.putString("url", moreitem.url);
                    TCUtil.startActivity(MallMoreActivity.this, TCHtmlActivity.class, bundle);
                }
            }

        }
;
    }

    public void onClick(View view)
    {
        if(view.getId()==2131361792)
        	onBackPressed();
 
   
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f030013);
        ((TextView)findViewById(0x7f0a0001)).setText(mallMoreData.chName);
        findViewById(0x7f0a0000).setOnClickListener(this);
        moreList = (ListView)findViewById(0x7f0a0075);
        moreList.setOnItemClickListener(itemListener);
        moreAdapter = new MoreAdapter(this);
        moreList.setAdapter(moreAdapter);
    }

    protected void onDestroy()
    {
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

    private static final String TAG = MallMoreActivity.class.getSimpleName();
    private android.widget.AdapterView.OnItemClickListener itemListener;
    private MallData.MallMoreData mallMoreData;
    private MoreAdapter moreAdapter;
    private ListView moreList;



}
