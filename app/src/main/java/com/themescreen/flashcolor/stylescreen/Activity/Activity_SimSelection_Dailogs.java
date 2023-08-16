package com.themescreen.flashcolor.stylescreen.Activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.Service.CallService;
import com.themescreen.flashcolor.stylescreen.utils.Help;

import java.util.List;

public class Activity_SimSelection_Dailogs extends AppCompatActivity {
    Context context;
    BroadcastReceiver receiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            Activity_SimSelection_Dailogs.this.finish();
        }
    };
    String stringExtra2;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_sim_selection);
        this.context = this;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.sim_selection.action");
        registerReceiver(this.receiver, intentFilter);
        isSimAvailable(this, getIntent().getStringExtra("num"));
    }

    private void createDialog() {
        final Dialog dialog = new Dialog(this.context);
        dialog.setContentView(dialog.getLayoutInflater().inflate(R.layout.lay_sim_selection, (ViewGroup) null));
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.liner_sim1);
        LinearLayout linearLayout2 = (LinearLayout) dialog.findViewById(R.id.liner_sim2);
        LinearLayout linearLayout3 = (LinearLayout) dialog.findViewById(R.id.liner_sim_one);
        LinearLayout linearLayout4 = (LinearLayout) dialog.findViewById(R.id.liner_sim_two);
        TextView textView = (TextView) dialog.findViewById(R.id.btn1);
        TextView textView2 = (TextView) dialog.findViewById(R.id.btn2);
        SubscriptionManager subscriptionManager = (SubscriptionManager) getSystemService("telephony_subscription_service");
        Help.getheightandwidth(this);
        Help.setSize((LinearLayout) dialog.findViewById(R.id.lay_sim), 868, 708, true);
        Help.setSize((ImageView) dialog.findViewById(R.id.img_sim_icon1), 120, 120, true);
        Help.setSize((ImageView) dialog.findViewById(R.id.img_sim_icon2), 120, 120, true);
        Help.setSize(linearLayout3, 298, 298, true);
        Help.setSize(linearLayout4, 298, 298, true);
        Help.setSize((ImageView) dialog.findViewById(R.id.imageView2), 200, 200, true);
        try {
            if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") != 0) {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_PHONE_STATE"}, 102);
            }
            SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(0);
            String str = "";
            String str2 = activeSubscriptionInfoForSimSlotIndex != null ? (String) activeSubscriptionInfoForSimSlotIndex.getCarrierName() : str;
            SubscriptionInfo activeSubscriptionInfoForSimSlotIndex2 = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(1);
            if (activeSubscriptionInfoForSimSlotIndex2 != null) {
                str = (String) activeSubscriptionInfoForSimSlotIndex2.getCarrierName();
            }
            textView.setText(str2);
            textView2.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        linearLayout3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Activity_Calling_InComing_Outgoing.sim_id = 1;
                Activity_Calling_InComing_Outgoing.from_call_or_not = true;
                String stringExtra = Activity_SimSelection_Dailogs.this.getIntent().getStringExtra("type");
                String stringExtra2 = Activity_SimSelection_Dailogs.this.getIntent().getStringExtra("num");
                if (TextUtils.isEmpty(stringExtra) || !stringExtra.equals("miss")) {
                    if (Activity_SimSelection_Dailogs.this.getIntent().getBooleanExtra("checksim", false)) {
                        Activity_SimSelection_Dailogs activity_SimSelection_Dailogs = Activity_SimSelection_Dailogs.this;
                        activity_SimSelection_Dailogs.call(activity_SimSelection_Dailogs.context, stringExtra2, 0);
                    } else {
                        CallService.doPhoneHandling(Activity_SimSelection_Dailogs.this, 0);
                    }
                } else if (!TextUtils.isEmpty(stringExtra2) && !stringExtra2.equals("")) {
                    Activity_SimSelection_Dailogs activity_SimSelection_Dailogs2 = Activity_SimSelection_Dailogs.this;
                    activity_SimSelection_Dailogs2.call(activity_SimSelection_Dailogs2.context, stringExtra2, 0);
                }
                dialog.dismiss();
                Activity_SimSelection_Dailogs.this.finish();
            }
        });
        linearLayout4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Activity_Calling_InComing_Outgoing.sim_id = 2;
                Activity_Calling_InComing_Outgoing.from_call_or_not = true;
                Intent intent = Activity_SimSelection_Dailogs.this.getIntent();
                String stringExtra = intent.getStringExtra("type");
                String stringExtra2 = intent.getStringExtra("num");
                if (TextUtils.isEmpty(stringExtra) || !stringExtra.equals("miss")) {
                    if (Activity_SimSelection_Dailogs.this.getIntent().getBooleanExtra("checksim", false)) {
                        Activity_SimSelection_Dailogs activity_SimSelection_Dailogs = Activity_SimSelection_Dailogs.this;
                        activity_SimSelection_Dailogs.call(activity_SimSelection_Dailogs.context, stringExtra2, 1);
                    } else {
                        CallService.doPhoneHandling(Activity_SimSelection_Dailogs.this, 1);
                    }
                } else if (!TextUtils.isEmpty(stringExtra2) && !stringExtra2.equals("")) {
                    Activity_SimSelection_Dailogs activity_SimSelection_Dailogs2 = Activity_SimSelection_Dailogs.this;
                    activity_SimSelection_Dailogs2.call(activity_SimSelection_Dailogs2.context, stringExtra2, 1);
                }
                dialog.dismiss();
                Activity_SimSelection_Dailogs.this.finish();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialogInterface) {
                CallService.unResigterCallback();
                Activity_SimSelection_Dailogs.this.finish();
            }
        });
        dialog.show();
    }

    public void call(Context context2, String str, int i) {
        Log.e("Call", "theme: " + str + "i=>" + i);
        if (ActivityCompat.checkSelfPermission(this, "android.permission.CALL_PHONE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CALL_PHONE"}, 101);
            return;
        }
        try {
            Uri parse = Uri.parse("tel: " + Uri.encode(str));
            Intent intent = getIntent();
            if (i != -1) {
                intent.putExtra("com.android.phone.extra.slot", i);
            }
            TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);
            List<PhoneAccountHandle> list = null;
            if (Build.VERSION.SDK_INT >= 23) {
                list = telecomManager.getCallCapablePhoneAccounts();
            }
            if (list.size() > 0) {
                if (i >= list.size()) {
                    i = 0;
                }
                PhoneAccountHandle phoneAccountHandle = list.get(i);
                if (Build.VERSION.SDK_INT >= 23) {
                    intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandle);
                }
                TelecomManager telecomManager2 = (TelecomManager) getSystemService(TELECOM_SERVICE);
                if (telecomManager2 == null) {
                    this.context.startActivity(intent);
                } else if (Build.VERSION.SDK_INT >= 23) {
                    telecomManager2.placeCall(parse, intent.getExtras());
                }
            } else {
                Toast.makeText(this.context, "No Sim Detect", Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException unused) {
            Toast.makeText(this.context, "Couldn't make a theme due to security reasons", Toast.LENGTH_LONG).show();
        } catch (NullPointerException unused2) {
            Toast.makeText(this.context, "Couldnt make a theme, no phone number", Toast.LENGTH_LONG).show();
        }
    }


    public void onBackPressed() {
        finish();
    }


    public void onPause() {
        super.onPause();
        finish();
    }


    public void onStop() {
        super.onStop();
        finish();
    }


    public void onDestroy() {
        unregisterReceiver(this.receiver);
        super.onDestroy();
    }

    public void finish() {
        if (Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 102 && i == 101 && iArr[0] == 0) {
            call(this.context, this.stringExtra2, 0);
        }
    }

    public void isSimAvailable(Context context2, String str) {
        if (Build.VERSION.SDK_INT >= 22) {
            SubscriptionManager subscriptionManager = (SubscriptionManager) context2.getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") == 0) {
                SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(0);
                SubscriptionInfo activeSubscriptionInfoForSimSlotIndex2 = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(1);
                if (activeSubscriptionInfoForSimSlotIndex != null && activeSubscriptionInfoForSimSlotIndex2 != null) {
                    createDialog();
                } else if (activeSubscriptionInfoForSimSlotIndex != null && activeSubscriptionInfoForSimSlotIndex2 == null) {
                    call(context2, str, 0);
                } else if (activeSubscriptionInfoForSimSlotIndex2 != null && activeSubscriptionInfoForSimSlotIndex == null) {
                    call(context2, str, 1);
                } else if (activeSubscriptionInfoForSimSlotIndex == null || activeSubscriptionInfoForSimSlotIndex2 == null) {
                    Toast.makeText(context2, "No Sim Detect", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    finish();
                }
            }
        } else {
            finish();
        }
    }
}
