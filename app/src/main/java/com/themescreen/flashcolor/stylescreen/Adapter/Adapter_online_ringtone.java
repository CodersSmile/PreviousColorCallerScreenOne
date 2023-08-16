package com.themescreen.flashcolor.stylescreen.Adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.themescreen.flashcolor.stylescreen.Model.OnlineRingtonModel;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.themescreen.flashcolor.stylescreen.utils.Utils_Stroage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Adapter_online_ringtone extends RecyclerView.Adapter<Adapter_online_ringtone.ringtoneholder> {
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    String TAG = "ONLINE_RINGTONE_ADAPTER";
    Activity activity;
    Context context;
    String folderpath;
    String namefordownlaod;
    ArrayList<OnlineRingtonModel> onlineRingtonModels;
    int pos = -1;
    String tempname;

    public Adapter_online_ringtone(Context context2, ArrayList<OnlineRingtonModel> arrayList, Activity activity2) {
        this.context = context2;
        this.onlineRingtonModels = arrayList;
        this.activity = activity2;
    }


    public ringtoneholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ringtoneholder(LayoutInflater.from(this.context).inflate(R.layout.raw_online_ringtone, viewGroup, false));
    }

    public void onBindViewHolder(final ringtoneholder ringtoneholder2, final int i) {
        String urlofringtone = this.onlineRingtonModels.get(i).getUrlofringtone();
        this.tempname = urlofringtone.substring(urlofringtone.lastIndexOf("/") + 1, urlofringtone.length());
        ringtoneholder2.ringtone_name.setText("Ringtone_" + this.tempname);
        this.folderpath = Utils_Stroage.create_folder_with_sub_folder(this.context.getResources().getString(R.string.app_name), "ringtone", Environment.DIRECTORY_MUSIC);
        if (new File(this.folderpath + "/" + this.tempname).exists()) {
            ringtoneholder2.img_download.setImageResource(R.drawable.music_done);
        } else {
            ringtoneholder2.img_download.setImageResource(R.drawable.download_ringtone_unpress);
        }
        ringtoneholder2.img_play_pause.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String urlofringtone = Adapter_online_ringtone.this.onlineRingtonModels.get(i).getUrlofringtone();
                Adapter_online_ringtone.this.namefordownlaod = urlofringtone.substring(urlofringtone.lastIndexOf("/") + 1, urlofringtone.length());
                if (!new File(Adapter_online_ringtone.this.folderpath + "/" + Adapter_online_ringtone.this.namefordownlaod).exists()) {
                    if (Adapter_online_ringtone.mediaPlayer.isPlaying()) {
                        Adapter_online_ringtone.mediaPlayer.stop();
                    }
                    new DownloadTask(ringtoneholder2.downloading_progress, ringtoneholder2.img_download, ringtoneholder2.progress_text).execute(Adapter_online_ringtone.this.onlineRingtonModels.get(i).getUrlofringtone());
                    return;
                }
                Adapter_online_ringtone.this.pos = i;
                Adapter_online_ringtone.this.notifyDataSetChanged();
            }
        });
        if (i == this.pos) {
            Boolean bool = false;
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                ringtoneholder2.img_play_pause.setImageResource(R.drawable.play_unpress);
            } else if (bool.booleanValue()) {
                mediaPlayer.start();
                ringtoneholder2.img_play_pause.setImageResource(R.drawable.pause_press);
            } else {
                String urlofringtone2 = this.onlineRingtonModels.get(i).getUrlofringtone();
                this.namefordownlaod = urlofringtone2.substring(urlofringtone2.lastIndexOf("/") + 1, urlofringtone2.length());
                playmedia(new File(this.folderpath + "/" + this.namefordownlaod).getAbsolutePath());
                ringtoneholder2.img_play_pause.setImageResource(R.drawable.pause_press);
            }
        } else {
            ringtoneholder2.img_play_pause.setImageResource(R.drawable.play_unpress);
        }
        ringtoneholder2.relative_ringtone.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String urlofringtone = Adapter_online_ringtone.this.onlineRingtonModels.get(i).getUrlofringtone();
                Adapter_online_ringtone.this.namefordownlaod = urlofringtone.substring(urlofringtone.lastIndexOf("/") + 1, urlofringtone.length());
                File file = new File(Adapter_online_ringtone.this.folderpath + "/" + Adapter_online_ringtone.this.namefordownlaod);
                if (!file.exists()) {
                    new DownloadTask(ringtoneholder2.downloading_progress, ringtoneholder2.img_download, ringtoneholder2.progress_text).execute(Adapter_online_ringtone.this.onlineRingtonModels.get(i).getUrlofringtone());
                    return;
                }
                Adapter_online_ringtone.mediaPlayer.stop();
                Adapter_online_ringtone.this.SetAsRingtoneAndroid(file);
            }
        });
    }

    public void setasringtone(File file) {
        int i = Build.VERSION.SDK_INT;
        Integer valueOf = Integer.valueOf((int) R.string.app_name);
        if (i >= 29) {
            Log.d(this.TAG, "onClick: " + file);
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", "ringtone");
            contentValues.put("mime_type", "audio/mp3");
            contentValues.put("_size", Long.valueOf(file.length()));
            contentValues.put("artist", valueOf);
            contentValues.put("is_ringtone", (Boolean) true);
            contentValues.put("is_notification", (Boolean) true);
            contentValues.put("is_alarm", (Boolean) true);
            contentValues.put("is_music", (Boolean) false);
            Uri insert = this.context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);
            Log.d(this.TAG, "onClick: " + insert);
            OutputStream openOutputStream = null;
            try {
                openOutputStream = this.context.getContentResolver().openOutputStream(insert);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            int length = (int) file.length();
            byte[] bArr = new byte[length];
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                bufferedInputStream.read(bArr, 0, length);
                bufferedInputStream.close();
                openOutputStream.write(bArr);
                openOutputStream.close();
                openOutputStream.flush();
            } catch (IOException unused) {
            }
            if (openOutputStream != null) {
                try {
                    openOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                RingtoneManager.setActualDefaultRingtoneUri(this.context, 1, insert);
                Toast.makeText(this.context, "Ringtone Set", Toast.LENGTH_SHORT).show();
                this.activity.finish();
            } catch (Throwable unused2) {
            }
        } else {
            Log.d(this.TAG, "onClick:1 " + file.getAbsolutePath());
            ContentResolver contentResolver = this.context.getContentResolver();
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("_data", file.getAbsolutePath());
            contentValues2.put("title", "ringtone");
            contentValues2.put("mime_type", "audio/*");
            contentValues2.put("_size", Long.valueOf(file.length()));
            contentValues2.put("artist", valueOf);
            contentValues2.put("is_ringtone", (Boolean) true);
            contentValues2.put("is_notification", (Boolean) true);
            contentValues2.put("is_alarm", (Boolean) true);
            contentValues2.put("is_music", (Boolean) false);
            Uri contentUriForPath = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
            Log.d(this.TAG, "onClick: 3" + contentUriForPath);
            contentResolver.delete(contentUriForPath, "_data=\"" + file.getAbsolutePath() + "\"", null);
            Uri insert2 = contentResolver.insert(contentUriForPath, contentValues2);
            Log.d(this.TAG, "onClick:2 " + insert2);
            RingtoneManager.setActualDefaultRingtoneUri(this.context, 1, insert2);
            Toast.makeText(this.context, "Ringtone Set", Toast.LENGTH_SHORT).show();
            this.activity.finish();
        }
    }

    public int getItemCount() {
        return this.onlineRingtonModels.size();
    }

    public class ringtoneholder extends RecyclerView.ViewHolder {
        LinearLayout divider;
        ProgressBar downloading_progress;
        ImageView img_download;
        ImageView img_play_pause;
        TextView progress_text;
        RelativeLayout relative_ringtone;
        RelativeLayout relative_top;
        TextView ringtone_name;

        public ringtoneholder(View view) {
            super(view);
            this.ringtone_name = (TextView) view.findViewById(R.id.ringtone_name);
            this.downloading_progress = (ProgressBar) view.findViewById(R.id.downloading_progress);
            this.img_download = (ImageView) view.findViewById(R.id.img_download);
            this.relative_ringtone = (RelativeLayout) view.findViewById(R.id.relative_ringtone);
            this.progress_text = (TextView) view.findViewById(R.id.progress_text);
            this.img_play_pause = (ImageView) view.findViewById(R.id.img_play_pause);
            this.relative_top = (RelativeLayout) view.findViewById(R.id.relative_top);
            Help.getheightandwidth(Adapter_online_ringtone.this.context);
            Help.setHeight(1080);
            Help.setSize(this.img_play_pause, 100, 100, true);
            Help.setSize(this.downloading_progress, 100, 100, true);
            Help.setSize(this.img_download, 80, 80, true);
            Help.setSize(this.relative_top, 983, 158, true);
        }
    }

    class DownloadTask extends AsyncTask<String, Integer, String> {
        ProgressBar downloading_progress;
        ImageView img_download;
        TextView progress_text;

        public DownloadTask(ProgressBar progressBar, ImageView imageView, TextView textView) {
            this.downloading_progress = progressBar;
            this.img_download = imageView;
            this.progress_text = textView;
        }

        public String doInBackground(String... strArr) {
            InputStream inputStream;
            HttpURLConnection httpURLConnection;
            FileOutputStream fileOutputStream;
            Throwable th;
            Exception e;
            String str = null;
            try {
                httpURLConnection = (HttpURLConnection) new URL(strArr[0]).openConnection();
                try {
                    httpURLConnection.connect();
                    Log.d(Adapter_online_ringtone.this.TAG, "doInBackground: connectioncode " + httpURLConnection.getResponseCode());
                    if (httpURLConnection.getResponseCode() != 200) {
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        return "Please Enter Valid Name";
                    }
                    int contentLength = httpURLConnection.getContentLength();
                    inputStream = httpURLConnection.getInputStream();
                    try {
                        String create_folder_with_sub_folder = Utils_Stroage.create_folder_with_sub_folder(Adapter_online_ringtone.this.context.getResources().getString(R.string.app_name), "ringtone", Environment.DIRECTORY_MUSIC);
                        FileOutputStream fileOutputStream2 = new FileOutputStream(create_folder_with_sub_folder + File.separator + Adapter_online_ringtone.this.namefordownlaod);
                        try {
                            byte[] bArr = new byte[4096];
                            long j = 0;
                            while (true) {
                                int read = inputStream.read(bArr);
                                if (read == -1) {
                                    try {
                                        fileOutputStream2.close();
                                        if (inputStream != null) {
                                            inputStream.close();
                                        }
                                        if (httpURLConnection != null) {
                                            httpURLConnection.disconnect();
                                        }
                                        return create_folder_with_sub_folder;
                                    } catch (IOException e2) {
                                        return e2.toString();
                                    }
                                } else if (isCancelled()) {
                                    inputStream.close();
                                    try {
                                        fileOutputStream2.close();
                                        if (inputStream != null) {
                                            inputStream.close();
                                        }
                                        if (httpURLConnection != null) {
                                            httpURLConnection.disconnect();
                                        }
                                        return str;
                                    } catch (IOException e3) {
                                        return e3.toString();
                                    }
                                } else {
                                    j += (long) read;
                                    if (contentLength > 0) {
                                        publishProgress(Integer.valueOf((int) ((100 * j) / ((long) contentLength))));
                                    }
                                    fileOutputStream2.write(bArr, 0, read);
                                    str = null;
                                }
                            }
                        } catch (Exception e4) {
                            e = e4;
                            fileOutputStream = fileOutputStream2;
                            try {
                                String exc = e.toString();
                                if (fileOutputStream != null) {
                                }
                                if (inputStream != null) {
                                }
                                if (httpURLConnection != null) {
                                }
                                return exc;
                            } catch (Throwable th2) {
                                th = th2;
                                if (fileOutputStream != null) {
                                }
                                if (inputStream != null) {
                                }
                                if (httpURLConnection != null) {
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            fileOutputStream = fileOutputStream2;
                            if (fileOutputStream != null) {
                            }
                            if (inputStream != null) {
                            }
                            if (httpURLConnection != null) {
                            }
                            throw th;
                        }
                    } catch (Exception e5) {
                        e = e5;
                        fileOutputStream = null;
                        String exc2 = e.toString();
                        if (fileOutputStream != null) {
                        }
                        if (inputStream != null) {
                        }
                        if (httpURLConnection != null) {
                        }
                        return exc2;
                    } catch (Throwable th4) {
                        th = th4;
                        fileOutputStream = null;
                        if (fileOutputStream != null) {
                        }
                        if (inputStream != null) {
                        }
                        if (httpURLConnection != null) {
                        }
                        throw th;
                    }
                } catch (Exception e6) {
                    e = e6;
                    fileOutputStream = null;
                    inputStream = null;
                    String exc22 = e.toString();
                    if (fileOutputStream != null) {
                    }
                    if (inputStream != null) {
                    }
                    if (httpURLConnection != null) {
                    }
                    return exc22;
                } catch (Throwable th5) {
                    th = th5;
                    fileOutputStream = null;
                    inputStream = null;
                    if (fileOutputStream != null) {
                    }
                    if (inputStream != null) {
                    }
                    if (httpURLConnection != null) {
                    }
                    throw th;
                }
            } catch (Exception e7) {
                e = e7;
                fileOutputStream = null;
                httpURLConnection = null;
                inputStream = null;
                String exc222 = e.toString();
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e8) {
                        return e8.toString();
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                return exc222;
            } catch (Throwable th6) {
                th = th6;
                fileOutputStream = null;
                httpURLConnection = null;
                inputStream = null;
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e9) {
                        return e9.toString();
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
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
            this.downloading_progress.setVisibility(View.VISIBLE);
            this.progress_text.setVisibility(View.VISIBLE);
            this.img_download.setVisibility(View.GONE);
        }


        public void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate(numArr);
            this.downloading_progress.setMax(100);
            this.downloading_progress.setProgress(numArr[0].intValue());
            this.progress_text.setText("" + numArr[0] + "%");
        }


        public void onPostExecute(String str) {
            this.downloading_progress.setVisibility(View.GONE);
            this.img_download.setVisibility(View.VISIBLE);
            this.progress_text.setVisibility(View.GONE);
            if (str != null) {
                File file = new File(str + File.separator + Adapter_online_ringtone.this.namefordownlaod);
                if (file.exists()) {
                    MediaScannerConnection.scanFile(Adapter_online_ringtone.this.context, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {

                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                }
            }
            Adapter_online_ringtone.this.notifyDataSetChanged();
        }
    }

    public void playmedia(String str) {
        try {
            File file = new File(str);
            if (file.exists()) {
                Log.d("audiopath", "" + file);
                mediaPlayer.reset();
                mediaPlayer.setDataSource(this.context, Uri.fromFile(file));
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("audiopath", "" + e);
        }
    }

    private void SetAsRingtoneAndroid(File file) {
        int i = Build.VERSION.SDK_INT;
        Integer valueOf = Integer.valueOf((int) R.string.app_name);
        if (i >= 29) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", file.getName());
            contentValues.put("mime_type", getMIMEType(file.getAbsolutePath()));
            contentValues.put("_size", Long.valueOf(file.length()));
            contentValues.put("artist", valueOf);
            contentValues.put("is_ringtone", (Boolean) true);
            Uri insert = this.context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                OutputStream openOutputStream = this.context.getContentResolver().openOutputStream(insert);
                int length = (int) file.length();
                byte[] bArr = new byte[length];
                try {
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                    bufferedInputStream.read(bArr, 0, length);
                    bufferedInputStream.close();
                    openOutputStream.write(bArr);
                    openOutputStream.close();
                    openOutputStream.flush();
                } catch (IOException unused) {
                }
                if (openOutputStream != null) {
                    openOutputStream.close();
                }
            } catch (Exception unused2) {
            }
            RingtoneManager.setActualDefaultRingtoneUri(this.context.getApplicationContext(), 1, insert);
            Toast.makeText(this.context, "Ringtone set", Toast.LENGTH_SHORT).show();
            this.activity.finish();
            return;
        }
        Log.d(this.TAG, "onClick:1 " + file.getAbsolutePath());
        ContentResolver contentResolver = this.context.getContentResolver();
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("_data", file.getAbsolutePath());
        contentValues2.put("title", file.getName());
        contentValues2.put("mime_type", "audio/*");
        contentValues2.put("_size", Long.valueOf(file.length()));
        contentValues2.put("artist", valueOf);
        contentValues2.put("is_ringtone", (Boolean) true);
        contentValues2.put("is_notification", (Boolean) true);
        contentValues2.put("is_alarm", (Boolean) true);
        contentValues2.put("is_music", (Boolean) false);
        Uri contentUriForPath = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
        Log.d(this.TAG, "onClick: 3" + contentUriForPath);
        contentResolver.delete(contentUriForPath, "_data=\"" + file.getAbsolutePath() + "\"", null);
        Uri insert2 = contentResolver.insert(contentUriForPath, contentValues2);
        Log.d(this.TAG, "onClick:2 " + insert2);
        try {
            RingtoneManager.setActualDefaultRingtoneUri(this.context, 1, insert2);
            Toast.makeText(this.context, "Ringtone Set", Toast.LENGTH_SHORT).show();
            this.activity.finish();
        } catch (Throwable th) {
            Toast.makeText(this.context, "Ringtone NotSet" + th, Toast.LENGTH_SHORT).show();
        }
    }

    public static String getMIMEType(String str) {
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
        if (fileExtensionFromUrl != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl);
        }
        return null;
    }
}
