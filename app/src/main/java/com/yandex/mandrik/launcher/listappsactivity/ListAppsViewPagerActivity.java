package com.yandex.mandrik.launcher.listappsactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yandex.mandrik.launcher.listappsactivity.pageadapter.AppsRecyclerScreenSlidePagerAdapter;
import com.yandex.mandrik.launcher.listappsactivity.appsrecycler.recycler.ApplicationsRecycler;

import com.yandex.mandrik.launcher.R;
import com.yandex.mandrik.launcher.util.eventbus.HideFavoritesEvent;
import com.yandex.mandrik.launcher.util.receiver.UpdateApplicationReceiver;
import com.yandex.mandrik.launcher.welcomeactivity.WelcomeActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.yandex.mandrik.launcher.util.constants.LauncherConstants.*;

public class ListAppsViewPagerActivity extends AppCompatActivity {

    /*@Override
    protected void onPause() {
        super.onPause();
        int count = 5;
        if(isSize4x6) {
            count = 4;
        }
        appRecyclerView.getAdapter().updatePopular(count);
    }*/

    AppsRecyclerScreenSlidePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        setAppTheme();
        setContentView(R.layout.activity_recycler);

        if(!isVisitedWelcomeActivity()) {
            this.finish();
            Intent intentWelcome = new Intent(this, WelcomeActivity.class);
            this.startActivity(intentWelcome);
        }


        /**/
        ViewPager pager = (ViewPager) findViewById(R.id.rec_pager);
        pagerAdapter = new AppsRecyclerScreenSlidePagerAdapter
                (getSupportFragmentManager(), 2);
        pager.setAdapter(pagerAdapter);

        UpdateApplicationReceiver receiver = new UpdateApplicationReceiver();
    }

    private boolean isVisitedWelcomeActivity() {
        SharedPreferences welcomeSettings =
                getSharedPreferences(APP_PREFERENCE_WELCOME_SETTINGS, Context.MODE_PRIVATE);
        boolean isVisited = welcomeSettings.getBoolean(IS_VISITED_WELCOME_ACTIVITY, false);
        return isVisited;
    }


    /*private void remCountClicks(int position) {
        appRecyclerView.getAdapter().getAppInfoById(position)
                .setCountClicks(appRecyclerView.getAdapter().
                        getAppInfoById(position).getCountClicks() + 1);
        ApplicationListManager appManager = appRecyclerView.getApplicationListManager();
        int index = appRecyclerView.getAdapter().getIndexInArray(position);
        if(!appManager.countClicks.containsKey(index)) {
            appManager.countClicks.put(index, 1);
        } else {
            appManager.countClicks.put(index, appManager.countClicks.get(index) + 1);
        }
    }*/

    private void setAppTheme() {
        SharedPreferences appSettings =
                getSharedPreferences(APP_PREFERENCE_RECYCLER_APPS_SETTINGS, Context.MODE_PRIVATE);

        String theme = appSettings.getString(THEME_OF_APPLICATION, "Theme.AmberTheme.Light");

        if(theme.equals("Theme.AmberTheme.Light")) {
            setTheme(R.style.Theme_AmberTheme_Light);
        }
        if(theme.equals("Theme.AmberTheme")) {
            setTheme(R.style.Theme_AmberTheme);
        }
    }

    @Subscribe
    public void onHideFavoritesEvent(HideFavoritesEvent event) {
        SharedPreferences appSettings = ListAppsViewPagerActivity.this.getSharedPreferences
                (APP_PREFERENCE_RECYCLER_APPS_SETTINGS, Context.MODE_PRIVATE);

        boolean isHiddenFavorites = appSettings.getBoolean(IS_HIDDEN_FAVORITES, false);

        if(!isHiddenFavorites) {
            pagerAdapter.setNumPages(2);
            pagerAdapter.notifyDataSetChanged();
        } else {
            pagerAdapter.setNumPages(1);
            pagerAdapter.notifyDataSetChanged();
        }
    }
}
