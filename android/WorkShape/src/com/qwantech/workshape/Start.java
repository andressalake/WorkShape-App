package com.qwantech.workshape;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.os.CountDownTimer;
import java.util.Vector;

import com.google.android.gms.internal.ba;
import com.google.android.gms.internal.eq;

/*
 * this class call the game activity where the player needs to wait for a new race.
 */
public class Start extends Activity {

	private TextView countDown;
	private TextView textViewTips, textViewMyTeam, textViewClickOnTeam;
	private static int indexTips = 0;
	private static double timetoStretches = 180;
	private static int countStretches =1;
	private static  int tenSecondsStretches = 175;
	CountDownTimer countDownTimer, countDownStretches;
	Button joinRace;
	 Vector<String> tipsVector;
	private static String firstTip1 = "Hold your phone with a firm grip; there are no bonus points for throwing your phone!";
	private static String firstTip2 = " Be sure to follow the stretching guide before each race to get" +
			" the full benefit of WORKSHAPE and reduce the chances of injury.";
	private static String firstTip3 = "Ensure there is enough room around each team member; WORKSHAPE isn’t a contact sport!";
	private static String firstTip4 = "Your avatar is designed to respond best to steady " +
			"running on the spot (so forget about putting your phone in a paint shaker).";
	
	
	//Object to call teamViewInfo
	//ShowInfo showinfoObj;
	
	//Images TeamView
	FrameLayout framelayoutTeamInfo,layout;;
	private ImageView waves;
	LayoutParams layoutParams, layoutParamsTextView;
	TextView equalSymbol, nextRace, countStretch;
	private static float dx =0, dy=0, x= 0, y=0;
	private static boolean teamInfoBool = false;
	
	private static long minutes ;
	private static long seconds ;
	private static long seconds2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_race);
		
		
		//deal with objects
		initializeObjs();
		hideTeamInfo();
		

	   
		tipsVector = new Vector<String>();
		tipsVector.add(firstTip1);
		tipsVector.add(firstTip2);
		tipsVector.add(firstTip3);
		tipsVector.add(firstTip4);
		setupTimer();
		

	}
	
	
	@Override
	public void onBackPressed() {
		//if the user touch the back button, it will hide the team Info or go back to the other activity
		
		
		if(teamInfoBool== true){
		hideTeamInfo();
		}else
			if(teamInfoBool==false)
			{
				super.onBackPressed();
			}
		
	}
	

	private void setupTimer() {
		
		 countDownTimer = new CountDownTimer(241000, 1000) {
			@Override
			public void onTick(long milisecondsUntilFinish) {
				 minutes = (milisecondsUntilFinish / 60000);
				 seconds = ((milisecondsUntilFinish % 60000) / 1000);
				 seconds2 = milisecondsUntilFinish /1000;
				countDown.setText( minutes +":"+ seconds);
				if (seconds2 == 5) {
					hideTeamInfo();
					TextView tip = (TextView) findViewById(R.id.textViewTips);
					tip.setText("READY?");
					tip.setGravity(Gravity.CENTER_HORIZONTAL);
					tip.setTextSize(30);
					joinRace.setText("Leave race");
					joinRace.setBackgroundColor(Color.RED);

				}
				
				double rest = seconds2%5; // so, after each 5 seconds the tips change
				
				if((rest == 0) && (seconds2 > 180))
				{
					textViewTips.setText(tipsVector.get(indexTips));
					indexTips++;
					if(indexTips == 4) // we have 4 tips, so the counter must go until 3, 
					{   				//if it is 4, it goes back to 0
						indexTips = 0;
					}
				}
				
				
				if((seconds2 == timetoStretches)  && (seconds2 >=45))
				{
					framelayoutTeamInfo.setVisibility(View.VISIBLE);
					switch (countStretches) {
					case 1:
						framelayoutTeamInfo.setBackgroundResource(R.drawable.ws_screen6b_arms_sides_eft_screen_only);
						showStretchs();
						break;

					case 2:
						framelayoutTeamInfo.setBackgroundResource(R.drawable.ws_screen6c_arms_and_sides_right);
						showStretchs();

						break;
						
					case 3:
						framelayoutTeamInfo.setBackgroundResource(R.drawable.ws_screen6d_arms_back_left);
						showStretchs();

						break;
					case 4:
						framelayoutTeamInfo.setBackgroundResource(R.drawable.ws_screen6g_legs_right);
						showStretchs();

						break;
					case 5:
						framelayoutTeamInfo.setBackgroundResource(R.drawable.ws_screen_6i_quads_right);
						showStretchs();

						break;
					case 6:
						framelayoutTeamInfo.setBackgroundResource(R.drawable.ws_screen6h_quads_left);
						showStretchs();

						break;
					case 7:
						framelayoutTeamInfo.setBackgroundResource(R.drawable.ws_screen6j_joke_stretch);
						showStretchs();

						break;
					case 8:
						framelayoutTeamInfo.setBackgroundResource(R.drawable.ws_screen_6a_wrists_back);
						showStretchs();

						break;
					case 9:
						framelayoutTeamInfo.setBackgroundResource(R.drawable.ws_screen_6e_arms_back_right_screen);
						showStretchs();

						break;
					case 10:
						framelayoutTeamInfo.setBackgroundResource(R.drawable.ws_screen_6f_legs_left);
						showStretchs();

						break;
						
					}
					
					
				}
				
				if((seconds2 == tenSecondsStretches)  && (seconds2 >=45))
				{
					setUpTimeStretchs();
				}
				
				
				if((seconds2 == 15) && (seconds2 >5))
				{
					framelayoutTeamInfo.setBackgroundResource(R.drawable.ws_screen7tips_sheet_up_screen_only);
				}
			}
			
			

			@Override
			public void onFinish() {
				countDown.setText("GO!");
				Intent intent = new Intent(Start.this, MainActivity.class);
				startActivity(intent); // after the countDown a new Race is started
			}

		}.start();

	}

	public void newRace(View view)
	{
		Log.d("Google", "New Race called ************");
		if(joinRace.getText() == "Leave race"){
			countDownTimer.cancel();
			Intent intent = new Intent(this, LoginScreen.class); // intent of the app to												
			startActivity(intent);
		
	}else
	{
		Intent intent = new Intent(this, MainActivity.class); // intent of the app to call another activity
		startActivity(intent);
	}
		}

	
public void showTeamInfo(View view)
{
	teamInfoBool = true;
	layout.setBackgroundResource(R.drawable.wsbackground);
	textViewTips.setVisibility(View.INVISIBLE);
	waves.setVisibility(View.VISIBLE);
	framelayoutTeamInfo.setVisibility(View.VISIBLE);
	equalSymbol.setVisibility(View.VISIBLE);
	joinRace.setVisibility(View.GONE);
	
	
	layoutParams.topMargin = 265;
	waves.setLayoutParams(layoutParams);
	
	layoutParamsTextView.topMargin = 40;
	layoutParamsTextView.gravity = Gravity.CENTER_HORIZONTAL;
	countDown.setLayoutParams(layoutParamsTextView);
	
	layoutParamsTextView.topMargin = 25;
	layoutParamsTextView.gravity = Gravity.CENTER_HORIZONTAL;
	nextRace.setLayoutParams(layoutParamsTextView);
	nextRace.setTextSize(15);
	
}
	
private void hideTeamInfo()
{
	countStretch.setVisibility(View.GONE);
	layout.setBackgroundResource(R.drawable.background);
	teamInfoBool = false;
	textViewTips.setVisibility(View.VISIBLE);
	framelayoutTeamInfo.setVisibility(View.INVISIBLE);
	joinRace.setVisibility(View.VISIBLE);
	
	waves.setVisibility(View.GONE);
	
	equalSymbol.setVisibility(View.INVISIBLE);
	

	
	layoutParamsTextView.topMargin = 165;
	layoutParamsTextView.gravity = Gravity.CENTER_HORIZONTAL;
	countDown.setLayoutParams(layoutParamsTextView);
	
	
	layoutParamsTextView.topMargin = 65;
	layoutParamsTextView.gravity = Gravity.CENTER_HORIZONTAL;
	nextRace.setLayoutParams(layoutParamsTextView);
	nextRace.setTextSize(25);
	
	
}

private void initializeObjs()
{
	countStretch = (TextView) findViewById(R.id.textViewsecondsStretch);
	layout = (FrameLayout) findViewById(R.id.startRaceLayout);
	textViewTips = (TextView) findViewById(R.id.textViewTips);
	joinRace = (Button) findViewById(R.id.buttonJoinRace);
	countDown = (TextView) findViewById(R.id.textViewTimer);
	textViewMyTeam = (TextView) findViewById(R.id.textViewMyTeams);
	textViewClickOnTeam = (TextView) findViewById(R.id.textViewClickOnTeamName);
	framelayoutTeamInfo = (FrameLayout) findViewById(R.id.teamInfoLayout);
	waves = (ImageView) findViewById(R.id.imageViewwaves_startScreen);
	equalSymbol = (TextView) findViewById(R.id.textViewEqualSymbol);
	nextRace = (TextView) findViewById(R.id.textViewNextRace);
	layoutParams = new LayoutParams(layoutParams.WRAP_CONTENT,layoutParams.WRAP_CONTENT);
	layoutParamsTextView = new LayoutParams(layoutParams.WRAP_CONTENT,layoutParams.WRAP_CONTENT);
}

private void showStretchs()
{
	teamInfoBool = true; 
	layout.setBackgroundResource(R.drawable.wsbackground);
	textViewTips.setVisibility(View.GONE);
	
	layoutParams.topMargin = 265;
	waves.setLayoutParams(layoutParams);
	waves.setVisibility(View.VISIBLE);
	
	equalSymbol.setVisibility(View.INVISIBLE);
	
	textViewMyTeam.setVisibility(View.GONE);
	textViewClickOnTeam.setVisibility(View.GONE);
	
	
	layoutParamsTextView.topMargin = 35;
	layoutParamsTextView.gravity = Gravity.CENTER_HORIZONTAL;
	countDown.setLayoutParams(layoutParamsTextView);
	
	layoutParamsTextView.topMargin = 25;
	layoutParamsTextView.gravity = Gravity.CENTER_HORIZONTAL;
	nextRace.setLayoutParams(layoutParamsTextView);
	nextRace.setTextSize(15);
	
	countStretches +=1;
	timetoStretches -=15;
	
}

private void setUpTimeStretchs()
{
	
	
	countDownStretches = new CountDownTimer(10000, 1000) {
		
		

		@Override
		public void onTick(long millisUntilFinished) {
			countStretch.setVisibility(View.VISIBLE);
			double tenSeconds = (millisUntilFinished/1000);
			
			countStretch.setText("" + tenSeconds);
			
		}
		
		@Override
		public void onFinish() {
			countStretch.setVisibility(View.GONE);
			tenSecondsStretches -=5;
		}
	};
	
}
}
