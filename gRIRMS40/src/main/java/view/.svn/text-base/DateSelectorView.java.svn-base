package view;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.PublicUtils;

/**
 * Created by qingli on 2016/12/9 月计划,周计划,日计划的选择封装类 设置类型:DATE_TYPE_MONTH 月
 * DATE_TYPE_WEEK 周 DATE_TYPE_DAY 日 可初始化对应类型的日期控件 回调选择时间的监听:
 * setOnDateClickListener(OnDateClickListener dateClickListener); 可回调出选择的时间
 */

public class DateSelectorView extends LinearLayout {
	public static final String DATE_TYPE_MONTH = "month";// 月类型
	public static final String DATE_TYPE_WEEK = "week";// 周类型
	public static final String DATE_TYPE_DAY = "day";// 日类型
	private static String dateType = "";// 展示类型,月,周,日
	private int spinner_textColor = 0;// 前面日期文字颜色
	private int number_textColor = 0;// 后面的数字展示颜色
	private LinearLayout select_view_layout;// 条目总布局
	private TextView dateTip;// 展示数据类型
	private Button dateButton;// 日期选择器
	private int initYear=0;// 初始化年
	private int initMonth=0;// 初始化月
	public int initDay = 1;// 初始化日
	private boolean isUseTextSelected=true;
	private int cacheYear;
	private int cacheMonth;
	private int cacheDay;
	private LinearLayout number_container;// 添加具体天数的容器
	private Context mContext;
	private OnDateClickListener dateClickListener;// 获取选中日期的监听
	private String dateSpinnerStr = "";// spinner选择日期
	private Map<Integer, String> weekData = new HashMap<Integer, String>();// 用来存储每月周数对应的时间段
	private boolean isCompareDate = false;
    public HorizontalScrollView mScrollView;
    public HorizontalScrollView getScrollview(){
    	return mScrollView;
    }
    public int getInitDay(){
    	return initDay;
    }
	public DateSelectorView(Context context) {
		this(context, null);
	}

	public DateSelectorView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DateSelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		// 获取属性,根据属性设置显示的是月份天数还是周的天数
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DateSelectorView);
		// dateType =
		// typedArray.getString(R.styleable.DateSelectorView_date_type);
		spinner_textColor = typedArray.getColor(R.styleable.DateSelectorView_spinner_textcolor, R.color.gray_sub);
		number_textColor = typedArray.getColor(R.styleable.DateSelectorView_number_textcolor, Color.BLACK);
		// 创建布局view ,添加布局view,g该布局用于添加显示数字内容
		select_view_layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.date_selectview, null);
		dateButton = (Button) select_view_layout.findViewById(R.id.select_view_spinner);
		mScrollView =(HorizontalScrollView) select_view_layout.findViewById(R.id.mScrollView) ;
		if (spinner_textColor != 0) {
			dateButton.setTextColor(spinner_textColor);// 前面日期控件设置颜色
		}
		number_container = (LinearLayout) select_view_layout.findViewById(R.id.select_view_layout);
		dateTip = (TextView) select_view_layout.findViewById(R.id.date_type_title);
		// 设置选择年份的监听,如果是月报,选择了某月,就回调出年月的标准字符形式,如果是周报就回调出具体年月日的字符形式,但是显示要显示为日期~日期的周表达形式
		// dateButton.setText(initYear);
		dateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//第一次创建初始化使用,如果未设置,则使用默认当前年月日
				if (initYear == 0 || initMonth == 0) {
					Calendar calendar = Calendar.getInstance(Locale.CHINA);
					calendar.setTimeInMillis(System.currentTimeMillis());
					initYear = calendar.get(Calendar.YEAR);
					initMonth = calendar.get(Calendar.MONTH)+1;
					initDay = calendar.get(Calendar.DAY_OF_MONTH);
				}
				//如果已经选择过日期,则用缓存的时间来展示日期框
				if (cacheYear!=0&&cacheMonth!=0&&cacheDay!=0) {
					initYear = cacheYear;
					initMonth = cacheMonth;
					initDay = cacheDay;
				}
				
				boolean ishowMonth = true;
				if (getDateType().equals(DATE_TYPE_MONTH)) {// 如果不是月类型就不隐藏月份控件
					ishowMonth = true;
				} else if (getDateType().equals(DATE_TYPE_DAY)) {
					ishowMonth = false;
				} else if (getDateType().equals(DATE_TYPE_WEEK)) {
					ishowMonth = false;
				}
//				Calendar calendar = Calendar.getInstance(Locale.CHINA);
//				calendar.set(initYear, initMonth, initDay);
//				initYear = calendar.get(Calendar.YEAR);
//				initMonth = calendar.get(Calendar.MONTH);
//				initDay = calendar.get(Calendar.DAY_OF_MONTH);

				DatePickerYMDialog dia = new DatePickerYMDialog(mContext, new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						//缓存已选则日期
						cacheYear = year;
						cacheMonth=monthOfYear;
//						cacheDay=dayOfMonth;
						if (getDateType().equals(DATE_TYPE_MONTH)) {// 如果是月类型就只显示年
							// 只显示年
							dateSpinnerStr = year + "";
//							dateButton.setText(year + PublicUtils.getResourceString(mContext,R.string.year));
							dateButton.setText(year + "");
							addMonthData();

						} else if (getDateType().equals(DATE_TYPE_DAY)) { // 日类型显示年月
							String m = "";
							if ((monthOfYear + 1) < 10) {
								m = "0" + (monthOfYear + 1);
							} else {
								m = (monthOfYear + 1) + "";
							}
							dateSpinnerStr = year + "-" + (m);
//							dateButton.setText(year + PublicUtils.getResourceString(mContext,R.string.year) + (monthOfYear + 1) + PublicUtils.getResourceString(mContext,R.string.month));
							dateButton.setText(year + "." + (monthOfYear + 1) );
							addDayData(year + "-" + (monthOfYear + 1));
						} else if (getDateType().equals(DATE_TYPE_WEEK)) {// 周类型显示年月
							// dateSpinnerStr = year + "-" + monthOfYear;
//							dateButton.setText(year + PublicUtils.getResourceString(mContext,R.string.year) + (monthOfYear + 1) + PublicUtils.getResourceString(mContext,R.string.month));
							dateButton.setText(year + "." + (monthOfYear + 1) );
							addWeekData(year + "-" + (monthOfYear + 1));
						}

					}
				}, initYear, initMonth, initDay, true, ishowMonth);
				// dia.setMinDate();
				dia.setCancelable(true);
				Locale locale = getResources().getConfiguration().locale;
				Locale.setDefault(locale);
				dia.show();

			}
		});
		// 默认月计划初始化数据
		if (TextUtils.isEmpty(dateType)) {
			initDate(DATE_TYPE_MONTH);
			dateTip.setText(PublicUtils.getResourceString(mContext,R.string.month_plan));

		}
		addView(select_view_layout);
		typedArray.recycle();

	}

	/**
	 * 获取当前显示类型 月 周 日
	 * 
	 * @return
	 */
	public String getDateType() {
		if (TextUtils.isEmpty(dateType)) {
			return DATE_TYPE_MONTH;
		} else {
			return dateType;
		}

	}

	/**
	 * 设置展示的日期类型 月 周 日
	 */
	public void setDateType(String type) {
		if (type.equals(DATE_TYPE_MONTH)) {
			dateType = DATE_TYPE_MONTH;
			initDate(DATE_TYPE_MONTH);

		} else if (type.equals(DATE_TYPE_WEEK)) {
			dateType = DATE_TYPE_WEEK;
			initDate(DATE_TYPE_WEEK);

		} else if (type.equals(DATE_TYPE_DAY)) {
			dateType = DATE_TYPE_DAY;
			initDate(DATE_TYPE_DAY);

		}
	}

	/**
	 * 给控件设置文本颜色,不设置的颜色设为0
	 * 
	 * @param left
	 *            左面年月控件字体颜色
	 * @param right
	 *            右面日期控件字体颜色
	 * 
	 */
	public void setTextColors(int left, int right) {
		this.spinner_textColor = left;
		this.number_textColor = right;
	}

	/**
	 * 设置回调选中日期的监听
	 */
	public void setOnDateClickListener(OnDateClickListener dateClickListener) {
		this.dateClickListener = dateClickListener;
	}

	/**
	 * 初始化显示类型,月,周,日
	 */
	private void initDate(String dt) {
		String initM = getCurrentYear() + "-" + getCurrentMonth();

		if (dt.equals(DATE_TYPE_MONTH)) {
			dateTip.setText(PublicUtils.getResourceString(mContext,R.string.month_plan));

			if (initYear == 0) { // 没有设置初始日期
				dateSpinnerStr = getCurrentYear();// 首先默认当前年份为第一个数值
//				dateButton.setText(dateSpinnerStr + PublicUtils.getResourceString(mContext,R.string.year));// 设置日期控件默认显示的日期
				dateButton.setText(dateSpinnerStr + "");// 设置日期控件默认显示的日期
			} else {
				dateSpinnerStr = initYear + "";
			}
			addMonthData();

		} else if (dt.equals(DATE_TYPE_WEEK)) {
			dateTip.setText(PublicUtils.getResourceString(mContext,R.string.week_plan));
			if (initYear == 0) {
//				dateButton.setText(getCurrentYear() + PublicUtils.getResourceString(mContext,R.string.year) + getCurrentMonth() + PublicUtils.getResourceString(mContext,R.string.month));// 设置日期控件默认显示的日期
				dateButton.setText(getCurrentYear() + "." + getCurrentMonth() );// 设置日期控件默认显示的日期
				dateSpinnerStr = getCurrentYearAndMonth();// 首先默认当前年月
				addWeekData(initM);
			} else {
				dateSpinnerStr = initYear + "-0" + initMonth;
			}

		} else if (dt.equals(DATE_TYPE_DAY)) {
			dateTip.setText(PublicUtils.getResourceString(mContext,R.string.day_plan));

			if (initYear == 0) {
				dateSpinnerStr = getCurrentYearAndMonth();// 首先默认当前年月
//				dateButton.setText(getCurrentYear() + PublicUtils.getResourceString(mContext,R.string.year) + getCurrentMonth() + PublicUtils.getResourceString(mContext,R.string.month));// 设置日期控件默认显示的日期
				dateButton.setText(getCurrentYear() + "." + getCurrentMonth() );// 设置日期控件默认显示的日期
				addDayData(initM);
			} else {
				dateSpinnerStr = initYear + "-0" + initMonth;
			}
		}

	}
//	List<NumberTextView> titleList = new ArrayList<>();
//	public List<NumberTextView> getLNumberTextViews(){
//		return titleList;
//	}
	private boolean isFristDisplay = false;
	NumberTextView initNextView = null;
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasWindowFocus);
		if(hasWindowFocus&&!isFristDisplay){
			isFristDisplay = true;
			
		if(initNextView!=null){
			 mScrollView.scrollTo( initNextView.getLeft(),0);
		}
			 
					
		}
	}
	/**
	 * 按日类型来添加数据
	 */
	private void addDayData(String m) {
		number_container.removeAllViews();

		int initDays = 30;// 初始化天数
		String[] ster = m.split("-");// 参数年月[2016,12]
		// 首先判断当前年份是否是闰年(平年2月28天,闰年29天)
		boolean isLeap = isRunYear(ster[0]);
		// 判断当前月份(1,3,5,7,8,10,12为31天2月为28或者29天 4,6,9,11位30天)
		int currMonth = Integer.parseInt(ster[1]);

		// Log.e("test", currMonth + "当前月份");
		if (currMonth == 1 || currMonth == 3 || currMonth == 5 || currMonth == 7 || currMonth == 8 || currMonth == 10
				|| currMonth == 12) {// 31天的月份
			initDays = 31;
		} else if (currMonth == 4 || currMonth == 6 || currMonth == 9 || currMonth == 11) {// 30天月份
			initDays = 30;
		} else if (currMonth == 2) {// 2月特殊处理
			if (isLeap) {// 29天
				initDays = 29;
			} else {// 28天
				initDays = 28;
			}

		}
		for ( int i = 1; i <= initDays; i++) {
			final NumberTextView n = new NumberTextView(mContext);
			// n.setTextColor(number_textColor);
			n.setText(i + "");
			// 如果设置了初始化日期,则改变背景色
			if (isUseTextSelected&&initDay == i) {
				changeOtherButton(n);
				initNextView = n;
			}
			n.showPointCircle(false);
			// if (number_textColor != 0) {
			// n.setTextColor(number_textColor);
			// }
			// n.showPointCircle(true);
			n.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 点击的view背景色改变,其他的不变
					changeOtherButton(n);
					// Log.e("test", "onClick: " + n.getText());
				}
			});
			number_container.addView(n);
			
		}
		
		

	}
	 //初始化偏移量
    private int offset = 0;
    private int scrollViewWidth = 0;
	/**
	 * 根据传入参数按照周类型添加数据 参数类型 2016-12
	 */
	private void addWeekData(String str) {
		number_container.removeAllViews();
		weekData.clear();// 先清空
		// 传入日期月份天数
		int theMonthDays = 0;
		// 根据参数定位到当月1号
		String result = str + "-" + 01;
		// 把时间解析为Date类型
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String startDate = "";// 当月第一周的开始日期
		String endDate = "";// 当月第一周的结束时间
		try {
			c.setTimeZone(TimeZone.getTimeZone("GMT+08"));
			c.setTime(sdf.parse(result));// 设置给予的时间值
			int year = c.get(Calendar.YEAR);// 对应的年份
			int month = c.get(Calendar.MONTH) + 1;// 对应的月份
			int moveDays = 0;// 根据一号计算的周几,需要移动到周一的天数
			// 计算对应的周几
			int week = c.get(Calendar.DAY_OF_WEEK);
			// Log.e("test", "当月一号是:--" + week);
			switch (week) {
			case 1:// 周日
					// 如果是周日,需要定位到5天前,也就是上月的的为第一周开始时间
				moveDays = 6;
				break;
			case 2:// 周一
					// 周一正好,不需要移动
				moveDays = 0;
				break;
			case 3:// 周二
					// 周二移动一天
				moveDays = 1;
				break;
			case 4:// 周三
					// 周三移动两天
				moveDays = 2;
				break;
			case 5:// 周四
					// 周四移动三天
				moveDays = 3;
				break;
			case 6:// 周五
					// 周五移动四天
				moveDays = 4;
				break;
			case 7:// 周六
					// 周六移动五天
				moveDays = 5;
				break;

			default:
				break;
			}
			theMonthDays = getMonthDays(str);

			if (moveDays == 0) {// 一号正好是周一,不需要计算上个月
				startDate = year + "-" + month + "-" + 1;
				endDate = year + "-" + month + "-" + 7;
			} else {// 1号不是周一,要计算上个月
					// 上一月份,如果这月是一月,则年份减1,月份变为12
				int lastMonth = month - 1;

				if (lastMonth == 0) {
					startDate = (year - 1) + "-" + 12 + "-" + (31 - moveDays + 1);
					int weekEndDay = 7 - (moveDays); // 周末为当月的几号
					endDate = year + "-" + month + "-" + weekEndDay;

				}
				// 移动天数-1,才为减去的正确天数
				moveDays -= 1;
				// 如果是2月,判断是平年,还是闰年,如果不是2月,判断该月有多少天()
				if (lastMonth == 2) {// 如果是2月份
					if (isRunYear(year + "")) {// 如果是闰年29天
						int day = 29 - moveDays;
						startDate = year + "-" + lastMonth + '-' + day;
						int weekEndDay = 7 - (moveDays + 1); // 周末为当月的几号
						if (weekEndDay < 10) {
							endDate = year + "-" + month + "-" + weekEndDay;// 第一周的技术日期
						} else {
							endDate = year + "-" + month + "-" + weekEndDay;
						}
					} else {// 平年28天
						int day = 28 - moveDays;
						startDate = year + "-" + lastMonth + '-' + day;
						int weekEndDay = 7 - (moveDays + 1); // 周末为当月的几号
						if (weekEndDay < 10) {
							endDate = year + "-" + month + "-" + weekEndDay;// 第一周的技术日期
						} else {
							endDate = year + "-" + month + "-" + weekEndDay;
						}
					}

				} else {// 不是2月份
					if (lastMonth == 1 || lastMonth == 3 || lastMonth == 5 || lastMonth == 7 || lastMonth == 8
							|| lastMonth == 10 || lastMonth == 12) {// 31天的月份

						int day = 31 - moveDays;
						startDate = year + "-" + lastMonth + '-' + day;
						int weekEndDay = 7 - (moveDays + 1); // 周末为当月的几号
						if (weekEndDay < 10) {
							endDate = year + "-" + month + "-" + weekEndDay;// 第一周的技术日期
						} else {
							endDate = year + "-" + month + "-" + weekEndDay;
						}

					} else if (lastMonth == 4 || lastMonth == 6 || lastMonth == 9 || lastMonth == 11) {// 30天月份

						int day = 30 - moveDays;
						startDate = year + "-" + lastMonth + '-' + day;
						int weekEndDay = 7 - (moveDays + 1); // 周末为当月的几号
						if (weekEndDay < 10) {
							endDate = year + "-" + month + "-" + weekEndDay;// 第一周的结束日期
						} else {
							endDate = year + "-" + month + "-" + weekEndDay;
						}
					}
				}

			}

			// --第一周的时间段已经确定,根据第一周时间段累加计算到本月最后一天的周
			String[] ss = endDate.split("-");
			int lastDayFirstWeek = Integer.parseInt(ss[2]);
			// 记录下第一周的时间段,每月最多6周最少4周,在第四周判断是否超过月末
			String oneWeek = startDate + "/" + endDate;
			weekData.put(0, oneWeek);
			// 第二周开始,结束

			startDate = year + "-" + month + "-" + (lastDayFirstWeek + 1);
			endDate = year + "-" + month + "-" + (lastDayFirstWeek + 7);
			String twoWeek = startDate + "/" + endDate;
			weekData.put(1, twoWeek);
			// 第三周
			startDate = year + "-" + month + "-" + (lastDayFirstWeek + 8);
			endDate = year + "-" + month + "-" + (lastDayFirstWeek + 14);
			String threeWeek = startDate + "/" + endDate;
			weekData.put(2, threeWeek);
			// 第四周
			startDate = year + "-" + month + "-" + (lastDayFirstWeek + 15);
			endDate = year + "-" + month + "-" + (lastDayFirstWeek + 21);
			String fourWeek = startDate + "/" + endDate;
			weekData.put(3, fourWeek);
			// 判断是否超过月末
			ss = endDate.split("-");
			int end = Integer.parseInt(ss[2]);
			if (end == theMonthDays) {// 说明是当月正好四周,不需要继续

			} else if (end < theMonthDays) {// 还未到本月最后一天,继续添加一周时间段,四周不可能超过月底
				// 第五周
				startDate = year + "-" + month + "-" + (lastDayFirstWeek + 22);
				endDate = year + "-" + month + "-" + (lastDayFirstWeek + 28);
				// 判断是否跨月,跨月要计算到下一月,
				ss = endDate.split("-");
				end = Integer.parseInt(ss[2]);

				if (end < theMonthDays) {// 说明还未跨月
					String fiveWeek = startDate + "/" + endDate;
					weekData.put(4, fiveWeek);
					// 继续下一周
					// 第六周,肯定跨月
					startDate = year + "-" + month + "-" + (lastDayFirstWeek + 29);
					endDate = year + "-" + month + "-" + (lastDayFirstWeek + 35);
					ss = endDate.split("-");
					end = Integer.parseInt(ss[2]);
					// 计算到下一月
					int nextMonthDay = end - theMonthDays;
					// 判断月份,如果为12月,年增长1
					if (month == 12) {
						endDate = (year + 1) + "-" + 1 + "-" + nextMonthDay;
					} else {
						endDate = year + "-" + (month + 1) + "-" + nextMonthDay;
					}
					String sixWeek = startDate + "/" + endDate;
					weekData.put(5, sixWeek);

				} else if (end > theMonthDays) {// 说明第五周跨月,计算到下一月
					startDate = year + "-" + month + "-" + (lastDayFirstWeek + 22);
					endDate = year + "-" + month + "-" + (lastDayFirstWeek + 28);
					// 判断是否跨月,跨月要计算到下一月,
					ss = endDate.split("-");
					end = Integer.parseInt(ss[2]);
					// 计算到下一月
					int nextMonthDay = end - theMonthDays;
					// 判断月份,如果为12月,年增长1
					if (month == 12) {
						endDate = (year + 1) + "-" + 1 + "-" + nextMonthDay;
					} else {
						endDate = year + "-" + (month + 1) + "-" + nextMonthDay;
					}
					String fiveWeek = startDate + "/" + endDate;
					weekData.put(4, fiveWeek);

				} else if (end == theMonthDays) {// 说明正好月底
					String fiveWeek = startDate + "/" + endDate;
					weekData.put(4, fiveWeek);
				}

			}

			// 遍历集合,添加数据到日期控件
			;
			int i = 0;
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			for (int j = 0; j < weekData.size(); j++) {
			
				String value = weekData.get(i);
				String[] timeArr = value.split("/");
				// 如果与设置的初始日期相同,则改变背景色
				Date d1 = sdf2.parse(timeArr[0]);
				Date d2 = sdf2.parse(initYear + "-" + initMonth + "-" + initDay);
				final NumberTextView n = new NumberTextView(mContext);
				if (isUseTextSelected&&d1.getTime() == d2.getTime()) {
					n.setTextSelected();
					initNextView = n;
				}
				String[] startStr = timeArr[0].split("-");
				String[] endStr = timeArr[1].split("-");
				String btnText = startStr[2] + "~" + endStr[2];
				n.setText(btnText);
				n.showPointCircle(false);
			
			

				n.setIndex(i++);
				// if (number_textColor != 0) {
				// n.setTextColor(number_textColor);
				// }
				n.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 点击的view背景色改变,其他的不变
						changeWeekButton(n);

					}
				});
				number_container.addView(n);
			}

		} catch (ParseException e) {

			e.printStackTrace();
		}
	}

	/**
	 * 改变周报控件背景颜色
	 * 
	 * @param n
	 */
	private void changeWeekButton(NumberTextView n) {
		for (int i = 0; i < number_container.getChildCount(); i++) {
			NumberTextView numberTextView = (NumberTextView) number_container.getChildAt(i);
			numberTextView.setTextNoSelected();
		}
		n.setTextSelected();
		// 解析日期,把小于10的月和日前面拼0
		String[] date1 = weekData.get(n.getIndex()).split("/");
		String[] date2 = date1[0].split("-");// 开始日期
		String[] date3 = date1[1].split("-");// 结束日期
		String resDate2 = date2[0];
		String resDate3 = date3[0];
		if (Integer.parseInt(date2[1]) < 10 || Integer.parseInt(date2[2]) < 10) {
			if (Integer.parseInt(date2[1]) < 10) {
				resDate2 = resDate2 + "-" + "0" + date2[1];
			} else {
				resDate2 = resDate2 + "-" + date2[1];
			}
			if (Integer.parseInt(date2[2]) < 10) {
				resDate2 = resDate2 + "-" + "0" + date2[2];
			} else {
				resDate2 = resDate2 + "-" + date2[2];
			}
		} else {
			resDate2 = date1[0];
		}
		if (Integer.parseInt(date3[1]) < 10 || Integer.parseInt(date3[2]) < 10) {
			if (Integer.parseInt(date3[1]) < 10) {
				resDate3 = resDate3 + "-" + "0" + date3[1];
			} else {
				resDate3 = resDate3 + "-" + date3[1];
			}
			if (Integer.parseInt(date3[2]) < 10) {
				resDate3 = resDate3 + "-" + "0" + date3[2];
			} else {
				resDate3 = resDate3 + "-" + date3[2];
			}
		} else {
			resDate3 = date1[1];
		}
		// 把选择的日期参数从集合查找出来回调出去
		dateSpinnerStr = resDate2 + "/" + resDate3;
		// 对选择的日期做判断,如果选择的是本周以及往前,返回结果为空
		String[] strs = dateSpinnerStr.split("/");

		// 取所选周的开始日期与当前时间做对比
		if (isCompareDate) {
			if (compareCurrentDate(date1[0], DATE_TYPE_WEEK)) {
				if (dateClickListener != null) {
					dateClickListener.onDateSelected(dateSpinnerStr);
				}
			} else {
				if (dateClickListener != null) {
					
					//计算以当前时间为基准的下个周一时间
					Calendar cal = Calendar.getInstance(Locale.CHINA);
					cal.setTimeInMillis(System.currentTimeMillis());
					int year = cal.get(Calendar.YEAR);
					int month = cal.get(Calendar.MONTH);
					int day = cal.get(Calendar.DAY_OF_MONTH);
					//获取
					int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
					int addDays = 0;
							switch (dayWeek) {
							case 1:// 周日
								addDays=1;
								break;
							case 2:// 周一

								addDays=7;
								break;
							case 3:// 周二
								addDays=6;
								break;
							case 4:// 周三
								addDays=5;
								break;
							case 5:// 周四
								addDays=4;
								break;
							case 6:// 周五
								addDays=3;
								break;
							case 7:// 周六
								addDays=2;
								break;

							default:
								break;
							}
			  
							cal.set(Calendar.DAY_OF_MONTH, day+addDays);
						    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						   try {
							String result =  sdf.format(sdf.parse(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH)));
//							System.out.println(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH));
//							System.out.println(result);
							Toast.makeText(mContext, PublicUtils.getResourceString(mContext,R.string.dateselectview1)+result+PublicUtils.getResourceString(mContext,R.string.dateselectview2), Toast.LENGTH_SHORT).show();
						   } catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					
				
					dateClickListener.onDateSelected("");
				}
			}
		} else {
			if (dateClickListener != null) {
				dateClickListener.onDateSelected(dateSpinnerStr);
			}
		}

	}

	/**
	 * 按月类型来添加数据
	 */
	private void addMonthData() {
		number_container.removeAllViews();
		for (int i = 1; i <= 12; i++) {
			final NumberTextView n = new NumberTextView(mContext);
			// n.setTextColor(number_textColor);
			n.setText(i + "");
			
			if (isUseTextSelected&&i == initMonth) {
				changeOtherButton(n);
				initNextView = n;
			}
			n.showPointCircle(false);
			// n.showPointCircle(true);
			// if (number_textColor != 0) {
			// n.setTextColor(number_textColor);
			// }
			n.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 点击的view背景色改变,其他的不变
					changeOtherButton(n);
					// Log.e("test", "onClick: " + n.getText());
					
				}
			});
			number_container.addView(n);
		}

	}

	/**
	 * 改变当前按钮背景色以及其他按钮背景色
	 *
	 * @param n
	 */
	private void changeOtherButton(NumberTextView n) {
		for (int i = 0; i < number_container.getChildCount(); i++) {
			NumberTextView numberTextView = (NumberTextView) number_container.getChildAt(i);
			numberTextView.setTextNoSelected();
		}
		n.setTextSelected();
		// 把选中的日期,dialog年份回调出去
		String selectedMonth = n.getText().toString();
		if (Integer.parseInt(selectedMonth) < 10) {
			selectedMonth = "0" + selectedMonth;
		}
		String resultDate = dateSpinnerStr + "-" + selectedMonth;
		if (isCompareDate) {
			if (compareCurrentDate(resultDate, dateType)) {
				if (dateClickListener != null) {
					dateClickListener.onDateSelected(resultDate);// 2016-02/日类型为2016-12-6
				}
			} else {
				if (dateClickListener != null) {
					//获取日历类,根据日历类来计算下个周期
					Calendar cal = Calendar.getInstance(Locale.CHINA);
					cal.setTimeInMillis(System.currentTimeMillis());
					int year = cal.get(Calendar.YEAR);
					int month = cal.get(Calendar.MONTH);
					int day = cal.get(Calendar.DAY_OF_MONTH);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					dateClickListener.onDateSelected("");// 2016-02/日类型为2016-12-6
					if (dateType.equals(DATE_TYPE_MONTH)) {						
						cal.set(Calendar.MONTH, month+1);	
						cal.set(Calendar.DAY_OF_MONTH, 1);
						try {
							String result =  sdf.format(sdf.parse(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH)));
							Toast.makeText(mContext, PublicUtils.getResourceString(mContext,R.string.dateselectview3)+result+PublicUtils.getResourceString(mContext,R.string.dateselectview2), Toast.LENGTH_SHORT).show();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					
					} else {
						cal.set(Calendar.DAY_OF_MONTH, day+1);						
						try {
//							String result =  sdf.format(sdf.parse(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH)));
							String result =  sdf.format(sdf.parse(cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)));
							Toast.makeText(mContext, PublicUtils.getResourceString(mContext,R.string.dateselectview4)+result+PublicUtils.getResourceString(mContext,R.string.dateselectview2), Toast.LENGTH_SHORT).show();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

					}
				}
			}
		} else {
			if (dateClickListener != null) {
				dateClickListener.onDateSelected(resultDate);// 2016-02/日类型为2016-12-6
			}
		}

	}

	/**
	 * 判断是否是闰年,闰年2月29天平年2月28天
	 *
	 * @param year
	 * @return
	 */
	private boolean isRunYear(String year) {
		int y = Integer.parseInt(year.trim());
		if (y % 4 == 0 && y % 100 != 0 || y % 400 == 0) {
			return true;
		} else {
			return false;
		}
	}

	class DateSpinnerAdapter extends BaseAdapter {
		private List<String> data;
		private Context context;
		private LayoutInflater inflater;

		public DateSpinnerAdapter(List<String> data, Context context) {
			this.data = data;
			this.context = context;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public String getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.date__spinner_layout, parent, false);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.spinner_textcontent);
			tv.setText(data.get(position));

			return convertView;
		}

	}

	/**
	 * 定义接口用于回调回去选中的日期
	 */
	public interface OnDateClickListener {
		public void onDateSelected(String date);
	}

	/**
	 * 获取当前年份
	 * 
	 * @return
	 */
	private String getCurrentYear() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		return year + "";
	}

	/**
	 * 获取当前年份,月份
	 * 
	 * @return
	 */
	private String getCurrentYearAndMonth() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		return year + "-" + month;
	}

	/**
	 * 根据传入的参数与今天的date进行比较,是否在其之后
	 * 
	 * @param date
	 *            日期 2016-12-12
	 * @param dateType
	 *            日期类型 年类型 周类型 月类型
	 * @return
	 */
	private boolean compareCurrentDate(String date, String dateType) {
		SimpleDateFormat sdf = null;
		boolean flag = false;
		if (dateType.equals(DATE_TYPE_MONTH)) {
			sdf = new SimpleDateFormat("yyyy-MM");
			try {
				Date d = sdf.parse(date);
				Date nowDate = new Date(System.currentTimeMillis());
				if (nowDate.before(d)) {// 如果此刻的日期在选择的月份之前,也就是选择的月份是下个月
					return true;
				} else {
					return false;
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (dateType.equals(DATE_TYPE_WEEK)) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date d = sdf.parse(date);
				Date nowDate = new Date(System.currentTimeMillis());
				if (nowDate.before(d)) {// 如果此刻的日期在选择的日期之前,也就是选择的周是下个周
					return true;
				} else {
					return false;
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (dateType.equals(DATE_TYPE_DAY)) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date d = sdf.parse(date);
				Date nowDate = new Date(System.currentTimeMillis());
				if (nowDate.before(d)) {// 如果此刻的日期在选择的日期之前,也就是选择的今天以后
					return true;
				} else {
					return false;
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return flag;
	}

	/**
	 * 获取当前月份
	 * 
	 * @return
	 */
	private String getCurrentMonth() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH) + 1;
		return month + "";
	}

	/**
	 * 根据年月获取本月有多少天 参数格式:2016-12
	 */
	private int getMonthDays(String str) {
		int days = 0;
		String[] ym = str.split("-");
		int year = Integer.parseInt(ym[0]);
		int month = Integer.parseInt(ym[1]);
		if (month == 2) {
			if (isRunYear(year + "")) {
				days = 29;
			} else {
				days = 28;
			}

		} else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {

			days = 31;

		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			days = 30;
		}
		return days;
	}

	/**
	 * 根据指定日期设置选择时间 月类型:2016-12-05 周类型:2016-08-03 日类型:2016-12-23
	 */

	public void setSpecifiedDate(String dateStr, String type) {
		if(!TextUtils.isEmpty(dateStr)){
			String[] rDates = dateStr.split("-");
			initYear = Integer.parseInt(rDates[0]);
			initMonth = Integer.parseInt(rDates[1]);
			initDay = Integer.parseInt(rDates[2]);
			if (type.equals(DATE_TYPE_DAY)) {
//				dateButton.setText(rDates[0] + PublicUtils.getResourceString(mContext,R.string.year) + rDates[1] + PublicUtils.getResourceString(mContext,R.string.month));
				dateButton.setText(rDates[0] + "." + rDates[1] );
				addDayData(dateStr);
				// 后面数字设置为指定日
			} else if (type.equals(DATE_TYPE_WEEK)) {
//				dateButton.setText(rDates[0] + PublicUtils.getResourceString(mContext,R.string.year) + rDates[1] + PublicUtils.getResourceString(mContext,R.string.month));
				dateButton.setText(rDates[0] + "." + rDates[1] );
				addWeekData(rDates[0] + "-" + rDates[1]);

			} else if (type.equals(DATE_TYPE_MONTH)) {
//				dateButton.setText(rDates[0] + PublicUtils.getResourceString(mContext,R.string.year));
				dateButton.setText(rDates[0] +"");
			}
		}
	}

	/**
	 * 是否启用时间比较,启用后选择日期只能为以后
	 * 
	 */

	public void setDateCompare(boolean f) {
		isCompareDate = f;
	}

	/**
	 * 根据传入的数组来控制数字表小红点显示与隐藏
	 */
	public LinearLayout getContainer() {
		return number_container;

	}
	public HorizontalScrollView getScrollView() {
		return mScrollView;
		
	}
	/**
	 * 清空所有选中状态
	 */
	public void clearAllSelected(){
		
	 for (int i = 0; i < number_container.getChildCount(); i++) {
		NumberTextView n = (NumberTextView) number_container.getChildAt(i);
		n.setTextNoSelected();
	}
		
	}
	
	/**
	 * 是否使用选中状态的判断
	 */
	public void isUseTextSelected(boolean flag){
		isUseTextSelected = flag;
	}

}
