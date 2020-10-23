package com.ibm2105.loyaltyapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(CartItem cartItem);

    @Update
    public void updateCartItem(CartItem cartItem);

    @Delete
    public void deleteCartItem(CartItem cartItem);

    @Query("SELECT * FROM cart_items WHERE cart_id = :cartId")
    LiveData<List<CartItem>> getAllCartItemForCart(int cartId);

    @Query("SELECT * FROM cart_items WHERE cart_id = :cartId AND item_id = :itemId")
    LiveData<CartItem> getCartItemFromCartIDAndItemId(int cartId, int itemId);
}
