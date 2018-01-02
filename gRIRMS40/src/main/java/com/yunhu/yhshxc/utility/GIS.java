package com.yunhu.yhshxc.utility;

import android.util.Pair;

public class GIS {

	private static boolean IsChina(double lon, double lat) {
		boolean flg = true;
		if (lon > 137.8347 || lon < 72.004 || lat > 55.8271 || lat < 18.2) {
			flg = false;
		}
		return flg;
	}

	public static Pair<Double, Double> BD2MGS(double lon, double lat) {
		if (!IsChina(lon, lat)) {
			return new Pair<Double, Double>(lon, lat);
		}
		double x_pi = Math.PI * 3000.0 / 180.0;
		double x = lon - 0.0065;
		double y = lat - 0.006;
		double z = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) - 0.00002
				* Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double reslon = z * Math.cos(theta);
		double reslat = z * Math.sin(theta);
		return new Pair<Double, Double>(reslon, reslat);
	}

}