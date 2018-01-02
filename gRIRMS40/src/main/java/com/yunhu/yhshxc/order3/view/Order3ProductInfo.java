package com.yunhu.yhshxc.order3.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.list.activity.ShowImageActivity;

public class Order3ProductInfo {
	private View view;
	private Context context;
	private TextView tv1;
	private TextView tv2;
	public Order3ProductInfo(Context context){
		this.context = context;
		view = View.inflate(context, R.layout.order3_chanpinxinxi,null);
		tv1 = (TextView) view.findViewById(R.id.tv_product_detail_info);
		tv2 = (TextView) view.findViewById(R.id.tv_product_detail_info1);
	}
	public View getView(){
		return view;
	}
	public void ininData(final String name,final String info){
		if(!TextUtils.isEmpty(info)){
			if (info.startsWith("http") && (info.endsWith(".jpg") || info.endsWith(".png") || info.endsWith(".JPG") || info.endsWith(".PNG") || info.endsWith(".bmp") || info.endsWith(".BMP") ||  info.endsWith(".gif") || info.endsWith(".GIF") || info.endsWith(".jpeg")||  info.endsWith(".JPEG"))) {
				tv2.setText("查看");
				tv2.setTextColor(context.getResources().getColor(R.color.blue));
				tv2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, ShowImageActivity.class);
						intent.putExtra("imageUrl", info);
						intent.putExtra("imageName", name);
						context.startActivity(intent);
					}
				});
			}else{
				tv2.setText(info);
			}
			
		}
		tv1.setText(name);
		
	}
}
