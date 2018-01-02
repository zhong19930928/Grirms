package com.yunhu.yhshxc.func;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.widget.ToastOrder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import view.PaintView;

/**
 * Created by Brook on 17/10/2017.
 */

public class PaintActivity extends Activity {
    private PaintView mView;
    private final int REQUEST_CODE_SIGNATURE = 100;
    Button btnOk;
    private Button paint_view_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_paint_view);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.tablet_view);

        mView = new PaintView(this);
        frameLayout.addView(mView);
        mView.requestFocus();

        TextView btnClear = (TextView) findViewById(R.id.tablet_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mView.clear();
            }
        });

        btnOk = (Button) findViewById(R.id.tablet_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mView.isDraw()){
                    btnOk.setEnabled(false);
                    Bitmap imageBitmap = mView.getCachebBitmap();

                    Intent in =  new Intent();
                    in.putExtra("path",saveBitMap(getRoundCornerImage(imageBitmap,1)));
                    setResult(Activity.RESULT_OK,in);

                    finish();
                }else{
                    ToastOrder.makeText(PaintActivity.this, "您还没有签名", ToastOrder.LENGTH_LONG).show();
                }

            }
        });
        paint_view_back = (Button) findViewById(R.id.paint_view_back);
        paint_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 画成圆角图片
     */
    public Bitmap getRoundCornerImage(Bitmap bitmap, int roundPixels)
    {
        /**创建一个和原始图片一样大小位图*/
        Bitmap roundConcerImage = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        /**创建带有位图roundConcerImage的画布*/
        Canvas canvas = new Canvas(roundConcerImage);
        /**创建画笔  */
        Paint paint = new Paint();
        /**创建一个和原始图片一样大小的矩形*/
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        /**去锯齿*/
        paint.setAntiAlias(true);
        /**画一个和原始图片一样大小的圆角矩形*/
        canvas.drawRoundRect(rectF, roundPixels, roundPixels, paint);
        /**设置相交模式  */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**把图片画到矩形去  */
        canvas.drawBitmap(bitmap, rect, rectF, paint);

////////////////////////////////////////////////////////////////////////////////////
        /**引时圆角区域为透明，给其填充白色  */
        paint.setColor(Color.WHITE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        canvas.drawRect(rectF, paint);
///////////////////////////////////////////////////////////////////////////////////

        return roundConcerImage;
    }
    String  saveBitMap(Bitmap bm){

//        String tempPath = Constants.SDCARD_PATH + "/temp/";
        String tempPath = Constants.PATH_TEMP;
        File file = new File(tempPath);
        if(!file.exists()){
            file.mkdirs();
        }
        Calendar c = Calendar.getInstance();
        String picName = c.getTime().getTime()+".jpg";
        File f = new File(tempPath,picName);


            if (f.exists()) {
                f.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(f);
                bm.compress(Bitmap.CompressFormat.JPEG, 1, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return picName;


    }
}
