#define LOG_TAG "serialport"

#define LOG_NDEBUG 0
#define LOG_NDDEBUG 0

#define LOG_NIDEBUG 0

#include "utils/Log.h"
#include "tf_test_SerialPort.h"
#include "serialport.h"

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <signal.h>
#include <sys/ioctl.h>
#include <termios.h> 
#include <sys/types.h>
#include <sys/stat.h>
#include <assert.h>
#include <pthread.h>


static int echo_flag = 1;
static int gpio_fd = -1;

static char scan_wakeup = 0x00;
static char scan_dis[6] = {0x04,0xEA,0x04,0x00,0xFF,0x0E};
static char scan_en[6] = {0x04,0xE9,0x04,0x00,0xFF,0x0F};


#define GPIO_SCAN_POWER_IC_PIN 186
#define GPIO_SCAN_TRIG_PIN     184
#define SCAN_OPEN     			1
#define SCAN_CLOSE    			0

void sig_handler_exit (int status)
{
	ALOGD("Stopping \n");
	echo_flag = 0;
}

static int write_test(const int fd, char *buf, const int count) { 
	int wnum;
        int i;
	struct termios c_opt;

	if (fd < 0) {
		ALOGD("Could not open uart port \n");
		return(-1);
	}
	if (tcflush(fd, TCIOFLUSH) <0) {
		ALOGD("Could not flush uart port");
		return(-1);
	}

	if (buf == NULL || count == 0) {
		ALOGD("buf is null \n");
		return(-1);
	}
	/* Blocking behavior */
	fcntl(fd, F_SETFL, 0);

	ioctl(fd, TCGETS, &c_opt);

	c_opt.c_cc[VTIME]    = 0;   /* inter-character timer unused */
	c_opt.c_cc[VMIN]     = 0;   /* blocking read until 5 chars received */

	c_opt.c_cflag &= ~(CSIZE | CRTSCTS);
        c_opt.c_cflag |= (CS8 | CLOCAL | CREAD);
        c_opt.c_cflag &= ~PARENB;   /*no parity add by liuqingling*/
        c_opt.c_cflag &= ~CSTOPB;   /*1 stop bit add by liuqingling*/
        c_opt.c_iflag &= ~INPCK;    /*no parity add by liuqingling*/
	c_opt.c_iflag = IGNPAR;
        c_opt.c_oflag = 0;
	c_opt.c_lflag = 0;	

	cfsetospeed(&c_opt, B9600); //origin 115200
	cfsetispeed(&c_opt, B9600);

	ioctl(fd, TCSETS, &c_opt);

	wnum = write(fd, buf, count);

	ALOGD("write success, count=%d\n", wnum);

	return(wnum);
}

#define TRANSFER_COUNT 4096

int read_test(const int fd, char *buf, const int count) { 
	int rnum;
	int tnum;
	int i;
	int timeout;
	int err;
	struct termios options;

	if (fd < 0) {
		ALOGD("Invalid serial port fd=%d\n", fd);
		return(-1);
	}

	if (buf == NULL || count == 0) {
		ALOGD("buf is null and don't need to read.\n");
		return(-1);
	}

	/* Blocking behavior */
	fcntl(fd, F_SETFL, 0);
	ioctl(fd, TCGETS, &options);
	options.c_cc[VTIME]    = 0;   /* inter-character timer unused */
	options.c_cc[VMIN]     = 0;   /* blocking read until 1 chars received */

	options.c_cflag &= ~CSIZE;
	options.c_cflag |= (CS8 | CLOCAL | CREAD | CRTSCTS);
        options.c_cflag &= ~PARENB;   /*no parity add by liuqingling*/
        options.c_cflag &= ~CSTOPB;   /*1 stop bit add by liuqingling*/
        options.c_iflag &= ~INPCK;    /*no parity add by liuqingling*/
	options.c_iflag = IGNPAR;
        options.c_oflag = 0;
	options.c_lflag = 0;

	cfsetospeed(&options, B9600);
	cfsetispeed(&options, B9600);
	ioctl(fd, TCSETS, &options);


	ALOGD("Loopback connection required! \n");


	rnum = 0;
	tnum = 0;

	tnum = read(fd, &buf[rnum], count);
        ALOGD("read Success! count=%d, buf=%s\n", tnum, buf);
        if (tcflush(fd, TCIOFLUSH) <0) {
		ALOGD("Could not flush uart port");
		return(-1);
	}	


//niuweifeng add st for india

	int my_tnum;
	long int my_ii;
	my_tnum=tnum;
	for(my_ii=0; my_tnum<=0&&my_ii<=16000000; my_ii++){

            if(my_ii%50==0){
		    my_tnum = read(fd, &buf[rnum], count);
            	}

	}
	tnum=my_tnum;

//


		
	return(tnum);
}

int scan_power(bool en){
    if(en){ //en 1: power on,GPIO is high
        int ret = ioctl(gpio_fd, GPIO_IOCTMODE0, GPIO_SCAN_POWER_IC_PIN); //set to gpio mode
        if (ret < 0) {
            ALOGD("Could not set gpio mode\n");
            return -1;
        }
        ret = ioctl(gpio_fd, GPIO_IOCSDIROUT, GPIO_SCAN_POWER_IC_PIN); //set direction (output)
        if (ret < 0) {
            ALOGD("Could not set direction output\n");
            return -1;
        }
        ret = ioctl(gpio_fd, GPIO_IOCSDATAHIGH, GPIO_SCAN_POWER_IC_PIN); //set output high
        if (ret < 0) {
            ALOGD("Could not set output high\n");
            return -1;
        }
        ALOGD("scan_power true: ret=%d\n", ret);
    }else{ //en 0: power off, GPIO is low
        int ret = ioctl(gpio_fd, GPIO_IOCTMODE0, GPIO_SCAN_POWER_IC_PIN); //set to gpio mode
        if (ret < 0) {
            ALOGD("Could not set gpio mode\n");
            return -1;
        }
        ret = ioctl(gpio_fd, GPIO_IOCSDIROUT, GPIO_SCAN_POWER_IC_PIN); //set direction (output)
        if (ret < 0) {
            ALOGD("Could not set direction output\n");
            return -1;
        }
        ret = ioctl(gpio_fd, GPIO_IOCSDATALOW, GPIO_SCAN_POWER_IC_PIN); //set output low
        if (ret < 0) {
            ALOGD("Could not set output low\n");
            return -1;
        }
        ALOGD("scan_power false: ret=%d\n", ret);
    }
    return 0;
}

int scan_trig(bool en){
    if(en){ //en 1: trig on,GPIO is low
        int ret = ioctl(gpio_fd, GPIO_IOCTMODE0, GPIO_SCAN_TRIG_PIN); //set to gpio mode
        if (ret < 0) {
            ALOGD("scan_trig Could not set gpio mode\n");
            return -1;
        }
        ret = ioctl(gpio_fd, GPIO_IOCSDIROUT, GPIO_SCAN_TRIG_PIN); //set direction (output)
        if (ret < 0) {
            ALOGD("scan_trig Could not set direction output\n");
            return -1;
        }
        ret = ioctl(gpio_fd, GPIO_IOCSDATALOW, GPIO_SCAN_TRIG_PIN); // set ouput low
        if (ret < 0) {
            ALOGD("scan_trig Could not set output high\n");
            return -1;
        }
        ALOGD("scan_trig true: ret=%d\n", ret);
    }else{ //en 0: trig off, GPIO is high
        int ret = ioctl(gpio_fd, GPIO_IOCTMODE0, GPIO_SCAN_TRIG_PIN); //set to gpio mode
        if (ret < 0) {
            ALOGD("scan_trig Could not set gpio mode\n");
            return -1;
        }
        ret = ioctl(gpio_fd, GPIO_IOCSDIROUT, GPIO_SCAN_TRIG_PIN); //set direction (output)
        if (ret < 0) {
            ALOGD("scan_trig Could not set direction output\n");
            return -1;
        }
        ret = ioctl(gpio_fd, GPIO_IOCSDATAHIGH, GPIO_SCAN_TRIG_PIN); // set ouput high
        if (ret < 0) {
            ALOGD("scan_trig Could not set output low\n");
            return -1;
        }
        ALOGD("scan_trig false: ret=%d\n", ret);
    }
    return 0;   
}

int mtk_scan_starttrig(){
    int i=30000;
    int ret = scan_trig(true);
    //while(i--){};
    usleep(300000);   //modified by zhouyuanwei  500000 -> 2500000
    return ret;
}

int mtk_scan_endtrig(){
    int i=30000;
    int ret = scan_trig(false);
    //while(i--){};
    usleep(300000);  //modifide by zhouyuanwei 500000 -> 1500000 
    return ret;
}




//niuweifeng add start ,  for key mode  0511
int scan_starttrig_v2(){ 
	
	int ret = ioctl(gpio_fd, GPIO_IOCTMODE0, GPIO_SCAN_TRIG_PIN); //gpio mode
	if (ret < 0) {
		ALOGD("scan_trig Could not set gpio mode\n");
	return -1;
	}

        ret = ioctl(gpio_fd, GPIO_IOCSPULLDISABLE, GPIO_SCAN_TRIG_PIN); // Pull_disable
        if (ret < 0) {
            ALOGD("scan_trig Could not pull_disable output\n");
            return -1;
        }

	ret = ioctl(gpio_fd, GPIO_IOCSDIROUT, GPIO_SCAN_TRIG_PIN); //set direction (output)
	if (ret < 0) {
		ALOGD("scan_trig Could not set direction output\n");
	return -1;
	}

	ret = ioctl(gpio_fd, GPIO_IOCSDATALOW, GPIO_SCAN_TRIG_PIN); // set ouput low
	if (ret < 0) {
		ALOGD("scan_trig Could not set output high\n");
	return -1;
	}
		ALOGD("@@@@niu@@@@ scan_starttrig_v2 OK : ret=%d\n", ret);

    return 0;   

}

int mtk_scan_starttrig_v2(){
    int i=30000;
    int ret = scan_starttrig_v2();
    //while(i--){};
    usleep(300000);   //the time can modify  by test
    return ret;
}

int mtk_scan_starttrig_v3()
{
    int ret = scan_starttrig_v2();
    //usleep(300000);   //hold low level time:6ms
    usleep(150000);   //hold low level time:20ms
    return ret;
}

int scan_endtrig_v2( ){
        int ret = ioctl(gpio_fd, GPIO_IOCTMODE0, GPIO_SCAN_TRIG_PIN); //gpio mode
        if (ret < 0) {
            ALOGD("scan_trig Could not set gpio mode\n");
            return -1;
        }
		
        ret = ioctl(gpio_fd, GPIO_IOCSDIRIN, GPIO_SCAN_TRIG_PIN); // direction (input)
        if (ret < 0) {
            ALOGD("scan_trig Could not set direction input\n");
            return -1;
        }
		
        ret = ioctl(gpio_fd, GPIO_IOCSPULLENABLE, GPIO_SCAN_TRIG_PIN); // Pull_able
        if (ret < 0) {
            ALOGD("scan_trig Could not pull_able output\n");
            return -1;
        }


        ret = ioctl(gpio_fd, GPIO_IOCSPULLUP, GPIO_SCAN_TRIG_PIN); // Pull UP
        if (ret < 0) {
            ALOGD("scan_trig Could not pull_up output\n");
            return -1;
        }


        ALOGD("@@@@niu@@@@  scan_endtrig_v2 OK : ret=%d\n", ret);
  
    return 0;   
}

int mtk_scan_endtrig_v2(){
    int i=30000;
    int ret = scan_endtrig_v2();
    //while(i--){};
    usleep(300000);   //the time can modify  by test
    return ret;
}
//niuweifeng add end ,  for key mode 0511
int mtk_scan_endtrig_v3()
{
    int ret = scan_endtrig_v2();
    //usleep(300000);   //the time can modify  by test
    usleep(150000);   //the time can modify  by test
    return ret;
}






int mtk_scan_powerOn(){
    //mtk_scan_endtrig();
    return scan_power(true);
}

int mtk_scan_powerOff(){
    return scan_power(false);
}

int scan_trig_init()
{
        int ret = ioctl(gpio_fd, GPIO_IOCTMODE0, GPIO_SCAN_TRIG_PIN); //gpio mode
        if (ret < 0) {
            ALOGD("scan_trig Could not set gpio mode\n");
            return -1;
        }
		
        ret = ioctl(gpio_fd, GPIO_IOCSDIRIN, GPIO_SCAN_TRIG_PIN); // direction (input)
        if (ret < 0) {
            ALOGD("scan_trig Could not set direction input\n");
            return -1;
        }
		
        /*ret = ioctl(gpio_fd, GPIO_IOCSPULLENABLE, GPIO_SCAN_TRIG_PIN); // Pull_able
        if (ret < 0) {
            ALOGD("scan_trig Could not pull_able output\n");
            return -1;
        }


        ret = ioctl(gpio_fd, GPIO_IOCSPULLUP, GPIO_SCAN_TRIG_PIN); // Pull UP
        if (ret < 0) {
            ALOGD("scan_trig Could not pull_up output\n");
            return -1;
        }*/


    ALOGD("@@@@niu@@@@  scan_endtrig_v2 OK : ret=%d\n", ret);
  
    return 0;   
}

int scan_switch(const int fd, int on_off)
{
	int ret = -1;

	ret = write_test(fd, &scan_wakeup, 1);
	if(ret <= 0)
	{
		ALOGD("Could not wakeup scanner, ret=%d\r\n", ret);
		return -1;
	}

	usleep(20000);//wait 20ms to wakeup scanner~!

	if(on_off == SCAN_OPEN)//open  scanner
	{
		ret = write_test(fd, scan_en, 6);
		if(ret <= 0)
		{
			ALOGD("Could not open scanner, ret=%d\r\n", ret);
			return -1;
		}
	}
	else if(on_off == SCAN_CLOSE)//close  scanner
	{
		ret = write_test(fd, scan_dis, 6);
		if(ret <= 0)
		{
			ALOGD("Could not close scanner, ret=%d\r\n", ret);
			return -1;
		}
	}
	else
	{
		ALOGD("Errpr:please check you flag, Flag = %d\r\n", on_off);
		return -1;
	}

	//usleep(25000);
	usleep(50000);
	if (tcflush(fd, TCIOFLUSH) <0)
	{
		ALOGD("Could not flush uart port");
		return -1;
	}
	
	return 0;
	
}

//add niuweifeng for inidia
static char codabar_sw[11] = {0x09,0xC6,0x04,0x00,0xFF,0x07,0x01,0x37,0x01,0xFD,0xEE};   //09 C6 04 00 FF 07 01 37 01 FD EE
int scan_codabar_sw(const int fd)
{
	int ret = -1;
	
	ret = write_test(fd, &scan_wakeup, 1);
	if(ret <= 0)
	{
		ALOGD("Could not wakeup scanner, ret=%d\r\n", ret);
		return -1;
	}
	
       usleep(20000);//wait 20ms to wakeup scanner~!

	ret = write_test(fd, codabar_sw,11);//  (fd, scan_en, 6);
		if(ret <= 0)
		{
			ALOGD("Could not set laser on 5s, ret=%d\r\n", ret);
			return -1;
		}

	usleep(50000);   //50000
	if (tcflush(fd, TCIOFLUSH) <0)
	{
		ALOGD("Could not flush uart port");
		return -1;
	}
	
	return 0;

}
//end niuweifeng
	
JNIEXPORT jint JNICALL Java_tf_test_SerialPort_open(JNIEnv *env, jclass cls, jstring str){
	int file_ptr;
	int ret = 0;

	int len = env->GetStringLength(str);
	if(len <= 0){
		ALOGD("device name is invalid.\n");
		return -1;
	}
	jboolean isCopy;
    	char* rt_string = (char*)env->GetStringUTFChars(str, &isCopy);
    	if(rt_string == NULL){
                ALOGD("Copying device name failed.\n");
        	return -1;
    	}	

	file_ptr = open(rt_string, O_RDWR | O_NOCTTY | O_NDELAY);
	if (file_ptr < 0) {
                env->ReleaseStringUTFChars(str, rt_string);
		ALOGD("Could not open serial port\n");
		return(-1);
	}
        env->ReleaseStringUTFChars(str, rt_string);

        gpio_fd = open("/dev/mtgpio", O_RDWR);
        if(gpio_fd < 0){
            ALOGD("Could not open GPIO, liuqingling\n");
	    return(-1);
        }

//add niuweifeng india
 	//int scan_codabar_sw(const int fd)
	ret = scan_codabar_sw(file_ptr);
	if(ret < 0)
		{
			ALOGD("Could not set codabar sw, ret=%d\r\n", ret);
			return(-1);
		}


//end niuweifeng

		ret = scan_switch(file_ptr, SCAN_CLOSE);
		if(ret < 0)
		{
			ALOGD("Could not disable scanner, ret=%d\r\n", ret);
			return(-1);
		}

		/*int ret = mtk_scan_powerOn();//Modify by jiawentao:2013-11-18--Begin
        if(ret < 0){
            ALOGD("Could not power on GPIO, ret=%d\n", ret);
	    return(-1);
        }
        ALOGD("open GPIO ret=%d\n", ret);//Modify by jiawentao:2013-11-18--End

		usleep(3000);
		ret = scan_trig_init();
		if(ret < 0)
		{
            ALOGD("Could not power on GPIO, ret=%d\n", ret);
	    	return(-1);
        }
		ALOGD("Trig int success: ret=%d\n", ret);*/
	ALOGD("Open serial port success.devName=%s, fd=%d, len=%d, gpio_fd=%d\n", rt_string, file_ptr, len, gpio_fd);
        //env->ReleaseStringUTFChars(str, rt_string);

	return file_ptr;
}

JNIEXPORT jint JNICALL Java_tf_test_SerialPort_writeData(JNIEnv *env, jclass cls, jint fd, jstring str){
	if (fd < 0) {
		ALOGD("Could not open uart port\n");
		return(-1);
	}
	if(str == NULL){
		ALOGD("String is NULL.\n");
		return(-1);
	}

	int len = env->GetStringLength(str);
	if(len <= 0){
		ALOGD("length of string is invalid.\n");
		return -1;
	}
	jboolean isCopy;
    	char* rt_string = (char*)env->GetStringUTFChars(str, &isCopy);
    	if(rt_string == NULL){
        	return -1;
    	}
	int result = write_test(fd, rt_string, len);
       ALOGD("liuer write data rt_string=%s, result=%d.\n", rt_string, result);
	env->ReleaseStringUTFChars(str, rt_string);
	return result;
}

/*---------140319 add for customer request-----------------------------------*/
int PreCheckUtfString(const char* bytes)
{
    const char* origBytes = bytes;
    if (bytes == NULL) {
        ALOGD("PreCheckUtfString empty string return 0.\n");
        return 0;
    }
    
    while (*bytes != '\0') {
        unsigned char utf8 = *(bytes++);
        
        // Switch on the high four bits.
        switch (utf8 >> 4) {
            case 0x00:
            case 0x01:
            case 0x02:
            case 0x03:
            case 0x04:
            case 0x05:
            case 0x06:
            case 0x07: {
                // Bit pattern 0xxx. No need for any extra bytes.
                break;
            }
            case 0x08:
            case 0x09:
            case 0x0a:
            case 0x0b:
            case 0x0f: {             
                ALOGD("****PreCheckUtfString: illegal start byte 0x%x\n", utf8);
                return -1;
            }
            case 0x0e: {
                // Bit pattern 1110, so there are two additional bytes.
                utf8 = *(bytes++);
                if ((utf8 & 0xc0) != 0x80) {
                    ALOGD("****PreCheckUtfString: illegal continuation byte 0x%x\n", utf8);
                    return -1;
                }
                // Fall through to take care of the final byte.
            }
            case 0x0c:
            case 0x0d: {
                // Bit pattern 110x, so there is one additional byte.
                utf8 = *(bytes++);
                if ((utf8 & 0xc0) != 0x80) {
                    ALOGD("****PreCheckUtfString: illegal continuation byte 0x%x\n", utf8);
                    return -1;
                }
                break;
            }
        }
    }

    ALOGD("PreCheckUtfString ok return 0.\n");
    return 0;
} 
/*---------140319 add for customer request-------end-------------------------*/

JNIEXPORT jstring JNICALL Java_tf_test_SerialPort_receiveData(JNIEnv *env, jclass cls, jint fd){
    int ret = -1;//Modify by jiawentao:2013-11-18

    if (fd < 0) {
        ALOGD("has not open serial port\n");
        return NULL;
    }
    if (gpio_fd < 0){
        ALOGD("has not open gpio port\n");
        return NULL;
    }
    
    //Modify by jiawentao:2013-11-18--Begin
    /*int ret = mtk_scan_powerOn();
    if(ret < 0){
        ALOGD("Could not power on GPIO, ret=%d\n", ret);
        return NULL;
    }
    ALOGD("receiveData poweron GPIO success ret=%d\n", ret);*/
    //Modify by jiawentao:2013-11-18--End

    ret = scan_switch(fd, SCAN_OPEN);
    if(ret < 0)
    {
        ALOGD("Could not disable scanner, ret=%d\r\n", ret);
        return NULL;
    }
    usleep(100);
    ret = mtk_scan_starttrig_v3();
    if(ret < 0)
    {
        ALOGD("couldn't start trig GPIO. ret=%d\n", ret);
        return NULL;
    }
    ALOGD("receiveData starttrig GPIO success ret=%d\n", ret);
    //while((ioctl(gpio_fd, GPIO_IOCQDATAIN, GPIO_SCAN_TRIG_PIN)) == 0){};
    //usleep(80000);//80000Wait a 25ms for high level
//	usleep(3000000); //////niuweifeng  3s delay for test india
    static char buf_read[TRANSFER_COUNT];
    memset(buf_read,0,sizeof(buf_read)); /*add for function*/
	
    if(buf_read == NULL){
        ALOGD("no buffer.\n");
        return NULL;
    }
    int len = read_test(fd, &buf_read[0], TRANSFER_COUNT);

    //usleep(80000);//80000

    /*ret = mtk_scan_endtrig();
    if(ret < 0){
        ALOGD("couldn't end trig GPIO. ret=%d\n", ret);
        //return NULL;
    }
    ALOGD("receiveData endtrig GPIO success ret=%d\n", ret);*/

    //Modify by jiawentao:2013-11-18--Begin
    /*ret = mtk_scan_powerOff();
    if(ret < 0){
        ALOGD("couldn't poweroff GPIO. ret=%d\n", ret);
        //return NULL;
    }
    ALOGD("receiveData poweroff GPIO success ret=%d\n", ret);*/
    //Modify by jiawentao:2013-11-18--End

    //Remove by jiawentao--2013-11-19

    ret = scan_switch(fd, SCAN_CLOSE);
    if(ret < 0)
    {
        ALOGD("Could not disable scanner, ret=%d\r\n", ret);
        return NULL;
    }

    ret = mtk_scan_endtrig_v3();//jiawentao add for key mode
    if(ret < 0)
    {
        ALOGD("couldn't end trig GPIO. ret=%d\n", ret);
        return NULL;
    }
    ALOGD("receiveData endtrig GPIO success ret=%d\n", ret);
    if(len <= 0){
        ALOGD("get zero data.\n");
        ALOGD("liuer read data len <= 0. buf_read=%s\n", buf_read);
        return NULL;
    }

    //for(int i=0; i < len; i++)
    //	ALOGD("buf_read[%d]=%c\n", i,buf_read[i]);

    ALOGD("liuer read data success. buf_read=%s\n", buf_read);

    /*---------140319 add for customer request-----------------------------------*/
    if(PreCheckUtfString(buf_read)<0)
    {
        ALOGD("Java_tf_test_SerialPort_receiveData PreCheckUtfString fail.\n");
        env->ThrowNew(env->FindClass("android/util/AndroidException"),"Invalid Utf String");
        return NULL; 
    }
    /*---------140319 add for customer request----end----------------------------*/	

    return env->NewStringUTF(&buf_read[0]);
}

JNIEXPORT jint JNICALL Java_tf_test_SerialPort_close(JNIEnv *env, jclass cls, jint fd){

	//Modify by jiawentao:2013-11-18--Begin
	/*int ret = mtk_scan_powerOff();
	if(ret < 0){
	ALOGD("couldn't poweroff GPIO. ret=%d\n", ret);
	//return NULL;
	}
	ALOGD("receiveData poweroff GPIO success ret=%d\n", ret);*/
	//Modify by jiawentao:2013-11-18--End

	if (gpio_fd < 0) {
            ALOGD("Could not close GPIO\n");
        } else {
            //int ret = mtk_scan_powerOff();
            close(gpio_fd);
            //ALOGD("close GPIO success ret=%d\n", ret);
        }
        if (fd < 0) {
		ALOGD("Could not close uart port\n");
		return(-1);
	}
	close(fd);
	ALOGD("liuer serial port closed fd=%d, gpio_fd=%d\n", fd, gpio_fd);
	return 0;
}

