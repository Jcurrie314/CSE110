package com.cse110.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MyActivity extends AppCompatActivity implements View.OnClickListener {

    Button bLogout;
    EditText etName, etUsername;
    UserLocal userLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        etName = (EditText) findViewById(R.id.etName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        bLogout = (Button) findViewById(R.id.bLogout);

        bLogout.setOnClickListener(this);

        userLocal = new UserLocal(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(authenticate() == true) {

        }

    }

    private boolean authenticate(){
        return userLocal.getUserLoggedIn()
    }

    private void displayUserDetails() {
        User user = userLocal.getLoggedInUser();

        etName.setText(user.name);
        etUsername.setText(user.username);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogout:
                userLocal.clearUserData();
                userLocal.setUserLoggedIn(false);

                startActivity(new Intent(this, Register.class));
                break;
        }
    }


}
