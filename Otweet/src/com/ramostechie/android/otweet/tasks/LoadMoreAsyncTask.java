package com.ramostechie.android.otweet.tasks;

import java.util.List;

import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.os.AsyncTask;

public class LoadMoreAsyncTask extends AsyncTask<Void, Void, LoadMoreAsyncTask.LoadMoreStatusesResult> {
	
	private long targetId;
	private boolean newerThanId;
	private Twitter twitter;
	private LoadMoreStatusesResponder responder;
	
	public interface LoadMoreStatusesResponder {
		public void loadingMoreStatuses();
		public void statusesLoaded(LoadMoreStatusesResult result);
	}
	
	public class LoadMoreStatusesResult {
	    public List<twitter4j.Status> statuses;
	    public boolean newer;
	    public LoadMoreStatusesResult(List<twitter4j.Status> statuses, boolean newer) {
	      super();
	      this.statuses = statuses;
	      this.newer = newer;
	    }
	}
	  
	public LoadMoreAsyncTask(LoadMoreStatusesResponder responder, Twitter twitter, long targetId, boolean newerThanId) {
	    super();
	    this.responder = responder;
	    this.targetId = targetId;
	    this.twitter = twitter;
	    this.newerThanId = newerThanId;
	}

	@Override
	protected LoadMoreAsyncTask.LoadMoreStatusesResult doInBackground(Void...params) {
	    List<twitter4j.Status> statii = null;
	    try {
	      if (newerThanId) {
	          statii = twitter.getHomeTimeline(new Paging().sinceId(targetId));
	      } else {
	        statii = twitter.getHomeTimeline(new Paging().maxId(targetId));
	      }
	    } catch (TwitterException e) {
	      throw new RuntimeException("Unable to load timeline", e);
	    }
	    
	    return new LoadMoreStatusesResult(statii, newerThanId);
	}

	@Override
	public void onPreExecute() {
		responder.loadingMoreStatuses();
	}
	  
	@Override
	public void onPostExecute(LoadMoreStatusesResult result) {
	    responder.statusesLoaded(result);
	}
}
