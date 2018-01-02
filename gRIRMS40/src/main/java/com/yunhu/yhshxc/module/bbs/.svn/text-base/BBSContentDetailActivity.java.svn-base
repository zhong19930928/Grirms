package com.yunhu.yhshxc.module.bbs;

import gcg.org.debug.JLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.BbsCommentItem;
import com.yunhu.yhshxc.bo.BbsInformationDetail;
import com.yunhu.yhshxc.bo.BbsInformationItem;
import com.yunhu.yhshxc.bo.BbsUserInfo;
import com.yunhu.yhshxc.bo.PhotoInfo;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.parser.BBSParse;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.FileHelper;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

public class BBSContentDetailActivity extends AbsBaseActivity {
	
	private static final String TAG = "BBSContentDetailActivity";
	// 返回成功
	protected static final int CONN_OK = 200;
	// 返回失败
	protected static final int CONN_FAIL = 404;
	// 图片下载完毕
	protected static final int LOAD_IMAGE_OVER = 300;
	protected static final int LOAD_BIG_IMAGE_OVER = 400;
	// 查询评论参数
	private HashMap<String, String> params ;
	// 显示评论LISTVIEW
	private ListView lv_bbs_detail;
	// 显示评论adapter
	private BBSReplyAdapter mAdapter;
	// listview footer
	private LinearLayout ll_bbs_footer;
	// listview footer 更多...
	private LinearLayout ll_footer_startload;
	// listview footer 正在加载...
	private LinearLayout ll_footer_loading;
	// 帖子图片
	private ImageView iv_bbs_detail_list_head_image;
	// 评论list
	private List<BbsCommentItem> bbsCommentItems;
	// 本机号码
	private String phoneno;
	// 帖子信息
	private BbsInformationDetail bbsInformationDetail;
	// 帖子id
	private String bbsId;
	// 用户信息
	private BbsUserInfo bbsUserInfo;
	// 联网类
//	private CoreHttpHelper mHttpHelper;
	// 回复内容
	private EditText et_replay;
	// 回复提交参数
	private HashMap<String, String> replayParams;
	// 是否第一次加载评论
	boolean isFirstLoad = true;
	// 是否是显示大图片
	private boolean isShowBigImage = false;
	// 加载会话框
	private ProgressBar downPB;
	// 评论条数textview
	private TextView tv_bbs_detail_list_head_reply_count;
	// 评论条数
	private String reply_count;
	
	// Handler
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case CONN_OK:// 评论查询成功
				unLoadFooder();
				// 评论json
				String replyReturnJSON = (String) msg.obj;
				JLog.d(TAG, "评论JSON==>"+replyReturnJSON);
				try {
					// 临时评论list--解析json
					List<BbsCommentItem> parseComments = new BBSParse().parseComment(replyReturnJSON);
					
					if(parseComments != null && parseComments.size() > 0){// 有评论
						JLog.d(TAG, "评论条数==>"+parseComments.size());
						if(isFirstLoad){// 第一次加载评论
							// 置空评论
							bbsCommentItems = new ArrayList<BbsCommentItem>();
							
							// 更新条目数
							tv_bbs_detail_list_head_reply_count.setText(reply_count);
						}
						// 添加评论
						bbsCommentItems.addAll(parseComments);
						if(parseComments.size() < 20){// 评论数小于20
							if(lv_bbs_detail.getFooterViewsCount() > 0){// 如果listview有 footer
								// 移除listview的footer
								lv_bbs_detail.removeFooterView(ll_bbs_footer);
							}
						}else{// 评论数不小于20
							if(lv_bbs_detail.getFooterViewsCount() < 1){// 如果listview没有footer
								// 添加listview的footer
								lv_bbs_detail.addFooterView(ll_bbs_footer);
							}
						}
					}else{// 没有评论
						if(lv_bbs_detail.getFooterViewsCount() > 0){// 如果listview有 footer
							// 移除listview的footer
							lv_bbs_detail.removeFooterView(ll_bbs_footer);
						}
						if(!isFirstLoad){// 不是第一次加载
							// 提示用户没有更多评论
							ToastOrder.makeText(BBSContentDetailActivity.this, setString(R.string.bbs_info_05), ToastOrder.LENGTH_LONG).show();
						}
					}
					// 改变加载状态
					isFirstLoad = false;
				} catch (Exception e) {
					e.printStackTrace();
					ToastOrder.makeText(BBSContentDetailActivity.this, setString(R.string.bbs_info_06), ToastOrder.LENGTH_LONG).show();
				}
		
				// 通知adapter更新数据
				mAdapter.notifyDataSetChanged();
				break;
			case 18001:// 回复提交成功
				JLog.d(TAG, "<回复成功!>");
				// 评论数加1
				reply_count = (Integer.valueOf(reply_count)+1)+"";
				
//				Toast.makeText(BBSContentDetailActivity.this, "回复成功!", 1).show();
				// 改变加载状态
				isFirstLoad = true;
				loadReplies();
				
//				// 通知adapter更新数据
//				mAdapter.notifyDataSetChanged();
				break;
			case LOAD_IMAGE_OVER:// 图片下载完毕
				// 获取图片路径
				String imagePathName = (String) msg.obj;
				// 图片bitmap
				Bitmap image = BitmapFactory.decodeFile(imagePathName);
				// 设置图片
				iv_bbs_detail_list_head_image.setImageBitmap(image);
				break;
			case LOAD_BIG_IMAGE_OVER:// 大图片下载完毕
				// 加载view隐藏
				downPB.setVisibility(View.GONE);
				// 获取图片路径
				String imageName = (String) msg.obj;
				// 初始化大图
				Dialog showPhoto=showPhotoDialog(imageName);
				// 显示大图
				showPhoto.show();
				break;
			case CONN_FAIL:// 评论查询失败
				unLoadFooder();
				ToastOrder.makeText(BBSContentDetailActivity.this, setString(R.string.bbs_info_07), ToastOrder.LENGTH_LONG).show();
				break;
			default:// 评论查询失败
				unLoadFooder();
				ToastOrder.makeText(BBSContentDetailActivity.this, setString(R.string.bbs_info_07), ToastOrder.LENGTH_LONG).show();
				break;
			}
		};
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_content_detail);
		initBase();
		initBBSDetial();
		
		
	}
	
	/**
	 * 初始化
	 */
	private void initBBSDetial() {
		// 获取bbsInformationItem 对象
		BbsInformationItem bbsInformationItem = (BbsInformationItem) getIntent()
				.getSerializableExtra("bbsinformationitem");
		
		if(bbsInformationItem == null){
			JLog.d(TAG, "bbsInformationItem is null");
		}
		// 获取bbsInformationDetail对象
		bbsInformationDetail =bbsInformationItem.getBbsInfoDetail();
		// 获取帖子id
		bbsId = bbsInformationDetail.getId();
		// 获取bbsUserInfo对象
		bbsUserInfo =bbsInformationItem.getUserInfo();
		// 回复layout
		LinearLayout ll_replay =(LinearLayout) findViewById(R.id.ll_bbs_detail_reply);
		// 点击监听
		ll_replay.setOnClickListener(this);
		// 回复edittext
		et_replay = (EditText) findViewById(R.id.et_bbs_detail_content);
		
		initBBSListView();
		
		initParams();
		
		loadReplies();
	}

	/**
	 * 初始化查询评论参数
	 */
	private void initParams() {
		
		// 获得存放电话SharedPreferences
		SharedPreferences sp = this.getSharedPreferences("MOBILE_MDN_PREF", Context.MODE_PRIVATE);
		// 获取电话号码
		phoneno = sp.getString("MOBILE_MDN_NO_PREF", "");
		// 初始化查询评论参数
		params = new HashMap<String, String>();
		// 存放电话号码
		params.put("phoneno", phoneno);
		// 帖子id
		params.put("ids", bbsId);
	}

	/**
	 * 加载评论
	 */
	private void loadReplies() {
		new Thread(){
			public void run() {
				if(!isFirstLoad){// 不是第一次登陆
					// 加载过的最后一条评论的创建时间
				    String lastCreateTime = bbsCommentItems.get(bbsCommentItems.size()-1).getCreateTime();
				    // 添加分页参数createtime
					params.put("createtime", lastCreateTime);
				}else{// 第一次登陆
					if(params.get("createtime") != null){// createtime 已存在
						// 移除 createtime
						params.remove("createtime"); 
					}
				}
				
				// 初始化HttpHelper
				HttpHelper httpHelper = new HttpHelper(BBSContentDetailActivity.this);
				// 连接服务器,获取评论json
				String replyStr = httpHelper.connectPost(UrlInfo.getUrlBbsCommentInfo(BBSContentDetailActivity.this), params);
				
				for(Map.Entry p : params.entrySet()){
					JLog.d(TAG, p.getKey()+"="+p.getValue()+"&");
				}
				
				// 初始化message
				Message msg = new Message();
				if(!TextUtils.isEmpty(replyStr)){//json不为空-->查询成功
					JLog.d(TAG, "评论json ==> "+replyStr);
					msg.what = CONN_OK;
					msg.obj = replyStr;
				}else{//json为空-->查询失败
					msg.what = CONN_FAIL;
				}
				//发出消息
				mHandler.sendMessage(msg);
			};
		}.start();
	}

	/**
	 * 初始化ListView
	 */
	private void initBBSListView() {
		// 获取list资源
		lv_bbs_detail = (ListView) findViewById(R.id.lv_bbs_detail_list);
		// 初始化发表时间
		String distanceTime = bbsInformationDetail.getCreateTime();
		try {
			// 转化为距今 多久 的时间 字符串
			distanceTime = PublicUtils.compareDate(distanceTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}finally{
			// list头信息 -->帖子信息
			LinearLayout ll_bbs_header = (LinearLayout) View.inflate(this, R.layout.bbs_content_detail_list_head, null);
			// 帖子内容
			TextView tv_bbs_detail_list_head_text = (TextView) ll_bbs_header.findViewById(R.id.tv_bbs_detail_list_head_text);
			// 发布地点
			TextView tv_bbs_detail_list_head_loc = (TextView) ll_bbs_header.findViewById(R.id.tv_bbs_detail_list_head_location);
			// 定位layout
			LinearLayout ll_bbs_detail_list_head_loc = (LinearLayout) ll_bbs_header.findViewById(R.id.ll_bbs_detail_list_head_location);
			// 评论条数
			tv_bbs_detail_list_head_reply_count = (TextView) ll_bbs_header.findViewById(R.id.tv_bbs_detail_list_head_repty_count);
			// 发布人
			TextView tv_bbs_detail_list_head_user = (TextView) ll_bbs_header.findViewById(R.id.tv_bbs_detail_list_head_user);
			// 具体时间
			TextView tv_bbs_detail_list_head_time = (TextView) ll_bbs_header.findViewById(R.id.tv_bbs_detail_list_head_time);
			// 大致时间
			TextView tv_bbs_detail_list_head_time_r = (TextView) ll_bbs_header.findViewById(R.id.tv_bbs_detail_list_head_time_r);
			// 帖子图片
			iv_bbs_detail_list_head_image = (ImageView) ll_bbs_header.findViewById(R.id.iv_bbs_detail_list_head_image);
			// 发帖人头像
			ImageView iv_bbs_detail_list_head_userhead = (ImageView) ll_bbs_header.findViewById(R.id.iv_bbs_detail_list_head_userhead);
			// 帖子图片layout
			LinearLayout ll_bbs_detail_list_head_image = (LinearLayout) ll_bbs_header.findViewById(R.id.ll_bbs_detail_list_head_image);
			// 用户等级
			ImageView iv_bbs_detail_list_head_userleve = (ImageView) ll_bbs_header.findViewById(R.id.iv_bbs_detail_list_head_userleve);
			// 用户积分
			TextView tv_bbs_detail_list_head_score  = (TextView) ll_bbs_header.findViewById(R.id.tv_bbs_detail_list_head_userscore);

			// 设置等级图标 && 积分
			if(!TextUtils.isEmpty(bbsUserInfo.getScore())){// 用户积分
				if(PublicUtils.getBBSLeve(bbsUserInfo.getScore()) != -1){
					iv_bbs_detail_list_head_userleve.setVisibility(View.VISIBLE);
					iv_bbs_detail_list_head_userleve.setImageResource(PublicUtils.getBBSLeveIcon(bbsUserInfo.getScore()));
				}else{
					iv_bbs_detail_list_head_userleve.setVisibility(View.GONE);
				}
				tv_bbs_detail_list_head_score.setText(bbsUserInfo.getScore());
			}else{
				iv_bbs_detail_list_head_userleve.setVisibility(View.GONE);
				tv_bbs_detail_list_head_score.setText("0");
			}
			JLog.d(TAG, "主贴:"+bbsUserInfo.getName()+"的积分==>"+bbsUserInfo.getScore());
			// 设置帖子文字内容
			if(!TextUtils.isEmpty(bbsInformationDetail.getContent())){// 有文字的时候
				// 设置帖子文字内容
				tv_bbs_detail_list_head_text.setText(bbsInformationDetail.getContent());
				// 显示帖子文字内容
				tv_bbs_detail_list_head_text.setVisibility(View.VISIBLE);
			}else{// 没有文字的时候
				// 隐藏帖子文字内容
				tv_bbs_detail_list_head_text.setVisibility(View.GONE);
			}
			// 设置地址
			if(!TextUtils.isEmpty(bbsInformationDetail.getAddress())){// 有地址的时候
				// 设置发布地点
				tv_bbs_detail_list_head_loc.setText(bbsInformationDetail.getAddress());
				// 显示地址layout
				ll_bbs_detail_list_head_loc.setVisibility(View.VISIBLE);
			}else{// 没有地址时
				// 隐藏地址layout
				ll_bbs_detail_list_head_loc.setVisibility(View.GONE);
			}
			// 记录评论数
			reply_count = bbsInformationDetail.getCommentNum();
			// 设置评论条数
			tv_bbs_detail_list_head_reply_count.setText(reply_count);
			// 设置发布人
			tv_bbs_detail_list_head_user.setText(PublicUtils.clearNumber(bbsUserInfo.getName()));
			// 设置具体时间
			tv_bbs_detail_list_head_time_r.setText(bbsInformationDetail.getCreateTime());
			// 设置大致时间
			tv_bbs_detail_list_head_time.setText(distanceTime);
			
			// 用户头像
			if((bbsUserInfo.getPhotoInfo() != null) && (!(TextUtils.isEmpty(bbsUserInfo.getPhotoInfo().getPhotoName() )))){// 如果用户有头像
				// 头像路径
				String userPhotoPathName = Constants.USERIV_PATH+bbsUserInfo.getPhotoInfo().getPhotoName();
				// 头像bitmap
				Bitmap userPhoto  = BitmapFactory.decodeFile(userPhotoPathName);
				// 设置头像
				iv_bbs_detail_list_head_userhead.setImageBitmap(userPhoto);
			}
			
			// 帖子图片
			if((bbsInformationDetail.getPhotoInfo() != null) &&
					(!(TextUtils.isEmpty(bbsInformationDetail.getPhotoInfo().getPhotoUrl())))){// 如果帖子有图片
				
				// 显示图片
				ll_bbs_detail_list_head_image.setVisibility(View.VISIBLE);
				// 点击事件
				ll_bbs_detail_list_head_image.setOnClickListener(this);
				// 从图片地址上获取图片名称
				String[] splitName = bbsInformationDetail.getPhotoInfo().getPhotoUrl().split("/");
				// 图片路径
				String imagePathName = Constants.CONTENTIV_PATH+splitName[splitName.length-1];
				// 图片文件
				File imageFile = new File(imagePathName);
				
				if(!imageFile.exists() || !imageFile.isFile()){// 图片不存在
					// 删除可能存在的文件夹
					imageFile.delete();
					
					LoadImageFromUrl(imageFile, bbsInformationDetail.getPhotoInfo().getPhotoUrl());
				}else{// 图片存在
					
//					Bitmap iamge  = BitmapFactory.decodeFile(imagePathName);
//					 Options opts = new Options();
//					 opts.outHeight = 160;
//					 opts.outWidth = 160;
					
					// 图片bitmap
					Bitmap image = BitmapFactory.decodeFile(imagePathName);
					// 设置图片
					iv_bbs_detail_list_head_image.setImageBitmap(image);
				}
			}else{// 如果帖子没有图片
				// 隐藏图片
				ll_bbs_detail_list_head_image.setVisibility(View.GONE);
			}
			
			// listview 添加头
			lv_bbs_detail.addHeaderView(ll_bbs_header);
			// listview footer
			ll_bbs_footer = (LinearLayout) View.inflate(this, R.layout.bbs_loadview, null);
			// 正在加载...
			ll_footer_loading = (LinearLayout) ll_bbs_footer.findViewById(R.id.bbs_loading_ll);
			// 更多...
			ll_footer_startload = (LinearLayout) ll_bbs_footer.findViewById(R.id.bbs_startload_ll);
			// 点击监听
			ll_footer_startload.setOnClickListener(this);
			
			unLoadFooder();
			// listview 添加脚
			lv_bbs_detail.addFooterView(ll_bbs_footer);
			// listview adapter
			mAdapter = new BBSReplyAdapter();
			// listview 设置 adapter
			lv_bbs_detail.setAdapter(mAdapter);
//			// 移除listview footer
//			lv_bbs_detail.removeFooterView(ll_bbs_footer);
			loadingFooder();
		}
	}
	
	/**
	 * 未加载时,listview footer状态
	 */
	private void unLoadFooder(){
		ll_footer_loading.setVisibility(View.GONE);
		ll_footer_startload.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 正在加载时,listview footer状态
	 */
	private void loadingFooder(){
		ll_footer_loading.setVisibility(View.VISIBLE);
		ll_footer_startload.setVisibility(View.GONE);
	}
	
	/**
	 * 点击回调
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bbs_startload_ll://更多
			loadReplies();
			loadingFooder();
			break;
		case R.id.ll_bbs_detail_reply://回复
			Replay();
			break;
		case R.id.ll_bbs_detail_list_head_image://点击缩略图片
			// 加载图片对话框
			downPB=(ProgressBar)v.findViewById(R.id.bbs_msg_content_iv_pb);
			
			downPhoto(bbsInformationDetail);
			break;
		}
	}
	
	/**
	 * 显示大图片
	 * @param informationDetail
	 */
	private void downPhoto(BbsInformationDetail informationDetail){
		// 获取PhotoInfo
		PhotoInfo photoInfo=informationDetail.getPhotoInfo();
		if(photoInfo!=null){// 图片信息不为空
			// 拆分图片url
			String[] str=photoInfo.getPhotoUrl().split("/");
			// 获取图片路径
			String path = Constants.CONTENTIV_PATH+str[str.length-1];
			// 图片文件
			File file = new File(path);
			JLog.d(TAG, "photoPath==>"+Constants.CONTENTIV_PATH+str[str.length-1]);
			
			if(file.exists() && file.isFile()){// 图片存在
				// 初始化大图片对话框
				Dialog showPhoto=showPhotoDialog(path);
				// 显示大图片
				showPhoto.show();
			}else{// 下载图片
				// 加载图片会话框
				downPB.setVisibility(View.VISIBLE);
				
				LoadImageFromUrl(file, photoInfo.getPhotoUrl());
				// 加载大图状态
				isShowBigImage = true;
			}
		}
	}
	
	/**
	 * 显示大图片会话框
	 * @param path
	 * @return
	 */
    private Dialog showPhotoDialog(String path) {
    	JLog.d(TAG, "photoPath==>"+path);
    	// 初始化对话框
    	Dialog photoDialog = new Dialog(BBSContentDetailActivity.this, R.style.transparentDialog1);
    	// 初始化会话框view
    	View view=View.inflate(BBSContentDetailActivity.this, R.layout.bbs_show_photo, null);
    	// 初始化图片linearlayout
    	LinearLayout iv=(LinearLayout)view.findViewById(R.id.bbs_showPhoto);
    	// 生成帖子图片
    	Drawable contentDrawable=Drawable.createFromPath(path);
    	// 设置图片linearlayout背景
    	iv.setBackgroundDrawable(contentDrawable);
    	// 将view添加到对话框
    	photoDialog.setContentView(view);
    	
    	return photoDialog;
    }
    
	/**
	 * 回复帖子
	 */
	private void Replay() {
		// 获取帖子内容
		String replay_content = et_replay.getText().toString().trim();
		if(!TextUtils.isEmpty(replay_content)){//非空
			// 初始化回复edittext
			et_replay.setText("");
			
			// 初始化 回复帖子参数
//			replayParams = new HashMap<String, String>();
//			replayParams.put("msgid", bbsId);
//			replayParams.put("postcontent", replay_content);
			
			RequestParams params = new RequestParams();
			params.put("msgid", bbsId);
			params.put("postcontent", replay_content);
			
			GcgHttpClient.getInstance(this).get(UrlInfo.getUrlBbsSubmitComment(BBSContentDetailActivity.this), params, new HttpResponseListener() {
				
				@Override
				public void onSuccess(int statusCode, String content) {
					ToastOrder.makeText(BBSContentDetailActivity.this, setString(R.string.bbs_info_01), ToastOrder.LENGTH_SHORT).show();
				}
				
				@Override
				public void onStart() {
					ToastOrder.makeText(BBSContentDetailActivity.this, setString(R.string.bbs_info_02), ToastOrder.LENGTH_SHORT).show();
				}
				
				@Override
				public void onFinish() {
					
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					ToastOrder.makeText(BBSContentDetailActivity.this, setString(R.string.bbs_info_03), ToastOrder.LENGTH_SHORT).show();
				}
			});
			
			
//			// 设置联网参数
//			CoreHttpHelperOptions options = new CoreHttpHelperOptions(BBSContentDetailActivity.this);
//			options.setErrorDelay(3000);
//			options.commitOptions();
			
//			if(mHttpHelper != null){
//				mHttpHelper.releaseHttpClient();
//			}
//			
//			// 初始化联网类
//			mHttpHelper = new CoreHttpHelper(BBSContentDetailActivity.this, mHandler);
			// 开启子线程 提交数据
			/*new Thread(){
				public void run() {
					// 提交数据
//					mHttpHelper.performQueryRequest(CoreHttpHelper.HTTP_METHOD_POST,REPLY_COMMIT_URL, replayParams, false, false, 0);
				};
			}.start();*/
//			SubmitInBackstageManager.getInstance(BBSContentDetailActivity.this).performJustSubmit(SubmitInBackstageManager.HTTP_METHOD_POST, UrlInfo.getUrlBbsSubmitComment(BBSContentDetailActivity.this), replayParams);
//			SubmitInBackstageManager.getInstance(BBSContentDetailActivity.this).commit();
//			Toast.makeText(this, "评论正在后台提交,请稍候查看", Toast.LENGTH_LONG).show();
//			// 初始化回复对话框
//			replayDialog = initDialog("正在提交评论,请稍候...");
//			// 显示
//			replayDialog.show();
		}else{
			// 提示评论内容为空
			ToastOrder.makeText(this, setString(R.string.bbs_info_04), ToastOrder.LENGTH_LONG).show();
			// 请求焦点
			et_replay.requestFocus();
		}
	}

	/**
	 * @author 王建雨
	 * ListView设配器
	 */
	private  final class BBSReplyAdapter extends BaseAdapter{
		
		/**
		 * 条目个数
		 */
		@Override
		public int getCount() {
			if(bbsCommentItems != null && bbsCommentItems.size() > 0){//bbsCommentItems 非空
				return bbsCommentItems.size();
			}else{
				return 0;
			}
		}
		
		/**
		 * 返回条目
		 */
		@Override
		public Object getItem(int position) { 
			return bbsCommentItems.get(position);
		}
		
		/**
		 * 返回条目id
		 */
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		/**
		 * 显示条目
		 */
		@SuppressWarnings("finally")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			// 获取评论
			BbsCommentItem bbsCommentItem = bbsCommentItems.get(position);
			// 获取评论人
			String replyUser = PublicUtils.clearNumber(bbsCommentItem.getCreateuser());
			// 获取评论时间
			String replyTime = bbsCommentItem.getCreateTime();
			// 获取评论等级
			String replyScore = bbsCommentItem.getScore();
			JLog.d(TAG, "用户积分 == >"+replyScore);
			try {
				// 获取在距离现在 的时间
				replyTime = PublicUtils.compareDate(replyTime);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				// 获取评论内容
				String replyContent = bbsCommentItem.getContent();
				// 显示的条目布局
				View view;
				// listview缓存view
				ItemViewHolder holder;
				
				if(convertView == null){// 历史view为非
					// 初始化itemholder
					holder = new ItemViewHolder();
					// 加载条目布局
					view = View.inflate(BBSContentDetailActivity.this, R.layout.bbs_content_detail_list_item, null);
					// 回复人
					holder.replyUser = (TextView) view.findViewById(R.id.tv_bbs_detail_list_item_user);
					// 回复时间
					holder.replyTime = (TextView) view.findViewById(R.id.tv_bbs_detail_list_item_time);
					// 回复内容
					holder.replyContent = (TextView) view.findViewById(R.id.tv_bbs_detail_list_item_text);
					// 回复人等级
					holder.replyLeve = (ImageView) view.findViewById(R.id.iv_bbs_detail_list_item_userleve);
					// 添加tag
					view.setTag(holder);
				}else{
					// 将历时view赋值给view
					view = convertView;
					// 获取tag
					holder = (ItemViewHolder) convertView.getTag();
				}
				// 设置回复人
				holder.replyUser.setText(replyUser);
				// 设置回复时间
				holder.replyTime.setText(replyTime);
				// 设置回复内容
				holder.replyContent.setText(replyContent);
				// 设置回复等级图标
				if(!TextUtils.isEmpty(replyScore)){
					if(PublicUtils.getBBSLeve(replyScore) == -1){
						holder.replyLeve.setVisibility(View.GONE);
					}else{
						holder.replyLeve.setVisibility(View.VISIBLE);
						holder.replyLeve.setImageResource(PublicUtils.getBBSLeveIconSmall(replyScore));
					}
					JLog.d(TAG,"回复:"+replyUser+"的积分==>"+replyScore);
				}else{
					holder.replyLeve.setVisibility(View.GONE);
				}
				
				
				return view;
			}
		}
	}
	
	/**
	 * @author 王建雨
	 * listview item缓存-->listview优化
	 */
	private final class ItemViewHolder{
		// 回复人
		private TextView replyUser;
		// 回复时间
		private TextView replyTime;
		// 回复内容
		private TextView replyContent;
		// 回复人等级
		private ImageView replyLeve;
	}
	
	
	/**
	 * 异步下载图片
	 * @param url 下载图片url
	 * @param locationPath图片存放路径
	 */
	private void LoadImageFromUrl(final File file,final String url) {
		
		new Thread(){
			public void run() {
				try {
					HttpHelper httpHelper = new HttpHelper(BBSContentDetailActivity.this);
					// 获取entity
					HttpEntity entity = httpHelper.connectGetReturnEntity(url);
					// 获取输入流
					InputStream is = entity.getContent();
					if (is != null) {
						// 设置输出流
						FileOutputStream fos = new FileOutputStream(file);
						// 缓存
						byte[] bt = new byte[1024];
						// 标志
						int i = 0;
						// 流的对考
						while ((i = is.read(bt)) != -1) {
							// 写入输出流缓冲区
							fos.write(bt, 0, i);
						}
						// 刷新输出流
						fos.flush();
						fos.close();
						is.close();
					}
					// message
					Message msg = new Message();
					if(isShowBigImage){ // 加载大图片
						msg.what = LOAD_BIG_IMAGE_OVER;
					}else{// 加载下图片
						msg.what = LOAD_IMAGE_OVER;
					}
					// 将图片路径设置给msg
					msg.obj = file.getPath();
					// 通知图片下载完毕
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					// 删除不完整图片
					new FileHelper().deleteFile(file.getPath());	
				}
			};
		}.start();
	}
	private String setString(int stringId){
		return getResources().getString(stringId);
	}
}
