package com.yunhu.yhshxc.nearbyVisit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

public class NearbyVisitActivity extends FragmentActivity{
    private DrawerLayout mDrawer_layout;//DrawerLayout容器
    private RelativeLayout mMenu_layout_left;//左边抽屉
    private ImageView iv_nearby_visit_search,iv_nearby_visit_refrash;
    private String title;
    public String menuId;
    private TextView titleTextView;
	private LinearLayout btn_clean;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.near_visit);
        titleTextView  = (TextView)findViewById(R.id.tv_nearby_visit_title);
        title = getIntent().getStringExtra("title");
        menuId = getIntent().getStringExtra("menuId");
        titleTextView.setText(title);
        initWidget();
        setLeftMenu();
        setDefaultFragment();
    }
    
    
    private void initWidget(){
    	 mDrawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
    	 mDrawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
         mMenu_layout_left = (RelativeLayout) findViewById(R.id.menu_layout_left);
         iv_nearby_visit_search = (ImageView)findViewById(R.id.iv_nearby_visit_search);
//         iv_nearby_visit_refrash = (ImageView)findViewById(R.id.iv_nearby_visit_refrash);
         iv_nearby_visit_search.setOnClickListener(listener);
		btn_clean = (LinearLayout) findViewById(R.id.btn_clean);
		btn_clean.setOnClickListener(listener);
//         iv_nearby_visit_refrash.setOnClickListener(listener);
    }
    
    public NearbyVisitLeftMenu nearbyVisitLeftMenu;
    private void setLeftMenu(){
    	nearbyVisitLeftMenu = new NearbyVisitLeftMenu(this);
    	View view = nearbyVisitLeftMenu.getView();
    	mMenu_layout_left.addView(view);
//    	showLeftMenu();
    }
    /**
     * 设置默认 Fragment
     */
    
    private NearbyVisitSearchResultFragment nearbyVisitSearchResultFragment;
    private void setDefaultFragment(){
    	nearbyVisitSearchResultFragment = new NearbyVisitSearchResultFragment();
    	FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_layout, nearbyVisitSearchResultFragment);
        ft.commit();
    }
    
    private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
//			case R.id.iv_nearby_visit_refrash:
//				refrash();
//				break;
			case R.id.iv_nearby_visit_search:
//				showLeftMenu();
				finish();
				break;
				case R.id.btn_clean:
					clearSearchData();
					break;

			default:
				break;
			}
		}

	};

	/**
	 * 清除查询条件
	 */
	private void clearSearchData() {
		if(nearbyVisitSearchResultFragment!=null){
			nearbyVisitSearchResultFragment.clearSearchData();
		}
	}
	
	/**
	 * 显示左侧menu
	 */
	private void showLeftMenu(){
		if (mDrawer_layout.isDrawerOpen(mMenu_layout_left)) {
			mDrawer_layout.closeDrawer(mMenu_layout_left);
		}else{
			mDrawer_layout.openDrawer(mMenu_layout_left);
		}
	}
	/**
	 * 刷新
	 */
	private void refrash(){
		if (mDrawer_layout.isDrawerOpen(mMenu_layout_left)) {
			mDrawer_layout.closeDrawer(mMenu_layout_left);//关闭mMenu_layout
		}
//		nearbyVisitSearchResultFragment.refresh("");
		nearbyVisitLeftMenu.search();
	}
	
	/**
	 * 查询
	 */
	public void search(String param){
//		mDrawer_layout.closeDrawer(mMenu_layout_left);//关闭mMenu_layout
		nearbyVisitSearchResultFragment.refresh(param);
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	outState.putString("title", title);
    	outState.putString("menuId", menuId);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	title = savedInstanceState.getString("title");
    	menuId = savedInstanceState.getString("menuId");
    }
    
}
