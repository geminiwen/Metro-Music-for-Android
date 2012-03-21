package com.MetroMusic.service;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
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

public class PlayerService extends Service implements OnCompletionListener,OnPreparedListener,OnBufferingUpdateListener,OnErrorListener {
	
	private MediaPlayer musicPlayer = new MediaPlayer();
	private boolean isLoad = false;
	private DataHelper dataHelper;
	private Song 	playSong;
	private PlayerUIHelper playerUIHelper;
	
	private PlayerServiceHelper.Stub helper = new PlayerServiceHelper.Stub(){

		@Override
		public void playSong(Song song) throws RemoteException {
			// TODO Auto-generated method stub
			try {
				playSong = song;
				isLoad = false;
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
				if(isLoad)
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
		Log.i(PlayerService.class.getName(),"Service start!");
		musicPlayer.setOnPreparedListener(this);
		musicPlayer.setOnCompletionListener(this);
		musicPlayer.setOnErrorListener(this);
		musicPlayer.setOnBufferingUpdateListener(this);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		MediaPlayer t = musicPlayer;
		t.reset();
		musicPlayer = null;
		t.release();
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
		
		isLoad = true;
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

	
	
	

}
