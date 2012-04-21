package com.MetroMusic.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.MetroMusic.data.Song;
import com.MetroMusic.helper.SongInfomation;
import com.MetroMusic.util.LrcUtil;

public class SongInfomationManager {
	private Handler uiHandler;
	private NetworkManager networkManager;
	private Song    song;
	private OnLrcDownloadCompletionListener lrcListener;
	
	public SongInfomationManager(Handler handler,Context context)
	{
		uiHandler = handler;
		networkManager = new NetworkManager(context);
	}
	
	public SongInfomationManager(Handler handler,Context context,Song song)
	{
		this(handler,context);
		this.song = song;
	}

	public void setSong(Song song) {
		this.song = song;
	}
	
	public void getSongInformation()
	{
		Message msg = this.uiHandler.obtainMessage();
		msg.what	= SongInfomation.TIME;
		int length  = song.getLength();
		String format  = String.format("%02d:%02d", length/60,length%60);
		String  strLeng = format;
		msg.obj		= strLeng;
		this.uiHandler.sendMessage(msg);
		msg			= new Message();
		msg.what	= SongInfomation.TITLE;
		String title = song.getTitle();
		String artist = song.getArtist();
		String str  = title + "  -  " + artist;
		msg.obj		= str;
		this.uiHandler.sendMessage(msg);
		getSongLRCAsync();
	}
	
	public static interface OnLrcDownloadCompletionListener
	{
		void onCompletion(String result);
	}
	
	public void getSongLRCAsync()
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String searchPathFormat = "http://ttlrcct2.qianqian.com/dll/lyricsvr.dll?sh?Artist=artist&Title=titleFlags=0";
				final String downloadPathFormat = "http://ttlrcct2.qianqian.com/dll/lyricsvr.dll?dl?Id=sId&Code=sCode";
				String result = "未找到匹配的歌词!";
				LrcUtil lrcHelper = new LrcUtil();
				String title	= lrcHelper.getUNICODE(song.getTitle()).toString();
				String artist	= lrcHelper.getUNICODE(song.getArtist()).toString();
				String searchPath = searchPathFormat.replace("artist", artist).replace("title", title); 
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder;
				
				try {
					builder = factory.newDocumentBuilder();
					// 读取歌词文件下载列表，由于存在同名或一个歌曲存在多个lrc文件，所以需要从列表中选择一个
					Document doc = builder.parse(searchPath); 
					Element root = doc.getDocumentElement();
					NodeList children = root.getChildNodes();
					for(int i = 0; i < children.getLength(); i++ )
					{
						Node child = children.item(i);
						if (child instanceof Element) {
						     Element childElement = (Element) child;
						     String sLrcId = childElement.getAttribute("id");
						     String sArtist = childElement.getAttribute("artist");
						     String sTitle = childElement.getAttribute("title");
						     String sCode = lrcHelper.createCode(sArtist, sTitle, Integer.parseInt(sLrcId));
						     // 从XML格式的列表中分析结点数据，修改歌词下载路径，获取歌词
						     String downloadPath = downloadPathFormat.replace("sId", sLrcId).replace("sCode", sCode);
						     InputStream is = networkManager.execute(downloadPath, null);
						     BufferedReader br = new BufferedReader(new InputStreamReader(is,
						    	     "UTF-8"));					  
						     StringBuilder sb = new StringBuilder();
						     String data;
						     while( (data = br.readLine()) != null )
						     {
						    	 sb.append(data + "\n");
						     }
						     result = sb.toString();  // 根据下载路径下载歌词
						     Log.v("lrc", result);
						     if(lrcListener != null)lrcListener.onCompletion(result);
						     return;
						}
					}
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
