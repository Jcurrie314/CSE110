package com.parse.tuber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
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

    /* Member variables for UI elements, strings that will hold spinner states with each update,
     * and some extra variables to keep track of some events that are triggered by code (spinner listener
     * attachment and adapter selection) that should be ignored
     */
    ArrayList<SearchBundle> listAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    EditText etSearchIn;
    ProgressBar progress;
    Spinner department_dropdown, classNumber_dropdown, sortBy_dropdown;
    String dep = "";
    String classNumber = "";
    String sortBy = "";
    String searchInput = "";
    boolean adapterJustSet = false;
    int numberOfEventsOnSpinners = 0;


    /* Different possibilities for Class Number Spinner to be populated with after
     * a department is selected. Emcompasses various engineering/science departments at UCSD.
     */
    String[] cse_numbers = {"Class", "5A", "7", "8A", "8B", "12", "20",
            "21", "30", "80", "100", "101", "103", "105", "110", "118", "120", "122", "120",
            "121", "123", "124", "125", "127", "130", "131", "132A", "132B", "135", "140", "140L",
            "141", "141L", "143", "144", "145", "148", "150", "151", "152", "153", "160", "164",
            "165", "166", "167", "168", "169", "170", "181", "182"};
    String[] ece_numbers = {"Class", "15", "25", "30", "35", "45", "65", "100",
            "101", "102", "103", "107", "109", "111", "118", "120", "121", "123", "125A", "125B",
            "134", "135A", "135B", "136L", "138L", "139", "145", "153", "154", "155", "156", "157",
            "158", "161", "163", "164", "165", "166", "171", "172", "174", "175", "180"};
    String[] phys_numbers = {"Class", "1A", "1B", "1C", "2A", "2B", "2C", "2D",
            "4A", "4B", "4C", "4D", "4E", "5", "7", "8", "9", "10", "12", "13", "100A", "100B",
            "100C", "105A", "105B", "110A", "110B", "111", "120", "122", "124", "130A", "130B", "130C"};
    String[] math_numbers = {"Class", "10A", "10B", "10C", "11", "15A", "20A", "20B",
            "20B", "20C", "20D", "20E", "20F", "31AH", "31BH", "31CH", "95", "100A", "100B", "100C",
            "102", "103A", "103B", "104A", "104C", "109", "110A", "110B", "111A", "111B", "120A",
            "120B", "121A", "121B", "130A", "130B", "140A", "140C", "142A", "142B", "150A", "150B",
            "152"};
    String[] ceng_numbers = {"Class", "1", "4", "15", "100", "101A", "101B", "101C",
            "102", "113", "114", "120", "122", "124A", "124B", "134", "157", "176A"};
    String[] chem_numbers = {"Class", "4", "6A", "6B", "6C", "7L", "11", "12", "13",
            "15", "100A", "104", "105A", "105B", "108", "109", "113", "114A", "114B", "114C", "114D",
            "116", "120A", "120B", "123", "125", "126", "127", "130", "131", "132", "135", "140A",
            "140B", "1401", "145", "146", "151", "152", "154", "155"};
    String[] mae_numbers = {"Class", "02", "05", "07", "08", "20", "92A", "93",
            "98", "101A", "101B", "101C", "104", "105", "107", "108", "110A", "110B", "113", "117A",
            "113", "117A", "118", "119", "120", "121", "122", "123", "124", "126A", "126B"};
    String[] nano_numbers = {"Class", "1", "4", "15", "100L", "101", "102",
            "103", "104", "106", "107", "108", "110", "111", "112", "114", "120A", "120B", "141A",
            "146", "148", "150", "156", "158", "161", "164", "168"};
    String[] se_numbers = {"Class", "1", "2", "2L", "3", "9", "101A", "101B", "101C",
            "101B", "101C", "102", "103", "110A", "110B", "111A-B", "115", "120", "121", "125", "130A-B",
            "131", "140", "142", "150", "151A", "151B", "152", "154", "160A", "160B", "163", "165", "168"};

    /* Refresh search data when activity resumed */
    @Override
    public void onResume() {
        super.onResume();

        searchInput = etSearchIn.getText().toString();
        listAdapter.clear();
        mAdapter.notifyDataSetChanged();
        classNumber = classNumber_dropdown.getSelectedItem().toString();
        dep = department_dropdown.getSelectedItem().toString();
        sortBy = sortBy_dropdown.getSelectedItem().toString();


        if (ParseUser.getCurrentUser() != null) {
            progress.setVisibility(View.VISIBLE);
            addUsersToSearchList();
        }

    }

    /* Fires when an element of an array on a spinner is selected. Happens 3 times initially, when
     * each of the three spinners' onItemSelected connected
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        /* Clear list if there is something present on the screen */
        listAdapter.clear();
        mAdapter.notifyDataSetChanged();

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {

            /* Runs as a state machine - list of search results depends on the state of the spinners
             * and the adapterJustSet and numberofEventsOnSpinners variable. Get spinner states
             * here
             */
            classNumber = classNumber_dropdown.getSelectedItem().toString();
            dep = department_dropdown.getSelectedItem().toString();
            sortBy = sortBy_dropdown.getSelectedItem().toString();

            /* Returns immediately if onItemSelected method was triggered artificially because of
             * change of array adapter by the code
             */
            if (adapterJustSet) {
                adapterJustSet = false;
                return;
            }
            /* Since each spinner's OnItemSelected is hit once when the adapter is added initially,
             * only let one through to get initial search list
             */
            if (numberOfEventsOnSpinners < 2) {
                numberOfEventsOnSpinners += 1;
                return;
            }

            /* Disable class number spinner unless Department spinner has been changed. If department
             * spinner is changed back to default before class number spinner, re-default class spinner
             * and stop the next event that will be triggered because of the artificial spinner selection
             * (by changing adapterJustSet = true)
             */
            if(dep.equals("Department")){
                //adapterJustSet = true;
                classNumber_dropdown.setEnabled(false);
                if(!classNumber.equals("Class")){
                    adapterJustSet = true;
                    classNumber_dropdown.setSelection(0);
                }
            }

            /* Populate Class number spinner depending on Department spinner's selected value */
            if ((!dep.equals("Department")) && classNumber.equals("Class")) {

                classNumber_dropdown.setEnabled(true);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cse_numbers);

                if (dep.equals("CSE")) {
                    dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cse_numbers);

                } else if (dep.equals("ECE")) {
                    dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ece_numbers);

                } else if (dep.equals("CE")) {
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
                adapterJustSet = true;
                classNumber_dropdown.setAdapter(dataAdapter);
            } else {
                /* If the last spinner to be set wasn't the department spinner, then populate search results */
                progress.setVisibility(View.VISIBLE);

                addUsersToSearchList(); /* Method to populate search results */
            }

        } else {
            /* If parse user is null, logout and land the user */
            ParseUser.logOut();
            Intent landingIntent = new Intent(this, Landing.class);
            startActivity(landingIntent);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /* Set up recyclerView (list view for cards) */
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /* Attach list adapter to recyclerView. List is comprised of SearchBundle objects
         * which hold various information about the user
         */
        listAdapter = new ArrayList<>();
        mAdapter = new MyAdapter(listAdapter);
        mRecyclerView.setAdapter(mAdapter);

        /* Attach variables to UI objects */
        department_dropdown = (Spinner) findViewById(R.id.sDepartmentFilter);
        classNumber_dropdown = (Spinner) findViewById(R.id.sClassNumberFilter);
        sortBy_dropdown = (Spinner) findViewById(R.id.sSortBy);
        progress = (ProgressBar) findViewById(R.id.progressbar_loading);
        etSearchIn = (EditText) findViewById(R.id.etSearchIn);


        /* Attach event listener to spinners */
        department_dropdown.setOnItemSelectedListener(this);
        classNumber_dropdown.setOnItemSelectedListener(this);
        sortBy_dropdown.setOnItemSelectedListener(this);

        /* Add an initial array of classnumbers to the classnumber spinner (will not be used, will
         * be changed later depending on department)
         */
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cse_numbers);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classNumber_dropdown.setAdapter(dataAdapter);


        /* Attach Text Listener to Search bar to refresh search with each character typed into
         * search bar
         */
        etSearchIn.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
                searchInput = etSearchIn.getText().toString();
                listAdapter.clear();
                mAdapter.notifyDataSetChanged();

                classNumber = classNumber_dropdown.getSelectedItem().toString();
                dep = department_dropdown.getSelectedItem().toString();
                sortBy = sortBy_dropdown.getSelectedItem().toString();
                if (ParseUser.getCurrentUser() != null) {
                    progress.setVisibility(View.VISIBLE);
                    addUsersToSearchList();
                }

            }
        });

    }

    private void addUsersToSearchList() {

        /* Create final strings (to be used in inner classes) */
        final String finalClassNumber = classNumber;
        final String finalDep = dep;
        final String item = dep + " " + classNumber;


        /* Sort ParseUser query according to what sort option is chosen (Rating/fee) */
        ParseQuery<ParseUser> query1 = ParseUser.getQuery();
        if (sortBy.equals("Fee")) {
            query1.orderByAscending("fee");
        } else {
            query1.orderByDescending("rating");

        }


        /* Start first query in background to get all Parse Users */
        query1.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects1, ParseException e) {
                if (e == null) {
                    listAdapter.clear();
                    mAdapter.notifyDataSetChanged();
                    for (int i1 = 0; i1 < objects1.size(); i1++) {
                        ParseUser u = (ParseUser) objects1.get(i1);
                        final SearchBundle s = new SearchBundle(u);
                        /* If no specific class is specified or name (or part of name) is inputted is desired
                         * to filter search results, simply list all Users according to sort preference. If
                         * a name (or part of name) is inputted then only display if the user's name contains
                         * the inputted string
                         */
                        if (finalClassNumber.equals("Class") || finalDep.equals("Department")) {
                            if (searchInput == "" || s.name.toLowerCase().contains(searchInput.toLowerCase())) {
                                listAdapter.add(s);
                                mAdapter.notifyItemInserted(listAdapter.size() - 1);
                            }

                        } else { /* Case where user has specified classes to filter on */
                            /* Get Tutor course relations (not in background) */
                            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("TutorCourseRelation");
                            query2.whereEqualTo("tutor", objects1.get(i1).getObjectId().toString());

                            try {
                                List<ParseObject> objects2 = query2.find();
                                if (objects2.size() > 0) {
                                    for (int i2 = 0; i2 < objects2.size(); i2++) {
                                        /* Get course name that goes with Course ID */
                                        ParseQuery<ParseObject> nameQuery = ParseQuery.getQuery("Courses");
                                        nameQuery.whereEqualTo("objectId", objects2.get(i2).get("course").toString());
                                        List<ParseObject> objects3 = nameQuery.find();
                                        if (objects3.size() > 0) {
                                            String className = objects3.get(0).get("department") + " " + objects3.get(0).get("number");
                                            /* If course that tutor tutors matches filter preference, display tutor (if
                                             * search input is nothing or if tutor's name matches search input
                                             */
                                            if (className.equals(item)) {
                                                if (searchInput == "" || s.name.toLowerCase().contains(searchInput.toLowerCase())) {
                                                    listAdapter.add(s);
                                                    mAdapter.notifyItemInserted(listAdapter.size() - 1);
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (ParseException e1) {
                                /* If anything failed, print stack */
                                e1.printStackTrace();
                            }
                        }
                        /* Hide progress bar when done with search */
                        progress.setVisibility(View.GONE);
                    }
                } else {
                    /* If anything failed in background, print error */
                    String error = e.toString();
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        /* Check if user is not null, if not land them and logout */
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {

        } else {
            ParseUser.logOut();
            Intent landingIntent = new Intent(this, Landing.class);
            startActivity(landingIntent);
        }


    }

}
