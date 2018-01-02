package com.yunhu.yhshxc.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.MenuUsableControl;
import com.yunhu.yhshxc.activity.todo.Todo;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.bo.Module;
import com.yunhu.yhshxc.database.DoubleDB;
import com.yunhu.yhshxc.database.ModuleDB;
import com.yunhu.yhshxc.database.NoticeDB;
import com.yunhu.yhshxc.database.StyleDB;
import com.yunhu.yhshxc.database.TablePendingDB;
import com.yunhu.yhshxc.database.TaskDB;
import com.yunhu.yhshxc.database.TodoDB;
import com.yunhu.yhshxc.style.Style;
import com.yunhu.yhshxc.style.StyleUtil;
import com.yunhu.yhshxc.submitManager.bo.TablePending;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.wechat.db.NotificationDB;
import com.yunhu.yhshxc.wechat.db.ReplyDB;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * HomeMenuActivity中显示所有菜单项的GridView所用的Adapter，负责根据 菜单项数据动态生成各个可视的菜单项组件
 *
 * @author wangchao
 * @version 2013.5.21
 */
public class HomeMenuAdapter extends BaseAdapter {


    private Context mContext = null;
    private HashMap<Integer, String> fixedName;
    /**
     * 所有菜单项数据的引用，这个List是从HomeMenuActivity传进来的
     */
    private List<Menu> srcList = null;

    public HomeMenuAdapter(Context mContext) {
        this.mContext = mContext;
        fixedName = SharedPreferencesUtil.getInstance(mContext).getFixedName();
    }

    /**
     * 获取菜单项总数，即菜单项List的总数
     *
     * @return 返回菜单项总数
     */
    @Override
    public int getCount() {
        return srcList.size();
    }

    /**
     * 获取指定位置的菜单项数据
     *
     * @param position 菜单项在List中的位置
     * @return 返回指定位置的菜单项数据
     */
    @Override
    public Object getItem(int position) {
        return srcList.get(position);
    }

    /**
     * 获取指定位置菜单项的id，通常情况下这个ID就是指定位置本身
     *
     * @param position 在菜单项List中的位置
     * @return 直接返回position值
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 系统接口，系统会自己调用
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Menu menu = srcList.get(position);// 获取数据项
        ViewHolder viewHolder = null;

        if (convertView == null) {
            // 创建可视的菜单组件
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.home_menu_item, null);
            viewHolder.ll_menu = (LinearLayout) convertView.findViewById(R.id.ll_menu);
            viewHolder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            viewHolder.ll_number = (LinearLayout) convertView.findViewById(R.id.ll_number);
            viewHolder.empty_layout = (LinearLayout) convertView.findViewById(R.id.empty_layout);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_cnName = (TextView) convertView.findViewById(R.id.tv_cnName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (menu.getIsEmptyModel() == 1) {
            viewHolder.empty_layout.setVisibility(View.VISIBLE);
            viewHolder.ll_number.setVisibility(View.GONE);
//			convertView.setBackgroundResource(R.drawable.menu_item_empty_bg);
        } else {
            viewHolder.empty_layout.setVisibility(View.GONE);
            viewHolder.ll_number.setVisibility(View.VISIBLE);
//			convertView.setBackgroundResource(R.drawable.menu_item_bg_selector);
//			if(position==srcList.size()-1&&position==7){
//				viewHolder.iv_icon.setBackgroundResource(R.drawable.home_more);
//				viewHolder.iv_icon.setImageBitmap(null);
//				viewHolder.tv_cnName.setText("更多");
//				viewHolder.ll_number.setVisibility(View.GONE);
//			}else{
            setContentView(menu, viewHolder);
//				 if(position==0){
//					viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_meeting);
//				}else if(position==1){
//					viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_visit);
//				}else if(position==2){
//					viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_delivery);
//				}else if(position==3){
//					viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_work);
//				}else if(position==4){
//					viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_asset);
//				}else if(position==5){
//					viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_fix);
//				}else if(position==6){
//					viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_food);
//				}else if(position==7){
//					 viewHolder.iv_icon.setBackgroundResource(R.drawable.meun_sport);
//				 }
        }
        return convertView;
    }

    /**
     * 根据菜单项数据更新菜单View所显示的内容
     *
     * @param menu       菜单项数据
     * @param viewHolder 对菜单View中各个子组件引用的封装
     */
    private void setContentView(Menu menu, ViewHolder viewHolder) {
        viewHolder.tv_cnName.setText(menu.getName());
        if ("耗材仓库管理".equals(menu.getName())) {
            viewHolder.tv_cnName.setText("耗材入库确认");
        }

        viewHolder.ll_number.setVisibility(View.GONE);
        viewHolder.tv_number.setText("");
        viewHolder.iv_icon.setImageBitmap(null);
        viewHolder.iv_icon.setBackgroundResource(0);
        int number = 0;
        // 根据style设置UI
        Style style = new StyleDB(mContext).findStyle(StyleUtil.styleType(menu.getType()), menu.getMenuId());
        int styleColor = 0;
        Bitmap styleBitmap = null;
        if (style != null) {
            String imageIcon = style.getImgName();
            String bgColor = style.getBgColor();
            if (!TextUtils.isEmpty(imageIcon)) {
                File file = new File(Constants.COMPANY_STYLE_PATH + imageIcon);
                if (file.exists()) {
                    styleBitmap = BitmapFactory.decodeFile(Constants.COMPANY_STYLE_PATH + imageIcon);
                }
            }
            if (!TextUtils.isEmpty(bgColor)) {
                styleColor = android.graphics.Color.parseColor(bgColor);
            }
        }
        // viewHolder.phoneUsableControl(menu,1);//styleColor

        switch (menu.getType()) {
            case Menu.TYPE_MODULE:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    if (menu.getName() != null) {
                        String menuName = menu.getName();
                        if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name))) {// 9
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_swap_horiz_selector);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name1))) {// 8
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_view_headline_selector);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name2))) {// 7
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_markunread_mailbox_selector);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name3))) {// 10
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_headset_selector);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name4))) {// 11
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_tmp3);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name5))) {// 12
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_trending_up_selector);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name6))) {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_description_selector);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name7))) {// 5 11457
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_drive_eta_selector);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name8))) {// 13
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_my_library_books_selector);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name9))) {// 17
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_tmp2);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name10))) {// 16
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_markunread_mailbox_selector);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name11))) {// 15
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_tmp1);
                        }
                        //

                        else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name12))) {// 6 11470
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_headset_selector);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name13))) {// 7 11473
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_markunread_mailbox_selector);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name14))) {// 8 11474
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_view_headline_selector);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name15))) {// 9 11475
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_swap_horiz_selector);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name16))) {// 10 11476
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_description_selector);
                        } else if (menuName.equals(PublicUtils.getResourceString(mContext, R.string.menu_name17))) {// 10 11476
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_description_selector);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_view_headline_selector);
                        }
                    }
                }
                break;
            case Menu.TYPE_CAR_SALES:

                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_custom));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_drive_eta_selector);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                break;
            case Menu.IS_STORE_ADD_MOD:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_custom));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_plus_one_black_36dp);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                break;
            case Menu.TYPE_NEARBY:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_custom));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_directions_walk_black_36dp);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                List<Module> moduleList = new ModuleDB(mContext).findModuleByMenuId(menu.getMenuId());
                if (moduleList.size() == 1 && !TextUtils.isEmpty(moduleList.get(0).getName())) {
                    viewHolder.tv_cnName.setText(moduleList.get(0).getName());
                }
                number = doubleNumber(menu);
                if (number > 0) {
                    // 显示未读执行的条数
                    viewHolder.ll_number.setVisibility(View.VISIBLE);
                    viewHolder.tv_number.setText(getNumber(number));
                }
                break;
            case Menu.TYPE_NEW_TARGET:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_custom));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_description_black_36dp);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                List<Module> newModuleList = new ModuleDB(mContext).findModuleByMenuId(menu.getMenuId());
                if (newModuleList.size() == 1 && !TextUtils.isEmpty(newModuleList.get(0).getName())) {
                    viewHolder.tv_cnName.setText(newModuleList.get(0).getName());
                }
                number = doubleNumberNew(menu);
                if (number > 0) {
                    // 显示未读执行的条数
                    viewHolder.ll_number.setVisibility(View.VISIBLE);
                    viewHolder.tv_number.setText(getNumber(number));
                }
                break;
            case Menu.TYPE_NOTICE:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_notice));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_notifications_paused_black_36dp);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                viewHolder.tv_cnName.setText(fixedName.containsKey(4) ? fixedName.get(4) : menu.getName());
                number = new NoticeDB(mContext).findAllUnreadNoticeNumber();
                if (number > 0) {
                    // 显示未读执行的条数
                    viewHolder.ll_number.setVisibility(View.VISIBLE);
                    viewHolder.tv_number.setText(getNumber(number));
                }
                break;
            case Menu.WEI_CHAT:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_notice));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_group_black_36dp);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                number = wechatNumber();
                if (number > 0) {
                    // 显示未读执行的条数
                    viewHolder.ll_number.setVisibility(View.VISIBLE);
                    viewHolder.tv_number.setText(getNumber(number));
                }
                break;
            case Menu.TYPE_TARGET:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_notice));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_description_black_36dp);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                // 从数据库中查询总共有多少条未读任务数,然后显示出来.
                number = new TaskDB(mContext).findAllUnreadTaskNumber(menu.getMenuId());
                if (number > 0) {
                    // 显示未读执行的条数
                    viewHolder.ll_number.setVisibility(View.VISIBLE);
                    viewHolder.tv_number.setText(getNumber(number));
                }
                break;
            case Menu.TYPE_VISIT:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_visit));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_directions_walk_nearly_black_36dp);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                viewHolder.tv_cnName.setText(fixedName.containsKey(1) ? fixedName.get(1) : menu.getName());
                break;
            case Menu.TYPE_ATTENDANCE:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_kaoqin));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_kaoqin);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                viewHolder.tv_cnName.setText(fixedName.containsKey(2) ? fixedName.get(2) : menu.getName());
                break;
            case Menu.TYPE_NEW_ATTENDANCE:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_kaoqin));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_kaoqin);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                viewHolder.tv_cnName.setText(fixedName.containsKey(3) ? fixedName.get(3) : menu.getName());
                break;
            case Menu.TYPE_BBS:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_bbs));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_people_outline_black_36dp);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                viewHolder.tv_cnName.setText(fixedName.containsKey(6) ? fixedName.get(6) : menu.getName());
                break;
            case Menu.TYPE_HELP:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_help));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_tmp2);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                viewHolder.tv_cnName.setText(fixedName.containsKey(5) ? fixedName.get(5) : menu.getName());
                break;
            case Menu.TYPE_REPORT_NEW:
                // break;
            case Menu.TYPE_REPORT:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_report));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_trending_up_black_36dp);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                break;
            case Menu.TYPE_MANAGER:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_custom));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_markunread_mailbox_black_36dp);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                number = unSubmitNumbers();
                if (number > 0) {
                    // 显示未读执行的条数
                    viewHolder.ll_number.setVisibility(View.VISIBLE);
                    viewHolder.tv_number.setText(String.valueOf(number));
                }
                break;
            case Menu.TYPE_TODO:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_custom));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_my_library_books_grey600_36dp);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                number = toDoNumbers();
                viewHolder.tv_cnName.setText(fixedName.containsKey(7) ? fixedName.get(7) : menu.getName());
                if (number > 0) {
                    // 显示未读执行的条数
                    viewHolder.ll_number.setVisibility(View.VISIBLE);
                    viewHolder.tv_number.setText(String.valueOf(number));
                }
                break;

            case Menu.TYPE_ORDER2:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_custom));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.icon_order);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                break;
            case Menu.MAIN_LIST:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_custom));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_my_library_books_black_36dp);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                break;
            case Menu.TYPE_ORDER3:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_custom));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.icon_order);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                break;
            case Menu.TYPE_ORDER3_SEND:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    // viewHolder.iv_icon.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    // R.drawable.menu_custom));
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_play_download_selector);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                break;
            case Menu.TYPE_WEB_REPORT:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_view_headline_black_36dp);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                break;
            case Menu.QUESTIONNAIRE:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_description_selector);
                    viewHolder.iv_icon.setImageBitmap(null);
                }
                break;
            case Menu.COMPANY_TYPE1:

                viewHolder.iv_icon.setImageResource(R.drawable.email_icon);
                break;
            case Menu.WORK_PLAN:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.workplan_home_icon);
                    viewHolder.iv_icon.setImageBitmap(null);
                }

                break;
            case Menu.WORK_SUM:
                if (styleBitmap != null) {
                    viewHolder.iv_icon.setImageBitmap(styleBitmap);
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                } else {
                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_my_library_books_black_36dp);
                    viewHolder.iv_icon.setImageBitmap(null);
                }

                break;

            default:
                viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_my_library_books_black_36dp);
                viewHolder.iv_icon.setImageBitmap(null);

        }
        String menuName = menu.getName();
        if (menuName.contains("访客")) {
            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_visit);
        } else if (menuName.contains("会议")) {
            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_meeting);
        } else if (menuName.contains("工位")) {
            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_work);
        } else if (menuName.contains("信件") || menuName.contains("快递")) {
            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_delivery);
        } else if (menuName.contains("维修")) {
            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_fix);
        } else if (menuName.contains("健身")) {
            viewHolder.iv_icon.setBackgroundResource(R.drawable.meun_sport);
        } else if (menuName.contains("餐厅")) {
            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_food);
        } else if (menuName.contains("资产")) {
            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_asset);
        } else if (menuName.contains("康体")) {
            viewHolder.iv_icon.setBackgroundResource(R.drawable.meun_sport);
        } else {
            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_food);
        }
    }

    /**
     * 用于格式化显示未执行的数目。 所谓格式化就是正常情况下直接显示数字，而当数字大于99时，显示“...”，当数字小于1时，显示空字符串
     *
     * @param number 需要格式化显示的数字
     * @return 返回格式化后的字符串
     */
    private String getNumber(int number) {
        if (number > 99) {
            return "...";
        } else if (number < 1) {
            return "";
        } else {
            return String.valueOf(number);
        }
    }

    /**
     * 企业微信未读信息条数
     *
     * @return
     */
    private int wechatNumber() {
        int number = 0;
        int replyCount = new ReplyDB(mContext).findAllRepayNum(0);
        int noticCount = new NotificationDB(mContext).findAllNoticeNum();
        number = replyCount + noticCount;
        return number;
    }

    /**
     * 查询双向模块中未执行的条数
     *
     * @param menu 双向模块的Menu类型数据
     * @return 如果Menu的类型是双向模块，则返回该模块未执行的数目，否则(即Menu.getType() !=
     * Menu.TYPE_MODULE)则返回-1
     */
    private int doubleNumber(Menu menu) {
        int number = 0;
        if (menu.getType() == Menu.TYPE_MODULE || menu.getType() == Menu.IS_STORE_ADD_MOD) {
            Module doubleModule = new ModuleDB(mContext).findModuleByTargetId(menu.getMenuId(),
                    Constants.MODULE_TYPE_EXECUTE);
            if (doubleModule != null) {
                number = new DoubleDB(mContext).findAllDoubleTaskNumber(doubleModule.getMenuId());
            } else {
                number = -1;
            }
        }
        return number;
    }

    /**
     * 查询新双向模块中未执行的条数
     *
     * @param menu 双向模块的Menu类型数据
     * @return 如果Menu的类型是双向模块，则返回该模块未执行的数目，否则(即Menu.getType() !=
     * Menu.TYPE_MODULE)则返回-1
     */
    private int doubleNumberNew(Menu menu) {
        int number = 0;
        if (menu.getType() == Menu.TYPE_NEW_TARGET) {
            Module doubleModule = new ModuleDB(mContext).findModuleByTargetId(menu.getMenuId(),
                    Constants.MODULE_TYPE_EXECUTE_NEW);
            if (doubleModule != null) {
                number = new DoubleDB(mContext).findAllDoubleTaskNumber(doubleModule.getMenuId());
            } else {
                number = -1;
            }
        }
        return number;
    }

    /**
     * 未提交的数据条数
     */
    private int unSubmitNumbers() {
        int number = 0;
        List<TablePending> list = new TablePendingDB(mContext).findAllTablePending();
        number = list.size();
        return number;
    }

    /**
     * 待办事项条数
     */
    private int toDoNumbers() {
        int number = 0;
        List<Todo> list = new TodoDB(mContext).findAllTodo();
        for (int i = 0; i < list.size(); i++) {
            Todo todo = list.get(i);
            number += todo.getTodoNum();
        }
        return number;
    }

    /**
     * 向Adapter传入所需的数据
     *
     * @param list 需要显示的所有菜单项数据
     */
    public void setDataSrc(List<Menu> list) {
        this.srcList = list;
    }

    /**
     * 对菜单View中各个子组件引用的封装类
     */
    private class ViewHolder {
        public LinearLayout ll_menu = null;
        public ImageView iv_icon = null;
        public TextView tv_cnName = null;
        public TextView tv_number = null;
        public LinearLayout ll_number = null;
        public LinearLayout empty_layout = null;

        public void phoneUsableControl(Menu menu, int styleColor) {
            String time = menu.getPhoneUsableTime();
            // time = "12:00|0.5,14:55|0.5";
            MenuUsableControl control = new MenuUsableControl();
            boolean isCanUse = control.isCanUse(time);
            if (menu.getType() == Menu.TYPE_ORDER3) {
                isCanUse = isCanUse && control.isOrder3CanUse(mContext);
            }
            if (isCanUse) {
                if (menu.getType() == Menu.IS_STORE_ADD_MOD || menu.getType() == Menu.TYPE_NEW_TARGET
                        || menu.getType() == Menu.TYPE_MODULE || menu.getType() == Menu.TYPE_REPORT_NEW
                        || menu.getType() == Menu.TYPE_REPORT || menu.getType() == Menu.TYPE_NEARBY
                        || menu.getType() == Menu.TYPE_ORDER3 || menu.getType() == Menu.TYPE_ORDER3_SEND) {
                    if (styleColor != 0) {
                        ll_menu.setBackgroundColor(styleColor);
                    } else {
                        ll_menu.setBackgroundResource(R.drawable.home_menu_custom_onclick);
                    }
                } else {
                    if (styleColor != 0) {
                        ll_menu.setBackgroundColor(styleColor);
                    } else {
                        ll_menu.setBackgroundResource(R.drawable.home_menu_onclick);
                    }
                }
            } else {
                ll_menu.setBackgroundResource(R.drawable.home_menu_unclick);
            }
        }

    }
}
