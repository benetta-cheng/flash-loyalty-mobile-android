package com.ibm2105.loyaltyapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Account.class, Notification.class, Cart.class, CartItem.class, History.class}, version = 1, exportSchema = false)
public abstract class LoyaltyDatabase extends RoomDatabase {

    public abstract AccountDao accountDao();
    public abstract NotificationDao notificationDao();
    public abstract CartDao cartDao();
    public abstract CartItemDao cartItemDao();
    public abstract HistoryDao historyDao();

    private static volatile LoyaltyDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static LoyaltyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LoyaltyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), LoyaltyDatabase.class, "flash_loyalty_database").build();
                }
            }
        }

        return INSTANCE;
    }
}
