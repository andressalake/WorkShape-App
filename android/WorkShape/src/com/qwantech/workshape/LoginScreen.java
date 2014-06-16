package com.qwantech.workshape;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class LoginScreen extends Activity implements OnClickListener {
	
	boolean verify = true;
	private ServerConnection serverObj;
	private MainActivity mainObj;
	private  StartActivityFacebook facebookObj;

	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set the content View to splashLayout
		setContentView(R.layout.login_screen);  //draw background race track 
		
		findViewById(R.id.sign_in_button).setOnClickListener(this);
		
		
	
	}
	
	@Override
	public void onClick(View view)
	{
		if(view.getId() == R.id.sign_in_button)
		{
			loginUsingGoole();
		}
	}
	 
	
public void loginUsingFacebook(View view) // view as parameter, to execute when
	
	{
		//verifyLogin(verify);
	Intent intent = new Intent(this, StartActivityFacebook.class); // intent of the app to												// call another activity
	startActivity(intent); // start activity
		

	}

public void loginUsingGoole()
{

	
	Intent intent = new Intent(this, Google.class); // intent of the app to												// call another activity
	startActivity(intent);
}

public void loginUsingTwitter(View view)
{
	Intent intent = new Intent(this, StartActivityTwitter.class); // intent of the app to												// call another activity
	startActivity(intent); 
}

public void verifyLogin(boolean isInvited)  //This function receives a boolean to check if is false(not invited) or true(invited)
{
		if(isInvited == true)
		{
			Intent intent = new Intent(this, StartActivityFacebook.class); // intent of the app to												// call another activity
			startActivity(intent); // start activity of the intentIntent intent = new Intent(this, StartActivityFacebook.class); // intent of the app to
		}
			else
				
		if(isInvited == false){
		
			
			Intent intent2 = new Intent(this, StopGame.class); // intent of the app to call another activity
			startActivity(intent2); // start activity of the intent
	   }
}


	
	




}


