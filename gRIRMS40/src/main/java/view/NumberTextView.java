package view;

import com.yunhu.yhshxc.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by qingli on 2016/12/9. 展示数字的单位view
 */

public class NumberTextView extends LinearLayout {
	private TextView mTextView;
	private ImageView mImageView;
	private int textColor = 0;
	private int index;// 该控件的位置

	public NumberTextView(Context context) {
		this(context, null);
	}

	public NumberTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NumberTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		View view = LayoutInflater.from(context).inflate(R.layout.date_selectorview_number, null);
		mTextView = (TextView) view.findViewById(R.id.date_selectorview_number_text);
		mImageView = (ImageView) view.findViewById(R.id.date_selectorview_number_point);
		mTextView.setTextColor(getResources().getColor(R.color.app_color_low));
		mImageView.setVisibility(INVISIBLE);
		addView(view);
	}

	public void setTextColor(int color) {
		this.textColor = color;
	}

	/**
	 * 获取控件的位置
	 * 
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * 设置控件的位置
	 * 
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * 设置控件内容
	 * 
	 * @param text
	 */
	public void setText(String text) {
		mTextView.setText(text);
	}

	/**
	 * 设置文本下部远原点是否显示
	 * 
	 * @param flag
	 */
	public void showPointCircle(boolean flag) {
		if (flag) {
			mImageView.setVisibility(VISIBLE);
		} else {
			mImageView.setVisibility(INVISIBLE);
		}
	}

	/**
	 * 获取当前view显示的内容
	 * 
	 * @return
	 */
	public String getText() {
		return mTextView.getText().toString();
	}

	/**
	 * 设置选中的背景颜色
	 */
	public void setTextSelected() {
		mTextView.setTextColor(Color.WHITE);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_shape_bg_blue));
	}

	/**
	 * 取消背景色
	 */
	public void setTextNoSelected() {
		setBackgroundColor(Color.TRANSPARENT);
		mTextView.setTextColor(getResources().getColor(R.color.app_color_low));

	}
}
