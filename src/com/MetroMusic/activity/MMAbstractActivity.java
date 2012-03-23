package com.MetroMusic.activity;

import android.app.Activity;
import com.MetroMusic.controller.MMAbstractController;

public abstract class MMAbstractActivity extends Activity{

	protected MMAbstractController controller;

	/***
	 * WARNING:It should be load in Subclass because it bind the controller and set the listeners!!
	 */
	protected void setupViews()
	{
		if( null != controller )controller.onBind();
		setListeners();
	}
	
	protected abstract void setListeners();
	
	/***
	 * Initialize controller;
	 * @param c the Controller of the View
	 */
	protected void setController(MMAbstractController c)
	{
		this.controller = c;
	}
	
}
