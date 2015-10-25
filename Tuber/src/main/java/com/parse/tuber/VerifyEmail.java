package com.parse.tuber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

/**
 * Created by jonathancurrie on 10/24/15.
 */
public class VerifyEmail extends ActionBarActivity implements View.OnClickListener {
    Button bMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyemail);

        bMain = (Button) findViewById(R.id.bMain);

        ParseUser currentUser = ParseUser.getCurrentUser();

        bMain.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bMain:
                startActivity(new Intent(this, MainActivity.class));
                break;

        }
    }





}

