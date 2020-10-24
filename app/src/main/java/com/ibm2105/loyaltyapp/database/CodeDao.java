package com.ibm2105.loyaltyapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CodeDao {
    
    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insert(Code code);

    @Update
    public void updateCode(Code code);

    @Query("SELECT * FROM codes WHERE code = :code")
    Code findCode(int code);
}
