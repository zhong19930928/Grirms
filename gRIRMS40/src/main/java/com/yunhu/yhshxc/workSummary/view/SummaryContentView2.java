package com.yunhu.yhshxc.workSummary.view;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.MeetingAgenda.AgendaPlanActivty;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.wechat.content.Photo;
import com.yunhu.yhshxc.wechat.content.Voice;
import com.yunhu.yhshxc.workSummary.WorkSummaryCreateActivity;
import com.yunhu.yhshxc.workSummary.bo.WorkSummaryModle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 展示工作总结的视图view展示
 *
 */
public class SummaryContentView2 implements OnClickListener{

	private Context mContext;
	private AgendaPlanActivty activity;
	private View mView;
	private TextView tv_summary_content;//展示内容
	private LinearLayout ll_summary_photo;//展示图片,点击时放大展示
	private LinearLayout ll_summay_voice;//展示语音,点击时进行播放
	private WorkSummaryModle summaryModle;
	private List<SummaryPhotoView> photoViewList = null;
	private List<SummaryVoiceView> voiceViewList = null;
	private List<String> photoPathList = new ArrayList<>();
	private List<String> voicePathList = new ArrayList<>();//录音路径集合
	private LinearLayout ll_gz_create;
	private LinearLayout ll_gz_content;
	private String date;
	private int planId;
	public SummaryContentView2(Context context){
		mContext = context;
		activity = (AgendaPlanActivty) context;
		mView = LayoutInflater.from(context).inflate(R.layout.work_summary_detail_view, null);
		tv_summary_content = (TextView) mView.findViewById(R.id.tv_summary_content);
		ll_summary_photo = (LinearLayout) mView.findViewById(R.id.ll_summary_photo);
		ll_summay_voice = (LinearLayout) mView.findViewById(R.id.ll_summay_voice);
		ll_gz_create = (LinearLayout) mView.findViewById(R.id.ll_gz_create);
		ll_gz_content = (LinearLayout) mView.findViewById(R.id.ll_gz_content);
		ll_gz_create.setOnClickListener(this);
	}
	
	/**
	 * 设置数据实体对象
	 * @param summaryModle
	 */
	public void setSummaryModle(WorkSummaryModle summaryModle){
		this.summaryModle=summaryModle;
		
		//进行初始化
		if(summaryModle!=null){
			ll_gz_create.setVisibility(View.GONE);
			ll_gz_content.setVisibility(View.VISIBLE);
			initAllInfo();
		}else{
			ll_gz_content.setVisibility(View.GONE);
			ll_gz_create.setVisibility(View.VISIBLE);
		}
	}
	public void setDate(String date){
		this.date = date;
	}
	public void setPlanId(int planId){
		this.planId = planId;
	}

	private void initAllInfo() {
		tv_summary_content.setText(summaryModle.getSumMarks());
		//加载图片
		photoPathList = summaryModle.getPicUrl();
		final int widthPic = PublicUtils.convertDIP2PX(mContext, 80);
		final int heightPic = PublicUtils.convertDIP2PX(mContext, 60);
		if(photoPathList!=null&&photoPathList.size()>0){
			for (int i = 0; i < photoPathList.size(); i++) {
				SummaryPhotoViewPreview photoView = new SummaryPhotoViewPreview(mContext);
				Photo photo = new Photo();
				String url = photoPathList.get(i);
				int index = url.lastIndexOf("/");
				String fileName = url.substring(index + 1);
				photo.setName(fileName);
				photo.setPhotoPath(url);
				photoView.setPhotoView(photo);
				ll_summary_photo.addView(photoView.getView());
			}
		}
		
		//加载录音
		
		voicePathList =summaryModle.getRecordUrl();
		if(voicePathList!=null&&voicePathList.size()>0){
			for (int i = 0; i < voicePathList.size(); i++) {
				SummaryVoiceViewPreview voiceView = new SummaryVoiceViewPreview(mContext);
				Voice voice = new Voice();
				String url = voicePathList.get(i);
				int index = url.lastIndexOf("/");
				String fileName = url.substring(index + 1);
				voice.setName(fileName);
				voiceView.setVoiceView(voice);
				ll_summay_voice.addView(voiceView.getView());
				voiceView.setFilePath(voicePathList.get(i));
//				voiceViewList.add(voiceView);
//				voicePathList.add(Constants.SUMMARY_PATH_LOAD+name);
			}
		}
		
		
		
		
	}

	/**
	 * 获取当前view
	 * @return
	 */
	public View getView(){
		return mView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_gz_content:
			
			break;
		case R.id.ll_gz_create:
	         Date currentdate = DateUtil.getDate(DateUtil.getCurDate(),"yyyy-MM-dd");
			Date date1 =DateUtil.getDate(date,"yyyy-MM-dd");
			if(currentdate.before(date1)){
				Toast.makeText(mContext,R.string.summary_toast,Toast.LENGTH_SHORT).show();
			}else{
				Intent intent = new Intent(mContext,WorkSummaryCreateActivity.class);
				intent.putExtra("date", date);
//				intent.putExtra("planId", planId);
				activity.startActivityForResult(intent, 0);
			}
			break;

		default:
			break;
		}
		
	}
}
