package com.themescreen.flashcolor.stylescreen.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Ad_mob_Manager;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;
import com.themescreen.flashcolor.stylescreen.utils.AppConst;

import java.io.IOException;

public class Theme_Dialer_Menu_Activity extends AppCompatActivity {
RelativeLayout butt_btn,dialer_btn,choose_btn,default_btn,sytem_btn;
ImageView back_btnp;
SharePreferencess sp;
LinearLayout ad_banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer_menu);
        butt_btn=findViewById(R.id.butt_btn);
        dialer_btn=findViewById(R.id.dialer_btn);
        choose_btn=findViewById(R.id.choose_btn);
        default_btn=findViewById(R.id.default_btn);
        sytem_btn=findViewById(R.id.sytem_btn);
        back_btnp=findViewById(R.id.back_btn);
        ad_banner=findViewById(R.id.ad_banner);
        sp=new SharePreferencess(this);
        Ad_mob_Manager.showNative(this);
        Ad_mob_Manager.showBanner(this,ad_banner,getWindow());
        butt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_Dialer_Menu_Activity.this).show_INTERSTIAL(Theme_Dialer_Menu_Activity.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        startActivity(new Intent(getApplicationContext(), Theme_Dialer_Pad_Activity.class));
                    }
                },sp.getMainCounter(),true);
            }
        });
        dialer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_Dialer_Menu_Activity.this).show_INTERSTIAL(Theme_Dialer_Menu_Activity.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        startActivity(new Intent(getApplicationContext(), Theme_Diler_Activity.class));
                    }
                },sp.getMainCounter(),true);
            }
        });

        choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConst.idiom = 1;
                Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 123);
            }
        });
        default_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_Dialer_Menu_Activity.this).show_INTERSTIAL(Theme_Dialer_Menu_Activity.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        startActivity(new Intent(getApplicationContext(), Theme_Default_BG_Activity.class));
                    }
                },sp.getMainCounter(),true);
            }
        });
        sytem_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_Dialer_Menu_Activity.this).show_INTERSTIAL(Theme_Dialer_Menu_Activity.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        startActivity(new Intent(getApplicationContext(), Theme_SytemInformation_Activity.class));
                    }
                },sp.getMainCounter(),true);
            }
        });
        back_btnp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_Dialer_Menu_Activity.this).show_INTERSTIAL(Theme_Dialer_Menu_Activity.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Theme_Dialer_Menu_Activity.super.onBackPressed();
                    }
                },sp.getBackCountAds(),false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == -1 && data != null && data.getData() != null) {
            try {
                AppConst.Main_Pic = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                startActivity(new Intent(this, Theme_CropActivity.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed() {
        Appmanage.getInstance(Theme_Dialer_Menu_Activity.this).show_INTERSTIAL(Theme_Dialer_Menu_Activity.this, new Appmanage.MyCallback() {
            @Override
            public void callbackCall() {
                Theme_Dialer_Menu_Activity.super.onBackPressed();
            }
        },sp.getBackCountAds(),false);
    }
}