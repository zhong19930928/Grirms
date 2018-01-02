package com.yunhu.yhshxc.http.submit;

import gcg.org.debug.JDebugOptions;
import gcg.org.debug.JLog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.exception.HTTPResponseResultException;
import com.yunhu.yhshxc.func.StoreExpandUtil;
import com.yunhu.yhshxc.http.CoreHttpHelper;
import com.yunhu.yhshxc.listener.SubmitDataListener;
import com.yunhu.yhshxc.submitManager.bo.PendingRequestVO;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.submitManager.core.SubmitWorkManager;
import com.yunhu.yhshxc.utility.ApplicationHelper;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.HttpHelper;
import com.yunhu.yhshxc.utility.MD5Helper;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

/**
 * 此类将保存数据提交到后台提交模块当中
 * 
 * @author david.hou
 * 
 */
public class SubmitManager {

	private final String TAG = "SubmitService";
	// private final String COMPANY_ID = "companyId"; // 公司ID
	private final String PLAN_ID = "planId"; // 拜访计划ID
	// private final String USER_ID = "userId"; // 用户ID
	private final String WAY_ID = "routeId"; // 线路ID
	private final String WAY_NAME = "routeName"; // 线路名称
	private final String STORE_ID = "storeId"; // 店面ID
	private final String STORE_NAME = "storeName"; // 店面名称
	private final String TARGET_ID = "tableId"; // 模块ID或任务ID
	private final String TIMESTAMP = "patchId"; // 时间戳
	private final String CHECKINGIS = "checkinGis";
	private final String CHECKINTIME = "checkinTime";
	private final String CHECKOUTGIS = "checkoutGis";
	private final String CHECKOUTTIME = "checkoutTime";
	private final String MODTYPE = "type";
	private final String DOUBLEUSER = "doubleUser";// 下发User
	private final String DOUBLE_ID = "doubleId"; // taskid

	private final String COMPANY_ID = "companyid";
	private final String UP_CTRL_MAIN = "up_ctrl_main";
	private final String UP_CTRL_TABLE = "up_ctrl_table";
	private final String IS_CODE_UPDATE = "is_code_update";
	private final String IS_CODE_UPDATE_TAB = "is_code_update_tab";
	private final String COMPARE_RULE_IDS = "compare_rule_ids";
	private final String COMPARE_CONTENT = "compare_content";
	private final String STORE_VISIT_NUMBERS = "submit_count";
	private final String DOUBLE_MASTER_TASK_NO = "doubleMasterTaskNo";
	private final String DOUBLE_BTN_TYPE = "doubleBtnType";

	private SubmitDB submitDB = null;
	private SubmitItemDB submitItemDB = null;

	private static SubmitManager mInstance = null;

	private static Context context = null;

	private SubmitDataListener submitDataListener = null;
	private boolean isBackstageSubmit = true;
	private Dialog initUiDialog;//初始化ui进度条

	private SubmitManager() {	
	}

	public static SubmitManager getInstance(Context mContext) {
		if (mInstance == null) {
			mInstance = new SubmitManager();
		}
		context = mContext;
		return mInstance;
	}

	// 所有数据全部提交完毕停止服务.
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(initUiDialog !=null && initUiDialog.isShowing()){
				initUiDialog.dismiss();
			}
			//initUiDialog = null;
			switch(msg.what){
			case 0:
				submitDataListener.submitReceive(true);
				break;
			case 1:
				if(isBackstageSubmit){
					// 开启后台提交服务
					SubmitWorkManager.getInstance(context.getApplicationContext()).commit();
				}
				submitDataListener.submitReceive(true);
				break;
			case 2:
				submitFail((String)msg.obj);
				break;
			case 3:
				ToastOrder.makeText(context, (String)msg.obj, ToastOrder.LENGTH_SHORT).show();
				submitDataListener.submitReceive(false);
				break;
			}
			
			super.handleMessage(msg);
		}
	};

	/**
	 * 查询所有要上传的数据.并且上传.
	 */
	private void submitData() {
		initUiDialog = new MyProgressDialog(context,R.style.CustomProgressDialog,context.getResources().getString(R.string.submint_loding));
		if (!isBackstageSubmit) {//如果是后台提交的话就不展示提示框
			initUiDialog.show();
		}
		submitDB = new SubmitDB(context);
		submitItemDB = new SubmitItemDB(context);
		new Thread("SubmitManager-1") {
			@Override
			public void run() {
				// 根据数据库中SUBMIT表里state字段查询该表所有数据
				List<Submit> submitList = submitDB.findAllSubmitDataByState(Submit.STATE_SUBMITED);
				JLog.d(TAG, "要上传的数据====>" + submitList.size());

				// 如何shopContentList为null,说明所有数据全部提交完毕并且已经checkout,可以结束service
				if (submitList.isEmpty()) {
					mHandler.sendEmptyMessage(0);
					JLog.d(TAG, "AutoSubmitService_checkOutListdata => null");
				} else {

					try {
						// 遍历submitList
						for (Submit submit : submitList) {
							exeUpload(submit);
//							// 删除表
//							submitItemDB.deleteSubmitData(submit.getId());
						}
						
						//要先设置完数据再删除，否则后台提交管理模块中超链接无法查到属于哪个模块 toVo方法中超链接设置title
						for (Submit submit : submitList) {
							// 删除表
							
							submitItemDB.deleteSubmitData(submit.getId());
						}
						mHandler.sendEmptyMessage(1);
					} catch (HTTPResponseResultException e) {
						mHandler.obtainMessage(2, e.getMessage()).sendToTarget();
					} catch (JSONException e) {
						JLog.e(e);
						mHandler.obtainMessage(2, context.getResources().getString(R.string.helpinstruc_message05)).sendToTarget();
					} catch (Exception e) {
						JLog.e(e);
						mHandler.obtainMessage(3, context.getResources().getString(R.string.ERROR_NETWORK)).sendToTarget();
					}
				}

			}
		}.start();
		
	}

	/**
	 * 提交数据，会根据isBackstageSubmit决定是否后台提交
	 * 
	 * @param isBackstageSubmit
	 *            是否要后台提交
	 * @param listener
	 *            无论是否后台提交，都会回调
	 */
	public void submitData(boolean isBackstageSubmit,
			SubmitDataListener listener) {
		this.submitDataListener = listener;
		this.isBackstageSubmit = isBackstageSubmit;
		submitData();
	}

	private void submitFail(String message) {
		final Dialog dialog = new Dialog(context, R.style.transparentDialog);
		View view = View.inflate(context, R.layout.submit_fail_dialog, null);
		TextView tipContent = (TextView) view
				.findViewById(R.id.tv_submit_content);
		Button again = (Button) view.findViewById(R.id.btn_submit_again);
		Button cancel = (Button) view.findViewById(R.id.btn_submit_cancel);
		tipContent.setText(message);
		again.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				submitData();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(submitDataListener != null){
					submitDataListener.submitReceive(false);
				}
			}
		});
		dialog.setContentView(view);
		dialog.setCancelable(false);// 将对话框设置为模态
		dialog.show();
	}

	/**
	 * 处理直接提交数据的返回值
	 * 
	 * @param result
	 * @throws HTTPResponseResultException
	 * @throws JSONException
	 */
	private void returned(String result) throws HTTPResponseResultException,
			JSONException {
		if (TextUtils.isEmpty(result)) {
			throw new HTTPResponseResultException(context.getResources().getString(R.string.submitmanager_message01));
		}
		JLog.d(JDebugOptions.TAG_SUBMIT,context.getResources().getString(R.string.submitmanager_message02)+result);
		JSONObject json = new JSONObject(result);
		String code = json.getString(Constants.RESULT_CODE);
		if (code.equals(Constants.RESULT_CODE_SUCCESS)) {
			return;
		} else if (code.equals(Constants.RESULT_CODE_FAILURE)) {
			throw new HTTPResponseResultException(context.getResources().getString(R.string.submitmanager_message03), code);
		} else if (code.equals(Constants.RESULT_CODE_NO_REGISTER)) {
			throw new HTTPResponseResultException(context.getResources().getString(R.string.submitmanager_message04), code);
		} else {
			throw new HTTPResponseResultException(context.getResources().getString(R.string.submitmanager_message05));
		}
	}

	/**
	 * 使用后台或直接提交数据
	 * 

	 * @throws JSONException
	 * @throws HTTPResponseResultException
	 * @throws Exception
	 */
	private void submit(PendingRequestVO vo)
			throws HTTPResponseResultException, JSONException {

		SubmitWorkManager manager = SubmitWorkManager
				.getInstance(context.getApplicationContext());
		if (isBackstageSubmit) {
			// 后台提交
			manager.performJustSubmit(vo);
		} else {
			// 直接提交
			String url = vo.getUrl();
			Map<String, String> params = vo.getParams();
			if (params != null) {
				String apkversion = ApplicationHelper.getApplicationName(context).appVersionCode;
				params.put("apkversion", apkversion);
//				Log.d("888", "apkversion=" + apkversion);
			}
			JLog.d(JDebugOptions.TAG_SUBMIT, "直接提交"+url);
			String result = new HttpHelper(context).connectPost(url, params);
			returned(result);
		}
	}

	/**
	 * 使用后台或直接上传图片
	 * 

	 * @throws Exception
	 */
	private void upload(PendingRequestVO vo) throws HTTPResponseResultException, JSONException,Exception {
		
		if (isBackstageSubmit) {
			// 后台提交
			SubmitWorkManager.getInstance(context.getApplicationContext()).performImageUpload(vo);
		} else {
			// 直接提交
			String url = vo.getUrl();
			Map<String, String> params = vo.getParams();
			
			HashMap<String, String> files = new HashMap<String, String>();
			files.put(SubmitWorkManager.FILE_KEY, vo.getImagePath());
			JLog.d(JDebugOptions.TAG_SUBMIT, "直接提交"+url);
			String result = new HttpHelper(context).uploadFile(url, params,files);
			returned(result);
		}
	}
	
	/**
	 * 使用后台或直接上传视频
	 * 

	 * @throws Exception
	 */
	private void uploadVideo(PendingRequestVO vo) throws HTTPResponseResultException, JSONException,Exception {
		
		if (isBackstageSubmit) {
			// 后台提交
			SubmitWorkManager.getInstance(context.getApplicationContext()).performFileUpload(vo);
		} else {
			// 直接提交
			if(vo !=null){
				String url = vo.getUrl();
				Map<String, String> params = vo.getParams();
				
				HashMap<String, String> files = new HashMap<String, String>();
				files.put(SubmitWorkManager.FILE_KEY, vo.getImagePath());
				JLog.d(JDebugOptions.TAG_SUBMIT, "直接提交"+url);
				String result = new HttpHelper(context).uploadFile(url, params,files);
				returned(result);
			}
			
		}
	}
	
	
	
	private PendingRequestVO toVo(String url,HashMap<String, String> params,Submit submit){
		PendingRequestVO vo = new PendingRequestVO();
		vo.setMethodType(SubmitWorkManager.HTTP_METHOD_POST);
		vo.setUrl(url);
		vo.setParams(params);
		vo.setType(TablePending.TYPE_DATA);
		if(submit.getTargetType() == Menu.TYPE_VISIT){
			Menu menu = new MainMenuDB(context).findMenuListByMenuType(Menu.TYPE_VISIT);
			vo.setTitle(menu!=null?menu.getName():"");
		}else if(submit.getTargetType() == Menu.TYPE_MODULE || submit.getTargetType() == Menu.TYPE_NEW_TARGET || submit.getTargetType() == Menu.TYPE_NEARBY){
			if (submit.getParentId()!=0) {//说明是超链接
				Submit parentSubmit = findParentSubmit(submit.getParentId());
				if (parentSubmit!=null && parentSubmit.getPlanId()!=null && parentSubmit.getPlanId()!=0) {//拜访
					Menu menu = new MainMenuDB(context).findMenuListByMenuType(Menu.TYPE_VISIT);
					vo.setTitle(menu!=null?menu.getName():"");
				}else{//自定义
					if(null!=parentSubmit){
						Menu menu = new MainMenuDB(context).findMenuListByMenuId(parentSubmit.getTargetid());
						vo.setTitle(menu!=null?menu.getName():"");
					}
				}
			}else{
				Menu menu = new MainMenuDB(context).findMenuListByMenuId(submit.getTargetid());
				vo.setTitle(menu!=null?menu.getName():"");
			}
		}else if(submit.getTargetType() == Menu.TYPE_ATTENDANCE || submit.getTargetType() == Menu.TYPE_NEW_ATTENDANCE){
			Menu menu = new MainMenuDB(context).findMenuListByMenuType(submit.getTargetType());
			vo.setTitle(menu!=null?menu.getName():"");
		}else if(submit.getTargetType() == Menu.TYPE_ORDER3){
			vo.setTitle(context.getResources().getString(R.string.order_number));
		}
			
		vo.setContent(submit.getContentDescription());
		return vo;
	}
	
	private PendingRequestVO toVoForSubmit(HashMap<String, String> params,Submit submit){
		for (Iterator<Entry<String, String>> iterator = params.entrySet()
				.iterator(); iterator.hasNext();) {
			Entry<String, String> entry = (Entry<String, String>) iterator
					.next();
			JLog.d(TAG," name=>" + entry.getKey() + " value=>"+ entry.getValue());

		}
		return toVo(UrlInfo.getUrlSubmit(context), params, submit);
	}
	
	private PendingRequestVO toVoForImage(HashMap<String, String> params,String name, String funcId,Submit submit){
		JLog.d(TAG, "imgName==>" + Constants.SDCARD_PATH + name);
		PendingRequestVO vo = toVo(UrlInfo.getUrlPhoto(context), params, submit);
		vo.setImagePath(Constants.SDCARD_PATH + name);
		vo.setType(TablePending.TYPE_IMAGE);
//		vo.setContent(vo.getContent()+"-"+imageName);
		Func func = findFunc(Integer.valueOf(funcId));
		if (func !=null) {
			String content = vo.getContent();
			if (TextUtils.isEmpty(content)) {
				vo.setContent(func.getName());
			}else{
				vo.setContent(content+"-"+func.getName());
			}
		}
		File file = new File(vo.getImagePath());
		if (file.exists()) {
			// 文件大于300M设置为大图片
			if (file.length() > 300000) {
				vo.setType(TablePending.TYPE_LARGE_IMAGE);
			}
		}
		return vo;
	}
	
	private PendingRequestVO toVoForAttendandceImage(HashMap<String, String> params,String imageName,Submit submit){
		PendingRequestVO vo = toVo(UrlInfo.getUrlAttendandceImage(context), params, submit);
		vo.setImagePath(Constants.SDCARD_PATH + imageName);
		vo.setType(TablePending.TYPE_IMAGE);
//		vo.setContent(vo.getContent()+"-"+imageName);
		vo.setContent(vo.getContent()+"-"+context.getResources().getString(R.string.take_picture));

		return vo;
	}
	
	private PendingRequestVO toVoForAudio(HashMap<String, String> params,String imageName, String funcId,Submit submit){
		JLog.d(TAG, "recordName==>" + Constants.RECORD_PATH + imageName);
		PendingRequestVO vo = toVo(UrlInfo.getUrlRecord(context), params, submit);
		vo.setImagePath(Constants.RECORD_PATH + imageName);
		vo.setType(TablePending.TYPE_AUDIO);
//		vo.setContent(vo.getContent()+"-"+fileName);
		Func func = findFunc(Integer.parseInt(funcId));
		if (func !=null) {
			vo.setContent(vo.getContent()+"-"+func.getName());
		}
		return vo;
	}
	
	private PendingRequestVO toVoForVideo(HashMap<String, String> params,String imageName, String funcId,Submit submit){
		JLog.d(TAG, "videName==>" + Constants.VIDEO_PATH + imageName);
		PendingRequestVO vo = toVo(UrlInfo.uploadHelpFileInfo(context), params, submit);
		vo.setImagePath(Constants.VIDEO_PATH + imageName);
		vo.setType(TablePending.TYPE_FILE);
//		vo.setContent(vo.getContent()+"-"+fileName);
		Func func = findFunc(Integer.parseInt(funcId));
		if (func !=null) {
			vo.setContent(vo.getContent()+"-"+func.getName());
		}
		return vo;
	}
	
	private PendingRequestVO toVoForAttendandce(HashMap<String, String> params,Submit submit){
		return toVo(UrlInfo.getUrlAttendance(context), params, submit);
	}
	
	private PendingRequestVO toVoForNewAttendandce(HashMap<String, String> params,Submit submit){
		return toVo(UrlInfo.getUrlNewAttendance(context), params, submit);
	}
	private PendingRequestVO toVoForNewAttendandceGang(HashMap<String, String> params,Submit submit){
		return toVo(UrlInfo.getUrlNewAttendanceGang(context), params, submit);
	}

	/**
	 * 按类型执行上传
	 * 
	 * @throws Exception
	 */
	private synchronized void exeUpload(Submit submit) throws Exception {
		HashMap<String, String> params;
		// 根据数据空中SUBMIT_ITEM表里submit_id字段查询该表说有数据
		List<SubmitItem> submitItems = submitItemDB.findSubmitItemBySubmitId(submit.getId());
		// 如果submitItems是空说明SUBMIT_ITEM表里没有数据
		if (submitItems.isEmpty()) {
			// 根据submitId删除SUBMIT表中数据
			// 发起网络连接
			params = composeParameter(submit);
			submit(toVoForSubmit(params,submit));
			JLog.d(TAG, "SubmitItems是空！");
			// 有数据的话就讲所有数据添加到HashMap里
		} else if (submit.getTargetType() == Menu.TYPE_ATTENDANCE) {// 考勤信息上传
			JLog.d(TAG, "-------------提交考勤表单------------------------");
			if (submitItems != null) {
				params = new HashMap<String, String>();
				for (SubmitItem submitItem : submitItems) {
					if (submitItem.getType() == Func.TYPE_CAMERA
							|| submitItem.getType() == Func.TYPE_CAMERA_HEIGHT
							|| submitItem.getType() == Func.TYPE_CAMERA_MIDDLE
							|| submitItem.getType() == Func.TYPE_CAMERA_CUSTOM) {
						String name = submitItem.getParamValue();
						if (TextUtils.isEmpty(name))
							continue;
						HashMap<String, String> param = new HashMap<String, String>();
						param.put("name", name);
						param.put(COMPANY_ID, String.valueOf(SharedPreferencesUtil.getInstance(context).getCompanyId()));
						param.put("md5Code", MD5Helper.getMD5Checksum2(Constants.SDCARD_PATH + name));
						upload(toVoForAttendandceImage(param,name,submit));
					}
					params.put(submitItem.getParamName(),submitItem.getParamValue());
					JLog.d(TAG, "PARAMS ---->" + submitItem.getParamName()+ "  ===>" + submitItem.getParamValue());
				}
				submit(toVoForAttendandce(params,submit));
				JLog.d(TAG, "-------------提交考勤结束------------------------");
			}
		} else if (submit.getTargetType() == Menu.TYPE_NEW_ATTENDANCE) {// 新考勤信息上传
			JLog.d(TAG, "-------------提交新考勤表单------------------------");
			if (submitItems != null) {
				boolean flag = false;
				params = new HashMap<String, String>();
				for (SubmitItem submitItem : submitItems) {
					if (submitItem.getType() == Func.TYPE_CAMERA
							|| submitItem.getType() == Func.TYPE_CAMERA_HEIGHT
							|| submitItem.getType() == Func.TYPE_CAMERA_MIDDLE
							|| submitItem.getType() == Func.TYPE_CAMERA_CUSTOM) {
						String name = submitItem.getParamValue();
						if (TextUtils.isEmpty(name))
							continue;
						HashMap<String, String> param = new HashMap<String, String>();
						param.put("name", name);
						param.put(COMPANY_ID, String.valueOf(SharedPreferencesUtil.getInstance(context).getCompanyId()));
						param.put("md5Code", MD5Helper.getMD5Checksum2(Constants.SDCARD_PATH + name));
//						params.put("md5CodeTest","test");
						upload(toVoForAttendandceImage(param,name,submit));
					}
					params.put(submitItem.getParamName(),submitItem.getParamValue());
					JLog.d(TAG, "PARAMS ---->" + submitItem.getParamName()+ "  ===>" + submitItem.getParamValue());
					if(submitItem.getParamName().equals("type")&&submitItem.getParamValue().equals("gang")){
						flag = true;
					}
				}
				
				
				if(flag){
					submit(toVoForNewAttendandceGang(params,submit));
					
					String over_intime = params.get("over_intime");
					SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//2016-08-23 09:13:57
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(d.parse(over_intime));
					checkGangVisibility(calendar);
					
				}else{
					submit(toVoForNewAttendandce(params,submit));	
				}
				JLog.d(TAG, "-------------提交新考勤结束------------------------");
			}
		} else {
			params = composeParameter(submit); // submit表中数据转成提交参数
			List<String> imgNameList = new ArrayList<String>(); // 缓存图片名字，以便另外提交图片
			List<String> recordList = new ArrayList<String>(); // 缓存录音名字，以便另外提交录音
			List<String> videoList = new ArrayList<String>(); // 缓存视频名字，以便另外提交视频
			List<String> attachmentList = new ArrayList<String>(); // 缓存附件名字，以便另外提交附件

			
			
			//后台提交管理页面显示使用
			List<SubmitItem> imageItemList = new ArrayList<SubmitItem>();
			List<SubmitItem> recordItemList = new ArrayList<SubmitItem>();
			List<SubmitItem> videoItemList = new ArrayList<SubmitItem>();
			List<SubmitItem> attachmentItemList = new ArrayList<SubmitItem>();

			// 遍历所有submitItems中的数据
			for (SubmitItem submitItem : submitItems) {
				
				/*********************/
				if(submitItem.getIsCacheFun()==1){
					submit.setIsCacheFun(1);
					new SubmitDB(context).updateSubmit(submit);
				}
				/*********************/
				
				
				
				//店面拓展模块的话手机端自己添加的定位控件数据不提交到后台
				if (String.valueOf(StoreExpandUtil.STORE_EXPAND_LOCATION_ID).equals(submitItem.getParamName())) {
					continue;
				}
				// submitItem表中的数据转成提交参数
				params.put(submitItem.getParamName(), submitItem
						.getParamValue() != null ? submitItem.getParamValue()
						: "");
				// 判断是否是照片类型，缓存图片名字，以便另外提交图片
				if (submitItem.getType() == Func.TYPE_CAMERA
						|| submitItem.getType() == Func.TYPE_CAMERA_HEIGHT
						|| submitItem.getType() == Func.TYPE_CAMERA_MIDDLE
						|| submitItem.getType() == Func.TYPE_CAMERA_CUSTOM||submitItem.getType()==Func.TYPE_SIGNATURE) {
					imgNameList.add(submitItem.getParamValue());
					imageItemList.add(submitItem);
				}
				
				if (submitItem.getType() == Func.TYPE_ATTACHMENT) {
					attachmentList.add(submitItem.getParamValue());
					attachmentItemList.add(submitItem);
				}
				// 判断是否是录音类型，缓存录音名字，以便另外提交录音
				if (submitItem.getType() == Func.TYPE_RECORD) {
					recordList.add(submitItem.getParamValue());
					recordItemList.add(submitItem);
				}
				
				// 判断是否是视频类型，缓存视频名字，以便另外提交视频
				if (submitItem.getType() == Func.TYPE_VIDEO) {
					videoList.add(submitItem.getParamValue());
					videoItemList.add(submitItem);
				}
				
				// 表格控件中的图片缓存
				if (submitItem.getType() == Func.TYPE_TABLECOMP) {
					String imageList = submitItem.getNote(); // 表格中的图片
					if (!TextUtils.isEmpty(imageList)) {
						String[] str = imageList.split(",");
						for (int i = 0; i < str.length; i++) {
							String src = str[i];
							if (!TextUtils.isEmpty(src) && src.endsWith(".jpg")) {
								imgNameList.add(src);
								imageItemList.add(submitItem);
							}
						}
					}

				}
			}
			JLog.d(TAG, "************提交表单**************");
			JLog.d(TAG, "-------------上传图片------------------------");
			int i = 0;
			for (String name : imgNameList) {
				if(!name.startsWith("http")){
					HashMap<String, String> param = new HashMap<String, String>();

					param.put("name", name);

					param.put("targetid", submit.getTargetid() + "");

					File file = new File(Constants.SDCARD_PATH + name);

					if (!file.exists()) {

						File fileTemp = new File(Constants.PATH_TEMP);

						if (fileTemp.exists()) {

							File fileTempPhoto = new File(Constants.PATH_TEMP
									+ name);

							if (fileTempPhoto.exists()) {

								param.put("md5Code", MD5Helper
										.getMD5Checksum2(Constants.SDCARD_PATH
												+ name));

								param.put(COMPANY_ID, String
										.valueOf(SharedPreferencesUtil.getInstance(
												context).getCompanyId()));

								upload(toVoForImage(param, name,
										imageItemList.get(i).getParamName(), submit));

							}

						}

					} else {

						param.put(
								"md5Code",
								MD5Helper.getMD5Checksum2(Constants.SDCARD_PATH
										+ name));

						param.put(
								COMPANY_ID,
								String.valueOf(SharedPreferencesUtil.getInstance(
										context).getCompanyId()));

						upload(toVoForImage(param, name, imageItemList.get(i)
								.getParamName(), submit));

					}
				}

				i++;}
			JLog.d(TAG, "-------------上传结束------------------------");
			JLog.d(TAG, "-------------上传录音------------------------");
			int j = 0;
			for (String name : recordList) {
				if(!name.startsWith("http")){
					HashMap<String, String> param = new HashMap<String, String>();
					param.put("name", name);
					param.put("targetid", submit.getTargetid() + "");
					param.put(COMPANY_ID,String.valueOf(SharedPreferencesUtil.getInstance(context).getCompanyId()));
					upload(toVoForAudio(param,name,recordItemList.get(j).getParamName(),submit));
				}
				j++;
			}
			JLog.d(TAG, "-------------上传结束------------------------");
			
			JLog.d(TAG, "-------------上传视频------------------------");
			int k = 0;
			for (String name : videoList) {
//				HashMap<String, String> param = new HashMap<String, String>();
//				param.put("cid", String.valueOf(SharedPreferencesUtil.getInstance(context).getCompanyId()));
//				param.put("name", name);
//				param.put("fip", "help.gcgcloud.com");
//				uploadVideo(toVoForVideo(param,name,recordItemList.get(k).getParamName(),submit));
//				k++;
				if(!name.startsWith("http")){
					
					PendingRequestVO vo = new PendingRequestVO();
					Func func = findFunc(Integer.parseInt(videoItemList.get(k).getParamName()));
					if (func !=null) {
						vo.setContent(func.getName()+"-"+name);
						vo.setTitle(func.getName());
						
					}
					vo.addFiles("name", Constants.VIDEO_PATH+name);	
					HashMap<String, String> param = new HashMap<String, String>();
					param.put("cid", String.valueOf(SharedPreferencesUtil.getInstance(context).getCompanyId()));
					param.put("name", name);
					param.put("fip", "help.gcgcloud.com");
					vo.setParams(param);
					new CoreHttpHelper().performWehatUpload(context, vo);
				}
				k++;
			}
			JLog.d(TAG, "-------------上传结束------------------------");
			
			JLog.d(TAG, "-------------上传附件------------------------");
			int p = 0;
			for (String name : attachmentList) {
				if(!name.startsWith("http")){
					PendingRequestVO vo = new PendingRequestVO();
					Func func = findFunc(Integer.parseInt(attachmentItemList.get(p).getParamName()));
					if (func !=null) {
						vo.setContent(func.getName()+"-"+name);
						vo.setTitle(func.getName());
						
					}
					vo.addFiles("name", Constants.FUNC_ATTACHMENT_PATH+name);	
					HashMap<String, String> param = new HashMap<String, String>();
					param.put("cid", String.valueOf(SharedPreferencesUtil.getInstance(context).getCompanyId()));
					param.put("name", name);
					param.put("fip", "help.gcgcloud.com");
					vo.setParams(param);
					new CoreHttpHelper().performWehatUpload(context, vo);
				}
				p++;
			}
			JLog.d(TAG, "-------------上传附件------------------------");


			// 发起网络连接
			submit(toVoForSubmit(params,submit));
			JLog.d(TAG, "************提交结束*************");
		}

	}

	/**
	 * 将遍历出来的数据加入到HashMap中
	 * 
	 * @param submit
	 * @return
	 */
	private synchronized HashMap<String, String> composeParameter(Submit submit) {
		HashMap<String, String> params = new HashMap<String, String>();
		// params.put(COMPANY_ID,
		// String.valueOf(SharedPreferencesUtil.getInstance(this).getCompanyId()));
		// params.put(USER_ID,
		// String.valueOf(SharedPreferencesUtil.getInstance(this).getUserId()));
		if (submit.getPlanId() != null) {
			params.put(PLAN_ID, submit.getPlanId().toString());
		}
		if (submit.getDoubleId() != null) {
			params.put(DOUBLE_ID, submit.getDoubleId().toString());
		}
		if (!TextUtils.isEmpty(submit.getSendUserId())) {
			params.put(DOUBLEUSER, submit.getSendUserId());
		}
		if (submit.getWayId() != null) {
			params.put(WAY_ID, submit.getWayId().toString());
		}
		if (!TextUtils.isEmpty(submit.getWayName())) {
			params.put(WAY_NAME, submit.getWayName());
		}
		if (submit.getStoreId() != null) {
			params.put(STORE_ID, submit.getStoreId().toString());
		}
		if (!TextUtils.isEmpty(submit.getStoreName())) {
			params.put(STORE_NAME, submit.getStoreName());
		}
		if (submit.getTargetid() != null) {
			params.put(TARGET_ID, submit.getTargetid().toString());
		}
		if (!TextUtils.isEmpty(submit.getTimestamp())) {
			params.put(TIMESTAMP, submit.getTimestamp());
		}
		if (!TextUtils.isEmpty(submit.getCheckinGis())) {
			params.put(CHECKINGIS, submit.getCheckinGis());
		}
		if (!TextUtils.isEmpty(submit.getCheckinTime())) {
			params.put(CHECKINTIME, submit.getCheckinTime());
		}
		if (!TextUtils.isEmpty(submit.getCheckoutGis())) {
			params.put(CHECKOUTGIS, submit.getCheckoutGis());
		}
		if (!TextUtils.isEmpty(submit.getCheckoutTime())) {
			params.put(CHECKOUTTIME, submit.getCheckoutTime());
		}
		if (submit.getModType() != null) {
			params.put(MODTYPE, String.valueOf(submit.getModType()));
		}
		if (!TextUtils.isEmpty(submit.getUpCtrlMain())) {
			params.put(UP_CTRL_MAIN, submit.getUpCtrlMain());
		}
		if (!TextUtils.isEmpty(submit.getUpCtrlTable())) {
			params.put(UP_CTRL_TABLE, submit.getUpCtrlTable());
		}
		if (!TextUtils.isEmpty(submit.getCodeUpdate())) {
			params.put(IS_CODE_UPDATE, submit.getCodeUpdate());
		}
		if (!TextUtils.isEmpty(submit.getCodeUpdateTab())) {
			params.put(IS_CODE_UPDATE_TAB, submit.getCodeUpdateTab());
		}
		if (!TextUtils.isEmpty(SharedPreferencesUtil.getInstance(context).getCompareRuleIds("ids_" + submit.getTargetid()))) {
			params.put(COMPARE_RULE_IDS,SharedPreferencesUtil.getInstance(context).getCompareRuleIds("ids_" + submit.getTargetid()));
			SharedPreferencesUtil.getInstance(context).clear("ids_" + submit.getTargetid());
		}
		if (!TextUtils.isEmpty(SharedPreferencesUtil.getInstance(context).getCompareContent("content_" + submit.getTargetid()))) {
			params.put(COMPARE_CONTENT,SharedPreferencesUtil.getInstance(context).getCompareContent("content_" + submit.getTargetid()));
			SharedPreferencesUtil.getInstance(context).clear("content_" + submit.getTargetid());
		}
		if (!TextUtils.isEmpty(submit.getDoubleBtnType())) {
			params.put(DOUBLE_BTN_TYPE, submit.getDoubleBtnType());
		}
		if (!TextUtils.isEmpty(submit.getDoubleMasterTaskNo())) {
			params.put(DOUBLE_MASTER_TASK_NO, submit.getDoubleMasterTaskNo());
		}
		params.put(STORE_VISIT_NUMBERS, String.valueOf(submit.getVisitNumbers()));
		return params;
	}
	
	private Submit findParentSubmit(int submitId){
		Submit submit = new SubmitDB(context).findSubmitById(submitId);
		if (submit!=null && submit.getParentId()!=0) {
			findParentSubmit(submit.getParentId());
		}
		return submit;
	}
	
	/**
	 * 查找func 因为只知道Id 无法确定是自定义中的控件还是访店中的控件所以要查询两个表
	 * @param funcId
	 * @return
	 */
	private Func findFunc(int funcId){
		Func func = new FuncDB(context).findFuncByFuncId(funcId);
		if (func==null) {
			func = new VisitFuncDB(context).findFuncByFuncId(funcId);
		}
		return func;
	}
	//检查是否需要报岗－－Aug23
	private boolean checkGangVisibility(Calendar c){
		boolean flag = false;
		int length = SharedPreferencesUtil.getInstance(context).gettInt("gang");
		for(int i=0;i<length;i++){
//			int isFlag = SharedPreferencesUtil.getInstance(context).gettInt("gang"+"isFlag"+i);
//			int isFlag = SharedPreferencesUtil.getInstance(context).gettInt("gang"+"isFlag"+i);
			
			
//			if(isFlag<=0){   //本次未报岗
				String startHM = SharedPreferencesUtil.getInstance(context).gettString("gang"+"startHM"+i);
				if(startHM!=null&&!startHM.equals("")&&!startHM.equals("null")){
					String [] arr = startHM.split(":");
					int HOUR =Integer.parseInt(arr[0]);//起始小时
					int MINUTE =Integer.parseInt(arr[1]);//起始分钟
					int cHour = c.get(Calendar.HOUR_OF_DAY);//当前小时
					int cMinute = c.get(Calendar.MINUTE);//当前分钟
					
					if(cHour>HOUR||(HOUR==cHour&&cMinute>=MINUTE)){//超过起始时间
						String endHM = SharedPreferencesUtil.getInstance(context).gettString("gang"+"endHM"+i);
						if(endHM!=null&&!endHM.equals("")&&!endHM.equals("null")){
							String [] arr1 = endHM.split(":");
							int HOUR1 =Integer.parseInt(arr1[0]);
							int MINUTE1 =Integer.parseInt(arr1[1]);
							if(HOUR1>cHour||(HOUR1==cHour&&MINUTE1>=cMinute)){//在结束时间之内
								
//								SharedPreferencesUtil.getInstance(context).putInt("gang"+"isFlag"+i, 0);
								SharedPreferencesUtil.getInstance(context).putString("gang"+"date"+i, (c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH));
								
								flag = true;
								break;
							}
						}
					}
				}	
//			}
		}
		return flag;
	}

	
}
