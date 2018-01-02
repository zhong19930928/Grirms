package com.yunhu.yhshxc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Func;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.bo.Submit;
import com.yunhu.yhshxc.comp.TitleBar;
import com.yunhu.yhshxc.database.DoubleDB;
import com.yunhu.yhshxc.database.FuncDB;
import com.yunhu.yhshxc.database.ModuleDB;
import com.yunhu.yhshxc.database.SubmitDB;
import com.yunhu.yhshxc.doubletask.DoubleWayActivity;
import com.yunhu.yhshxc.doubletask2.NewDoubleListActivity;
import com.yunhu.yhshxc.list.activity.TableListActivity;
import com.yunhu.yhshxc.list.activity.TableListActivityNew;
import com.yunhu.yhshxc.module.replenish.QueryFuncActivity;
import com.yunhu.yhshxc.pay.OrderListActivity;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.widget.ToastOrder;

import java.util.List;

import gcg.org.debug.ELog;

/**
 * 自定义模块类 activity 有 “上报” “查询” “双向执行” “下发” “审核” “改派” “修改” “支付”中的一项或多项 "上报"
 * 用户可以上报数据 "双向执行"用户可以执行或取消双向任务（是否可取消根据后台配置） "下发"
 * 用户可以给权限内的用户下发双向任务，下发成功后会在双向列表中显示 "改派" 可以将任务改派给权限内的其他用户 "修改"
 * 上报以后用户可以修改已经上报过的数据 "支付" 通过银联付款
 *
 * @author jishen
 * @version 2013-05-29
 */
public class ModuleActivity extends AbsBaseActivity {

    /**
     * 页面主view里面添加模块view
     */
    private LinearLayout moduleLayout;
    private GridView gv_module_layout;
    /**
     * 自定义模块的集合
     */
    private List<Module> moduleList;
    /**
     * 自定义模块的菜单ID
     */
    private int menuId;
    /**
     * 自定义模块的菜单类型
     */
    private int menuType;
    /**
     * 封装了顶部用于显示信号等信息的组件
     */
    private TitleBar titlebar;

    private boolean isNoWait = true;
    private int is_store_expand;
    String menuName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuId = getIntent().getIntExtra("menuId", 0);// 获取home页面传过来的菜单ID
        menuType = getIntent().getIntExtra("menuType", 0);// 获取home页面传过来的菜单类型
        isNoWait = getIntent().getBooleanExtra("isNoWait", true);
        menuName = getIntent().getStringExtra("menuName");
        is_store_expand = getIntent().getIntExtra("is_store_expand", 0);// 获取是否是店面拓展模块
        setContentView(R.layout.module_layout);


        TextView title_name = (TextView) findViewById(R.id.title_name);
        if (menuName != null) {
            title_name.setText(menuName);
        }
        // View bar = findViewById(R.id.rl_titlebar);
        // titlebar = new TitleBar(this, bar);
        // titlebar.register();//添加顶部组件

        // 从数据库中查询所有要显示的数据
        moduleList = new ModuleDB(this).findModuleByMenuId(menuId);
        boolean isCatchData = getIntent().getBooleanExtra("isCatchData", false);
        if (!moduleList.isEmpty()) {
            Log.d(TAG, String.valueOf(moduleList.size()));
            if (moduleList.size() == 1) {// 如果只有一项自定义项就直接跳转
                onClickForModule(moduleList.get(0));
            } else {
                initLayout(moduleList);// 有多项的情况就初始化页面
                if (isCatchData) {
                    Submit submit = new SubmitDB(this).findSubmit(null, Constants.DEFAULTINT, Constants.DEFAULTINT,
                            Constants.DEFAULTINT, menuId, menuType, null, null, null, null, null);
                    for (int i = 0; i < moduleList.size(); i++) {
                        Module module = moduleList.get(i);
                        if (module.getType() == submit.getModType()) {
                            switch (submit.getModType()) {
                                case 1:
                                    intentReport(module);// 上报
                                    break;
                                case 2:
                                    issued(module);// 下发
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        }
        initBase();// 父类的方法，目前没做处理
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

    }

    /**
     * 初始化界面
     *
     * @param moduleList 所有的自定义模块的集合
     *                   分等于3和不等于3的两种情况加载自定义模块的两种布局
     */
    private void initLayout(final List<Module> moduleList) {
        // 往这个布局里面添加
        float density = getResources().getDisplayMetrics().density;
        moduleLayout = (LinearLayout) this.findViewById(R.id.ll_module_layout);
        gv_module_layout = (GridView) this.findViewById(R.id.gv_module_layout);
        if (moduleList != null) {
            int len = moduleList.size();
            if (len == 3) {//自定义模块数量等于三的时候
                this.findViewById(R.id.scroll_module).setVisibility(View.VISIBLE);
                this.findViewById(R.id.scroll_module2).setVisibility(View.GONE);
                for (int i = 0; i < len; i++) {
                    // 自定义模块的view
                    final View item;
                    if (i == 1) {
                        item = View.inflate(getApplicationContext(), R.layout.module_item_right3, null);
                    } else {
                        item = View.inflate(getApplicationContext(), R.layout.module_item_left3, null);
                    }
                    // item.setBackgroundResource(R.color.visit_item_disorder);
                    item.setTag(moduleList.get(i));
                    // 设置view的内容
                    ImageView iconLeft = (ImageView) item.findViewById(R.id.leftIcon),
                            iconRight = (ImageView) item.findViewById(R.id.rightIcon);
                    TextView label = (TextView) item.findViewById(R.id.label);
                    label.setText(moduleList.get(i).getName());
                    setSwitch(moduleList.get(i).getType(),iconLeft);//根据条件设置人物头像

                    // 给自定义的view添加单击事件
                    item.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            onClickForModule((Module) v.getTag());
                        }
                    });
                    // 给自定义的view添加触摸事件 改变背景颜色
                    item.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    // item.setBackgroundResource(R.color.home_menu_pressed);
                                    break;

                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_CANCEL:
                                case MotionEvent.ACTION_OUTSIDE:
                                    // item.setBackgroundResource(R.color.visit_item_disorder);
                                    break;
                            }
                            return false;
                        }
                    });
                    // LinearLayout.LayoutParams lp = new
                    // LayoutParams(LayoutParams.WRAP_CONTENT,
                    // LayoutParams.WRAP_CONTENT, 1);
                    // lp.bottomMargin = (int)(5 * density);
                    // moduleLayout.addView(item, lp);//将自定义的view添加到父view中
                    moduleLayout.addView(item);
                }
            } else {//自定义模块数量不等于三的时候
                this.findViewById(R.id.scroll_module).setVisibility(View.GONE);
                this.findViewById(R.id.scroll_module2).setVisibility(View.VISIBLE);
                gv_module_layout.setAdapter(new gvAdapter(moduleList));
                gv_module_layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        onClickForModule(moduleList.get(i));
                    }
                });

            }
        }
    }

    void setResource(ImageView iconLeft,@DrawableRes int engineerResId,@DrawableRes int workerResId,@DrawableRes int salesResId){
        if (PublicUtils.COMBINE_TYPE.equals("sh")) {
            iconLeft.setImageResource(engineerResId);
        } else if (PublicUtils.COMBINE_TYPE.equals("aj")
                || PublicUtils.COMBINE_TYPE.equals("xj")
                || PublicUtils.COMBINE_TYPE.equals("pg")
                || PublicUtils.COMBINE_TYPE.equals("xc")) {
            iconLeft.setImageResource(workerResId);
        } else {
            iconLeft.setImageResource(salesResId);
        }
    }

    void setSwitch(Integer type,ImageView iconLeft ){
        switch (type) {
            case Constants.MODULE_TYPE_EXECUTE:// 双向执行
            case Constants.MODULE_TYPE_EXECUTE_NEW:// 新双向执行
                setResource(iconLeft,R.drawable.second_engineer_perform,R.drawable.second_worker_preform,R.drawable.sales_perform_new);
                break;
            case Constants.MODULE_TYPE_QUERY:// 查询
                setResource(iconLeft,R.drawable.second_engineer_query,R.drawable.second_worker_query,R.drawable.sales_query_new);
                break;
            case Constants.MODULE_TYPE_REPORT:// 上报
                setResource(iconLeft,R.drawable.second_engineer_report,R.drawable.second_worker_report,R.drawable.sales_report_new);
                break;
            case Constants.MODULE_TYPE_ISSUED:// 下发
                setResource(iconLeft,R.drawable.second_engineer_command,R.drawable.second_worker_command,R.drawable.sales_command_new);
                break;
            case Constants.MODULE_TYPE_VERIFY:// 审核
                setResource(iconLeft,R.drawable.second_engineer_audit,R.drawable.second_worker_audit,R.drawable.sales_audit_new);

                break;
            case Constants.MODULE_TYPE_REASSIGN:// 改派
                setResource(iconLeft,R.drawable.second_engineer_send,R.drawable.second_worker_send,R.drawable.sales_send_new);
                break;
            case Constants.MODULE_TYPE_UPDATE:// 修改
                setResource(iconLeft,R.drawable.second_engineer_modify,R.drawable.second_worker_modify,R.drawable.sales_modify_new);
                break;
            case Constants.MODULE_TYPE_PAY:// 支付
                setResource(iconLeft,R.drawable.second_engineer_pay,R.drawable.second_worker_pay,R.drawable.sales_pay_new);
                break;
        }
    }

    /**
     * 根据模块类型跳转到不同的页面
     *
     * @param module 模块的实例
     */
    public void onClickForModule(Module module) {

        switch (module.getType()) {
            case Constants.MODULE_TYPE_EXECUTE:// 双向执行
                execute(module);
                break;
            case Constants.MODULE_TYPE_EXECUTE_NEW:// 新双向执行
                executeNew(module);
                break;
            case Constants.MODULE_TYPE_QUERY:// 查询
                intentQuery(module);
                break;
            case Constants.MODULE_TYPE_REPORT:// 上报
                intentReport(module);
                break;
            case Constants.MODULE_TYPE_ISSUED:// 下发
                issued(module);
                break;
            case Constants.MODULE_TYPE_VERIFY:// 审核
                // verify(module);
                intentQuery(module);
                break;
            case Constants.MODULE_TYPE_REASSIGN:// 改派
                // reassign(module);
                intentQuery(module);
                break;
            case Constants.MODULE_TYPE_UPDATE:// 修改
                // update(module);
                intentQuery(module);
                break;
            case Constants.MODULE_TYPE_PAY:// 支付
                pay(module);
                break;

            default:
                break;
        }

    }

    /**
     * 双向执行
     */
    private void execute(Module module) {
        int doubleTaskNumber = new DoubleDB(this).findAllDoubleTaskNumber(module.getMenuId());
        if (doubleTaskNumber == 0) {// 可执行的任务条数为0
            ToastOrder.makeText(this, PublicUtils.getResourceString(ModuleActivity.this, R.string.no_content), ToastOrder.LENGTH_SHORT).show();
        } else {// 跳转到双向列表页面
            Intent doubleIntent = new Intent(ModuleActivity.this, DoubleWayActivity.class);
            doubleIntent.putExtra("targetId", module.getMenuId());// 数据ID
            doubleIntent.putExtra("doubleHead", module.getName());// 模块名称
            doubleIntent.putExtra("isNoWait", isNoWait);
            Bundle bundle = new Bundle();
            bundle.putSerializable("module", module);// 模块实例
            doubleIntent.putExtra("bundle", bundle);
            startActivity(doubleIntent);
        }
    }

    /**
     * 双向执行
     */
    private void executeNew(Module module) {
        int doubleTaskNumber = new DoubleDB(this).findAllDoubleTaskNumber(module.getMenuId());
        if (doubleTaskNumber == 0) {// 可执行的任务条数为0
            ToastOrder.makeText(this, PublicUtils.getResourceString(ModuleActivity.this, R.string.no_content), ToastOrder.LENGTH_SHORT).show();
        } else {// 跳转到双向列表页面
            Intent doubleIntent = new Intent(ModuleActivity.this, NewDoubleListActivity.class);
            doubleIntent.putExtra("targetId", module.getMenuId());// 数据ID
            doubleIntent.putExtra("doubleHead", module.getName());// 模块名称
            doubleIntent.putExtra("isNoWait", isNoWait);
            Bundle bundle = new Bundle();
            bundle.putSerializable("module", module);// 模块实例
            doubleIntent.putExtra("bundle", bundle);
            startActivity(doubleIntent);
        }
    }

    /**
     * 上报
     *
     * @param module
     */
    private void intentReport(Module module) {

        Bundle bundle = new Bundle();
        bundle.putInt("planId", Constants.DEFAULTINT);// 计划ID
        bundle.putInt("awokeType", Constants.DEFAULTINT);// 提醒类型
        bundle.putInt("wayId", Constants.DEFAULTINT);// 线路ID
        bundle.putInt("storeId", Constants.DEFAULTINT);// 店面ID
        bundle.putInt("targetId", module.getMenuId());// 数据ID
        bundle.putInt("taskId", menuId);// 超链接sql查询的时候使用
        bundle.putString("storeName", null);// 店面名称
        bundle.putString("wayName", null);// 线路名称
        bundle.putInt("isCheckin", Constants.DEFAULTINT);// 是否要进店定位1 是 0和null否
        bundle.putInt("isCheckout", Constants.DEFAULTINT);// 是否要离店定位1 是 0和null否
        bundle.putInt("menuType", menuType);// 菜单类型
        bundle.putSerializable("module", module);// 自定义模块实例
        bundle.putInt("is_store_expand", is_store_expand);
        Intent intent = new Intent(ModuleActivity.this, ModuleFuncActivity.class);// 跳转到上报页面
        intent.putExtra("isNoWait", isNoWait);
        bundle.putInt("menuId", menuId);
        bundle.putString("menuName", menuName);
        intent.putExtra("bundle", bundle);
        startActivity(intent);

    }

    /**
     * 改派
     *
     * @param module
     */
    private void reassign(Module module) {
        Bundle bundle = new Bundle();
        bundle.putInt("targetId", module.getMenuId());// 数据ID
        bundle.putInt("menuType", menuType);// 菜单类型
        bundle.putSerializable("module", module);// 自定义模块实例
        List<Func> funcList = new FuncDB(this).findFuncListReplenish(module.getMenuId(), null, Func.IS_Y, null);
        if (funcList != null && !funcList.isEmpty()) {// 有查询条件就跳转到查询条件页面
            Intent intent = new Intent(ModuleActivity.this, QueryFuncActivity.class);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        } else {// 没有查询条件，就直接跳转到查询列表
            ELog.d("Open TableListActivity");
            Intent intent = new Intent(ModuleActivity.this, TableListActivityNew.class);
            bundle.putInt("menuId", menuId);
            bundle.putString("menuName", menuName);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        }

    }

    /**
     * 数据下发
     *
     * @param module
     */
    private void issued(Module module) {

        Bundle bundle = new Bundle();
        bundle.putInt("planId", Constants.DEFAULTINT);// 计划ID
        bundle.putInt("awokeType", Constants.DEFAULTINT);// 提醒类型
        bundle.putInt("wayId", Constants.DEFAULTINT);// 线路ID
        bundle.putInt("storeId", Constants.DEFAULTINT);// 店面ID
        bundle.putInt("targetId", module.getMenuId());// 数据ID
        bundle.putInt("menuId", menuId);
        bundle.putString("storeName", null);// 店面名称
        bundle.putString("wayName", null);// 线路名称
        bundle.putString("menuName", menuName);
        bundle.putInt("isCheckin", Constants.DEFAULTINT);// 是否要进店定位1 是 0和null否
        bundle.putInt("isCheckout", Constants.DEFAULTINT);// 是否要离店定位1 是 0和null否
        bundle.putSerializable("module", module);// 自定义模块实例
        bundle.putInt("menuType", menuType);// 菜单类型
        bundle.putInt("is_store_expand", is_store_expand);
        Intent intent = new Intent(ModuleActivity.this, ModuleFuncActivity.class);// 跳转到数据下发页面
        intent.putExtra("bundle", bundle);
        intent.putExtra("isNoWait", isNoWait);
        startActivity(intent);

    }

    /**
     * 审核
     *
     * @param module
     */
    private void verify(Module module) {

        Bundle bundle = new Bundle();
        bundle.putInt("targetId", module.getMenuId());// 数据ID
        bundle.putInt("menuType", menuType);// 菜单类型
        bundle.putSerializable("module", module);// 自定义模块实例
        List<Func> viewCoum = new FuncDB(this).findFuncListReplenish(module.getMenuId(), 1, null, null);
        if (viewCoum == null || viewCoum.isEmpty()) {// 没有查询条件，就直接跳转到查询列表
            ELog.d("Open TableListActivity");
            Intent intent = new Intent(ModuleActivity.this, TableListActivityNew.class);
            bundle.putInt("menuId", menuId);
            bundle.putString("menuName", menuName);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        } else {// 有查询条件就跳转到查询条件页面
            Intent intent = new Intent(ModuleActivity.this, QueryFuncActivity.class);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        }

    }

    /**
     * 修改
     *
     * @param
     */
    private void update(Module module) {

        Bundle bundle = new Bundle();
        bundle.putInt("targetId", module.getMenuId());// 数据ID
        bundle.putInt("menuType", menuType);// 菜单类型
        bundle.putSerializable("module", module);// 自定义模块实例
        List<Func> viewCoum = new FuncDB(this).findFuncListReplenish(module.getMenuId(), 1, null, null);
        if (viewCoum == null || viewCoum.isEmpty()) {// 没有查询条件，就直接跳转到查询列表
            ELog.d("Open TableListActivity");
            Intent intent = new Intent(ModuleActivity.this, TableListActivityNew.class);
            bundle.putInt("menuId", menuId);
            bundle.putString("menuName", menuName);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        } else {// 有查询条件就跳转到查询条件页面
            Intent intent = new Intent(ModuleActivity.this, QueryFuncActivity.class);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        }
    }

    /**
     * 查询
     *
     * @param module
     */
    private void intentQuery(Module module) {
        Bundle replenishBundle = new Bundle();
        replenishBundle.putInt("planId", Constants.DEFAULTINT);// 计划ID
        replenishBundle.putInt("awokeType", Constants.DEFAULTINT);// 提醒类型
        replenishBundle.putInt("wayId", Constants.DEFAULTINT);// 线路ID
        replenishBundle.putInt("storeId", Constants.DEFAULTINT);// 店面ID
        replenishBundle.putInt("targetId", module.getMenuId());// 数据ID
        replenishBundle.putString("storeName", null);// 店面名称
        replenishBundle.putString("wayName", null);// 线路名称
        replenishBundle.putInt("isCheckin", Constants.DEFAULTINT);// 是否要进店定位1 是
        // 0和null否
        replenishBundle.putInt("isCheckout", Constants.DEFAULTINT);// 是否要离店定位1 是
        // 0和null否
        replenishBundle.putSerializable("module", module);// 自定义模块实例
        replenishBundle.putInt("menuType", menuType);// 菜单类型
        replenishBundle.putInt("is_store_expand", is_store_expand);

        // //查找数据库中所有的查询条件
        // List<Func> funcList = new
        // FuncDB(this).findFuncListReplenish(module.getMenuId(), null,
        // Func.IS_Y, null);
        // if (funcList != null && !funcList.isEmpty()) {//如果有查询条件就跳转到查询条件页面
        // Intent replenishIntent = new Intent(ModuleActivity.this,
        // QueryFuncActivity.class);
        // replenishIntent.putExtra("bundle", replenishBundle);
        // startActivity(replenishIntent);
        // }
        // else {//如果没有查询条件就直接跳转到查询列表页面
        // Intent replenishIntent = new Intent(ModuleActivity.this,
        // TableListActivityNew.class);
        // replenishIntent.putExtra("bundle", replenishBundle);
        // startActivity(replenishIntent);
        // }

        ELog.d("Open TableListActivity");
        Intent replenishIntent = new Intent(ModuleActivity.this, TableListActivityNew.class);
        // Intent replenishIntent = new Intent(ModuleActivity.this,
        // QueryTableActivity.class);
        replenishBundle.putInt("menuId", menuId);
        replenishBundle.putString("menuName", menuName);
        replenishIntent.putExtra("bundle", replenishBundle);
        replenishIntent.putExtra("isNoWait", isNoWait);
        startActivity(replenishIntent);
    }

    /**
     * 支付
     *
     * @param module
     */
    private void pay(Module module) {
        Bundle payBundle = new Bundle();
        payBundle.putInt("planId", Constants.DEFAULTINT);// 计划ID
        payBundle.putInt("awokeType", Constants.DEFAULTINT);// 提醒类型
        payBundle.putInt("wayId", Constants.DEFAULTINT);// 线路ID
        payBundle.putInt("storeId", Constants.DEFAULTINT);// 店面ID
        payBundle.putInt("targetId", module.getMenuId());// 数据ID
        payBundle.putString("storeName", null);// 店面名称
        payBundle.putString("wayName", null);// 店面名字
        payBundle.putInt("isCheckin", Constants.DEFAULTINT);// 是否要进店定位1 是
        // 0和null否
        payBundle.putInt("isCheckout", Constants.DEFAULTINT);// 是否要离店定位1 是
        // 0和null否
        payBundle.putSerializable("module", module);// 自定义模块实例
        payBundle.putInt("menuType", menuType);// 菜单类型
        Intent payIntent = new Intent(ModuleActivity.this, OrderListActivity.class);// 跳转到支付列表
        payIntent.putExtra("bundle", payBundle);
        payIntent.putExtra("isNoWait", isNoWait);// 是否无等待
        startActivity(payIntent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (moduleList.size() == 1) {// 返回的时候如果只有一项则关闭该页面
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (titlebar != null) {
            titlebar.unregister();
        }
        super.onDestroy();
    }

    /**
     * 根据模块数量不等于3的GridView适配器
     */
    class gvAdapter extends BaseAdapter {
        private List<Module> mList;

        public gvAdapter(List<Module> list) {
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            viewHolder holder;
            if (view == null) {
                if (i == 1 || i == 2 || i == 5 || i == 6) {
                    view = LayoutInflater.from(ModuleActivity.this).inflate(R.layout.module_item_gvitem_left, null);
                } else {
                    view = LayoutInflater.from(ModuleActivity.this).inflate(R.layout.module_item_gvitem, null);
                }
                holder = new viewHolder();
                holder.iv1 = (ImageView) view.findViewById(R.id.iv_module_item);
                holder.tv1 = (TextView) view.findViewById(R.id.tv_module_item);
                view.setTag(holder);
            } else {
                holder = (viewHolder) view.getTag();
            }
            holder.tv1.setText(mList.get(i).getName());
            setSwitch(mList.get(i).getType(),holder.iv1);//根据条件设置人物头像
            return view;
        }

        class viewHolder {
            TextView tv1;
            ImageView iv1;
        }
    }


}
