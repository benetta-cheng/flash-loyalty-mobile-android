package com.ibm2105.loyaltyapp.prelogin;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.ibm2105.loyaltyapp.R;
import com.ibm2105.loyaltyapp.database.Account;
import com.ibm2105.loyaltyapp.database.AccountDao;
import com.ibm2105.loyaltyapp.database.LoyaltyDatabase;

import android.os.Handler;

public class PreLoginViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> status;
    private final AccountDao accountDao;

    public PreLoginViewModel(@NonNull Application application) {
        super(application);
        status = new MutableLiveData<>(null);
        accountDao = LoyaltyDatabase.getDatabase(getApplication()).accountDao();
    }

    public void login(String username, String password) {
        LiveData<Account> accountLiveData = accountDao.findAccountWithUsername(username);

        accountLiveData.observeForever(new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                if (account != null && account.getPassword().equals(password)) {
                    status.setValue(R.string.login_successful);

                    SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication()).edit();
                    sharedPreferences.putInt(getApplication().getString(R.string.user_id), account.getId());
                    sharedPreferences.apply();
                } else {
                    status.setValue(R.string.incorrect_login);

                    // Reset the value right after to prevent re-triggering the toast after resuming the screen
                    status.setValue(null);
                }

                // Remove the observer right after getting a value since we will only be observing once
                accountLiveData.removeObserver(this);
            }
        });
    }

    public void registerUser(String username, String email, String password) {
        Account newAccount = new Account(username, password, email, null, null, null, null);

        LiveData<Account> accountLiveData = accountDao.findAccountWithUsername(username);

        accountLiveData.observeForever(new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                if (account == null) {
                    LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                        accountDao.insert(newAccount);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(() -> {
                            login(username, password);
                        });
                    });
                } else {
                    status.setValue(R.string.username_exists);

                    // Reset the value right after to prevent re-triggering the toast after resuming the screen
                    status.setValue(null);
                }

                // Remove the observer right after getting a value since we will only be observing once
                accountLiveData.removeObserver(this);
            }
        });

    }

    public void changePassword(String username, String resetCode, String password) {
        LiveData<Account> accountLiveData = accountDao.findAccountWithUsername(username);

        if (resetCode.startsWith("RST")) {
            accountLiveData.observeForever(new Observer<Account>() {
                @Override
                public void onChanged(Account account) {
                    if (account != null) {
                        account.setPassword(password);
                        LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                            LoyaltyDatabase.getDatabase(getApplication()).accountDao().updateAccount(account);
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(() -> {
                                status.setValue(R.string.password_reset_successful);

                                // Reset the value right after to prevent re-triggering the toast after resuming the screen
                                status.setValue(null);
                            });
                        });
                    } else {
                        status.setValue(R.string.invalid_reset_code);

                        // Reset the value right after to prevent re-triggering the toast after resuming the screen
                        status.setValue(null);
                    }

                    // Remove the observer right after getting a value since we will only be observing once
                    accountLiveData.removeObserver(this);
                }
            });
        } else {
            status.setValue(R.string.invalid_reset_code);

            // Reset the value right after to prevent re-triggering the toast after resuming the screen
            status.setValue(null);
        }
    }

    public LiveData<Integer> getStatus() {
        return status;
    }
}
