package gcg.org.debug;

import android.util.Log;

/**
 * 这个Log可以精确打印Log插入点在哪个类、哪个方法、哪行上，方便追踪Log输出的位置
 */
public class ELog {
	public static boolean isDebug = true;
	
	public static void d(String msg) {
		if (!isDebug)
			return;
		
		StackTraceElement invoker = getInvoker();
		Log.d(invoker.getClassName(), "【" + invoker.getMethodName() + ":" + invoker.getLineNumber() + "】" + msg);
	}
	
	public static void i(String msg) {
		if (!isDebug)
			return;

		StackTraceElement invoker = getInvoker();
		Log.i(invoker.getClassName(), "【" + invoker.getMethodName() + ":" + invoker.getLineNumber() + "】" + msg);
	}
	
	public static void e(String msg) {
		if (!isDebug)
			return;

		StackTraceElement invoker = getInvoker();
		Log.e(invoker.getClassName(), "【" + invoker.getMethodName() + ":" + invoker.getLineNumber() + "】" + msg);
	}
	
	public static void v(String msg) {
		if (!isDebug)
			return;

		StackTraceElement invoker = getInvoker();
		Log.v(invoker.getClassName(), "【" + invoker.getMethodName() + ":" + invoker.getLineNumber() + "】" + msg);
	}
	
	public static void w(String msg) {
		if (!isDebug)
			return;

		StackTraceElement invoker = getInvoker();
		Log.w(invoker.getClassName(), "【" + invoker.getMethodName() + ":" + invoker.getLineNumber() + "】" + msg);
	}
	
	private static StackTraceElement getInvoker() {
		return Thread.currentThread().getStackTrace()[4];
	}
}
