package com.MetroMusic.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AboutActivity extends MMAbstractActivity{

	private Button backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.about);
		setupViews();
	}

	@Override
	protected void setupViews() {
		// TODO Auto-generated method stub
		backButton = (Button)findViewById(R.id.backBtn);
		super.setupViews();
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		backButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AboutActivity.this.finish();
			}
			
		});
	}
	
}
