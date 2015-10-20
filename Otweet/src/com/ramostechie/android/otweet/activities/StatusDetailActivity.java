package com.ramostechie.android.otweet.activities;

import java.text.SimpleDateFormat;

import com.ramostechie.android.otweet.R;

import twitter4j.GeoLocation;
import twitter4j.Status;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class StatusDetailActivity extends Activity {
	
	public static final String STATUS = "status";
	  
	private TextView userNameText;
	private TextView statusText;
	private TextView createdAtText;
	private TextView replyToText;
	private TextView geoText;
	private Status status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.status_detail);
	    setUpViews();
	    status = (Status)getIntent().getSerializableExtra(STATUS);
	    showStatus();
	}

	private void showStatus() {
	    userNameText.setText(status.getUser().getName());
	    statusText.setText(status.getText());
	    
	    SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
	    
	    String createdAt = getResources().getString(
	        R.string.created_at_format,
	        df.format(status.getCreatedAt()) + " GMT");
	    createdAtText.setText(createdAt);
	    
	    if (null != status.getInReplyToScreenName()) {
	      String replyTo = getResources().getString(
	          R.string.in_reply_to_format,
	          status.getInReplyToScreenName());
	      replyToText.setText(replyTo);
	      replyToText.setVisibility(View.VISIBLE);
	    }
	    if (null != status.getGeoLocation()) {
	      GeoLocation location = status.getGeoLocation();
	      String geoLocation = getResources().getString(
	          R.string.geo_location_format,
	          location.getLatitude(), location.getLongitude());
	      geoText.setText(geoLocation);
	      geoText.setVisibility(View.VISIBLE);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) { //class when pressing menu button on phone the first time only
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) { //called after oncreateoptionsmenu, and every time an option is pressed
	    return MenuHelper.openActivityFromMenuItem(this, item);
	}

	private void setUpViews() {
	    userNameText = (TextView)findViewById(R.id.status_user_name_text);
	    statusText = (TextView)findViewById(R.id.status_text);
	    createdAtText = (TextView)findViewById(R.id.created_at_text);
	    replyToText = (TextView)findViewById(R.id.reply_to_text);
	    geoText = (TextView)findViewById(R.id.geo_text);
	}

}
