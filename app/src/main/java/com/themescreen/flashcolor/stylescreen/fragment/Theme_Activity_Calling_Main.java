package com.themescreen.flashcolor.stylescreen.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.themescreen.flashcolor.stylescreen.Adapter.Adapter_online_theme;
import com.themescreen.flashcolor.stylescreen.Adapter.Viewpager_Fragments_Adapter;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Theme_Activity_Calling_Main extends AppCompatActivity {
    public static final String MyPREFERENCES = "myprefs";
    ArrayList<Integer> backpressfragmnt = new ArrayList<>();
    ImageView img_back;
    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                return;
            }
            if (!Theme_Activity_Calling_Main.this.connectivity(context)) {
                Adapter_online_theme.canceltask();
                Theme_Activity_Calling_Main.this.pagerAdapter.notifyDataSetChanged();
                Toast.makeText(context, "NetWork Connection is OFF", Toast.LENGTH_LONG).show();
                return;
            }
            Theme_Activity_Calling_Main.this.pagerAdapter.notifyDataSetChanged();
        }
    };
    PagerAdapter pagerAdapter;
    SharedPreferences sharedpreferences;
    ImageView tab1;
    ImageView tab2;
    ImageView tab3;
    TabLayout tab_layout;
    TextView textview_actionbar;
    int v = 1;
    ViewPager viewpager;
    SharePreferencess sp;
    public void setting_click(View view) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_theams);
        sp=new SharePreferencess(this);
      //  Ad_mob_Manager.showNative(this);
        this.backpressfragmnt.add(0);
        this.img_back = (ImageView) findViewById(R.id.img_back);
        TextView textView = (TextView) findViewById(R.id.textview_actionbar);
        this.textview_actionbar = textView;
        textView.setText("Caller Theme");
        this.img_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Appmanage.getInstance(Theme_Activity_Calling_Main.this).show_INTERSTIAL(Theme_Activity_Calling_Main.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Theme_Activity_Calling_Main.this.onBackPressed();
                    }
                },sp.getBackCountAds(),false);

            }
        });
        Help.width = getResources().getDisplayMetrics().widthPixels;
        Help.height = getResources().getDisplayMetrics().heightPixels;
        this.tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        this.viewpager = (ViewPager) findViewById(R.id.viewpager);
        ui();

        TabLayout tabLayout = this.tab_layout;
        tabLayout.addTab(tabLayout.newTab());
        TabLayout tabLayout3 = this.tab_layout;
        tabLayout3.addTab(tabLayout3.newTab());

        ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.row_custom_tab_item, (ViewGroup) null);
        this.tab1 = imageView;
        imageView.setImageResource(R.drawable.home_press1);
        this.tab_layout.getTabAt(0).setCustomView(this.tab1);
        Help.getheightandwidth(this);
        Help.setHeight(1080);
        Help.setSize(this.tab1, 320, 76, true);

        ImageView imageView3 = (ImageView) getLayoutInflater().inflate(R.layout.row_custom_tab_item, (ViewGroup) null);
        this.tab3 = imageView3;
        imageView3.setImageResource(R.drawable.setting_unpress);
        this.tab_layout.getTabAt(1).setCustomView(this.tab3);
        Help.getheightandwidth(this);
        Help.setHeight(1080);
        Help.setSize(this.tab3, 320, 76, true);

        Viewpager_Fragments_Adapter viewpager_Fragments_Adapter = new Viewpager_Fragments_Adapter(getSupportFragmentManager(), this.tab_layout.getTabCount());
        this.pagerAdapter = viewpager_Fragments_Adapter;
        this.viewpager.setAdapter(viewpager_Fragments_Adapter);
        this.tab_layout.addOnTabSelectedListener((TabLayout.OnTabSelectedListener) new TabLayout.OnTabSelectedListener() {

            public void onTabReselected(TabLayout.Tab tab) {
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabSelected(TabLayout.Tab tab) {
                Theme_Activity_Calling_Main.this.viewpager.setCurrentItem(tab.getPosition());
                Theme_Activity_Calling_Main.this.backpressfragmnt.add(Integer.valueOf(tab.getPosition()));
                Log.e("TAB_POSITION", "onTabSelected: " + tab.getPosition());
                if (tab.getPosition() == 0) {
                    Theme_Activity_Calling_Main.this.tab1.setImageResource(R.drawable.home_press1);
                    Theme_Activity_Calling_Main.this.tab_layout.getTabAt(tab.getPosition()).setCustomView(Theme_Activity_Calling_Main.this.tab1);
                    Theme_Activity_Calling_Main.this.tab3.setImageResource(R.drawable.setting_unpress);
                    Theme_Activity_Calling_Main.this.tab_layout.getTabAt(1).setCustomView(Theme_Activity_Calling_Main.this.tab3);
                } else if (tab.getPosition() == 1) {
                    Theme_Activity_Calling_Main.this.tab1.setImageResource(R.drawable.home_unpress);
                    Theme_Activity_Calling_Main.this.tab_layout.getTabAt(0).setCustomView(Theme_Activity_Calling_Main.this.tab1);
                    Theme_Activity_Calling_Main.this.tab3.setImageResource(R.drawable.setting_press1);
                    Theme_Activity_Calling_Main.this.tab_layout.getTabAt(tab.getPosition()).setCustomView(Theme_Activity_Calling_Main.this.tab3);
                }
            }
        });
        this.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tab_layout));
    }

    private void ui() {
        Help.getheightandwidth(this);
        Help.setHeight(1080);
        Help.setSize(this.tab_layout, 1080, 140, true);
    }

    public void onBackPressed() {
        Adapter_online_theme.canceltask();
        Appmanage.getInstance(Theme_Activity_Calling_Main.this).show_INTERSTIAL(Theme_Activity_Calling_Main.this, new Appmanage.MyCallback() {
            @Override
            public void callbackCall() {
               Theme_Activity_Calling_Main.super.onBackPressed();
            }
        },sp.getBackCountAds(),false);
    }

    public void custom_internal_theam(View view) {
        this.sharedpreferences = getSharedPreferences("myprefs", 0);
        Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, 115);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 115) {
            if (i2 == -1) {
                Uri data = intent.getData();
                SharedPreferences.Editor edit = this.sharedpreferences.edit();
                edit.putString("theamvideo", data.toString());
                edit.apply();
            }
        } else if (i == 121) {
            if (i2 == -1) {
                Fragment_Setting.caller_theme.setImageResource(R.drawable.off_btn);
            }
        } else if (i == 114 && i2 == -1) {
            Fragment_Setting.caller_theme.setImageResource(R.drawable.on_btn);
        }
    }

    public String getPath(Uri uri) {
        Cursor query = getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
        if (query == null) {
            return null;
        }
        int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
        query.moveToFirst();
        return query.getString(columnIndexOrThrow);
    }

    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(this.networkChangeReceiver, intentFilter);
    }

    public void onPause() {
        super.onPause();
        unregisterReceiver(this.networkChangeReceiver);
    }

    public void onDestroy() {
        Adapter_online_theme.canceltask();
        super.onDestroy();
    }

    public boolean connectivity(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT < 29) {
                try {
                    if (connectivityManager.getActiveNetworkInfo() != null) {
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    Log.i("update_statut", "" + e.getMessage());
                }
            } else if (connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork()) != null) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


}
