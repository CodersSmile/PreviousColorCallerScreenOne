package com.themescreen.flashcolor.stylescreen.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.themescreen.flashcolor.stylescreen.Activity.Theme_Activity_Calling_Theme_Preview;
import com.themescreen.flashcolor.stylescreen.Model.OnlineThemeModel;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.themescreen.flashcolor.stylescreen.utils.Utils_Stroage;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Adapter_online_theme extends RecyclerView.Adapter<Adapter_online_theme.Myvideoholder> {
    public static final String MyPREFERENCES = "myprefs";
    public static String ansvalue = "ansicon";
    static DownloadTask downloadTask = null;
    static int fileLength = 0;
    public static String rejvalue = "reicon";
    public static String theampathhh;
    String TAG = "ONLINE_THEAME_ADAPTER";
    Context context;
    Dialog downloadingdailog;
    String folderpath;
    InputStream input = null;
    LinearLayout liner_downloading_dailog;
    String nameofsong;
    ArrayList<OnlineThemeModel> onlineTheamModels;
    OutputStream output = null;
    SharedPreferences sharedpreferences;
    private InterstitialAd mInterstitialAd;

    public Adapter_online_theme(ArrayList<OnlineThemeModel> arrayList, Context context2) {
        this.onlineTheamModels = arrayList;
        this.context = context2;
    }

    public Myvideoholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Myvideoholder(LayoutInflater.from(this.context).inflate(R.layout.raw_online_theme, viewGroup, false));
    }

    public void onBindViewHolder(Myvideoholder myvideoholder, @SuppressLint("RecyclerView") final int i) {

        SharedPreferences sharedPreferences = this.context.getSharedPreferences("myprefs", 0);
        this.sharedpreferences = sharedPreferences;
        int i2 = sharedPreferences.getInt(ansvalue, R.drawable.accept_button_three);
        int i3 = this.sharedpreferences.getInt(rejvalue, R.drawable.decline_button_three);
        Log.e(this.TAG, "onCreate:theam" + i2);
        Log.e(this.TAG, "onCreate:theam" + i3);
        myvideoholder.img_rej.setImageResource(i3);
        myvideoholder.img_recive.setImageResource(i2);
        if (i == 0) {
            Glide.with(this.context).load(this.onlineTheamModels.get(i).getNameurl()).into(myvideoholder.raw_online_theam_videoview);
        } else {
            String nameurl = this.onlineTheamModels.get(i).getNameurl();
            int lastIndexOf = nameurl.lastIndexOf("/");
            ((RequestBuilder) ((RequestBuilder) Glide.with(this.context).load(nameurl.substring(0, lastIndexOf) + "/thumbs/" + nameurl.substring(lastIndexOf, nameurl.length()).replace(".mp4", ".webp")).diskCacheStrategy(DiskCacheStrategy.DATA)).skipMemoryCache(false)).into(myvideoholder.raw_online_theam_videoview);
        }
        this.folderpath = Utils_Stroage.create_folder_in_app_package_dir(this.context, "theams");
        String nameurl2 = this.onlineTheamModels.get(i).getNameurl();
        String substring = nameurl2.substring(nameurl2.lastIndexOf("/"), nameurl2.lastIndexOf("."));
        this.nameofsong = substring.substring(substring.indexOf(".") + 1, substring.length());
        if (!new File(this.folderpath + "/" + this.nameofsong + ".mp4").exists()) {
            myvideoholder.img_download.setImageResource(R.drawable.download_press);
        } else {
            myvideoholder.img_download.setImageResource(R.drawable.done_btn);
        }
        myvideoholder.raw_online_theam_videoview.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String nameurl = Adapter_online_theme.this.onlineTheamModels.get(i).getNameurl();
                String substring = nameurl.substring(nameurl.lastIndexOf("/"), nameurl.lastIndexOf("."));
                Adapter_online_theme.this.nameofsong = substring.substring(substring.indexOf(".") + 1, substring.length());
                if (!new File(Adapter_online_theme.this.folderpath + "/" + Adapter_online_theme.this.nameofsong + ".mp4").exists()) {
                    Adapter_online_theme adapter_online_theme = Adapter_online_theme.this;
                    if (adapter_online_theme.connectivity(adapter_online_theme.context)) {
                        Adapter_online_theme.downloadTask = new DownloadTask();
                        Adapter_online_theme.downloadTask.execute(Adapter_online_theme.this.onlineTheamModels.get(i).getNameurl());
                        return;
                    }
                    return;
                }
                String nameurl2 = Adapter_online_theme.this.onlineTheamModels.get(i).getNameurl();
                String substring2 = nameurl2.substring(nameurl2.lastIndexOf("/") + 1, nameurl2.lastIndexOf("."));
                Intent intent = new Intent(Adapter_online_theme.this.context, Theme_Activity_Calling_Theme_Preview.class);
                intent.putExtra("theampath", substring2);
                Adapter_online_theme.this.context.startActivity(intent);

            }
        });
    }

    
    public int getItemCount() {
        return this.onlineTheamModels.size();
    }

    public class Myvideoholder extends RecyclerView.ViewHolder {
        ImageView avtar_text;
        ImageView img_download;
        ImageView img_recive;
        ImageView img_rej;
        ImageView img_user;
        ImageView img_user_pic_border;
        ImageView raw_online_theam_videoview;
        RelativeLayout relative_main;

        public Myvideoholder(View view) {
            super(view);
            this.raw_online_theam_videoview = (ImageView) view.findViewById(R.id.raw_online_theam_videoview);
            this.img_recive = (ImageView) view.findViewById(R.id.img_recive);
            this.img_rej = (ImageView) view.findViewById(R.id.img_rej);
            this.relative_main = (RelativeLayout) view.findViewById(R.id.relative_main);
            this.img_user_pic_border = (ImageView) view.findViewById(R.id.img_user_pic_border);
            this.img_user = (ImageView) view.findViewById(R.id.img_user);
            this.avtar_text = (ImageView) view.findViewById(R.id.avtar_text);
            this.img_download = (ImageView) view.findViewById(R.id.img_download);
            Help.getheightandwidth(Adapter_online_theme.this.context);
            Help.setHeight(1080);
            Help.setSize(this.relative_main, 550, 830, true);
            Help.setSize(this.img_user_pic_border, 170, 170, true);
            Help.setSize(this.img_user, 170, 170, true);
            Help.setSize(this.img_recive, 140, 140, true);
            Help.setSize(this.img_rej, 140, 140, true);
            Help.setSize(this.avtar_text, 314, 88, true);
            Help.setSize(this.img_download, 80, 80, true);
        }
    }

    public class DownloadTask extends AsyncTask<String, Integer, String> {
        public DownloadTask() {
        }

        public String doInBackground(String... strArr) {
            Throwable th;
            Exception e;
            HttpURLConnection httpURLConnection = null;
            try {
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(strArr[0]).openConnection();
                try {
                    httpURLConnection2.connect();
                    Log.d(Adapter_online_theme.this.TAG, "doInBackground: connectioncode " + httpURLConnection2.getResponseCode());
                    if (httpURLConnection2.getResponseCode() != 200) {
                        try {
                            if (Adapter_online_theme.this.output != null) {
                                Adapter_online_theme.this.output.close();
                            }
                            if (Adapter_online_theme.this.input != null) {
                                Adapter_online_theme.this.input.close();
                            }
                        } catch (IOException unused) {
                        }
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        return "Please Enter Valid Name";
                    }
                    Adapter_online_theme.fileLength = httpURLConnection2.getContentLength();
                    Adapter_online_theme.this.input = httpURLConnection2.getInputStream();
                    String create_folder_in_app_package_dir = Utils_Stroage.create_folder_in_app_package_dir(Adapter_online_theme.this.context, "theams");
                    Adapter_online_theme.theampathhh = create_folder_in_app_package_dir + "/" + Adapter_online_theme.this.nameofsong + ".mp4";
                    Adapter_online_theme.this.output = new FileOutputStream(create_folder_in_app_package_dir + "/" + Adapter_online_theme.this.nameofsong + ".mp4");
                    byte[] bArr = new byte[4096];
                    long j = 0;
                    while (true) {
                        int read = Adapter_online_theme.this.input.read(bArr);
                        if (read == -1) {
                            try {
                                if (Adapter_online_theme.this.output != null) {
                                    Adapter_online_theme.this.output.close();
                                }
                                if (Adapter_online_theme.this.input != null) {
                                    Adapter_online_theme.this.input.close();
                                }
                            } catch (IOException unused2) {
                            }
                            if (httpURLConnection2 != null) {
                                httpURLConnection2.disconnect();
                            }
                            return null;
                        } else if (isCancelled()) {
                            Adapter_online_theme.this.input.close();
                            try {
                                if (Adapter_online_theme.this.output != null) {
                                    Adapter_online_theme.this.output.close();
                                }
                                if (Adapter_online_theme.this.input != null) {
                                    Adapter_online_theme.this.input.close();
                                }
                            } catch (IOException unused3) {
                            }
                            if (httpURLConnection2 != null) {
                                httpURLConnection2.disconnect();
                            }
                            return null;
                        } else {
                            j += (long) read;
                            if (Adapter_online_theme.fileLength > 0) {
                                publishProgress(Integer.valueOf((int) ((100 * j) / ((long) Adapter_online_theme.fileLength))));
                            }
                            Adapter_online_theme.this.output.write(bArr, 0, read);
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    httpURLConnection = httpURLConnection2;
                    try {
                        String exc = e.toString();
                        if (Adapter_online_theme.this.output != null) {
                        }
                        if (Adapter_online_theme.this.input != null) {
                        }
                        if (httpURLConnection != null) {
                        }
                        return exc;
                    } catch (Throwable th2) {
                        th = th2;
                        try {
                            if (Adapter_online_theme.this.output != null) {
                                Adapter_online_theme.this.output.close();
                            }
                            if (Adapter_online_theme.this.input != null) {
                                Adapter_online_theme.this.input.close();
                            }
                        } catch (IOException unused5) {
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
                } catch (Throwable th3) {
                    th = th3;
                    httpURLConnection = httpURLConnection2;
                    if (Adapter_online_theme.this.output != null) {
                    }
                    if (Adapter_online_theme.this.input != null) {
                    }
                    if (httpURLConnection != null) {
                    }
                    try {
                        throw th;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            } catch (Exception e3) {
                e = e3;
                String exc2 = e.toString();
                if (Adapter_online_theme.this.output != null) {
                    try {
                        Adapter_online_theme.this.output.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                if (Adapter_online_theme.this.input != null) {
                    try {
                        Adapter_online_theme.this.input.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                return exc2;
            }
            return null;
        }

        
        public void onCancelled() {
            super.onCancelled();
            Adapter_online_theme.this.downloadingdailog.dismiss();
        }

        
        public void onPreExecute() {
            super.onPreExecute();
            Adapter_online_theme.this.downloadingdailog = new Dialog(Adapter_online_theme.this.context);
            Adapter_online_theme.this.downloadingdailog.getWindow().requestFeature(1);
            Adapter_online_theme.this.downloadingdailog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            Adapter_online_theme.this.downloadingdailog.setContentView(R.layout.custom_downloading_dailog);
            Adapter_online_theme.this.downloadingdailog.setCancelable(false);
            Adapter_online_theme adapter_online_theme = Adapter_online_theme.this;
            adapter_online_theme.liner_downloading_dailog = (LinearLayout) adapter_online_theme.downloadingdailog.findViewById(R.id.liner_downloading_dailog);
            Help.getheightandwidth(Adapter_online_theme.this.context);
            Help.setHeight(1080);
            Help.setSize(Adapter_online_theme.this.liner_downloading_dailog, 650, 350, true);
            Adapter_online_theme.this.downloadingdailog.show();
        }

        
        public void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate(numArr);
        }

        
        public void onPostExecute(String str) {
            Adapter_online_theme.this.downloadingdailog.dismiss();
            Adapter_online_theme.this.notifyDataSetChanged();
        }
    }

    public boolean connectivity(Context context2) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context2.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT < 29) {
                try {
                    if (connectivityManager.getActiveNetworkInfo() != null) {
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    Log.i("update_statut", "" + e.getMessage());
                }
            } else if (connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork()) != null) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static void canceltask() {
        DownloadTask downloadTask2 = downloadTask;
        if (downloadTask2 != null) {
            downloadTask2.cancel(true);
            String str = theampathhh;
            if (str != null && !str.isEmpty()) {
                File file = new File(theampathhh);
                if (file.exists() && file.length() != ((long) fileLength)) {
                    file.delete();
                }
            }
        }
    }


}
