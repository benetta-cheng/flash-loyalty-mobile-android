package com.ibm2105.loyaltyapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CartDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Cart cart);

    @Update
    public void updateCart(Cart cart);

    @Delete
    public void delete(Cart cart);

    @Query("SELECT * FROM carts WHERE user_id = :userId")
    LiveData<Cart> getCartForUser(int userId);

}
