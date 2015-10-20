package com.ramostechie.android.otweet.activities;

import com.ramostechie.android.otweet.OTweetApplication;
import com.ramostechie.android.otweet.R;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthorizationActivity extends Activity {

  private OTweetApplication app;
  private WebView webView;
  
  private WebViewClient webViewClient = new WebViewClient() {
    @Override
    public void onLoadResource(WebView view, String url) {
      // the URL we're looking for looks like this:
      // <twitterAPP-callbackURL>//authenticated?oauth_token=kAaoCJtNjcf7TNW92phyF65yvX8QKSOC&oauth_verifier=s8g9esckNY4rfA1Le0o3y09WHoc788wo
      Uri uri = Uri.parse(url);
      if (uri.getHost().equals("ramostechie.com")) {
        //String token = uri.getQueryParameter("oauth_token");
        String token = uri.getQueryParameter("oauth_verifier");
        if (null != token) {
          webView.setVisibility(View.INVISIBLE);
          app.authorized(token);
          finish();
        } else {
          // tell user to try again 
        }
      } else {
        super.onLoadResource(view, url);
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    /* Enabling strict mode */
	/*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	StrictMode.setThreadPolicy(policy);*/
	
    app = (OTweetApplication)getApplication();
    setContentView(R.layout.authorization_view);
    setUpViews();
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    String authURL = app.beginAuthorization();
    webView.loadUrl(authURL);
  }

  private void setUpViews() {
    webView = (WebView)findViewById(R.id.web_view);
    webView.setWebViewClient(webViewClient);
  }

}