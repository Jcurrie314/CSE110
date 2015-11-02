package com.parse.tuber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Search extends ActionBarActivity implements View.OnClickListener {

    Button bLogout, bChangePassword;
    ListView lvTutors;
    ArrayAdapter<String> listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        bLogout = (Button) findViewById(R.id.bLogout);
        bChangePassword = (Button) findViewById(R.id.bChangePassword);
        lvTutors = (ListView) findViewById(R.id.lvTutors);


        bChangePassword.setOnClickListener(this);
        bLogout.setOnClickListener(this);

        lvTutors = (ListView) findViewById(R.id.lvTutors);


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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogout:
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                Intent loginIntent = new Intent(this, Login.class);
                startActivity(loginIntent);
                break;
            case R.id.bChangePassword:
                Intent changePasswordIntent;
                changePasswordIntent = new Intent(this, ChangePassword.class);
                startActivity(changePasswordIntent);
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            //final String[] users = new String[]{};
            final ArrayAdapter<SearchBundle> listAdapter = new ArrayAdapter<SearchBundle>(this,
                    android.R.layout.simple_list_item_1);
            lvTutors.setAdapter(listAdapter);

            List<SearchBundle> output = new ArrayList<SearchBundle>();
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        for (int i = 0; i < objects.size(); i++) {
                            ParseUser u = (ParseUser) objects.get(i);
                            SearchBundle s = new SearchBundle();
                            String name = u.getString("username").toString();
                            String id = u.getObjectId().toString();
                            s.name = name;
                            s.id = id;
                            //output.add(s);
                            listAdapter.add(s);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
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
