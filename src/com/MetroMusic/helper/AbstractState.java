package com.MetroMusic.helper;

import android.os.Message;

public abstract class AbstractState {
	protected int mState;
	
	public AbstractState(int state)
	{
		this.mState	= state;
	}
	
	public void assembMessage(Message msg,Object obj)
	{
		msg.what = mState;
		msg.obj  = obj;
	}
}
