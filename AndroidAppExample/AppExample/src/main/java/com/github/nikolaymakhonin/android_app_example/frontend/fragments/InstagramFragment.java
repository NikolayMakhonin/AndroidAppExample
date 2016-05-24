package com.github.nikolaymakhonin.android_app_example.frontend.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.github.nikolaymakhonin.android_app_example.R;
import com.github.nikolaymakhonin.android_app_example.frontend.adapters.InstagramListAdapter;
import com.github.nikolaymakhonin.android_app_example.frontend.contracts.IFragmentWithHeader;
import com.github.nikolaymakhonin.android_app_example.frontend.contracts.IHasTitle;

public class InstagramFragment extends Fragment implements IHasTitle, IFragmentWithHeader {

    private static final int LAYOUT = R.layout.fragment_instagram;
    private static final int HEADER_COLOR = R.color.toolBarForBackground8;
    private static final int HEADER_DRAWABLE = R.drawable.navigation_header_background_8;

    private View _contentView;
    private RecyclerView _recyclerView;
    private RecyclerViewMaterialAdapter _recyclerViewMaterialAdapter;

    //region Override methods

    @Override
    public String getTitle() {
        return "Instagram";
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

    public static InstagramFragment getInstance() {
        Bundle            args     = new Bundle();
        InstagramFragment fragment = new InstagramFragment();
        fragment.setArguments(args);

        return fragment;
    }

    //endregion

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
        _recyclerView = (RecyclerView) _contentView.findViewById(R.id.recyclerView);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        _recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter recyclerViewAdapter = new InstagramListAdapter();
        _recyclerViewMaterialAdapter = new RecyclerViewMaterialAdapter(recyclerViewAdapter);
        _recyclerView.setAdapter(_recyclerViewMaterialAdapter);
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), _recyclerView, null);
    }

    //endregion
}
