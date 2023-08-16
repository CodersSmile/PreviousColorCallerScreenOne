package com.themescreen.flashcolor.stylescreen.utils;

import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

public class ColorCallUtils {
    public static String getContactNamebytherenumber(Context context, String str) {
        try {
            Cursor query = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), new String[]{"display_name"}, null, null, null);
            if (query == null) {
                return "Unknown";
            }
            String str2 = "";
            if (query.moveToFirst()) {
                str2 = query.getString(query.getColumnIndex("display_name"));
            }
            if (query != null && !query.isClosed()) {
                query.close();
            }
            return str2;
        } catch (Exception unused) {
            return "Unknown";
        }
    }

    public static Bitmap retrieveContactPhoto(Context context, String str) {
        String str2;
        Cursor query = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), new String[]{"display_name", "_id"}, null, null, null);
        Bitmap bitmap = null;
        if (query != null) {
            str2 = null;
            while (query.moveToNext()) {
                str2 = query.getString(query.getColumnIndexOrThrow("_id"));
            }
            query.close();
        } else {
            str2 = null;
        }
        if (str2 != null) {
            try {
                InputStream openContactPhotoInputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(str2).longValue()));
                if (openContactPhotoInputStream != null) {
                    bitmap = BitmapFactory.decodeStream(openContactPhotoInputStream);
                }
                if (openContactPhotoInputStream != null) {
                    openContactPhotoInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static boolean isMyServiceRunning(Context context) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if ("com.call.theme.colorcallflash.Service.CallInComingService".equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void onClickEvent(final View view) {
        view.setEnabled(false);
        view.postDelayed(new Runnable() {

            public void run() {
                view.setEnabled(true);
            }
        }, 150);
    }
}
