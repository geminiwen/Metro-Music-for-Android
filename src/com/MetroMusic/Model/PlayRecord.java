package com.MetroMusic.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayRecord implements Serializable{
	private int banned;
	private int liked;
	private int played;
	public int getBanned() {
		return banned;
	}
	public void setBanned(int banned) {
		this.banned = banned;
	}
	public int getLiked() {
		return liked;
	}
	public void setLiked(int liked) {
		this.liked = liked;
	}
	public int getPlayed() {
		return played;
	}
	public void setPlayed(int played) {
		this.played = played;
	}
	public PlayRecord(){}
	
	public PlayRecord(JSONObject jsonObject)
	{
		try {
			this.banned = jsonObject.getInt("banned");
			this.liked 	= jsonObject.getInt("liked");
			this.played	= jsonObject.getInt("played");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
