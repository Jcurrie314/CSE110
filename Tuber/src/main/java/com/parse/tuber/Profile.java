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
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


/**
 * Created by jonathancurrie on 10/22/15.
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

    String phone;
    String email;

    Float ratingCount, ratingTotal, ratingAverage;
    Float yourRating, oldRating;

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        isVerified();
        updateTutorStats();

    }

    @Override
    public void onStart() {  // After a pause OR at startup
        super.onStart();
        isVerified();
        updateTutorStats();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = (TextView) findViewById(R.id.tvName);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvPriceLabel = (TextView) findViewById(R.id.tvPriceLabel);
        tvNoRatingsLabel = (TextView) findViewById(R.id.tvNoRatingsLabel);

        rbRating = (RatingBar) findViewById(R.id.rbRating);
        tvCoursesLabel = (TextView) findViewById(R.id.tvCoursesLabel);
        lvCourses = (ListView) findViewById(R.id.lvCourses);

        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        fab = (FloatingActionsMenu) findViewById(R.id.fab);
        fabRequest = (FloatingActionButton) findViewById(R.id.fabRequest);

        ivMenu = (ImageView) findViewById(R.id.ivMenu);

        miChangePassword = (MenuItem) findViewById(R.id.miChangePassword);
        miLogout = (MenuItem) findViewById(R.id.miLogout);


        FloatingActionButton fabPhone = (FloatingActionButton) findViewById(R.id.fabPhone);
        fabPhone.setSize(FloatingActionButton.SIZE_MINI);
        fabPhone.setIcon(R.drawable.ic_phone_black_48dp);
        fabPhone.setStrokeVisible(false);
        fabPhone.setOnClickListener(this);


        FloatingActionButton fabEmail = (FloatingActionButton) findViewById(R.id.fabEmail);
        fabEmail.setSize(FloatingActionButton.SIZE_MINI);
        fabEmail.setIcon(R.drawable.ic_email_black_48dp);
        fabEmail.setStrokeVisible(false);
        fabEmail.setOnClickListener(this);

        fabRequest.setSize(FloatingActionButton.SIZE_NORMAL);
        fabRequest.setIcon(R.drawable.ic_message_black_24dp);
        fabRequest.setStrokeVisible(false);
        fabRequest.setOnClickListener(this);


        // Test that FAMs containing FABs with visibility GONE do not cause crashes

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
                    if (user != null) {
                        tvName.setText(user.get("name").toString());
                        tvPrice.setText(String.format("$%d/hr", user.get("fee")));
                        if(user.get("tutor") == false) {
                            tvPrice.setVisibility(View.GONE);
                            tvPriceLabel.setVisibility(View.GONE);
                            tvCoursesLabel.setVisibility(View.GONE);
                            lvCourses.setVisibility(View.GONE);
                        }
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
        updateTutorStats();
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            Log.d("The height is", "this");
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

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
                        Boolean verified;
                        if (objects.size() > 0) {
                            if(!((Boolean) objects.get(0).get("accepted")) && ((Boolean) objects.get(0).get("requested") )) {
                                fabRequest.setVisibility(View.GONE);
                            }
                            verified = (Boolean) objects.get(0).get("accepted");
                            relationshipId = objects.get(0).getObjectId();
                            addListenerOnRatingBar();
                            //displayUserDetails(true);


                        } else {
                            verified = false;
                        }
                        displayUserDetails(verified);
                    }

                }
            });
            ivMenu.setVisibility(View.GONE);

        } else {

            //case that user is looking at their own profile
            displayUserDetails(true);
            rbRating.setEnabled(false);

        }
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

    public void addListenerOnRatingBar() {
        rbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                yourRating = rating;
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Relationships");
                query.whereEqualTo("student", ParseUser.getCurrentUser().getObjectId());
                query.whereEqualTo("tutor", userId);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e == null) {
                            if (parseObject.get("rating") == null) {

                                ParseQuery<ParseObject> query = ParseQuery.getQuery("TutorRatingRelation");
                                query.whereEqualTo("tutorId", userId);

                                query.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject parseObject, ParseException e) {
                                        if (e == null) {
                                            float newRatingCount = ratingCount + 1;
                                            float newRatingTotal = ratingTotal + yourRating;
                                            float newRatingAverage = newRatingTotal / newRatingCount;
                                            parseObject.put("ratingCount", newRatingCount);
                                            parseObject.put("ratingTotal", newRatingTotal);
                                            parseObject.put("ratingAverage", newRatingAverage);

                                            try {
                                                parseObject.save();

                                                ParseObject point = ParseObject.createWithoutData("Relationships", relationshipId);
                                                point.put("rating", yourRating);
                                                updateTutorStats();
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }
                                            rbRating.setRating(newRatingAverage);

                                        } else {

                                        }
                                    }
                                });
                            } else {
                                oldRating = Float.parseFloat(parseObject.get("rating").toString());
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("TutorRatingRelation");
                                query.whereEqualTo("tutorId", userId);
                                query.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject parseObject, ParseException e) {
                                        if (e == null) {
                                            float newRatingCount = ratingCount;
                                            float newRatingTotal = ratingTotal - oldRating + yourRating;
                                            float newRatingAverage;
                                            if (newRatingCount > 0) {
                                                newRatingAverage = newRatingTotal / newRatingCount;
                                            } else {
                                                newRatingAverage = 0;
                                            }
                                            Log.d("Old Rating:", String.valueOf(oldRating));
                                            Log.d("New Rating:", String.valueOf(yourRating));
                                            Log.d("Rating Count:", String.valueOf(newRatingCount));
                                            Log.d("Rating Total:", String.valueOf(newRatingTotal));
                                            Log.d("Rating Average:", String.valueOf(newRatingAverage));
                                            parseObject.put("ratingCount", newRatingCount);
                                            parseObject.put("ratingTotal", newRatingTotal);
                                            parseObject.put("ratingAverage", newRatingAverage);
                                            try {
                                                parseObject.save();

                                                ParseObject point = ParseObject.createWithoutData("Relationships", relationshipId);
                                                point.put("rating", yourRating);
                                                updateTutorStats();
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }
                                            rbRating.setRating(newRatingAverage);

                                        } else {

                                        }
                                    }
                                });

                            }
                        } else {

                        }
                    }
                });

            }
        });
    }

    public void updateTutorStats() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TutorRatingRelation");
        query.whereEqualTo("tutorId", userId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    ratingCount = Float.parseFloat(parseObject.get("ratingCount").toString());
                    ratingTotal = Float.parseFloat(parseObject.get("ratingTotal").toString());
                    ratingAverage = Float.parseFloat(parseObject.get("ratingAverage").toString());
                    if((ParseUser.getCurrentUser().getObjectId()).equals(userId)){
                        ParseUser user = ParseUser.getCurrentUser();
                        user.put("rating", ratingAverage);
                        user.put("ratingSum",ratingTotal );
                        user.put("ratingCount", ratingCount);
                        try {
                            user.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                    rbRating.setRating(ratingAverage);

                    rbRating.setStepSize(1f);
                    rbRating.invalidate();
                    if (ratingCount < 1) {
                        tvNoRatingsLabel.setVisibility(View.VISIBLE);
                    } else {
                        tvNoRatingsLabel.setVisibility(View.GONE);
                    }

                } else {
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
                    tvNoRatingsLabel.setVisibility(View.VISIBLE);

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
                fabRequest.setIcon(R.drawable.ic_mode_edit_black_48dp);
                fabRequest.setVisibility(View.VISIBLE);
                fab.setVisibility(View.GONE);
            } else {
                fabRequest.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        } else {
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
                    if(objects.size() < 1){
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
}