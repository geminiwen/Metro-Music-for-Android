package com.MetroMusic.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import android.content.Context;

import com.MetroMusic.http.AsyncHttpClient;

public class ImageManager {
	private NetworkManager networkManager;
	private Object mutextObject = new Object();
	private InputStream fileStream;
	private OnDownloadCompletionListener listener;
	
	public ImageManager(Context context)
	{
		networkManager = new NetworkManager(context);
	}
	
	/***
	 * 异步获得图片流
	 * @param uri
	 */
	public void getImageFromUrlAsync(final URI uri)
	{
		final String strUrl = uri.toASCIIString();
		new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				synchronized (mutextObject) {
					try {
						fileStream = networkManager.execute(strUrl, null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					networkManager.closeExpiredConnection();
					if( null != listener ) listener.onCompletion(fileStream);
				}
			}
			
		}).start();
	}
	
	public void setOnDownloadCompletionlistener(
			OnDownloadCompletionListener listener)
	{
		this.listener = listener;
	}
	
	public static interface OnDownloadCompletionListener
	{
		public void onCompletion(InputStream is);
	}
}
