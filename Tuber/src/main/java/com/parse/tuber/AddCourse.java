package com.parse.tuber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

/**
 * Created by Juleelala on 10/25/15.
 */
public class AddCourse extends ActionBarActivity implements View.OnClickListener {

    AutoCompleteTextView actvDepartment, actvNumbers;
    Button bFindNumbers,bAddCourse;
    Spinner sGrade;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcourse);


        String[] array = getResources().getStringArray(R.array.course_department_array);

        ArrayAdapter<String> adapter;
        bFindNumbers = (Button) findViewById(R.id.bFindNumbers);
        bFindNumbers.setOnClickListener(this);
        bAddCourse = (Button) findViewById(R.id.bAddCourse);
        bAddCourse.setOnClickListener(this);
        sGrade = (Spinner) findViewById(R.id.sGrade);
        actvDepartment = (AutoCompleteTextView) findViewById(R.id.actvDepartment);
        actvNumbers = (AutoCompleteTextView) findViewById(R.id.actvNumbers);
        adapter = new ArrayAdapter<String>(AddCourse.this,
                android.R.layout.simple_list_item_1, array);

        actvDepartment.setAdapter(adapter);


    }

    public void getNumbers(){
        ArrayAdapter<String> dataAdapter;
        if(actvDepartment.getText().toString().equals("CSE")){
            dataAdapter = new ArrayAdapter<String>(AddCourse.this, android.R.layout.simple_list_item_1, cse_numbers);
            actvNumbers.setAdapter(dataAdapter);

        } else if (actvDepartment.getText().toString().equals("ECE")){
            dataAdapter = new ArrayAdapter<String>(AddCourse.this, android.R.layout.simple_list_item_1, ece_numbers);
            actvNumbers.setAdapter(dataAdapter);

        } // else if... ECE
        else if (actvDepartment.getText().toString().equals("CE")) {
            dataAdapter = new ArrayAdapter<String>(AddCourse.this, android.R.layout.simple_list_item_1, ceng_numbers);
            actvNumbers.setAdapter(dataAdapter);

        } else if (actvDepartment.getText().toString().equals("MAE")) {
            dataAdapter = new ArrayAdapter<String>(AddCourse.this, android.R.layout.simple_list_item_1, mae_numbers);
            actvNumbers.setAdapter(dataAdapter);

        } else if (actvDepartment.getText().toString().equals("SE")) {
            dataAdapter = new ArrayAdapter<String>(AddCourse.this, android.R.layout.simple_list_item_1, se_numbers);
            actvNumbers.setAdapter(dataAdapter);

        } else if (actvDepartment.getText().toString().equals("NANO")) {
            dataAdapter = new ArrayAdapter<String>(AddCourse.this, android.R.layout.simple_list_item_1, nano_numbers);
            actvNumbers.setAdapter(dataAdapter);

        } else if (actvDepartment.getText().toString().equals("MATH")) {
            dataAdapter = new ArrayAdapter<String>(AddCourse.this, android.R.layout.simple_list_item_1, math_numbers);
            actvNumbers.setAdapter(dataAdapter);

        } else if (actvDepartment.getText().toString().equals("PHYS")) {
            dataAdapter = new ArrayAdapter<String>(AddCourse.this, android.R.layout.simple_list_item_1, phys_numbers);
            actvNumbers.setAdapter(dataAdapter);

        } else if (actvDepartment.getText().toString().equals("CHEM")) {
            dataAdapter = new ArrayAdapter<String>(AddCourse.this, android.R.layout.simple_list_item_1, chem_numbers);
            actvNumbers.setAdapter(dataAdapter);

        }

    }

    public void addCourse(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Courses");
        query.whereEqualTo("department", actvDepartment.getText().toString().trim());
        query.whereEqualTo("number", actvNumbers.getText().toString().trim());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(java.util.List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                       String courseId = objects.get(0).getObjectId();
                        ParseObject request = new ParseObject("TutorCourseRelation");
                        request.put("course", courseId);
                        request.put("tutor", ParseUser.getCurrentUser().getObjectId());
                        request.put("grade", sGrade.getSelectedItem().toString());
                        try {
                            request.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Fuck",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bFindNumbers:
                getNumbers();

                break;
            case R.id.bAddCourse:
                addCourse();
                Intent profileIntent;
                profileIntent = new Intent(this, Profile.class);
                profileIntent.putExtra("id", ParseUser.getCurrentUser().getObjectId());
                startActivity(profileIntent);
                break;
        }
    }


}
