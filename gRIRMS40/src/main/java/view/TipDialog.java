package view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yunhu.yhshxc.R;

import static com.yunhu.yhshxc.R.drawable.tb;
import static com.yunhu.yhshxc.R.id.btn_tip_confirm;

/**
 * Created by xuelinlin on 2017/6/28.
 */

public class TipDialog extends Dialog {
    public TipDialog(Context context){
        super(context);
    }
    public TipDialog(Context context,int them){
        super(context,them);
//        View view = LayoutInflater.from(context).inflate(R.layout.tip_layout_dialog,null);
//        setContentView(view);
        setContentView(R.layout.tip_layout_dialog);
        LinearLayout btn_tip_confirm = (LinearLayout) findViewById(R.id.btn_tip_confirm);
        btn_tip_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        this.setCancelable(false);
    }
}
