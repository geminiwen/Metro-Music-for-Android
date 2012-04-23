package com.MetroMusic.listener;

import android.content.Context;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.MetroMusic.activity.R;

public class PlayerAlbumGestureListener implements OnGestureListener {

	private ImageView imageView;
	private View  lrcView;
	private Context   appContext;
	
	/*    data       */
	private int   	  imgDirection;
	private boolean	  direction;
	private Animation fadeInAnimation,fadeOutAnimation;
	
	public PlayerAlbumGestureListener(ImageView _imageView,View _lrcView,Context _appContext)
	{
		this.imageView	= _imageView;
		this.lrcView	= _lrcView;
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
		lrcView.setVisibility(View.INVISIBLE);
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
				fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
				fadeInAnimation.setDuration(500);
				lrcView.setAnimation(fadeInAnimation);
				lrcView.setVisibility(View.VISIBLE);
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
				fadeOutAnimation = new AlphaAnimation(1.0f,0.0f);
				fadeOutAnimation.setDuration(500);
				lrcView.setAnimation(fadeOutAnimation);
				lrcView.setVisibility(View.INVISIBLE);
				imgDirection = 0;
			}
		}
	}

}
