package com.parse.tuber;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by jonathancurrie on 10/22/15.
 */
public class Profile extends ActionBarActivity implements View.OnClickListener {
    String userId;

    TextView tvName, tvEmail, tvNotVerified;
    Button bContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvNotVerified = (TextView) findViewById(R.id.tvNotVerified);
        bContact = (Button) findViewById(R.id.bContact);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getString("id");
        }

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", userId);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            public void done(ParseUser user, com.parse.ParseException e) {
                if (e == null) {
                    // The query was successful.
                    // check if we got a match
                    if (user == null) {
                        // no matching user!
                    } else {
                        tvName.setText(user.get("name").toString());
                        isVerified(userId);

                    }
                } else {
                    // Something went wrong.
                }
            }
        });


    }

    public void isVerified(String tutorId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Relationships");
        if (!(ParseUser.getCurrentUser().getObjectId().toString()).equals(userId)) {
            query.whereEqualTo("student", ParseUser.getCurrentUser().getObjectId().toString());
            query.whereEqualTo("tutor", userId);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                    if (e == null) {
                        if (objects.size() > 0) {
                            Boolean verified = (Boolean) objects.get(0).get("accepted");
                            displayUserDetails(verified);
                        } else {

                        }
                    } else {
                    }

                }
            });
        } else {
            //case that user is looking at their own profile
            displayUserDetails(true);
        }
    }

    public void displayUserDetails(Boolean verified) {
        if (verified) {
            bContact.setVisibility(View.GONE);
            tvNotVerified.setVisibility(View.GONE);

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("objectId", userId);
            query.getFirstInBackground(new GetCallback<ParseUser>() {
                public void done(ParseUser user, com.parse.ParseException e) {
                    if (e == null) {
                        // The query was successful.
                        // check if we got a match
                        if (user == null) {
                            // no matching user!
                        } else {

                            tvEmail.setText(user.getEmail().toString());

                        }
                    } else {
                        // Something went wrong.
                    }
                }
            });
        } else {
            tvNotVerified.setVisibility(View.GONE);
            bContact.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bContact:
                break;
        }
    }
}
