package com.yandex.mandrik.launcher.util.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Home on 27.04.2017.
 */

public class UpdateApplicationReceiver extends BroadcastReceiver {

    public UpdateApplicationReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("U", "ACTION " + action);
    }
}
