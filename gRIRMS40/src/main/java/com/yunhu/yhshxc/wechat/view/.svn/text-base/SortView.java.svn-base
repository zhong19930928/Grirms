package com.yunhu.yhshxc.wechat.view;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.wechat.view.sortListView.SortModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SortView {
	private LinearLayout view;
	private Context context;
	private TextView tv_number;
	private TextView tv_topic_item;
	private TextView tv_time_item;
	private TextView tv_content_item;
	private TextView catalog;
	private ImageView iv_header;
	private RelativeLayout re_parent;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	public SortView(Context context) {
		this.context = context;
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.people_65)
		.showImageForEmptyUri(R.drawable.people_65)
		.showImageOnFail(R.drawable.people_65)
		.cacheInMemory()
		.cacheOnDisc().displayer(new RoundedBitmapDisplayer(10))
		.build();
		view = (LinearLayout) View.inflate(context, R.layout.wechat_chat_topic,
				null);
		tv_number = (TextView) view.findViewById(R.id.tv_number);
		tv_topic_item = (TextView) view.findViewById(R.id.tv_topic_item);
		tv_time_item = (TextView) view.findViewById(R.id.tv_time_item);
		tv_content_item = (TextView) view.findViewById(R.id.tv_content_item);
		catalog = (TextView) view.findViewById(R.id.catalog);
		iv_header = (ImageView) view.findViewById(R.id.iv_header);
		re_parent = (RelativeLayout) view.findViewById(R.id.re_parent);
	}

	public View getView() {
		return view;
	}

	public void initContract(SortModel user){
		if(user!=null){
			tv_topic_item.setText(user.getName());// 话题名称
			String time = "";
			String content = "";
			tv_number.setVisibility(View.GONE);
			OrgUser u = user.getUser();
			if(u!=null){
				String headUrl = SharedPrefrencesForWechatUtil.getInstance(context).getUserHeadImg(String.valueOf(u.getUserId()));
				if(!TextUtils.isEmpty(headUrl)){
					imageLoader.displayImage(headUrl, iv_header, options, null);
				}else{
					iv_header.setImageResource(R.drawable.people_65);
				}
			}
				
			tv_content_item.setText(content);// 最后一条发言人及内容
			tv_time_item.setText(time);
		}
	}

	public ImageView getImageView() {
		return iv_header;
	}

	public TextView getTextView() {
		return catalog;
	}
}
