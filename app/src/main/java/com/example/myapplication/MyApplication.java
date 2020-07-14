package com.example.myapplication;

import android.app.Application;

import androidx.room.Room;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Room.databaseBuilder(getApplicationContext(), MyAppDatabase.class, "myapp_database").build();
    }
}
