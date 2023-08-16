package com.themescreen.flashcolor.stylescreen.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootCompletedReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.d("onReceive:bootcomplete ", "onReceive:bootcomplete ");
        Toast.makeText(context, "Complete", Toast.LENGTH_LONG).show();
    }
}
