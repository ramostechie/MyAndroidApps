package com.ramostechie.android.otweet.authorization;

import java.io.InputStream;
import java.util.Properties;

import com.ramostechie.android.otweet.R;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class OAuthHelper {

  private static final String APPLICATION_PREFERENCES = "app_prefs";
  private static final String AUTH_KEY = "auth_key";
  private static final String AUTH_SEKRET_KEY = "auth_secret_key";
  private SharedPreferences prefs;
  private AccessToken accessToken;
  private String consumerSecretKey;
  private String consumerKey;
  private Context context;

  public OAuthHelper(Context context) {
    this.context = context;
    prefs = context.getSharedPreferences(APPLICATION_PREFERENCES, Context.MODE_PRIVATE);
    loadConsumerKeys();
    accessToken = loadAccessToken();
  }

  public void configureOAuth(Twitter twitter) {
    twitter.setOAuthConsumer(consumerKey, consumerSecretKey);
    twitter.setOAuthAccessToken(accessToken);
  }

  public boolean hasAccessToken() {
    return null != accessToken;
  }

  public void storeAccessToken(AccessToken accessToken) {
    Editor editor = prefs.edit();
    editor.putString(AUTH_KEY, accessToken.getToken());
    editor.putString(AUTH_SEKRET_KEY, accessToken.getTokenSecret());
    editor.commit();
    this.accessToken = accessToken;
  }

  private AccessToken loadAccessToken() {
    String token = prefs.getString(AUTH_KEY, null);
    String tokenSecret = prefs.getString(AUTH_SEKRET_KEY, null);
    if (null != token && null != tokenSecret) {
      return new AccessToken(token, tokenSecret);
    } else {
      return null;
    }
  }

  private void loadConsumerKeys() {
    try {
      Properties props = new Properties();
      InputStream stream = context.getResources().openRawResource(R.raw.oauth);
      props.load(stream);
      consumerKey = (String)props.get("consumer_key");
      consumerSecretKey = (String)props.get("consumer_secret_key");
    } catch (Exception e) {
      throw new RuntimeException("Unable to load consumer keys from oauth.properties", e);
    }
  }
}
