package com.ibm2105.loyaltyapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ibm2105.loyaltyapp.database.LoyaltyDatabase;
import com.ibm2105.loyaltyapp.database.Reward;

import java.util.ArrayList;
import java.util.List;

public class PointsListAdapter extends RecyclerView.Adapter<PointsListAdapter.ViewHolder> {
    List<Reward>listReward;
    PointsViewModel pointsViewModel;

    public PointsListAdapter(PointsViewModel pointsViewModel) {
        listReward = new ArrayList<>();
        this.pointsViewModel=pointsViewModel;
    }

    public void setListReward(List<Reward> listReward) {
        this.listReward = listReward;
    }

    @NonNull
    @Override
    public PointsListAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View listItem=LayoutInflater.from(parent.getContext()).inflate(R.layout.points_list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PointsListAdapter.ViewHolder holder, int position) {
        Reward reward;
        reward=listReward.get(position);
        //convert image
        byte[] decodeString = Base64.decode(reward.getImage(), android.util.Base64.DEFAULT);
        Bitmap decoded = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
        //set holder
        holder.rewardName.setText(reward.getRewardName());
        holder.rewardImage.setImageBitmap(decoded);
        holder.pointsValue.setText(reward.getPoints()+" points");
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pointsViewModel.redeemReward(reward);

            }
        });
    }


    @Override
    public int getItemCount() {
        return listReward.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView rewardName, pointsValue;
        public ImageView rewardImage;
        public ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.pointsValue=itemView.findViewById(R.id.pointsValue);
            this.rewardName=itemView.findViewById(R.id.rewardName);
            this.rewardImage=itemView.findViewById(R.id.pointsImg);
            this.constraintLayout=itemView.findViewById(R.id.pointListItem);
        }
    }
}
