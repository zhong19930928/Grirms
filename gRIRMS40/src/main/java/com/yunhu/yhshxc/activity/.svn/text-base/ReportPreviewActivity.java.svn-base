package com.yunhu.yhshxc.activity;



import com.yunhu.yhshxc.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import uk.co.senab.photoview.PhotoView;
/**
 * 展示一些Demo图标的界面
 * @author qingli
 *
 */
public class ReportPreviewActivity extends Activity  implements OnPageChangeListener, OnClickListener{
	private ViewPager mViewPager;
	//导航点的容器,用来根据页数进行动态的添加
	private LinearLayout ll_container_point;
	//viewpager页面引用的图片id数组
	private int[] picResId = { R.drawable.report_pic1, R.drawable.report_pic2, R.drawable.report_pic3 };
	//缓存当前选中的导航点位置
    private int currentPosition=0;
    private ViewPagerAdapter adapter;
    private ImageView report_demo_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_preview);
		initView();

	}

	private void initView() {
		report_demo_back = (ImageView) findViewById(R.id.report_demo_back);
		report_demo_back.setOnClickListener(this);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		ll_container_point = (LinearLayout) findViewById(R.id.ll_container_point);
		adapter = new ViewPagerAdapter();
		initTabPoint(ll_container_point);
		mViewPager.setAdapter(adapter);
		mViewPager.addOnPageChangeListener(this);
	}


	/**
	 * 根据viewpager页数动态添加导航点
	 * @param LinearLayout
	 */
	private void initTabPoint(LinearLayout ll_container_point2) {
		for (int i = 0; i < picResId.length; i++) {
			ImageView imageView = new ImageView(this);
			 LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20,20);             		  	
			if (i==0) {
				imageView.setImageResource(R.drawable.pager_point_select);
				 imageView.setLayoutParams(params);	
			}else{
				imageView.setImageResource(R.drawable.pager_point_normal);
				  params.setMargins(10, 0, 0, 0);		
				 imageView.setLayoutParams(params);	
			}
			ll_container_point2.addView(imageView);
			
		}
	
		
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		//当viewpager滑动到某一页,对应的导航点变色,原来的恢复默认
		ImageView nowImage=(ImageView) ll_container_point.getChildAt(position);
		nowImage.setImageResource(R.drawable.pager_point_select);
		ImageView lastImage=(ImageView) ll_container_point.getChildAt(currentPosition);
		lastImage.setImageResource(R.drawable.pager_point_normal);
		currentPosition=position;
	}
    
	class ViewPagerAdapter extends PagerAdapter {
		

		@Override
		public int getCount() {
			return picResId.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			photoView.setImageResource(picResId[position]);
			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.report_demo_back:
			finish();
			break;

		default:
			break;
		}
		
	}

	
}

