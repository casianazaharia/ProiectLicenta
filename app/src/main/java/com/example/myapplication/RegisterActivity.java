package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    UserDao userDao;
    private EditText eName;
    private EditText eUsername;
    private EditText ePassword;
    private EditText eEmail;
    private Button registerBt;
    private TextView textView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initDbDao();
        initViews();
        setActions();
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void initDbDao() {
        userDao = RegisterDatabase.getMyAppDatabase(getApplicationContext()).myDao();
    }

    private void initViews() {
        eName = findViewById(R.id.eName);
        eUsername = findViewById(R.id.eUsername);
        ePassword = findViewById(R.id.ePassword);
        eEmail = findViewById(R.id.eEmail);
        registerBt = findViewById(R.id.registerBt);
        textView2 = findViewById(R.id.textView2);
        textView2.setText("Sign Up");
    }

    private void setActions() {
        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = eUsername.getText().toString();
                String name = eName.getText().toString();
                String password = ePassword.getText().toString();
                String email = eEmail.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {

                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (!validEmail(email)) {

                        Toast.makeText(RegisterActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    } else {

                        User registeredData = new User();
                        registeredData.setUsername(username);
                        registeredData.setPassword(password);
                        registeredData.setName(name);
                        registeredData.setEmail(email);

                        UpdateDatabaseAsyncTask updateDatabaseAsyncTask = new UpdateDatabaseAsyncTask();
                        updateDatabaseAsyncTask.execute(registeredData);
                    }
                }
            }
        });
    }

    private class UpdateDatabaseAsyncTask extends AsyncTask<User, Void, Boolean> {
        @Override
        protected Boolean doInBackground(User... users) {
            boolean userCreatedSuccessful;
            String username = users[0].getUsername();
            if (userDao.findByUsername(username) != null) {
                userCreatedSuccessful = false;
            } else {
                userCreatedSuccessful = true;
                userDao.addUser(users[0]);
            }
            return userCreatedSuccessful;
        }

        @Override
        protected void onPostExecute(Boolean userCreated) {
            super.onPostExecute(userCreated);
            if (userCreated) {
                Intent register = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(register);
            } else {
                Toast.makeText(RegisterActivity.this, "Username already used", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
