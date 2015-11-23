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
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Timer;

public class Search extends Activity implements OnItemSelectedListener {

    ListView lvTutors;
    ArrayList<SearchBundle> listAdapter;
    //ArrayAdapter<SearchBundle> listAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    EditText etSearchIn;
    Timer timer;

    String[] cse_numbers = {"Choose a Class to filter", "5A", "7", "8A", "8B", "12", "20",
            "21", "30", "80", "100", "101", "103", "105", "110", "118", "120", "122", "120",
            "121", "123", "124", "125", "127", "130", "131", "132A", "132B", "135", "140", "140L",
            "141", "141L", "143", "144", "145", "148", "150", "151", "152", "153", "160", "164",
            "165", "166", "167", "168", "169", "170", "181", "182"};
    String[] ece_numbers = {"Choose a Class to filter", "15", "25", "30", "35", "45", "65", "100",
            "101", "102", "103", "107", "109", "111", "118", "120", "121", "123", "125A", "125B",
            "134", "135A", "135B", "136L", "138L", "139", "145", "153", "154", "155", "156", "157",
            "158", "161", "163", "164", "165", "166", "171", "172", "174", "175", "180"};
    String[] phys_numbers = {"Choose a Class to filter", "1A", "1B", "1C", "2A", "2B", "2C", "2D",
            "4A", "4B", "4C", "4D", "4E", "5", "7", "8", "9", "10", "12", "13", "100A", "100B",
            "100C", "105A", "105B", "110A", "110B", "111", "120", "122", "124", "130A", "130B", "130C"};
    String[] math_numbers = {"Choose a Class to filter", "10A", "10B", "10C", "11", "15A", "20A", "20B",
            "20B", "20C", "20D", "20E", "20F", "31AH", "31BH", "31CH", "95", "100A", "100B", "100C",
            "102", "103A", "103B", "104A", "104C", "109", "110A", "110B", "111A", "111B", "120A",
            "120B", "121A", "121B", "130A", "130B", "140A", "140C", "142A", "142B", "150A", "150B",
            "152"};
    String[] ceng_numbers = {"Choose a Class to filter", "1", "4", "15", "100", "101A", "101B", "101C",
            "102", "113", "114", "120", "122", "124A", "124B", "134", "157", "176A"};
    String[] chem_numbers = {"Choose a Class to filter", "4", "6A", "6B", "6C", "7L", "11", "12", "13",
            "15", "100A", "104", "105A", "105B", "108", "109", "113", "114A", "114B", "114C", "114D",
            "116", "120A", "120B", "123", "125", "126", "127", "130", "131", "132", "135", "140A",
            "140B", "1401", "145", "146", "151", "152", "154", "155"};
    String[] mae_numbers = {"Choose a Class to filter", "02", "05", "07", "08", "20", "92A", "93",
            "98", "101A", "101B", "101C", "104", "105", "107", "108", "110A", "110B", "113", "117A",
            "113", "117A", "118", "119", "120", "121", "122", "123", "124", "126A", "126B"};
    String[] nano_numbers = {"Choose a Class to filter", "1", "4", "15", "100L", "101", "102",
            "103", "104", "106", "107", "108", "110", "111", "112", "114", "120A", "120B", "141A",
            "146", "148", "150", "156", "158", "161", "164", "168" };
    String[] se_numbers = {"Choose a Class to filter", "1", "2", "2L", "3", "9", "101A", "101B", "101C",
            "101B", "101C", "102", "103", "110A", "110B", "111A-B", "115", "120", "121", "125", "130A-B",
            "131", "140", "142", "150", "151A", "151B", "152", "154", "160A", "160B", "163", "165", "168"};


    Spinner department_dropdown, classNumber_dropdown, sortBy_dropdown;
    Button bNameSearch;
    String dep = "";
    String classNumber = "";
    String sortBy = "";
    String searchInput = "";


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        //int thisView, departmentView, classesNumberView, sortByView;
        listAdapter.clear();
        mAdapter.notifyDataSetChanged();

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


//                if (classNumber.equals("Choose a Class to filter")){
//                    return;
//                }

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
                                    if(searchInput == "" ||  s.name.toLowerCase().contains(searchInput.toLowerCase())) {
                                        listAdapter.add(s);
                                        mAdapter.notifyItemInserted(listAdapter.size() - 1);
                                    }
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
                                                                        if(searchInput == "" ||  s.name.toLowerCase().contains(searchInput.toLowerCase())) {

                                                                            listAdapter.add(s);
                                                                            mAdapter.notifyItemInserted(listAdapter.size() - 1);
                                                                        }

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

            if (dep.equals("Choose a Department to filter") ) {
                return;
            }
            classNumber_dropdown.setEnabled(true);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cse_numbers);

            if(dep.equals("CSE")){
                dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cse_numbers);

            } else if (dep.equals("ECE")){
                dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ece_numbers);

            } // else if... ECE
              else if (dep.equals("CE")) {
                dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ceng_numbers);

            } else if (dep.equals("MAE")) {
                dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mae_numbers);

            } else if (dep.equals("SE")) {
                dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, se_numbers);

            } else if (dep.equals("NANO")) {
                dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nano_numbers);

            } else if (dep.equals("MATH")) {
                dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, math_numbers);

            } else if (dep.equals("PHYS")) {
                dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, phys_numbers);

            } else if (dep.equals("CHEM")) {
                dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, chem_numbers);

            }

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
                                    if(searchInput == "" ||  s.name.toLowerCase().contains(searchInput.toLowerCase())) {

                                        listAdapter.add(s);
                                        mAdapter.notifyItemInserted(listAdapter.size() - 1);
                                    }
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
                                                                        if(searchInput == "" ||  s.name.toLowerCase().contains(searchInput.toLowerCase())) {

                                                                            listAdapter.add(s);
                                                                            mAdapter.notifyItemInserted(listAdapter.size() - 1);
                                                                        }
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


        bNameSearch = (Button) findViewById(R.id.bSearchName);
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
        etSearchIn = (EditText) findViewById(R.id.etSearchIn);
        //timer = new Timer()

        /*
        etSearchIn.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
                searchInput = etSearchIn.getText().toString();
                if (ParseUser.getCurrentUser() != null) {
                    listAdapter.clear();
                    mAdapter.notifyDataSetChanged();

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
                                        if(searchInput == "" ||  s.name.toLowerCase().contains(searchInput.toLowerCase())) {

                                            listAdapter.add(s);
                                            mAdapter.notifyItemInserted(listAdapter.size() - 1);
                                        }
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
                                                                            if(searchInput == "" ||  s.name.toLowerCase().contains(searchInput.toLowerCase())) {

                                                                                listAdapter.add(s);
                                                                                mAdapter.notifyItemInserted(listAdapter.size() - 1);
                                                                            }
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
                                String error = e.toString();
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();


                            }
                        }
                    });

                } else {
                    ParseUser.logOut();
                    Intent landingIntent = new Intent(Search.this, Landing.class);
                    startActivity(landingIntent);
                }
            }

        });

        */
        bNameSearch.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                searchInput = etSearchIn.getText().toString();
                if (ParseUser.getCurrentUser() != null) {
                    listAdapter.clear();
                    mAdapter.notifyDataSetChanged();

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
                                        if(searchInput == "" ||  s.name.toLowerCase().contains(searchInput.toLowerCase())) {

                                            listAdapter.add(s);
                                            mAdapter.notifyItemInserted(listAdapter.size() - 1);
                                        }
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
                                                                            if(searchInput == "" ||  s.name.toLowerCase().contains(searchInput.toLowerCase())) {

                                                                                listAdapter.add(s);
                                                                                mAdapter.notifyItemInserted(listAdapter.size() - 1);
                                                                            }
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
                                String error = e.toString();
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();


                            }
                        }
                    });

                } else {
                    ParseUser.logOut();
                    Intent landingIntent = new Intent(Search.this, Landing.class);
                    startActivity(landingIntent);
                }
            }

        });

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
