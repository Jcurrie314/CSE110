package com.parse.tuber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by jonathancurrie on 10/22/15.
 */
public class Profile extends ActionBarActivity implements View.OnClickListener {
    String userId, relationshipId;


    double averageRating;

    TextView tvName, tvEmail, tvEmailLabel, tvNotVerified, tvRatingLabel, lvCoursesLabel;
    ListView lvCourses;
    RatingBar rbRating;
    Button bContact, bLogout, bChangePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bLogout = (Button) findViewById(R.id.bLogout);
        bChangePassword = (Button) findViewById(R.id.bChangePassword);


        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvEmailLabel = (TextView) findViewById(R.id.tvEmailLabel);
        rbRating = (RatingBar) findViewById(R.id.rbRating);
        tvRatingLabel = (TextView) findViewById(R.id.tvRatingLabel);
        lvCourses = (ListView) findViewById(R.id.lvCourses);
        lvCoursesLabel = (TextView) findViewById(R.id.lvCoursesLabel);


        tvNotVerified = (TextView) findViewById(R.id.tvNotVerified);
        bContact = (Button) findViewById(R.id.bContact);

        bContact.setOnClickListener(this);
        bChangePassword.setOnClickListener(this);
        bLogout.setOnClickListener(this);

        bChangePassword.setVisibility(View.GONE);
        bLogout.setVisibility(View.GONE);


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
        findTutorCourses();
        if (!(ParseUser.getCurrentUser().getObjectId().toString()).equals(userId)) {
            query.whereEqualTo("student", ParseUser.getCurrentUser().getObjectId().toString());
            query.whereEqualTo("tutor", userId);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                    if (e == null) {
                        if (objects.size() > 0) {
                            Boolean verified = (Boolean) objects.get(0).get("accepted");
                            relationshipId = objects.get(0).getObjectId();
                            addListenerOnRatingBar();
                            displayUserDetails(verified);

                        } else {
                            displayUserDetails(false);
                        }
                    } else {
                    }

                }
            });


        } else {

            bChangePassword.setVisibility(View.VISIBLE);
            bLogout.setVisibility(View.VISIBLE);
            bContact.setVisibility(View.GONE);

            //case that user is looking at their own profile
            displayUserDetails(true);


        }
    }

    public void getRating(String userId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Relationships");
        query.whereEqualTo("tutor", userId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        int sum = 0;
                        for (int i = 0; i < objects.size(); i++) {
                            sum += objects.get(i).getInt("rating");
                        }

                        averageRating = (double) sum / objects.size();
                    } else {
                        displayUserDetails(false);
                    }


                    rbRating.setStepSize(0.5f);
                    rbRating.setRating(Float.parseFloat(String.valueOf(averageRating)));
                    rbRating.invalidate();
                } else {
                }

            }
        });

    }

    public void findTutorCourses() {
        final ArrayAdapter<CourseBundle> listAdapter = new ArrayAdapter<CourseBundle>(this,
                android.R.layout.simple_list_item_1);
        lvCourses.setAdapter(listAdapter);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TutorCourseRelation");
        query.whereEqualTo("tutor", userId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (int i = 0; i < objects.size(); i++) {
                            final String grade = objects.get(i).get("grade").toString();
                            final String id = objects.get(i).getObjectId();

                            ParseQuery<ParseObject> nameQuery = ParseQuery.getQuery("Courses");
                            nameQuery.whereEqualTo("objectId", objects.get(i).get("course").toString());
                            nameQuery.findInBackground(new FindCallback<ParseObject>() {

                                @Override
                                public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                                    if (e == null) {
                                        if (objects.size() > 0) {
                                            final CourseBundle c = new CourseBundle();
                                            c.name = objects.get(0).get("department") + " " + objects.get(0).get("number");
                                            c.id = id;
                                            c.grade = grade;
                                            listAdapter.add(c);
                                        }
                                    } else {
                                    }
                                }
                            });

                        }
                    }
                } else {
                    //Something failed
                }
            }
        });

    }


    public void addListenerOnRatingBar() {

        rbRating = (RatingBar) findViewById(R.id.rbRating);
        rbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ParseObject point = ParseObject.createWithoutData("Relationships", relationshipId);

                point.put("rating", rating);

                point.saveInBackground(new SaveCallback() {
                    public void done(com.parse.ParseException e) {
                        if (e == null) {

                        } else {
                            // The save failed.
                        }
                    }
                });

            }
        });
    }

    public void displayUserDetails(Boolean verified) {
        getRating(userId);
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

                    }
                }
            });
        } else {
            tvNotVerified.setVisibility(View.VISIBLE);
            bContact.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.GONE);
            tvEmailLabel.setVisibility(View.GONE);
            rbRating.setEnabled(false);

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogout:
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
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
}
