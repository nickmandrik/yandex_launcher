package com.yandex.mandrik.launcher.settingsactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yandex.mandrik.launcher.R;
import com.yandex.mandrik.launcher.util.eventbus.ChangeCountCeilsEvent;
import com.yandex.mandrik.launcher.util.eventbus.ClearFavoritesEvent;
import com.yandex.mandrik.launcher.util.eventbus.HideFavoritesEvent;

import org.greenrobot.eventbus.EventBus;

import static com.yandex.mandrik.launcher.util.constants.LauncherConstants.*;

/**
 * Created by Home on 28.04.2017.
 */

public class SettingsActivity extends AppCompatActivity {

    /**
     * The context of represent this activity
     */
    Context context;

    RecyclerView settingsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        context = SettingsActivity.this;
    }

    // +
    public void hideFavorites(View view) {
        SharedPreferences appSettings = context.getSharedPreferences
                (APP_PREFERENCE_RECYCLER_APPS_SETTINGS, Context.MODE_PRIVATE);

        boolean isHiddenFavorites = appSettings.getBoolean(IS_HIDDEN_FAVORITES, false);

        boolean newIsHiddenFavorites = !isHiddenFavorites;

        SharedPreferences.Editor e = appSettings.edit();
        e.putBoolean(IS_HIDDEN_FAVORITES, newIsHiddenFavorites);
        e.apply();

        EventBus.getDefault().post(new HideFavoritesEvent());
    }

    // +
    public void clearFavorites(View view) {
        EventBus.getDefault().post(new ClearFavoritesEvent());
    }

    // +
    public void changeTheme(View view) {
        SharedPreferences appSettings = context.getSharedPreferences
                (APP_PREFERENCE_RECYCLER_APPS_SETTINGS, Context.MODE_PRIVATE);

        String theme = appSettings.getString(THEME_OF_APPLICATION, "Theme.AmberTheme.Light");

        String newTheme = theme;
        if(theme.equals("Theme.AmberTheme.Light")) {
            newTheme = "Theme.AmberTheme";
            setTheme(R.style.Theme_AmberTheme_Light);
        }
        if(theme.equals("Theme.AmberTheme")) {
            newTheme = "Theme.AmberTheme.Light";
            setTheme(R.style.Theme_AmberTheme);
        }

        SharedPreferences.Editor e = appSettings.edit();
        e.putString(THEME_OF_APPLICATION, newTheme);
        e.apply();
    }

    // +-
    public void changeCountCeils(View view) {
        SharedPreferences appSettings = context.getSharedPreferences
                (APP_PREFERENCE_RECYCLER_APPS_SETTINGS, Context.MODE_PRIVATE);

        int countElementsLandscape = appSettings.getInt(COUNT_ELEMENTS_IN_ROW_LANDSCAPE, 6);
        int countElementsPortrait = appSettings.getInt(COUNT_ELEMENTS_IN_ROW_PORTRAIT, 4);


        int newCountElementsLandscape = countElementsLandscape;
        int newCountElementsPortrait = countElementsPortrait;

        if(countElementsLandscape == 6) {
            newCountElementsLandscape = 7;
        } else if(countElementsLandscape == 7) {
            newCountElementsLandscape = 6;
        }
        if(countElementsPortrait == 5) {
            newCountElementsPortrait = 4;
        } else if(countElementsPortrait == 4) {
            newCountElementsPortrait = 5;
        }

        SharedPreferences.Editor e = appSettings.edit();
        e.putInt(COUNT_ELEMENTS_IN_ROW_LANDSCAPE, newCountElementsLandscape);
        e.putInt(COUNT_ELEMENTS_IN_ROW_PORTRAIT, newCountElementsPortrait);
        e.apply();

        EventBus.getDefault().post(new ChangeCountCeilsEvent());
    }

    // +
    public void clearHistoryUri(View view) {
        SharedPreferences appSettings = context.getSharedPreferences
                (APP_PREFERENCE_MEMORABLE_URI, Context.MODE_PRIVATE);

        SharedPreferences.Editor e = appSettings.edit();
        e.putInt(COUNT_URI_IN_SETTING, 0);
        e.apply();
    }
}