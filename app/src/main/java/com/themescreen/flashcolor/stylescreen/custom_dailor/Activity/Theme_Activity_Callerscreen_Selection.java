package com.themescreen.flashcolor.stylescreen.custom_dailor.Activity;

import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.themescreen.flashcolor.stylescreen.Activity.Theme_Dialer_Menu_Activity;
import com.themescreen.flashcolor.stylescreen.Activity.Theme_Wallpalerlist_Activity;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Ad_mob_Manager;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;
import com.themescreen.flashcolor.stylescreen.databinding.ActivityCallerscreenSelectionBinding;
import com.themescreen.flashcolor.stylescreen.fragment.Theme_Activity_Calling_Main;
import com.themescreen.flashcolor.stylescreen.utils.Help;

public class Theme_Activity_Callerscreen_Selection extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_DEFAULT_APP = 12345;
    ActivityCallerscreenSelectionBinding binding;
    Context context;
    TelecomManager systemServicee;
    SharePreferencess sp;
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityCallerscreenSelectionBinding inflate = ActivityCallerscreenSelectionBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView(inflate.getRoot());
        this.context = this;
        sp=new SharePreferencess(this);
        Ad_mob_Manager.showBanner(this,binding.adBanner,getWindow());
        Ad_mob_Manager.showNative(this);
        resize();
       systemServicee = (TelecomManager) getSystemService(TELECOM_SERVICE);
       binding.actionbar.imgBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Appmanage.getInstance(Theme_Activity_Callerscreen_Selection.this).show_INTERSTIAL(Theme_Activity_Callerscreen_Selection.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Theme_Activity_Callerscreen_Selection.this.onBackPressed();
                    }
                },sp.getBackCountAds(),false);

            }
        });
        binding.dailerPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_Activity_Callerscreen_Selection.this).show_INTERSTIAL(Theme_Activity_Callerscreen_Selection.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        startActivity(new Intent(getApplicationContext(), Theme_Dialer_Menu_Activity.class));
                    }
                },sp.getMainCounter(),true);

            }
        });
        binding.wallPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_Activity_Callerscreen_Selection.this).show_INTERSTIAL(Theme_Activity_Callerscreen_Selection.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        startActivity(new Intent(getApplicationContext(), Theme_Wallpalerlist_Activity.class));
                    }
                },sp.getMainCounter(),true);

            }
        });
    }

    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (this.systemServicee.getDefaultDialerPackage().equals(getPackageName())) {
            this.binding.imgServiewOnOff.setImageResource(R.drawable.on_btn);
        } else {
            this.binding.imgServiewOnOff.setImageResource(R.drawable.off_btn);
        }
    }

    private void resize() {
        this.binding.actionbar.textviewActionbar.setText("Caller Theme");
        Help.getheightandwidth(this);
        Help.setSize(this.binding.imgServiewOnOff, 138, 78, true);
    }

    public void caller_screen_click(View view) {
       Appmanage.getInstance(this).show_INTERSTIAL(this, new Appmanage.MyCallback() {
           @Override
           public void callbackCall() {
               Help.nextwithnew(Theme_Activity_Callerscreen_Selection.this, Theme_Activity_Calling_Main.class);
           }
       },sp.getMainCounter(),true);
    }

    public void caller_preview_click(View view) {
        Appmanage.getInstance(this).show_INTERSTIAL(this, new Appmanage.MyCallback() {
            @Override
            public void callbackCall() {
                Help.nextwithnew(Theme_Activity_Callerscreen_Selection.this, Theme_Caller_Preview_Activity.class);
            }
        },sp.getMainCounter(),true);

    }

    public void service_on_off(View view) {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (this.systemServicee.getDefaultDialerPackage().equals(getPackageName())) {

            try {
                Intent intent = new Intent();
                intent.setAction("android.settings.MANAGE_DEFAULT_APPS_SETTINGS");
                startActivityForResult(intent, 114);
            } catch (Exception e) {
                Log.e("Akash", "service_on_off:= " + e.getMessage());
            }
        } else {
            TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);
            if (telecomManager.getDefaultDialerPackage().equals(getPackageName())) {
                Toast.makeText(this.context, "Your App Is Already Default", Toast.LENGTH_SHORT).show();
            } else if (Build.VERSION.SDK_INT >= 29) {
                RoleManager roleManager = (RoleManager) getSystemService(RoleManager.class);
                if (roleManager.isRoleAvailable("android.app.role.DIALER") && !roleManager.isRoleHeld("android.app.role.DIALER")) {
                    startActivityForResult(roleManager.createRequestRoleIntent("android.app.role.DIALER"), REQUEST_PERMISSIONS_DEFAULT_APP);
                }
            } else if (Build.VERSION.SDK_INT >= 23 && !getPackageName().equals(telecomManager.getDefaultDialerPackage())) {
                Intent intent2 = new Intent("android.telecom.action.CHANGE_DEFAULT_DIALER");
                intent2.putExtra("android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME", getPackageName());
                startActivityForResult(intent2, REQUEST_PERMISSIONS_DEFAULT_APP);
            }
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == REQUEST_PERMISSIONS_DEFAULT_APP) {
            if (i2 == -1) {
                this.binding.imgServiewOnOff.setImageResource(R.drawable.off_btn);
            }
        } else if (i == 114 && i2 == -1) {
            this.binding.imgServiewOnOff.setImageResource(R.drawable.on_btn);
        }
    }

    @Override
    public void onBackPressed() {
        Appmanage.getInstance(this).show_INTERSTIAL(this, new Appmanage.MyCallback() {
            @Override
            public void callbackCall() {
               Theme_Activity_Callerscreen_Selection.super.onBackPressed();
            }
        },sp.getBackCountAds(),false);

    }
}
