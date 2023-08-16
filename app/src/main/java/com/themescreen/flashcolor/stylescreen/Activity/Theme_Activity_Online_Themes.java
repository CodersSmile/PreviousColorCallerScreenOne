package com.themescreen.flashcolor.stylescreen.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.themescreen.flashcolor.stylescreen.Adapter.Adapter_online_theme;
import com.themescreen.flashcolor.stylescreen.Model.OnlineThemeModel;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.themescreen.flashcolor.stylescreen.utils.Utils_Stroage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Theme_Activity_Online_Themes extends AppCompatActivity {
    String TAG = "ONLINE_THEAM_ACTIVITY";
    Adapter_online_theme adapterOnlinetheme;
    String folderpath;
    ArrayList<OnlineThemeModel> onlineTheamModels = new ArrayList<>();
    RecyclerView onlinetheam_recycleview;
    ProgressDialog songsprogress;



    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_online_theme);
        Help.FS(this);
        this.onlinetheam_recycleview = (RecyclerView) findViewById(R.id.onlinetheam_recycleview);
        this.folderpath = Utils_Stroage.create_folder_in_app_package_dir(this, "theams");
        Toast.makeText(this, "" + this.folderpath, Toast.LENGTH_SHORT).show();
        if (!new File(this.folderpath + "/asset_theme1.mp4").exists()) {
            try {
                CopyRAWtoSDCard(R.raw.asset_theme1, this.folderpath + "/asset_theme1.mp4");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {

            public void onSuccess(InstanceIdResult instanceIdResult) {
                Log.e(Theme_Activity_Online_Themes.this.TAG, "onSuccess:InstanceIdResult" + instanceIdResult);
                new catagorysogn().execute(instanceIdResult.getToken());
            }
        }).addOnFailureListener(new OnFailureListener() {


            public void onFailure(Exception exc) {
            }
        });
    }

    public class catagorysogn extends AsyncTask<String, Void, String> {
        public catagorysogn() {
        }


        public void onPreExecute() {
            super.onPreExecute();
            Theme_Activity_Online_Themes.this.songsprogress = new ProgressDialog(Theme_Activity_Online_Themes.this);
            Theme_Activity_Online_Themes.this.songsprogress.setMessage("Please wait...Songs are loading");
            Theme_Activity_Online_Themes.this.songsprogress.setIndeterminate(false);
            Theme_Activity_Online_Themes.this.songsprogress.show();
        }


        public String doInBackground(String... strArr) {
            Log.d(Theme_Activity_Online_Themes.this.TAG, "doInBackground: " + strArr[0]);
            return Theme_Activity_Online_Themes.this.getVideoList(strArr[0]);
        }


        public void onPostExecute(String str) {
            super.onPostExecute((String) str);
            Theme_Activity_Online_Themes.this.songsprogress.dismiss();
            Log.d(Theme_Activity_Online_Themes.this.TAG, "onPostExecute: " + str);
            if (str != null) {
                try {
                    JSONArray jSONArray = new JSONObject(str).getJSONObject("data").getJSONArray("posts");
                    Theme_Activity_Online_Themes.this.onlineTheamModels.clear();
                    Theme_Activity_Online_Themes.this.onlineTheamModels.add(new OnlineThemeModel(Theme_Activity_Online_Themes.this.folderpath + "/asset_theme1.mp4"));
                    for (int i = 0; i < jSONArray.length(); i++) {
                        String string = jSONArray.getJSONObject(i).getString("file_name");
                        Log.d(Theme_Activity_Online_Themes.this.TAG, "onPostExecute: " + string);
                        Theme_Activity_Online_Themes.this.listsongfile(string);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getVideoList(String str) {
        try {
            Response execute = new OkHttpClient().newCall(new Request.Builder().url("http://phpstack-350318-1085389.cloudwaysapps.com/getmagicalvideostatus.php").post(new FormBody.Builder().add("package", "KateSimmons_ColorCall").add("token", str).build()).build()).execute();
            Log.d(this.TAG, "getVideoList: " + execute);
            if (execute.isSuccessful()) {
                return execute.body().string();
            }
            throw new IOException("Unexpected code " + execute);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(this.TAG, "getVideoList: " + e);
            return null;
        }
    }

    private boolean listsongfile(String str) {
        Log.d(this.TAG, "listAssetFiles: " + str);
        this.onlineTheamModels.add(new OnlineThemeModel(str));
        this.onlinetheam_recycleview.setLayoutManager(new GridLayoutManager(this, 2));
        Adapter_online_theme adapter_online_theme = new Adapter_online_theme(this.onlineTheamModels, this);
        this.adapterOnlinetheme = adapter_online_theme;
        this.onlinetheam_recycleview.setAdapter(adapter_online_theme);
        return true;
    }

    private void CopyRAWtoSDCard(int i, String str) throws IOException {
        InputStream openRawResource = getResources().openRawResource(i);
        FileOutputStream fileOutputStream = new FileOutputStream(str);
        byte[] bArr = new byte[1024];
        while (true) {
            try {
                int read = openRawResource.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    return;
                }
            } finally {
                openRawResource.close();
                fileOutputStream.close();
            }
        }
    }
}
