package com.themescreen.flashcolor.stylescreen.utils;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Utils_Stroage {
    public static String TAG = "STORAGE_TAG";

    public static String create_folder(String str) {
        if (Build.VERSION.SDK_INT <= 29) {
            File file = new File(Environment.getExternalStorageDirectory() + "/" + str);
            Log.d(TAG, "createDir:" + file);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        File file2 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "/" + str)));
        Log.d(TAG, "createDir:" + file2);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        return file2.getAbsolutePath();
    }

    public static String create_hidden_folder(String str) {
        if (Build.VERSION.SDK_INT <= 29) {
            File file = new File(Environment.getExternalStorageDirectory() + "/." + str);
            Log.d(TAG, "createDir:" + file);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        File file2 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "/." + str)));
        Log.d(TAG, "createDir:" + file2);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        return file2.getAbsolutePath();
    }

    public static String create_folder_with_sub_folder(String str, String str2, String str3) {
        if (Build.VERSION.SDK_INT <= 29) {
            File file = new File(Environment.getExternalStorageDirectory() + "/" + str + "/" + str2);
            Log.d(TAG, "createDir:" + file);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
        File file2 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(str3 + File.separator + str + File.separator + str2)));
        Log.d(TAG, "createDir:" + file2);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        return file2.getAbsolutePath();
    }

    public static String create_folder_in_app_package_dir(Context context, String str) {
        File file = new File(context.getFilesDir(), str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String saveimage(Context context, Bitmap bitmap, String str, String str2) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file, str2);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast(context, "Saved Successfully " + file2);
            String absolutePath = file2.getAbsolutePath();
            MediaScannerConnection.scanFile(context, new String[]{file2.getPath()}, null, null);
            return absolutePath;
        } catch (Exception e) {
            Toast(context, "Failed to Save");
            Log.e(TAG, "saveimage: " + e.getMessage());
            return null;
        }
    }

    public static void downloadvideo(Context context, String str, String str2, String str3) {
        new DownloadFileFromURL(context, str2, str3).execute(str);
    }

    public static void Toast(Context context, String str) {
        Toast(context, str, 0);
    }

    public static void Toast(Context context, String str, int i) {
        Toast.makeText(context, str, i).show();
    }

    public static ArrayList<File> getfolderdata(String str) {
        ArrayList<File> arrayList = new ArrayList<>();
        if (!new File(str).exists()) {
            return null;
        }
        File file = new File(str);
        Log.d(TAG, "onResume: " + file);
        File[] listFiles = file.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return null;
        }
        for (File file2 : listFiles) {
            arrayList.add(file2);
        }
        return arrayList;
    }

    public static boolean deletefile(Context context, String str) {
        File file = new File(str);
        if (!file.exists() || !file.delete()) {
            return false;
        }
        MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, null, null);
        return true;
    }
    public static String createFolderInAppPackage(Context context, String str) {
        File file = new File(context.getFilesDir(), str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    private static class DownloadFileFromURL extends AsyncTask<String, Integer, String> {
        Context context;
        String nameoffile = "";
        String pathFile = "";
        String pathFolder = "";
        ProgressDialog pd;

        public DownloadFileFromURL(Context context2, String str, String str2) {
            this.context = context2;
            this.pathFolder = str;
            this.nameoffile = str2;
        }


        public String doInBackground(String... strArr) {
            InputStream inputStream;
            HttpURLConnection httpURLConnection;
            Throwable th;
            Exception e;
            FileOutputStream fileOutputStream;
            InputStream inputStream2;
            String str = this.pathFolder + "/" + this.nameoffile;
            this.pathFile = str;
            int i = 1;
            FileOutputStream fileOutputStream2 = null;
            MediaScannerConnection.scanFile(this.context, new String[]{str}, null, null);
            File file = new File(this.pathFolder);
            if (!file.exists()) {
                file.mkdirs();
            }
            try {
                httpURLConnection = (HttpURLConnection) new URL(strArr[0]).openConnection();
                try {
                    httpURLConnection.connect();
                    Log.d(Utils_Stroage.TAG, "doInBackground: connectioncode " + httpURLConnection.getResponseCode());
                    if (httpURLConnection.getResponseCode() != 200) {
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        return "Please Enter Valid Name";
                    }
                    int contentLength = httpURLConnection.getContentLength();
                    inputStream = httpURLConnection.getInputStream();
                    try {
                        Log.e(Utils_Stroage.TAG, "doInBackground: " + this.pathFile);
                        fileOutputStream = new FileOutputStream(this.pathFile);
                    } catch (Exception e2) {
                        e = e2;
                        try {
                            String exc = e.toString();
                            if (fileOutputStream2 != null) {
                            }
                            if (inputStream != null) {
                            }
                            if (httpURLConnection != null) {
                            }
                            return exc;
                        } catch (Throwable th2) {
                            th = th2;
                            if (fileOutputStream2 != null) {
                            }
                            if (inputStream != null) {
                            }
                            if (httpURLConnection != null) {
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        if (fileOutputStream2 != null) {
                        }
                        if (inputStream != null) {
                        }
                        if (httpURLConnection != null) {
                        }
                        throw th;
                    }
                    try {
                        byte[] bArr = new byte[4096];
                        long j = 0;
                        while (true) {
                            int read = inputStream.read(bArr);
                            if (read == -1) {
                                try {
                                    fileOutputStream.close();
                                    if (inputStream != null) {
                                        inputStream.close();
                                    }
                                } catch (IOException unused) {
                                }
                                if (httpURLConnection != null) {
                                    httpURLConnection.disconnect();
                                }
                                return this.pathFile;
                            } else if (isCancelled()) {
                                try {
                                    break;
                                } catch (Exception e3) {
                                    e = e3;
                                    fileOutputStream2 = fileOutputStream;
                                    String exc2 = e.toString();
                                    if (fileOutputStream2 != null) {
                                    }
                                    if (inputStream != null) {
                                    }
                                    if (httpURLConnection != null) {
                                    }
                                    return exc2;
                                } catch (Throwable th4) {
                                    th = th4;
                                    fileOutputStream2 = fileOutputStream;
                                    if (fileOutputStream2 != null) {
                                    }
                                    if (inputStream != null) {
                                    }
                                    if (httpURLConnection != null) {
                                    }
                                    throw th;
                                }
                            } else {
                                j += (long) read;
                                if (contentLength > 0) {
                                    Integer[] numArr = new Integer[i];
                                    inputStream2 = inputStream;
                                    try {
                                        numArr[0] = Integer.valueOf((int) ((100 * j) / ((long) contentLength)));
                                        publishProgress(numArr);
                                    } catch (Exception e4) {
                                        e = e4;
                                        inputStream = inputStream2;
                                        fileOutputStream2 = fileOutputStream;
                                        String exc22 = e.toString();
                                        if (fileOutputStream2 != null) {
                                        }
                                        if (inputStream != null) {
                                        }
                                        if (httpURLConnection != null) {
                                        }
                                        return exc22;
                                    } catch (Throwable th5) {
                                        th = th5;
                                        inputStream = inputStream2;
                                        fileOutputStream2 = fileOutputStream;
                                        if (fileOutputStream2 != null) {
                                        }
                                        if (inputStream != null) {
                                        }
                                        if (httpURLConnection != null) {
                                        }
                                        throw th;
                                    }
                                } else {
                                    inputStream2 = inputStream;
                                }
                                fileOutputStream.write(bArr, 0, read);
                                inputStream = inputStream2;
                                i = 1;
                            }
                        }
                    } catch (Exception e5) {
                        e = e5;
                        fileOutputStream2 = fileOutputStream;
                        String exc222 = e.toString();
                        if (fileOutputStream2 != null) {
                            try {
                                fileOutputStream2.close();
                            } catch (IOException unused2) {
                                if (httpURLConnection != null) {
                                }
                                return exc222;
                            }
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        return exc222;
                    } catch (Throwable th6) {
                        th = th6;
                        fileOutputStream2 = fileOutputStream;
                        if (fileOutputStream2 != null) {
                            try {
                                fileOutputStream2.close();
                            } catch (IOException unused3) {
                                if (httpURLConnection != null) {
                                }
                                throw th;
                            }
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        throw th;
                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    return null;
                } catch (Exception e6) {
                    e = e6;
                    inputStream = null;
                    String exc2222 = e.toString();
                    if (fileOutputStream2 != null) {
                    }
                    if (inputStream != null) {
                    }
                    if (httpURLConnection != null) {
                    }
                    return exc2222;
                } catch (Throwable th7) {
                    th = th7;
                    inputStream = null;
                    if (fileOutputStream2 != null) {
                    }
                    if (inputStream != null) {
                    }
                    if (httpURLConnection != null) {
                    }
                    throw th;
                }
            } catch (Exception e7) {
                e = e7;
                httpURLConnection = null;
                inputStream = null;
                String exc22222 = e.toString();
                if (fileOutputStream2 != null) {
                }
                if (inputStream != null) {
                }
                if (httpURLConnection != null) {
                }
                return exc22222;
            } catch (Throwable th8) {
                th = th8;
                httpURLConnection = null;
                inputStream = null;
                if (fileOutputStream2 != null) {
                }
                if (inputStream != null) {
                }
                if (httpURLConnection != null) {
                }
                try {
                    throw th;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
            return str;
        }


        public void onPreExecute() {
            super.onPreExecute();
            ProgressDialog progressDialog = new ProgressDialog(this.context);
            this.pd = progressDialog;
            progressDialog.setTitle("Processing...");
            this.pd.setMessage("Please wait.");
            this.pd.setMax(100);
            this.pd.setProgressStyle(1);
            this.pd.setCancelable(false);
            this.pd.show();
        }


        public void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate(numArr);
            this.pd.setProgress(Integer.parseInt(String.valueOf(numArr[0])));
        }


        public void onPostExecute(String str) {
            ProgressDialog progressDialog = this.pd;
            if (progressDialog != null) {
                progressDialog.dismiss();
                if (str != null) {
                    MediaScannerConnection.scanFile(this.context, new String[]{str}, null, null);
                }
                Toast.makeText(this.context, "Video Downloaded" + str, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static ArrayList<String> getasset_folder_data(Context context, String str) {
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

    public static void share_app(Context context) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + context.getPackageName());
        context.startActivity(Intent.createChooser(intent, "Share via"));
    }

    public static void rate_app(Context context) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (ActivityNotFoundException unused) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }
}
