package com.yunhu.yhshxc.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.TaskListActivity;

import com.yunhu.yhshxc.activity.todo.TodoListActivity;
import com.yunhu.yhshxc.bo.PushItem;
import com.yunhu.yhshxc.database.PushItemDB;
import com.yunhu.yhshxc.notify.NotifyListActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.visit.VisitWayActivity;
import com.yunhu.yhshxc.wechat.WechatActivity;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MessageFragment extends Fragment{
	
	
	 private Context context;
		private View rootView;
		private ListView messageListView;
		private View my_order;
		private PushAdapter pushAdapter;
		private ArrayList<PushItem> pushItmeList;
		private BroadcastReceiver myBroadcast;

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	        context = getActivity();

	        if (null != rootView) {
	            ViewGroup parent = (ViewGroup) rootView.getParent();
	            if (null != parent) {
	                parent.removeView(rootView);
	            }
	        } else {
	            rootView = inflater.inflate(R.layout.fragment_message, container, false);
	            messageListView = (ListView)rootView.findViewById(R.id.message_list);
	            my_order =   rootView.findViewById(R.id.my_order);//.setVisibility(View.GONE);
	            init();
//	            clock(rootView);
	            
	        }
	        myBroadcast = new MyBroadcast();
	    	context.registerReceiver(myBroadcast, new IntentFilter("new_wechat_message"));
	       
	        return rootView;
	    }
		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			
			context.unregisterReceiver(myBroadcast);
		}
		private void init() {
			// TODO Auto-generated method stub
			
//			List<Push> pushList = new PushDB(context).findAllPushIsFinish();
//			 pushList.addAll(new PushDB(context).findAllPush());
			 
//			 pushItmeList = new PushItemDB(context).findAllPushItem();
			String sqlStr =Constants.PUSH_DOUBLE+","+Constants.WECHAT_PUSH+","+Constants.PUSH_NOTIFY+","+Constants.PUSH_TASK+","+Constants.PUSH_TODO+","+Constants.PUSH_PLAN;
			 pushItmeList = new PushItemDB(context).findAllPushItemByType(sqlStr);
//			 boolean isHaveItem=false;
//			if (pushItmeList.size()>0) {
//				 for (int i = 0; i < pushItmeList.size(); i++) {
//						int type=pushItmeList.get(i).getType();
//						//如果查询出来的信息符合我们的所要类型
//						if (type==Constants.PUSH_DOUBLE||type==Constants.WECHAT_PUSH||type==Constants.PUSH_NOTIFY||type==Constants.PUSH_TASK||type==Constants.PUSH_TODO||type==Constants.PUSH_PLAN) {
//							isHaveItem=true;
//						}else{
//							pushItmeList.remove(i);//不属于符合我们要求的信息移除
//						    i--;
//						}
//					}
//					 
//			}
			 pushAdapter = new PushAdapter(pushItmeList);
			 messageListView.setAdapter(pushAdapter);
			 
			 if(pushItmeList.size()>0){
				 my_order.setVisibility(View.GONE);

			 }else{
				 my_order.setVisibility(View.VISIBLE);
			 }
		
			
			
			
		}


  @Override
public void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	init();
}
		
		
		class PushAdapter extends BaseAdapter{
			List<PushItem> pushList;
			LayoutInflater inflater;
			
			public void setData(List<PushItem> pushList){
				this.pushList = pushList;
			}
			
			
			public PushAdapter(List<PushItem> pushList){
				this.pushList = pushList;
				 inflater = LayoutInflater.from(context);
			}
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return pushList.size();
			}

			@Override
			public PushItem getItem(int position) {
				// TODO Auto-generated method stub
				return pushList.get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return pushList.get(position).hashCode();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				
				
				convertView = inflater.inflate(R.layout.message_itme, null);
                TextView push_itme_content = (TextView) convertView.findViewById(R.id.push_itme_content);
                PushItem pushItem = pushList.get(position);
                convertView.setOnClickListener(new MessageClickListner(pushItem));
                
                switch(pushItem.getType()){
            	case Constants.PUSH_NOTIFY:
            		
            		push_itme_content.setText(PublicUtils.getResourceString(context,R.string.new_notice));
            		
            		break;
            		
            	case Constants.PUSH_TASK:
            		
            		push_itme_content.setText(PublicUtils.getResourceString(context,R.string.new_notice1));
            		
            		break;
            		
            	case Constants.PUSH_TODO:
	
            		push_itme_content.setText(PublicUtils.getResourceString(context,R.string.new_notice2));
	
            		break;
	
            	case Constants.PUSH_DOUBLE:
	
            		push_itme_content.setText(PublicUtils.getResourceString(context,R.string.new_notice3));
	
            		break;
	
            	case Constants.PUSH_PLAN:
	
            		push_itme_content.setText(PublicUtils.getResourceString(context,R.string.new_notice4));
	
            		break;
            		
            		
            	case Constants.WECHAT_PUSH:
            		
            		push_itme_content.setText(PublicUtils.getResourceString(context,R.string.new_notice5));
	
            		break;
                }
            		
                
    
    
                return convertView;
			}
			
			
			
			
			class MessageClickListner implements View.OnClickListener{
				
				MessageClickListner(PushItem pushItme){
					 this.pushItem1 = pushItme;
					}
			
				
				PushItem pushItem1;
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent =null;
					switch(pushItem1.getType()){
	            	case Constants.PUSH_NOTIFY:
	            		intent = new Intent(context,NotifyListActivity.class);
//	            		push_itme_content.setText("公告："+pushItem.getContent());
	            		
	            		break;
	            		
	            	case Constants.PUSH_TASK:
	            		intent = new Intent(context,TaskListActivity.class);
//	            		push_itme_content.setText("任务："+pushItem.getContent());
	            		
	            		break;
	            		
	            	case Constants.PUSH_TODO:
	            		intent = new Intent(context, TodoListActivity.class); 
//	            		push_itme_content.setText("待办："+pushItem.getContent());
		
	            		break;
		
	            	case Constants.PUSH_DOUBLE:
//	            		int targetId=relayIntent.getIntExtra("targetId", Constants.DEFAULTINT);
//	            		Menu currentMenu=new MainMenuDB(context).findMenuListByMenuId(targetId);
//	            		if (currentMenu == null) {
//	            			return;
//	            		}
//	            		Intent intent = null;
//	            		Module module = null;
//	            		if (currentMenu.getType() == Menu.TYPE_NEW_TARGET) {
//	            			intent= new Intent(this,NewDoubleListActivity.class);
//	            			module=new ModuleDB(this).findModuleByTargetId(targetId,Constants.MODULE_TYPE_EXECUTE_NEW);
//	            		}else{
//	            			intent= new Intent(this,DoubleWayActivity.class);
//	            			module=new ModuleDB(this).findModuleByTargetId(targetId,Constants.MODULE_TYPE_EXECUTE);
//	            		}
//	            		Bundle bundle=new Bundle();
//	            		bundle.putSerializable("module", module);
//	            		intent.putExtra("bundle", bundle);
//	            		intent.putExtra("targetId", targetId);
//	            		intent.putExtra("doubleHead", module.getName());
//	            		bundle.putBoolean("isNoWait",currentMenu.getIsNoWait() == 1 ? true:false);
//	            		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//	            		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	            		SharedPreferencesUtil.getInstance(this).setKey(String.valueOf(currentMenu.getMenuId()), "0");
//	            		push_itme_content.setText("双向："+pushItem.getContent());
		
	            		break;
		
	            	case Constants.PUSH_PLAN:
	            		intent = new Intent(context,VisitWayActivity.class);
//	            		push_itme_content.setText("计划："+pushItem.getContent());
		
	            		break;
	            		
	            	case Constants.WECHAT_PUSH:            		
	            		intent = new Intent(context,WechatActivity.class);
	            		//如果是企信消息,点进去之后消息小红点消失,在此发送广播
	            		context.sendBroadcast(new Intent("cancle_type_wechat_redpoint"));
//	            		push_itme_content.setText("计划："+pushItem.getContent());
		
	            		break;
					}
					if (intent!=null) {
						
						startActivity(intent);
						
					}
					//点击完哪个消息就移除列表中的
					new PushItemDB(context).delete(pushItem1.getMsgId());
					pushItmeList.remove(pushItem1);
            		pushAdapter.setData(removePushItem(pushItmeList));
            		pushAdapter.notifyDataSetChanged();
					
				}
				
			}
		}
		
		
		
		
		
		class MyBroadcast extends BroadcastReceiver{

			@SuppressWarnings("deprecation")
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				
				PushItem pushItem = new PushItem();
				pushItem.setType(Constants.WECHAT_PUSH);
				if(removePushItem(pushItmeList).add(pushItem)){
//					pushAdapter.setData(pushItmeList);
//					pushAdapter.notifyDataSetChanged();
//					messageListView.setAdapter(pushAdapter);
					 my_order.setVisibility(View.GONE);
					 //消息初始化完成,让小红点显示
					 context.sendStickyBroadcast(new Intent("new_message"));
					
				}
				
				context.removeStickyBroadcast(intent);
				
//				image_p.setVisibility(View.VISIBLE);
//				if(intent!=null){
//					String type = intent.getStringExtra("type");
//					
//					
//					
//					
//				}
				init();
				
			}
			
		}
		
		ArrayList<PushItem>  removePushItem (ArrayList<PushItem> pushItmeList ){
			
			for (int i = 0; i < pushItmeList.size(); i++) {
				if(pushItmeList.get(i).getType()==Constants.WECHAT_PUSH){
					pushItmeList.remove(i);
					i--;
				}
			}
//			for (PushItem pushItem : pushItmeList) {
//				if(pushItem.getType()==Constants.WECHAT_PUSH){
//					pushItmeList.remove(pushItem);
//				}
//			}
			return pushItmeList;
			
			
			
		} 
		
}
