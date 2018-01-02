package com.yunhu.yhshxc.wechat.fragments;

import gcg.org.debug.JLog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.yunhu.yhshxc.wechat.approval.ApprovalActivity;
import com.yunhu.yhshxc.wechat.bo.Group;
import com.yunhu.yhshxc.wechat.bo.PersonalWechat;
import com.yunhu.yhshxc.wechat.bo.Topic;
import com.yunhu.yhshxc.wechat.createGroup.CreateGroupActivity;
import com.yunhu.yhshxc.wechat.db.GroupDB;
import com.yunhu.yhshxc.wechat.db.PersonalWechatDB;
import com.yunhu.yhshxc.wechat.db.ReplyDB;
import com.yunhu.yhshxc.wechat.db.TopicDB;
import com.yunhu.yhshxc.wechat.exchange.ExchangeActivity;
import com.yunhu.yhshxc.wechat.exchange.PersonalWechatActivity;
import com.yunhu.yhshxc.wechat.survey.SurveyActivity;
import com.yunhu.yhshxc.wechat.topic.CreateTopicActivity;
import com.yunhu.yhshxc.wechat.view.MyExpandableListView;
import com.yunhu.yhshxc.wechat.view.SlideView;
import com.yunhu.yhshxc.wechat.view.SlideView.ExpendListViewOnlick;
import com.yunhu.yhshxc.wechat.view.TopictView;
import com.yunhu.yhshxc.widget.GCGListView;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 汇聊
 * @author xuelinlin
 *
 */
@SuppressLint("NewApi")
public class SingleChatFragment extends Fragment implements ExpendListViewOnlick{
	
	private TextView tv_tianjia;
	private MyExpandableListView lv_chat;
	private GCGListView lv_siliao;
	private SiLiaoAdapter siLiaoAdapter;
	private LinearLayout ll_siliao;
	private ImageView iv_siliao;
	private TextView title_num;
	
	private List<List<Topic>> childrenData  = new ArrayList<List<Topic>>();
    private MyExpendableAdapter adapter;  
    private Context context;
	private TopicDB topicDb;
	private List<Topic> pubTopics = new ArrayList<Topic>();//公开话题
	private List<Topic> bumenTopics = new ArrayList<Topic>();//部门交流
//	private List<Topic> personalTopics ;//私聊话题
	private List<Topic> historyTopics = new ArrayList<Topic>();//历史话题
	private List<PersonalWechat> personalList = new ArrayList<PersonalWechat>();
	private PersonalWechatDB personalWechatDB ;
	private PopMenu popMenu;
	private ReplyDB replyDb;
	private GroupDB groupDB;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	public static String[] str = null;
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.wechat_sigle_chat, null);
		str = new String[]{ PublicUtils.getResourceString(SoftApplication.context,R.string.create_group), PublicUtils.getResourceString(SoftApplication.context,R.string.wechat_topic_cname) };
		initWidget();
        initPopMenu();
		initView(v);
		intiData();
		return v;
	}
	private void initWidget() {
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.wechat_moren_group_header)
		.showImageForEmptyUri(R.drawable.wechat_moren_group_header)
		.showImageOnFail(R.drawable.wechat_moren_group_header)
		.cacheInMemory()
		.cacheOnDisc().displayer(new RoundedBitmapDisplayer(10))
		.build();
		context = getActivity();
		replyDb= new ReplyDB(context);
		groupDB = new GroupDB(context);
        topicDb = new TopicDB(context);
        personalWechatDB = new PersonalWechatDB(context);
	}
	private void initPopMenu() {
		 popMenu = new PopMenu(context);
	        popMenu.addItems(str);
	        popMenu.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					switch (position) {
					case 0://建群
						if(SharedPrefrencesForWechatUtil.getInstance(getActivity()).getIsGroup().equals("0")){//不可
							ToastOrder.makeText(getActivity(), R.string.wechat_content27, ToastOrder.LENGTH_LONG).show();
						}else{
							createGroup();
						}
						popMenu.dismiss();
						break;
					case 1://建话题
						if(SharedPrefrencesForWechatUtil.getInstance(getActivity()).getIsTopic().equals("0")){
							ToastOrder.makeText(getActivity(), R.string.wechat_content26, ToastOrder.LENGTH_LONG).show();
						}else{
							Intent intent = new Intent(getActivity(),CreateTopicActivity.class);
							startActivity(intent);
						}
						
						popMenu.dismiss();
						break;
					case 2://私聊
						if(SharedPrefrencesForWechatUtil.getInstance(getActivity()).getIsPrivate().equals("0")){
							ToastOrder.makeText(getActivity(), R.string.wechat_content25, ToastOrder.LENGTH_LONG).show();
						}else{
							
						}
						popMenu.dismiss();
						break;

					default:
						break;
					}
				}
			});
		
	}
	private void initRefreshData(){
		childrenData.clear();
//		if(pubTopics.size() != 0){
//			Topic top = pubTopics.get(pubTopics.size()-1);
//			if(top!=null){
//				List<Topic> pub = topicDb.findTopicListByClassify(1,DateUtil.getCurDate(),0,top.getRecentTime(),top.getId());
//				pubTopics.addAll(pub);
//			}
//		}else{
//			pubTopics = topicDb.findTopicListByClassify(1,DateUtil.getCurDate(),0,null,0);
//		}
//		
//		if(bumenTopics.size() !=0){
//			Topic top = bumenTopics.get(bumenTopics.size()-1);
//			if(top != null){
//				List<Topic> bum = topicDb.findTopicListForBuM(DateUtil.getCurDate(),0,top.getRecentTime(),top.getId());
//				bumenTopics.addAll(bum);
//			}
//			
//		}else{
//			bumenTopics = topicDb.findTopicListForBuM(DateUtil.getCurDate(), 0, null, 0);
//		}
//        
//		if(historyTopics.size() !=0){
//			Topic top = historyTopics.get(historyTopics.size()-1);
//			if(top!=null){
//				List<Topic> his = topicDb.findHistoryTopicList(DateUtil.getCurDate(),1,top.getRecentTime(),top.getId());
//				historyTopics.addAll(his);
//			}
//		}else{
//			historyTopics = topicDb.findHistoryTopicList(DateUtil.getCurDate(),1, null,0);
//		}
        personalList = personalWechatDB.findPersonalWechat();
        siLiaoAdapter.refreshList(personalList);
        childrenData.add(pubTopics);
        childrenData.add(bumenTopics);
//      childrenData.add(personalTopics);
        childrenData.add(historyTopics);
	}
	int classify;
	private void intiData() {
		initRefreshData();
        adapter = new MyExpendableAdapter(this,childrenData);
        lv_chat.setAdapter(adapter);
        
	}
	private void initSiliaoCount(){
		int count = 0;
		count = personalWechatDB.findAllPersonalWechatCount();
		if (count > 0) {
			title_num.setVisibility(View.VISIBLE);
			title_num.setText(String.valueOf(count));
		} else {
			title_num.setVisibility(View.GONE);
		}
	}
	private void initView(View v) {
		title_num = (TextView) v.findViewById(R.id.title_num);
		initSiliaoCount();
		iv_siliao = (ImageView) v.findViewById(R.id.select_down);
		iv_siliao.setBackgroundResource(R.drawable.right_select);
		ll_siliao = (LinearLayout) v.findViewById(R.id.ll_siliao);
		ll_siliao.setOnClickListener(listener);
		lv_chat = (MyExpandableListView) v.findViewById(R.id.lv_chat);
		lv_siliao = (GCGListView) v.findViewById(R.id.lv_siliao);
		siLiaoAdapter = new SiLiaoAdapter(context,personalList);
		lv_siliao.setOnItemClickListener(itemListener);
		lv_siliao.setAdapter(siLiaoAdapter);
		tv_tianjia = (TextView) v.findViewById(R.id.tv_tianjia);
		tv_tianjia.setOnClickListener(listener);
		lv_chat.setGroupIndicator(null);
		lv_chat.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int groupPosition,
					int childPosition, long arg4) {
//				Toast.makeText(getActivity(), "groupPosition "+groupPosition+"  childPosition "+childPosition, 0).show();
				
//				if(childPosition == childrenData.get(groupPosition).size()){
//					Topic topic = childrenData.get(groupPosition).get(childPosition-1);
//					if(groupPosition == 0){
//						List<Topic> pub = topicDb.findTopicListByClassify(1,DateUtil.getCurDate(),0,topic.getRecentTime(),topic.getId());
//						pubTopics.addAll(pub);
//						childrenData.clear();
//						childrenData.add(pubTopics);
//					    childrenData.add(bumenTopics);
//					    childrenData.add(historyTopics);
//					    refresh(childrenData,pubTopics,bumenTopics,historyTopics);
//					}else if(groupPosition == 1){
//						List<Topic> pub = topicDb.findTopicListForBuM(DateUtil.getCurDate(),0,topic.getRecentTime(),topic.getId());
//						bumenTopics.addAll(pub);
//						childrenData.clear();
//						childrenData.add(pubTopics);
//					    childrenData.add(bumenTopics);
//					    childrenData.add(historyTopics);
//					    refresh(childrenData,pubTopics,bumenTopics,historyTopics);
//					}else if(groupPosition == 2){
//						List<Topic> pub = topicDb.findHistoryTopicList(DateUtil.getCurDate(),1,topic.getRecentTime(),topic.getId());
//						historyTopics.addAll(pub);
//						childrenData.clear();
//						childrenData.add(pubTopics);
//					    childrenData.add(bumenTopics);
//					    childrenData.add(historyTopics);
//					    refresh(childrenData,pubTopics,bumenTopics,historyTopics);
//					}
//				}else{
					Topic topic = childrenData.get(groupPosition).get(childPosition);
					if(topic!=null){
						int isHistory = 0;
						if(groupPosition == childrenData.size()-1){
							isHistory = 1;
						}else{
							isHistory = 0;
						}
						if(topic.getType().equals(Topic.TYPE_1)){
							Intent intent = new Intent(getActivity(),ExchangeActivity.class);
							intent.putExtra("topicId", topic.getTopicId());
							intent.putExtra("isHistory", isHistory);
							startActivity(intent);
						}else if(topic.getType().equals(Topic.TYPE_2)){
							Intent intent = new Intent(getActivity(),SurveyActivity.class);
							intent.putExtra("topicId", topic.getTopicId());
							intent.putExtra("isHistory", isHistory);
							startActivity(intent);
						}else if(topic.getType().equals(Topic.TYPE_3)){
							Intent intent = new Intent(getActivity(),ApprovalActivity.class);
							intent.putExtra("topicId", topic.getTopicId());
							intent.putExtra("isHistory", isHistory);
							startActivity(intent);
						}
						
					}
//				}
				return false;
			}
		});
	}
	private boolean isOpen = false;
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
//			showPopup(v);
			switch (v.getId()) {
			case R.id.ll_siliao:
				if(isOpen){
					isOpen = false;
					lv_siliao.setVisibility(View.GONE);
					iv_siliao.setBackgroundResource(R.drawable.right_select);
				}else{
					isOpen = true;
					lv_siliao.setVisibility(View.VISIBLE);
					iv_siliao
					.setBackgroundResource(R.drawable.up_select);
				}
				break;

			default:
				popMenu.showAsDropDown(v);
				break;
			}
			
		}
	};

	
	private void createGroup() {
		Intent intent = new Intent(getActivity(),CreateGroupActivity.class);
		startActivity(intent);
	}
	class MyExpendableAdapter extends BaseExpandableListAdapter{
		private ExpendListViewOnlick mOnClick;
		// 设置组视图的显示文字
					private String[] groupTypes = new String[] { PublicUtils.getResourceString(context,R.string.wechat_content22),
				PublicUtils.getResourceString(context,R.string.wechat_content23),PublicUtils.getResourceString(context,R.string.wechat_content24) };
					private int[] groupImages = new int[]{R.drawable.wechat_public,R.drawable.wechat_bumen,R.drawable.wecht_lishi};
					// 子视图显示文字
//					private String[][] child = new String[][] { { "1", "2", "3" },
//							{ "1", "2" }, { "1", "2", "3", "4", "5" },{"1", "2", "3", "4", "5"} };
					private List<List<Topic>> childrenData;
					// 重写ExpandableListAdapter中的各个方法
					public MyExpendableAdapter( ExpendListViewOnlick mOnClick,List<List<Topic>> childrenData){
						this.mOnClick = mOnClick;
						this.childrenData = childrenData;
					}
					
					@Override
					public int getGroupCount() {
						return groupTypes.length;
					}

					@Override
					public Object getGroup(int groupPosition) {
						return groupTypes[groupPosition];
					}

					@Override
					public long getGroupId(int groupPosition) {
						return groupPosition;
					}

					@Override
					public int getChildrenCount(int groupPosition) {
//						return child[groupPosition].length;
						if(childrenData.get(groupPosition).size() == 0){
							return childrenData.get(groupPosition).size();
						}else{
							return childrenData.get(groupPosition).size()+1;
						}
						
					}

					@Override
					public Object getChild(int groupPosition, int childPosition) {
//						return child[groupPosition][childPosition];
						return childrenData.get(groupPosition).get(childPosition);
					}

					@Override
					public long getChildId(int groupPosition, int childPosition) {
						return childPosition;
					}

					@Override
					public boolean hasStableIds() {
						return true;
					}

					@Override
					public View getGroupView(int groupPosition, boolean isExpanded,
							View convertView, ViewGroup parent) {
						RelativeLayout parentLayout = (RelativeLayout) View.inflate(
								getActivity(), R.layout.layout_group, null);
						TextView titleName = (TextView) parentLayout
								.findViewById(R.id.title_name);
						ImageView parentImageViw = (ImageView) parentLayout
								.findViewById(R.id.select_down);
						ImageView iv_groupname = (ImageView) parentLayout
								.findViewById(R.id.iv_groupname);
						TextView titleNum = (TextView) parentLayout
								.findViewById(R.id.title_num);
						if(groupPosition == groupTypes.length){
							titleNum.setVisibility(View.GONE);
						}else{
							if(groupPosition == 0){
								int count = replyDb.findClassfyReplyNum(1,0);
								if(count >0){
									titleNum.setVisibility(View.VISIBLE);
									titleNum.setText(String.valueOf(count));
								}else{
									titleNum.setVisibility(View.GONE);
								}
							}else if(groupPosition == 1){
								int count = replyDb.findBumenReplayNum(1,0);
								if(count >0){
									titleNum.setVisibility(View.VISIBLE);
									titleNum.setText(String.valueOf(count));
								}else{
									titleNum.setVisibility(View.GONE);
								}
							}
							
						}
						// 判断isExpanded就可以控制是按下还是关闭，同时更换图片
						if (isExpanded) {
							parentImageViw.setBackgroundResource(R.drawable.up_select);
						} else {
							parentImageViw
									.setBackgroundResource(R.drawable.right_select);
						}
						titleName.setText(getGroup(groupPosition) + "");
						iv_groupname.setImageDrawable(getActivity().getResources().getDrawable(groupImages[groupPosition]));
						return parentLayout;
					}

					@Override
					public View getChildView(int groupPosition, int childPosition,
							boolean isLastChild, View convertView, ViewGroup parent) {
						// 注意 子布局不能是LinearLayout 格式

//						LinearLayout childLayout = (LinearLayout) View.inflate(
//								getActivity(), R.layout.item_body, null);
						TopictView view = new TopictView(getActivity());
						SlideView slideView=null;
						if (groupPosition+1==groupTypes.length) {
							if(childPosition == childrenData.get(groupPosition).size()){
								slideView = new SlideView(getActivity(),
										getActivity().getResources(), view.getView(),false,mOnClick);
							}else{
								slideView = new SlideView(getActivity(),
										getActivity().getResources(), view.getView(),true,mOnClick);	
							}
							
						}else{
							slideView = new SlideView(getActivity(),
									getActivity().getResources(), view.getView(),false,mOnClick);
						}
						slideView.shrink();
						slideView.getBack().setText(R.string.delete);
						slideView.setPostion(groupPosition,childPosition);
						if(childPosition == childrenData.get(groupPosition).size()){
							try {
								view.initData(null,replyDb);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}else{
							Topic top = childrenData.get(groupPosition).get(childPosition);
							try {
								view.initData(top,replyDb);
							} catch (ParseException e) {
								e.printStackTrace();
								JLog.d("aaa", "e   =   "+e.toString());
							}
//							imageLoader.displayImage(childrenData.get(groupPosition).get(childPosition).getCreateTime(), view.getImageView(), options); 
							if(top!=null){
								Group group = groupDB.findGroupByGroupId(top.getGroupId());
								if(group!=null){
									if(!TextUtils.isEmpty(group.getLogo())){
										JLog.d("aaa", "url = "+group.getLogo());
											imageLoader.displayImage(group.getLogo(), view.getImageView(), options, null);
									}
								}
								
							}
						}
						
						
						return slideView;
					}

					@Override
					public boolean isChildSelectable(int groupPosition,
							int childPosition) {
						return true;
					}
					public void refreshAdapter(List<List<Topic>> childrenData){
						this.childrenData = childrenData;
						notifyDataSetChanged();
					}
	}
	@Override
	public void expendOnclick(int groupPosition, int childPosition) {
		//删除话题
		Topic topic = childrenData.get(groupPosition).get(childPosition);
		if(topic!=null){
			submitDelete(topic);
		}
		
	}
	private void submitDelete(final Topic topic) {
		String url = UrlInfo.doWeChatTopicHistoryInfo(getActivity());
		GcgHttpClient.getInstance(getActivity()).post(url, params(topic),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d("alin", "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							if ("0000".equals(resultcode)) {
								topicDb.deleteTopic(topic.getTopicId());
								initRefreshData();
								adapter.refreshAdapter(childrenData);
								ToastOrder.makeText(getActivity(), R.string.delete_sucsess,
										ToastOrder.LENGTH_LONG).show();
								
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
							// searchHandler.sendEmptyMessage(3);
							ToastOrder.makeText(getActivity(), R.string.delete_fail,
									ToastOrder.LENGTH_LONG).show();
						}
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailure(Throwable error, String content) {
						JLog.d("alin", "failure");
						ToastOrder.makeText(getActivity(), R.string.delete_fail,
								ToastOrder.LENGTH_LONG).show();
					}
				});
	}

	private RequestParams params(Topic topic)  {
		RequestParams params = new RequestParams();
		String json = "";
		JSONObject dObject = new JSONObject();
		try {
			dObject.put("groupId", topic.getGroupId());
			dObject.put("topicId", topic.getTopicId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		json = dObject.toString();
		params.put("data", json);
		JLog.d("alin", "params:" + params.toString());
		return params;
	}

	public void refresh(List<List<Topic>> childrenDat,List<Topic> pubTopics,List<Topic> bumenTopics,List<Topic> historyTopics){
		this.childrenData = childrenDat;
		this.pubTopics = pubTopics;
		this.bumenTopics = bumenTopics;
		this.historyTopics = historyTopics;
		adapter.refreshAdapter(childrenData);;
	}
	public void refreshSL(List<PersonalWechat> list){
		this.personalList = list;
		initSiliaoCount();
		siLiaoAdapter.refreshList(personalList);
	}
	class SiLiaoAdapter extends BaseAdapter{
		private List<PersonalWechat> list ;
		private Context context;
		public SiLiaoAdapter(Context context,List<PersonalWechat> list) {
			this.context = context;
			this.list = list;
		}
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TopictView view = null;
			if (convertView == null) {
				view = new TopictView(context);
				convertView = view.getView();
				convertView.setTag(view);
			} else {
				view = (TopictView) convertView.getTag();
			}
			try {
				view.initPerson(list.get(position));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return convertView;
		}
		public void refreshList(List<PersonalWechat> list){
			this.list = list;
			notifyDataSetChanged();
		}
		
	}
	private OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			PersonalWechat person = personalList.get(position);
			if(person != null){
				Intent intent = new Intent(getActivity(), PersonalWechatActivity.class);
				int sUserId = person.getsUserId();
				if (sUserId == SharedPreferencesUtil.getInstance(getActivity()).getUserId()) {
					intent.putExtra("userId", person.getdUserId());
					intent.putExtra("userName",person.getdUserName());
				}else{
					intent.putExtra("userId", person.getsUserId());
					intent.putExtra("userName",person.getsUserName());
				}
				startActivity(intent);
			}
		}
	};
}
