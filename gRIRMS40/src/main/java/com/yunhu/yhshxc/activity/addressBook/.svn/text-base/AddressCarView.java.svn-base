package com.yunhu.yhshxc.activity.addressBook;

import java.util.ArrayList;
import java.util.List;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.bo.AdressBookUser;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class AddressCarView {
	private Context context;
	private CardView card;
	private ListView lv;
	private ContentAdapter adapter;
	private OnItemAdd itemAdd;
	private TextView btn_close;
//	private int index;
	private List<AdressBookUser> listUser = new ArrayList<AdressBookUser>();
	private AdressBookUserDB userDB;
	public AddressCarView(Context context, final OnItemAdd itemAdd,
			 AdressBookUser user) {
//		this.index = index;
		this.context = context;
		this.itemAdd = itemAdd;
		userDB = new AdressBookUserDB(context);
		card = (CardView) LayoutInflater.from(context).inflate(
				R.layout.address_drable, null);
		View item = LayoutInflater.from(context).inflate(
				R.layout.address_drable_item, null);
		ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		card.addView(item, params2);
		lv = (ListView) item.findViewById(R.id.lv_addre);
		btn_close = (TextView) item.findViewById(R.id.btn_close);
		btn_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				itemAdd.onSubView();
				
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AdressBookUser user = listUser.get(position);
				if (user != null) {
					if(user.isOrg()){
							itemAdd.onAddView( user);
						
					}else{
						itemAdd.onIntent(user);
					}
					
				}
			}
		});
		if (user != null) {
			List<AdressBookUser> listPer = userDB
					.findOrgBookUser(user.getoId());
			List<AdressBookUser> listOrg = userDB.findAllOrgBook(user.getOl(),
					user.getOc());
			listUser.addAll(listPer);
			listUser.addAll(listOrg);
		}
		adapter = new ContentAdapter(context, listUser);
		lv.setAdapter(adapter);
	}

	public CardView getCardView() {
		return card;
	}

	public interface OnItemAdd {
		public void onAddView( AdressBookUser user);
		public void onIntent(AdressBookUser user);
		public boolean onSubView();
	}

}
