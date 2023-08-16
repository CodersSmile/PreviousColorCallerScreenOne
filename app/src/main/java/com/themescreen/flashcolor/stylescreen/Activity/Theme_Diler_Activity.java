package com.themescreen.flashcolor.stylescreen.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.themescreen.flashcolor.stylescreen.Adapter.ViewPagerAdapterr;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.fragment.Fragment_Keypad;
import com.themescreen.flashcolor.stylescreen.fragment.Frgament_Contacts;

public class Theme_Diler_Activity extends AppCompatActivity {
ViewPager viewpager_dailor;
ImageView img_Keypad,img_contects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diler);
        viewpager_dailor=findViewById(R.id.viewpager_dailor);
        img_Keypad=findViewById(R.id.img_Keypad);
        img_contects=findViewById(R.id.img_contects);
        ViewPagerAdapterr viewPagerAdapterr = new ViewPagerAdapterr(getSupportFragmentManager());
        viewPagerAdapterr.addFrag(new Fragment_Keypad(), "fragment_keypad");
        viewPagerAdapterr.addFrag(new Frgament_Contacts(), "fragment_contacts");
        viewpager_dailor.setAdapter(viewPagerAdapterr);

        img_Keypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              viewpager_dailor.setCurrentItem(0);
                setposition(0);
            }
        });

        img_contects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager_dailor.setCurrentItem(1);
                setposition(1);
            }
        });
    }
    public void setposition(int i) {
        if (i == 0) {
            img_contects.setImageResource(R.drawable.contacts_unpress_new);
            img_Keypad.setImageResource(R.drawable.keypad_press_new);
        } else if (i == 1) {
            img_contects.setImageResource(R.drawable.contacts_press_new);
            img_Keypad.setImageResource(R.drawable.keypad_unpress_neww);
        }
    }

}