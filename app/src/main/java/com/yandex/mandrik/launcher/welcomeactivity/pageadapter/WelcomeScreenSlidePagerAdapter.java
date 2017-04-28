package com.yandex.mandrik.launcher.welcomeactivity.pageadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yandex.mandrik.launcher.welcomeactivity.fragments.WelcomeViewPageFragment;

/**
 * A simple pager adapter that represents 5 ViewPageRecyclerFragment objects, in
 * sequence.
 */
public class WelcomeScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    int numPages;

    public WelcomeScreenSlidePagerAdapter(FragmentManager fm, int numPaages) {
        super(fm);
        numPages = numPaages;
    }

    @Override
    public Fragment getItem(int position) {
         return WelcomeViewPageFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return numPages;
    }

}