package com.MetroMusic.helper;

public class LoginState extends AbstractState{
	public LoginState(int state) {
		super(state);
		// TODO Auto-generated constructor stub
	}
	public final static int CAPTCHA_LOAD = 0x01;
	public final static int CAPTCHA_COMPLETE = 0x02;
}
