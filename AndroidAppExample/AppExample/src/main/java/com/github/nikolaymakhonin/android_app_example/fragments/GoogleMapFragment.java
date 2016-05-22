package com.github.nikolaymakhonin.android_app_example.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.nikolaymakhonin.android_app_example.R;
import com.github.nikolaymakhonin.android_app_example.contracts.IFragmentWithHeader;
import com.github.nikolaymakhonin.android_app_example.contracts.IHasTitle;

public class GoogleMapFragment extends Fragment implements IHasTitle, IFragmentWithHeader {

    private static final int LAYOUT = R.layout.fragment_google_map;
    private static final int HEADER_COLOR = R.color.toolBar;
    private static final int HEADER_DRAWABLE = R.drawable.navigation_header_background_1;

    //region Override methods

    @Override
    public String getTitle() {
        return "Google Map";
    }

    @Override
    public int getHeaderColorResId() {
        return HEADER_COLOR;
    }

    @Override
    public int getHeaderDrawableResId() {
        return HEADER_DRAWABLE;
    }

    //endregion

    //region Create Instance

    public static GoogleMapFragment getInstance() {
        Bundle            args     = new Bundle();
        GoogleMapFragment fragment = new GoogleMapFragment();
        fragment.setArguments(args);

        return fragment;
    }

    //endregion

    private View                 _contentView;
    private ObservableScrollView _scrollView;

    //region Init Controls

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        _contentView = inflater.inflate(LAYOUT, container, false);
        initControls();

        return _contentView;
    }

    private void initControls() {
        _scrollView = (ObservableScrollView) _contentView.findViewById(R.id.scrollView);
        MaterialViewPagerHelper.registerScrollView(getActivity(), _scrollView, null);
    }

    //endregion
}
