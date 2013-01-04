// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.community;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.tc.TCHtmlActivity;
import com.tc.TCUtil;
import com.tc.cg.CGBaseActivity;
import com.tc.cg.CGData;

// Referenced classes of package com.tc.community:
//            CommunityUtil, CommunityJSON

public class RegisterActivity extends CGBaseActivity
{

    public RegisterActivity()
    {
    }

    public void finish()
    {
        super.finish();
        TCUtil.hideSoftKeyBroad(this, nickName);
    }

    public void onBackPressed()
    {
        super.onBackPressed();
        TCUtil.hideSoftKeyBroad(this, nickName);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f03005a);
        findViewById(0x7f0a0153).setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                onBackPressed();
            }

          
        }
);
        nickName = (EditText)findViewById(0x7f0a0004);
        login_email = (EditText)findViewById(0x7f0a01c7);
        login_psw = (EditText)findViewById(0x7f0a01c8);
        makesure_psw = (EditText)findViewById(0x7f0a0210);
        confirmButton = (Button)findViewById(0x7f0a0211);
        confirmButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                nickName_tostring = nickName.getText().toString().trim();
                login_email_tostring = login_email.getText().toString().trim();
                login_psw_tostring = login_psw.getText().toString().trim();
                makesure_psw_tostring = makesure_psw.getText().toString().trim();
                if(nickName_tostring.equals("") || login_email_tostring.equals("") || login_psw_tostring.equals(""))
                    CommunityUtil.showCommunityToast(RegisterActivity.this, "0");
                else
                if(!login_email_tostring.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"))
                    CommunityUtil.showCommunityToast(RegisterActivity.this, "1");
                else
                if(!nickName_tostring.matches("^[a-zA-Z0-9_\u4E00-\u9FA5]+$"))
                    CommunityUtil.showCommunityToast(RegisterActivity.this, "26");
                else
                if(nickName_tostring.getBytes().length <= 6)
                    CommunityUtil.showCommunityToast(RegisterActivity.this, "18");
                else
                if(nickName_tostring.length() > 12)
                    CommunityUtil.showCommunityToast(RegisterActivity.this, "19");
                else
                if(login_psw_tostring.length() > 15 || login_psw_tostring.length() < 6)
                    CommunityUtil.showCommunityToast(RegisterActivity.this, "20");
                else
                if(!login_psw_tostring.equals(makesure_psw_tostring))
                    CommunityUtil.showCommunityToast(RegisterActivity.this, "21");
                else
                    (new AsyncTask() {

                        protected  Object doInBackground(Object aobj[])
                        {
                      
                      
                            String s = nickName_tostring;
                            String s1 = login_email_tostring;
                            String s2 = login_psw_tostring;
                
                            statusCode = CommunityJSON.register(RegisterActivity.this, s, s1, s2, CGData.CG_COMMUNITY_CODE);
                            return null;
                        }

                        protected  void onPostExecute(Object obj)
                        {
                        
                            if("OK".equals(statusCode))
                            {
                                CommunityUtil.setLoginType(RegisterActivity.this, "touchchina");
                                setResult(7);
                                finish();
                                CommunityUtil.showCommunityToast(RegisterActivity.this, "23");
                            } else
                            {
                                CommunityUtil.showCommunityToast(RegisterActivity.this, statusCode);
                            }
                        }

                        String statusCode;
                    
                    }
).execute(null);
            }

         
        }
);
        YinSiZhengCe = (TextView)findViewById(0x7f0a0212);
        YinSiZhengCe.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent(RegisterActivity.this, TCHtmlActivity.class);
                intent.putExtra("title", YinSiZhengCe.getText());
                intent.putExtra("url", "http://www.itouchchina.com/privatepolicy.html");
                startActivity(intent);
            }

          
        }
);
        ShiYongTiaoKuan = (TextView)findViewById(0x7f0a0213);
        ShiYongTiaoKuan.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent(RegisterActivity.this, TCHtmlActivity.class);
                intent.putExtra("title", ShiYongTiaoKuan.getText());
                intent.putExtra("url", "http://www.itouchchina.com/privatepolicy.html");
                startActivity(intent);
            }

        }
);
    }

    public static final String PRIVATE_POLICY = "http://www.itouchchina.com/privatepolicy.html";
    public static final int REGISTER_SUCCESS = 7;
    private TextView ShiYongTiaoKuan;
    private TextView YinSiZhengCe;
    private Button confirmButton;
    private EditText login_email;
    private String login_email_tostring;
    private EditText login_psw;
    private String login_psw_tostring;
    private EditText makesure_psw;
    private String makesure_psw_tostring;
    private EditText nickName;
    private String nickName_tostring;



/*
    static String access$002(RegisterActivity registeractivity, String s)
    {
        registeractivity.nickName_tostring = s;
        return s;
    }

*/




/*
    static String access$202(RegisterActivity registeractivity, String s)
    {
        registeractivity.login_email_tostring = s;
        return s;
    }

*/




/*
    static String access$402(RegisterActivity registeractivity, String s)
    {
        registeractivity.login_psw_tostring = s;
        return s;
    }

*/




/*
    static String access$602(RegisterActivity registeractivity, String s)
    {
        registeractivity.makesure_psw_tostring = s;
        return s;
    }

*/



}
