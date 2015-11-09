package com.parse.tuber;

import android.graphics.Bitmap;

import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by JulianneDeMars on 11/1/15.
 */
public class SearchBundle {
    String name;
    String id;
    double rating;
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
        courses = user.courses;
        rating = user.rating;
        name = user.name;
        profilePicture = user.profilePicture;
    }


}
