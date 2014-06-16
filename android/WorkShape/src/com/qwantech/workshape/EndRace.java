package com.qwantech.workshape;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EndRace extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.end_race_screen);
		
	}
	
	public void gotoTheBegginnning(View view)
	{
		Intent intent = new Intent(this, Start.class);
		startActivity(intent);
		
	}
	

}
