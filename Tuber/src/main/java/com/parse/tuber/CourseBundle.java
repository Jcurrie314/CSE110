package com.parse.tuber;

/**
 * Created by Jonathan on 11/1/15.
 */
public class CourseBundle {
    String id;
    String name;
    String grade;

    @Override
    public String toString() {

        return name + " (" + grade + ")" ;
    }
}
