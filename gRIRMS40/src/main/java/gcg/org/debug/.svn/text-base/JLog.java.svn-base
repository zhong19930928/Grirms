package gcg.org.debug;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import android.util.Log;

import com.yunhu.yhshxc.utility.PublicUtils;

public class JLog {

	
	public static void d(String content) {
		if (JDebugOptions.SHOW_DEBUGINFO) {
			Log.d(JDebugOptions.TAG, content);
		}
		writeErrorLog(content,JDebugOptions.TAG);
	}

	public static void d(String tag, String content) {
		if (JDebugOptions.SHOW_DEBUGINFO) {
			Log.d(tag, content);
		}
		if(JDebugOptions.TAG_ASYNC_HTTP.equals(tag) || JDebugOptions.TAG_SUBMIT.equals(tag)){
			writeErrorLog(content,tag);
		}
	}

	public static void e(Exception ex) {
		String info = null;
		ByteArrayOutputStream baos = null;
		PrintStream printStream = null;
		try {
			baos = new ByteArrayOutputStream();
			printStream = new PrintStream(baos);
			ex.printStackTrace(printStream);
			byte[] data = baos.toByteArray();
			info = new String(data);
			data = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (printStream != null) {
					printStream.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		writeErrorLog(info,JDebugOptions.TAG);
		if (JDebugOptions.SHOW_DEBUGINFO) {
			Log.e(JDebugOptions.TAG, info);
		}
		
	}
	
	public static void writeErrorLog(final String msg,final String tag){
		new Thread(){
			@Override
			public void run() {
				PublicUtils.writeErrorLog(null,msg,tag);
			}
			
		}.start();
	}
}
