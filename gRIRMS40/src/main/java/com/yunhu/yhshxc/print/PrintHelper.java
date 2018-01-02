package com.yunhu.yhshxc.print;

import gcg.org.debug.ELog;

import java.util.Set;

import org.json.JSONArray;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.yunhu.yhshxc.R;
import com.yunhu.yhshxc.print.device.BM9000;
import com.yunhu.yhshxc.print.templet.Element;
import com.yunhu.yhshxc.print.templet.JSONTemplet;
import com.yunhu.yhshxc.print.templet.Templet;
import com.yunhu.yhshxc.utility.PublicUtils;

public class PrintHelper {
	private static final String TAG = "PrintHelper";

	public static final String DEVICE_BM9000 = "BM9000";

	public static final String TEMPLET_JSON = "TEMPLET_JSON";
	public static final String TEMPLET_DEFAULT = TEMPLET_JSON;

	private static PrintHelper instance;

	private Context context;

	private Dialog dialogSearch;
	private ViewGroup list;
	private ProgressBar progress;
	private Button btnCancel, btnSearch;

	protected BluetoothAdapter adapter;

	protected BluetoothPrinter printer;
	protected Templet templet;
	protected BluetoothReceiver receiver;
	
	protected BluetoothDevice connectedDevice;

	public PrintHelper(Context context, String device) {
		this.context = context;

		if (DEVICE_BM9000.equals(device)) {
			printer = new BM9000();
		}

		receiver = new BluetoothReceiver(this);
		register();

		adapter = BluetoothAdapter.getDefaultAdapter();
		printer.init(context);
	}

	public void register() {
		IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		context.registerReceiver(receiver, intentFilter);
		intentFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
		context.registerReceiver(receiver, intentFilter);
		intentFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		context.registerReceiver(receiver, intentFilter);
		intentFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		context.registerReceiver(receiver, intentFilter);
		intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		context.registerReceiver(receiver, intentFilter);
		intentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		context.registerReceiver(receiver, intentFilter);
		intentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		context.registerReceiver(receiver, intentFilter);
	}

	public void release() {
		ELog.d("");
		if (adapter.isDiscovering()) {
			adapter.cancelDiscovery();
		}
		if (adapter.isEnabled()) {
			adapter.disable();
		}
		context.unregisterReceiver(receiver);
		
		printer.release();
	}

	public void setStatusListener(BluetoothStatusListener statusListener) {
		receiver.setStatusListener(statusListener);
	}

	public void setTemplet(String templetName, Object templetSource, Templet.DataSource dataSource) throws Exception {
		if (TEMPLET_JSON.equals(templetName)) {
			this.templet = new JSONTemplet(printer, (JSONArray)templetSource);
		}
		this.templet.setDataSource(dataSource);
	}

	public void initDialog(Context context) {
		if (btnCancel != null) {
			btnCancel.setOnClickListener(null);
		}
		if (btnSearch != null) {
			btnSearch.setOnClickListener(null);
		}
		
//		if (dialogSearch == null) {
			View layout = View.inflate(context, R.layout.bluetooth_search_list, null);
			list = (ViewGroup) layout.findViewById(R.id.list);
			btnCancel = (Button) layout.findViewById(R.id.cancel);
			btnCancel.setOnClickListener(btnClickListener);
			btnSearch = (Button) layout.findViewById(R.id.search);
			btnSearch.setOnClickListener(btnClickListener);
			
			progress = (ProgressBar)layout.findViewById(R.id.progress);

			dialogSearch = new Dialog(context);
			dialogSearch.setContentView(layout);
			layout.getRootView().setBackgroundColor(Color.TRANSPARENT);
//		}
	}
	
	public void setProgressVisibility(boolean isShow) {
		progress.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}
	
	public void dismissDialog() {
		adapter.cancelDiscovery();
		dialogSearch.dismiss();
	}
	
	public int status() {
		return printer.getStatus();
	}

	/**
	 * 解除所有设备绑定
	 */
	public void removeAllBond() {
		if (adapter == null)
			return;

		Set<BluetoothDevice> devices = adapter.getBondedDevices();
		for (BluetoothDevice device : devices) {
			try {
//				ClsUtils.removeBond(device.getClass(), device);
				ELog.i("解除绑定:" + device.getName() + "/" + device.getAddress());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void print() {
		if (!templet.isEmpty()) {
			ELog.i("开始打印:" + printer.getStatus());
			try {
				templet.print();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				printer.disconnect();
			}
		}
	}
	
	public void printOrder3() {
		if (!templet.isEmpty()) {
			ELog.i("开始打印:" + printer.getStatus());
			try {
				templet.printOrder3();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				printer.disconnect();
			}
		}
	}
	
	public void printElement(Element element) {
		templet.printElement(element);
	}
	
	public void printNewLine() {
		printer.printNewLine();
	}
	
	public void printDivider() {
		printer.printDivider();
	}

	public void connect() {
		list.removeAllViews();
		dialogSearch.show();
		if (!adapter.isEnabled()) {
			adapter.enable();
		}
		if (adapter.isDiscovering()) {
			adapter.cancelDiscovery();
		}
		adapter.startDiscovery();
	}

	protected void addItemView(BluetoothDevice device) {
		int padding = (int)(10 * context.getResources().getDisplayMetrics().density);
		Button btn = new Button(context);
		btn.setBackgroundResource(R.drawable.bt_search_item);
		btn.setGravity(Gravity.LEFT);
		btn.setPadding(padding, padding, padding, padding);
		btn.setText(PublicUtils.getResourceString(context,R.string.toast_string23) + device.getName());
		btn.setTag(device);
		btn.setOnClickListener(btnClickListener);
		list.addView(btn);
	}

	private final View.OnClickListener btnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.cancel) {
				adapter.cancelDiscovery();
				dialogSearch.dismiss();
			}
			else if (v.getId() == R.id.search) {
				list.removeAllViews();
				adapter.cancelDiscovery();
				adapter.startDiscovery();
			}
			else {
				ELog.i("Status:" + printer.getStatus());
				if (printer.getStatus() == BluetoothPrinter.STATUS_CONNECTED) {
//					print();
				}
				else if (printer.getStatus() == BluetoothPrinter.STATUS_DISCONNECTED) {
					BluetoothDevice device = (BluetoothDevice) v.getTag();
					printer.connect(device);
				}
				else if (printer.getStatus() == BluetoothPrinter.STATUS_CONNECTING) {

				}
				else if (printer.getStatus() == BluetoothPrinter.STATUS_DISCONNECTING) {

				}
			}
		}
	};
}
