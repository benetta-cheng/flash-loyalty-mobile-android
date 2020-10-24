package com.ibm2105.loyaltyapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RewardDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insert(Reward reward);

    @Update
    public void updateReward(Reward reward);

    @Query("SELECT * FROM  rewards")
    LiveData<List<Reward>> getAllRewards();
}
