package com.qwantech.workshape;

//import com.example.projetotest.R;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
	private static final int ZCOORDINATE = 2;
	private static final int YCOORDINATE = 1;
	private static final int XCOORDINATE = 0;
	
	CountDownTimer countDownTimer; //countDown
	//currentUserID
	private static String currentUserID;
	
	//post to server Object
	ServerConnection serverObj;
	
	Sensor acelerometer;
	SensorManager sensor_manager;

	TextView textViewX, textViewY, textViewZ, textViewMag, textViewMax,
			textViewMin, textViewHoldyourbutton1, textViewHoldyourbutton2,textViewHoldyourbutton3 ;

	float x, y, z, length;
	private static float lastX = 0 , lastY = 0, lastZ = 0 , geometricDelta, geometricDeltaa =0;
	//private float maxMagnitude;
	// private float minMagnitude;
	
	private static final String MESSAGEPART1 ="Hold your phone in";
	private static final String MESSAGEPART2 ="your hand and ";
	private static final String MESSAGEPART3 ="RUN!";

	CalculateStep calculateStepsObj;
	

	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.race_screen);
		serverObj  = new ServerConnection();
	/*	textViewX = (TextView) findViewById(R.id.textViewX);
		textViewY = (TextView) findViewById(R.id.textViewY);
		textViewZ = (TextView) findViewById(R.id.textViewZ);
		textViewMag = (TextView) findViewById(R.id.textViewMagnitude);
		textViewMax = (TextView) findViewById(R.id.textViewMax);
		textViewMin = (TextView) findViewById(R.id.textViewMin);
		calculateStepsObj = (CalculateSteps) findViewById(R.id.customView1); */
		textViewHoldyourbutton1 = (TextView) findViewById(R.id.textmessage1);
		textViewHoldyourbutton2 = (TextView) findViewById(R.id.textViewmessage2);
		textViewHoldyourbutton3 = (TextView) findViewById(R.id.textViewmessage3);
		textViewHoldyourbutton1.setText(MESSAGEPART1);
		textViewHoldyourbutton1.setTextColor(Color.BLUE);
		textViewHoldyourbutton2.setText(MESSAGEPART2);
		textViewHoldyourbutton2.setTextColor(Color.BLUE);
		textViewHoldyourbutton3.setText(MESSAGEPART3);
		textViewHoldyourbutton3.setTextColor(Color.BLUE);

		calculateStepsObj = new CalculateStep();
		sensor_manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		acelerometer = sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensor_manager.registerListener(this, acelerometer,	SensorManager.SENSOR_DELAY_NORMAL);
		serverObj.getEvent(); // choose the race
		setupTimer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}




	//@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		x = event.values[XCOORDINATE];
		y = event.values[YCOORDINATE];
		z = event.values[ZCOORDINATE]; 
	
	/*	textViewX.setText(Float.toString(event.values[XCOORDINATE]));
		textViewY.setText(Float.toString(event.values[YCOORDINATE]));
		textViewZ.setText(Float.toString(event.values[ZCOORDINATE]));
		textViewMag.setText(Float.toString(calculateVectorMag()));
		textViewMax.setText(Float.toString(maxMagnitude));
		textViewMin.setText(Float.toString(minMagnitude) + "\n\n\n\n\n\ngeoDelta" + geometricDeltaa ); */
		calculateStep();
	//	checkFinishLine();
	}
	


	/* private float calculateVectorMag() {
		length = (float) Math.sqrt(x * x + y * y + z * z);
		if (length > maxMagnitude)
			maxMagnitude = length;

		if (length < minMagnitude)
			minMagnitude = length;

			calculateStepsObj.setPosition((int) x*4+40, (int) y*4+40);
			
			if(length > 15) calculateStepsObj.increaseCount(); //increase the couter1, Why 15?
		
		return length;
	}
	
	/*
	 * double xDelta = self.lastAcceleration.x - acceleration.x;
double yDelta = self.lastAcceleration.y - acceleration.y;
double zDelta = self.lastAcceleration.z - acceleration.z;
double geometricDelta  = sqrt(pow(xDelta, 2)+pow(yDelta, 2)+pow(zDelta, 2));
self.lastAcceleration = acceleration;

	 * 
	 * *
	 */
	private void calculateStep() //Kris calculation
	{
		float xDelta, yDelta, zDelta;
	 
		xDelta = lastX - x; 
		yDelta = lastY - y;
		zDelta = lastZ - z;
   geometricDelta = (float)Math.sqrt(xDelta * xDelta +  yDelta * yDelta + zDelta* zDelta);
		lastX = x;
		lastY = y;
		lastZ = z;
		
 
	 	if (geometricDelta > geometricDeltaa  ) 
	 	{
	 		geometricDeltaa = geometricDelta; // just to print the last value of the magnitude
	 	}
	 	
	 		if(geometricDelta > 14) 
	 	{// testing if 14 is good to verify a step
	 			
	 		calculateStepsObj.increaseCounter2();
	 		String stepdelta =  Integer.toString(calculateStepsObj.returnStepDelta());
	 		serverObj.postMoviments(stepdelta);
	 		Log.d("MAinActivity","Number of steps" + stepdelta);
	 	}
	 
	}
	
	public void setCurrentUserID(String userId)
	{
		currentUserID = userId;
	}
	
	public String getCurrentUserID()
	{
		return currentUserID ;
	}
	

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

		
	}
	
	private void setupTimer()
	{
		final TextView countDown = (TextView) findViewById(R.id.textViewTimerRace);
		 countDownTimer = new CountDownTimer(10000,1000)
		{
			@Override
			public void onTick(long milisecondsUntilFinish) {
				long seconds = milisecondsUntilFinish/1000;
				countDown.setText(""+ seconds);
				
			}
			@Override
			public void onFinish() {
				Intent intent = new Intent(MainActivity.this, EndRace.class);
				startActivity(intent);
			}
			
		}.start();

	}
	
	private void checkFinishLine()
	{
		String isRaceFinished = serverObj.getFinishLine();
		if(isRaceFinished == "true")
		{
			countDownTimer.cancel();
			Intent intent2 = new Intent(MainActivity.this, EndRace.class);
			startActivity(intent2);
			
		}
	}
	
}
