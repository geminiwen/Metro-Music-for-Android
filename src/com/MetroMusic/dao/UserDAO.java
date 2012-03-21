package com.MetroMusic.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.MetroMusic.model.PlayRecord;
import com.MetroMusic.model.UserModel;

public class UserDAO {

	private SQLiteDatabase db;
	
	public UserDAO(SQLiteDatabase sqLiteDatabase)
	{
		this.db	= sqLiteDatabase;
		if(this.db == null)
		{
			throw new SQLiteException("sqlite can't open");
		}
	}
	
	public void saveUser(UserModel user,boolean autologin)
	{
		String username = user.getUsername();
		String password = user.getPassword();
		String nickname	= user.getNickname();
		String userid	= user.getUserId();
		String url		= user.getUrl();
		String ck		= user.getCk();
		PlayRecord playRecord = user.getPlayRecord();
		int	banned		= playRecord.getBanned();
		int liked		= playRecord.getLiked();
		int played		= playRecord.getPlayed();
		ContentValues cv = new ContentValues();
		cv.put("userid",userid);
		cv.put("username", username);
		cv.put("password", password);
		cv.put("nickname", nickname);
		cv.put("ck",ck);
		cv.put("url", url);
		cv.put("autologin",String.valueOf(autologin?1:0));
		cv.put("banned", banned);
		cv.put("liked", liked);
		cv.put("played",played);
		Cursor cursor = db.query("userinfo",null, "username = ? and password = ?", new String[]{username,password}, null, null, null);
		if ( cursor.getCount() == 0 ){
			db.insert("userinfo", null, cv);
		}
		else
		{
			db.update("userinfo", cv, "username = ?", new String[]{username});
		}
		cursor.close();
	}
	
	public UserModel getAutoLoginUserModel()
	{
		Cursor cursor = db.query("userinfo", null, "autologin = ?", new String[]{"1"}, null, null, null);
		if(cursor.getCount() > 0)
		{
			UserModel userModel = new UserModel();
			cursor.moveToFirst();
			String	userId	= cursor.getString(0);
			String	username	= cursor.getString(1);
			String	password	= cursor.getString(2);
			String	nickname	= cursor.getString(3);
			String	url			= cursor.getString(4);
			String	ck			= cursor.getString(5);
			int		played		= cursor.getInt(6);
			int		banned		= cursor.getInt(7);
			int		liked		= cursor.getInt(8);
			userModel.setCk(ck);
			userModel.setNickname(nickname);
			userModel.setPassword(password);
			userModel.setUrl(url);
			userModel.setUserId(userId);
			userModel.setUsername(username);
			PlayRecord playRecord = new PlayRecord();
			playRecord.setBanned(banned);
			playRecord.setLiked(liked);
			playRecord.setPlayed(played);
			userModel.setPlayRecord(playRecord);
			cursor.close();
			return userModel;
		}
		cursor.close();
 		return null;
	}
	
	public void dbClose()
	{
		db.close();
	}
	
}
