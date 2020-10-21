package com.ibm2105.loyaltyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PointsListAdapter extends RecyclerView.Adapter<PointsListAdapter.ViewHolder> {

    private PointsListItem[] pointsListItems;
    public PointsListAdapter(PointsListItem[] pointsListItems) {
        this.pointsListItems=pointsListItems;
    }

    @NonNull
    @Override
    public PointsListAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View listItem=layoutInflater.inflate(R.layout.points_list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(listItem);
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(parent.getContext(),"Item Redeemed!",Toast.LENGTH_SHORT).show();
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PointsListAdapter.ViewHolder holder, int position) {
        final PointsListItem pointsListItem=pointsListItems[position];
        holder.rewardName.setText(pointsListItems[position].getRewardName());
        holder.rewardImage.setImageResource(pointsListItems[position].getPointsImage());
        holder.pointsValue.setText(pointsListItems[position].getPointsValue()+" points");
    }

    @Override
    public int getItemCount() {
        return pointsListItems.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView rewardName, pointsValue;
        public ImageView rewardImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        this.pointsValue=itemView.findViewById(R.id.pointsValue);
        this.rewardName=itemView.findViewById(R.id.rewardName);
        this.rewardImage=itemView.findViewById(R.id.pointsImg);
        }
    }
}
