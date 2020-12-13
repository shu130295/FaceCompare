package com.example.facecompare;

import android.app.AlertDialog;
import android.content.Context;

public class AlertUtil {
    public static void showOkDialog(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.show();
    }
}
