package com.parse.tuber;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by jonathancurrie on 11/7/15.
 */
public class Contacts extends ActionBarActivity {
    ListView lvPending, lvConnections;
    TextView lvPendingLabel, lvConnectionsLabel;
    ArrayAdapter<String> listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);


        lvPending = (ListView) findViewById(R.id.lvPending);
        lvConnections = (ListView) findViewById(R.id.lvConnections);

        lvPendingLabel = (TextView) findViewById(R.id.lvPendingLabel);
        lvConnectionsLabel = (TextView) findViewById(R.id.lvConnectionsLabel);


        lvPending.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long rowId) {
                final ConnectionBundle item = (ConnectionBundle) lvPending.getItemAtPosition(position);

                AlertDialog.Builder adb = new AlertDialog.Builder(
                        Contacts.this);
                adb.setTitle("List");
                adb.setMessage("Would you like to give "
                        + item.name + " access to your contact information");
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        changeAccess(item.relationshipId);
                        dialog.cancel();

                    }
                });
                adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        changeAccess(item.relationshipId);

                        dialog.cancel();
                    }
                });
                adb.show();

            }

        });
        lvConnections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConnectionBundle item = (ConnectionBundle) lvConnections.getItemAtPosition(position);
                Intent intent = new Intent(Contacts.this, Profile.class);
                intent.putExtra("id", item.id);
                startActivity(intent);
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        findPending();
        findConnections();
    }
    @Override
    protected void onResume() {
        super.onResume();
        findPending();
        findConnections();
    }
    public void changeAccess(final String relationshipId) {
        ParseQuery query = new ParseQuery("Relationships");
        query.whereEqualTo("objectId", relationshipId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    objects.get(0).put("accepted",true);
                    objects.get(0).put("requested",false);

                    objects.get(0).saveInBackground();
                    Toast.makeText(getApplicationContext(),  objects.get(0).getObjectId() + " was updated", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void findPending() {
        final ArrayAdapter<ConnectionBundle> listAdapter = new ArrayAdapter<ConnectionBundle>(this,
                android.R.layout.simple_list_item_1);
        lvPending.setAdapter(listAdapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Relationships");
        query.whereEqualTo("tutor", ParseUser.getCurrentUser().getObjectId());
        query.whereEqualTo("requested", true);


        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if(objects.size() == 0) {
                        lvPendingLabel.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < objects.size(); i++) {
                        final String relationshipId = objects.get(i).getObjectId();
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereEqualTo("objectId", objects.get(i).get("student"));
                        query.findInBackground(new FindCallback<ParseUser>() {
                            public void done(List<ParseUser> objects, ParseException e) {
                                if (e == null) {
                                    for (int i = 0; objects.size() > i; i++) {
                                        ParseUser u = objects.get(i);
                                        ConnectionBundle s = new ConnectionBundle();
                                        String name = u.get("name").toString();
                                        String id = u.getObjectId();
                                        s.name = name;
                                        s.relationshipId = relationshipId;
                                        s.id = id;
                                        listAdapter.add(s);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void findConnections() {
        final ArrayAdapter<ConnectionBundle> listAdapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        lvConnections.setAdapter(listAdapter2);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Relationships");
        query.whereEqualTo("tutor", ParseUser.getCurrentUser().getObjectId());
        query.whereEqualTo("accepted", true);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if(objects.size() == 0) {
                        lvConnectionsLabel.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < objects.size(); i++) {
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereEqualTo("objectId", objects.get(i).get("student"));
                        query.findInBackground(new FindCallback<ParseUser>() {
                            public void done(List<ParseUser> objects, ParseException e) {
                                if (e == null) {
                                    for (int i = 0; i < objects.size(); i++) {
                                        ParseUser u = objects.get(i);
                                        ConnectionBundle s = new ConnectionBundle();
                                        String name = u.get("name").toString();
                                        String id = u.getObjectId();
                                        s.name = name;
                                        s.id = id;
                                        listAdapter2.add(s);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
