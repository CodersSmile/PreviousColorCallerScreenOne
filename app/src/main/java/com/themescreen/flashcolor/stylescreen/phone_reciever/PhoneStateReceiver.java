package com.themescreen.flashcolor.stylescreen.phone_reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.themescreen.flashcolor.stylescreen.utils.Utils;

import java.util.Date;

public class PhoneStateReceiver extends BroadcastReceiver {
    public static Date callStartTime;
    private static boolean isIncoming;
    private static int lastState;
    private static String savedNumber;

    public void onIncomingCallEnded(Context context, String str, Date date, Date date2) {
    }

    public void onIncomingCallStarted(Context context, String str, Date date) {
    }

    public void onMissedCall(Context context, String str, Date date) {
    }

    public void onOutgoingCallEnded(Context context, String str, Date date, Date date2) {
    }

    public void onOutgoingCallStarted(Context context, String str, Date date) {
    }

    public void onReceive(Context context, Intent intent) {
        String str = intent.getAction();
        Log.d("theme", "time: " + Utils.milliSecondsToTimer(System.currentTimeMillis()) + " theme type  " + intent.getStringExtra(PhoneCallReceiver.STATE) + " action:  " + intent.getAction() + "   " + intent.getStringExtra("incoming_number"));
    }
}
