package com.themescreen.flashcolor.stylescreen.admobmanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.themescreen.flashcolor.stylescreen.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;

public class Appmanage {
    public static Appmanage mInstance;
    ArrayList<String> interstitial_sequence = new ArrayList<>();
    static MyCallback myCallback;
    boolean is_foursesully;
    public static int count_click = -1;
    public static int count_click_for_alt = -1;
    SharePreferencess sp;
    private Dialog dialog;
    public InterstitialAd interstitial1;
    Context context;
    public boolean isLast = false;
    public boolean is_am_running = false;
    private static int BAck_count;
    private static int mainCount;
    OnInterstitialDismiss callback;
    public Appmanage(Context context) {
        setActivity(context);
        sp = new SharePreferencess(context);
    }
    public void setActivity(Context activity) {
        this.context = activity;
    }
    public static Appmanage getInstance(Context activity) {
        if (mInstance == null) {
            mInstance = new Appmanage(activity);
            return mInstance;
        } else {

            mInstance.setActivity(activity);
            return mInstance;
        }
    }
    public void show_INTERSTIAL(Context context, MyCallback myCallback, int click, boolean backAds) {

        displayInterstitial(context, myCallback, false, click, backAds);
    }
    public void loadintertialads(final Activity activity, final String google_i) {
        sp = new SharePreferencess(context);

        if (sp.getAd_show_Status().equals("0")) {
            Log.e("Ads", "loadintertialads: " + sp.getAd_show_Status().equals("0"));
            return;
        }


        if (sp.getAd_show_Admob().equals("1") && !google_i.isEmpty()) {

            Log.e("Ads", "loadintertialads: " + "Inter Loads" + google_i);
            loadAdmobInterstitial(activity, google_i);
        }
    }
    private void loadAdmobInterstitial(Activity activity, String google_i) {
        AdRequest adRequest = new AdRequest.Builder().build();
        //  new RequestConfiguration.Builder ().setTestDeviceIds (Arrays.asList ("F2DCF403D5F69BFBF279EBB68F03FDA5"));
        InterstitialAd.load(context, google_i, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                interstitial1 = interstitialAd;
                interstitial1.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        if (!isLast) {
                            delayAdmobInterLoads();
                        }

                        interstitialCallBack();
                        super.onAdDismissedFullScreenContent();
                    }
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        interstitial1 = null;
                        super.onAdFailedToShowFullScreenContent(adError);
                    }
                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }
                    @Override
                    public void onAdShowedFullScreenContent() {
                        interstitial1 = null;
                        super.onAdShowedFullScreenContent();
                    }
                });
                super.onAdLoaded(interstitialAd);
            }
        });
    }
    public void displayInterstitial(Context context, MyCallback myCallback2, boolean is_foursesully, int how_many_clicks, boolean back_ads) {

        sp = new SharePreferencess(context);
        this.myCallback = myCallback2;
        this.is_foursesully = is_foursesully;
        count_click++;
        count_click_for_alt++;
        if (back_ads) {

            if (sp.getAd_show_Status().equals("1") && sp.getAd_show_Admob().equals("1")) {

                if (sp.getMainCounter() == 0) {
                    showInterstitialAd(sp.getPlatFrom(), context);
                } else {

                    if (mainCount % sp.getMainCounter() == 0) {
                        showInterstitialAd(sp.getPlatFrom(), context);
                        mainCount++;
                    } else {
                        mainCount++;
                        myCallback2.callbackCall();

                    }
                }
            }
            else
            {
                myCallback2.callbackCall();
            }
        } else {
            if(sp.getAd_show_Status().equals("1") && sp.getAd_show_Admob().equals("1"))
            {
                if (sp.getBack_ads().equals("true")) {
                    if (sp.getBackCountAds() == 0) {
                        showInterstitialAd(sp.getPlatFrom(), context);
                    } else {
                        if (BAck_count % sp.getBackCountAds() == 0) {
                            showInterstitialAd(sp.getPlatFrom(), context);
                            BAck_count++;
                        } else {
                            BAck_count++;
                            myCallback2.callbackCall();
                        }
                    }
                }
                else
                {
                    myCallback2.callbackCall();
                }
            }
            else
            {
                myCallback2.callbackCall();
            }

        }

    }

    private void showInterstitialAd(String platform, final Context context) {

        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.ad_progress_dialog, null);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        if (platform.equals("Admob") && sp.getAd_show_Status().equals("1") && interstitial1 != null) {

            if (sp.getAppDialogShowBefore() == 1) {
                dialog.show();

                ProgressBar circular_progress = view.findViewById(R.id.circularProgressbar);
                circular_progress.setProgress(0);

                new CountDownTimer(sp.getCountTimer(), 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                        circular_progress.setProgress((int) millisUntilFinished/1000);
                    }

                    @Override
                    public void onFinish() {
                        dialog.dismiss();
                        interstitial1.show((Activity) context);


                    }
                }.start();

            } else {
                interstitial1.show((Activity) context);

            }

        } else {

            nextInterstitialPlatform(context);

        }
    }

    public interface MyCallback {
        void callbackCall();
    }

    private void nextInterstitialPlatform(Context context) {

        if (interstitial_sequence.size() != 0) {
            interstitial_sequence.remove(0);

            if (interstitial_sequence.size() != 0) {
                showInterstitialAd(interstitial_sequence.get(0), context);
            } else {
                if (is_foursesully == true && hasActiveInternetConnection(context)) {

                } else {
                    interstitialCallBack();
                }
            }

        } else {

            if (is_foursesully == true && hasActiveInternetConnection(context)) {

            } else {
                interstitialCallBack();
            }

        }
    }

    public void interstitialCallBack() {

        if (myCallback != null) {
            myCallback.callbackCall();
            Appmanage.this.myCallback = null;
        }
    }

    public static boolean hasActiveInternetConnection(Context context) {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void delayAdmobInterLoads() {

        if (is_am_running == false) {
            new Handler().postDelayed(new Runnable() {
                public void run() {

                    loadAdmobInterstitial((Activity) context, sp.getAdmob_Interstial());

                    is_am_running = false;
                }
            }, sp.getCountTimer());
        }
    }
    public interface OnInterstitialDismiss {
        void onDismiss();
    }


}
