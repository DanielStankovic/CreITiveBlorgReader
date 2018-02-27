package com.example.daniel.creitiveblorgreader;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;

public class BlogListScreenActivity extends AppCompatActivity {


    HttpURLConnection httpURLConnection;
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();

    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list_screen);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Blog List");
        }

        new DownloadTask().execute();

        ListView listView = findViewById(R.id.listView);


    }

    public class DownloadTask extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {

            try {

                Intent intent = getIntent();
                String token = intent.getStringExtra("token_value");
                URL url = new URL("http://blogsdemo.creitiveapps.com/blogs");

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Host", "blogsdemo.creitiveapps.com");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("X-Authorize", token);
                httpURLConnection.setDoInput(true);

                BufferedReader in=new BufferedReader(
                        new InputStreamReader(
                                httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder("");
                String line="";

                while((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();
                return sb.toString();


            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            }  finally {
                httpURLConnection.disconnect();
            }

            return null;
        }
    }

}
