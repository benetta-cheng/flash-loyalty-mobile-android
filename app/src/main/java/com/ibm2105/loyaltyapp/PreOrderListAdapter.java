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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PreOrderListAdapter extends RecyclerView.Adapter<PreOrderListAdapter.ViewHolder> {
    //
    private List<PreOrderListData> listData;

    //
    public PreOrderListAdapter(List<PreOrderListData> listData) { this.listData = listData; }

    @NonNull
    @Override
    public PreOrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PreOrderListData listDataItem = listData.get(position);
        holder.textView.setText(String.valueOf(listDataItem.getItemQuantity()));

        byte[] decodedString = Base64.decode(listDataItem.getPreOrderImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imageView.setImageBitmap(bitmap);

        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listDataItem.setItemQuantity(listDataItem.getItemQuantity()+1);
                holder.textView.setText(String.valueOf(listDataItem.getItemQuantity()));
            }
        });
        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listDataItem.setItemQuantity(listDataItem.getItemQuantity()-1);
                holder.textView.setText(String.valueOf(listDataItem.getItemQuantity()));
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textView;
        public Button plusButton;
        public Button minusButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView4);
            this.textView = itemView.findViewById(R.id.textView2);
            this.plusButton = itemView.findViewById(R.id.floatingActionButton);
            this.minusButton = itemView.findViewById(R.id.floatingActionButton2);
        }
    }
}
