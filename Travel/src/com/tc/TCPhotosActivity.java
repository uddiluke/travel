// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc;

import com.touchchina.cityguide.sz.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

// Referenced classes of package com.tc:
//            TCUtil

public class TCPhotosActivity extends Activity implements android.widget.ViewSwitcher.ViewFactory, android.widget.AdapterView.OnItemSelectedListener,
		android.view.View.OnTouchListener {
	class GestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

		public boolean onDown(MotionEvent motionevent) {
			onDown = true;
			return true;
		}

		public boolean onScroll(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1) {
			Log.i(TCPhotosActivity.TAG, "onScroll ");
			if (f > 20F) { // goto _L2; else goto _L1
				int j = 1 + gallery.getSelectedItemPosition();
				if (j > -1 + imageAdapter.getCount())
					j = -1 + imageAdapter.getCount();
				if (onDown) {
					onDown = false;
					gallery.setSelection(j);
				}
			} else if (f < -20F) {
				int i = -1 + gallery.getSelectedItemPosition();
				if (i < 0)
					i = 0;
				if (onDown) {
					onDown = false;
					gallery.setSelection(i);
				}
			}

			return false;

		}

		public boolean onSingleTapConfirmed(MotionEvent motionevent) {
			return true;
		}

		private boolean onDown;

	}

	public static class ImageAdapter extends BaseAdapter {

		public Bitmap getBitmap(Context context1, String s) {
			return TCUtil.getBitmap(context1, s);
		}

		public int getCount() {
			return TCPhotosActivity.albumData.album.size();
		}

		public Object getItem(int i) {
			return TCPhotosActivity.albumData.album.get(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			ImageView imageview;
			if (view == null) {
				imageview = new ImageView(context);
				imageview.setAdjustViewBounds(true);
				imageview.setLayoutParams(new android.widget.Gallery.LayoutParams(136, 88));
				imageview.setBackgroundResource(galleryItemBackground);
				view = imageview;
			} else {
				imageview = (ImageView) view;
			}
			imageview.setImageBitmap(TCUtil.getBitmap(context, (String) TCPhotosActivity.albumData.album.get(i)));
			return view;
		}

		private static final String TAG = ImageAdapter.class.getSimpleName();
		private Context context;
		private int galleryItemBackground;

		public ImageAdapter(Context context1) {
			context = context1;
			TypedArray typedarray = context1.obtainStyledAttributes(com.touchchina.cityguide.R.styleable.Gallery);
			galleryItemBackground = typedarray.getResourceId(0, 0);
			typedarray.recycle();
		}
	}

	public TCPhotosActivity() {
	}

	public View makeView() {
		ImageView imageview = new ImageView(this);
		imageview.setBackgroundColor(0xff000000);
		imageview.setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
		imageview.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
		return imageview;
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.tc_photos);
		backButton = (ImageView) findViewById(0x7f0a0000);
		backButton.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				onBackPressed();
			}

		});
		albumData = (TCData.TCAlbumData) getIntent().getExtras().getSerializable("album_data");
		titleText = (TextView) findViewById(0x7f0a0001);
		titleText.setText(albumData.name);
		switcher = (ImageSwitcher) findViewById(0x7f0a0262);
		switcher.setFactory(this);
		switcher.setInAnimation(AnimationUtils.loadAnimation(this, 0x10a0000));
		switcher.setOutAnimation(AnimationUtils.loadAnimation(this, 0x10a0001));
		switcher.setOnTouchListener(this);
		switcher.setLongClickable(true);
		gestureDetector = new GestureDetector(this, new GestureListener());
		gallery = (Gallery) findViewById(0x7f0a0261);
		imageAdapter = new ImageAdapter(getApplicationContext());
		gallery.setAdapter(imageAdapter);
		gallery.setOnItemSelectedListener(this);
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	public void onItemSelected(AdapterView adapterview, View view, int i, long l) {
		String s = (String) imageAdapter.getItem(i);
		BitmapDrawable bitmapdrawable = new BitmapDrawable(imageAdapter.getBitmap(this, s));
		switcher.setImageDrawable(bitmapdrawable);
	}

	public void onNothingSelected(AdapterView adapterview) {
	}

	public boolean onTouch(View view, MotionEvent motionevent) {
		return gestureDetector.onTouchEvent(motionevent);
	}

	public static final String KEY_ALBUM_DATA = "album_data";
	private static final String TAG = TCPhotosActivity.class.getSimpleName();
	private static TCData.TCAlbumData albumData;
	private ImageView backButton;
	private Gallery gallery;
	private GestureDetector gestureDetector;
	private ImageAdapter imageAdapter;
	private ImageSwitcher switcher;
	private TextView titleText;

}
