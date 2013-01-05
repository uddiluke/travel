//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.mall;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.tc.TCData;
import com.tc.TCUtil;
import com.tc.weibo.WeiboActivity;

// Referenced classes of package com.tc.mall:
//            MallPOIDataActivity, MallMoreActivity, MallFavoriteActivity, MallData, 
//            MallPOIActivity

public class MallGuideActivity extends Activity implements android.view.View.OnClickListener {

	public MallGuideActivity() {
		searchAdapter = new BaseAdapter() {

			public int getCount() {
				return searchPOIItems.size();
			}

			public Object getItem(int i) {
				return searchPOIItems.get(i);
			}

			public long getItemId(int i) {
				return (long) i;
			}

			public View getView(int i, View view, ViewGroup viewgroup) {
				if (view == null)
					view = TCUtil.getLayoutInflater(MallGuideActivity.this).inflate(0x7f03002c, null);
				MallData.MallPOIData.POI poi = (MallData.MallPOIData.POI) getItem(i);
				ImageView imageview = (ImageView) view.findViewById(0x7f0a0066);
				android.graphics.Bitmap bitmap = TCUtil.getBitmap(MallGuideActivity.this, poi.listIcon);
				android.graphics.Bitmap bitmap1 = TCUtil.getRoundedCornerBitmap(bitmap);
				TCUtil.recycleBitmap(bitmap);
				imageview.setImageBitmap(bitmap1);
				((TextView) view.findViewById(0x7f0a0067)).setText(poi.name);
				view.findViewById(0x7f0a0068).setVisibility(8);
				return view;
			}

		};
	}

	private TableRow addChild(TableRow tablerow, int i, String s, String s1, int j, String s2) {
		if (i % 4 == 0) {
			int k = i / 4;
			tablerow = new TableRow(this);
			tablerow.setLayoutParams(new android.widget.TableRow.LayoutParams(-1, -2));
			tablerow.setId(k);
			tableLayout.addView(tablerow, k);
		}
		RelativeLayout relativelayout = (RelativeLayout) TCUtil.getLayoutInflater(this).inflate(0x7f03004a, null);
		ImageButton imagebutton = (ImageButton) relativelayout.findViewById(0x7f0a01ce);
		TextView textview = (TextView) relativelayout.findViewById(0x7f0a01cf);
		imagebutton.setBackgroundDrawable(TCUtil.getDrawable1(this, s2));
		textview.setText(s1);
		imagebutton.setTag((new StringBuilder()).append(s).append(":").append(s1).append(":").append(j).toString());
		imagebutton.setOnClickListener(this);
		tablerow.addView(relativelayout);
		return tablerow;
	}

	private Dialog createWeiboDialog() {
		return TCUtil.createPositiveDialog(this, getString(0x7f080035), new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialoginterface, int i) {
				Bundle bundle = new Bundle();
				bundle.putBoolean("has_camera", true);
				bundle.putString("status", "");
				TCUtil.startActivity(MallGuideActivity.this, WeiboActivity.class, bundle);
			}

		});
	}

	private void showSearchDialog(Activity activity) {
		final Dialog dialog = new Dialog(activity, 0x7f090003);
		dialog.setContentView(0x7f03002b);
		android.view.WindowManager.LayoutParams layoutparams = dialog.getWindow().getAttributes();
		Rect rect = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		int i = rect.top;
		layoutparams.width = TCData.SCREEN_WIDTH;
		layoutparams.height = TCData.SCREEN_HEIGHT - i;
		dialog.getWindow().setAttributes(layoutparams);
		dialog.onWindowAttributesChanged(layoutparams);
		final EditText searchEdit = (EditText) dialog.findViewById(0x7f0a00df);
		String s = searchEdit.getText().toString();
		searchEdit.setText(s);
		searchEdit.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable editable) {
				searchPOIItems = new ArrayList();
				Iterator iterator1 = srcSearchPOIItems.iterator();
				do {
					if (!iterator1.hasNext())
						break;
					MallData.MallPOIData.POI poi1 = (MallData.MallPOIData.POI) iterator1.next();
					if (poi1.name.toLowerCase().contains(editable.toString().toLowerCase()))
						searchPOIItems.add(poi1);
				} while (true);
				searchAdapter.notifyDataSetChanged();
			}

			public void beforeTextChanged(CharSequence charsequence, int j, int k, int l) {
			}

			public void onTextChanged(CharSequence charsequence, int j, int k, int l) {
			}

		});
		dialog.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

			public void onCancel(DialogInterface dialoginterface) {
				searchEdit.setText(searchEdit.getText());
				searchButton.setImageResource(0x7f020092);
			}

		});
		dialog.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {

			public void onDismiss(DialogInterface dialoginterface) {
				searchEdit.setText(searchEdit.getText());
			}

		});
		ListView listview = (ListView) dialog.findViewById(0x7f0a00f9);
		listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView adapterview, View view, int j, long l) {
				MallData.MallPOIData.POI poi1 = (MallData.MallPOIData.POI) searchAdapter.getItem(j);
				Bundle bundle = new Bundle();
				bundle.putInt("poi_id", poi1.id);
				bundle.putInt("poi_type", poi1.typeId);
				TCUtil.startActivity(MallGuideActivity.this, MallPOIActivity.class, bundle);
				searchButton.setImageResource(0x7f020092);
				dialog.dismiss();
			}

		});
		searchPOIItems = new ArrayList();
		Iterator iterator = srcSearchPOIItems.iterator();
		do {
			if (!iterator.hasNext())
				break;
			MallData.MallPOIData.POI poi = (MallData.MallPOIData.POI) iterator.next();
			if (poi.name.toLowerCase().contains(s.toLowerCase()))
				searchPOIItems.add(poi);
		} while (true);
		listview.setAdapter(searchAdapter);
		dialog.show();
	}

	protected void onActivityResult(int i, int j, Intent intent) {
		if (j == -1 && i == 1000)
			showDialog(1000);
		super.onActivityResult(i, j, intent);
	}

	public void onBackPressed() {
		super.onBackPressed();
	}

	public void onClick(View view) {
		String s = (String) view.getTag();
		switch (view.getId()) {
		case 2131361792:
			onBackPressed();
			break;
		case 2131361905:
			searchButton.setImageResource(0x7f020093);
			showSearchDialog(this);
			break;
		case 2131362014:
			showSearchDialog(this);
			break;
		case 2131362015:
			showSearchDialog(this);
			break;
		case 2131362251:
			TCUtil.startCameraForResult(this, 1000);
			break;
		default:
			Bundle bundle;
			String as[];
			bundle = new Bundle();
			as = s.split(":");
			if ("poi".equals(as[0])) {
				bundle.putString(MallPOIDataActivity.KEY_MALL_POI_NAME, as[1]);
				bundle.putInt(MallPOIDataActivity.KEY_MALL_POI_TYPE_ID, Integer.valueOf(as[2]).intValue());
				TCUtil.startActivity(this, MallPOIDataActivity.class, bundle);
			} else {

				if ("more".equals(as[0]))
					TCUtil.startActivity(this, MallMoreActivity.class, bundle);
				else if ("favorite".equals(as[0]))
					TCUtil.startActivity(this, MallFavoriteActivity.class, bundle);
			}
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030049);
		((TextView) findViewById(0x7f0a0001)).setText(MallData.MALL_NAME);
		findViewById(0x7f0a0000).setOnClickListener(this);
		tableLayout = (TableLayout) findViewById(0x7f0a01cd);
		TableRow tablerow = null;
		int i;
		for (i = 0; i < MallData.MALL_TYPE_DATA.TYPES.size(); i++) {
			MallData.MallTypeData.Type type = (MallData.MallTypeData.Type) MallData.MALL_TYPE_DATA.TYPES.get(i);
			tablerow = addChild(tablerow, i, "poi", type.name, type.id, type.listIcon);
		}

		int j = i + 1;
		TableRow tablerow1 = addChild(tablerow, i, "favorite", getString(0x7f080031), 0, MallData.getTypeIcon("favor"));
		int _tmp = j + 1;
		addChild(tablerow1, j, "more", getString(0x7f080034), 0, MallData.getTypeIcon("more"));
		searchButton = (ImageView) findViewById(0x7f0a0071);
		searchButton.setImageResource(0x7f020092);
		searchButton.setTag("");
		searchButton.setOnClickListener(this);
		cameraButton = (ImageView) findViewById(0x7f0a01cb);
		cameraButton.setOnClickListener(this);
		srcSearchPOIItems = new ArrayList();
		for (Iterator iterator = MallData.MALL_POI_DATAS.iterator(); iterator.hasNext();) {
			Iterator iterator1 = ((MallData.MallPOIData) iterator.next()).POIS.iterator();
			while (iterator1.hasNext()) {
				MallData.MallPOIData.POI poi = (MallData.MallPOIData.POI) iterator1.next();
				srcSearchPOIItems.add(poi);
			}
		}

	}

	protected Dialog onCreateDialog(int i) {
		Dialog dialog = null;
		if (i == 1000) {
			dialog = createWeiboDialog();
		}
		return dialog;

	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

	private static final String TAG = MallGuideActivity.class.getSimpleName();
	private static final int TAKE_CAMERA_REQUEST_CODE = 1000;
	private ImageView cameraButton;
	private BaseAdapter searchAdapter;
	private ImageView searchButton;
	List searchPOIItems;
	List srcSearchPOIItems;
	private TableLayout tableLayout;

}
