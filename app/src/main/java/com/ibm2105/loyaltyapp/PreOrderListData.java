package com.ibm2105.loyaltyapp;

import android.widget.ImageView;

public class PreOrderListData {
    private int id;
    private String preOrderImage;
    private int itemQuantity;
    private float itemPrice;
    private String itemName;

    public PreOrderListData(int id, String preOrderImage, int itemQuantity, float itemPrice, String itemName) {
        this.id = id;
        this.preOrderImage = preOrderImage;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.itemName = itemName;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPreOrderImage() {
        return preOrderImage;
    }

    public void setPreOrderImage(String preOrderImage) {
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

    public int getId() {
        return id;
    }
}
