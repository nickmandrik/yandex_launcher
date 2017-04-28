package com.yandex.mandrik.launcher.welcomeactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.yandex.mandrik.launcher.listappsactivity.ListAppsViewPagerActivity;
import com.yandex.mandrik.launcher.welcomeactivity.viewpager.NonSwipeableViewPager;

import com.yandex.mandrik.launcher.R;
import com.yandex.mandrik.launcher.welcomeactivity.pageadapter.WelcomeScreenSlidePagerAdapter;

import static com.yandex.mandrik.launcher.util.constants.LauncherConstants.APP_PREFERENCE_RECYCLER_APPS_SETTINGS;
import static com.yandex.mandrik.launcher.util.constants.LauncherConstants.APP_PREFERENCE_WELCOME_SETTINGS;
import static com.yandex.mandrik.launcher.util.constants.LauncherConstants.COUNT_ELEMENTS_IN_ROW_LANDSCAPE;
import static com.yandex.mandrik.launcher.util.constants.LauncherConstants.COUNT_ELEMENTS_IN_ROW_PORTRAIT;
import static com.yandex.mandrik.launcher.util.constants.LauncherConstants.IS_VISITED_WELCOME_ACTIVITY;
import static com.yandex.mandrik.launcher.util.constants.LauncherConstants.THEME_OF_APPLICATION;

public class WelcomeActivity extends AppCompatActivity {

    /**
     * The context of represent this activity
     */
    Context context;

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager pager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Instantiate a ViewPager, PagerAdapter, TabLayout and Context
        pager = (NonSwipeableViewPager) findViewById(R.id.pager);
        pagerAdapter = new WelcomeScreenSlidePagerAdapter(getSupportFragmentManager(), NUM_PAGES);
        pager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
        context = WelcomeActivity.this;
    }

    boolean isSize4x6 = true;

    public void onClickNext(View view) {
        int currentPositionItem = getItemViewPager(0);
        if(currentPositionItem != NUM_PAGES - 1) {
            Button button = (Button) findViewById(R.id.button);
            if(currentPositionItem == NUM_PAGES - 2) {
                View rootView = pager.findViewWithTag("rootView_" + pager.getCurrentItem());
                CheckBox checkBoxSize4x6 = (CheckBox) rootView.findViewById(R.id.check_box_size_4x6);
                if(!checkBoxSize4x6.isChecked()) {
                    isSize4x6 = false;
                }
                button.setText(context.getString(R.string.begin));
            } else {
                button.setText(context.getString(R.string.next));
            }
            pager.setCurrentItem(getItemViewPager(1));
        } else {

            View rootView = pager.findViewWithTag("rootView_" + pager.getCurrentItem());
            boolean isLightTheme = true;
            CheckBox checkBoxLightTheme = (CheckBox) rootView.findViewById(R.id.check_box_light_theme);
            if(!checkBoxLightTheme.isChecked()) {
                isLightTheme = false;
            }

            setVisitedWelcomeSettings();
            this.finish();
            changeSizeTableAndTheme(isSize4x6, isLightTheme);
            Intent intentMainApps = new Intent(this, ListAppsViewPagerActivity.class);
            this.startActivity(intentMainApps);
        }
    }

    private int getItemViewPager(int i) {
        return pager.getCurrentItem() + i;
    }

    private boolean setVisitedWelcomeSettings() {
        SharedPreferences welcomeSettings = getSharedPreferences
                (APP_PREFERENCE_WELCOME_SETTINGS, Context.MODE_PRIVATE);
        boolean isVisited = welcomeSettings.getBoolean(IS_VISITED_WELCOME_ACTIVITY, false);

        if (!isVisited) {
            SharedPreferences.Editor e = welcomeSettings.edit();
            e.putBoolean(IS_VISITED_WELCOME_ACTIVITY, true);
            e.apply();
        }

        return isVisited;
    }

    private void changeSizeTableAndTheme(boolean isSize4x6, boolean isLightTheme) {
        SharedPreferences appSettings =
                getSharedPreferences(APP_PREFERENCE_RECYCLER_APPS_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = appSettings.edit();

        if(!isLightTheme) {
            e.putString(THEME_OF_APPLICATION, "Theme.AmberTheme");
        } else {
            e.putString(THEME_OF_APPLICATION, "Theme.AmberTheme.Light");
        }
        if(isSize4x6) {
            e.putInt(COUNT_ELEMENTS_IN_ROW_PORTRAIT, 4);
            e.putInt(COUNT_ELEMENTS_IN_ROW_LANDSCAPE, 6);
        } else {
            e.putInt(COUNT_ELEMENTS_IN_ROW_PORTRAIT, 5);
            e.putInt(COUNT_ELEMENTS_IN_ROW_LANDSCAPE, 7);
        }

        e.apply();
    }
}