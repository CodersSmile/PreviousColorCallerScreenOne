package com.themescreen.flashcolor.stylescreen.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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
import com.bumptech.glide.Glide;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Ad_mob_Manager;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;
import com.themescreen.flashcolor.stylescreen.api.VpnDisconnectService;
import com.themescreen.flashcolor.stylescreen.utils.Utils;
import com.themescreen.flashcolor.stylescreen.vpnstatus.CountryModal;
import com.themescreen.flashcolor.stylescreen.vpnstatus.Country_Code;
import com.themescreen.flashcolor.stylescreen.vpnstatus.RegionChooserDialog;
import com.northghost.caketube.OpenVpnTransportConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Theme_Vpn_Activity extends AppCompatActivity implements RegionChooserDialog.RegionChooserInterface  {
    public static String TAG="Vpn_ERRORE";
    TextView start_btn,text_country,skip_btn,get_start_btn,stop_btn;
    ImageView image_icon;
    RelativeLayout select_country;
    ProgressDialog dialog;
    SharePreferencess sp;
    private String selected = "";
    List<Country> list;
    ArrayList<Country_Code> list_country;
    LinearLayout ad_banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpn);
        start_btn=findViewById(R.id.start_btn);
        text_country=findViewById(R.id.text_country);
        image_icon=findViewById(R.id.image_icon);
        skip_btn=findViewById(R.id.skip_btn);
        select_country=findViewById(R.id.select_country);
        get_start_btn=findViewById(R.id.get_start_btn);
        stop_btn=findViewById(R.id.stop_btn);
        ad_banner=findViewById(R.id.ad_banner);
        sp=new SharePreferencess(this);
        Ad_mob_Manager.loadInterstialAds(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting...");
        dialog.setCancelable(false);
        Ad_mob_Manager.showBanner(Theme_Vpn_Activity.this,ad_banner,getWindow());
        select_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegionChooserDialog regionChooserDialog = RegionChooserDialog.newInstance(Theme_Vpn_Activity.this);
                regionChooserDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
                regionChooserDialog.show(getSupportFragmentManager(), RegionChooserDialog.TAG);
            }
        });
        if (sp.getVpnClose() == 1) {
            skip_btn.setVisibility(View.VISIBLE);
        } else {
            skip_btn.setVisibility(View.GONE);
        }
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifiedSDK.getVpnState(new Callback<VPNState>() {
                    @Override
                    public void success(@NonNull VPNState vpnState) {
                        dialog.show();
                        updateUi();
                        if (vpnState == VPNState.CONNECTED) {
                            disconnectFromVpn();
                        } else if (vpnState == VPNState.CONNECTING_VPN) {
                            Toast.makeText(Theme_Vpn_Activity.this, "Please wait...\n we are connecting to vpn", Toast.LENGTH_SHORT).show();
                        } else {

                            getCountry();
                        }
                    }

                    @Override
                    public void failure(@NonNull VpnException e) {
                        getCountry();
                    }
                });
            }
        });
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopVpn();
                get_start_btn.setVisibility(View.GONE);
                start_btn.setVisibility(View.VISIBLE);
                stop_btn.setVisibility(View.GONE);
            }
        });

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_Vpn_Activity.this).show_INTERSTIAL(Theme_Vpn_Activity.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        startActivity(new Intent(Theme_Vpn_Activity.this, Theme_Activity_VpnErrorScreen.class));
                    }
                },sp.getMainCounter(),true);

            }
        });
        get_start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_Vpn_Activity.this).show_INTERSTIAL(Theme_Vpn_Activity.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        startActivity(new Intent(getApplicationContext(), Theme_Activity_VpnErrorScreen.class));
                    }
                },sp.getMainCounter(),true);


            }
        });
    }

    private void updateUi() {

        if (sp.getVpnClose() == 1) {
            skip_btn.setVisibility(View.VISIBLE);
        } else {
            skip_btn.setVisibility(View.GONE);
        }

        UnifiedSDK.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
                if (vpnState == VPNState.CONNECTED && sp.getVpnButtonStatus() == 1) {

                } else {

                }
                switch (vpnState) {
                    case IDLE:
                        // tvConnectionStatus.setText("Status: " + getString(R.string.disconnected));
                        start_btn.setVisibility(View.VISIBLE);
                        stop_btn.setVisibility(View.GONE);
                        break;
                    case CONNECTED:
                        // tvConnectionStatus.setText("Status: " + getString(R.string.connected));
                        start_btn.setVisibility(View.GONE);
                        stop_btn.setVisibility(View.VISIBLE);
                        get_start_btn.setVisibility(View.VISIBLE);
                        break;
                    case CONNECTING_VPN:
                    case CONNECTING_CREDENTIALS:
                    case CONNECTING_PERMISSIONS:
                        start_btn.setVisibility(View.GONE);
                        stop_btn.setVisibility(View.VISIBLE);
                        break;
                    case PAUSED:
                        start_btn.setVisibility(View.GONE);
                        stop_btn.setVisibility(View.VISIBLE);
                        break;
                    case DISCONNECTING:
                        start_btn.setVisibility(View.GONE);
                        stop_btn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
                dialog.dismiss();
                get_start_btn.setVisibility(View.VISIBLE);
            }
        });

        getCurrentServer(new Callback<String>() {
            @Override
            public void success(@NonNull String s) {
                if (!s.equals("")) {
                    CountryModal country = Utils.getCountryFlagPath(selected, Theme_Vpn_Activity.this);
                    if (country != null) {
                        Glide.with(Theme_Vpn_Activity.this).load(country.getPathFlag()).into(image_icon);
                        text_country.setText(country.getName());
                    }
                }

            }

            @Override
            public void failure(@NonNull VpnException e) {
                dialog.dismiss();
            }
        });


    }

    public void getCountry() {
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
                                        Log.e("Country_Size", "Service_List " + code.getCode());
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
                        Log.e("test", "Error " + e.getMessage());
                        connectToVpn("");
                    }

                }

                @Override
                public void failure(@NonNull VpnException e) {
                    Log.e("test", "Vpn Error " + e.getMessage());
                    connectToVpn("");
                }
            });
        } catch (Exception e) {
            get_start_btn.setVisibility(View.VISIBLE);
        }

    }

    private void connectToVpn(String country) {
        isLoggedIn(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    Log.e(TAG, "success: logged in");
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
                            dialog.dismiss();
                            updateUi();
                            get_start_btn.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(Theme_Vpn_Activity.this, VpnDisconnectService.class);
                            startService(intent);

                        }

                        @Override
                        public void error(@NonNull VpnException e) {
                            dialog.dismiss();
                            Log.e("Check_Exception",e.getMessage());
                            get_start_btn.setVisibility(View.VISIBLE);
                            //  Toast.makeText(VPNActivity.this, "Error while connecting to vpn", Toast.LENGTH_SHORT).show();
                            // tvStartButton.setVisibility(View.VISIBLE);
                            updateUi();


                        }
                    });

                } else {
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), Theme_Vpn_Activity.class));
                    updateUi();

                }


            }

            @Override
            public void failure(@NonNull VpnException e) {
                dialog.dismiss();
                get_start_btn.setVisibility(View.VISIBLE);
                updateUi();

            }
        });

    }

    protected void isLoggedIn(Callback<Boolean> callback) {
        UnifiedSDK.getInstance().getBackend().isLoggedIn(callback);
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
    protected void onDestroy() {
        super.onDestroy();
        stopVpn();
    }
    public void disconnectFromVpn() {
        UnifiedSDK.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
                if (vpnState == VPNState.CONNECTED) {
                    UnifiedSDK.getInstance().getVPN().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
                        @Override
                        public void complete() {
                            dialog.dismiss();
                            updateUi();
                            Log.e(TAG, "complete: vpn is disconnect successfully");


                        }

                        @Override
                        public void error(@NonNull VpnException e) {
                            Log.e(TAG, "error: ", e);
                        }
                    });
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {

            }
        });
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
                            Log.e(TAG, "error: ", e);
                        }
                    });
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {

            }
        });
    }
    @Override
    public void onRegionSelected(Country item) {
        selected = item.getCountry();
        CountryModal country = Utils.getCountryFlagPath(selected, Theme_Vpn_Activity.this);
        if (country != null) {
            Glide.with(Theme_Vpn_Activity.this).load(country.getPathFlag()).into(image_icon);
            text_country.setText(country.getName());
        }
        connectToVpn(selected);
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

    @Override
    public void onBackPressed() {
        exitApp();
    }
    public void exitApp()
    {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Theme_Vpn_Activity.this);
        View view= LayoutInflater.from(Theme_Vpn_Activity.this).inflate(R.layout.dialog_exit_app,null);
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
                Appmanage.getInstance(Theme_Vpn_Activity.this).show_INTERSTIAL(Theme_Vpn_Activity.this, new Appmanage.MyCallback() {
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