package com.qwantech.workshape;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
 //Another attempt to login using Google =] 
public class Google extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {
 
    private static final String GOOGLE_UID = "googleUID";
	private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "MainActivity";
 
    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;
 
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    
    MainActivity mainObj = new MainActivity();
    
    ServerConnection serverObj = new ServerConnection();

   
    private static String userIDGoogle; // It is stored in this variable the userID to send to the server
 
    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
 
    private boolean mSignInClicked;
 
    private ConnectionResult mConnectionResult;
 
    
    
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView( R.layout.login_screen_avatar);
 
        // Initializing google plus api client
    

        
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        

        signInWithGplus();
        
    }
 
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
 
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
 




	@Override
	public void onConnectionFailed(ConnectionResult result) {
		
	    if (!result.hasResolution()){
	        GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
	                0).show();
	        return;
	    }
	 
	    if (!mIntentInProgress) {
	        // Store the ConnectionResult for later usage
	        mConnectionResult = result;
	 
	        if (mSignInClicked) {
	            // The user has already clicked 'sign-in' so we attempt to
	            // resolve all
	            // errors until the user is signed in, or they cancel.
	            resolveSignInError();
	        }
	    }
	 
	}
	 
	@Override
	protected void onActivityResult(int requestCode, int responseCode,
	        Intent intent) {
	    if (requestCode == RC_SIGN_IN) {
	        if (responseCode != RESULT_OK) {
	            mSignInClicked = false;
	        }
	 
	        mIntentInProgress = false;
	 
	        if (!mGoogleApiClient.isConnecting()) {
	            mGoogleApiClient.connect();
	        }
	    }
	}
	 
	@Override
	public void onConnected(Bundle arg0) {
	    mSignInClicked = false;
	    Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
	 
	    // Get user's information
	    getProfileInformation();
	 
	   
	 
	}
	 
	@Override
	public void onConnectionSuspended(int arg0) {
	    mGoogleApiClient.connect();
	   
	}
	 
	

	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {
	    if (!mGoogleApiClient.isConnecting()) {
	        mSignInClicked = true;
	        resolveSignInError();
	    }
	}
	 
	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
	    if (mConnectionResult != null) {
	        try {
	            mIntentInProgress = true;
	            mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
	        } catch (SendIntentException e) {
	            mIntentInProgress = false;
	            mGoogleApiClient.connect();
	        }
	    }
	}
	/**
	 * Fetching user's information name, email, profile pic
	 * */
	private void getProfileInformation() {
		Log.d("Google class","entrou na funcao de pegar informacao de usuario________________");
	    try {
	        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
	            Person currentPerson = Plus.PeopleApi
	                    .getCurrentPerson(mGoogleApiClient);
	            
	            String personName = currentPerson.getDisplayName();
	            String personPhotoUrl = currentPerson.getImage().getUrl();
	            String personGooglePlusProfile = currentPerson.getUrl();
	            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
	            userIDGoogle = currentPerson.getId(); // get UserID to pass to the server
	           mainObj.setCurrentUserID(userIDGoogle); // send user ID to MainActivity, so It can get the currentID, thinking if I need it
	           serverObj.postUser(userIDGoogle,GOOGLE_UID); //post to the server the currentID

	           
	            Log.e(TAG, "Name: " + personName + ", plusProfile: "
	                    + personGooglePlusProfile +" UserID = " +  userIDGoogle + ", email: " + email 
	                    + ", Image: " + personPhotoUrl);
	    		Log.d("Google class","pegou informancao , deve estar impresso aqui em cima.  _____________________________" + userIDGoogle);

	 
	            // by default the profile url gives 50x50 px image only
	            // we can replace the value with whatever dimension we want by
	            // replacing sz=X
	            
	            personPhotoUrl = personPhotoUrl.substring(0,
	                    personPhotoUrl.length() - 2)
	                    + PROFILE_PIC_SIZE;
	 
	 
	        } else {
	            Toast.makeText(getApplicationContext(),
	                    "Person information is null", Toast.LENGTH_LONG).show();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void startRace(View view) //call newRace
	{
		
		Intent intent = new Intent(this, Start.class); // intent of the app to	// call another activity
		startActivity(intent);
	}

	public String getUserIDGoogle()
	{
		return userIDGoogle;
		
	}
	

}