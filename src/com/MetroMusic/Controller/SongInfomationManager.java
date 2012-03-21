package com.MetroMusic.controller;

import android.os.Handler;
import android.os.Message;

import com.MetroMusic.data.Song;
import com.MetroMusic.helper.SongInfomation;

public class SongInfomationManager {
	private Handler uiHandler;
	private Song    song;
	
	public SongInfomationManager(Handler handler)
	{
		uiHandler = handler;
	}
	
	public SongInfomationManager(Handler handler,Song song)
	{
		this.uiHandler = handler;
		this.song 	   = song;
	}

	public void setSong(Song song) {
		this.song = song;
	}
	
	public void invokeUpdateUI()
	{
		Message msg = new Message();
		msg.what	= SongInfomation.TIME;
		int length  = song.getLength();
		String format  = String.format("%02d:%02d", length/60,length%60);
		String  strLeng = format;
		msg.obj		= strLeng;
		this.uiHandler.sendMessage(msg);
		msg			= new Message();
		msg.what	= SongInfomation.TITLE;
		String title = song.getTitle();
		String artist = song.getArtist();
		String str  = title + "  -  " + artist;
		msg.obj		= str;
		this.uiHandler.sendMessage(msg);
		
	}
}
