package com.themescreen.flashcolor.stylescreen.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anchorfree.partner.api.ClientInfo;
import com.anchorfree.partner.api.data.Country;
import com.anchorfree.partner.api.response.AvailableCountries;
import com.anchorfree.reporting.TrackingConstants;
import com.anchorfree.sdk.SessionConfig;
import com.anchorfree.sdk.SessionInfo;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.sdk.rules.TrafficRule;
import com.anchorfree.vpnsdk.callbacks.CompletableCallback;
import com.anchorfree.vpnsdk.compat.CredentialsCompat;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.anchorfree.vpnsdk.transporthydra.HydraTransport;
import com.anchorfree.vpnsdk.vpnservice.VPNState;
import com.themescreen.flashcolor.stylescreen.BuildConfig;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Ad_mob_Manager;
import com.themescreen.flashcolor.stylescreen.admobmanager.AppOpenManager;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.MyApplication;
import com.themescreen.flashcolor.stylescreen.admobmanager.OnAppOpenDisplayed;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;
import com.themescreen.flashcolor.stylescreen.api.Example;
import com.themescreen.flashcolor.stylescreen.api.GetInterface;
import com.themescreen.flashcolor.stylescreen.api.RestClient;
import com.themescreen.flashcolor.stylescreen.api.VpnDisconnectService;
import com.themescreen.flashcolor.stylescreen.custom_dailor.Activity.Theme_Activity_Main_Dialpad_Selection;
import com.themescreen.flashcolor.stylescreen.utils.MCrypt;
import com.themescreen.flashcolor.stylescreen.utils.Utils;
import com.themescreen.flashcolor.stylescreen.vpnstatus.Country_Code;
import com.northghost.caketube.OpenVpnTransportConfig;
import com.scottyab.rootbeer.RootBeer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;

public class Theme_Activity_Splash_screen extends AppCompatActivity {

    static {
        System.loadLibrary("keys");
    }
    SharePreferencess sp;
    private ClientInfo clientInfo;
    private AppOpenManager appOpenManager;
    private Dialog dialog;
    String currDate;
    private int appStatus;
    public native String getkey();
    public native String getvalue();
    GetInterface service;
    String key;
    Call<Example> list;
    ArrayList<Country_Code> list_country;
    List<Country> list_c;
    private String selected = "";
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_splash_screen);
        sp=new SharePreferencess(this);
        key = MyApplication.key_value;
        RetryDialog();
    }
    private void RetryDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_retry);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    finishAffinity();
                }
                return true;
            }
        });
        TextView retry_buttton = dialog.findViewById(R.id.retry_connection);
        TextView close_button = dialog.findViewById(R.id.close_connection);

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
        retry_buttton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    initAds();
                    dialog.dismiss();
                } else {
                    Toast.makeText(Theme_Activity_Splash_screen.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (!isNetworkAvailable()) {
            dialog.show();
        }
        initAds();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {

            isAvailable = true;
        }
        return isAvailable;
    }



    public void initAds()
    {
        Calendar calInstance = Calendar.getInstance();
        calInstance.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        currDate = format.format(calInstance.getTime());
        RootBeer rootBeer = new RootBeer(this);
        if (rootBeer.isRooted()) {
            Toast.makeText(this, "GET ROOTED DEVICE", Toast.LENGTH_LONG).show();
        } else {
            if (sp.getfirstTime()) {
                sp.setDate(currDate);
                sp.setfirstTime(false);
                appStatus = 1;
            } else {
                if (!currDate.equals(sp.getDate())) {
                    sp.setDate(currDate);
                    appStatus = 2;
                } else {
                    appStatus = 3;
                }
            }
            GetData(getPackageName(), appStatus, key);
        }
    }

    public void GetData(String pack, int num, String key) {
        service = RestClient.getRetrofitInstance().create(GetInterface.class);

        if (BuildConfig.DEBUG) {
            num=3;
            list = service.getDebugStatus(pack, num, key, true);
        } else {
            list = service.getStatus(pack, num, key);
        }
        list.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, retrofit2.Response<Example> response) {
                Example example = response.body();
                try {
                    byte[] bytes = example.getData().getBytes();
                    String code = MCrypt.getResponseString(bytes,getkey(), getvalue());
                    Log.e("Reso",code);
                    JSONObject jsonObject = new JSONObject(code);
                    JSONObject object = jsonObject.getJSONObject("app_config");
                    sp.setPrivacy(object.getString("policy_link"));
                    sp.setAd_show_Status(object.getString("app_ad_show_status"));
                    sp.setApp_open_Start(object.getString("app_open_start"));
                    sp.setMainCounter(Integer.parseInt(object.getString("counter_main")));
                    sp.setMainIner(Integer.parseInt(object.getString("counter_inner")));
                    sp.setPlatForm("Admob");
                    sp.setCountTimer(Integer.parseInt(object.getString("app_amDelay")));
                    sp.setAppDialogShowBefore(Integer.parseInt(object.getString("app_dialogBeforeAdShow")));
                    JSONObject place_ids = jsonObject.getJSONObject("place_ids");
                    JSONObject admob = place_ids.getJSONObject("Admob");

                    sp.setAd_show_Admob(admob.getString("ad_show_status"));
                    sp.setAdmob_Appid(admob.getString("app_id"));
                    sp.setAdmob_Interstial(admob.getString("intr1"));
                    sp.setAdmob_Native(admob.getString("ntv1"));
                    sp.setAdmob_Banner(admob.getString("bnr1"));
                    sp.setAppOpbId(admob.getString("app_open1"));

                    JSONObject other_config = jsonObject.getJSONObject("other_config");
                    sp.setBack_Ads(other_config.getString("back_ads"));
                    sp.setBackCountAds(Integer.parseInt(other_config.getString("back_counter")));
                    sp.setScreenFlag(Integer.parseInt(other_config.getString("start_screen")));
                    sp.setVpnStatus(Integer.parseInt(other_config.getString("vpn_status")));
                    sp.setVpnButtonStatus(Integer.parseInt(other_config.getString("vpn_button")));
                    sp.setBackendUrl(other_config.getString("url"));
                    sp.setCarrier(other_config.getString("key"));
                    sp.setVpnClose(Integer.parseInt(other_config.getString("vpn_close_button")));
                    sp.setcloseBottonBanner(Boolean.parseBoolean(other_config.getString("bottom_banner")));
                    if (!other_config.isNull("country_list")) {
                        JSONArray array = other_config.getJSONArray("country_list");
                        if (array.length() > 0) {
                            Utils.list_Country = new ArrayList<>();
                            for (int i = 0; i <array.length(); i++) {
                                String country = array.getString(i);
                                Country_Code modal = new Country_Code();
                                modal.setCode(country);
                                Utils.list_Country.add(modal);
                            }
                            getStart();
                        } else {

                            getStart();
                        }
                    } else {
                        Log.e("Error_msg", "other Null");
                        getStart();
                    }

                } catch (Exception e) {
                    Log.e("Error_msg", e.getMessage());
                    sp.setAdmob_Appid("");
                    sp.setAdmob_Banner("");
                    sp.setAdmob_Interstial("");
                    sp.setAdmob_Native("");
                    sp.setAppOpbId("");
                    sp.setAd_show_Admob("0");
                    sp.setAd_show_Status("0");
                    getStart();
                }
            }
            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                sp.setAdmob_Appid("");
                sp.setAdmob_Banner("");
                sp.setAdmob_Interstial("");
                sp.setAdmob_Native("");
                sp.setAppOpbId("");
                sp.setAd_show_Admob("0");
                sp.setAd_show_Status("0");
                getStart();
            }
        });


    }

    public void getStart() {
        if (sp.getAd_show_Admob().equals("1") && sp.getAd_show_Status().equals("1")) {
            Appmanage.getInstance (Theme_Activity_Splash_screen.this).loadintertialads (Theme_Activity_Splash_screen.this, sp.getAdmob_Interstial());
            Ad_mob_Manager.loadPreloadNative(Theme_Activity_Splash_screen.this);
            if (sp.getSecureBox() == true && sp.getVpnStatus() == 2) {
                VpnloginStatus();
            } else {
                AdOpen();
            }

        } else {
            sp.setAdmob_Appid("");
            sp.setAdmob_Banner("");
            sp.setAdmob_Interstial("");
            sp.setAdmob_Native("");
            sp.setAppOpbId("");
            VpnloginStatus();
        }
    }
    public void AdOpen() {
        appOpenManager = ((MyApplication) getApplicationContext()).getAppOpenManager();

        if (appOpenManager != null) {
            appOpenManager.fetchAndShowInSplash(new OnAppOpenDisplayed() {
                @Override
                public void OnSucess() {
                    VpnloginStatus();
                }

                @Override
                public void OnError() {
                    VpnloginStatus();
                }
            });

        } else {

            VpnloginStatus();
        }
    }
    public void VpnloginStatus() {
        if (sp.getVpnStatus() == 1 || sp.getVpnStatus() == 2) {
            loginVpnMethod();
        } else {
            VpnNotStart();
        }
    }
    public void loginVpnMethod() {
        String backendurl = sp.getBackendUrl();
        String key = sp.getKey();
        if (!backendurl.isEmpty() && !key.isEmpty()) {
            if(!backendurl.equals("") && !key.equals(""))
            {
                clientInfo = ClientInfo.newBuilder().addUrl(backendurl)
                        .carrierId(key).build();
                UnifiedSDK.clearInstances();
                Utils.loginClientInfo(clientInfo, new OnAppOpenDisplayed() {
                    @Override
                    public void OnSucess() {
                        if (sp.getVpnStatus() == 2 && sp.getSecureBox() == true) {
                            getCountry();
                        } else {
                            goToNext();
                        }
                    }
                    @Override
                    public void OnError() {
                        Log.e("Erro_login","testing");
                        VpnNotStart();
                    }
                });
            }
            else
            {
                VpnNotStart();
            }

        } else {
            VpnNotStart();
        }
    }
    public void VpnNotStart()
    {
        startActivity(new Intent(getApplicationContext(), Theme_Activity_VpnErrorScreen.class));
    }
    public void goToNext() {
        if (sp.getVpnStatus() == 1) {
            sp.setSecureBox(false);
            if(sp.getVpnButtonStatus()==1)
            {
                Intent intent = new Intent(Theme_Activity_Splash_screen.this, Theme_Vpn_Activity.class);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(Theme_Activity_Splash_screen.this, Theme_Activity_Main_Dialpad_Selection.class);
                startActivity(intent);
            }

        } else if(sp.getVpnStatus()==2) {
            Intent intent = new Intent(Theme_Activity_Splash_screen.this, Theme_Dialog_Activity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(Theme_Activity_Splash_screen.this, Theme_Activity_VpnErrorScreen.class);
            startActivity(intent);
        }
    }
    private void connectToVpn(String country) {
        isLoggedIn(new com.anchorfree.vpnsdk.callbacks.Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
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
                            updateUi();
                            GotoActivity();
                            Intent intent = new Intent(Theme_Activity_Splash_screen.this, VpnDisconnectService.class);
                            startService(intent);
                        }

                        @Override
                        public void error(@NonNull VpnException e) {
                            GotoActivity();
                            updateUi();
                        }
                    });

                } else {

                    GotoActivity();
                    updateUi();

                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
                GotoActivity();
                updateUi();
            }
        });
    }
    private void updateUi() {

        UnifiedSDK.getVpnState(new com.anchorfree.vpnsdk.callbacks.Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
                if (vpnState == VPNState.CONNECTED) {

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
        getCurrentServer(new com.anchorfree.vpnsdk.callbacks.Callback<String>() {
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
    protected void getCurrentServer(final com.anchorfree.vpnsdk.callbacks.Callback<String> callback) {
        UnifiedSDK.getVpnState(new com.anchorfree.vpnsdk.callbacks.Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState state) {
                if (state == VPNState.CONNECTED) {
                    UnifiedSDK.getStatus(new com.anchorfree.vpnsdk.callbacks.Callback<SessionInfo>() {
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
    public void GotoActivity() {
        AdOpenStatus();
    }
    public void AdOpenStatus() {
        appOpenManager = ((MyApplication) getApplicationContext()).getAppOpenManager();

        if (appOpenManager != null) {
            appOpenManager.fetchAndShowInSplash(new OnAppOpenDisplayed() {
                @Override
                public void OnSucess() {
                    startActivity(new Intent(getApplicationContext(), Theme_Activity_VpnErrorScreen.class));

                }

                @Override
                public void OnError() {
                    startActivity(new Intent(getApplicationContext(), Theme_Activity_VpnErrorScreen.class));

                }
            });

        } else {

            startActivity(new Intent(getApplicationContext(), Theme_Activity_VpnErrorScreen.class));
        }
    }
    public void getCountry() {
        if (isNetworkAvailable()) {
            try {
                UnifiedSDK.getInstance().getBackend().countries(new com.anchorfree.vpnsdk.callbacks.Callback<AvailableCountries>() {
                    @Override
                    public void success(@NonNull AvailableCountries availableCountries) {
                        try {
                            Utils.list = new ArrayList<>();
                            list_country = new ArrayList<>();
                            list_c = availableCountries.getCountries();
                            Utils.list = availableCountries.getCountries();
                            if (Utils.list_Country.size() > 0) {
                                for (Country country : Utils.list) {
                                    for (Country_Code code : Utils.list_Country)
                                    {
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
                                if (!list_c.isEmpty()) {
                                    selected = getRandomStringValue(list_c);
                                } else {
                                    selected = "";
                                }
                            }
                            Log.e("Random_country ",selected);
                            connectToVpn(selected);
                        } catch (Exception e) {
                            Log.e("Random_country ",e.getMessage());
                            Log.e("Random_country ",selected);
                            connectToVpn(selected);
                        }

                    }

                    @Override
                    public void failure(@NonNull VpnException e) {
                        Log.e("Error_Vpn", e.getMessage());
                        VpnNotStart();
                    }
                });
            }
            catch (Exception e)
            {
                VpnNotStart();
            }

        } else {
            VpnNotStart();
            Toast.makeText(getApplicationContext(), "No Network Connection Available!", Toast.LENGTH_SHORT).show();
        }
    }
    protected void isLoggedIn(com.anchorfree.vpnsdk.callbacks.Callback<Boolean> callback) {
        UnifiedSDK.getInstance().getBackend().isLoggedIn(callback);
    }
    public static void stopVpn() {
        UnifiedSDK.getVpnState(new com.anchorfree.vpnsdk.callbacks.Callback<VPNState>() {
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


}
