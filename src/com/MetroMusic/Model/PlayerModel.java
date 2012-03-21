package com.MetroMusic.model;

import com.MetroMusic.data.Song;

public class PlayerModel {
	private boolean isStop;
	private boolean isLogin;
	private StringBuilder musicHistory = new StringBuilder();
	private Song 		  lastSong	   = null;

	public void appendHistory(String songId, String flag)
	{
		musicHistory.append("|");
		musicHistory.append(songId);
		musicHistory.append(":");
		musicHistory.append(flag);
	}
	
	public String getHistory()
	{
		return musicHistory.toString();
	}
	
	public Song getLastSong() {
		return lastSong;
	}

	public void setLastSong(Song lastSong) {
		this.lastSong = lastSong;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public boolean isStop() {
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}
	
	public PlayerModel()
	{
		isStop = true;
	}
	
}
