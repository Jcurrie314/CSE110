package com.parse.tuber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Search extends Activity implements OnItemSelectedListener {

    ListView lvTutors;
    ArrayAdapter<SearchBundle> listAdapter;
    String[] cse_numbers = {"Choose a Class to filter", "30", "100", "110"};
    String[] ece_numbers = {};
    String[] phys_numbers = {};
    String[] math_numbers = {};
    String[] chem_numbers = {};

    Spinner department_dropdown, classNumber_dropdown;
    String dep = "";
    String classNumber = "";

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(adapterView.getContext(),
        //     "OnItemSelectedListener : " + adapterView.getItemAtPosition(i).toString(),
        //    Toast.LENGTH_SHORT).show();
        //View newView = view

        //String dep = department_dropdown.


        listAdapter.clear();
        listAdapter.notifyDataSetChanged();
        ParseUser currentUser = ParseUser.getCurrentUser();
        Spinner spinner = (Spinner) adapterView;
        int thisView = spinner.getId();
        int departmentView = R.id.sDepartmentFilter;
        int classesNumberView = R.id.sClassNumberFilter;
        if (thisView == classesNumberView){
            if (currentUser != null) {
                //final String[] users = new String[]{};
                final ArrayAdapter<SearchBundle> listAdapter = new ArrayAdapter<SearchBundle>(this,
                        android.R.layout.simple_list_item_1);
                lvTutors.setAdapter(listAdapter);


                classNumber = adapterView.getItemAtPosition(i).toString();
                final String finalClassNumber = dep;
                final String item = dep + " " + classNumber;
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            for (int i = 0; i < objects.size(); i++) {
                                ParseUser u = (ParseUser) objects.get(i);
                                final SearchBundle s = new SearchBundle(u);
                                if (finalClassNumber.equals("Choose a Class to filter")) {
                                    listAdapter.add(s);
                                }
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("TutorCourseRelation");
                                query.whereEqualTo("tutor", objects.get(i).getObjectId().toString());
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                                        if (e == null) {
                                            if (objects.size() > 0) {
                                                for (int i = 0; i < objects.size(); i++) {

                                                    ParseQuery<ParseObject> nameQuery = ParseQuery.getQuery("Courses");
                                                    nameQuery.whereEqualTo("objectId", objects.get(i).get("course").toString());
                                                    nameQuery.findInBackground(new FindCallback<ParseObject>() {

                                                        @Override
                                                        public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                                                            if (e == null) {
                                                                if (objects.size() > 0) {
                                                                    String className = objects.get(0).get("department") + " " + objects.get(0).get("number");
                                                                    if (className.equals(item)) {
                                                                        listAdapter.add(s);
                                                                    }
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
                        } else {
                            //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                            String error = e.toString();
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();

                        }
                    }
                });
                //ArrayAdapter<SearchBundle> listAdapter = new ArrayAdapter<SearchBundle>(this,
                // android.R.layout.simple_list_item_1, android.R.id.text1, output);

            } else {
                ParseUser.logOut();
                Intent landingIntent = new Intent(this, Landing.class);
                startActivity(landingIntent);
            }

        } else if( thisView == departmentView){
            dep = adapterView.getItemAtPosition(i).toString();

            if (dep.equals("Choose a Department to filter")) {
                return;
            }
            classNumber_dropdown.setClickable(true);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cse_numbers);

            if(dep.equals("CSE")){
                dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cse_numbers);

            } else if (dep.equals("ECE")){
                dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ece_numbers);

            } // else if... ECE
            //TODO: add more options
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            classNumber_dropdown.setAdapter(dataAdapter);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        department_dropdown = (Spinner) findViewById(R.id.sDepartmentFilter);
        classNumber_dropdown = (Spinner) findViewById(R.id.sClassNumberFilter);

        department_dropdown.setOnItemSelectedListener(this);

        classNumber_dropdown.setOnItemSelectedListener(this);



        lvTutors = (ListView) findViewById(R.id.lvTutors);

        final ArrayAdapter<SearchBundle> listAdapter = new ArrayAdapter<SearchBundle>(this,
                android.R.layout.simple_list_item_1);


        lvTutors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String item = ((TextView) view).getText().toString();
                SearchBundle item = (SearchBundle) lvTutors.getItemAtPosition(position);
                Intent intent = new Intent(Search.this, Profile.class);
                intent.putExtra("id", item.id);
                startActivity(intent);
            }

        });

    }




    @Override
    protected void onStart() {
        super.onStart();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            //final String[] users = new String[]{};
            listAdapter = new ArrayAdapter<SearchBundle>(this,
                    android.R.layout.simple_list_item_1);
            lvTutors.setAdapter(listAdapter);

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        for (int i = 0; i < objects.size(); i++) {
                            ParseUser u = (ParseUser) objects.get(i);
                            SearchBundle s = new SearchBundle(u);
                            listAdapter.add(s);
                        }
                    } else {
                        String error = e.toString();

                    }
                }
            });
            //ArrayAdapter<SearchBundle> listAdapter = new ArrayAdapter<SearchBundle>(this,
            // android.R.layout.simple_list_item_1, android.R.id.text1, output);

        } else {
            ParseUser.logOut();
            Intent landingIntent = new Intent(this, Landing.class);
            startActivity(landingIntent);
        }


    }

}
