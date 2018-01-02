package com.yunhu.yhshxc.service;

import java.util.List;

import com.yunhu.yhshxc.style.Style;
import com.yunhu.yhshxc.style.StyleUtil;

import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.HttpHelper;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

/**
 * 下载样式的服务类
 *
 */
public class StyleDownLoadService extends IntentService {
	//样式更新列表
	private List<Style> urlList;
	public StyleDownLoadService() {
		super("styleservice");
	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		urlList = StyleUtil.findImageUrlForStyle(this);
		if (urlList != null && !urlList.isEmpty()) {
			for (int i = 0; i < urlList.size(); i++) {
				String currentUrl = urlList.get(i).getImgUrl();
				try{
					//将指定路径下的图片下载到本地style文件下,并以指定名称进行命名
					int j = new HttpHelper(StyleDownLoadService.this).downloadStyleFile(currentUrl,
							Constants.COMPANY_STYLE_PATH, urlList.get(i).getImgName());					
				}catch(Exception e){					
					continue;
				}
			}
			
			
			
		}
		//下载完成之后,发送广播通知菜单页面进行图标更新		
		Intent intentCast = new Intent();
		intentCast.setAction(Constants.BROADCAST_ACTION_STYLE);
		sendBroadcast(intentCast);

	}

}
