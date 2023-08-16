package com.themescreen.flashcolor.stylescreen.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.anchorfree.partner.api.data.Country;
import com.anchorfree.partner.api.response.AvailableCountries;
import com.anchorfree.reporting.TrackingConstants;
import com.anchorfree.sdk.SessionConfig;
import com.anchorfree.sdk.SessionInfo;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.sdk.rules.TrafficRule;
import com.anchorfree.vpnsdk.callbacks.Callback;
import com.anchorfree.vpnsdk.callbacks.CompletableCallback;
import com.anchorfree.vpnsdk.compat.CredentialsCompat;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.anchorfree.vpnsdk.transporthydra.HydraTransport;
import com.anchorfree.vpnsdk.vpnservice.VPNState;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Ad_mob_Manager;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;
import com.themescreen.flashcolor.stylescreen.api.VpnDisconnectService;
import com.themescreen.flashcolor.stylescreen.utils.Utils;
import com.themescreen.flashcolor.stylescreen.vpnstatus.Country_Code;
import com.northghost.caketube.OpenVpnTransportConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Theme_Dialog_Activity extends AppCompatActivity {
    private AlertDialog dialog;
    SharePreferencess sp;
    private ProgressDialog dialog_vpn;
    List<Country> list;
    ArrayList<Country_Code> list_country;
    private String selected = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        sp = new SharePreferencess(this);
        dialog_vpn = new ProgressDialog(this);
        dialog_vpn.setMessage("Connecting...");
        dialog_vpn.setCancelable(false);
        SecureBox();
    }

    public void SecureBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.vpn_sequre_info, null);
        TextView turen_btn = view.findViewById(R.id.turen_btn);
        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        turen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turen_btn.setText("Connecting....");
                sp.setSecureBox(true);
                getCountry();
            }
        });
        dialog.show();
    }

    public void getCountry() {
        if (isNetworknotAvalebale()) {
            try {
                UnifiedSDK.getInstance().getBackend().countries(new Callback<AvailableCountries>() {
                    @Override
                    public void success(@NonNull AvailableCountries availableCountries) {
                        try {
                            Utils.list = new ArrayList<>();
                            list_country = new ArrayList<>();
                            list = availableCountries.getCountries();
                            Utils.list = availableCountries.getCountries();
                            if (Utils.list_Country.size() > 0) {
                                for (Country country : Utils.list) {
                                    for (Country_Code code : Utils.list_Country) {
                                        if (country.getCountry().equalsIgnoreCase(code.getCode())) {
                                            Country_Code new_code = new Country_Code();
                                            new_code.setCode(code.getCode());
                                            list_country.add(new_code);
                                        }

                                    }

                                }
                                if (!list_country.isEmpty()) {
                                    selected = getRandomString(list_country);
                                } else {
                                    selected = "";
                                }
                            } else {
                                if (!list.isEmpty()) {
                                    selected = getRandomStringValue(list);
                                } else {
                                    selected = "";
                                }
                            }

                            connectToVpn(selected);
                        } catch (Exception e) {

                            connectToVpn(selected);
                        }

                    }

                    @Override
                    public void failure(@NonNull VpnException e) {
                        startActivity(new Intent(getApplicationContext(), Theme_Activity_VpnErrorScreen.class));
                        dialog_vpn.dismiss();
                        Log.e("Error_Vpn", e.getMessage());
                        // GotoActivity();
                        // connectToVpn("");
                    }
                });
            } catch (Exception e) {
                startActivity(new Intent(getApplicationContext(), Theme_Activity_VpnErrorScreen.class));
                dialog_vpn.dismiss();

            }

        } else {
            dialog_vpn.dismiss();
            // GotoActivity();
            startActivity(new Intent(getApplicationContext(), Theme_Activity_VpnErrorScreen.class));
            Toast.makeText(getApplicationContext(), "No Network Connection Available!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworknotAvalebale() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void connectToVpn(String country) {
        isLoggedIn(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    Log.e("Test_Vpn", "success: logged in");
                    List<String> fallbackOrder = new ArrayList<>();
                    fallbackOrder.add(HydraTransport.TRANSPORT_ID);
                    fallbackOrder.add(OpenVpnTransportConfig.tcp().getName());
                    fallbackOrder.add(OpenVpnTransportConfig.udp().getName());
                    List<String> bypassDomains = new LinkedList<>();
                    UnifiedSDK.getInstance().getVPN().start(new SessionConfig.Builder()
                            .withReason(TrackingConstants.GprReasons.M_UI)
                            .withTransportFallback(fallbackOrder)
                            .withTransport(HydraTransport.TRANSPORT_ID)
                            .withVirtualLocation(country)
                            .addDnsRule(TrafficRule.Builder.bypass().fromDomains(bypassDomains))
                            .build(), new CompletableCallback() {
                        @Override
                        public void complete() {
                            dialog_vpn.dismiss();
                            updateUi();
                            startActivity(new Intent(getApplicationContext(), Theme_Activity_VpnErrorScreen.class));
                            Intent intent = new Intent(Theme_Dialog_Activity.this, VpnDisconnectService.class);
                            startService(intent);
                        }

                        @Override
                        public void error(@NonNull VpnException e) {
                            startActivity(new Intent(getApplicationContext(), Theme_Activity_VpnErrorScreen.class));
                            dialog.dismiss();
                            updateUi();
                        }
                    });

                } else {
                    dialog_vpn.dismiss();
                    startActivity(new Intent(getApplicationContext(), Theme_Activity_VpnErrorScreen.class));
                    dialog.dismiss();
                    updateUi();

                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
                dialog_vpn.dismiss();
                startActivity(new Intent(getApplicationContext(), Theme_Activity_VpnErrorScreen.class));
                Toast.makeText(getApplicationContext(), "Check_Error", Toast.LENGTH_SHORT).show();
                updateUi();
            }
        });
    }

    private void updateUi() {

        UnifiedSDK.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
                if (vpnState == VPNState.CONNECTED) {

                } else {

                }

                switch (vpnState) {
                    case IDLE:
                        // connectionStatus.setText("Status: " + getString(R.string.disconnected));
                        // connect.setImageResource(R.drawable.ic_off);
                        break;
                    case CONNECTED:
                        // connectionStatus.setText("Status: " + getString(R.string.connected));
                        //connect.setImageResource(R.drawable.ic_on);
                        break;
                    case CONNECTING_VPN:
                    case CONNECTING_CREDENTIALS:
                    case CONNECTING_PERMISSIONS:
                        // connectionStatus.setText("Status: " + getString(R.string.connecting));
                        //  connect.setImageResource(R.drawable.ic_off);
                        break;
                    case PAUSED:
                        // connectionStatus.setText("Status: " + getString(R.string.paused));
                        //  connect.setImageResource(R.drawable.ic_off);
                        break;
                    case DISCONNECTING:
                        //  connectionStatus.setText("Status: " + getString(R.string.disconnecting));
                        //  connect.setImageResource(R.drawable.ic_off);
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {


            }
        });
        getCurrentServer(new Callback<String>() {
            @Override
            public void success(@NonNull String s) {
                if (!s.equals("")) {
                }

            }

            @Override
            public void failure(@NonNull VpnException e) {

            }
        });
    }

    protected void getCurrentServer(final Callback<String> callback) {
        UnifiedSDK.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState state) {
                if (state == VPNState.CONNECTED) {
                    UnifiedSDK.getStatus(new Callback<SessionInfo>() {
                        @Override
                        public void success(@NonNull SessionInfo sessionInfo) {
                            callback.success(CredentialsCompat.getServerCountry(sessionInfo.getCredentials()));

                        }

                        @Override
                        public void failure(@NonNull VpnException e) {
                            callback.success("");

                        }
                    });
                } else {
                    callback.success("");

                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
                callback.failure(e);

            }
        });
    }

    protected void isLoggedIn(Callback<Boolean> callback) {
        UnifiedSDK.getInstance().getBackend().isLoggedIn(callback);
    }

    public static void stopVpn() {
        UnifiedSDK.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
                if (vpnState == VPNState.CONNECTED) {
                    UnifiedSDK.getInstance().getVPN().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
                        @Override
                        public void complete() {


                        }

                        @Override
                        public void error(@NonNull VpnException e) {

                        }
                    });
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {

            }
        });
    }

    private String getRandomString(ArrayList<Country_Code> list) {
        int min = 0;
        int max = list.size();
        return list.get(new Random().nextInt(((max - min) + 1) + min)).getCode();
    }

    private String getRandomStringValue(List<Country> list) {
        int min = 0;
        int max = list.size();
        return list.get(new Random().nextInt(((max - min) + 1) + min)).getCountry();
    }

    @Override
    public void onBackPressed() {
        exitApp();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopVpn();
    }
    public void exitApp()
    {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Theme_Dialog_Activity.this);
        View view= LayoutInflater.from(Theme_Dialog_Activity.this).inflate(R.layout.dialog_exit_app,null);
        alertDialog.setView(view);
        AlertDialog dialog=alertDialog.create();
        TextView no_btn=view.findViewById(R.id.tvCancel);
        TextView rate_btn=view.findViewById(R.id.rate_btn);
        TextView yes_btn=view.findViewById(R.id.tvOK);
        LinearLayout ad_native=view.findViewById(R.id.native_ads);
        Ad_mob_Manager.showNativeAd(this,ad_native,true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        rate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.dismiss();
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" +getPackageName())));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_Dialog_Activity.this).show_INTERSTIAL(Theme_Dialog_Activity.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        stopVpn();
                        startActivity(new Intent(getApplicationContext(), Activity_Exit.class));
                        finish();
                    }
                },sp.getMainCounter(),true);

            }
        });


    }
}