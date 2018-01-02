package com.yunhu.yhshxc.wechat.exchange;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.wechat.bo.GroupUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class GroupUserView implements OnClickListener{

	private View view;
	private Context context;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private ImageView iv_group_user_photo;
	private TextView tv_group_user_name;
	
	public GroupUserView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.activity_group_user_view, null);

		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.people_50)
		.showImageForEmptyUri(R.drawable.people_50)
		.showImageOnFail(R.drawable.people_50)
		.cacheOnDisk(true)
		.cacheInMemory(true).displayer(new RoundedBitmapDisplayer(10))
		.build();
	}

	public void setGroupUserView(GroupUser groupUser){
		
		iv_group_user_photo = (ImageView) view.findViewById(R.id.iv_group_user_photo);
		tv_group_user_name = (TextView) view.findViewById(R.id.tv_group_user_name);
		
		imageLoader.displayImage(groupUser.getPhoto(), iv_group_user_photo, options, null);
		tv_group_user_name.setText(groupUser.getUserName());
	}
	
	public View getView() {
		return view;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}


}
