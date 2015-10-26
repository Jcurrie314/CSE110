package com.parse.tuber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

  EditText etName, etEmail, etUsername;
  Button bLogout, bChangePassword;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    etUsername = (EditText) findViewById(R.id.etUsername);
    etName = (EditText) findViewById(R.id.etName);
    etEmail = (EditText) findViewById(R.id.etEmail);
    bLogout = (Button) findViewById(R.id.bLogout);
    bChangePassword = (Button) findViewById(R.id.bChangePassword);


    bChangePassword.setOnClickListener(this);
    bLogout.setOnClickListener(this);


  }

  @Override
  public void onClick(View v) {
    switch(v.getId()){
      case R.id.bLogout:
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent loginIntent = new Intent(this, Login.class);
        startActivity(loginIntent);
        break;
      case R.id.bChangePassword:
        Intent changePasswordIntent;
        changePasswordIntent = new Intent(this, ChangePassword.class);
        startActivity(changePasswordIntent);
        break;
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    ParseUser currentUser = ParseUser.getCurrentUser();
    if (currentUser != null) {
      displayUserDetails(currentUser);
    } else {
      ParseUser.logOut();
      Intent landingIntent = new Intent(this, Landing.class);
      startActivity(landingIntent);
    }
  }

  private boolean authenticate() {
   return false;
  }

  private void displayUserDetails(ParseUser currentUser) {
    etUsername.setText(currentUser.getUsername());
    //etName.setText(currentUser.get("name").toString());
    etEmail.setText(currentUser.getEmail());

  }
}
