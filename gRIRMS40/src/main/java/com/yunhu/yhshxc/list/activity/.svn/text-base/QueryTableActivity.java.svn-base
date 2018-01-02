package com.yunhu.yhshxc.list.activity;

import android.text.TextUtils;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.database.VisitFuncDB;
import com.yunhu.yhshxc.utility.Constants;

public class QueryTableActivity extends BaseQueryTableActivity {
	private String linkJson;// 超链接时使用
	private String sqlLinkJson;// sql超链接时使用
	private int funcId = 0;// 拜访中超链接历史数据时使用
	private Func linkFunc;// 拜访中超链接历史数据时使用
	private int storeId = 0;// 拜访中超链接历史数据时使用
	protected LinearLayout layHeader;
	protected Column[] columns;

	@Override
	protected void initParams() {
		super.initParams();
		
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
			linkFunc = new VisitFuncDB(this).findFuncByFuncId(funcId);
		}
	}

	@Override
	protected void initViews() {
		setContentView(R.layout.query_table);
		expListView = (ExpandableListView)findViewById(R.id.list);
		layHeader = (LinearLayout)findViewById(R.id.header);
	}

	
	protected class Column {
		String label;
		int width;
		
		protected Column(String label, int width) {
			this.label = label;
			this.width = width;
		}
	}
}
