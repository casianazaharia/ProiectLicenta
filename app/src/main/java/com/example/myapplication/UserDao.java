package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Insert
    public Void addUser(User user);

    @Query("SELECT * FROM USERS WHERE username=:username")
    User findByUsername(String username);

    @Query("UPDATE USERS SET parkingNo = :parkingSpotNo  WHERE username= :username ")
    public int insertSelectedParkingNo (String username, int parkingSpotNo);


}
