package com.ibm2105.loyaltyapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insert(Item item);

    @Update
    public void updateItem(Item item);

    @Query("SELECT * FROM  items")
    LiveData<List<Item>> getAllItems();
}
