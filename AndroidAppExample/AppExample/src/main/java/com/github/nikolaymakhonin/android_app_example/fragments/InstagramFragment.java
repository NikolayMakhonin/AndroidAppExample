package com.github.nikolaymakhonin.android_app_example.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.nikolaymakhonin.android_app_example.R;
import com.github.nikolaymakhonin.android_app_example.adapters.InstagramListAdapter;
import com.github.nikolaymakhonin.android_app_example.contracts.IHasTitle;

public class InstagramFragment extends Fragment implements IHasTitle {

    private static final int LAYOUT = R.layout.fragment_instagram;

    private View _contentView;
    private RecyclerView _recyclerView;

    public static InstagramFragment getInstance() {
        Bundle            args     = new Bundle();
        InstagramFragment fragment = new InstagramFragment();
        fragment.setArguments(args);

        return fragment;
    }

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
        _recyclerView.setAdapter(new InstagramListAdapter());
    }

    @Override
    public String getTitle() {
        return "Instagram";
    }
}
