package com.themescreen.flashcolor.stylescreen.custom_dailor.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.themescreen.flashcolor.stylescreen.Activity.Activity_Exit;
import com.themescreen.flashcolor.stylescreen.Activity.Theme_First_Screen_Activity;
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

public class Theme_Activity_Main_Dialpad_Selection extends AppCompatActivity {
    LinearLayout adbanner;
    SharePreferencess sp;
    ProgressDialog dialog_vpn;
    List<Country> list;
    ArrayList<Country_Code> list_country;
    private String selected = "";
    private TextView img_color_call_theme;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main_dailor);
        img_color_call_theme =findViewById(R.id.img_color_call_theme);

        adbanner=findViewById(R.id.ad_banner);
        sp=new SharePreferencess(this);
        Ad_mob_Manager.loadInterstialAds(this);

        sp = new SharePreferencess(this);
        dialog_vpn = new ProgressDialog(this);
        dialog_vpn.setMessage("Connecting...");
        dialog_vpn.setCancelable(false);
        if (sp.getVpnStatus() == 1 && sp.getVpnButtonStatus() == 0) {
            dialog_vpn.show();
            getCountry();

        } else {
            loadVisible();
            sp.setSecureBox(false);
            img_color_call_theme.setVisibility(View.VISIBLE);
        }

        img_color_call_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance (Theme_Activity_Main_Dialpad_Selection.this).show_INTERSTIAL (Theme_Activity_Main_Dialpad_Selection.this, new Appmanage.MyCallback () {
                    public void callbackCall () {
                        if(sp.getScreenFlag()==0)
                        {
                            startActivity(new Intent(getApplicationContext(), Theme_LetsStartActivity.class));

                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(), Theme_First_Screen_Activity.class));

                        }
                    }
                }, sp.getMainCounter(),true);

            }
        });


    }

    public void onBackPressed() {
        ShowExitDialog();
    }

    public void ShowExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_exit_app,null);
        builder.setView(inflate);
        final AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        create.show();
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

                        dialog_vpn.dismiss();
                        Log.e("Error_Vpn", e.getMessage());

                    }
                });
            } catch (Exception e) {
                dialog_vpn.dismiss();

            }

        } else {
            dialog_vpn.dismiss();
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
                            if (sp.getVpnStatus() == 1 && sp.getVpnButtonStatus()==0) {
                                loadVisible();
                            }
                            Intent intent = new Intent(Theme_Activity_Main_Dialpad_Selection.this, VpnDisconnectService.class);
                            startService(intent);
                        }

                        @Override
                        public void error(@NonNull VpnException e) {

                            dialog_vpn.dismiss();

                            updateUi();
                        }
                    });

                } else {
                    dialog_vpn.dismiss();
                    updateUi();

                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
                dialog_vpn.dismiss();
                Toast.makeText(getApplicationContext(), "Check_Error", Toast.LENGTH_SHORT).show();
                // Toast.makeText(VPNActivity.this, "Error while connecting to vpn", Toast.LENGTH_SHORT).show();
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
    public void loadVisible() {
        Ad_mob_Manager.showNative(this);
        Ad_mob_Manager.showBanner(this,adbanner,getWindow());
    }



}
