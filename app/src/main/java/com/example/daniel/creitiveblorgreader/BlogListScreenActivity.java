package com.example.daniel.creitiveblorgreader;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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

        ListView listView = findViewById(R.id.listView);


    }

    public class DownloadTask extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {

                URL url = new URL("http://blogsdemo.creitiveapps.com/blogs");

                httpURLConnection = (HttpURLConnection) url.openConnection();



            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            }


            return null;
        }
    }

}
