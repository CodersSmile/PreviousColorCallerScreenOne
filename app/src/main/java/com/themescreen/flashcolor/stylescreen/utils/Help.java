package com.themescreen.flashcolor.stylescreen.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.themescreen.flashcolor.stylescreen.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Help {
    public static String Image_Path = null;
    private static final String TAG = "Jay_Help";
    public static int checkAds;
    public static int height;
    public static String mAudioPath;
    public static Bitmap mBitmap;
    public static boolean mBoolean;
    public static Bitmap mOriginal;
    public static TextView mTextView;
    public static Uri mUri;
    public static String mVideoPath;
    public static String mWaterMarkPath;
    public static Bitmap stickBit;
    public static int width;

    public static Bitmap bitmapResize(Bitmap bitmap, int i, int i2) {
        int width2 = bitmap.getWidth();
        int height2 = bitmap.getHeight();
        if (width2 >= height2) {
            int i3 = (height2 * i) / width2;
            if (i3 > i2) {
                i = (i * i2) / i3;
            } else {
                i2 = i3;
            }
        } else {
            int i4 = (width2 * i2) / height2;
            if (i4 > i) {
                i2 = (i2 * i) / i4;
            } else {
                i = i4;
            }
        }
        return Bitmap.createScaledBitmap(bitmap, i, i2, true);
    }

    public static Bitmap getBitmap(String str) {
        return BitmapFactory.decodeFile(str);
    }

    public static void setLaySize(View view, int i, int i2) {
        int width2 = view.getWidth();
        int height2 = view.getHeight();
        if (i >= i2) {
            int i3 = (i2 * width2) / i;
            if (i3 > height2) {
                width2 = (width2 * height2) / i3;
            } else {
                height2 = i3;
            }
        } else {
            int i4 = (i * height2) / i2;
            if (i4 > width2) {
                height2 = (height2 * width2) / i4;
            } else {
                width2 = i4;
            }
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width2, height2);
        layoutParams.addRule(13);
        view.setLayoutParams(layoutParams);
    }

    public static void showLog(String str) {
        Log.e("999999", str);
    }

    public static Bitmap getBitmap(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getLayoutParams().width, view.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(canvas);
        return createBitmap;
    }

    public static float rotationToStartPoint(MotionEvent motionEvent, Matrix matrix) {
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        float f = (fArr[0] * 0.0f) + (fArr[1] * 0.0f) + fArr[2];
        return (float) Math.toDegrees(Math.atan2((double) (motionEvent.getY(0) - (((fArr[3] * 0.0f) + (fArr[4] * 0.0f)) + fArr[5])), (double) (motionEvent.getX(0) - f)));
    }

    public static PointF midPointToStartPoint(MotionEvent motionEvent, Matrix matrix) {
        PointF pointF = new PointF();
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        pointF.set(((((fArr[0] * 0.0f) + (fArr[1] * 0.0f)) + fArr[2]) + motionEvent.getX(0)) / 2.0f, ((((fArr[3] * 0.0f) + (fArr[4] * 0.0f)) + fArr[5]) + motionEvent.getY(0)) / 2.0f);
        return pointF;
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public static int w(int i) {
        return (width * i) / 1080;
    }

    public static int h(int i) {
        int i2 = height;
        if (i2 <= 1280 || i2 >= 1920) {
            return (i2 * i) / 1920;
        }
        return (i2 * i) / 1280;
    }

    public static Bitmap textAsBitmap(String str, float f, int i, Typeface typeface) {
        Paint paint = new Paint(1);
        paint.setTextSize(f);
        paint.setColor(i);
        paint.setTypeface(typeface);
        paint.setTextAlign(Paint.Align.LEFT);
        float f2 = -paint.ascent();
        Bitmap createBitmap = Bitmap.createBitmap((int) (paint.measureText(str) + 0.5f), (int) (paint.descent() + f2 + 0.5f), Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawText(str, 0.0f, f2, paint);
        return createBitmap;
    }

    public static Bitmap drawMultilineTextToBitmap(Context context, String str, int i, Typeface typeface) {
        float f = context.getResources().getDisplayMetrics().density;
        int i2 = width;
        Bitmap createBitmap = Bitmap.createBitmap(i2, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        TextPaint textPaint = new TextPaint(1);
        textPaint.setColor(i);
        if (str.length() < 25) {
            textPaint.setTextSize((float) ((int) (60.0f * f)));
        } else if (str.length() < 75) {
            textPaint.setTextSize((float) ((int) (40.0f * f)));
        } else {
            textPaint.setTextSize((float) ((int) (20.0f * f)));
        }
        textPaint.setTypeface(typeface);
        int width2 = canvas.getWidth() - ((int) (f * 16.0f));
        StaticLayout staticLayout = new StaticLayout(str, textPaint, width2, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        int height2 = staticLayout.getHeight();
        canvas.save();
        canvas.translate((float) ((createBitmap.getWidth() - width2) / 2), (float) ((createBitmap.getHeight() - height2) / 2));
        staticLayout.draw(canvas);
        canvas.restore();
        return createBitmap;
    }

    public static void deleteRecursive(File file) {
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                deleteRecursive(file2);
            }
        }
        file.delete();
    }

    public static void Toast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void ToastL(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }

    public static Bitmap getMask(Context context, Bitmap bitmap, Bitmap bitmap2) {
        int width2 = bitmap.getWidth();
        int height2 = bitmap.getHeight();
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, width2, height2, true);
        Bitmap createScaledBitmap2 = Bitmap.createScaledBitmap(bitmap2, width2, height2, true);
        Bitmap createBitmap = Bitmap.createBitmap(createScaledBitmap2.getWidth(), createScaledBitmap2.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(createScaledBitmap, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(createScaledBitmap2, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        return createBitmap;
    }

    public static String getImagePath(Context context, Uri uri) {
        String[] strArr = {"_data"};
        Cursor query = context.getContentResolver().query(uri, strArr, null, null, null);
        query.moveToFirst();
        @SuppressLint("Range") String string = query.getString(query.getColumnIndex(strArr[0]));
        query.close();
        return string;
    }

    public static GradientDrawable createGradient(int i, int i2, GradientDrawable.Orientation orientation) {
        GradientDrawable gradientDrawable = new GradientDrawable(orientation, new int[]{i, i2});
        gradientDrawable.setCornerRadius(0.0f);
        return gradientDrawable;
    }

    public static String getPath(Context context, Uri uri) {
        Uri uri2 = null;
        if (!(Build.VERSION.SDK_INT >= 19) || !DocumentsContract.isDocumentUri(context, uri)) {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }
                return getDataColumn(context, uri, null, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } else if (isExternalStorageDocument(uri)) {
            String[] split = DocumentsContract.getDocumentId(uri).split(":");
            if ("primary".equalsIgnoreCase(split[0])) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
        } else if (isDownloadsDocument(uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            if (!TextUtils.isEmpty(documentId)) {
                try {
                    return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId).longValue()), null, null);
                } catch (NumberFormatException e) {
                    Log.i(TAG, e.getMessage());
                    return null;
                }
            }
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
        IllegalArgumentException e;
        Cursor cursor;
        Cursor cursor2 = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, str, strArr, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                        if (cursor != null) {
                            cursor.close();
                        }
                        return string;
                    }
                } catch (IllegalArgumentException e2) {
                    e = e2;
                    try {
                        Log.i(TAG, String.format(Locale.getDefault(), "getDataColumn: _data - [%s]", e.getMessage()));
                    } catch (Throwable th2) {
                        th = th2;
                        cursor2 = cursor;
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        throw th;
                    }
                }
            }
        } catch (IllegalArgumentException e3) {
            e = e3;
            cursor = null;
            Log.i(TAG, String.format(Locale.getDefault(), "getDataColumn: _data - [%s]", e.getMessage()));
        } catch (Throwable th3) {
            th = th3;
            if (cursor2 != null) {
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

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Bitmap RotateBitmap(Bitmap bitmap, float f) {
        Matrix matrix = new Matrix();
        matrix.postRotate(f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap enhanceImage(Bitmap bitmap, float f, float f2) {
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{f, 0.0f, 0.0f, 0.0f, f2, 0.0f, f, 0.0f, 0.0f, f2, 0.0f, 0.0f, f, 0.0f, f2, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public static Bitmap updateSat(Bitmap bitmap, float f) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(f);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public static Bitmap LRBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap UDBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Typeface getTypeface(Context context, String str) {
        return Typeface.createFromAsset(context.getAssets(), str);
    }

    public static void FS(Activity activity) {
        activity.getWindow().addFlags(1024);
    }

    public static void FS2(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(4102);
    }

    public static void HideKeyBoard(Activity activity) {
        activity.getWindow().setSoftInputMode(3);
    }

    public static void next(Context context, Class cls) {
        context.startActivity(new Intent(context, cls));
    }

    public static void nextwithnew(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void unzip(Activity activity, Class cls, File file, File file2) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)));
        try {
            byte[] bArr = new byte[8192];
            while (true) {
                ZipEntry nextEntry = zipInputStream.getNextEntry();
                if (nextEntry != null) {
                    File file3 = new File(file2, nextEntry.getName());
                    File parentFile = nextEntry.isDirectory() ? file3 : file3.getParentFile();
                    if (!parentFile.isDirectory()) {
                        if (!parentFile.mkdirs()) {
                            throw new FileNotFoundException("Failed to ensure directory: " + parentFile.getAbsolutePath());
                        }
                    }
                    if (!nextEntry.isDirectory()) {
                        FileOutputStream fileOutputStream = new FileOutputStream(file3);
                        while (true) {
                            try {
                                int read = zipInputStream.read(bArr);
                                if (read == -1) {
                                    break;
                                }
                                fileOutputStream.write(bArr, 0, read);
                            } finally {
                                fileOutputStream.close();
                            }
                        }
                    }
                } else if (activity == null) {
                    return;
                } else {
                    return;
                }
            }
        } finally {
            zipInputStream.close();
            if (activity != null) {
                Intent intent = new Intent(activity, cls);
                intent.putExtra("path", file.getAbsolutePath().replace(".zip", ""));
                activity.startActivity(intent);
                activity.finish();
            }
        }
    }

    public static int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static int getNavBarHeight(Context context) {
        int i;
        boolean hasPermanentMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean deviceHasKey = KeyCharacterMap.deviceHasKey(4);
        if (hasPermanentMenuKey || deviceHasKey) {
            return 0;
        }
        Resources resources = context.getResources();
        int i2 = resources.getConfiguration().orientation;
        String str = "navigation_bar_height";
        if (isTablet(context)) {
            if (i2 != 1) {
                str = "navigation_bar_height_landscape";
            }
            i = resources.getIdentifier(str, "dimen", "android");
        } else {
            if (i2 != 1) {
                str = "navigation_bar_width";
            }
            i = resources.getIdentifier(str, "dimen", "android");
        }
        if (i > 0) {
            return resources.getDimensionPixelSize(i);
        }
        return 0;
    }

    private static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public static int getRealHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 17) {
            defaultDisplay.getRealMetrics(displayMetrics);
            return displayMetrics.heightPixels;
        }
        try {
            return ((Integer) Display.class.getMethod("getRawHeight", new Class[0]).invoke(defaultDisplay, new Object[0])).intValue();
        } catch (Exception unused) {
            defaultDisplay.getMetrics(displayMetrics);
            return displayMetrics.heightPixels;
        }
    }

    public static int getScreenWidth(Context context) {
        if (context == null) {
            return 320;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (windowManager == null) {
            return 320;
        }
        windowManager.getDefaultDisplay().getSize(point);
        return point.x;
    }

    public static String getVideoDate(String str) {
        File file = new File(str);
        return file.exists() ? getDate(file.lastModified()) : "";
    }

    public static String getVideoDuration(String str) {
        String str2 = "";
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(str);
            mediaPlayer.prepare();
            str2 = getDuration((long) mediaPlayer.getDuration());
            mediaPlayer.reset();
            return str2;
        } catch (Exception e) {
            e.printStackTrace();
            return str2;
        }
    }

    public static String getTime(long j) {
        return DateFormat.format("hh:mm aa", new Date(j)).toString();
    }

    public static String getDuration(long j) {
        return String.format("%02d:%02d", Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j))));
    }

    public static String getDate(long j) {
        return DateFormat.format("dd-MMM-yyyy", new Date(j)).toString();
    }

    public static String getDateTime(long j) {
        return DateFormat.format("dd-MMM-yyyy hh-mm-ss", new Date(j)).toString();
    }

    public static int getDay(long j) {
        DateFormat.format("dd", new Date(j)).toString();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(j);
        return gregorianCalendar.get(5);
    }

    public static int getMonth(long j) {
        DateFormat.format("MM", new Date(j)).toString();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(j);
        return gregorianCalendar.get(2);
    }

    public static String getMonth2(long j) {
        return DateFormat.format("MMM", new Date(j)).toString();
    }

    public static int getYear(long j) {
        return Integer.parseInt(DateFormat.format("yyyy", new Date(j)).toString());
    }

    public static int getHour(long j) {
        DateFormat.format("hh", new Date(j)).toString();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(j);
        return gregorianCalendar.get(10);
    }

    public static int getMinutes(long j) {
        DateFormat.format("mm", new Date(j)).toString();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(j);
        return gregorianCalendar.get(12);
    }

    public static boolean hasChar(String str) {
        return str.matches(".*[0-9].*") || str.matches(".*[A-Z].*") || str.matches(".*[a-z].*");
    }

    public static void getheightandwidth(Context context) {
        getHeight(context);
        getwidth(context);
    }

    public static int getwidth(Context context) {
        int i = context.getResources().getDisplayMetrics().widthPixels;
        width = i;
        return i;
    }

    public static int getHeight(Context context) {
        int i = context.getResources().getDisplayMetrics().heightPixels;
        height = i;
        return i;
    }

    public static void setSize(View view, int i, int i2, boolean z) {
        if (z) {
            view.getLayoutParams().height = setWidth(i2);
            view.getLayoutParams().width = setWidth(i);
            return;
        }
        view.getLayoutParams().height = setHeight(i2);
        view.getLayoutParams().width = setHeight(i);
    }

    public static int setHeight(int i) {
        return (height * i) / 1920;
    }

    public static int setWidth(int i) {
        return (width * i) / 1080;
    }

    public static void gone(View view) {
        view.setVisibility(View.GONE);
    }

    public static void seekThumb(SeekBar seekBar, int i) {
        seekBar.getThumb().setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
    }

    public static void visible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static void invisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    public static void setCenter(View view) {
        ((RelativeLayout.LayoutParams) view.getLayoutParams()).addRule(13);
    }

    public static void setCenterHorizontal(View view) {
        ((RelativeLayout.LayoutParams) view.getLayoutParams()).addRule(14);
    }

    public static void setCenterVertical(View view) {
        ((RelativeLayout.LayoutParams) view.getLayoutParams()).addRule(15);
    }

    public static void setMargin(View view, int i, int i2, int i3, int i4, boolean z) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (z) {
            marginLayoutParams.setMargins(w(i), h(i2), w(i3), h(i4));
        } else {
            marginLayoutParams.setMargins(w(i), w(i2), w(i3), w(i4));
        }
    }

    public static void set_share_rate(final Context context, View view, View view2) {
        view.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + context.getPackageName());
                context.startActivity(Intent.createChooser(intent, "Share via"));
            }
        });
        view2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try {
                    context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + context.getPackageName())));
                } catch (ActivityNotFoundException unused) {
                    context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
                }
            }
        });
    }

    public static void share(Context context) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + context.getPackageName());
        context.startActivity(Intent.createChooser(intent, "Share via"));
    }

    public static void rate(Context context) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (ActivityNotFoundException unused) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static String getDay(int i, int i2, int i3) {
        Calendar instance = Calendar.getInstance();
        instance.set(i, i2, i3);
        switch (instance.get(7)) {
            case 1:
                return "Sun";
            case 2:
                return "Mon";
            case 3:
                return "Tue";
            case 4:
                return "Wed";
            case 5:
                return "Thu";
            case 6:
                return "Fri";
            case 7:
                return "Sat";
            default:
                return "";
        }
    }

    public static String getMonth(int i, int i2, int i3) {
        Calendar instance = Calendar.getInstance();
        instance.set(i, i2, i3);
        switch (instance.get(2)) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "";
        }
    }

    public static void hideKeyBoard(Context context, View view) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String capitaliseName(String str) {
        String[] split = str.split(" ");
        String str2 = "";
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].trim().toLowerCase();
            if (!split[i].isEmpty()) {
                str2 = str2 + split[i].substring(0, 1).toUpperCase() + split[i].substring(1) + " ";
            }
        }
        return str2.trim();
    }

    public static String capitaliseOnlyFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getThemePath(Context context) {
        return Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName() + "/files/.Theme/";
    }

    public static String getFolderPath(Context context) {
        return Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.app_name) + "/";
    }

    public static String getString(Context context, int i) {
        return context.getResources().getString(i);
    }

    public static String getFileSize(long j) {
        double d = (double) j;
        Double.isNaN(d);
        Double.isNaN(d);
        double d2 = d / 1024.0d;
        double d3 = d2 / 1024.0d;
        if (d3 > 1.0d) {
            return new DecimalFormat("0.00").format(d3).concat("MB");
        }
        return new DecimalFormat("0").format(Math.round(d2)).concat("KB");
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static ProgressDialog setPD(Context context, String str, boolean z) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(str);
        progressDialog.setCancelable(z);
        return progressDialog;
    }

    public static void setLay(View view, int i, int i2, boolean z) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = w(i);
        if (z) {
            layoutParams.height = h(i2);
        } else {
            layoutParams.width = w(i2);
        }
    }

    public static BitmapDrawable RepeatedBitmap(Context context, int i, Bitmap bitmap) {
        if (i != -1) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), i);
        }
        int i2 = width;
        Bitmap.createScaledBitmap(bitmap, (i2 * 100) / 720, (i2 * 100) / 720, true);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        return bitmapDrawable;
    }

    public static ArrayList<String> setList(Context context, String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.clear();
        try {
            for (String str2 : context.getAssets().list(str)) {
                arrayList.add(str2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static int getColor(Context context, int i) {
        return context.getResources().getColor(i);
    }

    public static class AppPreferences {
        private static String PREFS_NAME;
        String AAG = "AAG";
        String Answer = "Answer";
        String LockApps = "LockApps";
        String LockType = "LockType";
        String PatternLock = "PatternLock";
        String PinLock = "PinLock";
        String Question = "Question";
        private final SharedPreferences.Editor editor;
        private final SharedPreferences preferences;

        public AppPreferences(Context context) {
            String string = context.getResources().getString(R.string.app_name);
            PREFS_NAME = string;
            SharedPreferences sharedPreferences = context.getSharedPreferences(string, 0);
            this.preferences = sharedPreferences;
            this.editor = sharedPreferences.edit();
        }

        public void setLockApps(String str) {
            this.editor.putString(this.LockApps, str);
            this.editor.commit();
        }

        public String getLockApps() {
            return this.preferences.getString(this.LockApps, "");
        }

        public void setLockType(String str) {
            this.editor.putString(this.LockType, str);
            this.editor.commit();
        }

        public void setAAG(boolean z) {
            this.editor.putBoolean(this.AAG, z);
            this.editor.commit();
        }

        public boolean getAAG() {
            return this.preferences.getBoolean(this.AAG, false);
        }

        public String getLockType() {
            return this.preferences.getString(this.LockType, "Pattern");
        }

        public void setPatternLock(String str) {
            this.editor.putString(this.PatternLock, str);
            this.editor.commit();
        }

        public String getPatternLock() {
            return this.preferences.getString(this.PatternLock, "");
        }

        public void setPinLock(String str) {
            this.editor.putString(this.PinLock, str);
            this.editor.commit();
        }

        public String getPinLock() {
            return this.preferences.getString(this.PinLock, "");
        }

        public void setQuestion(String str) {
            this.editor.putString(this.Question, str);
            this.editor.commit();
        }

        public String getQuestion() {
            return this.preferences.getString(this.Question, "");
        }

        public void setAnswer(String str) {
            this.editor.putString(this.Answer, str);
            this.editor.commit();
        }

        public String getAnswer() {
            return this.preferences.getString(this.Answer, "");
        }
    }

    public static float convertDpToPixel(float f, Context context) {
        return f * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }
}
