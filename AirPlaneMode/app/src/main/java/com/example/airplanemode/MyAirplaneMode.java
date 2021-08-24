package com.example.airplanemode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

public class MyAirplaneMode extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        boolean isAirplaneModeEnabled=intent.getBooleanExtra("state",false);
//
//        if(isAirplaneModeEnabled)
//        {
//            Toast.makeText(context, "Airplane mode is Enable", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Toast.makeText(context, "Airplane mode is Disable", Toast.LENGTH_SHORT).show();
//        }
//
        if (Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 0) {
            Toast.makeText(context, "AIRPLANE MODE Off", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "AIRPLANE MODE On", Toast.LENGTH_SHORT).show();
        }

    }
}