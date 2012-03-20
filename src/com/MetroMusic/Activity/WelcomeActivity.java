package com.MetroMusic.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.MetroMusic.Service.PlayerService;

public class WelcomeActivity extends Activity {
    /** Called when the activity is first created. */
	
	public WelcomeActivity()
	{
		super();
		//controller = new MainController(this);
	}
	
	private Handler wifiHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Resources res = WelcomeActivity.this.getResources();
			Intent serviceIntent = new Intent(getApplicationContext(),PlayerService.class);
			getApplicationContext().startService(serviceIntent);
			switch(msg.what)
			{
			case 1:
			{
				new AlertDialog.Builder(WelcomeActivity.this)
		        .setTitle("您的wifi没有连接")
		        .setIcon(res.getDrawable(R.drawable.ic_launcher))
		        .setMessage("您的wifi没有连接，将耗费大量流量，您确定继续吗？")
		        .setPositiveButton("确认退出", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						WelcomeActivity.this.finish();
						Intent intent = new Intent(getApplicationContext(),PlayerService.class);
						getApplicationContext().stopService(intent);
					}
		        })
		        .setNegativeButton("继续收听",new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(WelcomeActivity.this,PlayerActivity.class);
						WelcomeActivity.this.startActivity(intent);
						WelcomeActivity.this.finish();
					}
		        })
		        .show();
			}
				break;
			case 2:
			{
				new Thread(new Runnable(){
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Intent intent = new Intent(WelcomeActivity.this,PlayerActivity.class);
						WelcomeActivity.this.startActivity(intent);
						WelcomeActivity.this.finish();
					}
				}).start();
				
			}
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	FullScreen();
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.welcome);
        new Thread(new Runnable()
        {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				if (!wifiManager.isWifiEnabled()
						|| !isWiFiAvaliable(WelcomeActivity.this)) {
					wifiHandler.sendEmptyMessage(1);
				} else {
					wifiHandler.sendEmptyMessage(2);
				}
				// WelcomeActivity.this.handler.sendEmptyMessage(0);

			}
        	
        }
	).start();
    }
    
    private void FullScreen()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    private boolean isWiFiAvaliable(Context inContext) {  
        Context context = inContext.getApplicationContext();  
        ConnectivityManager connectivity = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        State state = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        return state == State.CONNECTED;
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		getWindow().setFlags(~WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onDestroy();
	}
    
    
    
}