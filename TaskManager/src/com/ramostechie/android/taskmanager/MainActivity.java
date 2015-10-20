package com.ramostechie.android.taskmanager;

//import java.util.ArrayList;

import com.ramostechie.android.taskmanager.adapter.TaskListAdapter;
import com.ramostechie.android.taskmanager.tasks.Task;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ToggleButton;

public class MainActivity extends /*TaskManagerActivity*/ ListActivity implements LocationListener {
	
	//private TextView taskText;
	private static final long LOCATION_FILTER_DISTANCE = 200;
	private Button addButton;
	private TaskManagerApplication app;
	private TaskListAdapter adapter;
	private Button removeButton;
	private TextView locationText;
	private LocationManager locationManager;
	private Location latestLocation;
	private ToggleButton localTasksToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setUpViews();
        
        app = (TaskManagerApplication)getApplication();
        adapter = new TaskListAdapter(app.getCurrentTasks(),this);
        setListAdapter(adapter);
        //ArrayList<Task> tasks = getTaskManagerApplication().getCurrentTasks();
        
        setUpLocation();
    }
    
    private void setUpLocation() {
		locationManager = (LocationManager)	getSystemService(Context.LOCATION_SERVICE);
		
		locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                60000,
                5,
                this);
	}

	@Override
	protected void onResume() {
    	super.onResume();
    	locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                60000,
                5,
                this);
    	adapter.forceReload();
    	//showTasks();
    }
    
    @Override
	protected void onPause() {
    	locationManager.removeUpdates(this);
		super.onPause();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		adapter.toggleTaskCompleteAtPosition(position);
		Task t = adapter.getItem(position);
		app.saveTask(t);
		
	}
    
	@Override
	public void onLocationChanged(Location location) {
		latestLocation = location;
		String locationString = String.format(
				"@ %f, %f +/- %fm",
				location.getLatitude(),
				location.getLongitude(),
				location.getAccuracy());
		locationText.setText(locationString);
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onProviderDisabled(String provider) {}
    
    /*private void showTasks(){
    	ArrayList<Task> tasks = getTaskManagerApplication().getCurrentTasks();
    	StringBuffer sb = new StringBuffer();
    	for(Task t:tasks){
    		sb.append(String.format("* %s\n", t.toString()));
    	}
    	taskText.setText(sb.toString());
    }*/
    
	protected void removeCompletedTasks() {
		Long [] ids = adapter.removeCompletedTasks();
		app.deleteTasks(ids);
	}
    
    private void setUpViews() {
		//taskText = (TextView)findViewById(R.id.task_list_text);
		addButton = (Button)findViewById(R.id.add_button);
		removeButton = (Button)findViewById(R.id.remove_button);
		localTasksToggle = (ToggleButton)findViewById(R.id.show_local_tasks_toggle);
		locationText = (TextView)findViewById(R.id.location_text);
		
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
				startActivity(intent);				
			}
		});
		
		removeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				removeCompletedTasks();				
			}
		});

		localTasksToggle.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showLocalTasks(localTasksToggle.isChecked());
			}
		});
	}

	protected void showLocalTasks(boolean checked) {
		if (checked) {
			adapter.filterTasksByLocation(latestLocation, LOCATION_FILTER_DISTANCE);
		} else {
			adapter.removeLocationFilter();
		}
		
	}


}
