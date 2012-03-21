package com.MetroMusic.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.MetroMusic.activity.R;
import com.MetroMusic.controller.ChannelManager;
import com.MetroMusic.controller.SettingController;
import com.MetroMusic.data.Channel;
import com.MetroMusic.preference.LoginPreference;
import com.MetroMusic.preference.RadioPreference;

public class SettingActivity extends PreferenceActivity implements OnPreferenceClickListener {

	private SettingController	controller;
	private LoginPreference		loginPreference;
	private PreferenceCategory	privateChannelCategory;
	private PreferenceCategory	publicChannelCategory;
	private List<RadioPreference>	channelPreferencs;
	private SharedPreferences sharedPrefer;
	
	public SettingActivity() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);
		controller = new SettingController(this);
		sharedPrefer = getSharedPreferences("CHANNEL", Activity.MODE_PRIVATE);  
		setupViews();
		initUserData(this.getIntent());
	}
	
	private void setupViews()
	{
		loginPreference			= (LoginPreference)this.findPreference("login_prefer");
		privateChannelCategory	= (PreferenceCategory)this.findPreference("private_channel");
		publicChannelCategory	= (PreferenceCategory)this.findPreference("public_channel");
		
		loginPreference.setParent(this);
	}
	
	
	public void initChannel(ChannelManager channelManager)
	{
		channelPreferencs = new ArrayList<RadioPreference>();
		int channelId	  = sharedPrefer.getInt("CHANNEL", -10);
		for( Channel channel: channelManager.getChannelList() )
		{
			RadioPreference prefer = new RadioPreference(this.getApplicationContext());
			prefer.setLayoutResource(R.layout.prefer_channel);
			String channelName = channel.getName();
			if( channel.getId() == channelId )
			{
				prefer.check();
			}
			prefer.setTitle(channel.getName());
			prefer.setOnPreferenceClickListener(this);
			if( channelName.equals("私人兆赫") || channelName.equals("红心兆赫") )
			{
				privateChannelCategory.addPreference(prefer);
			}
			else
			{
				publicChannelCategory.addPreference(prefer);
			}
			channelPreferencs.add(prefer);
		}
	}
	
	private void initUserData(Intent intent)
	{
		Bundle dataBundle =  intent.getBundleExtra("bundle");
		controller.setData(dataBundle);
	}
	
	//Called by it's child class
	public void logOut()
	{
		this.controller.logOut();
		loginPreference.setUsername("未登录");
		this.privateChannelCategory.setEnabled(false);
		this.privateChannelCategory.setTitle("未登录");
	}
	
	//Called by it's controller
	public void setUsername(String nickname) {
		// TODO Auto-generated method stub
		loginPreference.setUsername(nickname);
		this.privateChannelCategory.setEnabled(true);
		this.privateChannelCategory.setTitle(nickname+" 's 私人兆赫");
	}
	
	//Return from Login Activity
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		initUserData(intent);
		controller.loadNewLoginUser(SettingController.USER_NEW_LOGIN);
		super.onNewIntent(intent);
	}
	
	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		for( RadioPreference pref : channelPreferencs )
		{
			if ( pref == preference ) 
			{
				controller.changeChannelPrefer(preference.getTitle().toString());
				continue;
			}
			pref.cancleCheck();
		}
		return true;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		controller.onActivityStop(false );
	}
}
