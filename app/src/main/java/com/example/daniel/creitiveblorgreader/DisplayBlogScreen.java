package com.example.daniel.creitiveblorgreader;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DisplayBlogScreen extends AppCompatActivity {

    private HttpURLConnection httpURLConnection;
    private WebView webView;
    private DownloadTask task;
    private boolean connectedThroughDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_blog_screen);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Blog Display");
        }
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());

        if (!InternetStatus.getInstance(getApplicationContext()).isOnline()) {
            showEnableInternetDialog();
        } else {
            task = new DownloadTask();
            task.execute();
        }


    }

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
                String token = intent.getStringExtra("token_value");
                int articleId = intent.getIntExtra("article_id", 0);

                URL url = new URL("http://blogsdemo.creitiveapps.com/blogs/" + String.valueOf(articleId));

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

                    Toast.makeText(DisplayBlogScreen.this, "Can not fetch the article. Error code: " + String.valueOf(httpURLConnection.getResponseCode()), Toast.LENGTH_SHORT).show();
                    return null;
                }

            } catch (MalformedURLException e) {

                Toast.makeText(DisplayBlogScreen.this, "There is a problem with requested URL. " + e.getMessage(), Toast.LENGTH_SHORT).show();


            } catch (IOException e) {

                Toast.makeText(DisplayBlogScreen.this, "There is a problem. " + e.getMessage(), Toast.LENGTH_SHORT).show();

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

                    JSONObject jsonObject = new JSONObject(result);
                    String htmlContent = jsonObject.getString("content");
                    webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);


                } catch (JSONException e) {

                    Toast.makeText(DisplayBlogScreen.this, "There is a problem with fetched results. " + e.getMessage(), Toast.LENGTH_SHORT).show();

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
