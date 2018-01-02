package com.yunhu.yhshxc.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunhu.android.view.EditTextShakeHelper;
import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.activity.AbsBaseActivity;
import com.yunhu.yhshxc.activity.HomePageActivity;
import com.yunhu.yhshxc.activity.InitActivity;
import com.yunhu.yhshxc.application.SoftApplication;
import com.yunhu.yhshxc.dialog.DialogLawActivity;
import com.yunhu.yhshxc.help.QuestionActivity2;
import com.yunhu.yhshxc.utility.Constants;
import com.yunhu.yhshxc.utility.MD5Helper;
import com.yunhu.yhshxc.utility.PublicUtils;
import com.yunhu.yhshxc.utility.SharedPreferencesUtil;
import com.yunhu.yhshxc.utility.Version;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import gcg.org.debug.JLog;

/**
 * 这是注册、登录屏公用的Activity
 * 
 * 注册：需要用户在此Activity提供的模拟键盘上先后两次键入4位密码（称之为创建密码，密码保存在本地），两次键入的密码必须一致，否则提示错误。密码创建成功后会跳转到初始化页面。
 * 
 * 登录：需要用户在此Activity提供的模拟键盘上输入登录时创建的4位密码，如果输入有误会提示用户，如果连续三次输入错误，会锁定屏幕30秒，提示用户思考正确的密码是什么。
 * 密码通过本地验证，不需要联网。如果密码验证成功且此前已经完成过App初始化工作，则跳转到主界面。如果未完成初始化工作，则跳转到初始化页面。
 * 
 * @version: 2013.5.20
 * @author wangchao
 *
 */
public class LoginActivity extends AbsBaseActivity {
	

	
	/**
	 * State枚举用来标示当前用户界面的状态，包括登录、注册·创建密码（第一次输入密码）、注册·确认密码。
	 * 该枚举常量还封装了不同界面状态下相关的文字。
	 */
	private enum State {
		/**
		 * 登录状态
		 */
		LOGIN {
			@Override
			String getLabel() {
				return PublicUtils.getResourceString(SoftApplication.context,R.string.input_password);
			}

			@Override
			String geTitle() {
				return PublicUtils.getResourceString(SoftApplication.context,R.string.login_);
			}
		},
		/**
		 * 注册·创建密码（第一次输入密码）
		 */
		REGISTER {
			@Override
			String getLabel() {
				return PublicUtils.getResourceString(SoftApplication.context,R.string.input_new_pwd);
			}

			@Override
			String geTitle() {
				return PublicUtils.getResourceString(SoftApplication.context,R.string.input_password1);
			}
		},
		/**
		 * 注册·确认密码
		 */
		REGISTER_CONFIRM {
			@Override
			String getLabel() {
				return PublicUtils.getResourceString(SoftApplication.context,R.string.input_password2);
			}

			@Override
			String geTitle() {
				return PublicUtils.getResourceString(SoftApplication.context,R.string.input_password1);
			}
		};
		
		/**
		 * @return 获得此Activity标题栏的文字
		 */
		abstract String geTitle();
		
		/**
		 * @return 获得此Activity中间提示用户操作的文字
		 */
		abstract String getLabel();
	};
	
	private State state;
	
	/**
	 * 标题栏
	 */
	private TextView txtTitle;
	
	/**
	 * 3*4模拟键盘按钮
	 */
	private TextView btnKey1, btnKey2, btnKey3, btnKey4, btnKey5, btnKey6, btnKey7, btnKey8, btnKey9, btnKey0, btnKeyHelp, btnKeyDel;

	/**
	 * 显示错误的文本
	 */
	private TextView txtError;

	/**
	 * 屏幕中央的图标，默认是绿色，输入错误时会变红
	 */
	private ImageView imgIcon;
	
	/**
	 * 登录界面下连续三次输入错误后的倒计时提示
	 */
	private TextView txtTimer;
	
	/**
	 * 屏幕重要提示用户操作的文字
	 */
	private TextView txtMainLabel;
	
	/**
	 * 四个输入框，用户不能直接操作，通过模拟键盘的输入会以“*”显示在这里
	 */
	private TextView edtInput1, edtInput2, edtInput3, edtInput4;
	
	/**
	 * 用来标记用户当前输入的是第几位密码
	 */
	private int inputIndex;
	
	/**
	 * 用来标记用户连续输入错误的次数
	 */
	private int errorCount;
	
	/**
	 * 连续三次输入错误后倒计时用的线程池，每隔一秒刷新一次倒计时数字。
	 * 因为创建线程是件比较消耗资源的事情，所以为了节约资源，不需要一开始就实例化该对象，只有需要时才会创建，而且此Activity只需要创建一个单例线程池即可。
	 */
	private ExecutorService threadPool;
	private Context context;
	
	/**
	 * 该Handler只用来更新连续三次输入错误时的倒计时数字
	 */
	private final Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			//更新倒计时的数字
//			txtTimer.setText(String.valueOf(msg.what))
			txtError.setText(PublicUtils.getResourceString(context,R.string.input_password3)+String.valueOf(msg.what)+PublicUtils.getResourceString(context,R.string.input_password4));
			
			//如果what是30，说明锁屏（模拟键盘禁止输入）已达30秒，这时需要恢复相关可视组件的正常状态
			if (msg.what == 30) {
				imgIcon.setImageBitmap(null);
				imgIcon.setBackgroundResource(R.drawable.login_icon_bg_err);
				txtTimer.setVisibility(View.VISIBLE);
				isError = true;
				
				setKeyboardEnable(false);
			}
			//如果what是0，则说明错误计时刚开始，这时需要将相关可视组件调整为错误状态，并锁屏（模拟键盘禁止输入）
			else if (msg.what == 0) {
				errorCount = 0;
				isError = false;
				txtError.setText("");
				imgIcon.setBackgroundResource(R.drawable.login_icon_bg);
				imgIcon.setImageResource(R.drawable.lock);
				txtTimer.setVisibility(View.GONE);
				
				setKeyboardEnable(true);
			}
			return true;
		}
	});
	
	/**
	 * 用来缓存注册时用户第一次输入的密码
	 */
	private String password;
	
	/**
	 * 连续三次输入错误后倒计时线程每次需要执行的Runnable
	 */
	private Runnable timer;
	
	/**
	 * 任何State状态下，当用户输入第四位密码后延迟进行的操作。（只有只有登录状态下密码输入正确时是例外，为了方便，密码输入正确后会直接跳转到下一个Activity）
	 * 这是为了界面显示的合理性，因为如果用户输入第四位密码后不延迟一点时间而直接跳转到下一个界面的话，
	 * 用户会看不到输入第四位密码后应该显示出来的“*”符号，也就是说第四位密码框永远是空白的，用户永远只能看到前三位密码框输入后的“*”符号。
	 * 为了解决这个问题，就需要延迟一点时间，先让用户看到第四位密码的“*”符号，然后再执行相关操作。
	 */
	private final Runnable delayed = new Runnable() {
		
		@Override
		public void run() {
			isDelay = false;//解除延迟状态
			
			
			switch (state) {
				case LOGIN:
					//如果当前是登录状态，只有在输入错误时才会执行到这里，如果输入正确会直接跳转到下一个Activity
					
					clearEditInput();//清除屏幕中间4个输入框的内容
					inputIndex = 1;//将焦点切换到第一个输入框，也就是说下次通过模拟键盘输入时，结果显示在第一个输入框中。
					
					if (++errorCount == 3) {//每次输入错误都将错误计数器+1，到3次时锁屏，启动倒计时程序。
						txtError.setText(PublicUtils.getResourceString(context,R.string.input_password5)+"!");
						if (threadPool == null) {
							threadPool = Executors.newSingleThreadScheduledExecutor();
						}
						threadPool.execute(timer);
					}
					break;
					
				case REGISTER:
					//如果当前是注册·创建密码状态，则输入第四位密码后将状态切换到注册·确认密码状态，
					//并更新相关界面组件的显示信息
					state = State.REGISTER_CONFIRM;
					txtMainLabel.setText(state.getLabel());
					password = getPwd();//获取注册·创建密码状态下用户输入的密码，并缓存
					clearEditInput();
					inputIndex = 1;
					break;
					
				case REGISTER_CONFIRM:
					String pwd = getPwd();//获取注册·确认密码状态下用户输入的密码
					
					//如果用户两次输入的密码相同，则将密码以MD5的形式保存到本地，并将用户手机卡的SubscriberId也保存到本地（防止用户换卡），同时跳转到下一个Activity。
					if (pwd.equals(password)) {
						SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(getApplicationContext());
						prefs.setIsFirstLogin(false);//如果创建密码成功，则将用户是否第一次登录的状态标记设置为false，表示用户已经登录过此App了
						try {
							prefs.saveSubscriberId(PublicUtils.getSubscriberId(LoginActivity.this));//将用户手机卡的SubscriberId保存到本地
							String md5Pwd = MD5Helper.getMD5Checksum(pwd.getBytes());//将密码转换成md5形式
							prefs.setPassword(md5Pwd);//将密码保存到本地
							
							loadMain();//跳转到下一个Activity
						} catch (Exception e) {
							JLog.d(TAG, "MD5加密异常" + e.toString());
						}
					}
					//如果用户两次输入的密码不一致，则切换回注册·创建密码状态，并按照创建密码状态更新相关可视组件的信息
					else {
						state = State.REGISTER;//切换回注册·创建密码状态
						txtMainLabel.setText(state.getLabel());
						inputIndex = 1;//将焦点切换到第一个输入框，也就是说下次通过模拟键盘输入时，结果显示在第一个输入框中。

						txtError.setText(PublicUtils.getResourceString(context,R.string.input_password6));
						imgIcon.setBackgroundResource(R.drawable.login_icon_bg_err);//改变屏幕中间的图标
						isError = true;//标记错误状态
						clearEditInput();//清除屏幕中间4个输入框的内容
					}
					break;
			}
		}
	};
	
	/**
	 * 是否处在延迟状态中的标记。当用户调用Handler.postDelayed(Runnable, int)方法时会将该标记设置为true，
	 * 当过了延迟时间Runnable被执行时会将该标记设置为false。在该标记为true的这段延迟时间内，用户对屏幕组件的操作是无效的。
	 */
	private volatile boolean isDelay = false;
	
	/**
	 * 用于标记用户输入错误时的状态
	 */
	private volatile boolean isError = false;
	
	/**
	 * 如果用户已经执行过跳转到下一个Activity的逻辑，则将这个值设置为true。这么做是为了确保用户只会执行一次跳转Activity的逻辑
	 */
	private volatile boolean isProcessed = false;
	private boolean isResetPass ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		isResetPass = getIntent().getBooleanExtra("isResetPass", false);
		if(isResetPass){
			SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(getApplicationContext());
			prefs.setIsFirstLogin(true);
		}
		String projectVersion = this.getResources().getString(R.string.PROJECT_VERSIONS);//获取版本
		//根据不同版本执行不同的初始化
		if (projectVersion.equalsIgnoreCase(Constants.APP_VERSION_4_5)) {//4.5版
			initialize(Version.VER_45);
		}
		else if (projectVersion.equalsIgnoreCase("YILI")) {//伊利定制版
			initialize(Version.VER_YILI);
		}
		else {//正常版本
			initialize(Version.VER_NORAML);
		}
		
		// 提示法律条文
		if (!SharedPreferencesUtil.getInstance(this.getApplicationContext()).getIsReadedLaw()) {
			Intent intent = new Intent(this, DialogLawActivity.class);
			startActivity(intent);
		}
	}
	
	/**
	 * 根据版本初始化界面
	 * @param type 版本类型
	 */
	private void initialize(Version type) {
		inputIndex = 1;
		errorCount = 0;
		setContentView(R.layout.login);
		imgIcon = (ImageView)findViewById(R.id.main_icon);
		
		txtTimer = (TextView)findViewById(R.id.timer);
		
		txtMainLabel = (TextView)findViewById(R.id.main_label);
		
		edtInput1 = (TextView)findViewById(R.id.input_1);
		edtInput2 = (TextView)findViewById(R.id.input_2);
		edtInput3 = (TextView)findViewById(R.id.input_3);
		edtInput4 = (TextView)findViewById(R.id.input_4);

		//获取用户是否第一次登录的标记，如果是第一次登录，就切换到注册·创建密码状态，否则就切换到登录状态
		boolean isRegister = SharedPreferencesUtil.getInstance(this).getIsFirstLogin();
		if (isRegister) {
			state = State.REGISTER;
		}
		else {
			state = State.LOGIN;
			
			//初始化错误倒计时线程需要执行的Runnable
			timer = new Runnable() {
				
				@Override
				public void run() {
					//倒计时三十秒
					for (int i = 30; i > 0; i--) {
						handler.sendEmptyMessage(i);
						try {
							TimeUnit.SECONDS.sleep(1);
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					handler.sendEmptyMessage(0);//完成倒计时，将0秒的标记发送给handler。不能将此行代码放到上面的循环中，放入循环中会变成倒计时31秒。
				}
			};
		}
		txtMainLabel.setText(state.getLabel());
		
		txtTitle = (TextView)findViewById(R.id.titlebar);
		txtTitle.setText(state.geTitle());
		
		txtError = (TextView)findViewById(R.id.err);
		
		btnKey1 = (TextView)findViewById(R.id.btn1);
		btnKey1.setOnClickListener(keyboardListener);
		btnKey2 = (TextView)findViewById(R.id.btn2);
		btnKey2.setOnClickListener(keyboardListener);
		btnKey3 = (TextView)findViewById(R.id.btn3);
		btnKey3.setOnClickListener(keyboardListener);
		btnKey4 = (TextView)findViewById(R.id.btn4);
		btnKey4.setOnClickListener(keyboardListener);
		btnKey5 = (TextView)findViewById(R.id.btn5);
		btnKey5.setOnClickListener(keyboardListener);
		btnKey6 = (TextView)findViewById(R.id.btn6);
		btnKey6.setOnClickListener(keyboardListener);
		btnKey7 = (TextView)findViewById(R.id.btn7);
		btnKey7.setOnClickListener(keyboardListener);
		btnKey8 = (TextView)findViewById(R.id.btn8);
		btnKey8.setOnClickListener(keyboardListener);
		btnKey9 = (TextView)findViewById(R.id.btn9);
		btnKey9.setOnClickListener(keyboardListener);
		btnKeyHelp = (TextView)findViewById(R.id.btn_help);
		btnKeyHelp.setOnClickListener(keyboardListener);
		btnKey0 = (TextView)findViewById(R.id.btn0);
		btnKey0.setOnClickListener(keyboardListener);
		btnKeyDel = (TextView)findViewById(R.id.btn_delete);
		btnKeyDel.setOnClickListener(keyboardListener);
	}
	
	/**
	 * 从四个模拟输入框中提取密码字符串，将四个密码字符拼成一个字符串
	 * @return 四个输入框中的字符依次拼成的密码字符串
	 */
	private String getPwd() {
		return edtInput1.getTag().toString() + edtInput2.getTag().toString() + edtInput3.getTag().toString() + edtInput4.getTag().toString();
	}
	
	/**
	 * 设置锁屏状态，即模拟键盘是否可点击。
	 * @param enabled 如果模拟键盘可点击，则该值为true，否则为false
	 */
	private void setKeyboardEnable(boolean enabled) {
		btnKey0.setEnabled(enabled);
		btnKey1.setEnabled(enabled);
		btnKey2.setEnabled(enabled);
		btnKey3.setEnabled(enabled);
		btnKey4.setEnabled(enabled);
		btnKey5.setEnabled(enabled);
		btnKey6.setEnabled(enabled);
		btnKey7.setEnabled(enabled);
		btnKey8.setEnabled(enabled);
		btnKey9.setEnabled(enabled);
		btnKeyDel.setEnabled(enabled);
	}

	@Override
	protected void onDestroy() {
		//如果线程池不为null，则销毁当前Activity前要终止该线程池的所有线程。
		if (threadPool != null) {
			threadPool.shutdown();
		}
		super.onDestroy();
	}

	/**
	 * 模拟键盘事件监听器
	 */
	private final View.OnClickListener keyboardListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//如果已经三次输入错误，就要启动倒计时，这时模拟键盘不应该响应任何事件；
			//如果处在用户输入第四位密码后的延迟中，则模拟键盘也不应该响应任何事件。
			if (errorCount == 3 || isDelay)
				return;
			
			String pwd;
			
			//模拟键盘的12个按钮中只有删除和帮助按钮的事件需要独立的处理逻辑，
			//而数字按钮的事件逻辑相似，统一处理即可
			
			switch (v.getId()) {
				//删除密码事件
				case R.id.btn_delete:
					//一般情况下，拥有焦点的输入框会显示空白，因为接收到输入后焦点会立刻跳到下一个输入框上，
					//所以删除事件实际上应该删除前一位的密码，并将焦点同时移动到前一位。例如：当前用户的焦点
					//在第三位输入框上（此时第三位显示空白），那么按下删除按钮后应该清除第二位输入框中所显示
					//的密码，并将焦点移动到第二位输入框上。
					//例外：因为第四位输入框已经是末位，所以它接受输入后仍然自己持有焦点，这时删除事件应该先
					//考虑是否要清除它自己的密码（如果有的话），然后再清除第三位的密码。
					switch (inputIndex) {
						case 1:
							break;
						
						case 2:
//							edtInput1.setText("");
							edtInput1.setBackgroundResource(R.drawable.shape_ring_blue_small);
							edtInput1.setTag("");
							inputIndex--;//将焦点前移一位
							break;
							
						case 3:
//							edtInput2.setText("");
							edtInput2.setBackgroundResource(R.drawable.shape_ring_blue_small);
							edtInput2.setTag("");
							inputIndex--;
							break;
							
						case 4:
							//如果第四位输入框已经有密码了，此时删除事件应该清除的是它自己的密码，并仍然持有焦点
							//而不是把焦点交给前一位输入框
							if (edtInput4.getText().length() > 0) {
//								edtInput4.setText("");
								edtInput4.setBackgroundResource(R.drawable.shape_ring_blue_small);
								edtInput4.setTag("");
							}
							//如果第四位输入框没有密码，那么此时删除事件应该清除第三位的密码，并把焦点交给第三位输入框
							else {
//								edtInput3.setText("");
								edtInput3.setBackgroundResource(R.drawable.shape_ring_blue_small);
								edtInput3.setTag("");
								inputIndex--;
							}
							break;
					}
					break;
					
				//帮助按钮
				case R.id.btn_help:
					//打开帮助界面
					Intent i = new Intent(getApplicationContext(), QuestionActivity2.class);
					startActivity(i);
					break;
				
				//数字按钮的事件
				default:
					//数字按钮事件处理逻辑为：
					//1、将当前持有焦点的输入框文本设置为“*”，并把用户实际输入的密码字符存在该输入框组件的tag属性中。
					//2、将焦点转移到下一位输入框。（如果当前持有焦点的输入框是第四位，那么输入后焦点仍由第四位输入框自己持有）
					switch (inputIndex) {
						case 1:
							//当用户输入第一位密码时，如果当前有正在显示的错误提示，则在这里清除掉这些错误提示。
							if (txtError.getText().length() > 0) {
								txtError.setText("");//清除错误提示
							}
							if (isError) {
								imgIcon.setBackgroundResource(R.drawable.login_icon_bg);//将图标恢复成正常状态
							}
							
//							edtInput1.setText("*");
							edtInput1.setBackgroundResource(R.drawable.shape_cycle_blue);
							edtInput1.setTag(v.getTag());
							break;
						
						case 2:
							edtInput2.setBackgroundResource(R.drawable.shape_cycle_blue);
//							edtInput2.setText("*");
							edtInput2.setTag(v.getTag());
							break;
							
						case 3:
							edtInput3.setBackgroundResource(R.drawable.shape_cycle_blue);
//							edtInput3.setText("*");
							edtInput3.setTag(v.getTag());
							break;
							
						case 4:
							edtInput4.setBackgroundResource(R.drawable.shape_cycle_blue);
//							edtInput4.setText("*");
							edtInput4.setTag(v.getTag());
							break;
					}
					
					//如果持有焦点的输入框不是第四位，则焦点后移一位
					if (inputIndex < 4) {
						inputIndex++;//焦点后移一位
					}
					else {
						//如果当前持有焦点的输入框是第四位，那么接受输入后需要延迟一定时间用来显示第四位输入框上的“*”符号，
						//然后执行相关逻辑。为了方便，登录状态下输入第四位密码后，如果密码验证正确则直接跳转到下一个Activity
						switch (state) {
							case REGISTER:
							case REGISTER_CONFIRM:
								//两种注册状态下输入第四位密码后所应该执行的逻辑放在Runnable类型的delayed中，延迟100毫秒后执行
								isDelay = true;//设置延迟标记
								handler.postDelayed(delayed, 100);
								break;
								
							case LOGIN:
							default:
								pwd = getPwd();//获取输入的密码

								try {
									SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(getApplicationContext());
									
									//将当前密码转换成md5形式，并从本地取出md5形式的密码进行对比
									String pwdMD5 = MD5Helper.getMD5Checksum(pwd.getBytes()), prefsPwd = prefs.getPassword();

									if (prefsPwd != null && prefsPwd.equals(pwdMD5)) {//如果密码正确则跳转到下一个Activity
										loadMain();
										
										errorCount = 0;
									}
									else {//如果密码错误，则更新相关可视组件的属性以提示用户输入错误,输入错误后的逻辑也放在Runnable类型的delayed中
										imgIcon.setBackgroundResource(R.drawable.login_icon_bg_err);
										isError = true;
										
										isDelay = true;
										handler.postDelayed(delayed, 100);
									}
								} catch (Exception e1) {
									JLog.d(TAG, "MD5异常=====" + e1.toString());
									e1.printStackTrace();
								}
						}
						TextView[] view = new TextView[]{edtInput1,edtInput2,edtInput3,edtInput4};
						vibrate(view);//输入第四位密码后手机震动一下
					}
			}
		}
	};
	
	/**
	 * 清除四个输入框中的内容，包括清除其所显示的“*”符号和tag属性中存储的密码字符
	 */
	private void clearEditInput() {
//		edtInput1.setText("");
		edtInput1.setBackgroundResource(R.drawable.shape_ring_blue_small);
		edtInput2.setBackgroundResource(R.drawable.shape_ring_blue_small);
		edtInput3.setBackgroundResource(R.drawable.shape_ring_blue_small);
		edtInput4.setBackgroundResource(R.drawable.shape_ring_blue_small);
//		edtInput2.setText("");
//		edtInput3.setText("");
//		edtInput4.setText("");
		edtInput1.setTag("");
		edtInput2.setTag("");
		edtInput3.setTag("");
		edtInput4.setTag("");
	}
	
	/**
	 * 让手机震动500毫秒
	 */
	private void vibrate(TextView... mEtPrice) {
		Vibrator v = (Vibrator)getSystemService(VIBRATOR_SERVICE);
		v.vibrate(500);
		
		new EditTextShakeHelper(context).shake(mEtPrice);
	}

	/**
	 * 进入主界面
	 */
	private void loadMain() {
		if (isProcessed)//此方法只能被执行一次，所以如果已经执行过此方法，则直接返回。
			return;
		isProcessed = true;//第一次执行此方法时将此变量标记为true，以防止该方法被多次执行
		
		SharedPreferencesUtil prefs = SharedPreferencesUtil.getInstance(getApplicationContext());
		
		String locVersion = SharedPreferencesUtil.getInstance(this).getLocationVersion();
		if (!TextUtils.isEmpty(locVersion) && (Integer.valueOf(this.getString(R.string.IS_NEED_INIT_VERSION)) > Integer.valueOf(locVersion))) {
			// 如果本地版本小于等于在需要重新初始化的历史本版时
			SharedPreferencesUtil.getInstance(LoginActivity.this).setIsInit(false);
		}
		// 获取当前版本号
		String curVersion = PublicUtils.getCurVersion(this);
		// 存放当前版本号到本地
		SharedPreferencesUtil.getInstance(this).setLocationVersion(curVersion);
		
		//如果已经初始化，则跳转到主界面，否则跳转到初始化界面
		Intent intent = new Intent(getApplicationContext(), prefs.getIsInit() ? HomePageActivity.class : InitActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 检查版本号是否需要更新
	 * @return 如果需要更新则返回true，否则返回false
	 */
	private boolean isNeedShowUpdateContent() {
		//是否需要更新标记,初始为不需要更新
		boolean isNeed = false;
		//获取SharedPreferencesUtil
		SharedPreferencesUtil spu = SharedPreferencesUtil.getInstance(this);
		//获取当前版本号
		String curVersion = PublicUtils.getCurVersion(this);
		//获取本地版本号
		String locVersion = spu.getLocationVersion();
		JLog.d(TAG, "curVersion " + curVersion);
		JLog.d(TAG, "locVersion " + locVersion);
		if (!TextUtils.isEmpty(locVersion)) {//本地版本号不为空
			if (Integer.valueOf(curVersion) > Integer.valueOf(locVersion)) {// 当前版本号与本地版本号不一致
				//是否需要更新标记更改为需要更新
				isNeed = true;
			}
		}

		return isNeed;
	}
}