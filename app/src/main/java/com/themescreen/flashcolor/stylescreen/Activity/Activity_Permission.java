package com.themescreen.flashcolor.stylescreen.Activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.role.RoleManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.custom_dailor.Activity.Theme_Activity_Main_Dialpad_Selection;
import com.themescreen.flashcolor.stylescreen.utils.Call_Permission;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.judemanutd.autostarter.AutoStartPermissionHelper;

public class Activity_Permission extends AppCompatActivity {
    ImageView img_back;
    ImageView img_basic_premission;
    TextView img_continue_click;
    ImageView img_default_calling_app;
    ImageView img_dnd_click;
    ImageView img_screen_overlay_permission;
    ImageView img_system_setting;
    NotificationManager notificationManagerr;
    SharedPreferences sharedPreferences;
    TelecomManager systemServicee;
    TextView textview_actionbar;

    public void dndclick(View view) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_premisssion);
        this.sharedPreferences = getSharedPreferences("ColorCallFlash", 0);
        this.systemServicee = (TelecomManager) getSystemService(TELECOM_SERVICE);
        this.notificationManagerr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
       /* new Handler().postDelayed(new Runnable() {
            public void run() {
                Activity_Permission.this.checkmipermission();
            }
        }, 500);*/
        this.img_back = (ImageView) findViewById(R.id.img_back);
        this.textview_actionbar = (TextView) findViewById(R.id.textview_actionbar);
        this.img_default_calling_app = (ImageView) findViewById(R.id.img_default_calling_app);
        this.img_continue_click =  findViewById(R.id.img_continue_click);
        this.img_basic_premission = (ImageView) findViewById(R.id.img_basic_premission);
        this.img_system_setting = (ImageView) findViewById(R.id.img_system_setting);
        this.img_screen_overlay_permission = (ImageView) findViewById(R.id.img_screen_overlay_permission);
        this.img_dnd_click = (ImageView) findViewById(R.id.img_dnd_click);
        ui();
        this.img_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Activity_Permission.this.onBackPressed();
            }
        });
        if (new Call_Permission().isappdefault(this)) {
            this.img_default_calling_app.setImageResource(R.drawable.on_btn);
        } else {
            this.img_default_calling_app.setImageResource(R.drawable.off_btn);
        }
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") + ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") + ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS") != 0) {
            this.img_basic_premission.setImageResource(R.drawable.off_btn);
        } else {
            this.img_basic_premission.setImageResource(R.drawable.on_btn);
        }
        if (Settings.System.canWrite(this)) {
            this.img_system_setting.setImageResource(R.drawable.on_btn);
        } else {
            this.img_system_setting.setImageResource(R.drawable.off_btn);
        }
        if (Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(this)) {
            this.img_screen_overlay_permission.setImageResource(R.drawable.on_btn);
        } else {
            this.img_screen_overlay_permission.setImageResource(R.drawable.off_btn);
        }
        if (Build.VERSION.SDK_INT < 23 || this.notificationManagerr.isNotificationPolicyAccessGranted()) {
            this.img_dnd_click.setImageResource(R.drawable.on_btn);
        } else {
            this.img_dnd_click.setImageResource(R.drawable.off_btn);
        }
        this.img_basic_premission.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Activity_Permission.this, "android.permission.WRITE_EXTERNAL_STORAGE") + ContextCompat.checkSelfPermission(Activity_Permission.this, "android.permission.CAMERA") + ContextCompat.checkSelfPermission(Activity_Permission.this, "android.permission.READ_CONTACTS") != 0) {
                    ActivityCompat.requestPermissions(Activity_Permission.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA", "android.permission.ANSWER_PHONE_CALLS", "android.permission.READ_CONTACTS", "android.permission.READ_CALL_LOG", "android.permission.READ_PHONE_STATE", "android.permission.PROCESS_OUTGOING_CALLS", "android.permission.INTERNET"}, 101);
                }
            }
        });
    }

    public void default_calling_app(View view) {
        TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);
        if (telecomManager.getDefaultDialerPackage() == null || telecomManager.getDefaultDialerPackage().equals(getPackageName())) {
            Toast.makeText(this, "Your App Is Already Default", Toast.LENGTH_SHORT).show();
        } else if (Build.VERSION.SDK_INT >= 29) {
            RoleManager roleManager = (RoleManager) getSystemService(RoleManager.class);
            if (!roleManager.isRoleAvailable("android.app.role.DIALER")) {
                return;
            }
            if (roleManager.isRoleHeld("android.app.role.DIALER")) {
                Log.d("role", "role");
            } else {
                startActivityForResult(roleManager.createRequestRoleIntent("android.app.role.DIALER"), 100);
            }
        } else if (Build.VERSION.SDK_INT >= 23 && !getPackageName().equals(telecomManager.getDefaultDialerPackage())) {
            startActivityForResult(new Intent("android.telecom.action.CHANGE_DEFAULT_DIALER").putExtra("android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME", getPackageName()), 100);
        }
    }

    public void setting_click(View view) {
        Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 102);
       /* if (Build.VERSION.SDK_INT >= 23 && !Settings.System.canWrite(this)) {
            Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 102);
        }*/
    }

    public void screen_overay_permission(View view) {
        startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), 103);
       /* if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), 103);
        }*/
    }

    private boolean checkOptimization() {
        if (Build.VERSION.SDK_INT >= 23) {
            String packageName = getApplicationContext().getPackageName();
            PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
            if (powerManager != null && !powerManager.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent();
                intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 101);
                return false;
            }
        }
        return true;
    }

    public void autostartpermsission() {
        if (this.sharedPreferences.getBoolean("permissiondailogcheck", true)) {
            deviceCheck();
        }
    }

    public void deviceCheck() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.checkbox, (ViewGroup) null);
        String string = getSharedPreferences("auto", 0).getString("skipMessage", "NOT checked");
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(inflate);
        ((TextView) inflate.findViewById(R.id.tvptext)).setText("Your device requires additional AUTO START permission to work efficiently.");
        final CheckBox checkBox = (CheckBox) inflate.findViewById(R.id.skip);
        ((TextView) inflate.findViewById(R.id.tvallow)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    SharedPreferences.Editor edit = Activity_Permission.this.sharedPreferences.edit();
                    edit.putBoolean("permissiondailogcheck", false);
                    edit.commit();
                }
                if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(Activity_Permission.this)) {
                    AutoStartPermissionHelper.getInstance().getAutoStartPermission(Activity_Permission.this);
                } else {
                    Help.Toast(Activity_Permission.this, "No Auto Start Option Available");
                }
                dialog.dismiss();
            }
        });
        if (!string.equals("checked")) {
            dialog.show();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100) {
            if (i2 == -1) {
                this.img_default_calling_app.setImageResource(R.drawable.on_btn);
                checkbasicpermission();
            }
            if (i2 == 0) {
                this.img_default_calling_app.setImageResource(R.drawable.off_btn);
                checkbasicpermission();
            }
        } else if (i == 102) {
            if (Build.VERSION.SDK_INT < 23) {
                return;
            }
            if (Settings.System.canWrite(this)) {
                this.img_system_setting.setImageResource(R.drawable.on_btn);
            } else {
                this.img_system_setting.setImageResource(R.drawable.off_btn);
            }
        } else if (i == 103) {
            if (Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(this)) {
                this.img_screen_overlay_permission.setImageResource(R.drawable.on_btn);
            } else {
                this.img_screen_overlay_permission.setImageResource(R.drawable.off_btn);
            }
        } else if (i == 225) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (this.systemServicee.getDefaultDialerPackage().equals(getPackageName())) {
                    this.img_default_calling_app.setImageResource(R.drawable.on_btn);
                } else {
                    this.img_default_calling_app.setImageResource(R.drawable.off_btn);
                }
            }
            if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") + ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") + ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS") != 0) {
                this.img_basic_premission.setImageResource(R.drawable.off_btn);
            } else {
                this.img_basic_premission.setImageResource(R.drawable.on_btn);
            }
            if (Settings.System.canWrite(this)) {
                this.img_system_setting.setImageResource(R.drawable.on_btn);
            } else {
                this.img_system_setting.setImageResource(R.drawable.off_btn);
            }
            if (Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(this)) {
                this.img_screen_overlay_permission.setImageResource(R.drawable.on_btn);
            } else {
                this.img_screen_overlay_permission.setImageResource(R.drawable.off_btn);
            }
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 101) {
            return;
        }
        if (iArr.length <= 0 || iArr[0] != 0) {
            this.img_basic_premission.setImageResource(R.drawable.off_btn);
        } else {
            this.img_basic_premission.setImageResource(R.drawable.on_btn);
        }
    }

    public void next_activiy_click(View view) {
        if (Build.VERSION.SDK_INT < 23) {
            Toast.makeText(this, "Allow all permission", Toast.LENGTH_SHORT).show();
        } else if (this.systemServicee.getDefaultDialerPackage() == null || !this.systemServicee.getDefaultDialerPackage().equals(getPackageName())) {
            Toast.makeText(this, "Allow all permission", Toast.LENGTH_SHORT).show();
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") + ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") + ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS") != 0) {
        } else {
            if (!Settings.System.canWrite(this)) {
                Toast.makeText(this, "Allow all permission", Toast.LENGTH_SHORT).show();
            } else if (Build.VERSION.SDK_INT < 23 || !Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Allow all permission", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
    }

    public void ui() {
        Help.getheightandwidth(this);
        Help.setHeight(1080);
        Help.setSize(this.img_default_calling_app, 138, 78, true);
        Help.setSize(this.img_basic_premission, 138, 78, true);
        Help.setSize(this.img_system_setting, 138, 78, true);
        Help.setSize(this.img_screen_overlay_permission, 138, 78, true);
        Help.setSize(this.img_dnd_click, 138, 78, true);
        Help.setSize(this.img_continue_click, 600, 200, true);
        Help.setSize(findViewById(R.id.divider_one), 1000, 2, true);
        Help.setSize(findViewById(R.id.divider_two), 1000, 2, true);
        Help.setSize(findViewById(R.id.divider_three), 1000, 2, true);
        Help.setSize(findViewById(R.id.divider_four), 1000, 2, true);
    }

    public void onBackPressed() {
        finish();
    }

    public void checkpermission_next_activity() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (this.systemServicee.getDefaultDialerPackage() == null || this.systemServicee.getDefaultDialerPackage().isEmpty() || this.systemServicee.getDefaultDialerPackage().equals("null")) {
            if (Build.VERSION.SDK_INT >= 29) {
                RoleManager roleManager = (RoleManager) getSystemService(RoleManager.class);
                if (!roleManager.isRoleAvailable("android.app.role.DIALER")) {
                    return;
                }
                if (roleManager.isRoleHeld("android.app.role.DIALER")) {
                    Log.d("role", "role");
                } else {
                    startActivityForResult(roleManager.createRequestRoleIntent("android.app.role.DIALER"), 100);
                }
            } else if (Build.VERSION.SDK_INT >= 23 && !getPackageName().equals(this.systemServicee.getDefaultDialerPackage())) {
                startActivityForResult(new Intent("android.telecom.action.CHANGE_DEFAULT_DIALER").putExtra("android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME", getPackageName()), 100);
            }
        } else if (this.systemServicee.getDefaultDialerPackage().equals(getPackageName()) && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") + ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") + ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS") == 0 && Settings.System.canWrite(this) && Build.VERSION.SDK_INT >= 23 && Settings.canDrawOverlays(this)) {
            startActivity(new Intent(this, Theme_Activity_Main_Dialpad_Selection.class));
        }
    }

    public void checkmipermission() {
        String str = Build.MANUFACTURER;
        if (str.equalsIgnoreCase("xiaomi") && Build.VERSION.SDK_INT >= 28) {
            try {
                Toast.makeText(this, "Enable this permission to work application functionality completely", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                intent.putExtra("extra_pkgname", getPackageName());
                startActivityForResult(intent, 225);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (str.equalsIgnoreCase("xiaomi") && Build.VERSION.SDK_INT <= 28) {
            try {
                Toast.makeText(this, "Enable this permission to work application functionality completely", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent2.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent2.putExtra("extra_pkgname", "app_package_name");
                startActivityForResult(intent2, 225);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else if (str.equalsIgnoreCase("mi") && Build.VERSION.SDK_INT >= 28) {
            try {
                Toast.makeText(this, "Enable this permission to work application functionality completely", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent3.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                intent3.putExtra("extra_pkgname", getPackageName());
                startActivityForResult(intent3, 225);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } else if (str.equalsIgnoreCase("mi") && Build.VERSION.SDK_INT <= 28) {
            try {
                Toast.makeText(this, "Enable this permission to work application functionality completely", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent4.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent4.putExtra("extra_pkgname", "app_package_name");
                startActivityForResult(intent4, 225);
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
    }

    public void checkbasicpermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") + ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") + ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS") != 0) {
            this.img_basic_premission.setImageResource(R.drawable.off_btn);
        } else {
            this.img_basic_premission.setImageResource(R.drawable.on_btn);
        }
    }
}
