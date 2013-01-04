// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.community;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.tc.TCUtil;
import com.tc.cg.CGBaseActivity;
import com.tc.cg.CGData;
import com.tc.weibo.WeiboActivity;

// Referenced classes of package com.tc.community:
//            CommunityUtil, CommunityJSON, CommunityActivityMLDPI, LoginRegisterActivity, 
//            BindActivity

public class CommunityActivity extends CGBaseActivity {

	public CommunityActivity() {
	}

	public void finish() {
		File file = new File(CAMERA_PATH);
		if (file.exists())
			file.delete();
		super.finish();
		TCUtil.hideSoftKeyBroad(this, community_layout_content);
	}

	protected void onActivityResult(int i, int j, Intent intent) {
		if (j == -1) { // goto _L2; else goto _L1
			String s = null;
			switch (i) {
			case 77:
				community_layout_content.setText(intent.getStringExtra("CONTENT"));
				break;
			case 1000:
				if (intent != null) {
					community_layout_camera.setImageResource(0x7f0200fb);
					community_layout_pic.setImageResource(0x7f0200fe);
					community_layout_has_pic.setVisibility(0);
					Cursor cursor = managedQuery(intent.getData(), new String[] { "_data" }, null, null, null);
					int i1 = cursor.getColumnIndexOrThrow("_data");
					cursor.moveToFirst();
					s = cursor.getString(i1);
				}
				break;
			case 1001:
				community_layout_camera.setImageResource(0x7f0200fc);
				community_layout_pic.setImageResource(0x7f0200fd);
				community_layout_has_pic.setVisibility(0);
				s = CAMERA_PATH;
				break;
			}

			if (i != 77) {
				Bitmap bitmap = BitmapFactory.decodeFile(s);
				if (bitmap.getWidth() > 960 || bitmap.getHeight() > 960) {
					float f = (1.0F * (float) bitmap.getWidth()) / 960F;
					float f1 = (1.0F * (float) bitmap.getHeight()) / 960F;
					int k;
					int l;
					Cursor cursor;
					int i1;
					if (f > f1) {
						k = (int) ((float) bitmap.getWidth() / f);
						l = (int) ((float) bitmap.getHeight() / f);
					} else {
						k = (int) ((float) bitmap.getWidth() / f1);
						l = (int) ((float) bitmap.getHeight() / f1);
					}
					bitmap = Bitmap.createBitmap(Bitmap.createScaledBitmap(bitmap, k, l, true), 0, 0, k, l, CommunityUtil.getRotateMatrix(s), true);
					try {
						bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, new FileOutputStream(CAMERA_PATH));
					} catch (FileNotFoundException filenotfoundexception) {
						filenotfoundexception.printStackTrace();
					}
				}
				try {
					bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, new FileOutputStream(CAMERA_PATH));
				} catch (FileNotFoundException e) {
					Log.e("", "", e);
				}
			}
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		getWindow().addFlags(0x20000);
		setContentView(0x7f030038);
		pointId = getIntent().getIntExtra("pointid", -1);
		pointType = getIntent().getStringExtra("pointtype");
		pointName = getIntent().getStringExtra("point_name");
		locationManager = (LocationManager) getSystemService("location");
		locationListener = new LocationListener() {

			public void onLocationChanged(Location location) {
				mLocation = location;
				Log.v("", (new StringBuilder()).append(mLocation.getLatitude()).append("\t").append(mLocation.getLongitude()).toString());
			}

			public void onProviderDisabled(String s) {
			}

			public void onProviderEnabled(String s) {
			}

			public void onStatusChanged(String s, int i, Bundle bundle1) {
			}

		};
		backButton = (TextView) findViewById(0x7f0a0000);
		backButton.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				onBackPressed();
			}

		});
		sendButton = (TextView) findViewById(0x7f0a0002);
		sendButton.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				HashMap hashmap = new HashMap();
				hashmap.put("poi.name", pointName);
				String s;
				String s1;
				if (CommunityUtil.getTCToken(CommunityActivity.this) != null)
					s = "Registered";
				else
					s = "Guest";
				hashmap.put("identity", s);
				if (community_layout_has_pic.getVisibility() == 8)
					s1 = "WithoutPicture";
				else
					s1 = "WithPicture";
				hashmap.put("picStatus", s1);
				FlurryAgent.logEvent("PostedCommentOrRateInPOI", hashmap);
				if (community_layout_content.getText().toString().length() > 140)
					CommunityUtil.showCommunityToast(CommunityActivity.this, "12");
				else if (CommunityUtil.getTCToken(CommunityActivity.this) != null)
					(new AsyncTask() {

						protected Object doInBackground(Object aobj[]) {

							CGData _tmp = CG_DATA;
							String s = CGData.CG_COMMUNITY_CODE;
							String s1 = pointType;
							int i = pointId;
							String s2 = pointName;
							int j = (int) community_layout_ratingBar.getRating();
							String s3 = community_layout_content.getText().toString();
							String s4;
							StringBuilder stringbuilder;
							String s5;
							StringBuilder stringbuilder1;
							String s6;
							String s7;
							String s8;
							String s9;
							if (community_layout_has_pic.getVisibility() == 8)
								s4 = null;
							else
								s4 = CommunityActivity.CAMERA_PATH;
							stringbuilder = new StringBuilder();
							if (sinaStatus)
								s5 = "sina,";
							else
								s5 = "";
							stringbuilder1 = stringbuilder.append(s5);
							if (tencentStatus)
								s6 = "tencent";
							else
								s6 = "";
							s7 = stringbuilder1.append(s6).toString();
							if (needLoaction && mLocation != null)
								s8 = (new StringBuilder()).append("").append(mLocation.getLatitude()).toString();
							else
								s8 = "";
							if (needLoaction && mLocation != null)
								s9 = (new StringBuilder()).append("").append(mLocation.getLongitude()).toString();
							else
								s9 = "";
							statusCode = CommunityJSON.comment(CommunityActivity.this, s, s1, i, s2, j, s3, s4, s7, s8, s9);
							return null;
						}

						protected void onPostExecute(Object obj) {

							progressDialog.dismiss();
							if ("OK".equals(statusCode)) {
								File file = new File(CommunityActivity.CAMERA_PATH);
								if (file.exists())
									file.delete();
								CommunityUtil.showCommunityToast(CommunityActivity.this, "13");
								finish();
							} else {
								CommunityUtil.showCommunityToast(CommunityActivity.this, statusCode);
							}
						}

						protected void onPreExecute() {
							super.onPreExecute();
							progressDialog = new ProgressDialog(CommunityActivity.this);
							progressDialog.setTitle(0x7f08004d);
							progressDialog.setMessage(getString(0x7f08004c));
							progressDialog.show();
						}

						ProgressDialog progressDialog;
						String statusCode;

					}).execute(null);
				else if (CommunityUtil.getTCAnonymousToken(CommunityActivity.this) != null) {
					if (CommunityUtil.getTCAnonymousOptionCount(CommunityActivity.this) < 2)
						(new AsyncTask() {

							protected Object doInBackground(Object aobj[]) {

								CGData _tmp = CG_DATA;
								String s = CGData.CG_COMMUNITY_CODE;
								String s1 = pointType;
								int i = pointId;
								String s2 = pointName;
								int j = (int) community_layout_ratingBar.getRating();
								String s3 = community_layout_content.getText().toString();
								String s4;
								StringBuilder stringbuilder;
								String s5;
								StringBuilder stringbuilder1;
								String s6;
								String s7;
								String s8;
								String s9;
								if (community_layout_has_pic.getVisibility() == 8)
									s4 = null;
								else
									s4 = CommunityActivity.CAMERA_PATH;
								stringbuilder = new StringBuilder();
								if (sinaStatus)
									s5 = "sina,";
								else
									s5 = "";
								stringbuilder1 = stringbuilder.append(s5);
								if (tencentStatus)
									s6 = "tencent";
								else
									s6 = "";
								s7 = stringbuilder1.append(s6).toString();
								if (needLoaction && mLocation != null)
									s8 = (new StringBuilder()).append("").append(mLocation.getLatitude()).toString();
								else
									s8 = "";
								if (needLoaction && mLocation != null)
									s9 = (new StringBuilder()).append("").append(mLocation.getLongitude()).toString();
								else
									s9 = "";
								statusCode = CommunityJSON.comment(CommunityActivity.this, s, s1, i, s2, j, s3, s4, s7, s8, s9);
								return null;
							}

							protected void onPostExecute(Object obj) {

								progressDialog.dismiss();
								if ("OK".equals(statusCode)) {
									File file = new File(CommunityActivity.CAMERA_PATH);
									if (file.exists())
										file.delete();
									CommunityUtil.setTCAnonymousOptionCount(CommunityActivity.this,
											1 + CommunityUtil.getTCAnonymousOptionCount(CommunityActivity.this));
									CommunityUtil.showCommunityToast(CommunityActivity.this, "13");
									finish();
								} else {
									CommunityUtil.showCommunityToast(CommunityActivity.this, statusCode);
								}
							}

							protected void onPreExecute() {
								progressDialog = new ProgressDialog(CommunityActivity.this);
								progressDialog.setTitle(0x7f08004d);
								progressDialog.setMessage(getString(0x7f08004c));
								progressDialog.show();
							}

							ProgressDialog progressDialog;
							String statusCode;

						}).execute(null);
					else if (CommunityUtil.getTCAnonymousOptionCount(CommunityActivity.this) < 5)
						CommunityUtil.showAnonymous2TimesDialog(CommunityActivity.this, CG_ID, new Handler() {

							public void handleMessage(Message message) {
								(new AsyncTask() {

									protected Object doInBackground(Object aobj[]) {

										CGData _tmp = CG_DATA;
										String s = CGData.CG_COMMUNITY_CODE;
										String s1 = pointType;
										int i = pointId;
										String s2 = pointName;
										int j = (int) community_layout_ratingBar.getRating();
										String s3 = community_layout_content.getText().toString();
										String s4;
										StringBuilder stringbuilder;
										String s5;
										StringBuilder stringbuilder1;
										String s6;
										String s7;
										String s8;
										String s9;
										if (community_layout_has_pic.getVisibility() == 8)
											s4 = null;
										else
											s4 = CommunityActivity.CAMERA_PATH;
										stringbuilder = new StringBuilder();
										if (sinaStatus)
											s5 = "sina,";
										else
											s5 = "";
										stringbuilder1 = stringbuilder.append(s5);
										if (tencentStatus)
											s6 = "tencent";
										else
											s6 = "";
										s7 = stringbuilder1.append(s6).toString();
										if (needLoaction && mLocation != null)
											s8 = (new StringBuilder()).append("").append(mLocation.getLatitude()).toString();
										else
											s8 = "";
										if (needLoaction && mLocation != null)
											s9 = (new StringBuilder()).append("").append(mLocation.getLongitude()).toString();
										else
											s9 = "";
										statusCode = CommunityJSON.comment(CommunityActivity.this, s, s1, i, s2, j, s3, s4, s7, s8, s9);
										return null;
									}

									protected void onPostExecute(Object obj) {

										progressDialog.dismiss();
										if ("OK".equals(statusCode)) {
											File file = new File(CommunityActivity.CAMERA_PATH);
											if (file.exists())
												file.delete();
											CommunityUtil.setTCAnonymousOptionCount(CommunityActivity.this,
													1 + CommunityUtil.getTCAnonymousOptionCount(CommunityActivity.this));
											CommunityUtil.showCommunityToast(CommunityActivity.this, "13");
											finish();
										} else {
											CommunityUtil.showCommunityToast(CommunityActivity.this, statusCode);
										}
									}

									protected void onPreExecute() {
										progressDialog = new ProgressDialog(CommunityActivity.this);
										progressDialog.setTitle(0x7f08004d);
										progressDialog.setMessage(getString(0x7f08004c));
										progressDialog.show();
									}

									ProgressDialog progressDialog;
									String statusCode;

								}).execute(null);
							}

						});
					else
						CommunityUtil.showAnonymous5TimesDialog(CommunityActivity.this, CG_ID);
				} else {
					CommunityUtil.showAnonymousFirstTimesDialog(CommunityActivity.this, CG_ID, new Handler() {

						public void handleMessage(Message message) {
							(new AsyncTask() {

								protected Object doInBackground(Object aobj[]) {

									statusCode = CommunityJSON.getAnonymousToken(CommunityActivity.this);
									return null;
								}

								protected void onPostExecute(Object obj) {

									if ("OK".equals(statusCode))
										(new AsyncTask() {

											protected Object doInBackground(Object aobj[]) {

												CGData _tmp = CG_DATA;
												String s = CGData.CG_COMMUNITY_CODE;
												String s1 = pointType;
												int i = pointId;
												String s2 = pointName;
												int j = (int) community_layout_ratingBar.getRating();
												String s3 = community_layout_content.getText().toString();
												String s4;
												StringBuilder stringbuilder;
												String s5;
												StringBuilder stringbuilder1;
												String s6;
												String s7;
												String s8;
												String s9;
												if (community_layout_has_pic.getVisibility() == 8)
													s4 = null;
												else
													s4 = CommunityActivity.CAMERA_PATH;
												stringbuilder = new StringBuilder();
												if (sinaStatus)
													s5 = "sina,";
												else
													s5 = "";
												stringbuilder1 = stringbuilder.append(s5);
												if (tencentStatus)
													s6 = "tencent";
												else
													s6 = "";
												s7 = stringbuilder1.append(s6).toString();
												if (needLoaction && mLocation != null)
													s8 = (new StringBuilder()).append("").append(mLocation.getLatitude()).toString();
												else
													s8 = "";
												if (needLoaction && mLocation != null)
													s9 = (new StringBuilder()).append("").append(mLocation.getLongitude()).toString();
												else
													s9 = "";
												statusCode = CommunityJSON.comment(CommunityActivity.this, s, s1, i, s2, j, s3, s4, s7, s8, s9);
												return null;
											}

											protected void onPostExecute(Object obj) {

												progressDialog.dismiss();
												if ("OK".equals(statusCode)) {
													File file = new File(CommunityActivity.CAMERA_PATH);
													if (file.exists())
														file.delete();
													CommunityUtil.setTCAnonymousOptionCount(CommunityActivity.this,
															1 + CommunityUtil.getTCAnonymousOptionCount(CommunityActivity.this));
													CommunityUtil.showCommunityToast(CommunityActivity.this, "13");
													finish();
												} else {
													CommunityUtil.showCommunityToast(CommunityActivity.this, statusCode);
												}
											}

											protected void onPreExecute() {
												progressDialog = new ProgressDialog(CommunityActivity.this);
												progressDialog.setTitle(0x7f08004d);
												progressDialog.setMessage(getString(0x7f08004c));
												progressDialog.show();
											}

											ProgressDialog progressDialog;
											String statusCode;

										}).execute(null);
									else
										CommunityUtil.showCommunityToast(CommunityActivity.this, statusCode);
								}

								String statusCode;

							}).execute(null);
						}

					});
				}
			}

		});
		title = (TextView) findViewById(0x7f0a015f);
		title.setText((new StringBuilder()).append(pointName).append(getString(0x7f080134)).toString());
		community_layout_ratingBar = (RatingBar) findViewById(0x7f0a0161);
		Log.v("", (new StringBuilder()).append("--------------->").append(getIntent().getFloatExtra("RATING_AVERAGE", 0.0F)).toString());
		community_layout_ratingBar.setRating(getIntent().getFloatExtra("RATING_AVERAGE", 0.0F) / 2.0F);
		community_layout_rating_people = (TextView) findViewById(0x7f0a0162);
		community_layout_rating_people.setText((new StringBuilder()).append(getIntent().getIntExtra("point_commentCount", 0))
				.append(getString(0x7f08002a)).toString());
		community_layout_status_text1 = (TextView) findViewById(0x7f0a016e);
		community_layout_status_text2 = (TextView) findViewById(0x7f0a016c);
		community_layout_status_leftcount = (TextView) findViewById(0x7f0a016d);
		community_layout_content = (EditText) findViewById(0x7f0a016f);
		community_layout_content.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable editable) {
				if (editable.length() <= 140) {
					community_layout_status_text1.setTextColor(getResources().getColor(0x7f060011));
					community_layout_status_text1.setText(0x7f08015e);
					community_layout_status_text2.setTextColor(getResources().getColor(0x7f060011));
					community_layout_status_leftcount.setTextColor(getResources().getColor(0x7f060011));
					community_layout_status_leftcount.setText((new StringBuilder()).append("").append(140 - editable.length()).toString());
				} else {
					community_layout_status_text1.setTextColor(getResources().getColor(0x7f060012));
					community_layout_status_text1.setText(0x7f08015f);
					community_layout_status_text2.setTextColor(getResources().getColor(0x7f060012));
					community_layout_status_leftcount.setTextColor(getResources().getColor(0x7f060012));
					community_layout_status_leftcount.setText((new StringBuilder()).append("").append(-140 + editable.length()).toString());
				}
			}

			public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
			}

			public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
			}

		});
		community_layout_content.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				Intent intent = new Intent(CommunityActivity.this, CommunityActivityMLDPI.class);
				intent.putExtra("CONTENT", community_layout_content.getText().toString());
				startActivityForResult(intent, 77);
			}

		});
		needSina = true;
		needTencent = true;
		sinaStatus = false;
		community_layout_status_sina = (ImageView) findViewById(0x7f0a0169);
		community_layout_status_sina.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				if (sinaStatus) {
					needSina = false;
					sinaStatus = false;
					community_layout_status_sina.setImageResource(0x7f02013f);
				} else {
					needSina = true;
					if (CommunityUtil.getTCToken(CommunityActivity.this) == null)
						startActivity(CommunityActivity.this, LoginRegisterActivity.class);
					else if (CommunityUtil.isSinaBind(CommunityActivity.this)) {
						sinaStatus = true;
						community_layout_status_sina.setImageResource(0x7f020140);
					} else {
						startActivity(CommunityActivity.this, BindActivity.class);
					}
				}
			}

		});
		tencentStatus = false;
		community_layout_status_tencent = (ImageView) findViewById(0x7f0a016a);
		community_layout_status_tencent.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				if (tencentStatus) {
					needTencent = false;
					tencentStatus = false;
					community_layout_status_tencent.setImageResource(0x7f02016c);
				} else {
					needTencent = true;
					if (CommunityUtil.getTCToken(CommunityActivity.this) == null)
						startActivity(CommunityActivity.this, LoginRegisterActivity.class);
					else if (CommunityUtil.isTencentBind(CommunityActivity.this)) {
						tencentStatus = true;
						community_layout_status_tencent.setImageResource(0x7f02016d);
					} else {
						startActivity(CommunityActivity.this, BindActivity.class);
					}
				}
			}

		});
		community_layout_camera = (ImageView) findViewById(0x7f0a0164);
		community_layout_camera.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				Uri uri = Uri.fromFile(new File(WeiboActivity.CAMERA_PATH));
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				intent.putExtra("output", uri);
				startActivityForResult(intent, 1001);
			}

		});
		community_layout_pic = (ImageView) findViewById(0x7f0a0165);
		community_layout_pic.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				Intent intent = new Intent("android.intent.action.GET_CONTENT");
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, getString(0x7f080083)), 1000);
			}

		});
		needLoaction = false;
		community_layout_location = (ImageView) findViewById(0x7f0a0166);
		community_layout_location.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				if (needLoaction) {
					(new android.app.AlertDialog.Builder(CommunityActivity.this)).setMessage(0x7f0801c2).setNegativeButton(0x7f08001d, null)
							.setPositiveButton(0x7f08001e, new android.content.DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialoginterface, int i) {
									community_layout_location.setImageResource(0x7f0200c1);
									locationManager.removeUpdates(locationListener);
									needLoaction = false;
								}

							}).show();
				} else {
					community_layout_location.setImageResource(0x7f0200c2);
					locationManager.requestLocationUpdates(TCUtil.getBestProvider(locationManager), 1000L, 0.0F, locationListener);
					needLoaction = true;
				}
			}

		});
		community_layout_has_pic = (ImageView) findViewById(0x7f0a016b);
		community_layout_has_pic.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				(new android.app.AlertDialog.Builder(CommunityActivity.this)).setMessage(0x7f080175).setNegativeButton(0x7f08001d, null)
						.setPositiveButton(0x7f08001e, new android.content.DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialoginterface, int i) {
								community_layout_pic.setImageResource(0x7f0200fd);
								community_layout_camera.setImageResource(0x7f0200fb);
								(new File(CommunityActivity.CAMERA_PATH)).delete();
								community_layout_has_pic.setVisibility(8);
							}

						}).show();
			}

		});
	}

	protected void onResume() {
		super.onResume();
		if (needSina && CommunityUtil.isSinaBind(this)) {
			sinaStatus = true;
			community_layout_status_sina.setImageResource(0x7f020140);
		} else {
			sinaStatus = false;
			community_layout_status_sina.setImageResource(0x7f02013f);
		}
		if (needTencent && CommunityUtil.isTencentBind(this)) {
			tencentStatus = true;
			community_layout_status_tencent.setImageResource(0x7f02016d);
		} else {
			tencentStatus = false;
			community_layout_status_tencent.setImageResource(0x7f02016c);
		}
	}

	public static final String CAMERA_PATH = (new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina").append("/tc_comment.png")
			.toString();
	private static final int PIC_SIZE = 960;
	public static final String RATING_AVERAGE = "RATING_AVERAGE";
	public static final int RESAULT = 1002;
	public static final int RESAULT_FROM_CAMERA = 1001;
	public static final int RESAULT_FROM_PIC_LIB = 1000;
	private TextView backButton;
	private ImageView community_layout_camera;
	private EditText community_layout_content;
	private ImageView community_layout_has_pic;
	private ImageView community_layout_location;
	private ImageView community_layout_pic;
	private RatingBar community_layout_ratingBar;
	private TextView community_layout_rating_people;
	private TextView community_layout_status_leftcount;
	private ImageView community_layout_status_sina;
	private ImageView community_layout_status_tencent;
	private TextView community_layout_status_text1;
	private TextView community_layout_status_text2;
	private LocationListener locationListener;
	private LocationManager locationManager;
	private Location mLocation;
	private boolean needLoaction;
	private boolean needSina;
	private boolean needTencent;
	private int pointId;
	private String pointName;
	private String pointType;
	private TextView sendButton;
	private boolean sinaStatus;
	private boolean tencentStatus;
	private TextView title;

	/*
	 * static Location access$002(CommunityActivity communityactivity, Location
	 * location) { communityactivity.mLocation = location; return location; }
	 */

	/*
	 * static boolean access$1302(CommunityActivity communityactivity, boolean
	 * flag) { communityactivity.needSina = flag; return flag; }
	 */

	/*
	 * static boolean access$1502(CommunityActivity communityactivity, boolean
	 * flag) { communityactivity.needTencent = flag; return flag; }
	 */

	/*
	 * static boolean access$702(CommunityActivity communityactivity, boolean
	 * flag) { communityactivity.sinaStatus = flag; return flag; }
	 */

	/*
	 * static boolean access$802(CommunityActivity communityactivity, boolean
	 * flag) { communityactivity.tencentStatus = flag; return flag; }
	 */

	/*
	 * static boolean access$902(CommunityActivity communityactivity, boolean
	 * flag) { communityactivity.needLoaction = flag; return flag; }
	 */
}
