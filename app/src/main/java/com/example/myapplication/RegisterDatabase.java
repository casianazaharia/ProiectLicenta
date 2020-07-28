package com.example.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {User.class, ParkingSpot.class}, version = 1)
public abstract class RegisterDatabase extends RoomDatabase {

    public abstract DbDao myDao();

    private static volatile RegisterDatabase registerDatabase;

    static RegisterDatabase getMyAppDatabase(final Context context) {

        if (registerDatabase == null) {
            synchronized (RegisterDatabase.class) {
                if (registerDatabase == null) {
                    registerDatabase = Room.databaseBuilder(context.getApplicationContext(), RegisterDatabase.class, "myapp_database")
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull final SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            final List<ParkingSpot> parkingSpotList = new ArrayList<>();
                                            for (int i = 0; i < 40; i++) {
                                                ParkingSpot spot = new ParkingSpot();
                                                spot.spotNo = i + 1;
                                                spot.isAvailable = i % 7 != 0;
                                                parkingSpotList.add(spot);
                                            }
                                            RegisterDatabase.getMyAppDatabase(context).myDao().insertAllParkingSpots(parkingSpotList);
                                        }
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return registerDatabase;
    }


}
