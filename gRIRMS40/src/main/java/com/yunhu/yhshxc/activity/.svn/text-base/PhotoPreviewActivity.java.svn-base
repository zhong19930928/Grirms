package com.yunhu.yhshxc.activity;

import gcg.org.debug.JLog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

/**
 * 图片预览模块
 * 用于将可预览的图片显示在GridView中，并提供编辑、删除功能
 * 
 * @version 2013.5.23
 * @author wangchao
 *
 */
public class PhotoPreviewActivity extends Activity implements OnItemClickListener, View.OnClickListener {
	private ExecutorService thread = Executors.newFixedThreadPool(3);
	/**
	 * 存取SubmitItem的数据库封装
	 */
	private SubmitItemDB submitItemDB;
	
	/**
	 * 存取临时SubmitItem的数据库封装
	 */
	private SubmitItemTempDB submitItemTempDB;
	
	/**
	 * SubmitItem集合
	 */
	private final List<ItemData> data = new ArrayList<ItemData>();
	
	/**
	 * 显示图片的GridView
	 */
	private GridView grid;
	
	/**
	 * GridView用的Adapter
	 */
	private BaseAdapterImpl adapter;
	
	private int submitId;
	
	/**
	 * 标题
	 */
	private TextView txtTitle;
	
	/**
	 * 模式切换按钮(可选取照片的编辑模式和预览模式)
	 */
	private Button btnMode;
	
	/**
	 * Action bar
	 */
	private View barAction;
	
	/**
	 * 删除按钮
	 */
	private Button btnDelete;
	
	/**
	 * 是否是超链接
	 */
	private boolean isLink;
	
	/**
	 * 是否是超链接预览
	 */
	private boolean isFromLinkPriview;
	
	private Dialog dialogDelete;
	
	private Button btnDialogOk;
	
	private Button btnDialogCancel;
	
	private int size;
	
	private boolean isEditMode;
	private int screenW,screenH;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_review);
		DisplayMetrics  metric = new  DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenW = metric.widthPixels;
		screenH = metric.heightPixels;
		
		isEditMode = false;
		
		int width = getResources().getDisplayMetrics().widthPixels - (int)(10 * getResources().getDisplayMetrics().density);
		size = width / 3;
		
		adapter = new BaseAdapterImpl();

		//初始化
		txtTitle = (TextView) findViewById(R.id.txt_title_bar_title);
		btnMode = (Button) findViewById(R.id.btn_title_bar_right);
		btnMode.setOnClickListener(this);
		barAction = findViewById(R.id.action_bar);
		btnDelete = (Button) findViewById(R.id.btn_delete);
		btnDelete.setOnClickListener(this);
		grid = (GridView) this.findViewById(R.id.grid);
		grid.setOnItemClickListener(this);
		grid.setAdapter(adapter);
		
		dialogDelete = new Dialog(this);
		dialogDelete.setContentView(View.inflate(this, R.layout.delete_dialog, null), new ViewGroup.LayoutParams(getResources().getDisplayMetrics().widthPixels - 40, LayoutParams.WRAP_CONTENT));
		dialogDelete.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
		btnDialogOk = (Button)dialogDelete.findViewById(R.id.btn_pop_ok);
		btnDialogOk.setOnClickListener(this);
		btnDialogCancel = (Button)dialogDelete.findViewById(R.id.btn_pop_cancel);
		btnDialogCancel.setOnClickListener(this);

		submitItemDB = new SubmitItemDB(this);
		submitItemTempDB = new SubmitItemTempDB(this);
		
		//从Intent获取相关参数
		submitId = this.getIntent().getIntExtra("submitId", 0);
		isLink = this.getIntent().getBooleanExtra("isLink", false);
		isFromLinkPriview = this.getIntent().getBooleanExtra("isFromLinkPriview", false);
		
		loadData.run();
	}
	
	private final Runnable loadData = new Runnable() {
		
		@Override
		public void run() {
			SubmitDB submitDB = new SubmitDB(getApplicationContext());
			FuncDB funcDB = new FuncDB(getApplicationContext());
			VisitFuncDB visitFuncDB = new VisitFuncDB(getApplicationContext());
			
			// 如果是超链接预览跳转的话数据已经保存，此时不要查询临时表(超链接的时候只拍照且没有保存的时候数据都存储在临时表中)
			List<SubmitItem> submitItems = null;
			if (isLink && !isFromLinkPriview) {
				submitItems = submitItemTempDB.findPhotoSubmitItemBySubmitId(submitId);
			}else {
				submitItems = submitItemDB.findPhotoSubmitItemBySubmitId(submitId);
			}
			
			LinkedList<ItemData> items = new LinkedList<ItemData>();
			for (SubmitItem item : submitItems) {
				Submit submit = submitDB.findSubmitById(item.getSubmitId());
				Func func = null;
				// 然后，再根据targetid,func_id查出唯一的控件
				if (submit.getTargetType() == Menu.TYPE_VISIT) {
					func = visitFuncDB.findFuncListByFuncIdAndTargetId(item.getParamName(), submit.getTargetid());
				}else {
					func = funcDB.findFuncListByFuncIdAndTargetId(item.getParamName(), submit.getTargetid());
				}
				ItemData itemData = new ItemData(item, func);
				itemData.bitmap = loadImage(item.getParamValue());
				items.add(itemData);
			}
			data.clear();
			data.addAll(items);
			
			adapter.notifyDataSetChanged();
			
			if (data.isEmpty()) {
				btnMode.setVisibility(View.GONE);
				barAction.setVisibility(View.GONE);
			}
			else {
				btnMode.setVisibility(View.VISIBLE);
				if (isEditMode) {
					barAction.setVisibility(View.VISIBLE);
				}
			}
//			handler.sendEmptyMessage(0);
		}
	};
	
//	private final Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			adapter.notifyDataSetChanged();
//		}
//	};
	/**
	 * 加载图片
	 * @param path
	 * @return
	 */
	private Bitmap loadImage(String path) {
		JLog.d("path = "+path);
//		Bitmap bitmap = BitmapFactory.decodeFile(Constants.SDCARD_PATH + path);
		
		Bitmap bitmap = FileHelper.compressImagePriview(Constants.SDCARD_PATH + path, screenW, screenH);
		if(bitmap !=null){
			if (bitmap.getWidth() > bitmap.getHeight()) {
				Matrix matrix = new Matrix();
				matrix.setRotate(90, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			}
			bitmap = Bitmap.createScaledBitmap(bitmap, size, size, false);
		}else{
			  bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.bbs_menu_xinxiliu);
		}
		return bitmap;
		
	}

	@Override
	public void onClick(View v) {
		if (v == btnMode) {
			if (isEditMode) {
				barAction.setVisibility(View.GONE);
				btnMode.setText(PublicUtils.getResourceString(PhotoPreviewActivity.this,R.string.select_));
			}
			else {
				barAction.setVisibility(View.VISIBLE);
				btnMode.setText(PublicUtils.getResourceString(PhotoPreviewActivity.this,R.string.Cancle));
			}
			isEditMode = !isEditMode;
			adapter.notifyDataSetChanged();
		}
		else if (v == btnDelete) {
			if (data.isEmpty())
				return;
			boolean hasSelected = false;
			for (ItemData i : data) {
				if (i.isSelected) {
					hasSelected = true;
					break;
				}
			}
			
			if (hasSelected) {
				dialogDelete.show();
			}
			else {
				ToastOrder.makeText(PhotoPreviewActivity.this, R.string.tip_choose_delete_photo, ToastOrder.LENGTH_SHORT).show();
			}
		}
		else if (v == btnDialogOk) {
			dialogDelete.dismiss();
			
			for (ItemData i : data) {
				if (i.isSelected) {
					if (isLink && !isFromLinkPriview) {
						submitItemTempDB.deleteSubmitItemById(i.si.getId());
					}
					else {
						submitItemDB.deleteSubmitItemById(i.si.getId());
					}
					new FileHelper().deleteFile(Constants.SDCARD_PATH + i.si.getParamValue());
				}
			}

			loadData.run();
		}
		else if (v == btnDialogCancel) {
			dialogDelete.dismiss();
		}
	}

	/**
	 * 点击某个图片的事件
	 * 如果是编辑状态（isEditPage为true），则在所点击的图片上显示选中图标，否则打开PhotoActivity预览图片
	 * 
	 * @see OnItemClickListener
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ItemData itemData = data.get(position);
		if (isEditMode) {//如果是编辑状态
			itemData.isSelected = !itemData.isSelected;
			adapter.notifyDataSetChanged();
		}
		else {//如果不是编辑状态
			Intent i = new Intent(this, PhotoActivity.class);
			i.putExtra("title", itemData.func.getName());
			i.putExtra("fileName", itemData.si.getParamValue());
			i.putExtra("submitItemId", itemData.si.getId());
			i.putExtra("isLink", isLink);
			i.putExtra("isFromLinkPriview", isFromLinkPriview);
			this.startActivityForResult(i, 1);
		}

	}

	/**
	 * 根据PhotoActivity关闭时返回的结果来判断是否删除图片
	 * 如果requestCode == 1且resultCode == Activity.RESULT_OK，则说明用于在预览窗口中删除了图片
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intentData) {
		super.onActivityResult(requestCode, resultCode, intentData);
		if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {
				loadData.run();
			}
		}
	}
	
	private class BaseAdapterImpl extends BaseAdapter {

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ItemView item = null;
			if (convertView == null) {
				item = new ItemView(grid.getContext());
			}
			else {
				item = (ItemView)convertView;
			}
			item.update(data.get(position));
			return item;
		}
	}
	
	private class ItemData {
		SubmitItem si;
		Func func;
		Bitmap bitmap;
		boolean isSelected;
		
		ItemData(SubmitItem si, Func func) {
			this.si = si;
			this.func = func;
		}
	}
	
	private class ItemView extends FrameLayout {
		ImageView imgPhoto;
		ImageView imgIcon;
		ItemData itemData;

		public ItemView(Context context) {
			super(context);
			
			imgPhoto = new ImageView(context);
			addView(imgPhoto, size, size);
			
			int size = (int)(40 * context.getResources().getDisplayMetrics().density);
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size, Gravity.RIGHT | Gravity.TOP);
			params.topMargin = params.rightMargin = (int)(10 * context.getResources().getDisplayMetrics().density);
			imgIcon = new ImageView(context);
			imgIcon.setImageResource(R.drawable.hook);
			imgIcon.setVisibility(View.GONE);
			addView(imgIcon, params);
		}
		
		void update(ItemData itemData) {
			this.itemData = itemData;
			if (barAction.getVisibility() != View.VISIBLE)
				imgIcon.setVisibility(View.GONE);
			else
				imgIcon.setVisibility(itemData.isSelected ? View.VISIBLE : View.GONE);
			imgPhoto.setImageBitmap(itemData.bitmap);
		}
	}
}