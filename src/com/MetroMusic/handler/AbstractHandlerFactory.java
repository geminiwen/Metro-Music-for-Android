package com.MetroMusic.handler;

import android.app.Activity;
import android.os.Handler;

import com.MetroMusic.activity.MMAbstractActivity;
import com.MetroMusic.helper.AbstractState;

public abstract class AbstractHandlerFactory {
	protected Activity handleActivity;
	
	public static AbstractHandlerFactory createFactory(MMAbstractActivity obj)
	{
		String clsName = obj.getClass().getName();
		if( clsName.equals("com.MetroMusic.activity.PlayerActivity") )
		{
			return new PlayerHandlerFactory(obj);
		}
		else if ( clsName.equals("com.MetroMusic.activity.LoginActivity"))
		{
			return new LoginHandlerFactory(obj);
		}
		else
		{
			return null;
		}
	}
	
	protected AbstractHandlerFactory(MMAbstractActivity obj)
	{
		
	}
	
	public abstract Handler getHandler(AbstractState state);
}
