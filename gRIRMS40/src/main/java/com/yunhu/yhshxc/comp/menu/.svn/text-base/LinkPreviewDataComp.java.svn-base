package com.yunhu.yhshxc.comp.menu;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.VisitFuncDB;

/**
 * 超链接预览view
 * @author jishen
 */
public class LinkPreviewDataComp extends Menu {
	private Context context;
	private View view;
	private LinearLayout ll_link_priview_content;
	private Func func;
	private int level=0;
	private int preLevel=0;
	public LinkPreviewDataComp(Context context,Func func) {
		this.context = context;
		this.func=func;
		view = View.inflate(context, R.layout.link_priview_data_comp, null);
		ll_link_priview_content=(LinearLayout)view.findViewById(R.id.ll_link_priview_content);
	}

	public void addShowDataContent(View content) {
		ll_link_priview_content.addView(content);
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
	 * 控制有多层超链接数据的时候层级显示
	 * @param funcid
	 */
	public void initUI(int funcid){
		//要查询提交表中的数据，因为超链接点击保存的时候把临时表中的数据存到提交表中，如果查临时表可能查询不到数据
		SubmitItemDB submitItemDB=new SubmitItemDB(context);
		SubmitDB submitDB=new SubmitDB(context);
		FuncDB funcDB=new FuncDB(context);
		SubmitItem linkItem=submitItemDB.findSubmitItemByFuncId(funcid);
		Submit submit=submitDB.findSubmitById(linkItem.getSubmitId());
		if(submit.getTargetType()==2){//访店
			func = new VisitFuncDB(context).findFuncListByFuncIdAndTargetId(linkItem.getParamName(), submit.getTargetid());
		}else{
			func = funcDB.findFuncListByFuncIdAndTargetId(linkItem.getParamName(), submit.getTargetid());
		}
		LinkPreviewDataItem linkPreviewDataItem=new LinkPreviewDataItem(context);
		linkPreviewDataItem.setLinkName(func.getName());
		linkPreviewDataItem.setLevel(level);
		View linkView=linkPreviewDataItem.getView();
		linkView.setTag(linkItem);
		addShowDataContent(linkView);
		
		
		int submitId = submitDB.selectSubmitIdNotCheckOut(submit.getPlanId(), submit.getWayId(), submit.getStoreId(),
				func.getMenuId(), 3,Submit.STATE_NO_SUBMIT);
		List<SubmitItem> list=submitItemDB.findLinkSubmitItemBySubmitId(submitId);
		if(!list.isEmpty()){
			level++;

			for (int i = 0; i < list.size(); i++) {
				initUI(Integer.parseInt(list.get(i).getParamName()));
			}
			level--;
//			List<SubmitItem> linkList=new ArrayList<SubmitItem>();
//			for (int i = 0; i < list.size(); i++) {
//				SubmitItem item=list.get(i);
//				if(TextUtils.isEmpty(item.getParamValue())||!isOther(item.getParamName())){//如果是其他类型的跳过
//					continue;
//				}
//				Func currentFunc=funcDB.findFuncByFuncId(Integer.parseInt(item.getParamName()));
//				if(currentFunc.getType()!=Func.TYPE_LINK){
//					LinkPreviewDataItem dataItem=new LinkPreviewDataItem(context);
//					dataItem.setLinkName(currentFunc.getName());
//					dataItem.setVisiblePriview(false);
//					dataItem.setLevel(level);
//					View view=dataItem.getView();
//					addShowDataContent(view);
//				}else{
//					linkList.add(item);
//				}
//			}
//			if(!linkList.isEmpty()){
//				for (int i = 0; i < linkList.size(); i++) {
//					initUI(Integer.parseInt(linkList.get(i).getParamName()));
//				}
//			}
		}
	}
	
	/*
	 * 验证其他下拉框
	 */
	public boolean isOther(String value) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
}
