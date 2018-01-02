package com.yunhu.yhshxc.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
//import com.gcg.grirms.activity.HomeMenuFragment;
import com.yunhu.yhshxc.activity.HomePageActivity;

public class CloseAppDialogActivity extends AbsBaseActivity {
	
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.get_push_dialog);
		Button confirmBtn=(Button)this.findViewById(R.id.btn_get_push);
		TextView tv=(TextView)this.findViewById(R.id.get_push_content);
		tv.setText(this.getResources().getString(R.string.push_update_data));
		confirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CloseAppDialogActivity.this,HomePageActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("CLOSE", 1);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_BACK == keyCode){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
