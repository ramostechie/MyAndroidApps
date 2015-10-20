package com.ramostechie.android.taskmanager.adapter;

import java.util.ArrayList;

import com.ramostechie.android.taskmanager.R;
import com.ramostechie.android.taskmanager.tasks.Task;
import com.ramostechie.android.taskmanager.views.TaskListItem;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TaskListAdapter extends BaseAdapter {
	
	private ArrayList<Task> tasks;
	private Context context;
	private ArrayList<Task> filteredTasks;
	private ArrayList<Task> unfilteredTasks;
	
	public TaskListAdapter(ArrayList<Task> tasks, Context context) {
		super();
		this.tasks = tasks;
		this.unfilteredTasks = tasks;
		this.context = context;
	}

	@Override
	public int getCount() {
		return tasks.size();
	}

	@Override
	public Task getItem(int position) {
		return (null == tasks)? null: tasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TaskListItem tli;
		
		if (null==convertView) {
			tli = (TaskListItem)View.inflate(context, R.layout.task_list_item, null);			
		}
		else{
			tli = (TaskListItem)convertView;			
		}
		tli.setTask(tasks.get(position));
		return tli;
	}

	public void forceReload() {
		notifyDataSetChanged();		
	}

	public void toggleTaskCompleteAtPosition(int position) {
		Task t = tasks.get(position);
		t.toggleComplete();
		notifyDataSetChanged();
	}

	public Long[] removeCompletedTasks() {
		ArrayList<Task> completedTasks = new ArrayList<Task>();
		ArrayList<Long> completedIds = new ArrayList<Long>();
		for (Task task : tasks){
			if(task.isComplete()){
				completedIds.add(task.getId());
				completedTasks.add(task);
			}
		}
		tasks.removeAll(completedTasks);
		notifyDataSetChanged();
		
		
		return completedIds.toArray(new Long[]{}); 
	}

	public void filterTasksByLocation(Location latestLocation,long locationFilterDistance) {
		filteredTasks = new ArrayList<Task>();
		for (Task task : tasks) {
			if (task.hasLocation() && taskIsWithinGeofence(task, latestLocation, locationFilterDistance)) {
				filteredTasks.add(task);
			}
		}
		tasks = filteredTasks;
		notifyDataSetChanged();
		
	}

	private boolean taskIsWithinGeofence(Task task, Location latestLocation,long locationFilterDistance) {
		float[] distanceArray = new float[1];
		Location.distanceBetween(
				task.getLatitude(),
				task.getLongitude(),
				latestLocation.getLatitude(),
				latestLocation.getLongitude(),
				distanceArray
			);
		return (distanceArray[0] < locationFilterDistance);
	}
	
	public void removeLocationFilter() {
		tasks = unfilteredTasks;
		notifyDataSetChanged();
	}

}
