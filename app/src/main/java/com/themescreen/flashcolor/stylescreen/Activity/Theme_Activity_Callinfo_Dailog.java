package com.themescreen.flashcolor.stylescreen.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.phone_reciever.PhoneCallReceiver;
import com.themescreen.flashcolor.stylescreen.utils.CallManager;
import com.themescreen.flashcolor.stylescreen.utils.ColorCallUtils;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.themescreen.flashcolor.stylescreen.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Theme_Activity_Callinfo_Dailog extends AppCompatActivity {
    private Date callDate;
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aaa", Locale.ENGLISH);
    ImageView img1;
    ImageView imgCall;
    ImageView imgClose;
    ImageView imgMsg;
    CircleImageView imgType;
    ImageView img_miss_call_icon;
    ImageView imgwhts;
    LinearLayout layPop;
    RelativeLayout relative_bottom;
    RelativeLayout relative_close;
    private String strCallDuration;
    private String strCallType;
    private String strName = "";
    private String strNumber;
    TextView tvName;
    TextView tvNumber;
    TextView tvTime;
    TextView tv_duration;



    public void onCreate(Bundle bundle) {
        try {
            super.onCreate(bundle);
            requestWindowFeature(1);
            getWindow().setFlags(1024, 1024);
            setContentView(R.layout.activity_misscall);
            this.context = this;
            ui();
            resize();
            getCallDetail();
            setup();
            click();
        } catch (Exception e) {
            Log.d("missedCall", e.toString());
        }
    }

    public static void call(Context context2, String str) {
        try {
            Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel: " + Uri.encode(str)));
            int simSelection = CallManager.getSimSelection(context2);
            if (simSelection != -1) {
                intent.putExtra("com.android.phone.extra.slot", simSelection);
            }
            context2.startActivity(intent);
        } catch (SecurityException unused) {
            Toast.makeText(context2, "Couldn't make a theme due to security reasons", Toast.LENGTH_LONG).show();
        } catch (NullPointerException unused2) {
            Toast.makeText(context2, "Couldnt make a theme, no phone number", Toast.LENGTH_LONG).show();
        }
    }

    public void simSelectionToDoCall(String str) {
        if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_PHONE_STATE"}, 102);
            return;
        }
        boolean z = false;
        if (SubscriptionManager.from(this.context).getActiveSubscriptionInfoList().size() > 1) {
            z = true;
        }
        Log.e("SimSlot", "simSelectionToDoCall: " + z);
        if (z) {
            try {
                Intent intent = new Intent(this, Activity_SimSelection_Dailogs.class);
                intent.addFlags(268435456);
                intent.addFlags(67108864);
                intent.addFlags(8388608);
                intent.putExtra("type", "miss");
                intent.putExtra("num", str);
                startActivity(intent);
            } catch (SecurityException unused) {
                Toast.makeText(this.context, "Couldn't make a theme due to security reasons", Toast.LENGTH_LONG).show();
            } catch (NullPointerException unused2) {
                Toast.makeText(this.context, "couldn't make a theme, no phone number", Toast.LENGTH_LONG).show();
            }
        } else {
            try {
                Uri.parse("tel: " + Uri.encode(str));
                Intent intent2 = new Intent(this, Activity_SimSelection_Dailogs.class);
                intent2.addFlags(268435456);
                intent2.addFlags(67108864);
                intent2.addFlags(8388608);
                intent2.putExtra("type", "miss");
                intent2.putExtra("num", str);
                startActivity(intent2);
            } catch (Exception unused3) {
                Utils.displayToast(this, "Something When Wrong !");
            }
        }
    }

    private void callBy(int i, String str) {
        try {
            Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel: " + Uri.encode(str)));
            if (i != -1) {
                intent.putExtra("com.android.phone.extra.slot", i);
            }
            this.context.startActivity(intent);
        } catch (SecurityException unused) {
            Toast.makeText(this.context, "Couldn't make a theme due to security reasons", Toast.LENGTH_LONG).show();
        } catch (NullPointerException unused2) {
            Toast.makeText(this.context, "Couldnt make a theme, no phone number", Toast.LENGTH_LONG).show();
        }
    }

    private void click() {
        this.imgCall.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View view) {
                if (ActivityCompat.checkSelfPermission(Theme_Activity_Callinfo_Dailog.this.context, "android.permission.CALL_PHONE") != 0) {
                    ActivityCompat.requestPermissions(Theme_Activity_Callinfo_Dailog.this, new String[]{"android.permission.CALL_PHONE"}, 102);
                    return;
                }
                Theme_Activity_Callinfo_Dailog activity_Callinfo_Dailog = Theme_Activity_Callinfo_Dailog.this;
                activity_Callinfo_Dailog.simSelectionToDoCall(activity_Callinfo_Dailog.strNumber.trim());
                Theme_Activity_Callinfo_Dailog.this.totalDead();
                view.setEnabled(false);
                view.postDelayed(new Runnable() {

                    public void run() {
                        view.setEnabled(true);
                    }
                }, 150);
            }
        });
        this.imgwhts.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String str = "https://api.whatsapp.com/send?phone=" + Theme_Activity_Callinfo_Dailog.this.strNumber;
                try {
                    Theme_Activity_Callinfo_Dailog.this.context.getPackageManager().getPackageInfo("com.whatsapp", 1);
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse(str));
                    Theme_Activity_Callinfo_Dailog.this.startActivity(intent);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(Theme_Activity_Callinfo_Dailog.this.context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                Theme_Activity_Callinfo_Dailog.this.totalDead();
            }
        });
        this.imgMsg.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View view) {
                Theme_Activity_Callinfo_Dailog.this.startActivity(new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + Theme_Activity_Callinfo_Dailog.this.strNumber)));
                Theme_Activity_Callinfo_Dailog.this.totalDead();
                view.setEnabled(false);
                view.postDelayed(new Runnable() {

                    public void run() {
                        view.setEnabled(true);
                    }
                }, 150);
            }
        });
        this.imgClose.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View view) {
                view.setEnabled(false);
                view.postDelayed(new Runnable() {

                    public void run() {
                        view.setEnabled(true);
                    }
                }, 150);
                Theme_Activity_Callinfo_Dailog.this.totalDead();
            }
        });
    }

    private void setup() {
        this.tvName.setText(this.strName);
        this.tvNumber.setText(this.strNumber);
        Date date = this.callDate;
        if (date == null) {
            this.tvTime.setText(new SimpleDateFormat("KK:mm a").format(Calendar.getInstance().getTime()));
        } else {
            this.tvTime.setText(this.dateFormat.format(date));
        }
        Log.d("theme", "setup start " + this.strNumber);
        if (TextUtils.isEmpty(this.strNumber)) {
            this.tvName.setText("Unknown User");
            this.imgCall.setVisibility(View.GONE);
            this.imgMsg.setVisibility(View.GONE);
            Log.d("theme", "number  empty " + this.strNumber);
            return;
        }
        if (!TextUtils.isEmpty(this.strName)) {
            this.tvName.setText(this.strName);
            Log.d("theme", "number  1 " + this.strNumber);
        } else {
            this.tvName.setText("" + this.strNumber);
            Log.d("theme", "number  2 " + this.strNumber);
        }
        Log.e("MisscallActvity", "setup: " + this.strCallDuration);
        Log.e("MisscallActvity", "setup: " + this.strCallType);
        this.tv_duration.setText("" + this.strCallDuration);
        if (this.strCallType.contains("Missed")) {
            this.tv_duration.setText("Missed");
        }
    }

    private void getCallDetail() {
        long longExtra = getIntent().getLongExtra(PhoneCallReceiver.START_STATE, 0);
        long longExtra2 = getIntent().getLongExtra(PhoneCallReceiver.END_STATE, 0);
        if (longExtra == 0 && longExtra2 == 0) {
            this.callDate = null;
        } else {
            this.callDate = new Date(longExtra);
        }
        String stringExtra = getIntent().getStringExtra(PhoneCallReceiver.NUMBER_STATE);
        this.strNumber = stringExtra;
        Log.d("theme", "get Data " + this.strNumber);
        Bitmap retrieveContactPhoto = ColorCallUtils.retrieveContactPhoto(this, stringExtra);
        if (retrieveContactPhoto != null) {
            Glide.with(this.context).load(retrieveContactPhoto).into(this.imgType);
        }
        String contactName = Utils.getContactName(this.context, stringExtra);
        if (!TextUtils.isEmpty(contactName)) {
            this.strName = contactName;
        } else {
            this.strName = null;
        }
        int intExtra = getIntent().getIntExtra(PhoneCallReceiver.STATE, 0);
        if (intExtra == 2) {
            this.strCallType = "Call End";
            this.strCallDuration = getTimeFromSeconds((longExtra2 - longExtra) / 1000);
        } else if (intExtra != 3) {
            this.strCallType = "Call End";
            this.strCallDuration = getTimeFromSeconds((longExtra2 - longExtra) / 1000);
        } else {
            this.strCallType = "Missed theme";
            this.strCallDuration = getTimeFromSeconds((longExtra2 - longExtra) / 1000);
        }
    }

    private String getTimeFromSeconds(long j) {
        try {
            int i = (int) (j / 3600);
            int i2 = (int) ((j % 3600) / 60);
            int i3 = (int) (j % 60);
            if (i > 0) {
                return String.format("%02d:%02d:%02d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
            }
            return String.format("%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i3));
        } catch (Exception unused) {
            return "00:00";
        }
    }

    private void resize() {
        Help.getheightandwidth(this);
        Help.setSize(this.layPop, 864, 618, true);
        Help.setSize(this.imgType, 217, 217, true);
        Help.setSize(this.img_miss_call_icon, 40, 40, true);
        Help.setSize(this.imgCall, 275, 146, true);
        Help.setSize(this.imgMsg, 275, 146, true);
        Help.setSize(this.imgwhts, 275, 146, true);
        Help.setSize(this.relative_close, 866, 67, true);
        Help.setSize(this.imgClose, 67, 67, true);
        Help.setSize(this.relative_bottom, 864, 146, true);
    }

    private void ui() {
        this.tv_duration = (TextView) findViewById(R.id.tv_duration);
        this.tvName = (TextView) findViewById(R.id.tvName);
        this.tvNumber = (TextView) findViewById(R.id.tvNumber);
        this.tvTime = (TextView) findViewById(R.id.tvTime);
        this.layPop = (LinearLayout) findViewById(R.id.layPop);
        this.relative_bottom = (RelativeLayout) findViewById(R.id.relative_bottom);
        this.imgType = (CircleImageView) findViewById(R.id.imgType);
        this.img_miss_call_icon = (ImageView) findViewById(R.id.img_miss_call_icon);
        this.imgCall = (ImageView) findViewById(R.id.imgCall);
        this.imgwhts = (ImageView) findViewById(R.id.imgwhts);
        this.imgMsg = (ImageView) findViewById(R.id.imgMsg);
        this.relative_close = (RelativeLayout) findViewById(R.id.relative_close);
        this.imgClose = (ImageView) findViewById(R.id.imgClose);
        new IntentFilter().addAction("com.hello.missedCall");
    }


    public void onBackPressed() {
        totalDead();
    }



    public void onPause() {
        super.onPause();
        totalDead();
    }



    public void onStop() {
        super.onStop();
    }


    public void totalDead() {
        try {
            System.gc();
        } catch (Exception unused) {
        }
        if (Build.VERSION.SDK_INT >= 21) {
            finishAffinity();
            finishAndRemoveTask();
            return;
        }
        finish();
    }



    public void onDestroy() {
        super.onDestroy();
    }

    public void finish() {
        finishAndRemoveTask();
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 102) {
            if (Build.VERSION.SDK_INT >= 22) {
                simSelectionToDoCall(getIntent().getStringExtra("incomingNumberr"));
            }
            finish();
        }
    }
}
