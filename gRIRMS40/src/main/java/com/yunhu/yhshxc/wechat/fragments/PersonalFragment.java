package com.yunhu.yhshxc.wechat.fragments;

import java.io.File;

import gcg.org.debug.JLog;

import org.json.JSONObject;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.SharedPrefrencesForWechatUtil;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 我的
 * @author xuelinlin
 *
 */
public class PersonalFragment extends Fragment {
	private ImageView iv_header;
	private TextView tv_personal_name;
	private TextView tv_zw;
	private TextView tv_bm;
	private TextView tv_gxqm;
	private TextView tv_personal_nicheng;
	private Context context;
	private RelativeLayout ll_touxiang_xiugai;
	private LinearLayout ll_my_focus;
	private LinearLayout ll_history;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private boolean isLocalImg = false;//是否是本地图片
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View v = inflater.inflate(R.layout.wechat_personal, null);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.people)
		.showImageForEmptyUri(R.drawable.people)
		.showImageOnFail(R.drawable.people)
		.cacheInMemory()
		.cacheOnDisc().displayer(new RoundedBitmapDisplayer(10))
		.build();
		initView(v);
		refreshInfo();
		return v;
	}
	private void refreshInfo() {
		String nicheng = SharedPrefrencesForWechatUtil.getInstance(getActivity()).getNicheng();
		tv_personal_nicheng.setText(nicheng);
		String qianming = SharedPrefrencesForWechatUtil.getInstance(getActivity()).getQianMing();
		tv_gxqm.setText(qianming);
		String bumen = SharedPrefrencesForWechatUtil.getInstance(getActivity()).getBuMen();
		tv_bm.setText(bumen);
		String zhiwei = SharedPrefrencesForWechatUtil.getInstance(getActivity()).getZhiWei();
		tv_zw.setText(zhiwei);
		String urlP = SharedPrefrencesForWechatUtil.getInstance(getActivity()).getHeaderName();
		if(!TextUtils.isEmpty(urlP)){
			int index = urlP.lastIndexOf("/");
 			String fileName = urlP.substring(index + 1);
 			String[] str = fileName.split("@");
 			if (str.length>1) {
 				fileName = str[1];
			}
			File f = new File(Constants.WECHAT_PATH_HEADER_LOAD+fileName);
//			if (f.exists()) {
////				imageLoader.displayImage("file://"+Constants.WECHAT_PATH_HEADER_LOAD+fileName, iv_header, options, null);
//// 				iv_header.setTag(Constants.WECHAT_PATH_HEADER_LOAD+fileName);
//			}else{
				imageLoader.displayImage(urlP, iv_header, options, null);
//			}
		}
		
		String url = UrlInfo.weChatUserInfo(getActivity());
		GcgHttpClient.getInstance(getActivity()).post(url, params(),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d("alin", "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							String weChat_user = obj.getString("weChat_user");
							if ("0000".equals(resultcode)) {
								JSONObject objData = new JSONObject(weChat_user);
								if (PublicUtils.isValid(objData, "headImg")) {
									String imageUrl = objData.getString("headImg");
									if (!TextUtils.isEmpty(imageUrl)) {
										if(SharedPrefrencesForWechatUtil.getInstance(getActivity()).getIsChangedReader().equals("1")){
											DiskCacheUtils.removeFromCache(imageUrl, imageLoader.getDiskCache());
											MemoryCacheUtils.removeFromCache(imageUrl, imageLoader.getMemoryCache());
											SharedPrefrencesForWechatUtil.getInstance(getActivity()).setHeaderName(imageUrl);
										}else{
											
										}
										imageLoader.displayImage(imageUrl, iv_header, options, null);
									}
									SharedPrefrencesForWechatUtil.getInstance(getActivity()).setHeaderName(imageUrl);
								}
								String nicheng ="";
								if(PublicUtils.isValid(objData, "nickName")){
									 nicheng = objData.getString("nickName");
								}
								SharedPrefrencesForWechatUtil.getInstance(getActivity()).setNiCheng(nicheng);
								
								String gexingqianm = "";
								if(PublicUtils.isValid(objData, "signature")){
									gexingqianm = objData.getString("signature");
								}
								SharedPrefrencesForWechatUtil.getInstance(getActivity()).setQianMing(gexingqianm);
								
								String bumen = "";
								if(PublicUtils.isValid(objData, "orgName")){
									 bumen = objData.getString("orgName");
								}
								SharedPrefrencesForWechatUtil.getInstance(getActivity()).setBuMen(bumen);
								
								String zhiwei = "";
								if(PublicUtils.isValid(objData, "roleName")){
									 zhiwei = objData.getString("roleName");
								}
								SharedPrefrencesForWechatUtil.getInstance(getActivity()).setZhiWei(zhiwei);
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
//							searchHandler.sendEmptyMessage(3);
							Toast.makeText(getActivity(), R.string.reload_failure, Toast.LENGTH_LONG).show();
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
						Toast.makeText(getActivity(), R.string.reload_failure, Toast.LENGTH_LONG).show();
					}
				});
	}
	private RequestParams params() {
		RequestParams params = new RequestParams();
//		params.put("phoneno", PublicUtils.receivePhoneNO(getActivity()));
		params.put("userId", SharedPreferencesUtil.getInstance(getActivity()).getUserId());
		JLog.d("alin", "params:"+params.toString());
		return params;
	}
	private void initView(View v) {
		iv_header = (ImageView) v.findViewById(R.id.iv_header);
		tv_personal_name = (TextView) v.findViewById(R.id.tv_personal_name);
		tv_personal_nicheng = (TextView) v.findViewById(R.id.tv_personal_nicheng);
		ll_my_focus = (LinearLayout) v.findViewById(R.id.ll_my_focus);
		ll_history = (LinearLayout) v.findViewById(R.id.ll_history);
		ll_my_focus.setVisibility(View.VISIBLE);
		ll_history.setVisibility(View.VISIBLE);
		ll_my_focus.setOnClickListener(listner);
		ll_history.setOnClickListener(listner);
		tv_zw = (TextView) v.findViewById(R.id.tv_zw);
		tv_bm = (TextView) v.findViewById(R.id.tv_bm);
		tv_gxqm = (TextView) v.findViewById(R.id.tv_gxqm);
		ll_touxiang_xiugai = (RelativeLayout) v.findViewById(R.id.ll_touxiang_xiugai);
		ll_touxiang_xiugai.setOnClickListener(listner);
		tv_personal_name.setText(SharedPreferencesUtil.getInstance(getActivity()).getUserName());
		
		
	}
	
	private OnClickListener listner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_touxiang_xiugai:
				goToSetHearder();
				break;
			case R.id.ll_my_focus:
				goToMyFocus();
				break;
			case R.id.ll_history:
				goToHistory();
				break;
			default:
				break;
			}
		}

	};

	private void goToHistory() {
		Intent intent = new Intent(getActivity(),HistoryTopicActivity.class);
		startActivity(intent);
	}

	private void goToMyFocus() {
		Intent intent = new Intent(getActivity(),FocusActivity.class);
		startActivity(intent);
	}
	private void goToSetHearder() {
		Intent intent = new Intent(getActivity(),PersonalInformationActivity.class);
		startActivity(intent);
	}
	@Override
	public void onResume() {
		super.onResume();
		refreshInfo();
	}
}
