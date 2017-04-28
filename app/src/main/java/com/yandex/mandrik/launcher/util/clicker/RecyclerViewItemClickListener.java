package com.yandex.mandrik.launcher.util.clicker;

import android.view.View;

/**
 * Created by Home on 22.03.2017.
 */

public interface RecyclerViewItemClickListener {
    public void onClick(View view, int position);

    public void onLongClick(View view, int position);
}