package com.thenewcircle.instructoryamba;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.thenewcircle.instructoryamba.R;

public class TimelineActivity extends BaseYambaActivity implements TimelineFragment.TimelineItemSelectionCallback {

    private TimelineDetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new TimelineFragment(), "timeline");
        View fragmentDetails = findViewById(R.id.fragment_details);
        if(fragmentDetails != null) {
            detailsFragment = new TimelineDetailsFragment();
            ft.replace(R.id.fragment_details, detailsFragment);
        }
        ft.commit();
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
        if(detailsFragment == null) {
            FragmentTransaction tx = getFragmentManager().beginTransaction();
            TimelineDetailsFragment detailsFragment = new TimelineDetailsFragment();
            detailsFragment.setRowId(id);
            tx.replace(R.id.fragment_container, detailsFragment);
            tx.addToBackStack("TimelineDetails " + id);
            tx.commit();
        }
        else {
            detailsFragment.update(id);
        }
    }
}
