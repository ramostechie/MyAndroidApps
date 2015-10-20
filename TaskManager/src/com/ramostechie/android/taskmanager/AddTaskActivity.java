package com.ramostechie.android.taskmanager;

import com.ramostechie.android.taskmanager.tasks.Task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddTaskActivity extends TaskManagerActivity {

	private static final int REQUEST_CHOOSE_ADDRESS = 0;
	private EditText taskNameEditText;
	private EditText taskDescriptionEditText;
	private Button addButton;
	private Button cancelButton;
	protected boolean changesPending;
	private Address address;
	private AlertDialog unsavedChangesDialog;
	private Button addLocationButton;
	private TextView addressText;
	private Button editLocationButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_task);
		setUpViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (null == address) {
			addLocationButton.setVisibility(View.VISIBLE);
			addressText.setVisibility(View.GONE);
			editLocationButton.setVisibility(View.GONE);
		} else {
			addLocationButton.setVisibility(View.GONE);
			addressText.setVisibility(View.VISIBLE);
			addressText.setText(address.getAddressLine(0));
			editLocationButton.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (REQUEST_CHOOSE_ADDRESS == requestCode && RESULT_OK == resultCode) {
			address = data.getParcelableExtra(AddLocationMapActivity.ADDRESS_RESULT);
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	protected void cancel() {
		String taskName = taskNameEditText.getText().toString();
		if (changesPending && !taskName.equals(""))
		{
			unsavedChangesDialog = new AlertDialog.Builder(this)
			.setTitle(R.string.unsaved_changes_title)
			.setMessage(R.string.unsaved_changes_message)
			.setPositiveButton(R.string.add_task, new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					addTask();
				}
			})
			.setNeutralButton(R.string.discard, new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			})
			.setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					unsavedChangesDialog.cancel();
				}
			})
			.create();
			unsavedChangesDialog.show();
			
		}
		else
		{
			finish();
		}
	}

	protected void addTask() {
		String taskName = taskNameEditText.getText().toString();
		String taskDescription = taskDescriptionEditText.getText().toString();
		
		if(!taskName.equals(""))
		{
			Task t = new Task(taskName);
			t.setAddress(address);
			t.setDescription(taskDescription);
			getTaskManagerApplication().addTask(t);
		}
		
		finish();
	}
	
	public void addLocationButtonClicked(View view)
	{
		Intent intent = new Intent(AddTaskActivity.this, AddLocationMapActivity.class);
		startActivityForResult(intent, REQUEST_CHOOSE_ADDRESS);
	
	}
	
	public void editLocationButtonClicked(View view)
	{
		Intent intent = new Intent(AddTaskActivity.this, EditLocationMapActivity.class);
		intent.putExtra("location", address);
		startActivityForResult(intent, REQUEST_CHOOSE_ADDRESS);
	
	}

	private void setUpViews() {
		taskNameEditText = (EditText)findViewById(R.id.task_name);
		taskDescriptionEditText = (EditText)findViewById(R.id.task_description);
		addButton = (Button)findViewById(R.id.add_button);
		cancelButton = (Button)findViewById(R.id.cancel_button);
		
		addLocationButton = (Button)findViewById(R.id.add_location_button);
		addressText = (TextView)findViewById(R.id.address_text);
		editLocationButton = (Button)findViewById(R.id.edit_location_button);
		
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addTask();				
			}
		});
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();				
			}
		});
		
		taskNameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				changesPending = true;
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {}			
		});
		
		taskDescriptionEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				changesPending = true;
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {}			
		});
		
	}


}
