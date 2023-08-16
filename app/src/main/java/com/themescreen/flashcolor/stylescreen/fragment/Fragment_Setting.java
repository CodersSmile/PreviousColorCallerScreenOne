package com.themescreen.flashcolor.stylescreen.fragment;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.utils.Help;

public class Fragment_Setting extends Fragment {

    public static final String MyPREFERENCES = "myprefs";
    static ImageView caller_theme;
    static ImageView img_privacy_policy;
    static ImageView img_rate;
    static ImageView img_share;
    static ImageView onOffFlashlight;
    String TAG = "Settingfragment";
    Boolean callertheam;
    Boolean flashcheck = false;
    LinearLayout liner_app_event;
    LinearLayout liner_call_flash;
    LinearLayout liner_caller_flash;
    LinearLayout liner_main;
    LinearLayout liner_privacy_policy;
    LinearLayout liner_rate;
    LinearLayout liner_share;
    SharedPreferences sharedpreferences;
    TelecomManager systemServicee;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.activity_setting, viewGroup, false);
        this.sharedpreferences = getContext().getSharedPreferences("myprefs", 0);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.liner_main);
        this.liner_main = linearLayout;
        linearLayout.setVisibility(View.GONE);
        this.liner_call_flash = (LinearLayout) inflate.findViewById(R.id.liner_call_flash);
        this.liner_caller_flash = (LinearLayout) inflate.findViewById(R.id.liner_caller_flash);
        img_share = (ImageView) inflate.findViewById(R.id.img_share);
        img_rate = (ImageView) inflate.findViewById(R.id.img_rate);
        img_privacy_policy = (ImageView) inflate.findViewById(R.id.img_privacy_policy);
        this.liner_share = (LinearLayout) inflate.findViewById(R.id.liner_share);
        this.liner_rate = (LinearLayout) inflate.findViewById(R.id.liner_rate);
        this.liner_privacy_policy = (LinearLayout) inflate.findViewById(R.id.liner_privacy_policy);
        this.liner_app_event = (LinearLayout) inflate.findViewById(R.id.liner_app_event);
        onOffFlashlight = (ImageView) inflate.findViewById(R.id.onOffFlashlight);
        caller_theme = (ImageView) inflate.findViewById(R.id.caller_theme);
        ui();

        if (this.sharedpreferences.getBoolean("flashvalue", false)) {
            onOffFlashlight.setImageResource(R.drawable.on_btn);
            this.flashcheck = true;
        } else {
            onOffFlashlight.setImageResource(R.drawable.off_btn);
            this.flashcheck = false;
        }
        this.systemServicee = (TelecomManager) getContext().getSystemService(Context.TELECOM_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.systemServicee.getDefaultDialerPackage().equals(getContext().getPackageName())) {
                caller_theme.setImageResource(R.drawable.on_btn);
                this.callertheam = true;
            } else {
                caller_theme.setImageResource(R.drawable.off_btn);
                this.callertheam = false;
            }
        }
        caller_theme.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < 23) {
                    return;
                }
                if (Fragment_Setting.this.systemServicee.getDefaultDialerPackage().equals(Fragment_Setting.this.getContext().getPackageName())) {
                    Log.d(Fragment_Setting.this.TAG, String.valueOf(true));
                    Intent intent = new Intent();
                    intent.setAction("android.settings.MANAGE_DEFAULT_APPS_SETTINGS");
                    Fragment_Setting.this.getActivity().startActivityForResult(intent, 114);
                    return;
                }
                TelecomManager telecomManager = (TelecomManager) Fragment_Setting.this.getActivity().getSystemService(Context.TELECOM_SERVICE);
                if (telecomManager.getDefaultDialerPackage().equals(Fragment_Setting.this.getActivity().getPackageName())) {
                    Toast.makeText(Fragment_Setting.this.getActivity(), "Your App Is Already Default", Toast.LENGTH_SHORT).show();
                } else if (Build.VERSION.SDK_INT >= 29) {
                    RoleManager roleManager = (RoleManager) Fragment_Setting.this.getActivity().getSystemService(RoleManager.class);
                    if (roleManager.isRoleAvailable("android.app.role.DIALER") && !roleManager.isRoleHeld("android.app.role.DIALER")) {
                        Fragment_Setting.this.startActivityForResult(roleManager.createRequestRoleIntent("android.app.role.DIALER"), 100);
                    }
                } else if (Build.VERSION.SDK_INT >= 23 && !Fragment_Setting.this.getActivity().getPackageName().equals(telecomManager.getDefaultDialerPackage())) {
                    Intent intent2 = new Intent("android.telecom.action.CHANGE_DEFAULT_DIALER");
                    intent2.putExtra("android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME", Fragment_Setting.this.getActivity().getPackageName());
                    Fragment_Setting.this.startActivityForResult(intent2, 100);
                }
            }
        });
        onOffFlashlight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!Fragment_Setting.this.flashcheck.booleanValue()) {
                    SharedPreferences.Editor edit = Fragment_Setting.this.sharedpreferences.edit();
                    edit.putBoolean("flashvalue", true);
                    edit.apply();
                    Fragment_Setting.onOffFlashlight.setImageResource(R.drawable.on_btn);
                    Fragment_Setting.this.flashcheck = true;
                    return;
                }
                SharedPreferences.Editor edit2 = Fragment_Setting.this.sharedpreferences.edit();
                edit2.putBoolean("flashvalue", false);
                edit2.apply();
                Fragment_Setting.onOffFlashlight.setImageResource(R.drawable.off_btn);
                Fragment_Setting.this.flashcheck = false;
            }
        });
        this.liner_rate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Help.rate(Fragment_Setting.this.getContext());
            }
        });
        this.liner_share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Help.share(Fragment_Setting.this.getContext());
            }
        });
        this.liner_privacy_policy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.privacypolicies.com/live/9f1c82c1-653b-4614-963a-be120a41d44e"));
                startActivity(browserIntent);
            }
        });
        return inflate;
    }

    public void ui() {
        Help.getheightandwidth(getContext());
        Help.setHeight(1080);
        Help.setSize(this.liner_call_flash, 938, 358, true);
        Help.setSize(this.liner_caller_flash, 938, 358, true);
        Help.setSize(img_share, 70, 70, true);
        Help.setSize(img_rate, 70, 70, true);
        Help.setSize(img_privacy_policy, 70, 70, true);
        Help.setSize(onOffFlashlight, 138, 78, true);
        Help.setSize(caller_theme, 138, 78, true);
        Help.setSize(this.liner_app_event, 938, 537, true);

    }

    public boolean changeDefDialer(Activity activity, String str) {
        Intent intent = new Intent("android.telecom.action.CHANGE_DEFAULT_DIALER");
        intent.putExtra("android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME", str);
        getActivity().startActivityForResult(intent, 121);
        return false;
    }

    public String getPackagesOfDialerApps(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DIAL");
        for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(intent, 0)) {
            String str = resolveInfo.activityInfo.applicationInfo.packageName;
            if (!str.equalsIgnoreCase(getContext().getPackageName())) {
                return str;
            }
        }
        return null;
    }
}
