package com.yandex.mandrik.launcher.listappsactivity.pageadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.yandex.mandrik.launcher.listappsactivity.appsfavorities.AppsFavoritiesFragment;
import com.yandex.mandrik.launcher.listappsactivity.appsrecycler.AppsRecyclerFragment;
import com.yandex.mandrik.launcher.listappsactivity.appsrecycler.recycler.ApplicationsRecycler;

/**
 * A simple pager adapter that represents 5 ViewPageRecyclerFragment objects, in
 * sequence.
 */
public class AppsRecyclerScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    int numPages;

    public AppsRecyclerScreenSlidePagerAdapter(FragmentManager fm, int numPages) {
        super(fm);
        this.numPages = numPages;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "All applications";
            case 1:
                return "Favorities applications";
            default:
                return "None";
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new AppsRecyclerFragment();
            case 1:
                return new AppsFavoritiesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }
}