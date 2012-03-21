package com.MetroMusic.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.MetroMusic.activity.R;
import com.MetroMusic.activity.SettingActivity;
import com.MetroMusic.controller.LoginPreferenceController;

public class LoginPreference extends Preference  {

	private Button		loginBtn;
	private Button		logoutBtn;
	private LinearLayout logoutLayout;
	private TextView	loginUsernameTextView;
	private String 		username;
	private LoginPreferenceController controller;
	private SettingActivity parent;
	
	public LoginPreference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initController();
	}
	
	public LoginPreference(Context context,AttributeSet attrs)
	{
		super(context,attrs);
		initController();
	}
	
	
	private void initController()
	{
		controller = new LoginPreferenceController(this);
	}

	@Override
	protected void onBindView(View view) {
		// TODO Auto-generated method stub
		loginBtn				= (Button)view.findViewById(R.id.perfer_loginBtn);
		loginUsernameTextView	= (TextView)view.findViewById(R.id.prefer_loginUsername);
		logoutLayout			= (LinearLayout)view.findViewById(R.id.prefer_logoutlayout);
		logoutBtn				= (Button)view.findViewById(R.id.prefer_logoutBtn);
		if( username != null)
		{
			this.loginUsernameTextView.setText(username);
			this.logoutLayout.setVisibility(View.VISIBLE);
			this.loginBtn.setVisibility(View.GONE);
		}
		controller.Bind();
		super.onBindView(view);
	}
	
	public void setOnLogoutClickListener(OnClickListener l)
	{
		logoutBtn.setOnClickListener(l);
	}

	public void setOnLoginClickListener(OnClickListener l) {
		loginBtn.setOnClickListener(l);
	}
	
	public void setUsername(String username)
	{
		if( username.equals("未登录") )
		{
			this.username = null;
			this.loginUsernameTextView.setText("未登录");
			this.logoutLayout.setVisibility(View.GONE);
			this.loginBtn.setVisibility(View.VISIBLE);
			
		}
		this.username	= username;
	}
	
	public void logOut()
	{
		this.parent.logOut();
	}
	
	public void setParent(SettingActivity activity)
	{
		this.parent	= activity;
	}
	

}
