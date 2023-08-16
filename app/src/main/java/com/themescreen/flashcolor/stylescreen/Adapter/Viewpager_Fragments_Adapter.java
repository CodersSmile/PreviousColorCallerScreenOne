package com.themescreen.flashcolor.stylescreen.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.themescreen.flashcolor.stylescreen.fragment.Fragment_Online_Theme;
import com.themescreen.flashcolor.stylescreen.fragment.Fragment_Setting;

public class Viewpager_Fragments_Adapter extends FragmentPagerAdapter {
    private int numoftabs;

    public int getItemPosition(Object obj) {
        return -2;
    }

    public Viewpager_Fragments_Adapter(FragmentManager fragmentManager, int i) {
        super(fragmentManager);
        this.numoftabs = i;
    }

    public Fragment getItem(int i) {
        if (i == 0) {
            return new Fragment_Online_Theme();
        }
        if (i != 1) {
            return null;
        }
        return new Fragment_Setting();
    }

    public int getCount() {
        return this.numoftabs;
    }
}
