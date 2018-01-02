package com.yunhu.yhshxc.comp;



import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.widget.wheeltime.TimeView;

/**
 * 表格中的日期控件
 * @author jishen
 *
 */
public class TimeInTableComp extends Component {
	private Context context;
	private Button currentButton;
	private Func func;
	private View view;
	
    private int hour;//时
    private int minutes;//分
    
    private String tempValue;

	public TimeInTableComp(Context context, Func func) {
		this.context = context;
		this.func = func;
		this.type = func.getType();
		this.param = func.getFuncId();
		this.compFunc=func;
		view = View.inflate(context, R.layout.time_in_table_comp, null);
		currentButton = (Button) view.findViewById(R.id.time_in_table_Comp);
		
	}

	/**
	 * 当前控件
	 * @return
	 */
	public Func getFunc() {
		return func;
	}

	/**
	 * 设置当前控件的值
	 * @param value 值
	 */
	public void setValue(String value) {
		if (!TextUtils.isEmpty(value)) {
			currentButton.setText(value);
		}else{
			currentButton.setText(context.getResources().getString(R.string.table_time_comp));
		}
		this.value=value;
		tempValue = value;
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.comp.Component#getObject()
	 */
	@Override
	public View getObject() {
		currentButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(value)) {
					String[] currentValue = value.split(":");
					hour = Integer.parseInt(currentValue[0]);
					minutes = Integer.parseInt(currentValue[1]);
				}else{
					Calendar c = Calendar.getInstance();
					hour = c.get(Calendar.HOUR_OF_DAY);
					minutes = c.get(Calendar.MINUTE);
				}
				dateDialog().show();
			}
		});
		return view;
	}

	/*
	 * (non-Javadoc)
	 * @see com.gcg.grirms.comp.Component#setIsEnable(boolean)
	 */
	@Override
	public void setIsEnable(boolean isEnable) {
		currentButton.setEnabled(isEnable);
	}
	
	/**
	 * @return 当前button
	 */
	public Button getCurrentButton() {
		return currentButton;
	}
	
	/** 
	 * 表格中的日期控件
	 * @param data 要显示的日期
	 * @return
	 */
	private Dialog dateDialog(){
		final Dialog dialog=new Dialog(context, R.style.transparentDialog);
		View view=View.inflate(context, R.layout.time_intable_dialog, null);
		Button confirmBtn=(Button) view.findViewById(R.id.time_intable_confirmBtn);
		Button cancelBtn=(Button) view.findViewById(R.id.time_intable_cancelBtn);
		
		TimeView timeView = new TimeView(context, TimeView.TYPE_TIME, new TimeView.WheelTimeListener() {
			
			@Override
			public void onResult(String wheelTime) {
				tempValue = wheelTime;
			}
		});
		timeView.setOriTime(hour, minutes);
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_compDialog);
		ll.addView(timeView);
		confirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				setValue(tempValue);
			}
		});
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
		dialog.setContentView(view);
		return dialog;
	}
}
