package com.ramostechie.android.taskmanager;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditLocationMapActivity extends Activity implements OnMapReadyCallback {

	public static final String ADDRESS_RESULT = "address";
	
	private EditText addressText;
	private Button mapLocationButton;
	private View useLocationButton;
	private MapFragment mapFragment;
	private Address address;
	private Address oldaddress;

	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_location);
		oldaddress = getIntent().getParcelableExtra("location");
		setUpViews();
	}

	@Override
	public void onMapReady(GoogleMap arg0) {

		if (null != oldaddress) {
		mMap = mapFragment.getMap();
		LatLng latlon = new LatLng(oldaddress.getLatitude(), oldaddress.getLongitude());

        //mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon, 15));
        //mMap.setTrafficEnabled(true);
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        mMap.addMarker(new MarkerOptions()
                //.title("Sydney")
               // .snippet("The most populous city in Australia.")
                .position(latlon));
		}
		
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

		        mMap.setMyLocationEnabled(true);
		        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon, 15));

		        mMap.addMarker(new MarkerOptions()
		                .position(latlon));
				
				useLocationButton.setEnabled(true);
			}
			else{
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
                
	}

}
