package com.yunhu.yhshxc.notify;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Notice;
import com.yunhu.yhshxc.database.NoticeDB;
import com.yunhu.yhshxc.http.CoreHttpHelper;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.UrlInfo;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * 
 * @author jishen 公告列表的详细信息
 * 
 */
public class NotifyDetailActivity extends AbsBaseActivity {
	/**
	 * notifyDetailTitle 公告标题
	 * notifyDetailContent 公告详细内容
	 * notifyDetaildate 公告的创建日期
	 * notifyDetailOrg公告下发人所在的机构
	 * notifyDetailCreater公告下发人
	 * noticeType公告类型  "n" 表示非系统公告
	 */
	private TextView notifyDetailTitle, notifyDetailContent,notifyDetaildate,notifyDetailOrg,notifyDetailCreater,noticeType;
	/**
	 * 公告删除按钮
	 */
	private LinearLayout notifyDetailDelete;
	/**
	 * 公告删除图标
	 */
	private ImageView  notifyDetailDelete_iv;
	/**
	 * 当前的公告ID 主键
	 */
	private int currentNoticeId;
	/**
	 * 公告数据库
	 */
	private NoticeDB noticeDB;
	/**
	 * 弹出删除公告的PopupWindow
	 */
	private PopupWindow popupWindow;
	/**
	 * 公告id
	 */
	private Integer notifyId;
	/**
	 * 公告是否已读 0未读 1 是已读
	 */
	private Integer isRead;
	/**
	 * 公告类型 系统公告 和 非系统公告（“n”）
	 */
	private String notifyType;
	
	private LinearLayout ll_attachment;//公告附件
	private String attachment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify_detail);
		initBase();
		noticeDB=new NoticeDB(this);
		noticeType = (TextView) findViewById(R.id.notice_type);
		notifyDetailTitle = (TextView) findViewById(R.id.notify_detail_title);
		notifyDetailContent = (TextView) findViewById(R.id.notify_detail_content);
		notifyDetaildate = (TextView) findViewById(R.id.notify_list_detail_data);
		notifyDetailOrg = (TextView) findViewById(R.id.notify_detail_bumen);
		notifyDetailCreater = (TextView) findViewById(R.id.notify_detail_ren);
		notifyDetailDelete = (LinearLayout) findViewById(R.id.notify_detail_delete);
		notifyDetailDelete_iv = (ImageView) findViewById(R.id.notice_detail_delete_iv);
		ll_attachment = (LinearLayout)findViewById(R.id.ll_notify_attachment);
		notifyDetailDelete.setOnClickListener(this);
		notifyDetailDelete.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_UP){
					notifyDetailDelete_iv.setBackgroundResource(R.drawable.notice_del2);
				}else{
					notifyDetailDelete_iv.setBackgroundResource(R.drawable.notice_del1);
				}
				return false;
			}
		});
		bindData();
	}

	
	
	
	/**
	 * 给详细信息组件绑定数据
	 * 获取传过来的值
	 */
	private void bindData() {

		Bundle bundle = getIntent().getBundleExtra("currentNotice");
		notifyType=bundle.getString("notifyType");
		//Title设置为粗体
		String str=bundle.getString("currentNoticeTitle");
		notifyDetailTitle.setText(str);//设置标题
		notifyDetailContent.setText("			"+bundle.getString("currentNoticeDetail"));//首行缩进 公告详细内容
		notifyDetaildate.setText(bundle.getString("currentNoticeDate"));//公告创建日期
		notifyDetailOrg.setText(bundle.getString("currentNoticeOrg"));//公告下发人机构
		notifyDetailCreater.setText(bundle.getString("currentNoticeCreater"));//公告下发人
		currentNoticeId = bundle.getInt("currentNoticeId");//当前的公告ID 主键
		attachment = bundle.getString("attachment");
		initAttachment();
		notifyId=bundle.getInt("notifyId");//公告id
		isRead=bundle.getInt("isRead");//是否已读
		if(isRead==Notice.ISREAD_N && !TextUtils.isEmpty(notifyType) && notifyType.equalsIgnoreCase("n")){//未读公告并且不是系统公告的话将公告id发给服务器
			noticeDB.updateNoticeReadStateById(notifyId, Notice.ISREAD_Y);//更改已读状态
			String url=UrlInfo.getUrlIsReadNotify(NotifyDetailActivity.this)+"?"+"ids="+notifyId;
			
			PendingRequestVO vo = new PendingRequestVO();
        	vo.setContent(notifyDetailTitle.getText().toString());
        	vo.setTitle(getResources().getString(R.string.notify_activity_09));
        	vo.setMethodType(SubmitWorkManager.HTTP_METHOD_GET);
        	vo.setType(TablePending.TYPE_DATA);
        	vo.setUrl(url);
    		new CoreHttpHelper().performJustSubmit(this,vo);
		}
	}
	
	/**
	 * 初始化附件布局
	 */
	private void initAttachment(){
		try {
			if (!TextUtils.isEmpty(attachment)) {
				JSONObject  attObj = new JSONObject(attachment);
				Iterator<String> iterator = attObj.keys();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String url = attObj.getString(key);
					if (!(url.endsWith(".zip") || url.endsWith(".rar"))) {//压缩文件格式在手机端不显示
						NotifyAttachmentView attView = new NotifyAttachmentView(this);
						attView.setAttachmentData(key,url);
						ll_attachment.addView(attView.getView());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 点击删除按钮删除该条信息
	 */

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.notify_detail_delete:
			v.setEnabled(false);
			showDeleteDialog();
			v.setEnabled(true);
			break;
		default:
			break;
		}

	}
	
	/*
	 * 删除该条公告
	 */
	private void deleteNotice(int id) {
		if(id!=-1){
			noticeDB.deleteNoticeById(id+"");
		}
		NotifyDetailActivity.this.finish();
	}
	
	/**
	 * 弹出删除公告对话框
	 */
	private void showDeleteDialog(){
		View contentView = View.inflate(this, R.layout.delete_prompt, null);
		popupWindow = new PopupWindow(contentView,WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.FILL_PARENT,true);
		popupWindow.setAnimationStyle(R.style.popupwindow);
		int high=getWindowManager().getDefaultDisplay().getHeight();
		popupWindow.showAsDropDown(notifyDetailDelete,0,-(high/4+15));
		contentView.findViewById(R.id.btn_confirm).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				deleteNotice(currentNoticeId);
			}
		});
		contentView.findViewById(R.id.btn_canceled).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	}
}
