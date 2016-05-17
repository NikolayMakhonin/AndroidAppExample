package com.github.nikolaymakhonin.android_app_example.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.nikolaymakhonin.android_app_example.R;

public class GoogleMapFragment extends Fragment {

    private static final int LAYOUT = R.layout.fragment_google_map;

    private View _contentView;

    public static GoogleMapFragment getInstance() {
        Bundle args = new Bundle();
        GoogleMapFragment fragment = new GoogleMapFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        _contentView = inflater.inflate(LAYOUT, container, false);
        return _contentView;
    }
}
