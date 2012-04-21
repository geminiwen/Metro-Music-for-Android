package com.MetroMusic.listener;

import android.content.Context;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.MetroMusic.activity.R;

public class PlayerAlbumGestureListener implements OnGestureListener {

	private ImageView imageView;
	private Context   appContext;
	
	/*    data       */
	private int   	  imgDirection;
	private boolean	  direction;
	
	public PlayerAlbumGestureListener(ImageView _imageView,Context _appContext)
	{
		this.imageView	= _imageView;
		this.appContext	= _appContext;
		this.imgDirection = 0;
	}
	
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		float y  = e2.getY() - e1.getY();
		direction = y < 0;
		updateImageView();
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void refreshImage()
	{
		imageView.setVisibility(View.VISIBLE);
		this.imgDirection = 0;
	}
	
	private void updateImageView()
	{
		if( this.direction )
		{
			if(imgDirection == 0)
			{
				Animation animation = AnimationUtils.loadAnimation(appContext, R.anim.mp_songimage_gesture1);
				imageView.startAnimation(animation);
				imageView.setVisibility(View.INVISIBLE);
				imgDirection = 1;
			}
		}
		else
		{
			if(imgDirection == 1)
			{
				Animation animation = AnimationUtils.loadAnimation(appContext, R.anim.mp_songimage_gesture2);
				imageView.startAnimation(animation);
				imageView.setVisibility(View.VISIBLE);
				imgDirection = 0;
			}
		}
	}

}
