package com.example.daniel.creitiveblorgreader;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    EditText emailEditText;
    EditText passwordEditText;
    String email;
    String password;

    HttpURLConnection httpURLConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);


    }

    public void loginUser(View view){

         email = emailEditText.getText().toString();
        if(!isEmailValid(email)){
            emailEditText.setError("Invalid Email");
        }
         password = passwordEditText.getText().toString();

        if(!isPasswordValid(password)){
            passwordEditText.setError("Invalid password. Password should be longer than 6 characters");
        }

        if(email.matches("candidate@creitive.com") && password.equals("1234567")) {

           if(checkInternetConnection()){
               new SendPostRequest().execute();
           }
        }


    }

    private boolean isEmailValid(String email){

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    private boolean isPasswordValid(String password){

        return password != null && password.length() > 6;
    }

    private boolean checkInternetConnection(){

        if(InternetStatus.getInstance(getApplicationContext()).isOnline()){
            return true;

        }else{

            showEnableInternetDialog();
            return false;
        }

    }

    private void showEnableInternetDialog(){

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
        AlertDialog alert = builder.create();
        alert.show();

    }

    public class SendPostRequest extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://blogsdemo.creitiveapps.com/login");

                JSONObject postParameters = new JSONObject();

                postParameters.put("email", email);
                postParameters.put("password", password);

                httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Host", "blogsdemo.creitiveapps.com");
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);


                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(postParameters.toString());

                writer.flush();
                writer.close();
                os.close();

                int responseCode = httpURLConnection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);

                    }

                    in.close();
                    Log.i("RESPONSE", sb.toString());
                    return sb.toString(); //ovde vraca TOKEN

                }
                else {
                    Log.i("RESPONSE", String.valueOf(responseCode));
                    return "false : "+responseCode;

                }


            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (JSONException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }

   
}
