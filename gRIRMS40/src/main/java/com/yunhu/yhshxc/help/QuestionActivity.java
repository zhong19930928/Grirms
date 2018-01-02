package com.yunhu.yhshxc.help;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import gcg.org.debug.JLog;

/**
 * 常见问题模块
 * 用于通过HTTP加载常见问题数据、解析数据、并存入SharedPreferences中
 * 
 * @version 2013.5.23
 * @author wangchao
 * 
 */
public class QuestionActivity extends AbsBaseActivity implements OnItemClickListener {
	private final String TAG = "QuestionActivity";
    private ImageView iv_back;//标题位置返回
	/**
	 * 显示常见问题的ListView
	 */
	private ListView lv_question = null;

	/**
	 * 问题数据的List，该List的数据是与aList一一对应的
	 */
	private ArrayList<String> qList = null;

	/**
	 * 答案数据的List，该List的数据是与qList一一对应的
	 */
	private ArrayList<String> aList = null;

	/**
	 * 进度对话框，用于通过HTTP加载常见问题的数据时显示进度
	 */
	private ProgressDialog loadingDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.help_question);
		lv_question = (ListView) this.findViewById(R.id.lv_question);
		lv_question.setOnItemClickListener(this);// ItemClick事件处理器由当前类的onItemClick方法实现
        iv_back=(ImageView) findViewById(R.id.help_question_back);
        iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				QuestionActivity.this.finish();
				}
		});
		// 初始化进度对话框
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setMessage(this.getResources().getString(R.string.init));

		// 启动一个任务线程，通过HTTP去加载常见问题的数据
//		new Task().execute();
		task();
	}

	/**
	 * ListView所需的ItemClick事件处理程序。当用户点击某个Item时，打开对应的QuestionDetailActivity。 该方法实现自OnItemClickListener接口
	 * 
	 * @see OnItemClickListener
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, QuestionDetailActivity.class);
		intent.putExtra("Q", qList.get(position));// 将问题传给QuestionDetailActivity
		intent.putExtra("A", aList.get(position));// 将答案传给QuestionDetailActivity
		this.startActivity(intent);
	}

	/**
	 * 用户按下返回键时的事件处理（也可以通过覆盖Activity.onBackPressed()方法来响应返回键的事件）
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 点击返回按钮
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 任务线程，通过HTTP去加载常见问题的数据,加载过程中显示进度对话框
	 */
	private void task(){
		String url = UrlInfo.getUrlHelpQuestion(QuestionActivity.this) + "?phoneno=" + PublicUtils.receivePhoneNO(QuestionActivity.this);
		JLog.d(TAG, "url =>" + url);
		GcgHttpClient.getInstance(this).get(url, null, new HttpResponseListener(){
			@Override
			public void onStart() {
				loadingDialog.show();
			}
			
			
			@Override
			public void onFailure(Throwable error, String content) {
				JLog.d(TAG, "result =>" + content);
				new Task().execute("");
			}

			@Override
			public void onFinish() {
				
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d(TAG, "result =>" + content);
				new Task().execute(content);
			}
			
			
		});
	}
	
	/**
	 * 任务线程，通过HTTP去加载常见问题的数据,加载过程中显示进度对话框
	 */
	private class Task extends AsyncTask<String, Integer, String> {

		/**
		 * HTTP加载前先打开进度对话框
		 */
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			loadingDialog.show();
//		}

		/**
		 * 在后台线程中通过HTTP加载常见问题的数据，并解析保存到SharedPreferences中
		 */
		@Override
		protected String doInBackground(String... params) {
//			// 通过HTTP加载常见问题数据
//			String url = UrlInfo.getUrlHelpQuestion(QuestionActivity.this) + "?phoneno=" + PublicUtils.receivePhoneNO(QuestionActivity.this);
//			String result = new HttpHelper(QuestionActivity.this).connectGet(url);
//			JLog.d(TAG, "url =>" + url);
//			JLog.d(TAG, "result =>" + result);
			String result=params[0];
			try {
				// 如果数据不为空，就解析并保存到SharedPreferences中
				if (!TextUtils.isEmpty(result)) {
					JSONObject resultObject = new JSONObject(result);
					String resultcode = resultObject.getString(Constants.RESULT_CODE);
					// 验证返回的数据，如果有效，则存入SharedPreferences中
					if (resultcode.equalsIgnoreCase(Constants.RESULT_CODE_SUCCESS) && isValid(resultObject, "question")) {
						SharedPreferencesHelp.getInstance(QuestionActivity.this.getApplicationContext()).saveQA(resultObject.getString("question"));
					}
				}
				parse();//解析数据
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			isContinue(result);
			return result;
		}

		/**
		 * 完成数据加载和解析时调用
		 * 
		 * @param result 通过HTTP取来的数据
		 */
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			loadingDialog.dismiss();//关闭进度对话框
			if (TextUtils.isEmpty(result)) {//如果result为null，则提示网络错误
				ToastOrder.makeText(QuestionActivity.this, QuestionActivity.this.getResources().getString(R.string.ERROR_NETWORK), ToastOrder.LENGTH_SHORT);
			}
			
			//因为数据会在doInBackground中解析到qList和aList中，所以这里只要判断aList中是否有数据即可，
			//如果有数据，就通过填入Adapter模式填入lv_question
			if (qList != null && !qList.isEmpty()) {
				QuestionAdapter adapter = new QuestionAdapter(QuestionActivity.this);
				adapter.setDataSrcList(qList);
				lv_question.setAdapter(adapter);
			}
			else {
				ToastOrder.makeText(QuestionActivity.this, QuestionActivity.this.getResources().getString(R.string.no_data), ToastOrder.LENGTH_SHORT).show();
			}
		}

		/**
		 * 解析HTTP取回的数据，并添加进qList和aList中
		 * 
		 * @throws Exception 解析json数据时抛出的异常
		 */
		private void parse() throws Exception {
			//从SharedPreferences中取出常见问题的json格式数据
			String qa = SharedPreferencesHelp.getInstance(QuestionActivity.this.getApplicationContext()).getQA();
			if (!TextUtils.isEmpty(qa)) {
				JSONArray questionArr = new JSONArray(qa);
				qList = new ArrayList<String>();
				aList = new ArrayList<String>();
				// JSONArray questionArr = resultObject.getJSONArray("question");
				int len = questionArr.length();
				JSONObject obj = null;
				//解析json数据进aList和aList
				for (int i = 0; i < len; i++) {
					obj = questionArr.getJSONObject(i);
					if (isValid(obj, "answer")) {
						aList.add(obj.getString("answer"));
					}
					if (isValid(obj, "question")) {
						qList.add(obj.getString("question"));
					}
				}
			}
		}

		/**
		 * 判断数据是否有效（如果数据不为null或空字符串即认为有效）
		 * 
		 * @param jsonObject 要验证的json数据对象
		 * @param key 要验证的字段在json中的key
		 * @return 如果需要验证的字段不为null或空字符串则返回true，否则返回false
		 * @throws JSONException 解析json数据时抛出的异常
		 */
		private boolean isValid(JSONObject jsonObject, String key) throws JSONException {
			boolean flag = false;
			if (jsonObject.has(key) && !jsonObject.isNull(key)) {
				flag = !"".equals(jsonObject.getString(key));
			}
			return flag;
		}
		
		private void isContinue(String result){
			//4.1版本以上
			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
			}else{
					Message message = mHandler.obtainMessage();
					message.obj = result;
					message.sendToTarget();
			}
		}
		
		private Handler mHandler = new  Handler(){
			public void handleMessage(Message msg) {
				String result =  (String) msg.obj;
				loadingDialog.dismiss();//关闭进度对话框
				if (TextUtils.isEmpty(result)) {//如果result为null，则提示网络错误
					ToastOrder.makeText(QuestionActivity.this, QuestionActivity.this.getResources().getString(R.string.ERROR_NETWORK), ToastOrder.LENGTH_SHORT);
				}
				
				//因为数据会在doInBackground中解析到qList和aList中，所以这里只要判断aList中是否有数据即可，
				//如果有数据，就通过填入Adapter模式填入lv_question
				if (qList != null && !qList.isEmpty()) {
					QuestionAdapter adapter = new QuestionAdapter(QuestionActivity.this);
					adapter.setDataSrcList(qList);
					lv_question.setAdapter(adapter);
				}
				else {
					ToastOrder.makeText(QuestionActivity.this, QuestionActivity.this.getResources().getString(R.string.no_data), ToastOrder.LENGTH_SHORT).show();
				}
			};
		};
	}

	@Override
	protected void onDestroy() {
		loadingDialog.dismiss();// 销毁当前Activity时关掉进度对话框
		super.onDestroy();
	}

}
