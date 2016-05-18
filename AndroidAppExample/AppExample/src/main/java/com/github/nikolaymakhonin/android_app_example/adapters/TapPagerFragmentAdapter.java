package com.github.nikolaymakhonin.android_app_example.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.github.nikolaymakhonin.android_app_example.contracts.IHasTitle;
import com.github.nikolaymakhonin.android_app_example.fragments.GoogleMapFragment;

import java.util.ArrayList;
import java.util.List;

public class TapPagerFragmentAdapter extends FragmentPagerAdapter {

    private static final String LOG_TAG = "TapPagerFragmentAdapter";

    private final List<Fragment> _pages = new ArrayList<>();

    public TapPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        _pages.add(GoogleMapFragment.getInstance());
        _pages.add(GoogleMapFragment.getInstance());
        _pages.add(GoogleMapFragment.getInstance());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Fragment page = getItem(position);
        String title;
        if (page instanceof IHasTitle) {
            title = ((IHasTitle) page).getTitle();
        } else {
            Log.e(LOG_TAG, "Fragment not implemented IHasTitle: " + page);
            title = page.getClass().getSimpleName();
        }
        return title;
    }

    @Override
    public Fragment getItem(int position) {
        return _pages.get(position);
    }

    @Override
    public int getCount() {
        return _pages.size();
    }
}
