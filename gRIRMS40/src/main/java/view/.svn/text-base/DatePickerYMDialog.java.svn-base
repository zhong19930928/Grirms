package view;

import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * 
 * @author qingli 使日期类控件只显示年月
 *
 */
public class DatePickerYMDialog extends DatePickerDialog {
	private boolean isHidDay = false;//是否隐藏日
	private boolean isHidMonth = false;//是否隐藏月
	private Context context;

	public DatePickerYMDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {

		super(context, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, callBack, year, monthOfYear, dayOfMonth);
		this.context = context;
		setTitle(year +  PublicUtils.getResourceString(context, R.string.year) + (monthOfYear + 1) +  PublicUtils.getResourceString(context, R.string.month));
		((ViewGroup) ((ViewGroup) (getDatePicker().getChildAt(0))).getChildAt(0)).getChildAt(2)
				.setVisibility(View.GONE);
	}

	public DatePickerYMDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {

		super(context, callBack, year, monthOfYear, dayOfMonth);
		this.context = context;

	}

	public DatePickerYMDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth,
			boolean hidDay, boolean hidMonth) {

		super(context, callBack, year, monthOfYear, dayOfMonth);
		this.context = context;
		isHidDay = hidDay;
		isHidMonth = hidMonth;
		if (hidDay) {
			if(null!=((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2)){
				((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2)
						.setVisibility(View.GONE);
			}

		}
		if (hidMonth) {
			if(null!=((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(1)){

				((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(1)
						.setVisibility(View.GONE);
			}
		}
		Locale locale = context.getResources().getConfiguration().locale;
		Locale.setDefault(locale);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		super.onDateChanged(view, year, month, day);
		if (isHidDay&&!isHidMonth) {
			setTitle(year +  PublicUtils.getResourceString(context, R.string.year) + (month + 1) +  PublicUtils.getResourceString(context, R.string.month));
		} 
		if (isHidMonth&&isHidDay) {
			setTitle(year + PublicUtils.getResourceString(context, R.string.year));
		}
	}
	/**
	 * 设置最小开始时间从当前系统时间
	 */
//	public void setMinDate(){
//	DatePicker dp=	(DatePicker) ((ViewGroup) ((ViewGroup) this.getDatePicker()));
//	 dp.setMinDate(System.currentTimeMillis());
//	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
//		super.onStop();
	}
}
