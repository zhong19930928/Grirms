package com.yunhu.yhshxc.activity.addressBook;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.AddressCarView.OnItemAdd;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.widget.ParentListView;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class ContractBMFragment extends Fragment implements OnItemAdd {
	private FrameLayout mFrameLaoyut;
	// private GCGScrollView scrollview;
	private int topMargin = 0;
	private int leftMargin = 0;
	private ParentListView lv;
	private ContentAdapter adapter;
	private AdressBookUserDB userDB;
	private List<AdressBookUser> listUser = new ArrayList<AdressBookUser>();
	private boolean isFirst = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.address_bumen_content, container,
				false);
		userDB = new AdressBookUserDB(getActivity());
		initData();
		initView(view);
		return view;
	}

	private void initData() {
		AdressBookUser user = userDB.findFirstLevelAdressBook();
		listUser.clear();
		if (user != null) {
			List<AdressBookUser> listPer = userDB
					.findOrgBookUser(user.getoId());
			List<AdressBookUser> listOrg = userDB.findAllOrgBook(user.getOl(),
					null);
			listUser.addAll(listPer);
			listUser.addAll(listOrg);
		}

	}

	private void initView(View view) {
		// scrollview = (GCGScrollView) view.findViewById(R.id.scrollview);
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);

//		int width = wm.getDefaultDisplay().getWidth();
//		int height = wm.getDefaultDisplay().getHeight();

		mFrameLaoyut = (FrameLayout) view.findViewById(R.id.frameLayout);
		lv = (ParentListView) view.findViewById(R.id.lv_bumen_content);
		ViewGroup.LayoutParams params = lv.getLayoutParams();
//		params.height = height;
//		params.width = width;
//		lv.setLayoutParams(params);
		adapter = new ContentAdapter(getActivity(), listUser);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(mFrameLaoyut.getChildCount() == 1){
					AdressBookUser user = listUser.get(position);
					if (user != null) {
						if (user.isOrg()) {
							if(!isFirst){
								isFirst = true;
								addCardView(user);
							}
							
						} else {
							Intent intent = new Intent(getActivity(),
									AddressBookDetailActivity.class);
							intent.putExtra("userId", user.getuId());
							startActivity(intent);
						}
					}
				}
			}
		});
		
	}

//	public int index = 0;

	private CardView addCardView(AdressBookUser user) {
//		index++;
		AddressCarView view = new AddressCarView(getActivity(), this,
				user);
		CardView card = view.getCardView();
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				getWidth(getActivity()), getHeight(getActivity()));
		params.leftMargin = leftMargin - 20;
		params.topMargin = topMargin + lv.getScrollY() - 40;
		params.height = getHeight(getActivity()) - 80;
		leftMargin += 5;
		topMargin += 5;
		mFrameLaoyut.addView(card, params);
		show(card);
		mFrameLaoyut.requestLayout();
		mFrameLaoyut.invalidate();
		return card;
	}

	public static int getWidth(Activity mActivity) {
		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getHeight(Activity mActivity) {
		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	@Override
	public void onAddView(  AdressBookUser user) {
		if(!isFirst){
			isFirst = true;
			addCardView(user);
		}
		
	}
	@Override
	public void onIntent(AdressBookUser user) {
		Intent intent = new Intent(getActivity(),
				AddressBookDetailActivity.class);
		intent.putExtra("userId", user.getuId());
		startActivity(intent);
	}
	public void show(final View view) {
		view.setEnabled(false);
		ViewHelper.setRotationY(view, -90);
		ViewPropertyAnimator.animate(view).rotationY(0).setDuration(300)
				.setListener(new AnimatorListener() {
					
					@Override
					public void onAnimationStart(com.nineoldandroids.animation.Animator arg0) {
						
					}
					
					@Override
					public void onAnimationRepeat(com.nineoldandroids.animation.Animator arg0) {
						
					}
					
					@Override
					public void onAnimationEnd(com.nineoldandroids.animation.Animator arg0) {
						isFirst = false;
					}
					
					@Override
					public void onAnimationCancel(com.nineoldandroids.animation.Animator arg0) {
						
					}
				});
	}

	@Override
	public boolean onSubView() {
		if(!isFirst){
			isFirst = true;
			if (mFrameLaoyut.getChildCount() == 1) {
				return false;
			}
			final CardView card = (CardView) mFrameLaoyut.getChildAt(mFrameLaoyut
					.getChildCount() - 1);
			ViewPropertyAnimator.animate(card).rotationY(-90).setDuration(200)
					.setListener(new AnimatorListener() {

						@Override
						public void onAnimationStart(
								com.nineoldandroids.animation.Animator arg0) {

						}

						@Override
						public void onAnimationRepeat(
								com.nineoldandroids.animation.Animator arg0) {

						}

						@Override
						public void onAnimationEnd(
								com.nineoldandroids.animation.Animator arg0) {
							card.clearAnimation();
							subView();
							isFirst = false;
						}

						@Override
						public void onAnimationCancel(
								com.nineoldandroids.animation.Animator arg0) {

						}
					});
		}
		

		return true;
	}

	public void subView() {
		mFrameLaoyut.removeViewAt(mFrameLaoyut.getChildCount()-1);
		leftMargin -= 5;
		topMargin -= 5;
	}
	
	public void updata(){
		if(mFrameLaoyut.getChildCount() == 1){
			initData();
			adapter.refresh(listUser);
		}
	}

	
}
