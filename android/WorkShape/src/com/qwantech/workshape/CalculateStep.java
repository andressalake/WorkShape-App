package com.qwantech.workshape;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CalculateStep  {

	//counts how many times a large  acceleration occurs
	private int count=0;
	private int countStep =0;
	public int x=5, y=5;
	private static int lastStep = 0;

	
	public void increaseCount()
	{
		count++;
		//FUnctions with vectors
	}
	
	public void increaseCounter2()
	{
		
		countStep++;
		//function check acceleration 
	}
	
	private int calculaLastSteptoTheServer(int stepNumber)
	{
		
		//Calculate the delta of steps
		int deltaStep = stepNumber - lastStep;
		lastStep = stepNumber;
		Log.d("CalclateSteps****************", "LastStep" + deltaStep);
		return deltaStep;
		
	}
	
	public int returnStepDelta()
	{
		return calculaLastSteptoTheServer(countStep); 
		//return the delta of steps to post on the server
		
	}
	
	

}
