package com.MetroMusic.Data;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable{
	private String picture;
	private String albumTitle;
	private String company;
	private double ratingAvg;
	private String publicTime;
	private String ssid;
	private String album;
	private int isLike;
	private String artist;
	private String songUrl;
	private String title;
	private String subType;
	private int    length;
	private String sid;
	private String aid;
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getAlbumTitle() {
		return albumTitle;
	}
	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public double getRatingAvg() {
		return ratingAvg;
	}
	public void setRatingAvg(double ratingAvg) {
		this.ratingAvg = ratingAvg;
	}
	public String getPublicTime() {
		return publicTime;
	}
	public void setPublicTime(String publicTime) {
		this.publicTime = publicTime;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public int getIsLike() {
		return isLike;
	}
	public void setIsLike(int isLike) {
		this.isLike = isLike;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getSongUrl() {
		return songUrl;
	}
	public void setSongUrl(String songUrl) {
		this.songUrl = songUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	
	public Song(){}
	
	public Song(JSONObject fromJson)
	{
		try {
			this.picture 		= fromJson.getString("picture");
			this.albumTitle 	= fromJson.getString("albumtitle");
			this.company 		= fromJson.getString("company");
			this.ratingAvg 		= fromJson.getDouble("rating_avg");
			this.publicTime 	= fromJson.getString("public_time");
			this.ssid			= fromJson.getString("ssid");
			this.album 			= fromJson.getString("album");
			this.isLike			= fromJson.getInt("like");
			this.artist			= fromJson.getString("artist");
			this.songUrl 		= fromJson.getString("url");
			this.title 			= fromJson.getString("title");
			this.subType 		= fromJson.getString("subtype");
			this.length			= fromJson.getInt("length");
			this.sid 			= fromJson.getString("sid");
			this.aid 			= fromJson.getString("aid");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(picture);
		dest.writeString(albumTitle);
		dest.writeString(company);
		dest.writeDouble(ratingAvg);
		dest.writeString(publicTime);
		dest.writeString(ssid);
		dest.writeString(album);
		dest.writeInt(isLike);
		dest.writeString(artist);
		dest.writeString(songUrl);
		dest.writeString(title);
		dest.writeString(subType);
		dest.writeString(sid);
		dest.writeString(aid);
		dest.writeInt(length);
	}

    public static final Parcelable.Creator<Song> CREATOR = new Creator<Song>() {  
        public Song createFromParcel(Parcel source) {  
        	Song song = new Song();  
            song.picture = source.readString();
            song.albumTitle = source.readString();
            song.company = source.readString();
            song.ratingAvg = source.readDouble();
            song.publicTime = source.readString();
            song.ssid = source.readString();
            song.album = source.readString();
            song.isLike = source.readInt();
            song.artist = source.readString();
            song.songUrl = source.readString();
            song.title = source.readString();
            song.subType = source.readString();
            song.sid = source.readString();
            song.aid = source.readString();
            song.length = source.readInt();
            return song;  
        }  
        public Song[] newArray(int size) {  
            return new Song[size];  
        }  
    };  
	
}
