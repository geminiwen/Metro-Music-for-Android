package com.MetroMusic.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Channel implements Serializable{
	private int id;
	private String name;
	private int seqId;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public Channel()
	{
		
	}
	
	public Channel(JSONObject jsonObject)
	{
		try {
			this.id 	= jsonObject.getInt("channel_id");
			this.name 	= jsonObject.getString("name");
			this.seqId 	= jsonObject.getInt("seq_id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if( o == this)
		{
			return true;
		}
		
		if( ! (o instanceof Channel) )
		{
			return false;
		}
		
		if( o.hashCode() == this.hashCode() && ((Channel)(o)).name.equals(this.name) )
		{
			return true;
		}
		
		return false;
	}
	
	
	
}
