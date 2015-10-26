package com.parse.tuber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Button bLogout, bChangePassword;
    ListView lvTutors;
    ArrayAdapter<String> listAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bLogout = (Button) findViewById(R.id.bLogout);
        bChangePassword = (Button) findViewById(R.id.bChangePassword);
        lvTutors = (ListView) findViewById(R.id.lvTutors);


        bChangePassword.setOnClickListener(this);
        bLogout.setOnClickListener(this);



        // Find the ListView resource.
        lvTutors = (ListView) findViewById( R.id.lvTutors );

        // Create and populate a List of planet names.
        final String[] planets = new String[] { };
        final ArrayList<String> planetList = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, planetList);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        listAdapter.add(objects.get(i).getUsername().toString());
                    }
                } else {
                    // Something went wrong.
                }
            }
        });

        planetList.addAll(Arrays.asList(planets));

        // Set the ArrayAdapter as the ListView's adapter.
        lvTutors.setAdapter( listAdapter );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    }
}
