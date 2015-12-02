package com.parse.tuber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;


/**
 * Created by jonathancurrie on 10/23/15.
 *
 *
 *  Given: I am not signed in to any account
 *  When: I click Sign up/ Register for an account
 *  Then: I am taken to a page where I can enter my information to create an account
 *
 *  Test:
 *
 *  public boolean
 */
public class Landing extends ActionBarActivity implements View.OnClickListener {
    Button bLogin, bRegister;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        //startActivity(new Intent(getApplicationContext(), TabViewActivity.class));

        bLogin = (Button) findViewById(R.id.bLogin);
        bRegister = (Button) findViewById(R.id.bRegister);


        bLogin.setOnClickListener(this);
        bRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bRegister:
                Intent registerIntent = new Intent(this, Register.class);
                startActivity(registerIntent);
                break;
            case R.id.bLogin:
                Intent loginIntent = new Intent(this, Login.class);
                startActivity(loginIntent);
                break;
        }
    }
}
