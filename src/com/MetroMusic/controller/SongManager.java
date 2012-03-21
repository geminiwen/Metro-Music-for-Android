package com.MetroMusic.controller;

import java.io.IOException;
import java.util.List;

import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import api.Api;

import com.MetroMusic.activity.R;
import com.MetroMusic.dao.ChannelDAO;
import com.MetroMusic.data.Channel;
import com.MetroMusic.data.Song;
import com.MetroMusic.dbhelper.DataBaseHelper;
import com.MetroMusic.http.RequestParams;
import com.MetroMusic.model.PlayerModel;
import com.MetroMusic.model.UserModel;

public class SongManager {
	private NetworkManager networkManager;
	private ChannelManager channelManager;
	private ChannelDAO	   channelDAO;
	private Context		   appContext;
	private String		   operator ;
	
	private OnLoveOperateCompletionListener songOpComletelistener;
	
	private PlayerModel data;
	private static final String DOUBAN_URL = "http://douban.fm/j/mine/playlist";
	private boolean isNew = true, isInited = false;
	
	public synchronized void initializeIfNeed() throws IOException,SQLiteException
	{
		if(!isNew || isInited)return;
		String app_name = appContext.getResources().getString(R.string.app_name);
		try{
			channelDAO	   = new ChannelDAO(new DataBaseHelper(appContext,app_name).getWritableDatabase());
		}catch(SQLiteException sqle)
		{
			throw sqle;
		}
		List dbChannelList = channelDAO.getAvilableChannelList();
		if(dbChannelList.size() > 0)
		{
			channelManager = new ChannelManager();
			channelManager.setChannelList(dbChannelList);
		}
		else
		{
			try {
				JSONArray jsonArray = networkManager.executeAndGetJson(Api.API_CHANNEL,null).getJSONArray("channels");
				networkManager.closeExpiredConnection();
				channelManager 		= new ChannelManager(jsonArray);
				channelDAO.updateAvailableChannelList(channelManager.getChannelList());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException("服务器错误");
			}
		}
		channelDAO.dbClose();
		channelManager.initCurrentChannel();
		operator = "n";
		isInited = true;
	}
		
	public SongManager(Context appContext,PlayerModel model)
	{
		this.networkManager = new NetworkManager(appContext);
		this.data		   = model;
		this.appContext = appContext;
	}
	
	public Song loadNewSong() throws IOException,SQLiteException 
	{
		initializeIfNeed();
		RequestParams params = new RequestParams();
		Song thisSong = null;
		if(isNew)
		{
			operator = Api.OP_NEW;
		}
		else
		{
			params.put("sid", data.getLastSong().getSid());
			params.put("h", data.getHistory());
		}
		params.put("from", "ie9");
		params.put("type", operator);
		params.put("channel", String.valueOf(channelManager.getCurrentChannel().getId()));
		JSONObject responseJson = null;
		try {
			responseJson = networkManager.executeAndGetJson(Api.API_THIRD_PART_RADIO,params);
			JSONArray  array		= responseJson.getJSONArray("song");
			networkManager.closeExpiredConnection();
			thisSong = new Song(array.optJSONObject(0));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("JSON 转换失败："+responseJson.toString());
		} 
		data.setLastSong(thisSong);
		data.setStop(false);
		isNew = false;
		return thisSong;
	}
	
	public void loveSongAsync( final boolean isLove )
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				RequestParams params	= new RequestParams();
				Song		  thisSong	= data.getLastSong();
				params.put("from", "ie9");
				params.put("sid",thisSong.getSid());
				String operators = null;
				if(isLove)
				{
					operators = Api.OP_LIKE;
				}
				else
				{
					operators = Api.OP_CANCEL_LIKE;
				}
				params.put("type", operators);
				params.put("channel", String.valueOf(channelManager.getCurrentChannel().getId()));
				try {
					JSONObject json = networkManager.executeAndGetJson(Api.API_THIRD_PART_RADIO,params);
					if(songOpComletelistener!=null)songOpComletelistener.OnCompletion(isLove);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
		
	}
	
	public ChannelManager getChannelManager()
	{
		return this.channelManager;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public void addUserCookie(UserModel userModel)
	{
		networkManager.addCookie(new BasicClientCookie("ck",userModel.getCk()));
	}
	
	public Channel changeChannelById(int channelId)
	{	
		return channelManager.changeChannelById(channelId);
	}
	
	public Channel changeChannelByName(String channelName)
	{
		return channelManager.changeChannelByName(channelName);
	}
	
	public void closeManager()
	{
		networkManager.saveCookie(appContext);
	}
	
	public void setOnLoveOperateCompletionListener(OnLoveOperateCompletionListener listener)
	{
		this.songOpComletelistener = listener;
	}
	
	public static interface OnLoveOperateCompletionListener{
		void OnCompletion(boolean isloved);
	}
	
}
