package com.yunhu.yhshxc.MeetingAgenda.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.yhshxc.R;

/**
 * ＠author zhonghuibin
 * create at 2017/10/19.
 * describe
 */
public class MeetingName_GridviewAdapter extends BaseAdapter {
    private Context context=null;
    private String namelist[]=null;
    private String unnamelist[]=null;
    private String imglist[]=null;
    private int imgId[]=null;
    LayoutInflater inflater;


    private class Holder{
        ImageView item_img;
        TextView item_tex;
    }
    //构造方法
    public MeetingName_GridviewAdapter(Context context, String[] namelist, String[] imglist) {
        this.context = context;
        this.namelist = namelist;
        this.imglist = imglist;
    }
    public MeetingName_GridviewAdapter(Context context, String[] namelist,String[] unnamelist, String[] imglist) {
        this.context = context;
        this.namelist = namelist;
        this.unnamelist = unnamelist;
        this.imglist = imglist;
    }


    @Override
    public int getCount() {
        return namelist.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Holder holder;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_meetingname_gridview,null);
            holder=new Holder();
            holder.item_img=(ImageView)view.findViewById(R.id.item_img);
            holder.item_tex=(TextView)view.findViewById(R.id.item_text);
            view.setTag(holder);
        }else{
            holder=(Holder) view.getTag();
        }
        holder.item_tex.setText(namelist[position]);
        //测试黑白效果
//        for (int i=0;i<unnamelist.length;i++){
//            if (namelist[position].equals(unnamelist[i])){
//
//            }
//        }
        if (position ==3){
            Resources res = context.getResources();
            Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.help6);
            holder.item_img.setImageBitmap(convertBlackWhite(bmp));
        }
        return view;
    }
    /**
     *@date2017/11/2
     *@author zhonghuibin
     *@description 将头像转换成没有接受邀请的黑白模式
     */
    private Bitmap convertBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                // 分离三原色
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                // 转化成灰度像素
                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        // 新建图片
        Bitmap newbmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        newbmp.setPixels(pixels, 0, width, 0, 0, width, height);
        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newbmp, width,
                height);
        return resizeBmp;
    }
}
