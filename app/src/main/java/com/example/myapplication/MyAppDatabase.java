package com.example.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class MyAppDatabase extends RoomDatabase {

    public abstract MyDao myDao();

    private static volatile MyAppDatabase myAppDatabase;

    static MyAppDatabase getMyAppDatabase(final Context context) {

        if (myAppDatabase == null) {
            synchronized (MyAppDatabase.class) {
                if (myAppDatabase == null) {
                    myAppDatabase = Room.databaseBuilder(context.getApplicationContext(), MyAppDatabase.class, "myapp_database").build();
                }
            }
        }
        return myAppDatabase;
    }


}
