package com.MetroMusic.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.MetroMusic.controller.LoginController;
import com.MetroMusic.helper.LoginState;
public class LoginActivity extends MMAbstractActivity{

	private AutoCompleteTextView	  usernameView;
	private EditText				  passwordView;
	private Button					  loginButton;
	private EditText				  captchaEditText;
	private ProgressBar				  waitProgressBar;
	private ImageView				  captchaImage;
	
	private View.OnClickListener loginListener		= new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String username = usernameView.getText().toString();
			String password = passwordView.getText().toString();
			String captcha	= captchaEditText.getText().toString();
			((LoginController)controller).onLogin(username, password, captcha);
		}
	};
	
	private View.OnClickListener captchaListener	= new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			((LoginController)controller).onLoadCaptcha();
		}
	};
	
	private Handler uiHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what)
			{
			case LoginState.CAPTCHA_COMPLETE:
			{
				Bitmap bitmap = (Bitmap)msg.obj;
				captchaImage.setImageBitmap(bitmap);
				captchaImage.setVisibility(View.VISIBLE);
				waitProgressBar.setVisibility(View.GONE);
				break;
				
			}
			case LoginState.CAPTCHA_LOAD:
			{
				captchaImage.setVisibility(View.GONE);
				waitProgressBar.setVisibility(View.VISIBLE);
				break;
			}
			}
			super.handleMessage(msg);
		}
		
	};
	
	public Handler getUIHandler()
	{
		return this.uiHandler;
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fullScreen();
		setContentView(R.layout.login);
		setController(new LoginController(this));
		setupViews();
	}
	
	@Override
	protected void setupViews()
	{
		usernameView	= (AutoCompleteTextView)findViewById(R.id.loginUsername);
		passwordView	= (EditText)findViewById(R.id.loginPassword);
		loginButton		= (Button)findViewById(R.id.loginUserBtn);
		captchaImage	= (ImageView)findViewById(R.id.loginCaptchaImageView);
		captchaEditText	= (EditText)findViewById(R.id.loginCaptcha);
		waitProgressBar = (ProgressBar)findViewById(R.id.loginWaitProgressBar);
		super.setupViews();
	}
	
	private void fullScreen()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		this.loginButton.setOnClickListener(loginListener);
		this.captchaImage.setOnClickListener(captchaListener);
	}
	
	
}
