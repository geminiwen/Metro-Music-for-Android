package com.MetroMusic.view;

import java.util.List;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.MetroMusic.model.LyricModel;
import com.MetroMusic.model.SentenceModel;

public class LrcView extends TextView{

	private LyricModel lyricModel;
	private List<SentenceModel> list;
	private Paint highLightPaint;
	private Paint otherPaint;
	private int   index = 0;
	
	private float mX,mY,middleY;
	private static final int DY = 30; // 每一行的间隔
	private static final float fontSize = 16;
	
	public LrcView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		highLightPaint	= new Paint();
		highLightPaint.setAntiAlias(true);
		highLightPaint.setTextSize(fontSize);
		highLightPaint.setColor(Color.WHITE);
		highLightPaint.setTypeface(Typeface.SERIF);
		
		otherPaint		= new Paint();
		otherPaint.setAntiAlias(true);
		otherPaint.setTextSize(fontSize);
		otherPaint.setColor(Color.YELLOW);
		otherPaint.setTypeface(Typeface.SERIF);
	}
	
	
	
	public LrcView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		highLightPaint	= new Paint();
		highLightPaint.setAntiAlias(true);
		highLightPaint.setTextSize(fontSize);
		highLightPaint.setColor(Color.WHITE);
		highLightPaint.setTypeface(Typeface.SERIF);
		
		otherPaint		= new Paint();
		otherPaint.setAntiAlias(true);
		otherPaint.setTextSize(fontSize);
		otherPaint.setColor(Color.YELLOW);
		otherPaint.setTypeface(Typeface.SERIF);
	}



	public LrcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		highLightPaint	= new Paint();
		highLightPaint.setAntiAlias(true);
		highLightPaint.setTextSize(fontSize);
		highLightPaint.setColor(Color.WHITE);
		highLightPaint.setTypeface(Typeface.SERIF);
		
		otherPaint		= new Paint();
		otherPaint.setAntiAlias(true);
		otherPaint.setTextSize(fontSize);
		otherPaint.setColor(Color.YELLOW);
		otherPaint.setTypeface(Typeface.SERIF);
	}



	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint p		= highLightPaint;
		Paint p2	= otherPaint;
		p.setTextAlign(Paint.Align.CENTER);
		p2.setTextAlign(Paint.Align.CENTER);
		if( list == null )
		{
			canvas.drawText("没有歌词", mX, middleY, p2);
			return;
		}
		
		

		if (index == -1) return;
		
		// 先画当前行，之后再画他的前面和后面，这样就保持当前行在中间的位置
		canvas.drawText(list.get(index).getContent(), mX, middleY, p2);

		float tempY = middleY;
		// 画出本句之前的句子
		for (int i = index - 1; i >= 0; i--) {
			// Sentence sen = list.get(i);
			// 向上推移
			tempY = tempY - DY;
			if (tempY < 0) {
				break;
			}
			canvas.drawText(list.get(i).getContent(), mX, tempY, p);
			// canvas.translate(0, DY);
		}

		tempY = middleY;
		// 画出本句之后的句子
		for (int i = index + 1; i < list.size(); i++) {
			// 往下推移
			tempY = tempY + DY;
			if (tempY > mY) {
				break;
			}
			canvas.drawText(list.get(i).getContent(), mX, tempY, p);
			// canvas.translate(0, DY);
		}
		
	}
	
	public void setLyric(LyricModel model)
	{
		this.lyricModel = model;
		if(model != null )
		{
			this.list		= model.getSentenceList();
		}else {
			this.list		= null;
		}
	}
	
	public long updateIndex(long time) {
		// 歌词序号
		index = lyricModel.getNowSentenceIndex(time);
		if (index == -1)
			return -1;
		SentenceModel sen = list.get(index);
		// 返回歌词持续的时间，在这段时间内sleep
		return sen.getDuring();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		mX = w * 0.5f; // remember the center of the screen
		mY = h;
		middleY = h * 0.5f;
	}
	
	
}
