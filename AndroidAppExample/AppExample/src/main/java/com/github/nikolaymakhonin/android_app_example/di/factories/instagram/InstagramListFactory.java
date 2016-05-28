package com.github.nikolaymakhonin.android_app_example.di.factories.instagram;

import android.view.ViewGroup;

import com.github.nikolaymakhonin.utils.contracts.patterns.mvp.IRecyclerViewAdapterFactory;
import com.github.nikolaymakhonin.utils.contracts.patterns.mvp.ISinglePresenter;
import com.github.nikolaymakhonin.utils.contracts.patterns.mvp.IView;
import com.github.nikolaymakhonin.utils.contracts.patterns.ITreeModified;

public class InstagramListFactory implements IRecyclerViewAdapterFactory {

    @Override
    public IView createItemView(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public ISinglePresenter createItemPresenter(IView view, int viewType) {
        return null;
    }

    @Override
    public int getItemViewType(int position, ITreeModified instagramPost) {
        return 0;
    }
}
