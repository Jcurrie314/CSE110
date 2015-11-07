package com.parse.tuber;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class TuberUser {

    String email, username, password, id, name;
    ArrayList<String> courses = new ArrayList<String>();
    ParseUser user;
    Boolean isTutor;
    double rating, avgRating;



    public TuberUser(ParseUser user) {

        if(user != null) {
            this.id = user.getObjectId().toString();
            this.name = (String) user.get("name");
            this.email = (String) user.get("email");
            this.username = (String) user.get("username");
            this.isTutor = (Boolean) user.get("tutor");
            if (this.isTutor) {
                getCourses();
                getRating();
            }

        }
    }

    public void setUserId(String userId){
        this.id = userId;
    }

    public String getUserId(){
        return id;
    }

    public ArrayList<String> getCourses (){

        return courses;
    }


    public void getRating() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Relationships");
        query.whereEqualTo("tutor", this.id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        int sum = 0;
                        for (int i = 0; i < objects.size(); i++) {
                            sum += objects.get(i).getInt("rating");
                        }

                        avgRating = (double) sum / objects.size();
                    }

                } else {
                }

            }
        });

        this.rating = avgRating;

    }

    public void findTutorCourses() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TutorCourseRelation");
        query.whereEqualTo("tutor", this.id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (int i = 0; i < objects.size(); i++) {
                            //final String grade = objects.get(i).get("grade").toString();
                            //final String id = objects.get(i).getObjectId();

                            ParseQuery<ParseObject> nameQuery = ParseQuery.getQuery("Courses");
                            nameQuery.whereEqualTo("objectId", objects.get(i).get("course").toString());
                            nameQuery.findInBackground(new FindCallback<ParseObject>() {

                                @Override
                                public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                                    if (e == null) {
                                        if (objects.size() > 0) {
                                            courses.add(objects.get(0).get("department") + " " + objects.get(0).get("number"));

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

}