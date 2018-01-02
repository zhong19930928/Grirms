package com.yunhu.yhshxc.attendance;

import gcg.org.debug.JLog;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.AttendanceGang;
import com.yunhu.yhshxc.bo.AttendanceNew;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.list.activity.AbsSearchListActivity;
import com.yunhu.yhshxc.list.activity.ShowImageActivity;
import com.yunhu.yhshxc.parser.AttendanceParseNew;
import com.yunhu.yhshxc.utility.DateUtil;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;

import static com.yunhu.yhshxc.application.SoftApplication.context;

/**
 * 考勤查询模块 该类继承自AbsSearchListActivity，为AbsSearchListActivity提供考勤查询的layout和相关数据
 * 
 * @version 2013.6.3
 * @author wangchao
 */
public class AttendanceNewListActivity extends AbsSearchListActivity {

	/**
	 * “查看更多”按钮的容器
	 */
	private View loadMoreView = null;

	/**
	 * “查看更多”按钮
	 */
	private TextView loadMoreButton = null;

	/**
	 * 重写父类方法，为父类提供考勤列表的layout资源文件
	 * 
	 * @return 返回考勤列表的layout资源文件
	 */
	@Override
	protected int getLayoutId() {
		return R.layout.attendance_list;
	}

	/**
	 * 初始化基本功能
	 */
	@Override
	public void initBase() {
		super.initBase();
		this.addPaging(); // 添加分页功能
	}

	/**
	 * 解析JSON，返回列表显示的对象
	 * 
	 * @param json
	 *            需要解析的JSON字符串
	 * @return 返回列表显示的对象
	 * @throws Exception
	 *             可能引发的各种异常
	 */
	@Override
	protected List getDataListOnThread(String json) throws Exception {
		// 解析json,获取考勤信息列表
		List<AttendanceNew> attendanceList = new AttendanceParseNew()
				.parseAttendance(json);
		if (attendanceList != null && attendanceList.size() > 0) {// 考勤信息不为空
			JLog.d(TAG, "数据返回成功,刷新列表,共返回==>" + attendanceList.size() + "<==条数据");
			// 通知适配器更新数据
		}
		return attendanceList;
	}

	/**
	 * 获取URL地址
	 * 
	 * @return 返回URL地址
	 */
	@Override
	protected String getSearchUrl() {
		return UrlInfo.getUrlSearcheNewAttendance(this);
	}

	@Override
	protected void initTitle(List<Func> viewCoum, LinearLayout ll_title) {
	}

	/**
	 * 获取http请求的参数
	 * 
	 * @return 返回http请求的参数
	 */
	@Override
	protected HashMap<String, String> getSearchParams() {
		// 存放查询条件的map
		HashMap<String, String> searchParams = new HashMap<String, String>();
		// 从SharedPreferences获取 查询条件 的字符串
		String searchReplenishStr = SharedPreferencesUtil.getInstance(this)
				.getReplenish();
		if (!TextUtils.isEmpty(searchReplenishStr)) {// 判断searchReplenishStr非空
			// 根据"$"分割查询条件 的字符串
			String[] searchReplenishList = TextUtils.split(searchReplenishStr,
					"\\$@@");
			for (String searchParamsStr : searchReplenishList) {// 循环添加查询条件到map中
				if (searchParamsStr.contains(",")) {
					String[] searchParamsStrSplit = searchParamsStr.split(",",
							2);
					switch (Integer.valueOf(searchParamsStrSplit[0])) {
					case -4:// 角色,多个时,用","分隔
						searchParams.put("roleId", searchParamsStrSplit[1]);
						break;
					case -5:// 用户,多个时,用","分隔
						searchParams.put("userId", searchParamsStrSplit[1]);
						break;
					case -6:// 日期
						try{
						searchParams.put("startTime", DateUtil
								.dateZeroize(searchParamsStrSplit[1]
										.split("~@@")[0]));
						
						searchParams.put("endTime", DateUtil
								.dateZeroize(searchParamsStrSplit[1]
										.split("~@@")[1]));
						}catch(Exception e){
							e.printStackTrace();
						}
						break;
					}
				}
			}
		}
		return searchParams;
	}

	/**
	 * 获取ExpandableListView的Adapter的当前ChildrenCount
	 * 
	 * @param groupPosition
	 *            在ExpandableListView的Adapter中的组位置
	 * @return 返回Adapter中获取当前ChildrenCount
	 * 
	 * @see BaseExpandableListAdapter
	 */
	@Override
	protected int getCurChildrenCount(int groupPosition) {
		AttendanceNew attendance = (AttendanceNew) dataList.get(groupPosition);
		if (attendance != null) {// 有考勤数据
			if (!TextUtils.isEmpty(attendance.getInTime())) {// 已上班
				if (!TextUtils.isEmpty(attendance.getOutTime())) {// 已下班
					List<AttendanceGang> gs = attendance.getGangList();
					if(gs!=null&&!gs.isEmpty()&&gs.size()>0){
						return 2+gs.size();
					}
					return 2;
				}
				List<AttendanceGang> gs = attendance.getGangList();
				if(gs!=null&&!gs.isEmpty()&&gs.size()>0){
					return 1+gs.size();
				}
				return 1;
			}
		}
		return 0;
	}

	/**
	 * Adapter中获取当前GroupView
	 * 
	 * @see BaseExpandableListAdapter
	 */
	@Override
	protected View getCurGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// 考勤对象
		AttendanceNew attendance = (AttendanceNew) dataList.get(groupPosition);
		// viewholder
		ExpendGroupViewHolder groupHolder = null;
		if (convertView == null) {// 没有缓存对象
			// 初始化view
			convertView = View.inflate(AttendanceNewListActivity.this,
					R.layout.attendance_list_item, null);
			// 初始化holder
			groupHolder = new ExpendGroupViewHolder();
			// 打开关闭图标
			groupHolder.iv_isExpend = (ImageView) convertView
					.findViewById(R.id.iv_attendance_item_isExpanded);
			// 显示名字
			groupHolder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_attendance_item_user);
			// 考勤日期
			groupHolder.tv_date = (TextView) convertView
					.findViewById(R.id.tv_attendance_item_date);
			// item颜色
			groupHolder.ll_color = (LinearLayout) convertView
					.findViewById(R.id.ll_attendance_item_color);
			// // 审批图片
			// groupHolder.iv_do = (ImageView)
			// convertView.findViewById(R.id.iv_attendance_item_do);
			//
			// groupHolder.tv_state = (TextView)
			// convertView.findViewById(R.id.tv_attendance_item_state);
			// 设置tag
			convertView.setTag(groupHolder);
		} else {// 有缓存对象
				// 获取holder
			groupHolder = (ExpendGroupViewHolder) convertView.getTag();
		}

		// 设置显示名字
		groupHolder.tv_name.setText(attendance.getUserName());
		// 设置考勤日期
		groupHolder.tv_date.setText(attendance.getAttendTime());

		// 设置是否已经展开的图片
		if (isExpanded) {// 已展开
			groupHolder.iv_isExpend.setImageResource(R.drawable.att_cut);
			groupHolder.tv_name.setTextColor(Color.rgb(255, 255, 255));
			groupHolder.tv_date.setTextColor(Color.rgb(255, 255, 255));
			groupHolder.ll_color.setBackgroundResource(R.color.att_bg_color_blue);
		} else {// 未展开
			groupHolder.iv_isExpend.setImageResource(R.drawable.att_puls);
			groupHolder.tv_name.setTextColor(Color.rgb(0, 0, 0));
			groupHolder.tv_date.setTextColor(Color.rgb(0, 0, 0));
			if (!TextUtils.isEmpty(attendance.getAttendType()) && attendance.getAttendType().equals("2")) {
				groupHolder.ll_color.setBackgroundResource(R.color.green);
			}else{
				// 设置考勤状态
				if (!TextUtils.isEmpty(attendance.getInTime())) {// 上班
					if (!TextUtils.isEmpty(attendance.getOutTime())) {// 下班
						groupHolder.ll_color.setBackgroundResource(R.color.att_bg_color_normal);
					} else {
						groupHolder.ll_color.setBackgroundResource(R.color.att_bg_color_yellow);
					}
				} else {
					groupHolder.ll_color.setBackgroundResource(R.color.att_bg_color_red);
				}
			}
		}
		return convertView;
	}

	/**
	 * Adapter中获取当前ChildView
	 * 
	 * @see BaseExpandableListAdapter
	 */
	@Override
	protected View getCurChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// 初始化chidren view
		convertView = View.inflate(AttendanceNewListActivity.this,
				R.layout.attendance_list_item_children, null);
		// 考勤状态
		TextView tv_state = (TextView) convertView
				.findViewById(R.id.tv_attendance_item_children_state);
		// 考勤时间
		TextView tv_time = (TextView) convertView
				.findViewById(R.id.tv_attendance_item_children_time);
		// 考勤说明
		TextView tv_des = (TextView) convertView
				.findViewById(R.id.tv_attendance_item_children_des);

		LinearLayout ll_des = (LinearLayout) convertView
				.findViewById(R.id.ll_attendance_item_children_des);

		// 考勤图片
		TextView tv_image = (TextView) convertView
				.findViewById(R.id.tv_attendance_item_children_image);

		LinearLayout ll_image = (LinearLayout) convertView
				.findViewById(R.id.ll_attendance_item_children_image);

		// 考勤地址
		TextView tv_loc = (TextView) convertView
				.findViewById(R.id.tv_attendance_item_children_location);

		LinearLayout ll_loc = (LinearLayout) convertView
				.findViewById(R.id.ll_attendance_item_children_location);
		// 经度
		TextView tv_lon = (TextView) convertView
				.findViewById(R.id.tv_attendance_item_children_lon);

		LinearLayout ll_lon = (LinearLayout) convertView
				.findViewById(R.id.ll_attendance_item_children_lon);
		// 纬度
		TextView tv_lat = (TextView) convertView
				.findViewById(R.id.tv_attendance_item_children_lat);

		LinearLayout ll_lat = (LinearLayout) convertView
				.findViewById(R.id.ll_attendance_item_children_lat);
		// 定位类型
		TextView tv_type = (TextView) convertView
				.findViewById(R.id.tv_attendance_item_children_gisType);

		LinearLayout ll_type = (LinearLayout) convertView
				.findViewById(R.id.ll_attendance_item_children_gisType);

		// 考勤对象
		AttendanceNew attendance = (AttendanceNew) dataList.get(groupPosition);
		// 考勤信息
		String att_state = null;
		String att_time = null;
		String att_des = null;
		String att_loc = null;
		String att_lon = null;
		String att_lat = null;
		String att_type = null;
		String att_image_url = null;
		String overtimeName = SharedPreferencesUtil.getInstance(context).gettString("gangName");// gang
		String goName = SharedPreferencesUtil.getInstance(context).gettString("goName");// up
		String finishName = SharedPreferencesUtil.getInstance(context).gettString("finishName");// down

		// 初始化考勤信息
		if (childPosition == 0) {// 上班
			if (goName != null && !goName.equals("") && !goName.equals("null")) {
				att_state = goName+"  :  ";
			}else{
				att_state = PublicUtils.getResourceString(this,R.string.s_work)+"  :  ";
			}
			att_time = attendance.getInTime();
			if (!TextUtils.isEmpty(attendance.getInComment())) {
				att_des = attendance.getInComment();
				ll_des.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(attendance.getInAddress())) {
				att_loc = attendance.getInAddress();
				ll_loc.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(attendance.getInLon())) {
				att_lon = attendance.getInLon();
				ll_lon.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(attendance.getInLat())) {
				att_lat = attendance.getInLat();
				ll_lat.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(attendance.getInGisType())) {
				att_type = attendance.getInGisType();
				ll_type.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(attendance.getInImage())) {//
				att_image_url = attendance.getInImage();
				ll_image.setVisibility(View.VISIBLE);
			}
		} 

		else if(childPosition>=1){//报岗排在上班中间，即 上班  报岗  下班 
			List<AttendanceGang> gs = attendance.getGangList();
			if(gs!=null&&!gs.isEmpty()&&gs.size()>0){
				if(childPosition >gs.size()){//最后一条是下班
					if (finishName != null && !finishName.equals("") && !finishName.equals("null")) {
						att_state = finishName+"  :  ";
					}else{
						att_state = PublicUtils.getResourceString(this,R.string.e_work)+"  :  ";
					}
					att_time = attendance.getOutTime();
					if (!TextUtils.isEmpty(attendance.getOutComment())) {
						att_des = attendance.getOutComment();
						ll_des.setVisibility(View.VISIBLE);
					}
					if (!TextUtils.isEmpty(attendance.getOutAddress())) {
						att_loc = attendance.getOutAddress();
						ll_loc.setVisibility(View.VISIBLE);
					}
					if (!TextUtils.isEmpty(attendance.getOutLon())) {
						att_lon = attendance.getOutLon();
						ll_lon.setVisibility(View.VISIBLE);
					}
					if (!TextUtils.isEmpty(attendance.getOutLat())) {
						att_lat = attendance.getOutLat();
						ll_lat.setVisibility(View.VISIBLE);
					}
					if (!TextUtils.isEmpty(attendance.getOutGisType())) {
						att_type = attendance.getOutGisType();
						ll_type.setVisibility(View.VISIBLE);
					}
					if (!TextUtils.isEmpty(attendance.getOutImage())) {// 
						att_image_url = attendance.getOutImage();
						ll_image.setVisibility(View.VISIBLE);
					}
				}else{//中间数据是报岗
					AttendanceGang gang = gs.get(childPosition-1);
					if (overtimeName != null && !overtimeName.equals("") && !overtimeName.equals("null")) {
						att_state = overtimeName+"  :  ";
					}else{
						att_state = PublicUtils.getResourceString(this,R.string.attend_bg)+"  :  ";
					}
					att_time = gang.getGangTime();
					if (!TextUtils.isEmpty(gang.getGangC())) {//说明
						att_des = gang.getGangC();
						ll_des.setVisibility(View.VISIBLE);
					}
					if (!TextUtils.isEmpty(gang.getGangAdr())) {
						att_loc = gang.getGangAdr();
						ll_loc.setVisibility(View.VISIBLE);
					}
					if (!TextUtils.isEmpty(gang.getGangX())) {
						att_lon = gang.getGangX();
						ll_lon.setVisibility(View.VISIBLE);
					}
					if (!TextUtils.isEmpty(gang.getGangY())) {
						att_lat = gang.getGangY();
						ll_lat.setVisibility(View.VISIBLE);
					}
					if (!TextUtils.isEmpty(gang.getGangT())) {
						att_type = gang.getGangT();
						ll_type.setVisibility(View.VISIBLE);
					}
					if (!TextUtils.isEmpty(gang.getGangM())) {// 
						att_image_url = gang.getGangM();
						ll_image.setVisibility(View.VISIBLE);
					}
				}
				
			}else{//没有报岗数据，就是下班
				if (finishName != null && !finishName.equals("") && !finishName.equals("null")) {
					att_state = finishName+"  :  ";
				}else{
					att_state = PublicUtils.getResourceString(this,R.string.e_work)+"  :  ";
				}
				att_time = attendance.getOutTime();
				if (!TextUtils.isEmpty(attendance.getOutComment())) {
					att_des = attendance.getOutComment();
					ll_des.setVisibility(View.VISIBLE);
				}
				if (!TextUtils.isEmpty(attendance.getOutAddress())) {
					att_loc = attendance.getOutAddress();
					ll_loc.setVisibility(View.VISIBLE);
				}
				if (!TextUtils.isEmpty(attendance.getOutLon())) {
					att_lon = attendance.getOutLon();
					ll_lon.setVisibility(View.VISIBLE);
				}
				if (!TextUtils.isEmpty(attendance.getOutLat())) {
					att_lat = attendance.getOutLat();
					ll_lat.setVisibility(View.VISIBLE);
				}
				if (!TextUtils.isEmpty(attendance.getOutGisType())) {
					att_type = attendance.getOutGisType();
					ll_type.setVisibility(View.VISIBLE);
				}
				if (!TextUtils.isEmpty(attendance.getOutImage())) {// 
					att_image_url = attendance.getOutImage();
					ll_image.setVisibility(View.VISIBLE);
				}
			}
		}
		// 设置显示内容
		tv_state.setText(att_state);
		tv_time.setText(att_time);
		tv_des.setText(att_des);
		tv_loc.setText(att_loc);
		tv_lon.setText(att_lon);
		tv_lat.setText(att_lat);
		tv_type.setText(getTypeValue(att_type));
		tv_image.getPaint().setUnderlineText(true);
		tv_image.setTag(att_image_url);
		String[] dates = att_time.split(" ");
//		final String imageName = attendance.getAttendTime() + " - "
//				+ att_state.substring(0, att_state.length() - 4);
		final String imageName = dates[0] + " - "
				+ att_state.substring(0, att_state.length() - 4);
		tv_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(AttendanceListActivity.this,
				// imageName+"\n"+(String) v.getTag(), 1).show();
				Intent intent = new Intent(AttendanceNewListActivity.this,ShowImageActivity.class);
				intent.putExtra("imageUrl", (String) v.getTag());
				intent.putExtra("imageName", imageName);
				startActivity(intent);
			}
		});
		return convertView;
	}

	/**
	 * 获取定位方式的文字描述
	 * 
	 * @param att_type
	 *            定位类型
	 * @return 返回定位类型的文字描述
	 */
	private String getTypeValue(String att_type) {
		String value = "";
		if ("1".equals(att_type)) {
			value = PublicUtils.getResourceString(AttendanceNewListActivity.this,R.string.loation_type);
		} else {
			value = PublicUtils.getResourceString(AttendanceNewListActivity.this,R.string.loation_type1);
		}
		return value;
	}

	/**
	 * 可扩展listview的groupview的优化对象
	 */
	private final class ExpendGroupViewHolder {
		ImageView iv_isExpend;
		TextView tv_name;
		TextView tv_date;
		LinearLayout ll_color;
	}

	/**
	 * 重写父类为http请求添加翻页参数的方法，按照上班时间翻页
	 */
	@Override
	protected void setPageParams(HashMap<String, String> params) {
		if (dataList != null && !dataList.isEmpty()) {
			AttendanceNew attendance = (AttendanceNew) dataList
					.get(dataList.size() - 1);
			params.put(PAGE, attendance.getAttendTime());
			JLog.d(TAG, "page=" + attendance.getInTime());
		}
	}

	/**
	 * 添加分页功能
	 */
	@Override
	public void addPaging() {
		loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
		loadMoreButton = (TextView) loadMoreView
				.findViewById(R.id.loadMoreButton);
		loadMoreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMoreButton.setClickable(false);
				loadMoreButton.setText(PublicUtils.getResourceString(AttendanceNewListActivity.this,R.string.init));// 设置按钮文字
				netWorkInThread();// 加载更多数据
			}
		});
		elv_search.addFooterView(loadMoreView); // 设置列表底部视图
	}

	/**
	 * 重写父类方法，关闭加载对话框后恢复“查看更多”按钮的状态
	 */
	@Override
	public void dismissLoadDialog() {
		super.dismissLoadDialog();
		if (loadMoreButton != null) {
			loadMoreButton.setClickable(true);
			loadMoreButton.setText(PublicUtils.getResourceString(this,R.string.check_more));
		}
	}

	/**
	 * 解析JSON数据，由doInbackGround()方法调用
	 * 
	 * @param json
	 *            需要解析的json数据
	 * @throws Exception
	 *             各种可能出现的异常
	 */
	@Override
	protected void doInbackGroundInThread(String json) throws Exception {
		List list = getDataListOnThread(json);
		if (dataList != null && !dataList.isEmpty()) {
			if (list != null && !list.isEmpty()) {
				dataList.addAll(list);
				mHandler.sendEmptyMessage(PARSE_OK);
			} else {
				mHandler.sendEmptyMessage(LOAD_MORE_FINISH);
			}
		} else {
			dataList = list;
			mHandler.sendEmptyMessage(PARSE_OK);
		}
	}

	/**
	 * 重写父类方法，如果数据少于20条，则不显示“查看更多”按钮
	 */
	@Override
	protected void afterSuccessParse() {
		if (dataList == null || dataList.isEmpty() || dataList.size() < 20) {
			elv_search.removeFooterView(loadMoreView);
		}
		super.afterSuccessParse();
	}
}
