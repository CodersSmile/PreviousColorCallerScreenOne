package com.themescreen.flashcolor.stylescreen.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapterr extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList();
    private final List<String> mFragmentTitleList = new ArrayList();
    public ViewPagerAdapterr(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return (Fragment) this.mFragmentList.get(position);

    }

    @Override
    public int getCount() {
        return this.mFragmentList.size();
    }
    public void addFrag(Fragment fragment, String str) {
        this.mFragmentList.add(fragment);
        this.mFragmentTitleList.add(str);
    }
    public CharSequence getPageTitle(int i) {
        return (CharSequence) this.mFragmentTitleList.get(i);
    }

}
