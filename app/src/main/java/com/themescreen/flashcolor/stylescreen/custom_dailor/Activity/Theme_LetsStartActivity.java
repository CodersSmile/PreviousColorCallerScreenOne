package com.themescreen.flashcolor.stylescreen.custom_dailor.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.themescreen.flashcolor.stylescreen.Activity.Activity_Permission;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Ad_mob_Manager;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;
import com.themescreen.flashcolor.stylescreen.utils.Call_Permission;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.interstitial.InterstitialAd;

public class Theme_LetsStartActivity extends AppCompatActivity {
    private TextView lets_start_btn;
    private ImageView share_btn;
    private ImageView privacy_btn;
    private ImageView rate_btn;
    private AdLoader adLoader;
    private InterstitialAd mInterstitialAd;
    Context context;
    LinearLayout ad_banner;
    SharePreferencess sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lets_start);
        context = this;
        sp=new SharePreferencess(this);
        lets_start_btn = findViewById(R.id.lets_start_btn);
        share_btn = (ImageView) findViewById(R.id.share_btn);
        privacy_btn = (ImageView) findViewById(R.id.privacy_btn);
        rate_btn = (ImageView) findViewById(R.id.rate_btn);
        ad_banner=findViewById(R.id.ad_banner);
        Ad_mob_Manager.showBanner(this,ad_banner,getWindow());
        Ad_mob_Manager.showNative(this);
        lets_start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Appmanage.getInstance(Theme_LetsStartActivity.this).show_INTERSTIAL(Theme_LetsStartActivity.this, new Appmanage.MyCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void callbackCall() {
                        photo_caller_scrren_click();
                    }
                },sp.getMainCounter(),true);
            }
        });

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Help.share(context);
            }
        });

        privacy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sp.getPrivacy().equals("") && sp.getPrivacy().isEmpty())
                {

                }
                else
                {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sp.getPrivacy()));
                    startActivity(browserIntent);
                }

            }
        });

        rate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Help.rate(context);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void photo_caller_scrren_click() {
        if (!checkpermission().booleanValue()) {
            startActivity(new Intent(this, Activity_Permission.class));
        }else {
            startActivity(new Intent(this, Theme_Activity_Callerscreen_Selection.class));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Boolean checkpermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") + ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") + ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS") != 0 || !new Call_Permission().isappdefault(this) || !Settings.System.canWrite(this)) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(this)) {
            return true;
        }
        return false;
    }

    public void onBackPressed() {
       Appmanage.getInstance(this).show_INTERSTIAL(this, new Appmanage.MyCallback() {
           @Override
           public void callbackCall() {
               Theme_LetsStartActivity.super.onBackPressed();
           }
       },sp.getBackCountAds(),false);
    }





}