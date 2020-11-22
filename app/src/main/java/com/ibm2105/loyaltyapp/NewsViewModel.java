package com.ibm2105.loyaltyapp;

import android.app.Application;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ibm2105.loyaltyapp.database.LoyaltyDatabase;
import com.ibm2105.loyaltyapp.database.News;
import com.ibm2105.loyaltyapp.database.NewsDao;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {

    private NewsDao newsDao;

    private final MutableLiveData<Integer> status;
    private final LiveData<List<News>> newsLiveData;
    private News news;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        newsDao = LoyaltyDatabase.getDatabase(getApplication()).newsDao();
        status = new MutableLiveData<>(null);
        newsLiveData = newsDao.getAllNews();
    }

    public LiveData<News> accountWithNewsIdLiveData(int newsId) {
        newsDao = LoyaltyDatabase.getDatabase(getApplication()).newsDao();
        return newsDao.findNewsWithId(newsId);
    }

    public LiveData<List<News>> getNewsLiveData() {
        return newsLiveData;
    }

    public void setNews(News news) {
        this.news = news;
    }
}
