package com.ramostechie.android.taskmanager;

import java.util.ArrayList;

import com.ramostechie.android.taskmanager.tasks.Task;
import com.ramostechie.android.taskmanager.tasks.TasksSQLiteOpenHelper;
import static com.ramostechie.android.taskmanager.tasks.TasksSQLiteOpenHelper.*;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//add to the manifest android:name=".TaskManagerApplication"
public class TaskManagerApplication extends Application {

	private ArrayList<Task> currentTasks;
	private SQLiteDatabase database;
	
	@Override
	public void onCreate() {
		super.onCreate();
		TasksSQLiteOpenHelper helper = new TasksSQLiteOpenHelper(this);
		database = helper.getWritableDatabase();
		if (null == currentTasks) {
			loadTasks();
		}
	}

	private void loadTasks() {
		currentTasks = new ArrayList<Task>();
		Cursor tasksCursor = database.query(
				TASKS_TABLE, 
				new String[] {TASK_ID, TASK_NAME, TASK_COMPLETE,TASK_DESCRIPTION, TASK_ADDRESS, TASK_LATITUDE, TASK_LONGITUDE}, 
				null, null, null, null, String.format("%s,%s", TASK_COMPLETE, TASK_NAME,TASK_DESCRIPTION));
		tasksCursor.moveToFirst();
		Task t;
		if (! tasksCursor.isAfterLast()) {
			do {
				long id = tasksCursor.getLong(0);
				String name  = tasksCursor.getString(1);
				String boolValue = tasksCursor.getString(2);
				String description = tasksCursor.getString(3);
				String address = tasksCursor.getString(4);
				float latitude = tasksCursor.getFloat(5);
				float longitude = tasksCursor.getFloat(6);
				boolean complete = Boolean.parseBoolean(boolValue);
				t = new Task(name);
				t.setId(id);
				t.setComplete(complete);
				t.setDescription(description);
				t.setAddress(address);
				t.setLatitude(latitude);
				t.setLongitude(longitude);
				currentTasks.add(t);
			} while (tasksCursor.moveToNext());
		}
		
		tasksCursor.close();
		
	}

	public void setCurrentTasks(ArrayList<Task> currentTasks) {
		this.currentTasks = currentTasks;
	}

	public ArrayList<Task> getCurrentTasks() {
		return currentTasks;
	}
	
	public void addTask(Task t) {
		assert(null != t);
		
		ContentValues values = new ContentValues();
		values.put(TASK_NAME, t.getName());
		values.put(TASK_COMPLETE, Boolean.toString(t.isComplete()));
		values.put(TASK_DESCRIPTION, t.getDescription());
		values.put(TASK_ADDRESS, t.getAddress());
		values.put(TASK_LATITUDE, t.getLatitude());
		values.put(TASK_LONGITUDE, t.getLongitude());

		t.setId(database.insert(TASKS_TABLE, null, values));
		currentTasks.add(t);
	}
	
	public void saveTask(Task t) {
		assert(null != t);
		ContentValues values = new ContentValues();
		values.put(TASK_NAME, t.getName());
		values.put(TASK_COMPLETE, Boolean.toString(t.isComplete()));
		values.put(TASK_DESCRIPTION, t.getDescription());
		values.put(TASK_ADDRESS, t.getAddress());
		values.put(TASK_LATITUDE, t.getLatitude());
		values.put(TASK_LONGITUDE, t.getLongitude());
		
		long id = t.getId();
		String where = String.format("%s = %d", TASK_ID, id);

		database.update(TASKS_TABLE, values, where, null);
	}
	
	public void deleteTasks(Long[] ids) {
		StringBuffer idList = new StringBuffer();
		for (int i=0; i<ids.length; i++) {
			idList.append(ids[i]);
			if (i < ids.length - 1) {
				idList.append(",");
			}
		}
		String where = String.format("%s in (%s)", TASK_ID, idList);
		database.delete(TASKS_TABLE, where, null);
	}

}
