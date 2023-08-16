package com.themescreen.flashcolor.stylescreen.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.themescreen.flashcolor.stylescreen.Adapter.Adapter_calling_icon_preview;
import com.themescreen.flashcolor.stylescreen.Model.CallReceiveRejectCall;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Ad_mob_Manager;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;
import com.themescreen.flashcolor.stylescreen.fragment.Theme_Activity_Calling_Main;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.themescreen.flashcolor.stylescreen.utils.Utils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

public class Theme_Activity_Calling_Icon_Preview extends AppCompatActivity implements Adapter_calling_icon_preview.Adapterclick {

    public static String ansvalue = "ansicon";
    public static String individualrecivevalue = "individualreciveicon";
    public static String individualrejectvalue = "individualrejecticon";
    public static String rejvalue = "reicon";
    public static final String value = "theamvideo";
    String activity;
    String activitycheck;
    Adapter_calling_icon_preview adapterCallingiconpreview;
    ImageView avtar_text;
    ArrayList<CallReceiveRejectCall> callReceiveRejectCalls = new ArrayList<>();
    String folderpath;
    ImageView img_back;
    TextView img_done;
    ImageView img_recive_demo;
    ImageView img_rej_demo;
    ImageView img_user;
    ImageView img_user_pic_border;
    RecyclerView reject_call_recycle;
    RelativeLayout relative_videoview;
    SharedPreferences sharedpreferences;
    VideoView video_theam_preview;
    LinearLayout ad_banner;
    SharePreferencess sp;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_callicon_preview);
        ad_banner=findViewById(R.id.ad_banner);
        sp=new SharePreferencess(this);
        ui();
        Ad_mob_Manager.showBanner(this,ad_banner,getWindow());
        this.sharedpreferences = getSharedPreferences("myprefs", 0);
        YoYo.with(Techniques.Bounce).duration(700).repeat(-1).playOn(this.img_recive_demo);
        YoYo.with(Techniques.Bounce).duration(500).repeat(-1).playOn(this.img_rej_demo);
    }

    public void onResume() {
        super.onResume();
        String stringExtra = getIntent().getStringExtra("activityindividual");
        this.activity = stringExtra;
        if (stringExtra == null || stringExtra.isEmpty()) {
            this.activitycheck = "allcotacts";
            int i = this.sharedpreferences.getInt(ansvalue, R.drawable.accept_button_three);
            int i2 = this.sharedpreferences.getInt(rejvalue, R.drawable.decline_button_three);
            this.img_recive_demo.setImageResource(i);
            this.img_rej_demo.setImageResource(i2);
            this.video_theam_preview.setVideoURI(Uri.parse(this.sharedpreferences.getString("theamvideo", "android.resource://" + getPackageName() + "/" + R.raw.asset_theme1)));
            this.video_theam_preview.start();
            this.video_theam_preview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Theme_Activity_Calling_Icon_Preview.this.video_theam_preview.start();
                }
            });
        } else {
            this.activitycheck = "individual";
            int i3 = this.sharedpreferences.getInt(individualrecivevalue, R.drawable.accept_button_three);
            int i4 = this.sharedpreferences.getInt(individualrejectvalue, R.drawable.decline_button_three);
            this.img_recive_demo.setImageResource(i3);
            this.img_rej_demo.setImageResource(i4);
            this.video_theam_preview.setVideoURI(Uri.parse(getIntent().getStringExtra("theamsvalue")));
            this.video_theam_preview.start();
            this.video_theam_preview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mediaPlayer) {
                    Theme_Activity_Calling_Icon_Preview.this.video_theam_preview.start();
                }
            });
        }
        this.img_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Theme_Activity_Calling_Icon_Preview.this.onBackPressed();
            }
        });
        this.callReceiveRejectCalls.add(new CallReceiveRejectCall(R.drawable.accept_button_one, R.drawable.decline_button_one));
        this.callReceiveRejectCalls.add(new CallReceiveRejectCall(R.drawable.accept_button_two, R.drawable.decline_button_two));
        this.callReceiveRejectCalls.add(new CallReceiveRejectCall(R.drawable.accept_button_three, R.drawable.decline_button_three));
        this.callReceiveRejectCalls.add(new CallReceiveRejectCall(R.drawable.accept_button_four, R.drawable.decline_button_four));
        this.callReceiveRejectCalls.add(new CallReceiveRejectCall(R.drawable.accept_button_five, R.drawable.decline_button_five));
        this.callReceiveRejectCalls.add(new CallReceiveRejectCall(R.drawable.accept_button_six, R.drawable.decline_button_six));
        this.callReceiveRejectCalls.add(new CallReceiveRejectCall(R.drawable.accept_button_seven, R.drawable.decline_button_seven));
        this.callReceiveRejectCalls.add(new CallReceiveRejectCall(R.drawable.accept_button_eight, R.drawable.decline_button_eight));
        this.callReceiveRejectCalls.add(new CallReceiveRejectCall(R.drawable.accept_button_nine, R.drawable.decline_button_nine));
        this.callReceiveRejectCalls.add(new CallReceiveRejectCall(R.drawable.accept_button_ten, R.drawable.decline_button_ten));
        this.callReceiveRejectCalls.add(new CallReceiveRejectCall(R.drawable.accept_button_eleven, R.drawable.decline_button_eleven));
        this.callReceiveRejectCalls.add(new CallReceiveRejectCall(R.drawable.accept_button_twelve, R.drawable.decline_button_twelve));
        this.callReceiveRejectCalls.add(new CallReceiveRejectCall(R.drawable.accept_button_thirteen, R.drawable.decline_button_thirteen));
        this.callReceiveRejectCalls.add(new CallReceiveRejectCall(R.drawable.accept_button_fourteen, R.drawable.decline_button_fourteen));
        this.callReceiveRejectCalls.add(new CallReceiveRejectCall(R.drawable.accept_button_fiftyteen, R.drawable.decline_button_fiftyteen));
        this.reject_call_recycle.setLayoutManager(new LinearLayoutManager(this));
        Adapter_calling_icon_preview adapter_calling_icon_preview = new Adapter_calling_icon_preview(this.callReceiveRejectCalls, this, this, this.activitycheck);
        this.adapterCallingiconpreview = adapter_calling_icon_preview;
        this.reject_call_recycle.setAdapter(adapter_calling_icon_preview);
    }

    public String iconclick() {
        String stringExtra = getIntent().getStringExtra("activityindividual");
        if (stringExtra == null || stringExtra.isEmpty()) {
            int i = this.sharedpreferences.getInt(ansvalue, R.drawable.accept_button);
            int i2 = this.sharedpreferences.getInt(rejvalue, R.drawable.decline_button);
            this.img_recive_demo.setImageResource(i);
            this.img_rej_demo.setImageResource(i2);
            return null;
        }
        int i3 = this.sharedpreferences.getInt(individualrecivevalue, R.drawable.accept_button);
        int i4 = this.sharedpreferences.getInt(individualrejectvalue, R.drawable.decline_button);
        this.img_recive_demo.setImageResource(i3);
        this.img_rej_demo.setImageResource(i4);
        return null;
    }

    public void ui() {
        this.img_back = (ImageView) findViewById(R.id.img_back);
        this.relative_videoview = (RelativeLayout) findViewById(R.id.relative_videoview);
        this.video_theam_preview = (VideoView) findViewById(R.id.video_theam_preview);
        this.img_user_pic_border = (ImageView) findViewById(R.id.img_user_pic_border);
        this.img_user = (ImageView) findViewById(R.id.img_user);
        this.avtar_text = (ImageView) findViewById(R.id.avtar_text);
        this.img_recive_demo = (ImageView) findViewById(R.id.img_recive_demo);
        this.img_rej_demo = (ImageView) findViewById(R.id.img_rej_demo);
        this.reject_call_recycle = (RecyclerView) findViewById(R.id.reject_call_recycle);
        this.img_done = (TextView) findViewById(R.id.img_done);
        Help.getheightandwidth(this);
        Help.setHeight(1080);
        Help.setSize(this.relative_videoview, 643, 1000, true);
        Help.setSize(this.video_theam_preview, 643, 1000, true);
        Help.setSize(this.img_user_pic_border, 170, 170, true);
        Help.setSize(this.img_user, 170, 170, true);
        Help.setSize(this.avtar_text, 314, 88, true);
        Help.setSize(this.img_recive_demo, 135, 135, true);
        Help.setSize(this.img_rej_demo, 135, 135, true);
    }

    public void done_click(View view) {
        if (Utils.counter != Utils.totalClick) {
            Utils.counter++;
            if (this.activitycheck.equals("allcotacts")) {
                Toast.makeText(getApplicationContext(), "Set SuccessFully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Theme_Activity_Calling_Main.class));
                return;
            }
            int i2 = sharedpreferences.getInt(individualrecivevalue, R.drawable.accept_button_three);
            int i3 = sharedpreferences.getInt(individualrejectvalue, R.drawable.decline_button_three);
            Intent intent = new Intent(this, Theme_Activity_Contect_list.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("ansicon", i2);
            intent.putExtra("rejicon", i3);
            intent.putExtra("theamsvalue", getIntent().getStringExtra("theamsvalue"));
            startActivity(intent);
        } else if (Utils.counter == Utils.totalClick) {
            if (activitycheck.equals("allcotacts")) {
                Toast.makeText(getApplicationContext(), "Set SuccessFully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Theme_Activity_Calling_Main.class));
                return;
            }
            int i2 = sharedpreferences.getInt(individualrecivevalue, R.drawable.accept_button_three);
            int i3 = sharedpreferences.getInt(individualrejectvalue, R.drawable.decline_button_three);
            Intent intent = new Intent(getApplicationContext(), Theme_Activity_Contect_list.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("ansicon", i2);
            intent.putExtra("rejicon", i3);
            intent.putExtra("theamsvalue", getIntent().getStringExtra("theamsvalue"));
            startActivity(intent);
        }
    }

    public void onBackPressed() {
        Appmanage.getInstance(this).show_INTERSTIAL(this, new Appmanage.MyCallback() {
            @Override
            public void callbackCall() {
                Theme_Activity_Calling_Icon_Preview.super.onBackPressed();
            }
        },sp.getBackCountAds(),false);

    }


}
