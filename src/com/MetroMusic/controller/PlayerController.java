package com.MetroMusic.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import api.Api;

import com.MetroMusic.activity.PlayerActivity;
import com.MetroMusic.activity.SettingActivity;
import com.MetroMusic.aidl.DataHelper;
import com.MetroMusic.aidl.PlayerServiceHelper;
import com.MetroMusic.data.Channel;
import com.MetroMusic.data.Song;
import com.MetroMusic.helper.PlayerState;
import com.MetroMusic.helper.SongInfomation;
import com.MetroMusic.helper.SystemState;
import com.MetroMusic.model.PlayerModel;
import com.MetroMusic.model.UserModel;


public class PlayerController {
	
	private PlayerActivity			playerActivity;
	private PlayerModel				playerModel;
	private UserModel				userModel;
	private UserManager				userManager;
	private PlayerServiceHelper		serviceHelper;
	private SongManager				songManager;
	private ImageManager			imageManager;
	private SongInfomationManager songInfomationManager;
	private Context appContext;
	
	private View.OnClickListener startListener	= new OnStartClickListener();
	private View.OnClickListener pauseListener	= new OnPauseClickListener();
	private View.OnClickListener loveListener	= new OnLoveButtonClickListenerImpl();
	private View.OnClickListener unloveListener	= new OnUnLoveButtonClickListenerImpl();
	
	
	public PlayerController(PlayerActivity playerActivity)
	{
		this.playerActivity	= playerActivity;
		this.playerModel	= new PlayerModel();
	}
	
	public void changeState(int state)
	{	
		new LoadTask().execute(state);
	}
	
	class LoadTask extends AsyncTask<Integer,Void,Void>
	{
		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog = new ProgressDialog(playerActivity);
			progressDialog.setCancelable(true);
			progressDialog.setIndeterminate(true);
			progressDialog.setTitle("欢迎您");
			progressDialog.setMessage("正在初始化");
			progressDialog.show();
		}
		@Override
		protected Void doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			if( PlayerState.STOP == params[0])
			{
				playerActivity.setOnPlayerClick(startListener);
			}
			else if( PlayerState.PLAY == params[0] )
			{
				playerModel.setStop(false);
				playerActivity.setOnPlayerClick(pauseListener);
			}
			//Application Context
			appContext			= playerActivity.getApplicationContext();
			
			imageManager		= new ImageManager(appContext);
			userManager			= new UserManager(appContext);
			songManager			= new SongManager(appContext,playerModel);
			
			imageManager.setOnDownloadCompletionlistener(new ImageDownLoadCompletionImpl());
			playerActivity.setOnNextClick(new OnNextClickListener());
			playerActivity.setOnSettingClick(new OnSettingButtonClickListenerImpl());
			
			//歌曲名字、歌曲时间数据结构
			songInfomationManager = new SongInfomationManager(playerActivity.getSongInfomationHandler());
			
			userModel	= userManager.getAutoLoginUserFromDB();
			initSongManager();
			SharedPreferences sharedPrefer = playerActivity.getSharedPreferences("CHANNEL", Activity.MODE_PRIVATE);  
			int channelId	= sharedPrefer.getInt("CHANNEL", -10);
			if( channelId == -10 )
			{
				Channel channel = songManager.changeChannelByName("新歌");
				SharedPreferences.Editor editor = sharedPrefer.edit();
				editor.putInt("CHANNEL", channel.getId());
				editor.commit();
			}			
			startListener.onClick(null);
			return null;
		}
		
    	@Override
    	protected void onPostExecute(Void result) { 
    		progressDialog.dismiss();
    	}
	}
	
	public void closeSongManager()
	{
		songManager.closeManager();
	}
	
	private void initSongManager()
	{
		try {
			songManager.initializeIfNeed();
			songManager.setOnLoveOperateCompletionListener(new LoveSongOperateCompletion());  //初始化红心音乐操作的监听器
			
			if( userModel != null )
			{
				songManager.addUserCookie(userModel);
				SharedPreferences sharedPrefer = playerActivity.getSharedPreferences("CHANNEL", Activity.MODE_PRIVATE);  
				int channelId 	= sharedPrefer.getInt("CHANNEL", -10);
				if( channelId == -10 ) songManager.changeChannelByName("红心兆赫");
				else songManager.changeChannelById(channelId);
			}
			
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			systemMessageHandler(SystemState.NET_WORK_ERROR, "数据库加载错误");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			systemMessageHandler(SystemState.NET_WORK_ERROR, "网络I/O错误，歌曲管理器初始化失败");
		}
	}
	
	public void setPlayHelper(PlayerServiceHelper playHelper) {
		this.serviceHelper = playHelper;
		try {
			this.serviceHelper.setDataHelper(new MusicDataHelper());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void playerMessageHandler(int state)
	{
		Handler stateHandler = playerActivity.getStateHandler();
		Message msg = stateHandler.obtainMessage();
		msg.what 	= state;
		stateHandler.sendMessage(msg);
	}
	
	private void systemMessageHandler(int state,String message)
	{
		Handler systemHandler = playerActivity.getSystemHandler();
		Message msg = systemHandler.obtainMessage();
		msg.what 	= state;
		msg.obj		= message;
		systemHandler.sendMessage(msg);
	}
	
	//使用Handler切换红心按钮是否呈红色
	public void songIsLoved(boolean isLove)
	{
		Handler stateHandler = playerActivity.getStateHandler();
		if(isLove)
		{
			playerActivity.setOnLoveClick(unloveListener);
			stateHandler.sendEmptyMessage(PlayerState.LOVE);
		}
		else
		{
			playerActivity.setOnLoveClick(loveListener);
			stateHandler.sendEmptyMessage(PlayerState.UNLOVE);
		}
	}
	
	//使用Handler通知红心按钮是否可用
	public void toogleLoveButton(boolean enabled)
	{
		Handler stateHandler = playerActivity.getStateHandler();
		if(enabled)
		{
			stateHandler.sendEmptyMessage(PlayerState.ENABLE_LOVE);
		}
		else
		{
			stateHandler.sendEmptyMessage(PlayerState.DISABLE_LOVE);
		}
	}
	
	private synchronized void loadNewSong()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run() {
				// TODO Auto-generated method stub
					Song song = null;
					playerMessageHandler(PlayerState.WAIT);
					try {
						song = songManager.loadNewSong();
						songIsLoved(song.getIsLike() > 0);
						imageManager.getImageFromUrlAsync(URI.create(song.getPicture()));
						songInfomationManager.setSong(song);
						songInfomationManager.invokeUpdateUI();
						serviceHelper.playSong(song);
						playerMessageHandler(PlayerState.PLAY);
						playerActivity.setOnPlayerClick(pauseListener);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						systemMessageHandler(SystemState.NET_WORK_ERROR,"网络访问错误");
						Log.e(PlayerController.class.getName(), e.getMessage());
					}catch(RuntimeException e)
					{
						systemMessageHandler(SystemState.NET_WORK_ERROR,e.getMessage());
					}
				}
		}).start();
	}
	
	public void setUserData(Bundle bundle)
	{
		this.userModel			= (UserModel) bundle.get("loginuser");
		boolean needLoadNewSong = (Boolean)bundle.get("loadnewsong");
		int loginUserFlag		= bundle.getInt("loginuserflag");
		int	changeChannel		= bundle.getInt("changechannel", -10);
		if( changeChannel != -10 ) songManager.changeChannelById(changeChannel);
		if( loginUserFlag > 0 ) this.songManager.addUserCookie(userModel);	//当登陆用户后，需要保存用户中的ck参数到cookie中，这样才能被豆瓣服务器识别为登陆
		else if ( loginUserFlag < 0 ) 
		{
			songManager.changeChannelByName("新歌");
			this.userModel	= null;
		}
		
		if( needLoadNewSong )
		{
			try {
				serviceHelper.stopSong();
				songManager.setOperator(Api.OP_SKIP);
				playerModel.appendHistory(playerModel.getLastSong().getSid(), Api.OP_SKIP);
				playerModel.setStop(true);
				playerMessageHandler(PlayerState.STOP);
				playerActivity.setOnPlayerClick(startListener);
				loadNewSong();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class OnStartClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(playerModel.isStop())
			{
				loadNewSong();
			}
			else
			{
				try {
					serviceHelper.toogleSong(PlayerState.PAUSE);
					playerMessageHandler(PlayerState.PLAY);
					playerActivity.setOnPlayerClick(pauseListener);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	class OnPauseClickListener implements View.OnClickListener
	{

		@Override
		public void onClick(View v) { 
			// TODO Auto-generated method stub
			try {
				serviceHelper.toogleSong(PlayerState.PLAY);
				playerMessageHandler(PlayerState.PAUSE);
				playerActivity.setOnPlayerClick(startListener);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class OnNextClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						serviceHelper.stopSong();
						songManager.setOperator(Api.OP_SKIP);
						playerModel.appendHistory(playerModel.getLastSong().getSid(), Api.OP_SKIP);
						playerModel.setStop(true);
						playerMessageHandler(PlayerState.STOP);
						playerActivity.setOnPlayerClick(startListener);
						loadNewSong();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}).start();
			
		}
		
	}
	
	class OnSettingButtonClickListenerImpl implements View.OnClickListener
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(playerActivity,SettingActivity.class);
			Bundle	bundle	= new Bundle();
			if(userModel != null)
			{
				bundle.putSerializable("loginuser", userModel);
			}
			ChannelManager channelManager = songManager.getChannelManager();
			bundle.putSerializable("channelmanager", channelManager);
			intent.putExtra("bundle", bundle);
			playerActivity.startActivityForResult(intent, 1);
		}
	}
	
	class OnLoveButtonClickListenerImpl implements View.OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			toogleLoveButton(false);
			songManager.loveSongAsync(true);
		}
		
	}
	
	class OnUnLoveButtonClickListenerImpl implements View.OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			songManager.loveSongAsync(false);
		}
		
	}

	class MusicDataHelper extends DataHelper.Stub
	{
		@Override
		public void nextSong() throws RemoteException {
			// TODO Auto-generated method stub
			songManager.setOperator(Api.OP_END);
			playerModel.appendHistory(playerModel.getLastSong().getSid(), Api.OP_END);
			playerModel.setStop(true);
			playerMessageHandler(PlayerState.STOP);
			playerActivity.setOnPlayerClick(startListener);
			loadNewSong();
		}
	}
	
	class ImageDownLoadCompletionImpl implements ImageManager.OnDownloadCompletionListener
	{
		@Override
		public void onCompletion(InputStream is) {
			// TODO Auto-generated method stub 
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			Message msg   = playerActivity.getSongInfomationHandler().obtainMessage();
			msg.what 	  = SongInfomation.IMAGE;
			msg.obj 	  = bitmap;
			playerActivity.getSongInfomationHandler().sendMessage(msg);
		}
	}
	
	class LoveSongOperateCompletion implements SongManager.OnLoveOperateCompletionListener
	{
		@Override
		public void OnCompletion(boolean isloved) {
			// TODO Auto-generated method stub
			toogleLoveButton(true);
			songIsLoved(isloved);
		}
		
	}
	
}
