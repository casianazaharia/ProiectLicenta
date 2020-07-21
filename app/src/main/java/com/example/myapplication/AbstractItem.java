package com.example.myapplication;

public abstract class AbstractItem {
    public static final int TYPE_CENTER = 0;
    public static final int TYPE_EDGE = 1;
    public static final int TYPE_EMPTY = 2;

    private ParkingSpot parkingSpot;

    public AbstractItem(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }


    public ParkingSpot getParkingSpot() {

        return parkingSpot;
    }

    abstract public int getType();

}
