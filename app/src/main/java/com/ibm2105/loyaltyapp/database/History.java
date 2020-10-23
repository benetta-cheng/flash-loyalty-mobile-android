package com.ibm2105.loyaltyapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "histories")
public class History {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "user_id")
    @NonNull
    private int userId;

    private String total;
    private String date;

    public History(int userId, String total, String date) {
        this.userId = userId;
        this.total = total;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }
}
