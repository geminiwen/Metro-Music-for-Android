package com.MetroMusic.helper;

public class PlayerState extends AbstractState{
	public PlayerState(int state) {
		super(state);
		// TODO Auto-generated constructor stub
	}
	public final static int PLAY			= 0x01;
	public final static int PAUSE			= 0x02;
	public final static int STOP			= 0x03;
	public final static int WAIT			= 0x04;
	public final static int READY 		 	= 0x05;
	public final static int PROGRESS 		= 0x06;
	public final static int PROGRESS_MAX	= 0x07;
	public final static int BUFFERING		= 0x08;
	public final static int LRC_UPDATE		= 0x0d;
	public final static int LOVE			= 0x09;
	public final static int UNLOVE			= 0x0a; 
	public final static int DISABLE_LOVE	= 0x0b;
	public final static int ENABLE_LOVE		= 0x0c;
}
