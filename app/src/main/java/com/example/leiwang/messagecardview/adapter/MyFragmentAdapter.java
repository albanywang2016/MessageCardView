package com.example.leiwang.messagecardview.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei.wang on 4/5/2017.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {

    List<Fragment> pages = new ArrayList<>();

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pages.get(position).toString();
    }

    public void addPage (Fragment fragment){
        pages.add(fragment);
    }
}
