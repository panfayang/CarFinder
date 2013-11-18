/**
 * @author panfayang
 * 
 * This class displays map and the marker, if there is one.
 */
package com.example.carfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CarMap extends FragmentActivity { 
	GoogleMap mMap;
	private String mKeyCurLat;
	private String mKeyCurLng;
	private String mKeyCarLat;
	private String mKeyCarLng;

	SharedPreferences mPrefs;
	SharedPreferences.Editor mEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_map);
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mPrefs = this.getSharedPreferences("com.example.carfinder", Context.MODE_PRIVATE);

		mKeyCarLat = "carLocationLat";
		mKeyCarLng = "carLocationLng";
		mKeyCurLat = "currentLocationLat";
		mKeyCurLng = "currentLocationLng";

		if (mMap!=null){
			mMap.setMyLocationEnabled(true);

			double curLat = Double.valueOf(mPrefs.getString(mKeyCurLat, "0"));
			double curLng = Double.valueOf(mPrefs.getString(mKeyCurLng, "0"));

			LatLng curLoc = new LatLng(curLat,curLng);		
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLoc, 15));

			double carLat = Double.valueOf(mPrefs.getString(mKeyCarLat, "0"));
			double carLng = Double.valueOf(mPrefs.getString(mKeyCarLng, "0"));
			if (carLng!=0.0){
				LatLng carLoc = new LatLng(carLat,carLng);

				mMap.addMarker(new MarkerOptions()
				.position(carLoc)
				.title("Here's your car"));
			}
		}
	}

}
