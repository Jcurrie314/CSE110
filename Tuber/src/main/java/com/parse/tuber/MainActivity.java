package com.parse.tuber;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


import com.parse.ParseUser;

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Resources resources = getResources();
            TabHost tabHost = getTabHost();

            // My contacts tab
            Intent intentContacts = new Intent().setClass(this, Contacts.class);
            intentContacts.putExtra("id", ParseUser.getCurrentUser().getObjectId().toString());
            TabSpec tabSpecContacts = tabHost
                    .newTabSpec("Contacts")
                    .setIndicator("", resources.getDrawable(R.drawable.ic_face_black_36dp))
                    .setContent(intentContacts);

            // Search tab
            Intent intentSearch = new Intent().setClass(this, Search.class);
            TabSpec tabSpecSearch = tabHost
                    .newTabSpec("Search")
                    .setIndicator("", resources.getDrawable(R.drawable.ic_search_black_36dp))
                    .setContent(intentSearch);

            // My profile tab
            Intent intentProfile = new Intent().setClass(this, Profile.class);
            intentProfile.putExtra("id", ParseUser.getCurrentUser().getObjectId().toString());
            TabSpec tabSpecProfile = tabHost
                    .newTabSpec("Profile")
                    .setIndicator("", resources.getDrawable(R.drawable.ic_face_black_36dp))
                    .setContent(intentProfile);

            //add all tabs
            tabHost.addTab(tabSpecContacts);
            tabHost.addTab(tabSpecSearch);
            tabHost.addTab(tabSpecProfile);

            //set Search to be default tab
            tabHost.setCurrentTab(1);


        } else {
            ParseUser.logOut();
            Intent landingIntent = new Intent(this, Landing.class);
            startActivity(landingIntent);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

}
