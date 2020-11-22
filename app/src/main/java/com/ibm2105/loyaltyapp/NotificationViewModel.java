package com.ibm2105.loyaltyapp;

import android.app.Application;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ibm2105.loyaltyapp.database.LoyaltyDatabase;
import com.ibm2105.loyaltyapp.database.Notification;
import com.ibm2105.loyaltyapp.database.NotificationDao;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {

    private final NotificationDao notificationDao;

    private final MutableLiveData<Integer> status;
    private final LiveData<List<Notification>> notificationLiveData;
    private Notification notification;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        notificationDao = LoyaltyDatabase.getDatabase(getApplication()).notificationDao();
        status = new MutableLiveData<>(null);
        notificationLiveData = notificationDao.getAllNotificationsForUser(PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(getApplication().getString(R.string.user_id), -1));
    }
    public LiveData<List<Notification>> getNotificationLiveData() {
        return notificationLiveData;
    }

    public void setNotifications(Notification notification) {
        this.notification = notification;
    }
}
