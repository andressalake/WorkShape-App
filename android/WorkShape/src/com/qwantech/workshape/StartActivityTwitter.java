package com.qwantech.workshape;

import twitter4j.Twitter;

import android.app.Activity;
import android.os.Bundle;
//import com.example.projetotest.R;


import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
/*
 * This class allows the login using Twitter account
 * 23/may/2014
 */
public class StartActivityTwitter extends Activity {
Twitter twitter;
RequestToken requestToken;
//Please put the values of consumerKy and consumerSecret of your app 
public final static String consumerKey = "mfp8Lwn6AepMqp7qSvJcr9et7"; // "your key here";
public final static String consumerSecret = "ni6mHu5CFWN5aaR2dUNSzgbJJSaZKATZoZISSOiT3mjOUaIr2x"; // "your secret key here";
private final String CALLBACKURL = "http://www.qwantech.com/site/";  //Callback URL that tells the WebView to load this activity when it finishes with twitter.com. (see manifest)


/*
 * Calls the OAuth login method as soon as its started
 */
@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.login_screen_avatar );
	OAuthLogin();
}

/*
 * - Creates object of Twitter and sets consumerKey and consumerSecret
 * - Prepares the URL accordingly and opens the WebView for the user to provide sign-in details
 * - When user finishes signing-in, WebView opens your activity back
 */
void OAuthLogin() {
	try {
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(consumerKey, consumerSecret);
		requestToken = twitter.getOAuthRequestToken(CALLBACKURL);
		String authUrl = requestToken.getAuthenticationURL();
		this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
				.parse(authUrl)));
	} catch (TwitterException ex) {
		Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		Log.e("in Main.OAuthLogin", ex.getMessage());
	}
}


/*
 * - Called when WebView calls your activity back.(This happens when the user has finished signing in)
 * - Extracts the verifier from the URI received
 * - Extracts the token and secret from the URL 
 */
@Override
protected void onNewIntent(Intent intent) {
	super.onNewIntent(intent);
	Uri uri = intent.getData();
	try {
		String verifier = uri.getQueryParameter("oauth_verifier");
		AccessToken accessToken = twitter.getOAuthAccessToken(requestToken,
				verifier);
		String token = accessToken.getToken(), secret = accessToken
				.getTokenSecret();

	} catch (TwitterException ex) {
		Log.e("Main.onNewIntent", "" + ex.getMessage());
	}




	}
}
 /*
public class StartActivityTwitter extends Activity {
    // Constants
   
    static String TWITTER_CONSUMER_KEY = "mfp8Lwn6AepMqp7qSvJcr9et7";
    static String TWITTER_CONSUMER_SECRET = "ni6mHu5CFWN5aaR2dUNSzgbJJSaZKATZoZISSOiT3mjOUaIr2x";
    static String TWITTER_CONSUMER_TOKEN = "204455055-vSJo7vUpUB8xIYzRYVJZ7s9jJ3w9F1ZNO93DNFRJ";
    static String TWITTER_CONSUMER_TOKEN_SECRET = "e9IDJZQRiXSfOHcqOu3ZSK1jezSpR1DWIUuXPT3vRggAe";

 
    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
 
    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
 
    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
 
   
 //islooged?
    
    private static boolean isLogged = false;
    // Progress dialog
    ProgressDialog pDialog;
 
    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;
     
    // Shared Preferences
    private static SharedPreferences mSharedPreferences;
     
    // Internet Connection detector
    private ConnectionDetectorTwitter cd;
     
    // Alert Dialog Manager
    AlertDialogManagerTwitter alert = new AlertDialogManagerTwitter();
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
         
        cd = new ConnectionDetectorTwitter(getApplicationContext());
 
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(StartActivityTwitter.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
         
        // Check if twitter keys are set
        if(TWITTER_CONSUMER_KEY.trim().length() == 0 || TWITTER_CONSUMER_SECRET.trim().length() == 0){
            // Internet Connection is not present
            alert.showAlertDialog(StartActivityTwitter.this, "Twitter oAuth tokens", "Please set your twitter oauth tokens first!", false);
            // stop executing code by return
            return;
        }

 
        // Shared Preferences
        mSharedPreferences = getApplicationContext().getSharedPreferences(
                "MyPref", 0);

 
        /** This if conditions is tested once is
         * redirected from twitter page. Parse the uri to get oAuth
         * Verifier
         * */
   /*   if (!isLogged) {
            Uri uri = getIntent().getData();
            if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
                // oAuth verifier
                String verifier = uri
                        .getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
 
                try {
                    // Get the access token
                    AccessToken accessToken = twitter.getOAuthAccessToken(
                            requestToken, verifier);
 
                    // Shared Preferences
                    Editor e = mSharedPreferences.edit();
 
                    // After getting access token, access token secret
                    // store them in application preferences
                    e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
                    e.putString(PREF_KEY_OAUTH_SECRET,
                            accessToken.getTokenSecret());
                    // Store login status - true
                    e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
                    e.commit(); // save changes
 
                    Log.e("Twitter OAuth Token", "> " + accessToken.getToken());
                     
                    isLogged = true;
                    // Getting user details from twitter
                    // For now i am getting his name only
                    long userID = accessToken.getUserId();
                    User user = twitter.showUser(userID);
                    String username = user.getName();
                     
                    // Displaying in xml ui
                    TextView welcome = new TextView(this);
                    welcome.findViewById(R.id.textViewWelcome);
                    welcome.setText("<b>Welcome " + username + "</b>");
                } catch (Exception e) {
                    // Check log for login errors
                    Log.e("Twitter Login Error", "> " + e.getMessage());
                    isLogged = false;
                }
            }
        }
 
    } */ //final oncreation
 
  /*  /**
     * Function to login twitter
     * */
/*    public void loginToTwitter() {
        // Check if already logged in
        if (!isLogged) { // erro
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            builder.setOAuthAccessToken(TWITTER_CONSUMER_TOKEN);
            builder.setOAuthAccessTokenSecret(TWITTER_CONSUMER_TOKEN_SECRET);
            
            Configuration configuration = builder.build();
             
            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();
 
            try {
                requestToken = twitter
                        .getOAuthRequestToken(TWITTER_CALLBACK_URL);
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse(requestToken.getAuthenticationURL())));
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {
            // user already logged into twitter
            Toast.makeText(getApplicationContext(),
                    "Already Logged into twitter", Toast.LENGTH_LONG).show();
            isLogged= true;
        }
    }


 

 /*
    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     * */
  /*  private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
    	Log.d("Tag maldita", "Pref_key-twitter_login" + PREF_KEY_TWITTER_LOGIN);
    
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false); //erro
        
    } */
 
  //  protected void onResume() {
    //    super.onResume();
    //}
 
//}


  