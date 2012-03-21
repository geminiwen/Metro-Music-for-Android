package com.MetroMusic.aidl;
import com.metromusic.data.Song;
import com.MetroMusic.aidl.DataHelper;
import com.MetroMusic.aidl.PlayerUIHelper;
interface PlayerServiceHelper{
	void setDataHelper(DataHelper helper);
	void setPlayerUIHelper(PlayerUIHelper helper);
	void playSong(in Song song);
	void stopSong();
	void toogleSong(int toogle);
	boolean songIsPlaying();

}