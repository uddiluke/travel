//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tc.TCUtil;
import com.tc.logic.CGCommentData;
import com.touchchina.cityguide.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData

public class CGCommentActivity extends CGBaseActivity implements android.view.View.OnClickListener {
	private class CGCommentDataAsyncTask extends AsyncTask {

		protected CGCommentData doInBackground(Object aobj[]) {
			return CGCommentData.cgComment(CGCommentActivity.this, CG_DATA.CG_APPLICATION, ((String) aobj[0]).toLowerCase(),
					((Integer) aobj[1]).intValue(), ((Float) aobj[2]).intValue(), "");
		}

		protected void onPostExecute(CGCommentData cgcommentdata) {
			super.onPostExecute(cgcommentdata);
			progressBar.setVisibility(8);
			tipTitleText.setVisibility(0);
			if (cgcommentdata != null && cgcommentdata.status.equals("OK")) {
				sgItem.commentCount = cgcommentdata.markCount;
				sgItem.commentGrade = cgcommentdata.avgerageMark;
				tipTitleText.setText(getString(0x7f080049));
				commentText.setText((new StringBuilder()).append(sgItem.commentCount).append(getString(0x7f08002a)).toString());
				ratingBar.setRating(sgItem.commentGrade / 2.0F);
				sendButton.setVisibility(4);
			} else {
				tipTitleText.setText(getString(0x7f08004a));
			}
		}

		protected void onPostExecute(Object obj) {
			onPostExecute((CGCommentData) obj);
		}

		protected void onPreExecute() {
			super.onPreExecute();
			tipTitleText.setVisibility(8);
			progressBar.setVisibility(0);
		}

		protected void onProgressUpdate(Object aobj[]) {
			onProgressUpdate((String[]) aobj);
		}

		protected void onProgressUpdate(String as[]) {
			super.onProgressUpdate(as);
		}

	}

	public CGCommentActivity() {
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backButton:
			onBackPressed();
			break;
		case R.id.titleText:
			break;
		case R.id.sendButton:
			CGCommentDataAsyncTask cgcommentdataasynctask = new CGCommentDataAsyncTask();
			Object aobj[] = new Object[3];
			aobj[0] = sgItem.type;
			aobj[1] = Integer.valueOf(sgItem.id);
			aobj[2] = Float.valueOf(2.0F * ratingBar.getRating());
			cgcommentdataasynctask.execute(aobj);
			break;
		default:
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.cg_comment);
		Bundle bundle1 = getIntent().getExtras();
		sgType = bundle1.getString("sg_type");
		sgItem = CGData.getSGItem(CG_DATA, sgType, bundle1.getInt("sg_id"));
		ratingBar = (RatingBar) findViewById(0x7f0a001e);
		tipTitleText = (TextView) findViewById(0x7f0a001a);
		commentText = (TextView) findViewById(0x7f0a001c);
		backButton = (ImageView) findViewById(0x7f0a0000);
		backButton.setOnClickListener(this);
		sendButton = (ImageView) findViewById(0x7f0a0002);
		sendButton.setOnClickListener(this);
		if (sgItem.name != null) {
			String s = sgItem.name;
			if (s.contains("\uFF08"))
				s = s.substring(0, s.indexOf("\uFF08"));
			if (s.contains("("))
				s = s.substring(0, s.indexOf("("));
			if (s.contains(" "))
				s = s.substring(0, s.indexOf(" "));
			if (s.length() > 10)
				s = (new StringBuilder()).append(s.substring(0, 9)).append("\r\n").append(s.substring(9)).toString();
			tipTitleText.setText((new StringBuilder()).append(getString(0x7f080029)).append("\"").append(s).append("\"")
					.append(getString(0x7f080028)).toString());
		}
		commentText.setText((new StringBuilder()).append(sgItem.commentCount).append(getString(0x7f08002a)).toString());
		ratingBar.setRating(sgItem.commentGrade / 2.0F);
		progressBar = (ProgressBar) findViewById(0x7f0a001b);
		progressBar.setVisibility(8);
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onResume() {
		super.onResume();
		if (!TCUtil.isNetAvailable(this))
			TCUtil.showNetErrorDialog(this);
	}

	public static final String KEY_SG_ID = "sg_id";
	public static final String KEY_SG_TYPE = "sg_type";
	private static final String TAG = CGCommentActivity.class.getSimpleName();
	private ImageView backButton;
	private TextView commentText;
	private ProgressBar progressBar;
	private RatingBar ratingBar;
	private ImageView sendButton;
	private CGData.CGSGData.SGItem sgItem;
	private String sgType;
	private TextView tipTitleText;

}
