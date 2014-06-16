package com.qwantech.workshape;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class StopGame extends Activity{
	
	private String invitationStatusString;
	 
	private TextView textView_invitationStatus;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//set the content View to splashLayout
		setContentView(R.layout.login_screen);  
		
	TextView textView_invitationStatus = (TextView)findViewById(R.id.textViewWelcome);
		textView_invitationStatus.setText(" Stop!   " +
				"get an invitation  from your ORG to continue. ");
	
		}
}
