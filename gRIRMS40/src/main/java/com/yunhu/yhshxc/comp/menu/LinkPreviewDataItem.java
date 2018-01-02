package com.yunhu.yhshxc.comp.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.FuncDetailActivity;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.VisitFuncDB;

public class LinkPreviewDataItem extends Menu {
	private TextView tv_link_name;
	private ImageView showDataIcon;
	private View view;
	private LinearLayout ll_item;
	private Context mContext;
	private ArrayList<String> orgMapValueList = new ArrayList<String>();//超链接预览编辑跳转到上报页面的时候把机构关系传过去
	private StringBuffer orgBuffer=new StringBuffer();
	
	public LinkPreviewDataItem(final Context context) {
		this.mContext=context;
		view = View.inflate(context, R.layout.link_priview_data_item_comp, null);
		tv_link_name = (TextView) view.findViewById(R.id.tv_link_item_name);
		showDataIcon = (ImageView) view.findViewById(R.id.iv_link_item_priview);
		ll_item=(LinearLayout)view.findViewById(R.id.ll_link_item);
		showDataIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SubmitItem item=(SubmitItem) getView().getTag();
				showPriview(item);
			}
		});
	}


	public void setLinkName(String name){
		tv_link_name.setText(name);
	}
	
	public void setVisiblePriview(boolean isVisibile){
		if(isVisibile){
			showDataIcon.setVisibility(View.VISIBLE);
		}else{
			showDataIcon.setVisibility(View.GONE);
		}
	}
	public void setLevel(int level){
		ll_item.setPadding(30*level, 0, 0, 0);
	}

	public void setShowDataIcon(int resid){
		showDataIcon.setBackgroundResource(resid);
	}
	
	@Override
	public View getView() {
		return view;
	}

	@Override
	public void setBackgroundResource(int resid) {
		view.setBackgroundResource(resid);
	}
	
	/**
	 * 跳转到预览页面
	 * @param item
	 */
	private void showPriview(SubmitItem item){
		SubmitDB submitDB=new SubmitDB(mContext);
		Submit submit=submitDB.findSubmitById(item.getSubmitId());
		Func func=null;
		if(submit.getTargetType()== com.yunhu.yhshxc.bo.Menu.TYPE_VISIT){
			func=new VisitFuncDB(mContext).findFuncByFuncId(Integer.parseInt(item.getParamName()));
		}else{
			func=new FuncDB(mContext).findFuncByFuncId(Integer.parseInt(item.getParamName()));
		}
		int submitId = submitDB.selectSubmitIdNotCheckOut(submit.getPlanId(), submit.getWayId(), submit.getStoreId(),
				func.getMenuId(), 3,Submit.STATE_NO_SUBMIT);
		Intent intent = new Intent(mContext, FuncDetailActivity.class);
		intent.putExtra("submitId", submitId);//是指超链接控件所关联的下层的id
		intent.putExtra("linkKey", Integer.parseInt(item.getParamName()));//超链接控件的funcid
		intent.putExtra("linkId", item.getSubmitId());// 超链接的时候把上级SubimtID传过去
		intent.putExtra("targetId", func.getMenuId());
		intent.putExtra("menuType", submit.getTargetType());
		intent.putExtra("isFromLinkPriview", true);//表示是从预览界面超链接跳转到下层超链接预览
		setOrgRelation(submitId);
		if(orgBuffer.length()>1){
			orgMapValueList.add(func.getMenuId()+orgBuffer.toString());
			intent.putStringArrayListExtra("map",orgMapValueList);
		}
		mContext.startActivity(intent);
	}
	
	
	/**
	 * 机构关系
	 * @param func
	 * @param value
	 */
	private void setOrgRelation(int submitId){
		
		List<SubmitItem> itemList=new SubmitItemDB(mContext).findSubmitItemBySubmitId(submitId);
		for (int i = 0; i < itemList.size(); i++) {
			SubmitItem item=itemList.get(i);
			if(!isInteger(item.getParamName())){//机构里面不包含其他
				continue;
			}
			Func func=new FuncDB(mContext).findFuncByFuncId(Integer.parseInt(item.getParamName()));
			if(func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION){
				orgBuffer.append(";").append(func.getOrgLevel()).append(":").append(item.getParamValue());
			}
		}
		
	}
	
	/*
	 * 验证整数
	 */
	public boolean isInteger(String value) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
}
