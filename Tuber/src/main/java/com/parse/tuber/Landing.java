package com.parse.tuber;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by jonathancurrie on 10/23/15.
 */
public class Landing extends ActionBarActivity implements View.OnClickListener {
    Button bLogin, bRegister;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

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
