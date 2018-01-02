package com.yunhu.yhshxc.utility;

/**
 * 
* @ClassName:MapDistance
* @Package:  com.grandison.grirms.util 
* @Copyright: Copyright (c) 2015 GCG All rights reserved.
* @Description: 距离
* 
* @Created: 2012-4-17 grady.gu
* @History:
*
 */
public class MapDistance {


    private static double EARTH_RADIUS = 6378.137; //地球半径：6378.137km； 

 
    private static double rad(double d) { 

        return d * Math.PI / 180.0; 

    } 

 
    /**
     * 验证： http://blog.chinaunix.net/link.php?url=http://www.storyday.com%2Fwp-content%2Fuploads%2F2008%2F09%2Flatlung_dis.html
     * @param lat1  A点纬度
     * @param lng1  A点经度
     * @param lat2	B点纬度
     * @param lng2	B点经度
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) { 

        double radLat1 = rad(lat1); 

        double radLat2 = rad(lat2); 

        double a = radLat1 - radLat2; 	//两点纬度之差

        double b = rad(lng1) - rad(lng2); 	//两点经度之差

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) 

                + Math.cos(radLat1) * Math.cos(radLat2) 

                * Math.pow(Math.sin(b / 2), 2))); 

        s = s * EARTH_RADIUS * 1000; //以m为单位

        //System.out.println(s);
        
        s = Math.round(s * 100000.0) / 100000.0;//精确到小数点后5位数（“1”和“.”之间有多少个0，就精确到小数点后多少位）

        // System.out.println(" MapDistance =======>" + s);
        
        return s; 

    } 

    
//    public static void main(String[] args){  
//    	
//    	double lat1 = 59.14431;
//    	double lng1 = 116.42221;
//
//    	double lat2 = 58.13321;
//    	double lng2 = 114.55122;
//
//    	double ssss = getDistance(lat1,lng1,lat2,lng2);
//    	
//    	System.out.println(ssss);
//    	
//    }
    
}   

