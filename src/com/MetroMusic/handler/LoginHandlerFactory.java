package com.MetroMusic.handler;

import android.os.Handler;

import com.MetroMusic.activity.LoginActivity;
import com.MetroMusic.activity.MMAbstractActivity;
import com.MetroMusic.activity.PlayerActivity;
import com.MetroMusic.helper.AbstractState;

public class LoginHandlerFactory extends AbstractHandlerFactory {
	
	protected LoginHandlerFactory(MMAbstractActivity obj) {
		super(obj);
		// TODO Auto-generated constructor stub
		this.handleActivity = (LoginActivity)obj;
	}

	@Override
	public Handler getHandler(AbstractState state) {
		// TODO Auto-generated method stub
		LoginActivity activity = (LoginActivity)this.handleActivity;
		String cls = state.getClass().getName();
		if ( cls.equals("com.MetroMusic.helper.LoginState"))
		{
			return activity.getUIHandler();
		}
		return null;
	}
}
