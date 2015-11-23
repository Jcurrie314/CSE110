package com.parse.tuber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juleelala on 11/22/15.
 */
public class NameSearch extends ActionBarActivity implements View.OnClickListener {

    EditText etSearchInput;
    Button bSearch;
    ArrayList<SearchBundle> listAdapter;
    ArrayAdapter<SearchBundle> adapter;
    RecyclerView mRecyclerViewNS;
    RecyclerView.Adapter mAdapterNS;
    RecyclerView.LayoutManager mLayoutManagerNS;
    String items[];

    @Override
    public void onClick(View view) {
        listAdapter.clear();
        mAdapterNS.notifyDataSetChanged();

        final String searchInput = etSearchInput.getText().toString();
        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser!=null){
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        for (int i = 0; i < objects.size(); i++) {
                            ParseUser u = (ParseUser) objects.get(i);
                            final SearchBundle s = new SearchBundle(u);
                            if(s.name.toLowerCase().contains(searchInput.toLowerCase())) {
                                listAdapter.add(s);
                                mAdapterNS.notifyItemInserted(listAdapter.size() - 1);
                            }


                        }
                    } else {
                        //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                         String error = e.toString();
                         Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                  }
              }
          });

        } else {
            ParseUser.logOut();
            Intent landingIntent = new Intent(this, Landing.class);
            startActivity(landingIntent);
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_search);


        bSearch = (Button) findViewById(R.id.bSearch);
        etSearchInput = (EditText) findViewById(R.id.etSearchInput);
        bSearch.setOnClickListener(this);
        listAdapter = new ArrayList<SearchBundle>();


        mRecyclerViewNS = (RecyclerView) findViewById(R.id.my_recycler_view_nameSearch);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewNS.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerNS = new LinearLayoutManager(this);
        mRecyclerViewNS.setLayoutManager(mLayoutManagerNS);

        // specify an adapter (see also next example)
        mAdapterNS = new MyAdapter(listAdapter);
        mRecyclerViewNS.setAdapter(mAdapterNS);

        getList();
        etSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    //reset list
                    getList();
                } else {
                    //perform search
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void searchItem(String textToSearch){
        for(SearchBundle item:listAdapter){
            if(!item.name.toLowerCase().contains(textToSearch)){
                listAdapter.remove(item);
            }
        }
        adapter.notifyDataSetChanged();

    }

    public void getList(){
        final String searchInput = etSearchInput.getText().toString();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        ParseUser u = (ParseUser) objects.get(i);
                        final SearchBundle s = new SearchBundle(u);
                        if(s.name.toLowerCase().contains(searchInput.toLowerCase())) {
                            listAdapter.add(s);
                            mAdapterNS.notifyItemInserted(listAdapter.size() - 1);
                        }


                    }
                } else {
                    //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    String error = e.toString();
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                }
            }
        });
        }
    }
