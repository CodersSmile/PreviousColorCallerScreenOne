package com.themescreen.flashcolor.stylescreen.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ShortcutReciver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "ShortCut Created", Toast.LENGTH_SHORT).show();
    }
}
