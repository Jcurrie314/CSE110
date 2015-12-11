package com.parse.tuber;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;


/**
 * Created by jonathancurrie on 10/22/15.
 */

/* Given: I have clicked on a tutor name from the search tab
 *      When: I am not connected to the tutor
 *      Then: I will not be able to see their phone number/ email, but I will be able to see courses, price, and rating.
 *
 *      When: I am connected to the tutor
 *      Then: I will be able to see all information, including contact information
 *
 *
 *      Test:
//           public void testDisplayUserDetails(Boolean verified) {
//        displayUserDetails(verified);
//        if (rbRating.getVisibility() != View.VISIBLE || lvCourses.getVisibility() != View.VISIBLE || tvPrice.getVisibility() != View.VISIBLE) {
//            test1Success = false;
//        } else {
//            test1Success = true;
//        }
//        // TEST 1
//        if (verified == false) {
//            if (fab.getVisibility() == View.GONE && fabRequest.getVisibility() == View.VISIBLE) {
//                test2Success = true;
//            } else {
//                test2Success = false;
//            }
//            // TEST 2
//        } else if (verified == true) {
//            if (fab.getVisibility() == View.VISIBLE && fabRequest.getVisibility() == View.GONE) {
//                test2Success = true;
//            } else {
//                test2Success = false;
//            }
//        }
//        android.support.v7.app.AlertDialog.Builder adb = new android.support.v7.app.AlertDialog.Builder(
//                Profile.this);
//        adb.setTitle("Test Result");
//        if (test1Success == true && test2Success == true) {
//            adb.setMessage("Test 3: PASS \nTest 4: PASS");
//        } else if (test1Success == true && test2Success == false) {
//            adb.setMessage("Test 3: PASS \nTest 4: FAIL");
//        } else if (test1Success == false && test2Success == true) {
//            adb.setMessage("Test 3: FAIL \nTest 4: PASS");
//        } else {
//            adb.setMessage("Test 3: FAIL \nTest 4: FAIL");
//        }
//        adb.show();
//
//    }
 */


public class Profile extends Activity implements View.OnClickListener {
    String userId, relationshipId;


    TextView tvName, tvPrice, tvCoursesLabel, tvPriceLabel, tvRatingLabel, tvNoRatingsLabel;

    ListView lvCourses;
    RatingBar rbRating;
    ImageView ivProfilePicture, ivMenu;
    FloatingActionsMenu fab;
    FloatingActionButton fabRequest;
    MenuItem miChangePassword, miLogout;
    LinearLayout svMain;
    boolean artificiallySet = false;
    boolean test1Success, test2Success;

    String phone;
    String email;


    @Override
    public void onResume() {
        super.onResume();
        isVerified();
        refreshRatingBar();

    }

    @Override
    public void onStart() {
        super.onStart();
        isVerified();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = (TextView) findViewById(R.id.tvName);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvPriceLabel = (TextView) findViewById(R.id.tvPriceLabel);
        tvNoRatingsLabel = (TextView) findViewById(R.id.tvNoRatingsLabel);
        tvNoRatingsLabel.setVisibility(View.GONE);

        rbRating = (RatingBar) findViewById(R.id.rbRating);
        tvCoursesLabel = (TextView) findViewById(R.id.tvCoursesLabel);
        lvCourses = (ListView) findViewById(R.id.lvCourses);

        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        fab = (FloatingActionsMenu) findViewById(R.id.fab);
        fabRequest = (FloatingActionButton) findViewById(R.id.fabRequest);

        ivMenu = (ImageView) findViewById(R.id.ivMenu);

        miChangePassword = (MenuItem) findViewById(R.id.miChangePassword);
        miLogout = (MenuItem) findViewById(R.id.miLogout);


        //fab for delegating to a call to connection
        FloatingActionButton fabPhone = (FloatingActionButton) findViewById(R.id.fabPhone);
        fabPhone.setSize(FloatingActionButton.SIZE_MINI);
        fabPhone.setIcon(R.drawable.ic_phone_black_48dp);
        fabPhone.setStrokeVisible(false);
        fabPhone.setOnClickListener(this);

        //fab for delegating to an email to connection
        FloatingActionButton fabEmail = (FloatingActionButton) findViewById(R.id.fabEmail);
        fabEmail.setSize(FloatingActionButton.SIZE_MINI);
        fabEmail.setIcon(R.drawable.ic_email_black_48dp);
        fabEmail.setStrokeVisible(false);
        fabEmail.setOnClickListener(this);

        fabRequest.setSize(FloatingActionButton.SIZE_NORMAL);
        fabRequest.setIcon(R.drawable.ic_message_black_24dp);
        fabRequest.setStrokeVisible(false);
        fabRequest.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getString("id");
        }

        //Gives user's information from parse to UI elements
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", userId);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            public void done(ParseUser user, com.parse.ParseException e) {

                if (e == null) {

                    if (user != null) {
                        tvName.setText(user.get("name").toString());
                        tvPrice.setText(String.format("$%.2f/hr", (double) user.getDouble("fee")));
                        if (user.getBoolean("tutor") == false) {
                            tvPrice.setVisibility(View.GONE);
                            tvPriceLabel.setVisibility(View.GONE);
                            tvCoursesLabel.setVisibility(View.GONE);
                            lvCourses.setVisibility(View.GONE);
                        }
                        ParseFile imageFile = (ParseFile) user.get("profilePic");
                        if(imageFile != null) {
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
                        } else {
                            //profile pic not default
                        }
                        //Sets fab correctly depending on if users are connected
                        isVerified();
                    }
                }
            }
        });
        //update average rating on parse and locally
        refreshRatingBar();

//        boolean variable =  true;
//        android.support.v7.app.AlertDialog.Builder adb = new android.support.v7.app.AlertDialog.Builder(Profile.this);
//        adb.setTitle("Test Result");
//        if(testDisplayUserDetails(variable) == true){
//            adb.setMessage("Test: Pass");
//        } else {
//            adb.setMessage("Test: Fail");
//        }
//        adb.show();

        test1Success = false;
        test2Success = false;
        //testDisplayUserDetails(true);
    }

    public void refreshRatingBar() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", userId);
        try {
            List<ParseUser> objects = query.find();
            if (objects.size() == 1) {
                double averageRating = objects.get(0).getDouble("rating");
                rbRating.setStepSize(1f);
                if (averageRating == -1) {
                    addRatingRelationforNewTutor();
                    rbRating.setRating(0);
                    rbRating.invalidate();
                    tvNoRatingsLabel.setVisibility(View.VISIBLE);
                } else {
                    tvNoRatingsLabel.setVisibility(View.GONE);

                    rbRating.setRating((float) averageRating);
                }
            }
        } catch (ParseException e) {

        }
    }

    public void isVerified() {
        //Sets Fabs (connect, sendrequest, or edit) based on the two user's connection status

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Relationships");
        findTutorCourses();

        if (!(ParseUser.getCurrentUser().getObjectId()).equals(userId)) {
            query.whereEqualTo("student", ParseUser.getCurrentUser().getObjectId());
            query.whereEqualTo("tutor", userId);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                    if (e == null) {
                        Boolean verified;
                        if (objects.size() > 0) {
                            //Case that user has some connections
                            if (!((Boolean) objects.get(0).get("accepted")) && ((Boolean) objects.get(0).get("requested"))) {
                                //If the users are connected, hide the request connection fab
                                fabRequest.setVisibility(View.GONE);
                            }
                            //sets a boolean indicating whether or not the user whose profile
                            //you are at has verified you
                            verified = (Boolean) objects.get(0).get("accepted");
                            relationshipId = objects.get(0).getObjectId();
                            addListenerOnRatingBar();


                        } else {
                            //case that user has no connections
                            verified = false;
                        }
                        //Displays User details depending on whether or not you are connected
                        displayUserDetails(verified);
                    }

                }
            });
            //hides logout/change password menu if you're not on your own profile
            ivMenu.setVisibility(View.GONE);

        } else {
            //case that user is looking at their own profile
            displayUserDetails(true);
            rbRating.setEnabled(false);
        }
    }


    public void findTutorCourses() {
        //find and display tutor courses from TutorCourseRelation database (will only happen if user is tutor)
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
                                            setListViewHeightBasedOnChildren(lvCourses);
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

    //Expands listview to fit items
    // Source: http://stackoverflow.com/questions/3495890/how-can-i-put-a-listview-into-a-scrollview-without-it-collapsing
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    //Changes average rating based on user-inputted rating
    public void addListenerOnRatingBar() {
        rbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, final float rating,
                                        boolean fromUser) {
                if (!fromUser) {
                    return;
                }
                //yourRating = rating;
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Relationships");
                query.whereEqualTo("student", ParseUser.getCurrentUser().getObjectId());
                query.whereEqualTo("tutor", userId);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e == null) {
                            /* Case that current user has not issued a rating for selected user yet */

                            if (parseObject.get("rating") == null) {

                                ParseQuery<ParseObject> query = ParseQuery.getQuery("TutorRatingRelation");
                                query.whereEqualTo("tutorId", userId);
                                query.getFirstInBackground(new GetCallback<ParseObject>() {

                                    @Override
                                    public void done(ParseObject parseObject, ParseException e) {
                                        if (e == null) {
                                            if (parseObject != null) {
                                                // Simply add rating to total, count and compute average and add back to parse
                                                double newRatingCount = parseObject.getDouble("ratingCount") + 1;
                                                double newRatingTotal = parseObject.getDouble("ratingTotal") + rating;
                                                double newRatingAverage = newRatingTotal / newRatingCount;
                                                parseObject.put("ratingCount", newRatingCount);
                                                parseObject.put("ratingTotal", newRatingTotal);
                                                parseObject.put("ratingAverage", newRatingAverage);

                                                try {
                                                    parseObject.save();
                                                } catch (ParseException e1) {
                                                    e1.printStackTrace();
                                                }
                                                HashMap<String, Object> hashy = new HashMap<String, Object>();
                                                hashy.put("id", userId);
                                                try {
                                                    ParseCloud.callFunction("modifyRating", hashy);
                                                } catch (ParseException e2) {
                                                    e2.printStackTrace();
                                                }
                                                try {
                                                    parseObject.save();
                                                } catch (ParseException e1) {
                                                    e1.printStackTrace();
                                                }
                                                refreshRatingBar();
                                            }

                                        } else {

                                        }
                                    }
                                });


                            } else { // Case that current user has already added a rating and is now changing it
                                final float prevRating = Float.parseFloat(parseObject.get("rating").toString());
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("TutorRatingRelation");
                                query.whereEqualTo("tutorId", userId);
                                query.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject parseObject, ParseException e) {
                                        if (e == null) {
                                            // Change new Rating Total to include the delta of oldRating and newRating
                                            // and take new average
                                            if (parseObject != null) {

                                                double newRatingCount = parseObject.getDouble("ratingCount");
                                                double newRatingTotal = parseObject.getDouble("ratingTotal") - prevRating + rating;
                                                double newRatingAverage;
                                                if (newRatingCount > 0) {
                                                    newRatingAverage = newRatingTotal / newRatingCount;
                                                } else {
                                                    newRatingAverage = 0;
                                                }

                                                parseObject.put("ratingCount", newRatingCount);
                                                parseObject.put("ratingTotal", newRatingTotal);
                                                parseObject.put("ratingAverage", newRatingAverage);
                                                try {
                                                    parseObject.save();
                                                    //updateTutorStats(newRatingCount);
                                                } catch (ParseException e1) {
                                                    e1.printStackTrace();
                                                }

                                                HashMap<String, Object> hashy = new HashMap<String, Object>();
                                                hashy.put("id", userId);

                                                try {
                                                    ParseCloud.callFunction("modifyRating", hashy);
                                                } catch (ParseException e2) {
                                                    e2.printStackTrace();
                                                }

                                                refreshRatingBar();
                                            }
                                        } else {

                                        }
                                    }
                                });

                            }
                            parseObject.put("rating", rating);
                            try {
                                parseObject.save();
                            } catch (ParseException error) {
                                error.printStackTrace();
                            }

                        } else {

                        }
                    }
                });


            }

        });


    }

    public void addRatingRelationforNewTutor() {
        //update user databases' ratings fields depending on ratings database when logged in
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TutorRatingRelation");
        query.whereEqualTo("tutorId", userId);
        try {
            List<ParseObject> parseObjects = query.find();

//            int i = 0;
//            int i2 = i + i;
            if (parseObjects.size() < 1) {
                ParseObject request = new ParseObject("TutorRatingRelation");
                request.put("tutorId", userId);
                request.put("ratingCount", 0);
                request.put("ratingAverage", 0);
                request.put("ratingTotal", 0);
                try {
                    request.save();

                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            } else {

            }
        } catch (ParseException e){
            e.printStackTrace();

        }


    }

    public void showMenu(View v) {
        //Show connections fabs
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

    //Display user details based on verified boolean which indicates whether or not the
    //users are connected (requested and verified)
    public void displayUserDetails(Boolean verified) {
        if (verified) {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("objectId", userId);
            query.getFirstInBackground(new GetCallback<ParseUser>() {
                public void done(ParseUser user, com.parse.ParseException e) {
                    if (e == null) {
                        // The query was successful.
                        // check if we got a match
                        if (user != null) {

                            phone = user.get("phone").toString();
                            email = user.getEmail();
                            Log.d("Email", email);
                        }
                    }
                }
            });

            if ((ParseUser.getCurrentUser().getObjectId()).equals(userId)) {
                //case that current user is viewing their own profile
                fabRequest.setIcon(R.drawable.ic_mode_edit_black_48dp);
                fabRequest.setVisibility(View.VISIBLE);
                fab.setVisibility(View.GONE);
            } else {
                //case that users are connected, but current user!=profile's user
                fabRequest.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        } else {
            //Case that user is not verified, show request fab
            fabRequest.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
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
        //This prevents multiple requests
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Relationships");
        query.whereEqualTo("student", ParseUser.getCurrentUser().getObjectId());
        query.whereEqualTo("tutor", userId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    if (objects.size() < 1) {
                        ParseObject request = new ParseObject("Relationships");
                        request.put("tutor", userId);
                        request.put("student", ParseUser.getCurrentUser().getObjectId());
                        request.put("requested", true);
                        request.put("accepted", false);
                        request.saveInBackground();
                    }
                } else {
                    //Creates new request

                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //If the request fab was clicked, and it's the own user's account, then go into editprofile,
            //if the profile is not the user's, then send a connection request
            case R.id.fabRequest:
                if ((ParseUser.getCurrentUser().getObjectId()).equals(userId)) {
                    Intent editProfileIntent;
                    editProfileIntent = new Intent(this, EditProfile.class);
                    editProfileIntent.putExtra("id", ParseUser.getCurrentUser().getObjectId());
                    startActivity(editProfileIntent);
                } else {
                    requestAccess();
                    Toast.makeText(getApplicationContext(), "Requested information",
                            Toast.LENGTH_LONG).show();
                }
                break;

            //cases that user clicked email/phone fabs, go to phone call/new email
            case R.id.fabPhone:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
                break;
            case R.id.fabEmail:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
                emailIntent.setType("text/plain");
                startActivity(emailIntent);
                break;
        }
    }

    public void testDisplayUserDetails(Boolean verified) {
        displayUserDetails(verified);
        if (rbRating.getVisibility() != View.VISIBLE || lvCourses.getVisibility() != View.VISIBLE || tvPrice.getVisibility() != View.VISIBLE) {
            test1Success = false;
        } else {
            test1Success = true;
        }
        // TEST 1
        if (verified == false) {
            if (fab.getVisibility() == View.GONE && fabRequest.getVisibility() == View.VISIBLE) {
                test2Success = true;
            } else {
                test2Success = false;
            }
            // TEST 2
        } else if (verified == true) {
            if (fab.getVisibility() == View.VISIBLE && fabRequest.getVisibility() == View.GONE) {
                test2Success = true;
            } else {
                test2Success = false;
            }
        }
        android.support.v7.app.AlertDialog.Builder adb = new android.support.v7.app.AlertDialog.Builder(
                Profile.this);
        adb.setTitle("Test Result");
        if (test1Success == true && test2Success == true) {
            adb.setMessage("Test 3: PASS \nTest 4: PASS");
        } else if (test1Success == true && test2Success == false) {
            adb.setMessage("Test 3: PASS \nTest 4: FAIL");
        } else if (test1Success == false && test2Success == true) {
            adb.setMessage("Test 3: FAIL \nTest 4: PASS");
        } else {
            adb.setMessage("Test 3: FAIL \nTest 4: FAIL");
        }
        adb.show();
    }

}
