package com.anandharajr.mysample.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import com.anandharajr.mysample.R;
/**
 * Created by anandharajr on 21-06-18.
 */
public class Configuration {
    private static AlertDialog mAlertDialog;
    public static boolean checkConnection(@NonNull Context context) {
        return ((ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
    public static ProgressDialog generateProgressDialog(Context context, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.ProgressTheme);
        progressDialog.setMessage(context.getString(R.string.Loading));
        progressDialog.setCancelable(cancelable);
        return progressDialog;
    }

    @SuppressLint({"InflateParams", "DefaultLocale"})
    public static void warningAlertDialog(Context context, String message) {
        try {
            if (mAlertDialog != null) {
                if (mAlertDialog.isShowing()) {
                    mAlertDialog.dismiss();
                }
            }
            mAlertDialog = new AlertDialog.Builder(context).setCancelable(false)
                    .setMessage(message).setPositiveButton(R.string.close, null).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
