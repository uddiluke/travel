//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc;

import com.touchchina.cityguide.sz.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

// Referenced classes of package com.tc:
//            TCUtil

public class TCTitleImageActivity extends Activity
{

    public TCTitleImageActivity()
    {
    }

    public void onBackPressed()
    {
        super.onBackPressed();
    }

    public void onCreate(Bundle bundle)
    {
        Intent intent;
        super.onCreate(bundle);
        setContentView(R.layout.tc_title_image);
        backButton = (ImageView)findViewById(0x7f0a0000);
        backButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view1)
            {
                onBackPressed();
            }

        }
);
        intent = getIntent();
        if(intent != null) {  //goto _L2; else goto _L1
        	 View view;
             String s;
             int i;
             Bundle bundle1 = intent.getExtras();
             TextView textview = (TextView)findViewById(0x7f0a0001);
             view = findViewById(0x7f0a0267);
             textview.setText(bundle1.getString("TITLE"));
             s = bundle1.getString("IMAGE_PATH");
             i = bundle1.getInt("IMAGE_ID", -1);
             if(s == null || s.length() <= 0){
            	 if(i != -1)
                     view.setBackgroundResource(i);
             }else{
            	 view.setBackgroundDrawable(TCUtil.getDrawable(this, s));
             }
        }

    }

    protected void onResume()
    {
        super.onResume();
    }

    public static final String KEY_IMAGE_ID = "IMAGE_ID";
    public static final String KEY_IMAGE_PATH = "IMAGE_PATH";
    public static final String KEY_TITLE = "TITLE";
    private ImageView backButton;
}
