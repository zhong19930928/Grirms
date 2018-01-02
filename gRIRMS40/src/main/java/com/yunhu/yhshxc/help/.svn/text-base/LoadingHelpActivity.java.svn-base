package com.yunhu.yhshxc.help;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.yunhu.yhshxc.R;
//import com.gcg.grirms.activity.HomeMenuFragment;
import com.yunhu.yhshxc.activity.HomePageActivity;

/**
 * 引导屏
 * 用于滚动显示几幅使用说明的图
 * 
 * @version 2013.5.23
 * @author wangchao
 *
 */
public class LoadingHelpActivity extends Activity {
	
	/**
	 * 用于滚动屏幕的组件
	 */
	private ViewPager vp;
	
	/**
	 * 滚动屏幕组件所需的Adapter
	 */
	private MyPagerAdapter adapter;
	
	/**
	 * 导航栏（标识当前是第几页的指示器的容器）
	 */
	private ViewGroup pageControl;
	
	/**
	 * 使用说明图片的资源ID数组
	 */
	private int[] imageS = new int[6];
	
	/**
	 * 使用说明图片的View集合
	 */
	private List<View> mListViews = new ArrayList<View>(imageS.length);
	
	/**
	 * 最后一屏的“开始使用”按钮
	 */
	private Button btn_into;
	
//	private CheckBox addCheckBox;
//	private TextView tv_name;
//	private TextView tv_tel;
//	private String name;
//	private String tel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置为无标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置为全屏

		this.setContentView(R.layout.help);
		pageControl = (ViewGroup) this.findViewById(R.id.pageControl);
		initHelps();//初始化使用说明的图片和View
		vp = (ViewPager) findViewById(R.id.vp);
		adapter = new MyPagerAdapter();
		vp.setAdapter(adapter);
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * 当页面即将被加载时调用
			 * 如果是最后一屏就隐藏导航栏
			 */
			@Override
			public void onPageSelected(int position) {
				if (position == imageS.length - 1) {
					pageControl.setVisibility(View.INVISIBLE);//如果是最后一屏就隐藏导航栏
				}
				else {
					generatePageControl(position);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

			@Override
			public void onPageScrollStateChanged(int state) {}
		});
	}

	/**
	 * 初始化使用说明的图片和View
	 */
	public void initHelps() {
//		name = SharedPreferencesUtil.getInstance(this.getApplicationContext()).getHelpName();
//		tel = SharedPreferencesUtil.getInstance(this.getApplicationContext()).getHelpTel();
//		if (TextUtils.isEmpty(tel)) {
//			tel = "4008-1888-09";
//		}
//		if (TextUtils.isEmpty(name)) {
//			name = "销售管家客服";
//		}
//		JLog.d(TAG, "name==>" + name);
//		JLog.d(TAG, "tel==>" + tel);
		initPageContent();//将图片ID存入imageS数组
		int len = imageS.length;
		View view = null;
		//将所有使用帮助的图片包装进对应的View中，并存入List集合中
		//最后一屏因为有“开始使用”按钮，所以需要与其他屏分开处理
		for (int i = 0; i < len; i++) {
			if (i == (imageS.length - 1)) {//最后一屏单独处理
				view = lastLayout();
			}
			else {
				view = getLayoutInflater().inflate(R.layout.page, null);
			}
			view.setBackgroundResource(imageS[i]);
			mListViews.add(view);
		}
	}

	/**
	 * 获取最后一屏的View（包含开始使用按钮）
	 * @return 返回最后一屏的View
	 */
	private View lastLayout() {
		// 最后一页的布局
		View view = getLayoutInflater().inflate(R.layout.page_last, null);
		// addCheckBox=(CheckBox) view.findViewById(R.id.cb_add_contact);
		// String projectVersion=this.getResources().getString(R.string.PROJECT_VERSIONS);
		// if(projectVersion.equalsIgnoreCase("4.5")){
		// addCheckBox.setChecked(false);
		// addCheckBox.setVisibility(View.GONE);
		// }
		// tv_name = (TextView)view.findViewById(R.id.tv_name);
		// tv_name.setText(name);
		// tv_tel = (TextView)view.findViewById(R.id.tv_tel);
		// tv_tel.setText(tel);
		
		//设置“开始使用”按钮的事件，点击开始按钮后跳转至HomeMenuActivity
		btn_into = (Button) view.findViewById(R.id.btn_into);
		btn_into.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				initHome();
				// if(addCheckBox.isChecked()){
				// // 通过号码查询是否已经存在
				// String displayName = PublicUtils.getNameByNumber(LoadingHelpActivity.this, tel);
				// JLog.d(TAG, "displayName == >"+ displayName);
				// if(TextUtils.isEmpty(displayName)){// 如果不存在
				// // 插入联系方式
				// PublicUtils.insertContact(LoadingHelpActivity.this, name, tel, null);
				// }
				// // insertContact("销售管家客服","4008-1888-09",null);
				// }
			}
		});

		return view;
	}

	/**
	 * 将使用说明所有图片的ID存入数组，并将显示页设置为第一页
	 */
	public void initPageContent() {
		//将图片的资源ID添加进数组中
		imageS[0] = R.drawable.help1;
		imageS[1] = R.drawable.help2;
		imageS[2] = R.drawable.help3;
		imageS[3] = R.drawable.help4;
		imageS[4] = R.drawable.help5;
		imageS[5] = R.drawable.help6;
		generatePageControl(0);//将显示页设置为第一页
	}

	/**
	 * 设置导航栏指示器的位置
	 * 
	 * @param currentIndex 当前页数
	 */
	private void generatePageControl(int currentIndex) {
		if (!pageControl.isShown()) {
			pageControl.setVisibility(View.VISIBLE);
		}
		pageControl.removeAllViews();//每次换页先清空所有的执行器图标

		//每次换页都重新创建对应图片的指示器，如果是当前页时，显示为带焦点的指示器
		for (int i = 0; i < imageS.length - 1; i++) {
			ImageView imageView = new ImageView(this);
			if (currentIndex == i) {
				imageView.setImageResource(R.drawable.page_indicator_focused);
			}
			else {
				imageView.setImageResource(R.drawable.page_indicator);
			}
			this.pageControl.addView(imageView);
		}
	}

	/**
	 * 关闭当前Activity，并跳转至HomeMenuActivity
	 */
	private void initHome() {
		Intent intent = new Intent(LoadingHelpActivity.this, HomePageActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 用于ViewPager的Adapter
	 * ViewPager会在初始化时通过调用instantiateItem方法依次创建每一页View
	 */
	private class MyPagerAdapter extends PagerAdapter {

		/**
		 * 销毁时需要从ViewPager中移除每页View
		 */
		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((View) view);
		}

		@Override
		public void finishUpdate(View arg0) {}

		/**
		 * 获取ViewPager页数
		 */
		@Override
		public int getCount() {
			return imageS.length;
		}

		/**
		 * 添加一页View到ViewPager中
		 */
		@Override
		public Object instantiateItem(View collection, int position) {
			((ViewPager) collection).addView(mListViews.get(position), 0);
			return mListViews.get(position);

		}

		/**
		 * 简单对比一下view与object是否相等
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {}

	}

//	public void insertContact(String name, String phone, String email) {
//
//		ContentValues values = new ContentValues();
//		Uri rawContactUri = this.getContentResolver().insert(RawContacts.CONTENT_URI, values);
//		long rawContactId = ContentUris.parseId(rawContactUri);
//
//		if (!TextUtils.isEmpty(name)) {
//			// 往data表入姓名数据
//			values.clear();
//			values.put(Data.RAW_CONTACT_ID, rawContactId);
//			values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
//			values.put(StructuredName.DISPLAY_NAME, name);
//			this.getContentResolver().insert(Data.CONTENT_URI, values);
//			JLog.d(TAG, "通讯录插入姓名==》" + name);
//		}
//
//		if (!TextUtils.isEmpty(phone)) {
//			// 往data表入电话数据
//			values.clear();
//			values.put(Data.RAW_CONTACT_ID, rawContactId);
//			values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
//			values.put(Phone.NUMBER, phone);
//			values.put(Phone.TYPE, Phone.TYPE_CUSTOM);
//			this.getContentResolver().insert(Data.CONTENT_URI, values);
//			JLog.d(TAG, "通讯录插入电话号码==》" + phone);
//		}
//
//		// 往data表入Email数据
//		if (!TextUtils.isEmpty(email)) {
//			values.clear();
//			values.put(Data.RAW_CONTACT_ID, rawContactId);
//			values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
//			values.put(Email.DATA, email);
//			values.put(Email.TYPE, Email.TYPE_CUSTOM);
//			this.getContentResolver().insert(Data.CONTENT_URI, values);
//			JLog.d(TAG, "通讯录插入信箱==》" + email);
//		}
//	}
}