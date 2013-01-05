//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import java.util.ArrayList;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGGalleryActivity

public class CGIntroduceActivity extends CGBaseActivity implements android.view.View.OnTouchListener {
	class GestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

		public boolean onDown(MotionEvent motionevent) {
			return true;
		}

		public boolean onScroll(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1) {
			if (f > 20F && flag == 2) {
				if (!isIn) {
					isIn = true;
					startActivity(CGIntroduceActivity.this, CGGalleryActivity.class);
					finish();
				}
			} else if (f >= -20F)
				;
			return false;
		}

		public boolean onSingleTapConfirmed(MotionEvent motionevent) {
			return true;
		}

		boolean isIn;

	}

	public CGIntroduceActivity() {
	}

	public void onBackPressed() {
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030010);
		list = new ArrayList();
		ImageView imageview = new ImageView(this);
		imageview.setImageResource(0x7f0200a6);
		ImageView imageview1 = new ImageView(this);
		imageview1.setImageResource(0x7f0200a7);
		ImageView imageview2 = new ImageView(this);
		imageview2.setImageResource(0x7f0200a8);
		list.add(imageview);
		list.add(imageview1);
		list.add(imageview2);
		gestureDetector = new GestureDetector(this, new GestureListener());
		viewPager = (ViewPager) findViewById(0x7f0a0069);
		viewPager.setOnTouchListener(this);
		PagerAdapter pageradapter = new PagerAdapter() {

			public void destroyItem(View view, int i, Object obj) {
				((ViewPager) view).removeView((View) list.get(i));
				Log.e("TAG", (new StringBuilder()).append("destroyItem-----").append(i).toString());
			}

			public void finishUpdate(View view) {
			}

			public int getCount() {
				return list.size();
			}

			public Object instantiateItem(View view, int i) {
				((ViewPager) view).addView((View) list.get(i), 0);
				return list.get(i);
			}

			public boolean isViewFromObject(View view, Object obj) {
				boolean flag1;
				if (view == obj)
					flag1 = true;
				else
					flag1 = false;
				return flag1;
			}

			public void restoreState(Parcelable parcelable, ClassLoader classloader) {
			}

			public Parcelable saveState() {
				return null;
			}

			public void startUpdate(View view) {
			}

		};
		viewPager.setAdapter(pageradapter);
		viewPager.setOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener() {

			public void onPageScrollStateChanged(int i) {
				Log.e("TAG", (new StringBuilder()).append("onPageScrollStateChanged..............").append(i).toString());
			}

			public void onPageScrolled(int i, float f, int j) {
				flag = i;
			}

			public void onPageSelected(int i) {
				Log.e("TAG", (new StringBuilder()).append("onPageSelected..............").append(i).toString());
			}

		});
	}

	public boolean onTouch(View view, MotionEvent motionevent) {
		return gestureDetector.onTouchEvent(motionevent);
	}

	private int flag;
	private GestureDetector gestureDetector;
	private ArrayList list;
	private ViewPager viewPager;

	/*
	 * static int access$102(CGIntroduceActivity cgintroduceactivity, int i) {
	 * cgintroduceactivity.flag = i; return i; }
	 */
}
