package com.yunhu.yhshxc.inspection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.bo.Menu;
import com.yunhu.yhshxc.database.StyleDB;
import com.yunhu.yhshxc.style.Style;
import com.yunhu.yhshxc.style.StyleUtil;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.PublicUtils;

import java.io.File;
import java.util.List;

/**
 *@author suhu
 *@time 2017/6/30 15:40
 *
*/

public class GridViewAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private List<Menu> list;
    private  ViewHolder viewHolder;
    private Context mContext;

    public GridViewAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<Menu> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return list!=null?list.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_gridview_inspection,null);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.item_image);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_text);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(list.get(position).getName());


        Menu menu = list.get(position);

        if (list.get(position).getIcon()!=null){
            Glide.with(mContext).load(list.get(position).getIcon()).into(viewHolder.iv_icon);
        }else {
            int MenuId = Integer.parseInt(list.get(position).getMenuIdList().get(0));
            try {
                Style style = new StyleDB(mContext).findStyle(StyleUtil.styleType(menu.getType()), MenuId);
                Bitmap styleBitmap = null;
                if (style != null) {
                    String imageIcon = style.getImgName();
                    if (!TextUtils.isEmpty(imageIcon)) {
                        File file = new File(Constants.COMPANY_STYLE_PATH + imageIcon);
                        if (file.exists()) {
                            styleBitmap = BitmapFactory.decodeFile(Constants.COMPANY_STYLE_PATH + imageIcon);
                        }
                    }
                }

                switch (list.get(position).getType()) {
                    case Menu.TYPE_MODULE:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            if (menu.getName() != null) {
                                String menuName = menu.getName();
                                if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name))) {// 9
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_swap_horiz_selector);
                                } else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name1))) {// 8
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_view_headline_selector);
                                } else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name2))) {// 7
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_markunread_mailbox_selector);
                                } else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name3))) {// 10
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_headset_selector);
                                } else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name4))) {// 11
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_tmp3);
                                } else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name5))) {// 12
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_trending_up_selector);
                                } else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name6))) {
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_description_selector);
                                } else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name7))) {// 5 11457
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_drive_eta_selector);
                                } else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name8))) {// 13
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_my_library_books_selector);
                                }

                                else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name9))) {// 17
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_tmp2);
                                }

                                else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name10))) {// 16
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_markunread_mailbox_selector);
                                } else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name11))) {// 15
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_tmp1);
                                }
                                //

                                else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name12))) {// 6 11470
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_headset_selector);
                                } else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name13))) {// 7 11473
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_markunread_mailbox_selector);
                                } else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name14))) {// 8 11474
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_view_headline_selector);
                                } else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name15))) {// 9 11475
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_swap_horiz_selector);
                                } else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name16))) {// 10 11476
                                    viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_description_selector);
                                } else if (menuName.equals(PublicUtils.getResourceString(mContext,R.string.menu_name17))) {// 10 11476
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
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_drive_eta_selector);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.IS_STORE_ADD_MOD:
                        if (styleBitmap!=null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        }else{
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_plus_one_black_36dp);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.TYPE_NEARBY:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_directions_walk_black_36dp);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }

                        break;
                    case Menu.TYPE_NEW_TARGET:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_description_black_36dp);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.TYPE_NOTICE:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_notifications_paused_black_36dp);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.WEI_CHAT:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_group_black_36dp);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.TYPE_TARGET:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_description_black_36dp);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.TYPE_VISIT:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_directions_walk_nearly_black_36dp);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.TYPE_ATTENDANCE:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_kaoqin);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.TYPE_NEW_ATTENDANCE:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_kaoqin);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.TYPE_BBS:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_people_outline_black_36dp);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.TYPE_HELP:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_tmp2);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.TYPE_REPORT_NEW:
                        // break;
                    case Menu.TYPE_REPORT:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_trending_up_black_36dp);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.TYPE_MANAGER:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_markunread_mailbox_black_36dp);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.TYPE_TODO:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_my_library_books_grey600_36dp);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;

                    case Menu.TYPE_ORDER2:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.icon_order);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.MAIN_LIST:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.ic_my_library_books_black_36dp);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.TYPE_ORDER3:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.icon_order);
                            viewHolder.iv_icon.setImageBitmap(null);
                        }
                        break;
                    case Menu.TYPE_ORDER3_SEND:
                        if (styleBitmap != null) {
                            viewHolder.iv_icon.setImageBitmap(styleBitmap);
                            viewHolder.iv_icon.setBackgroundResource(R.drawable.menu_default_bg);
                        } else {
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

            }catch (Exception e){
                e.printStackTrace();
            }
        }






        return convertView;
    }


    private class ViewHolder{
        ImageView iv_icon;
        TextView textView;
    }
}
