package com.yandex.mandrik.launcher.listappsactivity.appdata;

import android.graphics.drawable.Drawable;

/**
 * AppInfo contains information about the app.
 * Created by Nick Mandrik on 21.03.2017.
 * @author Nick Mandrik
 */

public class AppInfo {

    private String label = "";
    private String packageName = "";
    private Drawable icon;

    private long lastModified = 0;
    private int countClicks = 0;


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getCountClicks() {
        return countClicks;
    }

    public void setCountClicks(int countClicks) {
        this.countClicks = countClicks;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}