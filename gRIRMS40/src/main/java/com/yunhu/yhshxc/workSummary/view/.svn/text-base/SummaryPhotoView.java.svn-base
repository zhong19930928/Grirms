package com.yunhu.yhshxc.workSummary.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.dragimage.PhotoPriviewActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.wechat.content.Photo;
import com.yunhu.yhshxc.workSummary.SummaryControlListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class SummaryPhotoView implements OnClickListener {

	private Context context;
	private View view;
	private ImageView iv_photo_items;
	private TextView tv_photo_items_name;
	private ImageButton ib_delete_photo_items;
	private Boolean isHave = false;
	private SummaryControlListener contentControlListener;
	private String filePath;

	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	public SummaryPhotoView(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.wechat_photo_items, null);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_empty)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_empty)
		.cacheOnDisk(true)
		.cacheInMemory(true).displayer(new RoundedBitmapDisplayer(10))
		.build();
		iv_photo_items = (ImageView) view.findViewById(R.id.iv_photo_items);
		tv_photo_items_name = (TextView) view.findViewById(R.id.tv_photo_items_name);
		ib_delete_photo_items = (ImageButton) view.findViewById(R.id.ib_delete_photo_items);

		ib_delete_photo_items.setOnClickListener(this);

	}

	
	
	public String getFilePath() {
		return filePath;
	}



	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}



	public void setPhotoView(final Photo photo) {

		String name = SharedPrefrencesForWechatUtil.getInstance(context).getGetFileName(photo.getName());
		tv_photo_items_name.setText(TextUtils.isEmpty(name)?photo.getName():name);
		imageLoader.displayImage("file://"+Constants.SUMMARY_PATH_LOAD+photo.getPhotoPath(), iv_photo_items, options, null);


//		iv_photo_items.setOnLongClickListener(new OnLongClickListener() {
//
//			@Override
//			public boolean onLongClick(View v) {
//				if (isHave) {
//					ib_delete_photo_items.setVisibility(View.GONE);
//					isHave = false;
//				} else {
//					ib_delete_photo_items.setVisibility(View.VISIBLE);
//					isHave = true;
//				}
//
//				return false;
//			}
//		});
		
		iv_photo_items.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, PhotoPriviewActivity.class);	
				intent.putExtra("isLocal", true);
				intent.putExtra("url", Constants.SUMMARY_PATH_LOAD + photo.getPhotoPath());
				context.startActivity(intent);
			}
		});
	}

	public View getView() {
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_delete_photo_items:
			showDialog();
//			contentControlListener.contentControl(this);
			break;

		default:
			break;
		}
	}
	
	private void showDialog(){
		new AlertDialog.Builder(context)
		.setMessage(R.string.is_confirm_delete)
		.setPositiveButton(R.string.Confirm,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						contentControlListener.contentControl(SummaryPhotoView.this);
						dialog.dismiss();
					}
				})
		.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
					}
				}).setCancelable(false).show();
	}

	public void setContentControlListener(SummaryControlListener contentControlListener) {
		this.contentControlListener = contentControlListener;
	}
	

}
