package com.MetroMusic.aidl;

interface PlayerUIHelper
{
	void showWaitBar(boolean show);
	void setMusicProgressBarMax(int max);
	void updateMusicProgress(int position);
	void updateBufferingProgress(int position);
}
