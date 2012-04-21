package com.MetroMusic.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import api.Api;

import com.MetroMusic.activity.PlayerActivity;
import com.MetroMusic.activity.SettingActivity;
import com.MetroMusic.aidl.DataHelper;
import com.MetroMusic.aidl.PlayerServiceHelper;
import com.MetroMusic.data.Channel;
import com.MetroMusic.data.Song;
import com.MetroMusic.helper.AbstractState;
import com.MetroMusic.helper.PlayerState;
import com.MetroMusic.helper.SongInfomation;
import com.MetroMusic.helper.SystemState;
import com.MetroMusic.model.LyricModel;
import com.MetroMusic.model.PlayerModel;
import com.MetroMusic.model.SentenceModel;
import com.MetroMusic.model.UserModel;


public class PlayerController extends MMAbstractController{
	
	private PlayerModel				playerModel;
	private UserModel				userModel;
	private UserManager				userManager;
	private PlayerServiceHelper		serviceHelper;
	private SongManager				songManager;
	private ImageManager			imageManager;
	private SongInfomationManager	songInfomationManager;
	
	public PlayerController(PlayerActivity activity)
	{
		super(activity);
	}
	
	private void initSongManager()
	{
		try {
			songManager.initializeIfNeed();
			songManager.setOnLoveOperateCompletionListener(new LoveSongOperateCompletion());  //初始化红心音乐操作的监听器
			
			if( userModel != null )
			{
				PlayerActivity playerActivity  = (PlayerActivity)this.activity;
				SharedPreferences sharedPrefer = playerActivity.getSharedPreferences("CHANNEL", Activity.MODE_PRIVATE);  
				int channelId 	= sharedPrefer.getInt("CHANNEL", -10);
				if( channelId == -10 ) songManager.changeChannelByName("红心兆赫");
				else songManager.changeChannelById(channelId);
			}
			
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AbstractState state = new SystemState(SystemState.NET_WORK_ERROR);
			notify(state,"数据库加载错误");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AbstractState state = new SystemState(SystemState.NET_WORK_ERROR);
			notify(state, "网络I/O错误，歌曲管理器初始化失败");
		}
	}
	
	/* Invoked by activities */
	public void close()
	{
		songManager.clean();
	}
	
	public void neverPlay()
	{
		try {
			serviceHelper.stopSong();
			songManager.setOperator(Api.OP_HATE);
			playerModel.appendHistory(playerModel.getLastSong().getSid(), Api.OP_HATE);
			playerModel.setStop(true);
			AbstractState state = new PlayerState( PlayerState.STOP );
			notify( state, null );
			loadNewSong();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/* ********************* */
	
	public void setPlayHelper(PlayerServiceHelper playHelper) {
		this.serviceHelper = playHelper;
		try {
			this.serviceHelper.setDataHelper(new MusicDataHelper());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void loadNewSong()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run() {
				// TODO Auto-generated method stub
					Song song = null;
					AbstractState state = new PlayerState(PlayerState.WAIT);
					PlayerController.this.notify(state,null);
					try {
						song = songManager.loadNewSong();
						
						if( song.getIsLike() > 0 )
						{
							state = new PlayerState(PlayerState.LOVE);
						} else state = new PlayerState(PlayerState.UNLOVE);
						
						PlayerController.this.notify(state,null);
						
						imageManager.getImageFromUrlAsync(URI.create(song.getPicture()));
						
						songInfomationManager.setSong(song);
						songInfomationManager.getSongInformation();
						
						
						serviceHelper.playSong(song);
						state = new PlayerState(PlayerState.PLAY);
						PlayerController.this.notify(state,null);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						state = new SystemState(SystemState.NET_WORK_ERROR);
						PlayerController.this.notify(state,"网络错误");
						Log.e(PlayerController.class.getName(), e.getMessage());
					}catch(RuntimeException e)
					{
						state = new SystemState(SystemState.NET_WORK_ERROR);
						PlayerController.this.notify(state,e.getMessage());
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
		if ( loginUserFlag < 0 ) 
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
				AbstractState state = new PlayerState(PlayerState.STOP);
				notify(state,null);
				loadNewSong();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void onStart()
	{
		if(playerModel.isStop())
		{
			loadNewSong();
		}
		else
		{
			try {
				serviceHelper.toogleSong(PlayerState.PAUSE);
				AbstractState state = new PlayerState(PlayerState.PLAY);
				notify(state,null);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void onPause()
	{
		try {
			serviceHelper.toogleSong(PlayerState.PLAY);
			AbstractState state = new PlayerState(PlayerState.PAUSE);
			notify(state,null);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onNext()
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					serviceHelper.stopSong();
					songManager.setOperator(Api.OP_SKIP);
					playerModel.appendHistory(playerModel.getLastSong().getSid(), Api.OP_SKIP);
					playerModel.setStop(true);
					AbstractState state = new PlayerState(PlayerState.STOP);
					PlayerController.this.notify(state,null);
					loadNewSong();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
	}
	
	public void onSetting()
	{
		PlayerActivity playerActivity  = (PlayerActivity)this.activity;
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
	
	public void onLove()
	{
		AbstractState state = new PlayerState(PlayerState.DISABLE_LOVE);
		notify(state,null);
		songManager.loveSongAsync(true);
	}
	
	public void onUnLove()
	{
		AbstractState state = new PlayerState(PlayerState.DISABLE_LOVE);
		notify(state,null);
		songManager.loveSongAsync(false);
	}
	
	class MusicDataHelper extends DataHelper.Stub
	{
		@Override
		public void nextSong() throws RemoteException {
			// TODO Auto-generated method stub
			songManager.setOperator(Api.OP_END);
			playerModel.appendHistory(playerModel.getLastSong().getSid(), Api.OP_END);
			playerModel.setStop(true);
			AbstractState state = new PlayerState(PlayerState.STOP);
			PlayerController.this.notify(state,null);
			loadNewSong();
		}
	}
	
	class ImageDownLoadCompletionImpl implements ImageManager.OnDownloadCompletionListener
	{
		@Override
		public void onCompletion(InputStream is) {
			// TODO Auto-generated method stub 			
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			AbstractState state = new SongInfomation(SongInfomation.IMAGE);
			PlayerController.this.notify(state,bitmap);
		}
	}
	
	class LoveSongOperateCompletion implements SongManager.OnLoveOperateCompletionListener
	{
		@Override
		public void OnCompletion(boolean isloved) {
			// TODO Auto-generated method stub
			AbstractState state = new PlayerState(PlayerState.ENABLE_LOVE);
			PlayerController.this.notify(state,null);
			if(isloved) state = new PlayerState(PlayerState.LOVE);
			else	state = new PlayerState(PlayerState.UNLOVE);
			PlayerController.this.notify(state,null);
		}
		
	}
	
	class LyricListener implements SongInfomationManager.OnLrcListener
	{

		@Override
		public void onDownloadCompletion(String result) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPareseCompletion(List<SentenceModel> sentenceList) {
			// TODO Auto-generated method stub
			LyricModel model = new LyricModel(sentenceList);
			Intent intent=new Intent("lrc");
			intent.putExtra("lyric", model);
			activity.sendBroadcast(intent);
		}
		
	}

	@Override
	public void onBind() {
		// TODO Auto-generated method stub
		new AsyncTask<Void,Void,Void>()
		{
			private ProgressDialog progressDialog;
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				PlayerActivity playerActivity  = (PlayerActivity)activity;
				progressDialog = new ProgressDialog(playerActivity);
				progressDialog.setCancelable(true);
				progressDialog.setIndeterminate(true);
				progressDialog.setTitle("欢迎您");
				progressDialog.setMessage("正在初始化");
				progressDialog.show();
			}
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				//Application Context
				PlayerActivity playerActivity  = (PlayerActivity)activity;

				playerModel			= new PlayerModel();
				imageManager		= new ImageManager(appContext);
				userManager			= new UserManager(appContext);
				songManager			= new SongManager(appContext,playerModel);
				
				
				//Add listeners
				imageManager.setOnDownloadCompletionlistener(new ImageDownLoadCompletionImpl());
				
				//歌曲名字、歌曲时间数据结构
				songInfomationManager = new SongInfomationManager(playerActivity.getSongInfomationHandler(),activity);
				songInfomationManager.setLrcListener(new LyricListener());
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
				onStart();
				return null;
			}
			
	    	@Override
	    	protected void onPostExecute(Void result) { 
	    		progressDialog.dismiss();
	    	}
		}.execute();
	}
	
}
