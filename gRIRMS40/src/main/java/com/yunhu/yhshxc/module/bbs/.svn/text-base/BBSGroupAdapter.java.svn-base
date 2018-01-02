package com.yunhu.yhshxc.module.bbs;

import gcg.org.debug.JLog;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.BbsUserInfo;
import com.yunhu.yhshxc.utility.PublicUtils;
/**
 * 
 * @author 石宗银 12.6.26
 *	圈子Item - adapter
 */
public class BBSGroupAdapter extends BaseAdapter{
	private Context context;
	private List<BbsUserInfo> list;
	public BBSGroupAdapter (Context context, List<BbsUserInfo> list) {
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		if(list != null){
			return list.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GroupItemViews itemViews = null;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.bbs_group_item, null);
			itemViews = new GroupItemViews();
			itemViews.iv =  (ImageView) convertView.findViewById(R.id.iv_usr_head);
			itemViews.iv_leve =  (ImageView) convertView.findViewById(R.id.iv_usr_leve);
			itemViews.tv = (TextView) convertView.findViewById(R.id.tv_usr_content);
			
			convertView.setTag(itemViews);
		}
		itemViews = (GroupItemViews) convertView.getTag();
//		itemViews.iv.setImageDrawable(list.get(position).);
		itemViews.iv.setImageResource(R.drawable.bbs_user);
		if(!TextUtils.isEmpty(list.get(position).getScore())){
			JLog.d("BBSGroupAdapter", "用户积分 ==>"+list.get(position).getScore());
			if(PublicUtils.getBBSLeve(list.get(position).getScore()) == -1){
				itemViews.iv_leve.setVisibility(View.GONE);
			}else{
				itemViews.iv_leve.setVisibility(View.VISIBLE);
				itemViews.iv_leve.setImageResource(PublicUtils.getBBSLeveIcon(list.get(position).getScore()));
			}
		}else{
			itemViews.iv_leve.setVisibility(View.GONE); 
		}
		itemViews.tv.setText(list.get(position).getName());
		return convertView;
	}
	
	
	public List<BbsUserInfo> getList() {
		return list;
	}
	public void setList(List<BbsUserInfo> list) {
		this.list = list;
	}


	private class GroupItemViews {
		ImageView iv;
		ImageView iv_leve;
		TextView tv;
		
	}
	
}
