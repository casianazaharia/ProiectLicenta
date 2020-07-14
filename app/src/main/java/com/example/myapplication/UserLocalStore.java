package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {

    public static final String SP_Name = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {

        userLocalDatabase = context.getSharedPreferences(SP_Name, 0);
    }

    public void storeUserData(User user) {

        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("username", user.username);
        spEditor.putString("password", user.password);
        spEditor.putString("name", user.name);
        spEditor.putString("email", user.email);
        spEditor.commit();
    }

    public User getLoggedIn() {

        String name = userLocalDatabase.getString("name", "");
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");
        String email = userLocalDatabase.getString("email", "");

        User storedUser = new User();
        storedUser.setUsername(username);
        storedUser.setPassword(password);
        storedUser.setName(name);
        storedUser.setEmail(email);
        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn) {

        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn() {
        return userLocalDatabase.getBoolean("loggedIn", false) == true;
    }

    public void clearUserData() {

        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }


}
