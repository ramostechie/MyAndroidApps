package com.ramostechie.android.taskmanager.views;

import com.ramostechie.android.taskmanager.R;
import com.ramostechie.android.taskmanager.tasks.Task;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskListItem extends LinearLayout {
	
	private Task task; 
	private CheckedTextView checkbox;
	private TextView descriptionText;
	private TextView addressText;

	public TaskListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		checkbox = (CheckedTextView)findViewById(android.R.id.text1);
		descriptionText = (TextView)findViewById(R.id.description);
		addressText = (TextView)findViewById(R.id.address_text);
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
		checkbox.setText(task.getName());
		checkbox.setChecked(task.isComplete());

		if (task.hasDescription()) {
			descriptionText.setText(task.getDescription());
			descriptionText.setVisibility(View.VISIBLE);
		}
		else{
			descriptionText.setVisibility(View.GONE);			
		}
		
		if (task.hasAddress()) {
			addressText.setText(task.getAddress());
			addressText.setVisibility(View.VISIBLE);
		} else {
			addressText.setVisibility(View.GONE);
		}
	}
}
