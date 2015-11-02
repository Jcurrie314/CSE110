package com.parse.tuber;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by jonathancurrie on 10/22/15.
 */
public class Profile extends ActionBarActivity implements View.OnClickListener {
    String userId;

    TextView tvName, tvNotVerified;
    Button bContact;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = (TextView) findViewById(R.id.tvName);
        bContact = (Button) findViewById(R.id.bContact);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getString("id");
        }
        progress = ProgressDialog.show(this, "Loading User",
                null, true);

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
        if (ParseUser.getCurrentUser().getObjectId().toString() != userId) {
            query.whereEqualTo("student", ParseUser.getCurrentUser().getObjectId().toString());
            query.whereEqualTo("tutor", userId);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                    if (e == null) {
                        if (objects.size() > 0) {
                            Boolean verified = (Boolean) objects.get(0).get("accepted");
                            dispayUserDetails(verified);
                        } else {

                        }
                    } else {
                    }

                }
            });
        } else {

        }
    }

    public void dispayUserDetails(Boolean verified) {
        if (verified) {
            bContact.setVisibility(View.GONE);

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
        progress.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bContact:
                break;
        }
    }
}
