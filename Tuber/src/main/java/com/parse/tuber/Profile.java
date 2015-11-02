package com.parse.tuber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by jonathancurrie on 10/22/15.
 */
public class Profile extends ActionBarActivity implements View.OnClickListener {
    String value;

    TextView tvName;
    Button bContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = (TextView) findViewById(R.id.tvName);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("id");
            Toast.makeText(getBaseContext(), value, Toast.LENGTH_LONG).show();

        }

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", value);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            public void done(ParseUser user, com.parse.ParseException e) {
                if (e == null) {
                    // The query was successful.
                    // check if we got a match
                    if (user == null) {
                        // no matching user!
                    } else {
                        isVerified(user.get("name").toString());
                        tvName.setText(user.get("name").toString());
                    }
                } else {
                    // Something went wrong.
                }
            }
        });


    }

    public void isVerified(String tutorId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Relationships");
        query.whereEqualTo("tutor", tutorId);
        query.whereEqualTo("student", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    Boolean verified = (Boolean) objects.get(0).get("accepted");
                    Toast.makeText(getApplicationContext(), verified.toString(), Toast.LENGTH_LONG).show();

                } else {
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bContact:
                break;
        }
    }
}
