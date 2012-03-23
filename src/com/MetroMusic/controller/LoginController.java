package com.MetroMusic.controller;

import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.MetroMusic.activity.MMAbstractActivity;
import com.MetroMusic.activity.SettingActivity;
import com.MetroMusic.helper.AbstractState;
import com.MetroMusic.helper.LoginState;
import com.MetroMusic.model.UserModel;

public class LoginController extends MMAbstractController{
	public LoginController(MMAbstractActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	private UserModel	  loginUser;
	private UserManager   userManager;
	private String 		  captchaCode;
	private String		  captcha;
	
	public void onBind() {
		// TODO Auto-generated method stub
		userManager	= new UserManager(appContext);
		userManager.setCaptchaCompeltionListener(new CaptchaLoadCompletion());
		userManager.loadCaptchaAsync();
	}
	
	public void onLogin(String username,String password,String captcha)
	{
		if(username.isEmpty())
		{
			Toast.makeText(appContext, "必须输入用户名", Toast.LENGTH_SHORT).show();
			return;
		}
		if(password.isEmpty())
		{
			Toast.makeText(appContext, "必须输入密码", Toast.LENGTH_SHORT).show();
			return;
		}
		if(captcha.isEmpty())
		{
			Toast.makeText(appContext, "必须输入验证码", Toast.LENGTH_SHORT).show();
			return;
		}
		this.captcha = captcha;
		UserModel user = new UserModel();
		user.setUsername(username);
		user.setPassword(password);
		new LoginTask().execute(user);
	}
	
	class LoginTask	extends AsyncTask<UserModel, Void, String>
	{
		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog = new ProgressDialog(activity);
			progressDialog.setCancelable(true);
			progressDialog.setIndeterminate(true);
			progressDialog.setTitle("正在登陆");
			progressDialog.setMessage("登陆中，请稍后");
			progressDialog.show();
		}

		@Override
		protected String doInBackground(UserModel... users) {
			// TODO Auto-generated method stub
			String msg = "success";
			try{
				loginUser = userManager.userLogin(users[0], captcha, captchaCode);
				
			}
			catch(RuntimeException re)
			{
				msg = re.getMessage();
			}
			return msg;
		}
		
    	@Override
    	protected void onPostExecute(String result) { 
    		progressDialog.dismiss();
    		if(result == null )
    		{
    			Toast.makeText(appContext, "登录的地方有异常没有捕获", Toast.LENGTH_LONG).show();
    			return;
    		}
    		if(result.equals("success"))
    		{
    			Toast.makeText(appContext, "登陆成功", Toast.LENGTH_LONG).show();
    			Intent intent = new Intent(appContext,SettingActivity.class);
    			Bundle bundle = new Bundle();
    			bundle.putSerializable("loginuser", loginUser);
    			intent.putExtra("bundle", bundle);
    			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    			activity.startActivity(intent);
    		}
    		else
    		{
    			Toast.makeText(appContext, "登陆失败："+result, Toast.LENGTH_LONG).show();
    			onLoadCaptcha();
    		}
    	}
		
	}
	
	class CaptchaLoadCompletion implements UserManager.OnCaptchaCompletionListener
	{

		@Override
		public void onCompletion(InputStream is, String code) {
			// TODO Auto-generated method stub
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			AbstractState state = new LoginState(LoginState.CAPTCHA_COMPLETE);
			LoginController.this.notify(state,bitmap);
			captchaCode	= code;
		}
		
	}
	
	public void onLoadCaptcha()
	{
		AbstractState state = new LoginState(LoginState.CAPTCHA_COMPLETE);
		LoginController.this.notify(state,null);
		userManager.loadCaptchaAsync();
	}
	
}
