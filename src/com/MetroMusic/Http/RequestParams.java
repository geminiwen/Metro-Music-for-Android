package com.MetroMusic.Http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class RequestParams{
	List<NameValuePair> pair;
	
	public RequestParams(){
		pair = new ArrayList<NameValuePair>(2); 
	}

	public void put(String name,String value)
	{
		pair.add(new BasicNameValuePair(name,value));
	}
	
	public List<NameValuePair> getEntityList()
	{
		return pair;
	}

	public URI getUrlWithParams(String url)
	{
		StringBuilder sb = new StringBuilder(url);
		
		Iterator<NameValuePair> iter = pair.iterator();
		if(iter.hasNext())
		{
			sb.append("?");
		}
		while(iter.hasNext())
		{
			NameValuePair tpaire = iter.next();
			sb.append(tpaire.getName());
			sb.append("=");
			String value = tpaire.getValue();
			if(value.contains("|")||value.contains(":")||value.contains("&"))
			{
				try {
					value = URLEncoder.encode(value,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sb.append(value);
			if(iter.hasNext())
			{
				sb.append("&");
			}
		}
		String result = sb.toString();

		return URI.create(result);
	}
}
