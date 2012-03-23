package com.MetroMusic.helper;

public class SongInfomation extends AbstractState{
	
	public SongInfomation(int state)
	{
		super(state);
	}
	
	public final static int IMAGE = 0x01;
	public final static int TITLE = 0x02;
	public final static int TIME  = 0x03;
}
