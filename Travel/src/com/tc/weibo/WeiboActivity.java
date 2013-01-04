// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.weibo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import weibo4android.User;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import weibo4android.http.ImageItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tc.TCUtil;
import com.tc.cg.CGData;
import com.tc.community.CommunityJSON;
import com.tc.community.CommunityUtil;

// Referenced classes of package com.tc.weibo:
//            WeiboUtil, WeiboSettings, WeiboLogic

public class WeiboActivity extends Activity implements android.view.View.OnClickListener, TextWatcher {
	class WeiboAsyncTask extends AsyncTask {

		public void cancel() {
			if (!isCancelled())
				cancel(true);
		}

		protected Object doInBackground(Object aobj[]) {

			String s;
			Map map;
			HashMap hashmap;
			s = (String) aobj[0];
			map = (Map) aobj[1];
			hashmap = new HashMap();
			hashmap.put("method", s);
			if ("login".equals(s)) { // goto _L2; else goto _L1
				Log.i(WeiboActivity.TAG, "login...");
				String s2 = (String) map.get("usr_name");
				String s3 = (String) map.get("usr_password");
				try {
					AccessToken accesstoken = weibo.getXAuthAccessToken(s2, s3);
					token = accesstoken.getToken();
					tokenSecret = accesstoken.getTokenSecret();
					uid = accesstoken.getUserId();
				} catch (WeiboException weiboexception2) {
					hashmap.put("weibo_exception", weiboexception2);
				}
			} else if ("get_friend_list".equals(s)) {

				friendsList = new ArrayList();

				int j = 0;
				int size = 0;
				try {
					do {
						List list = weibo.getFriendsStatuses(j);
						size = list.size();
						User user;
						for (Iterator iterator = list.iterator(); iterator.hasNext(); friendsList.add(user.getName()))
							user = (User) iterator.next();
						j += 20;
					} while ((size == 20 || size == 19));
				} catch (WeiboException e) {
					Log.e(TAG, "", e);
					hashmap.put("weibo_exception", e);
				}

				WeiboUtil.saveFriendList(WeiboActivity.this, friendsList);

			} else if ("status".equals(s)) {
				Log.i(WeiboActivity.TAG, "status...");
				String s1;
				Location location1;
				byte abyte0[];
				ImageItem imageitem;
				int i;
				s1 = (String) map.get("status");
				location1 = (Location) map.get("location");
				abyte0 = (byte[]) (byte[]) map.get("image");
				imageitem = null;
				try {
					if (abyte0 != null && abyte0.length > 0)
						imageitem = new ImageItem(abyte0);

					if (imageitem != null && location1 != null) {

						weibo.uploadStatus(s1, imageitem, location1.getLatitude(), location1.getLongitude());

					} else if (imageitem != null) {
						if (weibo.uploadStatus(s1, imageitem).getText().length() == 0)
							hashmap.put("weibo_exception", new WeiboException("text null", 400));
					} else if (location1 != null)
						weibo.updateStatus(s1, location1.getLatitude(), location1.getLongitude());
					else
						weibo.updateStatus(s1);

				} catch (WeiboException e) {
					Log.e(TAG, "", e);
					hashmap.put("weibo_exception", e);
				} catch (Exception e) {
					hashmap.put("weibo_exception", e);
					Log.e(TAG, "", e);
				}

			}

			return hashmap;

		}

		protected void onCancelled() {
			super.onCancelled();
			Message message = new Message();
			message.what = 3;
			handler.sendMessage(message);
		}

		protected void onPostExecute(Object obj) {
			onPostExecute((Map) obj);
		}

		protected void onPostExecute(Map map) {
			super.onPostExecute(map);
			Message message = new Message();
			message.what = 2;
			message.obj = map;
			handler.sendMessage(message);
		}

		protected void onPreExecute() {
			super.onPreExecute();
			Message message = new Message();
			message.what = 0;
			handler.sendMessage(message);
		}

		protected void onProgressUpdate(Integer ainteger[]) {
			super.onProgressUpdate(ainteger);
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}

		protected void onProgressUpdate(Object aobj[]) {
			onProgressUpdate((Integer[]) aobj);
		}

	}

	public WeiboActivity() {
		locationState = false;
		listener = new LocationListener() {

			public void onLocationChanged(Location location1) {
				updateLocation(location1);
			}

			public void onProviderDisabled(String s) {
			}

			public void onProviderEnabled(String s) {
			}

			public void onStatusChanged(String s, int i, Bundle bundle) {
			}

		};
		handler = new Handler() {

			public void handleMessage(Message message) {
				switch (message.what) {
				case 0:
					showProgressDialog(status);
					break;
				case 1:
					break;
				case 2:
					String s;
					WeiboException weiboexception;
					dismissProgressDialog();
					Map map = (Map) message.obj;
					s = (String) map.get("method");
					weiboexception = (WeiboException) map.get("weibo_exception");
					if (s.equals("login")) {

						if (weiboexception == null) {
							if (token == null && tokenSecret == null) {
								password = "";
								userName = "";
								weibo = new Weibo();
								statusText.setText(getString(0x7f080041));
								showDialog(1001);
								showToast(getString(0x7f080041));
							} else {
								WeiboSettings.putUserName(WeiboActivity.this, userName);
								WeiboSettings.putPassword(WeiboActivity.this, password);
								WeiboSettings.putToken(WeiboActivity.this, token);
								WeiboSettings.putTokenSecret(WeiboActivity.this, tokenSecret);
								doStatus();
								if (CommunityUtil.getTCToken(WeiboActivity.this) == null)
									(new AsyncTask() {

										protected Object doInBackground(Object aobj[]) {
											return doInBackground((Void[]) aobj);
										}

										protected Void doInBackground(Void avoid[]) {
											statusCode = CommunityJSON.otherUserLogin(WeiboActivity.this, "sina", (new StringBuilder()).append("")
													.append(uid).toString(), token, tokenSecret, CGData.CG_COMMUNITY_CODE);
											return null;
										}

										protected void onPostExecute(Object obj) {
											onPostExecute((Void) obj);
										}

										protected void onPostExecute(Void void1) {
											super.onPostExecute(void1);
											if ("OK".equals(statusCode)) {
												statusText.setText(getString(0x7f080040));
												showToast(getString(0x7f080040));
											} else {
												WeiboSettings.putToken(WeiboActivity.this, null);
												WeiboSettings.putTokenSecret(WeiboActivity.this, null);
												statusText.setText(getString(0x7f080041));
												showDialog(1001);
												showToast(getString(0x7f080041));
											}
										}

										String statusCode;

									}).execute(null);
								else
									(new AsyncTask() {

										protected Object doInBackground(Object aobj[]) {
											return doInBackground((Void[]) aobj);
										}

										protected Void doInBackground(Void avoid[]) {
											statusCode = CommunityJSON.bind(WeiboActivity.this, CGData.CG_COMMUNITY_CODE, "sina",
													(new StringBuilder()).append("").append(uid).toString(), token, tokenSecret, true);
											return null;
										}

										protected void onPostExecute(Object obj) {
											onPostExecute((Void) obj);
										}

										protected void onPostExecute(Void void1) {
											super.onPostExecute(void1);
											if ("OK".equals(statusCode)) {
												statusText.setText(getString(0x7f080040));
												showToast(getString(0x7f080040));
											} else if ("109".equals(statusCode))
												(new android.app.AlertDialog.Builder(WeiboActivity.this)).setTitle(0x7f08019e).setMessage(0x7f08019f)
														.setNegativeButton(0x7f08001d, new android.content.DialogInterface.OnClickListener() {

															public void onClick(DialogInterface dialoginterface, int i) {
																WeiboSettings.putToken(WeiboActivity.this, null);
																WeiboSettings.putTokenSecret(WeiboActivity.this, null);
																statusText.setText(getString(0x7f080041));
																showDialog(1001);
																showToast(getString(0x7f080041));
															}

														}).setPositiveButton(0x7f08001e, new android.content.DialogInterface.OnClickListener() {

															public void onClick(DialogInterface dialoginterface, int i) {
																(new AsyncTask() {

																	protected Object doInBackground(Object aobj[]) {
																		return doInBackground((Void[]) aobj);
																	}

																	protected Void doInBackground(Void avoid[]) {
																		status1 = CommunityJSON.bind(WeiboActivity.this, CGData.CG_COMMUNITY_CODE,
																				"sina", (new StringBuilder()).append("").append(uid).toString(),
																				token, tokenSecret, true);
																		return null;
																	}

																	protected void onPostExecute(Object obj) {
																		onPostExecute((Void) obj);
																	}

																	protected void onPostExecute(Void void1) {
																		super.onPostExecute(void1);
																		if ("OK".equals(status1)) {
																			statusText.setText(getString(0x7f080040));
																			showToast(getString(0x7f080040));
																		} else {
																			WeiboSettings.putToken(WeiboActivity.this, null);
																			WeiboSettings.putTokenSecret(WeiboActivity.this, null);
																			statusText.setText(getString(0x7f080041));
																			showDialog(1001);
																			showToast(getString(0x7f080041));
																		}
																	}

																	String status1;

																}).execute(null);
															}

														}).show();
											else if ("124".equals(statusCode)) {
												(new android.app.AlertDialog.Builder(WeiboActivity.this)).setTitle(0x7f0801ad).setMessage(0x7f0801ae)
														.setNegativeButton(0x7f08001d, new android.content.DialogInterface.OnClickListener() {

															public void onClick(DialogInterface dialoginterface, int i) {
																WeiboSettings.putToken(WeiboActivity.this, null);
																WeiboSettings.putTokenSecret(WeiboActivity.this, null);
																statusText.setText(getString(0x7f080041));
																showDialog(1001);
																showToast(getString(0x7f080041));
															}

														}).setPositiveButton(0x7f08001e, new android.content.DialogInterface.OnClickListener() {

															public void onClick(DialogInterface dialoginterface, int i) {
																(new AsyncTask() {

																	protected Object doInBackground(Object aobj[]) {
																		return doInBackground((Void[]) aobj);
																	}

																	protected Void doInBackground(Void avoid[]) {
																		status1 = CommunityJSON.bind(WeiboActivity.this, CGData.CG_COMMUNITY_CODE,
																				"sina", (new StringBuilder()).append("").append(uid).toString(),
																				token, tokenSecret, true);
																		return null;
																	}

																	protected void onPostExecute(Object obj) {
																		onPostExecute((Void) obj);
																	}

																	protected void onPostExecute(Void void1) {
																		super.onPostExecute(void1);
																		if ("OK".equals(status1)) {
																			statusText.setText(getString(0x7f080040));
																			showToast(getString(0x7f080040));
																		} else {
																			WeiboSettings.putToken(WeiboActivity.this, null);
																			WeiboSettings.putTokenSecret(WeiboActivity.this, null);
																			statusText.setText(getString(0x7f080041));
																			showDialog(1001);
																			showToast(getString(0x7f080041));
																		}
																	}

																	String status1;

																}).execute(null);
															}

														}).show();
											} else {
												WeiboSettings.putToken(WeiboActivity.this, null);
												WeiboSettings.putTokenSecret(WeiboActivity.this, null);
												statusText.setText(getString(0x7f080041));
												showDialog(1001);
												showToast(getString(0x7f080041));
											}
										}

										String statusCode;

									}).execute(null);
							}
						} else {
							statusText.setText(getString(0x7f080041));
							showDialog(1001);
							showToast(getString(0x7f080041));
						}
					} else if (s.equals("status")) {
						if (weiboexception == null) {
							statusText.setText(getString(0x7f080042));
							setStatusEdit("");
							setNullPreview();
							finish();
						} else if (weiboexception.getStatusCode() == 400) {
							statusText.setText(getString(0x7f080050));
							showToast(getString(0x7f08004e));
						} else {
							statusText.setText(getString(0x7f080042));
							showToast(getString(0x7f080042));
							finish();
						}
					} else if (s.equals("get_friend_list"))
						if (weiboexception == null) {
							statusText.setText(getString(0x7f080044));
							showDialog(1000);
						} else {
							statusText.setText(getString(0x7f080045));
						}

					dismissProgressDialog();
					break;
				case 3:
					dismissProgressDialog();
					break;
				}

			}

		};
	}

	private Dialog createFriendListDialog() {
		android.app.AlertDialog.Builder builder = (new android.app.AlertDialog.Builder(this)).setIcon(0x7f020078);
		ArrayList arraylist;
		final String cachedFriendsArray[];
		boolean aflag[];
		AlertDialog alertdialog;
		if (friendsList == null)
			arraylist = WeiboUtil.getFriendList(this);
		else
			arraylist = friendsList;
		cachedFriendsArray = new String[arraylist.size()];
		arraylist.toArray(cachedFriendsArray);
		aflag = new boolean[arraylist.size()];
		alertdialog = builder.setTitle(getString(0x7f08001c))
				.setMultiChoiceItems(cachedFriendsArray, aflag, new android.content.DialogInterface.OnMultiChoiceClickListener() {

					public void onClick(DialogInterface dialoginterface, int i, boolean flag) {
					}

				}).setPositiveButton(getString(0x7f08001e), new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialoginterface, int i) {
						String s = getStatusEdit();
						for (int j = 0; j < cachedFriendsArray.length; j++) {
							if (!listView.getCheckedItemPositions().get(j))
								continue;
							String s1 = (new StringBuilder()).append("@").append(listView.getAdapter().getItem(j)).toString();
							if (!s.contains(s1))
								s = (new StringBuilder()).append(s).append(s1).toString();
						}

						setStatusEdit(s);
					}

				}).setNegativeButton(getString(0x7f08001d), null).create();
		listView = alertdialog.getListView();
		return alertdialog;
	}

	private Dialog createLoginDialog() {
		View view = TCUtil.getLayoutInflater(this).inflate(0x7f030077, null);
		userNameEdit = (EditText) view.findViewById(0x7f0a0271);
		passwordEdit = (EditText) view.findViewById(0x7f0a0273);
		userNameEdit.setText(WeiboSettings.getUserName(this));
		passwordEdit.setText(WeiboSettings.getPassword(this));
		return (new android.app.AlertDialog.Builder(this)).setTitle(getString(0x7f08003b)).setView(view)
				.setPositiveButton(getString(0x7f08003c), new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialoginterface, int i) {
						userName = userNameEdit.getText().toString();
						password = passwordEdit.getText().toString();
						if (password.length() > 0 && userName.length() > 0) {
							doLogin();
						} else {
							showToast(getString(0x7f08003f));
							statusText.setText(getString(0x7f08003a));
							createLoginDialog().show();
						}
					}

				}).setNegativeButton(getString(0x7f080039), new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialoginterface, int i) {
						statusText.setText(getString(0x7f08003a));
					}

				}).create();
	}

	private Dialog createPreviewImageDialog() {
		return TCUtil.createPositiveDialog(this, getString(0x7f08003d), new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialoginterface, int i) {
				setNullPreview();
			}

		});
	}

	private Dialog createUpdateStatusDialog() {
		return TCUtil.createPositiveDialog(this, getString(0x7f08003e), new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialoginterface, int i) {
				finish();
			}

		});
	}

	private void dismissProgressDialog() {
		if (progressDialog != null)
			progressDialog.cancel();
	}

	private void doLogin() {
		status = getString(0x7f080047);
		execute(WeiboLogic.getLoginParams(userName, password));
	}

	private void doStatus() {
		status = getString(0x7f08004c);
		String s = statusEdit.getText().toString();
		if (textAvailable(s)) {
			byte abyte0[] = null;
			if (imageBitmap != null)
				abyte0 = TCUtil.getBitmapByteArray(imageBitmap);
			execute(WeiboLogic.status(s, location, abyte0));
		} else {
			statusText.setText(getString(0x7f080050));
			showToast(getString(0x7f08004f));
		}
	}

	private void dogetFriendList() {
		status = getString(0x7f080046);
		execute(WeiboLogic.getFriendList());
	}

	private void execute(Object aobj[]) {
		if (weiboAsyncTask != null)
			weiboAsyncTask.cancel();
		weiboAsyncTask = new WeiboAsyncTask();
		weiboAsyncTask.execute(aobj);
	}

	private Bitmap getCameraBitmap(String s) {
		Bitmap bitmap;
		if (s == null)
			bitmap = TCUtil.getBitmap(this, CAMERA_PATH);
		else
			bitmap = TCUtil.getBitmap(this, s);
		return bitmap;
	}

	private void getFriendList() {
		if (textAvailable(token) && textAvailable(tokenSecret))
			dogetFriendList();
		else if (textAvailable(password) && textAvailable(userName))
			doLogin();
		else
			showDialog(1001);
	}

	private String getStatusEdit() {
		return statusEdit.getText().toString();
	}

	private void initContorl() {
		Bundle bundle = getIntent().getExtras();
		boolean flag = bundle.getBoolean("has_camera", false);
		String s = bundle.getString("path_camera");
		Bitmap bitmap = (Bitmap) bundle.getParcelable("icon_bitmap");
		String s1 = bundle.getString("status");
		if (bitmap == null) {
			if (flag) {
				TCUtil.recycleBitmap(imageBitmap);
				imageBitmap = getCameraBitmap(null);
				setPreview(imageBitmap);
			}
			if (s != null) {
				TCUtil.recycleBitmap(imageBitmap);
				imageBitmap = getCameraBitmap(s);
				setPreview(imageBitmap);
			}
		} else {
			TCUtil.recycleBitmap(imageBitmap);
			imageBitmap = Bitmap.createBitmap(bitmap);
			setPreview(imageBitmap);
		}
		if (s1 != null)
			setStatusEdit(s1);
	}

	private void initModel() {
		userName = WeiboSettings.getUserName(this);
		password = WeiboSettings.getPassword(this);
		token = WeiboSettings.getToken(this);
		tokenSecret = WeiboSettings.getTokenSecret(this);
		weibo = new Weibo();
		if (token != null && tokenSecret != null)
			weibo.setOAuthAccessToken(new AccessToken(token, tokenSecret));
		locationManager = (LocationManager) getSystemService("location");
	}

	private void initView() {
		friendsButton = (ImageView) findViewById(0x7f0a026c);
		friendsButton.setOnClickListener(this);
		sendButton = (TextView) findViewById(0x7f0a0002);
		sendButton.setOnClickListener(this);
		backButton = (TextView) findViewById(0x7f0a0000);
		backButton.setOnClickListener(this);
		statusText = (TextView) findViewById(0x7f0a026d);
		restCountText = (TextView) findViewById(0x7f0a026e);
		takePhotoButton = (ImageView) findViewById(0x7f0a026b);
		takePhotoButton.setOnClickListener(this);
		takeCameraButton = (ImageView) findViewById(0x7f0a026a);
		takeCameraButton.setOnClickListener(this);
		photoPreviewButton = (ImageView) findViewById(0x7f0a0269);
		photoPreviewButton.setOnClickListener(this);
		locationButton = (ImageView) findViewById(0x7f0a0070);
		locationButton.setOnClickListener(this);
		statusEdit = (EditText) findViewById(0x7f0a0268);
		statusEdit.addTextChangedListener(this);
	}

	private void login() {
		if (textAvailable(token) && textAvailable(tokenSecret))
			doLogin();
		else
			showDialog(1001);
	}

	private void removeUpdates() {
		if (locationState)
			locationManager.removeUpdates(listener);
	}

	private void requestLocationUpdates() {
		if (locationState) {
			if (!TCUtil.isGPSAvailable(this))
				TCUtil.showTip(this, 0x7f08004b);
			String s = TCUtil.getBestProvider(locationManager);
			locationManager.requestLocationUpdates(s, 1000L, 0.0F, listener);
		}
	}

	private void setNullPreview() {
		TCUtil.recycleBitmap(scaledImageBitmap);
		scaledImageBitmap = null;
		TCUtil.recycleBitmap(imageBitmap);
		imageBitmap = null;
		photoPreviewButton.setImageBitmap(scaledImageBitmap);
	}

	private void setPreview(Bitmap bitmap) {
		TCUtil.recycleBitmap(scaledImageBitmap);
		scaledImageBitmap = TCUtil.scaleBitmap(bitmap, 50, 50);
		photoPreviewButton.setImageBitmap(scaledImageBitmap);
	}

	private void setRestCount(int i) {
		restCountText.setText(String.valueOf(i));
	}

	private void setStatusEdit(String s) {
		statusEdit.setText(s);
	}

	private void showProgressDialog(String s) {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(0);
		progressDialog.setTitle(getString(0x7f080048));
		progressDialog.setMessage(s);
		if (!progressDialog.isShowing())
			progressDialog.show();
	}

	private void showToast(String s) {
		Toast.makeText(this, s, 1).show();
	}

	private void status() {
		if (textAvailable(token) && textAvailable(tokenSecret))
			doStatus();
		else if (textAvailable(password) && textAvailable(userName))
			doLogin();
		else
			showDialog(1001);
	}

	private boolean textAvailable(String s) {
		boolean flag;
		if (s != null && s.length() > 0)
			flag = true;
		else
			flag = false;
		return flag;
	}

	private void updateLocation(Location location1) {
		if (location1 != null)
			location = location1;
	}

	public void afterTextChanged(Editable editable) {
		if (editable.length() > WEIBO_STATUS_LIMIT_COUNT)
			editable.delete(WEIBO_STATUS_LIMIT_COUNT, editable.length());
	}

	public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
	}

	protected void onActivityResult(int i, int j, Intent intent) {
		if (j == -1) { // goto _L2; else goto _L1
			if (i == 1000) {
				TCUtil.recycleBitmap(imageBitmap);
				imageBitmap = TCUtil.getPhotoBitmap(this, intent);
				setPreview(imageBitmap);
			} else if (i == 1001) {
				TCUtil.recycleBitmap(imageBitmap);
				imageBitmap = getCameraBitmap(null);
				setPreview(imageBitmap);
			}

		}
		super.onActivityResult(i, j, intent);

	}

	public void onBackPressed() {
		super.onBackPressed();
		if (weiboAsyncTask != null)
			weiboAsyncTask.cancel();
		File file = new File(CAMERA_PATH);
		if (file.exists())
			file.delete();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case 2131361792:
			onBackPressed();
			break;
		case 2131361794:
			status();
			break;
		case 2131361904:
			locationState = !locationState;
			location = null;
			if (locationState) {
				if (locationManager != null) {
					if (!TCUtil.isGPSAvailable(this)) {
						TCUtil.showTip(this, 0x7f08004b);
						locationState = false;
						break;
					}
					String s = TCUtil.getBestProvider(locationManager);
					locationManager.requestLocationUpdates(s, 1000L, 0.0F, listener);
					location = TCUtil.getBestLocation(this);
				}
				locationButton.setImageResource(0x7f0200c4);
			} else {
				locationManager.removeUpdates(listener);
				locationButton.setImageResource(0x7f0200c0);
			}
			break;
		case 2131362409:
			if (scaledImageBitmap != null)
				showDialog(1002);
			break;
		case 2131362410:
			WeiboUtil.startCameraForResult(this, 1001);
			break;
		case 2131362411:
			TCUtil.selectImage(this, 1000);
			break;
		case 2131362412:
			getFriendList();
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030075);
		initModel();
		initView();
		initContorl();
	}

	protected Dialog onCreateDialog(int i) {
		Dialog dialog = null;
		switch (i) {
		case 1000:
			dialog = createFriendListDialog();
			break;
		case 1001:
			dialog = createLoginDialog();
			break;
		case 1002:
			dialog = createPreviewImageDialog();
			break;
		case 1003:
			dialog = createUpdateStatusDialog();
			break;
		}
		return dialog;
	}

	protected void onDestroy() {
		super.onDestroy();
		TCUtil.recycleBitmap(scaledImageBitmap);
		TCUtil.recycleBitmap(imageBitmap);
	}

	protected void onPause() {
		removeUpdates();
		TCUtil.hideSoftKeyBroad(this, statusEdit);
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
		requestLocationUpdates();
		if (TCUtil.isNetAvailable(this)) {
			if (textAvailable(token) && textAvailable(tokenSecret))
				statusText.setText(getString(0x7f080040));
		} else
			TCUtil.showNetErrorDialog(this);

	}

	public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
		setRestCount(WEIBO_STATUS_LIMIT_COUNT - charsequence.length());
	}

	public static final String CAMERA_PATH = (new StringBuilder()).append(TCUtil.getSDPath()).append("TouchChina").append("/tc_comment.png")
			.toString();
	public static final int DIALOG_ID_FRIEND_LIST = 1000;
	public static final int DIALOG_ID_LOGIN = 1001;
	public static final int DIALOG_ID_PREVIEW_IMAGE = 1002;
	public static final int DIALOG_ID_UPDATE_STATUS = 1003;
	public static final String KEY_HAS_CAMERA = "has_camera";
	public static final String KEY_ICON_BITMAP = "icon_bitmap";
	public static final String KEY_PATH_CAMERA = "path_camera";
	public static final String KEY_STATUS = "status";
	private static final String TAG = WeiboActivity.class.getSimpleName();
	private static final int TAKE_CAMERA_REQUEST_CODE = 1001;
	private static final int TAKE_PHOTO_RESQUEST_CODE = 1000;
	private static int WEIBO_STATUS_LIMIT_COUNT = 140;
	private TextView backButton;
	private ImageView friendsButton;
	private ArrayList friendsList;
	Handler handler;
	private Bitmap imageBitmap;
	private ListView listView;
	private LocationListener listener;
	private Location location;
	private ImageView locationButton;
	private LocationManager locationManager;
	private boolean locationState;
	String password;
	private EditText passwordEdit;
	private ImageView photoPreviewButton;
	private ProgressDialog progressDialog;
	private TextView restCountText;
	private Bitmap scaledImageBitmap;
	private TextView sendButton;
	private String status;
	private EditText statusEdit;
	private TextView statusText;
	private ImageView takeCameraButton;
	private ImageView takePhotoButton;
	private String token;
	private String tokenSecret;
	private long uid;
	String userName;
	private EditText userNameEdit;
	private Weibo weibo;
	private WeiboAsyncTask weiboAsyncTask;

	static {
		Weibo.CONSUMER_KEY = "3780182769";
		Weibo.CONSUMER_SECRET = "832c9b655611271fa6f662c0281f3493";
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
	}

	/*
	 * static String access$1402(WeiboActivity weiboactivity, String s) {
	 * weiboactivity.token = s; return s; }
	 */

	/*
	 * static String access$1502(WeiboActivity weiboactivity, String s) {
	 * weiboactivity.tokenSecret = s; return s; }
	 */

	/*
	 * static Weibo access$1602(WeiboActivity weiboactivity, Weibo weibo1) {
	 * weiboactivity.weibo = weibo1; return weibo1; }
	 */

	/*
	 * static long access$1802(WeiboActivity weiboactivity, long l) {
	 * weiboactivity.uid = l; return l; }
	 */

	/*
	 * static ArrayList access$2002(WeiboActivity weiboactivity, ArrayList
	 * arraylist) { weiboactivity.friendsList = arraylist; return arraylist; }
	 */

}
