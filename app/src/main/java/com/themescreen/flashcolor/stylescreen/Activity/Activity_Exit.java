package com.themescreen.flashcolor.stylescreen.Activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Ad_mob_Manager;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;

public class Activity_Exit extends AppCompatActivity {
LinearLayout ad_banner;
SharePreferencess sp;
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_exit);
        ad_banner=findViewById(R.id.ad_banner);
        sp=new SharePreferencess(this);
        Ad_mob_Manager.showBanner(this,ad_banner,getWindow());
        Ad_mob_Manager.showNative(this);
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
