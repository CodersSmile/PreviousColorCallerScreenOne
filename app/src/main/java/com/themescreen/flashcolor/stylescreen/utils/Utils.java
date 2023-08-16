package com.themescreen.flashcolor.stylescreen.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.anchorfree.partner.api.ClientInfo;
import com.anchorfree.partner.api.auth.AuthMethod;
import com.anchorfree.partner.api.data.Country;
import com.anchorfree.partner.api.response.User;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.vpnsdk.callbacks.Callback;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.OnAppOpenDisplayed;
import com.themescreen.flashcolor.stylescreen.vpnstatus.CountryModal;
import com.themescreen.flashcolor.stylescreen.vpnstatus.Country_Code;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Utils {
    public static List<Country> list;
    public static List<Country_Code> list_Country;
    public static int counter = 0;
    public static int totalClick = 0;
    public static final String ISINCOMING = "IsIncoming";
    public static int Last_Volume_Level = 0;
    public static long callEndTime = 0;
    public static long callStartTime = 0;
    public static boolean isOutgoingScreenActive = false;
    public static boolean isSilentSystemRingtoneManually = false;
    public static String numberCall = "";
    private static Toast toast;

    public static void displayToast(Context context, String str) {
        Toast toast2 = toast;
        if (toast2 != null && toast2.getView().isShown()) {
            toast.cancel();
            toast = null;
        }
        if (!TextUtils.isEmpty(str)) {
            Toast makeText = Toast.makeText(context, str, Toast.LENGTH_SHORT);
            toast = makeText;
            makeText.show();
        }
    }



    private static boolean checkNet() {
        try {
            return Runtime.getRuntime().exec("ping -c 1 google.com").waitFor() == 0;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean youDesirePermissionCode(Activity activity) {
        boolean z;
        if (Build.VERSION.SDK_INT >= 23) {
            z = Settings.System.canWrite(activity);
        } else {
            z = ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_SETTINGS") == 0;
        }
        return z;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static boolean isPhoneLocked(Context context) {
        try {
            if (((KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE)).isDeviceLocked()) {
                return true;
            }
            return !((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isInteractive();
        } catch (Exception unused) {
            return true;
        }
    }

    public static void setring(Context context, String str) {
        File file = new File(str);
        ContentValues contentValues = new ContentValues();
        contentValues.put("_data", file.getAbsolutePath());
        contentValues.put("title", file.getName());
        contentValues.put("_size", Long.valueOf(file.length()));
        contentValues.put("mime_type", "audio/mp3");
        contentValues.put("artist", context.getResources().getString(R.string.app_name));
        contentValues.put("is_ringtone", (Boolean) true);
        contentValues.put("is_notification", (Boolean) false);
        contentValues.put("is_alarm", (Boolean) false);
        contentValues.put("is_music", (Boolean) false);
        Uri contentUriForPath = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
        context.getContentResolver().delete(contentUriForPath, "_data=\"" + file.getAbsolutePath() + "\"", null);
        RingtoneManager.setActualDefaultRingtoneUri(context, 1, context.getContentResolver().insert(contentUriForPath, contentValues));
        displayToast(context, "Set as Ringtone Successfully.");
    }



    @SuppressLint("Range")
    public static String getContactName(Context context, String str) {
        String str2;
        try {
            Cursor query = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), new String[]{"display_name"}, null, null, null);
            if (query == null) {
                return null;
            }
            if (query.moveToFirst()) {
                str2 = query.getString(query.getColumnIndex("display_name"));
            } else {
                str2 = "";
            }
            if (query != null && !query.isClosed()) {
                query.close();
            }
            return str2;
        } catch (Exception unused) {
            return "";
        }
    }


    public static Bitmap retrieveContactPhoto(Context context, String str, String str2) {
        Bitmap bitmap = null;
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
                InputStream openPhoto = openPhoto(context, new Long(str2).longValue());
                if (openPhoto != null) {
                    bitmap = BitmapFactory.decodeStream(openPhoto);
                }
                if (openPhoto != null) {
                    openPhoto.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static InputStream openPhoto(Context context, long j) {
        try {
            return context.getContentResolver().openAssetFileDescriptor(Uri.withAppendedPath(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, j), "display_photo"), "r").createInputStream();
        } catch (IOException unused) {
            return null;
        }
    }

    public static String milliSecondsToTimer(long j) {
        String str;
        int i = (int) (j / 3600000);
        long j2 = j % 3600000;
        int i2 = ((int) j2) / 60000;
        int i3 = (int) ((j2 % 60000) / 1000);
        if (i > 0) {
            String str2 = i + ":";
        }
        if (i3 < 10) {
            str = "0" + i3;
        } else {
            str = "" + i3;
        }
        return i2 + ":" + str;
    }

    public static void onClickEvent(final View view) {
        view.setEnabled(false);
        view.postDelayed(new Runnable() {

            public void run() {
                view.setEnabled(true);
            }
        }, 150);
    }

    public static boolean isMyServiceRunning(Context context, Class<?> cls) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isAlreadyDefaultDialer(Context context) {
        return context.getPackageName().equals(((TelecomManager) context.getSystemService(Context.TELECOM_SERVICE)).getDefaultDialerPackage());
    }

    public static int getResId(String str, Class<?> cls) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            return declaredField.getInt(declaredField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getThemeData(Context context) {
        return context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).getString("Theme", "theme_res key_1_selector");
    }

    public static String getBackgroundData(Context context) {
        return context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).getString("Background", "bg_res bg_1");
    }

    public static void setSoundData(Context context, Boolean bool) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).edit();
        edit.putBoolean("Sound", bool.booleanValue());
        edit.commit();
    }

    public static boolean getSoundData(Context context) {
        return context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).getBoolean("Sound", false);
    }

    public static String formatSeconds(long j) {
        int i = (int) (j / 3600);
        int i2 = (int) (j - ((long) (i * 3600)));
        int i3 = i2 / 60;
        int i4 = i2 - (i3 * 60);
        String str = "";
        if (i > 0) {
            str = str + i + "h ";
        }
        return (str + i3 + "m ") + i4 + "s";
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        Uri uri2 = null;
        if (!(Build.VERSION.SDK_INT >= 19) || !DocumentsContract.isDocumentUri(context, uri)) {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } else if (isExternalStorageDocument(uri)) {
            String[] split = DocumentsContract.getDocumentId(uri).split(":");
            if ("primary".equalsIgnoreCase(split[0])) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
        } else if (isDownloadsDocument(uri)) {
            return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
        } else if (isMediaDocument(uri)) {
            String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
            String str = split2[0];
            if ("image".equals(str)) {
                uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(str)) {
                uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(str)) {
                uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String str, String[] strArr) {
        Throwable th;
        Cursor cursor = null;
        try {
            Cursor query = context.getContentResolver().query(uri, new String[]{"_data"}, str, strArr, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        String string = query.getString(query.getColumnIndexOrThrow("_data"));
                        if (query != null) {
                            query.close();
                        }
                        return string;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor = query;
                    if (cursor != null) {
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
                cursor.close();
            }
            try {
                throw th;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return str;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static MultipartBody.Part getMultiPartBody(String str, String str2) {
        if (str2 == null) {
            return MultipartBody.Part.createFormData(str, "");
        }
        File file = new File(str2);
        return MultipartBody.Part.createFormData(str, file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
    }

    public static void setRingtone(Context context, String str) {
        if (str != null) {
            File file = new File(str);
            ContentValues contentValues = new ContentValues();
            contentValues.put("_data", file.getAbsolutePath());
            contentValues.put("title", str.substring(str.lastIndexOf("/") + 1));
            contentValues.put("mime_type", "audio/mp3");
            contentValues.put("_size", Long.valueOf(file.length()));
            contentValues.put("is_ringtone", (Boolean) true);
            Uri contentUriForPath = MediaStore.Audio.Media.getContentUriForPath(str);
            Cursor query = context.getContentResolver().query(contentUriForPath, new String[]{"_id"}, "_data=?", new String[]{str}, null);
            if (query != null && query.moveToFirst() && query.getCount() > 0) {
                String string = query.getString(0);
                contentValues.put("is_ringtone", (Boolean) true);
                context.getContentResolver().update(contentUriForPath, contentValues, "_data=?", new String[]{str});
                try {
                    RingtoneManager.setActualDefaultRingtoneUri(context, 1, ContentUris.withAppendedId(contentUriForPath, Long.valueOf(string).longValue()));
                    displayToast(context, "Set as Ringtone Successfully.");
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                query.close();
            }
        }
    }

    public static String Base_Url() {
        String decrypted = "";
        try {
            decrypted = AESUtils.decrypt("9D809B722B040E56F77668FD5D96DD63F9BE701748307590E64381E11820191B");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypted;
    }
    public static String getKeyHash(Context activity) {
        PackageInfo packageInfo;
        try {
            packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest digest;
                digest = MessageDigest.getInstance("SHA");
                digest.update(signature.toByteArray());
                String something = (Base64.encodeToString(digest.digest(), Base64.NO_WRAP));
                return something.replace("+", "*");
            }
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();

        } catch (NoSuchAlgorithmException e) {

        } catch (Exception e) {

        }
        return null;
    }
    public static void loginClientInfo(ClientInfo clientInfo, OnAppOpenDisplayed onAppOpenDisplayed) {
        UnifiedSDK unifiedSDK = UnifiedSDK.getInstance(clientInfo);
        AuthMethod authMethod = AuthMethod.anonymous();
        unifiedSDK.getBackend().login(authMethod, new Callback<User>() {
            @Override
            public void success(@NonNull User user) {
                Log.e("TAG", "success: " + user.getAccessToken() + " " + user.getSubscriber());
                onAppOpenDisplayed.OnSucess();
            }
            @Override
            public void failure(@NonNull VpnException e) {
                onAppOpenDisplayed.OnError();
                Log.e("TAG", "failure: ", e);
            }
        });
    }
    public static CountryModal getCountryFlagPath(String a, Activity activity) {
        String jObject = loadJSONFromAsset("country_flag.json", activity);
        try {
            JSONObject object = new JSONObject(jObject);
            JSONArray array = object.getJSONArray("country_flag");
            for (int i = 0; i < array.length(); i++) {
                JSONObject list = array.getJSONObject(i);
                String path = list.getString("url");
                String name = list.getString("Name");
                String code = list.getString("Code");
                CountryModal countryModal = new CountryModal();
                countryModal.setCode(code);
                countryModal.setName(name);
                countryModal.setPathFlag(path);
                if (a.equalsIgnoreCase(code)) {
                    return countryModal;
                }
            }

        } catch (JSONException e) {

        }
        return null;
    }
    public static String loadJSONFromAsset(String a, Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(a);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
