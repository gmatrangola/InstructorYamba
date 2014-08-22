package com.thenewcircle.instructoryamba;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.thenewcircle.instructoryamba.R;

public class TimelineActivity extends BaseYambaActivity implements TimelineFragment.TimelineItemSelectionCallback {

    private static final String TAG = "newcircle.yamba." + TimelineActivity.class.getSimpleName();
    public static final String SELECTED_TAB = "selectedTab";
    private TimelineDetailsFragment detailsFragment;
    private int selectedTab = 0;
    private View fragmentDetailsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        fragmentDetailsView = findViewById(R.id.fragment_details);
        detailsFragment = new TimelineDetailsFragment();
        if(getIntent().getBooleanExtra("fromNotification", false)) {
            Log.d(TAG, "From Notification");
        }
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        if(savedInstanceState != null) selectedTab = savedInstanceState.getInt(SELECTED_TAB, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ActionBar actionBar = getActionBar();

        ActionBar.Tab tab = actionBar.newTab();
        tab.setText("Timeline");
        tab.setTabListener(new TimelineTabListener(this, detailsFragment));
        actionBar.addTab(tab);

        tab = actionBar.newTab();
        tab.setText("Status");
        tab.setTabListener(new TabListener<StatusFragment>(this, "status", StatusFragment.class));
        actionBar.addTab(tab);
        if(selectedTab > -1) actionBar.setSelectedNavigationItem(selectedTab);
    }

    @Override
    protected void onPause() {
        getActionBar().removeAllTabs();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ActionBar.Tab selected = getActionBar().getSelectedTab();
        if(selected != null) {
            int idx = selected.getPosition();
            outState.putInt(SELECTED_TAB, idx);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.showPost :
                Intent statusIntent = new Intent(this, StatusActivity.class);
                startActivity(statusIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimelineItemSelected(long id) {
        if(fragmentDetailsView == null) {
            FragmentTransaction tx = getFragmentManager().beginTransaction();
            TimelineDetailsFragment detailsFragment = new TimelineDetailsFragment();
            detailsFragment.setRowId(id);
            tx.replace(R.id.fragment_container, detailsFragment);
            tx.addToBackStack("TimelineDetails " + id);
            tx.commit();
        }
        else {
            if(detailsFragment.getActivity() == null) {
                detailsFragment.setRowId(id);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_details, detailsFragment);
                ft.commit();
            }
            else {
                detailsFragment.update(id);
            }
        }
    }
}
