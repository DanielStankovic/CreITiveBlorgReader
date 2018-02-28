package com.example.daniel.creitiveblorgreader;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BlogListScreenActivity extends AppCompatActivity {


    private HttpURLConnection httpURLConnection;
    private List<BlogItem> blogItemArrayList;
    private List<Integer> articleIdsList;
    private ListView listView;
    private String token;
    private DownloadTask task;
    private boolean connectedThroughDialog = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list_screen);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Blog List");
        }
        blogItemArrayList = new ArrayList<>();
        articleIdsList = new ArrayList<>();
        listView = findViewById(R.id.listView);

        if (!InternetStatus.getInstance(getApplicationContext()).isOnline()) {
            showEnableInternetDialog();
        } else {
            task = new DownloadTask();
            task.execute();
        }


        BlogItemAdapter adapter = new BlogItemAdapter(BlogListScreenActivity.this, blogItemArrayList, R.layout.list_item_layout);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(BlogListScreenActivity.this, DisplayBlogScreen.class);
                intent.putExtra("token_value", token);
                intent.putExtra("article_id", articleIdsList.get(position));
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (InternetStatus.getInstance(getApplicationContext()).isOnline() && connectedThroughDialog) {

            if (task != null) {
                task.cancel(true);
            }
            connectedThroughDialog = false;
            this.recreate();

        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            try {

                Intent intent = getIntent();
                token = intent.getStringExtra("token_value");
                URL url = new URL("http://blogsdemo.creitiveapps.com/blogs");

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Host", "blogsdemo.creitiveapps.com");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("X-Authorize", token);
                httpURLConnection.setDoInput(true);

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder("");
                    String line;

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }

                    in.close();
                    return sb.toString();
                } else {

                    Toast.makeText(BlogListScreenActivity.this, "Can not fetch articles. Error code: " + String.valueOf(httpURLConnection.getResponseCode()), Toast.LENGTH_SHORT).show();
                    return null;
                }

            } catch (MalformedURLException e) {

                Toast.makeText(BlogListScreenActivity.this, "There is a problem with requested URL. " + e.getMessage(), Toast.LENGTH_SHORT).show();


            } catch (IOException e) {

                Toast.makeText(BlogListScreenActivity.this, "There is a problem. " + e.getMessage(), Toast.LENGTH_SHORT).show();


            } finally {
                httpURLConnection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {

                try {

                    JSONArray array = new JSONArray(result);
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jsonObject = array.getJSONObject(i);


                        int articleId = jsonObject.getInt("id");
                        BlogItem blogItem = new BlogItem(articleId, jsonObject.getString("title"),
                                jsonObject.getString("description"), jsonObject.getString("image_url"));
                        articleIdsList.add(articleId);
                        blogItemArrayList.add(blogItem);

                    }
                } catch (JSONException e) {

                    Toast.makeText(BlogListScreenActivity.this, "There is a problem with fetched results. " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }
    }

    private void showEnableInternetDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Connect to wifi, mobile data or quit")
                .setCancelable(false)
                .setNegativeButton("Connect to mobile data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                    }
                })
                .setPositiveButton("Connect to WIFI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNeutralButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        connectedThroughDialog = true;
        AlertDialog alert = builder.create();
        alert.show();

    }

}
