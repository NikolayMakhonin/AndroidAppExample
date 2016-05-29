package com.github.nikolaymakhonin.android_app_example.ui.controls;

import android.support.v7.widget.RecyclerView;

import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

public class RecyclerViewMaterialAdapterExt extends RecyclerViewMaterialAdapter {

    private final RecyclerView.Adapter _adapter;

    public RecyclerViewMaterialAdapterExt(RecyclerView.Adapter adapter) {
        super(adapter);
        _adapter = adapter;
    }

    public RecyclerViewMaterialAdapterExt(RecyclerView.Adapter adapter, int placeholderSize) {
        super(adapter, placeholderSize);
        _adapter = adapter;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        _adapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        _adapter.onDetachedFromRecyclerView(recyclerView);
    }
}
