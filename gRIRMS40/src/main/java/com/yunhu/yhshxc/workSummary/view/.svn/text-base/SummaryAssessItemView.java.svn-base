package com.yunhu.yhshxc.workSummary.view;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.yunhu.yhshxc.workSummary.bo.SummaryAssess;
import com.yunhu.yhshxc.workplan.bo.Assess;
import com.loopj.android.http.RequestParams;

public class SummaryAssessItemView {
	private Context context;
	private View view;
	private TextView tv_item_assess_content;//评价内容
	private TextView tv_item_assess_person;//评价人
	private LinearLayout ll_content_item;//展示评价
	private LinearLayout ll_edit_item;//编辑评价
	private LinearLayout ll_shenpi;
	private EditText assess_item_content_et;
	private Button btn_submit_assess;//提交按钮
	private int id;
	private Spinner sp_asssess;
	private Spinner sp_asssess1;
	private int positio;
	 private List<String> list = new ArrayList<String>(); 
	private ArrayAdapter<String> adapter; 
	public SummaryAssessItemView(final Context context){
		this.context = context;
		view = View.inflate(context, R.layout.item_work_summary_assess, null);
		tv_item_assess_content = (TextView) view.findViewById(R.id.tv_item_assess_content);
		tv_item_assess_person = (TextView) view.findViewById(R.id.tv_item_assess_person);
		ll_content_item = (LinearLayout) view.findViewById(R.id.ll_content_item);
		ll_shenpi = (LinearLayout) view.findViewById(R.id.ll_shenpi);
		ll_edit_item = (LinearLayout) view.findViewById(R.id.ll_edit_item);
		assess_item_content_et = (EditText) view.findViewById(R.id.assess_item_content_et);
		btn_submit_assess = (Button) view.findViewById(R.id.btn_submit_assess);
		sp_asssess = (Spinner) view.findViewById(R.id.sp_asssess);
		sp_asssess1 = (Spinner) view.findViewById(R.id.sp_asssess1);
		btn_submit_assess.setOnClickListener(listener);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。 
		list.add(PublicUtils.getResourceString(context,R.string.excellent));
		list.add(PublicUtils.getResourceString(context,R.string.good));
		list.add(PublicUtils.getResourceString(context,R.string.qualified));
		list.add(PublicUtils.getResourceString(context,R.string.un_qualified));
		adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item, list);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。    
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        //第四步：将适配器添加到下拉列表上    
        sp_asssess.setAdapter(adapter);    
        sp_asssess1.setAdapter(adapter);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中    
        sp_asssess1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tv = (TextView) view;
				if(tv!=null){
//					android:textColor="@color/work_plan_text_color"
//			                android:textSize="@dimen/work_plan_content_text_size"
			        tv.setTextColor(context.getResources().getColor(R.color.work_plan_text_color));
//			        tv.setTextSize(PublicUtils.convertDIP2PX(context, 14));
				}
				positio = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
        sp_asssess.setOnItemSelectedListener(new OnItemSelectedListener() {
        	
        	@Override
        	public void onItemSelected(AdapterView<?> parent, View view,
        			int position, long id) {
        		TextView tv = (TextView) view;
        		if(tv!=null){
//					android:textColor="@color/work_plan_text_color"
//			                android:textSize="@dimen/work_plan_content_text_size"
        			tv.setTextColor(context.getResources().getColor(R.color.work_plan_text_color));
//        			tv.setTextSize(PublicUtils.convertDIP2PX(context, 14));
        		}
//        		positio = position;
        	}
        	
        	@Override
        	public void onNothingSelected(AdapterView<?> parent) {
        		// TODO Auto-generated method stub
        		
        	}
        });
	}
	public void setWorkId(int id){
		this.id = id;
	}
	public View getView(){
		return view;
	}
	private SummaryAssess assess;
	public void initData(SummaryAssess data){
		if(data!=null){
			this.assess = data;
			ll_content_item.setVisibility(View.VISIBLE);
			ll_edit_item.setVisibility(View.GONE);
			String type = data.getAudit_type();
			if(!TextUtils.isEmpty(type)){
				sp_asssess.setSelection(Integer.parseInt(data.getAudit_type())-1);
				tv_item_assess_content.setText(data.getAudit_marks());
				tv_item_assess_person.setText(PublicUtils.getResourceString(context,R.string.approve_by_person)+": "+data.getAuditName());
			}else{
				tv_item_assess_content.setText(PublicUtils.getResourceString(context,R.string.un_approve));
				tv_item_assess_person.setVisibility(View.GONE);
				ll_shenpi.setVisibility(View.GONE);
			}
			
			sp_asssess.setEnabled(false);
			
		}else{
			ll_content_item.setVisibility(View.GONE);
			ll_edit_item.setVisibility(View.VISIBLE);
			
		}
	}
	
	private void setText(String string) {
		// TODO Auto-generated method stub
		
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
			obj.put("id", id);//工作总结id
				
				obj.put("audit_status","1"); //  审批状态（0：未审批；1：已审批）
			obj.put("audit_type",positio+1);   // 审批满意度类型（1:优秀、2:良好、3:合格、4:不合格）
			obj.put("audit_marks",assess_item_content_et.getText().toString());   // 审批内容
		} catch (JSONException e) {
			e.printStackTrace();
		}
		param.put("work", obj.toString());

		return param;
	}

	private Dialog mDialog;

	private void submit() {
		//先清空数据存储的上次查询数据
		mDialog = new MyProgressDialog(context, R.style.CustomProgressDialog, PublicUtils.getResourceString(context,R.string.submint_loding));
		String url = UrlInfo.saveWorkSumAudit(context);
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
						ToastOrder.makeText(context, PublicUtils.getResourceString(context,R.string.submit_ok), ToastOrder.LENGTH_SHORT).show();
					} else{
						
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastOrder.makeText(context, PublicUtils.getResourceString(context,R.string.ERROR_DATA), ToastOrder.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				JLog.d("alin", "onFailure:" + content);
				ToastOrder.makeText(context,PublicUtils.getResourceString(context,R.string.ERROR_NETWORK), ToastOrder.LENGTH_SHORT).show();
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
