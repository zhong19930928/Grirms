package com.yunhu.yhshxc.workplan;

import gcg.org.debug.JLog;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.yhshxc.workplan.bo.Assess;
import com.loopj.android.http.RequestParams;

public class AssessItemView {
	private Context context;
	private View view;
	private TextView tv_item_assess_content;//评价内容
	private TextView tv_item_assess_person;//评价人
	private LinearLayout ll_content_item;//展示评价
	private LinearLayout ll_edit_item;//编辑评价
	private EditText assess_item_content_et;
	private Button btn_submit_assess;//提交按钮
	private int planId;
	public AssessItemView(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.item_work_plan_assess, null);
		tv_item_assess_content = (TextView) view.findViewById(R.id.tv_item_assess_content);
		tv_item_assess_person = (TextView) view.findViewById(R.id.tv_item_assess_person);
		ll_content_item = (LinearLayout) view.findViewById(R.id.ll_content_item);
		ll_edit_item = (LinearLayout) view.findViewById(R.id.ll_edit_item);
		assess_item_content_et = (EditText) view.findViewById(R.id.assess_item_content_et);
		btn_submit_assess = (Button) view.findViewById(R.id.btn_submit_assess);
		btn_submit_assess.setOnClickListener(listener);
	}
	public void setPlanId(int planId){
		this.planId = planId;
	}
	public View getView(){
		return view;
	}
	
	public void initData(Assess data){
		if(data!=null){
			ll_content_item.setVisibility(View.VISIBLE);
			ll_edit_item.setVisibility(View.GONE);
			tv_item_assess_content.setText(data.getMarks());
			tv_item_assess_person.setText(PublicUtils.getResourceString(context,R.string.work_plan_c28)+data.getUser_name());
		}else{
			ll_content_item.setVisibility(View.GONE);
			ll_edit_item.setVisibility(View.VISIBLE);
			
		}
	}
	
	/**
	 * 获取填写的所有内容,返回一个Assess对象
	 */
	public Assess getAssessData(int planid,String useid,String username){
		Assess ass = new Assess();
		ass.setPlan_id(planid);
        ass.setUser_id(useid);
        ass.setUser_name(username);
        ass.setMarks(assess_item_content_et.getText().toString().trim());
        return ass;
	}
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_submit_assess:
				submit();
				break;

			default:
				break;
			}
		}
	};
	
	
	private RequestParams searchParams() {
		
		
		RequestParams param = new RequestParams();
		JSONObject obj = new JSONObject();
		try {
			obj.put("plan_id", planId);
			obj.put("marks",assess_item_content_et.getText().toString() );
		} catch (JSONException e) {
			e.printStackTrace();
		}
		param.put("work", obj.toString());

		return param;
	}

	private Dialog mDialog;

	private void submit() {
		//先清空数据存储的上次查询数据
		mDialog = new MyProgressDialog(context, R.style.CustomProgressDialog, PublicUtils.getResourceString(context,R.string.init));
		String url = UrlInfo.saveWorkPlanAssess(context);
		RequestParams param = searchParams();
		GcgHttpClient.getInstance(context).post(url, param, new HttpResponseListener() {
			@Override
			public void onStart() {
				mDialog.show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				Log.e("view", "onSuccess:" + content);
				// 解析加载下来的数据
				try {
					JSONObject obj = new JSONObject(content);
					String resultCode = obj.getString("resultcode");
					if ("0000".equals(resultCode)) {
						if (assessListener!=null) {
							assessListener.onSubmitSucess();	
						}
						ToastOrder.makeText(context, R.string.submit_ok, ToastOrder.LENGTH_SHORT).show();
					} else{
						
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastOrder.makeText(context, R.string.ERROR_DATA, ToastOrder.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				JLog.d("alin", "onFailure:" + content);
				ToastOrder.makeText(context, R.string.retry_net_exception, ToastOrder.LENGTH_SHORT).show();
			}

			@Override
			public void onFinish() {
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
			}

		});


		
	}
	private AssessSubmitSucessListener assessListener;
	public void setAssessSubmitListener(AssessSubmitSucessListener listener){
		this.assessListener=listener;
	}
	
	public interface AssessSubmitSucessListener{
		/**
		 * 提交评论成功的回调
		 */
		void onSubmitSucess();
	}

}
