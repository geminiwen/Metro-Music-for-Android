package com.MetroMusic.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.MetroMusic.data.Channel;
public class ChannelDAO {
	private SQLiteDatabase db;
	
	public ChannelDAO(SQLiteDatabase sqLiteDatabase)
	{
		this.db	= sqLiteDatabase;
		if(this.db == null)
		{
			throw new SQLiteException("sqlite can't open");
		}
	}

	public List getAvilableChannelList()
	{
		List list = new ArrayList();
		Timestamp prev = new Timestamp((long) (System.currentTimeMillis() - 1.5552e10));
		String columns[] = {"id","name","sid"};
		String selection = "lstupdate > ?";
		String selectionArgs[] = {prev.toString()};
		Cursor result = db.query("channel", columns, selection, selectionArgs, null, null, null);
		result.moveToFirst(); 
		while(!result.isAfterLast())
		{
			Channel tchannel = new Channel();
			int 	cid		 = result.getInt(0);
			String	name	 = result.getString(1);
			int		sid		 = result.getInt(2);
			tchannel.setId(cid);
			tchannel.setName(name);
			tchannel.setSeqId(sid);
			list.add(tchannel);
			result.moveToNext();
		}
		result.close();
		return list;
	}
	
	public void updateAvailableChannelList(List<Channel> list)
	{
		db.delete("channel", null, null);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		for( Channel cn : list )
		{
			int cid = cn.getId();
			String name = cn.getName();
			int sid	= cn.getSeqId();
			ContentValues cv = new ContentValues();
			cv.put("id", cid);
			cv.put("name", name);
			cv.put("sid", sid);
			cv.put("lstupdate",now.toString());
			db.insert("channel", null, cv);
		}
	}
	
	public void dbClose()
	{
		db.close();
	}
}
