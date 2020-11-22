package com.ibm2105.loyaltyapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.ibm2105.loyaltyapp.database.Account;
import com.ibm2105.loyaltyapp.database.AccountDao;
import com.ibm2105.loyaltyapp.database.LoyaltyDatabase;

public class ProfileViewModel extends AndroidViewModel {

    private final AccountDao accountDao;

    private final MutableLiveData<Integer> status;
    private final LiveData<Account> accountLiveData;
    private Account account;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        accountDao = LoyaltyDatabase.getDatabase(getApplication()).accountDao();
        status = new MutableLiveData<>(null);
        accountLiveData = accountDao.findAccountWithId(PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(getApplication().getString(R.string.user_id), -1));
    }

    public void update(String username, String fullName, String dob, String state, String email) {

        if (account.getUsername().equals(username)) {
            account.setFullName(fullName);
            account.setDateOfBirth(dob);
            account.setState(state);
            account.setEmail(email);
            ProfileViewModel.this.update(account);

        } else {
            LiveData<Account> accountWithUsernameLiveData = accountDao.findAccountWithUsername(username);
            accountWithUsernameLiveData.observeForever(new Observer<Account>() {
                @Override
                public void onChanged(Account accountWithUsername) {
                    if (accountWithUsername != null) {
                        status.setValue(R.string.username_exists);

                        // Reset the value right after to prevent re-triggering the toast after resuming the screen
                        status.setValue(null);

                    } else {
                        account.setUsername(username);
                        account.setFullName(fullName);
                        account.setDateOfBirth(dob);
                        account.setState(state);
                        account.setEmail(email);
                        ProfileViewModel.this.update(account);
                    }

                    // Remove the observer right after getting a value since we will only be observing once
                    accountWithUsernameLiveData.removeObserver(this);
                }
            });
        }
    }

    public void updateImage(String image) {
        account.setImage(image);
        ProfileViewModel.this.update(account);
    }

    private void update(Account account) {
        LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
            LoyaltyDatabase.getDatabase(getApplication()).accountDao().updateAccount(account);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                status.setValue(R.string.update_successful);

                // Reset the value right after to prevent re-triggering the toast after resuming the screen
                status.setValue(null);
            });
        });
    }

    public void changePassword(String password) {
        account.setPassword(password);
        this.update(account);
    }

    public LiveData<Account> getAccountLiveData() {
        return accountLiveData;
    }

    public MutableLiveData<Integer> getStatus() {
        return status;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
