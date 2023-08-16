package com.themescreen.flashcolor.stylescreen.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.themescreen.flashcolor.stylescreen.utils.Utils_Stroage;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class Theme_Activity_Calling_Theme_Preview extends AppCompatActivity {
    public static final String MyPREFERENCES = "myprefs";
    public static String ansvalue = "ansicon";
    public static String rejvalue = "reicon";
    public static final String value = "theamvideo";
    String TAG = "OnlineTheamPreviewActivity";
    TextView btn_set_theam;
    Context context;
    String folderpath;
    ImageView img_back;
    ImageView img_recive;
    ImageView img_reject;
    ImageView img_user;
    MediaPlayer mp = new MediaPlayer();
    Ringtone r;
    VideoView raw_online_theam_videoview;
    SharedPreferences sharedpreferences;
    SharePreferencess sp;
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_online_theme_preview);
        Help.FS(this);
        sp=new SharePreferencess(this);
        ui();
        this.context = this;
        int i = this.sharedpreferences.getInt(ansvalue, R.drawable.accept_button_three);
        int i2 = this.sharedpreferences.getInt(rejvalue, R.drawable.decline_button_three);
        Log.e(this.TAG, "onCreate:theam" + i);
        Log.e(this.TAG, "onCreate:theam" + i2);
        this.img_reject.setImageResource(i2);
        this.img_recive.setImageResource(i);
        this.folderpath = Utils_Stroage.create_folder_in_app_package_dir(this, "theams");
        String string = this.sharedpreferences.getString("theamvideo", "demo");
        if (string != null) {
            if (string.equals("demo")) {
                this.btn_set_theam.setText("Set Theme");
            } else if (string.equals(this.folderpath + "/" + getIntent().getStringExtra("theampath") + ".mp4")) {
                this.btn_set_theam.setText("Current Theme");
                this.btn_set_theam.setEnabled(false);
            } else {
                this.btn_set_theam.setText("Set Theme");
            }
        }
        this.raw_online_theam_videoview = (VideoView) findViewById(R.id.raw_online_theam_videoview);
        this.img_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Theme_Activity_Calling_Theme_Preview.this.onBackPressed();
            }
        });
        YoYo.with(Techniques.Bounce).duration(700).repeat(-1).playOn(this.img_reject);
        YoYo.with(Techniques.Bounce).duration(500).repeat(-1).playOn(this.img_recive);

        btn_set_theam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Appmanage.getInstance(Theme_Activity_Calling_Theme_Preview.this).show_INTERSTIAL(Theme_Activity_Calling_Theme_Preview.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        set_theam_click();
                    }
                },sp.getMainCounter(),true);

            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.r.stop();
        finish();
    }

    public void onPause() {
        super.onPause();
        this.r.stop();
    }

    public void onResume() {
        super.onResume();
        this.raw_online_theam_videoview.setVideoURI(Uri.parse(this.folderpath + "/" + getIntent().getStringExtra("theampath") + ".mp4"));
        this.raw_online_theam_videoview.start();
        this.raw_online_theam_videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                Theme_Activity_Calling_Theme_Preview.this.raw_online_theam_videoview.start();
            }
        });
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getActualDefaultRingtoneUri(this, 1));
        this.r = ringtone;
        ringtone.play();
        this.r.setLooping(true);
    }

    public void set_theam_click() {
        SharedPreferences.Editor edit = Theme_Activity_Calling_Theme_Preview.this.sharedpreferences.edit();
        edit.putString("theamvideo", Theme_Activity_Calling_Theme_Preview.this.folderpath + "/" + Theme_Activity_Calling_Theme_Preview.this.getIntent().getStringExtra("theampath") + ".mp4");
        edit.apply();
        Theme_Activity_Calling_Theme_Preview.this.startActivity(new Intent(Theme_Activity_Calling_Theme_Preview.this, Theme_Activity_Calling_Icon_Preview.class));
    }

    public void contect_click(View view) {
        Intent intent = new Intent(this, Theme_Activity_Contect_list.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        int i = this.sharedpreferences.getInt(ansvalue, R.drawable.accept_button);
        int i2 = this.sharedpreferences.getInt(rejvalue, R.drawable.decline_button);
        intent.putExtra("ansicon", i);
        intent.putExtra("rejicon", i2);
        intent.putExtra("theamsvalue", this.folderpath + "/" + getIntent().getStringExtra("theampath") + ".mp4");
        startActivity(intent);
    }

    public void ui() {
        this.img_recive = (ImageView) findViewById(R.id.img_recive);
        this.img_reject = (ImageView) findViewById(R.id.img_reject);
        this.btn_set_theam = (TextView) findViewById(R.id.btn_set_theam);
        this.img_user = (ImageView) findViewById(R.id.img_user);
        this.img_back = (ImageView) findViewById(R.id.img_back);
        this.sharedpreferences = getSharedPreferences("myprefs", 0);
        Help.getheightandwidth(this);
        Help.setHeight(1080);
        Help.setSize(this.img_back, 80, 80, true);
        Help.setSize(this.img_recive, 220, 220, true);
        Help.setSize(this.img_reject, 220, 220, true);
        Help.setSize(this.img_user, 579, 505, true);
    }


}
