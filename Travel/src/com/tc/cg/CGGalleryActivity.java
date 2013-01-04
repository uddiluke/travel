// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.cg;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Iterator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.flurry.android.FlurryAgent;
import com.tc.TCData;
import com.tc.TCHtmlActivity;
import com.tc.TCUtil;
import com.tc.logic.CGGalleryData;
import com.touchchina.cityguide.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGSiteActivity, CGData, CGMainActivity, 
//            CGSiteItemActivity, CGInfoActivity

public class CGGalleryActivity extends CGBaseActivity implements android.view.View.OnTouchListener {
	private class CGGalleryDataAsyncTask extends AsyncTask {

		protected Integer doInBackground(com.tc.logic.CGGalleryData.CGGalleryXmlData.ImageItem[] aimageitem) {
			int i = 0;
			while (true) {
				if (i >= 6) {
					for (CGData.InfoData.InfoItem infoitem : infoData.infoItems) {
						TCUtil.deleteFile((new StringBuilder()).append(newCGPath).append("gallery/").append(infoitem.id).toString());
					}
					(new File((new StringBuilder()).append(newCGPath).append("gallery/gallerytemp").toString())).renameTo(new File(
							(new StringBuilder()).append(newCGPath).append("gallery/gallery.xml").toString()));

					return Integer.valueOf(0);
				}
				if (isCancelled()) {
					return Integer.valueOf(1);
				}

				com.tc.logic.CGGalleryData.CGGalleryXmlData.ImageItem imageitem = aimageitem[i];
				CGData.InfoData.InfoItem infoitem1 = getInfoItem(imageitem.id);

				if (infoitem1 != null && !forceUpdate && infoitem1.modified >= imageitem.modified && infoitem1.index == i) {

				} else {
					String as[] = new String[1];
					as[0] = (new StringBuilder()).append("start:").append(i).toString();
					publishProgress(as);
					InputStream inputstream = CGGalleryData.cgGalleryImage(CGGalleryActivity.this, CG_DATA.CG_APPLICATION, CG_DATA.CG_ID, imageitem);
					long l = 0L;
					if (inputstream != null) {
						String s = (new StringBuilder()).append(newCGPath).append(imageitem.image).toString();
						String s1 = (new StringBuilder()).append(s).append("_temp").toString();

						String as1[];
						try {
							RandomAccessFile randomaccessfile = new RandomAccessFile(s1, "rwd");
							byte abyte0[] = new byte[0x100000];
							do {
								if (inputstream == null)
									break;
								int j = inputstream.read(abyte0, 0, 0x100000);
								if (j == -1)
									break;
								randomaccessfile.write(abyte0, 0, j);
								l += j;
							} while (true);
							randomaccessfile.close();
						} catch (Exception exception) {
						}
						if (l == imageitem.size) {
							TCUtil.deleteFile(s);
							File file = new File(s1);
							File file1 = new File(s);
							file.renameTo(file1);
							((CGData.InfoData.InfoItem) infoData.infoItems.get(i)).modified = imageitem.modified;
							((CGData.InfoData.InfoItem) infoData.infoItems.get(i)).content = imageitem.content;
							((CGData.InfoData.InfoItem) infoData.infoItems.get(i)).url = imageitem.url;
							((CGData.InfoData.InfoItem) infoData.infoItems.get(i)).imageUrl = (new StringBuilder()).append(newCGPath)
									.append(imageitem.image).toString();
							saveImageItem(imageitem);
						} else {
							TCUtil.deleteFile(s1);
						}
					}
				}
				String[] as1 = new String[1];
				as1[0] = (new StringBuilder()).append("end:").append(i).toString();
				publishProgress(as1);
				i++;
			}
		}

		protected Object doInBackground(Object aobj[]) {
			return doInBackground((com.tc.logic.CGGalleryData.CGGalleryXmlData.ImageItem[]) aobj);
		}

		protected void onCancelled() {
			super.onCancelled();
		}

		protected void onPostExecute(Integer integer) {
			super.onPostExecute(integer);
			Log.i(CGGalleryActivity.TAG, (new StringBuilder()).append("rest = ").append(integer.intValue()).toString());
			updateButton.setVisibility(0);
			updateProgress.setVisibility(8);
		}

		protected void onPostExecute(Object obj) {
			onPostExecute((Integer) obj);
		}

		protected void onProgressUpdate(Object aobj[]) {
			onProgressUpdate((String[]) aobj);
		}

		protected void onProgressUpdate(String as[]) {
			String as1[];
			super.onProgressUpdate(as);
			as1 = as[0].split(":");
			if (as1.length != 2)
				return;
			int j;
			RelativeLayout relativelayout;
			j = Integer.parseInt(as1[1]);
			int k = j / 3;
			int l = j % 3;
			relativelayout = (RelativeLayout) ((TableRow) galleryLayout.getChildAt(k)).getChildAt(l);
			if ("start".equals(as1[0])) {
				// goto _L4; else goto _L3
				relativelayout.getChildAt(1).setVisibility(0);
			} else {
				if ("end".equals(as1[0])) {

					relativelayout.getChildAt(1).setVisibility(8);
					Bitmap bitmap = TCUtil.getBitmap(CGGalleryActivity.this, ((CGData.InfoData.InfoItem) infoData.infoItems.get(j)).imageUrl);
					((ImageView) relativelayout.getChildAt(0)).setImageBitmap(bitmap);
					((ImageView) viewFlipper.getChildAt(j)).setImageBitmap(bitmap);

				}
			}

		}

	}

	class GestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

		public boolean onDown(MotionEvent motionevent) {
			onDown = true;
			return true;
		}

		public boolean onScroll(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1) {
			Log.i(CGGalleryActivity.TAG, "onScroll ");
			if (f > 20F) {// goto _L2; else goto _L1

				viewFlipper.setInAnimation(AnimationUtils.loadAnimation(CGGalleryActivity.this, 0x7f040000));
				viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(CGGalleryActivity.this, 0x7f040001));
				int k = 1 + ((Integer) viewFlipper.getTag()).intValue();
				if (onDown) {
					onDown = false;
					if (k > 5) {
						if (!is_from_news) {
							startActivity(CGGalleryActivity.this, CGMainActivity.class);
							finish();
						}
					} else {
						itemSelect(k);
					}
				}
			} else if (f < -20F) {
				viewFlipper.setInAnimation(AnimationUtils.loadAnimation(CGGalleryActivity.this, 0x7f040002));
				viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(CGGalleryActivity.this, 0x7f040003));
				int j = -1 + ((Integer) viewFlipper.getTag()).intValue();
				if (onDown) {
					onDown = false;
					if (j >= 0)
						itemSelect(j);
				}
			}

			return false;

		}

		public boolean onSingleTapConfirmed(MotionEvent motionevent) {
			int j = ((Integer) viewFlipper.getTag()).intValue();
			String s = ((CGData.InfoData.InfoItem) infoData.infoItems.get(j)).url;
			handleUrl(s);
			return true;
		}

		private boolean onDown;

	}

	public CGGalleryActivity() {
		clickListener = new android.view.View.OnClickListener() {

			public void onClick(View view) {
				Object obj = view.getTag();
				if (obj != null && (obj instanceof Integer)) {
					itemSelect(((Integer) obj).intValue());
				} else {
					switch (view.getId()) {
					case R.id.updateButton:
						update(false);
						break;
					case 2131361870:
						String s = (String) view.getTag();
						handleUrl(s);
						break;

					case R.id.skipButton:
						// FlurryAgent.logEvent("PictorialCloseButton");
						startActivity(CGGalleryActivity.this, CGMainActivity.class);
						finish();
						break;

					case 2131361866:
						finish();
						break;
					}
				}

			}

		};
		gestureDetector = null;
	}

	private CGData.InfoData.InfoItem getInfoItem(int j) {
		int k = 0;

		while (k < infoData.infoItems.size()) {

			if (((CGData.InfoData.InfoItem) infoData.infoItems.get(k)).id == j) {
				CGData.InfoData.InfoItem infoitem = ((CGData.InfoData.InfoItem) infoData.infoItems.get(k));
				infoitem.index = k;

				return infoitem;
			} else {
				k++;
			}
		}
		return null;
	}

	private void handleUrl(String s) {
		String as[] = s.split(":");
		Bundle bundle = new Bundle();
		if (s != null && s.length() > 0
				&& (as[0].equals("site") || as[0].equals("restaurant") || as[0].equals("hotel") || as[0].equals("shopping") || as[0].equals("fun"))) {
			bundle.putString("sg_type", (new StringBuilder()).append(as[0].substring(0, 1).toUpperCase()).append(as[0].substring(1)).toString());
			bundle.putString(CGSiteActivity.KEY_CG_TYPE, (new StringBuilder()).append(as[0].substring(0, 1).toUpperCase()).append(as[0].substring(1))
					.toString());
			bundle.putInt("sg_id", Integer.parseInt(as[1]));
			bundle.putInt("cg_id", CG_DATA.CG_ID);
			if (!is_from_news) {
				startActivity(this, CGMainActivity.class, bundle);
				finish();
			}
			startActivity(this, CGSiteActivity.class, bundle);
			startActivity(this, CGSiteItemActivity.class, bundle);
		}
		if (s != null && s.length() > 0)
			if (!as[0].equals("traffics"))
				;
		if (s != null && s.length() > 0 && as[0].equals("intro") || as[0].equals("traffics")) {
			bundle.putString("sg_type", (new StringBuilder()).append(as[0].substring(0, 1).toUpperCase()).append(as[0].substring(1)).toString());
			bundle.putInt("cg_id", CG_DATA.CG_ID);
			bundle.putString(CGInfoActivity.KEY_INFO_TYPE, as[0]);
			new CGData.InfoData.InfoParaser();
			Iterator iterator = CGData.InfoData.InfoParaser.parse(this, CG_DATA, as[0]).infoItems.iterator();
			do {
				if (!iterator.hasNext())
					break;
				CGData.InfoData.InfoItem infoitem = (CGData.InfoData.InfoItem) iterator.next();
				if (infoitem.id != Integer.parseInt(as[1]))
					continue;
				bundle.putString("url", infoitem.url);
				bundle.putString("title", infoitem.name);
				break;
			} while (true);
			if (!is_from_news) {
				startActivity(this, CGMainActivity.class, bundle);
				finish();
			}
			startActivity(this, CGInfoActivity.class, bundle);
			startActivity(this, TCHtmlActivity.class, bundle);
		}
	}

	private void itemSelect(int j) {
		RelativeLayout relativelayout = null;
		int k = 0;
		for (int l = 0; l < galleryLayout.getChildCount(); l++) {
			TableRow tablerow = (TableRow) galleryLayout.getChildAt(l);
			for (int l1 = 0; l1 < tablerow.getChildCount(); l1++) {
				tablerow.getChildAt(l1).setBackgroundColor(Color.BLACK);
				if (k == j)
					relativelayout = (RelativeLayout) tablerow.getChildAt(l1);
				k++;
			}

		}

		int i1 = j - ((Integer) viewFlipper.getTag()).intValue();
		if (i1 > 0) {
			for (int k1 = 0; k1 < i1; k1++)
				viewFlipper.showNext();

		} else {
			for (int j1 = 0; j1 < -i1; j1++)
				viewFlipper.showPrevious();

		}
		viewFlipper.setTag(Integer.valueOf(j));
		relativelayout.setBackgroundColor(Color.WHITE);
		CGData.InfoData.InfoItem infoitem = (CGData.InfoData.InfoItem) infoData.infoItems.get(j);
		contentText.setText(infoitem.content);
		contentText.setTag(infoitem.url);
	}

	private void readInfoItem(CGData.InfoData.InfoItem infoitem) {
		String s;
		s = (new StringBuilder()).append(galleryPath).append(infoitem.id).toString();
		if (!TCUtil.fileExists(s))
			return;
		try {
			DataInputStream datainputstream = new DataInputStream(new FileInputStream(s));
			infoitem.modified = datainputstream.readInt();
			infoitem.imageUrl = (new StringBuilder()).append(newCGPath).append(datainputstream.readUTF()).toString();
			infoitem.url = datainputstream.readUTF();
			infoitem.content = datainputstream.readUTF();
			datainputstream.close();
		} catch (Exception e) {

		}
	}

	private void saveImageItem(com.tc.logic.CGGalleryData.CGGalleryXmlData.ImageItem imageitem) {
		String s;
		s = (new StringBuilder()).append(galleryPath).append(imageitem.id).toString();
		if (TCUtil.fileExists(s))
			TCUtil.deleteFile(s);
		try {
			TCUtil.createNewFile(new File(s));
			DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(s));
			dataoutputstream.writeInt(imageitem.modified);
			dataoutputstream.writeUTF(imageitem.image);
			dataoutputstream.writeUTF(imageitem.url);
			dataoutputstream.writeUTF(imageitem.content);
			dataoutputstream.close();
		} catch (Exception e) {

		}

	}

	private void update(boolean flag) {
		if (TCUtil.isNetAvailable(this)) {
			forceUpdate = flag;
			updateButton.setVisibility(8);
			updateProgress.setVisibility(0);
			(new Thread(new Runnable() {

				public void run() {
					InputStream inputstream = CGGalleryData.cgGalleryXml(CGGalleryActivity.this, CG_DATA.CG_APPLICATION, CG_DATA.CG_ID,
							infoData.modified);
					if (inputstream != null) {
						byte abyte0[] = TCUtil.getByteArray(inputstream);
						TCUtil.saveNewFile(new ByteArrayInputStream(abyte0), (new StringBuilder()).append(newCGPath).append("gallery/gallerytemp")
								.toString());
						cgGalleryXmlData = com.tc.logic.CGGalleryData.Parser.parse(CGGalleryActivity.this, new ByteArrayInputStream(abyte0));
						if (cgGalleryXmlData != null) {
							com.tc.logic.CGGalleryData.CGGalleryXmlData.ImageItem aimageitem[] = new com.tc.logic.CGGalleryData.CGGalleryXmlData.ImageItem[cgGalleryXmlData.items
									.size()];
							int j = 0;
							for (Iterator iterator = cgGalleryXmlData.items.iterator(); iterator.hasNext();) {
								com.tc.logic.CGGalleryData.CGGalleryXmlData.ImageItem imageitem = (com.tc.logic.CGGalleryData.CGGalleryXmlData.ImageItem) iterator
										.next();
								int k = j + 1;
								aimageitem[j] = imageitem;
								j = k;
							}

							asyncTask = new CGGalleryDataAsyncTask();
							asyncTask.execute(aimageitem);
						}
					}
				}

			})).start();
		}
	}

	public void onBackPressed() {
		if (asyncTask != null && !asyncTask.isCancelled())
			asyncTask.cancel(true);
		if (is_from_news) {
			finish();
		} else {
			startActivity(this, CGMainActivity.class);
			finish();
		}
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		is_from_news = getIntent().getExtras().getBoolean("is_from_news");
		if (is_from_news)
			setContentView(R.layout.cg_gallery_copy);
		else
			setContentView(R.layout.cg_gallery);
		infoData = CGData.InfoData.InfoParaser.parse(this, CG_DATA, "gallery");
		newCGPath = (new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina").append("/").append("CG").append("/").append(CG_DATA.CG_ID)
				.append("/").toString();
		if (CG_DATA.CG_PATH.startsWith("file:///android_asset/")) // goto _L2;
																	// else goto
																	// _L1
		{
			if (!(new File((new StringBuilder()).append(newCGPath).append("gallery/").toString())).exists())
				(new File((new StringBuilder()).append(newCGPath).append("gallery/").toString())).mkdirs();
			if (TCUtil.fileExists((new StringBuilder()).append(newCGPath).append("gallery/gallery.xml").toString())) { // goto
																														// _L4;
																														// else
																														// goto
																														// _L3
				try {
					TCUtil.saveNewFile(
							getAssets().open(
									(new StringBuilder()).append("TouchChina/CG/").append(CG_DATA.CG_ID).append("/gallery/gallery.xml").toString()),
							(new StringBuilder()).append(newCGPath).append("gallery/gallery.xml").toString());

					for (CGData.InfoData.InfoItem infoitem1 : infoData.infoItems) {
						TCUtil.saveNewFile(
								getAssets().open(
										(new StringBuilder()).append("TouchChina/CG/").append(CG_DATA.CG_ID).append("/").append(infoitem1.imageUrl)
												.toString()), (new StringBuilder()).append(newCGPath).append(infoitem1.imageUrl).toString());
						infoitem1.imageUrl = (new StringBuilder()).append(CG_DATA.CG_PATH).append(infoitem1.imageUrl).toString();
					}

				} catch (IOException e) {
					Log.e(TAG, "", e);
				}

			} else {
				try {
					Iterator iterator1 = infoData.infoItems.iterator();
					while (iterator1.hasNext()) {
						CGData.InfoData.InfoItem infoitem = (CGData.InfoData.InfoItem) iterator1.next();
						infoitem.imageUrl = (new StringBuilder()).append(CG_DATA.CG_PATH).append(infoitem.imageUrl).toString();
					}
				}
				// Misplaced declaration of an exception variable
				catch (Exception e) {
					Log.e(TAG, "", e);
				}

			}
		}

		galleryPath = (new StringBuilder()).append(newCGPath).append("gallery/").toString();

		for (CGData.InfoData.InfoItem infoitem1 : infoData.infoItems) {
			readInfoItem(infoitem1);
		}

		((TextView) findViewById(0x7f0a0001)).setText((new StringBuilder()).append(CG_DATA.CG_NAME).append(getString(R.string.cg_map_gallery_title))
				.toString());
		updateProgress = (ProgressBar) findViewById(0x7f0a0048);
		updateButton = (ImageView) findViewById(R.id.updateButton);
		updateButton.setOnClickListener(clickListener);
		skipButton = (ImageView) findViewById(0x7f0a0049);
		skipButton.setOnClickListener(clickListener);
		crossButton = (ImageView) findViewById(0x7f0a004a);
		crossButton.setOnClickListener(clickListener);
		viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
		viewFlipper.setOnTouchListener(this);
		viewFlipper.setLongClickable(true);
		// viewFlipper.setBackgroundColor(Color.WHITE);
		contentText = (TextView) findViewById(R.id.contentText);
		contentText.setOnClickListener(clickListener);

		if (!TCData.USE_2X) {
			contentText.setSingleLine();
			contentText.setEllipsize(android.text.TextUtils.TruncateAt.MARQUEE);
			contentText.setMarqueeRepeatLimit(-1);
			contentText.setTextSize(14F);
			contentText.setFocusableInTouchMode(true);
		}

		galleryLayout = (TableLayout) findViewById(R.id.galleryLayout);
		int j = 0;
		viewFlipper.setTag(Integer.valueOf(0));
		contentText.setText(((CGData.InfoData.InfoItem) infoData.infoItems.get(0)).content);
		contentText.setTag(((CGData.InfoData.InfoItem) infoData.infoItems.get(0)).url);

		for (int k = 0; k < galleryLayout.getChildCount(); k++) {
			TableRow tablerow = (TableRow) galleryLayout.getChildAt(k);
			for (int l = 0; l < tablerow.getChildCount(); l++) {
				RelativeLayout relativelayout = (RelativeLayout) tablerow.getChildAt(l);
				Bitmap bitmap = TCUtil.getBitmap(this, ((CGData.InfoData.InfoItem) infoData.infoItems.get(j)).imageUrl);
				((ImageView) relativelayout.getChildAt(0)).setImageBitmap(bitmap);
				relativelayout.getChildAt(1).setVisibility(8);
				relativelayout.setBackgroundColor(0xff000000);
				relativelayout.setTag(Integer.valueOf(j));
				relativelayout.setOnClickListener(clickListener);
				ImageView imageview = new ImageView(this);
				imageview.setImageBitmap(bitmap);

				imageview.setLayoutParams(new android.view.ViewGroup.LayoutParams(TCData.SCREEN_WIDTH, (int) (bitmap.getHeight()
						* TCData.SCREEN_WIDTH * 1.0 / bitmap.getWidth())));
				Log.i(TAG, "" + TCData.SCREEN_WIDTH);
				imageview.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
				imageview.setTag(l + k * 3);

				viewFlipper.addView(imageview, l + k * 3);
				if (j == 0)
					relativelayout.setBackgroundColor(-1);
				j++;
			}

		}

		// TextView tv=new TextView(this);
		// tv.setText("111111111111111");
		// tv.setTag(0);
		// viewFlipper.addView(tv,0);
		// tv=new TextView(this);
		// tv.setText("22222222222222111111111111111");
		// tv.setTag(1);
		// viewFlipper.addView(tv,1);

		gestureDetector = new GestureDetector(this, new GestureListener());
		update(false);

	}

	protected void onDestroy() {
		super.onDestroy();
	}

	public boolean onTouch(View view, MotionEvent motionevent) {
		return gestureDetector.onTouchEvent(motionevent);
	}

	public static final String IS_FROM_NEWS = "is_from_news";
	private static final String TAG = CGGalleryActivity.class.getSimpleName();
	private CGGalleryDataAsyncTask asyncTask;
	com.tc.logic.CGGalleryData.CGGalleryXmlData cgGalleryXmlData;
	private android.view.View.OnClickListener clickListener;
	private TextView contentText;
	private ImageView crossButton;
	boolean forceUpdate;
	private TableLayout galleryLayout;
	String galleryPath;
	private GestureDetector gestureDetector;
	// private int i;
	private CGData.InfoData infoData;
	private boolean is_from_news;
	String newCGPath;
	private ImageView skipButton;
	private ImageView updateButton;
	private ProgressBar updateProgress;
	private ViewFlipper viewFlipper;

	/*
	 * static CGGalleryDataAsyncTask access$402(CGGalleryActivity
	 * cggalleryactivity, CGGalleryDataAsyncTask cggallerydataasynctask) {
	 * cggalleryactivity.asyncTask = cggallerydataasynctask; return
	 * cggallerydataasynctask; }
	 */

	/*
	 * static int access$602(CGGalleryActivity cggalleryactivity, int j) {
	 * cggalleryactivity.i = j; return j; }
	 */

	/*
	 * static int access$608(CGGalleryActivity cggalleryactivity) { int j =
	 * cggalleryactivity.i; cggalleryactivity.i = j + 1; return j; }
	 */

}
