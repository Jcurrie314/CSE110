package com.parse.tuber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

/**
 * Created by Juleelala on 10/25/15.
 */
public class ChangePassword extends ActionBarActivity implements View.OnClickListener {

    EditText etOldPassword, etNewPassword, etConfirmNewPassword;
    Button bSubmitNewPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);


        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmNewPassword = (EditText) findViewById(R.id.etConfirmNewPassword);

        bSubmitNewPassword = (Button) findViewById(R.id.bSubmitNewPassword);

        bSubmitNewPassword.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSubmitNewPassword:


                String oldPassword = etOldPassword.getText().toString();

                ParseUser.logInInBackground(ParseUser.getCurrentUser().getUsername(), oldPassword, new LogInCallback() {

                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            // Hooray! The password is correct
                            Toast.makeText(getApplicationContext(), "Old password correct!", Toast.LENGTH_SHORT).show();

                            String newPassword = etNewPassword.getText().toString();
                            String confirmNewPassword = etConfirmNewPassword.getText().toString();

                            if (newPassword.equals(confirmNewPassword)) {
                                Toast.makeText(getApplicationContext(), "Passwords Do Match!",
                                        Toast.LENGTH_SHORT).show();

                                ParseUser currentUser = ParseUser.getCurrentUser();
                                currentUser.setPassword(newPassword);
                                currentUser.saveInBackground();
                                Toast.makeText(getApplicationContext(), "Password Updated!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Passwords Don't match!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Old password not correct", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                break;

        }
    }


}
