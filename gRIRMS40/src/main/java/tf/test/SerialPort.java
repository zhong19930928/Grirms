package tf.test;

public class SerialPort {
	static{
		System.loadLibrary("tf_serialport");
	}
	public native static int open(String dev);
	public native static int close(final int fd);
	public native static int writeData(final int fd, String str);
	public native static String receiveData(final int fd);
}
