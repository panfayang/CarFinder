/**
 * @author panfayang
 * 
 * This is project 3 for Kalamazoo College Mobile Computing Fall 2013 class.
 * 
 * This app aims to locate your car by displaying a saved location and your 
 * current location.
 * 
 * This app displays your current location. If you step out of our car, you 
 * can update the car location, so you can find it later.
 * 
 * This class is the main class.
 */
package com.example.carfinder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class MainActivity extends FragmentActivity 
implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener

{

	private String mKeyCarLat;
	private String mKeyCarLng;

	private String mKeyCurLat;
	private String mKeyCurLng;


	LocationClient mLocationClient;
	Location mCurrentLocation;

	SharedPreferences mPrefs;
	SharedPreferences.Editor mEditor;

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mKeyCarLat = "carLocationLat";
		mKeyCarLng = "carLocationLng";
		mKeyCurLat = "currentLocationLat";
		mKeyCurLng = "currentLocationLng";

		mLocationClient = new LocationClient(this, this, this);
	}


	public void viewMap(View view){
		Intent intent = new Intent(this,CarMap.class);
		startActivity(intent);
	}

	public static class ErrorDialogFragment extends DialogFragment{
		private Dialog mDialog;

		public ErrorDialogFragment(){
			super();
			mDialog = null;
		}

		public void setDialog(Dialog dialog){
			mDialog = dialog;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch (requestCode){
		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			switch (resultCode){
			case Activity.RESULT_OK:
				break;
			}
		}
	}

	private boolean servicesConnected(){
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == resultCode){
			Log.d("Location Updates", "Google Play Services is available");

			return true;
		}else{
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode,this,CONNECTION_FAILURE_RESOLUTION_REQUEST);

			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment =
						new ErrorDialogFragment();
				// Set the dialog in the DialogFragment
				errorFragment.setDialog(errorDialog);
				// Show the error dialog in the DialogFragment
				errorFragment.show(getSupportFragmentManager(),
						"Location Updates");
			}
			return false;
		}
	}

	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		getCurLoc(null);
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects.
		 * If the error has a resolution, try sending an Intent to
		 * start a Google Play services activity that can resolve
		 * error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(
						this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Connect the client.
		mLocationClient.connect();
	}

	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		mLocationClient.disconnect();
		super.onStop();
	}

	public void getCarLoc(View view){
		mCurrentLocation = mLocationClient.getLastLocation();
		double carLat = mCurrentLocation.getLatitude();
		double carLng = mCurrentLocation.getLongitude();
		

		mPrefs = this.getSharedPreferences("com.example.carfinder", Context.MODE_PRIVATE);
		mPrefs.edit().putString(mKeyCarLat, Double.toString(carLat)).commit();
		mPrefs.edit().putString(mKeyCarLng, Double.toString(carLng)).commit();

	}

	public void getCurLoc(View view){
		mCurrentLocation = mLocationClient.getLastLocation();
		double curLat = mCurrentLocation.getLatitude();
		double curLng = mCurrentLocation.getLongitude();

		mPrefs = this.getSharedPreferences("com.example.carfinder", Context.MODE_PRIVATE);

		mPrefs.edit().putString(mKeyCurLat, Double.toString(curLat)).commit();
		mPrefs.edit().putString(mKeyCurLng, Double.toString(curLng)).commit();
		
		((TextView) findViewById(R.id.latitude)).setText(Double.toString(curLat));
		((TextView) findViewById(R.id.longitude)).setText(Double.toString(curLng));

	}

} 
