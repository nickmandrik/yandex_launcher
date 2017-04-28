package com.yandex.mandrik.launcher.listappsactivity.appsrecycler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yandex.mandrik.launcher.R;
import com.yandex.mandrik.launcher.listappsactivity.appsrecycler.recycler.ApplicationsRecycler;
import com.yandex.mandrik.launcher.listappsactivity.appdata.ApplicationListManager;
import com.yandex.mandrik.launcher.listappsactivity.appsrecycler.recycler.adapter.CustomRVItemTouchListener;
import com.yandex.mandrik.launcher.util.clicker.RecyclerViewItemClickListener;
import com.yandex.mandrik.launcher.util.eventbus.ChangeCountCeilsEvent;
import com.yandex.mandrik.launcher.util.eventbus.FavoritesRecyclerChangedEvent;
import com.yandex.mandrik.launcher.util.eventbus.HideFavoritesEvent;
import com.yandex.mandrik.launcher.util.layout.RecyclerSpanSizeLookup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.yandex.mandrik.launcher.util.constants.LauncherConstants.APP_PREFERENCE_RECYCLER_APPS_SETTINGS;
import static com.yandex.mandrik.launcher.util.constants.LauncherConstants.COUNT_ELEMENTS_IN_ROW_LANDSCAPE;
import static com.yandex.mandrik.launcher.util.constants.LauncherConstants.COUNT_ELEMENTS_IN_ROW_PORTRAIT;
import static com.yandex.mandrik.launcher.util.constants.LauncherConstants.THEME_OF_APPLICATION;

/**
 * Created by Home on 27.04.2017.
 */

public class AppsRecyclerFragment extends Fragment {

    private ApplicationsRecycler appRecyclerView;
    private Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();

        ApplicationListManager manager = new ApplicationListManager(context, getCountInRow());
        appRecyclerView = new ApplicationsRecycler(context, manager);

        appRecyclerView.setHasFixedSize(true);

        appRecyclerView.addOnItemTouchListener(new CustomRVItemTouchListener(context, appRecyclerView,
                new RecyclerViewItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if (appRecyclerView.getAdapter().getAppInfoById(position) != null) {
                            /*remCountClicks(position);*/
                            Intent intent = context.getPackageManager().getLaunchIntentForPackage(
                                    appRecyclerView.getAdapter().getAppInfoById(position).
                                            getPackageName().toString());
                            appRecyclerView.getAdapter().getAppInfoById(position).setCountClicks(
                                    appRecyclerView.getAdapter().getAppInfoById(position)
                                            .getCountClicks() + 1
                            );
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onLongClick(View view, final int position) {
                        if (appRecyclerView.getAdapter().getAppInfoById(position) != null) {
                            /*remCountClicks(position);*/
                            view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                                @Override
                                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                                    menu.add(0, v.getId(), 0, R.string.add_to_favorite);
                                    menu.add(0, v.getId(), 1, R.string.see_info);
                                    menu.add(0, v.getId(), 2, R.string.delete);

                                    menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem item) {
                                            EventBus bus = EventBus.getDefault();
                                            bus.post(new FavoritesRecyclerChangedEvent
                                                    (appRecyclerView.getAdapter().getAppInfoById(position), true));
                                            return false;
                                        }
                                    });

                                    menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem item) {
                                            Uri uri = Uri.fromParts("package", appRecyclerView.getAdapter()
                                                    .getAppInfoById(position).getPackageName(), null);
                                            Intent intent = new Intent(Intent.ACTION_ALL_APPS, uri);
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            startActivity(intent);
                                            return false;
                                        }
                                    });

                                    menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem item) {
                                            Uri uri = Uri.fromParts("package", appRecyclerView.getAdapter()
                                                    .getAppInfoById(position).getPackageName(), null);
                                            Intent it = new Intent(Intent.ACTION_DELETE, uri);
                                            startActivity(it);
                                            //appRecyclerView.getAdapter().remove(position);
                                            return false;
                                        }
                                    });
                                }
                            });
                        }
                    }
                }));
        setLayoutManagerOnRecycler();

        appRecyclerView.setLayoutParams(new StaggeredGridLayoutManager.LayoutParams(
                StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        return appRecyclerView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLayoutManagerOnRecycler();
        appRecyclerView.getApplicationListManager().updateNewApps(getCountInRow());
        appRecyclerView.getApplicationListManager().updatePopularApps(getCountInRow());
    }

    private void setLayoutManagerOnRecycler() {
        int countInRow = getCountInRow();

        GridLayoutManager layoutManager = new GridLayoutManager(context, countInRow, GridLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new RecyclerSpanSizeLookup(countInRow, appRecyclerView.getAdapter()));

        appRecyclerView.setLayoutManager(layoutManager);
    }

    public int getCountInRow() {
        SharedPreferences appSettings = context.getSharedPreferences
                (APP_PREFERENCE_RECYCLER_APPS_SETTINGS, Context.MODE_PRIVATE);

        int countInRow = appSettings.getInt(COUNT_ELEMENTS_IN_ROW_LANDSCAPE, 6);
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            countInRow = appSettings.getInt(COUNT_ELEMENTS_IN_ROW_PORTRAIT, 4);
        }

        return countInRow;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeCountCeilsEvent(ChangeCountCeilsEvent event) {
        setLayoutManagerOnRecycler();
        appRecyclerView.notifyAll();
    }
}
