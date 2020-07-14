package com.example.myapplication;

import android.app.Application;

import androidx.room.Room;

public class CarParkingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Room.databaseBuilder(getApplicationContext(), RegisterDatabase.class, "myapp_database").build();
    }
}
