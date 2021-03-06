package com.parse.tuber;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


//
//       GIVEN: I am not signed in to any account
//           WHEN: I enter any combination of incorrect email/passwords
//           THEN: I will not be granted access to the app and I will be able to try again to
//                 enter correct info
//
//           TEST 4:
//
//              public void testLogin() {
//
//                  etUsername.setText("nouser@ucsd.edu");
//                  etPassword.setText("wrongpassword");
//                  String username = etUsername.getText().toString();
//                  String password = etPassword.getText().toString();
//
//                  try {
//                      ParseUser.logIn(username,password);
//                      test1Success = false;
//                  } catch (ParseException e) {
//                      e.printStackTrace();
//                      test1Success = true;
//                  }
//                  ParseUser.logOut();
//
//                  etUsername.setText("");
//                  etPassword.setText("");
//                  username = etUsername.getText().toString();
//                  password = etPassword.getText().toString();
//
//                  try {
//                      ParseUser.logIn(username,password);
//                      test2Success = false;
//                  } catch (ParseException e) {
//                      e.printStackTrace();
//                      test2Success = true;
//                  }
//
//                  ParseUser.logOut();
//
//
//                  //if it gets here that means the user wasn't logged in because they didn't have correct
//                  //credentials therefore the test passed
//                  android.support.v7.app.AlertDialog.Builder adb = new android.support.v7.app.AlertDialog.Builder(
//                  Login.this);
//                      adb.setTitle("Test Result");
//
//                  if(test1Success == true && test2Success == true) {
//                      adb.setMessage("Test 1: PASS \nTest 2: PASS");
//                  } else if(test1Success == true && test2Success == false) {
//                      adb.setMessage("Test 1: PASS \nTest 2: FAIL");
//                  }  else if(test1Success == false && test2Success == true) {
//                      adb.setMessage("Test 1: FAIL \nTest 2: PASS");
//                  } else {
//                      adb.setMessage("Test 1: FAIL \nTest 2: FAIL");
//                  }
//                  adb.show();
//
//              }

public class Login extends ActionBarActivity implements View.OnClickListener {
    Button bLogin;
    TextView registerLink;
    EditText etUsername, etPassword;
    Boolean test1Success, test2Success;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bLogin = (Button) findViewById(R.id.bLoginUser);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        registerLink = (TextView) findViewById(R.id.tvRegisterLink);

        bLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);

        test1Success = false;
        test2Success = false;
        //testLogin();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bLoginUser:
                Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                //User user = new User(username, password);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                authenticate(username, password);
                break;
            case R.id.tvRegisterLink:
                Intent registerIntent = new Intent(Login.this, Register.class);
                startActivity(registerIntent);
                break;
        }
    }

    private void authenticate(String username, String password) {

        ParseUser user = new ParseUser();


        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    Intent registerIntent = new Intent(Login.this, MainActivity.class);
                    startActivity(registerIntent);
                } else {
                    showErrorMessage();

                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder
                .setMessage("Incorrect user details")
                .setCancelable(false)

                .setNegativeButton("Forgot Password", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent forgotPasswordIntent = new Intent(Login.this, ForgotPassword.class);
                        startActivity(forgotPasswordIntent);
                    }
                })
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        etPassword.setText("");
                    }
                });
        AlertDialog alert = dialogBuilder.create();
        alert.show();
    }

    private void showNetworkError() {
        Toast.makeText(getApplicationContext(), "Network Error",
                Toast.LENGTH_LONG).show();
    }


    private void logUserIn(User returnedUser) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void testLogin() {

        etUsername.setText("nouser@ucsd.edu");
        etPassword.setText("wrongpassword");
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        try {
            ParseUser.logIn(username, password);
            test1Success = false;
        } catch (ParseException e) {
            e.printStackTrace();
            test1Success = true;
        }
        ParseUser.logOut();

        etUsername.setText("");
        etPassword.setText("");
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        try {
            ParseUser.logIn(username, password);
            test2Success = false;
        } catch (ParseException e) {
            e.printStackTrace();
            test2Success = true;
        }

        ParseUser.logOut();


        //if it gets here that means the user wasn't logged in because they didn't have correct
        //credentials therefore the test passed
        android.support.v7.app.AlertDialog.Builder adb = new android.support.v7.app.AlertDialog.Builder(
                Login.this);
        adb.setTitle("Test Result");
        if (test1Success == true && test2Success == true) {
            adb.setMessage("Test 1: PASS \nTest 2: PASS");
        } else if (test1Success == true && test2Success == false) {
            adb.setMessage("Test 1: PASS \nTest 2: FAIL");
        } else if (test1Success == false && test2Success == true) {
            adb.setMessage("Test 1: FAIL \nTest 2: PASS");
        } else {
            adb.setMessage("Test 1: FAIL \nTest 2: FAIL");
        }
        adb.show();

    }


}
