package com.yunhu.yhshxc.attendance;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.MenuUsableControl;
import com.yunhu.yhshxc.attendance.backstage.AttendanceLocationService;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.MainMenuDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.func.AbsFuncActivity;
import com.yunhu.yhshxc.http.submit.SubmitManager;
import com.yunhu.yhshxc.listener.SubmitDataListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.widget.ToastOrder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import gcg.org.debug.JLog;

/**
 * 考勤打卡模块 继承自AbsFuncActivity类，大部分界面工作由父类完成，通过实现getFuncList()查询出 父类所需的与上下班打卡相关模块。
 *
 * @author wangchao
 * @version 2013.5.22
 */
public class AttendanceFuncActivity extends AbsFuncActivity implements SubmitDataListener {

    /**
     * 考勤状态，用于标识是上还是下班
     * 这个值参考Constants.ATTENDANCE_STATE_START和Constants.ATTENDANCE_STATE_STOP
     */
    private int attState;
    private String name;

    private Context context;
    private LinearLayout submitDataLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = this;
        init();
        super.onCreate(savedInstanceState);
        TextView title = (TextView) findViewById(R.id.tv_titleName);

        submitDataLayout = (LinearLayout) findViewById(R.id.ll_btn_layout);

        if (attState == Constants.ATTENDANCE_STATE_START) {
            title.setText(this.getResources().getString(R.string.s_work));
        } else if (attState == Constants.ATTENDANCE_STATE_STOP) {
            title.setText(this.getResources().getString(R.string.e_work));
        } else if (attState == Constants.NEW_ATTENDANCE_START) {
            title.setText(this.getResources().getString(R.string.s_work));
        } else if (attState == Constants.NEW_ATTENDANCE_END) {
            title.setText(this.getResources().getString(R.string.e_work));
        } else if (attState == Constants.NEW_ATTENDANCE_OVER_START) {
            title.setText(this.getResources().getString(R.string.s_work));
        } else if (attState == Constants.NEW_ATTENDANCE_OVER_END) {
            title.setText(this.getResources().getString(R.string.e_work));
        }
        if (name != null && !name.equals("") && !name.equals("null")) {
            title.setText(name);
        }
        cleanCurrentNoSubmit(targetId);
        SharedPreferencesUtil.getInstance(this).setCacheAttendanceData(null);
        setAttenTime();
    }

    /**
     * 获取Intent传进来的参数，并用当前时间设置SharedPreferences中的上班或下班时间
     */
    private void init() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        targetId = bundle.getInt("targetId");
        attState = bundle.getInt("attState");
        menuType = bundle.getInt("menuType");
        name = bundle.getString("name");

        bundle.putInt("targetType", menuType);

    }

    /**
     * 获取整个布局中所用到的控件数据
     *
     * @return 获取控件数据的List
     */
    @Override
    public List<Func> getFuncList() {
        List<Func> funcList = new FuncDB(this).findFuncListByTargetid(targetId, false);
        return funcList;
    }

    /**
     * 父类中必须实现的abstract方法
     */
    @Override
    public Func getFuncByFuncId(int funcId) {
        return null;
    }

    /**
     * 父类中必须实现的abstract方法
     */
    @Override
    public ArrayList<Func> getOrderFuncList(Integer funcId, int inputOrder) {
        return null;
    }

    /**
     * 父类中必须实现的abstract方法
     */
    @Override
    public List<Func> getButtonFuncList() {
        return null;
    }

    /**
     * 父类中必须实现的abstract方法
     */
    @Override
    public Integer[] findFuncListByInputOrder() {
        return null;
    }

    /**
     * 父类中必须实现的abstract方法
     */
    @Override
    protected void intoLink(Bundle linkBundle) {
    }

    int submitId;

    /**
     * 打开显示采集信息的FuncDetailActivity
     */
    private boolean isClickble;

    @Override
    public void showPreview() {

        if (!isClickble) {
            Log.e("alin", "*************提交");
            isClickble = true;
            SubmitDB submitDB = new SubmitDB(this);
            submitId = submitDB.selectSubmitId(planId, wayId, storeId, targetId, menuType);
            if (submitId != Constants.DEFAULTINT) {// 等于0表示没有操作任何选项
                submitDataLayout.setEnabled(false);
                // 提交
                if (menuType == Menu.TYPE_ATTENDANCE) {// 表示考勤操作
                    // if (usableControl()) {
                    // submitAttendance();
                    // }
                    isClickble = false;
                } else if (menuType == Menu.TYPE_NEW_ATTENDANCE) {// 新考勤操作
                    // if (usableControl()) {
                    // submitNewAttendance();
                    // }
                    setNewAttendTime();
                }

                // Intent intent = new Intent(this,
                // AttendanceFuncDetailActivity.class);
                // intent.putExtra("submitId", submitId);
                // intent.putExtra("taskId", taskId);// 双向模块的时候要传到服务端
                // intent.putExtra("targetId", targetId);
                // intent.putExtra("menuType", menuType);
                // intent.putExtra("attState", attState);// 考勤类型
                // intent.putExtra("entryFuncTime", TextUtils.isEmpty(attenTime) ?
                // DateUtil.getCurDateTime():attenTime);//进入上报页面时间
                // intent.putExtra("bundle", bundle);
                // startActivityForResult(intent, R.id.submit_succeeded);
            } else {
                isClickble = false;
                ToastOrder.makeText(this, getResources().getString(R.string.priview), ToastOrder.LENGTH_LONG).show();
            }
        }

    }

    /**
     * 点击返回对话框的确定按钮时，清除SharedPreferences中的上班时间或下班时间数据
     */
    @Override
    protected void onClickBackBtn() {
        this.finish();
    }

    /**
     * 非正常关闭时保存考勤状态
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        JLog.d(TAG, " Activity 正在被非正常关闭 ! ");
        outState.putInt("attState", attState);
        super.onSaveInstanceState(outState);
    }

    /**
     * 从非正常关闭状态恢复时取回考勤状态
     */
    @Override
    protected void restoreConstants(Bundle savedInstanceState) {
        super.restoreConstants(savedInstanceState);
        attState = savedInstanceState.getInt("attState");
    }

    MyTimerTask task;
    Thread getTimeThread = null;

    private void setNewAttendTime() {
        flag = true;
        timer = new Timer(true);
        if (task == null) {
            task = new MyTimerTask();
            timer.schedule(task, 5000, 100000000); // 延时3000ms后执行，1000ms执行一次
        } else {
            if (task.cancel()) {
                timer.schedule(task, 5000, 100000000); // 延时3000ms后执行，1000ms执行一次
            }
        }
        getTimeThread = new Thread(new Runnable() {

            @Override
            public void run() {
               // context.getApplicationContext().getResources().getString(R.string.bai_du)
                Date date = PublicUtils.getNetDate();
                Date date1 = PublicUtils.getNetDate();

                long l = date.getTime() - date1.getTime();

                if (Math.abs(l) < 10000) {
                    Message msg = Message.obtain();
                    msg.obj = date;
                    handler.sendMessage(msg);
                } else {
                    if (getTimeThread != null)
                        getTimeThread.start();
                }

            }
        });//.start();
        if (getTimeThread != null)
            getTimeThread.start();

    }

    Timer timer;

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(1);

        }

    }

    private boolean flag = true;
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (flag) {
                flag = false;
                if (task != null) {
                    task.cancel();
                    task = null;
                }
                if (timer != null) {
                    timer.cancel();
                }

                Date date = (Date) msg.obj;
                if (attState == Constants.NEW_ATTENDANCE_START || attState == Constants.NEW_ATTENDANCE_END
                        || attState == Constants.NEW_ATTENDANCE_GANG) {
                    if (null != date) {

						if (usableControl(date)) {
							submitNewAttendance(date);
						}else{
							isClickble = false;
						}
					} else {
						isClickble = false;
						Toast.makeText(context, R.string.check_net_time, Toast.LENGTH_LONG).show();
						submitDataLayout.setEnabled(true);
					}
				} else if (attState == Constants.NEW_ATTENDANCE_OVER_START
						|| attState == Constants.NEW_ATTENDANCE_OVER_END) {
					if (null != date) {
						if (usableControl(date)) {
							submitNewAttendance(date);
						}else{
							isClickble = false;
						}
					} else {
						isClickble = false;
						Toast.makeText(context, R.string.check_net_time, Toast.LENGTH_LONG).show();
						submitDataLayout.setEnabled(true);
					}
				}else{
					isClickble = false;
				}

			}
		};
	};

	/**
	 * 手机可用时间段控制,新考勤模块
	 * 
	 *
	 * @return
	 */
	public boolean usableControl(Date date) {
		boolean flag = true;
		String time = null;
		Menu menu = null;
		if (menuType == Menu.TYPE_VISIT || menuType == Menu.TYPE_ATTENDANCE || menuType == Menu.TYPE_NEW_ATTENDANCE) {
			menu = new MainMenuDB(this).findMenuListByMenuType(menuType);
		} else {
			menu = new MainMenuDB(this).findMenuListByMenuId(targetId);
		}
		// time = "12:00|0.5,14:55|0.5";
		time = menu != null ? menu.getPhoneUsableTime() : "";
		MenuUsableControl control = new MenuUsableControl();
		flag = control.isCanUse(time, date);
		if (menuType == Menu.TYPE_ORDER3) {
			if (flag) {// 时间过了判断天
				flag = control.isOrder3CanUse(this);
				if (!flag) {
					ToastOrder.makeText(context, R.string.toast_one8, ToastOrder.LENGTH_LONG).show();
					// showTip("该模块今天不能使用");
					submitDataLayout.setEnabled(true);
					isClickble = false;
				}
			} else {
				ToastOrder.makeText(context, control.tipInfo(time), ToastOrder.LENGTH_LONG).show();
				// showTip(control.tipInfo(time));
				submitDataLayout.setEnabled(true);
				isClickble = false;
			}
		} else {
			if (!flag) {
				ToastOrder.makeText(context, control.tipInfo(time), ToastOrder.LENGTH_LONG).show();
				// showTip(control.tipInfo(time));
				submitDataLayout.setEnabled(true);
				isClickble = false;
			}
		}
		return flag;
	}

    SubmitItemDB submitItemDB;
    private SubmitDB submitDB;
    private Submit submit;
    private String newAttendanceStart;
    private String newAttendanceEnd;
    private String newAttendanceStartOver;
    private String newAttendanceEndOver;

    /**
     * 提交新考勤信息
     */
    private void submitNewAttendance(Date date) {
        submitItemDB = new SubmitItemDB(this);
        submitDB = new SubmitDB(this);
        isNoWait = SharedPreferencesUtil.getInstance(this).getOverAttendWait() == 1 ? true : false;// 考勤的时候是否无等待从SharedPreferencesUtil获取
        List<SubmitItem> attSubmitItems = submitItemDB.findSubmitItemBySubmitId(submitId);
        if (attendanceMust(attSubmitItems)) {
            cacheAttendanceData(attSubmitItems);
            JLog.d("考勤数据缓存成功");
            submit = submitDB.findSubmitById(submitId);// 首先查询Submit
            submit.setState(Submit.STATE_SUBMITED);
            submitDB.updateState(submit);
            JLog.d("考勤数据更新成功");
            submitItemDB.deleteSubmitItemBySubmitId(submitId);// 删除已经采集的数据
            JLog.d("考勤原始数据删除成功");
            SubmitItem attTimeItem = new SubmitItem();
            if (attState == Constants.NEW_ATTENDANCE_START) {// 上班
                // newAttendanceStart = DateUtil.getCurDateTime();
                newAttendanceStart = DateUtil.dateToDateString(date, DateUtil.DATATIMEF_STR);
                insertSubmitItem(attTimeItem, Constants.NEW_IN_TIME, newAttendanceStart);
                // 考勤类型
                SubmitItem attendtype = new SubmitItem();
                insertSubmitItem(attendtype, Constants.NEW_ATTENDTYPE, "1");
                if (PublicUtils.isBeLate(this, newAttendanceStart) > 0) {
                    // 考勤上班迟到
                    SubmitItem in_result_type = new SubmitItem();
                    insertSubmitItem(in_result_type, Constants.NEW_IN_RESULT_TYPE, "1");
                    // 迟到时间
                    SubmitItem lateItem = new SubmitItem();
                    insertSubmitItem(lateItem, Constants.NEW_LATE_DURATION,
                            String.valueOf(PublicUtils.isBeLate(this, newAttendanceStart)));
                }
                // 考勤时间
                SubmitItem attendTime = new SubmitItem();
                // insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
                // newAttendanceStart);
                insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
                        SharedPreferencesUtil.getInstance(this).getNewAttendTime());
                // 地址$经度$纬度$时间$定位方式
                for (SubmitItem submitItem : attSubmitItems) {// 循环把考勤信息存到submititemdb中
                    submitItem.setId(null);// 设置item的id为空
                    if (submitItem.getParamName().equals("-1")) {// 上班定位
                        String[] locationInfo = submitItem.getParamValue().split("\\$");
                        insertSubmitItem(submitItem, Constants.NEW_IN_POSITION, locationInfo[0]);// 定位位置信息
                        insertSubmitItem(submitItem, Constants.NEW_IN_GIS_X, locationInfo[1]);// 定位经度信息
                        insertSubmitItem(submitItem, Constants.NEW_IN_GIS_Y, locationInfo[2]);// 定位纬度信息
                        insertSubmitItem(submitItem, Constants.NEW_IN_GIS_TYPE, locationInfo[4]);// 定位方式信息
                        insertSubmitItem(submitItem, Constants.ACC, locationInfo[5]);
                        insertSubmitItem(submitItem, Constants.STATUS, locationInfo[6]);
                        insertSubmitItem(submitItem, Constants.ACTION, locationInfo[7]);
                        insertSubmitItem(submitItem, Constants.VERSION, locationInfo[8]);
                    } else if (submitItem.getParamName().equals("-2")) {// 上班拍照
                        insertSubmitItem(submitItem, Constants.NEW_IN_IMAGE, submitItem.getParamValue());
                    } else if (submitItem.getParamName().equals("-3")) {// 上班说明
                        insertSubmitItem(submitItem, Constants.NEW_IN_COMMENT, submitItem.getParamValue());
                    }
                }

            } else if (attState == Constants.NEW_ATTENDANCE_END) {// 下班
                // newAttendanceEnd = DateUtil.getCurDateTime();
                // newAttendanceEnd = entryFuncTime;
                newAttendanceEnd = DateUtil.dateToDateString(date, DateUtil.DATATIMEF_STR);
                insertSubmitItem(attTimeItem, Constants.NEW_OUT_TIME, newAttendanceEnd);
                // 考勤类型
                SubmitItem attendtype = new SubmitItem();
                insertSubmitItem(attendtype, Constants.NEW_ATTENDTYPE, "1");
                if (PublicUtils.isLeaveEarly(this, newAttendanceEnd) > 0) {
                    // 考勤下班早退
                    SubmitItem out_result_type = new SubmitItem();
                    insertSubmitItem(out_result_type, Constants.NEW_OUT_RESULT_TYPE, "1");
                    // 早退时间
                    SubmitItem leaveEarlyItem = new SubmitItem();
                    insertSubmitItem(leaveEarlyItem, Constants.NEW_LEAVEEARLY_DURATION,
                            String.valueOf(PublicUtils.isLeaveEarly(this, newAttendanceEnd)));
                }
                // 考勤时间
                SubmitItem attendTime = new SubmitItem();
                // insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
                // SharedPreferencesUtil.getInstance(this).getNewAttendanceStart());
                insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
                        SharedPreferencesUtil.getInstance(this).getNewAttendTime());
                for (SubmitItem submitItem : attSubmitItems) {// 循环把考勤信息存到submititemdb中
                    submitItem.setId(null);// 设置item的id为空
                    if (submitItem.getParamName().equals("-1")) {// 下班定位
                        String[] locationInfo = submitItem.getParamValue().split("\\$");
                        insertSubmitItem(submitItem, Constants.NEW_OUT_POSITION, locationInfo[0]);// 定位位置信息
                        insertSubmitItem(submitItem, Constants.NEW_OUT_GIS_X, locationInfo[1]);// 定位经度信息
                        insertSubmitItem(submitItem, Constants.NEW_OUT_GIS_Y, locationInfo[2]);// 定位纬度信息
                        insertSubmitItem(submitItem, Constants.NEW_OUT_GIS_TYPE, locationInfo[4]);// 定位方式信息
                        insertSubmitItem(submitItem, Constants.ACC, locationInfo[5]);
                        insertSubmitItem(submitItem, Constants.STATUS, locationInfo[6]);
                        insertSubmitItem(submitItem, Constants.ACTION, locationInfo[7]);
                        insertSubmitItem(submitItem, Constants.VERSION, locationInfo[8]);
                    } else if (submitItem.getParamName().equals("-2")) {// 下班拍照
                        insertSubmitItem(submitItem, Constants.NEW_OUT_IMAGE, submitItem.getParamValue());
                    } else if (submitItem.getParamName().equals("-3")) {// 下班说明
                        insertSubmitItem(submitItem, Constants.NEW_OUT_COMMENT, submitItem.getParamValue());
                    }
                }
            } else if (attState == Constants.NEW_ATTENDANCE_OVER_START) {// 上班
                // newAttendanceStartOver = DateUtil.getCurDateTime();
                newAttendanceStartOver = DateUtil.dateToDateString(date, DateUtil.DATATIMEF_STR);
                insertSubmitItem(attTimeItem, Constants.NEW_IN_TIME, newAttendanceStartOver);
                // 考勤类型
                SubmitItem attendtype = new SubmitItem();
                insertSubmitItem(attendtype, Constants.NEW_ATTENDTYPE, "2");
                // 考勤时间
                SubmitItem attendTime = new SubmitItem();
                // insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
                // newAttendanceStartOver);
                insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
                        SharedPreferencesUtil.getInstance(this).getNewAttendOverTime());
                // 地址$经度$纬度$时间$定位方式
                for (SubmitItem submitItem : attSubmitItems) {// 循环把考勤信息存到submititemdb中
                    submitItem.setId(null);// 设置item的id为空
                    if (submitItem.getParamName().equals("-1")) {// 上班定位
                        String[] locationInfo = submitItem.getParamValue().split("\\$");
                        insertSubmitItem(submitItem, Constants.NEW_IN_POSITION, locationInfo[0]);// 定位位置信息
                        insertSubmitItem(submitItem, Constants.NEW_IN_GIS_X, locationInfo[1]);// 定位经度信息
                        insertSubmitItem(submitItem, Constants.NEW_IN_GIS_Y, locationInfo[2]);// 定位纬度信息
                        insertSubmitItem(submitItem, Constants.NEW_IN_GIS_TYPE, locationInfo[4]);// 定位方式信息
                        insertSubmitItem(submitItem, Constants.ACC, locationInfo[5]);
                        insertSubmitItem(submitItem, Constants.STATUS, locationInfo[6]);
                        insertSubmitItem(submitItem, Constants.ACTION, locationInfo[7]);
                        insertSubmitItem(submitItem, Constants.VERSION, locationInfo[8]);
                    } else if (submitItem.getParamName().equals("-2")) {// 上班拍照
                        insertSubmitItem(submitItem, Constants.NEW_IN_IMAGE, submitItem.getParamValue());
                    } else if (submitItem.getParamName().equals("-3")) {// 上班说明
                        insertSubmitItem(submitItem, Constants.NEW_IN_COMMENT, submitItem.getParamValue());
                    }
                }

            } else if (attState == Constants.NEW_ATTENDANCE_OVER_END) {
                // 下班
                // newAttendanceEndOver = DateUtil.getCurDateTime();
                // newAttendanceEndOver = entryFuncTime;
                newAttendanceEndOver = DateUtil.dateToDateString(date, DateUtil.DATATIMEF_STR);
                insertSubmitItem(attTimeItem, Constants.NEW_OUT_TIME, newAttendanceEndOver);
                // 考勤类型
                SubmitItem attendtype = new SubmitItem();
                insertSubmitItem(attendtype, Constants.NEW_ATTENDTYPE, "2");
                // 考勤时间
                SubmitItem attendTime = new SubmitItem();
                // insertSubmitItem(attendTime,Constants.NEW_ATTEND_TIME,
                // SharedPreferencesUtil.getInstance(AttendanceFuncDetailActivity.this).getNewAttendanceStartOver());
                insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
                        SharedPreferencesUtil.getInstance(this).getNewAttendOverTime());
                for (SubmitItem submitItem : attSubmitItems) {// 循环把考勤信息存到submititemdb中
                    submitItem.setId(null);// 设置item的id为空
                    if (submitItem.getParamName().equals("-1")) {// 下班定位
                        String[] locationInfo = submitItem.getParamValue().split("\\$");
                        insertSubmitItem(submitItem, Constants.NEW_OUT_POSITION, locationInfo[0]);// 定位位置信息
                        insertSubmitItem(submitItem, Constants.NEW_OUT_GIS_X, locationInfo[1]);// 定位经度信息
                        insertSubmitItem(submitItem, Constants.NEW_OUT_GIS_Y, locationInfo[2]);// 定位纬度信息
                        insertSubmitItem(submitItem, Constants.NEW_OUT_GIS_TYPE, locationInfo[4]);// 定位方式信息
                        insertSubmitItem(submitItem, Constants.ACC, locationInfo[5]);
                        insertSubmitItem(submitItem, Constants.STATUS, locationInfo[6]);
                        insertSubmitItem(submitItem, Constants.ACTION, locationInfo[7]);
                        insertSubmitItem(submitItem, Constants.VERSION, locationInfo[8]);
                    } else if (submitItem.getParamName().equals("-2")) {// 下班拍照
                        insertSubmitItem(submitItem, Constants.NEW_OUT_IMAGE, submitItem.getParamValue());
                    } else if (submitItem.getParamName().equals("-3")) {// 下班说明
                        insertSubmitItem(submitItem, Constants.NEW_OUT_COMMENT, submitItem.getParamValue());
                    }
                }

            } else if (attState == Constants.NEW_ATTENDANCE_GANG) {// 报岗
                newAttendanceStartOver = DateUtil.dateToDateString(date, DateUtil.DATATIMEF_STR);
                // newAttendanceStartOver = DateUtil.getCurDateTime();
                insertSubmitItem(attTimeItem, Constants.NEW_IN_TIME, newAttendanceStartOver);
                // 考勤类型
                SubmitItem attendtype = new SubmitItem();
                // insertSubmitItem(attendtype, Constants.NEW_ATTENDTYPE, "2");
                // 考勤时间
                SubmitItem attendTime = new SubmitItem();
                // insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
                // newAttendanceStartOver);
                // insertSubmitItem(attendTime, Constants.NEW_ATTEND_TIME,
                // SharedPreferencesUtil.getInstance(this).getNewAttendOverTime());
                // 地址$经度$纬度$时间$定位方式
                for (SubmitItem submitItem : attSubmitItems) {// 循环把考勤信息存到submititemdb中
                    submitItem.setId(null);// 设置item的id为空
                    if (submitItem.getParamName().equals("-1")) {// 上班定位
                        String[] locationInfo = submitItem.getParamValue().split("\\$");
                        insertSubmitItem(submitItem, Constants.NEW_IN_POSITION, locationInfo[0]);// 定位位置信息
                        insertSubmitItem(submitItem, Constants.NEW_IN_GIS_X, locationInfo[1]);// 定位经度信息
                        insertSubmitItem(submitItem, Constants.NEW_IN_GIS_Y, locationInfo[2]);// 定位纬度信息
                        insertSubmitItem(submitItem, Constants.NEW_IN_GIS_TYPE, locationInfo[4]);// 定位方式信息
                        insertSubmitItem(submitItem, Constants.ACC, locationInfo[5]);
                        insertSubmitItem(submitItem, Constants.STATUS, locationInfo[6]);
                        insertSubmitItem(submitItem, Constants.ACTION, locationInfo[7]);
                        insertSubmitItem(submitItem, Constants.VERSION, locationInfo[8]);
                        insertSubmitItem(submitItem, "type", "gang");
                    } else if (submitItem.getParamName().equals("-2")) {// 上班拍照
                        insertSubmitItem(submitItem, Constants.NEW_IN_IMAGE, submitItem.getParamValue());
                    } else if (submitItem.getParamName().equals("-3")) {// 上班说明
                        insertSubmitItem(submitItem, Constants.NEW_IN_COMMENT, submitItem.getParamValue());
                    }
                }
            } else {
                JLog.d("考勤类型:" + attState);
            }
            Constants.ISCHECKOUTMODUL = true;
            submit();
        }
    }

    private void insertSubmitItem(SubmitItem submitItem, String paramName, String paramValue) {
        if (submitItem != null) {
            submitItem.setSubmitId(submitId);
            submitItem.setParamName(paramName);
            submitItem.setParamValue(paramValue);
            submitItemDB.insertSubmitItem(submitItem, false);
        }
    }

    /**
     * 提交数据
     */
    public void submit() {
        // isNoWait = false;
        if (submit != null) {
            // submit.setContentDescription(contentDescription());
            submitDB.updateSubmit(submit);
        }
        SubmitManager.getInstance(this).submitData(isNoWait, this);
    }

	private boolean attendanceMust(List<SubmitItem> list) {
		boolean flag = true;
		// 1：拍照、2：定位、3：说明
		String attendanceMust = SharedPreferencesUtil.getInstance(this).getAttendanceMustDo();
		if (TextUtils.isEmpty(attendanceMust)) {
			if (list.size() < 1) {// 考勤信息不完全
				ToastOrder.makeText(context, R.string.check_net_time2, ToastOrder.LENGTH_LONG).show();
				flag = false;
			}
		} else {
			if (attendanceMust.contains("1")) {// 拍照
				SubmitItem item = submitItemDB.findSubmitItemByParamNameAndSubmitId("-2", submitId);
				if (item == null || TextUtils.isEmpty(item.getParamValue())) {
					ToastOrder.makeText(this, R.string.check_net_time3, ToastOrder.LENGTH_SHORT).show();
					flag = false;
				}
			}
			if (attendanceMust.contains("2")) {// 定位
				SubmitItem item = submitItemDB.findSubmitItemByParamNameAndSubmitId("-1", submitId);
				if (item == null || TextUtils.isEmpty(item.getParamValue())) {
					ToastOrder.makeText(this, R.string.check_net_time4, ToastOrder.LENGTH_SHORT).show();
					flag = false;
				}
			}
			if (attendanceMust.contains("3")) {// 说明
				SubmitItem item = submitItemDB.findSubmitItemByParamNameAndSubmitId("-3", submitId);
				if (item == null || TextUtils.isEmpty(item.getParamValue().trim())) {
					ToastOrder.makeText(this, R.string.check_net_time5, ToastOrder.LENGTH_SHORT).show();
					flag = false;
				}
			}
		}
		if (!flag) {
			submitDataLayout.setEnabled(true);
			isClickble = false;
		}
		return flag;
	}

    /**
     * 临时缓存考勤要上报的数据，即时提交的时候使用
     *
     * @param list
     */
    private void cacheAttendanceData(List<SubmitItem> list) {
        try {
            if (list != null && list.size() > 0) {
                JSONArray array = new JSONArray();
                JSONObject obj = null;
                for (int i = 0; i < list.size(); i++) {
                    obj = new JSONObject();
                    SubmitItem item = list.get(i);
                    obj.put("submitId", item.getSubmitId());
                    obj.put("paramName", TextUtils.isEmpty(item.getParamName()) ? "" : item.getParamName());
                    obj.put("paramValue", TextUtils.isEmpty(item.getParamValue()) ? "" : item.getParamValue());
                    obj.put("type", item.getType());
                    obj.put("orderId", item.getOrderId());
                    obj.put("note", TextUtils.isEmpty(item.getNote()) ? "" : item.getNote());
                    array.put(obj);
                }
                SharedPreferencesUtil.getInstance(this).setCacheAttendanceData(array.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void submitReceive(boolean isSuccess) {
        isClickble = false;
        submitDataLayout.setEnabled(true);
        if (isSuccess) {
            SharedPreferencesUtil.getInstance(this).setCacheAttendanceData(null);
            submitItemDB.deleteSubmitItemBySubmitId(submitId);// 删除已经采集的数据
            // if (menuType == Menu.TYPE_ATTENDANCE) {
            // if (attState == Constants.ATTENDANCE_STATE_START) {
            // SharedPreferencesUtil.getInstance(AttendanceFuncDetailActivity.this).setStartWorkTime(attendanceStart
            // + "do");
            // }else if (attState == Constants.ATTENDANCE_STATE_STOP) {
            // SharedPreferencesUtil.getInstance(AttendanceFuncDetailActivity.this).setStopWorkTime(attendanceEnd
            // + "do");
            // }
            // }
            if (menuType == Menu.TYPE_NEW_ATTENDANCE) {
                if (attState == Constants.NEW_ATTENDANCE_START) {
                    if (!PublicUtils.ISDEMO) {
                        SharedPreferencesUtil.getInstance(context).setNewAttendanceStart(newAttendanceStart);
                    } else {
                        SharedPreferencesUtil.getInstance(context).setNativeAttendanceStart(newAttendanceStart);

                    }
                    AttendanceUtil.startAttendanceLocationService(context, AttendanceLocationService.WORK_START);
                } else if (attState == Constants.NEW_ATTENDANCE_END) {
                    if (!PublicUtils.ISDEMO) {
                        SharedPreferencesUtil.getInstance(context).setNewAttendanceEnd(newAttendanceEnd);
                    } else {
                        SharedPreferencesUtil.getInstance(context).setNativeAttendanceEnd(newAttendanceEnd);
                    }
                    AttendanceUtil.startAttendanceLocationService(context, AttendanceLocationService.WORK_END);
                } else if (attState == Constants.NEW_ATTENDANCE_OVER_START) {
                    SharedPreferencesUtil.getInstance(context).setNewAttendanceStartOver(newAttendanceStartOver);
                } else if (attState == Constants.NEW_ATTENDANCE_OVER_END) {
                    SharedPreferencesUtil.getInstance(context).setNewAttendanceEndOver(newAttendanceEndOver);
                }
            }
            subimitResultToDo(R.id.submit_succeeded, null);
            // thisFinish(R.id.submit_succeeded,null);
            this.finish();
            Toast.makeText(AttendanceFuncActivity.this, R.string.submit_ok, Toast.LENGTH_SHORT).show();
            ;
        } else {
            if (submit != null) {
                submit.setState(Submit.STATE_NO_SUBMIT);
                submitDB.updateSubmit(submit);
                submitItemDB.deleteSubmitItemBySubmitId(submitId);// 删除已经采集的数据
                paraseCacheAttendanceData();
                Toast.makeText(AttendanceFuncActivity.this, R.string.toast_one7, Toast.LENGTH_SHORT).show();
                ;
            }
        }

    }

    /**
     * 解析缓存的考勤数据存储到数据库
     */
    private void paraseCacheAttendanceData() {
        try {
            String cacheJson = SharedPreferencesUtil.getInstance(this).getCacheAttendanceData();
            if (!TextUtils.isEmpty(cacheJson)) {
                JSONArray array = new JSONArray(cacheJson);
                JSONObject obj = null;
                SubmitItem item = null;
                for (int i = 0; i < array.length(); i++) {
                    obj = array.getJSONObject(i);
                    item = new SubmitItem();
                    item.setSubmitId(obj.getInt("submitId"));
                    item.setParamName(obj.getString("paramName"));
                    item.setParamValue(obj.getString("paramValue"));
                    item.setType(obj.getInt("type"));
                    item.setOrderId(obj.getInt("orderId"));
                    item.setNote(obj.getString("note"));
                    submitItemDB.insertSubmitItem(item, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
