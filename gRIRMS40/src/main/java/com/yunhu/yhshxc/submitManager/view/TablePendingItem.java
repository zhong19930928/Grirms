package com.yunhu.yhshxc.submitManager.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.database.TablePendingDB;
import com.yunhu.yhshxc.submitManager.SubmitManagerActivity;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;

public class TablePendingItem {
	private View view = null;
	private TextView tv_submit_manager_title;//标题
	private TextView tv_submit_manager_date;//日期
	private TextView tv_submit_manager_content;//内容
	private TextView tv_submit_fail_reason;//提交失败原因
	private ImageView iv_submit;//提交
	private ImageView iv_submit_icon;//标识
	private Context context;
	private SubmitManagerActivity submitManagerActivity;
	private ProgressBar bar_progress;
	public TablePendingItem(Context context) {
		this.context = context;
		submitManagerActivity = (SubmitManagerActivity) context;
		view = View.inflate(context, R.layout.table_pending_item, null);
		tv_submit_manager_title = (TextView)view.findViewById(R.id.tv_submit_manager_title);
		tv_submit_manager_date = (TextView)view.findViewById(R.id.tv_submit_manager_date);
		tv_submit_manager_content = (TextView)view.findViewById(R.id.tv_submit_manager_content);
		tv_submit_fail_reason = (TextView)view.findViewById(R.id.tv_submit_fail_reason);
		iv_submit = (ImageView)view.findViewById(R.id.iv_submit);
		iv_submit_icon = (ImageView)view.findViewById(R.id.iv_submit_icon);
		bar_progress = (ProgressBar)view.findViewById(R.id.bar_progress);
	}

	public void setData(final TablePending data){
		if (data!=null) {
			final int status = data.getStatus();
			tv_submit_manager_title.setText(TextUtils.isEmpty(data.getTitle())?"":data.getTitle());
			tv_submit_manager_date.setText(TextUtils.isEmpty(data.getCreateDate())?"":data.getCreateDate());
			tv_submit_manager_content.setText(TextUtils.isEmpty(data.getContent())?"":data.getContent());
			iv_submit.setBackgroundResource(R.drawable.iv_submit_click);
			iv_submit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (status!=TablePending.STATUS_SUBIMTTING && status!=TablePending.STATUS_READY) {
						new TablePendingDB(context).updateTablePendingErrorStatusToReadyById(data.getId());
						submitManagerActivity.refrshTablePendingList();
						SubmitWorkManager.getInstance(context).commit();
					}
					
				}
			});
			int dataType = data.getType();
			if (dataType == TablePending.TYPE_DATA || dataType == TablePending.TYPE_LOATION) {
				iv_submit_icon.setBackgroundResource(R.drawable.icon_data);
			}else if (dataType == TablePending.TYPE_AUDIO) {
				iv_submit_icon.setBackgroundResource(R.drawable.icon_rediao);
			}else if (dataType == TablePending.TYPE_IMAGE) {
				iv_submit_icon.setBackgroundResource(R.drawable.icon_pic);
			}
			bar_progress.setVisibility(View.GONE);
			iv_submit.setVisibility(View.VISIBLE);
			tv_submit_fail_reason.setVisibility(View.VISIBLE);
			if (status == TablePending.STATUS_SUBIMTTING) {
				bar_progress.setVisibility(View.VISIBLE);
				tv_submit_fail_reason.setVisibility(View.INVISIBLE);
				iv_submit.setVisibility(View.INVISIBLE);
//				tv_submit_fail_reason.setText("正在提交...");
//				tv_submit_fail_reason.setTextColor(context.getResources().getColor(R.color.darkGray));
			}else if (status == TablePending.STATUS_READY) {
				iv_submit.setVisibility(View.INVISIBLE);
				tv_submit_fail_reason.setText(R.string.utility_string40);
				tv_submit_fail_reason.setTextColor(context.getResources().getColor(R.color.green));
			}else if (status == TablePending.STATUS_ERROR_NETWORK) {
				tv_submit_fail_reason.setText(R.string.utility_string41);
				tv_submit_fail_reason.setTextColor(context.getResources().getColor(R.color.red));
			}else if (status == TablePending.STATUS_ERROR_BIG_SIZE) {
				tv_submit_fail_reason.setText(R.string.utility_string42);
				tv_submit_fail_reason.setTextColor(context.getResources().getColor(R.color.red));
			}else if (status == TablePending.STATUS_ERROR_SERVER) {
				tv_submit_fail_reason.setText(R.string.utility_string43);
				tv_submit_fail_reason.setTextColor(context.getResources().getColor(R.color.red));
			}else if (status == TablePending.STATUS_ERROR_USER) {
				tv_submit_fail_reason.setText(R.string.utility_string42);
				tv_submit_fail_reason.setTextColor(context.getResources().getColor(R.color.red));
			}
		}
	}
	
	public View getView(){
		return view;
	}
}
