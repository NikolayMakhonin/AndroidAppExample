package com.github.nikolaymakhonin.android_app_example.ui.views.fragments;

import android.os.Bundle;
import android.support.v7.preference.XpPreferenceFragment;

import com.github.nikolaymakhonin.android_app_example.R;

import net.xpece.android.support.preference.PreferenceScreenNavigationStrategy;

public class MainPreferenceFragment extends XpPreferenceFragment {

    public static MainPreferenceFragment newInstance(String rootKey) {
        Bundle args = new Bundle();
        args.putString(MainPreferenceFragment.ARG_PREFERENCE_ROOT, rootKey);
        MainPreferenceFragment fragment = new MainPreferenceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_main);

        //region Toolbar

        getPreferenceScreen().setTitle(getActivity().getTitle());

        //endregion

        //region Switch screens

        PreferenceScreenNavigationStrategy.ReplaceFragment.onCreatePreferences(this, rootKey);

        //endregion
    }

    //region Toolbar

    @Override
    public void onStart() {
        super.onStart();

        // Change activity title to preference title. Used with ReplaceFragment strategy.
        getActivity().setTitle(getPreferenceScreen().getTitle());
    }

    //endregion
}