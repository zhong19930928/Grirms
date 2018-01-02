package com.yunhu.yhshxc.weather;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.text.format.Time;

public class WeatherService extends Service {

	private static final int DAYTIME_BEGIN_HOUR = 8;
	private static final int DAYTIME_END_HOUR = 20;

	private static final String FORECAST_DATE = "forecast_date";
	private static final String CONDITION = "condition";
	private static final String TEMP_C = "temp_c";
	private static final String HUMIDITY = "humidity";
	private static final String ICON = "icon";
	private static final String WIND_CONDITION = "wind_condition";

	private static final String FORECAST_INFORMATION = "forecast_information";
	private static final String CURRENT_CONDITIONS = "current_conditions";
	private static final String FORECAST_CONDITIONS = "forecast_conditions";
	private static final String PROBLEM_CAUSE = "problem_cause";

	private static final String DAY_OF_WEEK = "day_of_week";
	private static final String LOW = "low";
	private static final String HIGH = "high";

	private static final int OK = 200;
	private static final int FAIL = 404;

	private String strData;
	private String strUrl;
	private WeatherEntity weather;

	private AlarmManager am;
	private PendingIntent pi;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OK:
//				Toast.makeText(WeatherService.this, strData, 1).show();
				// tvShow.setText(strData);
				// ivShow.setImageResource(getForecastImage(weather.icon,
				// isDaytime()));
				stopSelf();
				break;
			case FAIL:
//				Toast.makeText(WeatherService.this, "暂时没有获取到天气信息", 1).show();
				break;
			}
			
		};
	};

	@Override
	public void onCreate() {
		super.onCreate();
		
		this.am = ((AlarmManager) this.getSystemService(Context.ALARM_SERVICE));
		startAlarm(30*1000);
		
//		int lat = (int) (Float.valueOf(SharedPreferencesUtil.getInstance(WeatherService.this).getLat())* 1000000);
//		int lon = (int)(Float.valueOf(SharedPreferencesUtil.getInstance(WeatherService.this).getLon())  * 1000000);//经度
//		strUrl = "http://www.google.com/ig/api?hl=zh-cn&weather=,,," + lat + ","+ lon;

		runOnThread();

	}

	private void runOnThread() {
		new Thread() {

			public void run() {
				InputStream is = getResponse(strUrl);
				if (is == null) {
					mHandler.sendEmptyMessage(FAIL);
					return;
				}
				weather = parseWeather(is);
				if (weather == null || TextUtils.isEmpty(weather.condition)) {
					mHandler.sendEmptyMessage(FAIL);
					return;
				}
				strData = WeathertoString(weather);
				if (TextUtils.isEmpty(strData)) {
					mHandler.sendEmptyMessage(FAIL);
					return;
				}
				mHandler.sendEmptyMessage(OK);
			}
		}.start();
	}

	/**
	 * 判断是不是白天
	 * 
	 * @return
	 */
	public static boolean isDaytime() {
		Time time = new Time();
		time.setToNow();
		return (time.hour >= DAYTIME_BEGIN_HOUR && time.hour <= DAYTIME_END_HOUR);
	}

	protected String WeathertoString(WeatherEntity weather) {
		String str = "==-->>  " + weather.forecastDate + "\n==-->>  "
				+ weather.condition + "\n==-->>  " + weather.humidity
				+ "\n==-->>  " + weather.windCondition + "\n==-->>  "
				+ weather.tempC;
//		String str = "==-->>  " + weather.forecastDate + "\n==-->>  "
//				+ weather.condition + "\n==-->>  " + weather.humidity
//				+ "\n==-->>  " + weather.windCondition + "\n==-->>  "
//				+ weather.tempC + parseForecaseEntity(weather.details);
		return str;
	}

	private String parseForecaseEntity(ArrayList<ForecastEntity> details) {
		String str = "";
		for (ForecastEntity forecast : details) {

			str = str + "\n---------------------------------\n==-->>  "
					+ forecast.dayOfWeek + "\n==-->>  " + forecast.condition
					+ "\n==-->>  " + forecast.hight + "\n==-->>  "
					+ forecast.low;

		}

		return str;
	}

	/**
	 * 解析天气信息
	 * 
	 * @param response
	 * @return
	 */
	private WeatherEntity parseWeather(InputStream response) {
		WeatherEntity weatherEntity = new WeatherEntity();
		try {

			// int i = -1;
			// byte[] b = new byte[1024];
			// StringBuffer sb = new StringBuffer();
			// while ((i = response.read(b)) != -1) {
			// sb.append(new String(b, 0, i));
			// }
			// String content = sb.toString();
			// Log.i("google weather entry=====------>", content);

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xpp = factory.newPullParser();

			String tagName = null;

			xpp.setInput(response, "GB2312");
			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					tagName = xpp.getName();

					if (PROBLEM_CAUSE.equals(tagName)) {
					} else if (FORECAST_INFORMATION.equals(tagName)) {
						dealWithInfomation(tagName, weatherEntity, xpp);
					} else if (CURRENT_CONDITIONS.equals(tagName)) {
						dealWithCurrentConditions(tagName, weatherEntity, xpp);
					} else if (FORECAST_CONDITIONS.equals(tagName)) {
						dealWithForecastConditions(tagName, weatherEntity, xpp);
					}
				}

				eventType = xpp.next();
			}

		} catch (Exception e) {

		}
		return weatherEntity;
	}

	/**
	 * 解析4天的天气
	 * 
	 * @param name
	 * @param weatherEntity
	 * @param xpp
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private static void dealWithForecastConditions(String name,
			WeatherEntity weatherEntity, XmlPullParser xpp) throws IOException,
			XmlPullParserException {
		ForecastEntity forecast = new ForecastEntity();
		xpp.next();

		int eventType = xpp.getEventType();
		String tagName = xpp.getName();
		while ((!name.equals(tagName) || eventType != XmlPullParser.END_TAG)
				&& eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (tagName.equals(CONDITION)) {
					forecast.condition = xpp.getAttributeValue(null, "data");
				} else if (tagName.equals(DAY_OF_WEEK)) {
					forecast.dayOfWeek = xpp.getAttributeValue(null, "data");
				} else if (tagName.equals(HIGH)) {
					forecast.hight = Integer.parseInt(xpp.getAttributeValue(
							null, "data"));
				} else if (tagName.equals(LOW)) {
					forecast.low = Integer.parseInt(xpp.getAttributeValue(null,
							"data"));
				} else if (tagName.equals(ICON)) {
					forecast.icon = xpp.getAttributeValue(null, "data");
				}
			}
			xpp.next();
			eventType = xpp.getEventType();
			tagName = xpp.getName();
		}
		weatherEntity.details.add(forecast);
	}

	/**
	 * 解析当前天气
	 * 
	 * @param name
	 * @param weatherEntity
	 * @param xpp
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private static void dealWithCurrentConditions(String name,
			WeatherEntity weatherEntity, XmlPullParser xpp) throws IOException,
			XmlPullParserException {
		xpp.next();

		int eventType = xpp.getEventType();
		String tagName = xpp.getName();
		while ((!name.equals(tagName) || eventType != XmlPullParser.END_TAG)
				&& eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (tagName.equals(CONDITION)) {
					weatherEntity.condition = xpp.getAttributeValue(null,
							"data");
				} else if (tagName.equals(TEMP_C)) {
					weatherEntity.tempC = Integer.parseInt(xpp
							.getAttributeValue(null, "data"));
				} else if (tagName.equals(HUMIDITY)) {
					weatherEntity.humidity = xpp
							.getAttributeValue(null, "data");
				} else if (tagName.equals(ICON)) {
					weatherEntity.icon = xpp.getAttributeValue(null, "data");
				} else if (tagName.equals(WIND_CONDITION)) {
					weatherEntity.windCondition = xpp.getAttributeValue(null,
							"data");
				}
			}
			xpp.next();

			eventType = xpp.getEventType();
			tagName = xpp.getName();
		}
	}

	/**
	 * 解析日期
	 * 
	 * @param name
	 * @param weatherEntity
	 * @param xpp
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private static void dealWithInfomation(String name,
			WeatherEntity weatherEntity, XmlPullParser xpp) throws IOException,
			XmlPullParserException {
		xpp.next();
		int eventType = xpp.getEventType();
		String tagName = xpp.getName();
		while ((!name.equals(tagName) || eventType != XmlPullParser.END_TAG)
				&& eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (tagName.equals(FORECAST_DATE)) {
					weatherEntity.forecastDate = xpp.getAttributeValue(null,
							"data");
				}
			}
			xpp.next();
			eventType = xpp.getEventType();
			tagName = xpp.getName();
		}
	}

	// /**
	// * 日期--String2Date
	// * @param str
	// * @return
	// */
	// public static Date convertStr2Date(String str) {
	// Date d = null;
	// try {
	// SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
	// d = f.parse(str);
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// return d;
	// }

	/**
	 * 联网查询天气
	 * 
	 * @param queryURL
	 * @return
	 */
	protected InputStream getResponse(String queryURL) {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(queryURL);
		try {
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				return entity.getContent();
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	
	public void startAlarm(int millis) {
		Intent intent = new Intent(this, this.getClass());
		this.pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		this.am.setRepeating(0, Calendar.getInstance().getTimeInMillis()+millis, millis, this.pi);
	}

	public void cancelAlarm() {
		Intent intent = new Intent(this, this.getClass());
		this.pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		this.am.cancel(this.pi);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
