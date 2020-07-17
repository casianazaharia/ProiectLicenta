package com.example.myapplication;

public class CenterItem extends AbstractItem {
    public CenterItem(String label) {
        super(label);
    }


    @Override
    public int getType() {
        return TYPE_CENTER;
    }
}
