//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.community;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.*;
import com.tc.TCUtil;
import com.tc.cg.CGBaseActivity;
import com.tc.cg.CGData;
import com.tc.community.TencentWeibo.OAuth;
import com.tc.community.TencentWeibo.TencentTokenActivity;
import com.tc.weibo.WeiboSettings;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;

// Referenced classes of package com.tc.community:
//            ResetPasswordActivity, CommunityUtil, CommunityJSON, RegisterActivity

public class LoginRegisterActivity extends CGBaseActivity
{

    public LoginRegisterActivity()
    {
    }

    public void finish()
    {
        TCUtil.hideSoftKeyBroad(this, login_email);
        super.finish();
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        super.onActivityResult(i, j, intent);
        if(j == 7)
            finish();
        else if(intent != null){
        	final OAuth oAuth=(OAuth)intent.getSerializableExtra("TENCENT_TOKEN_INFO");
            (new AsyncTask() {

                protected  Object doInBackground(Object aobj[])
                {
                 
                   
                    String s = oAuth.getName();
                    String s1 = oAuth.getOauth_token();
                    String s2 = oAuth.getOauth_token_secret();
                    CGData _tmp = CG_DATA;
                    statusCode = CommunityJSON.otherUserLogin(LoginRegisterActivity.this, "tencent", s, s1, s2, CGData.CG_COMMUNITY_CODE);
                    return null;
                }

                protected  void onPostExecute(Object obj)
                {
               
                    if("OK".equals(statusCode))
                        finish();
                    else
                        CommunityUtil.showCommunityToast(LoginRegisterActivity.this, statusCode);
                }

                String statusCode;
         
            }
).execute(null);
        }
        else
            login_accountSpinner.setSelection(0);
    }

    public void onBackPressed()
    {
        TCUtil.hideSoftKeyBroad(this, login_email);
        super.onBackPressed();
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f030046);
        findViewById(0x7f0a0153).setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                onBackPressed();
            }

           
        }
);
        reset_psw = (TextView)findViewById(0x7f0a01c9);
        reset_psw.getPaint().setFlags(8);
        reset_psw.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                startActivity(new Intent(LoginRegisterActivity.this, ResetPasswordActivity.class));
            }

        }
);
        login_accountSpinner = (Spinner)findViewById(0x7f0a01c6);
        String as[] = new String[3];
        as[0] = getString(0x7f08014b);
        as[1] = getString(0x7f08014c);
        as[2] = getString(0x7f08014d);
        ArrayAdapter arrayadapter = new ArrayAdapter(this, 0x1090008, as);
        arrayadapter.setDropDownViewResource(0x1090009);
        login_accountSpinner.setAdapter(arrayadapter);
        login_accountSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapterview, View view, int i, long l)
            {
                login_email.setText("");
                login_psw.setText("");
                if(i == 0) {  //goto _L2; else goto _L1
                	 reset_psw.setVisibility(0);
                }else   if(i == 1)
                    reset_psw.setVisibility(8);
                else
                if(i == 2)
                {
                    reset_psw.setVisibility(8);
                    TCUtil.hideSoftKeyBroad(LoginRegisterActivity.this, login_email);
                    if(TCUtil.isNetAvailable(LoginRegisterActivity.this))
                        startActivityForResult(new Intent(LoginRegisterActivity.this, TencentTokenActivity.class), 0);
                    else
                        TCUtil.showNetErrorDialog(LoginRegisterActivity.this);
                }
                

            }

            public void onNothingSelected(AdapterView adapterview)
            {
            }

         
        }
);
        login_email = (EditText)findViewById(0x7f0a01c7);
        login_psw = (EditText)findViewById(0x7f0a01c8);
        loginButton = (Button)findViewById(0x7f0a019e);
        loginButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                login_email_tostring = login_email.getText().toString().trim();
                login_psw_tostring = login_psw.getText().toString().trim();
                if(login_email_tostring.equals("") || login_psw_tostring.equals(""))
                    CommunityUtil.showCommunityToast(LoginRegisterActivity.this, "4");
                else
                if(!login_email_tostring.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"))
                    CommunityUtil.showCommunityToast(LoginRegisterActivity.this, "1");
                else
                    (new AsyncTask() {

                        protected  Object doInBackground(Object aobj[])
                        {
                      
                            switch(login_accountSpinner.getSelectedItemPosition()){
                            case 0:
                            	 
                                   String s3 = login_email_tostring;
                                   String s4 = login_psw_tostring;
                            
                                   statusCode = CommunityJSON.login(LoginRegisterActivity.this, s3, s4, CGData.CG_COMMUNITY_CODE);
                                   break;
                            case 1:
                                try
                                {
                                    accessToken = (new Weibo()).getXAuthAccessToken(login_email_tostring, login_psw_tostring);
                                    if(accessToken != null)
                                    {
                                      
                                        String s = (new StringBuilder()).append("").append(accessToken.getUserId()).toString();
                                        String s1 = accessToken.getToken();
                                        String s2 = accessToken.getTokenSecret();
                            
                                        statusCode = CommunityJSON.otherUserLogin(LoginRegisterActivity.this, "sina", s, s1, s2, CGData.CG_COMMUNITY_CODE);
                                    }
                                }
                                catch(WeiboException weiboexception)
                                {
                                    statusCode = "100";
                                    weiboexception.printStackTrace();
                                }
                                break;
                            }
                           return null;  
                    
                        }

                        protected  void onPostExecute(Object obj)
                        {
                       
                            if("OK".equals(statusCode))
                            {
                                if(login_accountSpinner.getSelectedItemPosition() == 1)
                                {
                                    WeiboSettings.putUserName(LoginRegisterActivity.this, accessToken.getScreenName());
                                    WeiboSettings.putPassword(LoginRegisterActivity.this, login_psw_tostring);
                                    WeiboSettings.putToken(LoginRegisterActivity.this, accessToken.getToken());
                                    WeiboSettings.putTokenSecret(LoginRegisterActivity.this, accessToken.getTokenSecret());
                                }
                                finish();
                            } else
                            {
                                CommunityUtil.showCommunityToast(LoginRegisterActivity.this, statusCode);
                            }
                        }

                        AccessToken accessToken;
                        String statusCode;
                    
                    }).execute(null);
            }

         
        }
);
        registerButton = (Button)findViewById(0x7f0a01a0);
        registerButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                TCUtil.hideSoftKeyBroad(LoginRegisterActivity.this, login_email);
                Intent intent = new Intent(LoginRegisterActivity.this, RegisterActivity.class);
                intent.putExtra("cg_id", CG_ID);
                intent.putExtra("from_other_app", FROM_OTHER_APPLICATION);
                startActivityForResult(intent, 1000);
            }

          
        }
);
    }

    private Button loginButton;
    private Spinner login_accountSpinner;
    private EditText login_email;
    private String login_email_tostring;
    private EditText login_psw;
    private String login_psw_tostring;
    private Button registerButton;
    private TextView reset_psw;

    static 
    {
        Weibo.CONSUMER_KEY = "3780182769";
        Weibo.CONSUMER_SECRET = "832c9b655611271fa6f662c0281f3493";
        System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
        System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    }






/*
    static String access$302(LoginRegisterActivity loginregisteractivity, String s)
    {
        loginregisteractivity.login_email_tostring = s;
        return s;
    }

*/



/*
    static String access$402(LoginRegisterActivity loginregisteractivity, String s)
    {
        loginregisteractivity.login_psw_tostring = s;
        return s;
    }

*/

}
