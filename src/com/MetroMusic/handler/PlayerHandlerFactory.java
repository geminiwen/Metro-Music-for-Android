package com.MetroMusic.handler;

import android.os.Handler;

import com.MetroMusic.activity.MMAbstractActivity;
import com.MetroMusic.activity.PlayerActivity;
import com.MetroMusic.helper.AbstractState;

public class PlayerHandlerFactory extends AbstractHandlerFactory {
	
	protected PlayerHandlerFactory(MMAbstractActivity activity)
	{
		super(activity);
		this.handleActivity = (PlayerActivity)activity;
	}

	@Override
	public Handler getHandler(AbstractState state) {
		// TODO Auto-generated method stub
		PlayerActivity activity = (PlayerActivity)this.handleActivity;
		String cls = state.getClass().getName();
		if ( cls.equals("com.MetroMusic.helper.PlayerState"))
		{
			return activity.getStateHandler();
		}
		else if( cls.equals("com.MetroMusic.helper.SystemState") )
		{
			return activity.getSystemHandler();
		}
		else if( cls.equals("com.MetroMusic.helper.SongInfomation") )
		{
			return activity.getSongInfomationHandler();
		}
		return null;
	}
	
	
}
