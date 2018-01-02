package com.yunhu.yhshxc.func;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.AssetsBean;
import com.yunhu.yhshxc.bo.Func;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.bitmap;
import static android.R.attr.data;
import static android.R.attr.id;


/**
 * 新FuncView控制
 */

public class ListFuncViewT1 implements View.OnClickListener {

    private Context mContext;
    private View view;
    private ImageView showMenu, addOne, scan;//展示菜单按钮,添加按钮,扫一扫按钮
    private LinearLayout mItemContainer;
    private boolean isShowing = false;
    private List<AssetsBean> data;//选择的数据
    private OnListFuncListener mListener;//点击回调
    private RequestOptions requestOptions;

    private boolean currentUp = true;
    private Func mFunc;

    public ListFuncViewT1(Context context) {
        this.mContext = context;
        initView();
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.image_loading);
        requestOptions.error(R.drawable.image_failed);
    }

    private void initView() {
        data = new ArrayList<>();
        view = LayoutInflater.from(mContext).inflate(R.layout.list_func_view_t1, null);
        showMenu = (ImageView) view.findViewById(R.id.iv_show_menu);
        addOne = (ImageView) view.findViewById(R.id.iv_add_one);
        scan = (ImageView) view.findViewById(R.id.iv_scan);
        mItemContainer = (LinearLayout) view.findViewById(R.id.mListView);
        showMenu.setOnClickListener(this);
        addOne.setOnClickListener(this);
        scan.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_show_menu://展示菜单
//                if (mItemContainer.getChildCount()>1&&currentUp){
//                    showOrHideMenu(true);
//                }else{
                showOrHideMenu(false);
//                }
                break;

            case R.id.iv_add_one://添加内容
                if (mListener != null&&mFunc!=null) {
                    mListener.onAdd(mFunc);
                }
                break;

            case R.id.iv_scan://扫一扫
                if (mListener != null&&mFunc!=null) {
                    mListener.onScan(mFunc);
                }
                break;
        }

    }


    private void showOrHideMenu(boolean up) {
        //展示其他两个菜单,上移,渐变,缩放
        int addHeight = showMenu.getMeasuredHeight();
        int transV1 = (int) (addHeight + addHeight * 0.2 + 10);
        int transV2 = (int) (addHeight + addHeight * 0.2) * 2 + 10;

        PropertyValuesHolder transY1;
        PropertyValuesHolder transY2;
        PropertyValuesHolder scaleX;
        PropertyValuesHolder scaleY;
        PropertyValuesHolder transY1_;
        PropertyValuesHolder transY2_;
        PropertyValuesHolder scaleX_;
        PropertyValuesHolder scaleY_;
        PropertyValuesHolder rotation;
        PropertyValuesHolder rotation_;

//        if (up){
//            transY1 = PropertyValuesHolder.ofFloat("translationY", -transV1);
//            transY2 = PropertyValuesHolder.ofFloat("translationY", -transV2);
//            scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
//            scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
//            transY1_ = PropertyValuesHolder.ofFloat("translationY", transV1);
//            transY2_ = PropertyValuesHolder.ofFloat("translationY", transV2);
//            scaleX_ = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
//            scaleY_ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
//            rotation = PropertyValuesHolder.ofFloat("rotation", 45);
//            rotation_ = PropertyValuesHolder.ofFloat("rotation", 0);
//            if (isShowing){
//                currentUp=false;
//            }else{
//                currentUp=true;
//            }
//
//        }else{
        transY1 = PropertyValuesHolder.ofFloat("translationX", -transV1);
        transY2 = PropertyValuesHolder.ofFloat("translationX", -transV2);
        scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        transY1_ = PropertyValuesHolder.ofFloat("translationX", transV1);
        transY2_ = PropertyValuesHolder.ofFloat("translationX", transV2);
        scaleX_ = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
        scaleY_ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
        rotation = PropertyValuesHolder.ofFloat("rotation", -45);
        rotation_ = PropertyValuesHolder.ofFloat("rotation", 0);
//            if (isShowing){
//                currentUp =  false;
//            }else{
//                currentUp = true;
//            }
//
//        }


        if (isShowing) {
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(addOne, transY1_, scaleX_, scaleY_);
            objectAnimator1.setDuration(200);
            objectAnimator1.start();
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofPropertyValuesHolder(scan, transY2_, scaleX_, scaleY_);
            objectAnimator2.setDuration(200);
            objectAnimator2.start();
            ObjectAnimator objectAnimator3 = ObjectAnimator.ofPropertyValuesHolder(showMenu, rotation_);
            objectAnimator3.setDuration(200);
            objectAnimator3.start();

            objectAnimator2.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    addOne.setVisibility(View.INVISIBLE);
                    scan.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            isShowing = false;

        } else {
            addOne.setVisibility(View.VISIBLE);
            scan.setVisibility(View.VISIBLE);
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(addOne, transY1, scaleX, scaleY);
            objectAnimator1.setDuration(200);
            objectAnimator1.start();
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofPropertyValuesHolder(scan, transY2, scaleX, scaleY);
            objectAnimator2.setDuration(200);
            objectAnimator2.start();
            ObjectAnimator objectAnimator3 = ObjectAnimator.ofPropertyValuesHolder(showMenu, rotation);
            objectAnimator3.setDuration(200);
            objectAnimator3.start();
            isShowing = true;


        }

    }


    /**
     * 添加一个数据条目
     *
     */
    public void addOneItem(AssetsBean bean) {
        String url = bean.getUrl();
        String name = bean.getTitle();
        String id = bean.getId();//资产id
        String code = bean.getCode();//条码
        final View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_func_item_t1, null);
        ImageView itemIcon = (ImageView) itemView.findViewById(R.id.list_func_icon);
        ImageView itemDel = (ImageView) itemView.findViewById(R.id.list_func_del);
        TextView itemTitle = (TextView) itemView.findViewById(R.id.list_func_title);
        TextView itemId = (TextView) itemView.findViewById(R.id.list_func_id);
        itemView.setTag(id);
        Glide.with(mContext).setDefaultRequestOptions(requestOptions).asBitmap().load(url).into(itemIcon);
        if (!TextUtils.isEmpty(name)) {
            itemTitle.setText(name);
        }
        if (!TextUtils.isEmpty(code)) {
            itemId.setText(code);
        }

        itemDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemContainer.removeView(itemView);
                String idTag = (String) itemView.getTag();
                for (int i = 0; i < data.size(); i++) {
                    AssetsBean assetsBean = data.get(i);
                    if (assetsBean.getId().equals(idTag)) {
                        data.remove(assetsBean);
                        if (mListener!=null){
                            mListener.onDelete(assetsBean.getId());
                        }
                    }
                }

            }
        });

        data.add(bean);
        mItemContainer.addView(itemView);
    }


    /**
     * 获取整个控件的布局
     *
     * @return
     */
    public View getView() {
        return view;
    }

    /**
     * 获取添加的数据id
     *
     * @return
     */
    public List<AssetsBean> getData() {

        return data;
    }
    //清空数据,清空view
    public void celarAll(){
        mItemContainer.removeAllViews();
        data.clear();
    }

    /**
     *  设置当前控件属性
     * @param func
     */
   public void setFunc(Func func){

       this.mFunc = func;
   }
   public Func getmFunc(){

       return mFunc;
   }

    public void setOnListFuncListener(OnListFuncListener listener) {
        this.mListener = listener;
    }

    interface OnListFuncListener {
        //添加数据
        void onAdd(Func func);

        //扫一扫
        void onScan(Func func);

        //移除的回调

        void onDelete(String assId);


    }

}
