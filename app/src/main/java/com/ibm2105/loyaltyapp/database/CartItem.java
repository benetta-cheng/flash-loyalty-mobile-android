package com.ibm2105.loyaltyapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "cart_items", primaryKeys = {"cart_id", "item_id"})
public class CartItem {

    @ColumnInfo(name = "cart_id")
    @NonNull
    private int cartId;

    @ColumnInfo(name = "item_id")
    @NonNull
    private int itemId;

    private int quantity;

    public CartItem(int cartId, int itemId, int quantity) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public int getCartId() {
        return cartId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
