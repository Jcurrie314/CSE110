package com.parse.tuber;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by jonathancurrie on 10/22/15.
 */
public class Profile extends Activity implements View.OnClickListener {
    String userId, relationshipId;

    double averageRating;

    EditText tvEmail, tvPhone, tvPrice;
    TextView tvName, tvEmailLabel, tvCoursesLabel, tvPhoneLabel, tvPriceLabel;

    ListView lvCourses;
    RatingBar rbRating;
    ImageView ivProfilePicture, ivMenu;
    FloatingActionButton bContact, bSave;

    MenuItem miChangePassword, miLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (EditText) findViewById(R.id.tvEmail);
        tvEmailLabel = (TextView) findViewById(R.id.tvEmailLabel);
        tvPhone = (EditText) findViewById(R.id.tvPhone);
        tvPhoneLabel = (TextView) findViewById(R.id.tvPhoneLabel);
        tvPrice = (EditText)findViewById(R.id.tvPrice);
        tvPriceLabel = (TextView) findViewById(R.id.tvPriceLabel);


        rbRating = (RatingBar) findViewById(R.id.rbRating);
        tvCoursesLabel = (TextView) findViewById(R.id.tvCoursesLabel);
        lvCourses = (ListView) findViewById(R.id.lvCourses);

        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        bContact = (FloatingActionButton) findViewById(R.id.bContact);
        bSave = (FloatingActionButton) findViewById(R.id.bSave);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);

        miChangePassword = (MenuItem) findViewById(R.id.miChangePassword);
        miLogout = (MenuItem) findViewById(R.id.miLogout);


        bContact.setOnClickListener(this);
        bSave.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getString("id");
        }
        updateTutorStats();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", userId);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            public void done(ParseUser user, com.parse.ParseException e) {

                if (e == null) {
                    // The query was successful.
                    // check if we got a match
                    if (user != null) {
                        tvName.setText(user.get("name").toString());

                        ParseFile imageFile = (ParseFile) user.get("profilePic");
                        if (imageFile != null) {

                            imageFile.getDataInBackground(new GetDataCallback() {
                                public void done(byte[] data, com.parse.ParseException e) {
                                    if (e == null) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        ivProfilePicture.setImageBitmap(bitmap);


                                    }
                                }
                            });
                        }
                        isVerified();
                    }
                }
            }
        });
    }


    public void isVerified() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Relationships");
        findTutorCourses();
        if (!(ParseUser.getCurrentUser().getObjectId()).equals(userId)) {

            query.whereEqualTo("student", ParseUser.getCurrentUser().getObjectId());
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
                    }

                }
            });
            ivMenu.setVisibility(View.GONE);

        } else {
            bContact.setImageResource(R.drawable.ic_add_black_48dp);

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
                        //ParseUser.getCurrentUser().setObjectId("rating") = averageRating;
                    } else {
                        displayUserDetails(false);
                    }


                    rbRating.setStepSize(0.5f);
                    rbRating.setRating(Float.parseFloat(String.valueOf(averageRating)));
                    rbRating.invalidate();
                }

            }
        });

    }

    public void findTutorCourses() {
        final ArrayAdapter<CourseBundle> listAdapter = new ArrayAdapter<>(this,
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
                                    }
                                }
                            });

                        }
                    }
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
                            updateTutorStats();
                        }
                    }
                });

            }
        });
    }

    public void updateTutorStats() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Relationships");
        query.whereEqualTo("tutor", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {

                    final double ratingCount = objects.size();
                    double ratingSum = 0;
                    for (int i = 0; i < objects.size(); i++) {
                        ratingSum += objects.get(i).getDouble("rating");
                    }
                    final double ratingTotal = ratingSum;
                    final double averageRating = ratingSum / ratingCount;


                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
                    query.getFirstInBackground(new GetCallback<ParseUser>() {
                        public void done(ParseUser user, com.parse.ParseException e) {

                            if (e == null) {

                                user.put("ratingcount", ratingCount);
                                user.put("ratingsum", ratingTotal);
                                user.put("rating", averageRating);
                                user.saveInBackground();

                            }
                        }
                    });

                    rbRating.setStepSize(0.5f);
                    rbRating.setRating(Float.parseFloat(String.valueOf(averageRating)));
                    rbRating.invalidate();
                }

            }
        });

    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miChangePassword:
                        changePassword();
                        return true;
                    case R.id.miLogout:
                        logout();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.menu_main);
        popup.show();
    }


    public void displayUserDetails(Boolean verified) {
        getRating(userId);
        if (verified) {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("objectId", userId);
            query.getFirstInBackground(new GetCallback<ParseUser>() {
                public void done(ParseUser user, com.parse.ParseException e) {
                    if (e == null) {
                        // The query was successful.
                        // check if we got a match
                        if (user != null) {
                            tvEmail.setText(user.getEmail(),TextView.BufferType.EDITABLE);
                            tvPhone.setText(user.get("phone").toString(),TextView.BufferType.EDITABLE);
                            tvPrice.setText(String.format("$%d/hr", user.get("fee")),TextView.BufferType.EDITABLE);
                        }
                    }
                }
            });
        } else {
            bContact.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.GONE);
            tvEmailLabel.setVisibility(View.GONE);
            tvPhone.setVisibility(View.GONE);
            tvPhoneLabel.setVisibility(View.GONE);
            rbRating.setEnabled(false);

        }

    }

    public void logout() {
        ParseUser.logOut();
        Intent loginIntent = new Intent(this, Login.class);
        startActivity(loginIntent);
    }

    public void changePassword() {
        Intent changePasswordIntent;
        changePasswordIntent = new Intent(this, ChangePassword.class);
        startActivity(changePasswordIntent);
    }

    public void requestAccess() {
        ParseObject request = new ParseObject("Relationships");
        request.put("tutor", userId);
        request.put("student", ParseUser.getCurrentUser().getObjectId());
        request.put("requested", true);
        request.put("accepted", false);
        request.saveInBackground();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bContact:
                Toast.makeText(getApplicationContext(), "Requested their contact information",
                        Toast.LENGTH_LONG).show();
                requestAccess();
                Log.d("contact", "contact was pressed");
                break;
            case R.id.bSave:
                ParseUser user = ParseUser.getCurrentUser();
                String email = tvEmail.getText().toString();
                user.put("email",email);
                String phone = tvPhone.getText().toString();
                user.put("phone",phone);

                user.saveInBackground();

                Toast.makeText(getApplicationContext(), "Information Saved",
                        Toast.LENGTH_LONG).show();


                break;
        }

    }
}