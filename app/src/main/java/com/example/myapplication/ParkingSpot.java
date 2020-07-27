package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "ParkingSpot")
public class ParkingSpot {

    @PrimaryKey
    @ColumnInfo(name = "spotNo")
    public int spotNo;

    @ColumnInfo(name = "isAvailable")
    boolean isAvailable;

    @ColumnInfo(name = "isSelected")
    boolean isSelected;

    @ColumnInfo(name = "isBooked")
    boolean isBooked;

    @Nullable
    @ColumnInfo(name = "isBookedByUsername")
    String isBookedByUsername;

    public int getSpotNo() { return spotNo; }

    public void setSpotNo(int spotNo) { this.spotNo = spotNo; }

    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) { isAvailable = available; }

    public boolean isSelected() { return isSelected; }

    public void setSelected(boolean selected) { isSelected = selected; }

    public boolean isBooked() { return isBooked; }

    public void setBooked(boolean booked) { isBooked = booked; }

    @Nullable
    public String getIsBookedByUsername() {
        return isBookedByUsername;
    }

    public void setIsBookedByUsername(String isBookedByUsername) {
        this.isBookedByUsername = isBookedByUsername;
    }
}
