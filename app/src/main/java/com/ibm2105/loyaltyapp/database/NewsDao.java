package com.ibm2105.loyaltyapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insert(News news);

    @Update
    public void updateNews(News news);

    @Query("SELECT * FROM  news")
    LiveData<List<News>> getAllNews();
}
