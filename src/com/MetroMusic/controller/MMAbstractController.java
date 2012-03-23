package com.MetroMusic.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.MetroMusic.activity.MMAbstractActivity;
import com.MetroMusic.handler.AbstractHandlerFactory;
import com.MetroMusic.helper.AbstractState;

public abstract class MMAbstractController {
	
	protected MMAbstractActivity activity;
	protected Context			 appContext;
	
	public MMAbstractController(MMAbstractActivity activity)
	{
		this.activity	= activity;
		this.appContext	= activity.getApplicationContext();
	}
	
	public void notify(AbstractState state,Object obj)
	{
		Handler	 handler = AbstractHandlerFactory.createFactory(activity).getHandler(state);
		Message  msg	 = handler.obtainMessage();
		state.assembMessage(msg, obj);
		handler.sendMessage(msg);
	}
	
	public abstract void onBind();
}
