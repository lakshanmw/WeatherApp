package com.example.lakshan.weatherapp.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lakshan.weatherapp.R;

/**
 * Created by Lakshan on 10/11/2018.
 */

public class AppUtil {

    //check nerwork connection
    public static boolean checkNetworkConnection(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetworks = connectivityManager.getActiveNetworkInfo();
        if (activeNetworks != null && activeNetworks.isConnected()) {
            return activeNetworks.isConnectedOrConnecting();
        }
        return false;
    }

    public static void confirmAlert(final AlertDialog dialog, Activity activity, String title, String message,
                                    View.OnClickListener yesClickListener, View.OnClickListener noClickListener, String yesText, String noText, boolean cancelableState) {

        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_custom_confirm, null);
        dialog.setView(dialogView);
        dialog.setCancelable(cancelableState);

        TextView tvTitle = (TextView) dialogView.findViewById(R.id.dialog_custom_confirm_tv_title);
        TextView tvMessage = (TextView) dialogView.findViewById(R.id.dialog_custom_confirm_tv_message);
        Button btnOk = (Button) dialogView.findViewById(R.id.dialog_custom_confirm_btn_ok);
        Button btnCancel = (Button) dialogView.findViewById(R.id.dialog_custom_confirm_btn_cancel);
        btnOk.setOnClickListener(yesClickListener);
        btnCancel.setOnClickListener(noClickListener);

        if (yesClickListener == null)
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        if (noClickListener == null)
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


        if (!title.isEmpty()) {
            tvTitle.setText(title);
        } else {
            tvTitle.setText("");
        }

        if (!message.isEmpty()) {
            tvMessage.setText(message);
        } else {
            tvMessage.setText("");
        }

        btnOk.setText(yesText);
        btnCancel.setText(noText);
        dialog.show();
    }

    // show system settings dialog
    public static void showSystemSettingsDialog(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));

        } else {
            context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        }
    }
}


