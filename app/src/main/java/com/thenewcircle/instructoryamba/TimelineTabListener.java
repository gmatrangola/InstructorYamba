package com.thenewcircle.instructoryamba;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by geoff on 8/22/14.
 */
public class TimelineTabListener extends TabListener<TimelineFragment> {

    private final TimelineDetailsFragment detailsFragment;

    public TimelineTabListener(Activity activity, TimelineDetailsFragment detailsFragment) {
        super(activity, "timeline", TimelineFragment.class);
        this.detailsFragment = detailsFragment;
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        FragmentManager fm = fragment.getFragmentManager();
        fm.popBackStack();
        ft.remove(detailsFragment);
        super.onTabUnselected(tab, ft);
    }
}
