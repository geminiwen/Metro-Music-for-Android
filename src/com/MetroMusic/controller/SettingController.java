package com.MetroMusic.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.MetroMusic.activity.SettingActivity;
import com.MetroMusic.data.Channel;
import com.MetroMusic.model.UserModel;

public class SettingController {
	
	private SettingActivity activity;
	private UserModel		loginUser;
	private	ChannelManager	channelManager;
	private Bundle			bundle;
	private int				newLoginUser = 0;
	public final static int USER_NEW_LOGIN = 1;
	public final static int USER_NO_CHANGE	= 0;
	public final static int USER_LOGOUT	= -1;
	
	public SettingController(SettingActivity activity)
	{
		this.activity	= activity;
	}
	
	public void setData(Bundle bundle)
	{
		if( bundle != null )
		{
			this.bundle	=	bundle;
			this.loginUser	= (UserModel)bundle.get("loginuser");
			if( loginUser != null )
			{
				this.activity.setUsername(loginUser.getNickname());
			}
			if( this.channelManager == null )
			{
				this.channelManager = (ChannelManager)bundle.get("channelmanager");
				this.activity.initChannel(channelManager);
			}
			
		}
	}
	
	public void logOut()
	{
		this.loginUser = null;
		this.bundle.remove("loginuser");
		this.newLoginUser	= USER_LOGOUT;
		SharedPreferences sharedPrefer = activity.getSharedPreferences("CHANNEL", Activity.MODE_PRIVATE);  
		int channelId				   = sharedPrefer.getInt("CHANNEL", -10);
		if(channelId < 1)
		{
			SharedPreferences.Editor editor = sharedPrefer.edit();
			Channel channel = channelManager.getChannelByName("华语");
			editor.putInt("CHANNEL", channel.getId());
			editor.commit();
			onActivityStop(true);
		}
		else
		{
			onActivityStop(false);
		}
	}
	
	public void changeChannelPrefer(final String channelName)
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Channel channel = channelManager.getChannelByName(channelName);
				SharedPreferences sharedPrefer = activity.getSharedPreferences("CHANNEL", Activity.MODE_PRIVATE);  
				int channelId = sharedPrefer.getInt("channel", -10);
				if( channelId == channel.getId() )
				{
					onActivityStop(true);
					return;
				}
				else
				{
					bundle.putInt("changechannel", channel.getId());
					SharedPreferences.Editor editor = sharedPrefer.edit();
					editor.putInt("CHANNEL", channel.getId());
					editor.commit();
					onActivityStop(true);
				}
				
			}
			
		}).start();
	}
	
	public void onActivityStop(boolean loadNewSong)
	{
		if(bundle == null)bundle = new Bundle();
		bundle.putBoolean("loadnewsong", loadNewSong);
		bundle.putInt("loginuserflag", newLoginUser);
		Intent intent = new Intent();
		intent.putExtra("bundle", bundle);
		if (activity.getParent() == null) {
			activity.setResult(Activity.RESULT_OK, intent);
	    } else {
	    	activity.getParent().setResult(Activity.RESULT_OK, intent);
	    }
		activity.finish();
	}
	
	public void loadNewLoginUser(int loginUserFlag)
	{
		newLoginUser = loginUserFlag;
	}
	
}
