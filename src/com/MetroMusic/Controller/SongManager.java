package com.MetroMusic.Controller;

import java.io.IOException;
import java.util.List;

import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import api.Api;

import com.MetroMusic.Activity.R;
import com.MetroMusic.Data.Channel;
import com.MetroMusic.Data.Song;
import com.MetroMusic.Http.RequestParams;
import com.MetroMusic.Model.PlayerModel;
import com.MetroMusic.Model.UserModel;
import com.MetroMusic.dao.ChannelDAO;
import com.MetroMusic.dbhelper.DataBaseHelper;

public class SongManager {
	private NetworkManager networkManager;
	private ChannelManager channelManager;
	private ChannelDAO	   channelDAO;
	private Context		   appContext;
	private String		   operator ;
	
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
				throw new RuntimeException("·þÎñÆ÷´íÎó");
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
			throw new RuntimeException("JSON ×ª»»Ê§°Ü£º"+responseJson.toString());
		} 
		data.setLastSong(thisSong);
		data.setStop(false);
		isNew = false;
		return thisSong;
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
		networkManager.closeNetworkManager(appContext);
	}
	
}
