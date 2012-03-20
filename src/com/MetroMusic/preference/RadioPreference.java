package com.MetroMusic.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.MetroMusic.Activity.R;

public class RadioPreference extends Preference{

	private RadioButton		preferRadioButton;
	private TextView		channelTextView;
	private boolean			checked;
	
	public RadioPreference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public RadioPreference(Context context,AttributeSet attrs)
	{
		super(context,attrs);
		
	}
	

	@Override
	protected void onClick() {
		// TODO Auto-generated method stub
		preferRadioButton.setChecked(true);
		super.onClick();
	}
	
	public void cancleCheck()
	{
		if(preferRadioButton != null)
		{
			preferRadioButton.setChecked(false);
		}
	}
	
	public void check()
	{
		checked = true;
	}

	@Override
	protected void onBindView(View view) {
		// TODO Auto-generated method stub
		preferRadioButton	= (RadioButton)view.findViewById(R.id.prefer_channelRadio);
		channelTextView		= (TextView)view.findViewById(R.id.prefer_channelText);
		channelTextView.setText(getTitle());
		preferRadioButton.setChecked(checked);
		super.onBindView(view);
	}
	
	
	
	

}
