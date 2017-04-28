package com.yandex.mandrik.launcher.listappsactivity.appsfavorities.recycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yandex.mandrik.launcher.R;
import com.yandex.mandrik.launcher.listappsactivity.appdata.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter that use the views with appList image and text.
 * Created by Nick Mandrik on 21.03.2017.
 * @author Nick Mandrik
 */

public class FavoritesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<AppInfo> favoriteAppsList = new ArrayList();
    private String header = "Favorites";

    public class ApplicationViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image;
        protected TextView text;

        public ApplicationViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image_id);
            text = (TextView) itemView.findViewById(R.id.text_id);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        protected View line;
        protected TextView text;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            line = itemView.findViewById(R.id.line_view);
            text = (TextView) itemView.findViewById(R.id.section);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return 0;
        } else if(position < favoriteAppsList.size() + 1) {
            return 1;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {
            case 0:
                View v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.section_header, parent, false);
                HeaderViewHolder holder = new HeaderViewHolder(v);
                return holder;
            case 1:
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_app_view_on_recycler, parent, false);
                ApplicationViewHolder viewHolder = new ApplicationViewHolder(view);
                return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(holder.getItemViewType()) {
            case 0:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                if(position == 0) {
                    headerViewHolder.text.setText(header);
                }
                break;
            case 1:
                ApplicationViewHolder viewHolder = (ApplicationViewHolder) holder;
                viewHolder.image.setImageDrawable(getAppInfoById(position).getIcon());
                viewHolder.text.setText(getAppInfoById(position).getLabel());
                break;
        }
    }


    @Override
    public int getItemCount() {
        return favoriteAppsList.size() + 1;
    }

    /**
     * Remove a RecyclerView item by AppInfo position on recycler
     */
    public void remove(int position) {
        int id = getItemViewType(position);
        switch(id) {
            case 0:
                break;
            case 1:
                favoriteAppsList.remove(position-1);
                notifyItemRemoved(position);
                break;
            default:
                break;
        }
    }

    public AppInfo getAppInfoById(int position) {
        int id = getItemViewType(position);
        switch(id) {
            case 0:
                return null;
            case 1:
                return favoriteAppsList.get(position - 1);
            default:
                return null;
        }
    }

    public void setHeader(String header) {
        this.header = header;
    }
}