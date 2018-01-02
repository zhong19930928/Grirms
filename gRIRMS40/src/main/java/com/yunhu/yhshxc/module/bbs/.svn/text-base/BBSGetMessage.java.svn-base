package com.yunhu.yhshxc.module.bbs;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.BbsInformationItem;
import com.yunhu.yhshxc.parser.BBSParse;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.widget.ToastOrder;

import java.util.HashMap;
import java.util.List;

import gcg.org.debug.JLog;

public class BBSGetMessage extends AsyncTask<String, Integer, String>{

	private String TAG="BBSGetMessage";
	private Context context;
	private BBSMessageListActivity messageList;
	private View footView;
	private LinearLayout cliclLayout,loadLayout;
	private TextView loadTv;
	private HttpHelper httpHelper;
	private HashMap<String, String> paramMap;
	private boolean isReflash=false;
	
	public BBSGetMessage(Context context) {
		this.context=context;
		this.messageList=(BBSMessageListActivity)context;
		httpHelper=new HttpHelper(context);
		footView=messageList.footView;
		loadTv=(TextView) footView.findViewById(R.id.bbs_start_tv);
		cliclLayout=(LinearLayout) footView.findViewById(R.id.bbs_startload_ll);
		loadLayout=(LinearLayout) footView.findViewById(R.id.bbs_loading_ll);
		loadingFooder();
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(!isReflash){//不是点击刷新的情况
			if(messageList.msgList.getFooterViewsCount()==0){
				messageList.msgList.addFooterView(footView);//起初的时候给ListVIEW添加加载view
			}
		}
	}
	
	@Override
	protected String doInBackground(String... params) {
		String data=null;
		String url=params[0];
		data=httpHelper.connectPost(url, getParamMap());
		JLog.d(TAG, "data==>"+data);
		isContinue(data);
		return data;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(TextUtils.isEmpty(result)){
			loadFail();
		} else if(result.equals(Constants.HTTP_RESPONSE_ERROR)){
			loadFail();
		}else{
			
			if(messageList.isLoadind){//加载完成后如果改变是否正在加载的状态
				messageList.isLoadind=false;
			}
			
			if(isReflash){
				messageList.reflastPb.setVisibility(View.GONE);
				messageList.refalseIv.setVisibility(View.VISIBLE);
				
			}
			
			try {
				List<BbsInformationItem> newList=new BBSParse().parseInformation(result);
				if(newList!=null && newList.size()<20){
					messageList.msgList.removeFooterView(footView);
				}
				if(newList==null||newList.isEmpty()){
					messageList.msgList.removeFooterView(footView);
					ToastOrder.makeText(context, setString(R.string.bbs_info_09), ToastOrder.LENGTH_LONG).show();
				}else{
					if(messageList.informationList!=null && !isReflash){//第一次加载的时候informationList为null
						messageList.informationList.addAll(newList);
					}else{
						messageList.informationList=newList;
					}
					cliclLayout.setVisibility(View.VISIBLE);
					loadLayout.setVisibility(View.GONE);//改变底部布局
					
					messageList.messageAdapter.setInformationList(messageList.informationList);
					messageList.messageAdapter.notifyDataSetChanged();
//					messageList.msgList.removeFooterView(footView);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
		
		
	}
	private void isContinue(String result){
		//4.1版本以上
		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
		}else{
				Message message = mHandler.obtainMessage();
				message.obj = result;
				message.sendToTarget();
		}
	}
	
	private Handler mHandler = new  Handler(){
		public void handleMessage(Message msg) {
			String result =  (String) msg.obj;
			if(TextUtils.isEmpty(result)){
				loadFail();
			} else if(result.equals(Constants.HTTP_RESPONSE_ERROR)){
				loadFail();
			}else{
				
				if(messageList.isLoadind){//加载完成后如果改变是否正在加载的状态
					messageList.isLoadind=false;
				}
				
				if(isReflash){
					messageList.reflastPb.setVisibility(View.GONE);
					messageList.refalseIv.setVisibility(View.VISIBLE);
					
				}
				
				try {
					List<BbsInformationItem> newList=new BBSParse().parseInformation(result);
					if(newList!=null && newList.size()<20){
						messageList.msgList.removeFooterView(footView);
					}
					if(newList==null||newList.isEmpty()){
						messageList.msgList.removeFooterView(footView);
						ToastOrder.makeText(context, setString(R.string.bbs_info_09), ToastOrder.LENGTH_LONG).show();
					}else{
						if(messageList.informationList!=null && !isReflash){//第一次加载的时候informationList为null
							messageList.informationList.addAll(newList);
						}else{
							messageList.informationList=newList;
						}
						cliclLayout.setVisibility(View.VISIBLE);
						loadLayout.setVisibility(View.GONE);//改变底部布局
						
						messageList.messageAdapter.setInformationList(messageList.informationList);
						messageList.messageAdapter.notifyDataSetChanged();
//						messageList.msgList.removeFooterView(footView);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	};
	
	private void loadFail(){
		if(isReflash){
			ToastOrder.makeText(context, setString(R.string.bbs_info_08), ToastOrder.LENGTH_LONG).show();
			messageList.reflastPb.setVisibility(View.GONE);
			messageList.refalseIv.setVisibility(View.VISIBLE);
		}else{
			messageList.isLoadind=false;//此时没有加载
		}
		unLoadFooder();
		loadTv.setText(context.getResources().getString(R.string.BBS_LOAD_FAIL));//加载失败，重试
	}
	
	public HashMap<String, String> getParamMap() {
		return paramMap;
	}

	public void setParamMap(HashMap<String, String> paramMap) {
		this.paramMap = paramMap;
	}
	
	public void setIsReflash(boolean flag) {
		this.isReflash=flag;
	}
	
	/**
	 * 未加载时,listview footer状态
	 */
	private void unLoadFooder(){
		messageList.isLoadind=false;//此时没有加载
		loadLayout.setVisibility(View.GONE);
		cliclLayout.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 正在加载时,listview footer状态
	 */
	private void loadingFooder(){
		cliclLayout.setVisibility(View.GONE);
		loadLayout.setVisibility(View.VISIBLE);
	}

	private String setString(int stringId){
		return context.getResources().getString(stringId);
	}
}
