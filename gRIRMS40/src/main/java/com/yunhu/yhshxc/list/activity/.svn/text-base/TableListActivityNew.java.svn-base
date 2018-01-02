package com.yunhu.yhshxc.list.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.ChangeModuleFuncActivity;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Query;
import com.yunhu.yhshxc.bo.ReplenishSearchResult;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.bo.SubmitItem;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.QueryDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.database.SubmitItemDB;
import com.yunhu.yhshxc.database.SubmitItemTempDB;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.module.replenish.QueryFuncActivity;
import com.yunhu.yhshxc.parser.ReplenishParse;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.UrlInfo;
import com.yunhu.yhshxc.widget.ToastOrder;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gcg.org.debug.ELog;
import gcg.org.debug.JLog;

import static com.yunhu.yhshxc.R.color.darkGray;

/**
 * @author 王建雨 2012.7.25
 */
public class TableListActivityNew extends AbsSearchListActivityNew {

	public static final String TAG_IS_LINK = "is_link";
	public static final String TAG_IS_HISTORY = "is_history";

	// 需要在列表中显示的Func
	public List<Func> viewCoum;
	// 需要在下面显示的Func
	public List<Func> unViewCoum;
	// 按钮显示的名字
	public String btName;
	public int unitViewWidth;
	private String linkJson;// 超链接时使用
	private String sqlLinkJson;// sql超链接时使用
	private int funcId = 0;// 拜访中超链接历史数据时使用,超链接控件ID
	private Func linkFunc;// 拜访中超链接历史数据时使用
	private int storeId = 0;// 拜访中超链接历史数据时使用

	protected boolean isLink;
	protected boolean isHistory;
	protected ViewGroup queryIconContainer;
	protected ImageView imgQuery;
    protected Button search_btn;
	protected Bitmap bitmapQuery, bitmapQueryPressed;
	public int menuId;
//	public int menuType;
	private String menuType2;//针对拜访中跳转进来使用
	public String menuName;
	@Override
	protected void init() {
		super.init();
		isLink = bundle.getBoolean(TAG_IS_LINK, false);
		isHistory = bundle.getBoolean(TAG_IS_HISTORY, false);
		menuType2 = bundle.getString("menuType2");
		imgQuery = (ImageView) findViewById(R.id.img_query);
        search_btn = (Button) findViewById(R.id.search_btn);
        queryIconContainer = (ViewGroup) imgQuery.getParent();
		if (!isLink && !isHistory) {
			queryIconContainer.setVisibility(View.VISIBLE);
			queryIconContainer.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							imgQuery.setImageResource(R.drawable.query_pressed);
							break;
						case MotionEvent.ACTION_UP:
							// 查询跳转
							imgQuery.setImageResource(R.drawable.query);
							intentQuery();
//							finish();
							break;
						case MotionEvent.ACTION_CANCEL:
						case MotionEvent.ACTION_OUTSIDE:
							imgQuery.setImageResource(R.drawable.query);
							break;
					}
					return true;
				}
			});
		}
		search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentQuery();
            }
        });
	}

	/**
	 * 查询
	 * 
	 * @param
	 */
	private void intentQuery() {
		
		Bundle replenishBundle = setQueryBundle();
		
		// 查找数据库中所有的查询条件
		List<Func> funcList = new FuncDB(this).findFuncListReplenish(module == null?targetId : module.getMenuId(), null, Func.IS_Y, null);
		if (funcList != null && !funcList.isEmpty()) {// 如果有查询条件就跳转到查询条件页面
			Intent intent = new Intent(this, QueryFuncActivity.class);
			intent.putExtra("bundle", replenishBundle);
			startActivity(intent);
		}
		else {// 如果没有查询条件就直接跳转到查询列表页面
			Intent intent = new Intent(this, QueryFuncActivity.class);
			intent.putExtra("bundle", replenishBundle);
			startActivity(intent);
		}

	}
	
	protected Bundle setQueryBundle(){
		Bundle replenishBundle = new Bundle();
		replenishBundle.putInt("planId", Constants.DEFAULTINT);// 计划ID
		replenishBundle.putInt("awokeType", Constants.DEFAULTINT);// 提醒类型
		replenishBundle.putInt("wayId", Constants.DEFAULTINT);// 线路ID
		replenishBundle.putInt("storeId", Constants.DEFAULTINT);// 店面ID
		replenishBundle.putInt("targetId", module == null?targetId:module.getMenuId());// 数据ID
		replenishBundle.putString("storeName", null);// 店面名称
		replenishBundle.putString("wayName", null);// 线路名称
		replenishBundle.putInt("isCheckin", Constants.DEFAULTINT);// 是否要进店定位1 是
																	// 0和null否
		replenishBundle.putInt("isCheckout", Constants.DEFAULTINT);// 是否要离店定位1 是
																	// 0和null否
		replenishBundle.putSerializable("module", module);// 自定义模块实例
		replenishBundle.putInt("menuType", menuType);// 菜单类型
		return replenishBundle;
	}
	
	@Override
	public void initIntentData() {
		super.initIntentData();
		linkJson = getIntent().getStringExtra("linkJson");
		sqlLinkJson = getIntent().getStringExtra("sqlLinkJson");
		if (!TextUtils.isEmpty(linkJson)) {// 如果是超链接回调
			// 不连接网络
			isNeedSearch = false;
			isSqlLink = false;
		}
		else if (!TextUtils.isEmpty(sqlLinkJson)) { // jishen add sql超链接所用
			isNeedSearch = false;
			isSqlLink = true;
		}

		if (bundle != null && !bundle.isEmpty()) {
			funcId = bundle.getInt("funcId");
			storeId = bundle.getInt("storeId");
		}
		if (funcId != Constants.DEFAULTINT) {
			linkFunc = new VisitFuncDB(TableListActivityNew.this).findFuncByFuncId(funcId);
		}
		menuId = bundle.getInt("menuId");
//		menuType = bundle.getInt("menuType");
		menuName = bundle.getString("menuName");
	}

	@Override
	public void initBase() {

		super.initBase();
		addPaging();
	}

	@Override
	protected List<Map<String, String>> getDataListOnThread(String json) throws Exception {
		return parseJson(json);
	}

	@Override
	protected List<Map<String, String>> getDataListOnThread() throws Exception {
		return parseJson(linkJson);
	}

	@Override
	protected List<Map<String, String>> getDataListOnThreadForSql() throws Exception {
		return parseJson(sqlLinkJson);
	}

	private List<Map<String, String>> parseJson(String json) throws Exception {
		// 解析JSON
		ReplenishSearchResult searchResult = new ReplenishParse().parseSearchResult(json);
		this.settingPage(searchResult.getCacherows(), searchResult.getTotal());
		// 补报数据
		List<Map<String, String>> resultList = searchResult.getResultList();
		// 判断没有数据时,隐藏窗体
		return resultList;
	}

	@Override
	protected String getSearchUrl() {
		return UrlInfo.getUrlReplenish(this);
	}


	@Override
	protected void initTitle(List<Func> viewCoum, LinearLayout ll_title) {
		FuncDB funcDB = new FuncDB(this);
		if (linkFunc != null && linkFunc.getMenuId() != null && linkFunc.getMenuId() != Constants.DEFAULTINT) {
			viewCoum = funcDB.findFuncListReplenish(linkFunc.getMenuId(), 1, null, null);
			unViewCoum = funcDB.findFuncListReplenish(linkFunc.getMenuId(), 0, null, null);
		}
		else {
			unViewCoum = funcDB.findFuncListReplenish(targetId, 0, null, null);
		}
		if (viewCoum == null || viewCoum.isEmpty()) {
			ToastOrder.makeText(TableListActivityNew.this, "未配置列,请联系管理员!", ToastOrder.LENGTH_SHORT).show();
			this.finish();
		}
		this.viewCoum = viewCoum;

		unitViewWidth = computeViewWidth(viewCoum.size(), null);
		
		if (funcId == Constants.DEFAULTINT) {//第一位显示状态，如果funcId=0则不是超链接（超链接中的列表则不现实）
//			TextView tv_title_1 = (TextView) View.inflate(this, R.layout.table_list_item_unit, null);
//			tv_title_1.setGravity(Gravity.CENTER);//标题设置居中
//			tv_title_1.setLayoutParams(new LayoutParams(PublicUtils.convertDIP2PX(TableListActivityNew.this, 62), LayoutParams.WRAP_CONTENT));
//			tv_title_1.setText(setString(R.string.activity_after_state));
//			tv_title_1.setTextColor(Color.rgb(255, 255, 255));
//			tv_title_1.setTextSize(18);
//			ll_title.addView(tv_title_1);
		}
		else {
			int target_id = bundle.getInt("target_id", Constants.DEFAULTINT);
			if (target_id != Constants.DEFAULTINT) {
				targetId = target_id;
			}
		}
		for (Func func : viewCoum) {// 循环添加要显示func的列名
//			TextView tv_title = (TextView) View.inflate(this, R.layout.table_list_item_unit, null);
//			int width = func.getColWidth() == null || func.getColWidth() == 0 ? unitViewWidth : func.getColWidth();
//			tv_title.setGravity(Gravity.CENTER);//标题设置居中
//			tv_title.setLayoutParams(new LayoutParams(width, LayoutParams.WRAP_CONTENT));
//			tv_title.setText(func.getName());
//			tv_title.setTextColor(Color.rgb(255, 255, 255));
//			tv_title.setTextSize(18);
//			ll_title.addView(tv_title);
		}
		btName = getButtonName();
		if (linkFunc != null && linkFunc.getIsSearchModify() != null) {
			menuType = Menu.TYPE_MODULE;
		}

	}

	// 获取按钮的名字
	public String getButtonName() {
		if (TextUtils.isEmpty(linkJson)) {
			// if(!(module.getType() == Constants.MODULE_TYPE_QUERY )){
			// return module.getName();
			// }
			if (this.isSqlLink) {
				return setString(R.string.activity_after_modification);
			}
			if (linkFunc != null && linkFunc.getIsSearchModify() != null) {
				if (linkFunc.getIsSearchModify() == 2) {
					return setString(R.string.activity_after_modification);
				}
				else if (linkFunc.getIsSearchModify() == 1) {
					return "";
				}
				else if (linkFunc.getIsSearchModify() == 0) {
					ToastOrder.makeText(TableListActivityNew.this, setString(R.string.activity_after_17), ToastOrder.LENGTH_SHORT).show();
					this.finish();
				}
			}
			if (module != null && module.getType() != null) {
				switch (module.getType()) {
					case Constants.MODULE_TYPE_VERIFY:
						return setString(R.string.activity_after_audit);
					case Constants.MODULE_TYPE_UPDATE:
						return setString(R.string.activity_after_modification);
					case Constants.MODULE_TYPE_REASSIGN:
						return setString(R.string.activity_after_send);
					default:
						return "";
				}
			}
		}
		return "";
	}

	@Override
	protected HashMap<String, String> getSearchParams() {
		// 存放查询条件的map
		HashMap<String, String> searchParams = new HashMap<String, String>();
		searchParams.put(Constants.TASK_ID, String.valueOf(targetId));

		if (funcId != Constants.DEFAULTINT) {// 拜访中超链接历史数据
			searchParams.put("storeid", String.valueOf(storeId));
			searchParams.put("linkCtrlId", String.valueOf(funcId));
		}
		else {
			searchParams.put("type", String.valueOf(modType));
		}
		if (module != null) {
			List<Query> queryList = new QueryDB(this).findQueryByMid(module==null?targetId:module.getMenuId());
			if (queryList != null) {// 判断searchReplenishStr非空
				// 根据"$"分割查询条件 的字符串
				for (Query searchParam : queryList) {// 循环添加查询条件到map中
					if (searchParam.getFuncId().equals("-8968")) {
						searchParams.put("doubleUser", searchParam.getValue().replace("~@@", ","));
					}
					else {
						searchParams.put(searchParam.getFuncId(), searchParam.getValue().replace("~@@", ","));
					}
				}
			}
		}
		ELog.d("Params:" + searchParams);
		return searchParams;
	}

	@Override
	protected int getCurChildrenCount(int groupPosition) {
		if (TextUtils.isEmpty(btName)) {
			Map<String, String> itemData = (Map<String, String>) dataList.get(groupPosition);
			for (Func func : unViewCoum) {
				if (func.getType() != Func.TYPE_BUTTON && func.getType() != Func.TYPE_HIDDEN) {
					String value = itemData.get(func.getFuncId().toString());
					if (!TextUtils.isEmpty(value)) {
						value = getShowValue(itemData, func, value);
						if (!TextUtils.isEmpty(value)) {
							return 1;
						}
					}
				}
			}
			return 0;
		}
		else {
			return 1;
		}
	}

	@Override
	protected View getCurGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		Map<String, String> itemData = (Map<String, String>) dataList.get(groupPosition);
		// 优化...
		GroupViewHodler hodler;
		// 显示的item view
		LinearLayout view;

		if (convertView == null) {// 没有缓存view时
			// 加载item view
			view = (LinearLayout) View.inflate(TableListActivityNew.this, R.layout.table_list_item, null);
			// 初始化ViewHodler
			hodler = new GroupViewHodler();
			// 是否展开
			hodler.iv_isExpanded = (ImageView) view.findViewById(R.id.iv_table_list_item_isExpanded);
			int i = 1;
			if (funcId != Constants.DEFAULTINT) { //如果是超链接则没有“状态”，第一位不用占位
				hodler.tvs = new TextView[viewCoum.size()];
				i = 0;
			}else {  //第一位显示状态，如果funcId=0则不是超链接（超链接中的列表则不现实）
				// 设定hodler.tvs的大小
				hodler.tvs = new TextView[viewCoum.size() + 1];
				hodler.tvs[0] = (TextView) View.inflate(TableListActivityNew.this, R.layout.replenish_list_item_unit, null);
				hodler.tvs[0].setLayoutParams(new LayoutParams(PublicUtils.convertDIP2PX(TableListActivityNew.this, 62), LayoutParams.WRAP_CONTENT));
				view.addView(hodler.tvs[0]);
			}
			// 添加textview到item view
			Func _func = null;
			for (; i < hodler.tvs.length; i++) {
				if (funcId == Constants.DEFAULTINT) {// 如果不为超链接，说明列表第一位有一个占位（状态），
					_func = viewCoum.get(i - 1); // 则需要减去占位，才是当前控件
				} else {
					_func = viewCoum.get(i);
				}
				// 添加一个位置
				hodler.tvs[i] = (TextView) View.inflate(TableListActivityNew.this,R.layout.replenish_list_item_unit, null);
				// 添加列宽，如果列宽配置为0，则现实默认列宽
				int width = _func.getColWidth() == null ||_func.getColWidth() == 0 ? unitViewWidth : _func.getColWidth();
				hodler.tvs[i].setLayoutParams(new LayoutParams(width, LayoutParams.WRAP_CONTENT));
				hodler.tvs[i].setGravity(getGravity(_func.getPrintAlignment()));
				hodler.tvs[i].setPadding(2, 0, 2, 0);
//				if(_func.getCheckType() != null && _func.getCheckType() == Func.CHECK_TYPE_NUMERIC){
//					hodler.tvs[i].setGravity(Gravity.RIGHT);
//				}else if (_func.getDataType() != null
//						&& (_func.getDataType() == Func.DATATYPE_SMALL_INTEGER
//						|| _func.getDataType() == Func.DATATYPE_BIG_INTEGER
//						|| _func.getDataType() == Func.DATATYPE_DECIMAL)) { //验证如果控件是数字类型的，右对齐
//					hodler.tvs[i].setGravity(Gravity.RIGHT);
//				}
				// 添加textview到item view
				view.addView(hodler.tvs[i]);
			}

			// 将hodler全部添加到item view 的tag中
			view.setTag(hodler);

		}
		else {
			// 将缓存view赋给item view
			view = (LinearLayout) convertView;
			// 获取item view中的hodler
			hodler = (GroupViewHodler) view.getTag();
		}

		// 从第几列开始赋值
		int j = 1;
		if (funcId != Constants.DEFAULTINT) {
			j = 0;
		}
		else {
			hodler.tvs[0].setText(itemData.get("status_name"));// 状态
		}
		// 循环可被显示的func
		for (Func func : viewCoum) {
			if (func.getType() == Func.TYPE_BUTTON) {// 按钮 不显示的 进行下一次循环
				continue;
			}
			// textview显示的内容
			String tvText = itemData.get(func.getFuncId().toString()) != null ? itemData.get(func.getFuncId().toString()) : "";
			tvText = getShowValue(itemData, func, tvText);

			if (func.getType() == Func.TYPE_RECORD || 
				func.getType() == Func.TYPE_VIDEO || 
				func.getType() == Func.TYPE_ATTACHMENT || 
				func.getType() == Func.TYPE_CAMERA || 
				func.getType() == Func.TYPE_CAMERA_HEIGHT || 
				func.getType() == Func.TYPE_CAMERA_MIDDLE || 
				func.getType() == Func.TYPE_CAMERA_CUSTOM ||
				func.getType() == Func.TYPE_TABLECOMP || 
				func.getType() == Func.TYPE_LINK || 
				(func.getCheckType() != null && 
				(func.getCheckType() == Func.CHECK_TYPE_MOBILE_TELEPHONE || func.getCheckType() == Func.CHECK_TYPE_FIXED_TELEPHONE))) {
				hodler.tvs[j].setClickable(true);

				hodler.tvs[j].setTextColor(Color.rgb(0, 0, 255));
				hodler.tvs[j].getPaint().setUnderlineText(true);
				hodler.tvs[j].setId(groupPosition);
				hodler.tvs[j].setTag(func);
				if (func.getCheckType() != null && (func.getCheckType() == Func.CHECK_TYPE_MOBILE_TELEPHONE || func.getCheckType() == Func.CHECK_TYPE_FIXED_TELEPHONE)) {
					hodler.tvs[j].setTextSize(14);
					if (!TextUtils.isEmpty(tvText)) {
						PublicUtils.intentPhone(TableListActivityNew.this, tvText, hodler.tvs[j]);
					}
					else {
						hodler.tvs[j].setClickable(false);
					}
				}
				else {
					hodler.tvs[j].setTextSize(16);
					hodler.tvs[j].setOnClickListener(this);
				}
			}
			else {

				hodler.tvs[j].setTextSize(14);
				hodler.tvs[j].setTextColor(Color.rgb(0, 0, 0));
				hodler.tvs[j].setClickable(false);
				hodler.tvs[j].getPaint().setUnderlineText(false);
			}
			// 设置textview显示的内容
			hodler.tvs[j].setText(tvText);
			// 下一条func对应的textview的索引
			j++;
		}

		hodler.iv_isExpanded.setImageResource(isExpanded ? R.drawable.icon_reduce : R.drawable.icon_plus);

		return view;
	}
	
	public void btnControl(View parentView,int groupPosition,BtnBo bo){
		Button bt_do = (Button) parentView.findViewById(R.id.bt_table_list_item_children_do);
		if (!TextUtils.isEmpty(btName)) {
			bt_do.setVisibility(View.VISIBLE);
			bt_do.setText(btName);
			bt_do.setOnClickListener(this);
			bt_do.setId(groupPosition);
			if (this.isSqlLink) {//超链接web数据的时候，如果修改跳转查询不到控件的话修改控件就不显示
				List<Func> funcList = new FuncDB(this).findFuncListByTargetidReplash(targetId,bo.getStatus(),true);
				if (funcList.isEmpty()) {
					bt_do.setVisibility(View.GONE);
				}
			}
		}else {
			bt_do.setVisibility(View.GONE);
		}

	}
	
	@Override
	protected View getCurChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		Map<String, String> itemData = (Map<String, String>) dataList.get(groupPosition);
		// 显示的item view
		RelativeLayout view = (RelativeLayout) View.inflate(TableListActivityNew.this, R.layout.table_list_item_children, null);
		LinearLayout ll_child = (LinearLayout) view.findViewById(R.id.ll_table_list_item_children);
		btnControl(view, groupPosition, new BtnBo(itemData));
		boolean isShowCount = false;
		for (Func func : unViewCoum) {
			String tvValue = itemData.get(func.getFuncId().toString()) != null ? itemData.get(func.getFuncId().toString()) : "";
			if (!TextUtils.isEmpty(tvValue)) {

				//对应演进 277 
//				if (func.getDefaultType() != null && func.getDefaultType() == Func.DEFAULT_TYPE_SQL) {
//					continue;
//				}

				if (func.getType() == Func.TYPE_BUTTON) {// 按钮 连接 不显示的 进行下一次循环
					continue;
				}else {
					tvValue = getShowValue(itemData, func, tvValue);
					if (!TextUtils.isEmpty(tvValue)) {
						LinearLayout ll_unit = (LinearLayout) View.inflate(TableListActivityNew.this, R.layout.table_list_item_children_unit, null);
						TextView tv_name = (TextView) ll_unit.findViewById(R.id.tv_table_item_children_unit_name);
						TextView tv_value = (TextView) ll_unit.findViewById(R.id.tv_table_item_children_unit_value);
						tv_name.setText(func.getName());
						tv_value.setText(tvValue);
						if (func.getType() == Func.TYPE_RECORD || 
							func.getType() == Func.TYPE_VIDEO || 
							func.getType() == Func.TYPE_ATTACHMENT || 
							func.getType() == Func.TYPE_CAMERA || 
							func.getType() == Func.TYPE_CAMERA_HEIGHT || 
							func.getType() == Func.TYPE_CAMERA_MIDDLE || 
							func.getType() == Func.TYPE_CAMERA_CUSTOM ||
							func.getType() == Func.TYPE_TABLECOMP || 
							func.getType() == Func.TYPE_LINK || 
							(func.getCheckType() != null && 
							(func.getCheckType() == Func.CHECK_TYPE_MOBILE_TELEPHONE || 
							func.getCheckType() == Func.CHECK_TYPE_FIXED_TELEPHONE))) {
							tv_value.setTextSize(16);
							tv_value.setTextColor(Color.rgb(0, 0, 255));
							// tv_value.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
							// tv_value.getPaint().setFakeBoldText(true);
							tv_value.getPaint().setUnderlineText(true);
							// tv_value.getPaint().setSubpixelText(true);
							tv_value.setOnClickListener(this);
							tv_value.setId(groupPosition);
							tv_value.setTag(func);

							if (func.getCheckType() != null && (func.getCheckType() == Func.CHECK_TYPE_MOBILE_TELEPHONE || func.getCheckType() == Func.CHECK_TYPE_FIXED_TELEPHONE)) {
								tv_value.setTextSize(14);
								PublicUtils.intentPhone(TableListActivityNew.this, tvValue, tv_value);
							}else {
								tv_value.setTextSize(16);
								tv_value.setOnClickListener(this);
							}

						}
						ll_child.addView(ll_unit);
						isShowCount = true;
					}
				}
			}
		}
		if (isShowCount) {
			ll_child.setVisibility(View.VISIBLE);
		}
		else {
			ll_child.setVisibility(View.GONE);
		}
		return view;
	}

	@Override
	protected View getItemView(int i, View convertView, ViewGroup viewGroup) {
		Map<String, String> itemData = (Map<String, String>) dataList.get(i);
		// 显示的item view
		RelativeLayout view = (RelativeLayout) View.inflate(TableListActivityNew.this, R.layout.table_list_item_children_new, null);
		LinearLayout ll_child = (LinearLayout) view.findViewById(R.id.ll_table_list_item_children);
		btnControl(view, i, new BtnBo(itemData));
		boolean isShowCount = false;
        /***********************************************************************/

		if (funcId == Constants.DEFAULTINT) {//第一位显示状态，如果funcId=0则不是超链接（超链接中的列表则不现实）
			String str = itemData.get("status_name");
			if(!TextUtils.isEmpty(str)){
				LinearLayout ll_unit = (LinearLayout) View.inflate(TableListActivityNew.this, R.layout.table_list_item_children_unit_new, null);
				TextView tv_name = (TextView) ll_unit.findViewById(R.id.tv_table_item_children_unit_name);
				TextView tv_value = (TextView) ll_unit.findViewById(R.id.tv_table_item_children_unit_value);
				tv_value.setTextColor(getResources().getColor(R.color.gray_normal));
				tv_name.setTextColor(getResources().getColor(R.color.gray_normal));
				tv_name.setText(setString(R.string.activity_after_state));
				tv_value.setText(str);
				ll_child.addView(ll_unit);
				isShowCount = true;
			}
		}

        for (Func func : viewCoum) {// 循环添加要显示func的列名


			if (func.getType() == Func.TYPE_BUTTON) {// 按钮 不显示的 进行下一次循环
				continue;
			}
			// textview显示的内容
			String tvText = itemData.get(func.getFuncId().toString()) != null ? itemData.get(func.getFuncId().toString()) : "";
			tvText = getShowValue(itemData, func, tvText);
			if(TextUtils.isEmpty(tvText)){
				continue;
			}
			LinearLayout ll_unit = (LinearLayout) View.inflate(TableListActivityNew.this, R.layout.table_list_item_children_unit_new, null);
			TextView tv_name = (TextView) ll_unit.findViewById(R.id.tv_table_item_children_unit_name);
			TextView tv_value = (TextView) ll_unit.findViewById(R.id.tv_table_item_children_unit_value);
			tv_name.setText(func.getName());
			if (func.getType() == Func.TYPE_RECORD ||
					func.getType() == Func.TYPE_VIDEO ||
					func.getType() == Func.TYPE_ATTACHMENT ||
					func.getType() == Func.TYPE_CAMERA ||
					func.getType() == Func.TYPE_CAMERA_HEIGHT ||
					func.getType() == Func.TYPE_CAMERA_MIDDLE ||
					func.getType() == Func.TYPE_CAMERA_CUSTOM ||
					func.getType() == Func.TYPE_TABLECOMP ||
					func.getType() == Func.TYPE_LINK ||
					(func.getCheckType() != null &&
							(func.getCheckType() == Func.CHECK_TYPE_MOBILE_TELEPHONE || func.getCheckType() == Func.CHECK_TYPE_FIXED_TELEPHONE))) {
				tv_value.setClickable(true);

				tv_value.setTextColor(Color.rgb(0, 0, 255));
				tv_value.getPaint().setUnderlineText(true);
				tv_value.setId(i);
				tv_value.setTag(func);
				if (func.getCheckType() != null && (func.getCheckType() == Func.CHECK_TYPE_MOBILE_TELEPHONE || func.getCheckType() == Func.CHECK_TYPE_FIXED_TELEPHONE)) {
					tv_value.setTextSize(14);
					if (!TextUtils.isEmpty(tvText)) {
						PublicUtils.intentPhone(TableListActivityNew.this, tvText, tv_value);
					}
					else {
						tv_value.setClickable(false);
					}
				}
				else {
					tv_value.setTextSize(16);
					tv_value.setOnClickListener(this);
				}
			}
			else {

				tv_value.setTextSize(14);
				tv_value.setTextColor(getResources().getColor(R.color.gray_normal));
				tv_name.setTextColor(getResources().getColor(R.color.gray_normal));
				tv_value.setClickable(false);
				tv_value.getPaint().setUnderlineText(false);
			}
			// 设置textview显示的内容
			tv_value.setText(tvText);
			// 下一条func对应的textview的索引

			ll_child.addView(ll_unit);
			isShowCount = true;

        }
        /************************************************************************/
		for (Func func : unViewCoum) {
			String tvValue = itemData.get(func.getFuncId().toString()) != null ? itemData.get(func.getFuncId().toString()) : "";
			if (!TextUtils.isEmpty(tvValue)) {

				//对应演进 277
//				if (func.getDefaultType() != null && func.getDefaultType() == Func.DEFAULT_TYPE_SQL) {
//					continue;
//				}

				if (func.getType() == Func.TYPE_BUTTON) {// 按钮 连接 不显示的 进行下一次循环
					continue;
				}else {
					tvValue = getShowValue(itemData, func, tvValue);
					if (!TextUtils.isEmpty(tvValue)) {
						LinearLayout ll_unit = (LinearLayout) View.inflate(TableListActivityNew.this, R.layout.table_list_item_children_unit_new, null);
						TextView tv_name = (TextView) ll_unit.findViewById(R.id.tv_table_item_children_unit_name);
						TextView tv_value = (TextView) ll_unit.findViewById(R.id.tv_table_item_children_unit_value);
						tv_value.setTextColor(getResources().getColor(R.color.gray_normal));
						tv_name.setTextColor(getResources().getColor(R.color.gray_normal));
						tv_name.setText(func.getName());
						tv_value.setText(tvValue);
						if (func.getType() == Func.TYPE_RECORD ||
								func.getType() == Func.TYPE_VIDEO ||
								func.getType() == Func.TYPE_ATTACHMENT ||
								func.getType() == Func.TYPE_CAMERA ||
								func.getType() == Func.TYPE_CAMERA_HEIGHT ||
								func.getType() == Func.TYPE_CAMERA_MIDDLE ||
								func.getType() == Func.TYPE_CAMERA_CUSTOM ||
								func.getType() == Func.TYPE_TABLECOMP ||
								func.getType() == Func.TYPE_LINK ||
								(func.getCheckType() != null &&
										(func.getCheckType() == Func.CHECK_TYPE_MOBILE_TELEPHONE ||
												func.getCheckType() == Func.CHECK_TYPE_FIXED_TELEPHONE))) {
							tv_value.setTextSize(16);
							tv_value.setTextColor(Color.rgb(0, 0, 255));
							// tv_value.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
							// tv_value.getPaint().setFakeBoldText(true);
							tv_value.getPaint().setUnderlineText(true);
							// tv_value.getPaint().setSubpixelText(true);
							tv_value.setOnClickListener(this);
							tv_value.setId(i);
							tv_value.setTag(func);

							if (func.getCheckType() != null && (func.getCheckType() == Func.CHECK_TYPE_MOBILE_TELEPHONE || func.getCheckType() == Func.CHECK_TYPE_FIXED_TELEPHONE)) {
								tv_value.setTextSize(14);
								PublicUtils.intentPhone(TableListActivityNew.this, tvValue, tv_value);
							}else {
								tv_value.setTextSize(16);
								tv_value.setOnClickListener(this);
							}

						}
						ll_child.addView(ll_unit);
						isShowCount = true;
					}
				}
			}
		}
		if (isShowCount) {
			ll_child.setVisibility(View.VISIBLE);
		}
		else {
			ll_child.setVisibility(View.GONE);
		}
		return view;
	}

	/**
	 * 缓存listview中item view-->listview的优化
	 */
	public class GroupViewHodler {
		public ImageView iv_isExpanded;
		public TextView[] tvs;
	}

	/**
	 * 跳转单击事件
	 */
	@Override
	public void onClick(View v) {
		// 获取点击条目的map
		Map<String, String> clickItem = (Map<String, String>) dataList.get(v.getId());

		if (v.getTag() != null) {
			if (TextUtils.isEmpty(clickItem.get(((Func) v.getTag()).getFuncId().toString()))) {
				ToastOrder.makeText(TableListActivityNew.this, setString(R.string.activity_after_18), ToastOrder.LENGTH_SHORT).show();
				return;
			}
			Intent intent = null;
			int type = ((Func) v.getTag()).getType();
			if (type == Func.TYPE_CAMERA || type == Func.TYPE_CAMERA_MIDDLE || type == Func.TYPE_CAMERA_HEIGHT || type == Func.TYPE_CAMERA_CUSTOM) {
				intent = new Intent(TableListActivityNew.this, ShowImageActivity.class);
				intent.putExtra("imageUrl", clickItem.get(((Func) v.getTag()).getFuncId().toString()));
				intent.putExtra("imageName", ((Func) v.getTag()).getName());
			}else if (type == Func.TYPE_TABLECOMP) {
				int menutype = menuType;
				if (funcId != Constants.DEFAULTINT) {
					menutype = Menu.TYPE_MODULE;
				}
				intent = new Intent(TableListActivityNew.this, ShowTableActivity.class);
				intent.putExtra("menuType", menutype);
				intent.putExtra("tableId", ((Func) v.getTag()).getTableId());
				intent.putExtra("funcName", ((Func) v.getTag()).getName());
				intent.putExtra("tableJson", clickItem.get(((Func) v.getTag()).getFuncId().toString()));
			}
			else if (type == Func.TYPE_LINK) {
				ELog.d("Open TableListActivity");
				intent = new Intent(TableListActivityNew.this, TableListActivityNew.class);
				bundle.putBoolean(TAG_IS_LINK, true);
				bundle.putInt("menuType", menuType);
				bundle.putInt("modType", modType);
				if (funcId != Constants.DEFAULTINT) {
					bundle.putInt("funcId", Constants.DEFAULTINT);
					bundle.putInt("planId", Constants.DEFAULTINT);
					bundle.putInt("wayId", Constants.DEFAULTINT);
					bundle.putInt("menuType", Menu.TYPE_MODULE);
				}
				bundle.putInt("targetId", ((Func) v.getTag()).getMenuId());
				intent.putExtra("bundle", bundle);
				intent.putExtra("linkJson", clickItem.get(((Func) v.getTag()).getFuncId().toString()));
			}else if (type == Func.TYPE_RECORD) {
				String url = clickItem.get(((Func) v.getTag()).getFuncId().toString());// 音频资源地址
				intent = new Intent(TableListActivityNew.this, RecordAuditionActivity.class);
				intent.putExtra("url", url);
				if (TextUtils.isEmpty(url)) {
					ToastOrder.makeText(TableListActivityNew.this, setString(R.string.activity_after_19), ToastOrder.LENGTH_SHORT).show();
					return;
				}
			}else if(type == Func.TYPE_VIDEO){
				String url = clickItem.get(((Func) v.getTag()).getFuncId().toString());// 视频资源地址
				VideoReview vReview = new VideoReview(this);
				vReview.readAttachment(url);
			}else if(type == Func.TYPE_ATTACHMENT){
				String url = clickItem.get(((Func) v.getTag()).getFuncId().toString());// 文件资源地址
				AttachmentReview aReview = new AttachmentReview(this);
				aReview.readAttachment(url);
			}
			if (intent != null) {
				startActivity(intent);
			}
		}else {
			if (linkFunc != null && linkFunc.getIsSearchModify() != null && linkFunc.getIsSearchModify() != 2) {
				ToastOrder.makeText(TableListActivityNew.this, setString(R.string.activity_after_17), ToastOrder.LENGTH_SHORT).show();
				this.finish();
			}
			// 跳转的Activity
			Intent intent = new Intent(TableListActivityNew.this, ChangeModuleFuncActivity.class);
			intent.putExtra("isNoWait", isNoWait);
			intent.putExtra("menuType2", menuType2);
			// 传递getFuncActivity的Bundle
			// Bundle bundle = new Bundle();
			// 存放targetId
			int targetid = targetId;
			int menutype = menuType;
			if (funcId != Constants.DEFAULTINT) {
				targetid = linkFunc.getMenuId();
				menutype = Menu.TYPE_MODULE;
				bundle.putInt("planId", Constants.DEFAULTINT);
				bundle.putInt("wayId", Constants.DEFAULTINT);
				bundle.putBoolean("isLinkHistory", true);// 告诉页面是超链接历史跳转过去的，这样返回的时候不关闭本页面
			}
			// 存放data-status
			bundle.putInt("menuType", menutype);
			bundle.putInt("targetId", targetid);
			bundle.putInt("modType", modType);
			bundle.putBoolean("isSqlLink", isSqlLink);
			bundle.putString("sqlLinkJson", sqlLinkJson);
			bundle.putInt("taskId", Integer.parseInt(clickItem.get(Constants.TASK_ID)));
			bundle.putString(Constants.DATA_STATUS, clickItem.get(Constants.DATA_STATUS));
			bundle.putSerializable("module", module);
			bundle.putString("buttonName", getButtonName());
			bundle.putInt("is_store_expand", is_store_expand);
			// 保存数据到数据库
			SubmitDB submitDB = new SubmitDB(TableListActivityNew.this);
			SubmitItemDB submitItemDB = new SubmitItemDB(TableListActivityNew.this);
			ArrayList<String> orgString = new ArrayList<String>();

			saveData(targetid, menutype, clickItem, submitDB, submitItemDB, orgString, false, 0, null);
			if (orgString != null && !orgString.isEmpty()) {
				bundle.putStringArrayList("map", orgString);
			}

			// int submitId = submitDB.selectSubmitIdNotCheckOut(null, null,
			// null,targetId, menuType,Submit.STATE_NO_SUBMIT);
			// bundle.putInt("linkId", submitId);//submitID访店中超链接修改使用jishen
			bundle.putInt("funcId", funcId);// jishen添加20121026超链接历史数据到FuncActivity时取不到funcId删除数据库信息
			// 在intent中放入bundle
			intent.putExtra("bundle", bundle);
			if (funcId != Constants.DEFAULTINT) {
				startActivityForResult(intent, R.id.link_history_return);
			}
			else {
				startActivity(intent);
				this.finish();
			}
		}
	}

	/**
	 * 保存到数据库
	 * 
	 * @param targetId
	 * @param saveItem
	 * @param submitDB
	 * @param submitItemDB
	 * @param orgString
	 */
	public void saveData(int targetId, int menuType, Map<String, String> saveItem, SubmitDB submitDB, SubmitItemDB submitItemDB, ArrayList<String> orgString, boolean isLink, int submitParentId, Func linkItemFunc) {
		cleanCurrentNoSubmit(targetId);
		if (saveItem == null || saveItem.isEmpty()) {
			return;
		}
		Submit submit = new Submit();

		submit.setTargetid(targetId);
		submit.setTargetType(menuType);
		if (modType!=0) {
			submit.setModType(modType);
		}
		if (!TextUtils.isEmpty(saveItem.get(Constants.PATCH_ID))) {
			submit.setTimestamp(saveItem.get(Constants.PATCH_ID));
			JLog.d(TAG, "PATCH_ID=========>" + saveItem.get(Constants.PATCH_ID));
		}
		if (!TextUtils.isEmpty(saveItem.get(Constants.TASK_ID))) {
			submit.setDoubleId(Integer.valueOf(saveItem.get(Constants.TASK_ID)));
		}
		if (linkFunc != null && linkFunc.getIsSearchModify() != null && linkFunc.getIsSearchModify() == 2) {
			submit.setStoreId(storeId);
		}
		JLog.d(TAG, "clickItem.get(\"taskid\")=========>" + saveItem.get(Constants.TASK_ID));
		submit.setState(Submit.STATE_NO_SUBMIT);
		if (isLink) {
			submit.setParentId(submitParentId);
			Submit parentSubmit = submitDB.findSubmitById(submitParentId);
			SubmitItem submitItem = new SubmitItem();
			submitItem.setSubmitId(parentSubmit.getId());
			submitItem.setType(Func.TYPE_LINK);
			submitItem.setParamName(linkItemFunc.getFuncId().toString());
			submitItem.setParamValue(submit.getTimestamp());
			submitItem.setIsCacheFun(linkItemFunc.getIsCacheFun());
			submitItem.setTargetId(linkItemFunc.getTargetid()+"");
			submitItemDB.insertSubmitItem(submitItem,false);
		}
		submit.setMenuId(menuId);
		submit.setMenuType(menuType);
		submit.setMenuName(menuName);
		submitDB.insertSubmit(submit);

		// 通过targetId/targetType/Submit.State查询submitId
		int submitId = submitDB.selectSubmitIdNotCheckOut(null, null, null, targetId, menuType, Submit.STATE_NO_SUBMIT);

		List<Func> funcs = null;
		if (modType == Constants.MODULE_TYPE_REASSIGN) {// 改派
			funcs = new FuncDB(TableListActivityNew.this).findFuncListByDoubleReadOnly(targetId);
		}
		else if (isLink || funcId != Constants.DEFAULTINT) {
			funcs = new FuncDB(TableListActivityNew.this).findFuncListByTargetid(targetId, isSqlLink);
		}
		else {
			funcs = new FuncDB(TableListActivityNew.this).findFuncListByTargetidReplash(targetId, saveItem.get(Constants.DATA_STATUS), isSqlLink);
		}
		StringBuilder sb = new StringBuilder();
		for (Func func : funcs) {// 遍历
//			if (func.getType() == Func.TYPE_VIDEO || func.getType()==Func.TYPE_ATTACHMENT || func.getType() == Func.TYPE_CAMERA || func.getType() == Func.TYPE_CAMERA_HEIGHT || func.getType() == Func.TYPE_CAMERA_CUSTOM || func.getType() == Func.TYPE_CAMERA_MIDDLE || func.getType() == Func.TYPE_BUTTON) {
//				continue;
//			}
			
			if (!TextUtils.isEmpty(saveItem.get(func.getFuncId().toString()))) {
				if (func.getType() == Func.TYPE_LINK) {
					saveData(func.getMenuId(), menuType, parseLinkJson(saveItem.get(func.getFuncId().toString())), submitDB, submitItemDB, orgString, true, submitId, func);
				}else {
					SubmitItem submitItem = new SubmitItem();
					submitItem.setSubmitId(submitId);
					submitItem.setType(func.getType());
					submitItem.setIsCacheFun(func.getIsCacheFun());
					submitItem.setTargetId(func.getTargetid()+"");
					if (func.getOrgOption()!=null && func.getOrgOption()== Func.ORG_STORE) {
						submitItem.setNote(SubmitItem.NOTE_STORE);
					}
					if (func.getOrgOption()!=null && func.getOrgOption() == Func.OPTION_LOCATION) {
						SubmitItem storeItem = new SubmitItemDB(this).findSubmitItemByNote(submitId, SubmitItem.NOTE_STORE);
						if (storeItem!=null && !TextUtils.isEmpty(storeItem.getParamValue())) {
							submitItem.setNote(storeItem.getParamName());
						}
					}
					submitItem.setParamName(func.getFuncId().toString());
					if (func.getType() == Func.TYPE_SQL_BIG_DATA) {
						String json = saveItem.get(func.getFuncId().toString());
						try {
							JSONArray array = new JSONArray(json);
							submitItem.setParamValue(array.getString(0));
							submitItem.setNote(array.getString(1));
						}
						catch (JSONException e) {
							e.printStackTrace();
						}
					}else {
						submitItem.setParamValue(saveItem.get(func.getFuncId().toString()));
					}
					submitItemDB.insertSubmitItem(submitItem,false);
					JLog.d(TAG, "submitItem=====ParamName-->" + submitItem.getParamName() + "<=====>ParamValue-->" + submitItem.getParamValue());
					if ((func.getType() == Func.TYPE_SELECT_OTHER || func.getType() == Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP) && saveItem.get(func.getFuncId().toString()).equals("-1")) {
						SubmitItem submitItem_other = new SubmitItem();
						submitItem_other.setSubmitId(submitId);
						submitItem_other.setType(func.getType());

						submitItem_other.setParamName(func.getFuncId().toString() + "_other");
						submitItem_other.setParamValue(saveItem.get(func.getFuncId().toString() + "_other"));
						submitItemDB.insertSubmitItem(submitItem_other,false);
					}
					if (func.getOrgOption() != null && func.getOrgOption() == Func.ORG_OPTION) {
						sb.append(";").append(func.getOrgLevel()).append(":").append(saveItem.get(func.getFuncId().toString()));
					}
					if (func.getType() == Func.TYPE_PRODUCT_CODE && !TextUtils.isEmpty(saveItem.get("compare_content"))) {
						SharedPreferencesUtil.getInstance(this).setCompareContent("content_"+func.getTargetid(),saveItem.get("compare_content"));
					}
				}
			}
		}
		if (sb != null && sb.length() > 1) {
			orgString.add(targetId + sb.toString());
		}
	}
	
	/**
	 * 解析超链接json
	 * 
	 * @param linkJson
	 * @return
	 */
	public Map<String, String> parseLinkJson(String linkJson) {
		List<Map<String, String>> linkItemList = null;
		try {
			linkItemList = parseJson(linkJson);
			if (linkItemList == null || linkItemList.isEmpty()) {
				return null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return linkItemList.get(0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != R.id.link_history_return) {
			this.finish();
		}
	}
	
	public void cleanCurrentNoSubmit(Integer targetId) {
		SubmitItemDB submitItemDB = new SubmitItemDB(this);
		SubmitItemTempDB submitItemTempDB = new SubmitItemTempDB(this);
		SubmitDB submitDB = new SubmitDB(this);
		List<Submit> notSubmit = new SubmitDB(this).findAllSubmitDataByStateAndTargetId(Submit.STATE_NO_SUBMIT, targetId);
		if (!notSubmit.isEmpty()) {
			for (int i = 0; i < notSubmit.size(); i++) {
				int id = notSubmit.get(i).getId();
				submitDB.deleteSubmitById(id);
				submitItemTempDB.deleteSubmitItemBySubmitId(id);// 清除临时表中的数据
				List<SubmitItem> itemList = submitItemDB.findPhotoSubmitItemBySubmitId(id);
				if (!itemList.isEmpty()) {// 如果有照片的话删除照片
					for (int k = 0; k < itemList.size(); k++) {
						String tempPath = Constants.SDCARD_PATH + itemList.get(k).getParamValue();
						File tempFile = new File(tempPath);
						if (tempFile.exists()) {
							tempFile.delete();
						}
					}
				}
				// 清除录音文件
				List<SubmitItem> recordItemList = new SubmitItemDB(this).findSubmitItemByType(Func.TYPE_RECORD);
				if (!recordItemList.isEmpty()) {
					for (int k = 0; k < recordItemList.size(); k++) {
						String tempPath = Constants.RECORD_PATH + recordItemList.get(k).getParamValue();
						File tempFile = new File(tempPath);
						if (tempFile.exists()) {
							tempFile.delete();
						}
					}
				}
				submitItemDB.deleteSubmitItemBySubmitId(id);
				// 清楚所关联的超链接数据
				List<Submit> linkSubmit = new SubmitDB(this).findSubmitByParentId(id);
				if (!linkSubmit.isEmpty()) {
					for (int j = 0; j < linkSubmit.size(); j++) {
						cleanCurrentNoSubmit(linkSubmit.get(j).getTargetid());
					}
				}
			}
		}
	}

	/**
	 * 根据align返回Gravity值
	 * 用来设置单元格的位置
	 * 
	 * @param align
	 * @return
	 */
	private int getGravity(int align){
		if(align == 0){
			return Gravity.LEFT;
		}
		if(align == 3){
			return Gravity.CENTER;
		}else if(align == 1){
			return Gravity.LEFT;
		}else if(align == 2){
			return Gravity.RIGHT;
		}else{
			return Gravity.LEFT;
		}
	}
	
	public class BtnBo{
		private String status;
		private String taskid;
		private Map<String, String> itemData;
		public BtnBo(Map<String, String> itemData) {
			this.status = itemData.get(Constants.DATA_STATUS);
			this.taskid = itemData.get(Constants.TASK_ID);
			this.itemData = itemData;
		}
		public String getStatus() {
			return status;
		}
		public String getTaskid() {
			return taskid;
		}
		public Map<String, String> getItemData() {
			return itemData;
		}
		
	}
	private String setString(int stringId){
		return getResources().getString(stringId);
	}
}
