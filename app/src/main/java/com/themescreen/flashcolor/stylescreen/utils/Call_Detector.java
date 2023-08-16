package com.themescreen.flashcolor.stylescreen.utils;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.themescreen.flashcolor.stylescreen.phone_reciever.PhoneCallReceiver;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class Call_Detector {
    private WeakReference<Context> weakReference;

    public Call_Detector(Context context) {
        this.weakReference = new WeakReference<>(context);
    }

    private Context getWeakReference() {
        return this.weakReference.get();
    }

    public void answerCall() {
        try {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("input keyevent " + Integer.toString(79));
        } catch (IOException unused) {
            answerRingingCallWithIntent();
        }
    }

    private void answerRingingCallWithIntent() {
        try {
            Intent intent = new Intent("android.intent.action.HEADSET_PLUG");
            intent.addFlags(1073741824);
            intent.putExtra(PhoneCallReceiver.STATE, 1);
            intent.putExtra("microphone", 1);
            intent.putExtra("name", "Headset");
            getWeakReference().sendOrderedBroadcast(intent, "android.permission.CALL_PRIVILEGED");
            Intent intent2 = new Intent("android.intent.action.MEDIA_BUTTON");
            intent2.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(0, 79));
            getWeakReference().sendOrderedBroadcast(intent2, "android.permission.CALL_PRIVILEGED");
            Intent intent3 = new Intent("android.intent.action.MEDIA_BUTTON");
            intent3.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(1, 79));
            getWeakReference().sendOrderedBroadcast(intent3, "android.permission.CALL_PRIVILEGED");
            Intent intent4 = new Intent("android.intent.action.HEADSET_PLUG");
            intent4.addFlags(1073741824);
            intent4.putExtra(PhoneCallReceiver.STATE, 0);
            intent4.putExtra("microphone", 1);
            intent4.putExtra("name", "Headset");
            getWeakReference().sendOrderedBroadcast(intent4, "android.permission.CALL_PRIVILEGED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
