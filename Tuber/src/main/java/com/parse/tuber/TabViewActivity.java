package com.parse.tuber;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Julianne DeMars on 11/1/15.
 */
public class TabViewActivity extends Activity {
    ActionBar.Tab tab1, tab2;
    Fragment fragmentTab1 = new FragmentTab1();
    Fragment fragmentTab2 = new FragmentTab2();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        tab1 = actionBar.newTab().setText("1");
        tab2 = actionBar.newTab().setText("2");

        tab1.setTabListener(new MyTabListener(fragmentTab1));
        tab2.setTabListener(new MyTabListener(fragmentTab2));

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
    }
}

