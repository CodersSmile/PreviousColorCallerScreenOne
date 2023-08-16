package com.themescreen.flashcolor.stylescreen.custom_dailor.Activity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Ad_mob_Manager;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;
import com.themescreen.flashcolor.stylescreen.databinding.ActivityCallerPreview2Binding;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class Theme_Caller_Preview_Activity extends AppCompatActivity {
    public static final String MyPREFERENCES = "myprefs";
    ActivityCallerPreview2Binding binding;
    SharedPreferences sharedpreferences;
     SharePreferencess sp;
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityCallerPreview2Binding inflate = ActivityCallerPreview2Binding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView(inflate.getRoot());
        Help.FS(this);
        sp=new SharePreferencess(this);
        Ad_mob_Manager.showBanner(this, binding.adBanner, getWindow());
        resize();
    }
    public void onResume() {
        super.onResume();
        YoYo.with(Techniques.Bounce).duration(700).repeat(-1).playOn(this.binding.layAns1);
        YoYo.with(Techniques.Bounce).duration(500).repeat(-1).playOn(this.binding.layHangUp1);
        this.sharedpreferences = getSharedPreferences("myprefs", 0);
        this.binding.videoView.setVideoURI(Uri.parse(this.sharedpreferences.getString("theamvideo", "android.resource://" + getPackageName() + "/" + R.raw.asset_theme1)));
        this.binding.videoView.start();
        this.binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setVolume(0.0f, 0.0f);
            }
        });
        this.binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                Theme_Caller_Preview_Activity.this.binding.videoView.start();
            }
        });
    }

    private void resize() {
        Help.getheightandwidth(this);
        Help.setSize(this.binding.layPRof1, 350, 350, true);
        Help.setSize(this.binding.imageViewProfile1, 310, 310, true);
        Help.setSize(this.binding.imgProfile1, 310, 310, true);
        Help.setSize(this.binding.imgAns1, 180, 180, true);
        Help.setSize(this.binding.imgHangUp1, 180, 180, true);
    }

    @Override
    public void onBackPressed() {
        Appmanage.getInstance(this).show_INTERSTIAL(this, new Appmanage.MyCallback() {
            @Override
            public void callbackCall() {
              Theme_Caller_Preview_Activity.super.onBackPressed();
            }
        },sp.getBackCountAds(),false);
    }
}
