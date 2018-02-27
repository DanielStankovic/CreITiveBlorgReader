package com.example.daniel.creitiveblorgreader;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    EditText emailEditText;
    EditText passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);


    }

    public void loginUser(View view){

        String email = emailEditText.getText().toString();
        if(!isEmailValid(email)){
            emailEditText.setError("Invalid Email");
        }
        String password = passwordEditText.getText().toString();

        if(!isPasswordValid(password)){
            passwordEditText.setError("Invalid password. Password should be longer than 6 characters");
        }

        checkInternetConnection();

    }

    private boolean isEmailValid(String email){

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    private boolean isPasswordValid(String password){

        return password != null && password.length() > 6;
    }

    private void checkInternetConnection(){

        if(InternetStatus.getInstance(getApplicationContext()).isOnline()){

            //send POST Request and open new Activity
        }else{

            showEnableInternetDialog();
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
}
