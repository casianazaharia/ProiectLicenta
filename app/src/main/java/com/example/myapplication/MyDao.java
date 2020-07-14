package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MyDao {

    @Insert
    public Void addUser(User user);

    @Query("SELECT * FROM USERS WHERE username=:username")
    User findByUsername(String username);

}
