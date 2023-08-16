package com.themescreen.flashcolor.stylescreen.utils;

import android.content.Context;
import android.telecom.TelecomManager;

public class Call_Permission {
    TelecomManager systemService;

    public boolean isappdefault(Context context) {
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        this.systemService = telecomManager;
        return (telecomManager.getDefaultDialerPackage() == null || !this.systemService.getDefaultDialerPackage().equals(context.getPackageName()) || context.getPackageName() == null) ? false : true;
    }
}
