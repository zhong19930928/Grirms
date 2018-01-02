package com.yunhu.yhshxc.comp;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.exception.DataComputeException;
import com.yunhu.yhshxc.func.FuncTree;
import com.yunhu.yhshxc.func.FuncTreeForLink;
import com.yunhu.yhshxc.func.FuncTreeForVisit;
import com.nostra13.universalimageloader.core.ImageLoader;

import gcg.org.debug.JLog;

/**
 * 文本标签
 * @author jishen
 *
 */
public class TextComp extends Component {
	private Context context;
	private View view;
	private Func func;
	private TextView textView;
	private ImageView textCompImage;
	private CompDialog compDialog;
	private String defaultSql;//sql类型的默认值
    
	public String getDefaultSql() {
		return defaultSql;
	}

	public void setDefaultSql(String defaultSql) {
		this.defaultSql = defaultSql;
	}

	public TextComp(Context context, Func func,CompDialog compDialog) {
		this.context = context;
		this.compFunc=func;
		this.func=func;
		this.compDialog=compDialog;
		this.type = func.getType();
		view = View.inflate(context, R.layout.text_comp, null);
		textView = (TextView) view.findViewById(R.id.textComp);
		textView.setMovementMethod(new ScrollingMovementMethod());
		textCompImage = (ImageView) view.findViewById(R.id.textCompImage);
		textCompImage.setVisibility(View.GONE);
	}

	public Func getFunc() {
		return func;
	}

	@Override
	public View getObject() {
		initData();
		return view;
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.comp.Component#setIsEnable(boolean)
	 */
	@Override
	public void setIsEnable(boolean isEnable) {
		getObject().setEnabled(isEnable);
	}
	
	/**
	 * 给文本标签设置值
	 * @param textContent 要设置的值
	 */
	public  void setText(String textContent){
		textView.setText(textContent);
		this.value=textContent;
	}
	
	/*
	 * 初始化数据
	 */
	private void initData() {
		
		if(func.getDefaultType() != null && func.getDefaultType()==Func.DEFAULT_TYPE_SQL){//sql类型
			String itemValue = getDefaultSql();
			textView.setText(itemValue);
			this.value = itemValue;
		}else{
			int submitId = new SubmitDB(context).selectSubmitIdNotCheckOut(compDialog.planId,compDialog.wayId, compDialog.storeId, compDialog.targetid, compDialog.targetType,Submit.STATE_NO_SUBMIT);
			String data="";
			try {
				if(isLink){//超链接查询临时表
					data= new FuncTreeForLink(context,func,submitId).result;
				}else{
					if (compDialog.targetType == Menu.TYPE_VISIT) {
						data= new FuncTreeForVisit(context,func,submitId).result;
					}else{
						data= new FuncTree(context,func,submitId).result;

					}
				}
			} catch (NumberFormatException e) {
				data=context.getResources().getString(R.string.format_error);
				JLog.e(e);
			} catch (DataComputeException e) {
				data=e.getMessage();
				JLog.e(e);
			}
			textView.setText(data);

			this.value = data;
		}
		//view_xu判断是否是图片的路径,如果是就展示图片
		try{
			if(value.endsWith(".jpg")||value.endsWith(".png")||value.endsWith(".jpeg")){
				textCompImage.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(value, textCompImage);
				textView.setText("");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
