package com.ibm2105.loyaltyapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rewards")
public class Reward {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "reward_id")
    private int rewardId;

    @ColumnInfo(name = "reward_name")
    private String rewardName;

    private int points;
    private String image;

    public Reward(String rewardName, int points, String image) {
        this.rewardName = rewardName;
        this.points = points;
        this.image = image;
    }

    @NonNull
    public int getRewardId() {
        return rewardId;
    }

    public void setRewardId(int rewardId) {
        this.rewardId = rewardId;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
