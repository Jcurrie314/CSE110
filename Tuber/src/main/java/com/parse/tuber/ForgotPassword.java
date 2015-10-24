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
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.text.ParseException;

/**
 * Created by jonathancurrie on 10/23/15.
 */
public class ForgotPassword extends ActionBarActivity implements View.OnClickListener {
    EditText etEmail;
    Button bSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        bSend = (Button) findViewById(R.id.bSend);
        etEmail = (EditText) findViewById(R.id.etEmail);

        bSend.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSend:
                String email = etEmail.getText().toString();

                ParseUser.requestPasswordResetInBackground(email,
                        new RequestPasswordResetCallback() {
                            @Override
                            public void done(com.parse.ParseException e) {
                                if (e == null) {
                                    Toast.makeText(getApplicationContext(), "Email sent",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Send failed",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                break;
            case R.id.tvRegisterLink:
                Intent registerIntent = new Intent(ForgotPassword.this, Login.class);
                startActivity(registerIntent);
                break;
        }
    }





}
