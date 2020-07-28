package com.example.myapplication;

public class EdgeItem extends AbstractItem {
    public EdgeItem(ParkingSpot parkingSpot) {
        super(parkingSpot);
    }



    @Override
    public int getType() {
        return TYPE_EDGE;
    }
}
