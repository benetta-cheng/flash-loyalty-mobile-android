package com.ibm2105.loyaltyapp;

import android.app.Application;
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
import com.ibm2105.loyaltyapp.database.Code;
import com.ibm2105.loyaltyapp.database.CodeDao;
import com.ibm2105.loyaltyapp.database.LoyaltyDatabase;
import com.ibm2105.loyaltyapp.database.Notification;
import com.ibm2105.loyaltyapp.database.NotificationDao;
import com.ibm2105.loyaltyapp.database.Reward;
import com.ibm2105.loyaltyapp.database.RewardDao;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class PointsViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> status;
    public PointsViewModel(@NonNull Application application) {
        super(application);
        status = new MutableLiveData<>(null);
    }

    public LiveData<List<Reward>> getAllRewards(){
        LoyaltyDatabase database = LoyaltyDatabase.getDatabase(getApplication());
        RewardDao rewardDao = database.rewardDao();
        return rewardDao.getAllRewards();
    }

    public void redeemCode(int code){
        LoyaltyDatabase database = LoyaltyDatabase.getDatabase(getApplication());
        CodeDao codeDao = database.codeDao();
        AccountDao accountDao = database.accountDao();
        LiveData<Code> codeLiveData = codeDao.findCode(code);
        codeLiveData.observeForever(new Observer<Code>() {
            @Override
            public void onChanged(Code code) {
                if(code!=null){
                    LiveData<Account> accountLiveData = accountDao.findAccountWithId(PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt("UserID",-1));
                    accountLiveData.observeForever(new Observer<Account>() {
                        @Override
                        public void onChanged(Account account) {
                            account.setTotalPoints(account.getTotalPoints()+code.getPointsValue());
                            LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                                LoyaltyDatabase.getDatabase(getApplication()).accountDao().updateAccount(account);
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String formattedDate = df.format(Calendar.getInstance().getTime());
                                LoyaltyDatabase.getDatabase(getApplication()).notificationDao().insert(new Notification(account.getId(),"Redeemed a code!", "You have redeemed a code worth " + code.getPointsValue() + " points!", formattedDate));
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(() -> {
                                    status.setValue(R.string.code_successful);
                                    status.setValue(null);
                                });
                                codeDao.deleteCode(code);
                            });
                            accountLiveData.removeObserver(this);
                        }
                    });
                }
                else{
                    LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(() -> {
                            status.setValue(R.string.code_fail);
                            status.setValue(null);
                        });
                    });
                }
                codeLiveData.removeObserver(this);
            }
        });
    }

    public void redeemReward (Reward reward){
        LoyaltyDatabase database = LoyaltyDatabase.getDatabase(getApplication());
        RewardDao rewardDao = database.rewardDao();
        NotificationDao notificationDao = database.notificationDao();
        AccountDao accountDao = database.accountDao();
        LiveData<Account> accountLiveData = accountDao.findAccountWithId(PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt("UserID",-1));
        accountLiveData.observeForever(new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                if ( reward.getPoints() <= account.getTotalPoints()){
                    account.setTotalPoints(account.getTotalPoints()-reward.getPoints());
                    LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                        LoyaltyDatabase.getDatabase(getApplication()).accountDao().updateAccount(account);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(() -> {
                            status.setValue(R.string.reward_successful);
                            status.setValue(null);
                        });
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(Calendar.getInstance().getTime());
                        notificationDao.insert(new Notification(account.getId(),"Redeemed a reward!", "You have redeemed a "+reward.getRewardName(), formattedDate));
                    });
                }
                else{
                    LoyaltyDatabase.databaseWriteExecutor.execute(() -> {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(() -> {
                            status.setValue(R.string.reward_fail);
                            status.setValue(null);
                        });
                    });
                }
                accountLiveData.removeObserver(this);
            }
        });
    }

    public LiveData<Account> getAccount (){
        LoyaltyDatabase database = LoyaltyDatabase.getDatabase(getApplication());
        AccountDao accountDao=database.accountDao();
        return accountDao.findAccountWithId(PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt("UserID",-1));
    }

    public MutableLiveData<Integer> getStatus() {
        return status;
    }
}
