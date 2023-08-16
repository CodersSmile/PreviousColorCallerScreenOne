package com.themescreen.flashcolor.stylescreen.phone_reciever;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.themescreen.flashcolor.stylescreen.utils.Utils;

import java.util.Date;

public class PhoneCallReceiver extends PhoneStateReceiver {
    public static final String END_STATE = "end";
    public static final String NUMBER_STATE = "number";
    public static final String START_STATE = "start";
    public static final String STATE = "state";
    Context context;


    public void onReceive(Context context2, Intent intent) {
        super.onReceive(context2, intent);
        this.context = context2;
        intent.getStringExtra(STATE);
    }


    public void onIncomingCallStarted(Context context2, String str, Date date) {
        super.onIncomingCallStarted(context2, str, date);
        this.context = context2;
    }


    public void onOutgoingCallStarted(Context context2, String str, Date date) {
        super.onOutgoingCallStarted(context2, str, date);
        this.context = context2;
    }


    public void onIncomingCallEnded(Context context2, String str, Date date, Date date2) {
        super.onIncomingCallEnded(context2, str, date, date2);
        Log.e(NotificationCompat.CATEGORY_MESSAGE, "imComing");
        this.context = context2;
        intentCallReports(1, str, date.getTime(), date2.getTime());
    }


    public void onMissedCall(Context context2, String str, Date date) {
        super.onMissedCall(context2, str, date);
        Log.e(NotificationCompat.CATEGORY_MESSAGE, "Miss");
        this.context = context2;
        intentCallReports(3, str, date.getTime(), 0);
    }


    public void onOutgoingCallEnded(Context context2, String str, Date date, Date date2) {
        super.onOutgoingCallEnded(context2, str, date, date2);
        Log.e(NotificationCompat.CATEGORY_MESSAGE, "Out");
        this.context = context2;
        intentCallReports(2, str, date.getTime(), date2.getTime());
    }

    private void intentCallReports(int i, String str, long j, long j2) {
        Log.d("theme", "theme end state part  time: " + Utils.milliSecondsToTimer(System.currentTimeMillis()) + " theme type  " + i + " number:  " + str);
    }
}
