
#include <jni.h>  
#include "JNIHelp.h"
#include "SpecialKey.h"
#include "android_runtime/AndroidRuntime.h"  
#include "tf_test_SpecialKey.h" 

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <assert.h>
#include <pthread.h>
#include <sys/ioctl.h>


#define LOGD(...) //__android_log_print(ANDROID_LOG_SILENT, LOG_TAG, __VA_ARGS__)
#define LOGV(...) //__android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) //__android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#define SPECIAL_KEY_DEVICE_NAME "/dev/mtgpio"  
#define GPIO_SCAN_TRIG_PIN  184
static jobject mCallbacksObj = NULL;  
static jmethodID method_receive;  
static jboolean mThreadRunning = false;
static jclass  mClass; 
static int gpio_fd = -1;
static JNIEnv* m_env = NULL;
JavaVM *g_vm = NULL;
static int g_thread = 0; 

static void receive_callback(int state, JNIEnv *env)  
{  
    LOGV("shen receive_callback state = ");
    //invoke java callback method  
    if(mClass != NULL && method_receive!= NULL){
       LOGV("shen receive_callback 11 state = " );
       //JNIEnv* env = AndroidRuntime::getJNIEnv();
       env->CallVoidMethod(mClass, method_receive,state); 
    }
    LOGV("shen receive_callback 22 state " );

}  

#if 0
//niuweifeng add start ,  for key mode 0511
static void *thread_entry(void *args)  
{  
     
    LOGV("shen  enter thread mThreadRunning ");
    JNIEnv* env;
    int envErr = 0;
    envErr = g_vm->AttachCurrentThread(&env,NULL);
    if(envErr != 0){
         LOGV("shen  AttachCurrentThread error ");
    }
    if(env == NULL){
         LOGV("shen  get env error ");
    }
    gpio_fd = open(SPECIAL_KEY_DEVICE_NAME, O_RDWR);
    int temp = 1;
    while(mThreadRunning)  
    {  

		LOGV("@@@@niu@@@@ thread  Running..gpio_fd.=%d", gpio_fd);    
		int ret_log = ioctl(gpio_fd, GPIO_IOCQDATAIN, GPIO_SCAN_TRIG_PIN);
		LOGV("@@@@niu@@@@ thread  Running...temp=%d,ret_log=%d", temp,ret_log);    

		int ret_pullen =  ioctl(gpio_fd, GPIO_IOCQPULLEN, GPIO_SCAN_TRIG_PIN);  //0:dis    1:en
	       int ret_dir =  ioctl(gpio_fd, GPIO_IOCQDIR, GPIO_SCAN_TRIG_PIN);//0:in    1:out
	       int ret_outdata =  ioctl(gpio_fd, GPIO_IOCQDATAOUT, GPIO_SCAN_TRIG_PIN);//0:low    1:high

		if((0==ret_pullen)&&(1==ret_dir)&&(0==ret_outdata))
		{
			LOGV("@@@@niu@@@@ thread  Running..no set temp when starttrig");    
			usleep(100000);  
		}
		else	
		{
			LOGV("shen thread  Running..gpio_fd.=%d", gpio_fd);    
			int ret = ioctl(gpio_fd, GPIO_IOCQDATAIN, GPIO_SCAN_TRIG_PIN);
			LOGV("shen thread  Running...=%d", ret);    
			if(0 == ret){
			  if(temp != ret){
			  receive_callback(ret,env);
			     temp = ret;
			  }
			}else{
			    temp = ret;
			}
			  
			usleep(100000);  
		}

   }  
    g_vm->DetachCurrentThread();
    g_thread = 0;
    pthread_exit(NULL);
    
    LOGV("shen end thread mThreadRunning ");
    return NULL;
          
}  
//niuweifeng add end ,  for key mode 0511
#elif 0
//jiawentao add start ,  for key mode 2013-11-19
static void *thread_entry(void *args)  
{  
     
    LOGV("shen  enter thread mThreadRunning ");
    JNIEnv* env;
    int envErr = 0;
    envErr = g_vm->AttachCurrentThread(&env,NULL);
    if(envErr != 0){
         LOGV("shen  AttachCurrentThread error ");
    }
    if(env == NULL){
         LOGV("shen  get env error ");
    }
    gpio_fd = open(SPECIAL_KEY_DEVICE_NAME, O_RDWR);
    int temp = 1;
    while(mThreadRunning)  
    {  
        LOGV("shen thread  Running..gpio_fd.=%d", gpio_fd);    
        int ret = ioctl(gpio_fd, GPIO_IOCQDATAIN, GPIO_SCAN_TRIG_PIN);
        LOGV("shen thread  Running...=%d, temp=%d", ret,temp);    
		
		if(0 == ret)
		{
			usleep(10);//100
			ret = ioctl(gpio_fd, GPIO_IOCQDATAIN, GPIO_SCAN_TRIG_PIN);
			if(0 == ret)
			{
				if(temp != ret)
				{
					receive_callback(ret,env);
					temp = ret;
				}
	        }
			else
			{
	            temp = ret;
	        }
		}
		else
		{
			temp = ret;
		}
        usleep(25000);//25000
       
    }  
    g_vm->DetachCurrentThread();

    g_thread = 0;
    pthread_exit(NULL);
	
    LOGV("shen end thread mThreadRunning ");
    return NULL;
          
}  
//jiawentao add end ,  for key mode 2013-11-19
#elif 0
//jiawentao add start ,  for key mode 2013-11-19
static void *thread_entry(void *args)  
{  
     
    LOGV("shen  enter thread mThreadRunning ");
    JNIEnv* env;
    int envErr = 0;
    envErr = g_vm->AttachCurrentThread(&env,NULL);
    if(envErr != 0){
         LOGV("shen  AttachCurrentThread error ");
    }
    if(env == NULL){
         LOGV("shen  get env error ");
    }
    gpio_fd = open(SPECIAL_KEY_DEVICE_NAME, O_RDWR);
    int temp = 1;
	int up_flag = 1;//up:1    not up:0
    while(mThreadRunning)  
    {  
        LOGV("shen thread  Running..gpio_fd.=%d", gpio_fd);    
        int ret = ioctl(gpio_fd, GPIO_IOCQDATAIN, GPIO_SCAN_TRIG_PIN);
        LOGV("shen thread  Running...=%d, temp=%d", ret,temp);    
		
		if(0 == ret && 1 == up_flag)
		{
			//usleep(10);//100
			ret = ioctl(gpio_fd, GPIO_IOCQDATAIN, GPIO_SCAN_TRIG_PIN);
			if(0 == ret && 1 == up_flag)
			{
				receive_callback(ret,env);
	        }
			up_flag = 1;
		}

		ret = ioctl(gpio_fd, GPIO_IOCQDATAIN, GPIO_SCAN_TRIG_PIN);
		if(1 == ret)
			up_flag = 1;
		else
			up_flag = 0;
		
        usleep(25000);//25000
       
    }  
    g_vm->DetachCurrentThread();

    g_thread = 0;
    pthread_exit(NULL);
	
    LOGV("shen end thread mThreadRunning ");
    return NULL;
          
}
#else
//niuweifeng add start ,  for key mode 0511
static void *thread_entry(void *args)  
{  
     
    LOGV("shen  enter thread mThreadRunning ");
    JNIEnv* env;
    int envErr = 0;
    envErr = g_vm->AttachCurrentThread(&env,NULL);
    if(envErr != 0){
         LOGV("shen  AttachCurrentThread error ");
    }
    if(env == NULL){
         LOGV("shen  get env error ");
    }
    gpio_fd = open(SPECIAL_KEY_DEVICE_NAME, O_RDWR);
    int temp = 1;
    while(mThreadRunning)  
    {  

		LOGV("@@@@niu@@@@ thread  Running..gpio_fd.=%d", gpio_fd);    
		int ret_log = ioctl(gpio_fd, GPIO_IOCQDATAIN, GPIO_SCAN_TRIG_PIN);
		LOGV("@@@@niu@@@@ thread  Running...temp=%d,ret_log=%d", temp,ret_log);    

		int ret_pullen =  ioctl(gpio_fd, GPIO_IOCQPULLEN, GPIO_SCAN_TRIG_PIN);  //0:dis    1:en
	       int ret_dir =  ioctl(gpio_fd, GPIO_IOCQDIR, GPIO_SCAN_TRIG_PIN);//0:in    1:out
	       int ret_outdata =  ioctl(gpio_fd, GPIO_IOCQDATAOUT, GPIO_SCAN_TRIG_PIN);//0:low    1:high

		if((0==ret_pullen)&&(1==ret_dir)&&(0==ret_outdata))
		{
			LOGV("@@@@niu@@@@ thread  Running..no set temp when starttrig");    
			usleep(100000);
			//usleep(50000);  
		}
		else	
		{
			LOGV("shen thread  Running..gpio_fd.=%d", gpio_fd);    
			int ret = ioctl(gpio_fd, GPIO_IOCQDATAIN, GPIO_SCAN_TRIG_PIN);
			LOGV("shen thread  Running...=%d", ret);    
			if(0 == ret){
			  if(temp != ret){
			  receive_callback(ret,env);
			     temp = ret;
			  }
			}else{
			    temp = ret;
			}
			  
			usleep(100000);
			//usleep(50000);
		}

   }  
    g_vm->DetachCurrentThread();
    g_thread = 0;
    pthread_exit(NULL);
    
    LOGV("shen end thread mThreadRunning ");
    return NULL;
          
}  
//niuweifeng add end ,  for key mode 0511

#endif


/*shenchaojie*/
/*
static void *thread_entry(void *args)  
{  
     
    LOGV("shen  enter thread mThreadRunning ");
    JNIEnv* env;
    int envErr = 0;
    envErr = g_vm->AttachCurrentThread(&env,NULL);
    if(envErr != 0){
         LOGV("shen  AttachCurrentThread error ");
    }
    if(env == NULL){
         LOGV("shen  get env error ");
    }
    gpio_fd = open(SPECIAL_KEY_DEVICE_NAME, O_RDWR);
    int temp = 1;
    while(mThreadRunning)  
    {  
        LOGV("shen thread  Running..gpio_fd.=%d", gpio_fd);    
        int ret = ioctl(gpio_fd, GPIO_IOCQDATAIN, GPIO_SCAN_TRIG_PIN);
        LOGV("shen thread  Running...=%d", ret);    
        if(0 == ret){
          if(temp != ret){
          receive_callback(ret,env);
             temp = ret;
          }
        }else{
            temp = ret;
        }
          
        usleep(100000);  
       
    }  
    g_vm->DetachCurrentThread();
    LOGV("shen end thread mThreadRunning ");
    return NULL;
          
}  
*/


static void init()  
{     
    LOGV("shen init " );
    pthread_t thread;  
    ALOGD("init g_thread = %d",g_thread);
    if (g_thread == 0){
        pthread_create(&thread,NULL,thread_entry,(void *)NULL); 
        g_thread ++;
    }
}
 
/*
 * Class:     com_yunhu_SpecialKeyForCit
 * Method:    startListenthread
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_tf_test_SpecialKey_startListenthread
  (JNIEnv* env, jobject obj)
{ 
    LOGV("shen JNICALL Java_com_yunhu_SpecialKeyForCit_startListenthread");
    m_env = env;
    mThreadRunning = true;
    mClass = (jclass)env->NewGlobalRef(obj);
    jclass clazz = env->FindClass("tf/test/SpecialKey");
    method_receive = env->GetMethodID(clazz, "CallBack", "(I)V");       
    init();  
}

/*
 * Class:     com_yunhu_SpecialKeyForCit
 * Method:    stopListenthread
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_tf_test_SpecialKey_stopListenthread
  (JNIEnv* env, jobject clazz)
{
    LOGV("shen JNICALL Java_com_yunhu_SpecialKeyForCit_stopListenthread");
    mThreadRunning = false;
    mCallbacksObj = NULL;
    method_receive = NULL;
    gpio_fd = -1;
}

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
   LOGV("shen JNI_OnLoad");
   JNIEnv* env = NULL;
   if(vm->GetEnv((void**)&env, JNI_VERSION_1_4) != JNI_OK)
   {
       return -1;
   }
   g_vm = vm;
   return JNI_VERSION_1_4;
}


