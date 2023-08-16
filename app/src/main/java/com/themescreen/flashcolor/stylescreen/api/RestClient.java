package com.themescreen.flashcolor.stylescreen.api;

import android.app.Activity;

import com.themescreen.flashcolor.stylescreen.admobmanager.MyApplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestClient {
    private static Activity mActivity;
    private static final RestClient restClient = new RestClient();
    private static Retrofit Adsretrofit;
    private static final String BASE_URL = MyApplication.Base_Url;
    public static RestClient getInstance(Activity activity) {
        mActivity = activity;
        return restClient;
    }


    public static Retrofit getRetrofitInstance() {
        if (Adsretrofit == null) {
            Adsretrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return Adsretrofit;
    }
}