package com.example.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class RegisterDatabase extends RoomDatabase {

    public abstract UserDao myDao();

    private static volatile RegisterDatabase registerDatabase;

    static RegisterDatabase getMyAppDatabase(final Context context) {

        if (registerDatabase == null) {
            synchronized (RegisterDatabase.class) {
                if (registerDatabase == null) {
                    registerDatabase = Room.databaseBuilder(context.getApplicationContext(), RegisterDatabase.class, "myapp_database").build();
                }
            }
        }
        return registerDatabase;
    }


}
