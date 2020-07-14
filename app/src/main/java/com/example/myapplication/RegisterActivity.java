package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
                /*Intent resultIntent = new Intent();*/

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
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
        });
    }

    private class UpdateDatabaseAsyncTask extends AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... users) {
            userDao.addUser(users[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent register = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(register);
        }
    }

}
