package com.yunhu.yhshxc.workplan;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.workplan.bo.Assess;
import com.yunhu.yhshxc.workplan.bo.PlanData;
import com.yunhu.yhshxc.workplan.bo.WorkPlanModle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gcg.org.debug.JLog;

import static com.yunhu.yhshxc.application.SoftApplication.context;

/**
 * 后台联网查询当日是否有工作计划的 首先判断配置文件中是否存在今日日期,如果存在说明已经提醒过今日的计划
 * 
 *
 */
public class WorkPlanCheckService extends IntentService {

	/**
	 *@method
	 *@author suhu
	 *@time 2017/4/18 12:32
	 *@param
	 *
	*/
	public WorkPlanCheckService() {
		super("WorkPlanCheckService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if ("start_getworkplan".equals(intent.getAction())) {
			String url = UrlInfo.queryWorkPlan(this);
			RequestParams params = getRequestParams();
			// 开始访问网络
			GcgHttpClient.getInstance(this).get(url, params, new HttpResponseListener() {

				@Override
				public void onSuccess(int statusCode, String content) {
//					Log.e("view", "onSuccess:" + content);
					// 解析加载下来的数据
					try {
						JSONObject obj = new JSONObject(content);
						String resultCode = obj.getString("resultcode");
						if ("0000".equals(resultCode)) {
							int planId = 0;
							int planType = 0;
							WorkPlanUtils util = new WorkPlanUtils(WorkPlanCheckService.this);
							if (PublicUtils.isValid(obj, "planData")) {
								JSONArray arrayPlan = obj.optJSONArray("planData");
								List<PlanData> planList = util.parsePlanDataList(arrayPlan);
								planId = planList.get(0).getPlanId();
								planType = Integer.parseInt(planList.get(0).getPlanType());
							}
							if (PublicUtils.isValid(obj, "detail")) {
								JSONArray arrayDetail = obj.optJSONArray("detail");
								List<WorkPlanModle> moleList = util.parsePlanModleList(arrayDetail);
							}
							if (PublicUtils.isValid(obj, "assess")) {
								JSONArray arrayAssess = obj.optJSONArray("assess");
								List<Assess> assessList = util.parseAssessList(arrayAssess);
							}
							// 开启通知栏提醒
							if (planId != 0 && planType != 0) {
								String tipTitle = "";
								if (planType == 1) {
									tipTitle =  PublicUtils.getResourceString(context,R.string.work_plan_c24);
								} else if (planType == 2) {
									tipTitle = PublicUtils.getResourceString(context,R.string.work_plan_c23);
								} else if (planType == 3) {
									tipTitle = PublicUtils.getResourceString(context,R.string.work_plan_c22);
								}
//							    NotificationManager mNotificationManager = (NotificationManager)
//							    WorkPlanCheckService.this.getSystemService(Context.NOTIFICATION_SERVICE);
//                                int picDraw = R.drawable.icon_main;
//                                if (PublicUtils.ISCOMBINE) {
////                                	picDraw = R.drawable.xiaoshouban_icon;
//                                	picDraw = R.drawable.icon_main;
//								}
//							    Notification notification = new Notification(picDraw, "您有一个工作计划", System.currentTimeMillis());
//
//							    Intent intentN = new Intent(getApplicationContext(), WorkpLanTipActivity.class);
//						        intentN.putExtra("plan_id", planId);
//						        intentN.putExtra("planType", planType);
//						        PendingIntent pendingIntentN = PendingIntent.getActivity(getApplicationContext(), 0,
//										intentN, 0);
//						        notification.flags |= android.app.Notification.FLAG_AUTO_CANCEL;// 点击后就会自动消失了
//								notification.defaults |= android.app.Notification.DEFAULT_SOUND; // 提示声音
//								notification.defaults |= android.app.Notification.DEFAULT_VIBRATE; // 手机震动
//
//						        notification.setLatestEventInfo(WorkPlanCheckService.this, "工作提醒", "您有一个工作计划", pendingIntentN);
//								mNotificationManager.notify(111, notification);

								//修改后通知
								NotificationManager mNotificationManager = (NotificationManager)
										WorkPlanCheckService.this.getSystemService(Context.NOTIFICATION_SERVICE);
								Notification.Builder builder = new Notification.Builder(WorkPlanCheckService.this);
								int picDraw = R.drawable.icon_main;
								if (PublicUtils.ISCOMBINE) {
									picDraw = R.drawable.icon_main;
								}
								builder.setSmallIcon(picDraw);
								builder.setContentTitle(PublicUtils.getResourceString(context,R.string.work_plan_c21));
								builder.setContentText(PublicUtils.getResourceString(context,R.string.work_plan_c20));

								Intent intentN = new Intent(getApplicationContext(), WorkpLanTipActivity.class);
								intentN.putExtra("plan_id", planId);
								intentN.putExtra("planType", planType);
								PendingIntent pendingIntentN = PendingIntent.getActivity(getApplicationContext(), 0,
										intentN, 0);
								builder.setContentIntent(pendingIntentN);
								Notification notification = builder.getNotification();
								notification.flags |= android.app.Notification.FLAG_AUTO_CANCEL;// 点击后就会自动消失了
								notification.defaults |= android.app.Notification.DEFAULT_SOUND; // 提示声音
								notification.defaults |= android.app.Notification.DEFAULT_VIBRATE; // 手机震动
								mNotificationManager.notify(111,notification);

							}

						} else {
							JLog.d("WorkPlanCheckService", "返回码不为0000");
						}
					} catch (Exception e) {
						e.printStackTrace();
						JLog.d("WorkPlanCheckService", "解析数据异常");
					}

				}

				@Override
				public void onStart() {

				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFailure(Throwable error, String content) {
					// TODO Auto-generated method stub

				}
			});

		}

	}

	private RequestParams getRequestParams() {
		RequestParams param = new RequestParams();
		param.put("phoneno", PublicUtils.receivePhoneNO(getApplicationContext()));
		param.put("userId", SharedPreferencesUtil.getInstance(this).getUserId() + "");
		param.put("type", 4);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		param.put("fromTime", sdf.format(new Date(System.currentTimeMillis())));

		return param;
	}

}
