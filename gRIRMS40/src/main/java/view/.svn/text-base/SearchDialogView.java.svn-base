package view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.questionnaire.bo.SurveyAdress;
import com.yunhu.yhshxc.wechat.view.sortListView.ClearEditText;

/**
 * 带模糊搜索的弹出选择框
 *
 */
public class SearchDialogView implements OnItemClickListener {
	private Context mContext;
	private View view;
	private ClearEditText mSearchView;
	private ListView mListView;
	private AlertDialog alertDialog;
	private List<SurveyAdress> allDatas;
	private List<SurveyAdress> datas;
	private SearchListAdapter adapter;
	private OnSelectDataListener mListener;

	public SearchDialogView(Context context) {
		this.mContext = context;
		view = LayoutInflater.from(mContext).inflate(R.layout.search_list_dialogview, null);
		mSearchView = (ClearEditText) view.findViewById(R.id.dl_search_view);
		mListView = (ListView) view.findViewById(R.id.dl_listview);
		mSearchView.addTextChangedListener(watcher);
	}
	private TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			datas.clear();
			if (!TextUtils.isEmpty(s.toString())) {
				for (int i = 0; i < allDatas.size(); i++) {
					SurveyAdress oneString = allDatas.get(i);
					if (oneString.getAdress().contains(s.toString())) {
						datas.add(oneString);
					}
				}
			} else {
	               datas.addAll(allDatas);
				
			}

			adapter.notifyDataSetChanged();
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			
		}
	};

	/**
	 * 设置数据源List<String>
	 * 
	 * @param list
	 */
	public void setInitList(List<SurveyAdress> list) {
		allDatas = new ArrayList<SurveyAdress>(list);
		datas = new ArrayList<SurveyAdress>(list);
		
		if (adapter == null) {
			adapter = new SearchListAdapter();
		}

		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
//		mSearchView.setOnQueryTextListener(this);

	}

	/**
	 * 设置选择监听
	 * 
	 * @param listener
	 */
	public void setOnItemClickListener(OnSelectDataListener listener) {
		this.mListener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mListener != null) {
			mListener.onSelectData(datas.get(position));
		}
		if (alertDialog != null && alertDialog.isShowing()) {
			alertDialog.dismiss();
		}

	}


	public void showDialog() {
		alertDialog = new AlertDialog.Builder(mContext, R.style.NewAlertDialogStyle).setView(view).setCancelable(true)
				.create();
		alertDialog.show();
	}

	class SearchListAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return datas.size();
		}

		@Override
		public Object getItem(int position) {

			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.multi_choice_search_itme, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.content);
			tv.setText(datas.get(position).getAdress());

			return convertView;
		}

	}

	public interface OnSelectDataListener {

		void onSelectData(SurveyAdress text);
	}

}
