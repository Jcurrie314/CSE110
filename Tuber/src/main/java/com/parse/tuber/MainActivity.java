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

        Resources resources = getResources();
        TabHost tabHost = getTabHost();

        // Search tab
        Intent intentSearch = new Intent().setClass(this, Search.class);
        TabSpec tabSpecSearch = tabHost
                .newTabSpec("Search")
                .setIndicator("", resources.getDrawable(R.drawable.icon_search_config))
                .setContent(intentSearch);

        // My profile tab
        Intent intentProfile = new Intent().setClass(this, Profile.class);
        intentProfile.putExtra("id", ParseUser.getCurrentUser().getObjectId().toString());
        TabSpec tabSpecProfile = tabHost
                .newTabSpec("Profile")
                .setIndicator("", resources.getDrawable(R.drawable.icon_profile_config))
                .setContent(intentProfile);

        //add all tabs
        tabHost.addTab(tabSpecSearch);
        tabHost.addTab(tabSpecProfile);

        //set Search to be default tab
        tabHost.setCurrentTab(0);
    }


    @Override
    protected void onStart() {
        super.onStart();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent searchIntent = new Intent(this, Search.class);
            startActivity(searchIntent);
        } else {
            ParseUser.logOut();
            Intent landingIntent = new Intent(this, Landing.class);
            startActivity(landingIntent);
        }


    }

}
