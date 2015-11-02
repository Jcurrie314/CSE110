package com.parse.tuber;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;

/**
 * Created by Juleelala on 11/1/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MyTabListener implements ActionBar.TabListener {

    public MyTabListener(Fragment fragment) {

    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        //ft.replace(R.id.fragment_container, fragment);
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        //ft.remove(fragment);
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // nothing done here
    }
}
