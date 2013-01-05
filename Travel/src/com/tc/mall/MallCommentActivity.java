//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.mall;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tc.TCUtil;
import com.tc.logic.CGCommentData;

// Referenced classes of package com.tc.mall:
//            MallData

public class MallCommentActivity extends Activity implements android.view.View.OnClickListener {
	private class CGCommentDataAsyncTask extends AsyncTask {

		protected CGCommentData doInBackground(Object aobj[]) {
			return CGCommentData.cgComment(MallCommentActivity.this, MallData.MALL_APPLICATION, ((String) aobj[0]).toLowerCase(),
					((Integer) aobj[1]).intValue(), ((Float) aobj[2]).intValue(), "");
		}

		protected void onPostExecute(CGCommentData cgcommentdata) {
			super.onPostExecute(cgcommentdata);
			progressBar.setVisibility(8);
			tipTitleText.setVisibility(0);
			if (cgcommentdata != null && cgcommentdata.status.equals("OK")) {
				poi.commentCount = cgcommentdata.markCount;
				poi.commentGrade = cgcommentdata.avgerageMark;
				tipTitleText.setText(getString(0x7f080049));
				commentText.setText((new StringBuilder()).append(poi.commentCount).append(getString(0x7f08002a)).toString());
				ratingBar.setRating(poi.commentGrade / 2.0F);
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

	public MallCommentActivity() {
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case 2131361792:
			onBackPressed();
			break;
		case 2131361794:
			CGCommentDataAsyncTask cgcommentdataasynctask = new CGCommentDataAsyncTask();
			Object aobj[] = new Object[3];
			aobj[0] = "mallshop";
			aobj[1] = Integer.valueOf(poi.id);
			aobj[2] = Float.valueOf(2.0F * ratingBar.getRating());
			cgcommentdataasynctask.execute(aobj);
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030047);
		Bundle bundle1 = getIntent().getExtras();
		poi = MallData.getPOI(bundle1.getInt("poi_type"), bundle1.getInt("poi_id"));
		ratingBar = (RatingBar) findViewById(0x7f0a001e);
		tipTitleText = (TextView) findViewById(0x7f0a001a);
		commentText = (TextView) findViewById(0x7f0a001c);
		backButton = (ImageView) findViewById(0x7f0a0000);
		backButton.setOnClickListener(this);
		sendButton = (ImageView) findViewById(0x7f0a0002);
		sendButton.setOnClickListener(this);
		if (poi.name != null) {
			String s = poi.name;
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
		commentText.setText((new StringBuilder()).append(poi.commentCount).append(getString(0x7f08002a)).toString());
		ratingBar.setRating(poi.commentGrade / 2.0F);
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

	public static final String KEY_POI_ID = "poi_id";
	public static final String KEY_POI_TYPE = "poi_type";
	private static final String TAG = MallCommentActivity.class.getSimpleName();
	private ImageView backButton;
	private TextView commentText;
	private MallData.MallPOIData.POI poi;
	private ProgressBar progressBar;
	private RatingBar ratingBar;
	private ImageView sendButton;
	private TextView tipTitleText;

}
