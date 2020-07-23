package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DbDao {

    @Insert
    public Void addUser(User user);

    @Query("SELECT * FROM USERS WHERE username=:username")
    User findByUsername(String username);

    @Query("UPDATE USERS SET parkingNo = :parkingSpotNo  WHERE username= :username ")
    public int updateUserSelectedParkingSpot(String username, int parkingSpotNo);

    @Query("SELECT * FROM USERS WHERE parkingNo=:parkingNo")
    int findByParkingSpotNo(int parkingNo);

    @Query("UPDATE ParkingSpot SET isAvailable = 'false', isBookedByUsername = :bookedByUsername WHERE spotNo = :parkingSpotNo")
    public int updateBookedParkingSpot(int parkingSpotNo, String bookedByUsername);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllParkingSpots(List<ParkingSpot> listOfParkingSpots);

    @Query("SELECT * FROM ParkingSpot")
    List<ParkingSpot> getAllParkingSpots();
}
