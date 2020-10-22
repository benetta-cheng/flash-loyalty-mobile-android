package com.ibm2105.loyaltyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PreOrderConfirmationListAdapter extends RecyclerView.Adapter<PreOrderConfirmationListAdapter.ViewHolder> {
    private PreOrderListData[] listData;

    public PreOrderConfirmationListAdapter(PreOrderListData[] listData) { this.listData = listData; }

    @NonNull
    @Override
    public PreOrderConfirmationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_order_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PreOrderListData listDataItem = listData[position];
        holder.textViewItemAmount.setText(String.valueOf(listDataItem.getItemQuantity()));
        holder.textViewItemPrice.setText("RM" + listDataItem.getItemPrice());
        holder.textViewItemName.setText(String.valueOf(listDataItem.getItemName()));
        holder.imageView.setImageResource(listDataItem.getPreOrderImage());
        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listDataItem.setItemQuantity(listDataItem.getItemQuantity()+1);
                holder.textViewItemAmount.setText(String.valueOf(listDataItem.getItemQuantity()));
            }
        });
        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listDataItem.setItemQuantity(listDataItem.getItemQuantity()-1);
                holder.textViewItemAmount.setText(String.valueOf(listDataItem.getItemQuantity()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textViewItemName;
        public TextView textViewItemPrice;
        public TextView textViewItemAmount;
        public Button plusButton;
        public Button minusButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.confirmationImageView);
            this.textViewItemName = itemView.findViewById(R.id.textViewItemName);
            this.textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);
            this.textViewItemAmount= itemView.findViewById(R.id.textViewItemAmount);
            this.plusButton = itemView.findViewById(R.id.floatingActionButton);
            this.minusButton = itemView.findViewById(R.id.floatingActionButton2);
        }
    }
}

