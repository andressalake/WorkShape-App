package com.qwantech.workshape;

import android.app.Activity;
import android.os.Bundle;

//import com.example.projetotest.R;
import com.facebook.*; // import things needed form Facebook
import com.facebook.android.Facebook;
import com.facebook.model.*;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
/*22/may/2014
 * 
 * this classes allows the user to login using the Facebook account
 * 
 * 
 * */
public class StartActivityFacebook extends Activity{
	//this class is called when the user logs in using facebook
	private static String userNameFacebook;
	private static String userID;
    private static String FACEBOOK_UID = "fbUID";
    private static String statusFacebook;


	ServerConnection serverObj ;
	
	
	//MainObj
	MainActivity mainObj = new MainActivity();
	private Facebook FB;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//start Facebook Login
		
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			//callback when session changes state
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				// TODO Auto-generated method stub
				if(session.isOpened())
				{
					//make request to the  /me API
					Request.newMeRequest(session, new Request.GraphUserCallback(){
						
						//callback after Graph API response with user object
						@Override
						public void onCompleted(GraphUser user, Response reponse){
							//replacing the name with the user's name
							if(user !=null)
							{
								serverObj = new ServerConnection();
								setContentView(R.layout.login_screen_avatar);
								userNameFacebook = user.getName();
								userID = user.getId();  // get UserID from Facebook Account
								Log.d("FaceClass", "user number" + userID);
								serverObj.postUser(userID,FACEBOOK_UID); //post to the server the currentID
								login();
								
							}
							
							
						}
	
					}).executeAsync();
					
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data); // this manages mych of the process of authenticating
		
	}
	
	
	public String getUserIdFacebook()
	{
		return userID;
		
	}
	
	public String getStatusFacebook()
	{
	    Session session = Session.getActiveSession();
	    if (session != null && (session.isOpened() || session.isClosed()) ) {
	     return  session.getState().toString();
	       
	    }else
	    	
	    	return "Closed";
	    
	    
	}
	
	public void login()
	{
	
		mainObj.setCurrentUserID(userID);// send user ID to MainActivity, so It can get the currentID, thinking if I need it

	}
	
}
