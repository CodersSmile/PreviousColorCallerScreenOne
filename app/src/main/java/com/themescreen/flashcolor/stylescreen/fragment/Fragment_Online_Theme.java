package com.themescreen.flashcolor.stylescreen.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.themescreen.flashcolor.stylescreen.Adapter.Adapter_online_theme;
import com.themescreen.flashcolor.stylescreen.Model.OnlineThemeModel;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.utils.Utils_Stroage;
import com.facebook.shimmer.ShimmerFrameLayout;
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

public class Fragment_Online_Theme extends Fragment {
    String TAG = "ONLINE_THEAM_ACTIVITY";
    Adapter_online_theme adapterOnlinetheme;
    String folderpath;
    ArrayList<OnlineThemeModel> onlineTheamModels = new ArrayList<>();
    RecyclerView onlinetheam_recycleview;
    ShimmerFrameLayout shimmerFrameLayout;
    ProgressDialog songsprogress;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.activity_online_theme, viewGroup, false);
        this.onlinetheam_recycleview = (RecyclerView) inflate.findViewById(R.id.onlinetheam_recycleview);
        this.shimmerFrameLayout = (ShimmerFrameLayout) inflate.findViewById(R.id.shimmer);
        this.folderpath = Utils_Stroage.create_folder_in_app_package_dir(getContext(), "theams");
        this.shimmerFrameLayout.startShimmer();

        if (!new File(this.folderpath + "/asset_theme1.mp4").exists()) {
            try {
                CopyRAWtoSDCard(R.raw.asset_theme1, this.folderpath + "/asset_theme1.mp4");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (connectivity(getContext())) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {

                public void onSuccess(InstanceIdResult instanceIdResult) {
                    Log.e(Fragment_Online_Theme.this.TAG, "onSuccess:InstanceIdResult" + instanceIdResult);
                    new catagorysogn().execute(instanceIdResult.getToken());
                }
            }).addOnFailureListener(new OnFailureListener() {

                public void onFailure(Exception exc) {
                }
            });
        } else {
            Toast.makeText(getContext(), "Check Network Connectivity", Toast.LENGTH_SHORT).show();
        }
        return inflate;
    }

    public class catagorysogn extends AsyncTask<String, Void, String> {
        public catagorysogn() {
        }

        public void onPreExecute() {
            super.onPreExecute();
        }

        public String doInBackground(String... strArr) {
            Log.d(TAG, "doInBackground: " + strArr[0]);
            return getVideoList(strArr[0]);
        }

        public void onPostExecute(String str) {
            super.onPostExecute((String) str);
          /*  InputStream openRawResource = getResources().openRawResource(R.raw.response);
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

                    for (int i = 0; i < jSONArray.length(); i++) {
                        String string = jSONArray.getJSONObject(i).getString("file_name");
                        StringBuilder sb = new StringBuilder();
                        sb.append("onPostExecute: ");
                        sb.append(string);
                        Log.d("TAG", string);
                        listsongfile(string);
                    }

                   // recyclerview.setAdapter(new BgAdapter( arrayList, Default_BG_Activity.this));
                } catch (JSONException e6) {
                    e6.printStackTrace();
                }*/

            Fragment_Online_Theme.this.shimmerFrameLayout.stopShimmer();
            Log.d(Fragment_Online_Theme.this.TAG, "onPostExecute: " + str);
            if (str != null) {
                try {
                    JSONArray jSONArray = new JSONObject(str).getJSONObject("data").getJSONArray("posts");
                    Fragment_Online_Theme.this.onlineTheamModels.clear();
                    Fragment_Online_Theme.this.onlineTheamModels.add(new OnlineThemeModel(Fragment_Online_Theme.this.folderpath + "/asset_theme1.mp4"));
                    Log.e("Akash", "onPostExecute: " + folderpath);
                    for (int i = 0; i < jSONArray.length(); i++) {
                        String string = jSONArray.getJSONObject(i).getString("file_name");
                        Log.d(Fragment_Online_Theme.this.TAG, "onPostExecute: " + string);
                        Fragment_Online_Theme.this.listsongfile(string);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getVideoList(String str) {
        try {
            Response execute = new OkHttpClient().newCall(new Request.Builder().url("https://phpstack-350318-1085389.cloudwaysapps.com/getmagicalvideostatus.php").post(new FormBody.Builder().add("package", "Color_Call_Baxley_Themes_All").add("token", str).build()).build()).execute();
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
        this.onlinetheam_recycleview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        Adapter_online_theme adapter_online_theme = new Adapter_online_theme(this.onlineTheamModels, getContext());
        this.adapterOnlinetheme = adapter_online_theme;
        this.onlinetheam_recycleview.setAdapter(adapter_online_theme);
        return true;
    }

    private void CopyRAWtoSDCard(int id, String path) throws IOException {
        InputStream in = getResources().openRawResource(id);
        FileOutputStream out = new FileOutputStream(path);
        byte[] buff = new byte[1024];
        int read = 0;

        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
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
}
