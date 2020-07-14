package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //    UserLocalStore userLocalStore;
    MyDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editUsername = (EditText) findViewById(R.id.editUsername);
        final EditText editPassword = (EditText) findViewById(R.id.editPassword);

        TextView textView;
        textView = findViewById(R.id.textView);
        textView.setText("Login or Create your account");

//        userLocalStore = new UserLocalStore(this);
        userDao = MyAppDatabase.getMyAppDatabase(getApplicationContext()).myDao();

        Button registerbtn = (Button) findViewById(R.id.registerbtn);
        final Button logbtn = (Button) findViewById(R.id.logbtn);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent register = new Intent(getApplicationContext(), Registration.class);
                startActivity(register);
            }
        });


        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputUsername = editUsername.getText().toString();
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(inputUsername);
            }
        });
    }


   private class MyAsyncTask extends AsyncTask<String, Void, User> {
       @Override
       protected User doInBackground(String... strings) {
           String inputUsername = strings[0];
           return userDao.findByUsername(inputUsername);
       }

       @Override
       protected void onPostExecute(User foundUser) {
           super.onPostExecute(foundUser);
           if (foundUser == null) {
               Toast.makeText(MainActivity.this, "Wrong username/password", Toast.LENGTH_SHORT).show();
           } else {
               Intent loggedin = new Intent(getApplicationContext(), AvailableSlots.class);
               startActivity(loggedin);
           }

       }
   }

}



