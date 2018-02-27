package com.example.daniel.creitiveblorgreader;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

 class InternetStatus {


    static Context context;

     private ConnectivityManager connectivityManager;

    private static InternetStatus instance = new InternetStatus();
    private boolean isConnected = false;

    public static InternetStatus getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

     boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = null;
            if (connectivityManager != null) {
                networkInfo = connectivityManager.getActiveNetworkInfo();
            }
            isConnected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return isConnected;

        } catch (Exception e) {
            Toast.makeText(context, "Not connected to the Internet", Toast.LENGTH_SHORT).show();

        }
        return isConnected;
    }
}
