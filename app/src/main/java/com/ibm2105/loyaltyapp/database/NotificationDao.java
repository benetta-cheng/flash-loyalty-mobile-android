package com.ibm2105.loyaltyapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Notification notification);

    @Query("SELECT * FROM notifications WHERE user_id = :userId")
    LiveData<List<Notification>> getAllNotificationsForUser(int userId);
}
