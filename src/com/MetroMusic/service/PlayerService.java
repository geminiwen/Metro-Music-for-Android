package com.MetroMusic.service;

import java.io.IOException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.MetroMusic.aidl.DataHelper;
import com.MetroMusic.aidl.PlayerServiceHelper;
import com.MetroMusic.aidl.PlayerUIHelper;
import com.MetroMusic.data.Song;
import com.MetroMusic.helper.PlayerState;
import com.MetroMusic.model.LyricModel;
import com.MetroMusic.model.SentenceModel;

public class PlayerService extends Service implements OnCompletionListener,OnPreparedListener,OnBufferingUpdateListener,OnErrorListener {
	

	private MediaPlayer musicPlayer = new MediaPlayer();
	private boolean			isLoaded = false;
	private DataHelper		dataHelper;
	private Song			playSong;
	private PlayerUIHelper	playerUIHelper;
	private LyricModel		lyricModel;
	
	private PlayerServiceHelper.Stub helper = new PlayerServiceHelper.Stub(){

		@Override
		public void playSong(Song song) throws RemoteException {
			// TODO Auto-generated method stub
			try {
				playSong = song;
				isLoaded = false;
				playerUIHelper.updateMusicProgress(0);
				playerUIHelper.updateBufferingProgress(0);
				musicPlayer.reset();
				musicPlayer.setDataSource(song.getSongUrl());
				musicPlayer.prepareAsync();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException("音乐播放器加载URL失败");
			}
		}

		@Override
		public void toogleSong(int toogle) throws RemoteException {
			// TODO Auto-generated method stub
			if(toogle == PlayerState.PLAY)
			{
				if(musicPlayer.isPlaying())
				{
					musicPlayer.pause();
				}
			}
			else
			{
				if(isLoaded)
				{
					musicPlayer.start();
				}
			}
		}

		@Override
		public boolean songIsPlaying() throws RemoteException {
			// TODO Auto-generated method stub
			return musicPlayer.isPlaying();
		}

		@Override
		public void setDataHelper(DataHelper helper) throws RemoteException {
			// TODO Auto-generated method stub
			dataHelper = helper;
		}

		@Override
		public void stopSong() throws RemoteException {
			// TODO Auto-generated method stub
			musicPlayer.stop();
		}

		@Override
		public void setPlayerUIHelper(PlayerUIHelper helper)
				throws RemoteException {
			// TODO Auto-generated method stub
			playerUIHelper = helper;
		}

	};
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.i(PlayerService.class.getName(),"binded!");
		return helper;
	}

	@Override
	public void onCreate() {

		// TODO Auto-generated method stub
		Log.i(this.getClass().getName(),"Service start!");
		musicPlayer.setOnPreparedListener(this);
		musicPlayer.setOnCompletionListener(this);
		musicPlayer.setOnErrorListener(this);
		musicPlayer.setOnBufferingUpdateListener(this);
		
		IntentFilter intentFilter = new IntentFilter("lrc");
		registerReceiver(lrcReceiver,intentFilter);
		super.onCreate();
	}
	
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		MediaPlayer t = musicPlayer;
		t.reset();
		musicPlayer = null;
		t.release();
		Log.i(this.getClass().getName(),"Service Destroy!");
		unregisterReceiver(lrcReceiver);
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getName(),"unbinded!");
		return super.onUnbind(intent);
	}
                       
	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		try {
			dataHelper.nextSong();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		Log.i(PlayerService.class.getName(),"load song completed!");
		try {
			playerUIHelper.setMusicProgressBarMax(playSong.getLength());
			mp.start();
			playerUIHelper.showWaitBar(false);
			new Thread( new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (musicPlayer!=null) {
						try {
							if(musicPlayer.isPlaying())
							{
								int position = musicPlayer.getCurrentPosition()/1000;
								playerUIHelper.updateMusicProgress(position);
							}
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}
				} 
				
			}).start();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		isLoaded = true;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int arg1) {
		// TODO Auto-generated method stub
		int max = playSong.getLength();
		int value;
		value = (int) ((arg1 * 1.0 / 100.0) * max);
		try {
			playerUIHelper.updateBufferingProgress(value);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int errno1, int errno2) {
		// TODO Auto-generated method stub
		switch(errno1)
		{
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			mp.release();
			this.musicPlayer = new MediaPlayer();
			return true;
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			Log.i("MusicPlayerError",String.valueOf(errno2));
			return true;
		default:
			return false;
		}
	}

	public BroadcastReceiver lrcReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			lyricModel = (LyricModel)intent.getSerializableExtra("lyric");
			new UpdateLyricThread().start();
		}
	};
	 
	
	class UpdateLyricThread extends Thread
	{
		long time = 100; // 开始 的时间，不能为零，否则前面几句歌词没有显示出来
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while( (musicPlayer != null) && (musicPlayer.isPlaying()) ) 
			{
				//musicPlayer.getCurrentPosition();
				int index = lyricModel.getNowSentenceIndex(time);
				if(index == -1 )
				{
					continue;
				}
				SentenceModel sen = lyricModel.getSentenceList().get(index);
				long sleeptime = sen.getDuring();
				
				try {
					playerUIHelper.updateLrctime(time);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				time += sleeptime;
				if (sleeptime == -1)
					return;
				try {
					Thread.sleep(sleeptime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	

}
