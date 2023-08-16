package com.themescreen.flashcolor.stylescreen.Activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Ad_mob_Manager;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Theme_SytemInformation_Activity extends AppCompatActivity {
    long ACTION_PLAY_FROM_MEDIA_ID = 1024;
    Intent batteryIntent;
    ProgressBar f121pb;
    public long free = 0;
    public long total = 0;
    ImageView imgback;
    LinearLayout ad_banner;
    SharePreferencess sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sytem_information);
        TextView textView = (TextView) findViewById(R.id.sd_total);
        imgback=findViewById(R.id.imgback);
        ad_banner=findViewById(R.id.ad_banner);
        sp=new SharePreferencess(this);
        Ad_mob_Manager.showNative(this);
        Ad_mob_Manager.showBanner(this,ad_banner,getWindow());
        StringBuilder sb = new StringBuilder();
        String str = "";
        sb.append(str);
        sb.append(getTotalInternalMemorySize());
        textView.setText(sb.toString());
        TextView textView2 = (TextView) findViewById(R.id.sd_free);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(getAvailableInternalMemorySize());
        textView2.setText(sb2.toString());
        TextView textView3 = (TextView) findViewById(R.id.sdcard_total);
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append(getTotalExternalMemorySize());
        textView3.setText(sb3.toString());
        TextView textView4 = (TextView) findViewById(R.id.sd_used);
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str);
        sb4.append(getAvailableExternalMemorySize());
        textView4.setText(sb4.toString());
        getMemorySize();
        TextView textView5 = (TextView) findViewById(R.id.ram_size);
        StringBuilder sb5 = new StringBuilder();
        sb5.append(str);
        sb5.append(calSize((double) this.total));
        textView5.setText(sb5.toString());
        TextView textView6 = (TextView) findViewById(R.id.ram_used);
        StringBuilder sb6 = new StringBuilder();
        sb6.append(str);
        sb6.append(calSize((double) (this.total - this.free)));
        textView6.setText(sb6.toString());
        TextView textView7 = (TextView) findViewById(R.id.ram_free);
        StringBuilder sb7 = new StringBuilder();
        sb7.append(str);
        sb7.append(calSize((double) this.free));
        textView7.setText(sb7.toString());
        this.batteryIntent = registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        this.f121pb = (ProgressBar) findViewById(R.id.progressBar);
        TextView textView8 = (TextView) findViewById(R.id.batt_level_value);
        StringBuilder sb8 = new StringBuilder();
        sb8.append(str);
        sb8.append(batteryLevel());
        textView8.setText(sb8.toString());
        TextView textView9 = (TextView) findViewById(R.id.batt_status_val);
        StringBuilder sb9 = new StringBuilder();
        sb9.append(str);
        sb9.append(batteryStatus());
        textView9.setText(sb9.toString());
        TextView textView10 = (TextView) findViewById(R.id.batt_health_val);
        StringBuilder sb10 = new StringBuilder();
        sb10.append(str);
        sb10.append(batteryHealth());
        textView10.setText(sb10.toString());
        TextView textView11 = (TextView) findViewById(R.id.batt_temp_val);
        StringBuilder sb11 = new StringBuilder();
        sb11.append(str);
        sb11.append(batteryTemp());
        textView11.setText(sb11.toString());
        TextView textView12 = (TextView) findViewById(R.id.batt_tech_val);
        StringBuilder sb12 = new StringBuilder();
        sb12.append(str);
        sb12.append(batteryTech());
        textView12.setText(sb12.toString());
        TextView textView13 = (TextView) findViewById(R.id.batt_voltage_val);
        StringBuilder sb13 = new StringBuilder();
        sb13.append(str);
        sb13.append(batteryVolt());
        textView13.setText(sb13.toString());
        TextView textView14 = (TextView) findViewById(R.id.system_up_time);
        StringBuilder sb14 = new StringBuilder();
        sb14.append(str);
        sb14.append(uptimeMillis());
        textView14.setText(sb14.toString());
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_SytemInformation_Activity.this).show_INTERSTIAL(Theme_SytemInformation_Activity.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Theme_SytemInformation_Activity.super.onBackPressed();
                    }
                },sp.getBackCountAds(),false);

            }
        });

    }

    private String uptimeMillis() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        StringBuilder sb = new StringBuilder();
        if (elapsedRealtime > 86400000) {
            sb.append(elapsedRealtime / 86400000);
            sb.append(" days ");
            elapsedRealtime %= 86400000;
        }
        if (elapsedRealtime > 3600000) {
            sb.append(elapsedRealtime / 3600000);
            sb.append(" hours ");
            elapsedRealtime %= 3600000;
        }
        if (elapsedRealtime > 60000) {
            sb.append(elapsedRealtime / 60000);
            sb.append(" min. ");
            elapsedRealtime %= 60000;
        }
        if (elapsedRealtime > 1000) {
            sb.append(elapsedRealtime / 1000);
            sb.append(" sec.");
        }
        return sb.toString();
    }

    public String batteryVolt() {
        return String.valueOf(this.batteryIntent.getIntExtra("voltage", -1));
    }

    public String batteryTech() {
        return this.batteryIntent.getExtras().getString("technology");
    }

    public String batteryTemp() {
        StringBuilder sb = new StringBuilder();
        sb.append(((float) this.batteryIntent.getIntExtra("temperature", -1)) / 10.0f);
        sb.append(" *C");
        return sb.toString();
    }

    public String batteryHealth() {
        int intExtra = this.batteryIntent.getIntExtra("health", 1);
        if (intExtra == 2) {
            return "Good";
        }
        if (intExtra == 3) {
            return "Over Heat";
        }
        if (intExtra == 4) {
            return "Dead";
        }
        if (intExtra == 5) {
            return "Over Voltage";
        }
        return intExtra == 6 ? "Unspecified Failure" : "Unknown";
    }

    public String batteryStatus() {
        int intExtra = this.batteryIntent.getIntExtra("plugged", -1);
        if (intExtra == 4) {
            return "WIRELESS Charging";
        }
        if (intExtra != 1) {
            return intExtra != 2 ? "NOT Charging" : "USB Charging";
        }
        return "AC Charging";
    }

    public float batteryLevel() {
        int intExtra = this.batteryIntent.getIntExtra("level", -1);
        int intExtra2 = this.batteryIntent.getIntExtra("scale", -1);
        if (intExtra == -1 || intExtra2 == -1) {
            return 50.0f;
        }
        this.f121pb.setProgress(intExtra);
        return (float) this.f121pb.getProgress();
    }

    private void getMemorySize() {
        Pattern compile = Pattern.compile("([a-zA-Z]+):\\s*(\\d+)");
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("/proc/meminfo", "r");
            while (true) {
                String readLine = randomAccessFile.readLine();
                if (readLine != null) {
                    Matcher matcher = compile.matcher(readLine);
                    if (matcher.find()) {
                        String group = matcher.group(1);
                        String group2 = matcher.group(2);
                        if (group.equalsIgnoreCase("MemTotal")) {
                            this.total = Long.parseLong(group2);
                        } else if (group.equalsIgnoreCase("MemFree") || group.equalsIgnoreCase("SwapFree")) {
                            this.free = Long.parseLong(group2);
                        }
                    }
                } else {
                    randomAccessFile.close();
                    long j = this.total;
                    long j2 = this.ACTION_PLAY_FROM_MEDIA_ID;
                    this.total = j * j2;
                    this.free *= j2;
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String calSize(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double d2 = d / 1048576.0d;
        double d3 = d / 1.073741824E9d;
        double d4 = d / 1.099511627776E12d;
        if (d4 > 1.0d) {
            return decimalFormat.format(d4).concat(" TB");
        }
        if (d3 > 1.0d) {
            return decimalFormat.format(d3).concat(" GB");
        }
        if (d2 > 1.0d) {
            return decimalFormat.format(d2).concat(" MB");
        }
        return decimalFormat.format(d).concat(" KB");
    }

    public String getAvailableExternalMemorySize() {
        if (!externalMemoryAvailable()) {
            return "ERROR";
        }
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return formatSize((double) (((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize())));
    }

    public String getTotalExternalMemorySize() {
        if (!externalMemoryAvailable()) {
            return "ERROR";
        }
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return formatSize((double) (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())));
    }

    public String getAvailableInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return formatSize((double) (((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize())));
    }

    public String getTotalInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return formatSize((double) (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())));
    }

    public boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public String formatSize(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double d2 = d / 1048576.0d;
        double d3 = d / 1.073741824E9d;
        double d4 = d / 1.099511627776E12d;
        if (d4 > 1.0d) {
            return decimalFormat.format(d4).concat(" TB");
        }
        if (d3 > 1.0d) {
            return decimalFormat.format(d3).concat(" GB");
        }
        if (d2 > 1.0d) {
            return decimalFormat.format(d2).concat(" MB");
        }
        return decimalFormat.format(d).concat(" KB");
    }

    @Override
    public void onBackPressed() {
        Appmanage.getInstance(this).show_INTERSTIAL(this, new Appmanage.MyCallback() {
            @Override
            public void callbackCall() {
              Theme_SytemInformation_Activity.super.onBackPressed();
            }
        },sp.getBackCountAds(),false);
    }
}