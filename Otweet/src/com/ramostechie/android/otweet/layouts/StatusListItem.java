package com.ramostechie.android.otweet.layouts;

//import java.io.IOException;
//import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.ramostechie.android.otweet.R;
import com.ramostechie.android.otweet.tasks.LoadImageAsyncTask;
import com.ramostechie.android.otweet.tasks.LoadImageAsyncTask.LoadImageAsyncTaskResponder;

import twitter4j.Status;
import twitter4j.User;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
//import android.os.Handler;
import android.util.AttributeSet;
//import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StatusListItem extends RelativeLayout implements LoadImageAsyncTaskResponder {
	
	/* thread version
	 * final private Handler handler = new Handler();
	 * protected Drawable avatarDrawable;
	*/
	private ImageView avatarView;
	private TextView screenName;
	private TextView statusText;
	private AsyncTask<URL, Void, Drawable> latestLoadTask;	

	public StatusListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	/* thread version
	private Runnable finishedLoadingDrawable = new Runnable() {
	    public void run() {
	      finishedLoadingUserAvatar();
	    }
	  };
	*/

	/*@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		findViews();
	}*/

	/* thread version
	public void setStatus(Status status) {
		final User user = status.getUser();
		findViews();
		screenName.setText(user.getScreenName());
		statusText.setText(status.getText());
		avatarView.setImageDrawable(null);
		Thread loadUserAvatarThread = new Thread() {
		      public void run() {
		        try {
		          avatarDrawable = Drawable.createFromStream(
		        		  (InputStream) new URL(user.getProfileImageURL()).getContent(), user.getName());
		        } catch (IOException e) {
		          Log.e(getClass().getName(), "Could not load image.", e);
		        }
		         // nothing on the activity view can be modified directly in the thread as it is in a higher
		         // hierarchy, that's why we don't touch avatarView element here, we play with the avatarDrawable
		         // and assign it to avatarView in the other method called by the post
		        handler.post(finishedLoadingDrawable);
		      }
		    };
		    loadUserAvatarThread.start();
	}
	
	protected void finishedLoadingUserAvatar() {
	    avatarView.setImageDrawable(avatarDrawable);
	}
	*/
	
	public void setStatus(Status status) {
		final User user = status.getUser();
		findViews();
		screenName.setText(user.getScreenName());
		statusText.setText(status.getText());
		// cancel old task
		if (null != latestLoadTask) {
			latestLoadTask.cancel(true);
		}
		try {
			latestLoadTask = new LoadImageAsyncTask(this).execute(new URL(user.getProfileImageURL()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void imageLoading() {
		avatarView.setImageDrawable(null);
	}

	public void imageLoadCancelled() {
		// do nothing
	}

	public void imageLoaded(Drawable drawable) {
		avatarView.setImageDrawable(drawable);
	}

	private void findViews() {
		avatarView = (ImageView)findViewById(R.id.user_avatar);
		screenName = (TextView)findViewById(R.id.status_user_name_text);
		statusText = (TextView)findViewById(R.id.status_text);
	}

}
