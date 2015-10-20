package com.ramostechie.android.otweet.activities;

import java.util.List;

import com.ramostechie.android.otweet.OTweetApplication;
import com.ramostechie.android.otweet.R;
import com.ramostechie.android.otweet.adapters.StatusListAdapter;
import com.ramostechie.android.otweet.layouts.LoadMoreListItem;
import com.ramostechie.android.otweet.tasks.LoadMoreAsyncTask;
import com.ramostechie.android.otweet.tasks.LoadMoreAsyncTask.LoadMoreStatusesResponder;
import com.ramostechie.android.otweet.tasks.LoadMoreAsyncTask.LoadMoreStatusesResult;

//import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

public class StatusListActivity extends ListActivity implements LoadMoreStatusesResponder{

	final private Handler handler = new Handler();
	private OTweetApplication app;
	private Twitter twitter;
	private LoadMoreListItem headerView;
	private LoadMoreListItem footerView;
	private StatusListAdapter adapter;
	protected ProgressDialog progressDialog;

/*thread version	
	protected void finishedLoadingNewer(List<Status> statii) {
	    adapter.appendNewer(statii);
	    headerView.hideProgress();
	    setProgressBarIndeterminateVisibility(false);
	    getListView().setSelection(1);
	}
	
	protected void finishedLoadingOlder(List<Status> statii) {
	    adapter.appendOlder(statii);
	    footerView.hideProgress();
	    setProgressBarIndeterminateVisibility(false);
	}
	
	private class LoadNewerResult implements Runnable {
		private List<Status> statii;

	    public LoadNewerResult(List<Status> statii) {
	      super();
	      this.statii = statii;
	    }

	    public void run() {
	      finishedLoadingNewer(statii);
	    }
	};
	
	private class LoadOlderResult implements Runnable {
	    private List<Status> statii;

	    public LoadOlderResult(List<Status> statii) {
	      super();
	      this.statii = statii;
	    }

	    public void run() {
	      finishedLoadingOlder(statii);
	    }
	};*/
	
	
	
	
	private Runnable finishedLoadingListTask = new Runnable() {
	    public void run() {
	      finishedLoadingList();
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/* Enabling strict mode */
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		app = (OTweetApplication)getApplication();
		twitter = app.getTwitter();
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!app.isAuthorized()) {
			beginAuthorization();
		} else {
			loadTimelineIfNotLoaded();
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (v.equals(headerView)) {
			headerView.showProgress();
			loadNewerTweets();
		} else if (v.equals(footerView)) {
			footerView.showProgress();
			loadOlderTweets();
		}
		else {
			// Watch out! Doesn't account for header/footer! -> Status status =
		    // adapter.getItem(position);
		    Status status = (Status) getListView().getItemAtPosition(position);
		    Intent intent = new Intent(this, StatusDetailActivity.class);
		    intent.putExtra(StatusDetailActivity.STATUS, status);
		    startActivity(intent);
		}
	}
	
	private void loadTimelineIfNotLoaded() {
	    if (null == getListAdapter()) {
	      progressDialog = ProgressDialog.show(
	          StatusListActivity.this,
	          getResources().getString(R.string.loading_title),
	          getResources().getString(R.string.loading_home_timeline_description)
	         );
	      Thread loadHomeTimelineThread = new Thread() {
	        public void run() {
	          loadHomeTimeline();
	          handler.post(finishedLoadingListTask);
	        }
	      };
	      loadHomeTimelineThread.start();
	    }
	}
	
	private void beginAuthorization() {
		Intent intent = new Intent(this, AuthorizationActivity.class);
		startActivity(intent);
	}
	
	private void loadHomeTimeline() {
		try {
			List<Status> statii = twitter.getHomeTimeline();
		    adapter = new StatusListAdapter(this, statii);
		} catch (TwitterException e) {
			throw new RuntimeException("Unable to load home timeline", e);
		}
	}
	
	protected void finishedLoadingList() {
	    setLoadMoreViews();
	    setListAdapter(adapter);
	    getListView().setSelection(1);
	    progressDialog.dismiss();
	}

	public void loadingMoreStatuses() {
		setProgressBarIndeterminateVisibility(true);
	}

	public void statusesLoaded(LoadMoreStatusesResult result) {
		setProgressBarIndeterminateVisibility(false);
		if (result.newer) {
			headerView.hideProgress();
			adapter.appendNewer(result.statuses);
		    getListView().setSelection(1);
		} else {
			footerView.hideProgress();
			adapter.appendOlder(result.statuses);
		}
	}

	private void loadNewerTweets() {
		headerView.showProgress();
		/* thread version
		setProgressBarIndeterminateVisibility(true);
		Thread loadMoreThread = new Thread() {
			public void run() {
		        try {
		          List<Status> statii = twitter.getHomeTimeline(new Paging().sinceId(adapter.getFirstId()));
		          Runnable finishedLoadingNewerTask = new LoadNewerResult(statii);;
		          handler.post(finishedLoadingNewerTask);
		        } catch (TwitterException e) {
		          throw new RuntimeException("Unable to load home timeline", e);
		        }
		      }
		    };
		    loadMoreThread.start();
		    */
		 new LoadMoreAsyncTask(this, twitter, adapter.getFirstId(), true).execute();
	}
	
	private void loadOlderTweets() {
		footerView.showProgress();
	    /* thread version
	    setProgressBarIndeterminateVisibility(true);
	    Thread loadMoreThread = new Thread() {
	      public void run() {
	        try {
	          List<Status> statii = twitter.getHomeTimeline(new Paging().maxId(adapter.getLastId() - 1));
	          Runnable finishedLoadingOlderTask = new LoadOlderResult(statii);
	          handler.post(finishedLoadingOlderTask);
	        } catch (TwitterException e) {
	          throw new RuntimeException("Unable to load home timeline", e);
	        }
	      }
	    };
	    loadMoreThread.start();
	    */
	    new LoadMoreAsyncTask(this, twitter, adapter.getLastId()-1, false).execute();
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
	
	private void setLoadMoreViews() {
		headerView = (LoadMoreListItem) getLayoutInflater().inflate(R.layout.load_more, null);
		headerView.showHeaderText();
		footerView = (LoadMoreListItem) getLayoutInflater().inflate(R.layout.load_more, null);
		footerView.showFooterText();
		getListView().addHeaderView(headerView);
		getListView().addFooterView(footerView);
	}
}