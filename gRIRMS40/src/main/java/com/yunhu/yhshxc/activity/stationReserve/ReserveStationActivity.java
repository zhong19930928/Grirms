package com.yunhu.yhshxc.activity.stationReserve;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.addressBook.db.AdressBookUserDB;
import com.yunhu.yhshxc.activity.stationReserve.adapter.SureStationListAdapter;
import com.yunhu.yhshxc.activity.stationReserve.view.SeatTable;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.bo.StationBean;
import com.yunhu.yhshxc.bo.StationMapBean;
import com.yunhu.yhshxc.bo.SureStationBean;
import com.yunhu.yhshxc.core.ApiRequestFactory;
import com.yunhu.yhshxc.core.ApiRequestMethods;
import com.yunhu.yhshxc.database.OrgUserDB;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 选择工位页面
 */
public class ReserveStationActivity extends Activity implements View.OnClickListener {
    private SeatTable seatTable;
    private List<StationMapBean> dataList = new ArrayList<>();
    private TextView station_reservetime, layer_tx, floor_tx, confirm_btn;
    private int company_id;//公司id
    private int uid;
    private int bId;
    private  int roleId;
    private String org_code,userName;
    private SureStationBean sureStationBean;
    int flag;
    private TextView stationreserve_title;
    private ImageView layer_icon;
    private  int mXnum,mYnum;//一个楼层的x，y最大值


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_station);
        SharedPreferencesUtil s = SharedPreferencesUtil.getInstance(this);
        company_id = s.getCompanyId();
        uid = s.getUserId();
        userName=s.getUserName();
        bId=s.getOrgId();
        roleId=s.getRoleId();
        OrgUserDB orgUserDB=new OrgUserDB(this);
        org_code=orgUserDB.findUserByUserId(uid).getOrgCode();
        initView();
        initData();
    }

    private void initData() {
        if (sureStationBean != null) {
            beforeTime(sureStationBean.getStartTime(), sureStationBean.getEndTime());
            floor_tx.setText(sureStationBean.getLouName() + "座");
            layer_tx.setText(sureStationBean.getCengName() + "层");
            ApiRequestMethods.getCengXY(this, sureStationBean.getLouId(), sureStationBean.getCengId(), new ApiRequestFactory.HttpCallBackListener() {
                @Override
                public void onSuccess(String response, String url, int id) {
                    try{
                        JSONObject object = new JSONObject(response).getJSONObject("data");
                        if (object == null) {
                            Toast.makeText(ReserveStationActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        if (object.has("flag") && object.getInt("flag") == 0) {
                            JSONObject data = object.getJSONObject("data");
                            if (data != null && !"".equals(data.toString())) {
                                mXnum=data.getInt("x");
                                mYnum=data.getInt("y");
                                gongWeiInfoState(sureStationBean.getStartTime()==null?"":sureStationBean.getStartTime(), sureStationBean.getEndTime()==null?"":sureStationBean.getEndTime(), sureStationBean.getLouId(), sureStationBean.getCengId());
                            }else{
                                Toast.makeText(ReserveStationActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }else if(object.has("flag") && object.getInt("flag") == -5){
                            Toast.makeText(ReserveStationActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(ReserveStationActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }catch (Exception e){
                        Toast.makeText(ReserveStationActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }

                @Override
                public void failure(Call call, Exception e, int id) {
                    Toast.makeText(ReserveStationActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }, true);
            seatTable.setLouName(sureStationBean.getLouName());
            seatTable.setCengName(sureStationBean.getCengName());
        }else{
            Toast.makeText(ReserveStationActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
            finish();
        }
//        seatTable.setMaxSelected(4);
        seatTable.setClickPop(new SeatTable.ClickPop() {
            @Override
            public void clickSeat(int type, StationMapBean stationMapBean) {
                showPopWindow(stationMapBean);
            }
        });
    }

    private void initView() {
        findViewById(R.id.stationreserve_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        seatTable = (SeatTable) findViewById(R.id.my_seatTable);
        station_reservetime = (TextView) findViewById(R.id.station_reservetime);
        stationreserve_title = (TextView) findViewById(R.id.stationreserve_title);
        layer_tx = (TextView) findViewById(R.id.layer_tx);
        floor_tx = (TextView) findViewById(R.id.floor_tx);
        confirm_btn = (TextView) findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(this);
        layer_icon= (ImageView) findViewById(R.id.layer_icon);
        if (getIntent() != null) {
            sureStationBean = (SureStationBean) getIntent().getSerializableExtra("SureStationBean");
            if(getIntent().hasExtra("flag")){
                flag=getIntent().getIntExtra("flag",-1);
            }
        }

        if(flag==2||flag==3){
            if(flag==3){
                station_reservetime.setVisibility(View.GONE);
                layer_icon.setVisibility(View.VISIBLE);

            }
            confirm_btn.setVisibility(View.GONE);
            stationreserve_title.setText("工位详情");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_btn:
                Toast.makeText(ReserveStationActivity.this, "暂未开放", Toast.LENGTH_SHORT).show();
//                if(seatTable.getCheckedList()!=null&&seatTable.getCheckedList().size()>0){
//                    surePopWindow();
//                }else{
//                    Toast.makeText(ReserveStationActivity.this, "您还没预约工位", Toast.LENGTH_SHORT).show();
//                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(seatTable!=null){
            seatTable.clearBitmap();
        }
    }


    /**
     * 根据时间段和楼号楼层查询该楼层所有座位信息及状态
     *
     * @param starttime
     * @param endtime
     * @param louId
     * @param cengId
     */
    private void gongWeiInfoState(String starttime, String endtime, String louId, String cengId) {
        ApiRequestMethods.gongWeiInfoState(this, louId, cengId, starttime, endtime, company_id, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(final String response, String url, int id) {
                try {
                    JSONObject object = new JSONObject(response).getJSONObject("data");
                    if (object == null) {
                        Toast.makeText(ReserveStationActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    if (object.has("flag") && object.getInt("flag") == 0) {
                        JSONArray data = object.getJSONArray("data");
                        if (data != null && data.length() > 0) {
//                            int x_num = 0, y_num = 0;
                            Map<String, Integer> map = new HashMap<String, Integer>();
                            ArrayList<String> list=new ArrayList<String>();
                            dataList.clear();
                            StationMapBean stationMapBean;
                            JSONObject jsonObject;
                            for (int i = 0; i < data.length(); i++) {
                                stationMapBean = new StationMapBean();
                                jsonObject = data.getJSONObject(i);
                                if (jsonObject.has("id")) {
                                    stationMapBean.setId(jsonObject.getInt("id"));
                                }

                                if (jsonObject.has("gwsn")) {
                                    stationMapBean.setGwsn(jsonObject.getString("gwsn"));
                                }

                                if (jsonObject.has("company_id")) {
                                    stationMapBean.setCompany_id(jsonObject.getInt("company_id"));
                                }
                                if (jsonObject.has("ygid")) {
                                    stationMapBean.setYgid(jsonObject.getString("ygid"));
                                }

                                if (jsonObject.has("ygname")) {
                                    stationMapBean.setYgname(jsonObject.getString("ygname"));
                                }
                                if (jsonObject.has("login_id")) {
                                    stationMapBean.setLogin_id(jsonObject.getString("login_id"));
                                }
                                if (jsonObject.has("bmname")) {
                                    stationMapBean.setBmname(jsonObject.getString("bmname"));
                                }

                                if (jsonObject.has("louid")) {
                                    stationMapBean.setLouid(jsonObject.getInt("louid"));
                                }

                                if (jsonObject.has("cengid")) {
                                    stationMapBean.setCengid(jsonObject.getInt("cengid"));
                                }

                                if (jsonObject.has("bmid")) {
                                    stationMapBean.setBmid(jsonObject.getString("bmid"));
                                }

                                if (jsonObject.has("glyid")) {
                                    stationMapBean.setGlyid(jsonObject.getString("glyid"));
                                }
                                if (jsonObject.has("gwstye")) {
                                    stationMapBean.setGwstye(jsonObject.getInt("gwstye"));
                                }

                                if (jsonObject.has("fx") && !"null".equals(jsonObject.getString("fx"))) {
                                    stationMapBean.setFx(jsonObject.getInt("fx"));
                                }

                                if (jsonObject.has("fpstate")&& !"null".equals(jsonObject.getString("fpstate"))) {
                                    stationMapBean.setFpstate(jsonObject.getInt("fpstate"));
                                }
                                if (jsonObject.has("systate") && !"null".equals(jsonObject.getString("systate"))) {
                                    stationMapBean.setSystate(jsonObject.getInt("systate"));
                                }
                                if (jsonObject.has("gwdwd") && !"null".equals(jsonObject.getString("gwdwd"))) {
                                    if(flag!=2){
                                        stationMapBean.setGwdwd(jsonObject.getInt("gwdwd"));
                                    }
                                }
                                if(flag==2&&(stationMapBean.getId()+"").equals(sureStationBean.getGswn())){
                                    stationMapBean.setGwdwd(1);
                                }

                                if (jsonObject.has("gwxh") && !"null".equals(jsonObject.getString("gwxh"))) {
                                    stationMapBean.setGwxh(jsonObject.getInt("gwxh"));
                                }

                                if (jsonObject.has("zsx")) {
                                    stationMapBean.setxNum(jsonObject.getInt("zsx"));
                                }
//                                if (stationMapBean.getxNum() >= x_num) {
//                                    x_num = stationMapBean.getxNum();
//                                }

                                if (jsonObject.has("zsy")) {
                                    stationMapBean.setYnum(jsonObject.getInt("zsy"));
                                }

//                                if (stationMapBean.getYnum() >= y_num) {
//                                    y_num = stationMapBean.getYnum();
//                                }

                                dataList.add(stationMapBean);
                                if (stationMapBean.getGwstye() == 3 || stationMapBean.getGwstye() == 4) {
                                    if(!list.contains(stationMapBean.getGwsn())){
                                        list.add(stationMapBean.getGwsn());
                                    }

                                    if (map.containsKey(stationMapBean.getGwsn() + "minx")) {
                                        if (stationMapBean.getxNum() <= map.get(stationMapBean.getGwsn() + "minx")) {
                                            map.put(stationMapBean.getGwsn() + "minx", stationMapBean.getxNum());
                                        }

                                    } else {
                                        map.put(stationMapBean.getGwsn() + "minx", stationMapBean.getxNum());
                                    }
                                    if (map.containsKey(stationMapBean.getGwsn() + "maxx")) {
                                        if (stationMapBean.getxNum() >= map.get(stationMapBean.getGwsn() + "maxx")) {
                                            map.put(stationMapBean.getGwsn() + "maxx", stationMapBean.getxNum());
                                        }

                                    } else {
                                        map.put(stationMapBean.getGwsn() + "maxx", stationMapBean.getxNum());
                                    }

                                    if (map.containsKey(stationMapBean.getGwsn() + "miny")) {
                                        if (stationMapBean.getYnum() <= map.get(stationMapBean.getGwsn() + "miny")) {
                                            map.put(stationMapBean.getGwsn() + "miny", stationMapBean.getYnum());
                                        }

                                    } else {
                                        map.put(stationMapBean.getGwsn() + "miny", stationMapBean.getYnum());
                                    }
                                    if (map.containsKey(stationMapBean.getGwsn() + "maxy")) {
                                        if (stationMapBean.getYnum() >= map.get(stationMapBean.getGwsn() + "maxy")) {
                                            map.put(stationMapBean.getGwsn() + "maxy", stationMapBean.getYnum());
                                        }

                                    } else {
                                        map.put(stationMapBean.getGwsn() + "maxy", stationMapBean.getYnum());
                                    }
                                }

                            }
                            seatTable.setMap(map);
                            seatTable.setByList(list);
                            seatTable.setXYNum(mXnum, mYnum);
                            seatTable.setFlag(flag);
                            seatTable.setData(dataList);
                            Log.i("gongWeiInfoState", "gongWeiInfoState:" + dataList.size());


                        } else {
                            Toast.makeText(ReserveStationActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(ReserveStationActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    Toast.makeText(ReserveStationActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.i("gongWeiInfoState", "gongWeiInfoState:" + e.toString());
                    finish();
                }

            }

            @Override
            public void failure(Call call, Exception e, int id) {
                Toast.makeText(ReserveStationActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, true);

    }




    private void submitDate(String gwsn, String starttime, String endtime, String ydpeo, String ygid, String bmid, String louid, String cengid, String ydpeoname, String yghaoname
            , String data_org, String data_org_new, String data_role, String data_auth_user, int company_id) {
        ApiRequestMethods.reserveGongwei(this,  gwsn,  starttime,  endtime,  ydpeo,  ygid,  bmid,  louid,  cengid,  ydpeoname,  yghaoname
                ,  data_org,  data_org_new,  data_role,  data_auth_user,  company_id, new ApiRequestFactory.HttpCallBackListener() {
            @Override
            public void onSuccess(final String response, String url, int id) {
                try {
                    JSONObject object = new JSONObject(response).getJSONObject("data");
                    if (object == null) {
                        Toast.makeText(ReserveStationActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (object.has("flag") && object.getInt("flag") == 0) {
                        Toast.makeText(ReserveStationActivity.this, "预约成功", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK,new Intent());
                        if(SoftApplication.getInstance().getStationHandler()!=null){
                            SoftApplication.getInstance().getStationHandler().sendEmptyMessage(StationReserveMainActivity.UPDATEPAGE);
                        }
                        finish();

                    }else {
                        Toast.makeText(ReserveStationActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(Call call, Exception e, int id) {
                Toast.makeText(ReserveStationActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
            }
        }, true);
    }


    private void beforeTime(String stime, String etime) {
        if(stime!=null&&!"".equals(stime)&&etime!=null&&!"".equals(etime)){
            StringBuffer buffer = new StringBuffer();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

            try {
                buffer.append(sdf.format(sdf1.parse(stime))).append("-").append(sdf.format(sdf1.parse(etime)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            station_reservetime.setText(buffer.toString());
        }

    }

    private  PopupWindow infoWindow = null;//工位信息pop
    private  PopupWindow sureSeatWindow = null;//确认预约工位pop

    private void showPopWindow(StationMapBean stationBean) {
        if (infoWindow != null && infoWindow.isShowing()){
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout myView = (LinearLayout) inflater.inflate(R.layout.seatinfopop_layout, null);
        TextView lou_tx= (TextView) myView.findViewById(R.id.lou_tx);
        TextView ceng_tx= (TextView) myView.findViewById(R.id.ceng_tx);
        TextView gwnum_tx= (TextView) myView.findViewById(R.id.gwnum_tx);
        TextView ygnum_tx= (TextView) myView.findViewById(R.id.ygnum_tx);
        TextView ygname_tx= (TextView) myView.findViewById(R.id.ygname_tx);
        TextView bu_tx= (TextView) myView.findViewById(R.id.bu_tx);
        if(stationBean.getGwsn()!=null){
            String[] split = stationBean.getGwsn().split("-");
            if(split!=null&&split.length==3){
                lou_tx.setText(split[0]);
                ceng_tx.setText(split[1]);
                gwnum_tx.setText(split[2]);
            }else{
                lou_tx.setText(sureStationBean.getLouName());
                ceng_tx.setText(sureStationBean.getCengName());
            }
        }else {
            lou_tx.setText(sureStationBean.getLouName());
            ceng_tx.setText(sureStationBean.getCengName());
        }

        ygnum_tx.setText(stationBean.getLogin_id()==null||"null".equals(stationBean.getLogin_id())?"":stationBean.getLogin_id());
        ygname_tx.setText(stationBean.getYgname()==null||"null".equals(stationBean.getYgname())?"":stationBean.getYgname());
        if(stationBean.getBmname()!=null&&!"".equals(stationBean.getBmname())&&!"null".equals(stationBean.getBmname())){
            String[] split = stationBean.getBmname().split("-");
            if(split!=null&&split.length>0){
                bu_tx.setText(split[split.length-1]);
            }
        }

//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        int width = wm.getDefaultDisplay().getWidth();
        infoWindow = new PopupWindow(myView, PublicUtils.convertDIP2PX(this,180),
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        infoWindow.setAnimationStyle(R.style.AnimationPopup);
        infoWindow.setOutsideTouchable(true);
        infoWindow.setBackgroundDrawable(new BitmapDrawable());
        infoWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        if(!infoWindow.isShowing()){
            infoWindow.showAtLocation(stationreserve_title, Gravity.CENTER, 0, 0);
            backgroundAlpha(0.5f);
        }
    }


    private void surePopWindow() {
        if (sureSeatWindow != null && sureSeatWindow.isShowing()){
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout myView = (LinearLayout) inflater.inflate(R.layout.surecancle_layout, null);

        TextView textView= (TextView) myView.findViewById(R.id.message_tx);
        textView.setText("您确定预约吗？");

        myView.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sureSeatWindow.dismiss();
                StringBuffer buffer=new StringBuffer();
                for(int i=0;i<seatTable.getCheckedList().size();i++){
                    buffer.append(seatTable.getCheckedList().get(i)).append(",");
                }
                submitDate(buffer.toString(),sureStationBean.getStartTime(),sureStationBean.getEndTime(),uid+"",uid+"",bId+""
                        ,sureStationBean.getLouId(),sureStationBean.getCengId(),userName,userName,org_code,bId+"",roleId+"",uid+"",company_id);

            }
        });
        myView.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sureSeatWindow.dismiss();
            }
        });
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        sureSeatWindow = new PopupWindow(myView, width*2 /3,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        sureSeatWindow.setAnimationStyle(R.style.AnimationPopup);
        sureSeatWindow.setOutsideTouchable(true);
        sureSeatWindow.setBackgroundDrawable(new BitmapDrawable());
        sureSeatWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        if(!sureSeatWindow.isShowing()){
            sureSeatWindow.showAtLocation(stationreserve_title, Gravity.CENTER, 0, 0);
            backgroundAlpha(0.5f);
        }
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
