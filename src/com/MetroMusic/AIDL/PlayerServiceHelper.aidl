package com.MetroMusic.AIDL;
import com.metromusic.Data.Song;
import com.MetroMusic.AIDL.DataHelper;
import com.MetroMusic.AIDL.PlayerUIHelper;
interface PlayerServiceHelper{
	void setDataHelper(DataHelper helper);
	void setPlayerUIHelper(PlayerUIHelper helper);
	void playSong(in Song song);
	void stopSong();
	void toogleSong(int toogle);
	boolean songIsLoad();
	boolean songIsPlaying();

}