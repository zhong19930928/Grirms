package com.yunhu.yhshxc.attendance.attendCalendar;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.attendance.calendar.CardGridItem;
import com.yunhu.yhshxc.attendance.calendar.CheckableLayout;
import com.yunhu.yhshxc.attendance.calendar.OnCellItemClick;
import com.yunhu.yhshxc.attendance.calendar.OnItemRender;

public class AttendCalendarCard extends RelativeLayout {

	private int itemLayout = R.layout.attend_card_item_simple;
	private OnItemRender mOnItemRender;
	private OnItemRender mOnItemRenderDefault;
	private OnCellItemClick mOnCellItemClick;
	private Calendar dateDisplay;
	public ArrayList<CheckableLayout> cells = new ArrayList<CheckableLayout>();
	private LinearLayout cardGrid;

	public AttendCalendarCard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public AttendCalendarCard(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AttendCalendarCard(Context context) {
		super(context);
		init(context);
	}

	private void init(Context ctx) {
		if (isInEditMode())
			return;
		View layout = LayoutInflater.from(ctx).inflate(R.layout.card_view,
				null, false);

		if (dateDisplay == null)
			dateDisplay = Calendar.getInstance();
		
		cardGrid = (LinearLayout) layout.findViewById(R.id.cardGrid);
		
		LayoutInflater la = LayoutInflater.from(ctx);
		for (int y = 0; y < cardGrid.getChildCount(); y++) {
			LinearLayout row = (LinearLayout) cardGrid.getChildAt(y);
			for (int x = 0; x < row.getChildCount(); x++) {
				CheckableLayout cell = (CheckableLayout) row.getChildAt(x);
				cell.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (getOnCellItemClick() != null)
							getOnCellItemClick().onCellClick(v,(CardGridItem) v.getTag());
					}
				});

				View cellContent = la.inflate(itemLayout, cell, false);
				cell.addView(cellContent);
				cells.add(cell);
			}
		}

		addView(layout);

		mOnItemRenderDefault = new OnItemRender() {
			@Override
			public void onRender(CheckableLayout v, CardGridItem item) {
				((TextView) v.findViewById(R.id.attend_textView)).setText(item.getDayOfMonth().toString());
			}
		};

		updateCells();
	}

	private int getDaySpacing(int dayOfWeek) {
		if (Calendar.SUNDAY == dayOfWeek)
			return 6;
		else
			return dayOfWeek - 2;
	}

	private int getDaySpacingEnd(int dayOfWeek) {
		if (Calendar.SUNDAY == dayOfWeek)
			return 0;
		return 8 - dayOfWeek;
	}

	private void updateCells() {
		Calendar cal;
		Integer counter = 0;
		if (dateDisplay != null)
			cal = (Calendar) dateDisplay.clone();
		else
			cal = Calendar.getInstance();

		cal.set(Calendar.DAY_OF_MONTH, 1);

		int daySpacing = getDaySpacing(cal.get(Calendar.DAY_OF_WEEK));

		// INFO : wrong calculations of first line - fixed
		if (daySpacing > 0) {
			Calendar prevMonth = (Calendar) cal.clone();
			prevMonth.add(Calendar.MONTH, -1);
			prevMonth.set(Calendar.DAY_OF_MONTH,
					prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
							- daySpacing + 1);
			for (int i = 0; i < daySpacing; i++) {
				CheckableLayout cell = cells.get(counter);
				cell.setTag(new CardGridItem(Integer.valueOf(prevMonth
						.get(Calendar.DAY_OF_MONTH))).setEnabled(false));
				cell.setEnabled(false);
				(mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender)
						.onRender(cell, (CardGridItem) cell.getTag());
				counter++;
				prevMonth.add(Calendar.DAY_OF_MONTH, 1);
			}
		}

		int firstDay = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH,
				cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		int lastDay = cal.get(Calendar.DAY_OF_MONTH) + 1;
		for (int i = firstDay; i < lastDay; i++) {
			cal.set(Calendar.DAY_OF_MONTH, i - 1);
			Calendar date = (Calendar) cal.clone();
			date.add(Calendar.DAY_OF_MONTH, 1);
			CheckableLayout cell = cells.get(counter);
			cell.setTag(new CardGridItem(i).setEnabled(true).setDate(date));
			cell.setEnabled(true);
			cell.setVisibility(View.VISIBLE);
			(mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender)
					.onRender(cell, (CardGridItem) cell.getTag());
			counter++;
		}

		if (dateDisplay != null)
			cal = (Calendar) dateDisplay.clone();
		else
			cal = Calendar.getInstance();

		cal.set(Calendar.DAY_OF_MONTH,
				cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		daySpacing = getDaySpacingEnd(cal.get(Calendar.DAY_OF_WEEK));

		if (daySpacing > 0) {
			for (int i = 0; i < daySpacing; i++) {
				CheckableLayout cell = cells.get(counter);
				cell.setTag(new CardGridItem(i + 1).setEnabled(false)); // .setDate((Calendar)cal.clone())
				cell.setEnabled(false);
				cell.setVisibility(View.VISIBLE);
				(mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender)
						.onRender(cell, (CardGridItem) cell.getTag());
				counter++;
			}
		}

		if (counter < cells.size()) {
			for (int i = counter; i < cells.size(); i++) {
				cells.get(i).setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && cells.size() > 0) {
			int size = (r - l) / 7;
			for (CheckableLayout cell : cells) {
				cell.getLayoutParams().height = size;
			}
		}
	}

	public int getItemLayout() {
		return itemLayout;
	}

	public void setItemLayout(int itemLayout) {
		this.itemLayout = itemLayout;
		// mCardGridAdapter.setItemLayout(itemLayout);
	}

	public OnItemRender getOnItemRender() {
		return mOnItemRender;
	}

	public void setOnItemRender(OnItemRender mOnItemRender) {
		this.mOnItemRender = mOnItemRender;
		// mCardGridAdapter.setOnItemRender(mOnItemRender);
	}

	public Calendar getDateDisplay() {
		return dateDisplay;
	}

	public void setDateDisplay(Calendar dateDisplay) {
		this.dateDisplay = dateDisplay;
	}

	public OnCellItemClick getOnCellItemClick() {
		return mOnCellItemClick;
	}

	public void setOnCellItemClick(OnCellItemClick mOnCellItemClick) {
		this.mOnCellItemClick = mOnCellItemClick;
	}

	/**
	 * call after change any input data - to refresh view
	 */
	public void notifyChanges() {
		// mCardGridAdapter.init();
		updateCells();
	}

}
