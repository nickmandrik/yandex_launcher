package com.yandex.mandrik.launcher.util.layout;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yandex.mandrik.launcher.listappsactivity.appsrecycler.recycler.adapter.ApplicationListAdapter;

/**
 * Created by Home on 03.04.2017.
 */

public class RecyclerSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private int sizeRow;
    private RecyclerView.Adapter adapter;

    public RecyclerSpanSizeLookup(int sizeRow, RecyclerView.Adapter adapter) {
        this.sizeRow = sizeRow;
        this.adapter = adapter;
    }

    @Override
    public int getSpanSize(int position) {
        switch(adapter.getItemViewType(position)){
            case 0:
                return sizeRow;
            case 1:
                return 1;
            case 2:
                return 1;
            case 3:
                return 1;
            default:
                return -1;
        }
    }
}
