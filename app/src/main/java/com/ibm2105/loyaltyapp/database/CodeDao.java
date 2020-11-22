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
public interface CodeDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insert(Code code);

    @Update
    public void updateCode(Code code);

    @Delete
    public void deleteCode (Code code);

    @Query("SELECT * FROM codes WHERE code = :code")
    LiveData<Code> findCode(int code);

}
