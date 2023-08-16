package com.themescreen.flashcolor.stylescreen.admobmanager;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.themescreen.flashcolor.stylescreen.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class Ad_mob_Manager {
    public static InterstitialAd interstitialAds;
    private static SharePreferencess sp;
    private static int BAck_count;
    private static int mainCount;
    public static void loadPreloadNative(Context context) {
        sp=new SharePreferencess(context);
        if (sp.getAd_show_Status().equals(0)) {
            return;
        }
        Ads_helper.getInstance(context).preLoadAdmobNative();
    }
    public static void showNative(Activity activity) {
        showPreloadNative(activity, activity.findViewById(R.id.native_ads), false);
    }

    public static void showNativeAd(Activity activity, ViewGroup viewGroup, boolean isSmall) {
        showPreloadNative(activity, viewGroup, isSmall);
    }

    public static void showBanner(Context context, LinearLayout adtemp, Window window) {
        sp=new SharePreferencess(context);
        if(sp.getAd_show_Admob().equals("1") && sp.getAd_show_Status().equals("1"))
        {
            if(sp.getcloseBottonBanner())
            {
                AdView adView = new AdView(context);
                AdRequest adRequest = new AdRequest.Builder().build();
                AdSize adSize = getAdSize(context, window);
                adView.setAdSize(adSize);
                adView.setAdUnitId(sp.getAdmob_Banner());
                adtemp.addView(adView);
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();

                    }
                });
                adView.loadAd(adRequest);
            }
        }


    }

    public static void FixshowBanner(Context context, LinearLayout adtemp, Window window) {
        sp=new SharePreferencess(context);
        if(sp.getAd_show_Admob().equals("1") && sp.getAd_show_Status().equals("1")) {
            AdView adView = new AdView(context);
            AdRequest adRequest = new AdRequest.Builder().build();
            AdSize adSize = getAdSize(context, window);
            adView.setAdSize(adSize);
            adView.setAdUnitId(sp.getAdmob_Banner());
            adtemp.addView(adView);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }
            });
            adView.loadAd(adRequest);
        }
    }

    public static AdSize getAdSize(Context context, Window window) {
        Display display = window.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

    public static void loadInterstialAds(Context context) {
        sp=new SharePreferencess(context);
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, sp.getAdmob_Interstial(), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                interstitialAds = interstitialAd;
                interstitialAds.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                String error = String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                Log.e("Load_Ad",error.toString());
                // Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void showInterstitial(Context context) {

        if (interstitialAds != null) {
            interstitialAds.show((Activity) context);

        } else {

        }
    }

    /*public static void Back_AdsCount(Context context) {

        sp = new SharePreferencess(context);
            if (sp.getBack_ads().equals("true")) {
                if (sp.getBackCountAds() == 0) {
                    showInterstitial(context);
                } else {
                    if (BAck_count % sp.getBackCountAds() == 0) {
                        showInterstitial(context);
                         BAck_count++;
                    } else {
                        BAck_count++;
                    }
                }
            }

    }
    public static void Main_Count_Interstial(Context context) {
        sp = new SharePreferencess(context);
        if (sp.getAd_show_Status().equals("1")) {
            if (sp.getMainCounter() == 0) {
                showInterstitial(context);
            } else {
                if (mainCount % sp.getMainCounter() == 0) {
                    new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }
                        @Override
                        public void onFinish() {  // runs after 2 sec. (2000 msec.)
                            showInterstitial(context);
                        }
                    }.start();

                     mainCount++;
                } else {
                    mainCount++;
                }
            }
        }
    }*/

    public static void Inner_Count_Interstial(Context context) {
        sp = new SharePreferencess(context);
        if (sp.getAd_show_Status().equals("1")) {
            if(sp.getMainIner()==0)
            {
                showInterstitial(context);
            }
            else
            {
                if (mainCount%sp.getMainCounter()==0) {
                    showInterstitial(context);
                    mainCount++;
                } else {
                    mainCount++;

                }
            }
        }
    }
    private static void showPreloadNative(Context context, ViewGroup viewGroup, boolean isSmall) {
        sp=new SharePreferencess(context);
        if (sp.getAd_show_Status().equals("0") || sp.getAd_show_Admob().equals("0")) {
            viewGroup.setVisibility(View.GONE);
            return;
        }
            Ads_helper.getInstance(context).showAdmobPreloadNative(viewGroup, isSmall);
    }
}
