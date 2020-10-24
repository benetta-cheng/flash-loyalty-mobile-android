package com.ibm2105.loyaltyapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "codes")
public class Code {

    @PrimaryKey
    @NonNull
    private int code;

    @ColumnInfo(name = "points_value")
    private int pointsValue;

    public Code(@NonNull int code, int pointsValue) {
        this.code = code;
        this.pointsValue = pointsValue;
    }

    @NonNull
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getPointsValue() {
        return pointsValue;
    }

    public void setPointsValue(int pointsValue) {
        this.pointsValue = pointsValue;
    }
}
