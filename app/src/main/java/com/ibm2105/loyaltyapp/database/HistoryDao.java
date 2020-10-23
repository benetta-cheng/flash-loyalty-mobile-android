package com.ibm2105.loyaltyapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(History history);

    @Query("SELECT * FROM histories WHERE user_id = :userId")
    LiveData<List<History>> getAllHistoriesForUser(int userId);
}
