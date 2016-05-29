package com.github.nikolaymakhonin.android_app_example.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.nikolaymakhonin.android_app_example.R;
import com.github.nikolaymakhonin.android_app_example.di.components.AppComponent;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.presenters.InstagramListAdapter;
import com.github.nikolaymakhonin.android_app_example.ui.contracts.IFragmentWithHeader;
import com.github.nikolaymakhonin.android_app_example.ui.contracts.IHasTitle;
import com.github.nikolaymakhonin.android_app_example.ui.controls.RecyclerViewMaterialAdapterExt;
import com.github.nikolaymakhonin.common_di.contracts.IHasAppComponentBase;

public class InstagramFragment extends Fragment implements IHasTitle, IFragmentWithHeader {

    private static final int LAYOUT = R.layout.fragment_instagram;
    private static final int HEADER_COLOR = R.color.toolBarForBackground8;
    private static final int HEADER_DRAWABLE = R.drawable.navigation_header_background_8;

    private View                        _contentView;
    private RecyclerView                _recyclerView;
    private RecyclerViewMaterialAdapterExt _recyclerViewMaterialAdapter;
    private InstagramListAdapter        _instagramListAdapter;

    private AppComponent _appComponent;

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
        _appComponent = ((IHasAppComponentBase<AppComponent>)getContext().getApplicationContext()).getAppComponent();

        _contentView = inflater.inflate(LAYOUT, container, false);
        initControls();

        return _contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        _instagramListAdapter.loadByGeo(22.277872, 114.1762067, 1000);
    }

    private void initControls() {
        _recyclerView = (RecyclerView) _contentView.findViewById(R.id.recyclerView);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        _recyclerView.setHasFixedSize(true);
        _instagramListAdapter = _appComponent.getInstagramComponent().createInstagramListAdapter();
        _recyclerViewMaterialAdapter = new RecyclerViewMaterialAdapterExt(_instagramListAdapter);
        _recyclerView.setAdapter(_recyclerViewMaterialAdapter);
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), _recyclerView, null);
    }

    //endregion
}
