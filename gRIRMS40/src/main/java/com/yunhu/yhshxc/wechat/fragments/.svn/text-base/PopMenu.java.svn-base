package com.yunhu.yhshxc.wechat.fragments;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

import java.util.ArrayList;


/**
 * 
 * @author ly
 * 
 */
public class PopMenu {

	private ArrayList<String> itemList;
	private Context context;
	private PopupWindow popupWindow;
	private ListView listView;
	@SuppressWarnings("deprecation")
	public PopMenu(Context context) {
		this.context = context;
		itemList = new ArrayList<String>();
		View view = LayoutInflater.from(context)
				.inflate(R.layout.wechat_popmenu, null);
		// ���� listview
		listView = (ListView) view.findViewById(R.id.listView);
		listView.setAdapter(new PopAdapter());
		listView.setFocusable(true);

		popupWindow = new PopupWindow(view, context.getResources()
				.getDimensionPixelSize(R.dimen.popmenu_width), LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		// �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	// ���ò˵�����������
	public void setOnItemClickListener(OnItemClickListener listener) {
		listView.setOnItemClickListener(listener);
	}

	// ������Ӳ˵���
	public void addItems(String[] items) {
		for (String s : items)
			itemList.add(s);
	}

	// ������Ӳ˵���
	public void addItem(String item) {
		itemList.add(item);
	}

	// ����ʽ ���� pop�˵� parent ���½�
	public void showAsDropDown(View parent) {
		popupWindow.showAsDropDown(parent,
				10,
				// ��֤�ߴ��Ǹ�����Ļ�����ܶ�����
				context.getResources().getDimensionPixelSize(
						R.dimen.popmenu_yoff));

		// ʹ��ۼ�
		popupWindow.setFocusable(true);
		// ����������������ʧ
		popupWindow.setOutsideTouchable(true);
		// ˢ��״̬
		popupWindow.update();
	}

	// ���ز˵�
	public void dismiss() {
		popupWindow.dismiss();
	}

	// ������
	private final class PopAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.wechat_pomenu_item, null);
				holder = new ViewHolder();
				holder.groupItem = (TextView) convertView
						.findViewById(R.id.textView);
				holder.wechat_iv_line = (ImageView) convertView.findViewById(R.id.wechat_iv_line);
				holder.wechat_popmunu_iv = (ImageView) convertView.findViewById(R.id.wechat_popmunu_iv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.groupItem.setText(itemList.get(position));
			if(position == itemList.size()-1){
				holder.wechat_iv_line.setVisibility(View.INVISIBLE);
			}else{
				holder.wechat_iv_line.setVisibility(View.VISIBLE);
			}
			if(position == 0){
				holder.wechat_popmunu_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.wechat_create_groups));
			}else if(position == 1){
				holder.wechat_popmunu_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.wechat_create_topics));
			}else if(position == 2){
				holder.wechat_popmunu_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.wechat_create_siliao));
			}
			return convertView;
		}
		
		private final class ViewHolder {
			TextView groupItem;
			ImageView wechat_iv_line;
			ImageView wechat_popmunu_iv;
			
		}
	}
}
