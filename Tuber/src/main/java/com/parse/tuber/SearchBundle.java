package com.parse.tuber;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

/**
 * Created by JulianneDeMars on 11/1/15.
 */
public class SearchBundle {
    String name;
    String id;
    String rating;
    ArrayList<String> courses = new ArrayList<String>();

    @Override
    public String toString(){
        return name;
    }

    public void findTutorCourses() {
        //final ArrayAdapter<CourseBundle> listAdapter = new ArrayAdapter<SearchBundle>(this,
           //     android.R.layout.simple_list_item_1);
        //lvCourses.setAdapter(listAdapter);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TutorCourseRelation");
        query.whereEqualTo("tutor", id);
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
