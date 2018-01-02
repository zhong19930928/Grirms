package com.yunhu.yhshxc.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import gcg.org.debug.JLog;

public class CompanyFilesActivity extends Activity implements OnClickListener {
	protected static final String TAG = CompanyFilesActivity.class.getSimpleName();
	private Context context;
	private ImageView companyweb_back;// 返回按钮
	private ImageView clear_download_file;// 清除已下载文件的按钮
	private String phoneNumber;
	private MyProgressDialog mDialog;// 进度对话框
	private CompanyAdapter adapter;
	private GridView gview;
	private ArrayList<HashMap<String, String>> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_company_files);
		gview = (GridView) findViewById(R.id.company_files);
		companyweb_back = (ImageView) findViewById(R.id.companyweb_back);
		clear_download_file = (ImageView) findViewById(R.id.clear_download_file);
		clear_download_file.setOnClickListener(this);
		companyweb_back.setOnClickListener(this);
		phoneNumber = PublicUtils.receivePhoneNO(context);
		init();
		// 菜单短点击时间
		gview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final String loadaddress1 = list.get(position).get("comFilesurl") + "";
				String styleFileName = loadaddress1.substring(loadaddress1.lastIndexOf(".") + 1);

				if (list.get(position).get("flag").equals("ok")) {

					try {
						File file = new File(Constants.SDCARD_PATH + "companyfile/"
								+ loadaddress1.substring(loadaddress1.lastIndexOf("/") + 1));
						Intent intent = new Intent();
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setAction(android.content.Intent.ACTION_VIEW);
						intent.setClassName("cn.wps.moffice_eng", "cn.wps.moffice.documentmanager.PreStartActivity2");
						Uri uri = Uri.fromFile(file);
						intent.setData(uri);
						startActivity(intent);
					} catch (Exception e) {
						Toast.makeText(context, PublicUtils.getResourceString(context,R.string.toast_two) + styleFileName + PublicUtils.getResourceString(context,R.string.toast_two1), Toast.LENGTH_SHORT)
								.show();
					}

				} else {

					showLoadDialog(position);
				}

			}
		});
		// 菜单长点击时间
		gview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
				if (vibrator.hasVibrator()) {
					long[] pattern = {0,100};
					vibrator.vibrate(pattern, -1);
				}
				final String loadaddress1 = list.get(position).get("comFilesurl") + "";
				if (list.get(position).get("flag").equals("ok")) {// 如果当前位置的文件已经下载了,便弹出对话框可以进行单删除
					AlertDialog alDialog = new AlertDialog.Builder(context, R.style.WeChatAlertDialogStyle)
							.setItems(new String[] { PublicUtils.getResourceString(context,R.string.delete), PublicUtils.getResourceString(context,R.string.Cancle) }, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							switch (which) {
							case 0:
								String onePath = Constants.SDCARD_PATH + "companyfile/"
										+ loadaddress1.substring(loadaddress1.lastIndexOf("/") + 1);// 该文件的路径

								if (FileHelper.deleteAllFile(onePath)) {
									Toast.makeText(context, PublicUtils.getResourceString(context,R.string.delete_sucsess), Toast.LENGTH_SHORT).show();
									initData();
								} else {
									Toast.makeText(context, PublicUtils.getResourceString(context,R.string.delete_fail), Toast.LENGTH_SHORT).show();

								}
								break;
							case 1:

								break;

							default:
								break;
							}

						}

					}).create();
					alDialog.show();

				}else{
					Toast.makeText(context, PublicUtils.getResourceString(context,R.string.toast_two2), Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});

	}

	private AlertDialog mDialogs;
	private String loadaddress;

	/**
	 * 是否下载提示对话框
	 */
	private void showLoadDialog(int position) {
		loadaddress = list.get(position).get("comFilesurl") + "";
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(context, R.style.NewAlertDialogStyle);
		mBuilder.setTitle(PublicUtils.getResourceString(context,R.string.dialog_one5) + list.get(position).get("comFilesname") + ")?");
		mBuilder.setNegativeButton(PublicUtils.getResourceString(context,R.string.Cancle), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mDialogs.dismiss();

			}
		});
		mBuilder.setPositiveButton(PublicUtils.getResourceString(context,R.string.Confirm), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 进行下载
				Toast.makeText(context, PublicUtils.getResourceString(context,R.string.dialog_one6), Toast.LENGTH_SHORT).show();
				String fileName = loadaddress.substring(loadaddress.lastIndexOf("/") + 1);
				String sdPath = Constants.SDCARD_PATH + "companyfile/";
				new MyAsyncTask(sdPath, fileName).execute(loadaddress);
			}
		});
		mDialogs = mBuilder.create();
		mDialogs.setCancelable(false);
		mDialogs.show();

	}

	private ProgressDialog progressDialog;

	class MyAsyncTask extends AsyncTask<String, Integer, Boolean> {
		private boolean isStopDownload = false;// 是否停止正在进行的下载
		private String sdPath;// 保存路径
		private String fileName;// 文件名

		HttpURLConnection connection;

		public MyAsyncTask(String sdPath, String fileName) {
			this.fileName = fileName;
			this.sdPath = sdPath;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			boolean isFinishLoad = false;// 是否正常完成了下载
			double totalSize = 0;// 文件总大小
			int currentSize = 0;// 记录每此下载进度的叠加量

			try {
				URL url = new URL(params[0]);
				connection = (HttpURLConnection) url.openConnection();
				totalSize = connection.getContentLength();
				connection.setConnectTimeout(5000);
				File outFile = new File(sdPath);
				if (!outFile.exists()) {
					outFile.mkdirs();
				}
				File filePath = new File(outFile, fileName);
				if (200 == connection.getResponseCode()) {
					InputStream is = connection.getInputStream();
					FileOutputStream fos = new FileOutputStream(filePath);
					BufferedInputStream bis = new BufferedInputStream(is);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					byte[] bys = new byte[1024];
					int len = 0;
					while ((len = bis.read(bys)) != -1) {
						bos.write(bys, 0, len);
						bos.flush();
						currentSize += len;
						double downSize = currentSize / totalSize;
						int newprogress = (int) (downSize * 100);
						publishProgress(newprogress);
					}
					bos.close();
					bis.close();
					isFinishLoad = true;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
				return isFinishLoad;
			}

			return isFinishLoad;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setTitle(PublicUtils.getResourceString(context,R.string.dialog_one3));
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMax(100);
			progressDialog.setProgress(0);
			progressDialog.setCancelable(false);
			// 如果中途用户点击了取消下载就要停止当前下载
			progressDialog.setButton(PublicUtils.getResourceString(context,R.string.dialog_one4), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
						isStopDownload = true;
						// connection.disconnect();
						try {
							connection.disconnect();
							// Toast.makeText(context, "下载失败",
							// Toast.LENGTH_SHORT).show();
							// //下载失败以后可能因为某种原因文件下载不全,删除为完成文件
							// File delfile = new File(sdPath+fileName);
							// if (delfile.exists()) {
							// delfile.delete();
							// }

						} catch (Exception e) {

						}
					}

				}

			});
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result) {
				// mHandler.sendEmptyMessage(1);
				Toast.makeText(context, PublicUtils.getResourceString(context,R.string.download_finish), Toast.LENGTH_SHORT).show();
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
			} else {
				Toast.makeText(context, PublicUtils.getResourceString(context,R.string.download_failure), Toast.LENGTH_SHORT).show();
				// 下载失败以后可能因为某种原因文件下载不全,删除为完成文件
				File delfile = new File(sdPath + fileName);
				if (delfile.exists()) {
					delfile.delete();
				}
			}
			
			progressDialog.dismiss();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			if (!isStopDownload) {
				progressDialog.setProgress(values[0]);
			}

		}

	};

	/**
	 * 初始化对应的数据展示
	 */
	private void init() {
		mDialog = new MyProgressDialog(context, R.style.CustomProgressDialog,PublicUtils.getResourceString(context,R.string.dialog_one2));
		// 发起请求获取拜访,判断是个人还是管理者
		String url = UrlInfo.getCompanyFiles(context);
		// RequestParams params =
		// searchParams(PublicUtils.receivePhoneNO(getActivity()));

		RequestParams params = searchParams(phoneNumber);
		GcgHttpClient.getInstance(context).post(url, params, new HttpResponseListener() {

			@Override
			public void onSuccess(int statusCode, String content) {

				JLog.d(TAG, "onSuccess: " + content);

				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {// 有返回码
						if (obj.has("comFiles")) {
							new CacheData(context).parseComFiles(obj);
							initData();
						}

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				mDialog.show();

			}

			@Override
			public void onFinish() {
				JLog.d(TAG, "onFinish");
				mDialog.cancel();
			}

			@Override
			public void onFailure(Throwable error, String content) {

				Toast.makeText(context, PublicUtils.getResourceString(context,R.string.toast_one11), Toast.LENGTH_SHORT).show();

			}
		});

	}

	private void initData() {
		// TODO Auto-generated method stub

		int comFilesLength = SharedPreferencesUtil.getInstance(this).getInt("comFiles");
		list = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < comFilesLength; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("comFilesname", SharedPreferencesUtil.getInstance(this).getString("comFilesname" + i));
			map.put("comFilesurl", SharedPreferencesUtil.getInstance(this).getString("comFilesurl" + i));
			map.put("flag", "no");
			list.add(map);
		}
		String[] from = { "comFilesname" };
		int[] to = { R.id.text };
		// sim_adapter = new SimpleAdapter(this, list,
		// R.layout.item_company_file, from, to);

		// 配置适配器
		adapter = new CompanyAdapter(list);
		gview.setAdapter(adapter);

	}

	private RequestParams searchParams(String receivePhoneNO) {
		RequestParams param = new RequestParams();
		param.put("phoneno", receivePhoneNO);
		return param;

	}

	class CompanyAdapter extends BaseAdapter {

		private List<HashMap<String, String>> data;

		public CompanyAdapter(List<HashMap<String, String>> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public HashMap<String, String> getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = LayoutInflater.from(context).inflate(R.layout.item_company_file, parent, false);
			TextView content = (TextView) itemView.findViewById(R.id.text);
			content.setText(data.get(position).get("comFilesname"));
			ImageView image1 = (ImageView) itemView.findViewById(R.id.image1);
			File file = new File(Constants.SDCARD_PATH + "companyfile");
			if (file.exists()) {
				File[] fileArray = file.listFiles();
				for (File file2 : fileArray) {
					String fileName = file2.getName();
					if (data.get(position).get("comFilesurl")
							.substring(data.get(position).get("comFilesurl").lastIndexOf("/") + 1).equals(fileName)) {
						image1.setVisibility(View.VISIBLE);
						data.get(position).put("flag", "ok");
					}

				}
			}
			return itemView;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clear_download_file:// 删除所有文件
			deleteAllDownLoadfile();

			break;
		case R.id.companyweb_back:// 返回
			this.finish();

			break;

		default:
			break;
		}

	}

	/**
	 * 删除资料库下载的所有文件
	 */
	private void deleteAllDownLoadfile() {
		AlertDialog dialog = new AlertDialog.Builder(context, R.style.NewAlertDialogStyle)
				.setIcon(R.drawable.warning_icon).setTitle(PublicUtils.getResourceString(context,R.string.dialog_one)).setMessage(PublicUtils.getResourceString(context,R.string.dialog_one1))
				.setNegativeButton(PublicUtils.getResourceString(context,R.string.Cancle), null).setPositiveButton(PublicUtils.getResourceString(context,R.string.Confirm), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String filePath = Constants.SDCARD_PATH + "companyfile/";// 保存资料库下载文件的路径
						if (FileHelper.deleteAllFile(filePath)) {// 删除文件是否成功
							Toast.makeText(context, PublicUtils.getResourceString(context,R.string.toast_one9), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(context, PublicUtils.getResourceString(context,R.string.toast_one10), Toast.LENGTH_SHORT).show();
						}
						initData();

					}
				}).setCancelable(false).create();
		dialog.show();

	}

}
