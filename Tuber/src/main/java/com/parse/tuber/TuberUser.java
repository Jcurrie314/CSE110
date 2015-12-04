package com.parse.tuber;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;

public class TuberUser {

    String email, username, password, id, name;
    ArrayList<String> courses = new ArrayList<String>();
    //ParseUser user;
    Bitmap profilePicture;
    Boolean isTutor;

    Integer fee;
    double avgRating;


    public TuberUser(ParseUser user) {

        if (user != null) {
            this.id = user.getObjectId();
            this.name = (String) user.get("name");
            this.email = (String) user.get("email");
            this.username = (String) user.get("username");
            this.avgRating = user.getDouble("rating");
            this.fee = user.getInt("fee");

            ParseFile imageFile = (ParseFile) user.get("profilePic");

            Bitmap bitmap = null;
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                bitmap = BitmapFactory.decodeByteArray(imageFile.getData(), 0, imageFile.getData().length, options);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.profilePicture = bitmap;
            this.isTutor = (Boolean) user.get("tutor");



        }
    }





}