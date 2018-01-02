package com.yunhu.yhshxc.adapter;

import java.text.ParseException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yunhu.yhshxc.bo.Notice;
import com.yunhu.yhshxc.comp.NotifyListItem;
import com.yunhu.yhshxc.notify.NotifyDetailActivity;
import com.yunhu.yhshxc.notify.NotifyListActivity;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 
 * @author jishen 公告列表适配器
 */
public class NotifyListAdapter extends BaseAdapter {
	private String TAG="NotifyListAdapter";
	private Context context;
	/**
	 * 当前所有公告
	 */
	private List<Notice> noticeList;
	/**
	 * 当前公告的位置
	 */
	private int currentPos;
	public NotifyListAdapter(Context context, List<Notice> noticeList) {
		this.context = context;
		this.noticeList = noticeList;
	}

	public int getCount() {
		return noticeList.size();
	}

	public Object getItem(int pos) {
		return 0;
	}

	public long getItemId(int pos) {
		return pos;
	}

	public View getView(final int pos, View v, ViewGroup p) {
		final Notice currentNotice = noticeList.get(pos);
		NotifyListItem item=new NotifyListItem(context);
		View currentView=item.getView();
		item.setTitle(currentNotice.getNoticeTitle());//设置标题
		if((!TextUtils.isEmpty(currentNotice.getDetailNotice()))&&(currentNotice.getDetailNotice().length()>35)){//公告内容大于35个字的话以...的形式展示
			item.setContent(currentNotice.getDetailNotice().substring(0,30)+"...");
		}else{
			item.setContent(currentNotice.getDetailNotice());
		}
//		item.setContent(currentNotice.getDetailNotice());
		try {
			item.setDate(PublicUtils.compareDate(currentNotice.getCreateTime()));//设置日期
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(currentNotice.getIsread()==Notice.ISREAD_N){//设置已读未读状态
			item.setIsRead(false);//未读
		}else{
			item.setIsRead(true);//已读
		}
		item.setCreater(currentNotice.getCreateUser());//设置下发人
		item.getIv_del().setOnClickListener(new OnClickListener() {//删除按钮添加单击事件
			
			@Override
			public void onClick(View v) {
				v.setEnabled(false);
				((NotifyListActivity)context).showDeleteDialog(pos,currentNotice);
				v.setEnabled(true);
			}
		});
		
		currentView.setOnClickListener(new OnClickListener() {//公告视图添加单击事件 跳转到详细页面并传值
			
			@Override
			public void onClick(View v) {
				v.setEnabled(false);
				currentPos=pos;
				Notice notice = noticeList.get(pos);
				Bundle bundle = new Bundle();
				if (notice.getId() != null) {//主键
					bundle.putInt("currentNoticeId", notice.getId());
				}else{
					bundle.putInt("currentNoticeId", -1);
				}
				bundle.putString("currentNoticeTitle", notice.getNoticeTitle());//标题
				bundle.putString("currentNoticeDetail", notice.getDetailNotice());//详细内容
				bundle.putString("currentNoticeDate", notice.getCreateTime());//创建时间
				bundle.putString("currentNoticeOrg", notice.getCreateOrg());//下发机构
				bundle.putString("currentNoticeCreater", notice.getCreateUser());//下发人
				bundle.putString("notifyType", notice.getNotifyType());//公告类型
				bundle.putInt("notifyId", notice.getNotifyId());//公告ID
				bundle.putInt("isRead", notice.getIsread());//已读未读状态
				bundle.putString("attachment", notice.getAttachment());//公告附件json
				Intent intent = new Intent(context,NotifyDetailActivity.class);
				intent.putExtra("currentNotice", bundle);
				context.startActivity(intent);
				v.setEnabled(true);
			}
		});
		return currentView;
	}

	/**
	 * 返回当前适配器的所有公告集合
	 * @return
	 */
	public List<Notice> getNoticeList() {
		return noticeList;
	}

	/**
	 * 设置适配器数据
	 * @param noticeList
	 */
	public void setNoticeList(List<Notice> noticeList) {
		this.noticeList = noticeList;
	}

	
	/**
	 * 
	 * @return 当前位置
	 */
	public int getPos(){
		return currentPos;
	}
}
