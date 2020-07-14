package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Trace;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registration extends AppCompatActivity {
//    UserLocalStore userLocalStore;
    MyDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
//        userLocalStore = new UserLocalStore(this);
        userDao = MyAppDatabase.getMyAppDatabase(getApplicationContext()).myDao();

        final TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText("Sign Up");

        final EditText eName = findViewById(R.id.eName);
        final EditText eUsername = findViewById(R.id.eUsername);
        final EditText ePassword = findViewById(R.id.ePassword);
        final EditText eEmail = findViewById(R.id.eEmail);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = eUsername.getText().toString();
                String name = eName.getText().toString();
                String password = ePassword.getText().toString();
                String email = eEmail.getText().toString();
                /*Intent resultIntent = new Intent();*/

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                    Toast.makeText(Registration.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {

                    User registeredData = new User();
                    registeredData.setUsername(username);
                    registeredData.setPassword(password);
                    registeredData.setName(name);
                    registeredData.setEmail(email);

                    RegAsyncTask regAsyncTask = new RegAsyncTask();
                    regAsyncTask.execute(registeredData);

                }

            }
        });
    }

    private class RegAsyncTask extends AsyncTask<User, Void, Void >{
        @Override
        protected Void doInBackground(User... users) {
            userDao.addUser(users[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent register = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(register);
        }
    }

}
