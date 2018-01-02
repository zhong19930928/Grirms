package com.yunhu.yhshxc.activity.addressBook;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.AddressBFragmentPagerAdapter.FragmentListener;
import com.yunhu.yhshxc.utility.PublicUtils;

@SuppressLint("NewApi")
public class AddressBookActivity extends AppCompatActivity implements FragmentListener{

	private LinearLayout ll_parent;
	private ContractAllFragment allFragment;
	private ContractBMFragment bmFragment;
	private ViewPager viewPager;
	private int currentPager;
	AddressBFragmentPagerAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_book_main);
		ll_parent = (LinearLayout) findViewById(R.id.ll_parent);
		setupUI(ll_parent);
		 viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				currentPager = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				allFragment.setV();
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		List<Fragment> fragmentList = new ArrayList<Fragment>();
		// ContractAllFragment1 allFragment = new ContractAllFragment1();
		allFragment = new ContractAllFragment();
		ContractZWFragment zwFragment = new ContractZWFragment();
		 bmFragment = new ContractBMFragment();
		fragmentList.add(allFragment);
		fragmentList.add(zwFragment);
		fragmentList.add(bmFragment);
		List<String> tabList = new ArrayList<String>();
		tabList.add(PublicUtils.getResourceString(this,R.string.work_plan_name20));
		tabList.add(PublicUtils.getResourceString(this,R.string.work_plan_name21));
		tabList.add(PublicUtils.getResourceString(this,R.string.work_plan_name22));
		 adapter = new AddressBFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentList, tabList);
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(2);

		// TabLayout
		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
		tabLayout.setupWithViewPager(viewPager);// 将TabLayout和ViewPager关联起来。
		tabLayout.setTabsFromPagerAdapter(adapter);// 给Tabs设置适配器
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
		tabLayout.setTabMode(TabLayout.MODE_FIXED);

	}

	public void setupUI(View view) {

		if (!(view instanceof EditText)) {
			view.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {

					PublicUtils.hideKeyboard(AddressBookActivity.this); // Main.this是我的activity名

					return false;

				}

			});

		}

		// If a layout container, iterate over children and seed recursion.

		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(innerView);

			}

		}

	}
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN&&currentPager == 2) { 
	        		if(bmFragment.onSubView()){
		        		return true;
		        	}
	        	
	        } 
	        return super.onKeyDown(keyCode, event);
	    }

	@Override
	public void onFragmentClickListener(int item) {
		adapter.update(item);
	}

}
