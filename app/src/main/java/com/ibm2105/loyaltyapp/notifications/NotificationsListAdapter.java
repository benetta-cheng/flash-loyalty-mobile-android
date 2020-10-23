package com.ibm2105.loyaltyapp.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ibm2105.loyaltyapp.R;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.ViewHolder> {
    private NotificationsListData[] listdata;

    public NotificationsListAdapter(NotificationsListData[] listdata) {
        this.listdata = listdata;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    @NonNull
    @Override
    public NotificationsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.notifications_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    @Override
    public void onBindViewHolder(@NonNull NotificationsListAdapter.ViewHolder holder, int position) {
        holder.textViewNotificationsTitle.setText(listdata[position].getTitle());
        holder.textViewNotificationsTime.setText(listdata[position].getTime());
        holder.textViewNotificationsDescription.setText(listdata[position].getDescription());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNotificationsTitle;
        public TextView textViewNotificationsTime;
        public TextView textViewNotificationsDescription;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewNotificationsTitle = (TextView) itemView.findViewById(R.id.textViewNotificationsTitle);
            this.textViewNotificationsTime = (TextView) itemView.findViewById(R.id.textViewNotificationsTime);
            this.textViewNotificationsDescription = (TextView) itemView.findViewById(R.id.textViewNotificationsDescription);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
        }
    }
}
