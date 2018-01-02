package com.yunhu.yhshxc.list.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.C;
import com.loopj.android.http.RequestParams;
import com.yunhu.android.pulltorefresh.library.PullToRefreshBase;
import com.yunhu.android.pulltorefresh.library.PullToRefreshListView;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.bo.Dictionary;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.bo.Org;
import com.yunhu.yhshxc.bo.OrgStore;
import com.yunhu.yhshxc.bo.OrgUser;
import com.yunhu.yhshxc.comp.page.PagingAdapter;
import com.yunhu.yhshxc.database.DictDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.OrgDB;
import com.yunhu.yhshxc.database.OrgStoreDB;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.dialog.MyProgressDialog;
import com.yunhu.yhshxc.http.GcgHttpClient;
import com.yunhu.yhshxc.http.HttpResponseListener;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gcg.org.debug.JLog;

/**
 * AbsSearchListActivity是各种列表模块的抽象基类，提供构造页面、获取数据、解析数据、翻页功能
 * 子类通过实现抽象方法或重写AbsSearchListActivity的方法为AbsSearchListActivity提供数据和特殊界面
 * 
 * @version 2013.6.3
 * @author wangchao
 * 
 */
public abstract class AbsSearchListActivityNew extends AbsBaseActivity {

	public boolean isRecordIng = false;// 是否正在播放录音 true表示正在播放录音 false反之

	/**
	 * 解析完成常量
	 */
	protected final int PARSE_OK = 98;

	/**
	 * 解析失败常量
	 */
	protected final int PARSE_FAIL = 97;

	/**
	 * 加载更多常量
	 */
	protected final int LOAD_MORE_FINISH = 99;

	/**
	 * 查询url
	 */
	private String baseUrlSearch = null;

	/**
	 * 显示补报信息ListView
	 */
	public PullToRefreshListView elv_search;

	/**
	 * elv_search用的适配器
	 */
	private SearchAdapter mAdapter = null;

	/**
	 * 请求网络的参数
	 */
	private HashMap<String, String> params;

	/**
	 * 加载对话框
	 */
	private Dialog dialog = null;

	/**
	 * 数据ID
	 */
	protected int targetId;

	/**
	 * 自定义模块中二级菜单item类型 参考Constants.MODULE_TYPE_*常量组
	 */
	protected int modType;

	/**
	 * 菜单类型 参考Menu.TYPE_*
	 */
	protected int menuType;

	/**
	 * 服务器返回json,解析后的数据 map<funcId,value>
	 */
	protected List<?> dataList = new ArrayList<>();

	/**
	 * Bundle
	 */
	protected Bundle bundle;

	/**
	 * @see Module
	 */
	protected Module module = null;

	/**
	 * 是否需要搜索
	 */
	protected boolean isNeedSearch = true;
	protected boolean isSqlLink = false;
	
	
	protected int is_store_expand;//判断是否是店面拓展模块
	/**
	 * 页面layout的资源id
	 */
	private int layout_id = 0;

	/**
	 * 翻页常量
	 */
	public final String PAGE = "page";

	/**
	 * 底部的翻页栏的容器（包括TIP组件），如果需要翻页，就显示，否则隐藏
	 */
	private View ll_paging = null;

	private View ll_paging2 = null;

	/**
	 * 底部的翻页栏的容器（不包括TIP组件），如果需要翻页，就显示，否则隐藏
	 */
	private View ll_paging_cotrol = null;

	/**
	 * 翻页页数列表的容器(翻页栏中间按钮)
	 */
	private View ll_page_mid_list = null;

	/**
	 * 显示翻页的按钮
	 */
	private View ll_page_tip = null;

	/**
	 * 翻页页数列表
	 */
	private ListView pageMidListView = null;

	/**
	 * 显示页数的组件
	 */
	private TextView tv_paging_count = null;

	/**
	 * 总页数
	 */
	protected int pageCount = 0;

	/**
	 * 当前页数
	 */
	public int currentPage = 0;

	/**
	 * 上一页
	 */
	public int lastPage = 0;

	/**
	 * 每页数量
	 */
	protected int EachPageNum = 20;

	/**
	 * 表格每列的宽度
	 */
	protected int columnDefaultWidth = 150;

	public boolean isNoWait;

	
	/**
	 * title
	 */
	public TextView txt_title_bar_title;

	
	
	/**
	 * Activity入口
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		if (isNeedSearch) {
			postSearch();
		} else if (isSqlLink) {
			doInbackGroundForSql();
		} else {
			doInbackGround();
		}
	}

	/**
	 * 接受网络返回数据的Handler
	 */
	protected Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// case 100: // 数据返回成功
			// if (msg.obj != null) {
			// // 获取JSON
			// String json = (String) msg.obj;
			// // 判断是否返回 "resultcode":"0001"
			// if ("{\"resultcode\":\"0001\"}".equals(json)) {
			// JLog.d(TAG, "msg.obj   ====json===>>>>>" + msg.obj.toString());
			// fail("没有查询到相关数据!");
			// return;
			// }
			// JLog.d(TAG, "JSON   =======>>>>>" + json);
			// doInbackGround(json);
			// }
			// else {
			// fail("连接异常,请稍后重试!");
			// }
			// break;
			case PARSE_OK:
				afterSuccessParse();
				break;
			case PARSE_FAIL:
				--currentPage;
				fail(getResources().getString(R.string.activity_after_01));
				break;
			case LOAD_MORE_FINISH:
				--currentPage;
				fail(getResources().getString(R.string.activity_after_03));
				break;
			default:
				--currentPage;
				fail(getResources().getString(R.string.activity_after_02));
				break;
			}
		}
	};

	/**
	 * 提示错误信息
	 * 
	 * @param message
	 *            错误信息
	 */
	private void fail(String message) {
		dismissLoadDialog();// 关闭加载对话框
		ToastOrder.makeText(AbsSearchListActivityNew.this, message, ToastOrder.LENGTH_LONG).show();
	}

	/**
	 * 解析完在主线程中操作 可继承
	 */
	protected void afterSuccessParse() {
		if (dataList == null || dataList.isEmpty()) {
			fail(getResources().getString(R.string.activity_after_01));
			if (ll_paging!=null) {
				this.ll_paging.setVisibility(View.GONE);
			}
		} else {
			dismissLoadDialog();// 关闭加载对话框
			mAdapter.notifyDataSetChanged();
			if (layout_id == R.layout.search_list_new) {
//				findViewById(R.id.hsv_search).setVisibility(View.VISIBLE);// 如果有数据就显示列表
				if (this.ll_paging != null) {// 如果存在翻页栏就显示出来
//					this.ll_paging.setVisibility(View.VISIBLE);
					refreshPage();
				}
			}

		}
	}
	
	/**
	 * 
	 * @return 标题名称
	 */
	protected String titleName() {
		return getResources().getString(R.string.activity_after_query);
	}

	/**
	 * 子线程中解析json
	 * 
	 * @param json
	 *            需要解析的JSON字符串
	 */
	private void doInbackGround(final String json) {
		new Thread() {
			public void run() {
				try {
					doInbackGroundInThread(json);
				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(PARSE_FAIL);
				}
			};
		}.start();
	}

	/**
	 * 解析JSON数据，由doInbackGround()方法调用
	 * 
	 * @param json
	 *            需要解析的json数据
	 * @throws Exception
	 *             各种可能出现的异常
	 */
	protected void doInbackGroundInThread(String json) throws Exception {
		// 获取要显示的数据的list-->由子类重写改方法

//		dataList = getDataListOnThread(json);
		/***************************************/
		List list = getDataListOnThread(json);
		dataList.addAll(list);
		/***************************************/
		mHandler.sendEmptyMessage(PARSE_OK);
	}

	/**
	 * 子线程中解析json
	 */
	private void doInbackGround() {
		new Thread() {
			public void run() {
				try {
					// 获取要显示的数据的list-->由子类重写改方法

//					dataList = getDataListOnThread();
					/***********************************************/
					List list = getDataListOnThread();
					dataList.addAll(list);
					/**********************************************/
					mHandler.sendEmptyMessage(PARSE_OK);
				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(PARSE_FAIL);
				}
			}

		}.start();
	}

	/**
	 * 子线程中解析SQL超链接时使用的查询数据
	 */
	private void doInbackGroundForSql() {
		new Thread() {
			public void run() {
				try {
					// 获取要显示的数据的list-->由子类重写改方法
//					dataList = getDataListOnThreadForSql();
					/*************************************************/
					List list =getDataListOnThreadForSql();
					dataList.addAll(list);
					/*************************************************/

					mHandler.sendEmptyMessage(PARSE_OK);
				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(PARSE_FAIL);
				}
			}

		}.start();
	}

	/**
	 * SQL超链接时使用的查询
	 * 
	 * @return 返回数据List
	 * @throws Exception
	 *             各种可能出现的异常
	 */
	protected List<?> getDataListOnThreadForSql() throws Exception {
		return null;
	}

	/**
	 * 子类不联网时,继承此方法解析数据
	 * 
	 * @return 返回数据List
	 */
	protected List<?> getDataListOnThread() throws Exception {
		return null;
	}

	/**
	 * 关闭载入对话框
	 */
	public void dismissLoadDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		// 关闭加载对话框
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		super.onDestroy();

	}

	/**
	 * Activity的初始化，用于获取Intent中的Bundle数据并根据getLayoutId()方法获取layout的资源文件，
	 * 从而完成界面的初始化
	 */
	protected void init() {
		// 获取传进来的数据
		initIntentData();
		// 获取Activity的layout资源文件id，子类通过重写该方法来自定义界面，如果返回0则代表使用默认布局文件
		layout_id = getLayoutId();
		if (layout_id == 0) {
			layout_id = R.layout.search_list_new;
		}
		// 设置activity显示布局
		setContentView(layout_id);
		// 获取ListView
		elv_search = (PullToRefreshListView) findViewById(R.id.elv_search_list_new);
		initBase();

		// 隐藏窗体
		if (layout_id == R.layout.search_list_new) {// 调用默认布局
//			findViewById(R.id.hsv_search).setVisibility(View.GONE);
		}
		// 给url赋值-->由子类重写该方法
		baseUrlSearch = getSearchUrl();

		// 如果使用默认的layout或modType为支付，则初始化Title
		if (layout_id == R.layout.search_list_new || modType == Constants.MODULE_TYPE_PAY) {// 默认布局
			// 对title进行初始化
			initTitleView();
		}
		
		
		txt_title_bar_title = (TextView)findViewById(R.id.txt_title_bar_title);
		if (txt_title_bar_title!=null) {
			txt_title_bar_title.setText(titleName());
		}
		// 初始化适配器
		mAdapter = new SearchAdapter();
		elv_search.setAdapter(mAdapter);
		String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		elv_search.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		elv_search.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
//		elv_search.setEmptyView(View.inflate(this, R.layout.pull_refresh_empty, null));
//		elv_search.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//			@Override
//			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
////				refresh();
//			}
//		});

		elv_search.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//				Toast.makeText(AbsSearchListActivityNew.this,"刷新",Toast.LENGTH_SHORT).show();
				elv_search.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//				Toast.makeText(AbsSearchListActivityNew.this,"加载",Toast.LENGTH_SHORT).show();
//				elv_search.onRefreshComplete();
                if(currentPage>=pageCount){
                    ToastOrder.makeText(AbsSearchListActivityNew.this, getResources().getString(R.string.activity_after_03), ToastOrder.LENGTH_LONG).show();
					new Handler().post(new Runnable() {
						@Override
						public void run() {
							elv_search.onRefreshComplete();
						}
					});

                }else{
                    netWorkInThread();
                }
			}
		});
		elv_search.setRefreshing(true);
		
		
	}

	public void initIntentData(){
		bundle = getIntent().getBundleExtra("bundle");
		isNoWait = getIntent().getBooleanExtra("isNoWait", true);
		if (bundle != null && !bundle.isEmpty()) {
			targetId = bundle.getInt("targetId");
			menuType = bundle.getInt("menuType");
			module = (Module) bundle.getSerializable("module");
			if (module != null) {
				modType = module.getType();
			} else {
				modType = bundle.getInt("modType");
			}
			is_store_expand = bundle.getInt("is_store_expand");
		}
		bundle.putBoolean("isNoWait", isNoWait);
	}
	/**
	 * 联网查询数据
	 */
	private void postSearch() {
		// 初始化加载对话框
		dialog = new MyProgressDialog(this, R.style.CustomProgressDialog,getResources().getString(R.string.activity_after_04));
		// 显示加载对话框
		dialog.show();
		params = getSearchParams();// 获取查询参数
		// 加入电话号码
		params.put("phoneno", PublicUtils.receivePhoneNO(this));

//		for (Map.Entry e : params.entrySet()) {
//			JLog.d(TAG, "params  <<< " + e.getKey() + "==>" + e.getValue());
//		}
		JLog.d(TAG, "baseUrlSearch==>" + baseUrlSearch);

		netWorkInThread();
	}

	/**
	 * 在一个线程中用http获取数据
	 */
	protected void netWorkInThread() {
		setPageParams(params);


		RequestParams requestParams = new RequestParams();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			requestParams.put(entry.getKey(), entry.getValue());
			JLog.d(TAG, entry.getKey() + ":" + entry.getValue());
		}
		GcgHttpClient.getInstance(this).post(baseUrlSearch, requestParams,new HttpResponseListener() {
					@Override
					public void onStart() {
					}


					@Override
					public void onFailure(Throwable error, String content) {
//						currentPage = lastPage;
						fail(getResources().getString(R.string.activity_after_02));
					}

					@Override
					public void onFinish() {
						elv_search.onRefreshComplete();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						// 获取JSON
						// 判断是否返回 "resultcode":"0001"
						if ("{\"resultcode\":\"0001\"}".equals(content)) {
							JLog.d(TAG, "msg.obj   ====json===>>>>>" + content);
							--currentPage;
							fail("没有查询到相关数据!");
							return;
						}
						JLog.d(TAG, "JSON   =======>>>>>" + content);
						doInbackGround(content);
						
					}

				});
	}

	/**
	 * 初始化TitleView
	 */
	private void initTitleView() {
		FuncDB funcDB = new FuncDB(this);
		// 通过targetID和是否在列表中显示的标记查询需要在列表中显示的Func
		List<Func> viewCoum = funcDB.findFuncListReplenish(targetId, 1, null,
				null);
		// listview的标题
		LinearLayout ll_title = (LinearLayout) findViewById(R.id.ll_search_list_header);
		// 向标题中添加view-->由子类重写该方法
		initTitle(viewCoum, ll_title);
	}

	/**
	 * 计算每列显示的宽度
	 *
	 * @param showCount
	 *            显示列的个数
	 * @return 单位为px
	 */
	protected int computeViewWidth(int showCount, Func func) {
		if (showCount == 0) {
			return 0;
		}
		int realPX = getWindowManager().getDefaultDisplay().getWidth();
		int realDIP = PublicUtils.convertPX2DIP(AbsSearchListActivityNew.this,
				realPX);
		int tempWidth = realDIP / showCount;

		if (func != null && func.getColWidth() != null) {
			tempWidth = tempWidth + func.getColWidth();
		}
		return PublicUtils.convertDIP2PX(AbsSearchListActivityNew.this,
				columnDefaultWidth >= tempWidth ? columnDefaultWidth
						: tempWidth);


	}



	private final class SearchAdapter extends BaseAdapter{
        public SearchAdapter(){

        }
		@Override
		public int getCount() {
			if (dataList != null) {
				return dataList.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int i) {
			return dataList.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			return getItemView(i, view, viewGroup);
		}
	}

	/**
	 * 获取超链接显示的文字
	 * 
	 * @param itemData
	 *            数据map<funcId,value>
	 * @param func
	 *            Func对象
	 * @param value
	 *            根据funcId从itemData中取出的值
	 * @return 返回超链接显示的文字
	 */
	protected String getShowValue(Map<String, String> itemData, Func func,
			String value) {
		switch (func.getType()) {
		case Func.TYPE_SINGLE_CHOICE_FUZZY_QUERY_COMP:
		case Func.TYPE_SELECTCOMP:
			value = getSelectCompValue(func, value);
			break;
		case Func.TYPE_SINGLE_CHOICE_FUZZY_OTHER_QUERY_COMP:// 模糊搜索其他
		case Func.TYPE_SELECT_OTHER:
			value = getSelectOtherCompValue(func, itemData, value);
			break;
		case Func.TYPE_MULTI_CHOICE_SPINNER_COMP:
		case Func.TYPE_MULTI_CHOICE_FUZZY_QUERY_COMP:// 模糊多选
			value = getMultiSelectCompValue(func, value);
			break;
		case Func.TYPE_CAMERA:
		case Func.TYPE_CAMERA_HEIGHT:
		case Func.TYPE_CAMERA_CUSTOM:
		case Func.TYPE_CAMERA_MIDDLE:
		case Func.TYPE_VIDEO:
		case Func.TYPE_ATTACHMENT:
		case Func.TYPE_TABLECOMP:
		case Func.TYPE_LINK:
			value = getResources().getString(R.string.activity_after_look);
			break;
		case Func.TYPE_LOCATION:
			value = TextUtils.isEmpty(value) ? value : value.split("\\$")[0];
			break;
		case Func.TYPE_RECORD:
			if (isRecordIng) {
				value = getResources().getString(R.string.activity_after_stop);;
			} else {
				value = getResources().getString(R.string.activity_after_play);
			}
			break;
		case Func.TYPE_SQL_BIG_DATA:
			value = getSqlBigData(value);
			break;
		}

		return value;
	}

	protected String getSqlBigData(String json) {
		String value = null;
		try {
			JSONArray array = new JSONArray(json);
			value = array.getString(1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 其他-下拉框
	 * 
	 * @param func
	 *            Func对象
	 * @param itemData
	 *            数据map<funcId,value>
	 * @param key
	 *            数据ID
	 * @return 返回下拉框的文字
	 */
	protected String getSelectOtherCompValue(Func func,
			Map<String, String> itemData, String key) {
		String tvValue = "";
		if (!TextUtils.isEmpty(key)) {
			if ("-1".equals(key)) {
				tvValue = itemData.get(func.getFuncId() + "_other");
			} else {
				tvValue = getSelectCompValue(func, key);
			}
		}
		return tvValue;
	}

	/**
	 * 获取多选下拉框的值
	 * 
	 * @param mIds
	 *            用“,"分割的选项id集合
	 * @return 返回用“,"分割的选项文字
	 */
	protected String getMultiSelectCompValue(Func func, String mIds) {
		String tvValue = "";
		if (!TextUtils.isEmpty(mIds)) {// 判断非空
			if (func.getOrgOption() != null
					&& func.getOrgOption() == Func.ORG_STORE) {

				List<OrgStore> orgStoreList = new OrgStoreDB(
						AbsSearchListActivityNew.this).findOrgListByStoreId(mIds);
				StringBuffer sb = new StringBuffer();
				for (int j = 0; j < orgStoreList.size(); j++) {
					OrgStore orgStore = orgStoreList.get(j);
					sb.append(",").append(orgStore.getStoreName());
				}
				tvValue = sb.length() > 0 ? sb.substring(1).toString() : mIds;

			} else if (func.getOrgOption() != null
					&& func.getOrgOption() == Func.ORG_USER) {

				List<OrgUser> orgUserList = new OrgUserDB(
						AbsSearchListActivityNew.this).findOrgUserByUserId(mIds);
				StringBuffer sb = new StringBuffer();
				for (int j = 0; j < orgUserList.size(); j++) {
					OrgUser orgUser = orgUserList.get(j);
					sb.append(",").append(orgUser.getUserName());
				}
				tvValue = sb.length() > 0 ? sb.substring(1).toString() : mIds;

			} else if (func.getOrgOption() != null
					&& func.getOrgOption() == Func.ORG_OPTION) {

				List<Org> orgList = new OrgDB(AbsSearchListActivityNew.this)
						.findOrgByOrgId(mIds);
				StringBuffer sb = new StringBuffer();
				for (int j = 0; j < orgList.size(); j++) {
					Org org = orgList.get(j);
					sb.append(",").append(org.getOrgName());
				}
				tvValue = sb.length() > 0 ? sb.substring(1).toString() : mIds;

			} else {
				if(!",".equals(mIds)){
					tvValue = new DictDB(AbsSearchListActivityNew.this)
					.findDictMultiChoiceValueStr(mIds,
							func.getDictDataId(), func.getDictTable());
				}
				
			}
		}

		return tvValue;
	}

	/**
	 * 如果是下来选框时调用该方法,获取下拉选框的值
	 * 
	 * @param func
	 *            Func对象
	 * @param key
	 *            数据ID
	 * @return 返回需要显示的文字，如果是多个值，则用“,”分割
	 */
	protected String getSelectCompValue(Func func, String key) {
		String tvValue = "";
		if (!TextUtils.isEmpty(key)) {// 判断非空
			if (func.getOrgOption() != null
					&& func.getOrgOption() == Func.ORG_STORE) {// 机构店面联动
				// 根据storeId查询机构店面，然后取出所有店面名并作为结果返回
				// 如果找不到相关店面，则将storeId作为结果返回
				List<OrgStore> orgStoreList = new OrgStoreDB(
						AbsSearchListActivityNew.this).findOrgListByStoreId(key);
				StringBuffer sb = new StringBuffer();
				for (int j = 0; j < orgStoreList.size(); j++) {
					OrgStore orgStore = orgStoreList.get(j);
					sb.append(",").append(orgStore.getStoreName());
				}
				tvValue = sb.length() > 0 ? sb.substring(1).toString() : key;
			} else if (func.getOrgOption() != null
					&& func.getOrgOption() == Func.ORG_USER) {// 用户联动
				// 根据userId查询机构用户，然后取出所有用户名并作为结果返回
				// 如果找不到相关用户，则将userId作为结果返回
				List<OrgUser> orgUserList = new OrgUserDB(
						AbsSearchListActivityNew.this).findOrgUserByUserId(key);
				StringBuffer sb = new StringBuffer();
				for (int j = 0; j < orgUserList.size(); j++) {
					OrgUser orgUser = orgUserList.get(j);
					sb.append(",").append(orgUser.getUserName());
				}
				tvValue = sb.length() > 0 ? sb.substring(1).toString() : key;
			} else if (func.getOrgOption() != null
					&& func.getOrgOption() == Func.ORG_OPTION) {// 机构联动
				// 根据orgId查询机构，然后取出所有机构名并作为结果返回
				// 如果找不到相关机构，则将orgId作为结果返回
				List<Org> orgList = new OrgDB(AbsSearchListActivityNew.this)
						.findOrgByOrgId(key);
				StringBuffer sb = new StringBuffer();
				for (int j = 0; j < orgList.size(); j++) {
					Org org = orgList.get(j);
					sb.append(",").append(org.getOrgName());
				}
				tvValue = sb.length() > 0 ? sb.substring(1).toString() : key;
			} else {
				// 查询的表
				String tableName = func.getDictTable();
				// 查询表中的列
				String dataId = func.getDictDataId();
				// 获取下拉内容Dictionary
				Dictionary dic = new DictDB(AbsSearchListActivityNew.this)
						.findDictListByTable(tableName, dataId, key);
				if (dic != null) {
					// 复制下拉选框的内容
					tvValue = dic.getCtrlCol();
				}
			}
		}
		return tvValue;
	}

	/**
	 * 添加分页功能
	 */
	public void addPaging() {
		// 获取页面组件对象
		this.ll_paging_cotrol = this.findViewById(R.id.ll_paging_cotrol);
		this.ll_paging = this.findViewById(R.id.ll_paging);
		this.ll_paging2 = this.findViewById(R.id.ll_paging2);
		this.ll_page_tip = this.findViewById(R.id.ll_page_tip);
		this.ll_page_mid_list = this.findViewById(R.id.ll_page_mid_list);
		this.tv_paging_count = (TextView) this.findViewById(R.id.tv_paging_count);
		this.pageMidListView = (ListView) this.findViewById(R.id.lv_page_mid_list_view);

		this.pageMidListView.setDivider(null);// 取消分割线
		this.pageMidListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (currentPage == position)// 如果用户跳转的目标就是当前页，则什么都不做
					return;
				lastPage = currentPage;
				currentPage = position;
				pagingOnItemClick();
			}

		});
		this.pageMidListView.setAdapter(new PagingAdapter(this));
	}

	/**
	 * 页面跳转项的事件
	 */
	private void pagingOnItemClick() {
		dialog.show();
		PagingAdapter adapter = (PagingAdapter) this.pageMidListView.getAdapter();
		adapter.setItemViewSelected(this.currentPage);// 设置选中项的背景颜色
		netWorkInThread();// 获取目标页的数据
	}

	/**
	 * 刷新翻页栏
	 */
	private void refreshPage() {
//		if (pageCount <= 1) {// 如何只有一页的话，就隐藏翻页
//			ll_paging.setVisibility(View.GONE);
//		} else {
//			if (this.ll_paging != null) {
//				this.ll_paging.setVisibility(View.VISIBLE);
//
//				View footView = View.inflate(this, R.layout.foot_listview, null);
//				if (elv_search.getFooterViewsCount() == 0) {
//					elv_search.addFooterView(footView);
//				}
//				elv_search.setAdapter(mAdapter);
//			}
//		}
//		if (this.pageMidListView != null) {
//			this.pageMidListView.setSelection(this.currentPage - 1);
//			PagingAdapter adapter = (PagingAdapter) this.pageMidListView
//					.getAdapter();
//			adapter.setDataSrc(getPageListData());// 更新跳转项List
//			adapter.setItemViewSelected(this.currentPage);// 将当前页突出显示
//			adapter.notifyDataSetChanged();
//			tv_paging_count.setText((this.currentPage + 1) + "/"+ this.pageCount);
//		}
//		elv_search.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 获取页码数组
	 * 
	 * @return 返回页码数组
	 */
	private String[] getPageListData() {
		String[] pageArr = new String[this.pageCount];
		for (int i = 0; i < this.pageCount; i++) {
			pageArr[i] = getResources().getString(R.string.activity_after_d) + (i + 1) + getResources().getString(R.string.activity_after_page);
		}
		return pageArr;
	}

	/**
	 * 设置翻页栏的总页数和每页数量
	 * 
	 * @param cacherows
	 *            每页数据量
	 * @param total
	 *            数据总量
	 */
	public void settingPage(Integer cacherows, Integer total) {
		if (cacherows != null && cacherows != 0 && total != null) {
			this.pageCount = (int) Math.ceil((double) total / cacherows);
//			this.EachPageNum = cacherows;
			/*****************************************/
			this.EachPageNum = total;
		}
	}

	/**
	 * 翻页栏的点击事件处理
	 * 
	 * @param v
	 *            捕获到点击事件的组件
	 */
	public void onClickPage(View v) {
		switch (v.getId()) {
		case R.id.ll_page_tip:// 显示/隐藏翻页栏
			settingShowPagingNoneAnimation();
			break;
		case R.id.ll_page_left:// 上一页
			lastPage = currentPage;
			int temp1 = currentPage;
			temp1--;
			if (temp1 < 0) {
				ToastOrder.makeText(this, "已经到第一页了！", ToastOrder.LENGTH_LONG).show();
			} else {
				currentPage = temp1;
				pagingOnItemClick();
			}
			break;
		case R.id.ll_page_mid:// 按页码跳转
			settingShowSearchPagingList();
			break;
		case R.id.ll_page_right:// 下一页
			lastPage = currentPage;
			int temp2 = currentPage;
			temp2++;
			if (temp2 >= pageCount) {
				ToastOrder.makeText(this, getResources().getString(R.string.activity_after_03), ToastOrder.LENGTH_LONG).show();
			} else {
				currentPage = temp2;
				pagingOnItemClick();
			}
			break;
		}
	}

	/**
	 * 显示/隐藏翻页栏
	 */
	private void settingShowPaging() {
		
		if (ll_paging_cotrol.isShown()) {
			float to = (float)ll_paging.getHeight();
			TranslateAnimation animation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0, 
					Animation.RELATIVE_TO_SELF, 0, 
					Animation.RELATIVE_TO_SELF, 0f, 
					Animation.RELATIVE_TO_SELF, (float)ll_paging2.getHeight()/to);
			animation.setDuration(500);
			//animation.setFillAfter(true);
			animation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					ll_paging2.setVisibility(View.GONE);
					
				}
			});
			ll_paging.setAnimation(animation);
			ll_paging.setTag(to);
			ll_page_tip.setBackgroundResource(R.drawable.paging_tip_up);
			
		}else {
			float from = ll_paging.getTag()==null?0:(Float)ll_paging.getTag();
			TranslateAnimation animation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0, 
					Animation.RELATIVE_TO_SELF, 0, 
					Animation.RELATIVE_TO_SELF,(float)ll_paging2.getHeight()/from, 
					Animation.RELATIVE_TO_SELF, 0);
			animation.setDuration(500);
			//animation.setFillAfter(true);
//			ll_paging2.setVisibility(View.VISIBLE);
			ll_paging.setAnimation(animation);
			ll_page_tip.setBackgroundResource(R.drawable.paging_tip_down);
		}		
		
	}
	
	/**
	 * 显示/隐藏翻页栏
	 */
	private void settingShowPagingNoneAnimation() {
		
		if (ll_paging_cotrol.isShown()) {
			ll_paging2.setVisibility(View.GONE);
			ll_page_tip.setBackgroundResource(R.drawable.paging_tip_up);
			
		}else {
//			ll_paging2.setVisibility(View.VISIBLE);
			ll_page_tip.setBackgroundResource(R.drawable.paging_tip_down);
		}		
		
	}
	

	/**
	 * 用动画显示/隐藏页码跳转的List
	 */
	private void settingShowSearchPagingList() {
		if (this.ll_page_mid_list.isShown()) {
			this.ll_page_mid_list.setVisibility(View.GONE);
		} else {
			this.ll_page_mid_list.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 获取Activity的layout资源文件id，子类通过重写该方法来自定义界面，如果返回0则代表使用默认布局文件;
	 * 如重写此方法,initTitle不会被调用,且ExpandableListView的id必须是elv_search_list
	 * 
	 * @return 返回layout资源文件的id
	 */
	protected int getLayoutId() {
		return 0;
	}

	/**
	 * 初始化标题
	 * 
	 * @param viewCoum
	 *            Func数据集
	 * @param ll_title
	 *            显示列表的LinearLayout
	 */
	protected abstract void initTitle(List<Func> viewCoum, LinearLayout ll_title);

	/**
	 * 解析JSON,返回列表显示的对象,必须重写,此方法在子线程里执行
	 * 
	 * @param json
	 *            需要解析的JSON字符串
	 * @return 返回列表显示的对象
	 * @throws Exception
	 *             可能引发的各种异常
	 */
	protected abstract List getDataListOnThread(String json) throws Exception;

	/**
	 * 获取URL地址,必须重写,否则无法联网,且可能报错
	 * 
	 * @return 返回URL地址
	 */
	protected abstract String getSearchUrl();

	/**
	 * 获取设置联网查询的参数,必须重写,否则报错
	 * 
	 * @return 返回联网查询的参数
	 */
	protected abstract HashMap<String, String> getSearchParams();

	/**
	 * 获取ExpandableListView的Adapter的当前ChildrenCount
	 * 
	 * @param groupPosition
	 *            在ExpandableListView的Adapter中的组位置
	 * @return 返回Adapter中获取当前ChildrenCount
	 * 
	 * @see
	 */
	protected abstract int getCurChildrenCount(int groupPosition);

	/**
	 * Adapter中获取当前GroupView
	 * 
	 * @see
	 */
	protected abstract View getCurGroupView(int groupPosition,
			boolean isExpanded, View convertView, ViewGroup parent);

	/**
	 * Adapter中获取当前ChildView
	 * 
	 * @see
	 */
	protected abstract View getCurChildView(int groupPosition,
			int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent);

	/**
	 * 在http请求参数中添加下一页的页码参数
	 * 
	 * @param params
	 *            http请求参数集
	 */
	protected void setPageParams(HashMap<String, String> params) {
//		params.put(PAGE, String.valueOf(currentPage + 1));
		/**********************************************************/
		params.put(PAGE, String.valueOf(++currentPage));
	}

	protected abstract View getItemView(int i, View view, ViewGroup viewGroup);
}
