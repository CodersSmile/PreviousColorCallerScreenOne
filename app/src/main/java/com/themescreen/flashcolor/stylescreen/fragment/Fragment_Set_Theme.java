package com.themescreen.flashcolor.stylescreen.fragment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.themescreen.flashcolor.stylescreen.Adapter.Adapter_contect_theme;
import com.themescreen.flashcolor.stylescreen.Model.ContectIconModel;
import com.themescreen.flashcolor.stylescreen.Model.ContectThemeModel;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.sqllite.SQLiteHelperClass;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.themescreen.flashcolor.stylescreen.utils.Utils_Stroage;

import java.util.ArrayList;

public class Fragment_Set_Theme extends Fragment {
    public static final String MyPREFERENCES = "myprefs";
    public static String ansvalue = "ansicon";
    public static String rejvalue = "reicon";
    public static final String value = "theamvideo";
    String TAG = "MINE_FRAGMENT";
    Adapter_contect_theme adapterContecttheme;
    ArrayList<ContectIconModel> contectIconModels = new ArrayList<>();
    ArrayList<ContectThemeModel> contectTheameModels = new ArrayList<>();
    LinearLayout dividerrr;
    String folderpath;
    ImageView img_current_theam;
    RecyclerView recyclecontettheme;
    RelativeLayout relative_main;
    SharedPreferences sharedpreferences;


    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_set_theme, viewGroup, false);
        this.sharedpreferences = getContext().getSharedPreferences("myprefs", 0);
        ui(inflate);
        Log.e(this.TAG, "onCreate:theam" + this.sharedpreferences.getString("theamvideo", "demo"));
        this.folderpath = Utils_Stroage.create_folder_in_app_package_dir(getContext(), "theams");
        String str = "android.resource://" + getContext().getPackageName() + "/" + R.raw.asset_theme1;
        String string = this.sharedpreferences.getString("theamvideo", str);
        if (!string.equals(str)) {
            ((RequestBuilder) Glide.with(getContext()).load(string).centerCrop()).into(this.img_current_theam);
        } else {
            ((RequestBuilder) Glide.with(getContext()).load(str).centerCrop()).into(this.img_current_theam);
        }
        SQLiteDatabase readableDatabase = new SQLiteHelperClass(getContext()).getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("SELECT NUMBER,RECEVIEICON,REJECTICON   FROM calling_icon", new String[0]);
        if (rawQuery != null && rawQuery.moveToFirst()) {
            do {
                this.contectIconModels.add(new ContectIconModel(rawQuery.getString(0), rawQuery.getInt(1), rawQuery.getInt(2)));
            } while (rawQuery.moveToNext());
            rawQuery.close();
        }
        Cursor rawQuery2 = readableDatabase.rawQuery("SELECT NUMBER,THEMEVALUE  FROM contect_theme", new String[0]);
        this.contectTheameModels.clear();
        if (rawQuery2 != null && rawQuery2.moveToFirst()) {
            this.recyclecontettheme.setVisibility(View.VISIBLE);
            do {
                Log.d(this.TAG, "onCreateView:1 " + rawQuery2.getString(0));
                Log.d(this.TAG, "onCreateView:2 " + rawQuery2.getString(1));
                this.contectTheameModels.add(new ContectThemeModel(rawQuery2.getString(0), rawQuery2.getString(1)));
            } while (rawQuery2.moveToNext());
            rawQuery2.close();
            this.recyclecontettheme.setLayoutManager(new GridLayoutManager(getContext(), 2));
            Adapter_contect_theme adapter_contect_theme = new Adapter_contect_theme(getContext(), this.contectTheameModels, this.contectIconModels);
            this.adapterContecttheme = adapter_contect_theme;
            this.recyclecontettheme.setAdapter(adapter_contect_theme);
        }
        return inflate;
    }

    public void ui(View view) {
        this.img_current_theam = (ImageView) view.findViewById(R.id.img_current_theam);
        this.recyclecontettheme = (RecyclerView) view.findViewById(R.id.recyclecontettheme);
        this.relative_main = (RelativeLayout) view.findViewById(R.id.relative_main);
        Help.getheightandwidth(getContext());
        Help.setHeight(1080);
        Help.setSize(this.img_current_theam, 1040, 620, true);
        Help.setSize(this.relative_main, 1040, 620, true);
    }
}
