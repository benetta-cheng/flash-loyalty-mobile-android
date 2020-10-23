package com.ibm2105.loyaltyapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "accounts")
public class Account {

    @PrimaryKey
    @NonNull
    private String username;

    private String password;
    private String email;
    private String state;

    @ColumnInfo(name = "full_name")
    private String fullName;

    @ColumnInfo(name = "date_of_birth")
    private String dateOfBirth;

    @ColumnInfo(name = "total_points")
    private int totalPoints;

    public Account(@NonNull String username, String password, String email, String fullName, String dateOfBirth, String state, int totalPoints) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.state = state;
        this.totalPoints = totalPoints;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getState() {
        return state;
    }

    public int getTotalPoints() {
        return totalPoints;
    }
}
