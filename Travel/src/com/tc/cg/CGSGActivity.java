//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tc.TCApplication;
import com.tc.TCData;
import com.tc.TCUtil;
import com.tc.logic.CGDownData;
import com.tc.mall.MallStartActivity;
import com.tc.net.DownAsyncTask;
import com.tc.net.DownLoadUpdateAsyncTask;
import com.tc.net.GetTimeData;
import com.tc.sg.SGStartActivity;
import com.touchchina.cityguide.sz.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData, CGRecommendItemActivity

public class CGSGActivity extends CGBaseActivity {
	private class DownloadSimpeAdapter extends SimpleAdapter {

		private void bindView(final int i, View view) {
			Map map = (Map) mData.get(i);
			if (map != null) {
				View aview[] = (View[]) (View[]) view.getTag();
				String as[] = mFrom;
				int j = mTo.length;
				int k = 0;
				while (k < j) {
					View view1 = aview[k];
					if (view1 != null) {
						Object obj = map.get(as[k]);
						if (view1 instanceof ImageView) {
							if (obj instanceof Integer) {
								view1.setVisibility(0);
								((ImageView) view1).setImageResource(((Integer) obj).intValue());
							} else if (obj instanceof Bitmap) {
								view1.setVisibility(0);
								((ImageView) view1).setImageBitmap((Bitmap) obj);
							} else if (obj instanceof Boolean)
								if (!((Boolean) obj).booleanValue())
									view1.setVisibility(8);
								else
									view1.setVisibility(0);
						} else if (view1 instanceof Button) {
							if (obj instanceof Boolean) {
								if (!((Boolean) obj).booleanValue())
									view1.setVisibility(8);
							} else if (obj instanceof String) {
								view1.setVisibility(0);
								((Button) view1).setText((String) obj);
							}
						} else if (view1 instanceof TextView) {
							if (obj instanceof Boolean) {
								if (((Boolean) obj).booleanValue())
									view1.setVisibility(0);
								else
									view1.setVisibility(8);
							} else if (obj instanceof String) {
								view1.setVisibility(0);
								((TextView) view1).setText((String) obj);
							}
						} else if (view1 instanceof ProgressBar) {
							if (obj instanceof Boolean) {
								if (((Boolean) obj).booleanValue())
									view1.setVisibility(0);
								else
									view1.setVisibility(8);
							} else if (obj instanceof Integer) {
								view1.setVisibility(0);
								((ProgressBar) view1).setProgress(((Integer) obj).intValue());
							}
						} else if (view1 instanceof CheckBox) {
							((CheckBox) view1).setChecked(((Boolean) obj).booleanValue());
							((CheckBox) view1).setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

								public void onCheckedChanged(CompoundButton compoundbutton, boolean flag) {
									isCheckeds[i] = flag;
								}

							});
						}
					}
					k++;
				}
			}
		}

		private View getView(int i, View view, ViewGroup viewgroup, int j) {
			View view1;
			if (view == null) {
				view1 = mInflater.inflate(j, viewgroup, false);
				int ai[] = mTo;
				int k = ai.length;
				View aview[] = new View[k];
				for (int l = 0; l < k; l++)
					aview[l] = view1.findViewById(ai[l]);

				view1.setTag(aview);
			} else {
				view1 = view;
			}
			bindView(i, view1);
			return view1;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			View view1 = getView(i, view, viewgroup, mResource);
			if (needColor)
				view1.setBackgroundColor(colors[i % colors.length]);
			return view1;
		}

		private int colors[] = { 0xff98989c, 0xffadadb0 };
		private List mData;
		private String mFrom[];
		private LayoutInflater mInflater;
		private int mResource;
		private int mTo[];
		private boolean needColor;

		public DownloadSimpeAdapter(boolean flag, Context context, List list, int i, String as[], int ai[]) {

			super(context, list, i, as, ai);
			needColor = flag;
			mData = list;
			mResource = i;
			mFrom = as;
			mTo = ai;
			mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
		}
	}

	public CGSGActivity() {
		progressHandler = new Handler() {

			public void handleMessage(Message message) {
				switch (message.what) {
				case 0:
					progressDialog = ProgressDialog.show(CGSGActivity.this, "", getString(0x7f08008b), true, false);
					timeHandler.removeMessages(0);

					break;

				case 1:
					progressDialog.dismiss();
					refreshDone();
					if (allSGLatestTimeStamp != null) {
						refreshIng();
						refreshLib();
						refreshUpdate();
					}
					timeHandler.sendEmptyMessage(0);
					break;

				}

			}

		};
		timeHandler = new Handler() {

			public void handleMessage(Message message) {
				Log.v("", "\u5728\u53D1\u6D88\u606F");
				refreshIng();
				if (hashMaps_ing.size() > 0)
					sendEmptyMessageDelayed(0, 1000L);
			}

		};
	}

	private String DBType2WebType(String s) {

		if ("site".equalsIgnoreCase(s)) // goto _L2; else goto _L1
			return "site";
		else if ("shopping".equalsIgnoreCase(s))
			return "mall";
		;
		return null;
	}

	private void addNewDownAsyncTask(CGData.CGSGData.SGItem sgitem, boolean flag) {
		if (!isDownloading(sgitem)) {
			final TCApplication tcapplication = (TCApplication) getApplicationContext();
			// tcapplication.getClass();

			com.tc.TCApplication.DownloadingInfo downloadinginfo = tcapplication.new DownloadingInfo();
			tcapplication.downloadingInfos.add(downloadinginfo);

			downloadinginfo.name = sgitem.name;
			downloadinginfo.guideid = sgitem.sgId;
			downloadinginfo.isDownloading = flag;
			Handler handler = new Handler() {

				public void handleMessage(Message message) {
					switch (message.what) {
					case 0:
						long al[] = (long[]) message.obj;

						for (com.tc.TCApplication.DownloadingInfo downloadinginfo : tcapplication.downloadingInfos) {
							if ((long) downloadinginfo.guideid == al[2]) {

								downloadinginfo.hasDownloaded = al[0];
								downloadinginfo.timeoutCount = 0;
								downloadinginfo.TOTAL = al[1];
								if (downloadinginfo.hasDownloaded == downloadinginfo.TOTAL && downloadinginfo.hasDownloaded > 0L) {
									downloadinginfo.isDownloading = false;
									downloadinginfo.isUnZip = true;
								} else {
									downloadinginfo.isDownloading = true;
									downloadinginfo.isUnZip = false;
								}
								break;
							}
						}

						break;
					case 1:

						for (com.tc.TCApplication.DownloadingInfo downloadinginfo : tcapplication.downloadingInfos) {
							if ((long) downloadinginfo.guideid == ((long[]) message.obj)[0]) {
								downloadinginfo.isDownloading = false;
								downloadinginfo.timeoutCount = 0;
								long l = ((long[]) (long[]) message.obj)[1];
								downloadinginfo.TOTAL = l;
								downloadinginfo.hasDownloaded = l;
								downloadinginfo.isUnZip = true;

								break;
							}
						}

					case 2:

						for (com.tc.TCApplication.DownloadingInfo downloadinginfo : tcapplication.downloadingInfos) {
							if (downloadinginfo.guideid == ((Integer) message.obj).intValue()) {
								if (!downloadinginfo.isDownloading) {
									downloadinginfo.isDownloading = false;
									downloadinginfo.isUnZip = false;
									downloadinginfo.timeoutCount = 0;
								}

								break;
							}
						}
						refreshDone();
						refreshLib();
						break;
					case 3:

						for (com.tc.TCApplication.DownloadingInfo downloadinginfo : tcapplication.downloadingInfos) {
							if (downloadinginfo.guideid == ((int[]) (int[]) message.obj)[0]) {
								tcapplication.downloadingInfos.remove(downloadinginfo);
								break;
							}
						}
						setHasDownloaded(((int[]) (int[]) message.obj)[0], ((int[]) (int[]) message.obj)[1]);
						refreshDone();
						refreshLib();
						break;
					case 4:

						for (com.tc.TCApplication.DownloadingInfo downloadinginfo : tcapplication.downloadingInfos) {
							if (downloadinginfo.guideid == ((Integer) message.obj).intValue()) {

								DownLoadUpdateAsyncTask downloadupdateasynctask = new DownLoadUpdateAsyncTask(new Handler() {

									public void handleMessage(Message message) {
										if (message.what == 0) {
											allSGLatestTimeStamp = (HashMap) message.obj;
											for (CGData.CGSGData.SGItem sgitem : CG_SITE_DATAS) {

												File file = new File((new StringBuilder()).append(TCData.SG_ROOT).append(sgitem.sgId)
														.append("/Cache/").toString());
												if (file.exists())
													if (file.list().length == 0) {
														Log.v("",
																(new StringBuilder())
																		.append(TCData.SG_ROOT)
																		.append(sgitem.sgId)
																		.append("/Cache/\u951F\u4FA5\u7877\u62F7\u951F\u65A4\u62F7\u4E3A\u951F\u65A4\u62F7")
																		.toString());
													} else {
														String as[] = file.list()[0].split("_");
														addNewDownAsyncTask(sgitem, false);
														((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos
																.get(-1 + ((TCApplication) getApplicationContext()).downloadingInfos.size())).hasDownloaded = (new File(
																(new StringBuilder()).append(TCData.SG_ROOT).append(sgitem.sgId).append("/Cache/")
																		.append(file.list()[0]).toString())).length();
														((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos
																.get(-1 + ((TCApplication) getApplicationContext()).downloadingInfos.size())).TOTAL = Long
																.parseLong(as[1]);
													}

											}
											refreshUpdate();
										}

									}

								});
								// DownLoadUpdateAsyncTask
								// downloadupdateasynctask =
								// downLoadUpdateAsyncTask;
								Object aobj[] = new Object[3];
								aobj[0] = "getCGAllSGTimeStamp";
								aobj[1] = CGDownData.getCGGuideLastUpdateParams((new StringBuilder()).append("").append(CG_ID).toString(), "all");
								aobj[2] = (new StringBuilder()).append(CGDownData.getUrl()).append("cgallsgtimestamp").toString();
								downloadupdateasynctask.execute(aobj);
								downloadinginfo.downAsyncTask = new DownAsyncTask(downloadinginfo.handler);
								downloadinginfo.timeoutCount = 0;
								startDownAsyncTaskByGuideId(downloadinginfo.guideid, downloadinginfo.downAsyncTask);

								break;
							}
						}
						break;

					case 5:
						if (!TCUtil.isNetAvailable(CGSGActivity.this))
							break;

						for (com.tc.TCApplication.DownloadingInfo downloadinginfo : tcapplication.downloadingInfos) {
							if (downloadinginfo.guideid == ((Integer) message.obj).intValue()) {
								if (downloadinginfo.timeoutCount > 10) {
									downloadinginfo.timeoutCount = 0;
									downloadinginfo.isDownloading = false;
									downloadinginfo.isUnZip = false;
								} else {
									downloadinginfo.timeoutCount = 1 + downloadinginfo.timeoutCount;
									downloadinginfo.downAsyncTask = new DownAsyncTask(downloadinginfo.handler);
									startDownAsyncTaskByGuideId(downloadinginfo.guideid, downloadinginfo.downAsyncTask);
								}

								break;
							}
						}
						break;

					}

				}

			};
			downloadinginfo.handler = handler;
			DownAsyncTask downasynctask = new DownAsyncTask(handler);
			downloadinginfo.downAsyncTask = downasynctask;
			if (flag) {
				String s = (new StringBuilder()).append(TCData.SG_ROOT).append(sgitem.sgId).append("/Cache/").toString();
				String s1 = (new StringBuilder())
						.append(((com.tc.net.DownLoadUpdateAsyncTask.DownLoadUpdateData) allSGLatestTimeStamp.get(Integer.valueOf(sgitem.sgId))).timestamp)
						.append("_0").toString();
				(new File(s)).mkdirs();
				try {
					(new File((new StringBuilder()).append(s).append(s1).toString())).createNewFile();
				} catch (IOException ioexception) {
					ioexception.printStackTrace();
				}
				startDownAsyncTaskByGuideId(sgitem.sgId, downasynctask);
			}
		}
	}

	private Bitmap getSGIcon(Context context, int i) {
		Bitmap bitmap = null;
		Iterator iterator = CG_SITE_DATAS.iterator();
		do {
			if (!iterator.hasNext())
				break;
			CGData.CGSGData.SGItem sgitem = (CGData.CGSGData.SGItem) iterator.next();
			if (sgitem.sgId != i)
				continue;
			bitmap = TCUtil.getRoundedCornerBitmap(TCUtil.getBitmap(context,
					(new StringBuilder()).append(sgitem.icon.substring(0, sgitem.icon.lastIndexOf("icon.jpg"))).append("sgicon.jpg").toString()));
			if (!TCData.USE_2X)
				bitmap = TCUtil.scaleBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
			break;
		} while (true);
		return bitmap;
	}

	private boolean hasDownloaded(CGData.CGSGData.SGItem sgitem) {
		boolean flag = false;
		Iterator iterator = CG_SITE_DATAS.iterator();
		do {
			if (!iterator.hasNext())
				break;
			CGData.CGSGData.SGItem sgitem1 = (CGData.CGSGData.SGItem) iterator.next();
			if (sgitem1.sgId != sgitem.sgId || sgitem1.timestamp <= 0)
				continue;
			flag = true;
			break;
		} while (true);
		return flag;
	}

	private boolean isDownloading(CGData.CGSGData.SGItem sgitem) {
		for (com.tc.TCApplication.DownloadingInfo down : ((TCApplication) getApplicationContext()).downloadingInfos) {
			if (down.guideid == sgitem.sgId)
				return true;

		}
		return false;

	}

	private void refreshDone() {
		hashMaps_done.clear();
		Iterator iterator = CG_SITE_DATAS.iterator();
		do {
			if (!iterator.hasNext())
				break;
			CGData.CGSGData.SGItem sgitem = (CGData.CGSGData.SGItem) iterator.next();
			if (sgitem.timestamp > 0 && !TCUtil.isUnZipInterrapted("sg", sgitem.sgId, false)) {
				HashMap hashmap = new HashMap();
				hashmap.put("guideid", Integer.valueOf(sgitem.sgId));
				hashmap.put("image", getSGIcon(this, sgitem.sgId));
				hashmap.put("text", sgitem.name);
				hashMaps_done.add(hashmap);
			}
		} while (true);
		if (radioGroup.getCheckedRadioButtonId() == 0x7f0a01a3)
			if (hashMaps_done.isEmpty()) {
				gridViewBG.setImageResource(0x7f020026);
				download_manageIV.setVisibility(8);
			} else {
				gridViewBG.setImageResource(0x7f02000d);
				download_manageIV.setVisibility(0);
				download_manageIV.setImageResource(0x7f020064);
			}
		downloadSimpeAdapters[0].notifyDataSetChanged();
	}

	private void refreshIng() {
		hashMaps_ing.clear();
		Iterator iterator = ((TCApplication) getApplicationContext()).downloadingInfos.iterator();
		while (iterator.hasNext()) {
			com.tc.TCApplication.DownloadingInfo downloadinginfo = (com.tc.TCApplication.DownloadingInfo) iterator.next();
			HashMap hashmap = new HashMap();
			hashmap.put("guideid", Integer.valueOf(downloadinginfo.guideid));
			hashmap.put("icon", getSGIcon(this, downloadinginfo.guideid));
			hashmap.put("name", downloadinginfo.name);
			int i;
			String s;
			Object obj;
			if (downloadinginfo.TOTAL == 0L)
				i = 0;
			else
				i = (int) ((100D * (double) downloadinginfo.hasDownloaded) / (double) downloadinginfo.TOTAL);
			hashmap.put("progress_bar", Integer.valueOf(i));
			s = (new StringBuilder()).append(TCUtil.getDownloadFileSize(downloadinginfo.hasDownloaded)).append("/")
					.append(TCUtil.getDownloadFileSize(downloadinginfo.TOTAL)).toString();
			if (s.equals("0B/0B"))
				s = getString(0x7f08008f);
			hashmap.put("progress_text", s);
			if (downloadinginfo.isUnZip) {
				obj = Boolean.valueOf(false);
			} else {
				int j;
				if (downloadinginfo.isDownloading)
					j = 0x7f02006f;
				else
					j = 0x7f02005f;
				obj = Integer.valueOf(j);
			}
			hashmap.put("button", obj);
			hashmap.put("unzip", Boolean.valueOf(downloadinginfo.isUnZip));
			if (downloadinginfo.TOTAL == 0L)
				hashmap.put("progress_percent", "0.0%");
			else
				hashmap.put(
						"progress_percent",
						(new StringBuilder())
								.append(TCUtil.formatNum((100D * (double) downloadinginfo.hasDownloaded) / (double) downloadinginfo.TOTAL))
								.append("%").toString());
			hashMaps_ing.add(hashmap);
		}
		if (radioGroup.getCheckedRadioButtonId() == 0x7f0a01a5)
			if (hashMaps_ing.isEmpty()) {
				download_manageIV.setVisibility(8);
				download_list_layout.setVisibility(8);
			} else {
				download_list_layout.setVisibility(0);
				download_manageIV.setVisibility(0);
				download_manageIV.setImageResource(0x7f020064);
			}
		downloadSimpeAdapters[2].notifyDataSetChanged();
	}

	private void refreshLib() {
		hashMaps_lib.clear();
		boolean flag = true;
		Iterator iterator = CG_SITE_DATAS.iterator();
		while (iterator.hasNext()) {
			CGData.CGSGData.SGItem sgitem = (CGData.CGSGData.SGItem) iterator.next();
			HashMap hashmap = new HashMap();
			hashmap.put("icon", getSGIcon(this, sgitem.sgId));
			hashmap.put("type", sgitem.siteType.split(",")[0]);
			hashmap.put("name", sgitem.name);
			hashmap.put(
					"date",
					(new StringBuilder())
							.append(TCUtil.timeStamp2YYMMDD(((com.tc.net.DownLoadUpdateAsyncTask.DownLoadUpdateData) allSGLatestTimeStamp.get(Integer
									.valueOf(sgitem.sgId))).timestamp)).append(getString(0x7f080088)).toString());
			if (hasDownloaded(sgitem)) {
				hashmap.put("status", getString(0x7f08009d));
				flag &= true;
			} else if (isDownloading(sgitem)) {
				hashmap.put("status", getString(0x7f08009e));
				flag &= true;
			} else {
				hashmap.put("status", Boolean.valueOf(false));
				flag &= false;
			}
			hashMaps_lib.add(hashmap);
		}
		if (radioGroup.getCheckedRadioButtonId() == 0x7f0a01a4)
			if (hashMaps_lib.isEmpty()) {
				download_manageIV.setVisibility(8);
				download_list_layout.setVisibility(8);
			} else if (flag) {
				download_manageIV.setVisibility(8);
			} else {
				download_list_layout.setVisibility(0);
				download_manageIV.setVisibility(0);
				download_manageIV.setImageResource(0x7f020057);
			}
		downloadSimpeAdapters[1].notifyDataSetChanged();
	}

	private void refreshUpdate() {
		hashMaps_update.clear();
		Iterator iterator = CG_SITE_DATAS.iterator();
		do {
			if (!iterator.hasNext())
				break;
			CGData.CGSGData.SGItem sgitem = (CGData.CGSGData.SGItem) iterator.next();
			if (sgitem.timestamp > 0
					&& ((com.tc.net.DownLoadUpdateAsyncTask.DownLoadUpdateData) allSGLatestTimeStamp.get(Integer.valueOf(sgitem.sgId))).timestamp > sgitem.timestamp) {
				HashMap hashmap = new HashMap();
				hashmap.put("guideid", Integer.valueOf(sgitem.sgId));
				hashmap.put("icon", getSGIcon(this, sgitem.sgId));
				hashmap.put("type", sgitem.siteType);
				hashmap.put("name", sgitem.name);
				hashmap.put(
						"date",
						(new StringBuilder())
								.append(TCUtil.timeStamp2YYMMDD(((com.tc.net.DownLoadUpdateAsyncTask.DownLoadUpdateData) allSGLatestTimeStamp
										.get(Integer.valueOf(sgitem.sgId))).timestamp)).append(getResources().getString(0x7f080088)).toString());
				hashMaps_update.add(hashmap);
			}
		} while (true);
		RadioButton radiobutton;
		StringBuilder stringbuilder;
		Object obj;
		if (radioGroup.getCheckedRadioButtonId() == 0x7f0a01a6)
			if (hashMaps_update.isEmpty()) {
				download_manageIV.setVisibility(8);
				download_list_layout.setVisibility(8);
			} else {
				download_list_layout.setVisibility(0);
				download_manageIV.setVisibility(0);
				download_manageIV.setImageResource(0x7f02006e);
			}
		radiobutton = radioButton;
		stringbuilder = (new StringBuilder()).append(getResources().getString(0x7f080088)).append(" ");
		if (hashMaps_update.size() == 0)
			obj = "";
		else
			obj = Integer.valueOf(hashMaps_update.size());
		radiobutton.setText(stringbuilder.append(obj).toString());
		downloadSimpeAdapters[3].notifyDataSetChanged();
	}

	private void setHasDownloaded(int i, int j) {
		Iterator iterator = CG_SITE_DATAS.iterator();
		do {
			if (!iterator.hasNext())
				break;
			CGData.CGSGData.SGItem sgitem = (CGData.CGSGData.SGItem) iterator.next();
			if (sgitem.sgId != i)
				continue;
			sgitem.timestamp = j;
			break;
		} while (true);
		Iterator iterator1 = hashMaps_update.iterator();
		do {
			if (!iterator1.hasNext())
				break;
			HashMap hashmap = (HashMap) iterator1.next();
			if (((Integer) hashmap.get("guideid")).intValue() != i)
				continue;
			hashMaps_update.remove(hashmap);
			break;
		} while (true);
		if (hashMaps_update.size() > 0)
			radioButton.setText((new StringBuilder()).append(getResources().getString(0x7f080088)).append(" ").append(hashMaps_update.size())
					.toString());
		else
			radioButton.setText(0x7f080088);
		downloadSimpeAdapters[3].notifyDataSetChanged();
	}

	private void showSGGuideItem(int i, String s, int j) {
		Bundle bundle = new Bundle();
		String s1 = "";
		CGData.CGSGData.SGItem sgitem = null;
		int k = 0;
		while (k < CG_DATA.CG_SITE_DATAS.size()) {
			if (i == sgitem.id) {
				s1 = (new StringBuilder()).append(CG_DATA.CG_PATH).append(sgitem.type.toLowerCase()).append("/").append(i).append("/").toString();
				CGData.CGRecommendData.CGRecommendItem cgrecommenditem = CGData.CGRecommendData.CGRecommendItem.Parser.parse(this, s1);
				cgrecommenditem.icon = (new StringBuilder()).append(s1).append("sgicon.jpg").toString();
				if (s != null && s.length() > 0)
					cgrecommenditem.content = s;
				cgrecommenditem.name = sgitem.name;
				bundle.putSerializable(CGRecommendItemActivity.KEY_ITEM, cgrecommenditem);
				bundle.putInt(CGRecommendItemActivity.KEY_ID, i);
				bundle.putInt(CGRecommendItemActivity.KEY_UPDATE_TIMESTAMP, j);
				startActivityForResult(this, CGRecommendItemActivity.class, bundle, 1000);
				break;
			}
			k++;
		}

	}

	private void startDownAsyncTaskByGuideId(int i, DownAsyncTask downasynctask) {
		Iterator iterator = CG_SITE_DATAS.iterator();
		do {
			if (!iterator.hasNext())
				break;
			CGData.CGSGData.SGItem sgitem = (CGData.CGSGData.SGItem) iterator.next();
			if (sgitem.sgId != i)
				continue;
			Map map = CGDownData
					.getSGDownParams(
							(new StringBuilder()).append("").append(CG_ID).toString(),
							DBType2WebType(sgitem.type),
							(new StringBuilder()).append("").append(sgitem.sgId).toString(),
							(new StringBuilder()).append("").append(sgitem.timestamp).toString(),
							(new StringBuilder())
									.append("")
									.append(((com.tc.net.DownLoadUpdateAsyncTask.DownLoadUpdateData) allSGLatestTimeStamp.get(Integer
											.valueOf(sgitem.sgId))).timestamp).toString(), GetTimeData.getGotTime(this));
			Object aobj[] = new Object[4];
			aobj[0] = "get";
			aobj[1] = TCData.SG_ROOT;
			aobj[2] = map;
			aobj[3] = (new StringBuilder()).append(CGDownData.getUrl()).append("cgguidedown").toString();
			downasynctask.execute(aobj);
			break;
		} while (true);
	}

	protected void onActivityResult(int i, int j, Intent intent) {
		super.onActivityResult(i, j, intent);
		if (1000 == i) { // goto _L2; else goto _L1
			if (j == 77)
				radioGroup.check(0x7f0a01a5);
			else if (j == 7) {
				int k = intent.getExtras().getInt(CGRecommendItemActivity.KEY_ID);
				for (CGData.CGSGData.SGItem sgitem : CG_SITE_DATAS) {
					if (sgitem.id == k) {
						addNewDownAsyncTask(sgitem, true);
						switch (radioGroup.getCheckedRadioButtonId()) {
						case 2131362213:
						default:
							break;

						case 2131362212:
							TCUtil.flurryLog("SiteGuideDownload", sgitem.name);
							refreshLib();
							break;

						case 2131362214:
							TCUtil.flurryLog("SiteGuideUpdate", sgitem.name);
							for (HashMap hashmap : hashMaps_update) {
								if (((Integer) hashmap.get("guideid")).intValue() == sgitem.sgId) {
									hashMaps_update.remove(hashmap);
									break;
								}
							}
							refreshUpdate();
							break;

						}
						break;
					}
				}
			}
		}

	}

	public void onBackPressed() {
		super.onBackPressed();
		timeHandler.removeMessages(0);
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Log.v("", "====================onCreate");
		if (!TCUtil.isNetAvailable(this)) {
			android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
			builder.setTitle(getString(0x7f08007f));
			builder.setMessage(getString(0x7f080080));
			builder.setNegativeButton(getString(0x7f080082), null);
			builder.setPositiveButton(getString(0x7f080081), new android.content.DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialoginterface, int i) {
					dialoginterface.dismiss();
					TCUtil.startWirelessSetting(CGSGActivity.this);
				}

			});
			builder.create().show();
		}
		sharedPreferences = getSharedPreferences(TAG, 0);
		connectivityManager = (ConnectivityManager) getSystemService("connectivity");
		connectivityChanged = new BroadcastReceiver() {

			public void onReceive(Context context, Intent intent) {
				if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
					networkInfo = connectivityManager.getActiveNetworkInfo();
					if (networkInfo != null) {
						if (allSGLatestTimeStamp == null) {
							downLoadUpdateAsyncTask = new DownLoadUpdateAsyncTask(new Handler() {

								public void handleMessage(Message message) {
									if (message.what == 0) {
										relativeLayout.setVisibility(8);
										allSGLatestTimeStamp = (HashMap) message.obj;
										for (CGData.CGSGData.SGItem sgitem : CG_SITE_DATAS) {
											File file = new File((new StringBuilder()).append(TCData.SG_ROOT).append(sgitem.sgId).append("/Cache/")
													.toString());
											if (file.exists() && file.list().length != 0) {
												String as[] = file.list()[0].split("_");
												addNewDownAsyncTask(sgitem, false);
												((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos
														.get(-1 + ((TCApplication) getApplicationContext()).downloadingInfos.size())).hasDownloaded = (new File(
														(new StringBuilder()).append(TCData.SG_ROOT).append(sgitem.sgId).append("/Cache/")
																.append(file.list()[0]).toString())).length();
												((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos
														.get(-1 + ((TCApplication) getApplicationContext()).downloadingInfos.size())).TOTAL = Long
														.parseLong(as[1]);
												refreshIng();
											}
										}
										switch (radioGroup.getCheckedRadioButtonId()) {
										case R.id.download_lib:
											download_list_layout.setVisibility(0);
											refreshLib();
											break;
										case R.id.download_ing:
											download_list_layout.setVisibility(0);
											refreshIng();
											break;
										case R.id.download_update:
											download_list_layout.setVisibility(0);
											downloadSimpeAdapters[3].notifyDataSetChanged();
											break;
										}
										refreshUpdate();

									}

								}

							});
							DownLoadUpdateAsyncTask downloadupdateasynctask = downLoadUpdateAsyncTask;
							Object aobj[] = new Object[3];
							aobj[0] = "getCGAllSGTimeStamp";
							aobj[1] = CGDownData.getCGGuideLastUpdateParams((new StringBuilder()).append("").append(CG_ID).toString(), "all");
							aobj[2] = (new StringBuilder()).append(CGDownData.getUrl()).append("cgallsgtimestamp").toString();
							downloadupdateasynctask.execute(aobj);
						}
						editor = sharedPreferences.edit();
						if (networkInfo.getType() == 0) {
							Iterator iterator1 = ((TCApplication) getApplicationContext()).downloadingInfos.iterator();
							do {
								if (!iterator1.hasNext())
									break;
								com.tc.TCApplication.DownloadingInfo downloadinginfo1 = (com.tc.TCApplication.DownloadingInfo) iterator1.next();
								if (downloadinginfo1.isDownloading) {
									downloadinginfo1.isDownloading = false;
									downloadinginfo1.downAsyncTask.cancel();
									editor.putBoolean((new StringBuilder()).append("").append(downloadinginfo1.guideid).toString(), true);
									editor.commit();
								}
							} while (true);
						} else if (networkInfo.getType() == 1) {
							Iterator iterator = ((TCApplication) getApplicationContext()).downloadingInfos.iterator();
							do {
								if (!iterator.hasNext())
									break;
								com.tc.TCApplication.DownloadingInfo downloadinginfo = (com.tc.TCApplication.DownloadingInfo) iterator.next();
								if (sharedPreferences.getBoolean((new StringBuilder()).append("").append(downloadinginfo.guideid).toString(), false)
										&& (!downloadinginfo.isDownloading || !downloadinginfo.isUnZip)) {
									downloadinginfo.isDownloading = true;
									downloadinginfo.downAsyncTask = new DownAsyncTask(downloadinginfo.handler);
									editor.putBoolean((new StringBuilder()).append("").append(downloadinginfo.guideid).toString(), false);
									editor.commit();
									startDownAsyncTaskByGuideId(downloadinginfo.guideid, downloadinginfo.downAsyncTask);
								}
							} while (true);
						}
						refreshIng();
					}
				}
			}

		};
		registerReceiver(connectivityChanged, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
		CG_SITE_DATAS = CG_DATA.CG_SITE_DATAS;
		setContentView(0x7f03003f);
		findViewById(0x7f0a0153).setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				onBackPressed();
			}

		});
		hashMaps_checkBox = new ArrayList();
		download_manageIV = (ImageView) findViewById(0x7f0a01a2);
		download_manageIV.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {
				int i = 0;
				switch (radioGroup.getCheckedRadioButtonId()) {
				case 2131362211:
					if (!download_done) {
						download_done = true;
						hashMaps_checkBox.clear();
						isCheckeds = new boolean[hashMaps_done.size()];
						Iterator iterator4 = hashMaps_done.iterator();
						while (iterator4.hasNext()) {
							HashMap hashmap2 = (HashMap) iterator4.next();
							HashMap hashmap3 = new HashMap();
							int k;
							if (isCheckeds[i])
								k = 0x7f02005d;
							else
								k = 0x7f02006d;
							hashmap3.put("checked", Integer.valueOf(k));
							hashmap3.put("icon", getSGIcon(CGSGActivity.this, ((Integer) hashmap2.get("guideid")).intValue()));
							hashmap3.put("name", (String) hashmap2.get("text"));
							if (allSGLatestTimeStamp != null)
								hashmap3.put(
										"date",
										(new StringBuilder())
												.append(TCUtil
														.timeStamp2YYMMDD(((com.tc.net.DownLoadUpdateAsyncTask.DownLoadUpdateData) allSGLatestTimeStamp
																.get((Integer) hashmap2.get("guideid"))).timestamp)).append(getString(0x7f080088))
												.toString());
							else
								hashmap3.put("date", Boolean.valueOf(false));
							hashMaps_checkBox.add(hashmap3);
							i++;
						}
						checkListDownloadSimpeAdapter = new DownloadSimpeAdapter(false, CGSGActivity.this, hashMaps_checkBox, 0x7f030040,
								new String[] { "checked", "icon", "name", "date" }, new int[] { 0x7f0a01ad, 0x7f0a01ae, 0x7f0a01af, 0x7f0a01b0 });
						(new android.app.AlertDialog.Builder(CGSGActivity.this)).setTitle(0x7f080084)
								.setPositiveButton(0x7f08008c, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										try {
											Field field = dialoginterface.getClass().getSuperclass().getDeclaredField("mShowing");
											field.setAccessible(true);
											field.set(dialoginterface, Boolean.valueOf(true));
										} catch (Exception exception) {
										}
										(new Thread() {

											public void run() {
												progressHandler.sendEmptyMessage(0);
												for (int i = 0; i < isCheckeds.length; i++) {
													if (!isCheckeds[i])
														continue;
													for (CGData.CGSGData.SGItem sgitem : CG_SITE_DATAS) {
														if (sgitem.sgId == ((Integer) ((HashMap) hashMaps_done.get(i)).get("guideid")).intValue()) {
															TCUtil.flurryLog("SiteGuideDelete", sgitem.name);
															sgitem.timestamp = 0;
															for (TCApplication.DownloadingInfo downloadinginfo : ((TCApplication) getApplicationContext()).downloadingInfos) {
																if (downloadinginfo.guideid == sgitem.sgId) {
																	downloadinginfo.downAsyncTask.cancel();
																	((TCApplication) getApplicationContext()).downloadingInfos
																			.remove(downloadinginfo);
																	break;
																}
															}
															TCUtil.deleteEveryFile((new StringBuilder()).append(TCData.SG_ROOT).append(sgitem.sgId)
																	.append("/").toString());
															break;
														}
													}

												}

												progressHandler.sendEmptyMessage(1);
											}

										}).start();
										dialoginterface.dismiss();
										download_done = false;
									}

								}).setNegativeButton(0x7f08005a, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										try {
											Field field = dialoginterface.getClass().getSuperclass().getDeclaredField("mShowing");
											field.setAccessible(true);
											field.set(dialoginterface, Boolean.valueOf(true));
										} catch (Exception exception) {
										}
										dialoginterface.dismiss();
										download_done = false;
									}

								}).setAdapter(checkListDownloadSimpeAdapter, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										boolean flag = true;
										boolean aflag[];
										int j;
										Iterator iterator;
										try {
											Field field = dialoginterface.getClass().getSuperclass().getDeclaredField("mShowing");
											field.setAccessible(true);
											field.set(dialoginterface, Boolean.valueOf(false));
										} catch (Exception exception) {
										}
										aflag = isCheckeds;
										if (isCheckeds[i])
											flag = false;
										aflag[i] = flag;
										hashMaps_checkBox.clear();
										j = 0;
										iterator = hashMaps_done.iterator();
										while (iterator.hasNext()) {
											HashMap hashmap = (HashMap) iterator.next();
											HashMap hashmap1 = new HashMap();
											int k;
											if (isCheckeds[j])
												k = 0x7f02005d;
											else
												k = 0x7f02006d;
											hashmap1.put("checked", Integer.valueOf(k));
											hashmap1.put("icon", getSGIcon(CGSGActivity.this, ((Integer) hashmap.get("guideid")).intValue()));
											hashmap1.put("name", (String) hashmap.get("text"));
											if (allSGLatestTimeStamp != null)
												hashmap1.put(
														"date",
														(new StringBuilder())
																.append(TCUtil
																		.timeStamp2YYMMDD(((com.tc.net.DownLoadUpdateAsyncTask.DownLoadUpdateData) allSGLatestTimeStamp
																				.get((Integer) hashmap.get("guideid"))).timestamp))
																.append(getString(0x7f080088)).toString());
											else
												hashmap1.put("date", Boolean.valueOf(false));
											hashMaps_checkBox.add(hashmap1);
											j++;
										}
										checkListDownloadSimpeAdapter.notifyDataSetChanged();
									}

								}).setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

									public void onCancel(DialogInterface dialoginterface) {
										try {
											Field field = dialoginterface.getClass().getSuperclass().getDeclaredField("mShowing");
											field.setAccessible(true);
											field.set(dialoginterface, Boolean.valueOf(true));
										} catch (Exception exception) {
										}
										dialoginterface.dismiss();
										download_done = false;
									}

								}).show();
					}
					break;
				case 2131362212:
					if (TCUtil.getSdCardFree() > 50L) {
						for (Iterator iterator3 = CG_SITE_DATAS.iterator(); iterator3.hasNext(); refreshLib()) {
							CGData.CGSGData.SGItem sgitem1 = (CGData.CGSGData.SGItem) iterator3.next();
							if (!hasDownloaded(sgitem1))
								addNewDownAsyncTask(sgitem1, true);
						}

						radioGroup.check(0x7f0a01a5);
					} else {
						Toast.makeText(CGSGActivity.this, getString(0x7f080061), 1).show();
					}
					break;

				case 2131362213:
					if (!download_ing) {
						download_ing = true;
						hashMaps_checkBox.clear();
						isCheckeds = new boolean[((TCApplication) getApplicationContext()).downloadingInfos.size()];
						Iterator iterator2 = ((TCApplication) getApplicationContext()).downloadingInfos.iterator();
						while (iterator2.hasNext()) {
							com.tc.TCApplication.DownloadingInfo downloadinginfo = (com.tc.TCApplication.DownloadingInfo) iterator2.next();
							HashMap hashmap1 = new HashMap();
							int j;
							if (isCheckeds[i])
								j = 0x7f02005d;
							else
								j = 0x7f02006d;
							hashmap1.put("checked", Integer.valueOf(j));
							hashmap1.put("icon", getSGIcon(CGSGActivity.this, downloadinginfo.guideid));
							hashmap1.put("name", downloadinginfo.name);
							if (allSGLatestTimeStamp != null)
								hashmap1.put(
										"date",
										(new StringBuilder())
												.append(TCUtil
														.timeStamp2YYMMDD(((com.tc.net.DownLoadUpdateAsyncTask.DownLoadUpdateData) allSGLatestTimeStamp
																.get(Integer.valueOf(downloadinginfo.guideid))).timestamp))
												.append(getString(0x7f080088)).toString());
							else
								hashmap1.put("date", Boolean.valueOf(false));
							hashMaps_checkBox.add(hashmap1);
							i++;
						}
						checkListDownloadSimpeAdapter = new DownloadSimpeAdapter(false, CGSGActivity.this, hashMaps_checkBox, 0x7f030040,
								new String[] { "checked", "icon", "name", "date" }, new int[] { 0x7f0a01ad, 0x7f0a01ae, 0x7f0a01af, 0x7f0a01b0 });
						(new android.app.AlertDialog.Builder(CGSGActivity.this)).setTitle(0x7f080084)
								.setPositiveButton(0x7f08008c, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										try {
											Field field = dialoginterface.getClass().getSuperclass().getDeclaredField("mShowing");
											field.setAccessible(true);
											field.set(dialoginterface, Boolean.valueOf(true));
										} catch (Exception exception) {
										}
										(new Thread() {

											public void run() {
												progressHandler.sendEmptyMessage(0);
												Log.v("", (new StringBuilder()).append("============").append(hashMaps_ing.size())
														.append("=========").append(isCheckeds.length).toString());
												ArrayList<Integer> arraylist = new ArrayList<Integer>();

												for (int i = 0; i < isCheckeds.length; i++) {
													if (!isCheckeds[i])
														continue;
													for (TCApplication.DownloadingInfo downloadinginfo : ((TCApplication) getApplicationContext()).downloadingInfos) {
														if (downloadinginfo.guideid == ((Integer) ((HashMap) hashMaps_ing.get(i)).get("guideid"))) {
															downloadinginfo.downAsyncTask.cancel();
															if (downloadinginfo.downAsyncTask.isCancelled())
																Log.v("", "iterable_element.downAsyncTask.isCancelled()");
															arraylist.add(Integer.valueOf(downloadinginfo.guideid));
															TCUtil.deleteEveryFile((new StringBuilder()).append(TCData.SG_ROOT)
																	.append(downloadinginfo.guideid).append("/Cache/").toString());
															break;
														}
													}

												}
												for (Integer integer : arraylist) {
													for (TCApplication.DownloadingInfo downloadinginfo : ((TCApplication) getApplicationContext()).downloadingInfos) {
														if (downloadinginfo.guideid == integer.intValue()) {
															((TCApplication) getApplicationContext()).downloadingInfos.remove(downloadinginfo);
															break;
														}
													}
												}
												arraylist.clear();
												progressHandler.sendEmptyMessage(1);

											}

										}).start();
										dialoginterface.dismiss();
										download_ing = false;
									}

								}).setNeutralButton(0x7f080090, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										int j;
										try {
											Field field = dialoginterface.getClass().getSuperclass().getDeclaredField("mShowing");
											field.setAccessible(true);
											field.set(dialoginterface, Boolean.valueOf(true));
										} catch (Exception exception) {
										}
										for (j = 0; j < isCheckeds.length; j++)
											if (isCheckeds[j]
													&& !((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos
															.get(j)).isUnZip) {
												((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos
														.get(j)).downAsyncTask.cancel();
												((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos
														.get(j)).isDownloading = false;
												((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos
														.get(j)).isUnZip = false;
											}

										download_ing = false;
									}

								}).setNegativeButton(0x7f080097, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										Iterator iterator;
										try {
											Field field = dialoginterface.getClass().getSuperclass().getDeclaredField("mShowing");
											field.setAccessible(true);
											field.set(dialoginterface, Boolean.valueOf(true));
										} catch (Exception exception) {
										}
										iterator = ((TCApplication) getApplicationContext()).downloadingInfos.iterator();
										do {
											if (!iterator.hasNext())
												break;
											com.tc.TCApplication.DownloadingInfo downloadinginfo = (com.tc.TCApplication.DownloadingInfo) iterator
													.next();
											if (!downloadinginfo.isDownloading && !downloadinginfo.isUnZip) {
												downloadinginfo.isDownloading = true;
												downloadinginfo.downAsyncTask = new DownAsyncTask(downloadinginfo.handler);
												startDownAsyncTaskByGuideId(downloadinginfo.guideid, downloadinginfo.downAsyncTask);
											}
										} while (true);
										refreshIng();
										download_ing = false;
									}

								}).setAdapter(checkListDownloadSimpeAdapter, new android.content.DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialoginterface, int i) {
										boolean flag = true;
										boolean aflag[];
										int j;
										Iterator iterator;
										try {
											Field field = dialoginterface.getClass().getSuperclass().getDeclaredField("mShowing");
											field.setAccessible(true);
											field.set(dialoginterface, Boolean.valueOf(false));
										} catch (Exception exception) {
										}
										aflag = isCheckeds;
										if (isCheckeds[i])
											flag = false;
										aflag[i] = flag;
										hashMaps_checkBox.clear();
										j = 0;
										iterator = ((TCApplication) getApplicationContext()).downloadingInfos.iterator();
										while (iterator.hasNext()) {
											com.tc.TCApplication.DownloadingInfo downloadinginfo = (com.tc.TCApplication.DownloadingInfo) iterator
													.next();
											HashMap hashmap = new HashMap();
											int k;
											if (isCheckeds[j])
												k = 0x7f02005d;
											else
												k = 0x7f02006d;
											hashmap.put("checked", Integer.valueOf(k));
											hashmap.put("icon", getSGIcon(CGSGActivity.this, downloadinginfo.guideid));
											hashmap.put("name", downloadinginfo.name);
											if (allSGLatestTimeStamp != null)
												hashmap.put(
														"date",
														(new StringBuilder())
																.append(TCUtil
																		.timeStamp2YYMMDD(((com.tc.net.DownLoadUpdateAsyncTask.DownLoadUpdateData) allSGLatestTimeStamp
																				.get(Integer.valueOf(downloadinginfo.guideid))).timestamp))
																.append(getString(0x7f080088)).toString());
											else
												hashmap.put("date", Boolean.valueOf(false));
											hashMaps_checkBox.add(hashmap);
											j++;
										}
										checkListDownloadSimpeAdapter.notifyDataSetChanged();
									}

								}).setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

									public void onCancel(DialogInterface dialoginterface) {
										try {
											Field field = dialoginterface.getClass().getSuperclass().getDeclaredField("mShowing");
											field.setAccessible(true);
											field.set(dialoginterface, Boolean.valueOf(true));
										} catch (Exception exception) {
										}
										download_ing = false;
										dialoginterface.dismiss();
									}

								}).show();
					}

					break;
				case 2131362214:
					for (CGData.CGSGData.SGItem sgitem : CG_SITE_DATAS) {
						for (HashMap hashmap : hashMaps_update) {
							if (sgitem.sgId == ((Integer) hashmap.get("guideid")).intValue()) {
								if (!isDownloading(sgitem))
									addNewDownAsyncTask(sgitem, true);
								break;
							}
						}
					}
					hashMaps_update.clear();
					refreshUpdate();

					break;
				}

			}

		});

		download_manageIV.setOnTouchListener(new android.view.View.OnTouchListener() {

			public boolean onTouch(View view, MotionEvent motionevent) {
				if (motionevent.getAction() == 0) {// goto _L2; else goto _L1

					((ImageView) view).setAlpha(100);
				} else {
					if (motionevent.getAction() == 1)
						((ImageView) view).setAlpha(255);
				}

				return false;
			}

		});
		download_grid_layout = (RelativeLayout) findViewById(0x7f0a01a7);
		gridView = (GridView) findViewById(0x7f0a01a9);
		gridViewBG = (ImageView) findViewById(0x7f0a01a8);
		download_list_layout = (LinearLayout) findViewById(0x7f0a01aa);
		listView = (ListView) findViewById(0x7f0a01ab);
		relativeLayout = (RelativeLayout) findViewById(0x7f0a01ac);
		if (((TCApplication) getApplicationContext()).downloadingInfos == null)
			((TCApplication) getApplicationContext()).downloadingInfos = new ArrayList();
		hashMaps_done = new ArrayList();
		hashMaps_lib = new ArrayList();
		hashMaps_ing = new ArrayList();
		hashMaps_update = new ArrayList();
		downloadSimpeAdapters = new DownloadSimpeAdapter[4];
		downloadSimpeAdapters[0] = new DownloadSimpeAdapter(false, this, hashMaps_done, 0x7f030041, new String[] { "image", "text" }, new int[] {
				0x7f0a01b1, 0x7f0a01b2 });
		downloadSimpeAdapters[1] = new DownloadSimpeAdapter(true, this, hashMaps_lib, 0x7f030043, new String[] { "icon", "type", "name", "date",
				"status" }, new int[] { 0x7f0a01ba, 0x7f0a01bb, 0x7f0a01bc, 0x7f0a01bd, 0x7f0a01bf });
		downloadSimpeAdapters[2] = new DownloadSimpeAdapter(true, this, hashMaps_ing, 0x7f030042, new String[] { "icon", "name", "progress_bar",
				"progress_text", "button", "unzip", "progress_percent" }, new int[] { 0x7f0a01b3, 0x7f0a01b4, 0x7f0a01b5, 0x7f0a01b6, 0x7f0a01b7,
				0x7f0a01b8, 0x7f0a01b9 });
		downloadSimpeAdapters[3] = new DownloadSimpeAdapter(true, this, hashMaps_update, 0x7f030044, new String[] { "icon", "type", "name", "date" },
				new int[] { 0x7f0a01c0, 0x7f0a01c1, 0x7f0a01c2, 0x7f0a01c3 });
		gridView.setAdapter(downloadSimpeAdapters[0]);
		gridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView adapterview, View view, int i, long l) {
				int j;
				Iterator iterator;
				j = ((Integer) ((HashMap) hashMaps_done.get(i)).get("guideid")).intValue();
				for (CGData.CGSGData.SGItem sgitem : CG_SITE_DATAS) {
					if (sgitem.sgId == j) {
						if (sgitem.type.equals("Shopping")) {
							Bundle bundle1 = new Bundle();
							bundle1.putInt(MallStartActivity.KEY_MALL_ID, j);
							bundle1.putString(MallStartActivity.KEY_MALL_APPLICATION, (new StringBuilder()).append(sgitem.type).append(j).toString());
							startActivity(CGSGActivity.this, MallStartActivity.class, bundle1);
						} else {
							if (sgitem.type.equals("Site")) {
								TCUtil.flurryLog("SiteGuideStart", sgitem.name);
								Bundle bundle2 = new Bundle();
								bundle2.putInt("sg_id", j);
								bundle2.putBoolean("from_other_app", false);
								startActivity(CGSGActivity.this, SGStartActivity.class, bundle2);
							}
						}
						break;
					}
				}

			}

		});
		listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView adapterview, View view, int i, long l) {
				switch (radioGroup.getCheckedRadioButtonId()) {
				case 2131362212:
					CGData.CGSGData.SGItem sgitem1 = CG_SITE_DATAS.get(i);
					showSGGuideItem(
							sgitem1.id,
							null,
							((com.tc.net.DownLoadUpdateAsyncTask.DownLoadUpdateData) allSGLatestTimeStamp.get(Integer.valueOf(sgitem1.sgId))).timestamp);
					break;
				case 2131362213:
					if (!((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos.get(i)).isUnZip) {
						if (!((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos.get(i)).isDownloading) {
							((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos.get(i)).isDownloading = true;
							((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos.get(i)).downAsyncTask = new DownAsyncTask(
									((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos.get(i)).handler);
							startDownAsyncTaskByGuideId(
									((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos.get(i)).guideid,
									((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos.get(i)).downAsyncTask);
						} else {
							((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos.get(i)).isDownloading = false;
							((com.tc.TCApplication.DownloadingInfo) ((TCApplication) getApplicationContext()).downloadingInfos.get(i)).downAsyncTask
									.cancel();
						}
						refreshIng();
					}
					break;
				case 2131362214:
					for (CGData.CGSGData.SGItem sgitem : CG_SITE_DATAS) {
						if (sgitem.sgId == ((Integer) ((HashMap) hashMaps_update.get(i)).get("guideid")).intValue()) {
							showSGGuideItem(
									sgitem.id,
									((com.tc.net.DownLoadUpdateAsyncTask.DownLoadUpdateData) allSGLatestTimeStamp.get(Integer.valueOf(sgitem.sgId))).updateInfo,
									((com.tc.net.DownLoadUpdateAsyncTask.DownLoadUpdateData) allSGLatestTimeStamp.get(Integer.valueOf(sgitem.sgId))).timestamp);
							break;
						}
					}
					break;
				}

			}

		});
		radioButton = (RadioButton) findViewById(0x7f0a01a6);
		allSGLatestTimeStamp = null;
		radioGroup = (RadioGroup) findViewById(0x7f0a00d5);
		radioGroup.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup radiogroup, int i) {
				switch (i) {
				case 2131362211:
					timeHandler.removeMessages(0);
					download_grid_layout.setVisibility(0);
					download_list_layout.setVisibility(8);
					relativeLayout.setVisibility(8);
					refreshDone();
					gridView.setAdapter(downloadSimpeAdapters[0]);
					break;
				case 2131362212:
					timeHandler.removeMessages(0);
					download_grid_layout.setVisibility(8);
					if (allSGLatestTimeStamp == null) {
						download_list_layout.setVisibility(8);
						relativeLayout.setVisibility(0);
						download_manageIV.setVisibility(8);
					} else {
						download_list_layout.setVisibility(0);
						refreshLib();
					}
					listView.setAdapter(downloadSimpeAdapters[1]);
					break;
				case 2131362213:
					timeHandler.sendEmptyMessage(0);
					download_grid_layout.setVisibility(8);
					relativeLayout.setVisibility(8);
					download_list_layout.setVisibility(0);
					refreshIng();
					listView.setAdapter(downloadSimpeAdapters[2]);
					break;
				case 2131362214:
					timeHandler.removeMessages(0);
					download_grid_layout.setVisibility(8);
					relativeLayout.setVisibility(8);
					download_list_layout.setVisibility(0);
					if (hashMaps_update.isEmpty()) {
						download_manageIV.setVisibility(8);
						download_list_layout.setVisibility(8);
					} else {
						download_list_layout.setVisibility(0);
						download_manageIV.setVisibility(0);
						download_manageIV.setImageResource(0x7f02006e);
					}
					listView.setAdapter(downloadSimpeAdapters[3]);
					break;
				}

			}

		});

		refreshDone();
		radioGroup.check(0x7f0a01a4);
	}

	protected void onResume() {
		super.onResume();
	}

	public static final int DOWNLOAD_IT = 7;
	private static final int REQUEST_CODE = 1000;
	public static final int SHOW_IT = 77;
	private static final String TAG = CGSGActivity.class.getSimpleName();
	private List<CGData.CGSGData.SGItem> CG_SITE_DATAS;
	private HashMap allSGLatestTimeStamp;
	private DownloadSimpeAdapter checkListDownloadSimpeAdapter;
	private BroadcastReceiver connectivityChanged;
	private ConnectivityManager connectivityManager;
	private DownLoadUpdateAsyncTask downLoadUpdateAsyncTask;
	private DownloadSimpeAdapter downloadSimpeAdapters[];
	boolean download_done;
	private RelativeLayout download_grid_layout;
	boolean download_ing;
	private LinearLayout download_list_layout;
	private ImageView download_manageIV;
	private android.content.SharedPreferences.Editor editor;
	private GridView gridView;
	private ImageView gridViewBG;
	private ArrayList hashMaps_checkBox;
	private ArrayList hashMaps_done;
	private ArrayList hashMaps_ing;
	private ArrayList hashMaps_lib;
	private ArrayList<HashMap> hashMaps_update;
	private boolean isCheckeds[];
	private ListView listView;
	private NetworkInfo networkInfo;
	private ProgressDialog progressDialog;
	private Handler progressHandler;
	private RadioButton radioButton;
	private RadioGroup radioGroup;
	private RelativeLayout relativeLayout;
	private SharedPreferences sharedPreferences;
	Handler timeHandler;

	/*
	 * static ProgressDialog access$002(CGSGActivity cgsgactivity,
	 * ProgressDialog progressdialog) { cgsgactivity.progressDialog =
	 * progressdialog; return progressdialog; }
	 */

	/*
	 * static android.content.SharedPreferences.Editor access$1602(CGSGActivity
	 * cgsgactivity, android.content.SharedPreferences.Editor editor1) {
	 * cgsgactivity.editor = editor1; return editor1; }
	 */

	/*
	 * static boolean[] access$2002(CGSGActivity cgsgactivity, boolean aflag[])
	 * { cgsgactivity.isCheckeds = aflag; return aflag; }
	 */

	/*
	 * static HashMap access$202(CGSGActivity cgsgactivity, HashMap hashmap) {
	 * cgsgactivity.allSGLatestTimeStamp = hashmap; return hashmap; }
	 */

	/*
	 * static DownloadSimpeAdapter access$2302(CGSGActivity cgsgactivity,
	 * DownloadSimpeAdapter downloadsimpeadapter) {
	 * cgsgactivity.checkListDownloadSimpeAdapter = downloadsimpeadapter; return
	 * downloadsimpeadapter; }
	 */

	/*
	 * static NetworkInfo access$702(CGSGActivity cgsgactivity, NetworkInfo
	 * networkinfo) { cgsgactivity.networkInfo = networkinfo; return
	 * networkinfo; }
	 */

	/*
	 * static DownLoadUpdateAsyncTask access$902(CGSGActivity cgsgactivity,
	 * DownLoadUpdateAsyncTask downloadupdateasynctask) {
	 * cgsgactivity.downLoadUpdateAsyncTask = downloadupdateasynctask; return
	 * downloadupdateasynctask; }
	 */
}
