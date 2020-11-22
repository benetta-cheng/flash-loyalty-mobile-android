package com.ibm2105.loyaltyapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PreOrderConfirmationListAdapter extends RecyclerView.Adapter<PreOrderConfirmationListAdapter.ViewHolder> {
    private List<PreOrderListData> listData;
    private PreOrderViewModel viewModel;

    public PreOrderConfirmationListAdapter(List<PreOrderListData> listData, PreOrderViewModel viewModel) {
        this.listData = listData;
        this.viewModel = viewModel;
    }

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
        final PreOrderListData listDataItem = listData.get(position);
        holder.textViewItemAmount.setText(String.valueOf(listDataItem.getItemQuantity()));
        holder.textViewItemPrice.setText("RM" + String.format("%.2f", listDataItem.getItemPrice()));
        holder.textViewItemName.setText(String.valueOf(listDataItem.getItemName()));

        byte[] decodedString = Base64.decode(listDataItem.getPreOrderImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imageView.setImageBitmap(bitmap);

        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listDataItem.setItemQuantity(listDataItem.getItemQuantity() + 1);
                viewModel.incrementItemQuantity(listDataItem.getId(), listData);
                holder.textViewItemAmount.setText(String.valueOf(listDataItem.getItemQuantity()));
            }
        });
        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listDataItem.getItemQuantity() > 1) {
                    listDataItem.setItemQuantity(listDataItem.getItemQuantity() - 1);
                    viewModel.decrementItemQuantity(listDataItem.getId(), listData);
                    holder.textViewItemAmount.setText(String.valueOf(listDataItem.getItemQuantity()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setListData(List<PreOrderListData> listData) {
        this.listData = listData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
            this.textViewItemAmount = itemView.findViewById(R.id.textViewItemAmount);
            this.plusButton = itemView.findViewById(R.id.floatingActionButton);
            this.minusButton = itemView.findViewById(R.id.floatingActionButton2);
        }
    }
}

