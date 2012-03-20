package com.MetroMusic.AIDL;

interface PlayerUIHelper
{
	void showWaitBar(boolean show);
	void setMusicProgressBarMax(int max);
	void updateMusicProgress(int position);
	void updateBufferingProgress(int position);
}
