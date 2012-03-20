package com.MetroMusic.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.MetroMusic.Activity.R;
import com.MetroMusic.Activity.R.id;
import com.MetroMusic.Controller.LoginPreferenceController;

public class LoginPreference extends Preference  {

	private Button		loginBtn;
	private TextView	loginUsernameTextView;
	private String 		username;
	private LoginPreferenceController controller;
	
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
	protected void onClick() {
		// TODO Auto-generated method stub
		//Toast.makeText(this.getContext(), "µã»÷ÁËÎÒ", Toast.LENGTH_LONG).show();
		super.onClick();
	}

	@Override
	protected void onBindView(View view) {
		// TODO Auto-generated method stub
		loginBtn				= (Button)view.findViewById(R.id.perfer_loginBtn);
		loginUsernameTextView	= (TextView)view.findViewById(R.id.prefer_loginUsername);
		if(username != null)
		{
			this.loginUsernameTextView.setText(username);
			this.loginUsernameTextView.setVisibility(View.VISIBLE);
			this.loginBtn.setVisibility(View.GONE);
		}
		controller.Bind();
		super.onBindView(view);
	}

	public void setOnLoginClickListener(OnClickListener l) {
		loginBtn.setOnClickListener(l);
	}
	
	public void setUsername(String username)
	{
		this.username	= username;
	}
	
	

}
