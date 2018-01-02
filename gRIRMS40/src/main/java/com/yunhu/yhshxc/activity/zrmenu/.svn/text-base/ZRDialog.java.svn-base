package com.yunhu.yhshxc.activity.zrmenu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.AssetsBean;

/**
 * Created by xuelinlin on 2017/10/20.
 */

public class ZRDialog extends Dialog {
    Context mContext;
    private TextView tv_zt;
    private TextView tv_mc;
    private TextView tv_tm;
    private TextView tv_sn;
    private TextView tv_qy;
    private TextView tv_dd;
    private TextView tv_gs;
    private TextView tv_bm;
    private TextView tv_lb;
    private TextView tv_xflb;
    private TextView tv_zclb;
    private TextView tv_xzlb;
    private TextView tv_zt1;
    private TextView tv_mc1;
    private TextView tv_tm1;
    private TextView tv_sn1;
    private TextView tv_qy1;
    private TextView tv_dd1;
    private TextView tv_gs1;
    private TextView tv_bm1;
    private TextView tv_lb1;
    private TextView tv_xflb1;
    private TextView tv_zclb1;
    private TextView tv_xzlb1;

    public ZRDialog(Context context) {
        super(context, R.style.MyDialog);
        this.mContext=context;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zrmodule_dialog);
        initView();
    }

    private void initView() {
        tv_zt = (TextView) findViewById(R.id.tv_zt);
        tv_mc = (TextView) findViewById(R.id.tv_mc);
        tv_tm = (TextView) findViewById(R.id.tv_tm);
        tv_sn = (TextView) findViewById(R.id.tv_sn);
        tv_qy = (TextView) findViewById(R.id.tv_qy);
        tv_dd = (TextView) findViewById(R.id.tv_dd);
        tv_gs = (TextView) findViewById(R.id.tv_gs);
        tv_bm = (TextView) findViewById(R.id.tv_bm);
        tv_lb = (TextView) findViewById(R.id.tv_lb);
        tv_xflb = (TextView) findViewById(R.id.tv_xflb);
        tv_zclb = (TextView) findViewById(R.id.tv_zclb);
        tv_xzlb = (TextView) findViewById(R.id.tv_xzlb);
        tv_zt1 = (TextView) findViewById(R.id.tv_zt1);
        tv_mc1 = (TextView) findViewById(R.id.tv_mc1);
        tv_tm1 = (TextView) findViewById(R.id.tv_tm1);
        tv_sn1 = (TextView) findViewById(R.id.tv_sn1);
        tv_qy1 = (TextView) findViewById(R.id.tv_qy1);
        tv_dd1 = (TextView) findViewById(R.id.tv_dd1);
        tv_gs1 = (TextView) findViewById(R.id.tv_gs1);
        tv_bm1 = (TextView) findViewById(R.id.tv_bm1);
        tv_lb1 = (TextView) findViewById(R.id.tv_lb1);
        tv_xflb1 = (TextView) findViewById(R.id.tv_xflb1);
        tv_zclb1 = (TextView) findViewById(R.id.tv_zclb1);
        tv_xzlb1 = (TextView) findViewById(R.id.tv_xzlb1);
    }

    public void initData(AssetsBean bean){
        if(bean!=null){
            tv_zt.setText(!TextUtils.isEmpty(bean.getState())?bean.getState():"");
            tv_mc.setText(!TextUtils.isEmpty(bean.getTitle())?bean.getTitle():"");
            tv_tm.setText(!TextUtils.isEmpty(bean.getCode())?bean.getCode():"");
            tv_sn.setText(!TextUtils.isEmpty(bean.getSn())?bean.getSn():"");
            tv_qy.setText(!TextUtils.isEmpty(bean.getArea())?bean.getArea():"");
            tv_dd.setText(!TextUtils.isEmpty(bean.getPutAddress())?bean.getPutAddress():"");
            tv_gs.setText(!TextUtils.isEmpty(bean.getUseCompany())?bean.getUseCompany():"");
            tv_bm.setText(!TextUtils.isEmpty(bean.getUsePart())?bean.getUsePart():"");
            tv_lb.setText(!TextUtils.isEmpty(bean.getKind())?bean.getKind():"");
            tv_xflb.setText(!TextUtils.isEmpty(bean.getFineKind())?bean.getFineKind():"");
            tv_zclb.setText(!TextUtils.isEmpty(bean.getProKind())?bean.getProKind():"");
            tv_xzlb.setText(!TextUtils.isEmpty(bean.getBelongKind())?bean.getBelongKind():"");
            tv_zt1.setText(!TextUtils.isEmpty(bean.getStateTip())?bean.getStateTip():"");
            tv_mc1.setText(!TextUtils.isEmpty(bean.getTitleTip())?bean.getTitleTip():"");
            tv_tm1.setText(!TextUtils.isEmpty(bean.getCodeTip())?bean.getCodeTip():"");
            tv_sn1.setText(!TextUtils.isEmpty(bean.getSnTip())?bean.getSnTip():"");
            tv_qy1.setText(!TextUtils.isEmpty(bean.getAreaTip())?bean.getAreaTip():"");
            tv_dd1.setText(!TextUtils.isEmpty(bean.getPutAddressTip())?bean.getPutAddressTip():"");
            tv_gs1.setText(!TextUtils.isEmpty(bean.getUseCompanyTip())?bean.getUseCompanyTip():"");
            tv_bm1.setText(!TextUtils.isEmpty(bean.getUsePartTip())?bean.getUsePartTip():"");
            tv_lb1.setText(!TextUtils.isEmpty(bean.getKindTip())?bean.getKindTip():"");
            tv_xflb1.setText(!TextUtils.isEmpty(bean.getFineKindTip())?bean.getFineKindTip():"");
            tv_zclb1.setText(!TextUtils.isEmpty(bean.getProKindTip())?bean.getProKindTip():"");
            tv_xzlb1.setText(!TextUtils.isEmpty(bean.getBelongKindTip())?bean.getBelongKindTip():"");
        }

    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity= Gravity.BOTTOM;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.MATCH_PARENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);

    }

}
