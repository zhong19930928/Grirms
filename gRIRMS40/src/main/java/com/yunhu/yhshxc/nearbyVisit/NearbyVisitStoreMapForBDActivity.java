package com.yunhu.yhshxc.nearbyVisit;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.utility.PositionUtil;
import com.yunhu.yhshxc.utility.PositionUtil.Gps;

import gcg.org.debug.JLog;

public class NearbyVisitStoreMapForBDActivity extends AbsBaseActivity{
	
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Marker mMarker;
	private Marker storeMarker;
	private InfoWindow mInfoWindow;
	private double lat,lon;
	private double storeLat,storeLon;
	private String adress;
	private String storeName;
	private BitmapDescriptor bd;
	private BitmapDescriptor mBd;
	public Gps gps;
	public Gps storeGps;
	private TextView tv_nearby_visit_map_title;
	private LinearLayout ll_nearby_visit_map_search;
	private ImageView iv_nearby_visit_navigation;//导航按钮
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(this.getApplication());  
		setContentView(R.layout.nearby_visit_store_map_bd_activity);
		initIntentData();
		initWidget();
		setUserOverlay();
		setStoreOverlay();
		setMapStatusUpdate();
//		initPosition();
	}
	
	private void initIntentData(){
		lat = getIntent().getDoubleExtra("lat", 0);
		lon = getIntent().getDoubleExtra("lon", 0);
		adress = getIntent().getStringExtra("adress");
		gps = gcj02_To_Bd09(lat, lon);
		storeName = getIntent().getStringExtra("storeName");
		storeLat = getIntent().getDoubleExtra("storeLat", 0);
		storeLon = getIntent().getDoubleExtra("storeLon", 0);
		storeGps = store_gcj02_To_Bd09(storeLat, storeLon);
	}
	
	public Gps store_gcj02_To_Bd09(double lat,double lon){
		if (lat!=0 && lon!=0) {
			return new PositionUtil().gcj02_To_Bd09(lat, lon);
		}else{
			return null;
		}
	}
	public Gps gcj02_To_Bd09(double lat,double lon){
		return new PositionUtil().gcj02_To_Bd09(lat, lon);
	}
	
	private void initWidget(){
		iv_nearby_visit_navigation=(ImageView)findViewById(R.id.iv_nearby_visit_navigation);
		iv_nearby_visit_navigation.setOnClickListener(listener);
		tv_nearby_visit_map_title = (TextView)findViewById(R.id.tv_nearby_visit_map_title);
		tv_nearby_visit_map_title.setText(storeName);
		ll_nearby_visit_map_search = (LinearLayout)findViewById(R.id.ll_nearby_visit_map_search);
		ll_nearby_visit_map_search.setOnClickListener(listener);
		bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
		mBd = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setOnMarkerClickListener(markerClickListener);
		mBaiduMap.setOnMarkerDragListener(markerDragListener);

	}
	
	/**
	 * 设置用户显示点
	 */
	private void setUserOverlay(){
		LatLng llA = new LatLng(gps.getWgLat(), gps.getWgLon());
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(mBd);
		mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
	}
	
	/**
	 * 设置店面显示点
	 */
	public void setStoreOverlay(){
		if (storeGps!=null) {
			LatLng storeLlA = new LatLng(storeGps.getWgLat(), storeGps.getWgLon());
			OverlayOptions ooA = new MarkerOptions().position(storeLlA).icon(bd);
			storeMarker = (Marker)(mBaiduMap.addOverlay(ooA));
			LatLng ll = storeMarker.getPosition();
			Button button = new Button(getApplicationContext());
			button.setBackgroundResource(R.drawable.popup);
			button.setText(storeName);
			button.setTextColor(getResources().getColor(R.color.black));
			mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, null);
			mBaiduMap.showInfoWindow(mInfoWindow);
		}
	}
	
	private void setMapStatusUpdate(){
		LatLng llA = new LatLng(gps.getWgLat(), gps.getWgLon());
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(llA, 15.0f);
		mBaiduMap.setMapStatus(u);
	}
	
	
	private OnMarkerDragListener markerDragListener = new OnMarkerDragListener() {
		
		@Override
		public void onMarkerDragStart(Marker arg0) {
			
		}
		
		@Override
		public void onMarkerDragEnd(Marker marker) {
			Toast.makeText(NearbyVisitStoreMapForBDActivity.this,
					setString(R.string.nearby_visit_23) + marker.getPosition().latitude + ", "+ marker.getPosition().longitude,Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onMarkerDrag(Marker arg0) {
			
		}
	};
	
	
	private OnMarkerClickListener markerClickListener = new OnMarkerClickListener() {
		
		@Override
		public boolean onMarkerClick(Marker marker) {
			
//			Button button = new Button(getApplicationContext());
//			button.setBackgroundResource(R.drawable.popup);
//			OnInfoWindowClickListener listener = null;
//			if (marker == mMarker) {
//				button.setText(adress);
//				listener = new OnInfoWindowClickListener() {
//					public void onInfoWindowClick() {
////						LatLng ll = marker.getPosition();
////						LatLng llNew = new LatLng(ll.latitude + 0.005,ll.longitude + 0.005);
////						marker.setPosition(llNew);
////						mBaiduMap.hideInfoWindow();
//					}
//				};
//				LatLng ll = marker.getPosition();
//				mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
//				mBaiduMap.showInfoWindow(mInfoWindow);
//			} 
			if (marker == mMarker) {
				Toast.makeText(NearbyVisitStoreMapForBDActivity.this, adress, Toast.LENGTH_LONG).show();
			}else if(marker == storeMarker){
				Toast.makeText(NearbyVisitStoreMapForBDActivity.this, storeName, Toast.LENGTH_LONG).show();
			}
			return true;
		}
	};
	
	private OnInfoWindowClickListener infoWindowClickListener = new OnInfoWindowClickListener(){

		@Override
		public void onInfoWindowClick() {
			
		}
		
	};
	
	
	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
		// 回收 bitmap 资源
		bd.recycle();
		
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_nearby_visit_map_search:
				poi();
				break;
			case R.id.iv_nearby_visit_navigation://导航按钮
				//获取当前位置后开启导航
				startNavigationBaidu();
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 获取店面位置,进行百度或者高德地图导航
	 */

	private  void startNavigationBaidu() {	
		//测试用
//		storeLat=39.94285557445087;
//		storeLon=116.43304772949215;
	    if (storeLat==0&&storeLon==0) {
			JLog.writeErrorLog("该店面未配置经纬度:"+storeName, "NearbyVisitStoreDetailActivity");
		}
	    // 构建 导航参数
	   
       String[] styleItems = {
			   setString(R.string.nearby_visit_bus),
			   setString(R.string.nearby_visit_driving),
			   setString(R.string.nearby_visit_walking)};
	       AlertDialog selectStyle = new AlertDialog.Builder(NearbyVisitStoreMapForBDActivity.this,R.style.NewAlertDialogStyle)
	    		   .setTitle(setString(R.string.nearby_visit_21))
	    		   .setItems(styleItems, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0://公交
							try {    	
								StringBuffer roadIntent=new StringBuffer();
								roadIntent.append("intent://map/direction?origin=latlng:").append(lat).append(",").append(lon).append("|name:我的位置&destination=").append(storeLat).append(",").append(storeLon).append("&mode=").append("transit").append("&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");							
								Intent intent = Intent.getIntent(roadIntent.toString());
								startActivity(intent);
						      

						    } catch (Exception e) {
						      e.printStackTrace();
						      //如果没有安装百度地图,再去启动高德地图
						      startNavigationGaode();
						     
						    }
							break;
						case 1://开车
							 	
								try {    	
									StringBuffer roadIntent=new StringBuffer();
									roadIntent.append("intent://map/direction?origin=latlng:").append(lat).append(",").append(lon).append("|name:我的位置&destination=").append(storeLat).append(",").append(storeLon).append("&mode=").append("driving").append("&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");							
									Intent intent = Intent.getIntent(roadIntent.toString());
									startActivity(intent);

						    } catch (Exception e) {
						      e.printStackTrace();
						      //如果没有安装百度地图,再去启动高德地图
						      startNavigationGaode();
						     
						    }
							break;
						case 2://步行
							   	
								try {    	
									StringBuffer roadIntent=new StringBuffer();
									roadIntent.append("intent://map/direction?origin=latlng:").append(lat).append(",").append(lon).append("|name:我的位置&destination=").append(storeLat).append(",").append(storeLon).append("&mode=").append("walking").append("&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");							
									Intent intent = Intent.getIntent(roadIntent.toString());
									startActivity(intent);
						    } catch (Exception e) {
						      e.printStackTrace();
						      //如果没有安装百度地图,再去启动高德地图
						      startNavigationGaode();
						     
						    }
							break;

						default://默认开车
							try {    	
						      
								StringBuffer roadIntent=new StringBuffer();
								roadIntent.append("intent://map/direction?origin=latlng:").append(lat).append(",").append(lon).append("|name:我的位置&destination=").append(storeLat).append(",").append(storeLon).append("&mode=").append("driving").append("&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");							
								Intent intent = Intent.getIntent(roadIntent.toString());
								startActivity(intent);

						    } catch (Exception e) {
						      e.printStackTrace();
						      //如果没有安装百度地图,再去启动高德地图
						      startNavigationGaode();
						     
						    }
							break;
						}
						
					}
				}).create();
	       selectStyle.show();
	    		
				  
	
		
	}
	 /**
     * 开启高德地图导航
     */
	private void startNavigationGaode() {
//              //测试用数
//		storeLat=39.94285557445087;
//		storeLon=116.43304772949215;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("androidamap://navi?sourceApplication=").append("GRIRMS4.0").append("&poiname="+storeName).append("&lat="+storeLat).append("&lon="+storeLon+"&dev=0&style=2");
			Intent intentGD = new Intent("android.intent.action.VIEW",android.net.Uri.parse(buffer.toString()));  
			intentGD.setPackage("com.autonavi.minimap");  
			intentGD.addCategory("android.intent.category.DEFAULT");
			startActivity(intentGD);
		} catch (Exception e) {
			e.printStackTrace();
			//如果也没有安装高德地图就提示用户
			 AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.NewAlertDialogStyle);
		      builder.setMessage(setString(R.string.nearby_visit_22));
		      builder.setTitle(setString(R.string.nearby_visit_prompt));
		      builder.setPositiveButton(setString(R.string.nearby_visit_confirm), new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
//		          dialog.dismiss();
		          //如果没有安装百度地图,也没有安装高德地图,提示用户进行下载百度地图,打开浏览器进入下载页面
		        	  Intent intent = new Intent();        
		              intent.setAction("android.intent.action.VIEW");    
		              Uri content_url = Uri.parse("http://map.baidu.com/zt/client/index/?fr=pinzhuan&qd=1012337a");   
		              intent.setData(content_url);  
		              NearbyVisitStoreMapForBDActivity.this.startActivity(intent);

		        
		           
		        }
		      });

		      builder.setNegativeButton(setString(R.string.nearby_visit_cancel), new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		          dialog.dismiss();
		        }
		      });

		      builder.create().show();
		}
		
		
	}
	private void poi(){
		
	}
	
	
	private void initPosition(){
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(LocationMode.NORMAL, true, bd));
		LatLng llA = new LatLng(gps.getWgLat(), gps.getWgLon());
		
		MyLocationData locData = new MyLocationData.Builder()
		.accuracy(95)
		// 此处设置开发者获取到的方向信息，顺时针0-360
		.direction(100).latitude(gps.getWgLat())
		.longitude(gps.getWgLon()).build();
		mBaiduMap.setMyLocationData(locData);

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(llA);
		mBaiduMap.animateMapStatus(u);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putDouble("lat", lat);
		outState.putDouble("lon", lon);
		outState.putDouble("storeLat", storeLat);
		outState.putDouble("storeLon", storeLon);
		outState.putString("adress", adress);
		outState.putString("storeName", storeName);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		lat = savedInstanceState.getDouble("lat");
		lon = savedInstanceState.getDouble("lon");
		storeLat = savedInstanceState.getDouble("storeLat");
		storeLon = savedInstanceState.getDouble("storeLon");
		adress = savedInstanceState.getString("adress");
		storeName = savedInstanceState.getString("storeName");
	}
	private String setString(int stringId){
		return getResources().getString(stringId);
	}
	
}
