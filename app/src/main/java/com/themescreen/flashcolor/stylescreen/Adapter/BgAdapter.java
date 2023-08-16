package com.themescreen.flashcolor.stylescreen.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.button_style.SP_Helper;
import com.themescreen.flashcolor.stylescreen.utils.AppConst;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.themescreen.flashcolor.stylescreen.utils.Utils_Stroage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BgAdapter extends RecyclerView.Adapter<BgAdapter.Myholder> {
    ArrayList<String> list;
    Context context;
    String folders;
    String string1;

    public BgAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.shape_adapter_defult,parent,false);
        Myholder myholder=new Myholder(view);
        return myholder;
    }
    @Override
    public void onBindViewHolder(@NonNull Myholder holder, @SuppressLint("RecyclerView") int position) {
        Log.e("Img_Url",""+list.get(position));
        Glide.with(context).load(list.get(position)).centerCrop().into(holder.img);

       folders = createFolderInAppPackage(context, "Default Background");
        String str = (String) this.list.get(position);
        String str2 = "/";
        String str3 = ".";
        String substring = str.substring(str.lastIndexOf(str2), str.lastIndexOf(str3));
        string1 = substring.substring(substring.indexOf(str3) + 1, substring.length());
        StringBuilder sb = new StringBuilder();
        sb.append(folders);
        sb.append(str2);
        sb.append(string1);
        sb.append(".jpg");
        boolean exists = new File(sb.toString()).exists();
        String str4 = "";
        String str5 = "dd";
        if (!exists) {
            Log.e(str5, str4);
        } else {
            Log.e(str5, str4);
        }
        holder.imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConst.idiom = 2;
                String str = (String) BgAdapter.this.list.get(position);
                String str2 = "/";
                String str3 = ".";
                String substring = str.substring(str.lastIndexOf(str2), str.lastIndexOf(str3));
                BgAdapter.this.string1 = substring.substring(substring.indexOf(str3) + 1, substring.length());
                StringBuilder sb = new StringBuilder();
                sb.append(BgAdapter.this.folders);
                sb.append(str2);
                sb.append(BgAdapter.this.string1);
                sb.append(".jpg");
                boolean exists = new File(sb.toString()).exists();
                String str4 = "onClick: ";
                String str5 = "tag_img";
                if (exists) {
                    String str6 = list.get(position);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str4);
                    sb2.append(str6.substring(str6.lastIndexOf(str2) + 1, str6.lastIndexOf(str3)));
                    Log.e(str5, sb2.toString());
                    SP_Helper.putValueInSharedpreference(context, SP_Helper.IMAGER, list.get(position));
                    Toast.makeText(context, "Succsessfully Set Dailer Background..!", Toast.LENGTH_SHORT).show();
                    return;
                }
                new DownloadTask(position).execute(new String[]{(String) list.get(position)});
                String str7 = list.get(position);
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str4);
                sb3.append(str7.substring(str7.lastIndexOf(str2) + 1, str7.lastIndexOf(str3)));
                Log.e(str5, sb3.toString());
                SP_Helper.putValueInSharedpreference(context, SP_Helper.IMAGER, list.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView imgDownload;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            img =itemView.findViewById(R.id.img);
            imgDownload =itemView.findViewById(R.id.imgDownload);

        }
    }

    public static String createFolderInAppPackage(Context context, String str) {
        File file = new File(context.getFilesDir(), str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public class DownloadTask extends AsyncTask<String, Integer, String> {
        Dialog dialog;
        int fileLength;
        InputStream input = null;
        OutputStream output = null;
        String string;
        String string1;
        int pos;

        public DownloadTask(int pos) {
            this.pos=pos;
            Log.e("dd", "");
        }

        public String doInBackground(String... strArr) {
            HttpURLConnection httpURLConnection;
            String str = ".jpg";
            String str2 = "/";
            String str3 = "";
            String str4 = "dd";
            try {
                httpURLConnection = (HttpURLConnection) new URL(strArr[0]).openConnection();
                httpURLConnection.connect();
                StringBuilder sb = new StringBuilder();
                sb.append("doInBackground: connectioncode ");
                sb.append(httpURLConnection.getResponseCode());
                Log.d("tag", sb.toString());
                if (httpURLConnection.getResponseCode() != 200) {
                    OutputStream outputStream = this.output;
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    InputStream inputStream = this.input;
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    String str5 = "Please Enter Valid Name";
                    if (httpURLConnection == null) {
                        return str5;
                    }
                    httpURLConnection.disconnect();
                    return str5;
                }
                this.fileLength = httpURLConnection.getContentLength();
                this.input = httpURLConnection.getInputStream();
                String createFolderInAppPackage = Utils_Stroage.createFolderInAppPackage(context, "Default Background");
                StringBuilder sb2 = new StringBuilder();
                sb2.append(createFolderInAppPackage);
                sb2.append(str2);
                sb2.append(string1);
                sb2.append(str);
                string = sb2.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(createFolderInAppPackage);
                sb3.append(str2);
                sb3.append(string1);
                sb3.append(str);
                this.output = new FileOutputStream(sb3.toString());
                byte[] bArr = new byte[4096];
                long j = 0;
                while (true) {
                    int read = input.read(bArr);
                    if (read == -1) {
                        OutputStream outputStream2 = output;
                        if (outputStream2 != null) {
                            outputStream2.close();
                        }
                        InputStream inputStream2 = input;
                        if (inputStream2 != null) {
                            inputStream2.close();
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        return sb3.toString();
                    } else if (isCancelled()) {
                        break;
                    } else {
                        j += (long) read;
                        int i = this.fileLength;
                        if (i > 0) {
                            publishProgress(new Integer[]{Integer.valueOf((int) ((100 * j) / ((long) i)))});
                        }
                        this.output.write(bArr, 0, read);
                    }
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                return null;
            } catch (Exception e) {
                String exc = e.toString();
                return exc;
            }
        }

        public void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
        }

        public void onPreExecute() {
            super.onPreExecute();
            Dialog dialog2 = new Dialog(context);
            this.dialog = dialog2;
            dialog2.getWindow().requestFeature(1);
            this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            this.dialog.setContentView(R.layout.download_dialog);
            this.dialog.setCancelable(false);
            LinearLayout linearLayout = (LinearLayout) this.dialog.findViewById(R.id.liner_downloading_dailog);
            ImageView imageView = (ImageView) this.dialog.findViewById(R.id.img_loading);
            ((RequestBuilder) Glide.with((FragmentActivity)context).load(Integer.valueOf(R.drawable.downloading_gif)).override(300, 300)).into(imageView);
            Help.getheightandwidth(context);
            Help.setHeight(1080);
            Help.setSize(linearLayout, 600, 450, true);
            Help.setSize(imageView, 300, 300, true);
            this.dialog.show();
        }

        public void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate(numArr);
        }

        public void onPostExecute(String str) {
            this.dialog.dismiss();
            Log.e("Image_tag",str);
            SP_Helper.putValueInSharedpreference(context, SP_Helper.IMAGER, list.get(pos));
            Toast.makeText(context, "Succsessfully Set Dailer Background..!", Toast.LENGTH_SHORT).show();
        }
    }





}
