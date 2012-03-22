package com.MetroMusic.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.MetroMusic.activity.R;
import com.MetroMusic.aidl.PlayerServiceHelper;
import com.MetroMusic.aidl.PlayerUIHelper;
import com.MetroMusic.controller.PlayerController;
import com.MetroMusic.helper.PlayerState;
import com.MetroMusic.helper.SongInfomation;
import com.MetroMusic.helper.SystemState;
import com.MetroMusic.service.PlayerService;
public class PlayerActivity extends Activity{

	/* UIs  */
	private Button	playButton;
	private Button	nextButton;
	private Button	settingButton;
	private Button  loveButton;
	private ProgressBar waitProgressBar;
	private ImageView	songImageView;
	private TextView 	songTitle;
	private TextView	songTime;
	private ProgressBar musicProgressBar;
	private Notification  notification;
	private NotificationManager nm;   
	
	private Bitmap		prevBitmap = null;

	
	/*  Constraint */
	private final static int MENU_HATE			= 0x01;
	private final static int MENU_ABOUT			= 0x02;
	private final static int MENU_EXIT_PROCESS	= 0x03;

	private Handler stateHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Resources res = PlayerActivity.this.getResources();
			switch(msg.what)
			{
			case PlayerState.PLAY:
			{
				playButton.setBackgroundDrawable(res.getDrawable(R.drawable.mp_pausebtn_style));
				break;
			}
			case PlayerState.PAUSE:
			{
				playButton.setBackgroundDrawable(res.getDrawable(R.drawable.mp_playbutton_style));
				break;
			}
			case PlayerState.STOP:
			{
				playButton.setBackgroundDrawable(res.getDrawable(R.drawable.mp_playbutton_style));
				break;
			}
			case PlayerState.WAIT:
			{
				waitProgressBar.setVisibility(View.VISIBLE);
				playButton.setEnabled(false);
				nextButton.setEnabled(false);
				loveButton.setEnabled(false);
				break;
			}
			case PlayerState.READY:
			{
				waitProgressBar.setVisibility(View.GONE);
				playButton.setEnabled(true);
				nextButton.setEnabled(true);
				loveButton.setEnabled(true);
				break;
			}
			case PlayerState.PROGRESS_MAX:
			{
				int max = msg.arg1;
				musicProgressBar.setMax(max);
				break;
			}
			case PlayerState.PROGRESS:
			{
				int position = msg.arg1;
				musicProgressBar.setProgress(position);
				break;
			}
			case PlayerState.BUFFERING:
			{
				int position = msg.arg1;
				musicProgressBar.setSecondaryProgress(position);
				break;
			}
			case PlayerState.LOVE:
			{
				loveButton.setBackgroundDrawable(res.getDrawable(R.drawable.mp_islovebtn_style));
				break;
			}
			case PlayerState.UNLOVE:
			{
				loveButton.setBackgroundDrawable(res.getDrawable(R.drawable.mp_lovebtn_style));
				break;
			}
			case PlayerState.ENABLE_LOVE:
			{
				loveButton.setEnabled(true);
				break;
			}
			case PlayerState.DISABLE_LOVE:
			{
				loveButton.setEnabled(false);
			}
			}
			super.handleMessage(msg);
		}
	};
	
	private Handler systemHandler =  new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what)
			{
			case SystemState.NET_WORK_ERROR:
			{
				String message = (String)msg.obj;
				Toast.makeText(getApplicationContext(), "错误:\n"+message, Toast.LENGTH_LONG).show();
				break;
			}
			}
			super.handleMessage(msg);
		}
		 
	};
	
	private Handler songInfomationHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Resources res = PlayerActivity.this.getResources();
			switch(msg.what)
			{
			case SongInfomation.IMAGE:
				if( prevBitmap != null )
					prevBitmap.recycle();
				Bitmap bitmap = (Bitmap)msg.obj;
				songImageView.setBackgroundDrawable(res.getDrawable(R.drawable.mp_songimage_style));
				songImageView.setImageBitmap(bitmap);
				prevBitmap = bitmap;
				Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mp_songimage_animation);
				songImageView.startAnimation(animation);
				break;
			case SongInfomation.TITLE:
				String title  = (String)msg.obj;
				notification.tickerText = "正在播放："+title;
				RemoteViews rv	= notification.contentView;
				rv.setTextViewText(R.id.notificationStatusText, "正在播放："+title);
				songTitle.setText(title);
				nm.cancelAll();
				nm.notify(R.string.app_name, notification);
				break;
			case SongInfomation.TIME:
				String time   = (String)msg.obj;
				songTime.setText("本曲时长："+time);
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	public Handler getStateHandler() {
		return stateHandler;
	}
	
	public Handler getSystemHandler()
	{
		return systemHandler;
	}

	public Handler getSongInfomationHandler()
	{
		return songInfomationHandler;
	}
	/* Controller */
	private PlayerController controller;
	
	/* Service Interface */
	private PlayerServiceHelper serviceHelper;
	
	/* Connection */
	private ServiceConnection mConnection = new ServiceConnection() {  
    	public void onServiceConnected(ComponentName className,  IBinder service) {  
    		serviceHelper = PlayerServiceHelper.Stub.asInterface(service);  
    		try {
				serviceHelper.setPlayerUIHelper(new UIHelper());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		controller.setPlayHelper(serviceHelper);
    	}  
    	public void onServiceDisconnected(ComponentName className) {  
    		Log.i("Tag","disconnect service");  
    		serviceHelper = null;  
    	}  
	}; 
	
	public PlayerServiceHelper getServiceHelper() {
		return serviceHelper;
	}

	public PlayerActivity()
	{
		controller = new PlayerController(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  
		FullScreen();
		setContentView(R.layout.player);
		
		this.playButton 		= (Button)findViewById(R.id.playbtn);
		this.nextButton 		= (Button)findViewById(R.id.nextbtn);
		this.settingButton		= (Button)findViewById(R.id.settingBtn);
		this.waitProgressBar 	= (ProgressBar)findViewById(R.id.waitProgressBar);
		this.songImageView		= (ImageView)findViewById(R.id.songimageview);
		this.songTime			= (TextView)findViewById(R.id.songtime);
		this.songTitle			= (TextView)findViewById(R.id.songtitle);
		this.musicProgressBar	= (ProgressBar)findViewById(R.id.musicProgressbar);
		this.loveButton			= (Button)findViewById(R.id.lovebtn);
		this.nm					= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		this.notification 		= new Notification(R.drawable.ic_launcher, "正在播放：", System.currentTimeMillis()); 
		
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		Intent i = new Intent(getApplicationContext(), PlayerActivity.class);
		i.setAction(Intent.ACTION_MAIN);  
		i.addCategory(Intent.CATEGORY_LAUNCHER);  
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		i.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);  
		PendingIntent contentIntent = PendingIntent.getActivity(
		        getApplication(),
		        R.string.app_name,
		        i,
		        PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentView = new RemoteViews(PlayerActivity.this.getPackageName(), R.layout.notification);
		notification.contentIntent = contentIntent;
		controller.changeState(PlayerState.STOP);
	}

    private void FullScreen()
    {
        getWindow().setFlags(~WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    
    public void setOnSettingClick(View.OnClickListener listener)
    {
    	settingButton.setOnClickListener(listener);
    }
    
    public void setOnPlayerClick(View.OnClickListener listener)
    {
    	playButton.setOnClickListener(listener);
    }
    
    public void setOnNextClick(View.OnClickListener listener)
    {
    	nextButton.setOnClickListener(listener);
    }
    
    public void setOnLoveClick(View.OnClickListener listener)
    {
    	loveButton.setOnClickListener(listener);
    }
    
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Intent serviceIntent = new Intent(getApplicationContext(),PlayerService.class);
		bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		PlayerActivity.this.unbindService(mConnection);
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(1, MENU_HATE, MENU_HATE, "不再播放");
		menu.add(1, MENU_ABOUT, MENU_ABOUT, "关于");
		menu.add(1, MENU_EXIT_PROCESS, MENU_EXIT_PROCESS, "退出程序");
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case MENU_HATE:
		{
			controller.neverPlay();
		}
		case MENU_EXIT_PROCESS:
		{
			new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("确认退出")
	        .setMessage("您真的要退出吗")
	        .setPositiveButton("是", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					PlayerActivity.this.finish();
					controller.closeSongManager();
					Intent intent = new Intent(getApplicationContext(),PlayerService.class);
					nm.cancelAll();
					getApplicationContext().stopService(intent);
				}
	        })
	        .setNegativeButton("否",null)
	        .show();
			break;
		}
		case MENU_ABOUT:
		{
			Intent intent = new Intent(this,AboutActivity.class);
			startActivity(intent);
		}
		default:
			return super.onMenuItemSelected(featureId, item);
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent home = new Intent(Intent.ACTION_MAIN);  
		home.addCategory(Intent.CATEGORY_HOME);   
		startActivity(home); 
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode == Activity.RESULT_OK)
		{
			Bundle bundle = data.getBundleExtra("bundle"); 
			this.controller.setUserData(bundle);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}



	/***
	 * IPC interface for activity
	 * @author Coffee
	 *
	 */
	class  UIHelper extends PlayerUIHelper.Stub
	{
		@Override
		public void showWaitBar(boolean show) throws RemoteException {
			// TODO Auto-generated method stub
			Message msg = stateHandler.obtainMessage();
			if(show)
			{
				msg.what = PlayerState.WAIT;
			}
			else
			{
				msg.what = PlayerState.READY;
			}
			stateHandler.sendMessage(msg);
		}

		@Override
		public void setMusicProgressBarMax(int max) throws RemoteException {
			// TODO Auto-generated method stub
			Message msg = stateHandler.obtainMessage();
			msg.what	= PlayerState.PROGRESS_MAX;
			msg.arg1	= max;
			stateHandler.sendMessage(msg);
		}

		@Override
		public void updateMusicProgress(int position) throws RemoteException {
			// TODO Auto-generated method stub
			Message msg = stateHandler.obtainMessage();
			msg.what	= PlayerState.PROGRESS;
			msg.arg1	= position;
			stateHandler.sendMessage(msg);
		}

		@Override
		public void updateBufferingProgress(int position)
				throws RemoteException {
			// TODO Auto-generated method stub
			Message msg = stateHandler.obtainMessage();
			msg.what	= PlayerState.BUFFERING;
			msg.arg1	= position;
			stateHandler.sendMessage(msg);
		}
		
	};
	
}
