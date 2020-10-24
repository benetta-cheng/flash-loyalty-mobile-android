package com.ibm2105.loyaltyapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insert(Account account);

    @Update
    public void updateAccount(Account account);

    @Query("SELECT * FROM accounts WHERE username = :username")
    LiveData<Account> findAccountWithUsername(String username);

    @Query("SELECT * FROM accounts WHERE id = :id")
    LiveData<Account> findAccountWithId(int id);
}
