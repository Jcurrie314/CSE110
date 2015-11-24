package com.parse.tuber;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jonathancurrie on 10/22/15.
 */
public class EditProfile extends Activity implements View.OnClickListener {
    String userId, relationshipId;

    double averageRating;
    private static final int SELECT_PHOTO = 100;


    TextView tvCoursesLabel, tvPhoneLabel, tvPriceLabel;
    EditText etName, etPhone, etPrice;
    ListView lvCourses;
    ImageView ivProfilePicture, ivMenu;
    FloatingActionButton fab;
    Button bUpload;
    byte[] image;
    Button bAddCourse;


    MenuItem miChangePassword, miLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        InputMethodManager immPhone = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        immPhone.showSoftInput(etPhone, InputMethodManager.SHOW_IMPLICIT);
        tvPhoneLabel = (TextView) findViewById(R.id.tvPhoneLabel);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etPrice.requestFocus();
        InputMethodManager immPrice = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        immPrice.showSoftInput(etPrice, InputMethodManager.SHOW_IMPLICIT);
        tvPriceLabel = (TextView) findViewById(R.id.tvPriceLabel);
        bUpload = (Button) findViewById(R.id.bUpload);
        bUpload.setOnClickListener(this);

        bAddCourse = (Button) findViewById(R.id.bAddCourse);
        bAddCourse.setOnClickListener(this);


        tvCoursesLabel = (TextView) findViewById(R.id.tvCoursesLabel);
        lvCourses = (ListView) findViewById(R.id.lvCourses);

        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);

        miChangePassword = (MenuItem) findViewById(R.id.miChangePassword);
        miLogout = (MenuItem) findViewById(R.id.miLogout);


        fab.setOnClickListener(this);

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
                        etName.setText(user.get("name").toString());

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();

                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(
                                selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                    ivProfilePicture.setImageBitmap(bmp);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    getResizedBitmap(bmp, 500).compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    image = stream.toByteArray();
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stream = null;

                }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
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

                        relationshipId = objects.get(0).getObjectId();
                        displayUserDetails();

                    }

                }
            });
            ivMenu.setVisibility(View.GONE);

        } else {
            fab.setImageResource(R.drawable.ic_save_black_48dp);
            //case that user is looking at their own profile
            displayUserDetails();


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


    public void displayUserDetails() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", userId);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            public void done(ParseUser user, com.parse.ParseException e) {
                if (e == null) {
                    // The query was successful.
                    // check if we got a match
                    if (user != null) {
                        etName.setText(user.get("name").toString());
                        etPhone.setText(user.get("phone").toString());
                        etPrice.setText(user.get("fee").toString());
                    }
                }
            }
        });
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
            case R.id.bAddCourse:
                Intent addCourseIntent;
                addCourseIntent = new Intent(this, AddCourse.class);
                addCourseIntent.putExtra("id", ParseUser.getCurrentUser().getObjectId());
                startActivity(addCourseIntent);
                break;
            case R.id.bUpload:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;
            case R.id.fab:

                ParseUser user = ParseUser.getCurrentUser();
                String name = etName.getText().toString();
                user.put("name", name);
                String phone = etPhone.getText().toString();
                user.put("phone", phone);
                Integer price = Integer.parseInt(etPrice.getText().toString());
                user.put("fee", price);
                if (image != null) {
                    ParseFile file = new ParseFile("profilePic.png", image);
                    user.put("profilePic", file);
                }


                try {
                    user.save();
                    Toast.makeText(getApplicationContext(), "Information Saved",
                            Toast.LENGTH_LONG).show();
                    Intent profileIntent;
                    profileIntent = new Intent(this, Profile.class);
                    profileIntent.putExtra("id", ParseUser.getCurrentUser().getObjectId());
                    startActivity(profileIntent);
                    finish();

                } catch (ParseException e) {
                    e.printStackTrace();
                }


                break;

        }

    }
}