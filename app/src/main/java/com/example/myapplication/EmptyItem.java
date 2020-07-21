package com.example.myapplication;

public class EmptyItem extends AbstractItem {

    public EmptyItem(ParkingSpot parkingSpot) {
        super(parkingSpot);
    }


    @Override
    public int getType() {
        return TYPE_EMPTY;
    }
}
