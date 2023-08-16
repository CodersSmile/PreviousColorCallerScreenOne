package com.themescreen.flashcolor.stylescreen.Service;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.telecom.InCallService;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.ActivityCompat;

import com.themescreen.flashcolor.stylescreen.Activity.Theme_Activity_Callinfo_Dailog;
import com.themescreen.flashcolor.stylescreen.Activity.Activity_Calling_InComing_Outgoing;
import com.themescreen.flashcolor.stylescreen.Activity.Activity_SimSelection_Dailogs;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.phone_reciever.PhoneCallReceiver;
import com.themescreen.flashcolor.stylescreen.utils.CallManager;
import com.themescreen.flashcolor.stylescreen.utils.CallWaitingWindow;
import com.themescreen.flashcolor.stylescreen.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class CallService extends InCallService implements CallWaitingWindow.onWaitingListner {
    public static CallService callService;
    public static MyCallBackCall mycall;
    public static TelecomManager tm;
    String TAG = "CALL_SERVICE";
    Notification.Builder mBuilder;
    NotificationManager mNotificationManager;
    RemoteViews mRemoteViews;
    MediaPlayer mp = new MediaPlayer();
    private int oldState = -1;
    Ringtone r;
    String user_name;

    public void onCallAdded(Call call) {
        Log.e("AAA", "theme added");
        super.onCallAdded(call);
        callService = this;
        tm = (TelecomManager) getSystemService(TELECOM_SERVICE);
        if (!Utils.isOutgoingScreenActive || CallManager.sCall == null) {
            CallManager.sCall = call;
        } else if (call.getState() == 2) {
            if (check_DeviceLocked(callService)) {
                show_Wait_Call_View(call);
            } else {
                CallManager.showWaitingPop(this, call);
            }
        }
        mycall = new MyCallBackCall();
        try {
            Utils.numberCall = URLDecoder.decode(call.getDetails().getHandle().toString(), "utf-8").replace("tel:", "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("call1", "  befor theme  changed:" + call.getState());
        call.registerCallback(mycall);
        updateState(call, call.getState());
        Log.i("call1", "  after theme  changed:" + call.getState());
        String contactName = getContactName(this, Utils.numberCall);
        this.user_name = contactName;
        setUpNotification(false, contactName, Utils.numberCall);
    }

    public static String getContactName(Context context, String str) {
        Cursor query = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), new String[]{"display_name"}, null, null, null);
        String str2 = null;
        if (query == null) {
            return null;
        }
        if (query.moveToFirst()) {
            str2 = query.getString(query.getColumnIndex("display_name"));
        }
        if (query != null && !query.isClosed()) {
            query.close();
        }
        return str2;
    }

    private void show_Wait_Call_View(Call call) {
        CallManager.callWaiting = call;
        sendBroadcast(new Intent(Activity_Calling_InComing_Outgoing.SHOW_WaitCallAction));
    }

    private void hide_Wait_Call_View() {
        sendBroadcast(new Intent(Activity_Calling_InComing_Outgoing.HIDE_WaitCallAction));
    }

    private void update_Wait_Call_View() {
        sendBroadcast(new Intent(Activity_Calling_InComing_Outgoing.UPDATE_WaitCallAction));
    }

    private boolean check_DeviceLocked(CallService callService2) {
        return ((KeyguardManager) callService2.getSystemService(KEYGUARD_SERVICE)).isDeviceLocked();
    }

    public void register() {
        TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") == 0) {
            PhoneAccountHandle defaultOutgoingPhoneAccount = telecomManager.getDefaultOutgoingPhoneAccount("tel");
            if (defaultOutgoingPhoneAccount != null) {
                Log.i("call1", "  phone handler :" + defaultOutgoingPhoneAccount.toString() + "    " + defaultOutgoingPhoneAccount.getUserHandle().toString());
                CallManager.sCall.phoneAccountSelected(defaultOutgoingPhoneAccount, false);
                return;
            }
            Log.i("call1", "  phone handler :" + ((Object) null) + "    " + ((Object) null));
            if (getSimCount() > 1) {
                Intent intent = new Intent();
                intent.setAction("com.sim_selection.action");
                sendBroadcast(intent);
                Intent intent2 = new Intent(this, Activity_SimSelection_Dailogs.class);
                intent2.setFlags(268435456);
                startActivity(intent2);
                return;
            }
            try {
                if (((SubscriptionManager) getSystemService("telephony_subscription_service")).getActiveSubscriptionInfoForSimSlotIndex(0) == null) {
                    doPhoneHandling(this, 1);
                } else {
                    doPhoneHandling(this, 0);
                }
            } catch (Exception unused) {
                Utils.displayToast(this, "Something Went Wrong !");
            }
        }
    }

    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        call.unregisterCallback(mycall);
        if (CallManager.callWaiting == null || CallManager.callWaiting != call) {
            CallManager.sCall = null;
            swap();
            updateOugoingPage();
        } else {
            cleanUpWaiting_service();
            CallManager.callWaiting = null;
            swap();
            updateOugoingPage();
        }
        if (CallManager.callWaiting == null && CallManager.sCall != null) {
            cleanUpWaiting_service();
            CallManager.callWaiting = null;
            updateOugoingPage();
        } else if (CallManager.sCall != null || CallManager.callWaiting == null) {
            if (this.mNotificationManager == null) {
                this.mNotificationManager = (NotificationManager) getSystemService("notification");
            }
            try {
                this.mNotificationManager.cancelAll();
            } catch (Exception e) {
                Log.e(this.TAG, "onCallRemoved: " + e.getMessage());
            }
        } else {
            cleanUpWaiting_service();
            CallManager.callWaiting = null;
            swap();
            updateOugoingPage();
        }
        stopForeground(false);
    }

    private void swap() {
        if (CallManager.sCall != null && CallManager.callWaiting != null) {
            Call call = CallManager.callWaiting;
            CallManager.callWaiting = CallManager.sCall;
            CallManager.sCall = call;
        } else if (CallManager.callWaiting != null) {
            Call call2 = CallManager.callWaiting;
            CallManager.callWaiting = CallManager.sCall;
            CallManager.sCall = call2;
        }
    }


    public void updateOugoingPage() {
        if (this.mNotificationManager != null) {
            updatenoti();
        }
        sendBroadcast(new Intent(Activity_Calling_InComing_Outgoing.actionUpdate));
        if (check_DeviceLocked(this)) {
            update_Wait_Call_View();
        }
    }

    public void onCall_Accept(Boolean bool, View view) {
        swap();
        if (CallManager.callWaiting != null) {
            CallManager.callWaiting.hold();
        }
        if (CallManager.sCall != null) {
            if (CallManager.sCall.getState() == 3) {
                CallManager.sCall.unhold();
            } else {
                CallManager.sCall.answer(0);
            }
        }
        CallManager.updateWindow(this, CallManager.callWaiting);
        view.setTag("1");
        updateOugoingPage();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        String str;
        try {
            str = intent.getAction();
        } catch (Exception e) {
            e.printStackTrace();
            str = null;
        }
        if (str != null && str.equalsIgnoreCase("REJECT")) {
            try {
                CallManager.reject();
                onDestroy();
            } catch (Exception e2) {
                Log.e("Error", e2.getMessage());
                if (CallManager.sCall != null) {
                    CallManager.sCall.reject(false, "");
                }
            }
        } else if (str != null && str.equalsIgnoreCase("PICK")) {
            swap();
            if (CallManager.callWaiting != null) {
                CallManager.callWaiting.hold();
            }
            if (CallManager.sCall != null) {
                if (CallManager.sCall.getState() == 3) {
                    CallManager.sCall.unhold();
                } else {
                    CallManager.sCall.answer(0);
                    CallManager.callrecivefromnotifiction = true;
                }
            }
            this.mRemoteViews.setImageViewResource(R.id.noti_ans_call, R.drawable.after_received_call_btn_bg);
            this.mRemoteViews.setViewVisibility(R.id.noti_ans_call_name, 0);
            Log.e("username", "onStartCommand: " + this.user_name);
            String str2 = this.user_name;
            if (str2 == null || str2.isEmpty()) {
                this.mRemoteViews.setTextViewText(R.id.noti_ans_call_name, "U");
            } else {
                this.mRemoteViews.setTextViewText(R.id.noti_ans_call_name, this.user_name.substring(0, 1));
            }
            this.mNotificationManager.notify(1234, this.mBuilder.build());
            CallManager.updateWindow(this, CallManager.callWaiting);
            updateOugoingPage();
        }
        return 1;
    }

    private void setUpNotification(boolean z, String str, String str2) {
        this.mNotificationManager = (NotificationManager) getSystemService("notification");
        this.mRemoteViews = new RemoteViews(getPackageName(), (int) R.layout.calling_notification_lay);
        Intent intent = new Intent(this, CallService.class);
        intent.setAction("REJECT");
        this.mRemoteViews.setOnClickPendingIntent(R.id.noti_end_call, PendingIntent.getService(this, 0, intent, 0));
        Intent intent2 = new Intent(this, CallService.class);
        intent2.setAction("PICK");
        this.mRemoteViews.setOnClickPendingIntent(R.id.noti_ans_call, PendingIntent.getService(this, 0, intent2, 0));
        if (str == null || str.equalsIgnoreCase("")) {
            str = "Unknown";
        }
        this.mRemoteViews.setTextViewText(R.id.noti_name, str);
        this.mRemoteViews.setTextViewText(R.id.noti_number, str2);
        if (z) {
            this.mRemoteViews.setImageViewResource(R.id.noti_ans_call, R.drawable.after_received_call_btn_bg);
            this.mRemoteViews.setViewVisibility(R.id.noti_ans_call_name, 0);
            if (str == null || str.isEmpty()) {
                this.mRemoteViews.setTextViewText(R.id.noti_ans_call_name, "U");
            } else {
                this.mRemoteViews.setTextViewText(R.id.noti_ans_call_name, str.substring(0, 1));
            }
        } else {
            this.mRemoteViews.setImageViewResource(R.id.noti_ans_call, R.drawable.accept_button);
            this.mRemoteViews.setViewVisibility(R.id.noti_ans_call_name, 8);
            this.mRemoteViews.setTextViewText(R.id.noti_ans_call_name, "");
        }
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("channelid", "channelname", 0);
            notificationChannel.setLightColor(-16776961);
            notificationChannel.setShowBadge(false);
            this.mNotificationManager.createNotificationChannel(notificationChannel);
            this.mBuilder = new Notification.Builder(this, "channelid");
        } else {
            this.mBuilder = new Notification.Builder(this);
        }
        Intent intent3 = new Intent(this, Activity_Calling_InComing_Outgoing.class);
        intent3.addCategory("android.intent.category.LAUNCHER");
        intent3.setAction("android.intent.action.MAIN");
        intent3.setFlags(603979776);
        this.mBuilder.setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(false).setOngoing(true).setContentIntent(PendingIntent.getActivity(this, 0, intent3, 134217728)).setContent(this.mRemoteViews).setTicker(getResources().getString(R.string.app_name));
        startForeground(1234, this.mBuilder.build());
        this.mNotificationManager.notify(1234, this.mBuilder.build());
    }

    public void onCall_Decline() {
        Log.e("AAA", "Waiting theme End");
        Call call = CallManager.callWaiting;
        if (call.getState() != 2) {
            call.disconnect();
        } else {
            call.reject(false, "");
        }
    }

    private class MyCallBackCall extends Call.Callback {
        private MyCallBackCall() {
        }

        public void onStateChanged(Call call, int i) {
            super.onStateChanged(call, i);
            CallService.this.updateState(call, i);
        }
    }



    private void updateState(Call call, int i) {
        int state = call.getState();
        if (state == 1) {
            Log.e("SSS", "incall dialong");
            if ((CallManager.callWaiting == null || CallManager.callWaiting != call) && !Utils.isOutgoingScreenActive) {
                startOutGoingProcess();
            }
            updateOugoingPage();
        } else if (state == 2) {
            Log.e("SSS", "incall ringing");
            if ((CallManager.callWaiting == null || CallManager.callWaiting != call) && !Utils.isOutgoingScreenActive) {
                showIncomingCallPOpup();
            }
            updateOugoingPage();
        } else if (state == 3) {
            Log.e("SSS", "incall holding");
            Log.e("AAA", "");
            updateOugoingPage();
        } else if (state == 4) {
            Log.e("SSS", "incall active");
            if (CallManager.callWaiting == null || CallManager.callWaiting != call) {
                if (!Utils.isOutgoingScreenActive) {
                    Utils.callEndTime = 0;
                }
                changeAnswerToOutgoing();
            }
            updateOugoingPage();
        } else if (state == 7) {
            Log.e("SSS", "incall disconnect");
            if (CallManager.callWaiting == null || CallManager.callWaiting != call) {
                dismissIncomingScreen();
                if (CallManager.callWaiting == null) {
                    Log.e("SSS", "incall disconnect CallManager.callWaiting null");
                } else {
                    Log.e("SSS", "incall disconnect CallManager.callWaiting not null");
                }
                if (CallManager.callWaiting == null) {
                    startMissedCallPOp(call);
                } else if (CallManager.callWaiting.getState() == 3) {
                    CallManager.callWaiting.unhold();
                    cleanUpWaiting_service();
                } else if (CallManager.callWaiting.getState() == 2) {
                    CallManager.callWaiting.answer(0);
                    cleanUpWaiting_service();
                } else {
                    startMissedCallPOp(call);
                }
            } else {
                cleanUpWaiting_service();
                if (CallManager.sCall != null && CallManager.sCall.getState() == 3) {
                    CallManager.sCall.answer(0);
                }
            }
        } else if (state == 8) {
            register();
            updateOugoingPage();
        } else if (state == 9) {
            Log.e("SSS", "incall connecting");
            if ((CallManager.callWaiting == null || CallManager.callWaiting != call) && !Utils.isOutgoingScreenActive) {
                cleanUpWaiting_service();
                dismissIncomingScreen();
                startOutGoingProcess();
            }
            updateOugoingPage();
        }
        this.oldState = i;
        Log.i("call1", "old state " + this.oldState + "  theme  changed:" + i);
    }

    private void cleanUpWaiting_service() {
        hide_Wait_Call_View();
        CallManager.cleanUpWaiting();
    }

    private void muteSystemRingtone() {
        AudioManager audioManager = (AudioManager) getSystemService("audio");
        Utils.Last_Volume_Level = audioManager.getStreamVolume(2);
        audioManager.setStreamVolume(2, 0, 0);
        Utils.isSilentSystemRingtoneManually = true;
    }

    private void unMuteSystemRingtone() {
        if (Utils.isSilentSystemRingtoneManually) {
            AudioManager audioManager = (AudioManager) getSystemService("audio");
            audioManager.setStreamVolume(2, Utils.Last_Volume_Level, 0);
            Utils.Last_Volume_Level = audioManager.getStreamVolume(2);
            Utils.isSilentSystemRingtoneManually = false;
        }
    }

    private void changeAnswerToOutgoing() {
        Intent intent = new Intent();
        intent.setAction(Activity_Calling_InComing_Outgoing.ACTIONC_CHANGE);
        sendBroadcast(intent);
    }

    private void startMissedCallPOp(Call call) {
        try {
            Utils.numberCall = URLDecoder.decode(call.getDetails().getHandle().toString(), "utf-8").replace("tel:", "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {

            public void run() {
                Intent intent = new Intent();
                intent.setAction("com.hello.action1");
                intent.setAction("com.hello.action");
                CallService.this.sendBroadcast(intent);
                Intent intent2 = new Intent(CallService.this, Theme_Activity_Callinfo_Dailog.class);
                intent2.addFlags(268435456);
                intent2.addFlags(32768);
                intent2.addFlags(67108864);
                intent2.putExtra(PhoneCallReceiver.NUMBER_STATE, Utils.numberCall);
                intent2.putExtra(PhoneCallReceiver.START_STATE, Utils.callStartTime);
                intent2.putExtra(PhoneCallReceiver.END_STATE, Utils.callEndTime);
                if (Utils.callEndTime != 0) {
                    intent2.putExtra(PhoneCallReceiver.STATE, 2);
                } else {
                    intent2.putExtra(PhoneCallReceiver.STATE, 3);
                }
                CallService.this.startActivity(intent2);
            }
        }, 1500);
    }

    private void dismissIncomingScreen() {
        unMuteSystemRingtone();
        Intent intent = new Intent();
        intent.setAction(Activity_Calling_InComing_Outgoing.ActionCallAnswerStop);
        sendBroadcast(intent);
    }

    private void showIncomingCallPOpup() {
        Intent intent = new Intent(this, Activity_Calling_InComing_Outgoing.class);
        intent.putExtra(PhoneCallReceiver.NUMBER_STATE, PhoneNumberUtils.formatNumber(Utils.numberCall));
        intent.putExtra(Utils.ISINCOMING, true);
        intent.addFlags(268435456);
        startActivity(intent);
    }

    private void startOutGoingProcess() {
        Log.e("AAA", "Outgoig start theme");
        Intent intent = new Intent();
        intent.setAction("com.hello.action1");
        intent.setAction("com.hello.action");
        intent.setAction("com.hello.missedCall");
        sendBroadcast(intent);
        Intent intent2 = new Intent(this, Activity_Calling_InComing_Outgoing.class);
        intent2.addFlags(268435456);
        startActivity(intent2);
        Utils.isOutgoingScreenActive = true;
    }

    public static void unResigterCallback() {
        MyCallBackCall myCallBackCall = mycall;
        if (myCallBackCall != null) {
            CallManager.unregisterCallback(myCallBackCall);
        }
        CallManager.sCall = null;
    }

    public static void doPhoneHandling(Context context, int i) {
        try {
            if (tm != null && ActivityCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == 0) {
                List<PhoneAccountHandle> callCapablePhoneAccounts = tm.getCallCapablePhoneAccounts();
                if (CallManager.sCall != null) {
                    CallManager.sCall.phoneAccountSelected(callCapablePhoneAccounts.get(i), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getSimCount() {
        SubscriptionManager subscriptionManager = (SubscriptionManager) getSystemService("telephony_subscription_service");
        if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") != 0) {
            return -1;
        }
        Log.d("count", "" + subscriptionManager.getActiveSubscriptionInfoCount());
        return subscriptionManager.getActiveSubscriptionInfoCount();
    }

    public void updatenoti() {
        if (CallManager.sCall == null || CallManager.callWaiting == null) {
            if (CallManager.sCall != null) {
                if (CallManager.sCall.getState() == 4) {
                    try {
                        String replace = URLDecoder.decode(CallManager.sCall.getDetails().getHandle().toString(), "utf-8").replace("tel:", "");
                        setUpNotification(true, getContactName(this, replace), replace);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            } else if (CallManager.callWaiting != null && CallManager.callWaiting.getState() == 4) {
                try {
                    String replace2 = URLDecoder.decode(CallManager.callWaiting.getDetails().getHandle().toString(), "utf-8").replace("tel:", "");
                    setUpNotification(true, getContactName(this, replace2), replace2);
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                }
            }
        } else if (CallManager.sCall.getState() == 4) {
            try {
                String replace3 = URLDecoder.decode(CallManager.sCall.getDetails().getHandle().toString(), "utf-8").replace("tel:", "");
                setUpNotification(true, getContactName(this, replace3), replace3);
            } catch (UnsupportedEncodingException e3) {
                e3.printStackTrace();
            }
        } else if (CallManager.callWaiting.getState() == 4) {
            try {
                String replace4 = URLDecoder.decode(CallManager.callWaiting.getDetails().getHandle().toString(), "utf-8").replace("tel:", "");
                setUpNotification(true, getContactName(this, replace4), replace4);
            } catch (UnsupportedEncodingException e4) {
                e4.printStackTrace();
            }
        }
    }
}
