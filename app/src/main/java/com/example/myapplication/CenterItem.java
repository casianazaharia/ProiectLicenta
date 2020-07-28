package com.example.myapplication;

public class CenterItem extends AbstractItem {
    public CenterItem(ParkingSpot parkingSpot) {
        super(parkingSpot);
    }


    @Override
    public int getType() {
        return TYPE_CENTER;
    }
}
