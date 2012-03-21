package com.MetroMusic.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class UserModel implements Serializable {
	private String username;
	private String password;
	private String userId;
	private String nickname;
	private String ck;
	private String url;
	private PlayRecord playRecord;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getCk() {
		return ck;
	}
	public void setCk(String ck) {
		this.ck = ck;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public PlayRecord getPlayRecord() {
		return playRecord;
	}
	public void setPlayRecord(PlayRecord playRecord) {
		this.playRecord = playRecord;
	}
	public UserModel(){}
	
	public UserModel(JSONObject jsonObject)
	{
		try {
			this.ck			= jsonObject.getString("ck");
			this.nickname	= jsonObject.getString("name");
			this.userId		= jsonObject.getString("id");
			this.playRecord	= new PlayRecord(jsonObject.getJSONObject("play_record"));
			this.url		= jsonObject.getString("url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
