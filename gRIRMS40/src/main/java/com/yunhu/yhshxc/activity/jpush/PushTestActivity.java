package com.yunhu.yhshxc.activity.jpush;

import gcg.org.debug.JLog;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.wechat.bo.Group;
import com.yunhu.yhshxc.wechat.db.GroupDB;
import com.loopj.android.http.RequestParams;

public class PushTestActivity extends Activity {
	private Button initBtn,sendBtn;
	private EditText et_tag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.push_test);
		et_tag = (EditText)findViewById(R.id.et_tag);
		sendBtn = (Button)findViewById(R.id.button_send);
		sendBtn.setOnClickListener(listener);
		initBtn = (Button)findViewById(R.id.button_init);
		initBtn.setOnClickListener(listener);
	}
	
	private void initPush(){
		JPushInterface.init(getApplicationContext());
		JPushInterface.setDebugMode(true);
		Set<String> tags = new HashSet<String>();
		List<Group> groupList = new GroupDB(this).findGroupList();
		for (int i = 0; i < groupList.size(); i++) {
			Group group = groupList.get(i);
			tags.add(String.valueOf(group.getGroupId()));//添加本用户所在的所有群id
		}
		setTags(tags);
		setAlias(PublicUtils.receivePhoneNO(this));
		Log.i("jishen", "RegistrationID:"+JPushInterface.getRegistrationID(this));
		Log.i("jishen", "UDID:"+JPushInterface.getUdid(this));
		Log.i("jishen", "ConnectonState:"+JPushInterface.getConnectionState(this));
	}
	
	private void send(){
		String tags = et_tag.getText().toString();
		String url = "http://219.148.162.89:9090/com.grandison.grirms.phone.web-1.0.0/jpushMessageInfo.do";
		GcgHttpClient.getInstance(this).post(url, params("", tags), new HttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				JLog.d("jishen", "onSuccess:"+content);
			}
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				JLog.d("jishen", "onFailure:"+content);
			}
		});
	}
	
	
	private RequestParams params(String ids,String tags){
		RequestParams params = new RequestParams();
		params.put("registrationIds", ids);
		params.put("tags", tags);
		params.put("title", PublicUtils.getResourceString(PushTestActivity.this,R.string.title));
		params.put("alertContent", "AertContent内容");
		return params;
	}
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_send:
				send();
				break;
			case R.id.button_init:
				initPush();
				break;

			default:
				break;
			}
		}
	};
	
	/**
	 * 设置标签，用群ID
	 * @param tags
	 */
	private void setTags(Set<String> tags){
		JPushInterface.setTags(this, tags, new TagAliasCallback() {//设置标签
			
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				Log.i("jishen", "setTags-----arg0:"+arg0);
				Log.i("jishen", "setTags-----arg1:"+arg1);
			}
		});
	}
	
	/**
	 * 设置别名
	 * @param alias
	 */
	private void setAlias(String alias){
		JPushInterface.setAlias(this, alias, new TagAliasCallback() {//设置别名
			
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				Log.i("jishen", "setAlias----arg0:"+arg0);
				Log.i("jishen", "setAlias----arg1:"+arg1);
//				Log.i("jishen", "arg2:"+arg2.toString());
			}
		});
	}
}
