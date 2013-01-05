//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.tc.TCUtil;
import com.tc.community.CommunityUtil;
import com.tc.net.NetUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData

public class CGFeedBackActivity extends CGBaseActivity implements android.view.View.OnClickListener {

	public CGFeedBackActivity() {
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case 0x7F0A0000:
			TCUtil.hideSoftKeyBroads(this);
			super.onBackPressed();
			break;
		case 0x7F0A0041:
			if ("".equals(feedBackEmail.getText().toString().trim()) && !"".equals(feedBackSuggest.getText().toString().trim())) {

				sendEmail(CGData.CG_COMMUNITY_CODE, feedBackEmail.getText().toString().trim(), feedBackSuggest.getText().toString().trim());
			} else if (!"".equals(feedBackEmail.getText().toString().trim())
					&& feedBackEmail.getText().toString().trim().matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")
					&& !"".equals(feedBackSuggest.getText().toString().trim())) {

				sendEmail(CGData.CG_COMMUNITY_CODE, feedBackEmail.getText().toString().trim(), feedBackSuggest.getText().toString().trim());
			} else if (!feedBackEmail.getText().toString().trim().matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")
					&& !"".equals(feedBackEmail.getText().toString().trim()))
				Toast.makeText(this, getString(0x7f0800be), 1).show();
			else if ("".equals(feedBackSuggest.getText().toString().trim()))
				Toast.makeText(this, getString(0x7f0800bf), 1).show();
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030007);
		titleText = (TextView) findViewById(0x7f0a0001);
		titleText.setText(getIntent().getExtras().getString(FEED_BACK_NAME));
		feedBackSend = (ImageView) findViewById(0x7f0a0041);
		feedBackSend.setOnClickListener(this);
		backButton = (ImageView) findViewById(0x7f0a0000);
		backButton.setOnClickListener(this);
		feedBackEmail = (EditText) findViewById(0x7f0a0042);
		feedBackSuggest = (EditText) findViewById(0x7f0a0043);
		getWindow().setSoftInputMode(4);
	}

	public void sendEmail(final String appname, final String email, final String issue) {
		(new AsyncTask() {

			protected Object doInBackground(Object[] aobj) {
				return doInBackground((String[]) aobj);
			}

			protected String doInBackground(String as[]) {
				JSONObject jsonobject = null;
				HttpClient httpclient;
				HttpPost httppost;

				try {
					httpclient = NetUtil.getClient();
					httppost = new HttpPost("https://service.itouchchina.com/commentsvcs/issues/");
					ArrayList arraylist = new ArrayList();
					arraylist.add(new BasicNameValuePair("appname", appname));
					arraylist.add(new BasicNameValuePair("email", email));
					arraylist.add(new BasicNameValuePair("issue", issue));

					httppost.setEntity(new UrlEncodedFormEntity(arraylist, "UTF-8"));
					HttpResponse httpresponse = httpclient.execute(httppost);
					if (httpresponse.getStatusLine().getStatusCode() != 200) {
						httppost.abort();
						return null;
					}

					String s = EntityUtils.toString(httpresponse.getEntity());
					Log.v("", s);
					jsonobject = new JSONObject(s);

					status = jsonobject.getJSONObject("issues").getString("status");
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				return status;

			}

			protected void onPostExecute(Object obj) {
				onPostExecute((String) obj);
			}

			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				alertDialog.dismiss();
				if ("OK".equals(s)) {
					CommunityUtil.showCommunityToast(CGFeedBackActivity.this, "24");
					TCUtil.hideSoftKeyBroads(CGFeedBackActivity.this);
					finish();
				} else {
					CommunityUtil.showCommunityToast(CGFeedBackActivity.this, "25");
					TCUtil.hideSoftKeyBroads(CGFeedBackActivity.this);
				}
			}

			protected void onPreExecute() {
				super.onPreExecute();
				alertDialog = new ProgressDialog(CGFeedBackActivity.this);
				alertDialog.setMessage(getString(0x7f0801b2));
				alertDialog.show();
				alertDialog.setCancelable(true);
			}

			ProgressDialog alertDialog;
			String status;

		}).execute(new String[] { "" });
	}

	public static String FEED_BACK_NAME = "feedbackName";
	private ImageView backButton;
	private EditText feedBackEmail;
	private ImageView feedBackSend;
	private EditText feedBackSuggest;
	private TextView titleText;

}
