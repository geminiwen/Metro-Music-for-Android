package com.MetroMusic.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.MetroMusic.activity.R;
import com.MetroMusic.dao.UserDAO;
import com.MetroMusic.dbhelper.DataBaseHelper;
import com.MetroMusic.http.RequestParams;
import com.MetroMusic.model.UserModel;

public class UserManager {
	private NetworkManager networkManager;
	private UserDAO		   userDAO;
	private Context 	   appContext;
	private Object mutextObject = new Object();
	private InputStream imageis;
	private OnCaptchaCompletionListener listener;
	
	public UserManager(Context context)
	{
		networkManager = new NetworkManager(context);
	}
	
	public void setAppContext(Context appContext) {
		this.appContext = appContext;
	}

	public UserModel userLogin(UserModel user,String captcha,String code)
	{
		RequestParams params = new RequestParams();
		String username = user.getUsername();
		UserModel loginUser = null;
		JSONObject jsonObject = null;
		String password = user.getPassword();
		params.put("source", "radio");
		params.put("alias", username);
		params.put("form_password", password);
		params.put("captcha_solution", captcha);
		params.put("captcha_id", code);
		params.put("remember", "on");
		try {
			jsonObject = networkManager.executeAndPutJson("http://douban.fm/j/login", params);
			networkManager.closeExpiredConnection();
			boolean loginSuccess  = jsonObject.getInt("r") == 0;
			if(loginSuccess)
			{
				JSONObject userJson = jsonObject.getJSONObject("user_info");
				loginUser = new UserModel(userJson);
				loginUser.setUsername(user.getUsername());
				loginUser.setPassword(user.getPassword());
				String app_name = appContext.getResources().getString(R.string.app_name);
				userDAO = new UserDAO(new DataBaseHelper(appContext,app_name).getWritableDatabase());
				userDAO.saveUser(loginUser, true);
				userDAO.dbClose();
				return loginUser;
			}
			else
			{
				String msg = jsonObject.getString("err_msg");
				throw new RuntimeException(msg);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("JSON 转换错误");
		}
		return user;
	}

	public UserModel getAutoLoginUserFromDB()
	{
		String app_name = appContext.getResources().getString(R.string.app_name);
		userDAO = new UserDAO(new DataBaseHelper(appContext,app_name).getWritableDatabase());
		UserModel loginUser = userDAO.getAutoLoginUserModel();;
		userDAO.dbClose();
		return loginUser;
	}
	public void setCaptchaCompeltionListener(OnCaptchaCompletionListener listener)
	{
		this.listener = listener;
	}
	
	public void loadCaptchaAsync()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run() {
				// TODO Auto-generated method stub
				synchronized (mutextObject) {
					InputStream is = null;
					try {
						is = networkManager.execute("http://douban.fm/j/new_captcha", null);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					BufferedReader bfreader = new BufferedReader( new InputStreamReader(is) );
					try {
						String code = bfreader.readLine();
						code		= code.substring(1, code.length()-1);
						RequestParams params = new RequestParams();
						params.put("id", code);
						params.put("size", "m");
						imageis = networkManager.execute("http://douban.fm/misc/captcha", params);
						if(listener!=null)listener.onCompletion(imageis,code);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			
		}).start();
	}
	
	
	public static interface OnCaptchaCompletionListener
	{
		void onCompletion(InputStream is,String code);
	}
	
	
}
