package tf.test;

import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import java.io.IOException;

public class SpecialKey{
    private static final String TAG = "SpecialKey";
    private Context mcontext;

    static{
        System.loadLibrary("SpecialKey");
    }    

    public void SendContext(Context context){
        mcontext = context;
    }

    public void CallBack(int state){  
        Log.e(TAG ,"tf.test Ser Callbcak state="+state);

        Intent intent = new Intent();
        intent.setAction("tf.test.SpecialKeyPressed");
        mcontext.sendBroadcast(intent);

        /*try  
        {  
            String keyCommand = "input keyevent " + KeyEvent.KEYCODE_S;  
            Runtime runtime = Runtime.getRuntime();  
            Process proc = runtime.exec(keyCommand);  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }*/
    }  

    public native void startListenthread();
    public native void stopListenthread();    
}
