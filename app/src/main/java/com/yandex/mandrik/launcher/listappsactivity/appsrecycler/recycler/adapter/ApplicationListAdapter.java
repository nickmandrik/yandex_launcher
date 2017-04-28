package com.yandex.mandrik.launcher.listappsactivity.appsrecycler.recycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yandex.mandrik.launcher.listappsactivity.appdata.AppInfo;

import com.yandex.mandrik.launcher.R;
import com.yandex.mandrik.launcher.listappsactivity.appdata.ApplicationListManager;

/**
 * Adapter that use the views with appList image and text.
 * Created by Nick Mandrik on 21.03.2017.
 * @author Nick Mandrik
 */

public class ApplicationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ApplicationListManager manager;

    public ApplicationListAdapter(ApplicationListManager manager) {
        this.manager = manager;
    }

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
        } else if(position < manager.getPopularAppsList().size() + 1) {
            return 1;
        } else if(position == manager.getPopularAppsList().size() + 1) {
            return 0;
        } else if(position < manager.getPopularAppsList().size() + manager.getNewAppsList().size() + 2) {
            return 2;
        } else if(position == manager.getPopularAppsList().size() + manager.getNewAppsList().size() + 2) {
            return 0;
        } else if(position < manager.getPopularAppsList().size() +
                manager.getNewAppsList().size() + 3 + manager.getAppsList().size()) {
            return 3;
        }
        return 3;
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
            case 2:
            case 3:
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
                    headerViewHolder.text.setText(manager.getHeaders()[0]);
                } else if(position == manager.getPopularAppsList().size() + 1){
                    headerViewHolder.text.setText(manager.getHeaders()[1]);
                } else if(position == manager.getPopularAppsList().size() + 2 + manager.getNewAppsList().size()){
                    headerViewHolder.text.setText(manager.getHeaders()[2]);
                }
                break;
            case 2:
                ApplicationViewHolder viewHold = (ApplicationViewHolder) holder;
                viewHold.image.setImageDrawable(getAppInfoById(position).getIcon());
                viewHold.text.setText(getAppInfoById(position).getLabel());
                break;
            case 1:
            case 3:
                ApplicationViewHolder viewHolder = (ApplicationViewHolder) holder;
                viewHolder.image.setImageDrawable(getAppInfoById(position).getIcon());
                viewHolder.text.setText(getAppInfoById(position).getLabel());
                break;
        }
    }


    @Override
    public int getItemCount() {
        return manager.getAppsList().size() + manager.getNewAppsList().size() + manager.getPopularAppsList().size() + 3;
    }

    /**
     * Remove a RecyclerView item by AppInfo position on recycler
     */
    public void remove(int position) {
        int id = getItemViewType(position);
        int posr = getIndexInArray(position);
        switch(id) {
            case 0:
                break;
            case 1:

                if(manager.getAppsList().contains(manager.getPopularAppsList().get(posr))) {
                    int pos = manager.getAppsList().indexOf(manager.getPopularAppsList().get(posr));
                    manager.getAppsList().remove(manager.getPopularAppsList().get(posr));
                    notifyItemRemoved(pos + manager.getNewAppsList().size() + manager.getPopularAppsList().size() + 3);
                }
                if(manager.getNewAppsList().contains(manager.getPopularAppsList().get(posr))) {
                    int pos = manager.getNewAppsList().indexOf(manager.getPopularAppsList().get(posr));
                    manager.getNewAppsList().remove(manager.getPopularAppsList().get(posr));
                    notifyItemRemoved(pos + manager.getPopularAppsList().size() + 2);
                }
                manager.getPopularAppsList().remove(posr);
                notifyItemRemoved(position);
                break;
            case 2:

                AppInfo appInfo2 = manager.getNewAppsList().get(posr);
                int kof2 = 0;
                if(manager.getAppsList().contains(appInfo2)) {
                    int pos = manager.getAppsList().indexOf(appInfo2);
                    manager.getAppsList().remove(appInfo2);
                    notifyItemRemoved(pos + manager.getNewAppsList().size() + manager.getPopularAppsList().size() + 3);
                }
                if(manager.getPopularAppsList().contains(appInfo2)) {
                    int pos = manager.getPopularAppsList().indexOf(appInfo2);
                    manager.getPopularAppsList().remove(appInfo2);
                    notifyItemRemoved(pos + 1);
                    kof2 ++;
                }
                manager.getNewAppsList().remove(posr);
                notifyItemRemoved(position - kof2);
                break;
            case 3:
                AppInfo appInfo3 = manager.getAppsList().get(posr);
                int kof = 0;
                if(manager.getNewAppsList().contains(appInfo3)) {
                    int pos = manager.getNewAppsList().indexOf(appInfo3);
                    manager.getNewAppsList().remove(appInfo3);
                    notifyItemRemoved(pos + manager.getPopularAppsList().size() + 2);
                    kof ++;
                }
                if(manager.getPopularAppsList().contains(appInfo3)) {
                    int pos = manager.getPopularAppsList().indexOf(appInfo3);
                    manager.getPopularAppsList().remove(appInfo3);
                    notifyItemRemoved(pos + 1);
                    kof ++;
                }
                manager.getAppsList().remove(appInfo3);
                notifyItemRemoved(position - kof);
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
                return manager.getPopularAppsList().get(position - 1);
            case 2:
                return manager.getNewAppsList().get(position - manager.getPopularAppsList().size() - 2);
            case 3:
                return manager.getAppsList().get(position - manager.getPopularAppsList().size() -
                        manager.getNewAppsList().size() - 3);
            default:
                return null;
        }
    }

    public int getIndexInArray(int position) {
        int id = getItemViewType(position);
        switch(id) {
            case 0:
                return -1;
            case 1:
                return position - 1;
            case 2:
                return position - manager.getPopularAppsList().size() - 2;
            case 3:
                return position - manager.getPopularAppsList().size() -
                        manager.getNewAppsList().size() - 3;
            default:
                return -1;
        }
    }
}