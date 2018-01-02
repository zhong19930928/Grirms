package com.yunhu.yhshxc.wechat.topic;

import com.yunhu.yhshxc.R;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddApprovalSubView {
	private View view;
	private Context context;
	private TextView tv_approval_bh;
	private EditText et_approval_content;
	private AddApprovalSub approvalSub;
	public AddApprovalSubView(Context mContext) {
		view = View.inflate(mContext, R.layout.add_approval_sub_view, null);
		tv_approval_bh = (TextView)view.findViewById(R.id.tv_approval_bh);
		et_approval_content = (EditText)view.findViewById(R.id.et_approval_content);
	}
	
	public void setData( AddApprovalSub data){
		if (data!=null) {
			this.approvalSub = data;
			String number = approvalSub.getNumber();
			String content = approvalSub.getContent();
			if (!TextUtils.isEmpty(number)) {
				tv_approval_bh.setText(number);
			}
			if (!TextUtils.isEmpty(content)) {
				et_approval_content.setText(content);
			}
		}
	}
	
	public AddApprovalSub getAddapprovalSub(){
		if (approvalSub == null) {
			approvalSub = new AddApprovalSub();
		}
		approvalSub.setNumber(tv_approval_bh.getText().toString());
		approvalSub.setContent(et_approval_content.getText().toString());
		return approvalSub;
	}
	
	public View getView(){
		
		return view;
	}
}
