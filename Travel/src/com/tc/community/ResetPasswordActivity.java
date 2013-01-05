//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.community;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tc.TCUtil;
import com.tc.cg.CGBaseActivity;

// Referenced classes of package com.tc.community:
//            CommunityUtil, CommunityJSON

public class ResetPasswordActivity extends CGBaseActivity {

	public ResetPasswordActivity() {
	}

	public void onBackPressed() {
		TCUtil.hideSoftKeyBroad(this, resetEmail);
		super.onBackPressed();
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f03005c);
		findViewById(0x7f0a0000).setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				onBackPressed();
			}

		});
		((TextView) findViewById(0x7f0a0001)).setText(0x7f08016c);
		resetEmail = (EditText) findViewById(0x7f0a021b);
		findViewById(0x7f0a021c).setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				reset_email_tostring = resetEmail.getText().toString().trim();
				if (reset_email_tostring.equals(""))
					CommunityUtil.showCommunityToast(ResetPasswordActivity.this, "5");
				else if (!reset_email_tostring.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"))
					CommunityUtil.showCommunityToast(ResetPasswordActivity.this, "1");
				else
					(new AsyncTask() {

						protected Object doInBackground(Object aobj[]) {

							statusCode = CommunityJSON.resetPSW(ResetPasswordActivity.this, reset_email_tostring);
							return null;
						}

						protected void onPostExecute(Object obj) {

							if ("OK".equals(statusCode)) {
								CommunityUtil.showCommunityToast(ResetPasswordActivity.this, "8");
								onBackPressed();
							} else {
								CommunityUtil.showCommunityToast(ResetPasswordActivity.this, statusCode);
							}
						}

						String statusCode;

					}).execute(null);
			}

		});
	}

	private EditText resetEmail;
	private String reset_email_tostring;

	/*
	 * static String access$002(ResetPasswordActivity resetpasswordactivity,
	 * String s) { resetpasswordactivity.reset_email_tostring = s; return s; }
	 */

}
