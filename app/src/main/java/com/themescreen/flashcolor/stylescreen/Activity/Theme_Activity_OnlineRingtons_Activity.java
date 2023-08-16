package com.themescreen.flashcolor.stylescreen.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.themescreen.flashcolor.stylescreen.Adapter.Adapter_online_ringtone;
import com.themescreen.flashcolor.stylescreen.Model.OnlineRingtonModel;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.themescreen.flashcolor.stylescreen.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Theme_Activity_OnlineRingtons_Activity extends AppCompatActivity {
    String TAG = "DEMO_ACTIVITY";
    Adapter_online_ringtone adapterOnlineringtone;
    ImageView img_back;
    ArrayList<OnlineRingtonModel> onlineRingtonModels = new ArrayList<>();
    RecyclerView recycleview_onlineringtone;
    ProgressDialog songsprogress;
    TextView textview_actionbar;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_ringtone);
        Help.FS(this);
        ui();
        checkSystemWritePermission();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {

            public void onSuccess(InstanceIdResult instanceIdResult) {
                Log.e(Theme_Activity_OnlineRingtons_Activity.this.TAG, "onSuccess:InstanceIdResult" + instanceIdResult);
                new catagorysogn().execute(instanceIdResult.getToken());
            }
        }).addOnFailureListener(new OnFailureListener() {

            public void onFailure(Exception exc) {
            }
        });
        this.img_back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Theme_Activity_OnlineRingtons_Activity.this.onBackPressed();
            }
        });
    }

    public void ui() {
        this.recycleview_onlineringtone = (RecyclerView) findViewById(R.id.recycleview_onlineringtone);
        this.img_back = (ImageView) findViewById(R.id.img_back);
        TextView textView = (TextView) findViewById(R.id.textview_actionbar);
        this.textview_actionbar = textView;
        textView.setText("Select Ringtone");
        Help.getheightandwidth(this);
        Help.setHeight(1080);
        Help.setSize(this.img_back, 80, 80, true);
    }

    public class catagorysogn extends AsyncTask<String, Void, String> {
        public catagorysogn() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            Theme_Activity_OnlineRingtons_Activity.this.songsprogress = new ProgressDialog(Theme_Activity_OnlineRingtons_Activity.this);
            Theme_Activity_OnlineRingtons_Activity.this.songsprogress.setMessage("Please wait...Songs are loading");
            Theme_Activity_OnlineRingtons_Activity.this.songsprogress.setIndeterminate(false);
            Theme_Activity_OnlineRingtons_Activity.this.songsprogress.show();
        }

        public String doInBackground(String... strArr) {
            Log.d(Theme_Activity_OnlineRingtons_Activity.this.TAG, "doInBackground: " + strArr[0]);
            return Theme_Activity_OnlineRingtons_Activity.this.getVideoList(strArr[0]);
        }

        public void onPostExecute(String str) {
            super.onPostExecute((String) str);
            Theme_Activity_OnlineRingtons_Activity.this.songsprogress.dismiss();
            Log.d(Theme_Activity_OnlineRingtons_Activity.this.TAG, "onPostExecute: " + str);
            if (str != null) {
                try {
                    JSONArray jSONArray = new JSONObject(str).getJSONObject("data").getJSONArray("posts");
                    for (int i = 0; i < jSONArray.length(); i++) {
                        String string = jSONArray.getJSONObject(i).getString("file_name");
                        Log.d(Theme_Activity_OnlineRingtons_Activity.this.TAG, "onPostExecute:gallary " + string);
                        Theme_Activity_OnlineRingtons_Activity.this.onlineRingtonModels.add(new OnlineRingtonModel(string));
                    }
                    Theme_Activity_OnlineRingtons_Activity.this.recycleview_onlineringtone.setLayoutManager(new LinearLayoutManager(Theme_Activity_OnlineRingtons_Activity.this));
                    Theme_Activity_OnlineRingtons_Activity activity_OnlineRingtons_Activity = Theme_Activity_OnlineRingtons_Activity.this;
                    Theme_Activity_OnlineRingtons_Activity activity_OnlineRingtons_Activity2 = Theme_Activity_OnlineRingtons_Activity.this;
                    activity_OnlineRingtons_Activity.adapterOnlineringtone = new Adapter_online_ringtone(activity_OnlineRingtons_Activity2, activity_OnlineRingtons_Activity2.onlineRingtonModels, Theme_Activity_OnlineRingtons_Activity.this);
                    Theme_Activity_OnlineRingtons_Activity.this.recycleview_onlineringtone.setAdapter(Theme_Activity_OnlineRingtons_Activity.this.adapterOnlineringtone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getVideoList(String str) {
        try {
            Response execute = new OkHttpClient().newCall(new Request.Builder().url("http://phpstack-350318-1085389.cloudwaysapps.com/getmagicalvideostatus.php").post(new FormBody.Builder().add("package", "Color_Call_Baxley_Ringtone").add("token", str).build()).build()).execute();
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

    public void checkSystemWritePermission() {
        if (Settings.System.canWrite(this)) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 155);
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_SETTINGS"}, 155);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        boolean z;
        super.onActivityResult(i, i2, intent);
        if (i == 155) {
            if (Build.VERSION.SDK_INT >= 23) {
                z = Settings.System.canWrite(this);
            } else {
                z = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_SETTINGS") == 0;
            }
            if (z) {
                Utils.displayToast(this, "Check Again To Set");
            } else {
                Utils.displayToast(this, "You Didn't allow the Important permission !");
            }
        }
    }

    public void onBackPressed() {
        if (Adapter_online_ringtone.mediaPlayer.isPlaying()) {
            Adapter_online_ringtone.mediaPlayer.stop();
        }
        finish();
    }
}
