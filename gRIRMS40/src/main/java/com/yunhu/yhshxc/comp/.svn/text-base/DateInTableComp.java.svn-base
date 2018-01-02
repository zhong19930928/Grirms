package com.yunhu.yhshxc.comp;



import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;

/**
 * 表格中的日期控件
 * @author jishen
 *
 */
public class DateInTableComp extends Component {
	private Context context;
	private Button currentButton;
	private Func func;
	private View view;
	
    private int year;//年
    private int month;//月
    private int day;//日
    
    private String tempValue;

	public DateInTableComp(Context context, Func func) {
		this.context = context;
		this.func = func;
		this.type = func.getType();
		this.param = func.getFuncId();
		this.compFunc=func;
		view = View.inflate(context, R.layout.date_in_table_comp, null);
		currentButton = (Button) view.findViewById(R.id.date_in_table_Comp);
		
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
			currentButton.setText(context.getResources().getString(R.string.table_date_comp));
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
					String[] str = value.split(" ");
					if (str.length==2) {
						value = str[0];
					}
				}
				dateDialog(value==null?"":value).show();
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
	
	
	
	  DatePickerDialog pcikDialog =null;
	
	/** 
	 * 表格中的日期控件
	 * @param dota 要显示的日期
	 * @return
	 */
	private Dialog dateDialog(String dataContent){
		
		String[] str=dataContent.split("-");
		if(!TextUtils.isEmpty(dataContent) && str.length==3){//有默认值
			year = Integer.parseInt(str[0]);
			month = Integer.parseInt(str[1]) - 1;
			day = Integer.parseInt(str[2]);
		}else{
			Calendar _c = Calendar.getInstance();// 获取系统默认是日期
			year = _c.get(Calendar.YEAR);
			month = _c.get(Calendar.MONTH);
			day = _c.get(Calendar.DAY_OF_MONTH);
		}
		
//		final Dialog dialog=new Dialog(context, R.style.transparentDialog);
//		View view=View.inflate(context, R.layout.date_intable_dialog, null);
//		Button confirmBtn=(Button) view.findViewById(R.id.date_intable_confirmBtn);
//		Button cancelBtn=(Button) view.findViewById(R.id.date_intable_cancelBtn);
//		
//		LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_compDialog);
//		TimeView timeView = new TimeView(context, TimeView.TYPE_DATE, new TimeView.WheelTimeListener() {
//			
//			@Override
//			public void onResult(String wheelTime) {
//				tempValue = wheelTime;
//			}
//		});
//		timeView.setOriDate(year, month+1, day);
//		ll.addView(timeView);
//		confirmBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				setValue(tempValue);
//			}
//		});
//		
//		cancelBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				
//			}
//		});
//		dialog.setContentView(view);
//		return dialog;

      
        if (Integer.parseInt(Build.VERSION.SDK)>=21) {
			  pcikDialog = new DatePickerDialog(context,R.style.NewDatePickerDialog,null, year, month, day);
		}else{
			  pcikDialog = new DatePickerDialog(context,R.style.NewDatePickerDialogOld,null, year, month, day);
		}
        final DatePicker datePicker = pcikDialog.getDatePicker();
        pcikDialog.setButton(DialogInterface.BUTTON_NEGATIVE,context.getResources().getString(R.string.Cancle),new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (pcikDialog!=null&&pcikDialog.isShowing()) {
					pcikDialog.dismiss();
				}
			}
		});
        pcikDialog.setButton(DialogInterface.BUTTON_POSITIVE,context.getResources().getString(R.string.Confirm),new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int month1=datePicker.getMonth() + 1;
				int day1 = datePicker.getDayOfMonth();
				String month2=month1>=10?month1+"":"0"+month1;
				String day2 = day1>=10?day1+"":"0"+day1;
				String dateStr = datePicker.getYear() + "-" + month2 + "-"
						+ day2;
				
					
				setValue(dateStr);
			}
		});
        pcikDialog.setCanceledOnTouchOutside(false);
      
       return pcikDialog;
	}
}
