package com.MetroMusic.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper  {

	private final static int DB_VERSION = 1;
	
	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	public DataBaseHelper(Context context,String name)
	{
		super(context, name, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table channel(id INTEGER PRIMARY KEY," +
				"name varchar(30) NOT NULL," +
				"sid  INTEGER	  NOT NULL," +
				"lstupdate timestamp NOT NULL);");
		db.execSQL("create table userinfo(" +
				"userid	  varchar(8)  PRIMARY KEY," +
				"username varchar(50) NOT NULL," +
				"password varchar(50) NOT NULL," +
				"nickname varchar(50) NOT NULL," +
				"url	  varchar(50)," +
				"ck		  varchar(10)," +
				"played	  integer," +
				"banned	  integer," +
				"liked	  integer," +
				"autologin tinyint default '0');");
		db.execSQL("create table cookies(" +
				"cookieid		integer PRIMARY KEY," +
				"cookiename		varchar(20) NOT NULL," +
				"cookievalue	varchar(30) NOT NULL," +
				"cookiedomain	varchar(30)," +
				"cookiepath		varchar(30)," +
				"cookieexpired	datetime)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
