package com.MetroMusic.controller;

import java.io.Serializable;

import android.content.Intent;
import android.view.View;

import com.MetroMusic.activity.LoginActivity;
import com.MetroMusic.preference.LoginPreference;

public class LoginPreferenceController implements Serializable {
	private LoginPreference mainPreference;
	
	public LoginPreferenceController(LoginPreference mainPreference)
	{
		this.mainPreference	= mainPreference;
	}
	
	public void Bind()
	{
		mainPreference.setOnLoginClickListener(new LoginClickImpl());
	}
	
	class LoginClickImpl implements View.OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(mainPreference.getContext(),LoginActivity.class);
			mainPreference.getContext().startActivity(intent);
		}
		
	}

}
