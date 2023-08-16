package com.themescreen.flashcolor.stylescreen.Activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.themescreen.flashcolor.stylescreen.Adapter.ButtonStyle_Adapter;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Ad_mob_Manager;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;

public class Theme_Dialer_Pad_Activity extends AppCompatActivity {
    RecyclerView recycle_view;
    AssetManager manager;
    String[] list;
    ButtonStyle_Adapter adapter;
    ImageView back_btn;
    SharePreferencess sp;
    LinearLayout ad_banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer_pad);
        recycle_view=findViewById(R.id.recycle_view);
        back_btn=findViewById(R.id.back_btn);
        ad_banner=findViewById(R.id.ad_banner);
        sp=new SharePreferencess(this);
        Ad_mob_Manager.showBanner(this,ad_banner,getWindow());
        manager=getAssets();
        try {
            list=manager.list("button_bg");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        recycle_view.setLayoutManager(new GridLayoutManager(this,2));
        adapter=new ButtonStyle_Adapter(list,this);
        recycle_view.setAdapter(adapter);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_Dialer_Pad_Activity.this).show_INTERSTIAL(Theme_Dialer_Pad_Activity.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Theme_Dialer_Pad_Activity.super.onBackPressed();
                    }
                },sp.getBackCountAds(),false);

            }
        });

    }

    @Override
    public void onBackPressed() {
        Appmanage.getInstance(Theme_Dialer_Pad_Activity.this).show_INTERSTIAL(Theme_Dialer_Pad_Activity.this, new Appmanage.MyCallback() {
            @Override
            public void callbackCall() {
                Theme_Dialer_Pad_Activity.super.onBackPressed();
            }
        },sp.getBackCountAds(),false);
    }
}