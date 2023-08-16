package com.themescreen.flashcolor.stylescreen.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.themescreen.flashcolor.stylescreen.Adapter.Wallpaper_Adapter;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Ad_mob_Manager;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;

public class Theme_Wallpalerlist_Activity extends AppCompatActivity {
    public int[] wallpaper = {R.drawable.me11, R.drawable.ld18, R.drawable.bu7, R.drawable.vo24, R.drawable.bu19, R.drawable.re22, R.drawable.ye27, R.drawable.we33, R.drawable.po13, R.drawable.fe37, R.drawable.we5, R.drawable.re28, R.drawable.re45, R.drawable.bm23, R.drawable.ch4, R.drawable.fe42, R.drawable.bl33, R.drawable.ni14, R.drawable.mi8, R.drawable.ja3, R.drawable.bl31, R.drawable.to6, R.drawable.ba39, R.drawable.le5, R.drawable.we44, R.drawable.re21, R.drawable.gr20, R.drawable.ch19, R.drawable.we25, R.drawable.gr17, R.drawable.audi7, R.drawable.gr23, R.drawable.re9, R.drawable.ye10, R.drawable.ba35, R.drawable.audi4, R.drawable.ba49, R.drawable.ni7, R.drawable.bm22, R.drawable.ma25, R.drawable.bu35, R.drawable.bm18, R.drawable.la30, R.drawable.gr31, R.drawable.fe41, R.drawable.bu8, R.drawable.ba10, R.drawable.ma23, R.drawable.gr6, R.drawable.bm37};
    RecyclerView recycle_view;
    Wallpaper_Adapter adapter;
    ImageView back_btn;
    LinearLayout ad_banner;
    SharePreferencess sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpalerlist);
        recycle_view = findViewById(R.id.recycle_view);
        back_btn=findViewById(R.id.back_btn);
        ad_banner=findViewById(R.id.ad_banner);
        sp=new SharePreferencess(this);
        Ad_mob_Manager.showBanner(this,ad_banner,getWindow());
        recycle_view.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new Wallpaper_Adapter(wallpaper, this);
        recycle_view.setAdapter(adapter);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_Wallpalerlist_Activity.this).show_INTERSTIAL(Theme_Wallpalerlist_Activity.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Theme_Wallpalerlist_Activity.super.onBackPressed();
                    }
                },sp.getBackCountAds(),false);

            }
        });
    }
    @Override
    public void onBackPressed() {
        Appmanage.getInstance(Theme_Wallpalerlist_Activity.this).show_INTERSTIAL(Theme_Wallpalerlist_Activity.this, new Appmanage.MyCallback() {
            @Override
            public void callbackCall() {
                Theme_Wallpalerlist_Activity.super.onBackPressed();
            }
        },sp.getBackCountAds(),false);
    }
}