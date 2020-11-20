package com.ibm2105.loyaltyapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ibm2105.loyaltyapp.database.Account;
import com.ibm2105.loyaltyapp.database.AccountDao;
import com.ibm2105.loyaltyapp.database.LoyaltyDatabase;

public class MainViewModel extends AndroidViewModel {

    private final LiveData<Account> accountLiveData;
    private final MutableLiveData<Integer> status;

    public MainViewModel(@NonNull Application application) {
        super(application);
        accountLiveData =  LoyaltyDatabase.getDatabase(getApplication()).accountDao().findAccountWithId(PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(getApplication().getString(R.string.user_id), -1));
        status = new MutableLiveData<>(null);
    }

    public void logout() {
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication()).edit();
        sharedPreferences.remove(getApplication().getString(R.string.user_id));
        sharedPreferences.apply();

        status.setValue(R.string.logout_successful);

        // Reset the value right after to prevent re-triggering the toast after resuming the screen
        status.setValue(null);
    }

    public LiveData<Account> getAccountLiveData() {
        return accountLiveData;
    }

    public MutableLiveData<Integer> getStatus() {
        return status;
    }
}
