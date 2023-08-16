package com.themescreen.flashcolor.stylescreen.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Ad_mob_Manager;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;
import com.themescreen.flashcolor.stylescreen.custom_dailor.Activity.Theme_LetsStartActivity;

public class Theme_First_Screen_Activity extends AppCompatActivity {
     TextView start_btn;
     LinearLayout ad_banner;
     SharePreferencess sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        ad_banner=findViewById(R.id.ad_banner);
        start_btn=findViewById(R.id.start_btn);
        sp=new SharePreferencess(this);
        Ad_mob_Manager.showNative(this);
        Ad_mob_Manager.showBanner(this,ad_banner,getWindow());
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_First_Screen_Activity.this).show_INTERSTIAL(Theme_First_Screen_Activity.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                      if(sp.getScreenFlag()==1)
                      {
                         startActivity(new Intent(getApplicationContext(), Theme_LetsStartActivity.class));
                      }
                      else
                      {
                          startActivity(new Intent(getApplicationContext(), Theme_Second_Activity.class));
                      }
                    }
                },sp.getMainCounter(),true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Appmanage.getInstance(Theme_First_Screen_Activity.this).show_INTERSTIAL(Theme_First_Screen_Activity.this, new Appmanage.MyCallback() {
            @Override
            public void callbackCall() {
              Theme_First_Screen_Activity.super.onBackPressed();
            }
        },sp.getBackCountAds(),false);
    }
}