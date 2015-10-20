package com.androidexample.httppostexample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText; 
import android.widget.TextView;

public class HttpPostExample extends Activity {

	TextView content;
	EditText fname,email,login,pass;
    String Name,Email,Login,Pass; 
	
    /** Called when the activity is first created. */ 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_post_example);
        
        content = (TextView)findViewById(R.id.content);
        fname	=(EditText)findViewById(R.id.name);
        email	=(EditText)findViewById(R.id.email);
        login	=(EditText)findViewById(R.id.loginname);
        pass	=(EditText)findViewById(R.id.password);
        
        
        Button saveme=(Button)findViewById(R.id.save);
        saveme.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		try{
        			
        		   GetText();
        		 }
                catch(Exception ex)
                 {
                	content.setText("url exeption!");	 
                 }
        	}
        });  
    }
    
    
	public void GetText() throws UnsupportedEncodingException
    {
    
    	Name 	= fname.getText().toString();
    	Email 	= email.getText().toString();
    	Login 	= login.getText().toString();
    	Pass 	= pass.getText().toString();
    	
    
      	
          String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(Name, "UTF-8"); 
          data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(Email, "UTF-8"); 
          data += "&" + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(Login, "UTF-8");
          data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(Pass, "UTF-8");
         
          String text = "";
    	  BufferedReader reader=null;
          // Send data 
        try
  		{ 
         
        	URL url = new URL("http://androidexample.com/media/webservice/httppost.php");
        	
          URLConnection conn = url.openConnection(); 
          conn.setDoOutput(true); 
          OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
          wr.write(data); 
          wr.flush(); 
       // Get the response 
          
          
        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
  		StringBuilder sb = new StringBuilder();
  		String line = null;
       
  		
			while((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			text = sb.toString();
		}
		catch(Exception ex)
		{
			
		}
		finally
		{
			try
			{

				reader.close();
			}
			catch(Exception ex) {}
		}
             
		content.setText(text);
       
    }

}