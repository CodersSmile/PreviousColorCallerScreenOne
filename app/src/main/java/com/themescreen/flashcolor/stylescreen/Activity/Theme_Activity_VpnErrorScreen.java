package com.themescreen.flashcolor.stylescreen.Activity;

import static com.themescreen.flashcolor.stylescreen.Activity.Theme_Activity_Splash_screen.stopVpn;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Ad_mob_Manager;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;
import com.themescreen.flashcolor.stylescreen.custom_dailor.Activity.Theme_LetsStartActivity;

public class Theme_Activity_VpnErrorScreen extends AppCompatActivity {
  TextView img_color_call_theme;
  SharePreferencess sp;
  LinearLayout ad_banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpn_error_screen);
        img_color_call_theme=findViewById(R.id.img_color_call_theme);
        ad_banner=findViewById(R.id.ad_banner);
        sp=new SharePreferencess(this);

        Ad_mob_Manager.showNative(this);
        Ad_mob_Manager.showBanner(this,ad_banner,getWindow());
        img_color_call_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_Activity_VpnErrorScreen.this).show_INTERSTIAL(Theme_Activity_VpnErrorScreen.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {

                        if(sp.getScreenFlag()==0)
                        {

                            startActivity(new Intent(getApplicationContext(), Theme_LetsStartActivity.class));
                        }
                        else
                        {

                            startActivity(new Intent(getApplicationContext(), Theme_First_Screen_Activity.class));
                        }
                    }
                },sp.getMainCounter(),true);

            }
        });
    }

    @Override
    public void onBackPressed() {
        ShowExitDialog();
    }

    public void ShowExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_exit_app, null);
        LinearLayout native_ads=inflate.findViewById(R.id.native_ads);
        builder.setView(inflate);
        final AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        create.show();
        Ad_mob_Manager.showNativeAd(this,native_ads,true);
        inflate.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                create.cancel();
            }
        });
        inflate.findViewById(R.id.tvOK).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stopVpn();
                Intent exitIntent = new Intent(getApplicationContext(), Activity_Exit.class);
                startActivity(exitIntent);
            }
        });

    }
}