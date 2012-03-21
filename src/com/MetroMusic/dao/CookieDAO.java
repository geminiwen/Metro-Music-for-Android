 package com.MetroMusic.dao;

import java.util.Date;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class CookieDAO {
	private SQLiteDatabase db;
	
	public CookieDAO(SQLiteDatabase sqLiteDatabase)
	{
		this.db	= sqLiteDatabase;
		if(this.db == null)
		{
			throw new SQLiteException("sqlite can't open");
		}
	}
	
	public void saveCookie(List<Cookie> cookieList)
	{
		db.delete("cookies", null, null);
		for( int i = 0; i < cookieList.size(); i ++ )
		{
			ContentValues cv		= new ContentValues();
			int	cookieId			= i;
			String cookieName		= cookieList.get(i).getName();
			String cookieValue		= cookieList.get(i).getValue();
			String cookieDomain		= cookieList.get(i).getDomain();
			String cookiePath		= cookieList.get(i).getPath();
			Date  cookieExpireDate = cookieList.get(i).getExpiryDate();
			cv.put("cookieid", cookieId);
			cv.put("cookiename",cookieName);
			cv.put("cookievalue", cookieValue);
			cv.put("cookiepath", cookiePath);
			cv.put("cookiedomain", cookieDomain);
			if(cookieExpireDate == null)
			{
				cv.putNull("cookieexpired");
			}
			else
			{
				cv.put("cookieexpired",cookieExpireDate.getTime());
			}
			db.insert("cookies", null, cv);
		}
	}

	public CookieStore getLastCookieStore()
	{
		CookieStore cookieStore	= new BasicCookieStore();
		Long now = System.currentTimeMillis();
		Cursor cursor = db.query("cookies", null, "cookieexpired > ? or cookieexpired is null", new String[]{String.valueOf(now)}, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			String cookieName			= cursor.getString(1);
			String cookieValue			= cursor.getString(2);
			String cookieDomian			= cursor.getString(3);
			String cookiePath			= cursor.getString(4);
			Long cookieExipredStr		= cursor.getLong(5);
			BasicClientCookie	cookie	= new BasicClientCookie(cookieName,cookieValue);
			cookie.setDomain(cookieDomian);
			cookie.setPath(cookiePath);
			cookie.setExpiryDate(cookieExipredStr == null ? null :new Date(cookieExipredStr));
			cookieStore.addCookie(cookie);
			
			cursor.moveToNext();
		}
		return cookieStore;
	}
	
	public void dbClose()
	{
		db.close();
	}
}
