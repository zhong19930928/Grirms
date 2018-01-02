package com.yunhu.yhshxc.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class GCGButton extends Button {

	public GCGButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GCGButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GCGButton(Context context) {
		super(context);
	}

	@Override
	public void setPressed(boolean pressed) {
		if (pressed && getParent() instanceof View && ((View) getParent()).isPressed()) {
			return;
		}
		super.setPressed(pressed);
	}

}
