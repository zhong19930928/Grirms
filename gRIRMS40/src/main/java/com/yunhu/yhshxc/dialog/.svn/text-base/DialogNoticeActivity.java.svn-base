package com.yunhu.yhshxc.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Notice;
import com.yunhu.yhshxc.database.NoticeDB;
import com.yunhu.yhshxc.http.CoreHttpHelper;
import com.yunhu.yhshxc.parser.CacheData;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.UrlInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import gcg.org.debug.JLog;

/**
 * 必读公告展示
 * @author gcg_jishen
 *
 */
public class DialogNoticeActivity extends Activity{
	private final String TAG = "DialogNoticeActivity";
	private LinearLayout ll_content;
	private boolean isAdd = true;
	private String result;
	//private WakeLock mWakeLock;
//	private List<Notice> noticeList_;
	private NoticeDB noticeDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_notice);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		noticeDB=new NoticeDB(this);

		int high=getWindowManager().getDefaultDisplay().getHeight();
		int width=getWindowManager().getDefaultDisplay().getWidth();
		LayoutParams params = getWindow().getAttributes();   
//        params.height = high;    
        params.height = (int)(high * 0.8);    
        params.width = width;   
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params); 
		ll_content = (LinearLayout)this.findViewById(R.id.ll_content);
//		ll_content.addView(View.inflate(this, R.layout.dialog_notice_item, null));
//		ll_content.addView(View.inflate(this, R.layout.dialog_notice_item, null));
//		ll_content.addView(View.inflate(this, R.layout.dialog_notice_item, null));
//		ll_content.addView(View.inflate(this, R.layout.dialog_notice_item, null));
		JLog.d(TAG, "onCreate");
		result = this.getIntent().getStringExtra("push_notice");
		add();
	}
	
	/**
	 * 添加公告内容
	 */
	private void add(){
		JLog.d(TAG, "isAdd =>"+isAdd);
		
		if(isAdd){
			if(!TextUtils.isEmpty(result)){
				JLog.d(TAG, result);
				CacheData cacheData = new CacheData(this);
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.has(cacheData.NOTIFY)) {
						String jsonForDict = jsonObject.getString(cacheData.NOTIFY);
						List<Notice> noticeList = cacheData.parseGetNotify(jsonForDict);
//						noticeList_.addAll(noticeList);
						View itemView = null;
						for(Notice notice : noticeList){
							
							itemView = View.inflate(this, R.layout.dialog_notice_item, null);
	
							TextView tv_title = (TextView)itemView.findViewById(R.id.tv_title);
							tv_title.setText(notice.getNoticeTitle());
							
							TextView tv_createTime = ((TextView)itemView.findViewById(R.id.tv_createTime));
							tv_createTime.setText(notice.getCreateTime());
							
							TextView tv_createUser = (TextView)itemView.findViewById(R.id.tv_createUser);
							tv_createUser.setText(notice.getCreateUser());
							
							TextView tv_createOrg = (TextView)itemView.findViewById(R.id.tv_createOrg);
							tv_createOrg.setText(notice.getCreateOrg());
							
							TextView tv_content = (TextView)itemView.findViewById(R.id.tv_content);
							tv_content.setText(notice.getDetailNotice());
							
							ll_content.addView(itemView);
							
							final int notifyId=notice.getNotifyId();
							noticeDB.updateNoticeReadStateById(notifyId, Notice.ISREAD_Y);//更改已读状态
							String url=UrlInfo.getUrlIsReadNotify(DialogNoticeActivity.this)+"?"+"ids="+notifyId;
							
							PendingRequestVO vo = new PendingRequestVO();
				        	vo.setContent(notice.getNoticeTitle());
				        	vo.setTitle(getResources().getString(R.string.announcement));
				        	vo.setMethodType(SubmitWorkManager.HTTP_METHOD_GET);
				        	vo.setType(TablePending.TYPE_DATA);
				        	vo.setUrl(url);
				    		new CoreHttpHelper().performJustSubmit(this,vo);
						}
					}
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
			
			}
		}else{
			isAdd = true;
		}
	}

	public void onClick(View view){
		
		switch(view.getId()){
			case R.id.btn_confirm:
				sendBroadcast(new Intent("cancle_type_wechat_redpoint"));
				this.finish();
				
				break;

			default:
		}
	}



	@Override
	protected void onNewIntent(Intent intent) {
		
		super.onNewIntent(intent);
		result = intent.getStringExtra("push_notice");
		add();
		JLog.d(TAG, "onNewIntent");
	}



	@Override
	protected void onDestroy() {
	
		super.onDestroy();
//		if (mWakeLock != null) {
//			mWakeLock.release();
//		}

	}
	
	/**
	 * 处理  dialog样式的activity点击空白处自动关闭
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds(this, event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	private boolean isOutOfBounds(Activity context, MotionEvent event) {
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
		final View decorView = context.getWindow().getDecorView();
		return (x < -slop) || (y < -slop)|| (x > (decorView.getWidth() + slop))|| (y > (decorView.getHeight() + slop));
	}
}
