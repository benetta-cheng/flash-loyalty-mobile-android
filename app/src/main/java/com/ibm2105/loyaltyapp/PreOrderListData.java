package com.ibm2105.loyaltyapp;

import android.widget.ImageView;

public class PreOrderListData {
    private int preOrderImage;
    private int itemQuantity;
    private int itemPrice;
    private String itemName;

    public PreOrderListData(int preOrderImage, int itemQuantity, int itemPrice, String itemName) {
        this.preOrderImage = preOrderImage;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPreOrderImage() {
        return preOrderImage;
    }

    public void setPreOrderImage(int preOrderImage) {
        this.preOrderImage = preOrderImage;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        if (itemQuantity>=0){
            this.itemQuantity = itemQuantity;
        }
    }
}
