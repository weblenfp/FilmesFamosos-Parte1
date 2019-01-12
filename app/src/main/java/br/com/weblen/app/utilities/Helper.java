package br.com.weblen.app.utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Helper {

    public static boolean isInternetAvailable(Activity object) {
        ConnectivityManager cm = (ConnectivityManager) object.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }

        return netInfo != null && netInfo.isConnected();
    }

}
