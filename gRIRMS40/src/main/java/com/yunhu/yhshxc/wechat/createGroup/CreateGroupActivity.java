package com.yunhu.yhshxc.wechat.createGroup;

import gcg.org.debug.JLog;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.MD5Helper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.wechat.Util.ImageViewUtils;
import com.yunhu.yhshxc.wechat.Util.WechatUtil;
import com.yunhu.yhshxc.wechat.Util.ImageViewUtils.FileType;
import com.yunhu.yhshxc.wechat.bo.Group;
import com.yunhu.yhshxc.wechat.db.GroupDB;
import com.yunhu.yhshxc.wechat.db.GroupUserDB;
import com.yunhu.yhshxc.wechat.view.ChooseView;
import com.yunhu.yhshxc.widget.ToastOrder;
import com.loopj.android.http.RequestParams;

/**
 * 创建群
 * 
 * @author xuelinlin
 * 
 */
public class CreateGroupActivity extends AbsBaseActivity {
	private EditText et_group_name;
	private EditText et_group_infomation;
	private ImageView iv_group_touxiang;

	private TextView tv_create_group_finish;
	private TextView tv_create_group_return;
	private LinearLayout ll_tianjia;
	private LinearLayout ll_group_touxiang;
	private ChooseView chooseView;

	private GroupDB groupDB;
	private GroupUserDB groupUserDB;
	private WechatUtil util;
	private MyProgressDialog progressDialog;
	private Group group;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wechat_create_group);
		File file = ImageViewUtils.createFile(ImageViewUtils.TEMP_IMAGE_DIR,
				FileType.DIRECTORY);

		groupDB = new GroupDB(this);
		groupUserDB = new GroupUserDB(this);
		util = new WechatUtil(this);
		group = new Group();
		initView();
	}

	private void initView() {
		et_group_name = (EditText) findViewById(R.id.et_group_name);
		et_group_infomation = (EditText) findViewById(R.id.et_group_infomation);
		iv_group_touxiang = (ImageView) findViewById(R.id.iv_group_touxiang);
		tv_create_group_finish = (TextView) findViewById(R.id.tv_create_group_finish);
		tv_create_group_return = (TextView) findViewById(R.id.tv_create_group_return);
		tv_create_group_finish.setOnClickListener(listner);
		tv_create_group_return.setOnClickListener(listner);
		ll_tianjia = (LinearLayout) findViewById(R.id.ll_tianjia);
		ll_group_touxiang = (LinearLayout) findViewById(R.id.ll_group_touxiang);
		ll_group_touxiang.setOnClickListener(listner);
		chooseView = new ChooseView(CreateGroupActivity.this);
		ll_tianjia.addView(chooseView.getView());

	}

	private OnClickListener listner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_group_touxiang:// 头像设置
				setHearder();
				break;
			case R.id.tv_create_group_finish:// 创建群
				createGroup();
				break;
			case R.id.tv_create_group_return:// 返回
				finish();
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 创建群
	 */
	private void createGroup() {
		if (TextUtils.isEmpty(et_group_name.getText().toString().trim())) {
			ToastOrder.makeText(CreateGroupActivity.this, R.string.wechat_content85,
					ToastOrder.LENGTH_LONG).show();
			return;
		} else {
			group.setGroupName(et_group_name.getText().toString().trim());
		}
		String instruction = !TextUtils.isEmpty(et_group_infomation.getText()
				.toString()) ? et_group_infomation.getText().toString() : "";

		group.setExplain(instruction);
		// 部门
		int type = chooseView.getType();
//		if (type == 0)
			group.setType(type);
		// 职位
		List<OrgUser> orgList = chooseView.getOrgUsersZW();
//		if (orgList.size() == 0)
			
		StringBuffer sb = new StringBuffer();
		int roleIds = SharedPreferencesUtil.getInstance(this).getRoleId();
		boolean isExist = false;
		for (int i = 0; i < orgList.size(); i++) {
			OrgUser o = orgList.get(i);
			sb.append(o.getRoleId()).append(",");
			if (orgList.get(i).getRoleId() == roleIds) {
				isExist = true;
			}
		}
		if (!isExist) {
			group.setGroupUser(String.valueOf(SharedPreferencesUtil
					.getInstance(this).getUserId()));
		}
		group.setGroupRole(sb.toString());
		// 添加人
		List<OrgUser> orgListPer = chooseView.getOrgUsers();
		if (orgListPer.size() == 0&&orgList.size() == 0&&type == 0) {
			ToastOrder.makeText(CreateGroupActivity.this, R.string.wechat_content40,
					ToastOrder.LENGTH_LONG).show();
			return;
		}
		StringBuffer sbPer = new StringBuffer();
		for (int i = 0; i < orgListPer.size(); i++) {
			OrgUser o = orgListPer.get(i);
			sbPer.append(o.getUserId()).append(",");
		}
		int userId = SharedPreferencesUtil.getInstance(this).getUserId();
		sbPer.append(userId);
		group.setGroupUser(sbPer.toString());
		group.setOrgId(String.valueOf(SharedPreferencesUtil.getInstance(this)
				.getOrgId()));
		try {
			submit1(group);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void submit1(final Group group) throws JSONException {
		progressDialog = new MyProgressDialog(CreateGroupActivity.this,
				R.style.CustomProgressDialog, getResources().getString(
						R.string.str_wechat_submit));
		progressDialog.show();
		String url = UrlInfo.doWebChatGroupInfo(this);
		GcgHttpClient.getInstance(this).post(url, params(group),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d("alin", "content:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							String resultcode = obj.getString("resultcode");
							if ("0000".equals(resultcode)) {
								submitPhotoBackground(group, group.getLogo());
								SubmitWorkManager.getInstance(
										CreateGroupActivity.this).commit();
								ToastOrder.makeText(CreateGroupActivity.this,
										R.string.wechat_content84, ToastOrder.LENGTH_LONG).show();
								if (progressDialog != null
										&& progressDialog.isShowing()) {
									progressDialog.dismiss();
								}
								CreateGroupActivity.this.finish();
							} else if ("0005".equals(resultcode)) {
								ToastOrder.makeText(CreateGroupActivity.this,
										R.string.wechat_content83, ToastOrder.LENGTH_LONG)
										.show();
							} else {
								throw new Exception();
							}
						} catch (Exception e) {
							if (progressDialog != null
									&& progressDialog.isShowing()) {
								progressDialog.dismiss();
							}
							ToastOrder.makeText(CreateGroupActivity.this, R.string.wechat_content82,
									ToastOrder.LENGTH_LONG).show();
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {
						if (progressDialog != null
								&& progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						JLog.d("alin", "failure");
						if (progressDialog != null
								&& progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						ToastOrder.makeText(CreateGroupActivity.this, R.string.wechat_content82,
								ToastOrder.LENGTH_LONG).show();
					}
				});
	}

	private RequestParams params(Group group) {
		RequestParams params = new RequestParams();
		params.put("phoneno", PublicUtils.receivePhoneNO(this));
		try {
			String data = util.submitGroupJson(group);
			params.put("data", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JLog.d("alin", "params:" + params.toString());
		return params;
	}

	/**
	 * 提交群头像图片
	 * 
	 * @param name
	 */
	private void submitPhotoBackground(Group group, String name) {
		PendingRequestVO vo = new PendingRequestVO();
		vo.setContent(PublicUtils.getResourceString(CreateGroupActivity.this,R.string.web_chat)+":" + group.getGroupId());
		vo.setTitle(PublicUtils.getResourceString(CreateGroupActivity.this,R.string.wechat_content81));
		vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
		vo.setType(TablePending.TYPE_IMAGE);
		vo.setUrl(UrlInfo.getUrlPhoto(CreateGroupActivity.this));
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put(
				"companyid",
				String.valueOf(SharedPreferencesUtil.getInstance(
						CreateGroupActivity.this).getCompanyId()));
		params.put("md5Code",
				MD5Helper.getMD5Checksum2(Constants.WECHAT_PATH + name));
		vo.setParams(params);
		vo.setImagePath(Constants.WECHAT_PATH + name);
		SubmitWorkManager.getInstance(CreateGroupActivity.this)
				.performImageUpload(vo);
	}

	/**
	 * 头像设置
	 */
	private void setHearder() {
		View contentView = View.inflate(CreateGroupActivity.this,
				R.layout.wechat_touxiangxuanze, null);
		Button quertBtn = (Button) contentView
				.findViewById(R.id.btn_nearby_visit_store_info_check);
		Button updateBtn = (Button) contentView
				.findViewById(R.id.btn_nearby_visit_store_info_update);
		Button cancelBtn = (Button) contentView
				.findViewById(R.id.btn_nearby_visit_store_info_cancel);

		final PopupWindow popupWindow = new PopupWindow(contentView,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.popupwindow);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();// 更新弹出窗口的状态
		popupWindow.setBackgroundDrawable(new BitmapDrawable());// 设置一个默认的背景
		popupWindow.showAtLocation(
				(CreateGroupActivity.this).findViewById(R.id.ll_nearby_bottom),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		quertBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				camera(v);
				popupWindow.dismiss();
			}
		});
		updateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gallery(v);
				popupWindow.dismiss();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});

	}

	/**
	 * 从相册获取
	 */
	public void gallery(View view) {
		// 激活系统图库，选择一张图片
		ImageViewUtils.GoAlbum(this);
	}

	/**
	 * 从相机获取
	 */
	public void camera(View view) {
		ImageViewUtils.GoCaptureAndSavePicture(this, "aa.jpg");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ImageViewUtils.INTENT_PICTURE
				&& resultCode == RESULT_OK) {

			String name = System.currentTimeMillis() + ".jpg";
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
				ImageViewUtils.GoCropAndSave(this,
						new File(ImageViewUtils.getPathHigh(this, data)), name);
			} else {
				ImageViewUtils.GoCropAndSave(this,
						new File(ImageViewUtils.getPathLow(this, data)), name);
			}
			group.setLogo(name);
		}
		if (requestCode == ImageViewUtils.INTENT_CAPTURE
				&& resultCode == RESULT_OK) {
			String name = System.currentTimeMillis() + ".jpg";
			ImageViewUtils.GoCropAndSave(this,
					new File(ImageViewUtils.imageUri.getPath()), name);
			group.setLogo(name);
			JLog.d("alin", "nameCamera = " + name);
		}
		if (requestCode == ImageViewUtils.INTENT_CROP
				&& resultCode == RESULT_OK) {
			iv_group_touxiang.setImageBitmap(ImageViewUtils.getBitmapFromUri(
					ImageViewUtils.imageUri, this));
			// JLog.d("alin","path = "+Constants.WECHAT_PATH+PHOTO_FILE_NAME);
		}
		et_group_name.setFocusableInTouchMode(true);
		super.onActivityResult(requestCode, resultCode, data);
	}

	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	private void send(String tags) {
		String url = "http://219.148.162.89:9090/com.grandison.grirms.phone.web-1.0.0/jpushMessageInfo.do";
		GcgHttpClient.getInstance(this).post(url, params("", tags),
				new HttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						JLog.d("jishen", "onSuccess:" + content);
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailure(Throwable error, String content) {
						JLog.d("jishen", "onFailure:" + content);
					}
				});
	}

	private RequestParams params(String ids, String tags) {
		RequestParams params = new RequestParams();
		params.put("registrationIds", ids);
		params.put("tags", tags);
		params.put("title", "标题");
		params.put("alertContent", "AertContent内容");
		return params;
	}
}
