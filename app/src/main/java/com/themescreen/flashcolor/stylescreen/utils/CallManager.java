package com.themescreen.flashcolor.stylescreen.utils;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.telecom.Call;
import android.text.TextUtils;
import android.widget.Toast;

import com.themescreen.flashcolor.stylescreen.Activity.Activity_Calling_InComing_Outgoing;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.Service.CallService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class CallManager {
    public static Call callWaiting = null;
    private static CallWaitingWindow callWaitingWindow = null;
    public static boolean callrecivefromnotifiction = false;
    public static CameraManager camManager;
    public static Context cn;
    public static Call sCall;

    public static int getSimSelection(Context context) {
        return 1;
    }

    public static void call(Context context, String str) {
        try {
            cn = context;
            Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel: " + Uri.encode(str)));
            int simSelection = getSimSelection(context);
            if (simSelection != -1) {
                intent.putExtra("com.android.phone.extra.slot", simSelection);
            }
            context.startActivity(intent);
        } catch (SecurityException unused) {
            Toast.makeText(context, "Couldn't make a theme due to security reasons", Toast.LENGTH_LONG).show();
        } catch (NullPointerException unused2) {
            Toast.makeText(context, "Couldnt make a theme, no phone number", Toast.LENGTH_LONG).show();
        }
    }

    public static void answer() {
        Call call = sCall;
        if (call != null) {
            call.answer(0);
        }
    }

    public static void reject() {
        List<Call> calls = CallService.callService.getCalls();
        if (calls.size() > 1) {
            for (Call call : calls) {
                if (call.getState() == 4) {
                    call.disconnect();
                }
            }
        } else if (calls.size() == 1) {
            Call call2 = calls.get(0);
            if (call2.getState() == 2) {
                call2.reject(false, null);
            } else {
                call2.disconnect();
            }
        }
    }

    public static void reject(Call call) {
        if (call == null) {
            return;
        }
        if (call.getState() == 2) {
            call.reject(false, null);
        } else {
            call.disconnect();
        }
    }

    public static boolean hold(boolean z) {
        Call call = sCall;
        if (call == null) {
            return false;
        }
        if (z) {
            call.hold();
            return true;
        }
        call.unhold();
        return true;
    }

    public static void keypad(char c) {
        Call call = sCall;
        if (call != null) {
            call.playDtmfTone(c);
            sCall.stopDtmfTone();
        }
    }

    public static void addCall(Call call) {
        Call call2 = sCall;
        if (call2 != null) {
            call2.conference(call);
        }
    }

    public static void registerCallback(Activity_Calling_InComing_Outgoing.Callback callback) {
        Call call = sCall;
        if (call != null) {
            call.registerCallback(callback);
        }
    }

    public static void unregisterCallback(Call.Callback callback) {
        Call call = sCall;
        if (call != null) {
            call.unregisterCallback(callback);
        }
    }

    public static int getState() {
        Call call = sCall;
        if (call == null) {
            return 7;
        }
        return call.getState();
    }

    public static void showWaitingPop(Context context, Call call) {
        CallWaitingWindow callWaitingWindow2 = callWaitingWindow;
        if (callWaitingWindow2 != null) {
            callWaitingWindow2.cleanUp();
        }
        callWaiting = call;
        callWaitingWindow = new CallWaitingWindow(context);
        String str = null;
        try {
            str = URLDecoder.decode(call.getDetails().getHandle().toString(), "utf-8").replace("tel:", "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(str)) {
            callWaitingWindow.setAsWindow(str);
        }
    }

    public static void cleanUpWaiting() {
        CallWaitingWindow callWaitingWindow2 = callWaitingWindow;
        if (callWaitingWindow2 != null) {
            callWaitingWindow2.cleanUp();
            callWaitingWindow.finishWindow();
        }
        callWaitingWindow = null;
    }

    public static void updateWindow(Context context, Call call) {
        if (call != null) {
            try {
                String replace = URLDecoder.decode(call.getDetails().getHandle().toString(), "utf-8").replace("tel:", "");
                String contactNamebytherenumber = ColorCallUtils.getContactNamebytherenumber(context, replace);
                if (TextUtils.isEmpty(contactNamebytherenumber) || contactNamebytherenumber.equals("null") || contactNamebytherenumber.equals("")) {
                    callWaitingWindow.setName(replace);
                } else {
                    callWaitingWindow.setName(contactNamebytherenumber);
                }
                if (callrecivefromnotifiction && callWaiting != null) {
                    callWaitingWindow.btnBack.setImageResource(R.drawable.swap_unpressed);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
