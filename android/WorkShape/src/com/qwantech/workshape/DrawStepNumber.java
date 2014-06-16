package com.qwantech.workshape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DrawStepNumber extends View {

	//counts how many times a large  acceleration occurs
	private int count=0;
	private int countStep =0;
	public int x=5, y=5;
	private static int lastStep = 0;
	
	Paint paintObj = new Paint (Paint.ANTI_ALIAS_FLAG);
	
	public DrawStepNumber(Context context, AttributeSet attrs) {
		super(context, attrs);

	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		paintObj.setColor(Color.RED); // set the color of the textView to count numbers of jumps, for exemplo
		canvas.drawText(Integer.toString(count), 150, 100, paintObj); //draw textView that shows the counter for jumps or steps on thescreen
		
		paintObj.setColor(Color.DKGRAY);
		canvas.drawText(Integer.toString(countStep),160,110,paintObj);
		
		paintObj.setColor(Color.CYAN);

		canvas.drawCircle(x,y,5, paintObj);
	}
	
	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		
		invalidate(); 
	}
	
	
	

}
