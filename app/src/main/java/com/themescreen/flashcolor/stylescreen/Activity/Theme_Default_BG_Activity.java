package com.themescreen.flashcolor.stylescreen.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.Key;
import com.themescreen.flashcolor.stylescreen.Adapter.BgAdapter;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Ad_mob_Manager;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class Theme_Default_BG_Activity extends AppCompatActivity {
    ArrayList<String> arrayList = new ArrayList<>();
    RecyclerView recyclerview;
    ImageView back_btn;
    LinearLayout ad_banner;
    SharePreferencess sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_bg);
        back_btn=findViewById(R.id.back_btn);
        recyclerview=findViewById(R.id.recycle_view);
        ad_banner=findViewById(R.id.ad_banner);
        sp=new SharePreferencess(this);
        Ad_mob_Manager.showBanner(this,ad_banner,getWindow());
        arrayList.clear();
        recyclerview.setLayoutManager(new GridLayoutManager(Theme_Default_BG_Activity.this, 2));
        new catagorysogn().execute(new String[]{"instanceIdResult.getToken()"});

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_Default_BG_Activity.this).show_INTERSTIAL(Theme_Default_BG_Activity.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Theme_Default_BG_Activity.super.onBackPressed();
                    }
                },sp.getBackCountAds(),false);
            }
        });
    }

    public class catagorysogn extends AsyncTask<String, Void, String> {
        public catagorysogn() {
            Log.e("ss", "");
        }

        public void onPreExecute() {
            super.onPreExecute();
        }
        public String doInBackground(String... strArr) {
            StringBuilder sb = new StringBuilder();
            sb.append("doInBackground: ");
            sb.append(strArr[0]);
            Log.e("TAG", sb.toString());
            return getimageList(strArr[0]);
        }
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            InputStream openRawResource = getResources().openRawResource(R.raw.wallpaper);
            StringWriter stringWriter = new StringWriter();
            char[] cArr = new char[1024];
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openRawResource, Key.STRING_CHARSET_NAME));
                while (true) {
                    int read = bufferedReader.read(cArr);
                    if (read == -1) {
                        break;
                    }
                    stringWriter.write(cArr, 0, read);
                }
                openRawResource.close();
            } catch (UnsupportedEncodingException e) {
            } catch (IOException e2) {
                try {
                    e2.printStackTrace();
                    openRawResource.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            } catch (Throwable th) {
                try {
                    openRawResource.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
                throw th;
            }
            String stringWriter2 = stringWriter.toString();
            Log.e("String_Value",stringWriter2);
            if (stringWriter2 != null) {
                try {
                    JSONArray jSONArray = new JSONObject(stringWriter2).getJSONObject("data").getJSONArray("posts");
                    Log.e("String_Value",jSONArray.toString());
                    arrayList.clear();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        String string = jSONArray.getJSONObject(i).getString("file_name");
                        StringBuilder sb = new StringBuilder();
                        sb.append("onPostExecute: ");
                        sb.append(string);
                        Log.d("TAG", string);
                        arrayList.add(string);
                    }

                    recyclerview.setAdapter(new BgAdapter( arrayList, Theme_Default_BG_Activity.this));
                } catch (JSONException e6) {
                    e6.printStackTrace();
                }
            }
        }
    }

    public String getimageList(String str) {
        String str2 = "getVideoList: ";
        String str3 = "TAG";
        try {
            Response execute = new OkHttpClient().newCall(new Builder().url("http://phpstack-350318-1085389.cloudwaysapps.com/getmagicalvideostatus.php").post(new FormBody.Builder().add("package", "Color_Call_Baxley_DefaultBG").add("token", str).build()).build()).execute();
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(execute);
            Log.d(str3, sb.toString());
            if (execute.isSuccessful()) {
                return execute.body().string();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unexpected code ");
            sb2.append(execute);
            throw new IOException(sb2.toString());
        } catch (IOException e) {
            e.printStackTrace();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append(e);
            Log.d(str3, sb3.toString());
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        Appmanage.getInstance(Theme_Default_BG_Activity.this).show_INTERSTIAL(Theme_Default_BG_Activity.this, new Appmanage.MyCallback() {
            @Override
            public void callbackCall() {
                Theme_Default_BG_Activity.super.onBackPressed();
            }
        },sp.getBackCountAds(),false);
    }
}