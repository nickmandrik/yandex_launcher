package com.yandex.mandrik.launcher.util.eventbus;

import com.yandex.mandrik.launcher.listappsactivity.appdata.AppInfo;

/**
 * Created by Home on 27.04.2017.
 */

public class FavoritesRecyclerChangedEvent {
    public AppInfo appInfo;
    public boolean isInstall;

    public FavoritesRecyclerChangedEvent(AppInfo appInfo, boolean isInstall) {
        this.appInfo = appInfo;
        this.isInstall = isInstall;
    }
}
