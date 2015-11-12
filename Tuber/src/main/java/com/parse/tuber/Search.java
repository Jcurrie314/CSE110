package com.parse.tuber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

public class Search extends Activity implements OnItemSelectedListener {

    ListView lvTutors;
    ArrayList<SearchBundle> listAdapter;
    //ArrayAdapter<SearchBundle> listAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    String[] cse_numbers = {"Choose a Class to filter", "30", "100", "110"};
    String[] ece_numbers = {"Choose a Class to filter"};
    String[] phys_numbers = {};
    String[] math_numbers = {};
    String[] chem_numbers = {};

    Spinner department_dropdown, classNumber_dropdown, sortBy_dropdown;
    String dep = "";
    String classNumber = "";
    String sortBy = "";


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        //int thisView, departmentView, classesNumberView, sortByView;
        listAdapter.clear();
        mAdapter.notifyDataSetChanged();
        //listAdapter.notifyDataSetChanged();

        ParseUser currentUser = ParseUser.getCurrentUser();

        Spinner spinner = (Spinner) adapterView;
        int thisView = spinner.getId();
        int departmentView = R.id.sDepartmentFilter;
        int classesNumberView = R.id.sClassNumberFilter;
        int sortByView = R.id.sSortBy;
        int x = 0;

        if (thisView == classesNumberView){
            if (currentUser != null) {

                //final ArrayAdapter<SearchBundle> listAdapter = new ArrayAdapter<SearchBundle>(this,
                  //      android.R.layout.simple_list_item_1);
                //lvTutors.setAdapter(listAdapter);


                classNumber = adapterView.getItemAtPosition(i).toString();
                final String finalClassNumber = classNumber;
                final String finalDep = dep;
                final String item = dep + " " + classNumber;
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                if(sortBy.equals("Sort by: Fee")){
                    query.orderByAscending("fee");
                } else {
                    //default case, if changed will trigger if
                    query.orderByDescending("rating");

                }


                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            for (int i = 0; i < objects.size(); i++) {
                                ParseUser u = (ParseUser) objects.get(i);
                                final SearchBundle s = new SearchBundle(u);
                                if (finalClassNumber.equals("Choose a Class to filter") || finalDep.equals("Choose a Department to filter")) {
                                    listAdapter.add(s);
                                    mAdapter.notifyItemInserted(listAdapter.size()-1);

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
                                                                        mAdapter.notifyItemInserted(listAdapter.size()-1);

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
                            int i = 0;
                            String error = e.toString();
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                            i = 1;

                        }
                    }
                });

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
            classNumber_dropdown.setEnabled(true);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cse_numbers);

            if(dep.equals("CSE")){
                dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cse_numbers);

            } else if (dep.equals("ECE")){
                dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ece_numbers);

            } // else if... ECE
            //TODO: add more options
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            classNumber_dropdown.setAdapter(dataAdapter);

        } else if( thisView == sortByView ){
            sortBy = adapterView.getItemAtPosition(i).toString();

            if (currentUser != null) {

                //final ArrayAdapter<SearchBundle> listAdapter = new ArrayAdapter<SearchBundle>(this,
                  //      android.R.layout.simple_list_item_1);
                //lvTutors.setAdapter(listAdapter);

                final String finalClassNumber = classNumber;
                final String finalDep = dep;
                final String item = dep + " " + classNumber;
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                if(sortBy.equals("Sort by: Fee")){
                    query.orderByAscending("fee");
                } else {
                    //default case, if changed will trigger if
                    query.orderByDescending("rating");

                }


                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            for (int i = 0; i < objects.size(); i++) {
                                ParseUser u = (ParseUser) objects.get(i);
                                final SearchBundle s = new SearchBundle(u);
                                if (finalClassNumber.equals("Choose a Class to filter") || finalDep.equals("Choose a Department to filter")) {
                                    listAdapter.add(s);
                                    mAdapter.notifyItemInserted(listAdapter.size()-1);

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
                                                                        mAdapter.notifyItemInserted(listAdapter.size()-1);
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
                            int i = 0;
                            String error = e.toString();
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                            i = 1;

                        }
                    }
                });

            } else {
                ParseUser.logOut();
                Intent landingIntent = new Intent(this, Landing.class);
                startActivity(landingIntent);
            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        /*
        Spinner spinner = (Spinner) adapterView;
        thisView = spinner.getId();
        departmentView = R.id.sDepartmentFilter;
        classesNumberView = R.id.sClassNumberFilter;
        sortByView = R.id.sSortBy;

        if( thisView == departmentView ){
            dep = spinner.getSelectedItem().toString();
        } else if( thisView == classesNumberView ){
            classNumber = spinner.getSelectedItem().toString();
        } else if( thisView == sortByView ){
            sortBy = spinner.getSelectedItem().toString();
        }*/
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listAdapter = new ArrayList<SearchBundle>();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(listAdapter);
        mRecyclerView.setAdapter(mAdapter);

        department_dropdown = (Spinner) findViewById(R.id.sDepartmentFilter);
        classNumber_dropdown = (Spinner) findViewById(R.id.sClassNumberFilter);
        sortBy_dropdown = (Spinner) findViewById(R.id.sSortBy);

        department_dropdown.setOnItemSelectedListener(this);
        classNumber_dropdown.setOnItemSelectedListener(this);
        sortBy_dropdown.setOnItemSelectedListener(this);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cse_numbers);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classNumber_dropdown.setAdapter(dataAdapter);

        //lvTutors = (ListView) findViewById(R.id.lvTutors);

        //final ArrayAdapter<SearchBundle> listAdapter = new ArrayAdapter<SearchBundle>(this,
             //   android.R.layout.simple_list_item_1);

        /*
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String item = ((TextView) view).getText().toString();
                SearchBundle item = (SearchBundle) lvTutors.getItemAtPosition(position);
                Intent intent = new Intent(Search.this, Profile.class);
                intent.putExtra("id", item.id);
                startActivity(intent);
            }

        });*/

    }




    @Override
    protected void onStart() {
        super.onStart();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            //final String[] users = new String[]{};
            //listAdapter = new ArrayAdapter<SearchBundle>(this,android.R.layout.simple_list_item_1);
            //lvTutors.setAdapter(listAdapter);

            /*
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
            });*/
            //ArrayAdapter<SearchBundle> listAdapter = new ArrayAdapter<SearchBundle>(this,
            // android.R.layout.simple_list_item_1, android.R.id.text1, output);

        } else {
            ParseUser.logOut();
            Intent landingIntent = new Intent(this, Landing.class);
            startActivity(landingIntent);
        }


    }

}
