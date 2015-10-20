package com.ramostechie.android.taskmanager;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/*
http://wptrafficanalyzer.in/blog/android-geocoding-showing-user-input-location-on-google-map-android-api-v2/
http://sunil-android.blogspot.com.es/2013/07/google-map-android-api-v2-with-location.html
http://wptrafficanalyzer.in/blog/locating-user-input-address-in-google-maps-android-api-v2-with-geocoding-api/
https://developers.google.com/maps/documentation/android/start#install_the_android_sdk
*/

public class AddLocationMapActivity extends Activity implements OnMapReadyCallback {

	public static final String ADDRESS_RESULT = "address";
	//private static final String TAG="AddLocationMapActivity";
	
	private EditText addressText;
	private Button mapLocationButton;
	private Button useLocationButton;
	private MapFragment mapFragment;
	private Address address;
		
	private GoogleMap mMap;
    private UiSettings mUiSettings;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.add_location);
		setUpViews();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMap = mapFragment.getMap();
		mMap.setMyLocationEnabled(true);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMap = mapFragment.getMap();
		mMap.setMyLocationEnabled(false);
	}

	@Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        
        //mMap.setPadding(0, 0, 300, 0);       
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setMapToolbarEnabled(true);
        /*mUiSettings.setCompassEnabled(isChecked(R.id.compass_toggle));
        mUiSettings.setMyLocationButtonEnabled(isChecked(R.id.mylocationbutton_toggle));
        mMap.setMyLocationEnabled(isChecked(R.id.mylocationlayer_toggle));
        mUiSettings.setScrollGesturesEnabled(isChecked(R.id.scroll_toggle));
        mUiSettings.setZoomGesturesEnabled(isChecked(R.id.zoom_gestures_toggle));
        mUiSettings.setTiltGesturesEnabled(isChecked(R.id.tilt_toggle));
        mUiSettings.setRotateGesturesEnabled(isChecked(R.id.rotate_toggle));*/

        CircleOptions circleoptions = new CircleOptions()
        .center(new LatLng(37.45, -122.0))
        .strokeWidth(0)
        .fillColor(Color.GRAY)
        .radius(2000.5);

        PolygonOptions polygonoptions = new PolygonOptions()
        .fillColor(Color.YELLOW)
        .strokeWidth(0)
        .add(new LatLng(37.45, -122.1))
        .add(new LatLng(37.55, -122.1))  // North of the previous point, but at the same longitude
        .add(new LatLng(37.55, -122.3))  // Same latitude, and 30km to the west
        .add(new LatLng(37.45, -122.3))  // Same longitude, and 16km to the south
        .add(new LatLng(37.45, -122.1));

        PolylineOptions rectOptions = new PolylineOptions()//KML file
        .add(new LatLng(37.35, -122.0))
        .add(new LatLng(37.45, -122.0))  // North of the previous point, but at the same longitude
        .add(new LatLng(37.45, -122.2))  // Same latitude, and 30km to the west
        .add(new LatLng(37.35, -122.2))  // Same longitude, and 16km to the south
        .add(new LatLng(37.35, -122.0)); // Closes the polyline.

        // Set the rectangle's color to red
        rectOptions.color(Color.RED);

        // Get back the mutable Polyline
        // Polyline polyline = mMap.addPolyline(rectOptions);
        
        mMap.addCircle(circleoptions);
        mMap.addPolygon(polygonoptions);
        mMap.addPolyline(rectOptions);

    }


	private void setUpViews() {
		addressText = (EditText)findViewById(R.id.task_address);
		mapLocationButton = (Button)findViewById(R.id.map_location_button);
		useLocationButton = (Button)findViewById(R.id.use_this_location_button);
		
		useLocationButton.setEnabled(false);
		
		mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		
		mapLocationButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mapCurrentAddress();
			}
		});
		
		useLocationButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (null != address) {
					Intent intent = new Intent();
					intent.putExtra(ADDRESS_RESULT, address);
					setResult(RESULT_OK, intent);
				}
				
				finish();
			}
		});
		
	}
	
	protected void mapCurrentAddress()
	{
		String addressString = addressText.getText().toString();
		
		Geocoder g = new Geocoder(this);
		
		List<Address> addresses;
		
		try {
			addresses = g.getFromLocationName(addressString, 1);
			if (addresses.size() > 0) {
				address = addresses.get(0);
				
				mMap = mapFragment.getMap();
				LatLng latlon = new LatLng(address.getLatitude(), address.getLongitude());

		        //mMap.setMyLocationEnabled(true);
		        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon, 15));
		        //mMap.setTrafficEnabled(true);
		        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

		        mMap.addMarker(new MarkerOptions()
		                //.title("Sydney")
		               // .snippet("The most populous city in Australia.")
		                .position(latlon));
				/*Log.v(TAG+"-mapCurrentAddress",addressString);
				Log.d(TAG+"-mapCurrentAddress",addressString);
				Log.e(TAG+"-mapCurrentAddress",addressString);
				Log.i(TAG+"-mapCurrentAddress",addressString);
				Log.w(TAG+"-mapCurrentAddress",addressString);*/
				
				useLocationButton.setEnabled(true);
			}
			else{
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		//address.setAddressLine(0, addressString);
		
		
		//LatLng sydney = new LatLng(lat, lng);
		//LatLng sydney = new LatLng(-33.867, 151.206);
		
		//mMap = mapFragment.getMap();

        //mMap.setMyLocationEnabled(true);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        //mMap.addMarker(new MarkerOptions()
                //.title("Sydney")
               // .snippet("The most populous city in Australia.")
               // .position(sydney));
		
		//Log.v(TAG+"-mapCurrentAddress",addressString);
		//console.log("TAG+"-mapCurrentAddress + addressString);
		
		
		/*Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		try 
        {
            List<Address> addresses = geoCoder.getFromLocationName(addressString , 1);
            if (null != addresses && addresses.size() > 0) 
            {            
                GeoPoint p = new GeoPoint(
                        (int) (addresses.get(0).getLatitude() * 1E6), 
                        (int) (addresses.get(0).getLongitude() * 1E6));

                lat=p.getLatitudeE6()/1E6;
                lng=p.getLongitudeE6()/1E6;
            	*/
                
/*
            }
            else{
            	lat=-33.867;
                lng=151.206;
            }
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }*/
	}
	

}
