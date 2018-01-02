package com.yunhu.yhshxc.wechat.exchange;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.wechat.bo.GroupUser;
import com.yunhu.yhshxc.wechat.fragments.UserInfomationActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


public class ExchangePhotoView implements OnClickListener{

	
	private Context context;
	private View view;
	private ImageView iv_wechat_exchange_gridview_item;
	private TextView tv_wechat_exchange_gridview_item;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private GroupUser groupUser;
	private String groupUserId = "";

	public ExchangePhotoView(Context context) {
		this.context = context;
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.people_65)
		.showImageForEmptyUri(R.drawable.people_65)
		.showImageOnFail(R.drawable.people_65)
		.cacheOnDisk(true)
		.cacheInMemory(true).displayer(new RoundedBitmapDisplayer(10))
		.build();
		view = View.inflate(context, R.layout.activity_wechat_exchange_gridview_tiem, null);
		iv_wechat_exchange_gridview_item = (ImageView) view.findViewById(R.id.iv_wechat_exchange_gridview_item);
		tv_wechat_exchange_gridview_item = (TextView) view.findViewById(R.id.tv_wechat_exchange_gridview_item);
	
		
	}
	

	public void setExchangePhotoView(GroupUser groupUser) {
		this.groupUser = groupUser;
		String photo = groupUser.getPhoto();
		String name = groupUser.getUserName();
		groupUserId = String.valueOf(groupUser.getUserId());
		if(!TextUtils.isEmpty(photo)){
			imageLoader.displayImage(photo, iv_wechat_exchange_gridview_item, options, null);
		}
		if(!TextUtils.isEmpty(name)){
			tv_wechat_exchange_gridview_item.setText(name);
		}
		
		iv_wechat_exchange_gridview_item.setOnClickListener(this);
		
		
	}
	
	public View getView() {
		return view;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_wechat_exchange_gridview_item:
			Intent intent = new Intent();
			intent.putExtra("userId", groupUserId);
			intent.setClass(context, UserInfomationActivity.class);
			context.startActivity(intent);
			break;

		default:
			break;
		}
	}
	



}
