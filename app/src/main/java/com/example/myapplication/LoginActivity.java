package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public static final String ARG_USER = "Username";

    private UserDao userDao;
    private EditText editUsername;
    private EditText editPassword;
    private TextView textView;
    private Button registerBtn;
    private Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initDbDao();
        initViews();
        setActions();
    }

    private void initDbDao() {
        userDao = RegisterDatabase.getMyAppDatabase(getApplicationContext()).myDao();
    }

    private void initViews() {
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        textView = findViewById(R.id.textView);
        textView.setText("Login or Create your account");
        registerBtn = findViewById(R.id.registerBtn);
        loginBtn = findViewById(R.id.loginBtn);
    }

    private void setActions() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(register);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = editUsername.getText().toString();

                new SearchInDatabaseAsyncTask().execute(inputUsername);
            }
        });
    }

    private class SearchInDatabaseAsyncTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... strings) {
            String inputUsername = strings[0];
            return userDao.findByUsername(inputUsername);
        }

        @Override
        protected void onPostExecute(User foundUser) {
            super.onPostExecute(foundUser);
            String inputPassword = editPassword.getText().toString();
            if (foundUser != null && inputPassword.equals(foundUser.password)) {
                Intent loggedin = new Intent(getApplicationContext(), AvailableSlotsActivity.class);
                loggedin.putExtra(ARG_USER, foundUser);
                startActivity(loggedin);

            } else {
                Toast.makeText(LoginActivity.this, "Wrong username/password", Toast.LENGTH_SHORT).show();
            }

        }
    }
}



