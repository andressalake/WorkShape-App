package com.qwantech.workshape;


//import com.example.projetotest.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity{
	
	//variables
	private static String TAG = SplashScreen.class.getName();
	private static long SLEEP_TIME = 3; // time to the splashScreen appears on the screen 

		@Override 
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			// remove titles bar from the screen

			this.requestWindowFeature(Window.FEATURE_NO_TITLE); 		
			
			//remove notification bar
			this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
			
			//set the content View to splashLayout
			setContentView(R.layout.splash_screen); 

			launchbyLink();
			
			//timer to the splash
			IntentLauncher launcher = new IntentLauncher();
			launcher.start();
			

		}
		public class IntentLauncher extends Thread{
			
			public void run()
			{
				try
				{
					Thread.sleep(SLEEP_TIME *1000);
					//appAlreadyAllowedbytheUser();
					Intent intent = new Intent(SplashScreen.this, LoginScreen.class);		
					SplashScreen.this.startActivity(intent);
					SplashScreen.this.finish();

				}
				catch(Exception e)
				{
					Log.e(TAG, e.getMessage());
					
				}
				
				
			
			}
			
			
		}
		
		private void launchbyLink() // This function get the intent that lauched this activity through a link
		{
		//	Intent intent = getIntent();
			//Uri openUri = intent.getData();
		}
		
	/*	private void appAlreadyAllowedbytheUser() //check if the user already allow the app 
		{
			String sessionStatusFacebook = facebookObj.getStatusFacebook();
			//do for google
			Log.d("SplashScreen", "SessionStatus FAceboos8888888888888888888888888888888888888888" + sessionStatusFacebook);
			if(sessionStatusFacebook == "OPENED") // check closed, and app not allowed, check if user allow app
			{

				Intent intent = new Intent(SplashScreen.this, MainActivity.class);
				SplashScreen.this.startActivity(intent);
				facebookObj.login();	
				SplashScreen.this.finish();
				
			}else
				{ 
			Intent intent = new Intent(SplashScreen.this, LoginScreen.class);		
			SplashScreen.this.startActivity(intent);
			SplashScreen.this.finish();
				}
	
} */
}


