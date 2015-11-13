package com.parse.tuber;

import android.graphics.Bitmap;

import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by JulianneDeMars on 11/1/15.
 */
public class SearchBundle {
    String name;
    String id;
    double avgRating, numberOfRatings, fee;
    Bitmap profilePicture;
    ArrayList<String> courses = new ArrayList<String>();
    TuberUser user;


    @Override
    public String toString(){
        return user.name;

    }

    public SearchBundle(ParseUser u){
        user = new TuberUser(u);
        id = user.id;
        //courses = user.courses;
        avgRating = user.avgRating;
        numberOfRatings = user.numberOfRatings;
        name = user.name;
        profilePicture = user.profilePicture;
        fee = user.fee;
    }


}
