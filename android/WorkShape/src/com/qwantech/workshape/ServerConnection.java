package com.qwantech.workshape;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/*
 * this classes connect to the Server and send information 
 * It needs to make use of Async Task to Android version 3.0 or higher, otherwise it will trow an error of NetworkOnMainThreadException
 */
public class ServerConnection extends Activity {

	private static final String TAG = "ServerConnection";
	private static String finishLine;
	
	// object to get the steps
	private static String userIDWorkShape;
	private static String userIDSocialNetwork;
	private static String kindofSocialNetwork;
	private static String stepsDelta;

	// private static String teamID;
	private static String eventID;

	// Where to send data
	// initialize URLS
	InetAddress getIP;
	private static String ADDRESS;
	private static String SERVER_URL;
	private static String POST_URL_USER;
	// private static String POST_URL_USER_ID;
	private static String POST_URL_EVENTS;
	private static String GET_URL_EVENT_ID;

	// How to send
	// The server is looking for the data to identify with certain names,
	// particular values

	private static String POST_OPTIONS_USERID = "userID";
	// private static final String POST_OPTIONS_TEAMID = "teamID";
	private static final String POST_OPTIONS_STEPS = "movements";

	// What to send

	// HTTP Objects
	HttpClient clientHttp;
	HttpPost post;
	HttpResponse responsepostRace;
	HttpGet httpGet;
	


	// Use of the class ASyncTask to avoid connection error with android 3.0 or
	// higher
	public void postUser(String userIDpost, String typeAccount) {
		this.userIDSocialNetwork = userIDpost;
		this.kindofSocialNetwork = typeAccount;
		new Connection(1).execute();

	}

	public void getEvent() {

		new Connection(2).execute();
	}

	public void postMoviments(String stepsNumberDelta2) {
		this.stepsDelta = stepsNumberDelta2;
		new Connection(3).execute();

	}

	private class Connection extends AsyncTask {

		private  int typeofFunction ;

		public Connection(int a) {
			this.typeofFunction = a;
			initializeURL();
		}

		@Override
		protected Object doInBackground(Object... params) {
		
				clientHttp = new DefaultHttpClient(); // initialize HTTP Client
			if (typeofFunction == 1) {
				postUsertoServer();
			} else if (typeofFunction == 2) {
				getEventNumberFromServer();
			} else if (typeofFunction == 3) {
				postDatatoServer();
			}


			return null;
		}

	}

	public void postDatatoServer() {
		// This function post the movements to the server
	
		POST_URL_EVENTS = SERVER_URL + "events" + "/" + eventID + "/" + "data";
		post = new HttpPost(POST_URL_EVENTS);
		// Construct data to send
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(); // do
																			// function
																			// to
																			// deal
																			// with
																			// null
																			// name
																			// and
																			// id
		nameValuePair.add(new BasicNameValuePair(POST_OPTIONS_USERID,userIDWorkShape));
		// nameValuePair.add(new BasicNameValuePair(POST_OPTIONS_TEAMID,
		// itemId));
		nameValuePair
				.add(new BasicNameValuePair(POST_OPTIONS_STEPS, stepsDelta));

		try {
			// give the objects the data
			post.setEntity(new UrlEncodedFormEntity(nameValuePair));

			// execute the post
			responsepostRace = clientHttp.execute(post);
			// Handle response

			int status = responsepostRace.getStatusLine().getStatusCode();

			Log.i(TAG, "Post Status" + status);

			// Toast.makeText(this, "Post Status" + status, Toast.LENGTH_LONG);
			// erro aq

			String jsonString = EntityUtils.toString(responsepostRace.getEntity());

			// convert the Jason object to get the userID

			 finishLine = parseJasonobj(jsonString, "complete");// return
																		// race
																		// complete
																		// ,true,
																		// or
																		// not ,
																		// false
			//String winner = parseJasonobj(jsonString, "winner"); // return
																	// winner id
			

			if (responsepostRace.getEntity() != null) {
				responsepostRace.getEntity().consumeContent(); // close before
																// getting
																// another one
			}

		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "UnsupportedEncodingException", e);

		} catch (ClientProtocolException e) {

			Log.e(TAG, "ClientProtocolException", e);

		} catch (IOException e) {

			Log.e(TAG, "IOException", e);

		}

	}

	public void postUsertoServer() { // post userID
		
		POST_OPTIONS_USERID = kindofSocialNetwork;

		post = new HttpPost(POST_URL_USER);
		// Construct data to send
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair(POST_OPTIONS_USERID,
				userIDSocialNetwork));
		
		Log.d("Google class","post user_____________________________");

		try {
			// give the objects the data
			post.setEntity(new UrlEncodedFormEntity(nameValuePair));

			// execute the post
			HttpResponse response = clientHttp.execute(post);

			// Handle response

			int status = response.getStatusLine().getStatusCode();
			Log.i(TAG, "Post Status" + status);

			// userIDWorkShape
			String jsonString = EntityUtils.toString(response.getEntity());

			// convert the Jason object to get the userID

			userIDWorkShape = parseJasonobj(jsonString, "userID");
			Log.d(TAG,
					"************************************* Entrou na clpostDat  user id: "
							+ userIDWorkShape);

			

		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "UnsupportedEncodingException", e);

		} catch (ClientProtocolException e) {

			Log.e(TAG, "ClientProtocolException", e);

		} catch (IOException e) {

			Log.e(TAG, "IOException", e);

		}	
	

	}

	public void getEventNumberFromServer() { // get Number of the event

	
		httpGet = new HttpGet(GET_URL_EVENT_ID);
		// Construct data to send

		Log.d("ServerCon",
				"Entrou funcao getEvent _________________________httget + URL"+ httpGet + GET_URL_EVENT_ID);
		try {

			// execute the get request
			HttpResponse response = clientHttp.execute(httpGet);
			Log.d("ServerCon",
					"Entrou funcao getEvent response event _____________________________"+ response);

			// Handle response
			String jsonStringEvent = EntityUtils.toString(response.getEntity());

			eventID = parseJasonobj(jsonStringEvent, "eventID");

			Log.d(TAG,
					"************************************* Entrou na clpostDat maldit Evento number : "
							+ eventID);

			int status = response.getStatusLine().getStatusCode();

			Log.i(TAG, "Post Status" + status);


		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "UnsupportedEncodingException", e);

		} catch (ClientProtocolException e) {

			Log.e(TAG, "ClientProtocolException", e);

		} catch (IOException e) {

			Log.e(TAG, "IOException", e);

		}
		
	
	}

	private String parseJasonobj(String _jsonString, String whichOBJ) {
		String id;
		JSONObject jsonObj;

		try {
			jsonObj = new JSONObject(_jsonString);
			id = jsonObj.getString(whichOBJ); // run json object to get the
												// userID

		} catch (JSONException e) {
			Log.e(TAG, "JSONException", e);
			id = "error";
		}

		return id;

	}

	private void initializeURL() {
	/*	String host = "http://localhost:8085/race";
		try {
			getIP = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}// get the my IP in each connection, change it when we have the server
		ADDRESS = getIP.getHostAddress(); */
		ADDRESS = "192.168.0.12";
		SERVER_URL = "http://" + ADDRESS + ":8085/";
		POST_URL_USER = SERVER_URL + "users";
		// private static String POST_URL_USER_ID;
		GET_URL_EVENT_ID = SERVER_URL + "events" + "/" + "next";

	}
public String getFinishLine()
{
	return finishLine;
}
}
