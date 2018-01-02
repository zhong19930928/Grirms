package com.yunhu.yhshxc.activity.addressBook;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class AddressBFragmentPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> mFragments;
	private List<String> mTitles;
	private List<String> tagList = new ArrayList<String>();
	FragmentManager fm;

	public AddressBFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments,
			List<String> titles) {
		super(fm);
		mFragments = fragments;
		mTitles = titles;
		this.fm = fm;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles.get(position);
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {  
        tagList.add(makeFragmentName(container.getId(), getItemId(position)));  
        return super.instantiateItem(container, position);  
    }
	public static String makeFragmentName(int viewId, long index) {
	    return "android:switcher:" + viewId + ":" + index;
	  }
	public void update(int item) {
	      Fragment fragment = fm.findFragmentByTag(tagList.get(item));
	      if (fragment != null) {
	        switch (item) {
	        case 0:
//	        	JLog.d("alin","刷新-------   "+item);
	          break;
	        case 1:
//	        	JLog.d("alin","刷新-------   "+item);
	        	ContractZWFragment f = (ContractZWFragment) mFragments.get(item);
	        	f.updata();
	          break;
	        case 2:
//	        	JLog.d("alin","刷新-------   "+item);
	        	ContractBMFragment zw = (ContractBMFragment) mFragments.get(item);
	        	zw.updata();
	          break;
	        default:
	          break;
	        }
	      }
	    }
	public interface FragmentListener {  
	      
        public void onFragmentClickListener(int item);  
    }
}
