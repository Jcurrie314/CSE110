package com.parse.tuber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class Register extends AppCompatActivity implements View.OnClickListener{
    EditText etFirstName, etEmail, etUsername, etPassword;
    Spinner sMajor;
    Button bRegister;
    TextView loginLink;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        sMajor = (Spinner) findViewById(R.id.sMajor);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);
        loginLink = (TextView) findViewById(R.id.tvLoginLink);

        bRegister.setOnClickListener(this);
        loginLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRegister:
                String email = etEmail.getText().toString().trim();
                if( email.equals("")) {
                    etEmail.setError("Email is required!");
                } else if(!email.substring(Math.max(0, email.length() - 8)).equals("ucsd.edu")) {
                    etEmail.setError("UCSD email is required");
                } else if ( etUsername.getText().toString().trim().equals("") ){
                    etUsername.setError("Username is required!");
                } else if ( etPassword.getText().toString().trim().equals("")) {
                    etPassword.setError("Password is required!");
                }
                else {
                    registerUser();
                }
                break;
            case R.id.tvLoginLink:
                Intent loginIntent = new Intent(Register.this, Login.class);
                startActivity(loginIntent);
                break;
        }
    }

    private void registerUser() {


        ParseUser user = new ParseUser();

        user.setUsername(etUsername.getText().toString().trim());
        user.setPassword(etPassword.getText().toString().trim());
        user.setEmail(etEmail.getText().toString().trim());

        user.put("major", sMajor.getSelectedItem().toString());
        user.put("name", etFirstName.getText().toString().trim());





        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }
}
