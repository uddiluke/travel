// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tc.TCData;
import com.tc.TCUtil;

// Referenced classes of package com.tc.net:
//            NetUtil

public class DownAsyncTask extends AsyncTask {

	public DownAsyncTask() {
		count = 0;
	}

	public DownAsyncTask(Handler handler) {
		this();
		messageHandler = handler;
	}

	private com.tc.TCData.TCDownResult download(String s) {
		org.apache.http.client.HttpClient httpclient = NetUtil.getClient();
		httpGet = NetUtil.getHttpGet(httpclient, s, null);
		httpGet.addHeader("RANGE", (new StringBuilder()).append("bytes=").append(hasDownloaded).append("-").toString());
		httpResponse = NetUtil.getHttpResponse(httpclient, httpGet);
		if (httpResponse == null || httpResponse.getStatusLine().getStatusCode() != 206) {
			// goto _L2; else goto _L1
			if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 416) {
				TOTAL = hasDownloaded;
				if (TCUtil.getSdCardFree() < 50L)
					publishProgress(new String[] { "sdcard_not_free" });
				startUnZip((new StringBuilder()).append(saveFilePath).append(saveFileName).toString());
			}
		} else {
			try {
				InputStream inputstream = httpResponse.getEntity().getContent();
				TOTAL = httpResponse.getEntity().getContentLength() + hasDownloaded;
				saveFileName = (new StringBuilder()).append(timestamp).append("_").append(TOTAL).toString();
				saveInputStream(inputstream, (new StringBuilder()).append(saveFilePath).append(saveFileName).toString());
			} catch (IllegalStateException illegalstateexception) {
				Log.e(TAG, "private InputStream download(String url)", illegalstateexception);
			} catch (IOException ioexception) {
				Log.e(TAG, "private InputStream download(String url)", ioexception);
			}
		}

		return null;

	}

	private void saveInputStream(InputStream inputstream, String s) {
		if (TCData.SG_ROOT.equals(CGorSG)) {
			publishProgress(new String[] { String.valueOf(hasDownloaded), String.valueOf(TOTAL), String.valueOf(guideid) });
		} else if (TCData.CG_ROOT.equals(CGorSG)) {
			publishProgress(new String[] { String.valueOf(hasDownloaded), String.valueOf(TOTAL) });
		}
		RandomAccessFile randomaccessfile;
		byte bytes[] = new byte[1024];
		try {
			randomaccessfile = new RandomAccessFile(s, "rwd");
			randomaccessfile.seek(hasDownloaded);
			int size = 0;

			while ((size = inputstream.read(bytes)) > -1) {
				if (isCancelled() || inputstream == null)
					break;
				randomaccessfile.write(bytes, 0, size);
				hasDownloaded = hasDownloaded + size;
				Log.v("", (new StringBuilder()).append(hasDownloaded).append("/").append(TOTAL).toString());
				if (TCData.SG_ROOT.equals(CGorSG)) {
					publishProgress(new String[] { String.valueOf(hasDownloaded), String.valueOf(TOTAL), String.valueOf(guideid) });
				} else if (TCData.CG_ROOT.equals(CGorSG)) {
					publishProgress(new String[] { String.valueOf(hasDownloaded), String.valueOf(TOTAL) });
				}
			}
			randomaccessfile.close();
			inputstream.close();
			if (hasDownloaded == TOTAL && hasDownloaded > 0L) {
				if (TCUtil.getSdCardFree() < 50L)
					publishProgress(new String[] { "sdcard_not_free" });
				startUnZip(s);
			}
		} catch (Exception e) {

			Log.e(TAG, "private void saveInputStream(InputStream is, String saveFile)", e);
		}

	}

	private void startUnZip(String s) {
		ZipInputStream zipinputstream;
		String s1;
		Message message = new Message();
		message.what = 1;
		FileInputStream fileinputstream;
		Exception exception;
		int i;
		ZipEntry zipentry;
		String as[];
		String s2;
		if (TCData.SG_ROOT.equals(CGorSG)) {
			long al[] = new long[2];
			al[0] = guideid;
			al[1] = hasDownloaded;
			message.obj = al;
		} else if (TCData.CG_ROOT.equals(CGorSG))
			message.obj = Long.valueOf(hasDownloaded);
		messageHandler.sendMessage(message);

		try {
			fileinputstream = new FileInputStream(s);
			count = fileinputstream.available() / 14 / 1000;
			zipinputstream = new ZipInputStream(fileinputstream);
			i = 0;
			while (true) {
				zipentry = zipinputstream.getNextEntry();
				if (zipentry == null)
					break;
				i++;
				s1 = zipentry.getName();
				publishProgress(new String[] { i + ":" + count });
				if (zipentry.isDirectory()) {
					s2 = s1.substring(0, -1 + s1.length());
					(new File((new StringBuilder()).append(CGorSG).append(guideid).append(File.separator).append(s2).toString())).mkdirs();
				} else {
					File file = new File((new StringBuilder()).append(CGorSG).append(guideid).append(File.separator).append(s1).toString());
					File file1 = file.getParentFile();
					if (!file1.exists())
						file1.mkdirs();
					file.createNewFile();
					FileOutputStream fileoutputstream = new FileOutputStream(file);
					byte bytes[] = new byte[512];
					do {
						int j = zipinputstream.read(bytes);
						if (j == -1)
							break;
						fileoutputstream.write(bytes, 0, j);
						fileoutputstream.flush();
					} while (true);
					fileoutputstream.close();
				}
			}

			zipinputstream.close();
			TCUtil.deleteFile(s);

		} catch (Exception e) {
			Log.e("CGUpdateActivity", "unzipForlder", e);
		}

	}

	public void cancel() {
		Message message;
		Log.v("", "================Cancelled ++++++++++++++++++++");
		cancel(true);
		if (httpGet != null && !httpGet.isAborted())
			httpGet.abort();
		if (httpPost != null && !httpPost.isAborted())
			httpPost.abort();
		message = new Message();
		message.what = 2;
		if (TCData.SG_ROOT.equals(CGorSG)) {
			// goto _L2; else goto _L1
			message.obj = Integer.valueOf(guideid);
			messageHandler.sendMessage(message);
		} else if (TCData.CG_ROOT.equals(CGorSG))
			messageHandler.sendEmptyMessage(message.what);
		Log.v("", "================Cancelled");

	}

	protected com.tc.TCData.TCDownResult doInBackground(Object[] aobj) {
		com.tc.TCData.TCDownResult tcdownresult;
		HttpResponse httpresponse;
		int i;
		tcdownresult = null;
		String s = (String) aobj[0];
		CGorSG = (String) aobj[1];
		Map map = (Map) aobj[2];
		String s1 = (String) aobj[3];
		Log.v("", "=========================InBackground");
		File file;
		org.apache.http.client.HttpClient httpclient;
		if (TCData.SG_ROOT.equals(CGorSG))
			guideid = Integer.parseInt((String) map.get("guideid"));
		else if (TCData.CG_ROOT.equals(CGorSG))
			guideid = Integer.parseInt((String) map.get("cgid"));
		timestamp = Integer.parseInt((String) map.get("lasttimestamp"));
		saveFilePath = (new StringBuilder()).append(CGorSG).append(guideid).append("/Cache/").toString();
		file = new File(saveFilePath);
		if (file.exists()) {
			if (file.list().length == 1) {
				String as[] = file.list()[0].split("_");
				com.tc.TCData.TCDownResult tcdownresult1;
				if (Integer.parseInt(as[0]) != timestamp) {
					TCUtil.deleteFile((new StringBuilder()).append(saveFilePath).append(file.list()[0]).toString());
					hasDownloaded = 0L;
				} else if (as[1].equals("0")) {
					TCUtil.deleteFile((new StringBuilder()).append(saveFilePath).append(file.list()[0]).toString());
					hasDownloaded = 0L;
				} else {
					hasDownloaded = (new File((new StringBuilder()).append(saveFilePath).append(file.list()[0]).toString())).length();
					saveFileName = file.list()[0];
				}
			}
		} else {
			file.mkdirs();
			hasDownloaded = 0L;
		}
		httpclient = NetUtil.getClient();
		httpresponse = null;
		if (s.equals("get")) {
			// remove map data
			httpGet = NetUtil.getHttpGet(httpclient, s1, null);
			httpGet.addHeader("RANGE", (new StringBuilder()).append("bytes=").append(hasDownloaded).append("-").toString());
			httpresponse = NetUtil.getHttpResponse(httpclient, httpGet);
		} else if (s.equals("post")) {
			httpPost = NetUtil.getHttpPost(httpclient, s1, map);
			httpresponse = NetUtil.getHttpResponse(httpclient, httpPost);
		}
		if (httpresponse != null) {// goto _L2; else goto _L1
			i = httpresponse.getStatusLine().getStatusCode();
			if (i == 200) {
				try {
					tcdownresult = com.tc.logic.CGDownData.CGDownDataParaser.parse(httpresponse.getEntity().getContent());
				} catch (IllegalStateException illegalstateexception1) {
					Log.e(TAG, "protected  InputStream doInBackground(Object... params)", illegalstateexception1);

				} catch (IOException ioexception1) {
					Log.e(TAG, "protected  InputStream doInBackground(Object... params)", ioexception1);

				}
				return tcdownresult;
			} else {
				// L4
				if (i == 206) {
					try {
						InputStream inputstream = httpresponse.getEntity().getContent();
						TOTAL = httpresponse.getEntity().getContentLength() + hasDownloaded;
						saveFileName = (new StringBuilder()).append(timestamp).append("_").append(TOTAL).toString();
						saveInputStream(inputstream, (new StringBuilder()).append(saveFilePath).append(saveFileName).toString());
					} catch (IllegalStateException illegalstateexception) {
						Log.e(TAG, "private InputStream download(String url)", illegalstateexception);
					} catch (IOException ioexception) {
						Log.e(TAG, "private InputStream download(String url)", ioexception);

					}

				}
				if (httpresponse != null && httpresponse.getStatusLine().getStatusCode() == 416) {
					TOTAL = hasDownloaded;
					if (TCUtil.getSdCardFree() < 50L)
						publishProgress(new String[] { "sdcard_not_free" });
					startUnZip((new StringBuilder()).append(saveFilePath).append(saveFileName).toString());

				}
				if (i == 301 || i == 302) {
					Header aheader[] = httpresponse.getHeaders("Location");
					if (aheader != null && aheader.length != 0)
						tcdownresult = download(aheader[-1 + aheader.length].getValue());

				}
				if (i != 404 && i != 102)
					return tcdownresult; /* Loop/switch isn't completed */
				TCUtil.deleteFile((new StringBuilder()).append(timestamp).append("_").append(TOTAL).toString());
				Message message = new Message();
				message.what = 4;
				if (TCData.SG_ROOT.equals(CGorSG)) {
					message.obj = Integer.valueOf(guideid);
					messageHandler.sendMessage(message);
				} else if (TCData.CG_ROOT.equals(CGorSG)) {
					messageHandler.sendEmptyMessage(message.what);
				}

				cancel(true);
			}
		}
		return tcdownresult;

	}

	protected void onPostExecute(com.tc.TCData.TCDownResult tcdownresult) {
		super.onPostExecute(tcdownresult);
		Log.v("", "================onPostExecute(TCDownResult result)");
		if (hasDownloaded != TOTAL || hasDownloaded == 0L) {
			// goto _L2; else goto _L1
			Message message = new Message();
			message.what = 5;
			if (TCData.SG_ROOT.equals(CGorSG)) {
				message.obj = Integer.valueOf(guideid);
				messageHandler.sendMessage(message);
			} else if (TCData.CG_ROOT.equals(CGorSG))
				messageHandler.sendEmptyMessage(message.what);
		} else {
			Message message1;
			message1 = new Message();
			message1.what = 3;
			if (TCData.SG_ROOT.equals(CGorSG)) {
				int ai[] = new int[2];
				ai[0] = guideid;
				ai[1] = timestamp;
				message1.obj = ai;
				messageHandler.sendMessage(message1);
			} else {
				if (TCData.CG_ROOT.equals(CGorSG))
					messageHandler.sendEmptyMessage(message1.what);
			}
		}
	}

	protected void onPostExecute(Object obj) {
		onPostExecute((com.tc.TCData.TCDownResult) obj);
	}

	protected void onPreExecute() {
		super.onPreExecute();
		Log.v("", "================onPreExecute");
	}

	protected void onProgressUpdate(Object aobj[]) {
		onProgressUpdate((String[]) aobj);
	}

	protected void onProgressUpdate(String as[]) {
		super.onProgressUpdate(as);
		if (as.length > 1) {
			if (!isCancelled()) {
				Message message2 = new Message();
				message2.what = 0;
				Message message1;
				if (TCData.SG_ROOT.equals(CGorSG)) {
					long al1[] = new long[3];
					al1[0] = Long.parseLong(as[0]);
					al1[1] = Long.parseLong(as[1]);
					al1[2] = Integer.parseInt(as[2]);
					message2.obj = al1;
				} else if (TCData.CG_ROOT.equals(CGorSG)) {
					long al[] = new long[2];
					al[0] = Long.parseLong(as[0]);
					al[1] = Long.parseLong(as[1]);
					message2.obj = al;
				}
				messageHandler.sendMessage(message2);
			}
		} else if (as.length == 1 && !as[0].equals("sdcard_not_free")) {
			Message message = new Message();
			message.what = 6;
			String as1[] = as[0].split(":");
			message.arg1 = Integer.parseInt(as1[0]);
			message.arg2 = Integer.parseInt(as1[1]);
			messageHandler.sendMessage(message);
		}
		if ("sdcard_not_free".equals(as[0])) {
			Message message1 = new Message();
			message1.what = 7;
			messageHandler.sendMessage(message1);
		}
	}

	private static final String TAG = DownAsyncTask.class.getSimpleName();
	private String CGorSG;
	private long TOTAL;
	int count;
	private int guideid;
	private long hasDownloaded;
	private HttpGet httpGet;
	private HttpPost httpPost;
	private HttpResponse httpResponse;
	private Handler messageHandler;
	private String saveFileName;
	private String saveFilePath;
	private int timestamp;

}
