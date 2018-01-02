package com.yunhu.yhshxc.comp;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;

public class RadiobtnComp extends Component implements OnCheckedChangeListener {
	private Context context;
	private boolean isChecked;
	private Func func;
	private RadioButton radioButtonFirst;
	private RadioButton radioButtonSecond;

	public RadiobtnComp(Context context, Func func) {
		this.context = context;
		this.func = func;
		this.type = func.getType();
	}

	@Override
	public View getObject() {
		View view = View.inflate(context, R.layout.radiobtn_comp, null);
		RadioGroup radioGroup = (RadioGroup) view
				.findViewById(R.id.radioButtonComp);
		radioButtonFirst = (RadioButton) radioGroup.getChildAt(0);
		radioButtonSecond = (RadioButton) radioGroup.getChildAt(1);
		radioGroup.setOnCheckedChangeListener(this);
		return view;
	}

	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public void setIsEnable(boolean isEnable) {
		getObject().setEnabled(isEnable);
	}

	public Func getFunc() {
		return func;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == radioButtonFirst.getId()) {
			/* 选中第一个 */
			RadiobtnComp.this.value = radioButtonFirst.getText().toString();
		} else if (checkedId == radioButtonSecond.getId()) {
			/* 选中第二个 */
			RadiobtnComp.this.value = radioButtonSecond.getText().toString();

		}
	}

}
