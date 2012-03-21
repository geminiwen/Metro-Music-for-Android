package com.MetroMusic.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.MetroMusic.activity.R;
import com.MetroMusic.controller.LoginController;
import com.MetroMusic.helper.LoginState;
public class LoginActivity extends Activity{
	
	private LoginController			  loginController;
	private AutoCompleteTextView	  usernameView;
	private EditText				  passwordView;
	private Button					  loginButton;
	private EditText				  captchaEditText;
	private ProgressBar				  waitProgressBar;
	private ImageView				  captchaImage;
	   
	public LoginActivity() {
		super();
		// TODO Auto-generated constructor stub
		loginController = new LoginController(this);
	}
	
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
		setupViews();
	}
	
	private void setupViews()
	{
		usernameView	= (AutoCompleteTextView)findViewById(R.id.loginUsername);
		passwordView	= (EditText)findViewById(R.id.loginPassword);
		loginButton		= (Button)findViewById(R.id.loginUserBtn);
		captchaImage	= (ImageView)findViewById(R.id.loginCaptchaImageView);
		captchaEditText	= (EditText)findViewById(R.id.loginCaptcha);
		waitProgressBar = (ProgressBar)findViewById(R.id.loginWaitProgressBar);
		loginController.Bind();
	}
	
	private void fullScreen()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	public boolean checkUsernameAvaliable()
	{
		return !usernameView.getText().toString().equals("");
	}
	
	public boolean checkPasswordAvaliable()
	{
		return !passwordView.getText().toString().equals("");
	}
	
	public boolean chechCaptchaAvaliable()
	{
		return !captchaEditText.getText().toString().equals("");
	}

	public void setOnLoginClickListener(OnClickListener l) {
		loginButton.setOnClickListener(l);
	}
	
	
	public void setOnCaptchaImageClickListener(View.OnClickListener l) {
		captchaImage.setOnClickListener(l);
	}
	
	public String getUsername()
	{
		return this.usernameView.getText().toString();
	}
	
	public String getPassword()
	{
		return this.passwordView.getText().toString();
	}
	
	public String getCaptcha()
	{
		return this.captchaEditText.getText().toString();
	}

	public void updateCaptchaImage(Bitmap bitmap)
	{
		Message msg = uiHandler.obtainMessage();
		msg.obj		= bitmap;
		msg.what	= LoginState.CAPTCHA_COMPLETE;
		uiHandler.sendMessage(msg);
	}
	
	
}
