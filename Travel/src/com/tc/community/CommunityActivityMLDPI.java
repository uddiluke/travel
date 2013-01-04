// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.community;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tc.TCUtil;
import com.tc.cg.CGBaseActivity;

// Referenced classes of package com.tc.community:
//            CommunityUtil

public class CommunityActivityMLDPI extends CGBaseActivity {

	public CommunityActivityMLDPI() {
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f03005b);
		findViewById(0x7f0a0000).setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				Intent intent = new Intent();
				intent.putExtra("CONTENT", "");
				setResult(-1, intent);
				TCUtil.hideSoftKeyBroad(CommunityActivityMLDPI.this, reply_content);
				onBackPressed();
			}

		});
		findViewById(0x7f0a0215).setVisibility(8);
		okButton = (TextView) findViewById(0x7f0a00c4);
		okButton.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				if (reply_content.getText().length() > 140) {
					CommunityUtil.showCommunityToast(CommunityActivityMLDPI.this, "12");
				} else {
					Intent intent = new Intent();
					intent.putExtra("CONTENT", reply_content.getText().toString());
					setResult(-1, intent);
					onBackPressed();
					TCUtil.hideSoftKeyBroad(CommunityActivityMLDPI.this, reply_content);
				}
			}

		});
		okButton.setVisibility(8);
		reply_text1 = (TextView) findViewById(0x7f0a0219);
		reply_text2 = (TextView) findViewById(0x7f0a0217);
		reply_leftcount = (TextView) findViewById(0x7f0a0218);
		reply_content = (EditText) findViewById(0x7f0a021a);
		reply_content.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable editable) {
				if (editable.length() <= 140) {
					reply_text1.setTextColor(getResources().getColor(0x7f060011));
					reply_text1.setText(0x7f08015e);
					reply_text2.setTextColor(getResources().getColor(0x7f060011));
					reply_leftcount.setTextColor(getResources().getColor(0x7f060011));
					reply_leftcount.setText((new StringBuilder()).append("").append(140 - editable.length()).toString());
				} else {
					reply_text1.setTextColor(getResources().getColor(0x7f060012));
					reply_text1.setText(0x7f08015f);
					reply_text2.setTextColor(getResources().getColor(0x7f060012));
					reply_leftcount.setTextColor(getResources().getColor(0x7f060012));
					reply_leftcount.setText((new StringBuilder()).append("").append(-140 + editable.length()).toString());
				}
				if (editable.length() > 0)
					okButton.setVisibility(0);
				else
					okButton.setVisibility(8);
			}

			public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
			}

			public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
			}

		});
		reply_content.setText(getIntent().getStringExtra("CONTENT"));
	}

	public static final String CONTENT = "CONTENT";
	public static final int REQUEST_CODE = 77;
	private TextView okButton;
	private EditText reply_content;
	private TextView reply_leftcount;
	private TextView reply_text1;
	private TextView reply_text2;

}
