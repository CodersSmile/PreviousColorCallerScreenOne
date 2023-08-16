package com.themescreen.flashcolor.stylescreen.custom_dailor.Adapter;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.custom_dailor.utils.SP_Helper;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.themescreen.flashcolor.stylescreen.utils.Utils_Stroage;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Default_BG_Adapter extends RecyclerView.Adapter<Default_BG_Adapter.MyViewHolder> {
    static DownloadTask downloadTask;
    static int fileLength;
    static Context mContext;
    public static String theampathhh;
    Dialog downloadingdailog;
    String folderpath;
    InputStream input = null;
    LinearLayout liner_downloading_dailog;
    String nameofsong;
    OutputStream output = null;
    ArrayList<String> paths;

    public Default_BG_Adapter(Context context, ArrayList<String> arrayList) {
        mContext = context;
        this.paths = arrayList;
    }


    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shape_adapter, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        ((RequestBuilder) Glide.with(mContext).load(this.paths.get(i)).override(500, 500)).into(myViewHolder.img);
        this.folderpath = Utils_Stroage.create_folder_in_app_package_dir(mContext, "Default Background");
        String str = this.paths.get(i);
        String substring = str.substring(str.lastIndexOf("/"), str.lastIndexOf("."));
        this.nameofsong = substring.substring(substring.indexOf(".") + 1, substring.length());
        if (!new File(this.folderpath + "/" + this.nameofsong + ".jpg").exists()) {
            myViewHolder.img_download.setImageResource(R.drawable.download_press);
            myViewHolder.img_download.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.img_download.setImageResource(R.drawable.done_btn);
            myViewHolder.img_download.setVisibility(View.GONE);
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String str = Default_BG_Adapter.this.paths.get(i);
                String substring = str.substring(str.lastIndexOf("/"), str.lastIndexOf("."));
                Default_BG_Adapter.this.nameofsong = substring.substring(substring.indexOf(".") + 1, substring.length());
                if (new File(Default_BG_Adapter.this.folderpath + "/" + Default_BG_Adapter.this.nameofsong + ".jpg").exists()) {
                    String str2 = Default_BG_Adapter.this.paths.get(i);
                    Log.e("TAG", "onClick: " + str2.substring(str2.lastIndexOf("/") + 1, str2.lastIndexOf(".")));
                    SP_Helper.put_value_in_sharedpreference(Default_BG_Adapter.mContext, "imageurl", str2);
                    Toast.makeText(Default_BG_Adapter.mContext, "Change Dailor Image", Toast.LENGTH_SHORT).show();
                } else if (Default_BG_Adapter.this.connectivity(Default_BG_Adapter.mContext)) {
                    Default_BG_Adapter.downloadTask = new DownloadTask();
                    Default_BG_Adapter.downloadTask.execute(Default_BG_Adapter.this.paths.get(i));
                }
            }
        });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView img;
        public ImageView img_download;

        public MyViewHolder(View view) {
            super(view);
            this.img = (RoundedImageView) view.findViewById(R.id.img);
            this.img_download = (ImageView) view.findViewById(R.id.img_download);
            Help.setSize(this.img, 480, 610, true);
            Help.setSize(this.img_download, 80, 80, true);
        }
    }


    public int getItemCount() {
        return this.paths.size();
    }

    public boolean connectivity(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
                    Log.d("TAG", "doInBackground: connectioncode " + httpURLConnection2.getResponseCode());
                    if (httpURLConnection2.getResponseCode() != 200) {
                        try {
                            if (Default_BG_Adapter.this.output != null) {
                                Default_BG_Adapter.this.output.close();
                            }
                            if (Default_BG_Adapter.this.input != null) {
                                Default_BG_Adapter.this.input.close();
                            }
                        } catch (IOException unused) {
                        }
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        return "Please Enter Valid Name";
                    }
                    Default_BG_Adapter.fileLength = httpURLConnection2.getContentLength();
                    Default_BG_Adapter.this.input = httpURLConnection2.getInputStream();
                    String create_folder_in_app_package_dir = Utils_Stroage.create_folder_in_app_package_dir(Default_BG_Adapter.mContext, "Default Background");
                    Default_BG_Adapter.theampathhh = create_folder_in_app_package_dir + "/" + Default_BG_Adapter.this.nameofsong + ".jpg";
                    Default_BG_Adapter.this.output = new FileOutputStream(create_folder_in_app_package_dir + "/" + Default_BG_Adapter.this.nameofsong + ".jpg");
                    byte[] bArr = new byte[4096];
                    long j = 0;
                    while (true) {
                        int read = Default_BG_Adapter.this.input.read(bArr);
                        if (read == -1) {
                            try {
                                if (Default_BG_Adapter.this.output != null) {
                                    Default_BG_Adapter.this.output.close();
                                }
                                if (Default_BG_Adapter.this.input != null) {
                                    Default_BG_Adapter.this.input.close();
                                }
                            } catch (IOException unused2) {
                            }
                            if (httpURLConnection2 != null) {
                                httpURLConnection2.disconnect();
                            }
                            return null;
                        } else if (isCancelled()) {
                            Default_BG_Adapter.this.input.close();
                            try {
                                if (Default_BG_Adapter.this.output != null) {
                                    Default_BG_Adapter.this.output.close();
                                }
                                if (Default_BG_Adapter.this.input != null) {
                                    Default_BG_Adapter.this.input.close();
                                }
                            } catch (IOException unused3) {
                            }
                            if (httpURLConnection2 != null) {
                                httpURLConnection2.disconnect();
                            }
                            return null;
                        } else {
                            j += (long) read;
                            if (Default_BG_Adapter.fileLength > 0) {
                                publishProgress(Integer.valueOf((int) ((100 * j) / ((long) Default_BG_Adapter.fileLength))));
                            }
                            Default_BG_Adapter.this.output.write(bArr, 0, read);
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    httpURLConnection = httpURLConnection2;
                    try {
                        String exc = e.toString();
                        try {
                            if (Default_BG_Adapter.this.output != null) {
                                Default_BG_Adapter.this.output.close();
                            }
                            if (Default_BG_Adapter.this.input != null) {
                                Default_BG_Adapter.this.input.close();
                            }
                        } catch (IOException unused4) {
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        return exc;
                    } catch (Throwable th2) {
                        th = th2;
                        try {
                            if (Default_BG_Adapter.this.output != null) {
                                Default_BG_Adapter.this.output.close();
                            }
                            if (Default_BG_Adapter.this.input != null) {
                                Default_BG_Adapter.this.input.close();
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
                    if (Default_BG_Adapter.this.output != null) {
                    }
                    if (Default_BG_Adapter.this.input != null) {
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
                if (Default_BG_Adapter.this.output != null) {
                }
                if (Default_BG_Adapter.this.input != null) {
                }
                if (httpURLConnection != null) {
                }
                return exc2;
            }
            return null;
        }


        public void onCancelled() {
            super.onCancelled();
            Default_BG_Adapter.this.downloadingdailog.dismiss();
        }


        public void onPreExecute() {
            super.onPreExecute();
            Default_BG_Adapter.this.downloadingdailog = new Dialog(Default_BG_Adapter.mContext);
            Default_BG_Adapter.this.downloadingdailog.getWindow().requestFeature(1);
            Default_BG_Adapter.this.downloadingdailog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            Default_BG_Adapter.this.downloadingdailog.setContentView(R.layout.custom_downloading_dailog);
            Default_BG_Adapter.this.downloadingdailog.setCancelable(false);
            Default_BG_Adapter default_BG_Adapter = Default_BG_Adapter.this;
            default_BG_Adapter.liner_downloading_dailog = (LinearLayout) default_BG_Adapter.downloadingdailog.findViewById(R.id.liner_downloading_dailog);
            Help.getheightandwidth(Default_BG_Adapter.mContext);
            Help.setHeight(1080);
            Help.setSize(Default_BG_Adapter.this.liner_downloading_dailog, 600, 450, true);
            Default_BG_Adapter.this.downloadingdailog.show();
        }


        public void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate(numArr);
        }


        public void onPostExecute(String str) {
            Default_BG_Adapter.this.downloadingdailog.dismiss();
            Default_BG_Adapter.this.notifyDataSetChanged();
        }
    }


    public int getItemViewType(int i) {
        return super.getItemViewType(i);
    }


    public long getItemId(int i) {
        return super.getItemId(i);
    }
}
